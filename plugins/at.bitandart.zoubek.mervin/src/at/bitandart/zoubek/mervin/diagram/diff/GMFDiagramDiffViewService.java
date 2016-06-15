/*******************************************************************************
 * Copyright (c) 2015, 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.diagram.diff;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.commands.CreateDiagramCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

import com.google.common.collect.HashBiMap;

import at.bitandart.zoubek.mervin.diagram.diff.gmf.ModelReviewElementTypes;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

// TODO remove Createable annotation and move creation in addon
/**
 * 
 * @author Florian Zoubek
 *
 */
@Creatable
public class GMFDiagramDiffViewService {

	@Inject
	private ModelReviewFactory reviewFactory;

	/**
	 * creates a view model for the given {@link ModelReview} instance and adds
	 * adapters to update the view model based on changes to the
	 * {@link ModelReview} instance.
	 * 
	 * @param modelReview
	 *            the {@link ModelReview} instance which is used to create the
	 *            view model and gets the adapters attached
	 * @param editDomain
	 *            the GEF {@link EditDomain} used to execute commands which
	 *            affect the view model
	 * @param transactionalEditingDomain
	 *            the {@link TransactionalEditingDomain} used by commands to
	 *            update the view model.
	 * @param preferencesHint
	 * @return the view model diagram instance or null if the creation of the
	 *         view model failed
	 */
	public Diagram createAndConnectViewModel(final ModelReview modelReview, final EditDomain editDomain,
			final TransactionalEditingDomain transactionalEditingDomain, final PreferencesHint preferencesHint) {

		final Diagram diagram = createDiagram(modelReview, editDomain, transactionalEditingDomain, preferencesHint);

		if (diagram != null) {
			updateDiagramNodes(diagram, modelReview, editDomain, transactionalEditingDomain, preferencesHint);
		}

		modelReview.eAdapters().add(new ModelReviewChangeAdapter(editDomain, preferencesHint, diagram, modelReview,
				transactionalEditingDomain));

		return diagram;
	}

	/**
	 * removes all attached adapters and listeners from the model review model
	 * that belong to the given diagram diff view model.
	 * 
	 * @param diagram
	 *            the diagram diff view model
	 */
	public void disconnectViewModel(Diagram diagram) {
		EObject object = diagram.getElement();
		if (object instanceof ModelReview) {
			EList<Adapter> eAdapters = object.eAdapters();
			Adapter adapterToRemove = null;
			for (Adapter adapter : eAdapters) {
				if (adapter instanceof ModelReviewChangeAdapter
						&& ((ModelReviewChangeAdapter) adapter).getDiagram() == diagram) {
					adapterToRemove = adapter;
					break;
				}
			}
			eAdapters.remove(adapterToRemove);
		}
	}

	/**
	 * creates a diagram view for the given {@link ModelReview} instance.
	 * 
	 * @param modelReview
	 *            the {@link ModelReview} instance to create the diagram for
	 * @param editDomain
	 *            the {@link EditDomain} used to execute the creation command
	 * @param transactionalEditingDomain
	 *            the {@link TransactionalEditingDomain} used to create the
	 *            {@link Diagram} instance
	 * @param preferencesHint
	 * @return the diagram or null if no diagram could not be created
	 */
	private Diagram createDiagram(ModelReview modelReview, EditDomain editDomain,
			TransactionalEditingDomain transactionalEditingDomain, PreferencesHint preferencesHint) {
		CreateDiagramCommand createDiagramCommand = new CreateDiagramCommand(transactionalEditingDomain,
				"ModelReviewDiff", modelReview, DiagramDiffView.PART_DESCRIPTOR_ID, preferencesHint);
		CommandResult result = executeCommand(createDiagramCommand, editDomain);
		if (result.getStatus().isOK() && result.getReturnValue() instanceof Diagram) {
			Diagram diagram = (Diagram) result.getReturnValue();
			return diagram;
		}
		return null;
	}

	/**
	 * deletes all children of the given workspace diagram instance and creates
	 * new child diagram views with their content.
	 * 
	 * @param workspaceDiagram
	 *            the workspace diagram instance
	 * @param modelReview
	 *            the {@link ModelReview} instance used to obtain the original
	 *            diagram instances
	 * @param editDomain
	 *            the GEF {@link EditDomain} used to execute the commands
	 * @param transactionalEditingDomain
	 *            the {@link TransactionalEditingDomain} used by commands to
	 *            update the model
	 * @param preferencesHint
	 */
	private void updateDiagramNodes(Diagram workspaceDiagram, ModelReview modelReview, EditDomain editDomain,
			TransactionalEditingDomain transactionalEditingDomain, PreferencesHint preferencesHint) {

		// create/clear old unified model map
		HashBiMap<EObject, EObject> unifiedModelMap = modelReview.getUnifiedModelMap();
		if (unifiedModelMap == null) {

			unifiedModelMap = HashBiMap.create();
			modelReview.setUnifiedModelMap(unifiedModelMap);

		} else {
			unifiedModelMap.clear();
		}

		// remove old diagram nodes
		EList<?> children = workspaceDiagram.getChildren();
		if (!children.isEmpty()) {
			CompositeCommand compositeCommand = new CompositeCommand("");
			for (Object child : children) {
				if (child instanceof View) {
					compositeCommand.add(new DeleteCommand(transactionalEditingDomain, (View) child));
				}
			}
			executeCommand(compositeCommand, editDomain);
		}

		// add new diagram nodes
		PatchSet rightPatchSet = modelReview.getRightPatchSet();
		if (rightPatchSet != null) {
			EList<Diagram> allNewInvolvedDiagrams = rightPatchSet.getAllNewInvolvedDiagrams();
			CompositeCommand compositeCommand = new CompositeCommand("");
			for (Diagram diagram : allNewInvolvedDiagrams) {
				compositeCommand
						.add(new CreateCommand(transactionalEditingDomain,
								new ViewDescriptor(new EObjectAdapter(diagram), Node.class,
										ModelReviewElementTypes.DIAGRAM_SEMANTIC_HINT, preferencesHint),
								workspaceDiagram));
			}
			if (!compositeCommand.isEmpty()) {
				executeCommand(compositeCommand.reduce(), editDomain);
			}
		}

		// add children for each diagram
		Comparison diagramComparison = modelReview.getSelectedDiagramComparison();

		children = workspaceDiagram.getChildren();
		for (Object workspaceChild : children) {

			if (workspaceChild instanceof View && ((View) workspaceChild).getElement() instanceof Diagram) {
				final View childView = (View) workspaceChild;
				final Diagram diagram = (Diagram) childView.getElement();

				CompositeCommand compositeCommand = new CompositeCommand(
						"Create workspace diagram for " + diagram.toString());
				List<Object> childrenAndEdges = new LinkedList<Object>();
				childrenAndEdges.addAll((List<?>) diagram.getEdges());
				childrenAndEdges.addAll((List<?>) diagram.getVisibleChildren());

				/*
				 * Copy the diagram contents and keep the mapping information to
				 * associate the copies to the differences later
				 */
				Copier copier = new Copier();
				Collection<Object> childrenAndEdgesCopy = copier.copyAll(childrenAndEdges);
				copier.copyReferences();

				HashBiMap<EObject, EObject> copyMap = HashBiMap.create(copier);

				/*
				 * create copies of the deleted elements and merge them into one
				 * set of views to add to the unified diagram
				 */
				Match diagramMatch = diagramComparison.getMatch(diagram);
				if (diagramMatch != null) {
					Diagram matchedDiagram = safeCast(getOldValue(diagramMatch), Diagram.class);
					if (matchedDiagram != null) {
						Copier viewCopier = new Copier();
						Iterable<Diff> allDifferences = diagramMatch.getAllDifferences();
						for (Diff diff : allDifferences) {
							if (needsCopy(diff, copyMap)) {
								copyDiff(diagramComparison, childrenAndEdgesCopy, copyMap, viewCopier, diff);
							}
						}
						viewCopier.copyReferences();
					}
				}

				List<Object> childrenCopy = new LinkedList<Object>(childrenAndEdgesCopy);
				compositeCommand.add(new AddDiagramEdgesAndNodesCommand(transactionalEditingDomain, workspaceDiagram,
						(View) childView, filterEdges(childrenCopy), filterNonEdges(childrenCopy), diagram.getType()));

				compositeCommand.add(new AddOverlayNodesCommand(transactionalEditingDomain,
						modelReview.getSelectedDiagramComparison(), copyMap, childView, childrenCopy, preferencesHint,
						reviewFactory, modelReview));

				executeCommand(compositeCommand.reduce(), editDomain);

				modelReview.getUnifiedModelMap().putAll(copyMap);
			}
		}

		CompositeCommand compositeCommand = new CompositeCommand("Restore overlay type visibility");
		compositeCommand.add(new ApplyOverlayVisibilityStateCommand(transactionalEditingDomain, workspaceDiagram,
				new ModelReviewVisibilityState(modelReview, true)));

		executeCommand(compositeCommand, editDomain);
	}

	/**
	 * determines if the value of the given {@link Diff} must be copied into the
	 * given copy map. A value must be copied if the {@link Diff} represents a
	 * deletion and the value has not been copied yet.
	 * 
	 * @param diff
	 *            the diff to check.
	 * @param copyMap
	 *            the copy map that may contain the value of the diff.
	 * @return true if the value of the given diff must be copied into the given
	 *         copy map, false otherwise.
	 */
	private boolean needsCopy(Diff diff, HashBiMap<EObject, EObject> copyMap) {
		Object value = MatchUtil.getValue(diff);
		return diff.getKind() == DifferenceKind.DELETE && !copyMap.containsKey(value);
	}

	/**
	 * copies the value of the given diff into the given copy map and view
	 * collection if it is a view. Any Views in {@link Diff}s that require the
	 * given {@link Diff} will also be copied if necessary. The copied view will
	 * also placed into the corresponding copy of the matched new container at
	 * the nearest insertion point. The new container of the view must be a
	 * diagram or part of the copied objects, otherwise the value will not be
	 * copied.
	 * 
	 * @param diagramComparison
	 *            the comparison containing the {@link Diff}
	 * @param childrenAndEdgesCopy
	 *            the collection of copied views.
	 * @param copyMap
	 *            the bidirectional map that links the original model elements
	 *            to the copied elements.
	 * @param viewCopier
	 *            the copier used to copy the object,
	 *            {@link Copier#copyReferences()} must be invoked by the caller
	 *            if it is desired.
	 * @param diff
	 *            the {@link Diff} whose value should be copied.
	 */
	private void copyDiff(Comparison diagramComparison, Collection<Object> childrenAndEdgesCopy,
			HashBiMap<EObject, EObject> copyMap, Copier viewCopier, Diff diff) {

		/* copy diffs that require the given diff */
		EList<Diff> requiredByDiffs = diff.getRequiredBy();
		for (Diff requiredDiff : requiredByDiffs) {
			if (needsCopy(requiredDiff, copyMap)) {
				copyDiff(diagramComparison, childrenAndEdgesCopy, copyMap, viewCopier, requiredDiff);
			}
		}

		/* Copy the view, if possible */
		Object value = MatchUtil.getValue(diff);
		if (value instanceof View) {

			View node = (View) value;
			EObject oldContainer = node.eContainer();
			Match containerMatch = diagramComparison.getMatch(oldContainer);
			EReference containmentFeature = node.eContainmentFeature();

			/*
			 * the copy must be contained in a known (and copied) container, if
			 * the container is unknown, ignore it.
			 */
			if (containerMatch != null) {

				EObject newContainer = getNewValue(containerMatch);

				/* the diagram is not part of the copy map */
				if (newContainer instanceof Diagram || copyMap.containsKey(newContainer)) {

					EObject containerCopy = copyMap.get(newContainer);
					View copiedView = (View) viewCopier.copy(node);

					if (containerCopy != null && containerCopy.eClass().getEReferences().contains(containmentFeature)) {

						/*
						 * Insert the copied element into the copy of the
						 * matched container
						 */

						if (containmentFeature.isMany()) {

							/*
							 * Multi-valued feature, insert at the closest
							 * original index
							 */

							@SuppressWarnings("unchecked")
							EList<EObject> oldContainerContent = (EList<EObject>) oldContainer.eGet(containmentFeature);
							int oldIndex = oldContainerContent.indexOf(node);
							int newIndex = 0;
							@SuppressWarnings("unchecked")
							EList<EObject> newContainerContent = (EList<EObject>) newContainer.eGet(containmentFeature);

							/*
							 * determine the new index by finding the index of
							 * the first previous unchanged element.
							 */
							ListIterator<EObject> listIterator = oldContainerContent.listIterator(oldIndex);
							while (listIterator.hasPrevious()) {
								EObject previous = listIterator.previous();
								Match match = diagramComparison.getMatch(previous);
								EObject newValue = getNewValue(match);
								if (newValue != null) {
									newIndex = newContainerContent.indexOf(newValue) + 1;
									break;
								}
							}

							@SuppressWarnings("unchecked")
							EList<EObject> copiedContainerContent = (EList<EObject>) containerCopy
									.eGet(containmentFeature);

							copiedContainerContent.add(newIndex, copiedView);

						} else {

							/*
							 * single-valued feature, simply set it
							 */
							containerCopy.eSet(containmentFeature, copiedView);
						}
					}
					if (newContainer instanceof Diagram) {
						/*
						 * if the container is a diagram, add it to the
						 * collection instead of the container
						 */
						childrenAndEdgesCopy.add(copiedView);
					}
					copyMap.putAll(viewCopier);
				}

			}
		}
	}

	/**
	 * convenience method to get the "old" value of a match.
	 * 
	 * @param match
	 *            the match containing the value.
	 * @return the old value of the given match.
	 */
	private EObject getOldValue(Match match) {
		return match.getRight();
	}

	/**
	 * 
	 * convenience method to get the "new" value of a match.
	 * 
	 * @param match
	 *            the match containing the value.
	 * @return the new value of the given match.
	 */
	private EObject getNewValue(Match match) {
		return match.getLeft();
	}

	/**
	 * convenience method that casts the given object to the given type if
	 * possible.
	 * 
	 * @param obj
	 *            the object to cast.
	 * @param type
	 *            the type to cast to.
	 * @return the casted object or null if the object could not be cast to that
	 *         object or the object was null.
	 */
	private <T> T safeCast(Object obj, Class<T> type) {
		if (type.isInstance(obj)) {
			return type.cast(obj);
		}
		return null;
	}

	private List<Edge> filterEdges(List<Object> elements) {
		List<Edge> edges = new LinkedList<Edge>();
		for (Object child : elements) {
			if (child instanceof Edge) {
				edges.add((Edge) child);
			}
		}
		return edges;
	}

	private List<View> filterNonEdges(List<Object> elements) {

		List<View> nodes = new LinkedList<View>();
		for (Object child : elements) {
			if (child instanceof View && !(child instanceof Edge)) {
				nodes.add((View) child);
			}
		}
		return nodes;
	}

	private class AddDiagramEdgesAndNodesCommand extends AbstractTransactionalCommand {

		private View parentView;
		private Diagram diagram;
		private String originalDiagramType;
		private List<Edge> edges;
		private List<View> nodes;

		public AddDiagramEdgesAndNodesCommand(TransactionalEditingDomain domain, Diagram diagram, View parentView,
				List<Edge> edges, List<View> nodes, String originalDiagramType) {
			super(domain, "", null);
			this.parentView = parentView;
			this.diagram = diagram;
			this.edges = edges;
			this.nodes = nodes;
			this.originalDiagramType = originalDiagramType;
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {

			for (Edge edge : edges) {
				diagram.insertEdge(edge);
				addMissingModelHint(edge);
			}

			for (View node : nodes) {
				parentView.insertChild(node);
				addMissingModelHint(node);
			}

			return CommandResult.newOKCommandResult();
		}

		/**
		 * sets a model hint for generated GMF editpart provider if it does not
		 * exist.
		 * 
		 * @param view
		 *            the view to add the model hint
		 */
		private void addMissingModelHint(View view) {
			if (view.getEAnnotation("Shortcut") == null) {
				EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
				eAnnotation.setSource("Shortcut");
				eAnnotation.getDetails().put("modelID", originalDiagramType);
				view.getEAnnotations().add(eAnnotation);
			}
		}
	}

	/**
	 * executes the given {@link ICommand} with the given {@link EditDomain} and
	 * returns the result of the command.
	 * 
	 * @param command
	 *            the command to execute
	 * @param editDomain
	 *            the GEF {@link EditDomain} used to execute the command
	 * @return the result of the command
	 */
	private CommandResult executeCommand(ICommand command, EditDomain editDomain) {
		editDomain.getCommandStack().execute(new ICommandProxy(command));
		return command.getCommandResult();
	}

	/**
	 * An {@link EContentAdapter} used to listen to changes in a
	 * {@link ModelReview} instance that trigger changes in the view model.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private final class ModelReviewChangeAdapter extends EContentAdapter {
		private final EditDomain editDomain;
		private final PreferencesHint preferencesHint;
		private final Diagram diagram;
		private final ModelReview modelReview;
		private final TransactionalEditingDomain transactionalEditingDomain;

		private ModelReviewChangeAdapter(EditDomain editDomain, PreferencesHint preferencesHint, Diagram diagram,
				ModelReview modelReview, TransactionalEditingDomain transactionalEditingDomain) {
			this.editDomain = editDomain;
			this.preferencesHint = preferencesHint;
			this.diagram = diagram;
			this.modelReview = modelReview;
			this.transactionalEditingDomain = transactionalEditingDomain;
		}

		@Override
		public void notifyChanged(Notification notification) {

			// needed to adapt also containment references
			super.notifyChanged(notification);

			int featureID = notification.getFeatureID(PatchSet.class);
			if (featureID == ModelReviewPackage.MODEL_REVIEW__SELECTED_DIAGRAM_COMPARISON
					|| featureID == ModelReviewPackage.MODEL_REVIEW__COMMENTS) {

				updateDiagramNodes(diagram, modelReview, editDomain, transactionalEditingDomain, preferencesHint);

			}

		}

		public Diagram getDiagram() {
			return diagram;
		}
	}

	/**
	 * applies the given visibility state and updates the visibility of the
	 * overlays accordingly.
	 * 
	 * @param editDomain
	 *            the GEF {@link EditDomain} used to execute commands which
	 *            affect the view model
	 * @param transactionalEditingDomain
	 *            the {@link TransactionalEditingDomain} used by commands to
	 *            update the view model.
	 * @param diagram
	 *            the diagram to operate upon.
	 * @param visibilityState
	 *            the visibility state to apply.
	 */
	public void applyOverlayVisibilityState(EditDomain editDomain,
			TransactionalEditingDomain transactionalEditingDomain, Diagram diagram,
			IOverlayVisibilityState visibilityState) {

		CompositeCommand compositeCommand = new CompositeCommand("");
		compositeCommand
				.add(new ApplyOverlayVisibilityStateCommand(transactionalEditingDomain, diagram, visibilityState));
		executeCommand(compositeCommand, editDomain);

	}
}

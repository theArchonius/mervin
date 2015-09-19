/*******************************************************************************
 * Copyright (c) 2015 Florian Zoubek.
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

import javax.inject.Inject;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
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
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import at.bitandart.zoubek.mervin.diagram.diff.gmf.ModelReviewElementTypes;
import at.bitandart.zoubek.mervin.model.modelreview.ChangeOverlay;
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
	 *            the {@link TransactionalEditingDomain} used tby commands to
	 *            update the model
	 * @param preferencesHint
	 */
	private void updateDiagramNodes(Diagram workspaceDiagram, ModelReview modelReview, EditDomain editDomain,
			TransactionalEditingDomain transactionalEditingDomain, PreferencesHint preferencesHint) {

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

		children = workspaceDiagram.getChildren();
		for (Object workspaceChild : children) {

			if (workspaceChild instanceof View && ((View) workspaceChild).getElement() instanceof Diagram) {
				final View childView = (View) workspaceChild;
				final Diagram diagram = (Diagram) childView.getElement();

				CompositeCommand compositeCommand = new CompositeCommand("");
				/*
				 * TODO return only context children. For now, return all
				 * visible child views
				 */
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

				List<Object> childrenCopy = new LinkedList<Object>(childrenAndEdgesCopy);
				compositeCommand.add(new AddDiagramEdgesAndNodesCommand(transactionalEditingDomain, workspaceDiagram,
						(View) childView, filterEdges(childrenCopy), filterNonEdges(childrenCopy), diagram.getType()));

				compositeCommand.add(new AddOverlayNodesCommand(transactionalEditingDomain,
						modelReview.getSelectedDiagramComparison(), copyMap, childView, preferencesHint,
						reviewFactory));

				executeCommand(compositeCommand.reduce(), editDomain);
			}
		}
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

	private class AddOverlayNodesCommand extends AbstractTransactionalCommand {

		private Comparison diagramComparison;
		private View container;
		private BiMap<EObject, EObject> inverseCopyMap;
		private PreferencesHint preferencesHint;
		private ModelReviewFactory reviewFactory;

		public AddOverlayNodesCommand(TransactionalEditingDomain domain, Comparison diagramComparison,
				BiMap<EObject, EObject> copyMap, View container, PreferencesHint preferencesHint,
				ModelReviewFactory reviewFactory) {
			super(domain, "", null);
			this.diagramComparison = diagramComparison;
			this.container = container;
			this.inverseCopyMap = copyMap.inverse();
			this.preferencesHint = preferencesHint;
			this.reviewFactory = reviewFactory;
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {

			for (Object child : container.getChildren()) {

				if (child instanceof View) {

					View childView = (View) child;
					EObject element = childView.getElement();
					EObject originalElement = inverseCopyMap.get(childView);
					Match match = diagramComparison.getMatch(originalElement);

					for (Diff difference : match.getAllDifferences()) {

						if (difference instanceof ReferenceChange || difference instanceof AttributeChange) {

							ChangeOverlay changeOverlay = reviewFactory.createChangeOverlay();
							changeOverlay.setDiff(difference);

							String type = "";

							switch (difference.getKind()) {
							case ADD:
								type = ModelReviewElementTypes.OVERLAY_ADDITION_SEMANTIC_HINT;
								break;
							case DELETE:
								type = ModelReviewElementTypes.OVERLAY_DELETION_SEMANTIC_HINT;
								break;
							case CHANGE:
							case MOVE:
								type = ModelReviewElementTypes.OVERLAY_MODIFICATION_SEMANTIC_HINT;
								break;
							}

							ViewService.createNode(container, changeOverlay, type, preferencesHint);
						}
					}
				}
			}

			return CommandResult.newOKCommandResult();
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
			if (featureID == ModelReviewPackage.MODEL_REVIEW__LEFT_PATCH_SET
					|| featureID == ModelReviewPackage.MODEL_REVIEW__RIGHT_PATCH_SET) {

				updateDiagramNodes(diagram, modelReview, editDomain, transactionalEditingDomain, preferencesHint);

			}
		}

		public Diagram getDiagram() {
			return diagram;
		}
	}
}

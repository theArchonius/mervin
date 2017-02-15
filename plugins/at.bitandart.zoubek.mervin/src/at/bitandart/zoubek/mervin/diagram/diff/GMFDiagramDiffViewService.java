/*******************************************************************************
 * Copyright (c) 2015, 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.diagram.diff;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Set;

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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
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
import org.eclipse.gmf.runtime.emf.type.core.commands.SetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

import at.bitandart.zoubek.mervin.diagram.diff.gmf.ModelReviewElementTypes;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.util.UnifiedModelMap;

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
	 * the currently registered {@link DiagramDiffServiceListener}s for this
	 * service.
	 */
	private List<DiagramDiffServiceListener> serviceListeners = new ArrayList<>();

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
	 * adds the given {@link DiagramDiffServiceListener} to the list of
	 * registered service listeners of this service. Does nothing if the
	 * listener has been registered before.
	 * 
	 * @param listener
	 *            the {@link DiagramDiffServiceListener} to add to this service.
	 */
	public void addListener(DiagramDiffServiceListener listener) {
		if (!serviceListeners.contains(listener)) {
			serviceListeners.add(listener);
		}
	}

	/**
	 * removes the given {@link DiagramDiffServiceListener} from the list of
	 * registered service listeners of this service. Does nothing if the
	 * listener has not been registered before.
	 * 
	 * @param listener
	 *            the {@link DiagramDiffServiceListener} to remove from this
	 *            service.
	 */
	public void removeListener(DiagramDiffServiceListener listener) {
		serviceListeners.remove(listener);
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
		UnifiedModelMap unifiedModelMap = modelReview.getUnifiedModelMap();
		if (unifiedModelMap == null) {

			unifiedModelMap = new UnifiedModelMap();
			SetValueCommand setUnifiedMapCommand = new SetValueCommand(new SetRequest(transactionalEditingDomain,
					modelReview, ModelReviewPackage.Literals.MODEL_REVIEW__UNIFIED_MODEL_MAP, unifiedModelMap));
			executeCommand(setUnifiedMapCommand, editDomain);

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
		PatchSet oldPatchSet = modelReview.getLeftPatchSet();
		PatchSet newPatchSet = modelReview.getRightPatchSet();
		EList<PatchSet> patchSets = modelReview.getPatchSets();
		EList<Diagram> allNewInvolvedDiagrams = null;

		if (newPatchSet != null) {
			allNewInvolvedDiagrams = newPatchSet.getAllNewInvolvedDiagrams();
		} else if (oldPatchSet != null) {
			/* fall back to the original version of the old patch set */
			allNewInvolvedDiagrams = oldPatchSet.getAllOldInvolvedDiagrams();
		} else if (!patchSets.isEmpty()) {
			/*
			 * fall back to the default original version - the old diagrams of
			 * the first patch set
			 */
			allNewInvolvedDiagrams = patchSets.get(0).getAllOldInvolvedDiagrams();
		}

		if (allNewInvolvedDiagrams != null) {
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

				unifiedModelMap.putAll(copier);

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
							if (isDeletionCopyNecessary(diff, unifiedModelMap)) {
								copyDeletedViewDiff(diagramComparison, childrenAndEdgesCopy, unifiedModelMap,
										viewCopier, diff);
							}
						}
						viewCopier.copyReferences();
						updateDeletionReferences(viewCopier, unifiedModelMap, diagramComparison);
					}
				}

				List<Object> childrenCopy = new LinkedList<Object>(childrenAndEdgesCopy);
				compositeCommand.add(new AddDiagramEdgesAndNodesCommand(transactionalEditingDomain, workspaceDiagram,
						(View) childView, filterEdges(childrenCopy), filterNonEdges(childrenCopy), diagram.getType()));

				compositeCommand.add(new AddOverlayNodesCommand(transactionalEditingDomain,
						modelReview.getSelectedDiagramComparison(), unifiedModelMap, childView, childrenCopy,
						preferencesHint, reviewFactory, modelReview));

				executeCommand(compositeCommand.reduce(), editDomain);

			}
		}

		CompositeCommand compositeCommand = new CompositeCommand("Restore overlay type visibility");
		compositeCommand.add(new ApplyOverlayVisibilityStateCommand(transactionalEditingDomain, workspaceDiagram,
				new ModelReviewVisibilityState(modelReview, true)));

		executeCommand(compositeCommand, editDomain);

		notifyNodesUpdated(workspaceDiagram, modelReview);
	}

	/**
	 * notifies all registered {@link DiagramDiffServiceListener}s that the
	 * diagram nodes of the given workspace diagram for the given model review
	 * have been updated.
	 * 
	 * @param workspaceDiagram
	 *            the workspace diagram (the root diagram for a model review)
	 *            instance.
	 * @param modelReview
	 *            the model review associated with the given workspace diagram.
	 */
	private void notifyNodesUpdated(Diagram workspaceDiagram, ModelReview modelReview) {

		for (DiagramDiffServiceListener listener : serviceListeners) {
			listener.diagramNodesUpdated(workspaceDiagram, modelReview);
		}
	}

	/**
	 * determines if the value of the given {@link Diff} must be copied into the
	 * given copy map. A value must be copied if the {@link Diff} represents a
	 * deletion and the value has not been copied yet.
	 * 
	 * @param diff
	 *            the diff to check.
	 * @param unifiedModelMap
	 *            the unified model map that may contain the value of the diff.
	 * @return true if the value of the given diff must be copied into the given
	 *         copy map, false otherwise.
	 */
	private boolean isDeletionCopyNecessary(Diff diff, UnifiedModelMap unifiedModelMap) {

		Object value = MatchUtil.getValue(diff);
		return diff.getKind() == DifferenceKind.DELETE && value instanceof EObject
				&& !unifiedModelMap.containsOriginal((EObject) value);
	}

	/**
	 * copies the value of the given deletion diff into the given copy map and
	 * view collection if it is a view. Any Views in {@link Diff}s that require
	 * the given {@link Diff} will also be copied if necessary. The copied view
	 * will also placed into the corresponding copy of the matched new container
	 * at the nearest insertion point. The new container of the view must be a
	 * diagram or part of the copied objects, otherwise the value will not be
	 * copied.
	 * 
	 * @param diagramComparison
	 *            the comparison containing the {@link Diff}
	 * @param topLevelViewCopies
	 *            the collection of copied top-level views.
	 * @param unifiedModelMap
	 *            the map that links the original model elements to the copied
	 *            elements.
	 * @param viewCopier
	 *            the copier used to copy the object,
	 *            {@link Copier#copyReferences()} must be invoked by the caller
	 *            if it is desired.
	 * @param diff
	 *            the {@link Diff} whose value should be copied.
	 */
	private void copyDeletedViewDiff(Comparison diagramComparison, Collection<Object> topLevelViewCopies,
			UnifiedModelMap unifiedModelMap, Copier viewCopier, Diff diff) {

		/* copy diffs that require the given diff */
		EList<Diff> requiredByDiffs = diff.getRequiredBy();
		for (Diff requiredDiff : requiredByDiffs) {
			if (isDeletionCopyNecessary(requiredDiff, unifiedModelMap)) {
				copyDeletedViewDiff(diagramComparison, topLevelViewCopies, unifiedModelMap, viewCopier, requiredDiff);
			}
		}

		/* Copy the view, if possible */
		Object value = MatchUtil.getValue(diff);
		if (value instanceof View) {

			View node = (View) value;
			EObject oldContainer = node.eContainer();
			Match containerMatch = diagramComparison.getMatch(oldContainer);

			/*
			 * the copy must be contained in a known (and copied) container, if
			 * the container is unknown, ignore it.
			 */
			if (containerMatch != null) {

				EObject newContainer = getNewValue(containerMatch);

				/* the diagram is not part of the copy map */
				if (newContainer instanceof Diagram || unifiedModelMap.containsOriginal(newContainer)) {

					EObject copy = viewCopier.copy(node);

					copyNonUnifieableDeletionReferences(node, diff.getMatch().getComparison(), viewCopier,
							unifiedModelMap, topLevelViewCopies);

					if (newContainer instanceof Diagram) {
						/*
						 * if the container is a diagram, add it to the
						 * collection instead of the container
						 */
						topLevelViewCopies.add(copy);
					}
				}
				unifiedModelMap.putAll(viewCopier);

			}
		}
	}

	/**
	 * copies referenced {@link EObject}s of the given deleted {@link EObject}
	 * if they cannot be unified without loosing information for the unified
	 * model.
	 * 
	 * @param eObject
	 *            the {@link EObject} whose reference should be copied.
	 * @param comparison
	 *            the comparison used to determine the {@link Diff}s and
	 *            {@link Match}es used for the decision.
	 * @param copier
	 *            the copier used to copy the actual value of a reference.
	 * @param unifiedModelMap
	 *            the map that links the original model elements to the copied
	 *            elements.
	 * @param topLevelViewCopies
	 *            the collection of copied top-level views.
	 */
	@SuppressWarnings("unchecked")
	private void copyNonUnifieableDeletionReferences(EObject eObject, Comparison comparison, Copier copier,
			UnifiedModelMap unifiedModelMap, Collection<Object> topLevelViewCopies) {

		for (EReference reference : eObject.eClass().getEAllReferences()) {
			if (reference.getEOpposite() != null && !reference.getEOpposite().isContainer()
					&& !reference.getEOpposite().isMany() && !reference.isContainer() && !reference.isDerived()
					&& reference.isChangeable()) {
				/*
				 * references with an mono valued opposite contain values that
				 * cannot be unified without loosing one reference - so check if
				 * we have to copy them
				 */

				List<EObject> objList = null;

				if (reference.isMany()) {

					objList = (List<EObject>) eObject.eGet(reference);
				} else {

					objList = new ArrayList<EObject>(1);
					Object value = eObject.eGet(reference);

					if (value instanceof EObject) {
						objList.add((EObject) value);
					}
				}

				for (EObject refValue : objList) {

					boolean copyNecessary = true;

					EList<Diff> valueDiffs = comparison.getDifferences(refValue);
					for (Diff valueDiff : valueDiffs) {
						if (valueDiff.getKind() == DifferenceKind.DELETE) {
							/*
							 * It is not necessary to copy deleted objects, they
							 * will be copied anyway
							 */
							copyNecessary = false;
						}
					}

					EObject oldContainer = refValue.eContainer();
					Match containerMatch = comparison.getMatch(oldContainer);
					/*
					 * the copy must be contained in a known (and copied)
					 * container, if the container is unknown, ignore it.
					 */
					if (containerMatch != null && copyNecessary) {

						EObject newContainer = getNewValue(containerMatch);

						if (!copier.containsKey(refValue) && (newContainer instanceof Diagram
								|| unifiedModelMap.containsOriginal(newContainer))) {

							EObject valueCopy = copier.copy(refValue);

							if (newContainer instanceof Diagram) {
								/*
								 * if the container is a diagram, add it to the
								 * collection instead of the container
								 */
								topLevelViewCopies.add(valueCopy);
							}
						}
					}

				}
			}
		}
	}

	/**
	 * updates the references of the copied deleted elements with their value
	 * copies if possible.
	 * 
	 * @param copier
	 *            the {@link Copier} that contains the copied deleted elements
	 *            to update.
	 * @param unifiedModelMap
	 *            the map that links the original model elements to the copied
	 *            elements.
	 * @param comparison
	 *            the comparison used to determine the match for a copied
	 *            element.
	 */
	private void updateDeletionReferences(Copier copier, UnifiedModelMap unifiedModelMap, Comparison comparison) {
		Set<Entry<EObject, EObject>> entries = copier.entrySet();
		for (Entry<EObject, EObject> entry : entries) {

			EObject original = entry.getKey();
			EObject copy = entry.getValue();
			EClass eClass = original.eClass();

			EList<EStructuralFeature> allStructuralFeatures = eClass.getEAllStructuralFeatures();

			for (EStructuralFeature feature : allStructuralFeatures) {
				if (feature.isChangeable() && !feature.isDerived()) {
					if (feature instanceof EReference) {
						EReference eReference = (EReference) feature;
						if (!eReference.isContainment() && !eReference.isContainer()) {
							updateDeletionReference(eReference, original, copy, unifiedModelMap, comparison);
						}
					}
				}
			}
			updateDeletionContainment(original, copy, unifiedModelMap, comparison);
		}
	}

	/**
	 * @param oldObject
	 *            the old object to find the matching copy for.
	 * @param unifiedModelMap
	 *            the map that links the original model elements to the copied
	 *            elements.
	 * @param comparison
	 *            the comparison used to determine the match for a copied
	 *            element.
	 * @return the copies of the matched new object for the given old object.
	 */
	private Collection<EObject> getNewValueCopies(EObject oldObject, UnifiedModelMap unifiedModelMap,
			Comparison comparison) {
		Match match = comparison.getMatch(oldObject);
		EObject newValue = getNewValue(match);
		return unifiedModelMap.getCopies(newValue);
	}

	/**
	 * updates the given reference of the copy the represents a deleted EObject
	 * with their value copy, if possible.
	 * 
	 * @param eReference
	 *            the {@link EReference} of the given deleted EObject to update.
	 * @param original
	 *            the original "deleted" EObject.
	 * @param copy
	 *            the copy of the deleted EObject to update.
	 * @param unifiedModelMap
	 *            the map that links the original model elements to the copied
	 *            elements.
	 * @param comparison
	 *            the comparison used to determine the match for a copied
	 *            element.
	 */
	private void updateDeletionReference(EReference eReference, EObject original, EObject copy,
			UnifiedModelMap unifiedModelMap, Comparison comparison) {

		if (eReference.isMany()) {

			@SuppressWarnings("unchecked")
			EList<EObject> copyList = (EList<EObject>) copy.eGet(eReference);
			@SuppressWarnings("unchecked")
			EList<EObject> originalList = (EList<EObject>) original.eGet(eReference);

			int index = 0;
			for (EObject originalValue : originalList) {

				if (unifiedModelMap.containsOriginal(originalValue)) {
					/*
					 * A copy exists for this value, so replace it with its
					 * value
					 */
					Collection<EObject> valueCopies = unifiedModelMap.getCopies(originalValue);
					if (!valueCopies.isEmpty()) {
						copyList.remove(originalValue);
					}
					for (EObject valueCopy : valueCopies) {
						if (valueCopy != null && !copyList.contains(valueCopy)) {
							copyList.add(index, valueCopy);
						}
						index++;
					}

				} else {
					/*
					 * No known copy for this value, so search for a copy of the
					 * matched value
					 */
					Collection<EObject> newValueCopies = getNewValueCopies((EObject) originalValue, unifiedModelMap,
							comparison);
					if (!newValueCopies.isEmpty()) {
						copyList.remove(originalValue);
					}
					for (EObject newValueCopy : newValueCopies) {
						if (newValueCopy != null && !copyList.contains(newValueCopy)) {
							copyList.add(index, newValueCopy);
						}
						index++;
					}
					if (newValueCopies.isEmpty()) {
						/*
						 * keep the value as is, but the index must be
						 * incremented to keep the correct order
						 */
						index++;
					}

				}
			}

		} else {

			Object originalValue = original.eGet(eReference);
			Object copyValue = copy.eGet(eReference);
			EObject objValue = null;

			if (copyValue instanceof EObject) {

				objValue = (EObject) copyValue;

			} else if (originalValue instanceof EObject) {

				objValue = (EObject) originalValue;
			}

			if (objValue != null && !unifiedModelMap.containsCopy(objValue)) {
				/*
				 * the value is not a copy so check if it is an original object
				 * with copies
				 */
				Collection<EObject> valueCopies = unifiedModelMap.getCopies(objValue);
				if (!valueCopies.isEmpty()) {

					if (valueCopies.size() > 1) {
						throw new IllegalStateException(MessageFormat.format(
								"Could not update reference: Multiple value copy candidates for mono-valued reference {0} found",
								eReference.toString()));
					}

					/* copy found, now replace the old value with it */
					copy.eSet(eReference, valueCopies.iterator().next());

				} else {
					/*
					 * the value is neither a copy nor an original so try to
					 * find the copy of the matched object
					 */
					Collection<EObject> newValueCopies = getNewValueCopies(objValue, unifiedModelMap, comparison);

					if (!newValueCopies.isEmpty()) {

						/*
						 * copies found, now replace the old value with them
						 */
						if (newValueCopies.size() > 1) {
							throw new IllegalStateException(MessageFormat.format(
									"Could not update reference: Multiple value copy candidates for mono-valued reference {0} found",
									eReference.toString()));
						}

						/* copy found, now replace the old value with it */
						copy.eSet(eReference, newValueCopies.iterator().next());
					}
				}
			}

		}

	}

	/**
	 * updates the containment reference of the copy the represents a deleted
	 * EObject using their matched value copy, if possible.
	 * 
	 * @param original
	 *            the original "deleted" EObject.
	 * @param copy
	 *            the copy of the deleted EObject to update.
	 * @param unifiedModelMap
	 *            the map that links the original model elements to the copied
	 *            elements.
	 * @param comparison
	 *            the comparison used to determine the match for a copied
	 *            element.
	 */
	private void updateDeletionContainment(EObject original, EObject copy, UnifiedModelMap unifiedModelMap,
			Comparison comparison) {

		if (copy.eContainer() != null) {
			/* do not update container if it has been set already */
			return;
		}

		EObject oldContainer = original.eContainer();
		Match containerMatch = comparison.getMatch(oldContainer);
		EObject newContainer = getNewValue(containerMatch);
		EReference containmentFeature = original.eContainmentFeature();
		EObject containerCopy = null;

		Collection<EObject> newContainerCopies = unifiedModelMap.getCopies(newContainer);

		if (newContainerCopies.size() > 1) {
			throw new IllegalStateException(MessageFormat.format(
					"Could not update containment reference: Multiple value copy candidates for mono-valued reference {0} found",
					containmentFeature.toString()));
		}

		if (!newContainerCopies.isEmpty()) {
			containerCopy = newContainerCopies.iterator().next();
		}

		if (containerCopy != null && containerCopy.eClass().getEAllReferences().contains(containmentFeature)) {

			/*
			 * Insert the copied element into the copy of the matched container
			 */

			if (containmentFeature.isMany()) {

				/*
				 * Multi-valued feature, insert at the closest original index
				 */

				@SuppressWarnings("unchecked")
				EList<EObject> oldContainerContent = (EList<EObject>) oldContainer.eGet(containmentFeature);
				int oldIndex = oldContainerContent.indexOf(original);
				int newIndex = 0;
				@SuppressWarnings("unchecked")
				EList<EObject> newContainerContent = (EList<EObject>) newContainer.eGet(containmentFeature);

				/*
				 * determine the new index by finding the index of the first
				 * previous unchanged element.
				 */
				ListIterator<EObject> listIterator = oldContainerContent.listIterator(oldIndex);
				while (listIterator.hasPrevious()) {
					EObject previous = listIterator.previous();
					Match match = comparison.getMatch(previous);
					EObject newValue = getNewValue(match);
					if (newValue != null) {
						newIndex = newContainerContent.indexOf(newValue) + 1;
						break;
					}
				}

				@SuppressWarnings("unchecked")
				EList<EObject> copiedContainerContent = (EList<EObject>) containerCopy.eGet(containmentFeature);

				copiedContainerContent.add(newIndex, copy);

			} else {

				/*
				 * single-valued feature, simply set it
				 */
				containerCopy.eSet(containmentFeature, copy);
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
	 * A Listener for events of a {@link GMFDiagramDiffViewService} instance.
	 * 
	 * @author Florian Zoubek
	 *
	 * @see GMFDiagramDiffViewService#addListener(DiagramDiffServiceListener)
	 * @see GMFDiagramDiffViewService#removeListener(DiagramDiffServiceListener)
	 */
	public interface DiagramDiffServiceListener {

		/**
		 * This method is called after all diagram nodes of the given workspace
		 * diagram for the given model review have been updated.
		 * 
		 * @param workspaceDiagram
		 *            the workspace diagram (the root diagram for a model
		 *            review) instance.
		 * @param modelReview
		 *            the model review associated with the given workspace
		 *            diagram.
		 */
		public void diagramNodesUpdated(Diagram workspaceDiagram, ModelReview review);

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

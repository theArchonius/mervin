/*******************************************************************************
 * Copyright (c) 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.review.explorer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeViewer;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.review.explorer.content.IReviewExplorerContentProvider;
import at.bitandart.zoubek.mervin.review.explorer.content.ITreeItem;

/**
 * A {@link IRunnableWithProgress} that derives the set of objects to highlight
 * based on a set of elements in the review explorer view.
 * 
 * @author Florian Zoubek
 *
 */
public class ReviewExplorerHighlightUpdater implements IRunnableWithProgress {

	private Set<Object> baseElements;
	private Set<Object> objectsToHighlight;
	private TreeViewer treeViewer;
	private IReviewExplorerContentProvider contentProvider;
	private IEventBroker eventBroker;

	/**
	 * 
	 * @param baseElements
	 *            the set of elements to derive the highlighted objects from
	 * @param objectsToHighlight
	 *            the set of objects to store the highlighted object into.
	 * @param treeViewer
	 *            the {@link TreeViewer} that must be refreshed after the
	 *            highlighted elements have been computed.
	 * @param contentProvider
	 *            the {@link IReviewExplorerContentProvider} used to derive
	 *            objects.
	 * @param eventBroker
	 *            the {@link IEventBroker} to use for UI update requests
	 */
	public ReviewExplorerHighlightUpdater(Set<Object> baseElements, Set<Object> objectsToHighlight,
			TreeViewer treeViewer, IReviewExplorerContentProvider contentProvider, IEventBroker eventBroker) {

		this.baseElements = baseElements;
		this.objectsToHighlight = objectsToHighlight;
		this.treeViewer = treeViewer;
		this.contentProvider = contentProvider;
		this.eventBroker = eventBroker;
	}

	@Override
	public void run(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException {
		SubMonitor subMonitor = SubMonitor.convert(progressMonitor, "Recalculating highlights", 410);
		// TODO apply filter
		objectsToHighlight.addAll(baseElements);

		addReferencingMatches(new HashSet<>(objectsToHighlight), objectsToHighlight, subMonitor.newChild(100));

		addTreeItems(new HashSet<>(objectsToHighlight), objectsToHighlight, subMonitor.newChild(100));

		addContainers(new HashSet<>(objectsToHighlight), objectsToHighlight, subMonitor.newChild(100));

		addParentElements(new HashSet<>(objectsToHighlight), objectsToHighlight, subMonitor.newChild(100));

		treeViewer.getControl().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				treeViewer.refresh();

				eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
			}
		});
	}

	/**
	 * adds the matches referencing the given set of elements to the set of
	 * highlighted elements.
	 * 
	 * @param elements
	 *            the set of objects to find the matches for.
	 * @param objectsToHighlight
	 *            the set of objects to store the highlighted objects into.
	 * @param progressMonitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	private void addReferencingMatches(Set<Object> elements, Set<Object> objectsToHighlight,
			IProgressMonitor progressMonitor) {

		SubMonitor subMonitor = SubMonitor.convert(progressMonitor, 100);

		Object input = treeViewer.getInput();
		if (input instanceof ModelReview) {
			addReferencingMatches((ModelReview) input, elements, objectsToHighlight, subMonitor.newChild(100));
		}

	}

	/**
	 * adds the matches referencing the given set of elements to the set of
	 * highlighted elements.
	 * 
	 * @param modelReview
	 *            the model review to find the matches in.
	 * @param elements
	 *            the set of objects to find the matches for.
	 * @param objectsToHighlight
	 *            the set of objects to store the highlighted objects into.
	 * @param progressMonitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	private void addReferencingMatches(ModelReview modelReview, Set<Object> elements, Set<Object> objectsToHighlight,
			IProgressMonitor progressMonitor) {

		SubMonitor subMonitor = SubMonitor.convert(progressMonitor, elements.size() * 100);

		for (Object element : elements) {
			addReferencingMatches(modelReview, element, objectsToHighlight, subMonitor.newChild(100));
		}
	}

	/**
	 * adds the matches referencing the given set of elements to the set of
	 * highlighted elements.
	 * 
	 * @param modelReview
	 *            the model review to find the matches in.
	 * @param element
	 *            the object to find the matches for.
	 * @param objectsToHighlight
	 *            the set of objects to store the highlighted objects into.
	 * @param progressMonitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	private void addReferencingMatches(ModelReview modelReview, Object element, Set<Object> objectsToHighlight,
			IProgressMonitor progressMonitor) {

		List<PatchSet> patchSets = modelReview.getPatchSets();
		SubMonitor subMonitor = SubMonitor.convert(progressMonitor, patchSets.size() * 100 + 100);

		Comparison comparison = modelReview.getSelectedModelComparison();
		if (comparison != null) {
			addReferencingMatches(comparison, element, objectsToHighlight, subMonitor.newChild(50));
		} else {
			subMonitor.setWorkRemaining(patchSets.size() * 100 + 50);
		}

		comparison = modelReview.getSelectedDiagramComparison();
		if (comparison != null) {
			addReferencingMatches(comparison, element, objectsToHighlight, subMonitor.newChild(50));
		} else {
			subMonitor.setWorkRemaining(patchSets.size() * 100);
		}

		for (PatchSet patchSet : patchSets) {
			addReferencingMatches(patchSet, element, objectsToHighlight, subMonitor.newChild(100));
		}

	}

	/**
	 * adds the matches referencing the given set of elements to the set of
	 * highlighted elements.
	 * 
	 * @param comparison
	 *            the comparison to find the matches in.
	 * @param element
	 *            the object to find the matches for.
	 * @param objectsToHighlight
	 *            the set of objects to store the highlighted objects into.
	 * @param progressMonitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	private void addReferencingMatches(Comparison comparison, Object element, Set<Object> objectsToHighlight,
			IProgressMonitor progressMonitor) {

		if (element instanceof EObject) {
			Match match = comparison.getMatch((EObject) element);
			if (match != null) {
				objectsToHighlight.add(match);
			}
		}

	}

	/**
	 * adds the matches referencing the given set of elements to the set of
	 * highlighted elements.
	 * 
	 * @param patchSet
	 *            the patch set to find the matches in.
	 * @param element
	 *            the object to find the matches for.
	 * @param objectsToHighlight
	 *            the set of objects to store the highlighted objects into.
	 * @param progressMonitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	private void addReferencingMatches(PatchSet patchSet, Object element, Set<Object> objectsToHighlight,
			IProgressMonitor progressMonitor) {

		SubMonitor subMonitor = SubMonitor.convert(progressMonitor, 200);

		Comparison comparison = patchSet.getModelComparison();
		if (comparison != null) {
			addReferencingMatches(comparison, element, objectsToHighlight, subMonitor.newChild(100));
		} else {
			subMonitor.setWorkRemaining(100);
		}

		comparison = patchSet.getDiagramComparison();
		if (comparison != null) {
			addReferencingMatches(comparison, element, objectsToHighlight, subMonitor.newChild(100));
		}

	}

	/**
	 * adds the tree items for the given set of elements to the set of
	 * highlighted elements. Adds nothing for elements that have no
	 * {@link ITreeItem}s assigned.
	 * 
	 * @param elements
	 *            the set of objects to find the {@link ITreeItem} for.
	 * @param objectsToHighlight
	 *            the set of objects to store the highlighted objects into.
	 * @param progressMonitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	private void addTreeItems(Set<Object> elements, Set<Object> objectsToHighlight, IProgressMonitor progressMonitor) {

		SubMonitor subMonitor = SubMonitor.convert(progressMonitor, elements.size() * 100);

		for (Object element : elements) {
			subMonitor.newChild(100);
			Set<ITreeItem> treeItems = contentProvider.getTreeItemsFor(element);
			objectsToHighlight.addAll(treeItems);
		}

	}

	/**
	 * Adds the parent elements for the given set of child objects to the set of
	 * highlighted objects using the current {@link #contentProvider}.
	 * 
	 * @param children
	 *            the set of objects to calculate the parents for.
	 * @param objectsToHighlight
	 *            the set of objects to store the parent objects into.
	 * @param progressMonitor
	 *            the {@link IProgressMonitor} to check the cancellation state.
	 */
	protected void addParentElements(Set<Object> children, Set<Object> objectsToHighlight,
			IProgressMonitor progressMonitor) {

		if (progressMonitor.isCanceled()) {
			return;
		}

		for (Object child : children) {

			if (progressMonitor.isCanceled()) {
				return;
			}

			Object parent = contentProvider.getParent(child);

			while (parent != null) {

				if (progressMonitor.isCanceled()) {
					return;
				}

				objectsToHighlight.add(parent);
				parent = contentProvider.getParent(parent);
			}

		}
	}

	/**
	 * Adds the containers for EObjects in the given set of child objects to the
	 * set of highlighted objects.
	 * 
	 * @param children
	 *            the set of objects to calculate the containers for.
	 * @param objectsToHighlight
	 *            the set of objects to store the containers into.
	 * @param progressMonitor
	 *            the {@link IProgressMonitor} to check the cancellation state.
	 */
	protected void addContainers(Set<Object> children, Set<Object> objectsToHighlight,
			IProgressMonitor progressMonitor) {

		for (Object child : children) {

			if (child instanceof EObject) {

				if (progressMonitor.isCanceled()) {
					return;
				}

				EObject container = ((EObject) child).eContainer();

				while (container != null) {

					if (progressMonitor.isCanceled()) {
						return;
					}

					objectsToHighlight.add(container);
					container = container.eContainer();
				}
			}
		}
	}

}

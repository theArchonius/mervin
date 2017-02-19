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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

import at.bitandart.zoubek.mervin.IDiagramModelHelper;
import at.bitandart.zoubek.mervin.IModelReviewHelper;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.IPatchSetHistoryEntry;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;
import at.bitandart.zoubek.mervin.review.explorer.content.IReviewExplorerContentProvider;
import at.bitandart.zoubek.mervin.swt.ProgressPanel;
import at.bitandart.zoubek.mervin.swt.ProgressPanelOperationThread;

/**
 * A {@link ProgressPanelOperationThread} derives the set of objects to
 * highlight based on a list of element in the review explorer view.
 * 
 * @author Florian Zoubek
 *
 */
public class ReviewExplorerHighlightUpdater extends ProgressPanelOperationThread {

	private List<Object> baseElements;
	private Set<Object> objectsToHighlight;
	private ModelReview modelReview;
	private TreeViewer treeViewer;
	private IReviewExplorerContentProvider contentProvider;
	private IDiagramModelHelper diagramModelHelper;
	private IModelReviewHelper modelReviewHelper;

	/**
	 * 
	 * @param progressPanel
	 *            the progress panel to show while the update is in progress.
	 * @param mainPanel
	 *            the main panel that needs to be layouted when the progress
	 *            panel is shown or hidden.
	 * @param baseElements
	 *            the list of elements to derive the highlighted objects from
	 * @param objectsToHighlight
	 *            the set of objects to store the highlighted object into.
	 * @param modelReview
	 *            the context {@link ModelReview} used to derive objects.
	 * @param treeViewer
	 *            the {@link TreeViewer} that must be refreshed after the
	 *            highlighted elements have been computed.
	 * @param contentProvider
	 *            the {@link IReviewExplorerContentProvider} used to derive
	 *            objects.
	 * @param diagramModelHelper
	 *            the {@link IDiagramModelHelper} used to derive objects related
	 *            to the diagram model.
	 * @param modelReviewHelper
	 *            the {@link IModelReviewHelper} used to derive objects related
	 *            to the given {@link ModelReview}.
	 */
	public ReviewExplorerHighlightUpdater(ProgressPanel progressPanel, Composite mainPanel, List<Object> baseElements,
			Set<Object> objectsToHighlight, ModelReview modelReview, TreeViewer treeViewer,
			IReviewExplorerContentProvider contentProvider, IDiagramModelHelper diagramModelHelper,
			IModelReviewHelper modelReviewHelper) {

		super(progressPanel, mainPanel);

		this.baseElements = baseElements;
		this.objectsToHighlight = objectsToHighlight;
		this.modelReview = modelReview;
		this.treeViewer = treeViewer;
		this.contentProvider = contentProvider;
		this.diagramModelHelper = diagramModelHelper;
		this.modelReviewHelper = modelReviewHelper;
	}

	@Override
	protected void runOperation() {

		IProgressMonitor progressMonitor = getProgressMonitor();
		progressMonitor.beginTask("Recalculating highlights...", IProgressMonitor.UNKNOWN);
		// TODO apply filter
		objectsToHighlight.addAll(baseElements);

		addDerivedElementsToHighlight(modelReview, baseElements, objectsToHighlight, progressMonitor);

		addReferencingDiffs(modelReview, new HashSet<>(objectsToHighlight), objectsToHighlight, progressMonitor);

		addContainers(new HashSet<>(objectsToHighlight), objectsToHighlight, progressMonitor);

		addParentElements(new HashSet<>(objectsToHighlight), objectsToHighlight, progressMonitor);

		getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				treeViewer.refresh();
			}
		});
	}

	/**
	 * adds the derived objects to highlight for the given {@link ModelReview}
	 * {@link Diff} to the given list of highlighted objects.
	 * 
	 * @param modelReview
	 *            the model review to highlight elements for.
	 * @param highlightedElements
	 *            the highlighted elements as reported by the highlight service.
	 * @param objectsToHighlight
	 *            the set of elements to add the derived highlighted elements
	 *            to.
	 * @param progressMonitor
	 */
	protected void addDerivedElementsToHighlight(ModelReview modelReview, List<Object> highlightedElements,
			Set<Object> objectsToHighlight, IProgressMonitor progressMonitor) {

		for (Object highlightedElement : highlightedElements) {

			if (progressMonitor.isCanceled()) {
				return;
			}

			if (highlightedElement instanceof IPatchSetHistoryEntry<?, ?>) {

				IPatchSetHistoryEntry<?, ?> historyEntry = (IPatchSetHistoryEntry<?, ?>) highlightedElement;
				Object entryObject = historyEntry.getEntryObject();

				/* check the entry object first */
				if (entryObject instanceof Diff) {

					// TODO apply filter
					Diff diff = (Diff) entryObject;
					addDerivedElementsToHighlight(diff, objectsToHighlight, progressMonitor);

					if (progressMonitor.isCanceled()) {
						return;
					}
				}

				EList<PatchSet> patchSets = modelReview.getPatchSets();
				for (PatchSet patchSet : patchSets) {

					if (progressMonitor.isCanceled()) {
						return;
					}

					Object value = historyEntry.getValue(patchSet);
					if (value instanceof DiffWithSimilarity) {

						// TODO apply filter
						Diff diff = ((DiffWithSimilarity) value).getDiff();
						// TODO apply filter
						addDerivedElementsToHighlight(diff, objectsToHighlight, progressMonitor);

						if (progressMonitor.isCanceled()) {
							return;
						}
					}
				}
			}

			if (highlightedElement instanceof EObject) {
				objectsToHighlight.addAll(diagramModelHelper.getReferencingViews((EObject) highlightedElement));
			}
		}
	}

	/**
	 * adds the derived objects to highlight for the given highlighted
	 * {@link Diff} to the given list of highlighted objects.
	 * 
	 * @param diff
	 *            the highlighted diff to add derived highlighted objects to the
	 *            given list of highlighted objects
	 * @param objectsToHighlight
	 *            the set of objects to store the derived elements into.
	 * @param progressMonitor
	 *            the {@link IProgressMonitor} to check the cancellation state.
	 */
	protected void addDerivedElementsToHighlight(Diff diff, Set<Object> objectsToHighlight,
			IProgressMonitor progressMonitor) {

		if (progressMonitor.isCanceled()) {
			return;
		}

		Object value = MatchUtil.getValue(diff);
		// TODO apply filter
		if (value != null) {
			objectsToHighlight.add(value);
		}
	}

	/**
	 * adds all {@link Diff}s that references the given candidates in the given
	 * {@link ModelReview}.
	 * 
	 * @param modelReview
	 *            the {@link ModelReview} to obtain the {@link Diff}s from.
	 * @param candidates
	 *            the candidates to search for {@link Diff}s.
	 * @param objectsToHighlight
	 *            the set of objects to store the {@link Diff}s into.
	 * @param progressMonitor
	 *            the {@link IProgressMonitor} to check the cancellation state.
	 */
	protected void addReferencingDiffs(ModelReview modelReview, Set<Object> candidates, Set<Object> objectsToHighlight,
			IProgressMonitor progressMonitor) {

		for (Object candidate : candidates) {

			if (progressMonitor.isCanceled()) {
				return;
			}

			if (candidate instanceof EObject) {
				objectsToHighlight.addAll(modelReviewHelper.getDifferences((EObject) candidate, modelReview));
			}
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

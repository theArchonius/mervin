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
package at.bitandart.zoubek.mervin.patchset.history;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;

import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;

/**
 * A {@link IRunnableWithProgress} that derives the set of objects to highlight
 * based on a set of elements in the patch set history view. Supports only
 * {@link TreeViewer}s with {@link ITreeContentProvider}s.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetHistoryHighlightUpdater implements IRunnableWithProgress {

	private Set<Object> highlightedElements;
	private IReviewHighlightService highlightService;
	private ModelReview modelReview;
	private TreeViewer treeViewer;
	private IEventBroker eventBroker;

	/**
	 * @param highlightedElements
	 *            the set of highlighted elements shown in the
	 *            {@link TreeViewer} to fill.
	 * @param highlightService
	 *            the {@link IReviewHighlightService} used to obtain the current
	 *            set of objects to highlight.
	 * @param treeViewer
	 *            the {@link TreeViewer} to update once the highlighted elements
	 *            have been determined. The TreeViewer must have an
	 *            {@link ITreeContentProvider} assigned.
	 * @param modelReview
	 *            the model review to be highlighted.
	 * @param eventBroker
	 *            the {@link IEventBroker} called to update the UI.
	 */
	public PatchSetHistoryHighlightUpdater(Set<Object> highlightedElements, IReviewHighlightService highlightService,
			TreeViewer treeViewer, ModelReview modelReview, IEventBroker eventBroker) {
		this.highlightedElements = highlightedElements;
		this.treeViewer = treeViewer;
		this.modelReview = modelReview;
		this.highlightService = highlightService;
		this.eventBroker = eventBroker;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Updating highlights..", 102);

		ITreeContentProvider contentProvider = getTreeContentProvider();
		Set<Object> objectsToHighlight = highlightService.getHighlightedElements(modelReview);
		subMonitor.worked(1);

		if (contentProvider != null) {

			Object[] elements = contentProvider.getElements(treeViewer.getInput());

			subMonitor.setWorkRemaining(elements.length * 100 + 1);

			for (Object element : elements) {
				collectHighlightedElements(element, objectsToHighlight, contentProvider, subMonitor.newChild(100));
			}

			treeViewer.getControl().getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					treeViewer.refresh();
					eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
				}
			});
		}
	}

	/**
	 * @return the {@link ITreeContentProvider} of the {@link TreeViewer} or
	 *         null if no {@link ITreeContentProvider} could be found.
	 */
	private ITreeContentProvider getTreeContentProvider() {

		IContentProvider contentProvider = treeViewer.getContentProvider();
		if (contentProvider instanceof ITreeContentProvider) {
			return (ITreeContentProvider) contentProvider;
		}

		return null;
	}

	/**
	 * collects the highlighted elements in the {@link TreeViewer} for the given
	 * element and its children provided by the give
	 * {@link ITreeContentProvider}.
	 * 
	 * @param element
	 *            the element to collect the highlighted elements for.
	 * @param objectsToHighlight
	 *            the set of objects that should be highlighted.
	 * @param contentProvider
	 *            the {@link ITreeContentProvider} used to obtain the children
	 *            of the given element.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	private void collectHighlightedElements(Object element, Set<Object> objectsToHighlight,
			ITreeContentProvider contentProvider, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 1000);
		if (subMonitor.isCanceled()) {
			return;
		}

		if (isHighlighted(element, objectsToHighlight, contentProvider)) {
			highlightedElements.add(element);
		}

		subMonitor.worked(100);

		Object[] children = contentProvider.getChildren(element);
		subMonitor.setWorkRemaining(children.length * 100);
		for (Object child : children) {
			collectHighlightedElements(child, objectsToHighlight, contentProvider, subMonitor.newChild(100));
		}

	}

	/**
	 * @param element
	 *            the element to determine the highlight state.
	 * @param highlightedObjects
	 *            the set of highlighted objects
	 * @param contentProvider
	 *            the content provider used to obtain the parent or the children
	 *            of the element.
	 * @return true if the element should be highlighted, false otherwise.
	 */
	public boolean isHighlighted(Object element, Set<Object> highlightedObjects, ITreeContentProvider contentProvider) {

		if (element == null) {
			return false;
		}

		if (highlightedObjects.contains(element)) {
			return true;
		}

		if (element instanceof IPatchSetHistoryEntry) {
			if (isHighlighted(((IPatchSetHistoryEntry<?, ?>) element).getEntryObject(), highlightedObjects,
					contentProvider)) {
				return true;
			}

			Map<PatchSet, ?> values = ((IPatchSetHistoryEntry<?, ?>) element).getValues(modelReview.getPatchSets());

			for (Object value : values.values()) {
				if (isHighlighted(value, highlightedObjects, contentProvider)) {
					return true;
				}
			}
		}

		if (element instanceof DiffWithSimilarity
				&& isHighlighted(((DiffWithSimilarity) element).getDiff(), highlightedObjects, contentProvider)) {
			return true;
		}

		if (element instanceof Match) {
			Match match = (Match) element;

			EObject left = match.getLeft();
			if (left != null && isHighlighted(left, highlightedObjects, contentProvider)) {
				return true;
			}

			EObject right = match.getRight();
			if (right != null && isHighlighted(right, highlightedObjects, contentProvider)) {
				return true;
			}
		}

		if (element instanceof ObjectHistoryEntryContainer) {

			Map<PatchSet, Match> matches = ((ObjectHistoryEntryContainer) element)
					.getMatches(modelReview.getPatchSets());

			for (Match match : matches.values()) {
				if (isHighlighted(match, highlightedObjects, contentProvider)) {
					return true;
				}
			}

		}

		if (element instanceof NamedHistoryEntryContainer) {
			for (Object entry : ((NamedHistoryEntryContainer) element).getSubEntries()) {
				if (isHighlighted(entry, highlightedObjects, contentProvider)) {
					return true;
				}
			}
		}

		Object[] children = contentProvider.getChildren(element);

		for (Object child : children) {
			if (isHighlighted(child, highlightedObjects, contentProvider)) {
				return true;
			}
		}

		return false;
	}

}

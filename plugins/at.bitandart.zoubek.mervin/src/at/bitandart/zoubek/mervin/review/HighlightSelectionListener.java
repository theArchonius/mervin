/*******************************************************************************
 * Copyright (c) 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.review;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * An {@link ISelectionChangedListener} that highlights the current selection
 * using the highlight service and model review of the associated
 * {@link IReviewHighlightingPart}. Nothing will be done if the
 * {@link HighlightMode} of the associated {@link IReviewHighlightingPart} is
 * not set to {@link HighlightMode#SELECTION}.
 * 
 * @author Florian Zoubek
 *
 */
public class HighlightSelectionListener implements ISelectionChangedListener {

	private IReviewHighlightingPart part;

	/**
	 * @param part
	 *            the {@link IReviewHighlightingPart} associated with this
	 *            listener.
	 */
	public HighlightSelectionListener(IReviewHighlightingPart part) {
		this.part = part;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		ModelReview review = part.getHighlightedModelReview();
		ISelection selection = event.getSelection();

		if (part.getHighlightMode() == HighlightMode.SELECTION && review != null) {

			IReviewHighlightService highlightService = part.getReviewHighlightService();

			highlightService.clearHighlights(review);
			if (selection instanceof IStructuredSelection) {

				Set<Object> elements = getHighlightedElementsFor((IStructuredSelection) selection);

				for (Object element : elements) {
					highlightService.addHighlightFor(review, element);
				}
			}
		}
	}

	/**
	 * computes the highlighted elements for the given selection. This method is
	 * called when the selection has changed.
	 * 
	 * @param selection
	 *            the selection to compute the set of highlighted elements for.
	 * @return the set of elements to highlight for the given selection.
	 */
	protected Set<Object> getHighlightedElementsFor(IStructuredSelection selection) {

		Set<Object> highlightedElements = new HashSet<>();
		Iterator<?> selectionIterator = selection.iterator();

		while (selectionIterator.hasNext()) {
			addElementsToHighlight(selectionIterator.next(), highlightedElements);
		}

		return highlightedElements;
	}

	/**
	 * determines the elements to highlight for a particular element. This
	 * method is called per default at least once per element in the selection.
	 * Subclasses may override.
	 * 
	 * @param object
	 *            the object to determine the elements for.
	 * @param elements
	 *            the set to add the elements to highlight for.
	 */
	protected void addElementsToHighlight(Object object, Set<Object> elements) {
		elements.add(object);
	}
}

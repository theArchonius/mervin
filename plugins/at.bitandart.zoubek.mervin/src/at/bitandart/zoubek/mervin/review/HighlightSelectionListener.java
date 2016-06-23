/*******************************************************************************
 * Copyright (c) 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.review;

import java.util.Iterator;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * An {@link ISelectionChangedListener} that highlights the current selection
 * using the highlight service and model review of the associated
 * {@link IReviewHighlightProvidingPart}. Nothing will be done if the
 * {@link HighlightMode} of the associated {@link IReviewHighlightProvidingPart}
 * is not set to {@link HighlightMode#SELECTION}.
 * 
 * @author Florian Zoubek
 *
 */
public class HighlightSelectionListener implements ISelectionChangedListener {

	private IReviewHighlightProvidingPart part;

	/**
	 * @param part
	 *            the {@link IReviewHighlightProvidingPart} associated with this
	 *            listener.
	 */
	public HighlightSelectionListener(IReviewHighlightProvidingPart part) {
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

				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				Iterator<?> iterator = structuredSelection.iterator();

				while (iterator.hasNext()) {
					highlightService.addHighlightFor(review, iterator.next());
				}
			}
		}
	}
}

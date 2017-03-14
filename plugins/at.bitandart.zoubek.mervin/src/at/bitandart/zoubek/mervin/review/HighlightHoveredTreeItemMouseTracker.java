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

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.google.common.collect.Sets;

import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * A {@link MouseTrackListener} that highlights the data element of a tree item
 * using the highlight service and the model review of the
 * {@link IReviewHighlightingPart} when the mouse hovers over a {@link TreeItem}
 * . This tracker does nothing if the the highlight mode of the associated
 * {@link IReviewHighlightingPart} is not set to {@link HighlightMode#HOVER}.
 * 
 * @author Florian Zoubek
 *
 */
public class HighlightHoveredTreeItemMouseTracker implements MouseTrackListener {

	private IReviewHighlightingPart part;

	/**
	 * @param part
	 *            the {@link IReviewHighlightingPart} associated with this
	 *            tracker.
	 */
	public HighlightHoveredTreeItemMouseTracker(IReviewHighlightingPart part) {
		this.part = part;
	}

	@Override
	public void mouseHover(MouseEvent e) {

		ModelReview modelReview = part.getHighlightedModelReview();

		if (part.getHighlightMode() == HighlightMode.HOVER && modelReview != null) {
			Object source = e.getSource();

			if (source instanceof Tree) {
				Tree tree = (Tree) source;
				TreeItem item = tree.getItem(new Point(e.x, e.y));

				if (item != null) {

					Object data = item.getData();

					if (data != null) {

						IReviewHighlightService highlightService = part.getReviewHighlightService();
						/*
						 * clear existing highlights as elements should only be
						 * highlighted while the user hovers over them
						 */
						highlightService.clearHighlights(modelReview);
						/* add new highlighted element */
						highlightService.addHighlightFor(modelReview, Sets.newHashSet(data));
					}
				}
			}
		}
	}

	@Override
	public void mouseExit(MouseEvent e) {

		if (part.getHighlightMode() == HighlightMode.HOVER) {
			/*
			 * clear existing highlights as elements should only be highlighted
			 * while the user hovers over them
			 */
			part.getReviewHighlightService().clearHighlights(part.getHighlightedModelReview());
		}
	}

	@Override
	public void mouseEnter(MouseEvent e) {
		// Intentionally left empty
	}
}

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

import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * Base Interface for parts that provide highlights using a
 * {@link IReviewHighlightService}.
 * 
 * @author Florian Zoubek
 *
 */
public interface IReviewHighlightProvidingPart {

	/**
	 * set the current highlight mode for this view.
	 * 
	 * @param highlightMode
	 *            the highlight mode to set, null values are not allowed and
	 *            will be ignored.
	 * @throws UnsupportedOperationException
	 *             if the highlight mode is not supported, the highlight mode
	 *             will not be changed.
	 */
	public void setHighlightMode(HighlightMode highlightMode) throws UnsupportedOperationException;

	/**
	 * @return the currently active {@link HighlightMode}
	 */
	public HighlightMode getHighlightMode();

	/**
	 * @return the current model review to provide highlights for.
	 */
	public ModelReview getHighlightedModelReview();

	/**
	 * @return the {@link IReviewHighlightService} used to highlight elements.
	 */
	public IReviewHighlightService getReviewHighlightService();

}

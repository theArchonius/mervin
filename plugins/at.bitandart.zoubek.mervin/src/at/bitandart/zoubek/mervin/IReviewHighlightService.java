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
package at.bitandart.zoubek.mervin;

import java.util.List;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * Base interface for a service that manages and notifies about temporary
 * highlighting for given objects associated with a given model review.
 * 
 * @author Florian Zoubek
 *
 */
public interface IReviewHighlightService {

	/**
	 * adds a highlight for the given object associated with the given review.
	 * Does nothing if the object has already been highlight with the given
	 * review.
	 * 
	 * @param review
	 * @param object
	 */
	public void addHighlightFor(ModelReview review, Object object);

	/**
	 * removes a highlight for the given object associated with the given
	 * review. Does nothing if the object has already been highlight with the
	 * given review.
	 * 
	 * @param review
	 * @param object
	 */
	public void removeHighlightFor(ModelReview review, Object object);

	/**
	 * clears all highlights for the given review.
	 * 
	 * @param review
	 */
	public void clearHighlights(ModelReview review);

	/**
	 * 
	 * @param review
	 * @return an unmodifiable list of the highlighted elements of the review.
	 */
	public List<Object> getHighlightedElements(ModelReview review);

	/**
	 * registers a new highlight listener, does nothing if it has already been
	 * registered.
	 * 
	 * @param listener
	 */
	public void addHighlightServiceListener(IReviewHighlightServiceListener listener);

	/**
	 * removes an existing highlight listener.
	 * 
	 * @param listener
	 */
	public void removeHighlightServiceListener(IReviewHighlightServiceListener listener);
}

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

import java.util.Set;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * Base interface for a Listener that is notified by the
 * {@link IReviewHighlightService}.
 * 
 * @author Florian Zoubek
 *
 */
public interface IReviewHighlightServiceListener {

	/**
	 * called when an elements have been added to the set of highlighted
	 * elements.
	 * 
	 * @param review
	 * @param elements
	 */
	public void elementsAdded(ModelReview review, Set<Object> elements);

	/**
	 * called when an elements have been removed from the set of highlighted
	 * elements.
	 * 
	 * @param review
	 * @param elements
	 */
	public void elementsRemoved(ModelReview review, Set<Object> elements);

}

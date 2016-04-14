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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * Default implementation for {@link IReviewHighlightService} that uses a map to
 * store the highlighted elements.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinReviewHighlightService implements IReviewHighlightService {

	private Map<ModelReview, List<Object>> highlightedElements = new HashMap<>();

	private List<IReviewHighlightServiceListener> listeners = new LinkedList<>();

	@Override
	public void addHighlightFor(ModelReview review, Object object) {

		List<Object> elements = highlightedElements.get(review);
		if (elements == null) {
			elements = new LinkedList<Object>();
			highlightedElements.put(review, elements);
		}
		if (!elements.contains(object)) {
			elements.add(object);
			notifyElementAdded(review, object);
		}

	}

	private void notifyElementAdded(ModelReview review, Object object) {

		for (IReviewHighlightServiceListener listener : listeners) {
			listener.elementAdded(review, object);
		}

	}

	@Override
	public void removeHighlightFor(ModelReview review, Object object) {

		List<Object> elements = highlightedElements.get(review);
		if (elements != null && elements.contains(object)) {
			elements.remove(object);
			notifyElementRemoved(review, object);
		}

	}

	private void notifyElementRemoved(ModelReview review, Object object) {

		for (IReviewHighlightServiceListener listener : listeners) {
			listener.elementRemoved(review, object);
		}

	}

	@Override
	public void clearHighlights(ModelReview review) {

		List<Object> elements = highlightedElements.remove(review);

		if (elements != null) {
			for (Object object : elements) {
				notifyElementRemoved(review, object);
			}
		}

	}

	@Override
	public List<Object> getHighlightedElements(ModelReview review) {

		List<Object> elements = highlightedElements.get(review);
		if (elements == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(elements);

	}

	@Override
	public void addHighlightServiceListener(IReviewHighlightServiceListener listener) {

		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}

	}

	@Override
	public void removeHighlightServiceListener(IReviewHighlightServiceListener listener) {

		listeners.add(listener);

	}

}

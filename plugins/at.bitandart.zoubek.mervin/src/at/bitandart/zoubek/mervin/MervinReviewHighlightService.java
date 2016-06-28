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

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.UIEvents;

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

	private ModelReview activeModelReview;

	private MApplication application;

	@Inject
	private IEventBroker eventBroker;

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

		if (review == activeModelReview) {
			updateHighlightedElementsInContext();
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

		if (review == activeModelReview) {
			updateHighlightedElementsInContext();
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

		if (review == activeModelReview) {
			updateHighlightedElementsInContext();
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

		listeners.remove(listener);

	}

	@Optional
	@Inject
	private void activeModelReviewChanged(MApplication application,
			@Named(IMervinContextConstants.ACTIVE_MODEL_REVIEW) ModelReview modelReview) {

		this.application = application;
		this.activeModelReview = modelReview;
		updateHighlightedElementsInContext();
	}

	private void updateHighlightedElementsInContext() {

		application.getContext().modify(IMervinContextConstants.HIGHLIGHTED_ELEMENTS,
				getHighlightedElements(activeModelReview));

		eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
	}

}

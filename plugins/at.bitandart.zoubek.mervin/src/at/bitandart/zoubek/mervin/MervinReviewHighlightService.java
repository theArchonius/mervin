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
package at.bitandart.zoubek.mervin;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.UIEvents;

import com.google.common.collect.Sets;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * Default implementation for {@link IReviewHighlightService} that uses a map to
 * store the highlighted elements.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinReviewHighlightService implements IReviewHighlightService {

	private Map<ModelReview, Set<Object>> highlightedElements = new HashMap<>();

	private List<IReviewHighlightServiceListener> listeners = new LinkedList<>();

	private ModelReview activeModelReview;

	private MApplication application;

	@Inject
	private IEventBroker eventBroker;

	@Override
	public void addHighlightFor(ModelReview review, Set<Object> objects) {

		Set<Object> elements = highlightedElements.get(review);
		if (elements == null) {
			elements = new HashSet<Object>();
			highlightedElements.put(review, elements);
		}
		Set<Object> difference = Sets.newHashSet(objects);
		difference.removeAll(elements);
		if (!difference.isEmpty()) {
			elements.addAll(difference);
			notifyElementsAdded(review, difference);
		}

		if (review == activeModelReview) {
			updateHighlightedElementsInContext();
		}
	}

	private void notifyElementsAdded(ModelReview review, Set<Object> objects) {

		for (IReviewHighlightServiceListener listener : listeners) {
			listener.elementsAdded(review, objects);
		}

	}

	@Override
	public void removeHighlightFor(ModelReview review, Set<Object> objects) {

		Set<Object> elements = highlightedElements.get(review);
		if (elements != null) {

			Set<Object> intersection = Sets.<Object> intersection(elements, objects).immutableCopy();
			if (!intersection.isEmpty()) {
				elements.removeAll(intersection);
				notifyElementsRemoved(review, intersection);
			}
		}

		if (review == activeModelReview) {
			updateHighlightedElementsInContext();
		}
	}

	private void notifyElementsRemoved(ModelReview review, Set<Object> objects) {

		for (IReviewHighlightServiceListener listener : listeners) {
			listener.elementsRemoved(review, objects);
		}

	}

	@Override
	public void clearHighlights(ModelReview review) {

		Set<Object> elements = highlightedElements.remove(review);

		if (elements != null) {
			notifyElementsRemoved(review, elements);
		}

		if (review == activeModelReview) {
			updateHighlightedElementsInContext();
		}

	}

	@Override
	public Set<Object> getHighlightedElements(ModelReview review) {

		Set<Object> elements = highlightedElements.get(review);
		if (elements == null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(elements);

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

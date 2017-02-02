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
package at.bitandart.zoubek.mervin.diagram.diff;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * A temporary {@link IOverlayVisibilityState} based on the given model review.
 * The state must be updated and passed to the appropriate service to apply the
 * visibility to the model. This state is aware of the following overlay types:
 * 
 * <ul>
 * <li>{@link IOverlayTypeDescriptor#TYPE_DESCRIPTOR_ADDITION}</li>
 * <li>{@link IOverlayTypeDescriptor#TYPE_DESCRIPTOR_DELETION}</li>
 * <li>{@link IOverlayTypeDescriptor#TYPE_DESCRIPTOR_MODIFICATION}</li>
 * <li>{@link IOverlayTypeDescriptor#TYPE_DESCRIPTOR_LAYOUT}</li>
 * <li>{@link IOverlayTypeDescriptor#TYPE_DESCRIPTOR_COMMENTS}</li>
 * </ul>
 * 
 * It also returns true for
 * {@link #shouldUpdateOverlayedElement(IOverlayTypeDescriptor)} if the the
 * passed descriptor is equal to
 * {@link IOverlayTypeDescriptor#TYPE_DESCRIPTOR_DELETION}.
 * 
 * @author Florian Zoubek
 *
 */
public class ModelReviewVisibilityState implements IOverlayVisibilityState {

	private Map<IOverlayTypeDescriptor, Boolean> visibilityStates = new HashMap<>();
	private boolean defaultVisibilty;

	/**
	 * creates a new {@link ModelReviewVisibilityState} initialized with the
	 * visibilities stored in the given model review.
	 * 
	 * @param review
	 *            the review to initialize the state from.
	 * @param defaultVisibility
	 *            the default visibility for an unknown overlay type.
	 */
	public ModelReviewVisibilityState(ModelReview review, boolean defaultVisibility) {
		visibilityStates.put(IOverlayTypeDescriptor.TYPE_DESCRIPTOR_ADDITION, review.isShowAdditions());
		visibilityStates.put(IOverlayTypeDescriptor.TYPE_DESCRIPTOR_DELETION, review.isShowDeletions());
		visibilityStates.put(IOverlayTypeDescriptor.TYPE_DESCRIPTOR_MODIFICATION, review.isShowModifications());
		visibilityStates.put(IOverlayTypeDescriptor.TYPE_DESCRIPTOR_LAYOUT, review.isShowLayoutChanges());
		visibilityStates.put(IOverlayTypeDescriptor.TYPE_DESCRIPTOR_COMMENTS, review.isShowComments());
		this.defaultVisibilty = defaultVisibility;
	}

	@Override
	public IOverlayTypeDescriptor[] getAllOverlayTypeDescriptors() {

		Set<IOverlayTypeDescriptor> descriptors = visibilityStates.keySet();
		return descriptors.toArray(new IOverlayTypeDescriptor[descriptors.size()]);

	}

	@Override
	public boolean getVisibility(IOverlayTypeDescriptor descriptor) {

		Boolean visibility = visibilityStates.get(descriptor);
		if (visibility != null) {
			return visibility;
		}
		return defaultVisibilty;

	}

	/**
	 * sets the visibility for the given overlay type. This method does not
	 * update the model accordingly. The state must be updated and passed to the
	 * appropriate service to apply the visibility to the model.
	 * 
	 * @param descriptor
	 * @param visibility
	 */
	public void setVisibility(IOverlayTypeDescriptor descriptor, boolean visibility) {

		visibilityStates.put(descriptor, visibility);

	}

	@Override
	public boolean shouldUpdateOverlayedElement(IOverlayTypeDescriptor descriptor) {
		return descriptor == IOverlayTypeDescriptor.TYPE_DESCRIPTOR_DELETION;
	}

}

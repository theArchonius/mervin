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
package at.bitandart.zoubek.mervin.diagram.diff;

/**
 * Base interface for all objects that hold a visibility state for one or more
 * {@link IOverlayTypeDescriptor}s.
 * 
 * @author Florian Zoubek
 *
 */
public interface IOverlayVisibilityState {

	/**
	 * @return an array containing all overly type descriptors contained in this
	 *         state.
	 */
	public IOverlayTypeDescriptor[] getAllOverlayTypeDescriptors();

	/**
	 * retrieves the visibility for a given overlay type.
	 * 
	 * @param descriptor
	 *            the overlay type descriptor.
	 * @return true if overlays of this type should be visible or not.
	 */
	public boolean getVisibility(IOverlayTypeDescriptor descriptor);

	/**
	 * retrieves if the visibility of the overlayed element of the given
	 * descriptor should also be updated.
	 * 
	 * @param descriptor
	 *            the overlay type descriptor.
	 * @return true if the visibility of the overlayed element should also be
	 *         updated if the visibility of the overlay changes.
	 */
	public boolean shouldUpdateOverlayedElement(IOverlayTypeDescriptor descriptor);

}

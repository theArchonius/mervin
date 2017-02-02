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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;

import at.bitandart.zoubek.mervin.model.modelreview.Difference;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.LayoutDifference;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifferenceType;

/**
 * Base interface for Objects that describe a specific type of overlays.
 * 
 * @author Florian Zoubek
 *
 */
public interface IOverlayTypeDescriptor {

	/**
	 * tests if the overlay is the type that this descriptor represents or not.
	 * 
	 * @param overlay
	 *            the overlay to test.
	 * @return true if the given overlay is the type this descriptor represents,
	 *         false otherwise.
	 */
	public boolean isType(DifferenceOverlay overlay);

	/**
	 * called once to store the visibility value for the overlay type that this
	 * descriptor represents in the given {@link Diagram}. This method is meant
	 * to store the visibility state in a way that it can be restored once
	 * overlays have been removed or added. Therefore implementors must not
	 * change the visibility of an view containing an overlay within this method
	 * as this would be very likely overridden.
	 * 
	 * @param diagram
	 * @param visibility
	 */
	public void storeTypeVisibility(Diagram diagram, boolean visibility);

	/**
	 * {@link IOverlayTypeDescriptor} that selects addition overlays.
	 */
	public static final IOverlayTypeDescriptor TYPE_DESCRIPTOR_ADDITION = new StateDifferenceOverlayTypeDescriptor(
			StateDifferenceType.ADDED);

	/**
	 * {@link IOverlayTypeDescriptor} that selects deletion overlays.
	 */
	public static final IOverlayTypeDescriptor TYPE_DESCRIPTOR_DELETION = new StateDifferenceOverlayTypeDescriptor(
			StateDifferenceType.DELETED);

	/**
	 * {@link IOverlayTypeDescriptor} that selects modification overlays.
	 */
	public static final IOverlayTypeDescriptor TYPE_DESCRIPTOR_MODIFICATION = new StateDifferenceOverlayTypeDescriptor(
			StateDifferenceType.MODIFIED);

	/**
	 * {@link IOverlayTypeDescriptor} that selects layout change overlays.
	 */
	public static final IOverlayTypeDescriptor TYPE_DESCRIPTOR_LAYOUT = new IOverlayTypeDescriptor() {

		@Override
		public boolean isType(DifferenceOverlay overlay) {
			for (Difference difference : overlay.getDifferences()) {
				if (difference instanceof LayoutDifference) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void storeTypeVisibility(Diagram diagram, boolean visibility) {

			EObject element = diagram.getElement();
			if (element instanceof ModelReview) {
				((ModelReview) element).setShowLayoutChanges(visibility);
			}

		}

	};

	/**
	 * {@link IOverlayTypeDescriptor} that selects overlays with comments.
	 */
	public static final IOverlayTypeDescriptor TYPE_DESCRIPTOR_COMMENTS = new IOverlayTypeDescriptor() {

		@Override
		public boolean isType(DifferenceOverlay overlay) {
			return overlay.isCommented();
		}

		@Override
		public void storeTypeVisibility(Diagram diagram, boolean visibility) {

			EObject element = diagram.getElement();
			if (element instanceof ModelReview) {
				((ModelReview) element).setShowComments(visibility);
			}

		}

	};
}
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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;

import at.bitandart.zoubek.mervin.model.modelreview.Difference;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifference;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifferenceType;

/**
 * An {@link IOverlayTypeDescriptor} that represents all overlays that show a
 * difference with the given {@link StateDifferenceType}.
 * 
 * @author Florian Zoubek
 *
 */
public class StateDifferenceOverlayTypeDescriptor implements IOverlayTypeDescriptor {

	private StateDifferenceType differenceType;

	public StateDifferenceOverlayTypeDescriptor(StateDifferenceType differenceType) {
		this.differenceType = differenceType;
	}

	@Override
	public boolean isType(DifferenceOverlay overlay) {
		for (Difference difference : overlay.getDifferences()) {
			if (difference instanceof StateDifference && ((StateDifference) difference).getType() == differenceType) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void storeTypeVisibility(Diagram diagram, boolean visibility) {

		EObject element = diagram.getElement();
		if (element instanceof ModelReview) {
			switch (differenceType) {
			case ADDED:
				((ModelReview) element).setShowAdditions(visibility);
				break;
			case DELETED:
				((ModelReview) element).setShowDeletions(visibility);
				break;
			case MODIFIED:
				((ModelReview) element).setShowModifications(visibility);
				break;
			default:
				// do not store the visibility for unknown types
				break;
			}
		}

	}

}
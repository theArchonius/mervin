/*******************************************************************************
 * Copyright (c) 2015 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.diagram.diff.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

import at.bitandart.zoubek.mervin.draw2d.figures.ChangeOverlayNodeFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.ChangeOverlayNodeFigure.DimensionPropertyChangeType;
import at.bitandart.zoubek.mervin.draw2d.figures.ChangeType;
import at.bitandart.zoubek.mervin.model.modelreview.Difference;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.DimensionChange;
import at.bitandart.zoubek.mervin.model.modelreview.LocationDifference;
import at.bitandart.zoubek.mervin.model.modelreview.NodeDifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.SizeDifference;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifference;

/**
 * An {@link ShapeNodeEditPart} implementation for {@link NodeDifferenceOverlay}
 * s.
 * 
 * @author Florian Zoubek
 *
 */
public class NodeDifferenceOverlayEditPart extends AbstractDifferenceOverlayEditPart {

	public NodeDifferenceOverlayEditPart(View view) {
		super(view);
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();

		DifferenceOverlay differenceOverlay = getDifferenceOverlay();
		ChangeOverlayNodeFigure changeOverlayNodeFigure = getChangeOverlayNodeFigure();

		if (differenceOverlay != null && changeOverlayNodeFigure != null) {

			EList<Difference> differences = differenceOverlay.getDifferences();
			boolean noStateDifference = true;
			for (Difference difference : differences) {

				if (difference instanceof StateDifference) {

					switch (((StateDifference) difference).getType()) {
					case ADDED:
						changeOverlayNodeFigure.setChangeType(ChangeType.ADDITION);
						noStateDifference = false;
						break;
					case DELETED:
						changeOverlayNodeFigure.setChangeType(ChangeType.DELETION);
						noStateDifference = false;
						break;
					case MODIFIED:
						changeOverlayNodeFigure.setChangeType(ChangeType.MODIFICATION);
						noStateDifference = false;
						break;
					default:
						// do nothing
					}

				} else if (difference instanceof SizeDifference) {

					SizeDifference sizeDifference = (SizeDifference) difference;

					changeOverlayNodeFigure
							.setBoundsHeightChangeType(toDimensionPropertyChangeType(sizeDifference.getHeightChange()));
					changeOverlayNodeFigure
							.setBoundsWidthChangeType(toDimensionPropertyChangeType(sizeDifference.getWidthChange()));

				} else if (difference instanceof LocationDifference) {

					changeOverlayNodeFigure.setMoveDirection(((LocationDifference) difference).getMoveDirection());

				}

			}
			if (noStateDifference) {
				changeOverlayNodeFigure.setChangeType(ChangeType.LAYOUT);
			}
		}

	}

	/**
	 * converts a {@link DimensionChange} to a
	 * {@link DimensionPropertyChangeType}
	 * 
	 * @param dimensionChange
	 *            the {@link DimensionChange} to convert
	 * @return the corresponding {@link DimensionPropertyChangeType}
	 */
	private static DimensionPropertyChangeType toDimensionPropertyChangeType(DimensionChange dimensionChange) {
		switch (dimensionChange) {
		case SMALLER:
			return DimensionPropertyChangeType.SMALLER;
		case BIGGER:
			return DimensionPropertyChangeType.BIGGER;
		default:
		case EQUAL:
		case UNKNOWN:
			return DimensionPropertyChangeType.NONE;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#
	 * createNodeFigure()
	 */
	@Override
	protected NodeFigure createNodeFigure() {
		return new ChangeOverlayNodeFigure(styleAdvisor, ChangeType.ADDITION);
	}

	protected ChangeOverlayNodeFigure getChangeOverlayNodeFigure() {
		IFigure figure = getFigure();
		if (figure instanceof ChangeOverlayNodeFigure) {
			return (ChangeOverlayNodeFigure) figure;
		}
		return null;
	}

}

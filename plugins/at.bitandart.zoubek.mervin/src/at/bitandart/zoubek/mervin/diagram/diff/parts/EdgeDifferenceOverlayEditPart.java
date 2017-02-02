/*******************************************************************************
 * Copyright (c) 2015, 2016, 2017 Florian Zoubek.
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

import at.bitandart.zoubek.mervin.draw2d.figures.OverlayConnectionFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.OverlayType;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.ConnectionOffScreenOverlayIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.IOffScreenIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.OffScreenOverlayIndicator;
import at.bitandart.zoubek.mervin.model.modelreview.BendpointsDifference;
import at.bitandart.zoubek.mervin.model.modelreview.Difference;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.EdgeDifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifference;

/**
 * An {@link ShapeNodeEditPart} implementation for {@link EdgeDifferenceOverlay}
 * s.
 * 
 * @author Florian Zoubek
 *
 */
public class EdgeDifferenceOverlayEditPart extends AbstractDifferenceOverlayEditPart {

	public EdgeDifferenceOverlayEditPart(View view) {
		super(view);
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();

		DifferenceOverlay differenceOverlay = getDifferenceOverlay();
		OverlayConnectionFigure changeOverlayConnectionFigure = getChangeOverlayConnectionFigure();
		OffScreenOverlayIndicator offScreenChangeIndicator = getOffScreenChangeIndicator();

		if (differenceOverlay != null && changeOverlayConnectionFigure != null) {

			boolean bendpointsChanged = false;
			boolean stateChanged = false;

			changeOverlayConnectionFigure.setShowCommentHint(differenceOverlay.isCommented());

			EList<Difference> differences = differenceOverlay.getDifferences();

			/*
			 * assume a comment only overlay by default if the overlayed element
			 * has comments, this will be overridden if state difference are
			 * found.
			 */
			if (differences.isEmpty() && differenceOverlay.isCommented()) {
				changeOverlayConnectionFigure.setOverlayType(OverlayType.COMMENT);
				if (offScreenChangeIndicator != null) {
					offScreenChangeIndicator.setOverlayType(OverlayType.COMMENT);
				}
			}

			for (Difference difference : differences) {

				if (difference instanceof StateDifference) {

					switch (((StateDifference) difference).getType()) {
					case ADDED:
						changeOverlayConnectionFigure.setOverlayType(OverlayType.ADDITION);
						if (offScreenChangeIndicator != null) {
							offScreenChangeIndicator.setOverlayType(OverlayType.ADDITION);
						}
						break;
					case DELETED:
						changeOverlayConnectionFigure.setOverlayType(OverlayType.DELETION);
						if (offScreenChangeIndicator != null) {
							offScreenChangeIndicator.setOverlayType(OverlayType.DELETION);
						}
						break;
					case MODIFIED:
						changeOverlayConnectionFigure.setOverlayType(OverlayType.MODIFICATION);
						if (offScreenChangeIndicator != null) {
							offScreenChangeIndicator.setOverlayType(OverlayType.MODIFICATION);
						}
						break;
					default:
						// do nothing
					}
					stateChanged = true;

				}

				bendpointsChanged |= difference instanceof BendpointsDifference;

			}

			if (bendpointsChanged && !stateChanged) {
				changeOverlayConnectionFigure.setOverlayType(OverlayType.LAYOUT);
				if (offScreenChangeIndicator != null) {
					offScreenChangeIndicator.setOverlayType(OverlayType.LAYOUT);
				}
			}

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
		return new OverlayConnectionFigure(getStyleAdvisor(), OverlayType.ADDITION);
	}

	@Override
	protected IOffScreenIndicator createOffScreenIndicator() {
		return new ConnectionOffScreenOverlayIndicator(getStyleAdvisor());
	}

	protected OverlayConnectionFigure getChangeOverlayConnectionFigure() {
		IFigure figure = getFigure();
		if (figure instanceof OverlayConnectionFigure) {
			return (OverlayConnectionFigure) figure;
		}
		return null;
	}

	protected OffScreenOverlayIndicator getOffScreenChangeIndicator() {
		IOffScreenIndicator offScreenIndicator = getOffScreenIndicator();
		if (offScreenIndicator instanceof OffScreenOverlayIndicator) {
			return (OffScreenOverlayIndicator) offScreenIndicator;
		}
		return null;
	}

}

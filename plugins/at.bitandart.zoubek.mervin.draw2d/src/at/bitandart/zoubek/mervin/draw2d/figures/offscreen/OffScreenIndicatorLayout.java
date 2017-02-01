/*******************************************************************************
 * Copyright (c) 2015, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.draw2d.figures.offscreen;

import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

import at.bitandart.zoubek.mervin.draw2d.DoublePrecisionVector;

/**
 * A {@link LayoutManager} that positions {@link IOffScreenIndicator}s at the
 * closest border on the line between the center of the indicators assigned
 * container and the center of the combined bounds of the indicators linked
 * figures. All other figures will be ignored.
 * 
 * @author Florian Zoubek
 *
 */
public class OffScreenIndicatorLayout extends AbstractLayout {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void layout(IFigure container) {

		List<?> children = container.getChildren();

		for (Object child : children) {

			if (child instanceof IOffScreenIndicator) {

				IOffScreenIndicator offScreenIndicator = (IOffScreenIndicator) child;
				IFigure linkedContainer = offScreenIndicator.getContainerFigure();
				Dimension childDimension = ((IOffScreenIndicator) child).getPreferredSize().getCopy();

				Rectangle shrinkedBounds = linkedContainer.getBounds().getShrinked(childDimension.preciseWidth() / 2.0,
						childDimension.preciseHeight() / 2.0);

				// the shape to project the child indicators on
				PrecisionPoint[] shape = new PrecisionPoint[] { //
						new PrecisionPoint(shrinkedBounds.getTopLeft()),
						new PrecisionPoint(shrinkedBounds.getBottomLeft()),
						new PrecisionPoint(shrinkedBounds.getBottomRight()),
						new PrecisionPoint(shrinkedBounds.getTopRight()),
						new PrecisionPoint(shrinkedBounds.getTopLeft()) };
				for (int i = 0; i < shape.length; i++) {
					linkedContainer.translateToAbsolute(shape[i]);
				}

				PrecisionPoint center = new PrecisionPoint(linkedContainer.getBounds().getCenter());
				linkedContainer.translateToAbsolute(center);
				PrecisionPoint figureLocation = offScreenIndicator.getReferencePoint();

				DoublePrecisionVector centerToFigureDir = new DoublePrecisionVector(center, figureLocation);
				DoublePrecisionVector centerToFigureDirPerpLeft = centerToFigureDir.getLeftPerpendicularVectorScreen();
				DoublePrecisionVector centerToFigureDirPerpRight = centerToFigureDir
						.getRightPerpendicularVectorScreen();

				for (int i = 0; i < shape.length - 1; i++) {

					PrecisionPoint firstPoint = shape[i];
					PrecisionPoint secondPoint = shape[i + 1];

					DoublePrecisionVector segmentDir = new DoublePrecisionVector(firstPoint, secondPoint);
					DoublePrecisionVector segmentDirPerpRight = segmentDir.getRightPerpendicularVectorScreen();

					DoublePrecisionVector w = new DoublePrecisionVector(center, firstPoint);

					double perp = centerToFigureDirPerpRight.getDotProduct(segmentDir);
					if (perp != 0) {
						double s = centerToFigureDirPerpLeft.getDotProduct(w) / perp;
						double t = segmentDirPerpRight.getDotProduct(w) / -perp;
						if (s >= 0.0 && s <= 1.0 && t >= 0.0 && t <= 1.0) {
							PrecisionPoint newLocation = centerToFigureDir.getMultipliedDoublePrecision(t)
									.toPrecisionPoint();
							newLocation.translate(center);

							/*
							 * the current location is center based, but the
							 * bounds location is top-left, so translate to top-
							 * left based location
							 */
							newLocation.translate(-childDimension.preciseWidth() / 2.0,
									-childDimension.preciseHeight() / 2.0);
							container.translateToRelative(newLocation);
							container.translateFromParent(newLocation);
							((IOffScreenIndicator) child)
									.setBounds(new PrecisionRectangle(newLocation.preciseX(), newLocation.preciseY(),
											childDimension.preciseWidth(), childDimension.preciseHeight()));

							break;
						}
					}

				}

			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.
	 * draw2d.IFigure, int, int)
	 */
	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {

		/*
		 * the preferred size is the size of the combined bounds of all
		 * container figures of the indicators.
		 */

		PrecisionRectangle bounds = null;

		List<?> children = container.getChildren();

		for (Object child : children) {

			if (child instanceof IOffScreenIndicator) {

				IFigure containerFigure = ((IOffScreenIndicator) child).getContainerFigure();

				if (containerFigure != null) {
					PrecisionRectangle containerBounds = new PrecisionRectangle(containerFigure.getBounds());
					containerFigure.translateToAbsolute(containerBounds);

					if (bounds == null) {
						bounds = containerBounds;
					} else {
						bounds.union((Rectangle) containerBounds);
					}
				}

			}
		}

		if (bounds == null) {
			return new Dimension();
		}

		return bounds.getSize();
	}

}

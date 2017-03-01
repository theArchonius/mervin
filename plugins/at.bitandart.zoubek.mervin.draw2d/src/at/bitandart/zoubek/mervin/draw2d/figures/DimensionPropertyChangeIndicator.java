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
package at.bitandart.zoubek.mervin.draw2d.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A figure which indicates if one property of a dimension has changed (smaller
 * or bigger). This Figure is not intended to have child figures.
 * 
 * @author Florian Zoubek
 * 
 * @see DimensionProperty
 * @see DimensionChange
 *
 */
public class DimensionPropertyChangeIndicator extends Figure {

	/**
	 * The dimension properties supported by
	 * {@link DimensionPropertyChangeIndicator}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public enum DimensionProperty {
		WIDTH, HEIGHT
	}

	public enum DimensionChange {
		BIGGER, SMALLER, SET, UNSET
	}

	/**
	 * determines which property of the dimension has changed.
	 */
	private DimensionProperty dimensionProperty;

	/**
	 * the change of the property to display.
	 */
	private DimensionChange dimensionChange;

	/**
	 * @param dimensionProperty
	 *            the changed property.
	 * @param dimensionChange
	 *            the change of the property.
	 */
	public DimensionPropertyChangeIndicator(DimensionProperty dimensionProperty, DimensionChange dimensionChange) {
		this.dimensionProperty = dimensionProperty;
		this.dimensionChange = dimensionChange;
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		graphics.pushState();

		Rectangle paintingArea = getBounds().getCopy().shrink(getInsets());

		if (dimensionProperty == DimensionProperty.HEIGHT) {
			graphics.translate(paintingArea.getLeft());
			graphics.rotate(-90);
			graphics.translate(0, -1);
		} else {
			graphics.translate(paintingArea.getTop());
		}

		switch (dimensionChange) {
		case BIGGER:
		case SMALLER:
			drawArrows(graphics, paintingArea);
			break;
		case SET:
		case UNSET:
			drawSetUnsetIndicator(graphics, paintingArea);
			break;
		default:

		}

		graphics.popState();
	}

	/**
	 * draws the set or unset indicator based on {@link #dimensionChange} and
	 * {@link #dimensionProperty}.
	 * 
	 * @param graphics
	 *            the graphics context used to draw.
	 * @param paintingArea
	 *            the area to draw into (relative to the bounds, no rotation or
	 *            translation applied)
	 */
	private void drawSetUnsetIndicator(Graphics graphics, Rectangle paintingArea) {

		int width = dimensionProperty == DimensionProperty.WIDTH ? paintingArea.width() : paintingArea.height();
		int height = dimensionProperty == DimensionProperty.WIDTH ? paintingArea.height() : paintingArea.width();
		width = Math.round(width * 0.65f);
		int verticalLineOffset = Math.round(width * 0.5f);

		int middleLineX = Math.round(height * 0.5f);

		graphics.setLineWidthFloat(2.0f);

		graphics.drawLine(-verticalLineOffset, 0, -verticalLineOffset, height);
		graphics.drawLine(verticalLineOffset, 0, verticalLineOffset, height);
		graphics.setLineWidthFloat(1.0f);
		graphics.drawLine(-verticalLineOffset, middleLineX, verticalLineOffset, middleLineX);

		if (dimensionChange == DimensionChange.UNSET) {
			int offsetX = Math.round(width * 0.1f);
			int offsetY = Math.round(height * 0.1f);
			graphics.drawLine(-offsetX, offsetY, offsetX, height - offsetY);
		}

	}

	/**
	 * draws the dimension change arrows based on {@link #dimensionChange} and
	 * {@link #dimensionProperty}.
	 * 
	 * @param graphics
	 *            the graphics context used to draw.
	 * @param paintingArea
	 *            the area to draw into (relative to the bounds, no rotation or
	 *            translation applied)
	 */
	private void drawArrows(Graphics graphics, Rectangle paintingArea) {

		int arrowSpacing = 2;
		double arrowHeight = (dimensionProperty == DimensionProperty.WIDTH) ? paintingArea.preciseHeight()
				: paintingArea.preciseWidth();
		double arrowWidth = ((dimensionProperty == DimensionProperty.WIDTH) ? paintingArea.preciseWidth()
				: paintingArea.preciseHeight()) / 2.0 - arrowSpacing;

		arrowHeight = Math.min(arrowHeight, arrowWidth);
		arrowWidth = arrowHeight;

		graphics.translate((float) -arrowWidth, 0.0f);

		int[] arrowRight = new int[] {
				/* top left */
				0, 0,
				/* arrow tip */
				(int) arrowWidth, (int) (arrowHeight / 2.0),
				/**/
				0, (int) arrowHeight };

		int[] arrowLeft = new int[] {
				/* top left */
				(int) arrowWidth, 0,
				/* arrow tip */
				0, (int) (arrowHeight / 2.0),
				/**/
				(int) arrowWidth, (int) arrowHeight };

		graphics.setBackgroundColor(graphics.getForegroundColor());

		if (dimensionChange == DimensionChange.SMALLER) {
			graphics.fillPolygon(arrowRight);
		} else {
			graphics.fillPolygon(arrowLeft);
		}

		graphics.translate((int) arrowWidth + arrowSpacing, 0);

		if (dimensionChange == DimensionChange.SMALLER) {
			graphics.fillPolygon(arrowLeft);
		} else {
			graphics.fillPolygon(arrowRight);
		}
	}

	/**
	 * @param dimensionChange
	 *            the change to display.
	 */
	public void setDimensionChange(DimensionChange dimensionChange) {
		this.dimensionChange = dimensionChange;
		repaint();
	}

	/**
	 * @return the displayed change.
	 */
	public DimensionChange getDimensionChange() {
		return dimensionChange;
	}

	/**
	 * @param dimensionProperty
	 *            the property of the dimension change to display.
	 */
	public void setDimensionProperty(DimensionProperty dimensionProperty) {
		this.dimensionProperty = dimensionProperty;
		repaint();
	}

	/**
	 * @return the property of the dimension change to display.
	 */
	public DimensionProperty getDimensionProperty() {
		return dimensionProperty;
	}
}

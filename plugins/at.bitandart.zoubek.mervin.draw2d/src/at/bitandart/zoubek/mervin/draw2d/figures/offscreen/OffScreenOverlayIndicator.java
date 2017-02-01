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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;

import at.bitandart.zoubek.mervin.draw2d.figures.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.OverlayType;

/**
 * A {@link IOffScreenIndicator} that indicates the overlay type of an
 * off-screen {@link IFigure}. It draws a single character representing the
 * overlay type in the center, surrounded by a circle. Additionally an arrow
 * points towards the invisible figure. The visibility of this figure depends on
 * the visibility of the combined bounds of the linked figures - if the they
 * intersect the containers bounds the indicator is invisible, otherwise it is
 * visible.
 * 
 * @author Florian Zoubek
 *
 */
public class OffScreenOverlayIndicator extends AbstractOffScreenOverlayIndicator {

	/**
	 * the overlay type to indicate.
	 */
	private OverlayType overlayType = OverlayType.ADDITION;

	/**
	 * creates a new {@link OffScreenOverlayIndicator} that uses the given
	 * {@link IOverlayTypeStyleAdvisor} for drawing.
	 * 
	 * @param styleAdvisor
	 */
	public OffScreenOverlayIndicator(IOverlayTypeStyleAdvisor styleAdvisor) {
		super(styleAdvisor);
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		PrecisionRectangle bounds = new PrecisionRectangle(getBounds());
		OverlayType overlayType = getOverlayType();

		graphics.setBackgroundColor(styleAdvisor.getForegroundColorForOverlayType(overlayType));
		Rectangle shrinked = getSymbolBounds(bounds);

		if (linkedFigures != null) {

			drawArrow(graphics, bounds, overlayType);

		}

		// draw enclosing circle

		drawSymbolBorder(graphics, shrinked);

		Font symbolFont = createSymbolFont();
		String symbol = getSymbolFor(overlayType);

		graphics.setForegroundColor(ColorConstants.white);
		drawSymbol(symbol, graphics, shrinked, symbolFont);

		symbolFont.dispose();

	}

	/**
	 * @return the symbol to draw.
	 */
	@Override
	protected String getSymbol() {
		return getSymbolFor(getOverlayType());
	}

	/**
	 * @param overlayType
	 * @return the corresponding symbol to the given overlay type
	 */
	protected String getSymbolFor(OverlayType overlayType) {
		switch (overlayType) {
		case ADDITION:
			return "A";
		case DELETION:
			return "D";
		case MODIFICATION:
			return "M";
		case COMMENT:
			return "C";
		default:
		case LAYOUT:
			return "L";
		}
	}

	/**
	 * sets the overlay type of this indicator.
	 * 
	 * @param overlayType
	 */
	public void setOverlayType(OverlayType overlayType) {
		this.overlayType = overlayType;
	}

	/**
	 * 
	 * @return the overlay type of this indicator.
	 */
	public OverlayType getOverlayType() {
		return overlayType;
	}

}

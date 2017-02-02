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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import at.bitandart.zoubek.mervin.draw2d.DoublePrecisionVector;
import at.bitandart.zoubek.mervin.draw2d.figures.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.OverlayType;

/**
 * 
 * Abstract {@link IOffScreenIndicator} based on {@link Figure} that provides
 * common implementations and methods for off-screen overlay indicators.
 * 
 * @author Florian Zoubek
 *
 */
public abstract class AbstractOffScreenOverlayIndicator extends AbstractOffScreenIndicator {

	/**
	 * the style advisor used to obtain the colors for this indicator
	 */
	protected IOverlayTypeStyleAdvisor styleAdvisor;

	/**
	 * the line width used to draw the circle of this indicator
	 */
	private double lineWidth = 2.0;

	/**
	 * the padding between the circle and the bounds of the figure - this
	 * padding is used to make the padding visible.
	 */
	protected double arrowPadding = 5.0;

	/**
	 * 
	 * creates a new {@link AbstractOffScreenOverlayIndicator} that uses the
	 * given {@link IOverlayTypeStyleAdvisor} for drawing.
	 * 
	 * @param styleAdvisor
	 */
	public AbstractOffScreenOverlayIndicator(IOverlayTypeStyleAdvisor styleAdvisor) {
		super();
		this.styleAdvisor = styleAdvisor;
	}

	/**
	 * derives the symbol bounds from a given rectangle.
	 * 
	 * @param bounds
	 * @return the symbol bounds based on the given rectangle.
	 */
	protected Rectangle getSymbolBounds(Rectangle bounds) {
		return bounds.getShrinked(lineWidth / 2.0 + arrowPadding, lineWidth / 2.0 + arrowPadding);
	}

	/**
	 * draws the border around the symbol.
	 * 
	 * @param graphics
	 *            the graphics context used to draw the arrow.
	 * @param bounds
	 *            the bounds to draw the border within.
	 */
	protected void drawSymbolBorder(Graphics graphics, Rectangle bounds) {

		Rectangle copy = bounds.getCopy();
		copy.setWidth(bounds.width + 1);
		copy.setHeight(bounds.height + 1);
		graphics.fillOval(copy);
		graphics.setLineWidthFloat((float) lineWidth);

	}

	/**
	 * draws the given symbol.
	 * 
	 * @param symbol
	 *            the symbol to draw
	 * @param graphics
	 *            the graphics context used to draw the arrow.
	 * @param bounds
	 *            the bounds to draw the Symbol within.
	 * @param symbolFont
	 *            the font used to draw the symbol.
	 */
	protected void drawSymbol(String symbol, Graphics graphics, Rectangle bounds, Font symbolFont) {

		graphics.pushState();
		graphics.setFont(symbolFont);

		// determine the extends of the symbol

		Dimension symbolExtents = TextUtilities.INSTANCE.getStringExtents(symbol, symbolFont);
		Point textPosition = bounds.getCenter().translate(-symbolExtents.preciseWidth() / 2.0,
				-symbolExtents.preciseHeight() / 2.0);

		// draw the overlay type symbol

		graphics.drawText(symbol, (int) Math.round(textPosition.preciseX()), (int) Math.round(textPosition.preciseY()));
		graphics.popState();

	}

	/**
	 * draws an arrow pointing at the center of the linked figures combined
	 * bounds.
	 * 
	 * @param graphics
	 *            the graphics context used to draw the arrow.
	 * @param bounds
	 *            the bounds to draw the the arrow within.
	 * @param overlayType
	 *            the overlay type of the figure that the arrow points at.
	 */
	protected void drawArrow(Graphics graphics, PrecisionRectangle bounds, OverlayType overlayType) {

		Point center = new PrecisionPoint(bounds.getCenter());
		Point absoluteCenter = center.getCopy();
		translateToAbsolute(absoluteCenter);

		double arrowLength = (bounds.preciseWidth() + 1) / 2.0;
		double arrowWidth = arrowLength;

		Point linkedFigureCenter = getReferencePoint();

		DoublePrecisionVector direction = new DoublePrecisionVector(new PrecisionPoint(absoluteCenter),
				new PrecisionPoint(linkedFigureCenter)).normalize();

		DoublePrecisionVector arrowTipVector = direction.getMultipliedDoublePrecision(arrowLength);
		Point arrowTip = center.getTranslated(arrowTipVector.x, arrowTipVector.y);

		DoublePrecisionVector arrowRightVector = direction.getRightPerpendicularVectorScreen()
				.getMultipliedDoublePrecision(arrowWidth);
		Point arrowRight = center.getTranslated(arrowRightVector.x, arrowRightVector.y);

		DoublePrecisionVector arrowLeftVector = direction.getLeftPerpendicularVectorScreen()
				.getMultipliedDoublePrecision(arrowWidth);
		Point arrowLeft = center.getTranslated(arrowLeftVector.x, arrowLeftVector.y);

		int[] arrowshape = new int[] { arrowTip.x, arrowTip.y, arrowRight.x, arrowRight.y, arrowLeft.x, arrowLeft.y };

		graphics.pushState();
		graphics.setBackgroundColor(styleAdvisor.getForegroundColorForOverlayType(overlayType));
		graphics.fillPolygon(arrowshape);
		graphics.popState();

	}

	/**
	 * creates the font used for drawing the symbol. Callers are responsible for
	 * correctly disposing the created font.
	 * 
	 * @return a new font based on the assigned font, which should be used to
	 *         draw the smybol at the center of this indicator
	 */
	protected Font createSymbolFont() {
		Font font = getFont();
		return FontDescriptor.createFrom(font).setStyle(SWT.BOLD).increaseHeight(0).createFont(Display.getDefault());

	}

	/**
	 * @return the symbol that is drawn at the center of this indicator.
	 */
	protected abstract String getSymbol();

	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		Font font = createSymbolFont();
		Dimension symbolExtents = TextUtilities.INSTANCE.getStringExtents(getSymbol(), font);
		font.dispose();

		double diameter = Math.max(symbolExtents.preciseWidth(), symbolExtents.preciseHeight());
		diameter += 2 * arrowPadding;
		return new PrecisionDimension(diameter, diameter);
	}

}
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

import at.bitandart.zoubek.mervin.draw2d.figures.ChangeType;
import at.bitandart.zoubek.mervin.draw2d.figures.IChangeTypeStyleAdvisor;

/**
 * A {@link IOffScreenIndicator} that indicates the change type of an off-screen
 * {@link IFigure}. It draws a single character representing the change type in
 * the center, surrounded by a circle. Additionally an arrow points towards the
 * invisible figure. The visibility of this figure depends on the visibility of
 * the combined bounds of the linked figures - if the they intersect the
 * containers bounds the indicator is invisible, otherwise it is visible.
 * 
 * @author Florian Zoubek
 *
 */
public class OffScreenChangeIndicator extends AbstractOffScreenChangeIndicator {

	/**
	 * the change type to indicate.
	 */
	private ChangeType changeType = ChangeType.ADDITION;

	/**
	 * creates a new {@link OffScreenChangeIndicator} that uses the given
	 * {@link IChangeTypeStyleAdvisor} for drawing.
	 * 
	 * @param styleAdvisor
	 */
	public OffScreenChangeIndicator(IChangeTypeStyleAdvisor styleAdvisor) {
		super(styleAdvisor);
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		PrecisionRectangle bounds = new PrecisionRectangle(getBounds());
		ChangeType changeType = getChangeType();

		graphics.setBackgroundColor(styleAdvisor.getForegroundColorForChangeType(changeType));
		Rectangle shrinked = getSymbolBounds(bounds);

		if (linkedFigures != null) {

			drawArrow(graphics, bounds, changeType);

		}

		// draw enclosing circle

		drawSymbolBorder(graphics, shrinked);

		Font symbolFont = createSymbolFont();
		String symbol = getSymbolFor(changeType);

		graphics.setForegroundColor(ColorConstants.white);
		drawSymbol(symbol, graphics, shrinked, symbolFont);

		symbolFont.dispose();

	}

	/**
	 * @return the symbol to draw.
	 */
	@Override
	protected String getSymbol() {
		return getSymbolFor(getChangeType());
	}

	/**
	 * @param changeType
	 * @return the corresponding symbol to the given change type
	 */
	protected String getSymbolFor(ChangeType changeType) {
		switch (changeType) {
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
	 * sets the change type of this indicator.
	 * 
	 * @param changeType
	 */
	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	/**
	 * 
	 * @return the change type of this indicator.
	 */
	public ChangeType getChangeType() {
		return changeType;
	}

}

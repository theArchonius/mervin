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
package at.bitandart.zoubek.mervin.draw2d.figures.offscreen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;

import at.bitandart.zoubek.mervin.draw2d.DoublePrecisionVector;
import at.bitandart.zoubek.mervin.draw2d.figures.ChangeType;
import at.bitandart.zoubek.mervin.draw2d.figures.IChangeTypeStyleAdvisor;

/**
 * Represents an {@link IOffScreenIndicator} that replaces a set of
 * {@link OffScreenChangeIndicator}s. Each merged
 * {@link OffScreenChangeIndicator} must be present in the same figure tree as
 * this indicator (ideally in the same layer) and the container figures of each
 * merged {@link OffScreenChangeIndicator} must be the same. Violation of these
 * constraints may result in unexpected behavior. This class does not update the
 * visibility of the merged indicators, so each class that utilizes this type of
 * indicators must handle the visibility management on their own.
 * 
 * @author Florian Zoubek
 *
 */
public class MergedOffScreenChangeIndicator extends AbstractOffScreenChangeIndicator {

	/**
	 * the set of merged indicators.
	 */
	private Set<OffScreenChangeIndicator> mergedIndicators = new LinkedHashSet<OffScreenChangeIndicator>();

	/**
	 * creates a new {@link MergedOffScreenChangeIndicator} that uses the given
	 * {@link IChangeTypeStyleAdvisor} for drawing.
	 * 
	 * @param styleAdvisor
	 */
	public MergedOffScreenChangeIndicator(IChangeTypeStyleAdvisor styleAdvisor) {
		super(styleAdvisor);
	}

	/**
	 * adds the given indicator to the list of merged indicators.
	 * 
	 * @param indicator
	 *            the indicator to add.
	 */
	public void addIndicator(OffScreenChangeIndicator indicator) {
		if (mergedIndicators.add(indicator)) {
			for (IFigure linkedFigure : indicator.getLinkedFigures()) {
				addLinkedFigure(linkedFigure);
			}
			repaint();
		}
	}

	/**
	 * removes the given indicator from.
	 * 
	 * @param indicator
	 */
	public void removeIndicator(OffScreenChangeIndicator indicator) {

		if (mergedIndicators.remove(indicator)) {
			for (IFigure linkedFigure : indicator.getLinkedFigures()) {
				removeLinkedFigure(linkedFigure);
			}
			repaint();
		}
	}

	/**
	 * @return an unmodifiable copy of the set of merged indicators.
	 */
	public Set<OffScreenChangeIndicator> getMergedIndicators() {
		return Collections.unmodifiableSet(mergedIndicators);
	}

	@Override
	protected void paintFigure(Graphics graphics) {

		super.paintFigure(graphics);

		PrecisionRectangle bounds = new PrecisionRectangle(getBounds());
		Collection<ChangeType> changeTypes = getChangeTypes();
		Font symbolFont = createSymbolFont();
		String symbol = getSymbol();
		int changeTypeIndex = 0;
		double slotAngleRange = 180.0;
		double slotAngle = slotAngleRange / (changeTypes.size() + 1);

		for (ChangeType changeType : changeTypes) {

			graphics.pushState();

			graphics.setBackgroundColor(styleAdvisor.getForegroundColorForChangeType(changeType));

			drawChangeTypeIndicator(graphics, bounds, changeTypeIndex, slotAngleRange, slotAngle);

			/*
			 * use clipping to partially draw the indicator in the change type's
			 * color
			 */

			PrecisionRectangle clip = new PrecisionRectangle(bounds);
			double height = bounds.preciseHeight() / changeTypes.size();
			clip.setPreciseHeight(height);
			clip.translate(0.0, changeTypeIndex * height);
			graphics.clipRect(clip);

			// draw the indicator similar to normal off-screen change indicators

			Rectangle shrinked = getSymbolBounds(bounds);

			if (linkedFigures != null) {
				drawArrow(graphics, bounds, changeType);
			}

			drawSymbolBorder(graphics, shrinked);

			graphics.setForegroundColor(ColorConstants.white);

			drawSymbol(symbol, graphics, shrinked, symbolFont);

			graphics.popState();

			changeTypeIndex++;
		}

		symbolFont.dispose();

	}

	/**
	 * draws a change type indicator in the given slot opposite of the arrow.
	 * Each slot is placed around the symbol border in clockwise order and
	 * occupies at least the given angle. The number of slots is derived from
	 * the given {@code angle} and {@code angleRange} by @{code
	 * floor(angleRange/angle)}. Specifying a slot that exceeds the maximal
	 * results in drawing outside the given range.
	 * 
	 * @param graphics
	 *            the graphics context used to draw.
	 * @param bounds
	 *            the bounds to draw into.
	 * @param slot
	 *            the slot to draw into.
	 * @param angleRange
	 *            the maximum angle range.
	 * @param angle
	 *            the angle each slot occupies.
	 */
	protected void drawChangeTypeIndicator(Graphics graphics, PrecisionRectangle bounds, int slot, double angleRange,
			double angle) {

		/*
		 * Note: the rectangle is expanded by 1 pixel to make sure that fill
		 * operations completely fill the given rectangle as they width x height
		 * pixels but not the enclosing bottom and right side
		 */
		PrecisionRectangle fillingRectangle = new PrecisionRectangle(bounds);
		fillingRectangle.setPreciseWidth(fillingRectangle.preciseWidth() + 1.0);
		fillingRectangle.setPreciseHeight(fillingRectangle.preciseHeight() + 1.0);

		Point center = new PrecisionPoint(fillingRectangle.getCenter());
		Point absoluteCenter = center.getCopy();
		translateToAbsolute(absoluteCenter);

		Point linkedFigureCenter = new PrecisionPoint(getLinkedFiguresBounds().getCenter());

		// opposite direction of the arrow
		DoublePrecisionVector indicatorCenter = new DoublePrecisionVector(new PrecisionPoint(absoluteCenter),
				new PrecisionPoint(linkedFigureCenter)).normalize().negate();

		// rotate it to the correct slot and expand it past the symbol border
		indicatorCenter.rotate(Math.toRadians(angleRange / 2.0 - angle * (slot + 1)))
				.multiply(fillingRectangle.preciseWidth() / 2.0 - arrowPadding + 2);

		// translate it to the center to obtain the real coordinates
		PrecisionPoint centerPoint = indicatorCenter.toPrecisionPoint();
		centerPoint.translate(center);

		// draw the indicator
		graphics.fillOval((int) Math.round(centerPoint.x - arrowPadding / 2.0),
				(int) Math.round(centerPoint.y - arrowPadding / 2.0), (int) arrowPadding, (int) arrowPadding);

	}

	@Override
	protected String getSymbol() {

		// the symbol is the number of visible merged indicators

		int visibleIndicators = 0;
		for (OffScreenChangeIndicator indicator : mergedIndicators) {
			if (!indicator.areLinkedFiguresVisible()) {
				visibleIndicators++;
			}
		}

		return String.valueOf(visibleIndicators);

	}

	/**
	 * @return a list of change types that occur in the set of merged
	 *         {@link OffScreenChangeIndicator}s without duplicates.
	 */
	private List<ChangeType> getChangeTypes() {

		List<ChangeType> changeTypes = new ArrayList<ChangeType>(ChangeType.values().length);

		for (OffScreenChangeIndicator indicator : mergedIndicators) {
			if (!indicator.areLinkedFiguresVisible()) {
				ChangeType changeType = indicator.getChangeType();
				if (!changeTypes.contains(changeType)) {
					changeTypes.add(changeType);
				}
			}
		}

		return changeTypes;

	}

}

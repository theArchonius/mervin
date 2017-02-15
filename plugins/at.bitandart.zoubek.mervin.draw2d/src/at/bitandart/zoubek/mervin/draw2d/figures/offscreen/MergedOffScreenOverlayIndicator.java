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
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.swt.graphics.Font;

import at.bitandart.zoubek.mervin.draw2d.DoublePrecisionVector;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.OverlayType;

/**
 * Represents an {@link IOffScreenIndicator} that replaces a set of
 * {@link OffScreenOverlayIndicator}s. Each merged
 * {@link OffScreenOverlayIndicator} must be present in the same figure tree as
 * this indicator (ideally in the same layer) and the container figures of each
 * merged {@link OffScreenOverlayIndicator} must be the same. Violation of these
 * constraints may result in unexpected behavior. This class does not update the
 * visibility of the merged indicators, so each class that utilizes this type of
 * indicators must handle the visibility management on their own.
 * 
 * @author Florian Zoubek
 *
 */
public class MergedOffScreenOverlayIndicator extends AbstractOffScreenOverlayIndicator {

	/**
	 * the set of merged indicators.
	 */
	private Set<OffScreenOverlayIndicator> mergedIndicators = new LinkedHashSet<OffScreenOverlayIndicator>();

	/**
	 * creates a new {@link MergedOffScreenOverlayIndicator} that uses the given
	 * {@link IOverlayTypeStyleAdvisor} for drawing.
	 * 
	 * @param styleAdvisor
	 */
	public MergedOffScreenOverlayIndicator(IOverlayTypeStyleAdvisor styleAdvisor) {
		super(styleAdvisor);
	}

	/**
	 * adds the given indicator to the list of merged indicators.
	 * 
	 * @param indicator
	 *            the indicator to add.
	 */
	public void addIndicator(OffScreenOverlayIndicator indicator) {
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
	public void removeIndicator(OffScreenOverlayIndicator indicator) {

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
	public Set<OffScreenOverlayIndicator> getMergedIndicators() {
		return Collections.unmodifiableSet(mergedIndicators);
	}

	@Override
	protected void paintFigure(Graphics graphics) {

		super.paintFigure(graphics);

		PrecisionRectangle bounds = new PrecisionRectangle(getBounds());
		Collection<OverlayType> overlayTypes = getOverlayTypes();
		Font symbolFont = createSymbolFont();
		String symbol = getSymbol();
		int overlayTypeIndex = 0;
		double slotAngleRange = 180.0;
		double slotAngle = slotAngleRange / (overlayTypes.size() + 1);

		for (OverlayType overlayType : overlayTypes) {

			graphics.pushState();

			graphics.setBackgroundColor(styleAdvisor.getForegroundColorForOverlayType(overlayType));

			drawOverlayTypeIndicator(graphics, bounds, overlayTypeIndex, slotAngleRange, slotAngle);

			/*
			 * use clipping to partially draw the indicator in the overlay
			 * type's color
			 */

			PrecisionRectangle clip = new PrecisionRectangle(bounds);
			double height = bounds.preciseHeight() / overlayTypes.size();
			clip.setPreciseHeight(height);
			clip.translate(0.0, overlayTypeIndex * height);
			graphics.clipRect(clip);

			// draw the indicator similar to normal off-screen change indicators

			Rectangle shrinked = getSymbolBounds(bounds);

			if (linkedFigures != null) {
				drawArrow(graphics, bounds, overlayType);
			}

			drawSymbolBorder(graphics, shrinked);

			graphics.setForegroundColor(ColorConstants.white);

			drawSymbol(symbol, graphics, shrinked, symbolFont);

			graphics.popState();

			overlayTypeIndex++;
		}

		symbolFont.dispose();

	}

	/**
	 * draws a overlay type indicator in the given slot opposite of the arrow.
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
	protected void drawOverlayTypeIndicator(Graphics graphics, PrecisionRectangle bounds, int slot, double angleRange,
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

		Point linkedFigureCenter = getReferencePoint();

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
		for (OffScreenOverlayIndicator indicator : mergedIndicators) {
			if (!indicator.areLinkedFiguresVisible()) {
				visibleIndicators++;
			}
		}

		return String.valueOf(visibleIndicators);

	}

	/**
	 * @return a list of overlay types that occur in the set of merged
	 *         {@link OffScreenOverlayIndicator}s without duplicates.
	 */
	private List<OverlayType> getOverlayTypes() {

		List<OverlayType> overlayTypes = new ArrayList<OverlayType>(OverlayType.values().length);

		for (OffScreenOverlayIndicator indicator : mergedIndicators) {
			if (!indicator.areLinkedFiguresVisible()) {
				OverlayType overlayType = indicator.getOverlayType();
				if (!overlayTypes.contains(overlayType)) {
					overlayTypes.add(overlayType);
				}
			}
		}

		return overlayTypes;

	}

	@Override
	public boolean areLinkedFiguresVisible() {

		for (OffScreenOverlayIndicator indicator : mergedIndicators) {
			if (!indicator.areLinkedFiguresVisible()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public PrecisionPoint getReferencePoint() {

		DoublePrecisionVector refPointVector = new DoublePrecisionVector(0, 0);
		int numRefPoints = 0;
		Vector cache = new Vector(0, 0);

		for (OffScreenOverlayIndicator indicator : mergedIndicators) {

			if (!indicator.areLinkedFiguresVisible()) {

				PrecisionPoint inidcatorReferencePoint = indicator.getReferencePoint();
				cache.x = inidcatorReferencePoint.preciseX();
				cache.y = inidcatorReferencePoint.preciseY();
				refPointVector.add(cache);

				numRefPoints++;
			}
		}

		if (numRefPoints > 0) {
			refPointVector.divide(numRefPoints);
		}

		return refPointVector.toPrecisionPoint();
	}

}

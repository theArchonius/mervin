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
package at.bitandart.zoubek.mervin.draw2d.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Vector;

/**
 * A figure that shows an arrow which points in the specified direction. This
 * Indicator is not intended to have child figures. The arrow is drawn using the
 * foreground color.
 * 
 * @author Florian Zoubek
 *
 */
public class DirectionIndicator extends Figure {

	/**
	 * the direction vector for this indicator
	 */
	private Vector direction;

	/**
	 * the default direction vector for rotation angle 0
	 */
	private static final Vector DEFAULT_DIRECTION = new Vector(1.0, 0.0);

	/**
	 * creates a new {@link DirectionIndicator} pointing in the given direction.
	 * 
	 * @param direction
	 *            the direction vector
	 * 
	 * @throws IllegalArgumentException
	 *             if the length of the direction vector is smaller than 0
	 */
	public DirectionIndicator(Vector direction) {
		if (direction.getLength() <= 0) {
			throw new IllegalArgumentException("The direction vector length must be bigger than 0");
		}
		this.direction = direction;
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		Rectangle bounds = getBounds().getCopy().shrink(getInsets());

		double diameter = Math.min(bounds.preciseWidth(), bounds.preciseHeight());
		double radius = diameter / 2.0;
		Point center = bounds.getCenter();

		graphics.pushState();
		graphics.translate(center);

		double angleDeg = direction.getAngle(DEFAULT_DIRECTION);
		graphics.rotate((float) angleDeg * (direction.y < 0.0 ? -1 : 1));

		// derive the angle using the golden ratio
		double p1Angle = Math.toRadians(180 + 0.618 * 45);
		double p2Angle = Math.toRadians(180 - 0.618 * 45);

		graphics.setBackgroundColor(graphics.getForegroundColor());

		/*
		 * points defining the arrow starting with the upper left point
		 */
		int[] arrowPoints = new int[] {
				/* upper left */
				(int) (Math.cos(p1Angle) * radius), (int) (Math.sin(p1Angle) * radius),
				/* arrow tip */
				(int) (Math.cos(p1Angle) * -radius), 0,
				/* lower left */
				(int) (Math.cos(p2Angle) * radius), (int) (Math.sin(p2Angle) * radius),
				/* arrow left end */
				(int) (Math.cos(p1Angle) * radius * 0.618), 0 };

		graphics.fillPolygon(arrowPoints);

		graphics.popState();

	}

	/**
	 * sets the direction of this indicator
	 * 
	 * @param direction
	 *            the direction vector
	 * @throws IllegalArgumentException
	 *             if the length of the direction vector is smaller than 0
	 */
	public void setDirection(Vector direction) {
		if (direction.getLength() <= 0) {
			throw new IllegalArgumentException("The direction vector length must be bigger than 0");
		}
		this.direction = direction;
		repaint();
	}

	/**
	 * @return a copy of the current direction vector of this indicator.
	 */
	public Vector getDirection() {
		return new Vector(direction.x, direction.y);
	}
}

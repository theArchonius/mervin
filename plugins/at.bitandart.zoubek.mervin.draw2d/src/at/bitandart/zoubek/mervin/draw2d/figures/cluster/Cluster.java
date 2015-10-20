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
package at.bitandart.zoubek.mervin.draw2d.figures.cluster;

import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Vector;

import com.google.common.collect.Lists;

/**
 * A cluster inside a {@link ClusterView}. A cluster holds a set of the
 * descendants inside clustered layer of a {@link ClusterView}.
 * 
 * @author Florian Zoubek
 *
 */
public class Cluster extends LinkedHashSet<IFigure> {

	private static final long serialVersionUID = 8083229281038816106L;

	/**
	 * the location of this cluster
	 */
	private Point location = new PrecisionPoint();

	/**
	 * @return the location of the cluster. The location is defined as the top
	 *         left point of the rectangle that covers this cluster completely.
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @return a number of rectangles describing the clipping area for this
	 *         cluster in <b>absolute</b> coordinates.
	 */
	public Rectangle[] getClippingArea() {
		return new Rectangle[] { getRealBounds() };
	}

	/**
	 * @return the bounds of this cluster.
	 */
	public Rectangle getBounds() {
		return getRealBounds().setLocation(location);
	}

	/**
	 * @return a copy of the real rectangular boundary based on the figures of
	 *         this cluster.
	 */
	public Rectangle getRealBounds() {

		Rectangle bounds = null;
		Rectangle boundsCache = new PrecisionRectangle();

		for (IFigure child : this) {

			boundsCache.setBounds(child.getBounds()).expand(10, 10);
			child.translateToAbsolute(boundsCache);

			if (bounds == null) {
				bounds = new PrecisionRectangle(boundsCache);
			} else {
				bounds.union(boundsCache);
			}
		}

		if (bounds == null) {
			bounds = new PrecisionRectangle();
		}
		return bounds;

	}

	/**
	 * sets the location of this cluster. The location is defined as the top
	 * left point of the rectangle that covers this cluster completely.
	 * 
	 * @param location
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * @return the containing child figures as list
	 */
	public List<IFigure> asList() {
		return Lists.newArrayList(this);
	}

	/**
	 * @return the offset vector between the location of the rectangular
	 *         boundary for all contained figures and the location of the
	 *         cluster.
	 */
	public Vector getTranslationOffset() {
		return new Vector(new PrecisionPoint(getRealBounds().getLocation()), new PrecisionPoint(location));
	}

}
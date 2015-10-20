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

import java.util.List;

import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A simple {@link IClusterLayoutManager} that arranges clusters along a single
 * axis, top aligned ({@link Axis#HORIZONTAL}) or left aligned (
 * {@link Axis#VERTICAL}) ).
 * 
 * @author Florian Zoubek
 *
 */
public class SingleAxisClusterLayout implements IClusterLayoutManager {

	/**
	 * the layout axis
	 */
	private Axis axis = Axis.HORIZONTAL;

	/**
	 * the spacing between the clusters
	 */
	private double spacing = 10.0f;

	public enum Axis {
		HORIZONTAL, VERTICAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.draw2d.figures.cluster.IClusterLayoutManager#
	 * layout(at.bitandart.zoubek.mervin.draw2d.figures.cluster.
	 * ClusterViewFigure)
	 */
	@Override
	public void layout(ClusterView clusterViewFigure) {

		PrecisionPoint refPoint = new PrecisionPoint();
		List<Cluster> clusters = clusterViewFigure.getClusters();

		for (Cluster cluster : clusters) {
			cluster.setLocation(refPoint.getCopy());
			Rectangle realBounds = cluster.getRealBounds();
			if (axis == Axis.HORIZONTAL) {
				refPoint.translate(realBounds.width + spacing, 0.0);
			} else {
				refPoint.translate(0.0, realBounds.height + spacing);
			}
		}

	}

	/**
	 * @param axis
	 *            the layout axis.
	 */
	public void setAxis(Axis axis) {
		this.axis = axis;
	}

	/**
	 * @return the layout axis.
	 */
	public Axis getAxis() {
		return axis;
	}

	/**
	 * sets the spacing between the clusters.
	 * 
	 * @param spacing
	 */
	public void setSpacing(double spacing) {
		this.spacing = spacing;
	}

	/**
	 * @return the current spacing between the clusters.
	 */
	public double getSpacing() {
		return spacing;
	}

}

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

/**
 * Base interface for classes that manage the positions of {@link Cluster}s in
 * {@link ClusterView}s.
 * 
 * @author Florian Zoubek
 *
 */
public interface IClusterLayoutManager {

	/**
	 * layouts the given cluster view's clusters.
	 * 
	 * @param clusterView
	 *            the cluster view to layout
	 */
	public void layout(ClusterView clusterView);

}

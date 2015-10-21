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
package at.bitandart.zoubek.mervin.draw2d.figures.examples;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ScrollPaneLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemsAwareFreeFormLayer;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;
import org.eclipse.gmf.runtime.diagram.ui.util.MeasurementUnitHelper;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimatableScrollPane;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScalableFreeformLayeredPane;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;

import at.bitandart.zoubek.mervin.draw2d.figures.cluster.ClusterView;
import at.bitandart.zoubek.mervin.draw2d.figures.cluster.SingleAxisClusterLayout;

/**
 * Demonstrates the usage of {@link ClusterView} inside a {@link ScrollPane}.
 * 
 * @author Florian Zoubek
 *
 */
@SuppressWarnings("restriction")
public class ClusterViewScrollpaneExample extends ClusterViewExample {

	public static void main(String[] args) {
		new ClusterViewScrollpaneExample().run();
	}

	@Override
	protected IFigure createRootFigure() {

		FreeformViewport mainViewport = new FreeformViewport();

		ScalableFreeformLayeredPane mainPane = new ScalableFreeformLayeredPane(
				MeasurementUnitHelper.getMapMode(MeasurementUnit.PIXEL_LITERAL));
		mainViewport.setContents(mainPane);

		BorderItemsAwareFreeFormLayer layer = new BorderItemsAwareFreeFormLayer();
		layer.setLayoutManager(new FreeFormLayoutEx());
		mainPane.add(layer, LayerConstants.PRIMARY_LAYER);

		// create the cluster view itself, it already contains a set of layers
		ClusterView clusterView = new ClusterView();

		/*
		 * set the desired layout manager for the clusters, if no layout manager
		 * is set, the cluster locations must be set manually
		 */
		clusterView.setClusterLayoutManager(new SingleAxisClusterLayout());

		// create a viewport that contains the cluster view
		FreeformViewport freeformViewport = new FreeformViewport();

		// add the cluster view to the viewport and initialize it
		freeformViewport.setBackgroundColor(ColorConstants.white);
		freeformViewport.setOpaque(true);

		ScrollPane scrollpane = new AnimatableScrollPane();
		scrollpane.setViewport(freeformViewport);
		scrollpane.setScrollBarVisibility(ScrollPane.ALWAYS);
		scrollpane.setLayoutManager(new ScrollPaneLayout());
		scrollpane.setContents(clusterView);

		layer.add(scrollpane, new Rectangle(0, 0, 300, 300));

		return mainViewport;
	}

	@Override
	protected ClusterView getClusterView(IFigure parentFigure) {
		if (parentFigure == null) {
			return null;
		}
		if (parentFigure instanceof ClusterView) {
			return (ClusterView) parentFigure;
		}
		for (Object child : parentFigure.getChildren()) {
			if (child instanceof IFigure) {
				ClusterView clusterView = getClusterView((IFigure) child);
				if (clusterView != null) {
					return clusterView;
				}
			}
		}
		return null;
	}

	@Override
	protected int getCanvasHeight() {
		return 300;
	}

	@Override
	protected int getCanvasWidth() {
		return 300;
	}

}

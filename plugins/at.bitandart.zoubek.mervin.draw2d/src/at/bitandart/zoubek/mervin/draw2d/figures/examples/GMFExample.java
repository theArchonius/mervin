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

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemsAwareFreeFormLayer;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;
import org.eclipse.gmf.runtime.diagram.ui.util.MeasurementUnitHelper;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScalableFreeformLayeredPane;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;

/**
 * Base class for examples that use a root figures similar to GMF using
 * {@link MeasurementUnit#PIXEL_LITERAL}.
 * 
 * @author Florian Zoubek
 *
 */
@SuppressWarnings("restriction")
public class GMFExample extends BaseExample {

	private FreeformViewport mainViewport;

	private ScalableFreeformLayeredPane mainPane;

	public GMFExample() {
		super();
	}

	@Override
	protected IFigure createRootFigure() {

		mainViewport = createFreeformViewport();

		mainPane = new ScalableFreeformLayeredPane(MeasurementUnitHelper.getMapMode(MeasurementUnit.PIXEL_LITERAL));
		mainViewport.setContents(mainPane);

		BorderItemsAwareFreeFormLayer primarylayer = new BorderItemsAwareFreeFormLayer();
		primarylayer.setLayoutManager(new FreeFormLayoutEx());
		mainPane.add(primarylayer, LayerConstants.PRIMARY_LAYER);

		ConnectionLayer connectionLayer = new ConnectionLayer();
		connectionLayer.setLayoutManager(new FreeFormLayoutEx());
		mainPane.add(connectionLayer, LayerConstants.CONNECTION_LAYER);

		return mainViewport;

	}

	/**
	 * @return the {@link FreeformViewport} to use as main viewport
	 */
	protected FreeformViewport createFreeformViewport() {
		return new FreeformViewport();
	}

	/**
	 * @return the main viewport.
	 */
	public FreeformViewport getMainViewport() {
		return mainViewport;
	}

	/**
	 * @param layerId
	 * @return the layer with the given {@code layerId} of the main pane or null
	 *         if the layer does not exist.
	 */
	protected Layer getLayer(Object layerId) {
		return mainPane.getLayer(layerId);
	}

	/**
	 * @return the primary layer of the main pane.
	 */
	protected Layer getPrimaryLayer() {
		return getLayer(LayerConstants.PRIMARY_LAYER);
	}

	/**
	 * @return the connection layer of the main pane.
	 */
	protected Layer getConnectionLayer() {
		return getLayer(LayerConstants.CONNECTION_LAYER);
	}

	/**
	 * @return the map mode for this example
	 */
	protected IMapMode getMapMode() {
		return MeasurementUnitHelper.getMapMode(MeasurementUnit.PIXEL_LITERAL);
	}

}
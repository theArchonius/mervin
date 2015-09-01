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
package at.bitandart.zoubek.mervin.draw2d.figures.workbench;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ScrollPaneLayout;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemsAwareFreeFormLayer;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScalableFreeformLayeredPane;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.tooling.runtime.linklf.LinkLFShapeCompartmentEditPart;

import at.bitandart.zoubek.mervin.draw2d.MervinLayerConstants;

/**
 * An {@link IDiffWorkbenchContainer} implementation that shows the contents of
 * a single diagram.
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramContainerFigure extends LinkLFShapeCompartmentEditPart.ShapeCompartmentFigureEx
		implements IDiffWorkbenchContainer {

	IDiffWorkbenchTrayFigure trayFigure;

	IDiffWorkbenchWindowTitleFigure windowTitleFigure;

	public DiagramContainerFigure(String compartmentTitle, IMapMode mm) {
		super(compartmentTitle, mm);
		remove(getTextPane());
		ScrollPane scrollPane = getScrollPane();
		scrollPane.setLayoutManager(new ScrollPaneLayout());

		// TODO find some better way than using an internal GMF class
		ScalableFreeformLayeredPane layeredPane = new ScalableFreeformLayeredPane(mm);
		IFigure primaryLayer = new BorderItemsAwareFreeFormLayer();
		primaryLayer.setLayoutManager(new FreeFormLayoutEx());
		layeredPane.add(primaryLayer, LayerConstants.PRIMARY_LAYER);
		IFigure connectionlayer = new ConnectionLayerEx();
		layeredPane.add(connectionlayer, LayerConstants.CONNECTION_LAYER);

		FreeformLayer overlayLayer = new FreeformLayer();
		overlayLayer.setLayoutManager(new DelegatingLayout());
		layeredPane.add(overlayLayer, MervinLayerConstants.DIFF_HIGHLIGHT_LAYER);
		scrollPane.setContents(layeredPane);

		setBorder(new LineBorder(ColorConstants.lightGray, 2));
	}

	@Override
	public String getWindowTitle() {
		return getCompartmentTitle();
	}

	@Override
	public IFigure getStatusArea() {
		// TODO implement status area
		return null;
	}

	@Override
	public IFigure getToolbarArea() {
		// TODO implement toolbar area
		return null;
	}

	@Override
	public IFigure getContentPane() {
		IFigure contents = getScrollPane().getContents();
		if (contents instanceof LayeredPane) {
			return ((LayeredPane) contents).getLayer(LayerConstants.PRIMARY_LAYER);
		}
		return super.getContentPane();
	}

	public Layer getLayer(Object key) {
		IFigure contents = getScrollPane().getContents();
		if (contents instanceof LayeredPane) {
			return ((LayeredPane) contents).getLayer(key);
		}
		return null;
	}

	@Override
	public IFigure getContentArea() {
		return getContentPane();
	}

	@Override
	public IDiffWorkbenchTrayFigure getTrayFigure() {
		if (trayFigure == null) {
			trayFigure = createTrayFigure();
		}
		return trayFigure;
	}

	/**
	 * Creates the associated {@link IDiffWorkbenchTrayFigure} for this figure.
	 */
	protected IDiffWorkbenchTrayFigure createTrayFigure() {
		return new TitleTrayFigure(this);
	}

	@Override
	public IDiffWorkbenchWindowTitleFigure getWindowTitleFigure() {
		if (windowTitleFigure == null) {
			windowTitleFigure = createWindowTitleFigure();
		}
		return windowTitleFigure;
	}

	/**
	 * Creates the associated {@link IDiffWorkbenchWindowTitleFigure} for this
	 * figure.
	 */
	protected IDiffWorkbenchWindowTitleFigure createWindowTitleFigure() {
		return new LabelWindowTitle(this);
	}

}

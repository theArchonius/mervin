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
package at.bitandart.zoubek.mervin.draw2d.figures.examples;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
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

import at.bitandart.zoubek.mervin.draw2d.MervinLayerConstants;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.IOffScreenIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.OffScreenIndicatorLayout;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.OffScreenOverlayIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.OffScreenOverlayIndicatorMerger;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.DefaultOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.OverlayType;

/**
 * Demonstrates the usage of {@link IOffScreenIndicator}s.
 * 
 * @author Florian Zoubek
 *
 */
public class OffScreenChangeIndicatorExample extends BaseExample {
	private IOverlayTypeStyleAdvisor styleAdvisor;

	public static void main(String[] args) {
		new OffScreenChangeIndicatorExample().run();
	}

	@Override
	protected IFigure createRootFigure() {

		// create the main viewport and layer

		FreeformViewport mainViewport = new FreeformViewport();
		mainViewport.setBackgroundColor(ColorConstants.white);
		mainViewport.setOpaque(true);

		ScalableFreeformLayeredPane mainPane = new ScalableFreeformLayeredPane(
				MeasurementUnitHelper.getMapMode(MeasurementUnit.PIXEL_LITERAL));
		mainViewport.setContents(mainPane);

		BorderItemsAwareFreeFormLayer mainLayer = new BorderItemsAwareFreeFormLayer();
		mainLayer.setLayoutManager(new FreeFormLayoutEx());
		mainPane.add(mainLayer, LayerConstants.PRIMARY_LAYER);

		// create the scrollpane and its layers which hold the content
		FreeformViewport freeformViewport = new FreeformViewport();

		ScrollPane scrollpane = new AnimatableScrollPane();
		scrollpane.setViewport(freeformViewport);
		scrollpane.setScrollBarVisibility(ScrollPane.ALWAYS);
		scrollpane.setLayoutManager(new ScrollPaneLayout());

		// create the layers

		FreeformLayeredPane contentPane = new FreeformLayeredPane();

		BorderItemsAwareFreeFormLayer primaryLayer = new BorderItemsAwareFreeFormLayer();
		primaryLayer.setLayoutManager(new FreeFormLayoutEx());
		contentPane.add(primaryLayer, LayerConstants.PRIMARY_LAYER);

		/* indicators must be added to another layer above the content */

		BorderItemsAwareFreeFormLayer indicatorLayer = new BorderItemsAwareFreeFormLayer();
		indicatorLayer.setLayoutManager(new OffScreenIndicatorLayout());
		contentPane.add(indicatorLayer, MervinLayerConstants.DIFF_INDICATOR_LAYER);

		scrollpane.setContents(contentPane);

		mainLayer.add(scrollpane, new Rectangle(0, 0, getCanvasWidth(), getCanvasHeight()));

		return mainViewport;
	}

	@Override
	protected void addChildFigures(IFigure parentFigure) {

		ScrollPane contentScrollPane = getContentScrollPane(parentFigure);
		Layer contentLayer = getContentLayer(contentScrollPane);
		Layer indicatorLayer = getIndicatorLayer(contentScrollPane);

		styleAdvisor = new DefaultOverlayTypeStyleAdvisor();

		if (contentLayer != null && indicatorLayer != null) {

			OffScreenOverlayIndicatorMerger merger = new OffScreenOverlayIndicatorMerger(indicatorLayer, styleAdvisor);
			indicatorLayer.addLayoutListener(merger);

			// left

			IFigure leftFigure = createFigureFor(OverlayType.ADDITION, contentScrollPane, contentLayer, indicatorLayer,
					styleAdvisor, merger);
			contentLayer.setConstraint(leftFigure, new Rectangle(0 - getCanvasWidth(), getCanvasHeight() / 2, 150, 50));

			IFigure leftFigure2 = createFigureFor(OverlayType.ADDITION, contentScrollPane, contentLayer, indicatorLayer,
					styleAdvisor, merger);
			contentLayer.setConstraint(leftFigure2,
					new Rectangle(0 - getCanvasWidth() + 150, getCanvasHeight() / 2, 150, 50));

			// right

			IFigure rightFigure = createFigureFor(OverlayType.DELETION, contentScrollPane, contentLayer, indicatorLayer,
					styleAdvisor, merger);
			contentLayer.setConstraint(rightFigure,
					new Rectangle(getCanvasWidth() * 2 - 150, (getCanvasHeight()) / 2, 150, 50));

			IFigure rightFigure2 = createFigureFor(OverlayType.ADDITION, contentScrollPane, contentLayer,
					indicatorLayer, styleAdvisor, merger);
			contentLayer.setConstraint(rightFigure2,
					new Rectangle(getCanvasWidth() * 2 - 150 * 2, (getCanvasHeight()) / 2, 150, 50));

			IFigure rightFigure3 = createFigureFor(OverlayType.MODIFICATION, contentScrollPane, contentLayer,
					indicatorLayer, styleAdvisor, merger);
			contentLayer.setConstraint(rightFigure3,
					new Rectangle(getCanvasWidth() * 2 - 150 * 3, (getCanvasHeight()) / 2, 150, 50));

			IFigure rightFigure4 = createFigureFor(OverlayType.LAYOUT, contentScrollPane, contentLayer, indicatorLayer,
					styleAdvisor, merger);
			contentLayer.setConstraint(rightFigure4,
					new Rectangle(getCanvasWidth() * 2 - 150 * 4, (getCanvasHeight()) / 2, 150, 50));

			// top

			IFigure topFigure = createFigureFor(OverlayType.MODIFICATION, contentScrollPane, contentLayer,
					indicatorLayer, styleAdvisor, merger);
			contentLayer.setConstraint(topFigure,
					new Rectangle((getCanvasWidth() - 100) / 2, 0 - getCanvasHeight(), 150, 50));

			// bottom

			IFigure bottomFigure = createFigureFor(OverlayType.LAYOUT, contentScrollPane, contentLayer, indicatorLayer,
					styleAdvisor, merger);
			contentLayer.setConstraint(bottomFigure,
					new Rectangle((getCanvasWidth() - 100) / 2, getCanvasHeight() * 2 - 50, 150, 50));

		}

	}

	/**
	 * creates a figure and associates an {@link OffScreenOverlayIndicator} with
	 * the given overlay type to it.
	 * 
	 * @param overlayType
	 *            the overlay type of the {@link OffScreenOverlayIndicator}.
	 * @param contentScrollPane
	 *            the {@link ScrollPane} containing the content.
	 * @param contentLayer
	 *            the content layer to add the figure to.
	 * @param indicatorLayer
	 *            the indicator layer to add the indicator to.
	 * @param styleAdvisor
	 *            the {@link IOverlayTypeStyleAdvisor} to use.
	 * @param merger
	 *            the indicator merger instance to use.
	 * @return the created figure.
	 */
	private IFigure createFigureFor(OverlayType overlayType, ScrollPane contentScrollPane, Layer contentLayer,
			Layer indicatorLayer, IOverlayTypeStyleAdvisor styleAdvisor, OffScreenOverlayIndicatorMerger merger) {

		Label figure = new Label(overlayType.name());
		figure.setTextAlignment(PositionConstants.CENTER);
		figure.setBorder(new LineBorder());
		contentLayer.add(figure);

		OffScreenOverlayIndicator indicator = new OffScreenOverlayIndicator(styleAdvisor);
		indicator.addLinkedFigure(figure);
		indicator.setContainerFigure(contentScrollPane.getViewport());
		indicator.setOverlayType(overlayType);
		merger.registerIndicator(indicator);
		indicatorLayer.add(indicator);

		return figure;
	}

	/**
	 * finds the content scroll pane contained in the given figure tree.
	 * 
	 * @param figure
	 *            the root figure of the figure tree the search through.
	 * @return the content scroll pane if it exists, null otherwise.
	 */
	private ScrollPane getContentScrollPane(IFigure figure) {

		if (figure instanceof ScrollPane) {
			return (ScrollPane) figure;
		}
		for (Object child : figure.getChildren()) {
			if (child instanceof IFigure) {
				ScrollPane contentScrollPane = getContentScrollPane((IFigure) child);
				if (contentScrollPane != null) {
					return contentScrollPane;
				}
			}
		}

		return null;
	}

	/**
	 * finds the content layer of the given scrollpane.
	 * 
	 * @param scrollpane
	 *            the containing scrollpane (null values allowed).
	 * @return the content layer of the given scrollpane or null, if none can be
	 *         found or the given scrollpane is null.
	 */
	private Layer getContentLayer(ScrollPane scrollpane) {
		if (scrollpane != null) {
			IFigure contents = scrollpane.getContents();
			if (contents instanceof LayeredPane) {
				return ((LayeredPane) contents).getLayer(LayerConstants.PRIMARY_LAYER);
			}
		}
		return null;
	}

	/**
	 * finds the content layer of the given scrollpane.
	 * 
	 * @param scrollpane
	 *            the containing scrollpane (null values allowed).
	 * @return the content layer of the given scrollpane or null, if none can be
	 *         found or the given scrollpane is null.
	 */
	private Layer getIndicatorLayer(ScrollPane scrollpane) {
		if (scrollpane != null) {
			IFigure contents = scrollpane.getContents();
			if (contents instanceof LayeredPane) {
				return ((LayeredPane) contents).getLayer(MervinLayerConstants.DIFF_INDICATOR_LAYER);
			}
		}
		return null;
	}

	@Override
	protected void cleanUp() {
		styleAdvisor.dispose();
		super.cleanUp();
	}

	@Override
	protected int getCanvasHeight() {
		return 600;
	}

	@Override
	protected int getCanvasWidth() {
		return 600;
	}
}

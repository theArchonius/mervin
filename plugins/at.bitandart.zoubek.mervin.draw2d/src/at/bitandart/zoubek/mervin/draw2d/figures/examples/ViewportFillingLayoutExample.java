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
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.RectangleFigure;

import at.bitandart.zoubek.mervin.draw2d.ViewportFillingLayout;

/**
 * Demonstrates the usage of {@link ViewportFillingLayout}.
 * 
 * @author Florian Zoubek
 *
 */
public class ViewportFillingLayoutExample extends GMFExample {

	public static void main(String[] args) {
		new ViewportFillingLayoutExample().run();
	}

	@Override
	protected void addChildFigures(IFigure parentFigure) {

		final Layer primaryLayer = getPrimaryLayer();

		getMainViewport().addFigureListener(new FigureListener() {

			@Override
			public void figureMoved(IFigure source) {
				primaryLayer.revalidate();
			}
		});

		primaryLayer.setLayoutManager(new ViewportFillingLayout());
		RectangleFigure rectangleFigure = new RectangleFigure();
		rectangleFigure.setOpaque(true);
		rectangleFigure.setBackgroundColor(ColorConstants.lightBlue);
		primaryLayer.add(rectangleFigure);

	}

	@Override
	protected FreeformViewport createFreeformViewport() {
		/*
		 * for some reason the scrollbars also appear even if no scrolling is
		 * possible/necessary, so deactivate them for the figure canvas
		 */
		getCanvas().setScrollBarVisibility(FigureCanvas.NEVER);
		return super.createFreeformViewport();
	}
}

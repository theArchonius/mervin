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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;

import at.bitandart.zoubek.mervin.draw2d.figures.DefaultOverlayLocator;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.OverlayConnectionFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.OverlayLinkedFigureListener;
import at.bitandart.zoubek.mervin.draw2d.figures.OverlayType;

/**
 * Demonstrates the usage of {@link OverlayConnectionFigure}.
 * 
 * @author Florian Zoubek
 *
 */
public class ChangeOverlayConnectionFiguresExample extends BaseExample {

	private static final String PRIMARY_LAYER = "primary layer";
	private static final String CONNECTION_LAYER = "connection layer";
	private static final String OVERLAY_LAYER = "overlay layer";
	private DefaultOverlayTypeStyleAdvisor styleAdvisor;

	public static void main(String[] args) {
		new ChangeOverlayConnectionFiguresExample().run();
	}

	@Override
	protected IFigure createRootFigure() {

		LayeredPane layeredPane = new LayeredPane();

		/*
		 * the primary layer where all linked figures reside
		 */

		Layer primaryLayer = new Layer();
		primaryLayer.setOpaque(true);
		primaryLayer.setBackgroundColor(org.eclipse.draw2d.ColorConstants.white);
		primaryLayer.setLayoutManager(new XYLayout());
		layeredPane.add(primaryLayer, PRIMARY_LAYER);

		Layer connectionLayer = new ConnectionLayerEx();
		layeredPane.add(connectionLayer, CONNECTION_LAYER);

		/*
		 * Overlay Figures should always be placed on a overlay layer with a
		 * delegating layout (or any layout that calls
		 * IOverlayFigure#updateBoundsfromLinkedFigures()) that is placed above
		 * the layer with the linked figures.
		 */

		Layer overlayLayer = new Layer();
		overlayLayer.setLayoutManager(new DelegatingLayout());
		layeredPane.add(overlayLayer, OVERLAY_LAYER);

		return layeredPane;
	}

	@Override
	protected void addChildFigures(IFigure parentFigure) {

		if (parentFigure instanceof LayeredPane) {

			LayeredPane layeredPane = (LayeredPane) parentFigure;
			Layer primaryLayer = layeredPane.getLayer(PRIMARY_LAYER);
			Layer connectionLayer = layeredPane.getLayer(CONNECTION_LAYER);
			Layer overlayLayer = layeredPane.getLayer(OVERLAY_LAYER);

			styleAdvisor = new DefaultOverlayTypeStyleAdvisor();

			OverlayLinkedFigureListener overlayLinkedFigureListener = new OverlayLinkedFigureListener(overlayLayer);

			Point offsets = new Point(30, 30);
			Rectangle drawingArea = new Rectangle();
			drawingArea.setLocation(offsets);
			drawingArea.setSize(100, 100);
			for (OverlayType overlayType : OverlayType.values()) {

				addSimpleConnection(overlayType, drawingArea, primaryLayer, connectionLayer, overlayLayer,
						overlayLinkedFigureListener, false);
				drawingArea.translate(drawingArea.width + offsets.x, 0);

				addRectRoutedConnection(overlayType, drawingArea, primaryLayer, connectionLayer, overlayLayer,
						overlayLinkedFigureListener, false);
				drawingArea.translate(drawingArea.width + offsets.x, 0);

				addCustomRoutedConnection(overlayType, drawingArea, primaryLayer, connectionLayer, overlayLayer,
						overlayLinkedFigureListener, false);
				drawingArea.translate(drawingArea.width + offsets.x, 0);

				addSimpleConnection(overlayType, drawingArea, primaryLayer, connectionLayer, overlayLayer,
						overlayLinkedFigureListener, true);
				drawingArea.translate(drawingArea.width + offsets.x, 0);

				addRectRoutedConnection(overlayType, drawingArea, primaryLayer, connectionLayer, overlayLayer,
						overlayLinkedFigureListener, true);
				drawingArea.translate(drawingArea.width + offsets.x, 0);

				addCustomRoutedConnection(overlayType, drawingArea, primaryLayer, connectionLayer, overlayLayer,
						overlayLinkedFigureListener, true);
				drawingArea.translate(drawingArea.width + offsets.x, 0);

				drawingArea.x = offsets.x;
				drawingArea.translate(0, drawingArea.height + offsets.y);
			}

		}
	}

	private void addSimpleConnection(OverlayType overlayType, Rectangle drawingArea, Layer primaryLayer,
			Layer connectionLayer, Layer overlayLayer, OverlayLinkedFigureListener overlayLinkedFigureListener,
			boolean withComments) {
		/* add the linked figures to the primary layer */

		IFigure startNode = new Ellipse();
		IFigure endNode = new Ellipse();

		primaryLayer.add(startNode, new Rectangle(drawingArea.getBottomLeft().translate(-4, -4), new Dimension(8, 8)));
		primaryLayer.add(endNode, new Rectangle(drawingArea.getTopRight().translate(-4, -4), new Dimension(8, 8)));

		/* add the linked figures to the connection layer */

		PolylineConnectionEx connection = new PolylineConnectionEx();
		connection.addFigureListener(overlayLinkedFigureListener);
		connection.setSourceAnchor(new EllipseAnchor(startNode));
		connection.setTargetAnchor(new EllipseAnchor(endNode));
		connectionLayer.add(connection);

		/*
		 * Add the overlay figures to the overlay layer. In this case overlay
		 * figures are linked to other figures on the primary layer using the
		 * DefaultOverlayLocator. This locator simply delegates the bounds
		 * update command to IOverlayFigure#updateBoundsfromLinkedFigures().
		 * Notice that the following figures demonstrate the usage but do not
		 * represent all possible variations.
		 */

		OverlayConnectionFigure overlay = new OverlayConnectionFigure(styleAdvisor, overlayType);
		overlayLayer.add(overlay, new DefaultOverlayLocator(connection));
		overlay.setShowCommentHint(withComments);
	}

	private void addRectRoutedConnection(OverlayType overlayType, Rectangle drawingArea, Layer primaryLayer,
			Layer connectionLayer, Layer overlayLayer, OverlayLinkedFigureListener overlayLinkedFigureListener,
			boolean withComments) {
		/* add the linked figures to the primary layer */

		IFigure startNode = new Ellipse();
		IFigure endNode = new Ellipse();

		primaryLayer.add(startNode, new Rectangle(drawingArea.getBottomLeft().translate(-4, -4), new Dimension(8, 8)));
		primaryLayer.add(endNode, new Rectangle(drawingArea.getTopRight().translate(-4, -4), new Dimension(8, 8)));

		/* add the linked figures to the connection layer */

		PolylineConnectionEx connection = new PolylineConnectionEx();
		connection.addFigureListener(overlayLinkedFigureListener);
		connection.setSourceAnchor(new EllipseAnchor(startNode));
		connection.setTargetAnchor(new EllipseAnchor(endNode));
		connectionLayer.add(connection);
		if (connectionLayer instanceof ConnectionLayerEx) {
			connection.setConnectionRouter(((ConnectionLayerEx) connectionLayer).getRectilinearRouter());
		}

		/*
		 * Add the overlay figures to the overlay layer. In this case overlay
		 * figures are linked to other figures on the primary layer using the
		 * DefaultOverlayLocator. This locator simply delegates the bounds
		 * update command to IOverlayFigure#updateBoundsfromLinkedFigures().
		 * Notice that the following figures demonstrate the usage but do not
		 * represent all possible variations.
		 */

		OverlayConnectionFigure overlay = new OverlayConnectionFigure(styleAdvisor, overlayType);
		overlayLayer.add(overlay, new DefaultOverlayLocator(connection));
		overlay.setShowCommentHint(withComments);
	}

	private void addCustomRoutedConnection(OverlayType overlayType, Rectangle drawingArea, Layer primaryLayer,
			Layer connectionLayer, Layer overlayLayer, OverlayLinkedFigureListener overlayLinkedFigureListener,
			boolean withComments) {

		Dimension size = drawingArea.getSize();

		/* add the linked figures to the primary layer */

		IFigure startNode = new Ellipse();
		IFigure endNode = startNode;

		primaryLayer.add(startNode, new Rectangle(drawingArea.getRight().translate(-4, -4), new Dimension(8, 8)));

		/* add the linked figures to the connection layer */

		PolylineConnectionEx connection = new PolylineConnectionEx();
		connection.addFigureListener(overlayLinkedFigureListener);
		connection.setSourceAnchor(new EllipseAnchor(startNode));
		connection.setTargetAnchor(new EllipseAnchor(endNode));

		int segments = 13;
		List<Bendpoint> pointlist = new ArrayList<Bendpoint>(segments);
		for (int i = 0; i < segments; i++) {
			double angle = 360.0 / (double) (segments - 1) * (double) i;
			Point point = new PrecisionPoint(Math.cos(Math.toRadians(angle)) * size.preciseWidth() / 2.0,
					Math.sin(Math.toRadians(angle)) * size.preciseHeight() / 2.0).translate(drawingArea.getCenter());
			AbsoluteBendpoint bendpoint = new AbsoluteBendpoint(point);
			pointlist.add(bendpoint);
		}
		connectionLayer.add(connection);

		connection.getConnectionRouter().setConstraint(connection, pointlist);

		/*
		 * Add the overlay figures to the overlay layer. In this case overlay
		 * figures are linked to other figures on the primary layer using the
		 * DefaultOverlayLocator. This locator simply delegates the bounds
		 * update command to IOverlayFigure#updateBoundsfromLinkedFigures().
		 * Notice that the following figures demonstrate the usage but do not
		 * represent all possible variations.
		 */

		OverlayConnectionFigure overlay = new OverlayConnectionFigure(styleAdvisor, overlayType);
		overlayLayer.add(overlay, new DefaultOverlayLocator(connection));
		overlay.setShowCommentHint(withComments);
	}

}

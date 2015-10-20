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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.ConnectionRouter.NullConnectionRouter;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.util.MeasurementUnitHelper;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;

import at.bitandart.zoubek.mervin.draw2d.figures.cluster.Cluster;
import at.bitandart.zoubek.mervin.draw2d.figures.cluster.ClusterView;
import at.bitandart.zoubek.mervin.draw2d.figures.cluster.SingleAxisClusterLayout;

/**
 * Demonstrates the usage of {@link ClusterView}.
 * 
 * @author Florian Zoubek
 *
 */
public class ClusterViewExample extends BaseExample {

	/**
	 * the seed used to generate nodes
	 */
	private static final long SEED_NODES = 216858325495L;
	/**
	 * the seed used to generate connections
	 */
	private static final long SEED_CONNECTIONS = 193624725253L;

	public static void main(String[] args) {
		new ClusterViewExample().run();
	}

	@Override
	protected IFigure createRootFigure() {

		// create a viewport that contains the cluster view
		FreeformViewport freeformViewport = new FreeformViewport();

		// create the cluster view itself, it already contains a set of layers
		ClusterView clusterView = new ClusterView(MeasurementUnitHelper.getMapMode(MeasurementUnit.PIXEL_LITERAL));

		/*
		 * set the desired layout manager for the clusters, if no layout manager
		 * is set, the cluster locations must be set manually
		 */
		clusterView.setClusterLayoutManager(new SingleAxisClusterLayout());

		// add the cluster view to the viewport and initialize it
		freeformViewport.setContents(clusterView);
		freeformViewport.setBackgroundColor(ColorConstants.white);
		freeformViewport.setOpaque(true);

		return freeformViewport;
	}

	@Override
	protected void addChildFigures(IFigure parentFigure) {

		// find the cluser view figure
		ClusterView clusterView = null;
		if (parentFigure instanceof Viewport) {
			IFigure contents = ((Viewport) parentFigure).getContents();
			if (contents instanceof ClusterView) {
				clusterView = (ClusterView) contents;
			}
		}

		if (clusterView != null) {

			// get the different layers and add children to them

			Layer contentLayer = clusterView.getContentLayer();
			Layer connectionLayer = clusterView.getConnectionLayer();

			List<IFigure> nodeFigures = createNodes(contentLayer);
			List<Connection> connectionFigures = createConnections(connectionLayer, contentLayer, nodeFigures);

			// now select some of the created children and add them to clusters

			Cluster cluster1 = new Cluster();
			Connection connection = connectionFigures.get(0);
			cluster1.add(connection);
			cluster1.add(connection.getSourceAnchor().getOwner());
			cluster1.add(connection.getTargetAnchor().getOwner());

			Cluster cluster2 = new Cluster();
			cluster2.add(nodeFigures.get(nodeFigures.size() - 1));
			cluster2.add(connection.getTargetAnchor().getOwner());

			// register the clusters, otherwise they are not drawn

			clusterView.registerCluster(cluster1);
			clusterView.registerCluster(cluster2);
		}
	}

	/**
	 * creates a number of node figures with random attributes based on
	 * {@link #SEED_NODES}.
	 * 
	 * @param contentLayer
	 *            the layer to add the created figure to.
	 * @return the created node figures
	 */
	private List<IFigure> createNodes(Layer contentLayer) {
		List<IFigure> nodeFigures = new ArrayList<IFigure>();

		// use a pseudorandom number generator with a reproducible seed
		Random random = new Random(SEED_NODES);
		int numNodes = 20;

		int minWidth = 20;
		int maxWidth = 100;
		int minHeight = minWidth;
		int maxHeight = maxWidth;
		int widthRange = maxWidth - minWidth;
		int heightRange = maxHeight - minHeight;

		int xRange = 500;
		int yRange = xRange;

		for (int i = 0; i < numNodes; i++) {
			IFigure figure = null;
			if (random.nextBoolean()) {
				figure = new RectangleFigure();
			} else {
				figure = new Ellipse();
			}
			figure.setBackgroundColor(ColorConstants.lightGray);
			figure.setForegroundColor(ColorConstants.darkGray);

			// add a mouse listener for demonstration purpose
			figure.addMouseMotionListener(new ColorChangeMouseMotionListener());

			int width = (int) (minWidth + random.nextDouble() * widthRange);
			int height = (int) (minHeight + random.nextDouble() * heightRange);
			int x = (int) (random.nextDouble() * xRange);
			int y = (int) (random.nextDouble() * yRange);

			contentLayer.add(figure, new Rectangle(x, y, width, height));
			nodeFigures.add(figure);
		}

		return nodeFigures;
	}

	/**
	 * 
	 * creates a number of connection figures with random attributes based on
	 * {@link #SEED_CONNECTIONS}.
	 * 
	 * @param connectionLayer
	 * @param contentLayer
	 * @param nodes
	 * @return
	 */
	private List<Connection> createConnections(Layer connectionLayer, Layer contentLayer, List<IFigure> nodes) {
		List<Connection> connectionFigures = new ArrayList<Connection>();

		// use a pseudorandom number generator with a reproducible seed
		Random random = new Random(SEED_CONNECTIONS);
		double connectionCreationFactor = 0.8;
		int numNodes = nodes.size();
		int numConnections = (int) Math.round(Math.floor(numNodes / 2.0) * connectionCreationFactor);

		int numMaxBendpoints = 7;
		int minBendpointXOffset = -40;
		int maxBendpointXOffset = 40;
		int minBendpointYOffset = minBendpointXOffset;
		int maxBendpointYOffset = maxBendpointXOffset;
		int bendpointXOffsetRange = maxBendpointXOffset - minBendpointXOffset;
		int bendpointYOffsetRange = maxBendpointYOffset - minBendpointYOffset;

		List<ConnectionRouter> connectionRouters = new ArrayList<ConnectionRouter>();
		connectionRouters.add(NullConnectionRouter.NULL);
		connectionRouters.add(new ManhattanConnectionRouter());
		connectionRouters.add(new ShortestPathConnectionRouter(contentLayer));
		connectionRouters.add(new BendpointConnectionRouter());

		for (int connectionIndex = 0; connectionIndex < numConnections; connectionIndex++) {
			Connection connection = new PolylineConnectionEx();
			connection.addMouseMotionListener(new ColorChangeMouseMotionListener());

			IFigure source = nodes.get((int) Math.floor(numNodes * random.nextDouble()));
			IFigure target = nodes.get((int) Math.floor(numNodes * random.nextDouble()));

			// create the appropriate anchor for the source
			if (source instanceof Ellipse) {
				connection.setSourceAnchor(new EllipseAnchor(source));
			} else {
				connection.setSourceAnchor(new ChopboxAnchor(source));
			}

			// create the appropriate anchor for the target
			if (target instanceof Ellipse) {
				connection.setTargetAnchor(new EllipseAnchor(target));
			} else {
				connection.setTargetAnchor(new ChopboxAnchor(target));
			}

			connectionLayer.add(connection);

			ConnectionRouter connectionRouter = connectionRouters
					.get((int) Math.floor(random.nextDouble() * connectionRouters.size()));

			if (connectionRouter instanceof BendpointConnectionRouter) {

				/*
				 * add some random bendpoints if a BendpointConnectionRouter is
				 * assigned
				 */

				int numBendpoints = (int) (1 + random.nextDouble() * (numMaxBendpoints - 1));

				List<Bendpoint> bendpoints = new ArrayList<Bendpoint>(numBendpoints);

				for (int bendpointIndex = 0; bendpointIndex < numBendpoints; bendpointIndex++) {

					RelativeBendpoint bendpoint = new RelativeBendpoint(connection);
					bendpoint.setRelativeDimensions(
							new Dimension((int) (minBendpointXOffset + bendpointXOffsetRange * random.nextDouble()),
									(int) (minBendpointYOffset + bendpointYOffsetRange * random.nextDouble())),
							new Dimension((int) (minBendpointXOffset + bendpointXOffsetRange * random.nextDouble()),
									(int) (minBendpointYOffset + bendpointYOffsetRange * random.nextDouble())));
					bendpoint.setWeight(random.nextFloat());
					bendpoints.add(bendpoint);

				}

				connectionRouter.setConstraint(connection, bendpoints);

			}

			connection.setConnectionRouter(connectionRouter);

			connectionFigures.add(connection);
		}

		return connectionFigures;
	}

	@Override
	protected String getTitle() {
		return "Mervin - Cluster View Example";
	}

	@Override
	protected int getCanvasHeight() {
		return 800;
	}

	@Override
	protected int getCanvasWidth() {
		return 800;
	}

	/**
	 * A simple {@link MouseMotionListener} that changes the color of a figure
	 * when the mouse is above a figure.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private final class ColorChangeMouseMotionListener implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent me) {
			// intentionally left empty
		}

		@Override
		public void mouseEntered(MouseEvent me) {
			Object source = me.getSource();
			if (source instanceof IFigure) {
				IFigure figure = ((IFigure) source);
				figure.setBackgroundColor(ColorConstants.lightBlue);
				figure.setForegroundColor(ColorConstants.darkBlue);
				if (figure instanceof Shape) {
					((Shape) figure).setLineWidthFloat(3.0f);
				}
			}
		}

		@Override
		public void mouseExited(MouseEvent me) {
			Object source = me.getSource();
			if (source instanceof IFigure) {
				IFigure figure = ((IFigure) source);
				figure.setBackgroundColor(ColorConstants.lightGray);
				figure.setForegroundColor(ColorConstants.darkGray);
				if (figure instanceof Shape) {
					((Shape) figure).setLineWidthFloat(1.0f);
				}
			}
		}

		@Override
		public void mouseHover(MouseEvent me) {
			// intentionally left empty
		}

		@Override
		public void mouseMoved(MouseEvent me) {
			// intentionally left empty
		}
	}

}

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GraphicsSource;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.UpdateListener;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemsAwareFreeFormLayer;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import at.bitandart.zoubek.mervin.draw2d.MervinLayerConstants;
import at.bitandart.zoubek.mervin.draw2d.figures.IComposedFigure;

/**
 * A composed {@link ScalableFreeformLayeredPane} that provides views for a set
 * of {@link Cluster} s containing a subset of the descendant figures contained
 * in a clustered layer. Currently all layers are considered as clustered
 * layers. Each cluster provides a view on its contained {@link IFigure}s and
 * may modify their location based on the cluster location. Drawing clusters on
 * top of each other is not supported. <b>IMPORTANT NOTICE:</b> Due to the fact
 * that this figure may draw the same figure multiple times at different
 * locations, it is not possible to retrieve the correct coordinates of the
 * drawn figures through the {@code translate*()} methods. Methods to get these
 * coordinates are subject of further development. These methods operate upon
 * the the real coordinates of the figure. Also note that several methods have
 * been rewritten to support mouse events for the drawn copies of the figures:
 * {@link #containsPoint(int, int)}, {@link #containsPoint(Point)},
 * {@link #findFigureAtExcluding(int, int, java.util.Collection)},
 * {@link #findFigureAt(int, int, TreeSearch)}, {@link #findFigureAt(int, int)},
 * as well as {@link #findFigureAt(Point)} consider the real coordinates of the
 * figure as well as the coordinates to draw the "copies" of the figure. In
 * contrast, {@link #findMouseEventTargetInDescendantsAt(int, int)} considers
 * <b>only</b> the coordinates used to draw the "copies" of the figure.
 * 
 * @author Florian Zoubek
 *
 */
public class ClusterView extends ScalableFreeformLayeredPane implements IComposedFigure {

	// TODO add methods to retrieve the coordinates of the drawn figures
	// TODO add non-clustered layers (and update javadoc)

	/**
	 * the registered clusters.
	 */
	private List<Cluster> clusters = new ArrayList<Cluster>();

	/**
	 * indicates if this figure has been properly initialized or not.
	 */
	private boolean initialized;

	/**
	 * the layout manager used to layout the clusters, may be null.
	 */
	private IClusterLayoutManager clusterLayoutManager;

	public ClusterView() {
		initialized = false;
	}

	// IComposedFigure logic

	@Override
	public void addNotify() {
		if (!initialized) {
			initialize();
		}
		super.addNotify();
	}

	@Override
	public List<?> getChildren() {
		if (!initialized) {
			initialize();
		}
		return super.getChildren();
	}

	/**
	 * initializes the figure and its child figures
	 */
	public void initialize() {
		initializeFigure();
		initializeChildren();
		initialized = true;
		notifyInitializationComplete();
	}

	/**
	 * initializes the figure and its child figures
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Initializes the figure itself. Intended to be overridden by subclasses.
	 */
	protected void initializeFigure() {
		Color color = new Color(Display.getDefault(), new RGB(225, 225, 225));
		setBackgroundColor(color);
		setOpaque(true);
	}

	/**
	 * Initializes all child figures. Intended to be overridden by subclasses.
	 */
	protected void initializeChildren() {

		IFigure primaryLayer = new BorderItemsAwareFreeFormLayer();
		primaryLayer.setLayoutManager(new FreeFormLayoutEx());
		add(primaryLayer, LayerConstants.PRIMARY_LAYER);
		IFigure connectionlayer = new ConnectionLayerEx();
		add(connectionlayer, LayerConstants.CONNECTION_LAYER);

		FreeformLayer overlayLayer = new FreeformLayer();
		overlayLayer.setLayoutManager(new DelegatingLayout());
		add(overlayLayer, MervinLayerConstants.DIFF_HIGHLIGHT_LAYER);

	}

	/**
	 * Called once the figure and its children have been initialized (hence,
	 * {@link #isInitialized()} will always return false). Intended to be
	 * overridden by subclasses, does nothing by default.
	 */
	protected void notifyInitializationComplete() {
		// intentionally left empty
	}

	@Override
	public Layer getLayer(Object key) {
		if (!isInitialized()) {
			initialize();
		}
		return super.getLayer(key);
	}

	// Cluster logic

	/**
	 * registers the given cluster to this figure. Does nothing if the cluster
	 * is already registered to this figure.
	 * 
	 * @param cluster
	 *            the cluster to register.
	 * @return true if the cluster has been registered properly, false if it has
	 *         been already registered before this operation.
	 */
	public boolean registerCluster(Cluster cluster) {

		if (!clusters.contains(cluster)) {

			ClusterView oldClusterView = cluster.getClusterView();
			if (oldClusterView != null) {
				oldClusterView.unregisterCluster(cluster);
			}

			cluster.setClusterView(this);

			boolean result = clusters.add(cluster);
			repaint();
			return result;

		}
		return false;
	}

	/**
	 * unregisters the given cluster to this figure. Does nothing if the cluster
	 * is not registered to this figure.
	 * 
	 * @param cluster
	 *            the cluster to unregister.
	 * @return true if the cluster has been registered before this operation,
	 *         false otherwise.
	 */
	public boolean unregisterCluster(Cluster cluster) {
		boolean result = clusters.remove(cluster);
		cluster.setClusterView(null);
		repaint();
		return result;
	}

	/**
	 * checks whether the given cluster is registered to this figure or not.
	 * 
	 * @param cluster
	 *            the cluster to check
	 * @return true if the given cluster is registered to this figure, false
	 *         otherwise
	 */
	public boolean isClusterRegistered(Cluster cluster) {
		return clusters.contains(cluster);
	}

	/**
	 * @param figure
	 *            the figure that should be contained
	 * @return the cluster containing the given figure or null if no cluster
	 *         contains the given figure.
	 */
	public Cluster getClusterContaining(IFigure figure) {
		for (Cluster cluster : clusters) {
			if (cluster.contains(figure)) {
				return cluster;
			}
		}
		return null;
	}

	/**
	 * @return a unmodifiable list of all registered clusters.
	 */
	public List<Cluster> getClusters() {
		return Collections.unmodifiableList(clusters);
	}

	/**
	 * returns the content layer of this cluster view. Use this layer to add
	 * child figures that may be contained in {@link Cluster}s.
	 * 
	 * @return the content layer
	 */
	public Layer getContentLayer() {
		if (!isInitialized()) {
			initialize();
		}
		return getLayer(LayerConstants.PRIMARY_LAYER);
	}

	/**
	 * 
	 * returns the connection layer of this cluster view. Use this layer to add
	 * child connections that may be contained in {@link Cluster}s.
	 * 
	 * @return the connection layer
	 */
	public Layer getConnectionLayer() {
		if (!isInitialized()) {
			initialize();
		}
		return getLayer(LayerConstants.CONNECTION_LAYER);
	}

	@Override
	protected void paintChildren(Graphics graphics) {

		// draw children for each cluster
		for (Cluster cluster : clusters) {

			List<IFigure> affectedChildren = getAffectedChildren(cluster);
			Vector translationOffset = cluster.getTranslationOffset();
			Rectangle[] clipping = cluster.getClippingArea();

			/*
			 * translate the children to the local coordinate system of the
			 * cluster
			 */
			graphics.translate((float) translationOffset.x, (float) translationOffset.y);
			graphics.pushState();

			graphics.setBackgroundColor(ColorConstants.white);
			graphics.setForegroundColor(ColorConstants.lightGray);
			graphics.setLineWidthFloat(1.0f);
			graphics.setLineStyle(SWT.LINE_CUSTOM);
			graphics.setLineDash(new int[] { 5, 10 });
			for (Rectangle clipRect : clipping) {
				translateToRelative(clipRect);
				translateFromParent(clipRect);

				graphics.fillRectangle(clipRect.x + 1, clipRect.y + 1, clipRect.width - 1, clipRect.height - 1);
				graphics.drawRectangle(clipRect);
			}
			graphics.restoreState();

			for (IFigure child : affectedChildren) {

				if (child.isVisible()) {

					// child may now paint inside the clipping areas
					for (int j = 0; j < clipping.length; j++) {
						if (clipping[j].intersects(graphics.getClip(Rectangle.SINGLETON))) {
							graphics.clipRect(clipping[j]);
							child.paint(graphics);
							graphics.restoreState();
						}
					}

				}

			}

			// restore translated state
			graphics.popState();
			// restore state before translating
			graphics.restoreState();

		}

	}

	/**
	 * @param cluster
	 *            the cluster to find the affected children for.
	 * @return the children of this figure that must be drawn for the given
	 *         cluster. This method always returns a List instance.
	 */
	private List<IFigure> getAffectedChildren(Cluster cluster) {
		List<?> children = getChildren();
		List<IFigure> affectedChildren = new ArrayList<IFigure>(children.size());

		for (Object child : children) {

			if (child instanceof IFigure) {
				/*
				 * a child is affected if its bounds intersects with the bounds
				 * of a figure in the cluster
				 */
				IFigure childFigure = (IFigure) child;
				Rectangle childBounds = childFigure.getBounds();
				childFigure.translateToAbsolute(childBounds);

				for (IFigure clusterChild : cluster) {

					Rectangle clusterChildBounds = clusterChild.getBounds().getCopy();
					clusterChild.translateToAbsolute(clusterChildBounds);

					if (clusterChildBounds.intersects(childBounds)) {
						affectedChildren.add(childFigure);
						break;
					}

				}
			}

		}

		return affectedChildren;
	}

	/**
	 * 
	 * @param ancestor
	 * @param descendant
	 * @return true if the given {@code ancestor} figure is an ancestor of the
	 *         given {@code descendant} figure, false otherwise.
	 */
	private boolean isAncestor(IFigure ancestor, IFigure descendant) {

		if (descendant == null) {
			return false;
		} else if (ancestor.equals(descendant)) {
			return true;
		} else {
			return isAncestor(ancestor, descendant.getParent());
		}

	}

	@Override
	public void validate() {
		super.validate();
		if (clusterLayoutManager != null) {
			clusterLayoutManager.layout(this);
			repaint();
		}
	}

	@Override
	protected IFigure findDescendantAtExcluding(int x, int y, TreeSearch search) {

		// transform the coordinates based on the cluster offset
		Point clusterPoint = toClusterPoint(x, y);
		if (clusterPoint == null) {
			clusterPoint = new PrecisionPoint(x, y);
		}
		return super.findDescendantAtExcluding(clusterPoint.x, clusterPoint.y, search);
	}

	@Override
	public boolean containsPoint(int x, int y) {

		Point clusterPoint = toClusterPoint(x, y);
		if (clusterPoint == null) {
			clusterPoint = new PrecisionPoint(x, y);
		}
		return super.containsPoint(clusterPoint.x, clusterPoint.y);

	}

	@Override
	protected IFigure findMouseEventTargetInDescendantsAt(int x, int y) {

		Point clusterPoint = toClusterPoint(x, y);
		/*
		 * valid mouse event target are only contained in clusters or not part
		 * of the clustered layers
		 */
		// TODO find mouse event targets in other non-clustered layers
		if (clusterPoint != null) {
			return super.findMouseEventTargetInDescendantsAt(clusterPoint.x, clusterPoint.y);
		}
		return null;
	}

	@Override
	public UpdateManager getUpdateManager() {

		return new ClusterViewUpdateManager(super.getUpdateManager());
	}

	/**
	 * transforms the given cluster view related coordinates to the coordinate
	 * system of child layers based on the contained clusters bounds. This
	 * method assumes that the bounds of of the drawn cluster do not intersect.
	 * 
	 * @param x
	 * @param y
	 * @return the transformed coordinates
	 */
	private Point toClusterPoint(int x, int y) {

		PrecisionPoint point = new PrecisionPoint(x, y);

		for (Cluster cluster : clusters) {

			if (cluster.getBounds().contains(x, y)) {
				Vector translationOffset = cluster.getTranslationOffset();
				point.translate(-translationOffset.x, -translationOffset.y);
				return point;
			}

		}

		return null;
	}

	/**
	 * sets the layout manager which is responsible for managing the location of
	 * the clusters of this figure.
	 * 
	 * @param clusterLayoutManager
	 *            the layout manager to set, passing null disables automatic
	 *            layout of clusters.
	 */
	public void setClusterLayoutManager(IClusterLayoutManager clusterLayoutManager) {
		this.clusterLayoutManager = clusterLayoutManager;
	}

	/**
	 * An {@link UpdateManager} that delegates to a given {@link UpdateManager}
	 * but modifies the calls to support cluster redrawing.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ClusterViewUpdateManager extends UpdateManager {

		/**
		 * the {@link UpdateManager} to delegate to.
		 */
		private UpdateManager realManager;

		public ClusterViewUpdateManager(UpdateManager realManager) {
			this.realManager = realManager;
		}

		@Override
		public void addDirtyRegion(IFigure figure, int x, int y, int w, int h) {

			Rectangle dirtyRegion = new Rectangle(x, y, w, h);

			/*
			 * a single figure may be drawn in multiple clusters, make all
			 * cluster regions dirty that contain the figure
			 */

			for (Cluster cluster : clusters) {
				if (cluster.getRealBounds().intersects(dirtyRegion)) {
					realManager.addDirtyRegion(ClusterView.this, cluster.getBounds());
				}
			}

		}

		@Override
		public void runWithUpdate(Runnable run) {
			realManager.runWithUpdate(run);
		}

		@Override
		public void addInvalidFigure(IFigure figure) {
			realManager.addInvalidFigure(figure);
		}

		@Override
		public void addUpdateListener(UpdateListener listener) {
			realManager.addUpdateListener(listener);
		}

		@Override
		public void dispose() {
			realManager.dispose();
		}

		@Override
		protected void firePainting(Rectangle damage, Map dirtyRegions) {
			super.firePainting(damage, dirtyRegions);
		}

		@Override
		protected void fireValidating() {
			super.fireValidating();
		}

		@Override
		protected boolean isDisposed() {
			return super.isDisposed();
		}

		@Override
		public void performUpdate() {
			realManager.performUpdate();
		}

		@Override
		protected void paint(GC gc) {
			super.paint(gc);
		}

		@Override
		public void performUpdate(Rectangle exposed) {
			realManager.performUpdate();
		}

		@Override
		public void removeUpdateListener(UpdateListener listener) {
			realManager.removeUpdateListener(listener);
		}

		@Override
		public void setGraphicsSource(GraphicsSource gs) {
			realManager.setGraphicsSource(gs);
		}

		@Override
		public void setRoot(IFigure figure) {
			realManager.setRoot(figure);
		}

		@Override
		public void performValidation() {
			realManager.performValidation();
		}
	}

}

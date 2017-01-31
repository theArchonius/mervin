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
package at.bitandart.zoubek.mervin.diagram.diff.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;

import at.bitandart.zoubek.mervin.draw2d.MervinLayerConstants;
import at.bitandart.zoubek.mervin.draw2d.ViewportFillingLayout;
import at.bitandart.zoubek.mervin.draw2d.figures.FocusFreeformViewport;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * A {@link ScalableFreeformRootEditPart} that contains the default layers and
 * an additional {@link #DIFF_HIGHLIGHT_LAYER} on top.
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramDiffRootEditPart extends DiagramRootEditPart implements IFocusHighlightEditPart {

	public DiagramDiffRootEditPart(MeasurementUnit measurementUnit) {
		super(measurementUnit);
	}

	@Override
	protected IFigure createFigure() {

		/*
		 * The default viewport must be replaced with a FocusFreeformViewport -
		 * however, createFigure() also initializes inaccessible attributes so
		 * this must be done by setting the old viewport content to the new
		 * viewport content
		 */
		FreeformViewport oldViewport = (FreeformViewport) super.createFigure();
		FreeformViewport viewport = new FocusFreeformViewport(ColorConstants.black, 128, 0.0f, false);
		viewport.setContents(oldViewport.getContents());

		return viewport;
	}

	private FocusFreeformViewport getFocusFreeformViewport() {
		return (FocusFreeformViewport) getFigure();
	}

	@Override
	public void addFocusHighlightFigure(IFigure focusFigure) {

		if (focusFigure != null) {
			FocusFreeformViewport viewport = getFocusFreeformViewport();
			viewport.addFocusFigure(focusFigure);
			viewport.setFocusEnabled(true);
		}

	}

	@Override
	public void removeFocusHighlightFigure(IFigure focusFigure) {

		if (focusFigure != null) {
			FocusFreeformViewport viewport = getFocusFreeformViewport();
			viewport.removeFocusFigure(focusFigure);
			if (viewport.getFocusedFigures().isEmpty()) {
				viewport.setFocusEnabled(false);
			}
		}

	}

	@Override
	public void disableFocusHighlightMode() {

		FocusFreeformViewport viewport = getFocusFreeformViewport();
		viewport.clearFocusedFigures();
		viewport.setFocusEnabled(false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.diagram.diff.parts.IFocusableEditPart#
	 * isFocusModeEnabled()
	 */
	@Override
	public boolean isFocusHighlightModeEnabled() {

		return getFocusFreeformViewport().isFocusEnabled();

	}

	public ModelReview getModelReview() {
		return (ModelReview) this.getModel();
	}

	@Override
	protected void createLayers(LayeredPane layeredPane) {
		super.createLayers(layeredPane);
		layeredPane.add(new FreeformLayer(), MervinLayerConstants.DIFF_HIGHLIGHT_LAYER);
		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
		layout.setStretchMinorAxis(false);
		layout.setStretchMajorAxis(false);
		layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_TOPLEFT);
		getContentPane().setLayoutManager(new ViewportFillingLayout());
	}

	@Override
	protected LayeredPane createPrintableLayers() {

		FreeformLayeredPane layeredPane = new FreeformLayeredPane();
		layeredPane.add(new FreeformLayer(), PRIMARY_LAYER);
		layeredPane.add(new ConnectionLayerEx() {

			/**
			 * the figure listener that is used to update the connections parent
			 * layer
			 */
			private Object connectionListener = new ConnectionUpdateListener();

			/**
			 * A listener that triggers updating the parent of connections in
			 * this layer and layers this layer delegates connections to. Note
			 * that this listener is not the optimal solution but the only
			 * option as it is currently not possible to listen to anchor
			 * assignments. Also note that using this class as a FigureListener
			 * is a hack and should only be used as a fallback option when using
			 * it as a PropertyListener causes problems during editing. It is a
			 * hack because the update should be done if the anchors change and
			 * not if a figure move is detected. However, if the anchors change,
			 * usually also the bounds change and a move is triggered, so the
			 * update is triggered correctly. Using it as a
			 * PropertyChangeListener is better but once it is possible to
			 * listen to anchor set events modify this listener to listen to
			 * these events instead.
			 * 
			 * @author Florian Zoubek
			 *
			 */
			class ConnectionUpdateListener implements FigureListener, PropertyChangeListener {

				@Override
				public void figureMoved(IFigure source) {
					if (source instanceof Connection) {
						updateConnectionLayer((Connection) source);
					}
				}

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Object source = evt.getSource();
					if (source instanceof Connection) {
						updateConnectionLayer((Connection) source);
					}
				}

			}

			/**
			 * a set of all connections managed by this layer
			 */
			private Set<Connection> managedConnections = new HashSet<Connection>();

			@Override
			public void add(IFigure figure, Object constraint, int index) {

				if (figure instanceof Connection) {
					/*
					 * try to find a closer connection layer and insert it
					 * there, if possible. This is done to provide proper
					 * clipping of connections within child layered panes (which
					 * are usually created within scrollpanes)
					 */

					Connection connection = (Connection) figure;
					if (!managedConnections.contains(connection)) {
						managedConnections.add(connection);
						connection.addPropertyChangeListener(Connection.PROPERTY_POINTS,
								(PropertyChangeListener) connectionListener);
					}
					Layer layer = findNearestConnectionLayer(connection);
					if (layer != null && layer != connection.getParent()) {
						if (layer == this) {
							super.add(connection, constraint, -1);
						} else {
							layer.add(connection, constraint);
						}
					}

				} else {
					super.add(figure, constraint, index);
				}
			}

			/**
			 * updates the given connection parent by re-adding it to the layer
			 * and trigger the delegation mechanism of this layer
			 * 
			 * @param connection
			 */
			private void updateConnectionLayer(Connection connection) {
				IFigure parent = connection.getParent();
				// re-add this figure to move it to the correct container figure
				this.add(connection);
			}

			/**
			 * searches for the nearest connection layer that is able to contain
			 * the given connection (the nearest connection layer contained in a
			 * {@link LayeredPane} which is the parent of both connection
			 * anchors).
			 * 
			 * @param connection
			 * @return the nearest connection layer that is able to contain the
			 *         given connection
			 */
			private Layer findNearestConnectionLayer(Connection connection) {
				List<Layer> sourceLayers = findAllParentConnectionLayers(connection.getSourceAnchor());
				List<Layer> targetLayers = findAllParentConnectionLayers(connection.getTargetAnchor());

				for (Layer sourceLayer : sourceLayers) {
					for (Layer targetLayer : targetLayers) {
						if (sourceLayer == targetLayer) {
							return sourceLayer;
						}
					}
				}
				return null;
			}

			/**
			 * creates a list of all connection layers in parent layered panes,
			 * ordered by their distance to the anchors owner. The current
			 * connection layer instance is always part of the list.
			 * 
			 * @param figure
			 * @return
			 */
			private List<Layer> findAllParentConnectionLayers(ConnectionAnchor anchor) {
				if (anchor != null) {
					IFigure owner = anchor.getOwner();
					if (owner != null) {
						return findAllParentConnectionLayers(owner);
					}
				}
				List<Layer> parentConnectionLayers = new ArrayList<Layer>(1);
				parentConnectionLayers.add(this);
				return parentConnectionLayers;
			}

			/**
			 * creates a list of all connection layers in parent layered panes,
			 * ordered by their distance to the figure. The provided figure
			 * itself is not considered as a possible layered pane. The current
			 * connection layer instance is always part of the list.
			 * 
			 * @param figure
			 * @return
			 */
			private List<Layer> findAllParentConnectionLayers(IFigure figure) {
				List<Layer> parentConnectionLayers = new ArrayList<Layer>();
				Layer nearestLayer = findNearestConnectionLayer(figure);
				while (nearestLayer != null && nearestLayer != this) {
					parentConnectionLayers.add(nearestLayer);
					nearestLayer = findNearestConnectionLayer(nearestLayer.getParent());
				}
				if (!parentConnectionLayers.contains(this)) {
					/*
					 * make sure this connection layer is always contained in
					 * the list of parent connection layers - although this is
					 * only possible if the figure is no child of the layered
					 * pane containing this connection layer
					 */
					parentConnectionLayers.add(this);
				}
				return parentConnectionLayers;
			}

			/**
			 * searches for the nearest connection layer with is contained in a
			 * parent {@link LayeredPane}. The search starts at the parent of
			 * the given figure.
			 * 
			 * @param owner
			 * @return
			 */
			private Layer findNearestConnectionLayer(IFigure owner) {
				IFigure parent = owner.getParent();
				Layer layer = null;
				if (parent instanceof LayeredPane) {
					layer = ((LayeredPane) parent).getLayer(CONNECTION_LAYER);
				}
				if (layer == null && parent != null) {
					return findNearestConnectionLayer(parent);
				}
				return layer;
			}

			@Override
			public void remove(IFigure figure) {
				if (figure instanceof Connection && managedConnections.contains((Connection) figure)) {
					figure.removePropertyChangeListener(Connection.PROPERTY_POINTS,
							(PropertyChangeListener) connectionListener);
					managedConnections.remove((Connection) figure);
				}
				IFigure parent = figure.getParent();
				if (parent == this) {
					super.remove(figure);
				} else {
					parent.remove(figure);
				}
			}

		}, CONNECTION_LAYER);
		return layeredPane;
	}

}

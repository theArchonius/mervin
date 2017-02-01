/*******************************************************************************
 * Copyright (c) 2015, 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.draw2d.figures.workbench;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ScrollPaneLayout;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemsAwareFreeFormLayer;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.tooling.runtime.linklf.LinkLFShapeCompartmentEditPart;

import at.bitandart.zoubek.mervin.draw2d.MervinLayerConstants;
import at.bitandart.zoubek.mervin.draw2d.figures.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.OffScreenIndicatorLayout;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.OffScreenOverlayIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.OffScreenOverlayIndicatorMerger;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.IDiffWorkbench.DisplayMode;

/**
 * An {@link IDiffWorkbenchContainer} implementation that shows the contents of
 * a single diagram.
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramContainerFigure extends LinkLFShapeCompartmentEditPart.ShapeCompartmentFigureEx
		implements IDiffWorkbenchContainer {

	private IDiffWorkbenchTrayFigure trayFigure;

	private IDiffWorkbenchWindowTitleFigure windowTitleFigure;

	private IOverlayTypeStyleAdvisor styleAdvisor;

	private OffScreenOverlayIndicatorMerger offScreenOverlayIndicatorMerger;

	private IFigure toolbarFigure;

	private IDiffWorkbench workbench;

	private IWorkbenchListener workbenchListener = new IWorkbenchListener() {

		private boolean oldActiveState = false;

		private Map<Connection, Object> cachedConnectionConstraints = new HashMap<>();

		@Override
		public void preTrayUpdate(IDiffWorkbench workbench) {
			// intentionally left empty
		}

		@Override
		public void preTopContainerChanged(IDiffWorkbench workbench, IDiffWorkbenchContainer oldTopContainer,
				IDiffWorkbenchContainer newTopContainer) {

			oldActiveState = isActive();

			if (newTopContainer == DiagramContainerFigure.this) {

				/*
				 * Connection constraints get lost when moving the current
				 * container to the top, so cache and re-apply them after the
				 * move has been done
				 */
				cachedConnectionConstraints.clear();
				Layer connectionLayer = getLayer(LayerConstants.CONNECTION_LAYER);
				List<?> children = connectionLayer.getChildren();
				for (Object child : children) {
					if (child instanceof Connection) {
						Connection connection = (Connection) child;
						ConnectionRouter connectionRouter = connection.getConnectionRouter();
						Object constraint = connectionRouter.getConstraint(connection);
						cachedConnectionConstraints.put(connection, constraint);
					}
				}

			}
		}

		@Override
		public void preSendToTrayArea(IDiffWorkbench workbench, IDiffWorkbenchContainer container) {
			// intentionally left empty

		}

		@Override
		public void preSendToContentArea(IDiffWorkbench workbench, IDiffWorkbenchContainer container) {
			// intentionally left empty

		}

		@Override
		public void preDisplayModeChange(IDiffWorkbench workbench, DisplayMode oldMode, DisplayMode newMode) {
			// intentionally left empty

		}

		@Override
		public void postTrayUpdate(IDiffWorkbench workbench) {
			// intentionally left empty
		}

		@Override
		public void postTopContainerChanged(IDiffWorkbench workbench, IDiffWorkbenchContainer oldTopContainer,
				IDiffWorkbenchContainer newTopContainer) {

			if (newTopContainer == DiagramContainerFigure.this) {

				/*
				 * Connection constraints get lost when moving the current
				 * container to the top, so cache and re-apply them after the
				 * move has been done
				 */
				for (Connection connection : cachedConnectionConstraints.keySet()) {
					ConnectionRouter connectionRouter = connection.getConnectionRouter();
					connectionRouter.setConstraint(connection, cachedConnectionConstraints.get(connection));
				}
				cachedConnectionConstraints.clear();

			}

			boolean newActiveState = isActive();

			if (oldActiveState != newActiveState) {
				Iterator<?> listeners = getListeners(IWorkbenchContainerListener.class);
				while (listeners.hasNext()) {
					((IWorkbenchContainerListener) listeners.next()).activeStateChanged(newActiveState);
				}
			}
		}

		@Override
		public void postSendToTrayArea(IDiffWorkbench workbench, IDiffWorkbenchContainer container) {
			// intentionally left empty
		}

		@Override
		public void postSendToContentArea(IDiffWorkbench workbench, IDiffWorkbenchContainer container) {
			// intentionally left empty
		}

		@Override
		public void postDisplayModeChange(IDiffWorkbench workbench, DisplayMode oldMode, DisplayMode newMode) {
			// intentionally left empty
		}
	};

	public DiagramContainerFigure(String compartmentTitle, IOverlayTypeStyleAdvisor styleAdvisor, IMapMode mm) {
		super(compartmentTitle, mm);
		remove(getTextPane());

		ScrollPane scrollPane = getScrollPane();
		scrollPane.setLayoutManager(new ScrollPaneLayout());

		toolbarFigure = new Figure() {
			@Override
			public void add(IFigure figure, Object constraint, int index) {
				super.add(figure, constraint, index);
				if (getBorder() == null) {
					setBorder(new MarginBorder(3));
				}
			}

			@Override
			public void remove(IFigure figure) {
				super.remove(figure);
				if (getChildren().isEmpty()) {
					setBorder(null);
				}
			}
		};
		toolbarFigure.setBackgroundColor(ColorConstants.lightGray);
		toolbarFigure.setOpaque(true);
		ConstrainedToolbarLayout constrainedToolbarLayout = new ConstrainedToolbarLayout(true);
		constrainedToolbarLayout.setSpacing(10);
		constrainedToolbarLayout.setStretchMajorAxis(false);
		constrainedToolbarLayout.setStretchMinorAxis(false);
		toolbarFigure.setLayoutManager(constrainedToolbarLayout);
		add(toolbarFigure, 0);

		this.styleAdvisor = styleAdvisor;

		// TODO find some better way than using an internal GMF class
		FreeformLayeredPane layeredPane = new FreeformLayeredPane();
		IFigure primaryLayer = new BorderItemsAwareFreeFormLayer();
		primaryLayer.setLayoutManager(new FreeFormLayoutEx());
		layeredPane.add(primaryLayer, LayerConstants.PRIMARY_LAYER);
		IFigure connectionlayer = new ConnectionLayerEx();
		layeredPane.add(connectionlayer, LayerConstants.CONNECTION_LAYER);

		FreeformLayer overlayLayer = new FreeformLayer();
		overlayLayer.setLayoutManager(new DelegatingLayout());
		layeredPane.add(overlayLayer, MervinLayerConstants.DIFF_HIGHLIGHT_LAYER);

		FreeformLayer indicatorLayer = new FreeformLayer();
		indicatorLayer.setLayoutManager(new OffScreenIndicatorLayout());
		offScreenOverlayIndicatorMerger = createOffScreenChangeIndicatorMerger(styleAdvisor, indicatorLayer);
		indicatorLayer.addLayoutListener(offScreenOverlayIndicatorMerger);
		layeredPane.add(indicatorLayer, MervinLayerConstants.DIFF_INDICATOR_LAYER);
		scrollPane.setContents(layeredPane);

		setBorder(new LineBorder(ColorConstants.lightGray, 2));
		setOpaque(true);
	}

	/**
	 * @param styleAdvisor
	 * @param indicatorLayer
	 * @return a new {@link OffScreenOverlayIndicatorMerger} that should be used
	 *         to merge {@link OffScreenOverlayIndicator}s.
	 */
	protected OffScreenOverlayIndicatorMerger createOffScreenChangeIndicatorMerger(
			IOverlayTypeStyleAdvisor styleAdvisor, FreeformLayer indicatorLayer) {
		return new OffScreenOverlayIndicatorMerger(indicatorLayer, styleAdvisor);
	}

	@Override
	public Figure getTextPane() {
		return super.getTextPane();
	}

	public OffScreenOverlayIndicatorMerger getOffScreenOverlayIndicatorMerger() {
		return offScreenOverlayIndicatorMerger;
	}

	public IOverlayTypeStyleAdvisor getStyleAdvisor() {
		return styleAdvisor;
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
		return toolbarFigure;
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

	@Override
	public void setActive() {

		if (workbench != null) {
			workbench.setActiveContainer(this);
		}
	}

	@Override
	public boolean isActive() {
		return workbench != null && workbench.getActiveContainer() == this;
	}

	@Override
	public void setWorkbench(IDiffWorkbench workbench) {

		if (workbench != this.workbench) {

			if (this.workbench != null && this.workbench.containsContainer(this)) {
				this.workbench.removeContainer(this);
				this.workbench.removeWorkbenchListener(workbenchListener);
			}

			this.workbench = workbench;

			if (!workbench.containsContainer(this)) {
				workbench.addContainer(this, null);
			}

			workbench.addWorkbenchListener(workbenchListener);
		}
	}

	@Override
	public IDiffWorkbench getWorkbench() {
		return workbench;
	}

	@Override
	public void addWorkbenchContainerListener(IWorkbenchContainerListener workbenchContainerListener) {
		addListener(IWorkbenchContainerListener.class, workbenchContainerListener);

	}

	@Override
	public void removeWorkbenchContainerListener(IWorkbenchContainerListener workbenchContainerListener) {
		removeListener(IWorkbenchContainerListener.class, workbenchContainerListener);
	}

}

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
package at.bitandart.zoubek.mervin.diagram.diff.parts;

import java.util.Iterator;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IEditableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableEditPolicyEx;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.linklf.LinkLFShapeCompartmentEditPart;

import at.bitandart.zoubek.mervin.diagram.diff.OffScreenIndicatorResolver;
import at.bitandart.zoubek.mervin.draw2d.MervinLayerConstants;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.IOffScreenIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.MergedOffScreenOverlayIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.OffScreenOverlayIndicatorMerger;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.DefaultOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.DiagramContainerFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.IDiffWorkbench;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.IDiffWorkbench.DisplayMode;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.IDiffWorkbenchContainer;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.IWorkbenchListener;

/**
 * An {@link EditPart} that provides a view on a diagram.
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramEditPart extends LinkLFShapeCompartmentEditPart {

	IWorkbenchListener workbenchListener;

	private IOverlayTypeStyleAdvisor styleAdvisor;

	public DiagramEditPart(View model) {
		super(model);
		styleAdvisor = new DefaultOverlayTypeStyleAdvisor();
	}

	@Override
	public IFigure createFigure() {
		DiagramContainerFigure figure = new InternalDiagramContainerFigure(getCompartmentName(), styleAdvisor,
				getMapMode());
		figure.getContentPane().setLayoutManager(getLayoutManager());
		figure.getContentPane().addLayoutListener(LayoutAnimator.getDefault());
		return figure;
	}

	/**
	 * convenience method to retrieve the associated
	 * {@link DiagramContainerFigure}.
	 * 
	 * @return the {@link DiagramContainerFigure} of this edit part.
	 */
	public DiagramContainerFigure getDiagramContainerFigure() {
		return (DiagramContainerFigure) getFigure();
	}

	/**
	 * A {@link DiagramContainerFigure} that allows scrolling to merged
	 * {@link IOffScreenIndicator}s if a mouse button click is detected on a
	 * merged off-screen indicator.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private static class InternalDiagramContainerFigure extends DiagramContainerFigure {

		public InternalDiagramContainerFigure(String compartmentTitle, IOverlayTypeStyleAdvisor styleAdvisor,
				IMapMode mm) {
			super(compartmentTitle, styleAdvisor, mm);
		}

		@Override
		protected OffScreenOverlayIndicatorMerger createOffScreenChangeIndicatorMerger(
				IOverlayTypeStyleAdvisor styleAdvisor, FreeformLayer indicatorLayer) {

			return new OffScreenOverlayIndicatorMerger(indicatorLayer, styleAdvisor) {

				@Override
				protected MergedOffScreenOverlayIndicator createMergedOffScreenChangeIndicator() {
					MergedOffScreenOverlayIndicator mergedIndicator = super.createMergedOffScreenChangeIndicator();
					mergedIndicator
							.addMouseListener(new OffScreenIndicatorResolver(InternalDiagramContainerFigure.this));
					return mergedIndicator;
				}
			};
		}

	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		if (workbenchListener == null) {
			EditPart parentEditPart = getParent();
			IFigure parentFigure = null;
			if (parentEditPart instanceof GraphicalEditPart) {
				parentFigure = ((GraphicalEditPart) parentEditPart).getFigure();
			}
			if (parentFigure instanceof IDiffWorkbench) {

				workbenchListener = new DiagramWorkbenchListener();
				IDiffWorkbench workbench = ((IDiffWorkbench) parentFigure);
				workbench.addWorkbenchListener(workbenchListener);

			}
		}
		refreshBounds();
	}

	@Override
	public void deactivate() {

		if (workbenchListener != null) {

			EditPart parentEditPart = getParent();
			IFigure parentFigure = null;
			if (parentEditPart instanceof GraphicalEditPart) {
				parentFigure = ((GraphicalEditPart) parentEditPart).getFigure();
			}

			if (parentFigure instanceof IDiffWorkbench) {

				IDiffWorkbench workbench = ((IDiffWorkbench) parentFigure);
				workbench.removeWorkbenchListener(workbenchListener);
			}
		}
		super.deactivate();
		styleAdvisor.dispose();
	}

	@Override
	protected void reorderChild(EditPart child, int index) {

		Object constraint = null;
		IFigure childFigure = ((GraphicalEditPart) child).getFigure();
		if (child instanceof IOverlayEditPart) {
			/*
			 * Save the constraint of the overlay to re-apply it once it has
			 * been reordered, otherwise it gets lost during reordering
			 */
			LayoutManager layout = childFigure.getParent().getLayoutManager();
			if (layout != null)
				constraint = layout.getConstraint(childFigure);

		}
		super.reorderChild(child, index);
		if (constraint != null) {
			setLayoutConstraint(child, childFigure, constraint);
		}
	}

	@Override
	protected void addChild(EditPart child, int index) {
		super.addChild(child, index);
		if (child instanceof IEditableEditPart) {
			((IEditableEditPart) child).disableEditMode();
		}
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {

		IFigure figure = getFigure();
		if (childEditPart instanceof IOverlayEditPart && childEditPart instanceof GraphicalEditPart
				&& figure instanceof DiagramContainerFigure) {
			DiagramContainerFigure containerFigure = (DiagramContainerFigure) figure;
			Layer layer = containerFigure.getLayer(MervinLayerConstants.DIFF_HIGHLIGHT_LAYER);
			layer.add(((GraphicalEditPart) childEditPart).getFigure(), toOverlayIndex(index, containerFigure));
		} else {
			super.addChildVisual(childEditPart, index);
		}

	}

	@Override
	protected void removeChildVisual(EditPart childEditPart) {

		IFigure figure = getFigure();
		if (childEditPart instanceof IOverlayEditPart && childEditPart instanceof GraphicalEditPart
				&& figure instanceof DiagramContainerFigure) {
			DiagramContainerFigure containerFigure = (DiagramContainerFigure) figure;
			Layer layer = containerFigure.getLayer(MervinLayerConstants.DIFF_HIGHLIGHT_LAYER);
			layer.remove(((GraphicalEditPart) childEditPart).getFigure());
		} else {
			super.removeChildVisual(childEditPart);
		}

	}

	private int toOverlayIndex(int index, DiagramContainerFigure figure) {

		Layer layer = figure.getLayer(LayerConstants.PRIMARY_LAYER);
		int numPrimaryChildren = layer.getChildren().size();
		return Math.max(index - numPrimaryChildren, 0);
	}

	/**
	 * refresh the bounds
	 */
	protected void refreshBounds() {
		int width = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getSize_Width())).intValue();
		int height = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getSize_Height())).intValue();
		int x = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getLocation_X())).intValue();
		int y = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getLocation_Y())).intValue();
		if (width == -1 && height == -1) {
			x = getParent().getChildren().indexOf(this) * 10;
			y = x;
			width = 300;
			height = 300;
		}
		Dimension size = new Dimension(width, height);
		Point loc = new Point(x, y);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), new Rectangle(loc, size));
	}

	@Override
	public String getCompartmentName() {
		EObject element = getNotationView().getElement();
		if (element instanceof Diagram) {
			return ((Diagram) element).getName();
		}
		return super.getCompartmentName();
	}

	@Override
	protected void setRatio(Double ratio) {
		// ratio is not supported by parent figures with XYLayout layout
		// managers
	}

	@Override
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
	}

	@Override
	protected void handleNotificationEvent(Notification event) {
		Object feature = event.getFeature();
		if (NotationPackage.eINSTANCE.getSize_Width().equals(feature)
				|| NotationPackage.eINSTANCE.getSize_Height().equals(feature)
				|| NotationPackage.eINSTANCE.getLocation_X().equals(feature)
				|| NotationPackage.eINSTANCE.getLocation_Y().equals(feature)) {
			refreshBounds();
		}
		super.handleNotificationEvent(event);
	}

	@Override
	protected ConnectionRefreshMgr createConnectionRefreshMgr() {
		return new ConnectionRefreshMgr() {

			@Override
			protected void refreshConnections(ShapeCompartmentEditPart scep) {

				/*
				 * modified, based on
				 * org.eclipse.gmf.runtime.diagram.ui.editparts.
				 * ShapeCompartmentEditPart.ConnectionRefreshMgr.
				 * refreshConnections(ShapeCompartmentEditPart)
				 * 
				 * Differences to the base version are mentioned in the
				 * comments.
				 */

				Iterator<?> connectionNodes = getConnectionNodes(scep).iterator();

				while (connectionNodes.hasNext()) {

					ConnectionNodeEditPart cep = (ConnectionNodeEditPart) connectionNodes.next();
					Connection connection = (Connection) cep.getFigure();
					View connectionView = cep.getNotationView();
					if (connectionView != null && !connectionView.isVisible()) {
						/*
						 * Compartment is not responsible for refreshing a
						 * connection, the view of which is not visible
						 */
						continue;
					}

					IGraphicalEditPart source = (IGraphicalEditPart) getSourceEditPart(cep);
					IGraphicalEditPart target = (IGraphicalEditPart) getTargetEditPart(cep);
					if (source == null || target == null) {
						connection.setVisible(false);
						continue;
					}

					if (!source.getFigure().isShowing() || !target.getFigure().isShowing()) {
						connection.setVisible(false);
						continue;
					}

					ShapeCompartmentEditPart sContainer = getOwningShapeCompartment(source);
					ShapeCompartmentEditPart tContainer = getOwningShapeCompartment(target);
					// only deal with items contained within a shape compartment
					if (sContainer == null && tContainer == null) {
						continue;
					}

					/*
					 * the following code differs from the original: connections
					 * inside this compartment are stored in the connection
					 * layer of the compartment, which is automatically clipped,
					 * so just make sure all connections are visible
					 */
					connection.setVisible(true);
					refreshConnectionEnds(cep);
				}
			}

			/*
			 * copied from org.eclipse.gmf.runtime.diagram.ui.editparts.
			 * ShapeCompartmentEditPart.ConnectionRefreshMgr.
			 * refreshConnectionEnds(ConnectionEditPart)
			 */
			private void refreshConnectionEnds(ConnectionEditPart cEP) {
				EditPart srcEditPart = cEP.getSource();
				EditPart trgEditPart = cEP.getTarget();
				Object model = cEP.getModel();
				if (model instanceof Edge) {
					Edge edge = (Edge) model;
					View source = edge.getSource();
					View target = edge.getTarget();
					if (srcEditPart == null) {
						refreshEditPart(cEP.getViewer(), source);
					}
					if (trgEditPart == null) {
						refreshEditPart(cEP.getViewer(), target);
					}
				}
			}

			/*
			 * copied from org.eclipse.gmf.runtime.diagram.ui.editparts.
			 * ShapeCompartmentEditPart.ConnectionRefreshMgr.refreshEditPart(
			 * EditPartViewer, View)
			 */
			private void refreshEditPart(EditPartViewer viewer, View view) {
				EditPart ep = (EditPart) viewer.getEditPartRegistry().get(view);
				if (ep != null) {
					ep.refresh();
				}
			}
		};
	}

	private final class DiagramWorkbenchListener implements IWorkbenchListener {
		@Override
		public void preTrayUpdate(IDiffWorkbench workbench) {
			// Intentionally left empty
		}

		@Override
		public void preDisplayModeChange(IDiffWorkbench workbench, DisplayMode oldMode, DisplayMode newMode) {
			/*
			 * the selection edit policy should only be active in window display
			 * mode
			 */
			if (oldMode == DisplayMode.WINDOW && newMode != oldMode) {

				removeEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE);

			} else if (oldMode == DisplayMode.TAB && newMode != oldMode) {

				installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ResizableEditPolicyEx());

			}
		}

		@Override
		public void postTrayUpdate(IDiffWorkbench workbench) {
			// Intentionally left empty
		}

		@Override
		public void postDisplayModeChange(IDiffWorkbench workbench, DisplayMode oldMode, DisplayMode newMode) {
			// Intentionally left empty
		}

		@Override
		public void preTopContainerChanged(IDiffWorkbench workbench, IDiffWorkbenchContainer oldTopContainer,
				IDiffWorkbenchContainer newTopContainer) {
			// Intentionally left empty
		}

		@Override
		public void postTopContainerChanged(IDiffWorkbench workbench, IDiffWorkbenchContainer oldTopContainer,
				IDiffWorkbenchContainer newTopContainer) {
			// Intentionally left empty
		}

		@Override
		public void preSendToTrayArea(IDiffWorkbench workbench, IDiffWorkbenchContainer container) {
			// Intentionally left empty
		}

		@Override
		public void postSendToTrayArea(IDiffWorkbench workbench, IDiffWorkbenchContainer container) {
			forceRefreshConnections();
			DiagramEditPart.this.setSelected(EditPart.SELECTED_NONE);
		}

		@Override
		public void preSendToContentArea(IDiffWorkbench workbench, IDiffWorkbenchContainer container) {
			// intentionally left empty
		}

		@Override
		public void postSendToContentArea(IDiffWorkbench workbench, IDiffWorkbenchContainer container) {
			forceRefreshConnections();
		}
	}
}

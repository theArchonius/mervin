/*******************************************************************************
 * Copyright (c) 2015, 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.diagram.diff.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IEditableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableEditPolicyEx;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.linklf.LinkLFShapeCompartmentEditPart;

import at.bitandart.zoubek.mervin.draw2d.MervinLayerConstants;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultChangeTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.IChangeTypeStyleAdvisor;
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

	private IChangeTypeStyleAdvisor styleAdvisor;

	public DiagramEditPart(View model) {
		super(model);
		styleAdvisor = new DefaultChangeTypeStyleAdvisor();
	}

	@Override
	public IFigure createFigure() {
		DiagramContainerFigure figure = new DiagramContainerFigure(getCompartmentName(), styleAdvisor, getMapMode());
		figure.getContentPane().setLayoutManager(getLayoutManager());
		figure.getContentPane().addLayoutListener(LayoutAnimator.getDefault());
		return figure;
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

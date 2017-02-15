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

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableEditPolicyEx;
import org.eclipse.gmf.runtime.notation.View;

import at.bitandart.zoubek.mervin.diagram.diff.OffScreenIndicatorResolver;
import at.bitandart.zoubek.mervin.draw2d.MervinLayerConstants;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultOverlayLocator;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.OverlayLinkedFigureListener;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.IOffScreenIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.OffScreenOverlayIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.DiagramContainerFigure;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;

/**
 * Abstract base class for all {@link DifferenceOverlay}s.
 * 
 * @author Florian Zoubek
 *
 */
public abstract class AbstractDifferenceOverlayEditPart extends ShapeNodeEditPart implements IOverlayEditPart {

	private DefaultOverlayTypeStyleAdvisor styleAdvisor;
	protected IFigure prevLinkedFigure = null;
	private IOffScreenIndicator offScreenIndicator;
	private GraphicalEditPart previousLinkedParentEditPart = null;

	/**
	 * An {@link EditPartListener} that will be attached to the first existing edit part of the
	 * linked views parents. It is used to trigger an update of the bounds
	 * constraint information once the children of the edit part change. This is
	 * necessary as the linked view edit part may not (yet) exist at the time
	 * the bounds constraints are set in {@link #refreshBounds()}.
	 */
	private EditPartListener linkedParentEditPartListener = new EditPartListener() {

		@Override
		public void selectedStateChanged(EditPart editpart) {
		}

		@Override
		public void removingChild(EditPart child, int index) {
		}

		@Override
		public void partDeactivated(EditPart editpart) {
		}

		@Override
		public void partActivated(EditPart editpart) {
		}

		@Override
		public void childAdded(EditPart child, int index) {
			refreshVisuals();
		}
	};

	public AbstractDifferenceOverlayEditPart(View view) {
		super(view);
	}

	/**
	 * convenience method to get the associated change overlay.
	 * 
	 * @return the associated {@link DifferenceOverlay} instance or null if the
	 *         semantic element is not an instance of {@link DifferenceOverlay}.
	 */
	public DifferenceOverlay getDifferenceOverlay() {

		EObject semanticElement = resolveSemanticElement();
		if (semanticElement instanceof DifferenceOverlay) {
			return (DifferenceOverlay) semanticElement;
		}
		return null;

	}

	@Override
	public EditPolicy getPrimaryDragEditPolicy() {
		return new NonResizableEditPolicyEx() {
			@Override
			protected List<?> createSelectionHandles() {
				return Collections.emptyList();
			}
		};
	}

	protected Layer getParentOverlayLayer() {
		// TODO retrieve it from the parent edit part instead
		return (Layer) getFigure().getParent();
	}

	/**
	 * @return the containing diagram container figure, or null if it does not
	 *         exist.
	 */
	protected DiagramContainerFigure getContainerFigure() {
		EditPart parent = getParent();
		if (parent instanceof IGraphicalEditPart) {
			IFigure figure = ((IGraphicalEditPart) parent).getFigure();
			if (figure instanceof DiagramContainerFigure) {
				return (DiagramContainerFigure) figure;
			}
		}
		return null;
	}

	/**
	 * @return the indicator layer for this edit part or null, if none can be
	 *         found.
	 */
	protected Layer getIndicatorLayer() {
		DiagramContainerFigure parent = getContainerFigure();
		if (parent != null) {
			return parent.getLayer(MervinLayerConstants.DIFF_INDICATOR_LAYER);
		}
		return null;
	}

	@Override
	public GraphicalEditPart getLinkedEditPart() {

		DifferenceOverlay changeOverlay = getDifferenceOverlay();
		if (changeOverlay != null) {

			Object editPart = getViewer().getEditPartRegistry().get(changeOverlay.getLinkedView());
			if (editPart instanceof GraphicalEditPart) {
				return (GraphicalEditPart) editPart;
			}

		}

		return null;
	}

	@Override
	public GraphicalEditPart getLinkedExistingParentEditPart() {

		DifferenceOverlay changeOverlay = getDifferenceOverlay();
		if (changeOverlay != null) {
			EObject container = changeOverlay.getLinkedView().eContainer();
			while (container != null) {
				Object editPart = getViewer().getEditPartRegistry().get(container);
				if (editPart instanceof GraphicalEditPart) {
					return (GraphicalEditPart) editPart;
				}
				container = container.eContainer();
			}

		}

		return null;
	}

	public IOverlayTypeStyleAdvisor getStyleAdvisor() {
		DiagramContainerFigure containerFigure = getContainerFigure();
		if (containerFigure != null) {
			IOverlayTypeStyleAdvisor styleAdvisor = containerFigure.getStyleAdvisor();
			if (styleAdvisor != null) {
				return styleAdvisor;
			}
		}
		// Cannot derive style advisor, so use own instance
		if (this.styleAdvisor == null) {
			this.styleAdvisor = new DefaultOverlayTypeStyleAdvisor();
		}
		return this.styleAdvisor;

	}

	@Override
	public void activate() {

		super.activate();
		DiagramContainerFigure containerFigure = getContainerFigure();
		Layer indicatorLayer = getIndicatorLayer();
		final IOffScreenIndicator offScreenIndicator = getOffScreenIndicator();
		offScreenIndicator.addLinkedFigure(getFigure());
		offScreenIndicator.setContainerFigure(containerFigure.getScrollPane().getViewport());
		offScreenIndicator.addMouseListener(new OffScreenIndicatorResolver(getContainerFigure()));

		if (offScreenIndicator instanceof OffScreenOverlayIndicator) {
			containerFigure.getOffScreenOverlayIndicatorMerger()
					.registerIndicator((OffScreenOverlayIndicator) offScreenIndicator);
		}

		indicatorLayer.add(offScreenIndicator);

	}

	abstract protected IOffScreenIndicator createOffScreenIndicator();

	public IOffScreenIndicator getOffScreenIndicator() {

		if (offScreenIndicator == null) {
			offScreenIndicator = createOffScreenIndicator();
		}
		return offScreenIndicator;

	}

	@Override
	public void deactivate() {
		super.deactivate();

		DiagramContainerFigure containerFigure = getContainerFigure();
		Layer indicatorLayer = getIndicatorLayer();
		if (offScreenIndicator != null) {
			if (offScreenIndicator instanceof OffScreenOverlayIndicator) {
				containerFigure.getOffScreenOverlayIndicatorMerger()
						.unregisterIndicator((OffScreenOverlayIndicator) offScreenIndicator);
			}
			indicatorLayer.remove(offScreenIndicator);
		}

		/*
		 * clean up the assigned editor part listener on the edit part of the
		 * linked view's parent
		 */
		if (previousLinkedParentEditPart != null) {
			previousLinkedParentEditPart.removeEditPartListener(linkedParentEditPartListener);
			previousLinkedParentEditPart = null;
		}

		if (styleAdvisor != null) {
			styleAdvisor.dispose();
		}
	}

	@Override
	protected void refreshBounds() {
		// apply the constraint
		EditPart parent = getParent();

		/*
		 * listen to changes to the children of the linked view's parent view
		 * edit part to keep track when the linked view's edit part is created
		 * and destroyed.
		 */
		GraphicalEditPart linkedParentEditPart = getLinkedExistingParentEditPart();
		if (linkedParentEditPart != previousLinkedParentEditPart) {
			if (previousLinkedParentEditPart != null) {
				previousLinkedParentEditPart.removeEditPartListener(linkedParentEditPartListener);
			}
			previousLinkedParentEditPart = linkedParentEditPart;
			if (linkedParentEditPart != null) {
				linkedParentEditPart.addEditPartListener(linkedParentEditPartListener);
			}
		}

		GraphicalEditPart linkedEditPart = getLinkedEditPart();

		IFigure linkedFigure = null;

		if (linkedEditPart != null) {
			linkedFigure = linkedEditPart.getFigure();
		}
		if (parent instanceof GraphicalEditPart && linkedFigure != null) {

			if (linkedFigure != prevLinkedFigure) {
				linkedFigure.addFigureListener(new OverlayLinkedFigureListener(getParentOverlayLayer()));
				prevLinkedFigure = linkedFigure;
			}

			((GraphicalEditPart) parent).setLayoutConstraint(this, getFigure(),
					new DefaultOverlayLocator(linkedFigure));
		}
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

}
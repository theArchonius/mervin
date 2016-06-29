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

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableEditPolicyEx;
import org.eclipse.gmf.runtime.notation.View;

import at.bitandart.zoubek.mervin.diagram.diff.OffScreenIndicatorResolver;
import at.bitandart.zoubek.mervin.draw2d.MervinLayerConstants;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultChangeTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultOverlayLocator;
import at.bitandart.zoubek.mervin.draw2d.figures.IChangeTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.OverlayLinkedFigureListener;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.IOffScreenIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.OffScreenChangeIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.DiagramContainerFigure;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;

/**
 * Abstract base class for all {@link DifferenceOverlay}s.
 * 
 * @author Florian Zoubek
 *
 */
public abstract class AbstractDifferenceOverlayEditPart extends ShapeNodeEditPart implements IOverlayEditPart {

	private DefaultChangeTypeStyleAdvisor styleAdvisor;
	protected IFigure prevLinkedFigure = null;
	private IOffScreenIndicator offScreenIndicator;

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

	public IChangeTypeStyleAdvisor getStyleAdvisor() {
		DiagramContainerFigure containerFigure = getContainerFigure();
		if (containerFigure != null) {
			IChangeTypeStyleAdvisor styleAdvisor = containerFigure.getStyleAdvisor();
			if (styleAdvisor != null) {
				return styleAdvisor;
			}
		}
		// Cannot derive style advisor, so use own instance
		if (this.styleAdvisor == null) {
			this.styleAdvisor = new DefaultChangeTypeStyleAdvisor();
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

		if (offScreenIndicator instanceof OffScreenChangeIndicator) {
			containerFigure.getOffScreenChangeIndicatorMerger()
					.registerIndicator((OffScreenChangeIndicator) offScreenIndicator);
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
			if (offScreenIndicator instanceof OffScreenChangeIndicator) {
				containerFigure.getOffScreenChangeIndicatorMerger()
						.unregisterIndicator((OffScreenChangeIndicator) offScreenIndicator);
			}
			indicatorLayer.remove(offScreenIndicator);
		}
		if (styleAdvisor != null) {
			styleAdvisor.dispose();
		}
	}

	@Override
	protected void refreshBounds() {
		// apply the constraint
		EditPart parent = getParent();
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

}
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
package at.bitandart.zoubek.mervin.diagram.diff.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

import at.bitandart.zoubek.mervin.draw2d.figures.ChangeOverlayNodeFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.ChangeType;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultChangeTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultOverlayLocator;
import at.bitandart.zoubek.mervin.draw2d.figures.OverlayLinkedFigureListener;
import at.bitandart.zoubek.mervin.model.modelreview.Difference;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifference;

/**
 * An {@link ShapeNodeEditPart} implementation for {@link DifferenceOverlay}s.
 * 
 * @author Florian Zoubek
 *
 */
public class DifferenceOverlayEditPart extends ShapeNodeEditPart implements IOverlayEditPart {

	private DefaultChangeTypeStyleAdvisor styleAdvisor;

	private IFigure prevLinkedFigure = null;

	public DifferenceOverlayEditPart(View view) {
		super(view);
		styleAdvisor = new DefaultChangeTypeStyleAdvisor();
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();

		DifferenceOverlay differenceOverlay = getDifferenceOverlay();
		ChangeOverlayNodeFigure changeOverlayNodeFigure = getChangeOverlayNodeFigure();
		if (differenceOverlay != null && changeOverlayNodeFigure != null) {
			EList<Difference> differences = differenceOverlay.getDifferences();
			for (Difference difference : differences) {
				if (difference instanceof StateDifference) {
					switch (((StateDifference) difference).getType()) {
					case ADDED:
						changeOverlayNodeFigure.setChangeType(ChangeType.ADDITION);
						break;
					case DELETED:
						changeOverlayNodeFigure.setChangeType(ChangeType.DELETION);
						break;
					case MODIFIED:
						changeOverlayNodeFigure.setChangeType(ChangeType.MODIFICATION);
						break;
					default:
						// do nothing
					}
				}
			}
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

	private Layer getParentOverlayLayer() {
		// TODO retrieve it from the parent edit part instead
		return (Layer) getFigure().getParent();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#
	 * createNodeFigure()
	 */
	@Override
	protected NodeFigure createNodeFigure() {
		return new ChangeOverlayNodeFigure(styleAdvisor, ChangeType.ADDITION);
	}

	protected ChangeOverlayNodeFigure getChangeOverlayNodeFigure() {
		IFigure figure = getFigure();
		if (figure instanceof ChangeOverlayNodeFigure) {
			return (ChangeOverlayNodeFigure) figure;
		}
		return null;
	}

	@Override
	public void deactivate() {
		super.deactivate();
		styleAdvisor.dispose();
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

}

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
import org.eclipse.emf.common.util.EList;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

import at.bitandart.zoubek.mervin.draw2d.figures.ChangeOverlayNodeFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.ChangeType;
import at.bitandart.zoubek.mervin.model.modelreview.Difference;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.NodeDifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifference;

/**
 * An {@link ShapeNodeEditPart} implementation for {@link NodeDifferenceOverlay}
 * s.
 * 
 * @author Florian Zoubek
 *
 */
public class NodeDifferenceOverlayEditPart extends AbstractDifferenceOverlayEditPart {

	public NodeDifferenceOverlayEditPart(View view) {
		super(view);
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

}

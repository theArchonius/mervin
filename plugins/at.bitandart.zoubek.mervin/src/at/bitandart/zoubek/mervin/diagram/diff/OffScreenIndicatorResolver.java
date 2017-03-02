/*******************************************************************************
 * Copyright (c) 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.diagram.diff;

import java.util.Set;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Vector;

import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.IOffScreenIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.MergedOffScreenOverlayIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.OffScreenOverlayIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.DiagramContainerFigure;

/**
 * A {@link MouseListener} that scrolls to linked figures bounds of an
 * IOffScreenIndicator or to nearest the linked figures bounds of an
 * {@link MergedOffScreenOverlayIndicator} when a mouse click is performed. The
 * mouse event is consumed by this {@link MouseListener} if the source is an
 * {@link IOffScreenIndicator}.
 * 
 * @author Florian Zoubek
 *
 */
public class OffScreenIndicatorResolver implements MouseListener {

	private DiagramContainerFigure diagramContainerFigure;

	/**
	 * @param diagramContainerFigure
	 *            the {@link DiagramContainerFigure} that should be scrolled to
	 *            the nearest indicator of all merged indicators.
	 */
	public OffScreenIndicatorResolver(DiagramContainerFigure diagramContainerFigure) {
		super();
		this.diagramContainerFigure = diagramContainerFigure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.MouseListener#mousePressed(org.eclipse.draw2d.
	 * MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent event) {

		Object source = event.getSource();

		Point figureLocation = null;

		if (source instanceof IOffScreenIndicator && ((IOffScreenIndicator) source).isVisible()) {

			if (source instanceof MergedOffScreenOverlayIndicator) {

				MergedOffScreenOverlayIndicator mergedIndicator = (MergedOffScreenOverlayIndicator) source;

				PrecisionPoint mergedIndicatorLocation = new PrecisionPoint(mergedIndicator.getLocation());
				mergedIndicator.getParent().translateToAbsolute(mergedIndicatorLocation);

				double closestDistance = -1;

				Set<OffScreenOverlayIndicator> mergedIndicators = mergedIndicator.getMergedIndicators();
				for (OffScreenOverlayIndicator indicator : mergedIndicators) {

					Point location = indicator.getLinkedFiguresBounds().getLocation();
					if (closestDistance < 0) {

						figureLocation = location;
						closestDistance = new Vector(mergedIndicatorLocation, new PrecisionPoint(location)).getLength();

					} else {

						double distance = new Vector(mergedIndicatorLocation, new PrecisionPoint(location)).getLength();
						if (closestDistance > distance) {
							figureLocation = location;
							closestDistance = distance;
						}
					}
				}

			} else {
				figureLocation = ((IOffScreenIndicator) source).getLinkedFiguresBounds().getLocation();
			}
		}

		if (figureLocation != null) {

			ScrollPane scrollPane = diagramContainerFigure.getScrollPane();
			Viewport viewport = scrollPane.getViewport();
			viewport.translateToRelative(figureLocation);
			viewport.translateFromParent(figureLocation);
			scrollPane.scrollTo(figureLocation);
			event.consume();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.MouseListener#mouseReleased(org.eclipse.draw2d.
	 * MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
		// Intentionally left empty

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.MouseListener#mouseDoubleClicked(org.eclipse.draw2d.
	 * MouseEvent)
	 */
	@Override
	public void mouseDoubleClicked(MouseEvent me) {
		// Intentionally left empty

	}

}

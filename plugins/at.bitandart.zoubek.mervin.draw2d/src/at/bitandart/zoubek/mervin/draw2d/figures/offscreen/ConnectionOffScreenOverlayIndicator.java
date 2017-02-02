/*******************************************************************************
 * Copyright (c) 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.draw2d.figures.offscreen;

import java.util.Collection;
import java.util.Set;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

import at.bitandart.zoubek.mervin.draw2d.figures.OverlayConnectionFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.ConnectionOutline;
import at.bitandart.zoubek.mervin.draw2d.figures.IOverlayTypeStyleAdvisor;

/**
 * An {@link OffScreenOverlayIndicator} that supports more precise visibility and
 * reference point calculations for {@link OverlayConnectionFigure}s. The
 * visibility check is based on the intersection of each outline point list with
 * the containers bounds. The assumption is made that outlines are not big
 * enough that they enclose the whole containers bounds which should work for
 * most connections. The midpoint of the connection of the first outline will be
 * used for the reference point.
 * 
 * @author Florian Zoubek
 *
 */
public class ConnectionOffScreenOverlayIndicator extends OffScreenOverlayIndicator {

	public ConnectionOffScreenOverlayIndicator(IOverlayTypeStyleAdvisor styleAdvisor) {
		super(styleAdvisor);
	}

	@Override
	public boolean areLinkedFiguresVisible() {

		IFigure containerFigure = getContainerFigure();
		Rectangle containerBounds = containerFigure.getBounds().getCopy();
		containerFigure.translateToAbsolute(containerBounds);

		Set<IFigure> linkedFigures = getLinkedFigures();

		for (IFigure linkedFigure : linkedFigures) {

			if (linkedFigure instanceof OverlayConnectionFigure) {

				Collection<ConnectionOutline> outlines = ((OverlayConnectionFigure) linkedFigure)
						.getCurrentOutlines();

				for (ConnectionOutline outline : outlines) {
					if (outline.intersectsWith(containerBounds)) {
						return true;
					}
				}

			} else {
				if (linkedFigure.getBounds().intersects(containerBounds)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public PrecisionPoint getReferencePoint() {

		for (IFigure linkedFigure : linkedFigures) {

			if (linkedFigure instanceof OverlayConnectionFigure) {

				Collection<ConnectionOutline> outlines = ((OverlayConnectionFigure) linkedFigure)
						.getCurrentOutlines();

				if (!outlines.isEmpty()) {
					return outlines.iterator().next().getPointOnLine(0.5);
				}

			}
		}

		return super.getReferencePoint();

	}

}

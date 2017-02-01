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

import at.bitandart.zoubek.mervin.draw2d.figures.ChangeOverlayConnectionFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.ConnectionChangeOutline;
import at.bitandart.zoubek.mervin.draw2d.figures.IChangeTypeStyleAdvisor;

/**
 * An {@link OffScreenChangeIndicator} that supports more precise visibility and
 * reference point calculations for {@link ChangeOverlayConnectionFigure}s. The
 * visibility check is based on the intersection of each outline point list with
 * the containers bounds. The assumption is made that outlines are not big
 * enough that they enclose the whole containers bounds which should work for
 * most connections. The midpoint of the connection of the first outline will be
 * used for the reference point.
 * 
 * @author Florian Zoubek
 *
 */
public class ConnectionOverlayOffScreenChangeIndicator extends OffScreenChangeIndicator {

	public ConnectionOverlayOffScreenChangeIndicator(IChangeTypeStyleAdvisor styleAdvisor) {
		super(styleAdvisor);
	}

	@Override
	public boolean areLinkedFiguresVisible() {

		IFigure containerFigure = getContainerFigure();
		Rectangle containerBounds = containerFigure.getBounds().getCopy();
		containerFigure.translateToAbsolute(containerBounds);

		Set<IFigure> linkedFigures = getLinkedFigures();

		for (IFigure linkedFigure : linkedFigures) {

			if (linkedFigure instanceof ChangeOverlayConnectionFigure) {

				Collection<ConnectionChangeOutline> outlines = ((ChangeOverlayConnectionFigure) linkedFigure)
						.getCurrentOutlines();

				for (ConnectionChangeOutline outline : outlines) {
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

			if (linkedFigure instanceof ChangeOverlayConnectionFigure) {

				Collection<ConnectionChangeOutline> outlines = ((ChangeOverlayConnectionFigure) linkedFigure)
						.getCurrentOutlines();

				if (!outlines.isEmpty()) {
					return outlines.iterator().next().getPointOnLine(0.5);
				}

			}
		}

		return super.getReferencePoint();

	}

}

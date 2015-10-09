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
package at.bitandart.zoubek.mervin.draw2d.figures;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;

/**
 * A {@link FigureListener} that listens for moves of the attached figure and
 * revalidates the given overlay layer if the figure has been moved.
 * 
 * @author Florian Zoubek
 *
 */
public class OverlayLinkedFigureListener implements FigureListener {

	private Layer overlayLayer;

	public OverlayLinkedFigureListener(Layer overlayLayer) {
		this.overlayLayer = overlayLayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.FigureListener#figureMoved(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void figureMoved(IFigure source) {
		overlayLayer.revalidate();
	}

}

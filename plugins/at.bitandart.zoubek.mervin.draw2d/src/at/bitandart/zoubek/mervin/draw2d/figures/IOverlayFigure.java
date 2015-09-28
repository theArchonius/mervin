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

import java.util.Collection;

import org.eclipse.draw2d.IFigure;

/**
 * Base interface for overlay figures which are usually linked to one or more
 * {@link IFigure}s which are not direct or indirect children of the overlay
 * figure.
 * 
 * @author Florian Zoubek
 *
 */
public interface IOverlayFigure extends IFigure {

	/**
	 * updates the figure based on the given linked figures.
	 * 
	 * @param linkedFigures
	 * 
	 */
	public void updateFigure(Collection<IFigure> linkedFigures);

}

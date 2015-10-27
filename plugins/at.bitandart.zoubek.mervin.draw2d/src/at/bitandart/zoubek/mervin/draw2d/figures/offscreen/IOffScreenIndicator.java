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
package at.bitandart.zoubek.mervin.draw2d.figures.offscreen;

import java.util.Set;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Base interface for all {@link IFigure}s that serve as a indicator for a
 * linked {@link IFigure} if it is moved outside the bounds of its container.
 * The container must be an ancestor of the linked figure, but does not need to
 * be the parent of the linked figure. If this figure is not used any more, the
 * creator of this figure should call {@link #cleanUp()}.
 * 
 * @author Florian Zoubek
 *
 */
public interface IOffScreenIndicator extends IFigure {

	/**
	 * adds the given figure to the set of linked figures of this indicator.
	 * 
	 * @param linkedFigure
	 *            the linked figure to add.
	 */
	public void addLinkedFigure(IFigure linkedFigure);

	/**
	 * @return an unmodifiable copy of the set of linked figures.
	 */
	public Set<IFigure> getLinkedFigures();

	/**
	 * removes the given figure from the set of linked figures.
	 * 
	 * @param linkedFigure
	 *            the figure to remove.
	 */
	public void removeLinkedFigure(IFigure linkedFigure);

	/**
	 * sets the container figure of the linked figure. The container figure must
	 * be an ancestor of the current linked figure or null.
	 * 
	 * @param containerFigure
	 *            the container figure of the linked figure or null if no
	 *            container can be determined.
	 * @throws IllegalArgumentException
	 *             if the container figure is not an ancestor of the current
	 *             linked figure.
	 */
	public void setContainerFigure(IFigure containerFigure) throws IllegalArgumentException;

	/**
	 * @return the container figure of the linked figure.
	 */
	public IFigure getContainerFigure();

	/**
	 * calculates the union of all linked figure bounds in absolute coordinates.
	 * 
	 * @return the union of all linked figure bounds.
	 */
	public Rectangle getLinkedFiguresBounds();

	/**
	 * determines if parts of the linked figures are visible in the current
	 * container figure or not.
	 * 
	 * @return true if at least a part of one of the linked figures is visible
	 *         in the current container figure, false otherwise.
	 */
	public boolean areLinkedFiguresVisible();

	/**
	 * cleans up this figure and detaches all linked figures and containers.
	 * Should be called when this figure is not used any more.
	 */
	public void cleanUp();

}

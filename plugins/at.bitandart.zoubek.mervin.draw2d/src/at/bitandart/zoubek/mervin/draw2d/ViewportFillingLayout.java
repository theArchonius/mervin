/*******************************************************************************
 * Copyright (c) 2015, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.draw2d;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A {@link LayoutManager} that fills the nearest parent viewport with the first
 * child.
 * 
 * @author Florian Zoubek
 *
 */
public class ViewportFillingLayout extends AbstractLayout {

	@Override
	public void layout(IFigure container) {
		Viewport viewport = findParentViewport(container);
		Dimension preferredSize = new Dimension(0, 0);

		Point location = new PrecisionPoint(0, 0);
		viewport.translateToAbsolute(location);

		if (viewport != null) {
			preferredSize.setSize(viewport.getBounds().getSize());
			for (Object child : container.getChildren()) {
				if (child instanceof IFigure) {
					IFigure childFigure = (IFigure) child;
					childFigure.setBounds(Rectangle.SINGLETON.setLocation(location).setSize(preferredSize));
					break;
				}
			}
		}
	}

	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		Viewport viewport = findParentViewport(container);
		Dimension preferredSize = new Dimension(0, 0);

		if (viewport != null) {
			preferredSize.setSize(viewport.getBounds().getSize());
		}

		return preferredSize;
	}

	/**
	 * finds the nearest parent viewport of the figure.
	 * 
	 * @param figure
	 *            the figure to start the search.
	 * @return the nearest parent viewport instance or null if no viewport could
	 *         be found.
	 */
	private Viewport findParentViewport(IFigure figure) {
		IFigure parent = figure.getParent();
		while (parent != null) {
			if (parent instanceof Viewport) {
				return (Viewport) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}

}
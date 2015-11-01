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
package at.bitandart.zoubek.mervin.draw2d.figures.workbench;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;

import at.bitandart.zoubek.mervin.draw2d.figures.workbench.IDiffWorkbench.DisplayMode;

/**
 * A {@link LayoutManager} implemtation which is able to switch between stack
 * and freeform layout based on the given {@link DisplayMode}.
 * 
 * @author Florian Zoubek
 *
 */
public class WorkbenchContentLayout extends FreeFormLayoutEx {

	private IDiffWorkbench.DisplayMode displayMode;

	@Override
	public void layout(IFigure container) {
		switch (displayMode) {
		case TAB:
			doTabLayout(container);
			break;
		default:
		case WINDOW:
			doWindowLayout(container);
			break;
		}
	}

	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		switch (displayMode) {
		case TAB:
			return calculatePreferredTabSize(container, wHint, hHint);
		default:
		case WINDOW:
			return calculatePreferredWindowSize(container, wHint, hHint);
		}
	}

	/**
	 * calculates the preferred size in tab mode.
	 * 
	 * @param container
	 * @param wHint
	 * @param hHint
	 * @return the preferred size
	 */
	private Dimension calculatePreferredTabSize(IFigure container, int wHint, int hHint) {

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

	/**
	 * the preferred size in window mode.
	 * 
	 * @param container
	 * @param wHint
	 * @param hHint
	 * @return the preferred size
	 */
	private Dimension calculatePreferredWindowSize(IFigure container, int wHint, int hHint) {
		return super.calculatePreferredSize(container, wHint, hHint);
	}

	/**
	 * performs the tab layout.
	 * 
	 * @param container
	 */
	protected void doTabLayout(IFigure container) {

		Viewport viewport = findParentViewport(container);
		Dimension preferredSize = new Dimension(0, 0);

		Point location = viewport.getLocation();
		viewport.translateToAbsolute(location);
		if (viewport != null) {
			preferredSize.setSize(viewport.getBounds().getSize());

			List<?> children = container.getChildren();
			boolean firstChild = true;
			Iterator<?> childIt = children.iterator();
			while (childIt.hasNext()) {
				Object child = childIt.next();
				if (child instanceof IFigure) {
					IFigure childFigure = (IFigure) child;
					if (firstChild) {
						// only the first child is shown

						childFigure.translateToRelative(location);
						childFigure.setBounds(Rectangle.SINGLETON.setLocation(location).setSize(preferredSize));

						// childFigure.setBounds(container.getClientArea());
						firstChild = false;
					} else {
						/*
						 * all other children are hidden by setting their width
						 * and height to 0
						 */
						// TODO find a better way to hide the other children
						Rectangle bounds = childFigure.getBounds().getCopy();
						bounds.setWidth(0);
						bounds.setHeight(0);
						childFigure.setBounds(bounds);
					}
				}
			}
		}
	}

	/**
	 * performs the window layout.
	 * 
	 * @param container
	 */
	protected void doWindowLayout(IFigure container) {
		super.layout(container);
	}

	/**
	 * sets the {@link DisplayMode} used to select layout procedure
	 * 
	 * @param displayMode
	 *            the {@link DisplayMode} to set
	 */
	public void setDisplayMode(IDiffWorkbench.DisplayMode displayMode) {
		this.displayMode = displayMode;
	}

	/**
	 * @return the current {@link DisplayMode}
	 */
	public IDiffWorkbench.DisplayMode getDisplayMode() {
		return displayMode;
	}
}

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
/**
 * 
 */
package at.bitandart.zoubek.mervin.diagram.diff.figures.workbench;

import org.eclipse.draw2d.IFigure;

import at.bitandart.zoubek.mervin.diagram.diff.figures.workbench.IDiffWorkbench.DisplayMode;

/**
 * Base interface for a container inside a {@link IDiffWorkbench}.
 * Implementations must provide figure containers for at least the following
 * areas: The Status area, the toolbar area, and the content area. However, not
 * all of these areas have to be visible at once in the UI.
 * 
 * @author Florian Zoubek
 *
 */
public interface IDiffWorkbenchContainer extends IFigure {

	/**
	 * @return the window title.
	 */
	public String getWindowTitle();

	/**
	 * @return the figure that represents the status area.
	 */
	public IFigure getStatusArea();

	/**
	 * @return the figure that represents the toolbar area.
	 */
	public IFigure getToolbarArea();

	/**
	 * @return the figure that represents the content area.
	 */
	public IFigure getContentArea();

	/**
	 * @return the {@link IDiffWorkbenchTrayFigure} figure which should be used
	 *         in the workbench tray area.
	 */
	public IDiffWorkbenchTrayFigure getTrayFigure();

	/**
	 * @return the {@link IDiffWorkbenchWindowTitleFigure} figure which should
	 *         be used in the workbench as title in {@link DisplayMode#WINDOW}
	 *         mode.
	 */
	public IDiffWorkbenchWindowTitleFigure getWindowTitleFigure();

}

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

/**
 * Base interface for Workbench figures used in the diff view. Implementations
 * must contain at least a content area and a tray area. This figure is meant to
 * contain children which implement {@link IDiffWorkbenchContainer} and draw
 * them based on the current display mode. The handling of other child figures
 * depends on the actual implementation. Child {@link IDiffWorkbenchContainer}
 * may be added or removed through the {@code add()} and {@code remove()}
 * methods or through {@link #addContainer(IDiffWorkbenchContainer)} and
 * {@link #removeContainer(IDiffWorkbenchContainer)}. You may also add children
 * directly to the content or tray area using {@link #getContentArea()} or
 * {@link #getTrayArea()}.
 * 
 * @author Florian Zoubek
 *
 */
public interface IDiffWorkbench extends IFigure {

	/**
	 * The display modes for the {@link IDiffWorkbench}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public enum DisplayMode {
		/**
		 * Child {@link IDiffWorkbenchContainer} are drawn on top of each other,
		 * leaving only one visible to the user. Tabs in the tray area allow
		 * selection of the visible container.
		 */
		TAB, /**
				 * Child {@link IDiffWorkbenchContainer} are drawn as windows
				 * with a certain size and position. Additionally containers may
				 * also be removed from the content layer and placed in the tray
				 * area, which makes them invisible but the title and is drawn
				 * in the tray area.
				 */
		WINDOW
	}

	/**
	 * @return the current display mode of this workbench.
	 * @see DisplayMode
	 */
	public DisplayMode getDisplayMode();

	/**
	 * @param displayMode
	 *            the display mode to set for this workbench.
	 * @see DisplayMode
	 */
	public void setDisplayMode(DisplayMode displayMode);

	/**
	 * adds the given container to the workbench.
	 * 
	 * @param container
	 *            the container to add.
	 */
	public void addContainer(IDiffWorkbenchContainer container);

	/**
	 * removes the given container from the workbench.
	 * 
	 * @param container
	 *            the container to remove.
	 */
	public void removeContainer(IDiffWorkbenchContainer container);

	/**
	 * sends the given container to the tray area. Does nothing if the container
	 * is already in the tray area or if the current display mode is
	 * {@link DisplayMode#TAB}.
	 * 
	 * @param container
	 *            the container to send to the tray area.
	 * @see #sendToContentArea(IDiffWorkbenchContainer)
	 */
	public void sendToTrayArea(IDiffWorkbenchContainer container);

	/**
	 * sends the given container to the content area. Does nothing if the
	 * container is already in the content area or if the current display mode
	 * is {@link DisplayMode#TAB}.
	 * 
	 * @param container
	 *            the container to send to the content area.
	 * @see #sendToTrayArea(IDiffWorkbenchContainer)
	 */
	public void sendToContentArea(IDiffWorkbenchContainer container);

	/**
	 * sends the given container to the content area and removes the tray figure
	 * from the tray. Does nothing if the container is already in the content
	 * area or if the current display mode is {@link DisplayMode#TAB}.
	 * 
	 * @param trayFigure
	 *            the tray figure whose associated
	 *            {@link IDiffWorkbenchContainer} should be send to the content
	 *            area.
	 * @see #sendToTrayArea(IDiffWorkbenchContainer)
	 */
	public void sendToContentArea(IDiffWorkbenchTrayFigure trayFigure);

	/**
	 * moves the given container to the front.
	 * 
	 * @param container
	 *            the container to move to the front.
	 */
	public void setTopContainer(IDiffWorkbenchContainer container);

	/**
	 * @return the content area figure.
	 */
	public IFigure getContentArea();

	/**
	 * @return the tray area figure.
	 */
	public IFigure getTrayArea();

	/**
	 * registers the given workbench listener to this workbench.
	 * 
	 * @param workbenchListener
	 *            the listener to register.
	 */
	public void addWorkbenchListener(IWorkbenchListener workbenchListener);

	/**
	 * removes the given registered workbench listener from this workbench. Does
	 * nothing if the given workbench listener is unknown to this figure.
	 * 
	 * @param workbenchListener
	 *            the listener to remove.
	 */
	public void removeWorkbenchListener(IWorkbenchListener workbenchListener);

}

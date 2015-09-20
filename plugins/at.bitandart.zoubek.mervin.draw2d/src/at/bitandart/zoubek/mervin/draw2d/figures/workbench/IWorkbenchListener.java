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

import at.bitandart.zoubek.mervin.draw2d.figures.workbench.IDiffWorkbench.DisplayMode;

/**
 * Base interface for {@link IDiffWorkbench} events.
 * 
 * @author Florian Zoubek
 *
 */
public interface IWorkbenchListener {

	/**
	 * called once before the display mode is changed.
	 * 
	 * @param workbench
	 *            the workbench where the event occurred
	 * @param oldMode
	 *            the old display mode
	 * @param newMode
	 *            the new display mode
	 */
	public void preDisplayModeChange(IDiffWorkbench workbench, DisplayMode oldMode, DisplayMode newMode);

	/**
	 * called once after the display mode is changed.
	 * 
	 * @param workbench
	 *            the workbench where the event occurred
	 * @param oldMode
	 *            the old display mode
	 * @param newMode
	 *            the new display mode
	 */
	public void postDisplayModeChange(IDiffWorkbench workbench, DisplayMode oldMode, DisplayMode newMode);

	/**
	 * called once before each tray update.
	 * 
	 * @param workbench
	 *            the workbench where the event occurred
	 */
	public void preTrayUpdate(IDiffWorkbench workbench);

	/**
	 * called once after each tray update.
	 * 
	 * @param workbench
	 *            the workbench where the event occurred
	 */
	public void postTrayUpdate(IDiffWorkbench workbench);

	/**
	 * called once before the top container have been changed.
	 * 
	 * @param workbench
	 *            the workbench where the event occurred
	 * @param oldTopContainer
	 *            the old top container
	 * @param newTopContainer
	 *            the new top container
	 */
	public void preTopContainerChanged(IDiffWorkbench workbench, IDiffWorkbenchContainer oldTopContainer,
			IDiffWorkbenchContainer newTopContainer);

	/**
	 * called once after the top container have been changed.
	 * 
	 * @param workbench
	 *            the workbench where the event occurred
	 * @param oldTopContainer
	 *            the old top container
	 * @param newTopContainer
	 *            the new top container
	 */
	public void postTopContainerChanged(IDiffWorkbench workbench, IDiffWorkbenchContainer oldTopContainer,
			IDiffWorkbenchContainer newTopContainer);

	/**
	 * called once before a {@link IDiffWorkbenchContainer} is sent to the tray
	 * area.
	 * 
	 * @param workbench
	 *            the workbench where the event occurred
	 * @param container
	 *            the container which is send to the area
	 */
	public void preSendToTrayArea(IDiffWorkbench workbench, IDiffWorkbenchContainer container);

	/**
	 * called once after a {@link IDiffWorkbenchContainer} is sent to the tray
	 * area.
	 * 
	 * @param workbench
	 *            the workbench where the event occurred
	 * @param container
	 *            the container which is send to the area
	 */
	public void postSendToTrayArea(IDiffWorkbench workbench, IDiffWorkbenchContainer container);

	/**
	 * called once before a {@link IDiffWorkbenchContainer} is sent to the
	 * content area.
	 * 
	 * @param workbench
	 *            the workbench where the event occurred
	 * @param container
	 *            the container which is send to the area
	 */
	public void preSendToContentArea(IDiffWorkbench workbench, IDiffWorkbenchContainer container);

	/**
	 * called once before a {@link IDiffWorkbenchContainer} is sent to the
	 * content area.
	 * 
	 * @param workbench
	 *            the workbench where the event occurred
	 * @param container
	 *            the container which is send to the area
	 */
	public void postSendToContentArea(IDiffWorkbench workbench, IDiffWorkbenchContainer container);

}

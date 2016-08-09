/*******************************************************************************
 * Copyright (c) 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.draw2d.figures.workbench;

/**
 * Base interface for {@link IDiffWorkbenchContainer} event listeners.
 * 
 * @author Florian Zoubek
 *
 */
public interface IWorkbenchContainerListener {

	/**
	 * called once the active state changes.
	 * 
	 * @param active
	 *            the new state, note that the old state is is always the
	 *            inverse of this state ({@code !active}).
	 */
	public void activeStateChanged(boolean active);

}

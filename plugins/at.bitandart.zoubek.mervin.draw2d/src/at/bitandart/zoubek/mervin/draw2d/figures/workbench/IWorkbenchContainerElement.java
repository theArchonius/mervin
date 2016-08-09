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
 * Represents elements related to an {@link IDiffWorkbenchContainer}.
 * 
 * @author Florian Zoubek
 *
 */
public interface IWorkbenchContainerElement {

	/**
	 * @return the associated {@link IDiffWorkbenchContainer}
	 */
	public IDiffWorkbenchContainer getContainer();
}

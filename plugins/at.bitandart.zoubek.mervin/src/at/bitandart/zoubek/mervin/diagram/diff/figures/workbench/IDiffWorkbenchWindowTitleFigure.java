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
package at.bitandart.zoubek.mervin.diagram.diff.figures.workbench;

import org.eclipse.draw2d.IFigure;

/**
 * @author Florian Zoubek
 *
 */
public interface IDiffWorkbenchWindowTitleFigure extends IFigure {

	/**
	 * @return the associated {@link IDiffWorkbenchContainer}
	 */
	public IDiffWorkbenchContainer getContainer();

	/**
	 * Returns the figure which should be used to attach child figures of this
	 * figure.
	 * 
	 * @return the content pane figure.
	 */
	public IFigure getContentPane();
}

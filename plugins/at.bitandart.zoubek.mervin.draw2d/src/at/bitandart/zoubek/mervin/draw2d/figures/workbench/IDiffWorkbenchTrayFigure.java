/*******************************************************************************
 * Copyright (c) 2015, 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.draw2d.figures.workbench;

import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.IFigure;

/**
 * Base interface for all figures which represent an
 * {@link IDiffWorkbenchContainer} in the workbench tray.
 * 
 * @author Florian Zoubek
 *
 */
public interface IDiffWorkbenchTrayFigure extends IFigure, IWorkbenchContainerElement {

	/**
	 * Returns the figure which should be used to attach child figures of this
	 * figure.
	 * 
	 * @return the content pane figure.
	 */
	public IFigure getContentPane();

	/**
	 * registers the given {@link ActionListener} to this figure.
	 * 
	 * @param listener
	 */
	public void addActionListener(ActionListener listener);

	/**
	 * removes the given {@link ActionListener} from this figure.
	 * 
	 * @param listener
	 */
	public void removeActionListener(ActionListener listener);

}

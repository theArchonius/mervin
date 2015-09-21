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
package at.bitandart.zoubek.mervin.draw2d.figures;

import org.eclipse.draw2d.IFigure;

/**
 * Base interface for figures that are composed of multiple {@link IFigure}s
 * which need a initialization step. This can be done explicitly by invoking
 * {@link #initialize()} or implicitly through initialization triggers. The
 * default initialization triggers are: before adding the figure to a figure
 * hierarchy, and before calling getChildren(). However, the initialization
 * <b>should never</b> be done in the constructor which allows rewritable
 * initialization mechanisms without removing unwanted children and unforeseen
 * side effects due to partially initialized attributes.
 * 
 * @author Florian Zoubek
 *
 */
public interface IComposedFigure extends IFigure {

	/**
	 * initializes the figure and its child figures.
	 */
	public void initialize();

	/**
	 * @return true if the figure is initialized, false otherwise.
	 */
	public boolean isInitialized();

}

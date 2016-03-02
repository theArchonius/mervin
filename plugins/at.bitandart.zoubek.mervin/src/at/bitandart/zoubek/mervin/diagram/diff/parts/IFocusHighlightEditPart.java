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
package at.bitandart.zoubek.mervin.diagram.diff.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;

/**
 * Base interface for {@link EditPart}s that provide temporary highlighting that
 * aims to draw the focus of the user on a subset of child figures. The Focus
 * highlight mode is enabled as long as the list of focus figures is not empty.
 * 
 * @author Florian Zoubek
 *
 */
public interface IFocusHighlightEditPart extends EditPart {

	/**
	 * adds the given focus figure and enables the focus mode.
	 * 
	 * @param focusFigure
	 *            the figure to add to the set of focused figures
	 */
	void addFocusHighlightFigure(IFigure focusFigure);

	/**
	 * removes the given focus figure and disables the focus mode if no focus
	 * figures exist any more.
	 * 
	 * @param focusFigure
	 *            the figure to remove from the set of focused figures
	 */
	void removeFocusHighlightFigure(IFigure focusFigure);

	/**
	 * disables focus mode and clears all focused figures.
	 */
	void disableFocusHighlightMode();

	/**
	 * @return true if the focus mode is enabled, false otherwise.
	 */
	boolean isFocusHighlightModeEnabled();

}
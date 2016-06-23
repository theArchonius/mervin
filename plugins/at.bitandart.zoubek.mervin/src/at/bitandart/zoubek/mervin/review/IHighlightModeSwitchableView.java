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
package at.bitandart.zoubek.mervin.review;

/**
 * Base Interface for views that are able to switch between some or all
 * available {@link HighlightMode}s.
 * 
 * @author Florian Zoubek
 *
 */
public interface IHighlightModeSwitchableView {

	/**
	 * set the current highlight mode for this view.
	 * 
	 * @param highlightMode
	 *            the highlight mode to set, null values are not allowed and
	 *            will be ignored.
	 */
	public void setHighlightMode(HighlightMode highlightMode);

}

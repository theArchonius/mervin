/*******************************************************************************
 * Copyright (c) 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.swt.text.styles;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.graphics.TextStyle;

/**
 * A {@link StyledString.Styler} that applies a list of
 * {@link StyledString.Stylers} in the given order. Note that some attributes
 * might get overridden by subsequent stylers.
 * 
 * @author Florian Zoubek
 *
 */
public class ComposedStyler extends Styler {

	private Styler[] stylers;

	/**
	 * @param stylers
	 *            the stylers in the order to apply them.
	 */
	public ComposedStyler(Styler... stylers) {
		this.stylers = stylers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.StyledString.Styler#applyStyles(org.eclipse.swt
	 * .graphics.TextStyle)
	 */
	@Override
	public void applyStyles(TextStyle textStyle) {
		for (Styler styler : stylers) {
			styler.applyStyles(textStyle);
		}
	}

}

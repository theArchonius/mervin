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

import at.bitandart.zoubek.mervin.draw2d.figures.overlay.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.OverlayType;

/**
 * An {@link StyledString.Styler} that colors a styled string based on a given
 * {@link OverlayType} using the colors from the given
 * {@link IOverlayTypeStyleAdvisor}.
 * 
 * @author Florian Zoubek
 *
 */
public class OverlayTypeStyler extends Styler {

	private OverlayType type;
	private IOverlayTypeStyleAdvisor styleAdvisor;

	public OverlayTypeStyler(OverlayType type, IOverlayTypeStyleAdvisor styleAdvisor) {
		super();
		this.type = type;
		this.styleAdvisor = styleAdvisor;
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
		textStyle.foreground = styleAdvisor.getForegroundColorForOverlayType(type);
	}

}

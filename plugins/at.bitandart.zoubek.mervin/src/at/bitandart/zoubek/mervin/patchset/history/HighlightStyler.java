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
package at.bitandart.zoubek.mervin.patchset.history;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

/**
 * A {@link Styler} responsible for the style of highlighted elements. This
 * class allocates memory and must be explicitly disposed via {@link #dispose()}
 * if it is not used any more.
 * 
 * @author Florian Zoubek
 *
 */
public class HighlightStyler extends StyledString.Styler {

	private Map<Font, Font> highlightFonts;
	private Display display;

	public HighlightStyler(Display display) {
		this.display = display;
		highlightFonts = new HashMap<Font, Font>();
	}

	@Override
	public void applyStyles(TextStyle textStyle) {

		/*
		 * elements are highlighted with a bold font, so adapt the currently
		 * used font and cache the adapted font
		 */
		Font font = textStyle.font;
		if (font == null) {
			font = display.getSystemFont();
		}

		Font highlightFont = highlightFonts.get(font);
		if (highlightFont == null) {
			highlightFont = FontDescriptor.createFrom(font).setStyle(SWT.BOLD).createFont(display);
			highlightFonts.put(font, highlightFont);
		}

		textStyle.font = highlightFont;

	}

	public void dispose() {

		// dispose created bold fonts
		for (Entry<Font, Font> entry : highlightFonts.entrySet()) {
			entry.getValue().dispose();
		}

	}

}
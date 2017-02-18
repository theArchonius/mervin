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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

/**
 * A {@link StyledString.Styler} that derives the font from the existing font.
 * This class allocates memory and must be explicitly disposed via
 * {@link #dispose()} if it is not used any more.
 * 
 * @author Florian Zoubek
 *
 */
public abstract class FontStyler extends StyledString.Styler {

	private Map<Font, Font> customFonts;
	private Display display;

	public FontStyler(Display display) {
		this.display = display;
		customFonts = new HashMap<Font, Font>();
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

		Font customFont = customFonts.get(font);
		if (customFont == null) {
			customFont = deriveFont(FontDescriptor.createFrom(font)).createFont(display);
			customFonts.put(font, customFont);
		}

		textStyle.font = customFont;

	}

	protected abstract FontDescriptor deriveFont(FontDescriptor fontDescriptor);

	public void dispose() {

		// dispose created fonts
		for (Entry<Font, Font> entry : customFonts.entrySet()) {
			entry.getValue().dispose();
		}

	}

}

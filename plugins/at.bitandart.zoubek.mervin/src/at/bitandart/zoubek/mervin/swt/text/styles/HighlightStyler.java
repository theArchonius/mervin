/*******************************************************************************
 * Copyright (c) 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.swt.text.styles;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * A {@link FontStyler} responsible for the style of highlighted elements. This
 * class allocates memory and must be explicitly disposed via {@link #dispose()}
 * if it is not used any more.
 * 
 * @author Florian Zoubek
 *
 */
public class HighlightStyler extends FontStyler {

	public HighlightStyler(Display display) {
		super(display);
	}

	@Override
	protected FontDescriptor deriveFont(FontDescriptor fontDescriptor) {
		return fontDescriptor.withStyle(SWT.BOLD);
	}

}
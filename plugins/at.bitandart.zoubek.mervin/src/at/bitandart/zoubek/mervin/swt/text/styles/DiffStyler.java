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

import org.eclipse.emf.compare.Diff;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * A {@link FontStyler} that applies the style for a string representing a
 * {@link Diff}. This class allocates memory and must be explicitly disposed via
 * dispose() if it is not used any more.
 * 
 * @author Florian Zoubek
 *
 */
public class DiffStyler extends FontStyler {

	public DiffStyler(Display display) {
		super(display);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.swt.text.styles.FontStyler#deriveFont(org.
	 * eclipse.jface.resource.FontDescriptor)
	 */
	@Override
	protected FontDescriptor deriveFont(FontDescriptor fontDescriptor) {
		return fontDescriptor.withStyle(SWT.ITALIC);
	}

}

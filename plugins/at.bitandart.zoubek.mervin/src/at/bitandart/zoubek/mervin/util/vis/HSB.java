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
package at.bitandart.zoubek.mervin.util.vis;

import org.eclipse.swt.SWT;

/**
 * @author Florian Zoubek
 *
 */
public class HSB {
	public float hue;
	public float saturation;
	public float brightness;

	public HSB(float hue, float saturation, float brightness) {
		if (hue < 0 || hue > 360 || saturation < 0 || saturation > 1
				|| brightness < 0 || brightness > 1) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
	}
}

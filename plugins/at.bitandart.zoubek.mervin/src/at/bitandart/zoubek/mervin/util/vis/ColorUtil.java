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
package at.bitandart.zoubek.mervin.util.vis;

import org.eclipse.swt.graphics.RGB;

/**
 * Utility class providing basic color operations.
 * 
 * @author Florian Zoubek
 *
 */
public class ColorUtil {

	/**
	 * calculates the contrast ratio as defined in
	 * {@link https://www.w3.org/TR/WCAG20/#contrast-ratiodef}. The RGB values
	 * may be passed in any order.
	 * 
	 * @param rgb
	 *            one of the colors to calculate the contrast ratio for.
	 * @param rgb2
	 *            the other color.
	 * @return the contrast ratio.
	 * @see https://www.w3.org/TR/WCAG20/#contrast-ratiodef
	 */
	public static double calculateContrastRatio(RGB rgb, RGB rgb2) {
		double luminance1 = calculateRelativeLuminance(rgb);
		double luminance2 = calculateRelativeLuminance(rgb2);

		double lighterLuminance = Math.max(luminance1, luminance2);
		double darkerLuminance = Math.min(luminance1, luminance2);

		return (lighterLuminance + 0.05) / (darkerLuminance + 0.05);
	}

	/**
	 * calculates the relative luminance of the given RGB values as defined in
	 * {@link https://www.w3.org/TR/WCAG20/#relativeluminancedef}.
	 * 
	 * @param rgb
	 *            the {@link RGB} values to calculate the luminance for.
	 * @return the luminance in the range (0.1), where 0 is the darkest value
	 *         and 1 is brightest value.
	 * @see https://www.w3.org/TR/WCAG20/#relativeluminancedef
	 */
	public static double calculateRelativeLuminance(RGB rgb) {

		double sR = rgb.red / 255.0;
		double sG = rgb.green / 255.0;
		double sB = rgb.blue / 255.0;

		double r = 0.0;
		double g = 0.0;
		double b = 0.0;

		if (sR <= 0.03928) {
			r = sR / 12.92;
		} else {
			r = Math.pow((sR + 0.055) / 1.055, 2.4);
		}
		if (sG <= 0.03928) {
			g = sG / 12.92;
		} else {
			g = Math.pow((sG + 0.055) / 1.055, 2.4);
		}
		if (sB <= 0.03928) {
			b = sB / 12.92;
		} else {
			b = Math.pow((sB + 0.055) / 1.055, 2.4);
		}

		return 0.2126 * r + 0.7152 * g + 0.0722 * b;
	}

	/**
	 * blends the given foreground color with the given background color using
	 * the given alpha value.
	 * 
	 * @param foreground
	 *            the foreground color to blend.
	 * @param alpha
	 *            the alpha value in the range (0,1) to blend the foreground
	 *            color over the background color. An alpha value of 1 means
	 *            that the foreground color is blended over the background color
	 *            completely, an alpha value of 0 means the the foreground is
	 *            completely transparent, resulting in the background color.
	 * @param background
	 *            the background color to blend.
	 * @return the blended color {@link RGB} values.
	 */
	public static RGB blend(RGB foreground, double alpha, RGB background) {
		double invAlpha = 1.0 - alpha;
		return new RGB((int) Math.round(foreground.red * alpha + background.red * invAlpha),
				(int) Math.round(foreground.green * alpha + background.green * invAlpha),
				(int) Math.round(foreground.blue * alpha + background.blue * invAlpha));
	}

}

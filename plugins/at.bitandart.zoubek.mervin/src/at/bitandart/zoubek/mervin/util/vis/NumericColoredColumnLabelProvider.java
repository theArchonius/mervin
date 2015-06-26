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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * The base class for {@link NumericColumnLabelProvider}s that color the
 * background of a cell according to its value and provided minimum and maximum
 * values. The color is determined by interpolating to given colors in the HSB
 * color space. Two different colors can be specified for the foreground of the
 * cell, which are chosen depending on their contrast to the calculated
 * background.
 * 
 * @author Florian Zoubek
 *
 */
public abstract class NumericColoredColumnLabelProvider extends
		NumericColumnLabelProvider {

	private Map<Object, Color> colors = new HashMap<>();

	// store in HSB as we interpolate in HSB - one RGB value (e.g. white) might
	// be represented by many HSB values, so we need the HSB values for correct
	// interpolation
	private HSB minHSB;
	private HSB maxHSB;

	private Color fgColor1;
	private Color fgColor2;
	private float fgColor1Brightness;
	private float fgColor2Brightness;

	public NumericColoredColumnLabelProvider(HSB minHSB, HSB maxHSB,
			Color fgColor1, Color fgColor2) {
		this.minHSB = minHSB;
		this.maxHSB = maxHSB;
		this.fgColor1 = fgColor1;
		this.fgColor2 = fgColor2;
		fgColor1Brightness = getBrightness(this.fgColor1.getRGB());
		fgColor2Brightness = getBrightness(this.fgColor2.getRGB());
	}

	@Override
	public Color getBackground(Object element) {
		if (hasValue(element)) {

			RGB rgb = computeRGB(getValue(element), getMinValue(element),
					getMaxValue(element));
			Color color = new Color(Display.getCurrent(), rgb);

			if (colors.containsKey(element)) {
				colors.get(element).dispose();
			}
			colors.put(element, color);

			return color;
		}
		return null;
	}

	/**
	 * computes the RGB value for the given value and range by interpolating
	 * linearly between the colors of this provider.
	 * 
	 * @param value
	 * @param minValue
	 * @param maxValue
	 * @return the computed color for the given value
	 */
	private RGB computeRGB(float value, float minValue, float maxValue) {

		RGB rgb = new RGB((float) map(value, minValue, maxValue, minHSB.hue,
				maxHSB.hue), (float) map(value, minValue, maxValue,
				minHSB.saturation, maxHSB.saturation), (float) map(value,
				minValue, maxValue, minHSB.brightness, maxHSB.brightness));
		return rgb;
	}

	/**
	 * maps the value of the given source range to the destination range.
	 * 
	 * @param value
	 * @param srcMin
	 * @param srcMax
	 * @param destMin
	 * @param destMax
	 * @return the value mapped into the destination range
	 */
	private double map(double value, double srcMin, double srcMax,
			double destMin, double destMax) {
		double srcRange = srcMax - srcMin;
		double destRange = destMax - destMin;
		return destMin + (destRange / srcRange * (value - srcMin));
	}

	/**
	 * @param rgb
	 * @return the brightness of the given RGB color
	 */
	private float getBrightness(RGB rgb) {
		return ((rgb.red * 299) + (rgb.green * 587) + (rgb.blue * 114)) / 1000.0f;
	}

	/**
	 * 
	 * @param rgb
	 * @param rgb2
	 * @return the accumulated absolute difference between the components of the
	 *         RGB values.
	 */
	private float getColorDifference(RGB rgb, RGB rgb2) {
		return Math.abs(rgb.red - rgb2.red) + Math.abs(rgb.green - rgb2.green)
				+ Math.abs(rgb.blue - rgb2.blue);
	}

	@Override
	public Color getForeground(Object element) {
		if (hasValue(element)) {

			RGB rgb = computeRGB(getValue(element), getMinValue(element),
					getMaxValue(element));
			float brightness = getBrightness(rgb);
			// Difference metric based on
			// http://www.w3.org/TR/AERT#color-contrast
			float fgColor1Diff = Math.abs(brightness - fgColor1Brightness)
					+ getColorDifference(fgColor1.getRGB(), rgb);
			float fgColor2Diff = Math.abs(brightness - fgColor2Brightness)
					+ getColorDifference(fgColor2.getRGB(), rgb);

			if (fgColor2Diff > fgColor1Diff) {
				return fgColor2;
			} else {
				return fgColor1;
			}
		}
		return null;
	}

	@Override
	public void dispose() {
		for (Color color : colors.values()) {
			color.dispose();
		}
		super.dispose();
	}

	/**
	 * @param element
	 * @return the maximum value used to calculate the final cell color, this
	 *         value must at least satisfy the condition
	 *         <code>maxVal &gt;= value</code> for all values returned by
	 *         {@link #getValue(Object)}
	 */
	public abstract float getMaxValue(Object element);

	/**
	 * @param element
	 * @return the minimum value used to calculate the final cell color, this
	 *         value must at least satisfy the condition
	 *         <code>minVal &lt;= value</code> for all values returned by
	 *         {@link #getValue(Object)}
	 */
	public abstract float getMinValue(Object element);

}
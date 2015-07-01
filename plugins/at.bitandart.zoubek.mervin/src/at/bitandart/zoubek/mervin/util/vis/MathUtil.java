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

/**
 * This class provides utility methods for common math operations.
 * 
 * @author Florian Zoubek
 *
 */
public class MathUtil {

	/**
	 * maps the value of the given source range to the destination range.
	 * 
	 * @param value the value to map
	 * @param srcMin the lower bound of the source range (inclusive)
	 * @param srcMax the upper bound of the source range (inclusive)
	 * @param destMin the lower bound of the destination range (inclusive)
	 * @param destMax the upper bound of the destination range (inclusive)
	 * @return the value mapped into the destination range
	 */
	public static double map(double value, double srcMin, double srcMax,
			double destMin, double destMax) {
		double srcRange = srcMax - srcMin;
		double destRange = destMax - destMin;
		return destMin + (destRange / srcRange * (value - srcMin));
	}
}

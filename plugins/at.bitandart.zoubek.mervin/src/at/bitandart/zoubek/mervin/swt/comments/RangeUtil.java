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
package at.bitandart.zoubek.mervin.swt.comments;

/**
 * Utility class for range checks.
 * 
 * @author Florian Zoubek
 *
 */
public class RangeUtil {

	/**
	 * checks if the given index is within the specified range (
	 * {@code start <= index <= start+length})
	 * 
	 * @param index
	 *            the index to check.
	 * @param start
	 *            the start index of the range.
	 * @param length
	 *            the length of the range.
	 * @return true if the index is within the range, false otherwise;
	 */
	public static boolean isInRange(int index, int start, int length) {
		return start <= index && index < start + length;
	}

	/**
	 * checks if two ranges overlap.
	 * 
	 * @param firstRangeStart
	 *            the start index of the first range.
	 * @param firstRangeLength
	 *            the length of the first range (must be positive).
	 * @param secondRangeStart
	 *            the start index of the second range.
	 * @param secondRangeLength
	 *            the length of the second range (must be positive).
	 * @return true if the two ranges overlap, false otherwise
	 */
	public static boolean doRangesOverlap(int firstRangeStart, int firstRangeLength, int secondRangeStart,
			int secondRangeLength) {
		return (firstRangeStart <= secondRangeStart && secondRangeStart < firstRangeStart + firstRangeLength)
				|| (secondRangeStart < firstRangeStart && (secondRangeStart + secondRangeLength > firstRangeStart));
	}

}

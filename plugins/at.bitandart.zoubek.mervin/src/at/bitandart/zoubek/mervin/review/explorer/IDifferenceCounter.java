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
package at.bitandart.zoubek.mervin.review.explorer;

import org.eclipse.emf.compare.DifferenceKind;

/**
 * Base interface for classes that provide difference count values for objects
 * in a particular scope. The scope is defined by the implementing class.
 * 
 * @author Florian Zoubek
 *
 */
public interface IDifferenceCounter {

	/**
	 * @param object
	 *            the object to obtain the maximum difference count for.
	 * @return the maximum difference count within the scope that contains the
	 *         given object or -1 if the count cannot be determined. This value
	 *         must be always less that or equal to
	 *         {@link #getTotalDiffCount(Object)} for the same object.
	 */
	public int getMaximumDiffCount(Object object);

	/**
	 * 
	 * @param object
	 *            the object to obtain the total difference count for.
	 * @return the total difference count within the scope that contains the
	 *         given object or -1 if the count cannot be determined. This value
	 *         must be always greater that or equal to
	 *         {@link #getMaximumDiffCount(Object)} for the same object. Also
	 *         this value must be equal to the sum of all difference counts of
	 *         all elements within the same scope as the given object.
	 */
	public int getTotalDiffCount(Object object);

	/**
	 * @param object
	 *            the object to obtain the difference count for.
	 * @return the difference count of the object or -1 if the count cannot be
	 *         determined. The value must be the the same as the sum
	 *         {@link #getDiffCount(Object, DifferenceKind)} with all possible
	 *         {@link DifferenceKind} for the same object.
	 */
	public int getDiffCount(Object object);

	/**
	 * 
	 * @param object
	 *            the object to obtain the difference count for.
	 * @param kind
	 *            the kind to obtain the difference count for.
	 * @return the count of differences with the given {@link DifferenceKind} of
	 *         the object or -1 if the count cannot be determined.
	 */
	public int getDiffCount(Object object, DifferenceKind kind);

}

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

import java.util.Comparator;

import org.eclipse.emf.compare.DifferenceKind;

/**
 * A {@link Comparator} that compares {@link Object}s based on the given
 * {@link IDifferenceCounter}.
 * 
 * @author Florian Zoubek
 *
 */
public class DiffCounterComparator implements Comparator<Object> {

	private IDifferenceCounter diffCounter;
	private DifferenceKind kind;

	/**
	 * creates a new {@link DiffCounterComparator} that compares based on the
	 * number of differences of two objects.
	 * 
	 * @param diffCounter
	 *            the {@link IDifferenceCounter} that provides the difference
	 *            count for the objects to compare.
	 */
	public DiffCounterComparator(IDifferenceCounter diffCounter) {
		this.diffCounter = diffCounter;
	}

	/**
	 * 
	 * creates a new {@link DiffCounterComparator} that compares based on the
	 * number of differences of the given kind of two objects.
	 * 
	 * @param diffCounter
	 *            the {@link IDifferenceCounter} that provides the difference
	 *            count for the objects to compare.
	 * @param kind
	 *            the difference kind to restrict the comparison to, pass null
	 *            for no restriction.
	 */
	public DiffCounterComparator(IDifferenceCounter diffCounter, DifferenceKind kind) {
		this.diffCounter = diffCounter;
		this.kind = kind;
	}

	@Override
	public int compare(Object o1, Object o2) {
		int diff = 0;
		if (kind != null) {
			diff = diffCounter.getDiffCount(o1, kind) - diffCounter.getDiffCount(o2, kind);
		} else {
			diff = diffCounter.getDiffCount(o1) - diffCounter.getDiffCount(o2);
		}
		return diff;
	}

}

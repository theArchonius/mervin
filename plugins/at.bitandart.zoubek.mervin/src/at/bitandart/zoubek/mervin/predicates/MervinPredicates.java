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
package at.bitandart.zoubek.mervin.predicates;

import org.eclipse.emf.compare.Match;

import com.google.common.base.Predicate;

import at.bitandart.zoubek.mervin.predicates.MatchValuesPredicate.Operation;

/**
 * Static utility methods to obtain commonly used predicates for Mervin.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinPredicates {

	/**
	 * @param leftAndRightPredicate
	 *            the predicate to apply to the left and right value.
	 * @param operation
	 *            the operation to apply to the values returned by the
	 *            predicate.
	 * @return a predicate that evaluates to {@code true} if the given predicate
	 *         evaluates to {@code true} for the left and right value combined
	 *         with the given operation. If the match being tested is
	 *         {@code null} this predicate evaluates to {@code false}.
	 */
	public static Predicate<Match> matchValues(Predicate<Object> leftAndRightPredicate, Operation operation) {
		return matchValues(leftAndRightPredicate, leftAndRightPredicate, operation);
	}

	/**
	 * 
	 * @param leftPredicate
	 *            the predicate to apply to the left value.
	 * @param rightPredicate
	 *            the predicate to apply to the right value.
	 * @param operation
	 *            the operation to apply to the values returned by the
	 *            predicates.
	 * @return a predicate that evaluates to {@code true} if the given
	 *         predicates evaluate to {@code true} for corresponding left and
	 *         right values combined with the given operation. If the match
	 *         being tested is {@code null} this predicate evaluates to
	 *         {@code false}.
	 */
	public static Predicate<Match> matchValues(Predicate<Object> leftPredicate, Predicate<Object> rightPredicate,
			Operation operation) {
		return new MatchValuesPredicate(leftPredicate, rightPredicate, operation);
	}
}

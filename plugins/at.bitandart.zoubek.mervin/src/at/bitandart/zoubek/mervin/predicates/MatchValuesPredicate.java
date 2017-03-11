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

/**
 * A {@link Predicate} that applies predicates to the left and right values of
 * matches and combines them using the given {@link Operation} to determine the
 * actual result.
 * 
 * @author Florian Zoubek
 *
 */
public class MatchValuesPredicate implements Predicate<Match> {

	/**
	 * Logical operations supported by {@link MatchValuesPredicate}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public enum Operation {
		/**
		 * logical AND
		 */
		AND,
		/**
		 * logical OR
		 */
		OR,
		/**
		 * logical exclusive OR
		 */
		XOR
	}

	private Predicate<Object> leftPredicate;
	private Predicate<Object> rightPredicate;
	private Operation operation;

	/**
	 * @param leftPredicate
	 *            the predicate applied to the left value.
	 * @param rightPredicate
	 *            the predicate applied to the right value.
	 */
	public MatchValuesPredicate(Predicate<Object> leftPredicate, Predicate<Object> rightPredicate,
			Operation operation) {
		super();
		this.leftPredicate = leftPredicate;
		this.rightPredicate = rightPredicate;
		this.operation = operation;
	}

	@Override
	public boolean apply(Match match) {

		if (match != null) {
			switch (operation) {
			case AND:
				return leftPredicate.apply(match.getLeft()) && rightPredicate.apply(match.getRight());

			case OR:
				return leftPredicate.apply(match.getLeft()) || rightPredicate.apply(match.getRight());

			case XOR:
				boolean leftValue = leftPredicate.apply(match.getLeft());
				boolean rightValue = rightPredicate.apply(match.getRight());
				return leftValue && !rightValue || !leftValue && rightValue;
			}
		}

		return false;
	}

}

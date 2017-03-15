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

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.resource.Resource;

import com.google.common.base.Predicate;

import at.bitandart.zoubek.mervin.gerrit.GitURIParser;
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

	/**
	 * @param kind
	 * @return a predicate that evaluates to {@code true} if the given
	 *         {@link Diff} has the given {@link DifferenceKind}. If the
	 *         {@link Diff} being tested is {@code null} this predicate
	 *         evaluates to {@code false}.
	 */
	public static Predicate<Diff> diffKind(DifferenceKind kind) {
		return new DiffKindPredicate(kind);
	}

	/**
	 * @return a {@link Resource} predicate that returns true if the resource is
	 *         a mervin git resource.
	 */
	public static Predicate<Resource> mervinGitResource() {
		return PREDICATE_GIT_RESOURCE;
	}

	/**
	 * @return a {@link Resource} predicate that returns true if the resource is
	 *         a diagram resource.
	 */
	public static Predicate<Resource> diagramResource() {
		return PREDICATE_DIAGRAM_RESOURCE;
	}

	/**
	 * @return a {@link Resource} predicate that returns true if the resource is
	 *         a model resource (excluding diagram resources).
	 */
	public static Predicate<Resource> modelResource() {
		return PREDICATE_MODEL_RESOURCE;
	}

	/**
	 * A {@link Resource} predicate that returns true if the resource is a
	 * mervin git resource.
	 */
	private static final Predicate<Resource> PREDICATE_GIT_RESOURCE = new Predicate<Resource>() {

		@Override
		public boolean apply(Resource resource) {
			org.eclipse.emf.common.util.URI uri = resource.getURI();
			return uri.scheme().equals(GitURIParser.GIT_COMMIT_SCHEME);
		}
	};

	/**
	 * A {@link Resource} predicate that returns true if the resource is a
	 * diagram resource.
	 */
	private static final Predicate<Resource> PREDICATE_DIAGRAM_RESOURCE = new Predicate<Resource>() {

		@Override
		public boolean apply(Resource resource) {
			org.eclipse.emf.common.util.URI uri = resource.getURI();
			return uri.fileExtension().equals("notation");
		}
	};

	/**
	 * A {@link Resource} predicate that returns true if the resource is a model
	 * resource(excluding diagram resources).
	 */
	private static final Predicate<Resource> PREDICATE_MODEL_RESOURCE = new Predicate<Resource>() {

		@Override
		public boolean apply(Resource resource) {
			org.eclipse.emf.common.util.URI uri = resource.getURI();
			return !uri.fileExtension().equals("notation");
		}
	};
}

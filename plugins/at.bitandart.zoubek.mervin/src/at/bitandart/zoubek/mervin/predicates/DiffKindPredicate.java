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

import com.google.common.base.Predicate;

/**
 * A {@link Predicate} that evaluates to true if the kind of a {@link Diff} is
 * equal to the given {@link DifferenceKind}. Evaluates to false for null
 * values.
 * 
 * @author Florian Zoubek
 *
 */
public class DiffKindPredicate implements Predicate<Diff> {

	private DifferenceKind kind;

	public DiffKindPredicate(DifferenceKind kind) {
		this.kind = kind;
	}

	@Override
	public boolean apply(Diff input) {
		if (input == null) {
			return false;
		}
		return input.getKind() == kind;
	}
}

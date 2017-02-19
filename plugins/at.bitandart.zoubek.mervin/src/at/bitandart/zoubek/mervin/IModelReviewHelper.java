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
package at.bitandart.zoubek.mervin;

import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * Base interface for helper classes providing shared operations for
 * {@link ModelReview}s using a specifc strategy.
 * 
 * @author Florian Zoubek
 *
 */
public interface IModelReviewHelper {

	/**
	 * calculates the differences that reference the given {@link EObject} in
	 * the given {@link ModelReview}.
	 * 
	 * @param eObject
	 *            the {@link EObject} to find the differences for.
	 * @param modelReview
	 *            the {@link ModelReview} to obtain the difference from.
	 * @return a set of all differences tat reference the given {@link EObject}
	 *         in the given {@link ModelReview}, never null.
	 */
	public Set<Diff> getDifferences(EObject eObject, ModelReview modelReview);

}

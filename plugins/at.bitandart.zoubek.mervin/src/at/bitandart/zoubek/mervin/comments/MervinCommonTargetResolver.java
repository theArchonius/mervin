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
package at.bitandart.zoubek.mervin.comments;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * The default {@link ICommonTargetResolver} implementation for targets within a
 * mervin {@link ModelReview} instance. The common targets are the {@link Match}
 * es of the current selected model comparison or the EObject itself if there is
 * no match for it or if it is not part of a model within the patch set.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinCommonTargetResolver implements ICommonTargetResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.comments.CommonTargetMatcher#getCommonTargets(
	 * java.util.Collection,
	 * at.bitandart.zoubek.mervin.model.modelreview.PatchSet)
	 */
	@Override
	public Set<EObject> findCommonTargets(Collection<EObject> targets, PatchSet patchSet) {

		Set<EObject> commonTargets = null;
		if (patchSet != null) {
			ModelReview review = patchSet.getReview();
			Comparison modelComparison = review.getSelectedModelComparison();
			commonTargets = new HashSet<>();
			for (EObject target : targets) {

				EObject commonTarget = null;
				Match match = modelComparison.getMatch(target);
				if (match != null) {
					commonTarget = match;
				}

				if (commonTarget != null) {
					commonTargets.add(commonTarget);
				} else {
					// no common target found, use the target itself instead
					commonTargets.add(target);
				}

			}
		} else {
			// no context patch set, the targets itself are the common targets
			commonTargets = new HashSet<>(targets);
		}

		return commonTargets;
	}

}

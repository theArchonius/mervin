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
package at.bitandart.zoubek.mervin.patchset.history;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import at.bitandart.zoubek.mervin.IMatchHelper;

/**
 * An {@link IEObjectMatcher} that matches patch set comparison {@link Diff}s
 * based on their containing matches. The old values of the given {@link Diff}
 * {@link Match}es on the left and right side are considered to be contained in
 * the same model. The new values are considered to be contained in different
 * models. This matcher support only two-way matches.
 * 
 * This matcher works by separating {@link Diff} by their common {@link EObject}
 * {@link Match} and {@link EStructuralFeature}. Afterwards the given matcher is
 * applied to each created {@link Diff} collection to match them. The given
 * matcher is not called if no separation of {@link Diff}s exists on one side
 * but on the other side. Matches will be created by
 * {@link PatchSetHistoryDiffMatcher} in that case. Non-Diff EObjects will also
 * be separated in own collections and matched using the given matcher.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetHistoryDiffMatcher implements IEObjectMatcher {

	private IEObjectMatcher eObjectMatcher;

	private IMatchHelper matchHelper;

	/**
	 * 
	 * @param eObjectMatcher
	 *            the matcher used to match the subsets of the given objects to
	 *            match.
	 * @param matchHelper
	 *            the {@link IMatchHelper} to determine the old an new versions
	 *            of matches.
	 */
	public PatchSetHistoryDiffMatcher(IEObjectMatcher eObjectMatcher, IMatchHelper matchHelper) {
		this.eObjectMatcher = eObjectMatcher;
		this.matchHelper = matchHelper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.compare.match.eobject.IEObjectMatcher#createMatches(org.
	 * eclipse.emf.compare.Comparison, java.util.Iterator, java.util.Iterator,
	 * java.util.Iterator, org.eclipse.emf.common.util.Monitor)
	 */
	@Override
	public void createMatches(Comparison comparison, Iterator<? extends EObject> leftEObjects,
			Iterator<? extends EObject> rightEObjects, Iterator<? extends EObject> originEObjects, Monitor monitor) {

		List<EObject> unmatchedLeftObjects = new LinkedList<>();
		List<EObject> unmatchedRightObjects = new LinkedList<>();

		/*
		 * These indices hold diffs that can be related to the same EObjects
		 * using the key of the first map
		 */
		Map<EObject, Multimap<EStructuralFeature, Diff>> leftDiffs = new HashMap<>();
		Map<EObject, Multimap<EStructuralFeature, Diff>> rightDiffs = new HashMap<>();

		/*
		 * These indices hold diffs that use the new value as key and cannot be
		 * related to the same EObjects using the key of the first map unless
		 * they are matched.
		 */
		Map<EObject, Multimap<EStructuralFeature, Diff>> leftNewDiffs = new HashMap<>();
		Map<EObject, Multimap<EStructuralFeature, Diff>> rightNewDiffs = new HashMap<>();

		Set<EObject> newLeftObjects = new HashSet<EObject>();
		Set<EObject> newRightObjects = new HashSet<EObject>();

		/*
		 * collect the diffs on each side which can be related without matching
		 * the diff's match values
		 */
		indexDiffs(leftEObjects, unmatchedLeftObjects, leftDiffs, leftNewDiffs, newLeftObjects);
		indexDiffs(rightEObjects, unmatchedRightObjects, rightDiffs, rightNewDiffs, newRightObjects);

		/*
		 * match the new values of the diff that cannot be related without
		 * matching
		 */
		Comparison newObjectComparison = CompareFactory.eINSTANCE.createComparison();
		eObjectMatcher.createMatches(newObjectComparison, newLeftObjects.iterator(), newRightObjects.iterator(),
				Collections.<EObject> emptyList().iterator(), monitor);

		/*
		 * use the mapping information as key and copy them in the main diff
		 * indices -> now they can be related
		 */
		mergeDiffIndices(leftNewDiffs, leftDiffs, newObjectComparison);
		mergeDiffIndices(rightNewDiffs, rightDiffs, newObjectComparison);

		/* now match based on the separated collections in the indices */
		matchLeftDiffCandidates(comparison, monitor, leftDiffs, rightDiffs);
		/*
		 * all possible candidates have been matched, so add matches for the
		 * remaining diffs on the right side
		 */
		addRemainingRightDiffs(comparison, rightDiffs);

		eObjectMatcher.createMatches(comparison, unmatchedLeftObjects.iterator(), unmatchedRightObjects.iterator(),
				Collections.<EObject> emptyList().iterator(), monitor);

	}

	/**
	 * matches the left diff candidates with the right diff candidates and
	 * removes all right diff candidates form the collection once they have been
	 * matched.
	 * 
	 * @param comparison
	 *            the comaprison to store the matches into.
	 * @param monitor
	 *            the task monitor passed to
	 *            {@link #createMatches(Comparison, Iterator, Iterator, Iterator, Monitor)}
	 *            .
	 * @param leftDiffs
	 *            the left diff candidates to match.
	 * @param rightDiffs
	 *            the right diff candidates to match.
	 */
	private void matchLeftDiffCandidates(Comparison comparison, Monitor monitor,
			Map<EObject, Multimap<EStructuralFeature, Diff>> leftDiffs,
			Map<EObject, Multimap<EStructuralFeature, Diff>> rightDiffs) {

		for (Entry<EObject, Multimap<EStructuralFeature, Diff>> entry : leftDiffs.entrySet()) {

			EObject key = entry.getKey();
			Multimap<EStructuralFeature, Diff> leftFeatureMap = entry.getValue();
			Multimap<EStructuralFeature, Diff> rightFeatureMap = rightDiffs.remove(key);

			if (rightFeatureMap != null) {

				Set<EStructuralFeature> leftFeatureSet = leftFeatureMap.keySet();

				for (EStructuralFeature leftFeature : leftFeatureSet) {
					Collection<Diff> rightFeatureDiffs = rightFeatureMap.removeAll(leftFeature);
					Collection<Diff> leftFeatureDiffs = leftFeatureMap.get(leftFeature);
					eObjectMatcher.createMatches(comparison, leftFeatureDiffs.iterator(), rightFeatureDiffs.iterator(),
							Collections.<EObject> emptyList().iterator(), monitor);
				}

				/*
				 * all left diffs have been matched, so add remaining right diff
				 * matches
				 */
				Set<EStructuralFeature> rightFeatureSet = new HashSet<>(rightFeatureMap.keySet());

				for (EStructuralFeature rightFeature : rightFeatureSet) {

					Collection<Diff> rightFeatureDiffs = rightFeatureMap.removeAll(rightFeature);

					for (Diff rightDiff : rightFeatureDiffs) {
						Match match = CompareFactory.eINSTANCE.createMatch();
						match.setRight(rightDiff);
						comparison.getMatches().add(match);
					}

				}

			} else {

				/*
				 * no candidates on the right side, so add matches for only the
				 * left diffs
				 */

				Set<EStructuralFeature> leftFeatureSet = leftFeatureMap.keySet();

				for (EStructuralFeature leftFeature : leftFeatureSet) {

					Collection<Diff> leftFeatureDiffs = leftFeatureMap.get(leftFeature);

					for (Diff leftDiff : leftFeatureDiffs) {
						Match match = CompareFactory.eINSTANCE.createMatch();
						match.setLeft(leftDiff);
						comparison.getMatches().add(match);
					}
				}

			}
		}
	}

	/**
	 * Adds right side only matches for the given right diff candidates.
	 * 
	 * @param comparison
	 *            the comparison to add the matches to.
	 * @param rightDiffs
	 *            the right diff candidates to create matches for.
	 */
	private void addRemainingRightDiffs(Comparison comparison,
			Map<EObject, Multimap<EStructuralFeature, Diff>> rightDiffs) {

		for (Entry<EObject, Multimap<EStructuralFeature, Diff>> entry : rightDiffs.entrySet()) {

			Multimap<EStructuralFeature, Diff> rightFeatureMap = entry.getValue();

			Set<EStructuralFeature> rightFeatureSet = rightFeatureMap.keySet();

			for (EStructuralFeature rightFeature : rightFeatureSet) {

				Collection<Diff> rightFeatureDiffs = rightFeatureMap.get(rightFeature);

				for (Diff rightDiff : rightFeatureDiffs) {
					Match match = CompareFactory.eINSTANCE.createMatch();
					match.setRight(rightDiff);
					comparison.getMatches().add(match);
				}

			}
		}
	}

	/**
	 * merges the given diff index in the final diff index using the matching
	 * information form the given comparison.
	 * 
	 * @param diffsToMerge
	 *            the diff index to merge.
	 * @param finalDiffs
	 *            the diff index to merge the other diff into.
	 * @param matchComparison
	 *            the {@link Comparison} providing the matching information for
	 *            the {@link EObject} keys in the diff index to merge.
	 */
	private void mergeDiffIndices(Map<EObject, Multimap<EStructuralFeature, Diff>> diffsToMerge,
			Map<EObject, Multimap<EStructuralFeature, Diff>> finalDiffs, Comparison matchComparison) {

		for (Entry<EObject, Multimap<EStructuralFeature, Diff>> entry : diffsToMerge.entrySet()) {

			EObject object = entry.getKey();
			Match match = matchComparison.getMatch(object);

			if (match != null) {

				Multimap<EStructuralFeature, Diff> multimap = finalDiffs.get(match);

				if (multimap == null) {
					finalDiffs.put(match, entry.getValue());
				} else {
					multimap.putAll(entry.getValue());
				}

			}
		}
	}

	/**
	 * @param eObjects
	 * @param unmatchedObjects
	 * @param diffCache
	 * @param newDiffCache
	 */
	private void indexDiffs(Iterator<? extends EObject> eObjects, List<EObject> unmatchedObjects,
			Map<EObject, Multimap<EStructuralFeature, Diff>> diffCache,
			Map<EObject, Multimap<EStructuralFeature, Diff>> newDiffCache, Set<EObject> newEObjects) {
		while (eObjects.hasNext()) {

			EObject leftObject = eObjects.next();

			if (leftObject instanceof Diff) {

				Diff leftDiff = (Diff) leftObject;

				EObject oldValue = matchHelper.getOldValue(leftDiff.getMatch());

				EStructuralFeature structuralFeature = MatchUtil.getStructuralFeature(leftDiff);

				if (structuralFeature != null) {
					if (oldValue != null) {

						assignDiff(leftDiff, oldValue, structuralFeature, diffCache);

					} else {

						EObject newValue = matchHelper.getNewValue(leftDiff.getMatch());
						if (newValue != null) {

							assignDiff(leftDiff, newValue, structuralFeature, newDiffCache);
							newEObjects.add(newValue);

						} else {
							unmatchedObjects.add(leftObject);
						}
					}

				} else {
					unmatchedObjects.add(leftObject);
				}

			} else {
				unmatchedObjects.add(leftObject);
			}
		}
	}

	/**
	 * @param leftDiff
	 * @param oldValue
	 * @param structuralFeature
	 * @param diffsCache
	 */
	private void assignDiff(Diff leftDiff, EObject oldValue, EStructuralFeature structuralFeature,
			Map<EObject, Multimap<EStructuralFeature, Diff>> diffsCache) {
		Multimap<EStructuralFeature, Diff> featureMap = diffsCache.get(oldValue);
		if (featureMap == null) {
			featureMap = HashMultimap.create();
			diffsCache.put(oldValue, featureMap);
		}
		featureMap.get(structuralFeature).add(leftDiff);
	}

}

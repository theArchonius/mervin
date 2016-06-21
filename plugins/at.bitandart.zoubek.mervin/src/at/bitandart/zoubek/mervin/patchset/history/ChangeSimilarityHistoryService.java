/*******************************************************************************
 * Copyright (c) 2015, 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.patchset.history;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.eobject.EditionDistance;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.util.vis.MathUtil;

/**
 * A {@link IPatchSetHistoryService} that calculates the history of similarities
 * of a {@link Diff} object to the best matching {@link Diff} in a
 * {@link PatchSet}.
 * 
 * @author Florian Zoubek
 *
 */
public class ChangeSimilarityHistoryService implements ISimilarityHistoryService {

	@Override
	public IPatchSetHistoryEntry<Diff, DiffWithSimilarity> createEntryFor(Diff object, Collection<PatchSet> patchSets,
			Object context) {

		PatchSetHistoryEntryImpl<Diff, DiffWithSimilarity> entry = new PatchSetHistoryEntryImpl<Diff, DiffWithSimilarity>(
				object);
		updateEntryFor(entry, patchSets, context);

		return entry;
	}

	@Override
	public void updateEntryFor(IPatchSetHistoryEntry<Diff, DiffWithSimilarity> entry, Collection<PatchSet> patchSets,
			Object context) {

		for (PatchSet patchSet : patchSets) {
			updateEntryFor(entry, patchSet, context);
		}

	}

	@Override
	public void updateEntryFor(IPatchSetHistoryEntry<Diff, DiffWithSimilarity> entry, PatchSet patchSet,
			Object context) {

		Diff entryObject = entry.getEntryObject();
		Comparison comparison = null;
		Match match = null;

		if (context instanceof Map) {
			/* try to find the match in the cache */
			Object object = ((Map<?, ?>) context).get(patchSet);
			if (object instanceof Comparison) {
				comparison = ((Comparison) object);
				match = comparison.getMatch(entryObject);
			}
		}

		if (match == null) {
			/*
			 * fallback: try to find the match by matching the entry diff with
			 * the diffs of the patch set
			 */
			comparison = CompareFactory.eINSTANCE.createComparison();
			match = findMatch(entryObject, patchSet, comparison);
		}

		EObject oppositeObject = getOppositeObject(match, entryObject);

		if (oppositeObject instanceof Diff) {

			double distance = calculateDistance(comparison, entryObject, oppositeObject);
			entry.setValue(patchSet, new DiffWithSimilarity((Diff) oppositeObject, distance));

		}

		postProcessEntry(entry, patchSet, match, comparison);

	}

	/**
	 * extract the opposite object in the given match.
	 * 
	 * @param match
	 *            the match to extract the opposite object from.
	 * @param object
	 *            the object whose opposite should extracted.
	 * @return the opposite object or null if the given object has no opposite
	 *         or if the given object is not present in the match.
	 */
	private EObject getOppositeObject(Match match, Object object) {

		if (match != null) {

			EObject left = match.getLeft();
			EObject right = match.getRight();

			if (left == object) {
				return right;
			} else if (right == object) {
				return left;
			}
		}
		return null;
	}

	/**
	 * called once after
	 * {@link #updateEntryFor(IPatchSetHistoryEntry, PatchSet)} has finished.
	 * 
	 * @param entry
	 * @param patchSet
	 * @param match
	 *            the matching diff in the patchSet, or null if none has been
	 *            found
	 * @param comparison
	 */
	protected void postProcessEntry(IPatchSetHistoryEntry<Diff, DiffWithSimilarity> entry, PatchSet patchSet,
			Match match, Comparison comparison) {
		// intentionally left empty
	}

	/**
	 * matches the given list of diffs and returns a comparison containing the
	 * matches. However, no {@link Diff}s will be created for the matches.
	 * 
	 * @param leftEObjects
	 *            the list of diffs on the left side to match.
	 * @param rightEObjects
	 *            the list of diffs on the right side to match.
	 * @return a comparison containing the matches but without {@link Diff}s.
	 */
	protected Comparison matchDiffs(List<? extends EObject> leftEObjects, List<? extends EObject> rightEObjects) {

		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		IEObjectMatcher objectMatcher = DefaultMatchEngine.createDefaultEObjectMatcher(UseIdentifiers.WHEN_AVAILABLE);

		objectMatcher.createMatches(comparison, leftEObjects.iterator(), rightEObjects.iterator(),
				new ArrayList<EObject>().iterator(), new BasicMonitor());

		return comparison;
	}

	/**
	 * finds the {@link Match} for a given {@link Diff} in the differences of
	 * the given patchset and stores the underlying matching the given
	 * comparison.
	 * 
	 * @param entryObject
	 *            the diff to search the match for.
	 * @param patchSet
	 *            the patch set to retrieve the diffs to match.
	 * @param comparison
	 *            the comparison to store the underlying matches to.
	 * @return the match for the given diff or null if no match has been found.
	 *         The match may contain the given diff on the left or the right
	 *         side.
	 */
	protected Match findMatch(Diff entryObject, PatchSet patchSet, Comparison comparison) {

		// TODO refactor: remove duplicated code here and in matchDiffs()
		IEObjectMatcher objectMatcher = DefaultMatchEngine.createDefaultEObjectMatcher(UseIdentifiers.WHEN_AVAILABLE);
		List<EObject> leftEObjects = new ArrayList<EObject>(1);
		leftEObjects.add(entryObject);

		EList<Diff> modelDifferences = patchSet.getModelComparison().getDifferences();
		EList<Diff> diagramDifferences = patchSet.getDiagramComparison().getDifferences();
		List<EObject> rightEObjects = new ArrayList<EObject>(modelDifferences.size() + diagramDifferences.size());
		rightEObjects.addAll(modelDifferences);
		rightEObjects.addAll(diagramDifferences);

		objectMatcher.createMatches(comparison, leftEObjects.iterator(), rightEObjects.iterator(),
				new ArrayList<EObject>().iterator(), new BasicMonitor());

		return comparison.getMatch(entryObject);
	}

	/**
	 * calculates the distance between two {@link EObject}s in the range 0(not
	 * equal) and 1(equal).
	 * 
	 * @param comparison
	 * @param left
	 * @param right
	 * @return
	 */
	protected double calculateDistance(Comparison comparison, EObject left, EObject right) {

		EditionDistance editionDistance = new EditionDistance(
				WeightProviderDescriptorRegistryImpl.createStandaloneInstance());
		return MathUtil.map(editionDistance.distance(comparison, left, right), 0.0, Double.MAX_VALUE, 1.0, 0.0);
	}

	/**
	 * This method provides a consistent result across all create*Entries()
	 * methods for the same list of patch sets and if the given patch set is
	 * contained in the list of patch sets.
	 * 
	 * @see #createDiagramEntries(PatchSet, List)
	 * @see #createDiagramEntries(List)
	 * @see #createModelEntries(List)
	 * 
	 */
	@Override
	public List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> createModelEntries(PatchSet patchSet,
			List<PatchSet> patchSets, boolean mergeEqualDiffs) {

		EList<Diff> modelDiffs = patchSet.getModelComparison().getDifferences();
		Map<PatchSet, List<Diff>> diffs = new HashMap<>();
		diffs.put(patchSet, modelDiffs);

		List<List<Diff>> patchSetToMatchDiffs = new ArrayList<>(patchSets.size());

		for (PatchSet otherPatchSet : patchSets) {
			patchSetToMatchDiffs.add(otherPatchSet.getModelComparison().getDifferences());
		}

		/*
		 * build the cache for all patch sets in order to be produce a
		 * consistent matching along all create*Entries() methods
		 */
		Map<PatchSet, Map<PatchSet, Comparison>> diffMatchCache = buildDiffMatchCache(patchSets, patchSets,
				patchSetToMatchDiffs, patchSetToMatchDiffs);

		return createEntriesForDiffs(diffs, patchSets, diffMatchCache, mergeEqualDiffs);
	}

	/**
	 * This method provides a consistent result across all create*Entries()
	 * methods for the same list of patch sets and if the given patch set is
	 * contained in the list of patch sets.
	 * 
	 * @see #createDiagramEntries(PatchSet, List)
	 * @see #createDiagramEntries(List)
	 * @see #createModelEntries(List)
	 * 
	 */
	@Override
	public List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> createDiagramEntries(PatchSet patchSet,
			List<PatchSet> patchSets, boolean mergeEqualDiffs) {

		EList<Diff> diagramDiffs = patchSet.getDiagramComparison().getDifferences();
		Map<PatchSet, List<Diff>> diffs = new HashMap<>();
		diffs.put(patchSet, diagramDiffs);

		List<List<Diff>> patchSetToMatchDiffs = new ArrayList<>(patchSets.size());

		for (PatchSet otherPatchSet : patchSets) {
			patchSetToMatchDiffs.add(otherPatchSet.getDiagramComparison().getDifferences());
		}

		/*
		 * build the cache for all patch sets in order to be produce a
		 * consistent matching along all create*Entries() methods
		 */
		Map<PatchSet, Map<PatchSet, Comparison>> diffMatchCache = buildDiffMatchCache(patchSets, patchSets,
				patchSetToMatchDiffs, patchSetToMatchDiffs);

		return createEntriesForDiffs(diffs, patchSets, diffMatchCache, mergeEqualDiffs);
	}

	/**
	 * creates the entries for the given set of differences. No entry will be
	 * created for diffs that are a perfect match (similarity >= 1.0) in an
	 * already created entry.
	 * 
	 * @param differences
	 *            a map describing the {@link Diff}s contained in a particular
	 *            patch set to create entries for.
	 * @param patchSets
	 *            the patch sets to create values for
	 * @param diffMatchCache
	 *            a cache to speed up matching, may be null.
	 * @param mergeEqualDiffs
	 *            true if equal diffs should be merged into a single entry,
	 *            false if an entry should be created for each diff in all patch
	 *            sets.
	 * @return a list of entries for the given differences.
	 */
	private List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> createEntriesForDiffs(
			Map<PatchSet, List<Diff>> differences, List<PatchSet> patchSets,
			Map<PatchSet, Map<PatchSet, Comparison>> diffMatchCache, boolean mergeEqualDiffs) {

		List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> entries = new LinkedList<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>>();
		Set<Diff> processedDiffs = new HashSet<Diff>();

		for (PatchSet diffPatchSet : differences.keySet()) {

			List<Diff> patchSetDiffs = differences.get(diffPatchSet);

			for (Diff diff : patchSetDiffs) {

				if (!processedDiffs.contains(diff)) {

					IPatchSetHistoryEntry<Diff, DiffWithSimilarity> historyEntry = createEntryFor(diff, patchSets,
							diffMatchCache.get(diffPatchSet));
					for (PatchSet patchSet : patchSets) {

						DiffWithSimilarity value = historyEntry.getValue(patchSet);
						if (mergeEqualDiffs && value != null && value.getSimilarity() >= 1.0) {
							processedDiffs.add(value.getDiff());
						}
					}

					entries.add(historyEntry);
				}
			}
		}

		return entries;
	}

	/**
	 * builds a cache containing the matches for each patch set in
	 * {@code patchSetsToCache} with all patch sets in
	 * {@code patchSetsToCompare}.
	 * 
	 * @param patchSetsToCache
	 *            the patch sets to cache matches for.
	 * @param patchSetsToMatch
	 *            the patch sets to match with the previous patch sets to cache.
	 * @param patchSetsToCacheDiffs
	 *            the corresponding diffs for the patch sets to cache.
	 * @param patchSetsToMatchDiffs
	 *            the corresponding diffs for the patch sets to match.
	 * @return a map containing a map for each cached patch set - the map
	 *         describes the diff matches between the patch set to cache and the
	 *         patch sets to match.
	 */
	private Map<PatchSet, Map<PatchSet, Comparison>> buildDiffMatchCache(List<PatchSet> patchSetsToCache,
			List<PatchSet> patchSetsToMatch, List<List<Diff>> patchSetsToCacheDiffs,
			List<List<Diff>> patchSetsToMatchDiffs) {

		Map<PatchSet, Map<PatchSet, Comparison>> matchCache = new HashMap<>();

		ListIterator<PatchSet> currentPatchSetIterator = patchSetsToCache.listIterator();
		while (currentPatchSetIterator.hasNext()) {

			int currentIndex = currentPatchSetIterator.nextIndex();
			PatchSet patchSet = currentPatchSetIterator.next();
			Map<PatchSet, Comparison> patchSetMatches = new HashMap<>();

			ListIterator<PatchSet> otherPatchSetIterator = patchSetsToMatch.listIterator();
			while (otherPatchSetIterator.hasNext()) {

				int otherIndex = otherPatchSetIterator.nextIndex();
				PatchSet otherPatchSet = otherPatchSetIterator.next();

				/*
				 * if an matching exists, reuse it to save computation time and
				 * prevent inconsistency as a matching might be different when
				 * switching sides
				 */
				Map<PatchSet, Comparison> existingMatchingMap = matchCache.get(otherPatchSet);
				if (existingMatchingMap != null) {
					Comparison comparison = existingMatchingMap.get(patchSet);
					if (comparison != null) {
						patchSetMatches.put(otherPatchSet, comparison);
						continue;
					}
				}

				patchSetMatches.put(otherPatchSet,
						matchDiffs(patchSetsToCacheDiffs.get(currentIndex), patchSetsToMatchDiffs.get(otherIndex)));
			}

			matchCache.put(patchSet, patchSetMatches);
		}
		return matchCache;
	}

	/**
	 * This method provides a consistent result across all create*Entries()
	 * methods for the same list of patch sets and if the given patch set is
	 * contained in the list of patch sets.
	 * 
	 * @see #createDiagramEntries(PatchSet, List)
	 * @see #createDiagramEntries(List)
	 * @see #createModelEntries(PatchSet, List)
	 * 
	 */
	@Override
	public List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> createModelEntries(List<PatchSet> patchSets,
			boolean mergeEqualDiffs) {

		Map<PatchSet, List<Diff>> diffs = new HashMap<>();
		List<List<Diff>> patchSetDiffs = new ArrayList<>(patchSets.size());

		for (PatchSet patchSet : patchSets) {
			diffs.put(patchSet, patchSet.getModelComparison().getDifferences());
			patchSetDiffs.add(patchSet.getModelComparison().getDifferences());
		}

		Map<PatchSet, Map<PatchSet, Comparison>> diffMatchCache = buildDiffMatchCache(patchSets, patchSets,
				patchSetDiffs, patchSetDiffs);

		return createEntriesForDiffs(diffs, patchSets, diffMatchCache, mergeEqualDiffs);
	}

	/**
	 * This method provides a consistent result across all create*Entries()
	 * methods for the same list of patch sets and if the given patch set is
	 * contained in the list of patch sets.
	 * 
	 * @see #createDiagramEntries(PatchSet, List)
	 * @see #createModelEntries(PatchSet, List)
	 * @see #createModelEntries(List)
	 * 
	 */
	@Override
	public List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> createDiagramEntries(List<PatchSet> patchSets,
			boolean mergeEqualDiffs) {

		Map<PatchSet, List<Diff>> diffs = new HashMap<>();
		List<List<Diff>> patchSetDiffs = new ArrayList<>(patchSets.size());

		for (PatchSet patchSet : patchSets) {
			diffs.put(patchSet, patchSet.getDiagramComparison().getDifferences());
			patchSetDiffs.add(patchSet.getDiagramComparison().getDifferences());
		}

		Map<PatchSet, Map<PatchSet, Comparison>> diffMatchCache = buildDiffMatchCache(patchSets, patchSets,
				patchSetDiffs, patchSetDiffs);

		return createEntriesForDiffs(diffs, patchSets, diffMatchCache, mergeEqualDiffs);
	}

}

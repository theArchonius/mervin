/*******************************************************************************
 * Copyright (c) 2015, 2016, 2017 Florian Zoubek.
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

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.eobject.EditionDistance;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import at.bitandart.zoubek.mervin.IMatchHelper;
import at.bitandart.zoubek.mervin.IReviewCompareService;
import at.bitandart.zoubek.mervin.IReviewCompareService.Version;
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
public class DiffSimilarityHistoryService implements ISimilarityHistoryService {

	@Inject
	private IMatchHelper matchHelper;

	@Inject
	private IReviewCompareService compareService;

	@Override
	public IPatchSetHistoryEntry<Diff, DiffWithSimilarity> createEntryFor(Diff object, Collection<PatchSet> patchSets,
			Object context, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		PatchSetHistoryEntryImpl<Diff, DiffWithSimilarity> entry = new PatchSetHistoryEntryImpl<Diff, DiffWithSimilarity>(
				object);
		updateEntryFor(entry, patchSets, context, subMonitor.newChild(100));

		return entry;
	}

	@Override
	public void updateEntryFor(IPatchSetHistoryEntry<Diff, DiffWithSimilarity> entry, Collection<PatchSet> patchSets,
			Object context, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, patchSets.size() * 100);

		for (PatchSet patchSet : patchSets) {
			updateEntryFor(entry, patchSet, context, subMonitor.newChild(100));
		}

	}

	@Override
	public void updateEntryFor(IPatchSetHistoryEntry<Diff, DiffWithSimilarity> entry, PatchSet patchSet, Object context,
			IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		Diff entryObject = entry.getEntryObject();

		Collection<Diff> candidates = getCandidates(entryObject, patchSet, context, subMonitor.newChild(50));

		Comparison oldComparison = CompareFactory.eINSTANCE.createComparison();
		Comparison newComparison = CompareFactory.eINSTANCE.createComparison();

		if (context instanceof DiffContext) {
			/* retrieve the comparisons form the context cache if possible */
			DiffContext diffContext = (DiffContext) context;
			PatchSet containingPatchSet = diffContext.getContainingPatchSet();
			PatchSetComparisonCache cache = diffContext.getCache();

			oldComparison = cache.getComparison(containingPatchSet, patchSet, Version.OLD);
			newComparison = cache.getComparison(containingPatchSet, patchSet, Version.NEW);
		}

		Diff bestCandidate = null;
		double bestSimilarity = -1;

		subMonitor.setWorkRemaining(candidates.size() + 10);

		for (Diff candidate : candidates) {

			double distance = calculateSimilarity(oldComparison, newComparison, entryObject, candidate);
			if (distance > bestSimilarity) {
				bestSimilarity = distance;
				bestCandidate = candidate;
			}
			subMonitor.worked(1);
		}

		if (bestCandidate != null) {
			entry.setValue(patchSet, new DiffWithSimilarity(bestCandidate, bestSimilarity));
		}

		postProcessEntry(entry, patchSet, subMonitor.newChild(10));

	}

	/**
	 * returns the collection of diffs from a given patch set to consider when
	 * calculating the similarity of the given diff.
	 * 
	 * @param diff
	 *            the diff to find the similar diffs for.
	 * @param patchSet
	 *            the patch set to retrieve the collection of similar diff
	 *            candidates.
	 * @param context
	 *            a context value that may be used during candidate retrieval.
	 *            The default implementation uses {@link DiffContext}s to
	 *            retrieve cached matchings of patch sets.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @return a collection of diffs from the given patch set comparison that
	 *         may be similar to the given diff.
	 */
	private Collection<Diff> getCandidates(Diff diff, PatchSet patchSet, Object context, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		Set<Diff> candidates = new HashSet<Diff>();

		Set<Diff> uncategorizedDiffs = new HashSet<Diff>();
		uncategorizedDiffs.addAll(patchSet.getModelComparison().getDifferences());
		uncategorizedDiffs.addAll(patchSet.getDiagramComparison().getDifferences());

		EStructuralFeature diffFeature = MatchUtil.getStructuralFeature(diff);
		Match diffMatch = diff.getMatch();
		EObject oldValue = matchHelper.getOldValue(diffMatch);
		EObject newValue = matchHelper.getNewValue(diffMatch);

		Comparison oldComparison = null;
		Comparison newComparison = null;

		if (context instanceof DiffContext) {
			/* retrieve the comparisons form the context cache if possible */
			DiffContext diffContext = (DiffContext) context;
			PatchSet containingPatchSet = diffContext.getContainingPatchSet();
			PatchSetComparisonCache cache = diffContext.getCache();

			oldComparison = cache.getComparison(containingPatchSet, patchSet, Version.OLD);
			newComparison = cache.getComparison(containingPatchSet, patchSet, Version.NEW);
		}

		/*
		 * the value that is actually used to obtain the set of candidates - a
		 * diff is a candidate if it belongs to the "same" object and structural
		 * feature
		 */
		EObject matchingPatchSetValue = null;

		if (oldValue != null) {

			/*
			 * find the "same" object across the given patch sets by matching
			 * the old versions of the object
			 */
			if (oldComparison == null) {
				oldComparison = compareService.matchWithPatchSetVersion(oldValue, patchSet, Version.OLD,
						subMonitor.newChild(50));
			}
			Match valueMatch = oldComparison.getMatch(oldValue);
			matchingPatchSetValue = matchHelper.getOpposite(oldValue, valueMatch);

		} else if (newValue != null) {

			/*
			 * find the "same" object across the given patch sets by matching
			 * the new versions of the object
			 */
			if (newComparison == null) {
				newComparison = compareService.matchWithPatchSetVersion(newValue, patchSet, Version.NEW,
						subMonitor.newChild(50));
			}
			Match valueMatch = newComparison.getMatch(newValue);
			matchingPatchSetValue = matchHelper.getOpposite(newValue, valueMatch);
		}

		subMonitor.setWorkRemaining(uncategorizedDiffs.size() * 100);

		/*
		 * filter the whole set of diffs which:
		 * 
		 * 1) belong to the same object
		 * 
		 * 2) belong to the same structural feature
		 */
		for (Diff diffCandidate : uncategorizedDiffs) {

			Match match = diffCandidate.getMatch();
			EObject newCandidateValue = matchHelper.getNewValue(match);
			EObject oldCandidateValue = matchHelper.getOldValue(match);

			if (newCandidateValue == matchingPatchSetValue || oldCandidateValue == matchingPatchSetValue) {
				EStructuralFeature candidateFeature = MatchUtil.getStructuralFeature(diffCandidate);
				if (diffFeature == candidateFeature) {
					candidates.add(diffCandidate);
				}
			}
			subMonitor.worked(100);
		}

		return candidates;
	}

	/**
	 * called once after
	 * {@link #updateEntryFor(IPatchSetHistoryEntry, PatchSet)} has finished.
	 * 
	 * @param entry
	 *            the entry that has been processed.
	 * @param patchSet
	 *            the patch set whose value has been processed.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void postProcessEntry(IPatchSetHistoryEntry<Diff, DiffWithSimilarity> entry, PatchSet patchSet,
			IProgressMonitor monitor) {

		SubMonitor.convert(monitor, 100);

		DiffWithSimilarity value = entry.getValue(patchSet);

		if (value != null) {
			new SubDiffEntry(entry, patchSet, entry.getValue(patchSet));
		}

	}

	/**
	 * calculates the similarity between two {@link Diff}s in the range 0 (not
	 * equal) and 1 (equal).
	 * 
	 * @param oldComparison
	 *            the comparison containing the matches of the old versions of
	 *            the patch sets containing the {@link Diff}s.
	 * @param newComparison
	 *            the comparison containing the matches of the new versions of
	 *            the patch sets containing the {@link Diff}s.
	 * @param diff
	 *            the reference {@link Diff} to calculate the similarity for.
	 * @param otherDiff
	 *            the {@link Diff} to calculate the similarity for.
	 * @return the similarity in the range 0 (not equal) and 1 (equal).
	 */
	protected double calculateSimilarity(Comparison oldComparison, Comparison newComparison, Diff diff,
			Diff otherDiff) {

		EditionDistance editionDistance = new EditionDistance(
				WeightProviderDescriptorRegistryImpl.createStandaloneInstance());

		/* determine similarity of the diff value */

		double valueSimilarity = 0.0;

		Object diffValue = MatchUtil.getValue(diff);
		Object otherDiffValue = MatchUtil.getValue(otherDiff);

		if (diffValue == otherDiffValue) {
			valueSimilarity = 1.0;

		} else if (diffValue instanceof EObject && otherDiffValue instanceof EObject) {

			EObject valueEObject = (EObject) diffValue;
			EObject otherValueEObject = (EObject) otherDiffValue;

			if (valueEObject.eClass().equals(otherValueEObject.eClass())) {

				double oldSimilarity = MathUtil.map(
						editionDistance.distance(oldComparison, valueEObject, otherValueEObject), 0.0, Double.MAX_VALUE,
						1.0, 0.0);
				double newSimilarity = MathUtil.map(
						editionDistance.distance(newComparison, valueEObject, otherValueEObject), 0.0, Double.MAX_VALUE,
						1.0, 0.0);

				valueSimilarity = Math.max(oldSimilarity, newSimilarity);

				double featureSimilarity = 0.0;

				EStructuralFeature structuralFeature = valueEObject.eContainingFeature();
				EStructuralFeature otherStructuralFeature = otherValueEObject.eContainingFeature();

				if (structuralFeature != null && structuralFeature == otherStructuralFeature) {
					if (structuralFeature.isMany()) {
						int valueIndex = ((EList<?>) valueEObject.eContainer().eGet(structuralFeature))
								.indexOf(valueEObject);
						int otherValueIndex = ((EList<?>) otherValueEObject.eContainer().eGet(otherStructuralFeature))
								.indexOf(otherValueEObject);
						featureSimilarity = MathUtil.map(Math.abs(valueIndex - otherValueIndex), 0.0, Integer.MAX_VALUE,
								1.0, 0.0);
					} else {
						featureSimilarity = 1.0;
					}
				}

				valueSimilarity = valueSimilarity * 0.9 + featureSimilarity * 0.1;

			}

		} else if (diffValue instanceof Number && otherDiffValue instanceof Number) {

			Number leftNumber = (Number) diffValue;
			Number rightNumber = (Number) otherDiffValue;

			valueSimilarity = MathUtil.map(Math.abs(leftNumber.doubleValue() - rightNumber.doubleValue()), 0.0,
					Double.MAX_VALUE, 1.0, 0.0);

		} else if (diffValue != null && diffValue.equals(otherDiffValue)) {
			valueSimilarity = 1.0;
		}

		/* determine similarity of the difference type */

		double typeSimilarity = 0.0;

		if (diff.eClass().equals(otherDiff.eClass())) {
			typeSimilarity = 1.0;
		}

		/* determine similarity of the difference kind */

		double kindSimilarity = 0.0;

		if (diff.getKind().equals(otherDiff.getKind())) {
			kindSimilarity = 1.0;
		}

		return typeSimilarity * 0.25 + kindSimilarity * 0.25 + valueSimilarity * 0.5;
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
			List<PatchSet> patchSets, boolean mergeEqualDiffs, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

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
		PatchSetComparisonCache matchCache = buildPatchSetComparisonCache(patchSets, patchSets, patchSetToMatchDiffs,
				patchSetToMatchDiffs);
		subMonitor.worked(50);

		return createEntriesForDiffs(diffs, patchSets, matchCache, mergeEqualDiffs, subMonitor.newChild(50));
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
			List<PatchSet> patchSets, boolean mergeEqualDiffs, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

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
		PatchSetComparisonCache matchCache = buildPatchSetComparisonCache(patchSets, patchSets, patchSetToMatchDiffs,
				patchSetToMatchDiffs);
		subMonitor.worked(50);

		return createEntriesForDiffs(diffs, patchSets, matchCache, mergeEqualDiffs, subMonitor.newChild(50));
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
	 * @param matchCache
	 *            a cache to speed up matching, may be null.
	 * @param mergeEqualDiffs
	 *            true if equal diffs should be merged into a single entry,
	 *            false if an entry should be created for each diff in all patch
	 *            sets.
	 * @return a list of entries for the given differences.
	 */
	private List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> createEntriesForDiffs(
			Map<PatchSet, List<Diff>> differences, List<PatchSet> patchSets, PatchSetComparisonCache matchCache,
			boolean mergeEqualDiffs, IProgressMonitor monitor) {

		List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> entries = new LinkedList<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>>();
		Set<Diff> processedDiffs = new HashSet<Diff>();

		Set<PatchSet> diffPatchSets = differences.keySet();

		SubMonitor subMonitor = SubMonitor.convert(monitor, diffPatchSets.size() * 100);

		for (PatchSet diffPatchSet : diffPatchSets) {

			List<Diff> patchSetDiffs = differences.get(diffPatchSet);

			DiffContext diffContext = new DiffContext(matchCache, diffPatchSet);

			for (Diff diff : patchSetDiffs) {

				SubMonitor childMonitor = subMonitor.newChild(100);

				if (!processedDiffs.contains(diff)) {

					IPatchSetHistoryEntry<Diff, DiffWithSimilarity> historyEntry = createEntryFor(diff, patchSets,
							diffContext, childMonitor);
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
	 * @return a {@link PatchSetComparisonCache} containing the matching
	 *         information between combinations of the given patch sets.
	 */
	private PatchSetComparisonCache buildPatchSetComparisonCache(List<PatchSet> patchSetsToCache,
			List<PatchSet> patchSetsToMatch, List<List<Diff>> patchSetsToCacheDiffs,
			List<List<Diff>> patchSetsToMatchDiffs) {

		PatchSetComparisonCache cache = new PatchSetComparisonCache();

		ListIterator<PatchSet> currentPatchSetIterator = patchSetsToCache.listIterator();
		while (currentPatchSetIterator.hasNext()) {

			PatchSet patchSet = currentPatchSetIterator.next();

			ListIterator<PatchSet> otherPatchSetIterator = patchSetsToMatch.listIterator();
			while (otherPatchSetIterator.hasNext()) {

				PatchSet otherPatchSet = otherPatchSetIterator.next();

				if (!cache.containsValues(patchSet, otherPatchSet)) {
					Comparison oldComparison = compareService.matchPatchSetVersions(patchSet, Version.OLD,
							otherPatchSet, Version.OLD, null);
					Comparison newComparison = compareService.matchPatchSetVersions(patchSet, Version.NEW,
							otherPatchSet, Version.NEW, null);
					cache.add(patchSet, otherPatchSet, oldComparison, newComparison);
				}
			}
		}
		return cache;
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
			boolean mergeEqualDiffs, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		Map<PatchSet, List<Diff>> diffs = new HashMap<>();
		List<List<Diff>> patchSetDiffs = new ArrayList<>(patchSets.size());

		for (PatchSet patchSet : patchSets) {
			diffs.put(patchSet, patchSet.getModelComparison().getDifferences());
			patchSetDiffs.add(patchSet.getModelComparison().getDifferences());
		}

		PatchSetComparisonCache matchCache = buildPatchSetComparisonCache(patchSets, patchSets, patchSetDiffs,
				patchSetDiffs);
		subMonitor.worked(50);

		return createEntriesForDiffs(diffs, patchSets, matchCache, mergeEqualDiffs, subMonitor.newChild(50));
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
			boolean mergeEqualDiffs, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		Map<PatchSet, List<Diff>> diffs = new HashMap<>();
		List<List<Diff>> patchSetDiffs = new ArrayList<>(patchSets.size());

		for (PatchSet patchSet : patchSets) {
			diffs.put(patchSet, patchSet.getDiagramComparison().getDifferences());
			patchSetDiffs.add(patchSet.getDiagramComparison().getDifferences());
		}

		PatchSetComparisonCache matchCache = buildPatchSetComparisonCache(patchSets, patchSets, patchSetDiffs,
				patchSetDiffs);
		subMonitor.worked(50);

		return createEntriesForDiffs(diffs, patchSets, matchCache, mergeEqualDiffs, subMonitor.newChild(50));
	}

	/**
	 * Holds the caching information processed in
	 * {@link DiffSimilarityHistoryService#updateEntryFor(IPatchSetHistoryEntry, PatchSet, Object, IProgressMonitor)}
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class DiffContext {

		private PatchSetComparisonCache cache;
		private PatchSet containingPatchSet;

		public DiffContext(PatchSetComparisonCache cache, PatchSet containingPatchSet) {
			super();
			this.cache = cache;
			this.containingPatchSet = containingPatchSet;
		}

		/**
		 * @return the patch set comparison cache that should be used for
		 *         matching objects between patch sets if possible.
		 */
		public PatchSetComparisonCache getCache() {
			return cache;
		}

		/**
		 * @return the patch set that contains the diff that has been provided
		 *         with this context.
		 */
		public PatchSet getContainingPatchSet() {
			return containingPatchSet;
		}
	}

}

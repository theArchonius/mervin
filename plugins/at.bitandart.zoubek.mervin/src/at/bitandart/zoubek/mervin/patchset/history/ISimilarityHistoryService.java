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

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.Diff;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;

/**
 * Base interface for services that create and handle entries that provide a
 * similarity history for a Diff across multiple {@link PatchSet}s.
 * 
 * @author Florian Zoubek
 *
 */
public interface ISimilarityHistoryService extends IPatchSetHistoryService<Diff, DiffWithSimilarity> {

	/**
	 * creates all entries for all model differences in the given patch set.
	 * 
	 * @param patchSet
	 *            the patchSet to retrieve the {@link Diff}s from
	 * @param patchSets
	 *            the patchSets used to calculate the similarity
	 * @param mergeEqualDiffs
	 *            true if equal diffs should be merged into a single entry,
	 *            false if an entry should be created for each diff in all patch
	 *            sets.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @return a list of {@link IPatchSetHistoryEntry}s for all model
	 *         differences in the given patch set
	 */
	public List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> createModelEntries(PatchSet patchSet,
			List<PatchSet> patchSets, boolean mergeEqualDiffs, IProgressMonitor monitor);

	/**
	 * creates all entries for all model differences in the given list of patch
	 * sets. No duplicated entries will be created for equal diffs in different
	 * patch sets.
	 * 
	 * @param patchSets
	 * @param mergeEqualDiffs
	 *            true if equal diffs should be merged into a single entry,
	 *            false if an entry should be created for each diff in all patch
	 *            sets.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @return a list of {@link IPatchSetHistoryEntry}s for all model
	 *         differences in the given patch set
	 */
	public List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> createModelEntries(List<PatchSet> patchSets,
			boolean mergeEqualDiffs, IProgressMonitor monitor);

	/**
	 * creates all entries for all diagram differences in the given patch set.
	 * 
	 * @param patchSet
	 *            the patchSet to retrieve the {@link Diff}s from
	 * @param patchSets
	 *            the patchSets used to calculate the similarity
	 * @param mergeEqualDiffs
	 *            true if equal diffs should be merged into a single entry,
	 *            false if an entry should be created for each diff in all patch
	 *            sets.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @return a list of {@link IPatchSetHistoryEntry}s for all diagram
	 *         differences in the given patch set
	 */
	public List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> createDiagramEntries(PatchSet patchSet,
			List<PatchSet> patchSets, boolean mergeEqualDiffs, IProgressMonitor monitor);

	/**
	 * creates all entries for all diagram differences in the given list of
	 * patch sets. No duplicated entries will be created for equal diffs in
	 * different patch sets if specified.
	 * 
	 * @param patchSets
	 * @param mergeEqualDiffs
	 *            true if equal diffs should be merged into a single entry,
	 *            false if an entry should be created for each diff in all patch
	 *            sets.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @return a list of {@link IPatchSetHistoryEntry}s for all diagram
	 *         differences in the given patch set
	 */
	public List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> createDiagramEntries(List<PatchSet> patchSets,
			boolean mergeEqualDiffs, IProgressMonitor monitor);

	/**
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public static class DiffWithSimilarity {

		private Diff diff;
		private double similarity;

		/**
		 * @return the diff
		 */
		public Diff getDiff() {
			return diff;
		}

		/**
		 * @return the similarity
		 */
		public double getSimilarity() {
			return similarity;
		}

		public DiffWithSimilarity(Diff diff, double similarity) {
			super();
			this.diff = diff;
			this.similarity = similarity;
		}

	}
}

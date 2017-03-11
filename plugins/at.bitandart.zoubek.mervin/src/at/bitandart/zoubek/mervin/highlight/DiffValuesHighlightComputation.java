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
package at.bitandart.zoubek.mervin.highlight;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredSelection;

import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.IPatchSetHistoryEntry;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;

/**
 * @author Florian Zoubek
 *
 */
public class DiffValuesHighlightComputation extends SelectionHighlightComputation {

	public DiffValuesHighlightComputation(IStructuredSelection selection, IReviewHighlightService highlightService,
			ModelReview review) {
		super(selection, highlightService, review);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.highlight.SelectionHighlightComputation#
	 * collectHighlightedElements(java.lang.Object, java.util.Set,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void collectHighlightedElements(Object candidate, Set<Object> highlightedElements,
			IProgressMonitor monitor) {
		collectDiffValues(candidate, highlightedElements, monitor);
	}

	/**
	 * collects the diff values for the given {@link Object}.
	 * 
	 * @param candidate
	 *            the {@link Object} to collect the diff values for.
	 * @param diffValues
	 *            the set of diff values to add the diff values to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void collectDiffValues(Object candidate, Set<Object> diffValues, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		checkCancellation(subMonitor);

		if (candidate instanceof IPatchSetHistoryEntry<?, ?>) {
			collectDiffValues((IPatchSetHistoryEntry<?, ?>) candidate, diffValues, monitor);

		} else if (candidate instanceof Match) {
			collectDiffValues((Match) candidate, diffValues, monitor);

		} else if (candidate instanceof DiffWithSimilarity) {
			collectDiffValues((DiffWithSimilarity) candidate, diffValues, monitor);

		} else if (candidate instanceof Diff) {
			collectDiffValues((Diff) candidate, diffValues, monitor);
		}
	}

	/**
	 * collects the diff values for the given {@link IPatchSetHistoryEntry}.
	 * 
	 * @param entry
	 *            the {@link IPatchSetHistoryEntry} to collect the diff values
	 *            for.
	 * @param diffValues
	 *            the set of diff values to add the diff values to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void collectDiffValues(IPatchSetHistoryEntry<?, ?> entry, Set<Object> diffValues,
			IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 1100);
		checkCancellation(subMonitor);

		collectDiffValues(entry.getEntryObject(), diffValues, subMonitor.newChild(100));

		Map<PatchSet, ?> values = entry.getValues(getHighlightedReview().getPatchSets());
		subMonitor.setWorkRemaining(values.size() * 100);

		for (Object value : values.values()) {
			collectDiffValues(value, diffValues, subMonitor.newChild(100));
		}
	}

	/**
	 * collects the diff values for the given {@link Diff}.
	 * 
	 * @param diff
	 *            the {@link Diff} to collect the diff values for.
	 * @param diffValues
	 *            the set of diff values to add the diff values to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void collectDiffValues(Diff diff, Set<Object> diffValues, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		checkCancellation(subMonitor);

		Object value = MatchUtil.getValue(diff);
		if (value != null) {
			diffValues.add(value);
		}
	}

	/**
	 * collects the diff values for the given {@link DiffWithSimilarity}.
	 * 
	 * @param similarityDiff
	 *            the {@link DiffWithSimilarity} to collect the diff values for.
	 * @param diffValues
	 *            the set of diff values to add the diff values to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void collectDiffValues(DiffWithSimilarity similarityDiff, Set<Object> diffValues,
			IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		checkCancellation(subMonitor);

		collectDiffValues(similarityDiff.getDiff(), diffValues, subMonitor.newChild(100));
	}

	/**
	 * collects the diff values for the given {@link Match}.
	 * 
	 * @param match
	 *            the {@link Match} to collect the diff values for.
	 * @param diffValues
	 *            the set of diff values to add the diff values to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void collectDiffValues(Match match, Set<Object> diffValues, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		checkCancellation(subMonitor);

		EObject left = match.getLeft();
		if (left != null) {
			collectDiffValues(left, diffValues, subMonitor.newChild(100));
		} else {
			subMonitor.setWorkRemaining(100);
		}

		EObject right = match.getRight();
		if (right != null) {
			collectDiffValues(right, diffValues, subMonitor.newChild(100));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.highlight.SelectionHighlightComputation#
	 * getCollectTaskLabel()
	 */
	@Override
	protected String getCollectTaskLabel() {
		return "Searching for diff values...";
	}

}

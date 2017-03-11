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
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IStructuredSelection;

import at.bitandart.zoubek.mervin.IDiagramModelHelper;
import at.bitandart.zoubek.mervin.IModelReviewHelper;
import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.ObjectHistoryEntryContainer;

/**
 * A {@link SelectionHighlightComputation} that highlights the referencing
 * {@link Diff}s of elements in the selection.
 * 
 * @author Florian Zoubek
 *
 */
public class ReferencingDiffHighlightComputation extends SelectionHighlightComputation {

	private IDiagramModelHelper diagramModelHelper;
	private IModelReviewHelper modelReviewHelper;

	public ReferencingDiffHighlightComputation(IStructuredSelection selection, IReviewHighlightService highlightService,
			ModelReview review, IDiagramModelHelper diagramModelHelper, IModelReviewHelper modelReviewHelper) {
		super(selection, highlightService, review);
		this.diagramModelHelper = diagramModelHelper;
		this.modelReviewHelper = modelReviewHelper;
	}

	@Override
	protected String getCollectTaskLabel() {
		return "Searching for differences...";
	}

	@Override
	protected void collectHighlightedElements(Object candidate, Set<Object> referencingDiffs,
			IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		if (candidate instanceof Match) {
			collectReferencingDiffs((Match) candidate, referencingDiffs, subMonitor.newChild(100));

		} else if (candidate instanceof ObjectHistoryEntryContainer) {
			collectReferencingDiffs((ObjectHistoryEntryContainer) candidate, referencingDiffs,
					subMonitor.newChild(100));

		} else if (candidate instanceof EditPart) {
			collectReferencingDiffs((EditPart) candidate, referencingDiffs, subMonitor.newChild(100));

		} else if (candidate instanceof EObject) {
			collectReferencingDiffs((EObject) candidate, referencingDiffs, subMonitor.newChild(100));
		}
	}

	/**
	 * collects the referencing diffs for the given {@link EditPart}.
	 * 
	 * @param editPart
	 *            the edit part to collect the highlighted elements for.
	 * @param referencingDiffs
	 *            the set of diffs to add the referencing diffs to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * 
	 */
	protected void collectReferencingDiffs(EditPart editPart, Set<Object> referencingDiffs,
			IProgressMonitor monitor) {

		/*
		 * use the semantic and the notation model to retrieve the diffs for
		 * selected edit parts
		 */

		SubMonitor subMonitor = SubMonitor.convert(monitor, 200);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}

		ModelReview review = getHighlightedReview();

		EObject semanticModel = diagramModelHelper.getSemanticModel(editPart);

		if (semanticModel != null) {
			referencingDiffs.addAll(modelReviewHelper.getDifferences(semanticModel, review));
			subMonitor.worked(100);
		} else {
			subMonitor.setWorkRemaining(100);
		}

		EObject notationModel = diagramModelHelper.getNotationModel(editPart);

		if (notationModel != null) {

			EObject originalNotationModel = review.getUnifiedModelMap().getOriginal(notationModel);
			if (originalNotationModel != null) {
				notationModel = originalNotationModel;
			}

			referencingDiffs.addAll(modelReviewHelper.getDifferences(notationModel, review));
		}
	}

	/**
	 * collects the referencing diffs for the given
	 * {@link ObjectHistoryEntryContainer}.
	 * 
	 * @param container
	 *            the {@link ObjectHistoryEntryContainer} to collect the
	 *            highlighted elements for.
	 * @param referencingDiffs
	 *            the set of diffs to add the referencing diffs to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * 
	 */
	protected void collectReferencingDiffs(ObjectHistoryEntryContainer container, Set<Object> referencingDiffs,
			IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}

		ModelReview review = getHighlightedReview();

		Map<PatchSet, Match> matches = container.getMatches(review.getPatchSets());
		subMonitor.setWorkRemaining(100 * matches.size());

		for (Match match : matches.values()) {
			collectReferencingDiffs(match, referencingDiffs, subMonitor.newChild(100));
		}
	}

	/**
	 * collects the referencing diffs for the given {@link Match}.
	 * 
	 * @param match
	 *            the match to collect the highlighted elements for.
	 * @param referencingDiffs
	 *            the set of diffs to add the referencing diffs to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * 
	 */
	protected void collectReferencingDiffs(Match match, Set<Object> referencingDiffs, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 200);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}

		EObject left = match.getLeft();
		if (left != null) {
			collectReferencingDiffs(left, referencingDiffs, subMonitor.newChild(100));
		} else {
			subMonitor.setWorkRemaining(100);
		}

		EObject right = match.getRight();
		if (right != null) {
			collectReferencingDiffs(right, referencingDiffs, subMonitor.newChild(100));
		}
	}

	/**
	 * collects the referencing diffs for the given {@link EObject}.
	 * 
	 * @param match
	 *            the {@link EObject} to collect the highlighted elements for.
	 * @param referencingDiffs
	 *            the set of diffs to add the referencing diffs to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * 
	 */
	protected void collectReferencingDiffs(EObject eObject, Set<Object> referencingDiffs, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		referencingDiffs.addAll(modelReviewHelper.getDifferences(eObject, getHighlightedReview()));
	}

}

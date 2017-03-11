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
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredSelection;

import at.bitandart.zoubek.mervin.IDiagramModelHelper;
import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.ObjectHistoryEntryContainer;

/**
 * A {@link SelectionHighlightComputation} that highlights the semantic model
 * elements of elements in the selection.
 * 
 * @author Florian Zoubek
 *
 */
public class SemanticModelElementsHighlightComputation extends SelectionHighlightComputation {

	private IDiagramModelHelper diagramModelHelper;

	public SemanticModelElementsHighlightComputation(IStructuredSelection selection,
			IReviewHighlightService highlightService, ModelReview review, IDiagramModelHelper diagramModelHelper) {
		super(selection, highlightService, review);
		this.diagramModelHelper = diagramModelHelper;
	}

	@Override
	protected void collectHighlightedElements(Object candidate, Set<Object> highlightedElements,
			IProgressMonitor monitor) {

		collectSemanticModels(candidate, highlightedElements, monitor);
	}

	/**
	 * collects the semantic models for the given {@link Object}.
	 * 
	 * @param object
	 *            the {@link Object} to collect the semantic models for.
	 * @param semanticModelElements
	 *            the set of semantic elements to add the semantic model
	 *            elements to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void collectSemanticModels(Object candidate, Set<Object> semanticModelElements,
			IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		checkCancellation(subMonitor);

		if (candidate instanceof Match) {
			collectSemanticModels((Match) candidate, semanticModelElements, subMonitor.newChild(100));

		} else if (candidate instanceof ObjectHistoryEntryContainer) {
			collectSemanticModels((ObjectHistoryEntryContainer) candidate, semanticModelElements,
					subMonitor.newChild(100));

		} else {

			EObject semanticModel = diagramModelHelper.getSemanticModel(candidate);
			if (semanticModel != null) {
				semanticModelElements.add(semanticModel);
			}
		}
	}

	/**
	 * collects the semantic models for the given
	 * {@link ObjectHistoryEntryContainer}.
	 * 
	 * @param container
	 *            the {@link ObjectHistoryEntryContainer} to collect the
	 *            semantic models for.
	 * @param semanticModelElements
	 *            the set of semantic elements to add the semantic model
	 *            elements to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void collectSemanticModels(ObjectHistoryEntryContainer container, Set<Object> semanticModelElements,
			IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor);
		checkCancellation(subMonitor);

		Map<PatchSet, Match> matches = container.getMatches(getHighlightedReview().getPatchSets());

		subMonitor.setWorkRemaining(100 * matches.size());

		for (Match match : matches.values()) {
			collectSemanticModels(match, semanticModelElements, subMonitor.newChild(100));
		}

	}

	/**
	 * collects the semantic models for the given {@link Match}.
	 * 
	 * @param match
	 *            the {@link Match} to collect the semantic models for.
	 * @param semanticModelElements
	 *            the set of semantic elements to add the semantic model
	 *            elements to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void collectSemanticModels(Match match, Set<Object> semanticModelElements, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor);
		checkCancellation(subMonitor);

		EObject left = match.getLeft();
		if (left != null) {
			collectSemanticModels(left, semanticModelElements, subMonitor.newChild(100));
		} else {
			subMonitor.setWorkRemaining(100);
		}

		EObject right = match.getRight();
		if (right != null) {
			collectSemanticModels(right, semanticModelElements, subMonitor.newChild(100));
		}
	}

	@Override
	protected String getCollectTaskLabel() {
		return "Searching for semantic model elements...";
	}

}

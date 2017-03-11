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
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IStructuredSelection;

import at.bitandart.zoubek.mervin.IDiagramModelHelper;
import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.ObjectHistoryEntryContainer;

/**
 * A {@link SelectionHighlightComputation} that highlights the referencing
 * {@link View}s of elements in the selection.
 * 
 * @author Florian Zoubek
 *
 */
public class ReferencingViewHighlightComputation extends SelectionHighlightComputation {

	private IDiagramModelHelper diagramModelHelper;

	public ReferencingViewHighlightComputation(IStructuredSelection selection, IReviewHighlightService highlightService,
			ModelReview review, IDiagramModelHelper diagramModelHelper) {
		super(selection, highlightService, review);
		this.diagramModelHelper = diagramModelHelper;
	}

	@Override
	protected void collectHighlightedElements(Object candidate, Set<Object> highlightedElements,
			IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		if (candidate instanceof ObjectHistoryEntryContainer) {
			collectReferencingViews((ObjectHistoryEntryContainer) candidate, highlightedElements,
					subMonitor.newChild(100));

		} else if (candidate instanceof EditPart) {
			collectReferencingViews((EditPart) candidate, highlightedElements, subMonitor.newChild(100));

		} else if (candidate instanceof EObject) {
			collectReferencingViews((EObject) candidate, highlightedElements, subMonitor);
		}
	}

	/**
	 * collects the referencing views for the given {@link EditPart}.
	 * 
	 * @param editPart
	 *            the edit part to collect the referencing views for.
	 * @param referencingViews
	 *            the set of views to add the referencing views to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void collectReferencingViews(EditPart editPart, Set<Object> referencingViews, IProgressMonitor monitor) {

		/*
		 * use the semantic model to retrieve the referencing views for selected
		 * edit parts
		 */

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		checkCancellation(subMonitor);

		EObject semanticModel = diagramModelHelper.getSemanticModel(editPart);

		if (semanticModel != null) {
			referencingViews.addAll(diagramModelHelper.getReferencingViews(semanticModel));
		}

	}

	/**
	 * collects the referencing views for the given {@link EObject}.
	 * 
	 * @param eObject
	 *            the {@link EObject} to collect the referencing views for.
	 * @param referencingViews
	 *            the set of views to add the referencing views to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void collectReferencingViews(EObject eObject, Set<Object> referencingViews, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		checkCancellation(subMonitor);

		referencingViews.addAll(diagramModelHelper.getReferencingViews(eObject));
	}

	/**
	 * collects the referencing views for the given
	 * {@link ObjectHistoryEntryContainer}.
	 * 
	 * @param container
	 *            the {@link ObjectHistoryEntryContainer} to collect the
	 *            referencing views for.
	 * @param referencingViews
	 *            the set of views to add the referencing views to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void collectReferencingViews(ObjectHistoryEntryContainer container, Set<Object> referencingViews,
			IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor);
		checkCancellation(subMonitor);

		Map<PatchSet, Match> matches = container.getMatches(getHighlightedReview().getPatchSets());

		subMonitor.setWorkRemaining(100 * matches.size());

		for (Match match : matches.values()) {
			collectReferencingViews(match, referencingViews, subMonitor.newChild(100));
		}
	}

	/**
	 * collects the referencing views for the given {@link Match}.
	 * 
	 * @param match
	 *            the {@link Match} to collect the referencing views for.
	 * @param referencingViews
	 *            the set of views to add the referencing views to.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void collectReferencingViews(Match match, Set<Object> referencingViews, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 200);
		checkCancellation(subMonitor);

		EObject left = match.getLeft();
		if (left != null) {
			collectHighlightedElements(left, referencingViews, subMonitor.newChild(100));
		} else {
			subMonitor.setWorkRemaining(100);
		}

		EObject right = match.getRight();
		if (right != null) {
			collectHighlightedElements(right, referencingViews, subMonitor.newChild(100));
		}
	}

	@Override
	protected String getCollectTaskLabel() {
		return "Searching for view references...";
	}

}

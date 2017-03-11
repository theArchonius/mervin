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
package at.bitandart.zoubek.mervin.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import at.bitandart.zoubek.mervin.IDiagramModelHelper;
import at.bitandart.zoubek.mervin.IMervinContextConstants;
import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.highlight.ReferencingViewHighlightComputation;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.ObjectHistoryEntryContainer;
import at.bitandart.zoubek.mervin.predicates.MatchValuesPredicate.Operation;
import at.bitandart.zoubek.mervin.predicates.MervinPredicates;

/**
 * A command handler that performs a highlight request for the referencing
 * {@link View}s of the current selection.
 * 
 * @author Florian Zoubek
 *
 */
@SuppressWarnings("restriction")
public class HighlightReferencingViews {

	@Inject
	private Logger logger;

	@Inject
	@Named(IMervinContextConstants.ACTIVE_MODEL_REVIEW)
	@Optional
	private ModelReview review;

	private final Predicate<Object> validSelectionPredicate = new Predicate<Object>() {

		@Override
		public boolean apply(Object element) {

			if (element instanceof ObjectHistoryEntryContainer && review != null) {
				Map<PatchSet, Match> matches = ((ObjectHistoryEntryContainer) element)
						.getMatches(review.getPatchSets());
				return Iterables.any(matches.values(),
						MervinPredicates.matchValues(validSelectionPredicate, Operation.OR));
			}

			return element != null && !(element instanceof Diff) && !(element instanceof View)
					&& (element instanceof EObject || element instanceof EditPart);
		}
	};

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional IStructuredSelection selection) {

		if (selection != null) {

			return Iterators.any((Iterator<?>) selection.iterator(), validSelectionPredicate);
		}
		return false;
	}

	@Execute
	public void execute(Shell shell, final IReviewHighlightService highlightService,
			IDiagramModelHelper diagramModelHelper,
			@Named(IServiceConstants.ACTIVE_SELECTION) @Optional IStructuredSelection selection,
			@Named(IMervinContextConstants.ACTIVE_MODEL_REVIEW) @Optional ModelReview review) {

		if (review != null && selection != null) {

			ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell);
			try {
				progressMonitorDialog.run(true, true, new ReferencingViewHighlightComputation(selection,
						highlightService, review, diagramModelHelper));
			} catch (InvocationTargetException e) {
			} catch (InterruptedException e) {
			} catch (OperationCanceledException e) {
				logger.warn(e, "Highlighting of referencing views has been cancelled");
			}

		}
	}

}

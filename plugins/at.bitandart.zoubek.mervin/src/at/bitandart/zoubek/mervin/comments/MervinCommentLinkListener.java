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
package at.bitandart.zoubek.mervin.comments;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.IMervinContextConstants;
import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.IReviewHighlightServiceListener;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.swt.comments.CommentList.CommentLinkListener;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLinkTarget;

/**
 * A {@link CommentLinkListener} that highlights link targets of the active
 * model review. Hovering over a link results in a temporary highlight, clicking
 * results in a permanent highlight until it is cleared by the
 * {@link IReviewHighlightService}.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinCommentLinkListener implements CommentLinkListener {

	@Inject
	@Optional
	@Named(IMervinContextConstants.ACTIVE_MODEL_REVIEW)
	private ModelReview activeModelReview;

	private IReviewHighlightService reviewHighlightService;

	private Set<EObject> permanentLinkTargets = new HashSet<>();

	/**
	 * This listener is needed to update the set of permanent link targets -
	 * while this class will not request removal of permanent link target
	 * highlights, other classes may remove such targets.
	 */
	private IReviewHighlightServiceListener highlightListener = new IReviewHighlightServiceListener() {

		@Override
		public void elementRemoved(ModelReview review, Object element) {
			/*
			 * listen to highlight removal requests - if a permanent highlight
			 * has been deleted also delete it from the set of permanent
			 * highlighted link targets
			 */
			permanentLinkTargets.remove(element);
		}

		@Override
		public void elementAdded(ModelReview review, Object element) {
			// Intentionally left empty
		}
	};

	@Override
	public void commentLinkClicked(ICommentLink commentLink) {

		ICommentLinkTarget commentLinkTarget = commentLink.getCommentLinkTarget();

		if (activeModelReview != null && commentLinkTarget instanceof MervinCommentLinkTarget) {

			List<EObject> targets = ((MervinCommentLinkTarget) commentLinkTarget).getTargets();
			reviewHighlightService.clearHighlights(activeModelReview);

			for (EObject target : targets) {

				permanentLinkTargets.add(target);
				reviewHighlightService.addHighlightFor(activeModelReview, target);
			}
		}
	}

	@Override
	public void commentLinkEnter(ICommentLink commentLink) {

		ICommentLinkTarget commentLinkTarget = commentLink.getCommentLinkTarget();

		if (activeModelReview != null && commentLinkTarget instanceof MervinCommentLinkTarget) {
			List<EObject> targets = ((MervinCommentLinkTarget) commentLinkTarget).getTargets();

			for (EObject target : targets) {
				reviewHighlightService.addHighlightFor(activeModelReview, target);
			}
		}

	}

	@Override
	public void commentLinkExit(ICommentLink commentLink) {

		ICommentLinkTarget commentLinkTarget = commentLink.getCommentLinkTarget();

		if (activeModelReview != null && commentLinkTarget instanceof MervinCommentLinkTarget) {
			List<EObject> targets = ((MervinCommentLinkTarget) commentLinkTarget).getTargets();

			for (EObject target : targets) {
				/*
				 * Hovering is also needed for clicking on a comment link - so
				 * do not remove highlighted link targets that should be
				 * permanent
				 */
				if (!permanentLinkTargets.contains(target)) {
					reviewHighlightService.removeHighlightFor(activeModelReview, target);
				}
			}
		}

	}

	@Inject
	public void setReviewHighlightService(IReviewHighlightService reviewHighlightService) {

		if (this.reviewHighlightService != null) {
			this.reviewHighlightService.removeHighlightServiceListener(highlightListener);
		}
		this.reviewHighlightService = reviewHighlightService;
		this.reviewHighlightService.addHighlightServiceListener(highlightListener);
	}
}

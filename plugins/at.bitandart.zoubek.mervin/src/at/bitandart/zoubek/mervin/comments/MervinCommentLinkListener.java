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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.IMervinContextConstants;
import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.swt.comments.CommentList.CommentLinkListener;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLinkTarget;

/**
 * A {@link CommentLinkListener} that temporarily highlights link targets of the
 * active model review while hovering over the comment link.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinCommentLinkListener implements CommentLinkListener {

	@Inject
	@Optional
	@Named(IMervinContextConstants.ACTIVE_MODEL_REVIEW)
	private ModelReview activeModelReview;

	@Inject
	private IReviewHighlightService reviewHighlightService;

	@Override
	public void commentLinkClicked(ICommentLink commentLink) {
		// Intentionally left empty
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
				reviewHighlightService.removeHighlightFor(activeModelReview, target);
			}
		}

	}
}

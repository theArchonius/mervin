/*******************************************************************************
 * Copyright (c) 2015 Florian Zoubek.
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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.CommentLink;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.User;
import at.bitandart.zoubek.mervin.review.ModelReviewEditorTrackingView;
import at.bitandart.zoubek.mervin.swt.comments.CommentList;
import at.bitandart.zoubek.mervin.swt.comments.CommentList.CommentLinkListener;
import at.bitandart.zoubek.mervin.swt.comments.CommentList.CommentModifyListener;
import at.bitandart.zoubek.mervin.swt.comments.CommentListViewer;
import at.bitandart.zoubek.mervin.swt.comments.data.IComment;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLinkTarget;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentProvider;

public class CommentsView extends ModelReviewEditorTrackingView {

	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.comments";

	/**
	 * the comment provider used to load the comments from the mervin model
	 */
	@Inject
	private ICommentProvider commentProvider;

	@Inject
	private ModelReviewFactory modelFactory;

	@Inject
	private User currentUser;

	// Viewers
	private CommentListViewer commentListViewer;

	// Data
	private boolean viewInitialized = false;

	/**
	 * content adapter used to notify this view of updates to the current review
	 * model
	 */
	private UpdateCommentViewAdapter commentViewUpdater = new UpdateCommentViewAdapter();

	@Inject
	public CommentsView() {
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		parent.setLayout(new FillLayout());

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());

		commentListViewer = new CommentListViewer(parent, toolkit, SWT.V_SCROLL);
		commentListViewer.setCommentProvider(commentProvider);
		CommentList control = commentListViewer.getCommentListControl();
		control.addCommentLinkListener(new CommentLinkListener() {

			@Override
			public void commentLinkClicked(ICommentLink commentLink) {
				// TODO replace with correct click behaviour
				System.out.println("comment link click handler not yet implemented");
			}

			@Override
			public void commentLinkEnter(ICommentLink commentLink) {
				// TODO replace with correct click behaviour
				System.out.println("comment link enter handler not yet implemented");
			}

			@Override
			public void commentLinkExit(ICommentLink commentLink) {
				// TODO replace with correct click behaviour
				System.out.println("comment link exit handler not yet implemented");
			}
		});
		control.addCommentModifyListener(new CommentModifyListener() {

			@Override
			public void commentAdded(String text, List<ICommentLink> commentLinks, ICommentColumn commentColumn,
					IComment answerdComment) {

				ModelReview currentModelReview = getCurrentModelReview();
				if (currentModelReview != null) {

					Comment comment = modelFactory.createComment();
					comment.setText(text);
					comment.setAuthor(currentUser);
					comment.setCreationTime(System.currentTimeMillis());

					if (answerdComment != null && answerdComment instanceof MervinComment) {
						comment.setRepliedTo(((MervinComment) answerdComment).getRealComment());
					}

					for (ICommentLink commentLink : commentLinks) {

						ICommentLinkTarget commentLinkTarget = commentLink.getCommentLinkTarget();
						if (commentLinkTarget instanceof MervinCommentLinkTarget) {
							CommentLink realCommentLink = modelFactory.createCommentLink();
							realCommentLink.setStart(commentLink.getStartIndex());
							realCommentLink.setLength(commentLink.getLength());
							realCommentLink.setComment(comment);
							realCommentLink.getTargets()
									.addAll(((MervinCommentLinkTarget) commentLinkTarget).getTargets());
						}

					}

					currentModelReview.getComments().add(comment);

					commentListViewer.refresh();
				}

			}
		});

		viewInitialized = true;
		updateValues();

	}

	@Override
	protected void updateValues() {

		if (!viewInitialized) {
			return;
		}
		ModelReview currentModelReview = getCurrentModelReview();
		if (currentModelReview != null) {
			if (!currentModelReview.eAdapters().contains(commentViewUpdater)) {
				currentModelReview.eAdapters().add(commentViewUpdater);
			}
		}
		commentListViewer.setInput(currentModelReview);
		commentListViewer.refresh();
	}

	/**
	 * {@link EContentAdapter} that notifies this view about changes in the
	 * model.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class UpdateCommentViewAdapter extends EContentAdapter {

		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			updateValues();
		}

	}

}
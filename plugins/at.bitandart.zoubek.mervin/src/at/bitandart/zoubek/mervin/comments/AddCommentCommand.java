/*******************************************************************************
 * Copyright (c) 2016 Florian Zoubek.
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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;

import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.CommentLink;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.User;
import at.bitandart.zoubek.mervin.swt.comments.data.IComment;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLinkTarget;

/**
 * @author Florian Zoubek
 *
 */
public class AddCommentCommand extends AbstractTransactionalCommand {

	private String text;
	private List<ICommentLink> commentLinks;
	private ICommentColumn commentColumn;
	private IComment answeredComment;
	private ModelReviewFactory modelFactory;
	private User author;
	private ModelReview review;

	public AddCommentCommand(TransactionalEditingDomain domain, String text, List<ICommentLink> commentLinks,
			User author, ICommentColumn commentColumn, IComment answeredComment, ModelReviewFactory modelFactory,
			ModelReview review) {
		super(domain, "Add Comment", null);
		this.text = text;
		this.commentLinks = commentLinks;
		this.author = author;
		this.commentColumn = commentColumn;
		this.answeredComment = answeredComment;
		this.review = review;
		this.modelFactory = modelFactory;
	}

	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		Comment comment = modelFactory.createComment();
		comment.setText(text);
		comment.setAuthor(author);
		comment.setCreationTime(System.currentTimeMillis());

		if (answeredComment instanceof MervinComment) {
			comment.setRepliedTo(((MervinComment) answeredComment).getRealComment());
		}

		if (commentColumn instanceof PatchSetColumn) {
			PatchSet patchSet = ((PatchSetColumn) commentColumn).getPatchSet();
			if (patchSet != null) {
				comment.setPatchset(patchSet);
			}
		}

		for (ICommentLink commentLink : commentLinks) {

			ICommentLinkTarget commentLinkTarget = commentLink.getCommentLinkTarget();
			if (commentLinkTarget instanceof MervinCommentLinkTarget) {
				CommentLink realCommentLink = modelFactory.createCommentLink();
				realCommentLink.setStart(commentLink.getStartIndex());
				realCommentLink.setLength(commentLink.getLength());
				realCommentLink.setComment(comment);
				realCommentLink.getTargets().addAll(((MervinCommentLinkTarget) commentLinkTarget).getTargets());
			}

		}

		review.getComments().add(comment);

		return CommandResult.newOKCommandResult();
	}

}

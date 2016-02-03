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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.CommentLink;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.swt.comments.data.IComment;
import at.bitandart.zoubek.mervin.swt.comments.data.IComment.Alignment;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentGroup;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentProvider;

/**
 * @author Florian Zoubek
 *
 */
public class MervinCommentProvider implements ICommentProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.swt.comments.data.ICommentProvider#
	 * getCommentGroups(java.lang.Object)
	 */
	@Override
	public List<ICommentGroup> getCommentGroups(Object input) {

		List<ICommentGroup> groups = new LinkedList<>();
		if (input instanceof ModelReview) {
			ModelReview review = (ModelReview) input;
			// TODO remove default group and load it from the model
			groups.add(new MervinCommentGroup());

			for (Comment comment : review.getComments()) {

				Set<EObject> targets = new HashSet<>();
				for (CommentLink link : comment.getCommentLinks()) {
					targets.addAll(link.getTargets());
				}

			}

		}
		return groups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.swt.comments.data.ICommentProvider#
	 * getCommentColumns(java.lang.Object)
	 */
	@Override
	public List<ICommentColumn> getCommentColumns(Object input) {

		List<ICommentColumn> columns = new ArrayList<>(2);

		if (input instanceof ModelReview) {
			ModelReview review = (ModelReview) input;
			PatchSet leftPatchSet = review.getLeftPatchSet();
			if (leftPatchSet != null) {
				PatchSetColumn leftPatchSetColumn = new PatchSetColumn(leftPatchSet);
				columns.add(leftPatchSetColumn);
			}

			PatchSet rightPatchSet = review.getRightPatchSet();
			if (rightPatchSet != null) {
				PatchSetColumn rightPatchSetColumn = new PatchSetColumn(rightPatchSet);
				columns.add(rightPatchSetColumn);
			}
		}

		return columns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.swt.comments.data.ICommentProvider#getComments
	 * (java.lang.Object,
	 * at.bitandart.zoubek.mervin.swt.comments.data.ICommentGroup,
	 * at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn)
	 */
	@Override
	public List<IComment> getComments(Object input, ICommentGroup group, ICommentColumn column) {

		List<IComment> comments = new LinkedList<>();
		if (input instanceof ModelReview) {
			ModelReview review = (ModelReview) input;
			// TODO respect the column and the group
			if (column instanceof PatchSetColumn) {
				PatchSet patchSet = ((PatchSetColumn) column).getPatchSet();
				for (Comment realComment : patchSet.getComments()) {
					comments.add(new MervinComment(realComment, Alignment.LEFT));
				}
			}
		}
		return comments;
	}
}

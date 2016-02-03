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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.CommentLink;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.User;
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

	@Inject
	private User currentMervinUser;

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.swt.comments.data.ICommentProvider#
	 * getCommentGroups(java.lang.Object)
	 */
	@Override
	public List<ICommentGroup> getCommentGroups(Object input) {

		final Map<MervinCommentGroup, Long> oldestComments = new HashMap<>();

		List<MervinCommentGroup> groups = new LinkedList<>();
		if (input instanceof ModelReview) {

			ModelReview review = (ModelReview) input;

			// initial group collection phase

			createInitialGroups(oldestComments, groups, review);

			// merge (refinement) phase

			mergeGroups(groups);

		}

		// sort the groups by the oldest comment creation time
		Collections.sort(groups, new Comparator<MervinCommentGroup>() {

			@Override
			public int compare(MervinCommentGroup o1, MervinCommentGroup o2) {
				return (int) (oldestComments.get(o1) - oldestComments.get(o2));
			}
		});

		return new ArrayList<ICommentGroup>(groups);
	}

	/**
	 * creates a initial list of groups based on the comments of the given
	 * review.
	 * 
	 * @param oldestComments
	 *            the map of the oldest found comment creation time for each
	 *            group.
	 * @param groups
	 *            the list of groups to store the groups into.
	 * @param review
	 *            the review to extract the comments from.
	 */
	private void createInitialGroups(final Map<MervinCommentGroup, Long> oldestComments,
			List<MervinCommentGroup> groups, ModelReview review) {
		/*
		 * the default group contains all comments that do not contain any link
		 * with a target.
		 */
		MervinCommentGroup defaultGroup = null;

		for (Comment comment : review.getComments()) {

			Set<EObject> targets = new HashSet<>();
			collectTargets(comment, targets, true, true);

			boolean newGroup = true;

			if (targets.isEmpty()) {
				if (defaultGroup == null) {
					defaultGroup = createGroup(Collections.<EObject> emptySet(), Long.MAX_VALUE, oldestComments,
							groups);
				}
				addToGroup(defaultGroup, targets, comment.getCreationTime(), oldestComments);
				newGroup = false;
			} else {
				for (MervinCommentGroup group : groups) {
					Set<EObject> groupTargets = group.getTargets();
					for (EObject target : targets) {
						if (groupTargets.contains(target)) {
							addToGroup(group, targets, comment.getCreationTime(), oldestComments);
							newGroup = false;
							break;
						}
					}
				}
			}

			if (newGroup) {
				createGroup(targets, comment.getCreationTime(), oldestComments, groups);
			}

		}
	}

	/**
	 * merges all groups which share at least on target.
	 * 
	 * @param groups
	 *            the groups to merge
	 */
	private void mergeGroups(List<MervinCommentGroup> groups) {
		boolean groupsMerged = true;

		/*
		 * try to merge the groups until it is not possible any more. Groups can
		 * be merged if they share at least one target.
		 */
		while (groupsMerged) {

			groupsMerged = false;

			Set<MervinCommentGroup> groupsToRemove = new HashSet<MervinCommentGroup>();

			for (MervinCommentGroup group : groups) {

				if (!groupsToRemove.contains(group)) {

					for (MervinCommentGroup otherGroup : groups) {

						if (otherGroup != group && !groupsToRemove.contains(otherGroup)) {

							for (EObject target : otherGroup.getTargets()) {

								if (group.getTargets().contains(target)) {
									group.getTargets().addAll(otherGroup.getTargets());
									groupsToRemove.add(otherGroup);
									groupsMerged = true;
								}

							}

						}

					}

				}

			}

			groups.removeAll(groupsToRemove);
			groupsToRemove.clear();

		}
	}

	/**
	 * collects all targets of the comment, its replies, and its answered
	 * comments if specified. The targets will be added to the given set.
	 * 
	 * @param comment
	 *            the comment used to start the collect task.
	 * @param targets
	 *            the set to add the targets to.
	 * @param includeAnswered
	 *            pass true to include the targets of the answered comments. If
	 *            the answered comment is also an reply, its answered comment
	 *            targets will also be collected. This applies to all found
	 *            answered comments until it has no answered comment itself.
	 * @param includeReplies
	 *            pass true to include the targets of all replies to this
	 *            comment. This also applies to all found replies until a reply
	 *            does not have any replies itself.
	 */
	private void collectTargets(Comment comment, Set<EObject> targets, boolean includeAnswered,
			boolean includeReplies) {

		for (CommentLink link : comment.getCommentLinks()) {
			targets.addAll(link.getTargets());
		}
		Comment answeredComment = comment.getRepliedTo();
		if (includeAnswered && answeredComment != null) {
			collectTargets(answeredComment, targets, includeAnswered, false);
		}
		if (includeReplies) {
			for (Comment reply : comment.getReplies()) {
				collectTargets(reply, targets, false, includeReplies);
			}
		}

	}

	/**
	 * adds the given targets to the the given group and updates all
	 * accompanying data.
	 * 
	 * @param group
	 *            the group to add the targets to.
	 * @param newTargets
	 *            the new targets to add.
	 * @param commentCreationTime
	 *            the creation time of the comment which these targets belongs
	 *            to.
	 * @param oldestComments
	 *            the map of the oldest found comment creation time for each
	 *            group.
	 */
	private void addToGroup(MervinCommentGroup group, Set<EObject> newTargets, long commentCreationTime,
			Map<MervinCommentGroup, Long> oldestComments) {

		group.getTargets().addAll(newTargets);
		Long oldestTimeStamp = oldestComments.get(group);
		if (oldestTimeStamp == null || oldestTimeStamp.longValue() > commentCreationTime) {
			oldestComments.put(group, commentCreationTime);
		}

	}

	/**
	 * creates a new comment group with the given parameters.
	 * 
	 * @param newTargets
	 *            the set of targets referenced in this group.
	 * @param commentCreationTime
	 *            the creation time of the oldest comment in this group.
	 * @param oldestComments
	 *            the map of the oldest found comment creation time for each
	 *            group.
	 * @param groups
	 *            the list of groups to store this group.
	 * @return the created group.
	 */
	private MervinCommentGroup createGroup(Set<EObject> newTargets, long commentCreationTime,
			Map<MervinCommentGroup, Long> oldestComments, List<MervinCommentGroup> groups) {

		MervinCommentGroup commentGroup = new MervinCommentGroup();
		groups.add(commentGroup);
		addToGroup(commentGroup, newTargets, commentCreationTime, oldestComments);

		return commentGroup;
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

		if (column instanceof PatchSetColumn && group instanceof MervinCommentGroup) {

			PatchSet patchSet = ((PatchSetColumn) column).getPatchSet();
			Set<EObject> targets = ((MervinCommentGroup) group).getTargets();
			ArrayList<Comment> patchSetComments = new ArrayList<>(patchSet.getComments());

			Collections.sort(patchSetComments, new Comparator<Comment>() {

				@Override
				public int compare(Comment o1, Comment o2) {
					return (int) (o1.getCreationTime() - o2.getCreationTime());
				}
			});

			for (Comment realComment : patchSetComments) {
				Set<EObject> commentTargets = new HashSet<>();
				collectTargets(realComment, commentTargets, true, true);

				if (targets.isEmpty() && commentTargets.isEmpty()) {
					addComment(comments, realComment);
				} else {
					for (EObject target : commentTargets) {

						if (targets.contains(target)) {

							addComment(comments, realComment);
							break;
						}

					}
				}

			}
		}

		return comments;
	}

	/**
	 * adds a {@link IComment} for the given comment to the given comment list.
	 * 
	 * @param comments
	 *            the comment list to add the {@link IComment} to.
	 * @param realComment
	 *            the real comment to create the {@link IComment} for.
	 */
	private void addComment(List<IComment> comments, Comment realComment) {
		Alignment alignment = Alignment.RIGHT;
		User author = realComment.getAuthor();
		if (author != null && currentMervinUser != null && currentMervinUser.getName().equals(author.getName())) {
			alignment = Alignment.LEFT;
		}
		comments.add(new MervinComment(realComment, alignment));
	}

}

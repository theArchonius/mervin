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
package at.bitandart.zoubek.mervin.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.junit.Before;
import org.junit.Test;

import at.bitandart.zoubek.mervin.comments.ICommonTargetResolver;
import at.bitandart.zoubek.mervin.comments.MervinComment;
import at.bitandart.zoubek.mervin.comments.MervinCommentGroup;
import at.bitandart.zoubek.mervin.comments.MervinCommentProvider;
import at.bitandart.zoubek.mervin.comments.PatchSetColumn;
import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.CommentLink;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.User;
import at.bitandart.zoubek.mervin.swt.comments.data.IComment;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentGroup;

/**
 * Test the methods of {@link MervinCommentProvider}. This test currently
 * assumes that the returned comment groups are sorted by the creation date of
 * their oldest comment in ascending order.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinCommentProviderTest {

	/**
	 * the factory used to create the test model.
	 */
	private ModelReviewFactory reviewFactory;

	/**
	 * the provider to test
	 */
	private MervinCommentProvider commentProvider;

	/**
	 * counter used to create the unique ids of comments during each test.
	 */
	private int commentCounter = 0;

	/**
	 * the current user used by the provider
	 */
	private User testUser;

	@Before
	public void init() {

		reviewFactory = ModelReviewFactory.eINSTANCE;
		IEclipseContext context = EclipseContextFactory.create();

		testUser = reviewFactory.createUser();
		context.set(User.class, testUser);

		ICommonTargetResolver commonTargetResolver = new ICommonTargetResolver() {

			@Override
			public Set<EObject> findCommonTargets(Collection<EObject> targets, PatchSet patchSet) {
				return new HashSet<EObject>(targets);
			}
		};
		context.set(ICommonTargetResolver.class, commonTargetResolver);

		commentProvider = ContextInjectionFactory.make(MervinCommentProvider.class, context);
		commentCounter = 0;
	}

	/**
	 * Single column with one comment that resides in one group.
	 */
	@Test
	public void testSingleColumnWithOneGroupOneComment() {

		// input
		ModelReview review = reviewFactory.createModelReview();

		List<PatchSet> patchSets = new ArrayList<PatchSet>();
		patchSets.add(createLeftPatchSet(review));

		List<Comment> leftPSG1Comments = new ArrayList<Comment>();
		leftPSG1Comments.add(createComment(patchSets.get(0), null, review));

		List<Set<EObject>> expectedTargetSets = new ArrayList<Set<EObject>>();
		expectedTargetSets.add(new HashSet<EObject>());

		// test
		List<ICommentColumn> commentColumns = commentProvider.getCommentColumns(review);
		assertColumns(patchSets, commentColumns);

		List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(review);
		assertCommentGroups(expectedTargetSets, commentGroups);

		List<IComment> comments = commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(0));
		assertComments(leftPSG1Comments, comments);

	}

	/**
	 * Single column with 2 comments, each in a different group.
	 */
	@Test
	public void testSingleColumnWithTwoGroupsOneCommentEach() {

		// input
		ModelReview review = reviewFactory.createModelReview();

		List<PatchSet> patchSets = new ArrayList<PatchSet>();
		patchSets.add(createLeftPatchSet(review));

		EObject target1 = EcoreFactory.eINSTANCE.createEObject();
		Set<EObject> group1Targets = new HashSet<EObject>();
		group1Targets.add(target1);

		List<Comment> leftPSG1Comments = new ArrayList<Comment>();
		leftPSG1Comments.add(createComment(patchSets.get(0), null, review));
		createCommentLink(leftPSG1Comments.get(0), group1Targets);

		List<Comment> leftPSG2Comments = new ArrayList<Comment>();
		leftPSG2Comments.add(createComment(patchSets.get(0), null, review));

		List<Set<EObject>> expectedTargetSets = new ArrayList<Set<EObject>>();
		expectedTargetSets.add(group1Targets);
		expectedTargetSets.add(new HashSet<EObject>());

		// test
		List<ICommentColumn> commentColumns = commentProvider.getCommentColumns(review);
		assertColumns(patchSets, commentColumns);

		List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(review);
		assertCommentGroups(expectedTargetSets, commentGroups);

		assertComments(leftPSG1Comments,
				commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(0)));
		assertComments(leftPSG2Comments,
				commentProvider.getComments(review, commentGroups.get(1), commentColumns.get(0)));

	}

	/**
	 * Single column with a comment in one group, and a comment with a reply in
	 * a second group. The target that causes the second group is specified in
	 * the second comment.
	 */
	@Test
	public void testSingleColumnWithTwoGroupsOneCommentEachAndOneReplyInSecondGroup() {

		// input
		ModelReview review = reviewFactory.createModelReview();

		List<PatchSet> patchSets = new ArrayList<PatchSet>();
		patchSets.add(createLeftPatchSet(review));

		List<Comment> leftPSG1Comments = new ArrayList<Comment>();
		leftPSG1Comments.add(createComment(patchSets.get(0), null, review));

		EObject target1 = EcoreFactory.eINSTANCE.createEObject();
		Set<EObject> group2Targets = new HashSet<EObject>();
		group2Targets.add(target1);

		List<Comment> leftPSG2Comments = new ArrayList<Comment>();
		leftPSG2Comments.add(createComment(patchSets.get(0), null, review));
		leftPSG2Comments.add(createComment(patchSets.get(0), leftPSG2Comments.get(0), review));

		createCommentLink(leftPSG2Comments.get(0), group2Targets);

		List<Set<EObject>> expectedTargetSets = new ArrayList<Set<EObject>>();
		expectedTargetSets.add(new HashSet<EObject>());
		expectedTargetSets.add(group2Targets);

		// test
		List<ICommentColumn> commentColumns = commentProvider.getCommentColumns(review);
		assertColumns(patchSets, commentColumns);

		List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(review);
		assertCommentGroups(expectedTargetSets, commentGroups);

		assertComments(leftPSG1Comments,
				commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(0)));
		assertComments(leftPSG2Comments,
				commentProvider.getComments(review, commentGroups.get(1), commentColumns.get(0)));

	}

	/**
	 * Single column with a comment in one group, and a comment with a reply in
	 * a second group. The target that causes the second group is specified in
	 * the reply.
	 */
	@Test
	public void testSingleColumnWithTwoGroupsOneCommentEachAndOneReplyInSecondGroupTargetInReply() {

		// input
		ModelReview review = reviewFactory.createModelReview();

		List<PatchSet> patchSets = new ArrayList<PatchSet>();
		patchSets.add(createLeftPatchSet(review));

		List<Comment> leftPSG1Comments = new ArrayList<Comment>();
		leftPSG1Comments.add(createComment(patchSets.get(0), null, review));

		EObject target1 = EcoreFactory.eINSTANCE.createEObject();
		Set<EObject> group2Targets = new HashSet<EObject>();
		group2Targets.add(target1);

		List<Comment> leftPSG2Comments = new ArrayList<Comment>();
		leftPSG2Comments.add(createComment(patchSets.get(0), null, review));
		leftPSG2Comments.add(createComment(patchSets.get(0), leftPSG2Comments.get(0), review));

		createCommentLink(leftPSG2Comments.get(1), group2Targets);

		List<Set<EObject>> expectedTargetSets = new ArrayList<Set<EObject>>();
		expectedTargetSets.add(new HashSet<EObject>());
		expectedTargetSets.add(group2Targets);

		// test
		List<ICommentColumn> commentColumns = commentProvider.getCommentColumns(review);
		assertColumns(patchSets, commentColumns);

		List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(review);
		assertCommentGroups(expectedTargetSets, commentGroups);

		assertComments(leftPSG1Comments,
				commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(0)));
		assertComments(leftPSG2Comments,
				commentProvider.getComments(review, commentGroups.get(1), commentColumns.get(0)));

	}

	/**
	 * Single column with a comment in one group, and a comment with a reply in
	 * a second group. The comment in the first group contains one target, the
	 * comments in the second group contain multiple targets.
	 */
	@Test
	public void testSingleColumnWithTwoGroupsOneCommentEachAndOneReplyMultipleTargetsInGroupTwo() {

		// input
		ModelReview review = reviewFactory.createModelReview();

		List<PatchSet> patchSets = new ArrayList<PatchSet>();
		patchSets.add(createLeftPatchSet(review));

		EObject target1 = EcoreFactory.eINSTANCE.createEObject();
		EObject target2 = EcoreFactory.eINSTANCE.createEObject();
		EObject target3 = EcoreFactory.eINSTANCE.createEObject();
		EObject target4 = EcoreFactory.eINSTANCE.createEObject();

		Set<EObject> group1Targets = new HashSet<EObject>();
		group1Targets.add(target1);

		Set<EObject> group2Targets = new HashSet<EObject>();
		group2Targets.add(target2);
		group2Targets.add(target3);
		group2Targets.add(target4);

		List<Comment> leftPSG1Comments = new ArrayList<Comment>();
		leftPSG1Comments.add(createComment(patchSets.get(0), null, review));

		createCommentLink(leftPSG1Comments.get(0), new HashSet<EObject>(Arrays.asList(new EObject[] { target1 })));

		List<Comment> leftPSG2Comments = new ArrayList<Comment>();
		leftPSG2Comments.add(createComment(patchSets.get(0), null, review));
		leftPSG2Comments.add(createComment(patchSets.get(0), leftPSG2Comments.get(0), review));

		createCommentLink(leftPSG2Comments.get(0), new HashSet<EObject>(Arrays.asList(new EObject[] { target2 })));
		createCommentLink(leftPSG2Comments.get(1),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target3, target4 })));

		List<Set<EObject>> expectedTargetSets = new ArrayList<Set<EObject>>();
		expectedTargetSets.add(group1Targets);
		expectedTargetSets.add(group2Targets);

		// test
		List<ICommentColumn> commentColumns = commentProvider.getCommentColumns(review);
		assertColumns(patchSets, commentColumns);

		List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(review);
		assertCommentGroups(expectedTargetSets, commentGroups);

		assertComments(leftPSG1Comments,
				commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(0)));
		assertComments(leftPSG2Comments,
				commentProvider.getComments(review, commentGroups.get(1), commentColumns.get(0)));

	}

	/**
	 * Single column with multiple comments in two groups. The comments in the
	 * first group contains multiple targets, the comments in the second group
	 * contain only one same target.
	 */
	@Test
	public void testSingleColumnWithTwoGroupsMultipleCommentsEachAndMultipleTargetsInGroupOne() {

		// input
		ModelReview review = reviewFactory.createModelReview();

		List<PatchSet> patchSets = new ArrayList<PatchSet>();
		patchSets.add(createLeftPatchSet(review));

		EObject target1 = EcoreFactory.eINSTANCE.createEObject();
		EObject target2 = EcoreFactory.eINSTANCE.createEObject();
		EObject target3 = EcoreFactory.eINSTANCE.createEObject();
		EObject target4 = EcoreFactory.eINSTANCE.createEObject();

		Set<EObject> group1Targets = new HashSet<EObject>();
		group1Targets.add(target1);
		group1Targets.add(target2);
		group1Targets.add(target3);

		Set<EObject> group2Targets = new HashSet<EObject>();
		group2Targets.add(target4);

		List<Comment> leftPSG1Comments = new ArrayList<Comment>();
		leftPSG1Comments.add(createComment(patchSets.get(0), null, review));
		leftPSG1Comments.add(createComment(patchSets.get(0), null, review));
		leftPSG1Comments.add(createComment(patchSets.get(0), null, review));
		leftPSG1Comments.add(createComment(patchSets.get(0), null, review));

		createCommentLink(leftPSG1Comments.get(0), new HashSet<EObject>(Arrays.asList(new EObject[] { target1 })));
		createCommentLink(leftPSG1Comments.get(1),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target1, target2 })));
		createCommentLink(leftPSG1Comments.get(2),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target3, target2 })));
		createCommentLink(leftPSG1Comments.get(3),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target3, target1 })));

		List<Comment> leftPSG2Comments = new ArrayList<Comment>();
		leftPSG2Comments.add(createComment(patchSets.get(0), null, review));
		leftPSG2Comments.add(createComment(patchSets.get(0), null, review));
		leftPSG2Comments.add(createComment(patchSets.get(0), null, review));

		createCommentLink(leftPSG2Comments.get(0), new HashSet<EObject>(Arrays.asList(new EObject[] { target4 })));
		createCommentLink(leftPSG2Comments.get(1), new HashSet<EObject>(Arrays.asList(new EObject[] { target4 })));
		createCommentLink(leftPSG2Comments.get(2), new HashSet<EObject>(Arrays.asList(new EObject[] { target4 })));

		List<Set<EObject>> expectedTargetSets = new ArrayList<Set<EObject>>();
		expectedTargetSets.add(group1Targets);
		expectedTargetSets.add(group2Targets);

		// test
		List<ICommentColumn> commentColumns = commentProvider.getCommentColumns(review);
		assertColumns(patchSets, commentColumns);

		List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(review);
		assertCommentGroups(expectedTargetSets, commentGroups);

		assertComments(leftPSG1Comments,
				commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(0)));
		assertComments(leftPSG2Comments,
				commentProvider.getComments(review, commentGroups.get(1), commentColumns.get(0)));

	}

	/**
	 * Single column with multiple comments in one group. The input data is not
	 * sorted by the creation date.
	 */
	@Test
	public void testSingleColumnWithOneGroupAndUnsortedComments() {

		// input
		ModelReview review = reviewFactory.createModelReview();

		List<PatchSet> patchSets = new ArrayList<PatchSet>();
		patchSets.add(createLeftPatchSet(review));

		List<Comment> leftPSG1Comments = createComments(5, patchSets.get(0), null, review);

		shuffle(review.getComments(), new Random(0));
		shuffle(patchSets.get(0).getComments(), new Random(0));

		List<Set<EObject>> expectedTargetSets = new ArrayList<Set<EObject>>();
		expectedTargetSets.add(new HashSet<EObject>());

		// test
		List<ICommentColumn> commentColumns = commentProvider.getCommentColumns(review);
		assertColumns(patchSets, commentColumns);

		List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(review);
		assertCommentGroups(expectedTargetSets, commentGroups);

		List<IComment> comments = commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(0));
		assertComments(leftPSG1Comments, comments);

	}

	/**
	 * Two columns with one group and one comment in the left column.
	 */
	@Test
	public void testTwoColumnsWithOneGroupOneCommentLeft() {

		// input
		ModelReview review = reviewFactory.createModelReview();

		List<PatchSet> patchSets = createBothPatchSets(review);

		List<Comment> leftPSG1Comments = new ArrayList<Comment>();
		leftPSG1Comments.add(createComment(patchSets.get(0), null, review));

		List<Set<EObject>> expectedTargetSets = new ArrayList<Set<EObject>>();
		expectedTargetSets.add(new HashSet<EObject>());

		// test
		List<ICommentColumn> commentColumns = commentProvider.getCommentColumns(review);
		assertColumns(patchSets, commentColumns);

		List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(review);
		assertCommentGroups(expectedTargetSets, commentGroups);

		List<IComment> comments = commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(0));
		assertComments(leftPSG1Comments, comments);

		comments = commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(1));
		assertComments(new ArrayList<Comment>(), comments);

	}

	/**
	 * Two columns with one group and one comment in each column.
	 */
	@Test
	public void testTwoColumnsWithOneGroupOneCommentEach() {

		// input
		ModelReview review = reviewFactory.createModelReview();

		List<PatchSet> patchSets = createBothPatchSets(review);

		List<Comment> leftPSG1Comments = new ArrayList<Comment>();
		leftPSG1Comments.add(createComment(patchSets.get(0), null, review));

		List<Comment> rightPSG1Comments = new ArrayList<Comment>();
		rightPSG1Comments.add(createComment(patchSets.get(1), null, review));

		List<Set<EObject>> expectedTargetSets = new ArrayList<Set<EObject>>();
		expectedTargetSets.add(new HashSet<EObject>());

		// test
		List<ICommentColumn> commentColumns = commentProvider.getCommentColumns(review);

		assertColumns(patchSets, commentColumns);

		List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(review);
		assertCommentGroups(expectedTargetSets, commentGroups);

		List<IComment> comments = commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(0));
		assertComments(leftPSG1Comments, comments);

		comments = commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(1));
		assertComments(rightPSG1Comments, comments);

	}

	/**
	 * Single column with one group and one comment with a reply.
	 */
	@Test
	public void testSingleColumnsWithOneGroupOneCommentWithReply() {

		// input
		ModelReview review = reviewFactory.createModelReview();

		PatchSet expectedLeftPatchSet = createLeftPatchSet(review);

		List<Comment> commentWithReply = createCommentWithReply(expectedLeftPatchSet, review);

		List<Set<EObject>> expectedTargetSets = new ArrayList<Set<EObject>>();
		expectedTargetSets.add(new HashSet<EObject>());

		// test
		List<ICommentColumn> commentColumns = commentProvider.getCommentColumns(review);
		assertColumns(new PatchSet[] { expectedLeftPatchSet }, commentColumns);

		List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(review);
		assertCommentGroups(expectedTargetSets, commentGroups);

		List<IComment> comments = commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(0));
		assertComments(commentWithReply, comments);

	}

	/**
	 * Two columns with multiple comments in two groups. The comments in both
	 * groups contain multiple targets and each column has comments.
	 */
	@Test
	public void testTwoColumnsWithTwoGroupsMultipleCommentsEachAndMultipleTargets() {

		// input
		ModelReview review = reviewFactory.createModelReview();

		List<PatchSet> patchSets = createBothPatchSets(review);

		EObject target1 = EcoreFactory.eINSTANCE.createEObject();
		EObject target2 = EcoreFactory.eINSTANCE.createEObject();
		EObject target3 = EcoreFactory.eINSTANCE.createEObject();

		EObject target4 = EcoreFactory.eINSTANCE.createEObject();
		EObject target5 = EcoreFactory.eINSTANCE.createEObject();
		EObject target6 = EcoreFactory.eINSTANCE.createEObject();

		Set<EObject> group1Targets = new HashSet<EObject>();
		group1Targets.add(target1);
		group1Targets.add(target2);
		group1Targets.add(target3);

		Set<EObject> group2Targets = new HashSet<EObject>();
		group2Targets.add(target4);
		group2Targets.add(target5);
		group2Targets.add(target6);

		List<Comment> leftPSG1Comments = createComments(4, patchSets.get(0), null, review);

		createCommentLink(leftPSG1Comments.get(0), new HashSet<EObject>(Arrays.asList(new EObject[] { target1 })));
		createCommentLink(leftPSG1Comments.get(1),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target1, target2 })));
		createCommentLink(leftPSG1Comments.get(2),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target3, target2 })));
		createCommentLink(leftPSG1Comments.get(3),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target3, target1 })));

		List<Comment> leftPSG2Comments = createComments(3, patchSets.get(0), null, review);

		createCommentLink(leftPSG2Comments.get(0), new HashSet<EObject>(Arrays.asList(new EObject[] { target4 })));
		createCommentLink(leftPSG2Comments.get(1), new HashSet<EObject>(Arrays.asList(new EObject[] { target4 })));
		createCommentLink(leftPSG2Comments.get(2), new HashSet<EObject>(Arrays.asList(new EObject[] { target4 })));

		List<Comment> rightPSG1Comments = createComments(4, patchSets.get(1), null, review);

		createCommentLink(rightPSG1Comments.get(0), new HashSet<EObject>(Arrays.asList(new EObject[] { target1 })));
		createCommentLink(rightPSG1Comments.get(1),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target1, target2 })));
		createCommentLink(rightPSG1Comments.get(2),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target3, target2 })));
		createCommentLink(rightPSG1Comments.get(3),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target3, target1 })));

		List<Comment> rightPSG2Comments = createComments(5, patchSets.get(1), null, review);

		createCommentLink(rightPSG2Comments.get(0), new HashSet<EObject>(Arrays.asList(new EObject[] { target4 })));
		createCommentLink(rightPSG2Comments.get(1),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target4, target5 })));
		createCommentLink(rightPSG2Comments.get(2),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target5, target6 })));
		createCommentLink(rightPSG2Comments.get(3), new HashSet<EObject>(Arrays.asList(new EObject[] { target6 })));
		createCommentLink(rightPSG2Comments.get(4), new HashSet<EObject>(Arrays.asList(new EObject[] { target5 })));

		List<Set<EObject>> expectedTargetSets = new ArrayList<Set<EObject>>();
		expectedTargetSets.add(group1Targets);
		expectedTargetSets.add(group2Targets);

		// test
		List<ICommentColumn> commentColumns = commentProvider.getCommentColumns(review);
		assertColumns(patchSets, commentColumns);

		List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(review);
		assertCommentGroups(expectedTargetSets, commentGroups);

		assertComments(leftPSG1Comments,
				commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(0)));
		assertComments(leftPSG2Comments,
				commentProvider.getComments(review, commentGroups.get(1), commentColumns.get(0)));

		assertComments(rightPSG1Comments,
				commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(1)));
		assertComments(rightPSG2Comments,
				commentProvider.getComments(review, commentGroups.get(1), commentColumns.get(1)));

	}

	/**
	 * Two columns with multiple comments in two groups. The comments in both
	 * groups contains multiple targets and each group contains only comments in
	 * one column (group one -> right, group two -> left).
	 */
	@Test
	public void testTwoColumnsWithTwoGroupsAlternatingMultipleCommentsEachAndMultipleTargets() {

		// input
		ModelReview review = reviewFactory.createModelReview();

		List<PatchSet> patchSets = createBothPatchSets(review);

		EObject target1 = EcoreFactory.eINSTANCE.createEObject();
		EObject target2 = EcoreFactory.eINSTANCE.createEObject();
		EObject target3 = EcoreFactory.eINSTANCE.createEObject();

		EObject target4 = EcoreFactory.eINSTANCE.createEObject();
		EObject target5 = EcoreFactory.eINSTANCE.createEObject();
		EObject target6 = EcoreFactory.eINSTANCE.createEObject();

		Set<EObject> group1Targets = new HashSet<EObject>();
		group1Targets.add(target1);
		group1Targets.add(target2);
		group1Targets.add(target3);

		Set<EObject> group2Targets = new HashSet<EObject>();
		group2Targets.add(target4);
		group2Targets.add(target5);
		group2Targets.add(target6);

		List<Comment> rightPSG1Comments = createComments(4, patchSets.get(1), null, review);

		createCommentLink(rightPSG1Comments.get(0), new HashSet<EObject>(Arrays.asList(new EObject[] { target1 })));
		createCommentLink(rightPSG1Comments.get(1),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target1, target2 })));
		createCommentLink(rightPSG1Comments.get(2),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target3, target2 })));
		createCommentLink(rightPSG1Comments.get(3),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target3, target1 })));

		List<Comment> leftPSG2Comments = createComments(3, patchSets.get(0), null, review);

		createCommentLink(leftPSG2Comments.get(0),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target4, target5 })));
		createCommentLink(leftPSG2Comments.get(1),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target4, target6 })));
		createCommentLink(leftPSG2Comments.get(2),
				new HashSet<EObject>(Arrays.asList(new EObject[] { target5, target6 })));

		List<Set<EObject>> expectedTargetSets = new ArrayList<Set<EObject>>();
		expectedTargetSets.add(group1Targets);
		expectedTargetSets.add(group2Targets);

		// test
		List<ICommentColumn> commentColumns = commentProvider.getCommentColumns(review);
		assertColumns(patchSets, commentColumns);

		List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(review);
		assertCommentGroups(expectedTargetSets, commentGroups);

		assertComments(leftPSG2Comments,
				commentProvider.getComments(review, commentGroups.get(1), commentColumns.get(0)));

		assertComments(rightPSG1Comments,
				commentProvider.getComments(review, commentGroups.get(0), commentColumns.get(1)));

	}

	/**
	 * creates a patch set for the given review.
	 * 
	 * @param review
	 * @return the created patch set.
	 */
	private PatchSet createPatchSet(ModelReview review) {

		PatchSet patchSet = reviewFactory.createPatchSet();
		review.getPatchSets().add(patchSet);

		return patchSet;
	}

	/**
	 * creates a patch set for the given review and assigns it to be the
	 * selected left patch set.
	 * 
	 * @param review
	 * @return the created patch set.
	 */
	private PatchSet createLeftPatchSet(ModelReview review) {

		PatchSet patchSet = createPatchSet(review);
		review.setLeftPatchSet(patchSet);

		return patchSet;
	}

	/**
	 * creates a patch set for the given review and assigns it to be the
	 * selected right patch set.
	 * 
	 * @param review
	 * @return the created patch set.
	 */
	private PatchSet createRightPatchSet(ModelReview review) {

		PatchSet patchSet = createPatchSet(review);
		review.setRightPatchSet(patchSet);

		return patchSet;
	}

	/**
	 * creates two patch sets and assigns one to the left selected patch set and
	 * the other to the right selected patch set.
	 * 
	 * @param review
	 * @return a list containing both patch sets, the first is the left patch
	 *         set, the second is the right patch set.
	 */
	private List<PatchSet> createBothPatchSets(ModelReview review) {

		List<PatchSet> patchSets = new ArrayList<>();
		patchSets.add(createLeftPatchSet(review));
		patchSets.add(createRightPatchSet(review));

		return patchSets;
	}

	/**
	 * creates a new comment for the given patch set in the given model review
	 * and marks it as reply to another comment if a comment to reply to is
	 * passed.
	 * 
	 * @param patchSet
	 *            the patch set associated with this comment.
	 * @param replyTo
	 *            the comment which will be replied to by this comment, may be
	 *            null if the new comment should not be a reply.
	 * @param review
	 *            the review which contains the comment.
	 * @return the created comment.
	 */
	private Comment createComment(PatchSet patchSet, Comment replyTo, ModelReview review) {

		commentCounter++;
		Comment comment = reviewFactory.createComment();
		comment.setPatchset(patchSet);
		comment.setRepliedTo(replyTo);
		comment.setId("" + commentCounter);
		comment.setCreationTime(commentCounter);
		review.getComments().add(comment);

		return comment;
	}

	/**
	 * creates a new comment for the given patch set in the given model review
	 * and a second one that replies to the first one.
	 * 
	 * @param patchSet
	 *            the patch set associated with this comment.
	 * @param review
	 *            the review which contains the comment.
	 * @return a list of the created comments.
	 */
	private List<Comment> createCommentWithReply(PatchSet patchSet, ModelReview review) {

		List<Comment> comments = new ArrayList<>();
		Comment comment = createComment(patchSet, null, review);
		Comment reply = createComment(patchSet, comment, review);
		comments.add(comment);
		comments.add(reply);

		return comments;
	}

	/**
	 * creates a number of new comments for the given patch set in the given
	 * model review and marks them as reply to another comment if a comment to
	 * reply to is passed.
	 * 
	 * @param numComments
	 *            the number of comments to create.
	 * @param patchSet
	 *            the patch set associated with this comment.
	 * @param replyTo
	 *            the comment which will be replied to by this comment, may be
	 *            null if the new comment should not be a reply.
	 * @param review
	 *            the review which contains the comment.
	 * @return a list of the created comments.
	 */
	private List<Comment> createComments(int numComments, PatchSet patchSet, Comment replyTo, ModelReview review) {

		List<Comment> comments = new ArrayList<>();

		for (int i = 0; i < numComments; i++) {
			comments.add(createComment(patchSet, replyTo, review));
		}

		return comments;
	}

	/**
	 * creates a new comment link for the given comment.
	 * 
	 * @param comment
	 *            the comment to create the comment link for.
	 * @param targets
	 *            the targets of the comment link.
	 * @return the created comment link.
	 */
	private CommentLink createCommentLink(Comment comment, Set<EObject> targets) {

		CommentLink commentLink = reviewFactory.createCommentLink();
		commentLink.setComment(comment);
		commentLink.getTargets().addAll(targets);

		return commentLink;
	}

	/**
	 * shuffles the given list without creating temporary duplicated entries.
	 * 
	 * @param list
	 * @param random
	 */
	private <T> void shuffle(List<T> list, Random random) {

		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				int index1 = random.nextInt(list.size());
				int index2 = random.nextInt(list.size());
				if (index1 < index2) {
					index2--;
				}
				T element = list.remove(index1);
				list.add(index2, element);
			}
		}

	}

	/**
	 * asserts that the columns match the corresponding patch sets.
	 * 
	 * @param patchSets
	 * @param columns
	 */
	private static void assertColumns(PatchSet[] patchSets, List<ICommentColumn> columns) {
		assertColumns(Arrays.asList(patchSets), columns);
	}

	/**
	 * asserts that the columns match the corresponding patch sets.
	 * 
	 * @param patchSets
	 * @param columns
	 */
	private static void assertColumns(List<PatchSet> patchSets, List<ICommentColumn> columns) {

		assertEquals(patchSets.size(), columns.size());
		Iterator<ICommentColumn> iterator = columns.iterator();
		for (PatchSet patchSet : patchSets) {
			ICommentColumn column = iterator.next();
			assertTrue(column instanceof PatchSetColumn);
			assertEquals(patchSet, ((PatchSetColumn) column).getPatchSet());
		}

	}

	/**
	 * asserts that the comments match the expected comments.
	 * 
	 * @param expectedComments
	 * @param comments
	 */
	private static void assertComments(List<Comment> expectedComments, List<IComment> comments) {

		assertEquals(expectedComments.size(), comments.size());
		Iterator<IComment> iterator = comments.iterator();
		for (Comment expectedComment : expectedComments) {
			IComment comment = iterator.next();
			assertTrue(comment instanceof MervinComment);
			assertEquals(expectedComment, ((MervinComment) comment).getRealComment());
		}

	}

	/**
	 * asserts that the comment groups match the expected target sets.
	 * 
	 * @param expectedTargetSets
	 * @param groups
	 */
	private static void assertCommentGroups(List<Set<EObject>> expectedTargetSets, List<ICommentGroup> groups) {

		assertEquals(expectedTargetSets.size(), groups.size());

		Iterator<Set<EObject>> iterator = expectedTargetSets.iterator();
		for (ICommentGroup group : groups) {
			Set<EObject> expectedTargets = iterator.next();
			assertTrue(group instanceof MervinCommentGroup);
			Set<EObject> targets = ((MervinCommentGroup) group).getTargets();
			assertEquals(expectedTargets, targets);
		}

	}

}

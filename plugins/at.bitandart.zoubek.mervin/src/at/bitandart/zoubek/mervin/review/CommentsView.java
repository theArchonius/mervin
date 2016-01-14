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
package at.bitandart.zoubek.mervin.review;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.swt.comments.Comment;
import at.bitandart.zoubek.mervin.swt.comments.Comment.Alignment;
import at.bitandart.zoubek.mervin.swt.comments.CommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.CommentGroup;
import at.bitandart.zoubek.mervin.swt.comments.CommentLink;
import at.bitandart.zoubek.mervin.swt.comments.CommentList;
import at.bitandart.zoubek.mervin.swt.comments.CommentList.CommentLinkListener;
import at.bitandart.zoubek.mervin.swt.comments.CommentListViewer;
import at.bitandart.zoubek.mervin.swt.comments.CommentProvider;

public class CommentsView extends ModelReviewEditorTrackingView {

	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.comments";

	// Viewers
	private CommentListViewer commentListViewer;

	// Data
	private boolean viewInitialized = false;

	// SWT Controls
	private ScrolledForm mainForm;
	private Composite mainPanel;

	@Inject
	public CommentsView() {
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		mainForm = toolkit.createScrolledForm(parent);

		mainPanel = mainForm.getBody();
		mainPanel.setLayout(new TableWrapLayout());

		commentListViewer = new CommentListViewer(mainPanel, toolkit);
		commentListViewer.setCommentProvider(new MervinCommentProvider());
		CommentList control = commentListViewer.getCommentListControl();
		control.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		control.addCommentLinkListener(new CommentLinkListener() {

			@Override
			public void commentLinkClicked(CommentLink commentLink) {
				// TODO replace with correct click behaviour
				System.out.println("comment link click handler not yet implemented");
			}

			@Override
			public void commentLinkEnter(CommentLink commentLink) {
				// TODO replace with correct click behaviour
				System.out.println("comment link enter handler not yet implemented");
			}

			@Override
			public void commentLinkExit(CommentLink commentLink) {
				// TODO replace with correct click behaviour
				System.out.println("comment link exit handler not yet implemented");
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

		// TODO
		commentListViewer.refresh();
	}

	private class MervinCommentProvider implements CommentProvider {

		@Override
		public List<CommentColumn> getCommentColumns(Object input) {

			List<CommentColumn> commentColumn = new LinkedList<CommentColumn>();
			commentColumn.add(new MervinCommentColumn("PatchSet 1"));
			commentColumn.add(new MervinCommentColumn("PatchSet 2"));
			return commentColumn;
		}

		@Override
		public List<CommentGroup> getCommentGroups(Object input) {

			List<CommentGroup> commentGroups = new LinkedList<CommentGroup>();
			// TODO read from model and remove test values below
			commentGroups.add(new MervinCommentGroup("Review comments"));
			commentGroups.add(new MervinCommentGroup("Group 1"));
			commentGroups.add(new MervinCommentGroup("Group 2"));
			commentGroups.add(new MervinCommentGroup("Group 3"));

			return commentGroups;
		}

		@Override
		public List<Comment> getComments(CommentGroup group, CommentColumn commentColumn) {

			List<Comment> comments = new LinkedList<Comment>();
			// TODO read from model and remove test values below

			String prefix = group.getGroupTitle() + " - " + commentColumn.getTitle() + " -\n";

			List<CommentLink> links = new LinkedList<CommentLink>();
			links.add(new MervinCommentLink(prefix.length(), 5));// Lorem
			links.add(new MervinCommentLink(prefix.length() + 22, 4));// amet

			comments.add(new MervinComment("Someone", new GregorianCalendar(2015, 3, 2, 3, 46),
					prefix + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
					Alignment.LEFT, links));
			comments.add(new MervinComment("Someone other", new GregorianCalendar(2001, 12, 7, 3, 13),
					prefix + "At vero eos et accusam et justo duo dolores et ea rebum.", Alignment.RIGHT, null));
			comments.add(new MervinComment("Someone", new GregorianCalendar(2015, 6, 2, 5, 46),
					prefix + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
					Alignment.LEFT, null));
			comments.add(new MervinComment("Someone other", new GregorianCalendar(2015, 11, 2, 8, 16), prefix + "...",
					Alignment.RIGHT, null));
			comments.add(new MervinComment("Someone other", new GregorianCalendar(2015, 11, 2, 8, 16),
					prefix + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
					Alignment.RIGHT, null));

			return comments;
		}

	}

	private class MervinCommentGroup implements CommentGroup {

		private String name;

		public MervinCommentGroup(String name) {
			this.name = name;
		}

		@Override
		public String getGroupTitle() {
			return name;
		}

	}

	private class MervinComment implements Comment {

		// TODO replace with reference to comment of the mervin model

		private String author;
		private Calendar creationTime;
		private String body;
		private Alignment alignment;
		private List<CommentLink> commentLinks;

		/**
		 * @param author
		 * @param creationTime
		 * @param body
		 * @param alignment
		 */
		public MervinComment(String author, Calendar creationTime, String body, Alignment alignment,
				List<CommentLink> commentLinks) {
			super();
			this.author = author;
			this.creationTime = creationTime;
			this.body = body;
			this.alignment = alignment;
			if (commentLinks == null) {
				this.commentLinks = Collections.emptyList();
			} else {
				this.commentLinks = commentLinks;
			}
		}

		@Override
		public String getAuthor() {
			return author;
		}

		@Override
		public Calendar getCreationTime() {
			return creationTime;
		}

		@Override
		public String getBody() {
			return body;
		}

		@Override
		public Alignment getAlignment() {
			return alignment;
		}

		@Override
		public List<CommentLink> getCommentLinks() {
			return commentLinks;
		}

	}

	private class MervinCommentColumn implements CommentColumn {

		private String title;

		public MervinCommentColumn(String title) {
			super();
			this.title = title;
		}

		@Override
		public String getTitle() {
			return title;
		}

	}

	private class MervinCommentLink implements CommentLink {

		private int startIndex;

		private int length;

		public MervinCommentLink(int startIndex, int length) {
			super();
			this.startIndex = startIndex;
			this.length = length;
		}

		@Override
		public int getStartIndex() {
			return startIndex;
		}

		@Override
		public int getLength() {
			return length;
		}

	}

}
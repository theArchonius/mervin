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
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.swt.comments.Comment;
import at.bitandart.zoubek.mervin.swt.comments.Comment.Alignment;
import at.bitandart.zoubek.mervin.swt.comments.CommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.CommentGroup;
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
		Control control = commentListViewer.getControl();
		control.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
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
			comments.add(new MervinComment("Someone", new GregorianCalendar(2015, 3, 2, 3, 46),
					group.getGroupTitle() + " - " + commentColumn.getTitle() + " - "
							+ "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
					Alignment.LEFT));
			comments.add(new MervinComment("Someone other", new GregorianCalendar(2001, 12, 7, 3, 13),
					group.getGroupTitle() + " - " + commentColumn.getTitle() + " - "
							+ "At vero eos et accusam et justo duo dolores et ea rebum.",
					Alignment.RIGHT));
			comments.add(new MervinComment("Someone", new GregorianCalendar(2015, 6, 2, 5, 46),
					group.getGroupTitle() + " - " + commentColumn.getTitle() + " - "
							+ "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
					Alignment.LEFT));
			comments.add(new MervinComment("Someone other", new GregorianCalendar(2015, 11, 2, 8, 16),
					group.getGroupTitle() + " - " + commentColumn.getTitle() + " - " + "...", Alignment.RIGHT));
			comments.add(new MervinComment("Someone other", new GregorianCalendar(2015, 11, 2, 8, 16),
					group.getGroupTitle() + " - " + commentColumn.getTitle() + " - "
							+ "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
					Alignment.RIGHT));

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

		/**
		 * @param author
		 * @param creationTime
		 * @param body
		 * @param alignment
		 */
		public MervinComment(String author, Calendar creationTime, String body, Alignment alignment) {
			super();
			this.author = author;
			this.creationTime = creationTime;
			this.body = body;
			this.alignment = alignment;
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

}
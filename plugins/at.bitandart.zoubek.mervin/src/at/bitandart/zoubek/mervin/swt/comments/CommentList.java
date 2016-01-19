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
package at.bitandart.zoubek.mervin.swt.comments;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import at.bitandart.zoubek.mervin.swt.comments.Comment.Alignment;

/**
 * A SWT Control that shows a list of grouped comments in columns.
 * 
 * @author Florian Zoubek
 *
 */
public class CommentList extends Composite {

	// Data
	private List<CommentColumn> baseColumns = new ArrayList<CommentColumn>(2);
	private LinkedHashMap<CommentGroup, InternalCommentGroup> groups = new LinkedHashMap<CommentGroup, CommentList.InternalCommentGroup>();

	// Utilities
	private FormToolkit toolkit;
	private DateFormat dateFormat = DateFormat.getDateTimeInstance();

	// SWT controls
	private Composite columnHeaderComposite;
	private Map<CommentColumn, Section> columnHeaders = new HashMap<>();

	// Listeners
	private List<CommentLinkListener> commentLinkListeners = new ArrayList<CommentList.CommentLinkListener>();

	public CommentList(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style);
		this.toolkit = toolkit;
		toolkit.adapt(this);

		setLayout(new TableWrapLayout());

		// create the composite that contains the column headers
		columnHeaderComposite = toolkit.createComposite(this);
		columnHeaderComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		TableWrapLayout headerLayout = new TableWrapLayout();
		headerLayout.makeColumnsEqualWidth = true;
		headerLayout.numColumns = baseColumns.size();
		headerLayout.leftMargin = 0;
		headerLayout.rightMargin = 0;
		columnHeaderComposite.setLayout(headerLayout);

	}

	/**
	 * adds the given comment column. Does nothing if the column already exists
	 * in this widget.
	 * 
	 * @param commentColumn
	 */
	public void addCommentColumn(CommentColumn commentColumn) {

		if (!baseColumns.contains(commentColumn)) {
			baseColumns.add(commentColumn);

			// create the section for the column header
			Section columnSection = toolkit.createSection(columnHeaderComposite, Section.TITLE_BAR);
			columnSection.setText(commentColumn.getTitle());
			columnSection.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
			columnHeaders.put(commentColumn, columnSection);

			// update the column header layout
			Layout headerLayout = columnHeaderComposite.getLayout();
			if (headerLayout instanceof TableWrapLayout) {
				/*
				 * replace the table layout as the internal caches do not
				 * support changing the number of columns during runtime
				 */
				TableWrapLayout headerTableLayout = new TableWrapLayout();
				headerTableLayout.rightMargin = 0;
				headerTableLayout.leftMargin = 0;
				headerTableLayout.numColumns = ((TableWrapLayout) headerLayout).numColumns + 1;
				columnHeaderComposite.setLayout(headerTableLayout);
			}
			columnHeaderComposite.layout();

			/*
			 * the internal columns are created for each group, so update also
			 * all existing groups
			 */
			for (InternalCommentGroup commentGroup : groups.values()) {

				InternalCommentColumn internalCommentColumn = commentGroup.getInternalColumnFor(commentColumn);
				if (internalCommentColumn == null) {

					Composite groupComposite = commentGroup.getComposite();

					// create the internal column
					internalCommentColumn = new InternalCommentColumn(commentColumn);
					createColumnControls(groupComposite, internalCommentColumn);
					commentGroup.addColumn(internalCommentColumn);

					// update the layout of the group composite
					Layout groupLayout = groupComposite.getLayout();
					if (groupLayout instanceof TableWrapLayout) {

						/*
						 * replace the table layout as the internal caches do
						 * not support changing the number of columns during
						 * runtime
						 */
						TableWrapLayout groupTableLayout = new TableWrapLayout();
						groupTableLayout.rightMargin = 0;
						groupTableLayout.leftMargin = 0;
						groupTableLayout.numColumns = ((TableWrapLayout) groupLayout).numColumns + 1;
						groupComposite.setLayout(groupTableLayout);
					}
					groupComposite.layout();

				}
			}
		}

	}

	/**
	 * removes the given column from this widget. Does nothing if the column is
	 * not present in this widget.
	 * 
	 * @param commentColumn
	 */
	public void removeCommentColumn(CommentColumn commentColumn) {

		if (!baseColumns.contains(commentColumn)) {
			baseColumns.remove(commentColumn);

			// update the column header layout
			Layout headerLayout = columnHeaderComposite.getLayout();
			if (headerLayout instanceof TableWrapLayout) {
				/*
				 * replace the table layout as the internal caches do not
				 * support changing the number of columns during runtime
				 */
				TableWrapLayout headerTableLayout = new TableWrapLayout();
				headerTableLayout.rightMargin = 0;
				headerTableLayout.leftMargin = 0;
				headerTableLayout.numColumns = ((TableWrapLayout) headerLayout).numColumns - 1;
				columnHeaderComposite.setLayout(headerTableLayout);
			}
			columnHeaderComposite.layout();
			columnHeaders.get(commentColumn).dispose();
			columnHeaders.remove(commentColumn);

			/*
			 * the internal columns are created for each group, so remove also
			 * the internal columns
			 */
			for (InternalCommentGroup group : groups.values()) {

				InternalCommentColumn internalCommentColumn = group.getInternalColumnFor(commentColumn);
				if (internalCommentColumn != null) {

					// remove the internal column and dispose its composite
					group.removeColumn(internalCommentColumn);
					internalCommentColumn.getComposite().dispose();

					// update the layout of the group composite
					Composite groupComposite = group.getComposite();
					Layout groupLayout = groupComposite.getLayout();
					if (groupLayout instanceof TableWrapLayout) {
						/*
						 * replace the table layout as the internal caches do
						 * not support changing the number of columns during
						 * runtime
						 */
						TableWrapLayout groupTableLayout = new TableWrapLayout();
						groupTableLayout.rightMargin = 0;
						groupTableLayout.leftMargin = 0;
						groupTableLayout.numColumns = ((TableWrapLayout) groupLayout).numColumns - 1;
						groupComposite.setLayout(groupTableLayout);
					}
					groupComposite.layout();

				}

			}

		}

	}

	/**
	 * adds a new comment group to the bottom of the list, does nothing if the
	 * group already exists.
	 * 
	 * @param commentGroup
	 */
	public void addCommentGroup(CommentGroup commentGroup) {

		if (!groups.containsKey(commentGroup)) {

			// create internal group and section
			InternalCommentGroup internalCommentGroup = new InternalCommentGroup(commentGroup);
			Section groupSection = toolkit.createSection(this, Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
			groupSection.setText(internalCommentGroup.getGroupTitle());
			groupSection.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
			internalCommentGroup.setSection(groupSection);

			// create the group composite
			Composite sectionClient = toolkit.createComposite(groupSection);
			TableWrapLayout clientLayout = new TableWrapLayout();
			clientLayout.makeColumnsEqualWidth = true;
			clientLayout.numColumns = baseColumns.size();
			clientLayout.leftMargin = 0;
			clientLayout.rightMargin = 0;
			sectionClient.setLayout(clientLayout);
			groupSection.setClient(sectionClient);
			internalCommentGroup.setComposite(sectionClient);

			// create internal columns for the group

			for (CommentColumn column : baseColumns) {

				InternalCommentColumn internalCommentColumn = new InternalCommentColumn(column);
				createColumnControls(sectionClient, internalCommentColumn);
				internalCommentGroup.addColumn(internalCommentColumn);
			}

			groups.put(commentGroup, internalCommentGroup);

			layout();

		}

	}

	/**
	 * creates the controls for the given {@link InternalCommentColumn} in the
	 * given {@link Composite}.
	 * 
	 * @param groupComposite
	 * @param internalCommentColumn
	 */
	private void createColumnControls(Composite groupComposite, InternalCommentColumn internalCommentColumn) {

		Composite columnComposite = toolkit.createComposite(groupComposite);
		columnComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		TableWrapLayout columnLayout = new TableWrapLayout();
		columnLayout.leftMargin = 0;
		columnLayout.rightMargin = 0;
		columnLayout.topMargin = 0;
		columnLayout.bottomMargin = 0;
		columnComposite.setLayout(columnLayout);
		internalCommentColumn.setComposite(columnComposite);

	}

	/**
	 * removes the given comment group from the widget, does nothing if the
	 * group is not contained in the widget.
	 * 
	 * @param commentGroup
	 *            the comment group to remove
	 */
	public void removeGroup(CommentGroup commentGroup) {

		InternalCommentGroup internalCommentGroup = groups.get(commentGroup);
		if (internalCommentGroup != null) {
			internalCommentGroup.getSection().dispose();
			groups.remove(commentGroup);
		}

	}

	/**
	 * @return an unmodifiable set containing all comment groups shown in this
	 *         widget.
	 */
	public Set<CommentGroup> getCommentGroups() {

		return Collections.unmodifiableSet(groups.keySet());

	}

	/**
	 * adds a new comment to the bottom of the specified comment group list,
	 * does nothing if the comment group or column is not contained in this
	 * widget or if the comment already exists.
	 * 
	 * @param commentGroup
	 *            the comment group where the comment should reside.
	 * @param column
	 *            the column of the comment.
	 * @param comment
	 *            the comment to add.
	 */
	public void addComment(CommentGroup commentGroup, CommentColumn column, Comment comment) {

		// find the corresponding internal group
		InternalCommentGroup internalCommentGroup = groups.get(commentGroup);
		if (internalCommentGroup != null) {

			// find the corresponding internal column
			InternalCommentColumn internalCommentColumn = internalCommentGroup.getInternalColumnFor(column);
			if (internalCommentColumn != null) {

				// check if there is a corresponding internal comment
				InternalComment internalComment = internalCommentColumn.getInternalCommentFor(comment);
				if (internalComment == null) {

					// no internal comment, so create one
					internalComment = new InternalComment(comment);
					internalCommentColumn.addComment(internalComment);

					Composite columnComposite = internalCommentColumn.getComposite();
					Composite alignmentComposite = toolkit.createComposite(columnComposite);
					alignmentComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
					TableWrapLayout alignmentLayout = new TableWrapLayout();
					alignmentLayout.bottomMargin = 0;
					alignmentLayout.topMargin = 0;
					if (internalComment.getAlignment() == Alignment.LEFT) {
						alignmentLayout.rightMargin = 30;
					} else {
						alignmentLayout.leftMargin = 30;
					}
					alignmentComposite.setLayout(alignmentLayout);

					// add the comment composite
					Composite commentComposite = toolkit.createComposite(alignmentComposite, SWT.BORDER);
					internalComment.setComposite(commentComposite);

					// set the layout for the comment elements
					TableWrapLayout commentLayout = new TableWrapLayout();
					commentLayout.numColumns = 2;
					commentComposite.setLayout(commentLayout);
					commentComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

					// create the author label
					Label authorLabel = toolkit.createLabel(commentComposite, comment.getAuthor());
					authorLabel.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

					// create the creation time label
					Label creationTimeLabel = toolkit.createLabel(commentComposite,
							dateFormat.format(comment.getCreationTime().getTime()));
					creationTimeLabel.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

					// create a StyledText for the body
					final StyledText commentBodyText = new StyledText(commentComposite, SWT.WRAP | SWT.READ_ONLY);
					toolkit.adapt(commentBodyText);
					commentBodyText.setText(comment.getBody());
					TableWrapData tableWrapData = new TableWrapData();
					tableWrapData.colspan = 2;
					commentBodyText.setLayoutData(tableWrapData);

					commentBodyText.addListener(SWT.MouseDown, new Listener() {
						@Override
						public void handleEvent(Event event) {
							try {
								int offset = commentBodyText.getOffsetAtLocation(new Point(event.x, event.y));
								StyleRange style = commentBodyText.getStyleRangeAtOffset(offset);
								if (style != null && style instanceof LinkStyleRange) {
									handleCommentLinkClicked(((LinkStyleRange) style).getCommentLink());
								}
							} catch (IllegalArgumentException e) {
								// no character under event.x, event.y
							}
						}

					});

					commentBodyText.addListener(SWT.MouseMove, new Listener() {

						private CommentLink currentCommentLink;

						@Override
						public void handleEvent(Event event) {
							try {
								int offset = commentBodyText.getOffsetAtLocation(new Point(event.x, event.y));
								StyleRange style = commentBodyText.getStyleRangeAtOffset(offset);
								if (style != null && style instanceof LinkStyleRange) {
									CommentLink commentLink = ((LinkStyleRange) style).getCommentLink();
									if (currentCommentLink != commentLink) {
										if (currentCommentLink != null) {
											handleCommentLinkExit(currentCommentLink);
										}
										handleCommentLinkEnter(commentLink);
										currentCommentLink = commentLink;
									}
								} else {
									if (currentCommentLink != null) {
										handleCommentLinkExit(currentCommentLink);
										currentCommentLink = null;
									}
								}
							} catch (IllegalArgumentException e) {
								// no character under event.x, event.y
							}
						}

					});

					// add links
					for (CommentLink link : internalComment.getCommentLinks()) {
						commentBodyText.setStyleRange(new LinkStyleRange(link));
					}

					layout();

				}
			}

		}

	}

	/**
	 * handles a click on a comment link and calls the corresponding listeners.
	 * 
	 * @param commentLink
	 *            the comment link that has been clicked
	 */
	private void handleCommentLinkClicked(CommentLink commentLink) {
		for (CommentLinkListener listener : commentLinkListeners) {
			listener.commentLinkClicked(commentLink);
		}
	}

	/**
	 * handles a mouse enter event on a comment link and calls the corresponding
	 * listeners.
	 * 
	 * @param commentLink
	 *            the comment link that has been clicked
	 */
	private void handleCommentLinkEnter(CommentLink commentLink) {
		for (CommentLinkListener listener : commentLinkListeners) {
			listener.commentLinkEnter(commentLink);
		}
	}

	/**
	 * handles a mouse exit event on a comment link and calls the corresponding
	 * listeners.
	 * 
	 * @param commentLink
	 *            the comment link that has been clicked
	 */
	private void handleCommentLinkExit(CommentLink commentLink) {
		for (CommentLinkListener listener : commentLinkListeners) {
			listener.commentLinkExit(commentLink);
		}
	}

	/**
	 * adds the given comment link listener to this widget.
	 * 
	 * @param listener
	 */
	public void addCommentLinkListener(CommentLinkListener listener) {
		commentLinkListeners.add(listener);
	}

	/**
	 * removes the given comment link listener from this widget
	 * 
	 * @param listener
	 */
	public void removeCommentLinkListener(CommentLinkListener listener) {
		commentLinkListeners.remove(listener);
	}

	/**
	 * The base listener interface for comment link events.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public static interface CommentLinkListener {

		/**
		 * called when the comment link has been clicked.
		 * 
		 * @param commentLink
		 */
		public void commentLinkClicked(CommentLink commentLink);

		/**
		 * called when the mouse enters the comment links bounds.
		 * 
		 * @param commentLink
		 */
		public void commentLinkEnter(CommentLink commentLink);

		/**
		 * called when the mouse leaves the comment links bounds.
		 * 
		 * @param commentLink
		 */
		public void commentLinkExit(CommentLink commentLink);
	}

	/**
	 * 
	 * removes the given comment from the widget, does nothing if the comment is
	 * not contained in the widget.
	 * 
	 * @param commentGroup
	 *            the group where the comment resides.
	 * @param column
	 *            the column where the comment resides.
	 * @param comment
	 *            the comment to remove.
	 */
	public void removeComment(CommentGroup commentGroup, CommentColumn column, Comment comment) {

		InternalCommentGroup internalCommentGroup = groups.get(commentGroup);
		if (internalCommentGroup != null) {

			InternalCommentColumn internalCommentColumn = internalCommentGroup.getInternalColumnFor(column);
			if (internalCommentColumn != null) {

				InternalComment internalComment = internalCommentColumn.getInternalCommentFor(comment);
				if (internalComment != null) {
					internalComment.getComposite().dispose();
					internalCommentColumn.removeComment(internalComment);
					layout();
				}
			}
		}
	}

	/**
	 * removes all data shown by this widget.
	 */
	public void clearData() {

		for (CommentGroup commentGroup : groups.keySet()) {
			removeGroup(commentGroup);
		}

		for (CommentColumn commentColumn : baseColumns) {
			removeCommentColumn(commentColumn);
		}

	}

	private class InternalCommentGroup implements CommentGroup {

		private CommentGroup realCommentGroup;

		private Composite composite;

		private Section section;

		/*
		 * there are usually only a small number (usually 2) of columns, so use
		 * a list instead of a LinkedHashMap to save memory
		 */
		private List<InternalCommentColumn> columns = new ArrayList<InternalCommentColumn>(2);

		/**
		 * @param realCommentGroup
		 */
		public InternalCommentGroup(CommentGroup realCommentGroup) {
			super();
			this.realCommentGroup = realCommentGroup;
		}

		@Override
		public String getGroupTitle() {
			return realCommentGroup.getGroupTitle();
		}

		public Composite getComposite() {
			return composite;
		}

		public void setComposite(Composite groupComposite) {
			this.composite = groupComposite;
		}

		public Section getSection() {
			return section;
		}

		public void setSection(Section section) {
			this.section = section;
		}

		public CommentGroup getRealCommentGroup() {
			return realCommentGroup;
		}

		public void addColumn(InternalCommentColumn internalCommentColumn) {
			columns.add(internalCommentColumn);
		}

		public void removeColumn(InternalCommentColumn internalCommentColumn) {
			columns.remove(internalCommentColumn);
		}

		public boolean isInternalCommentGroupFor(CommentGroup commentGroup) {
			return commentGroup == realCommentGroup;
		}

		public InternalCommentColumn getInternalColumnFor(CommentColumn commentColumn) {

			for (InternalCommentColumn column : columns) {
				if (column.isInternalCommentColumnFor(commentColumn)) {
					return column;
				}
			}

			return null;
		}
	}

	private class InternalCommentColumn implements CommentColumn {

		private CommentColumn realCommentColumn;

		private Composite composite;

		private LinkedHashMap<Comment, InternalComment> comments = new LinkedHashMap<Comment, InternalComment>();

		public InternalCommentColumn(CommentColumn realCommentColumn) {
			this.realCommentColumn = realCommentColumn;
		}

		@Override
		public String getTitle() {
			return realCommentColumn.getTitle();
		}

		public Composite getComposite() {
			return composite;
		}

		public void setComposite(Composite groupComposite) {
			this.composite = groupComposite;
		}

		public CommentColumn getRealCommentColumn() {
			return realCommentColumn;
		}

		public boolean isInternalCommentColumnFor(CommentColumn commentColumn) {
			return commentColumn == realCommentColumn;
		}

		public InternalComment getInternalCommentFor(Comment comment) {
			return comments.get(comment);
		}

		public void addComment(InternalComment internalComment) {
			comments.put(internalComment.getRealComment(), internalComment);
		}

		public void removeComment(InternalComment internalComment) {
			comments.remove(internalComment.getRealComment());
		}
	}

	private class InternalComment implements Comment {

		private Comment realComment;

		private Composite composite;

		public InternalComment(Comment realComment) {
			this.realComment = realComment;
		}

		@Override
		public String getAuthor() {
			return realComment.getAuthor();
		}

		@Override
		public Calendar getCreationTime() {
			return realComment.getCreationTime();
		}

		@Override
		public String getBody() {
			return realComment.getBody();
		}

		@Override
		public Alignment getAlignment() {
			return realComment.getAlignment();
		}

		@Override
		public List<CommentLink> getCommentLinks() {
			return realComment.getCommentLinks();
		}

		public Composite getComposite() {
			return composite;
		}

		public void setComposite(Composite groupComposite) {
			this.composite = groupComposite;
		}

		public Comment getRealComment() {
			return realComment;
		}

		public boolean isInternalCommentFor(Comment comment) {
			return comment == realComment;
		}
	}

}

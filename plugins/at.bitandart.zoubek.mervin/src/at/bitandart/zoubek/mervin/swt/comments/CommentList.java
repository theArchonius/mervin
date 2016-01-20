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

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.bitandart.zoubek.mervin.swt.comments.data.IComment;
import at.bitandart.zoubek.mervin.swt.comments.data.IComment.Alignment;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentGroup;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;

/**
 * A SWT Control that shows a list of grouped comments in columns.
 * 
 * @author Florian Zoubek
 *
 */
public class CommentList extends Composite {

	// Data
	private List<ICommentColumn> baseColumns = new ArrayList<ICommentColumn>(2);
	private LinkedHashMap<ICommentGroup, InternalCommentGroup> groups = new LinkedHashMap<ICommentGroup, CommentList.InternalCommentGroup>();

	// Utilities
	private FormToolkit toolkit;
	private DateFormat dateFormat = DateFormat.getDateTimeInstance();

	// SWT controls
	private Composite columnHeaderComposite;
	private Map<ICommentColumn, Control> columnHeaders = new HashMap<>();

	// Listeners
	private List<CommentLinkListener> commentLinkListeners = new ArrayList<CommentList.CommentLinkListener>();

	private Font titleFont;
	private Color titleForeground;
	private Color titleBackground;

	public CommentList(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style);
		this.toolkit = toolkit;
		toolkit.adapt(this);

		setLayout(new GridLayout());

		// create the composite that contains the column headers
		columnHeaderComposite = toolkit.createComposite(this);
		columnHeaderComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		columnHeaderComposite.setLayout(new GridLayout());

		titleFont = FontDescriptor.createFrom(getFont()).setStyle(SWT.BOLD).increaseHeight(1)
				.createFont(Display.getDefault());

		addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				titleFont.dispose();
			}
		});

		// the title colors are system colors, so do not dispose them later
		titleForeground = getDisplay().getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
		titleBackground = getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
	}

	/**
	 * adds the given comment column. Does nothing if the column already exists
	 * in this widget.
	 * 
	 * @param commentColumn
	 */
	public void addCommentColumn(ICommentColumn commentColumn) {

		if (!baseColumns.contains(commentColumn)) {
			baseColumns.add(commentColumn);

			// create the section for the column header
			Label mainColumnLabel = toolkit.createLabel(columnHeaderComposite, commentColumn.getTitle(), SWT.CENTER);
			mainColumnLabel.setBackground(titleBackground);
			mainColumnLabel.setForeground(titleForeground);
			mainColumnLabel.setFont(titleFont);
			mainColumnLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			columnHeaders.put(commentColumn, mainColumnLabel);
			// update the column header layout
			GridLayout gridLayout = new GridLayout(baseColumns.size(), true);
			gridLayout.marginWidth = 0;
			columnHeaderComposite.setLayout(gridLayout);
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

					groupComposite.setLayout(new GridLayout(baseColumns.size(), true));
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
	public void removeCommentColumn(ICommentColumn commentColumn) {

		if (!baseColumns.contains(commentColumn)) {
			baseColumns.remove(commentColumn);

			// update the column header layout
			if (!baseColumns.isEmpty()) {
				columnHeaderComposite.setLayout(new GridLayout(baseColumns.size(), true));
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
					Control[] children = group.getComposite().getChildren();
					// find the separator and dispose it
					Composite columnComposite = internalCommentColumn.getComposite();
					for (int i = 0; i < children.length; i++) {
						if (children[i] == columnComposite && i > 0) {
							children[i - 1].dispose();
						}
					}
					// dispose the column composite
					columnComposite.dispose();

					// update the layout of the group composite
					Composite groupComposite = group.getComposite();
					groupComposite.setLayout(new GridLayout(baseColumns.size(), true));
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
	public void addCommentGroup(ICommentGroup commentGroup) {

		if (!groups.containsKey(commentGroup)) {

			// create internal group and section
			InternalCommentGroup internalCommentGroup = new InternalCommentGroup(commentGroup);

			// create the group title
			Label groupTitle = toolkit.createLabel(this, internalCommentGroup.getGroupTitle(), SWT.CENTER);
			groupTitle.setBackground(titleBackground);
			groupTitle.setForeground(titleForeground);
			groupTitle.setFont(titleFont);
			groupTitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			internalCommentGroup.setGroupTitleControl(groupTitle);

			// create the group composite
			Composite groupComposite = toolkit.createComposite(this);
			groupComposite.setLayout(createColumnLayout());
			groupComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			internalCommentGroup.setComposite(groupComposite);

			// create internal columns for the group

			for (ICommentColumn column : baseColumns) {

				InternalCommentColumn internalCommentColumn = new InternalCommentColumn(column);
				createColumnControls(groupComposite, internalCommentColumn);
				internalCommentGroup.addColumn(internalCommentColumn);

			}

			groups.put(commentGroup, internalCommentGroup);

			layout();

		}

	}

	/**
	 * @return the layout that should be used for the group composite that
	 *         contains the comment columns.
	 */
	private Layout createColumnLayout() {
		GridLayout gridLayout = new GridLayout(baseColumns.size() * 2 - 1, false);
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 2;
		return gridLayout;
	}

	/**
	 * creates the controls for the given {@link InternalCommentColumn} in the
	 * given {@link Composite}.
	 * 
	 * @param groupComposite
	 * @param internalCommentColumn
	 */
	private void createColumnControls(Composite groupComposite, InternalCommentColumn internalCommentColumn) {

		if (groupComposite.getChildren().length > 0) {
			Label label = toolkit.createLabel(groupComposite, "", SWT.SEPARATOR | SWT.VERTICAL);
			label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		}

		Composite columnComposite = toolkit.createComposite(groupComposite);

		columnComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginWidth = 0;
		columnComposite.setLayout(gridLayout);
		internalCommentColumn.setComposite(columnComposite);

	}

	/**
	 * removes the given comment group from the widget, does nothing if the
	 * group is not contained in the widget.
	 * 
	 * @param commentGroup
	 *            the comment group to remove
	 */
	public void removeGroup(ICommentGroup commentGroup) {

		InternalCommentGroup internalCommentGroup = groups.get(commentGroup);
		if (internalCommentGroup != null) {
			internalCommentGroup.getComposite().dispose();
			internalCommentGroup.getGroupTitleControl().dispose();
			groups.remove(commentGroup);
		}

	}

	/**
	 * @return an unmodifiable set containing all comment groups shown in this
	 *         widget.
	 */
	public Set<ICommentGroup> getCommentGroups() {

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
	public void addComment(ICommentGroup commentGroup, ICommentColumn column, IComment comment) {

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

					int metaDataAlignment = SWT.LEFT;

					if (internalComment.getAlignment() != Alignment.LEFT) {
						// add a filler composite
						addCommentFillerComposite(internalComment, columnComposite);
						metaDataAlignment = SWT.RIGHT;
					}

					// add the comment composite
					Composite commentComposite = toolkit.createComposite(columnComposite, SWT.BORDER);
					internalComment.setComposite(commentComposite);

					// set the layout for the comment elements
					commentComposite.setLayout(new GridLayout(1, false));
					commentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

					if (internalComment.getAlignment() == Alignment.LEFT) {
						// add a filler composite
						addCommentFillerComposite(internalComment, columnComposite);
					}

					// create the author label
					Label authorLabel = toolkit.createLabel(commentComposite, comment.getAuthor());
					authorLabel.setLayoutData(new GridData(metaDataAlignment, SWT.CENTER, true, false));
					authorLabel.setForeground(titleBackground);
					authorLabel.setBackground(titleForeground);

					// create the creation time label
					Label creationTimeLabel = toolkit.createLabel(commentComposite,
							dateFormat.format(comment.getCreationTime().getTime()));
					creationTimeLabel.setLayoutData(new GridData(metaDataAlignment, SWT.CENTER, true, false));
					creationTimeLabel.setForeground(titleBackground);
					creationTimeLabel.setBackground(titleForeground);

					// create a StyledText for the body
					final StyledText commentBodyText = new StyledText(commentComposite,
							SWT.WRAP | SWT.READ_ONLY | metaDataAlignment | SWT.MULTI);
					toolkit.adapt(commentBodyText);
					commentBodyText.setText(comment.getBody());
					commentBodyText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

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

						private ICommentLink currentCommentLink;

						@Override
						public void handleEvent(Event event) {
							try {
								int offset = commentBodyText.getOffsetAtLocation(new Point(event.x, event.y));
								StyleRange style = commentBodyText.getStyleRangeAtOffset(offset);
								if (style != null && style instanceof LinkStyleRange) {
									ICommentLink commentLink = ((LinkStyleRange) style).getCommentLink();
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
					commentBodyText.redraw();

					// add links
					for (ICommentLink link : internalComment.getCommentLinks()) {
						commentBodyText.setStyleRange(new LinkStyleRange(link));
					}
					layout(true, true);

				}
			}

		}

	}

	/**
	 * adds a filler composite used to create a blank space of at least 5 pixels
	 * for a given internal comment.
	 * 
	 * @param internalComment
	 * @param columnComposite
	 */
	private void addCommentFillerComposite(InternalComment internalComment, Composite columnComposite) {
		Composite filler = toolkit.createComposite(columnComposite, SWT.NONE);
		GridData fillerData = new GridData(SWT.FILL, SWT.FILL, false, false);
		fillerData.minimumWidth = 5;
		fillerData.widthHint = 20;
		filler.setLayoutData(fillerData);
		internalComment.setCommentFiller(filler);
	}

	/**
	 * handles a click on a comment link and calls the corresponding listeners.
	 * 
	 * @param commentLink
	 *            the comment link that has been clicked
	 */
	private void handleCommentLinkClicked(ICommentLink commentLink) {
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
	private void handleCommentLinkEnter(ICommentLink commentLink) {
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
	private void handleCommentLinkExit(ICommentLink commentLink) {
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
		public void commentLinkClicked(ICommentLink commentLink);

		/**
		 * called when the mouse enters the comment links bounds.
		 * 
		 * @param commentLink
		 */
		public void commentLinkEnter(ICommentLink commentLink);

		/**
		 * called when the mouse leaves the comment links bounds.
		 * 
		 * @param commentLink
		 */
		public void commentLinkExit(ICommentLink commentLink);
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
	public void removeComment(ICommentGroup commentGroup, ICommentColumn column, IComment comment) {

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

		for (ICommentGroup commentGroup : groups.keySet()) {
			removeGroup(commentGroup);
		}

		for (ICommentColumn commentColumn : baseColumns) {
			removeCommentColumn(commentColumn);
		}

	}

	private class InternalCommentGroup implements ICommentGroup {

		private ICommentGroup realCommentGroup;

		private Composite composite;

		private Control groupTitleControl;

		/*
		 * there are usually only a small number (usually 2) of columns, so use
		 * a list instead of a LinkedHashMap to save memory
		 */
		private List<InternalCommentColumn> columns = new ArrayList<InternalCommentColumn>(2);

		/**
		 * @param realCommentGroup
		 */
		public InternalCommentGroup(ICommentGroup realCommentGroup) {
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

		public Control getGroupTitleControl() {
			return groupTitleControl;
		}

		public void setGroupTitleControl(Control groupTitleControl) {
			this.groupTitleControl = groupTitleControl;
		}

		public ICommentGroup getRealCommentGroup() {
			return realCommentGroup;
		}

		public void addColumn(InternalCommentColumn internalCommentColumn) {
			columns.add(internalCommentColumn);
		}

		public void removeColumn(InternalCommentColumn internalCommentColumn) {
			columns.remove(internalCommentColumn);
		}

		public boolean isInternalCommentGroupFor(ICommentGroup commentGroup) {
			return commentGroup == realCommentGroup;
		}

		public InternalCommentColumn getInternalColumnFor(ICommentColumn commentColumn) {

			for (InternalCommentColumn column : columns) {
				if (column.isInternalCommentColumnFor(commentColumn)) {
					return column;
				}
			}

			return null;
		}
	}

	private class InternalCommentColumn implements ICommentColumn {

		private ICommentColumn realCommentColumn;

		private Composite composite;

		private LinkedHashMap<IComment, InternalComment> comments = new LinkedHashMap<IComment, InternalComment>();

		public InternalCommentColumn(ICommentColumn realCommentColumn) {
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

		public ICommentColumn getRealCommentColumn() {
			return realCommentColumn;
		}

		public boolean isInternalCommentColumnFor(ICommentColumn commentColumn) {
			return commentColumn == realCommentColumn;
		}

		public InternalComment getInternalCommentFor(IComment comment) {
			return comments.get(comment);
		}

		public void addComment(InternalComment internalComment) {
			comments.put(internalComment.getRealComment(), internalComment);
		}

		public void removeComment(InternalComment internalComment) {
			comments.remove(internalComment.getRealComment());
		}
	}

	private class InternalComment implements IComment {

		private IComment realComment;

		private Composite composite;

		private Composite commentFiller;

		public InternalComment(IComment realComment) {
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
		public List<ICommentLink> getCommentLinks() {
			return realComment.getCommentLinks();
		}

		public Composite getComposite() {
			return composite;
		}

		public void setComposite(Composite groupComposite) {
			this.composite = groupComposite;
		}

		public Composite getCommentFiller() {
			return commentFiller;
		}

		public void setCommentFiller(Composite commentFiller) {
			this.commentFiller = commentFiller;
		}

		public IComment getRealComment() {
			return realComment;
		}

		public boolean isInternalCommentFor(IComment comment) {
			return comment == realComment;
		}
	}

}

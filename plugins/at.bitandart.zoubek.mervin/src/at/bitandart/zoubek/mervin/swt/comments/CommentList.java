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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.bitandart.zoubek.mervin.swt.comments.CommentEditor.CommentEditorListener;
import at.bitandart.zoubek.mervin.swt.comments.data.IComment;
import at.bitandart.zoubek.mervin.swt.comments.data.IComment.Alignment;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentGroup;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLinkTarget;

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
	private List<CommentEditor> activeEditors = new LinkedList<CommentEditor>();
	private ICommentLinkTarget currentLinkTarget;

	// Utilities
	private FormToolkit toolkit;
	private DateFormat dateFormat = DateFormat.getDateTimeInstance();

	// SWT controls
	private Composite columnHeaderArea;
	private Map<ICommentColumn, Control> columnHeaders = new HashMap<>();
	private Map<ICommentColumn, CommentEditor> columnHeaderEditors = new HashMap<>();

	// Listeners
	private List<CommentLinkListener> commentLinkListeners = new ArrayList<CommentList.CommentLinkListener>();
	private List<CommentModifyListener> commentModifyListeners = new ArrayList<CommentList.CommentModifyListener>();

	private Font titleFont;
	private Color titleForeground;
	private Color titleBackground;

	public CommentList(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style);
		this.toolkit = toolkit;
		toolkit.adapt(this);

		setLayout(new GridLayout());

		// create the composite that contains the column headers
		columnHeaderArea = toolkit.createComposite(this);
		columnHeaderArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		columnHeaderArea.setLayout(new GridLayout());

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

	@Override
	public void setLayout(Layout layout) {
		/*
		 * disallow changing the layout once it has been set
		 */
		if (getLayout() == null) {
			super.setLayout(layout);
		}
	}

	public void setCurrentLinkTarget(ICommentLinkTarget linkTarget) {
		this.currentLinkTarget = linkTarget;
		for (CommentEditor editor : activeEditors) {
			editor.setCurrentLinkTarget(currentLinkTarget);
		}
	}

	/**
	 * adds the given comment column. Does nothing if the column already exists
	 * in this widget.
	 * 
	 * @param commentColumn
	 */
	public void addCommentColumn(final ICommentColumn commentColumn) {

		if (!baseColumns.contains(commentColumn)) {
			baseColumns.add(commentColumn);

			// create the section for the column header
			final Composite columnHeaderContainer = toolkit.createComposite(columnHeaderArea);
			columnHeaderContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			GridLayout headerContainerLayout = new GridLayout();
			headerContainerLayout.marginWidth = 0;
			columnHeaderContainer.setLayout(headerContainerLayout);

			Label columnHeaderLabel = toolkit.createLabel(columnHeaderContainer, commentColumn.getTitle(), SWT.CENTER);
			columnHeaderLabel.setBackground(titleBackground);
			columnHeaderLabel.setForeground(titleForeground);
			columnHeaderLabel.setFont(titleFont);
			columnHeaderLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			final Button addCommentButton = toolkit.createButton(columnHeaderContainer, "add Comment", SWT.PUSH);
			addCommentButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

			final CommentEditor addCommentEditor = new CommentEditor(columnHeaderContainer, SWT.NONE, toolkit);
			addCommentEditor.setVisible(false);
			GridData commentEditorGD = new GridData(SWT.FILL, SWT.FILL, true, false);
			commentEditorGD.exclude = true;
			addCommentEditor.setLayoutData(commentEditorGD);
			addCommentEditor.setCurrentLinkTarget(currentLinkTarget);
			/*
			 * note that the comment editors to add a comment to a specific
			 * column are always active
			 */
			activeEditors.add(addCommentEditor);
			columnHeaderEditors.put(commentColumn, addCommentEditor);

			/*
			 * comment save handling
			 */
			addCommentEditor.addCommentEditorListener(new CommentEditorListener() {

				@Override
				public void doSave(CommentEditor commentEditor) {

					addCommentButton.setVisible(true);
					((GridData) addCommentButton.getLayoutData()).exclude = false;
					addCommentEditor.setVisible(false);
					((GridData) addCommentEditor.getLayoutData()).exclude = true;
					layout(true, true);

					notifyAddComment(commentEditor.getText(), commentEditor.getCommentLinks(),
							new InternalCommentColumn(commentColumn), null);
				}

			});

			/*
			 * hide button and show comment editor if the button has been
			 * clicked
			 */
			addCommentButton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					addCommentButton.setVisible(false);
					((GridData) addCommentButton.getLayoutData()).exclude = true;
					addCommentEditor.setVisible(true);
					((GridData) addCommentEditor.getLayoutData()).exclude = false;
					layout(true, true);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// intentionally left empty
				}
			});

			columnHeaders.put(commentColumn, columnHeaderContainer);
			// update the column header layout
			GridLayout gridLayout = new GridLayout(baseColumns.size(), true);
			gridLayout.marginWidth = 0;
			columnHeaderArea.setLayout(gridLayout);
			columnHeaderArea.layout();

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
	 * notifies all registered {@link CommentModifyListener}s that a comment has
	 * been added.
	 * 
	 * @param text
	 *            the raw text of the comment.
	 * @param commentLinks
	 *            the list of links in the comment.
	 * @param answeredComment
	 *            the comment that has been replied to, null if the comment was
	 *            not a reply to any comment.
	 */
	private void notifyAddComment(String text, List<ICommentLink> commentLinks,
			InternalCommentColumn internalCommentColumn, InternalComment answeredComment) {

		for (CommentModifyListener commentModifyListener : commentModifyListeners) {
			commentModifyListener.commentAdded(text, commentLinks, internalCommentColumn.getRealCommentColumn(),
					(answeredComment != null) ? answeredComment.getRealComment() : null);
		}

	}

	/**
	 * removes the given column from this widget. Does nothing if the column is
	 * not present in this widget.
	 * 
	 * @param commentColumn
	 */
	public void removeCommentColumn(ICommentColumn commentColumn) {

		if (baseColumns.contains(commentColumn)) {
			baseColumns.remove(commentColumn);

			// update the column header layout
			if (!baseColumns.isEmpty()) {
				columnHeaderArea.setLayout(new GridLayout(baseColumns.size(), true));
			}
			columnHeaderArea.layout();
			columnHeaders.get(commentColumn).dispose();
			columnHeaders.remove(commentColumn);
			CommentEditor commentEditor = columnHeaderEditors.remove(commentColumn);
			activeEditors.remove(commentEditor);

			/*
			 * the internal columns are created for each group, so remove also
			 * the internal columns
			 */
			for (InternalCommentGroup group : groups.values()) {

				InternalCommentColumn internalCommentColumn = group.getInternalColumnFor(commentColumn);
				if (internalCommentColumn != null) {

					// remove the internal column and dispose its composite
					group.removeColumn(internalCommentColumn);
					Composite columnComposite = internalCommentColumn.getComposite();
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
		GridLayout gridLayout = new GridLayout(2, true);
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
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

		Composite columnComposite = toolkit.createComposite(groupComposite, SWT.BORDER);

		columnComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginWidth = 5;
		gridLayout.horizontalSpacing = 0;
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
			final InternalCommentColumn internalCommentColumn = internalCommentGroup.getInternalColumnFor(column);
			if (internalCommentColumn != null) {

				// check if there is a corresponding internal comment
				if (internalCommentColumn.getInternalCommentFor(comment) == null) {

					// no internal comment, so create one
					final InternalComment internalComment = new InternalComment(comment);
					internalCommentColumn.addComment(internalComment);

					Composite columnComposite = internalCommentColumn.getComposite();

					int metaDataAlignment = SWT.RIGHT;

					if (internalComment.getAlignment() == Alignment.LEFT) {
						// add the avatar
						addCommentAvatar(internalComment, columnComposite, SWT.LEFT);
						metaDataAlignment = SWT.LEFT;
					}

					// add the comment composite
					final Composite commentComposite = toolkit.createComposite(columnComposite, SWT.BORDER);
					internalComment.setComposite(commentComposite);

					// set the layout for the comment elements
					commentComposite.setLayout(new GridLayout(1, false));
					commentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

					if (internalComment.getAlignment() != Alignment.LEFT) {
						// add the avatar
						addCommentAvatar(internalComment, columnComposite, SWT.RIGHT);
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
					final CommentLineStyleListener commentLineStyleListener = new CommentLineStyleListener();
					commentBodyText.addLineStyleListener(commentLineStyleListener);
					commentLineStyleListener.setCommentLinks(internalComment.getCommentLinks());
					commentBodyText.setText(comment.getBody());
					commentBodyText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

					commentBodyText.addListener(SWT.MouseDown, new Listener() {
						@Override
						public void handleEvent(Event event) {
							try {
								int offset = commentBodyText.getOffsetAtLocation(new Point(event.x, event.y));
								StyleRange style = commentBodyText.getStyleRangeAtOffset(offset);
								if (style != null && style instanceof LinkStyleRange) {
									notifyCommentLinkClicked(((LinkStyleRange) style).getCommentLink());
								}
							} catch (IllegalArgumentException e) {
								// no character under event.x, event.y
							}
						}

					});
					CommentBodyMouseListener commentBodyMouseListener = new CommentBodyMouseListener(
							commentLineStyleListener, commentBodyText);
					commentBodyText.addMouseTrackListener(commentBodyMouseListener);
					commentBodyText.addMouseMoveListener(commentBodyMouseListener);

					// add reply button

					final Button replyButton = toolkit.createButton(commentComposite, "Reply", SWT.PUSH);
					replyButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));

					replyButton.addSelectionListener(new SelectionListener() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							replyButton.setVisible(false);
							((GridData) replyButton.getLayoutData()).exclude = true;
							createReplyCommentEditor(internalComment, internalCommentColumn, commentComposite,
									replyButton);
							layout(true, true);
						}

						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
							// Intentionally left empty
						}
					});

					layout(true, true);

				}
			}

		}

	}

	/**
	 * adds an avatar for the given internal comment to the column composite.
	 * 
	 * @param internalComment
	 * @param columnComposite
	 * @param alignment
	 *            either {@link SWT#LEFT}, {@link SWT#RIGHT} or {@link SWT#NONE}
	 */
	private void addCommentAvatar(InternalComment internalComment, Composite columnComposite, int alignment) {
		CommentAvatar avatar = new CommentAvatar(columnComposite, alignment);
		toolkit.adapt(avatar);
		GridData avatarData = new GridData(SWT.FILL, SWT.FILL, false, false);
		avatar.setLayoutData(avatarData);
		internalComment.setCommentFiller(avatar);
	}

	private void createReplyCommentEditor(final InternalComment internalComment,
			final InternalCommentColumn internalCommentColumn, Composite commentComposite, final Button replyButton) {
		final CommentEditor addCommentEditor = new CommentEditor(commentComposite, SWT.NONE, toolkit);
		addCommentEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		/*
		 * comment save handling
		 */
		addCommentEditor.addCommentEditorListener(new CommentEditorListener() {

			@Override
			public void doSave(CommentEditor commentEditor) {

				replyButton.setVisible(true);
				((GridData) replyButton.getLayoutData()).exclude = false;
				notifyAddComment(commentEditor.getText(), commentEditor.getCommentLinks(), internalCommentColumn,
						internalComment);
				addCommentEditor.dispose();
				layout(true, true);

			}

		});
		addCommentEditor.setCurrentLinkTarget(currentLinkTarget);
		activeEditors.add(addCommentEditor);
	}

	/**
	 * handles a click on a comment link and calls the corresponding listeners.
	 * 
	 * @param commentLink
	 *            the comment link that has been clicked
	 */
	private void notifyCommentLinkClicked(ICommentLink commentLink) {
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
	private void notifyCommentLinkEnter(ICommentLink commentLink) {
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
	private void notifyCommentLinkExit(ICommentLink commentLink) {
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
	 * A {@link MouseTrackListener} and {@link MouseMoveListener} that generates
	 * the mouse movement events for comment links of a given comment body.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private final class CommentBodyMouseListener implements MouseTrackListener, MouseMoveListener {
		private final CommentLineStyleListener commentLineStyleListener;
		private final StyledText commentBodyText;
		private ICommentLink currentCommentLink;

		/**
		 * @param commentLineStyleListener
		 *            the {@link CommentLineStyleListener} associated with the
		 *            given comment body text widget.
		 * @param commentBodyText
		 *            the comment body text widget to listen for.
		 */
		private CommentBodyMouseListener(CommentLineStyleListener commentLineStyleListener,
				StyledText commentBodyText) {
			this.commentLineStyleListener = commentLineStyleListener;
			this.commentBodyText = commentBodyText;
		}

		@Override
		public void mouseHover(MouseEvent event) {
			// intentionally left empty
		}

		@Override
		public void mouseExit(MouseEvent event) {
			if (currentCommentLink != null) {
				notifyCommentLinkExit(currentCommentLink);
				currentCommentLink = null;
			}
		}

		@Override
		public void mouseEnter(MouseEvent event) {
			handleMouseMove(event);
		}

		@Override
		public void mouseMove(MouseEvent event) {
			handleMouseMove(event);
		}

		private void handleMouseMove(MouseEvent event) {
			try {
				int offset = commentBodyText.getOffsetAtLocation(new Point(event.x, event.y));
				ICommentLink commentLink = commentLineStyleListener.getCommentLinkAtLocation(offset);
				if (commentLink != null) {
					if (currentCommentLink != commentLink) {
						if (currentCommentLink != null) {
							notifyCommentLinkExit(currentCommentLink);
						}
						notifyCommentLinkEnter(commentLink);
						currentCommentLink = commentLink;
					}
				} else {
					if (currentCommentLink != null) {
						notifyCommentLinkExit(currentCommentLink);
						currentCommentLink = null;
					}
				}
			} catch (IllegalArgumentException e) {
				// no character under event.x, event.y
				if (currentCommentLink != null) {
					notifyCommentLinkExit(currentCommentLink);
					currentCommentLink = null;
				}
			}
		}

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
	 * adds the given comment modify listener from this widget.
	 * 
	 * @param commentModifyListener
	 */
	public void addCommentModifyListener(CommentModifyListener commentModifyListener) {
		commentModifyListeners.add(commentModifyListener);
	}

	/**
	 * removes the given comment modify listener from this widget.
	 * 
	 * @param commentModifyListener
	 */
	public void removeCommentModifyListener(CommentModifyListener commentModifyListener) {
		commentModifyListeners.remove(commentModifyListener);
	}

	/**
	 * The base listener interface for comment modification events.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public static interface CommentModifyListener {

		/**
		 * called when a new comment has been added.
		 * 
		 * @param text
		 *            the raw text of the comment.
		 * @param commentLinks
		 *            the comment links contained in the raw text.
		 * @param commentColumn
		 *            the comment column in which the comment has been added.
		 * @param answerdComment
		 *            the comment that has been replied to with this comment,
		 *            null if the comment is not a reply.
		 */
		public void commentAdded(String text, List<ICommentLink> commentLinks, ICommentColumn commentColumn,
				IComment answerdComment);

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

		Set<ICommentGroup> groupsSet = new HashSet<>(groups.keySet());
		for (ICommentGroup commentGroup : groupsSet) {
			removeGroup(commentGroup);
		}

		List<ICommentColumn> columnList = new ArrayList<>(baseColumns);
		for (ICommentColumn commentColumn : columnList) {
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

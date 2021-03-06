/*******************************************************************************
 * Copyright (c) 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.swt.comments;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.bitandart.zoubek.mervin.swt.comments.data.IComment;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentGroup;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentProvider;

/**
 * A {@link Viewer} that manages the content of a {@link CommentList} widget.
 * 
 * @author Florian Zoubek
 *
 */
public class CommentListViewer extends Viewer {

	private CommentList commentListControl;
	private ICommentProvider commentProvider;

	private Object input;

	public CommentListViewer(Composite parent, FormToolkit toolkit, int style) {
		commentListControl = new CommentList(parent, style, toolkit);
	}

	@Override
	public Control getControl() {
		return commentListControl;
	}

	@Override
	public Object getInput() {
		return input;
	}

	@Override
	public ISelection getSelection() {
		// This viewer does not support selection (yet)
		return StructuredSelection.EMPTY;
	}

	@Override
	public void refresh() {

		if (commentProvider != null) {

			commentListControl.setRedraw(false);
			commentListControl.setLayoutDeferred(true);
			commentListControl.clearData();

			List<ICommentColumn> allCommentColumns = commentProvider.getAllCommentColumns(input);
			commentListControl.setOverviewColumns(allCommentColumns);

			List<ICommentColumn> commentColumns = commentProvider.getVisibleCommentColumns(input);

			for (ICommentColumn commentColumn : commentColumns) {
				commentListControl.addCommentColumn(commentColumn);
			}
			if (!commentColumns.isEmpty()) {
				List<ICommentGroup> commentGroups = commentProvider.getCommentGroups(input);
				for (ICommentGroup commentGroup : commentGroups) {

					commentListControl.addCommentGroup(commentGroup);
					for (ICommentColumn commentColumn : commentColumns) {
						List<IComment> comments = commentProvider.getComments(input, commentGroup, commentColumn);
						for (IComment comment : comments) {
							commentListControl.addComment(commentGroup, commentColumn, comment);
						}
					}
				}
			}
			commentListControl.setRedraw(true);
			commentListControl.setLayoutDeferred(false);

			commentListControl.layout(true, true);
		}

	}

	@Override
	public void setInput(Object input) {
		this.input = input;
	}

	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		// This viewer does not support selection (yet)
	}

	/**
	 * @return the comment list control used by this viewer
	 */
	public CommentList getCommentListControl() {
		return commentListControl;
	}

	/**
	 * sets the provider that is used to update the controls of this viewer.
	 * 
	 * @param commentProvider
	 *            the provider to use.
	 */
	public void setCommentProvider(ICommentProvider commentProvider) {
		this.commentProvider = commentProvider;
	}

}

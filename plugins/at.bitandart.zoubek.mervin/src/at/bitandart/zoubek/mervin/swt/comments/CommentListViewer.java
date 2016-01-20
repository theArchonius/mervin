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

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.bitandart.zoubek.mervin.swt.comments.data.Comment;
import at.bitandart.zoubek.mervin.swt.comments.data.CommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.data.CommentGroup;
import at.bitandart.zoubek.mervin.swt.comments.data.CommentProvider;

/**
 * A {@link Viewer} that manages the content of a {@link CommentList} widget.
 * 
 * @author Florian Zoubek
 *
 */
public class CommentListViewer extends Viewer {

	private CommentList commentListControl;
	private CommentProvider commentProvider;

	private Object input;

	public CommentListViewer(Composite parent, FormToolkit toolkit) {
		commentListControl = new CommentList(parent, SWT.NONE, toolkit);
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

			commentListControl.clearData();

			List<CommentColumn> commentColumns = commentProvider.getCommentColumns(input);

			for (CommentColumn commentColumn : commentColumns) {
				commentListControl.addCommentColumn(commentColumn);
			}

			List<CommentGroup> commentGroups = commentProvider.getCommentGroups(input);
			for (CommentGroup commentGroup : commentGroups) {

				commentListControl.addCommentGroup(commentGroup);
				for (CommentColumn commentColumn : commentColumns) {
					List<Comment> comments = commentProvider.getComments(commentGroup, commentColumn);
					for (Comment comment : comments) {
						commentListControl.addComment(commentGroup, commentColumn, comment);
					}
				}
			}
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
	public void setCommentProvider(CommentProvider commentProvider) {
		this.commentProvider = commentProvider;
	}

}

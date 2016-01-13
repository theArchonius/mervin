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

/**
 * Base interface for classes that provide comments and comment groups for
 * comment widgets like {@link CommentListViewer}.
 * 
 * @author Florian Zoubek
 *
 */
public interface CommentProvider {

	/**
	 * returns the list of top-level comment groups
	 * 
	 * @return an ordered list of comment groups containing the actual comments.
	 *         Comments must always reside in a group and a column.
	 */
	public List<CommentGroup> getCommentGroups(Object input);

	/**
	 * returns the list of comment columns
	 * 
	 * @return an ordered list of comment columns containing the actual
	 *         comments. Comments must always reside in a group and a column.
	 */
	public List<CommentColumn> getCommentColumns(Object input);

	/**
	 * returns the comments for a given group.
	 * 
	 * @param group
	 *            the group to retrieve the comments for.
	 * @param column
	 *            the column to retrieve the comments for.
	 * @return the comments of the group in the given column
	 */
	public List<Comment> getComments(CommentGroup group, CommentColumn column);

}

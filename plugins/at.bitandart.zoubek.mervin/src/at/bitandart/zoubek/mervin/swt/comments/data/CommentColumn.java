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
package at.bitandart.zoubek.mervin.swt.comments.data;

/**
 * A simple {@link ICommentColumn} implementation which stores the values in
 * fields and that provides additional assignment based setters without any
 * further logic.
 * 
 * @author Florian Zoubek
 *
 */
public class CommentColumn implements ICommentColumn {

	private String title;

	/**
	 * @see ICommentColumn#getTitle()
	 */
	public CommentColumn(String title) {
		this.title = title;
	}

	/**
	 * @see ICommentColumn#getTitle()
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CommentColumn [title=" + title + "]";
	}

}

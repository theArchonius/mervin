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
 * A simple {@link ICommentGroup} implementation which stores the values in
 * fields and that provides additional assignment based setters without any
 * further logic.
 * 
 * @author Florian Zoubek
 *
 */
public class CommentGroup implements ICommentGroup {

	private String title;

	/**
	 * 
	 * @see ICommentGroup#getGroupTitle()
	 */
	public CommentGroup(String title) {
		this.title = title;
	}

	/**
	 * @see ICommentGroup#getGroupTitle()
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.swt.comments.data.ICommentGroup#getGroupTitle(
	 * )
	 */
	@Override
	public String getGroupTitle() {
		return title;
	}

}

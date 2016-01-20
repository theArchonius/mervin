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
 * A simple {@link ICommentLink} implementation which stores the values in
 * fields and that provides additional assignment based setters without any
 * further logic.
 * 
 * @author Florian Zoubek
 *
 */
public class CommentLink implements ICommentLink {

	private int startIndex;
	private int length;
	private ICommentLinkTarget commentLinkTarget;

	/**
	 * @see ICommentLink#getStartIndex()
	 * @see ICommentLink#getLength()
	 * @see ICommentLink#getCommentLinkTarget()
	 */
	public CommentLink(int startIndex, int length, ICommentLinkTarget commentLinkTarget) {
		super();
		this.startIndex = startIndex;
		this.length = length;
		this.commentLinkTarget = commentLinkTarget;
	}

	/**
	 * @see ICommentLink#getStartIndex()
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink#getStartIndex()
	 */
	@Override
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @see ICommentLink#getLength()
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink#getLength()
	 */
	@Override
	public int getLength() {
		return length;
	}

	/**
	 * @see ICommentLink#getCommentLinkTarget()
	 */
	public void setCommentLinkTarget(ICommentLinkTarget commentLinkTarget) {
		this.commentLinkTarget = commentLinkTarget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink#
	 * getCommentLinkTarget()
	 */
	@Override
	public ICommentLinkTarget getCommentLinkTarget() {
		return commentLinkTarget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CommentLink [startIndex=" + startIndex + ", length=" + length + ", commentLinkTarget="
				+ commentLinkTarget + "]";
	}

}

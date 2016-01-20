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

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link IComment} implementation which stores the values in fields
 * and that provides additional assignment based setters without any further
 * logic.
 * 
 * @author Florian Zoubek
 *
 */
public class Comment implements IComment {

	private String author;
	private Calendar creationTime;
	private String body;
	private Alignment alignment;
	private List<ICommentLink> commentLinks;

	/**
	 * @see IComment#getAuthor()
	 * @see IComment#getCreationTime()
	 * @see IComment#getBody()
	 * @see IComment#getAlignment()
	 * @see IComment#getCommentLinks()
	 */
	public Comment(String author, Calendar creationTime, String body, Alignment alignment,
			List<ICommentLink> commentLinks) {
		this.author = author;
		this.creationTime = creationTime;
		this.body = body;
		this.alignment = alignment;
		this.commentLinks = commentLinks;
	}

	/**
	 * @see IComment#getAuthor()
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.swt.comments.data.IComment#getAuthor()
	 */
	@Override
	public String getAuthor() {
		return author;
	}

	/**
	 * @see IComment#getCreationTime()
	 */
	public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.swt.comments.data.IComment#getCreationTime()
	 */
	@Override
	public Calendar getCreationTime() {
		return creationTime;
	}

	/**
	 * @see IComment#getBody()
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.swt.comments.data.IComment#getBody()
	 */
	@Override
	public String getBody() {
		return body;
	}

	/**
	 * @see IComment#getAlignment()
	 */
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.swt.comments.data.IComment#getAlignment()
	 */
	@Override
	public Alignment getAlignment() {
		return alignment;
	}

	/**
	 * @see IComment#getCommentLinks()
	 */
	public void setCommentLinks(List<ICommentLink> commentLinks) {
		this.commentLinks = commentLinks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.swt.comments.data.IComment#getCommentLinks()
	 */
	@Override
	public List<ICommentLink> getCommentLinks() {
		return commentLinks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Comment [author=" + author + ", creationTime=" + creationTime + ", body=" + body + ", alignment="
				+ alignment + ", commentLinks=" + commentLinks + "]";
	}

}

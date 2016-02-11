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
package at.bitandart.zoubek.mervin.comments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.CommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.IComment;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;

/**
 * An {@link IComment} implemenation for comment from the mervin model.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinComment implements IComment {

	private Comment mervinComment;
	private Alignment alignment;

	public MervinComment(Comment mervinComment, Alignment alignment) {
		this.mervinComment = mervinComment;
		this.alignment = alignment;
	}

	@Override
	public String getAuthor() {
		return mervinComment.getAuthor().getName();
	}

	@Override
	public Calendar getCreationTime() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTimeInMillis(mervinComment.getCreationTime());
		return calendar;
	}

	@Override
	public String getBody() {
		return mervinComment.getText();
	}

	@Override
	public Alignment getAlignment() {
		return alignment;
	}

	@Override
	public List<ICommentLink> getCommentLinks() {
		EList<CommentLink> commentLinks = mervinComment.getCommentLinks();
		List<ICommentLink> adaptedLinks = new ArrayList<ICommentLink>(commentLinks.size());
		for (CommentLink link : commentLinks) {
			adaptedLinks.add(new MervinCommentLink(link));
		}
		return adaptedLinks;
	}

	public Comment getRealComment() {
		return mervinComment;
	}

}
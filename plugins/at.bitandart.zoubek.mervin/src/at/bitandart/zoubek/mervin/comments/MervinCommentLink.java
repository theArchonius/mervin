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

import at.bitandart.zoubek.mervin.model.modelreview.CommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLinkTarget;

/**
 * An {@link ICommentLink} implementation for comment links of the mervin model.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinCommentLink implements ICommentLink {

	private CommentLink mervinLink;

	public MervinCommentLink(CommentLink mervinLink) {
		this.mervinLink = mervinLink;
	}

	@Override
	public void setStartIndex(int startIndex) {
		mervinLink.setStart(startIndex);
	}

	@Override
	public int getStartIndex() {
		return mervinLink.getStart();
	}

	@Override
	public void setLength(int length) {
		mervinLink.setLength(length);
	}

	@Override
	public int getLength() {
		return mervinLink.getLength();
	}

	@Override
	public ICommentLinkTarget getCommentLinkTarget() {
		return new MervinCommentLinkTarget(mervinLink.getTargets());
	}

}
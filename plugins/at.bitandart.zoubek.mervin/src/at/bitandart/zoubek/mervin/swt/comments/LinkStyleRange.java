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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;

/**
 * A {@link StyleRange} based on a {@link ICommentLink}.
 * 
 * @author Florian Zoubek
 *
 */
class LinkStyleRange extends StyleRange {

	private ICommentLink commentLink;

	public LinkStyleRange(ICommentLink commentLink) {
		this.commentLink = commentLink;
		this.underline = true;
		this.fontStyle = SWT.BOLD;
		this.start = commentLink.getStartIndex();
		this.length = commentLink.getLength();
	}

	public ICommentLink getCommentLink() {
		return commentLink;
	}
}

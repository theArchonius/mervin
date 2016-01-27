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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;

import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;

/**
 * A {@link LineStyleListener} that creates the line style ranges based on a
 * list of {@link ICommentLink}s.
 * 
 * @author Florian Zoubek
 *
 */
final class CommentLineStyleListener implements LineStyleListener {

	/**
	 * the current links that should be highlighted by this
	 * {@link LineStyleListener}.
	 */
	List<ICommentLink> commentLinks = new LinkedList<>();

	@Override
	public void lineGetStyle(LineStyleEvent event) {

		int lineStart = event.lineOffset;
		int lineEnd = lineStart + event.lineText.length();
		List<StyleRange> styles = new ArrayList<>(commentLinks.size());
		for (ICommentLink link : commentLinks) {
			int start = link.getStartIndex();
			int end = start + link.getLength();
			if (start >= lineStart && end <= lineEnd) {
				styles.add(createLinkStyleRange(start, end));
			}
		}
		event.styles = styles.toArray(new StyleRange[styles.size()]);

	}

	/**
	 * creates a StyleRange representing a link in the text.
	 * 
	 * @param start
	 *            the start index of the link.
	 * @param end
	 *            the end index of the link.
	 * @return a StyleRange for the given range.
	 */
	private StyleRange createLinkStyleRange(int start, int end) {

		StyleRange styleRange = new StyleRange();
		styleRange.start = start;
		styleRange.length = end - start;
		styleRange.fontStyle = SWT.BOLD;
		styleRange.underline = true;

		return styleRange;
	}

	/**
	 * @return the list of comment links that is used by this
	 *         {@link LineStyleListener}.
	 */
	public List<ICommentLink> getCommentLinks() {
		return commentLinks;
	}
}
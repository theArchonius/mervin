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

import java.util.Calendar;

/**
 * Represents a comment that is shown in a {@link CommentListViewer}.
 * 
 * @author Florian Zoubek
 *
 */
public interface Comment {

	public enum Alignment {
		LEFT, RIGHT
	}

	/**
	 * @return the name of the author.
	 */
	public String getAuthor();

	/**
	 * @return the creation time
	 */
	public Calendar getCreationTime();

	/**
	 * @return the comment text.
	 */
	public String getBody();

	/**
	 * @return the alignment of the comment.
	 */
	public Alignment getAlignment();

}

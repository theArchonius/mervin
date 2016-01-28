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
 * Describes a link (substring) within a text starting at a specific index and
 * with a given length.
 * 
 * @author Florian Zoubek
 *
 */
public interface ICommentLink {

	/**
	 * 
	 * @param startIndex
	 *            the index of the first character index within the containing
	 *            text.
	 */
	public void setStartIndex(int startIndex);

	/**
	 * @return the index of the first character index within the containing
	 *         text.
	 */
	public int getStartIndex();

	/**
	 * 
	 * @param length
	 *            the length of the substring that represents this link.
	 */
	public void setLength(int length);

	/**
	 * @return the length of the substring that represents this link.
	 */
	public int getLength();

	/**
	 * @return the target of this link.
	 */
	public ICommentLinkTarget getCommentLinkTarget();

}

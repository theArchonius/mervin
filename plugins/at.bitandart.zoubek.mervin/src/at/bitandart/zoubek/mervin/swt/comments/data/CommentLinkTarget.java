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
 * A simple {@link ICommentLinkTarget} implementation which stores the values in
 * fields and that provides additional assignment based setters without any
 * further logic.
 * 
 * @author Florian Zoubek
 *
 */
public class CommentLinkTarget implements ICommentLinkTarget {

	private String defaultText;

	/**
	 * @see ICommentLinkTarget#getDefaultText()
	 */
	public CommentLinkTarget(String defaultText) {
		super();
		this.defaultText = defaultText;
	}

	/**
	 * @see ICommentLinkTarget#getDefaultText()
	 */
	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.swt.comments.data.ICommentLinkTarget#
	 * getDefaultText()
	 */
	@Override
	public String getDefaultText() {
		return defaultText;
	}

}

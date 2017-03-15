/*******************************************************************************
 * Copyright (c) 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.review.explorer.content;

import org.eclipse.jface.viewers.TreeViewer;

/**
 * Base interface for a temporary container of elements in a {@link TreeViewer}
 * .
 * 
 * @author Florian Zoubek
 *
 */
public interface ITreeItem {

	/**
	 * @return true if the container has children, false otherwise.
	 */
	public boolean hasChildren();

	/**
	 * @return an array of all children of this container.
	 */
	public Object[] getChildren();

	/**
	 * @return the parent of the container or null, if it has no parent.
	 */
	public Object getParent();

	/**
	 * @return the element represented by this {@link ITreeItem}.
	 */
	public Object getElement();
}
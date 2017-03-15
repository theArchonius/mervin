/*******************************************************************************
 * Copyright (c) 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.review.explorer.content;

/**
 * An {@link ITreeItem} with no children.
 * 
 * @author Florian Zoubek
 *
 */
public class LeafTreeItem implements ITreeItem {

	private static final Object[] EMPTY = new Object[0];

	private Object element;
	private Object parent;

	public LeafTreeItem(Object element, Object parent) {
		this.element = element;
		this.parent = parent;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Object[] getChildren() {
		return EMPTY;
	}

	@Override
	public Object getParent() {
		return parent;
	}

	@Override
	public Object getElement() {
		return element;
	}
}

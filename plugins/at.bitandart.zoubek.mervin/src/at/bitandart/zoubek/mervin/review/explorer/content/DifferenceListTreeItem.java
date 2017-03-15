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

import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.ecore.EObject;

/**
 * An {@link ITreeItem} that holds a list of {@link Diff}s for a given
 * {@link EObject}.
 * 
 * @author Florian Zoubek
 *
 */
public class DifferenceListTreeItem implements ITreeItem {

	private EObject parent;
	private List<Diff> diffs;

	/**
	 * @param parent
	 *            the {@link EObject} to store the {@link Diff}s for.
	 * @param diffs
	 *            the diffs of the given {@link EObject}.
	 */
	public DifferenceListTreeItem(EObject parent, List<Diff> diffs) {
		this.parent = parent;
		this.diffs = diffs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItemContainer#
	 * hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return !diffs.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItemContainer#
	 * getChildren()
	 */
	@Override
	public Object[] getChildren() {
		return diffs.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItemContainer#
	 * getParent()
	 */
	@Override
	public Object getParent() {
		return parent;
	}

	@Override
	public Object getElement() {
		return "[Differences]";
	}
}

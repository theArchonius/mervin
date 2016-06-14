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
package at.bitandart.zoubek.mervin.swt.diff.tree;

import org.eclipse.swt.graphics.Image;

/**
 * Base interface for item providers of hierarchical diff items based on an
 * input object.
 * 
 * @author Florian Zoubek
 *
 */
public interface ITreeDiffItemProvider {

	/**
	 * @param input
	 *            the input object.
	 * @return the root diff item objects.
	 */
	public Object[] getRootItems(Object input);

	/**
	 * @param item
	 *            the diff item object to retrieve the items for.
	 * @return the child diff item objects for the given diff item object.
	 */
	public Object[] getChildren(Object item);

	/**
	 * @param item
	 *            the diff item to retrieve the label text for.
	 * @param side
	 *            the side to retrieve the label text for.
	 * @param changedSide
	 *            the side which is considered as containing the changed (new)
	 *            version. This is needed to determine {@link TreeDiffType#ADD}
	 *            and {@link TreeDiffType#DELETE} correctly, as an addition seen
	 *            from the left side is also an deletion seen from the right
	 *            side.
	 * @return the label text for the given item shown on the given
	 *         {@link TreeDiffSide}.
	 */
	public String getDiffItemText(Object item, TreeDiffSide side, TreeDiffSide changedSide);

	/**
	 * 
	 * @param item
	 *            the diff item to retrieve the image for.
	 * @param side
	 *            the side to retrieve the image for.
	 * @param changedSide
	 *            the side which is considered as containing the changed (new)
	 *            version. This is needed to determine {@link TreeDiffType#ADD}
	 *            and {@link TreeDiffType#DELETE} correctly, as an addition seen
	 *            from the left side is also an deletion seen from the right
	 *            side.
	 * @return the image for the given item shown on the given
	 *         {@link TreeDiffSide}.
	 */
	public Image getDiffItemImage(Object item, TreeDiffSide side, TreeDiffSide changedSide);

	/**
	 * @param item
	 *            the diff item to retrieve the {@link TreeDiffType} for.
	 * @param changedSide
	 *            the side which is considered as containing the changed (new)
	 *            version. This is needed to determine {@link TreeDiffType#ADD}
	 *            and {@link TreeDiffType#DELETE} correctly, as an addition seen
	 *            from the left side is also an deletion seen from the right
	 *            side.
	 * @return the {@link TreeDiffType} of the given diff item.
	 */
	public TreeDiffType getTreeDiffType(Object item, TreeDiffSide changedSide);

}

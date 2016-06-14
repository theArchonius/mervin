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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an item shown in a {@link TreeDiff}.
 * 
 * @author Florian Zoubek
 *
 */

public class TreeDiffItem {

	/**
	 * the parent item or null if it has no items.
	 */
	private TreeDiffItem parent;

	/**
	 * the left {@link TreeDiffSideItem} of this item.
	 */
	private TreeDiffSideItem leftSideItem;

	/**
	 * the right {@link TreeDiffSideItem} of this item.
	 */
	private TreeDiffSideItem rightSideItem;

	/**
	 * a collection of all child {@link TreeDiffItem}s.
	 */
	private List<TreeDiffItem> children = new ArrayList<>();

	/**
	 * the {@link TreeDiffType} of this item.
	 */
	private TreeDiffType treeDiffType;

	/**
	 * creates a new {@link TreeDiffItem} with the given arguments.
	 * 
	 * @param parent
	 *            the parent {@link TreeDiffItem} or null if this item has no
	 *            parent.
	 * @param leftSideItem
	 *            the left side item of the new item, must not be null.
	 * @param rightSideItem
	 *            the right side item of the new item, must not be null.
	 * @param treeDiffType
	 *            the {@link TreeDiffType} of this item, must not be null.
	 */
	public TreeDiffItem(TreeDiffItem parent, TreeDiffSideItem leftSideItem, TreeDiffSideItem rightSideItem,
			TreeDiffType treeDiffType) {
		super();
		this.parent = parent;

		if (leftSideItem == null) {
			throw new IllegalArgumentException("The left side item must not be null.");
		}
		this.leftSideItem = leftSideItem;

		if (rightSideItem == null) {
			throw new IllegalArgumentException("The right side item must not be null.");
		}
		this.rightSideItem = rightSideItem;

		if (treeDiffType == null) {
			throw new IllegalArgumentException("The tree diff type must not be null.");
		}
		this.treeDiffType = treeDiffType;
	}

	/**
	 * @param item
	 *            the item to check.
	 * @return true if the given item is a sibling of this item, false
	 *         otherwise.
	 */
	public boolean isSiblingOf(TreeDiffItem item) {
		return item != null && item.getParent() == parent;
	}

	/**
	 * @return the parent {@link TreeDiffItem} or null if this item has no
	 *         parent.
	 */
	public TreeDiffItem getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent {@link TreeDiffItem} to set or null if this item
	 *            has no parent.
	 */
	public void setParent(TreeDiffItem parent) {
		this.parent = parent;
	}

	/**
	 * @return the left side item of this item, never null.
	 */
	public TreeDiffSideItem getLeftSideItem() {
		return leftSideItem;
	}

	/**
	 * @param leftSideItem
	 *            the left side item to set for this item, must not be null.
	 */
	public void setLeftSideItem(TreeDiffSideItem leftSideItem) {

		if (leftSideItem == null) {
			throw new IllegalArgumentException("The left side item must not be null.");
		}

		this.leftSideItem = leftSideItem;
	}

	/**
	 * @return the right side item of this item, never null.
	 */
	public TreeDiffSideItem getRightSideItem() {
		return rightSideItem;
	}

	/**
	 * @param rightSideItem
	 *            the right side item to set for this item, must not be null
	 */
	public void setRightSideItem(TreeDiffSideItem rightSideItem) {

		if (rightSideItem == null) {
			throw new IllegalArgumentException("The right side item must not be null.");
		}

		this.rightSideItem = rightSideItem;
	}

	/**
	 * @return the children of this item, may be empty but never null.
	 */
	public List<TreeDiffItem> getChildren() {
		return children;
	}

	/**
	 * @return the tree diff type of this item, never null.
	 */
	public TreeDiffType getTreeDiffType() {
		return treeDiffType;
	}

	/**
	 * @param treeDiffType
	 *            the tree diff type to set for this item, must not be null.
	 */
	public void setTreeDiffType(TreeDiffType treeDiffType) {

		if (treeDiffType == null) {
			throw new IllegalArgumentException("The tree diff type must not be null.");
		}

		this.treeDiffType = treeDiffType;
	}
}

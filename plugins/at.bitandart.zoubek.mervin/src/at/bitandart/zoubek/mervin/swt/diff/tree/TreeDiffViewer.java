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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A {@link Viewer} that adapts a {@link TreeDiff} control and uses a
 * {@link ITreeDiffItemProvider} to create items for the control. This viewer
 * currently does not support specifying a selection.
 * 
 * @author Florian Zoubek
 *
 */
public class TreeDiffViewer extends Viewer {

	/**
	 * the input data.
	 */
	private Object input;

	/**
	 * the tree control that this viewer adapts.
	 */
	private TreeDiff treeDiffControl;

	/**
	 * the {@link ITreeDiffItemProvider} that is used to build the items for the
	 * {@link TreeDiff} control from the input data.
	 */
	private ITreeDiffItemProvider treeDiffItemProvider;

	/**
	 * determines which side is consider as changed (new) while building the
	 * tree items. For now, always consider the right side as the new side as
	 * long as TreeDiff does not support the other way round.
	 */
	private TreeDiffSide changedSide = TreeDiffSide.RIGHT;

	/**
	 * creates a new {@link TreeDiffViewer} and the underlying {@link TreeDiff}
	 * as child of the given parent.
	 * 
	 * @param parent
	 *            the parent {@link Composite} of the internal {@link TreeDiff}.
	 */
	public TreeDiffViewer(Composite parent) {
		treeDiffControl = new TreeDiff(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	@Override
	public Control getControl() {
		return treeDiffControl;
	}

	/**
	 * @return the underlying {@link TreeDiff} control.
	 */
	public TreeDiff getTreeDiff() {
		return treeDiffControl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getInput()
	 */
	@Override
	public Object getInput() {
		return input;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return StructuredSelection.EMPTY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#refresh()
	 */
	@Override
	public void refresh() {

		if (treeDiffItemProvider != null) {
			Object[] rootItemObjects = treeDiffItemProvider.getRootItems(input);
			List<TreeDiffItem> rootItems = new ArrayList<>(rootItemObjects.length);
			for (Object itemObj : rootItemObjects) {
				TreeDiffItem treeDiffItem = createTreeDiffItem(itemObj);
				if (treeDiffItem != null) {
					rootItems.add(treeDiffItem);
				}
			}
			treeDiffControl.setItemData(rootItems);
		}

	}

	/**
	 * creates a {@link TreeDiffItem} for the given item object using the
	 * current {@link ITreeDiffItemProvider}. This method requires that an
	 * {@link ITreeDiffItemProvider} has been set.
	 * 
	 * @param itemObj
	 *            the item object to create the {@link TreeDiffItem} for.
	 * @return the {@link TreeDiffItem} for the given item object.
	 */
	private TreeDiffItem createTreeDiffItem(Object itemObj) {

		TreeDiffSideItem leftSide = createTreeDiffItemSide(itemObj, TreeDiffSide.LEFT);
		TreeDiffSideItem rightSide = createTreeDiffItemSide(itemObj, TreeDiffSide.RIGHT);
		TreeDiffType diffType = treeDiffItemProvider.getTreeDiffType(itemObj, changedSide);

		TreeDiffItem item = new TreeDiffItem(null, leftSide, rightSide, diffType);

		Object[] children = treeDiffItemProvider.getChildren(itemObj);
		for (Object childItemObj : children) {
			TreeDiffItem treeDiffItemChild = createTreeDiffItem(childItemObj);
			item.getChildren().add(treeDiffItemChild);
			treeDiffItemChild.setParent(item);

			/*
			 * enforce that children of deleted or added items have the same
			 * diff type
			 */
			if (diffType != treeDiffItemChild.getTreeDiffType()
					&& (diffType == TreeDiffType.ADD || diffType == TreeDiffType.DELETE)) {

				treeDiffItemChild.setTreeDiffType(diffType);
			}
		}

		return item;
	}

	/**
	 * creates a {@link TreeDiffSideItem} for the given item object using the
	 * current {@link ITreeDiffItemProvider}. This method requires that an
	 * {@link ITreeDiffItemProvider} has been set.
	 * 
	 * @param itemObj
	 *            the item object to create the {@link TreeDiffItem} for.
	 * @param side
	 *            the side to create.
	 * @return the {@link TreeDiffSideItem} for the given item object.
	 */
	private TreeDiffSideItem createTreeDiffItemSide(Object itemObj, TreeDiffSide side) {

		Image diffItemImage = treeDiffItemProvider.getDiffItemImage(itemObj, side, changedSide);
		String diffItemText = treeDiffItemProvider.getDiffItemText(itemObj, side, changedSide);

		return new TreeDiffSideItem(diffItemText, diffItemImage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#setInput(java.lang.Object)
	 */
	@Override
	public void setInput(Object input) {
		this.input = input;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.
	 * ISelection, boolean)
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		// Currently not supported
	}

	/**
	 * sets the {@link ITreeDiffItemProvider} to use for extracting the
	 * {@link TreeDiff} items from the input data.
	 * 
	 * @param treeDiffItemProvider
	 *            the {@link ITreeDiffItemProvider} to set.
	 */
	public void setTreeDiffItemProvider(ITreeDiffItemProvider treeDiffItemProvider) {
		this.treeDiffItemProvider = treeDiffItemProvider;
	}

}

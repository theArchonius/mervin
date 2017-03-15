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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;

import at.bitandart.zoubek.mervin.IMatchHelper;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.review.explorer.content.TreeItemCache.ITreeItemFactory;

/**
 * An {@link IReviewExplorerContentProvider} for the model review tree. Shows
 * currently the contents of all {@link PatchSet}s in a {@link ModelReview} as
 * well as the involved models and diagrams.
 * 
 * @author Florian Zoubek
 *
 */
public class ModelReviewContentProvider implements IReviewExplorerContentProvider {

	private ModelReview modelReview;
	private IMatchHelper matchHelper;
	private SelectedComparisonTreeItem selectedComparisonTreeItem;
	private Object input;
	private TreeItemCache<PatchSet> patchSetItemCache;
	private ITreeItemFactory<PatchSet> patchSetTreeItemFactory = new ITreeItemFactory<PatchSet>() {

		@Override
		public ITreeItem createTreeItem(PatchSet patchSet, Object parent) {
			return new PatchSetTreeItem(patchSet, parent);
		}
	};

	public ModelReviewContentProvider(IMatchHelper matchHelper) {
		this.matchHelper = matchHelper;
		patchSetItemCache = new TreeItemCache<>(patchSetTreeItemFactory);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		input = newInput;
		// we need the root model review to find the parent of some children
		if (newInput instanceof ModelReview) {
			modelReview = (ModelReview) newInput;
		}
		// clear the cache
		selectedComparisonTreeItem = null;
		patchSetItemCache.clear();
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ITreeItem) {
			return ((ITreeItem) element).hasChildren();
		}
		return false;
	}

	@Override
	public Object getParent(Object element) {

		if (element instanceof ITreeItem) {
			return ((ITreeItem) element).getParent();
		}
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ModelReview) {

			ModelReview modelReview = ((ModelReview) inputElement);
			EList<PatchSet> patchSets = modelReview.getPatchSets();
			List<Object> children = new ArrayList<>(patchSets.size() + 1);

			if (selectedComparisonTreeItem == null) {
				selectedComparisonTreeItem = new SelectedComparisonTreeItem(modelReview);
			}

			children.add(selectedComparisonTreeItem);

			patchSetItemCache.invalidateOldElements(patchSets);
			patchSetItemCache.addTreeItems(patchSets, null, children);

			return children.toArray();
		}
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof ITreeItem) {
			return ((ITreeItem) parentElement).getChildren();
		}

		return new Object[0];
	}

	@Override
	public PatchSet getContainingPatchSet(Object object) {

		Object parent = object;
		while (parent != null) {

			if (parent instanceof PatchSet) {
				return (PatchSet) parent;
			}
			parent = getParent(parent);
		}

		return null;
	}

	@Override
	public List<Diff> getMatchDiffsFor(EObject eObject) {

		List<Diff> differences = new LinkedList<Diff>();
		if (eObject instanceof Match) {
			differences.addAll(((Match) eObject).getDifferences());
			return differences;
		}

		PatchSet patchSet = getContainingPatchSet(eObject);
		if (patchSet != null) {

			Match match = patchSet.getModelComparison().getMatch(eObject);
			if (match != null) {
				differences.addAll(match.getDifferences());
			}

			match = patchSet.getDiagramComparison().getMatch(eObject);
			if (match != null) {
				differences.addAll(match.getDifferences());
			}
		}
		return differences;
	}

	@Override
	public List<Diff> getDiffsFor(EObject eObject) {

		List<Diff> differences = new LinkedList<Diff>();
		if (eObject instanceof Match) {
			Match match = (Match) eObject;
			EObject left = match.getLeft();
			EObject right = match.getRight();
			Comparison comparison = match.getComparison();

			Set<Diff> diffSet = new HashSet<>();
			if (left != null) {
				diffSet.addAll(comparison.getDifferences(left));
			}
			if (right != null) {
				diffSet.addAll(comparison.getDifferences(right));
			}

			differences.addAll(diffSet);

			return differences;
		}

		PatchSet patchSet = getContainingPatchSet(eObject);
		if (patchSet != null) {

			differences.addAll(patchSet.getModelComparison().getDifferences(eObject));
			differences.addAll(patchSet.getDiagramComparison().getDifferences(eObject));
		}
		return differences;
	}

	@Override
	public Set<ITreeItem> getTreeItemsFor(Object element) {
		Set<ITreeItem> treeItems = new HashSet<>();
		if (input != null) {
			collectTreeItems(input, element, treeItems);
		}
		return treeItems;
	}

	/**
	 * collects the {@link ITreeItem}s that are associated with the given
	 * element.
	 * 
	 * @param root
	 *            the root content tree element to start collecting.
	 * @param element
	 *            the element to search {@link ITreeItem}s for.
	 * @param treeItems
	 *            the set of {@link ITreeItem} to store the found
	 *            {@link ITreeItem} into.
	 */
	private void collectTreeItems(Object root, Object element, Set<ITreeItem> treeItems) {

		if (root instanceof ITreeItem && ((ITreeItem) root).getElement() == element) {
			treeItems.add((ITreeItem) root);
		}

		Object[] children = null;
		if (root == input) {
			children = getElements(root);
		} else {
			children = getChildren(root);
		}

		for (Object child : children) {
			collectTreeItems(child, element, treeItems);
		}

	}
}
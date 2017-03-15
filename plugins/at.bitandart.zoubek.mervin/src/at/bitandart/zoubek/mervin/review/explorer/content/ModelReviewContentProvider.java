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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.bitandart.zoubek.mervin.IMatchHelper;
import at.bitandart.zoubek.mervin.model.modelreview.DiagramResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.Patch;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * An {@link IReviewExplorerContentProvider} for the model review tree. Shows
 * currently the contents of all {@link PatchSet}s in a {@link ModelReview} as
 * well as the involved models and diagrams.
 * 
 * @author Florian Zoubek
 *
 */
public class ModelReviewContentProvider implements IReviewExplorerContentProvider {

	private AdapterFactoryContentProvider adapterFactoryContentProvider;

	private ModelReview modelReview;
	private Map<PatchSet, Collection<Object>> cachedPatchSetChildren = new HashMap<>();
	private Map<EObject, DifferenceListTreeItem> cachedDifferenceTreeItems = new HashMap<>();
	private IMatchHelper matchHelper;
	private SelectedComparisonTreeItem selectedComparisonTreeItem;
	private Object input;

	public ModelReviewContentProvider(IMatchHelper matchHelper) {
		adapterFactoryContentProvider = new AdapterFactoryContentProvider(
				new ComposedAdapterFactory(EMFCompareRCPPlugin.getDefault().createFilteredAdapterFactoryRegistry()));
		this.matchHelper = matchHelper;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		input = newInput;
		adapterFactoryContentProvider.inputChanged(viewer, oldInput, newInput);
		// we need the root model review to find the parent of some children
		if (newInput instanceof ModelReview) {
			modelReview = (ModelReview) newInput;
		}
		// clear the cache
		cachedPatchSetChildren.clear();
		cachedDifferenceTreeItems.clear();
		selectedComparisonTreeItem = null;
	}

	@Override
	public void dispose() {
		adapterFactoryContentProvider.dispose();
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ModelReview) {
			return true;
		}
		if (element instanceof PatchSet) {
			return true;
		}
		if (element instanceof ITreeItem) {
			return ((ITreeItem) element).hasChildren();
		}
		if (element instanceof ModelResource) {
			return !((ModelResource) element).getObjects().isEmpty();
		}
		if (element instanceof EObject) {
			return adapterFactoryContentProvider.hasChildren(element) || !getMatchDiffsFor((EObject) element).isEmpty();
		}
		return false;
	}

	@Override
	public Object getParent(Object element) {

		if (element instanceof ITreeItem) {
			return ((ITreeItem) element).getParent();
		}
		if (element instanceof ModelReview) {
			return null;
		}
		if (element instanceof PatchSet) {
			return null;
		}
		if (element instanceof DiagramResource || element instanceof ModelResource || element instanceof Patch) {
			return getPatchSetChildContainer(element);
		}
		if (element instanceof Diff) {
			return getDifferenceContainer((Diff) element);
		}
		if (element instanceof EObject) {
			EObject eObject = (EObject) element;
			Object parent = adapterFactoryContentProvider.getParent(element);

			/*
			 * FIXME It might be better to use an own implementation of
			 * EcoreUtil.UsageCrossReferencer to detect references to the model
			 * resource
			 */
			if (parent == null || parent instanceof Resource) {
				// search for an containing model or diagram resource
				for (PatchSet patchSet : modelReview.getPatchSets()) {
					ModelResource modelResource = findContainingModelResource(patchSet.getNewInvolvedDiagrams(),
							eObject);
					if (modelResource != null)
						return modelResource;
					modelResource = findContainingModelResource(patchSet.getOldInvolvedDiagrams(), eObject);
					if (modelResource != null)
						return modelResource;
					modelResource = findContainingModelResource(patchSet.getNewInvolvedModels(), eObject);
					if (modelResource != null)
						return modelResource;
					modelResource = findContainingModelResource(patchSet.getOldInvolvedModels(), eObject);
					if (modelResource != null)
						return modelResource;
				}
			}
			return parent;
		}
		return null;
	}

	/**
	 * @param object
	 *            the object to get the container for.
	 * @return the container containing the given object, or null if no
	 *         container has been found.
	 */
	private Object getPatchSetChildContainer(Object object) {

		PatchSet patchSet = null;
		if (object instanceof EObject) {
			EObject container = ((EObject) object).eContainer();
			if (container instanceof PatchSet) {
				patchSet = (PatchSet) container;
			}
		}

		if (patchSet != null && !cachedPatchSetChildren.containsKey(patchSet)) {
			rebuildPatchSetChildrenContainersFor(patchSet);
		}

		Collection<Collection<Object>> cachedPatchSetChildrenEntries = cachedPatchSetChildren.values();

		for (Collection<Object> cachedChildren : cachedPatchSetChildrenEntries) {

			if (cachedChildren != null) {

				for (Object child : cachedChildren) {

					if (child instanceof ITreeItem
							&& Arrays.asList(((ITreeItem) child).getChildren()).contains(object)) {
						return child;
					}
				}
			}
		}
		return null;
	}

	/**
	 * rebuilds the cached containers for the given patch set
	 * 
	 * @param patchSet
	 *            the patch set to rebuild the container cache for.
	 */
	private void rebuildPatchSetChildrenContainersFor(PatchSet patchSet) {
		getChildren(patchSet);
	}

	/**
	 * @param diff
	 *            the {@link Diff} to find the {@link DifferenceListTreeItem}
	 *            for.
	 * @return the {@link DifferenceListTreeItem} containing the given diff or
	 *         null if no {@link DifferenceListTreeItem} could be found.
	 */
	private DifferenceListTreeItem getDifferenceContainer(Diff diff) {

		EObject newValue = matchHelper.getNewValue((diff).getMatch());

		if (newValue != null) {

			if (!cachedDifferenceTreeItems.containsKey(newValue)) {
				rebuildCachedDifferenceContainerFor(newValue);
			}
			return cachedDifferenceTreeItems.get(newValue);
		}
		return null;
	}

	/**
	 * rebuilds the cached containers for the given {@link EObject}
	 * 
	 * @param object
	 *            the EObject to rebuild the container cache for.
	 */
	private void rebuildCachedDifferenceContainerFor(EObject object) {
		getChildren(object);
	}

	/**
	 * finds the {@link ModelResource} that contains the given object.
	 * 
	 * @param modelResources
	 *            a {@link Collection} of {@link ModelResource}s to check
	 * @param object
	 * @return the {@link ModelResource} or null if no {@link ModelResource}
	 *         contains the object
	 */
	private ModelResource findContainingModelResource(Collection<? extends ModelResource> modelResources,
			EObject object) {
		for (ModelResource modelResource : modelResources) {
			if (modelResource.getObjects().contains(object)) {
				return modelResource;
			}
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
			children.addAll(patchSets);

			return children.toArray();
		}
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof PatchSet) {

			PatchSet patchSet = (PatchSet) parentElement;
			/*
			 * These categories do not exist in the model, so we have to create
			 * temporary containers. We cache them to make sure that these
			 * categories stay the same even if the tree is refreshed.
			 */
			if (!cachedPatchSetChildren.containsKey(patchSet)) {
				List<Object> children = new LinkedList<>();
				children.add(new PatchSetInvolvedModelsTreeItem(patchSet));
				children.add(new PatchSetInvolvedDiagramsTreeItem(patchSet));
				children.add(new PatchSetTreeItem(patchSet));
				cachedPatchSetChildren.put(patchSet, children);
			}
			return cachedPatchSetChildren.get(patchSet).toArray();
		}
		if (parentElement instanceof ITreeItem) {
			return ((ITreeItem) parentElement).getChildren();
		}
		if (parentElement instanceof ModelResource) {
			return ((ModelResource) parentElement).getObjects().toArray();
		}
		if (parentElement instanceof EObject) {

			EObject parentEObject = (EObject) parentElement;

			List<Object> children = new LinkedList<Object>();
			children.addAll(Arrays.asList(adapterFactoryContentProvider.getChildren(parentElement)));

			List<Diff> matchDiffs = getMatchDiffsFor(parentEObject);

			if (!matchDiffs.isEmpty()) {
				/*
				 * This category does not exist in the model, so we have to
				 * create a temporary container. We cache them to make sure that
				 * the category stays the same even if the tree is refreshed.
				 */
				if (!cachedDifferenceTreeItems.containsKey(parentEObject)) {
					cachedDifferenceTreeItems.put(parentEObject, new DifferenceListTreeItem(parentEObject, matchDiffs));
				}
				children.add(cachedDifferenceTreeItems.get(parentEObject));
			}

			return children.toArray();
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
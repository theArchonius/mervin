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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.bitandart.zoubek.mervin.IMatchHelper;
import at.bitandart.zoubek.mervin.model.modelreview.DiagramResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * An {@link ITreeContentProvider} for the model review tree. Shows currently
 * the contents of all {@link PatchSet}s in a {@link ModelReview} as well as the
 * involved models and diagrams.
 * 
 * @author Florian Zoubek
 *
 */
public class ModelReviewContentProvider implements IReviewExplorerContentProvider {

	private AdapterFactoryContentProvider adapterFactoryContentProvider;

	private ModelReview modelReview;
	private Map<PatchSet, Collection<Object>> cachedPatchSetChildren = new HashMap<>();
	private Map<EObject, DifferencesTreeItem> cachedDiffrenceTreeItems = new HashMap<>();
	private IMatchHelper matchHelper;

	public ModelReviewContentProvider(IMatchHelper matchHelper) {
		adapterFactoryContentProvider = new AdapterFactoryContentProvider(
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		this.matchHelper = matchHelper;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		adapterFactoryContentProvider.inputChanged(viewer, oldInput, newInput);
		// we need the root model review to find the parent of some children
		if (newInput instanceof ModelReview) {
			modelReview = (ModelReview) newInput;
		}
		// clear the cache
		cachedPatchSetChildren.clear();
		cachedDiffrenceTreeItems.clear();
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
		if (element instanceof ITreeItemContainer) {
			return ((ITreeItemContainer) element).hasChildren();
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

		if (element instanceof ITreeItemContainer) {
			return ((ITreeItemContainer) element).getParent();
		}
		if (element instanceof ModelReview) {
			return null;
		}
		if (element instanceof PatchSet) {
			return ((PatchSet) element).getReview();
		}
		if (element instanceof DiagramResource) {
			DiagramResource diagramResource = (DiagramResource) element;
			for (PatchSet patchSet : modelReview.getPatchSets()) {
				if (patchSet.getNewInvolvedDiagrams().contains(diagramResource)
						|| patchSet.getNewInvolvedDiagrams().contains(diagramResource)) {
					return patchSet;
				}
			}
			return null;
		}
		if (element instanceof ModelResource) {
			ModelResource modelResource = (ModelResource) element;
			for (PatchSet patchSet : modelReview.getPatchSets()) {
				if (patchSet.getNewInvolvedModels().contains(modelResource)
						|| patchSet.getOldInvolvedModels().contains(modelResource)) {
					return patchSet;
				}
			}
			return null;
		}
		if (element instanceof Diff) {
			return matchHelper.getNewValue(((Diff) element).getMatch());
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
			return ((ModelReview) inputElement).getPatchSets().toArray();
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
				children.add(new InvolvedModelsTreeItem(patchSet));
				children.add(new InvolvedDiagramsTreeItem(patchSet));
				children.add(new PatchSetTreeItem(patchSet));
				cachedPatchSetChildren.put(patchSet, children);
			}
			return cachedPatchSetChildren.get(patchSet).toArray();
		}
		if (parentElement instanceof ITreeItemContainer) {
			return ((ITreeItemContainer) parentElement).getChildren();
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
				if (!cachedDiffrenceTreeItems.containsKey(parentEObject)) {
					cachedDiffrenceTreeItems.put(parentEObject, new DifferencesTreeItem(parentEObject, matchDiffs));
				}
				children.add(cachedDiffrenceTreeItems.get(parentEObject));
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

		PatchSet patchSet = getContainingPatchSet(eObject);
		if (patchSet != null) {

			differences.addAll(patchSet.getModelComparison().getDifferences(eObject));
			differences.addAll(patchSet.getDiagramComparison().getDifferences(eObject));
		}
		return differences;
	}
}
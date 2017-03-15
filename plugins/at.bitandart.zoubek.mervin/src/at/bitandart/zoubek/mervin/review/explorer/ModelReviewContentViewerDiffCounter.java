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
package at.bitandart.zoubek.mervin.review.explorer;

import java.util.Map;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.google.common.collect.Iterables;

import at.bitandart.zoubek.mervin.model.modelreview.ModelResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.predicates.MervinPredicates;
import at.bitandart.zoubek.mervin.review.explorer.content.ComparisonTreeItem;
import at.bitandart.zoubek.mervin.review.explorer.content.ComparisonWithTitle;
import at.bitandart.zoubek.mervin.review.explorer.content.ITreeItem;

/**
 * An {@link IDifferenceCounter} that provides difference counts for a model
 * review shown in a {@link ContentViewer} with an {@link ITreeContentProvider}.
 * 
 * @author Florian Zoubek
 *
 */
public class ModelReviewContentViewerDiffCounter implements IDifferenceCounter {

	private ContentViewer viewer;

	public ModelReviewContentViewerDiffCounter(ContentViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public int getMaximumDiffCount(Object object) {

		ITreeContentProvider contentProvider = getTreeContentProvider();

		if (object instanceof PatchSet) {
			return ((PatchSet) object).getMaxObjectChangeCount();
		}
		if (object instanceof ComparisonTreeItem) {
			return ((ComparisonTreeItem) object).getComparison().getDifferences().size();
		}
		if (object == null) {
			/*
			 * the root element, so determine the count based on its children.
			 */
			Object[] children = getTreeContentProvider().getElements(viewer.getInput());
			if (children != null) {
				return getMaximumDiffCountOf(children);
			}
		}

		Object parent = contentProvider.getParent(object);

		if (parent != null) {

			int maximumDiffCount = getMaximumDiffCount(parent);
			if (maximumDiffCount >= 0) {
				return maximumDiffCount;
			}
		}
		return -1;
	}

	/**
	 * @param objects
	 *            the objects to calculate the maximum difference count for.
	 * @return the maximum difference count of the given objects by calling
	 *         {@link #getDiffCount(Object)} for each object. -1 is returned if
	 *         none of the objects have a valid difference count.
	 */
	private int getMaximumDiffCountOf(Object[] objects) {
		int maxDiffCount = -1;
		for (Object child : objects) {
			maxDiffCount = Math.max(getDiffCount(child), maxDiffCount);
		}
		return maxDiffCount;
	}

	@Override
	public int getTotalDiffCount(Object object) {
		return getMaximumDiffCount(null);
	}

	@Override
	public int getDiffCount(Object object) {

		if (object instanceof Match) {
			return Iterables.size(((Match) object).getAllDifferences());
		}

		if (object instanceof ComparisonWithTitle) {
			return ((ComparisonWithTitle) object).getComparison().getDifferences().size();
		}

		if (object instanceof Comparison) {
			return ((Comparison) object).getDifferences().size();
		}

		if (object instanceof Diff) {
			return 1;
		}

		if (object instanceof ITreeItem) {
			int diffCount = getDiffCount(((ITreeItem) object).getElement());
			if (diffCount >= 0) {
				return diffCount;
			}
		}

		if (object instanceof ITreeItem || object instanceof ModelResource || object instanceof PatchSet) {
			Object[] children = getTreeContentProvider().getChildren(object);
			if (children != null) {
				return getDiffCountOf(children);
			}
		}

		if (object == null) {
			/*
			 * treat root as a container
			 */
			Object[] children = getTreeContentProvider().getElements(viewer.getInput());
			if (children != null) {
				return getDiffCountOf(children);
			}
		}

		PatchSet patchSet = findPatchSet(object);
		if (patchSet != null) {

			Map<EObject, Integer> objectChangeCount = patchSet.getObjectChangeCount();
			if (objectChangeCount.containsKey(object)) {
				return objectChangeCount.get(object);
			}

		}

		return -1;
	}

	/**
	 * calculates the sum of all difference counts of the specified objects.
	 * Objects that have no valid difference count will be ignored, except if
	 * all objects have no valid difference count.
	 * 
	 * @param objects
	 * @return the sum of all difference counts of the given objects, -1 if none
	 *         of the given object have valid difference counts.
	 */
	private int getDiffCountOf(Object[] objects) {
		int totalDiffCount = 0;
		boolean hasValidDiffCount = false;
		for (Object child : objects) {
			int diffCount = getDiffCount(child);
			if (diffCount >= 0) {
				hasValidDiffCount = true;
				totalDiffCount += diffCount;
			}
		}
		if (!hasValidDiffCount) {
			return -1;
		}
		return totalDiffCount;
	}

	@Override
	public int getDiffCount(Object object, final DifferenceKind kind) {

		if (object instanceof Match) {
			return Iterables
					.size(Iterables.filter(((Match) object).getAllDifferences(), MervinPredicates.diffKind(kind)));
		}

		if (object instanceof ComparisonWithTitle) {
			return Iterables.size(Iterables.filter(((ComparisonWithTitle) object).getComparison().getDifferences(),
					MervinPredicates.diffKind(kind)));
		}

		if (object instanceof Comparison) {
			return Iterables
					.size(Iterables.filter(((Comparison) object).getDifferences(), MervinPredicates.diffKind(kind)));
		}

		if (object instanceof Diff) {
			return 1;
		}

		if (object instanceof ITreeItem) {
			int diffCount = getDiffCount(((ITreeItem) object).getElement(), kind);
			if (diffCount >= 0) {
				return diffCount;
			}
		}

		if (object instanceof ITreeItem || object instanceof ModelResource || object instanceof PatchSet
				|| object instanceof ModelReview) {
			Object[] children = getTreeContentProvider().getChildren(object);
			if (children != null) {
				return getDiffCountOf(children, kind);
			}
		}

		if (object == null) {
			/*
			 * treat root as a container
			 */
			Object[] children = getTreeContentProvider().getElements(viewer.getInput());
			if (children != null) {
				return getDiffCountOf(children, kind);
			}
		}

		PatchSet patchSet = findPatchSet(object);
		if (object instanceof EObject && patchSet != null) {
			Match match = patchSet.getDiagramComparison().getMatch((EObject) object);
			if (match == null) {
				match = patchSet.getModelComparison().getMatch((EObject) object);
			}
			if (match != null) {
				return Iterables.size(Iterables.filter(match.getAllDifferences(), MervinPredicates.diffKind(kind)));
			}

		}

		return -1;
	}

	/**
	 * calculates the sum of all difference counts of the specified objects with
	 * respect to the given {@link DifferenceKind}. Objects that have no valid
	 * difference count will be ignored, except if all objects have no valid
	 * difference count.
	 * 
	 * @param objects
	 * @return the sum of all difference counts of the given objects, -1 if none
	 *         of the given object have valid difference counts.
	 */
	private int getDiffCountOf(Object[] objects, DifferenceKind kind) {
		int totalDiffCount = 0;
		boolean hasValidDiffCount = false;
		for (Object child : objects) {
			int diffCount = getDiffCount(child, kind);
			if (diffCount >= 0) {
				hasValidDiffCount = true;
				totalDiffCount += diffCount;
			}
		}
		if (!hasValidDiffCount) {
			return -1;
		}
		return totalDiffCount;
	}

	/**
	 * finds the parent {@link PatchSet} of the given element, if it exists.
	 * 
	 * @param element
	 * @return the parent {@link PatchSet} or null if it cannot be found
	 */
	protected PatchSet findPatchSet(Object element) {

		ITreeContentProvider contentProvider = getTreeContentProvider();
		Object currentElement = element;

		while (currentElement != null && !(currentElement instanceof PatchSet)) {
			currentElement = contentProvider.getParent(currentElement);
		}

		if (currentElement instanceof PatchSet) {
			return (PatchSet) currentElement;
		}

		return null;
	}

	/**
	 * @return the {@link ITreeContentProvider} of the assigned viewer.
	 */
	protected ITreeContentProvider getTreeContentProvider() {
		return (ITreeContentProvider) viewer.getContentProvider();
	}

}

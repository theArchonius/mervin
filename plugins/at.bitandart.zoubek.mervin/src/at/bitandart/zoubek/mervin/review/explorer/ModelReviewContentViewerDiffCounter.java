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

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * An {@link IDifferenceCounter} that provides difference counts within patch
 * sets shown in a {@link ContentViewer} with an {@link ITreeContentProvider}.
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

		PatchSet patchSet = findPatchSet(object);
		if (patchSet != null) {
			return patchSet.getMaxObjectChangeCount();
		}
		return -1;
	}

	@Override
	public int getTotalDiffCount(Object object) {
		PatchSet patchSet = findPatchSet(object);
		if (patchSet != null) {
			return patchSet.getModelComparison().getDifferences().size()
					+ patchSet.getDiagramComparison().getDifferences().size();
		}
		return -1;
	}

	@Override
	public int getDiffCount(Object object) {

		PatchSet patchSet = findPatchSet(object);
		if (patchSet != null) {

			Map<EObject, Integer> objectChangeCount = patchSet.getObjectChangeCount();
			if (objectChangeCount.containsKey(object)) {
				return objectChangeCount.get(object);
			}

		}

		return -1;
	}

	@Override
	public int getDiffCount(Object object, final DifferenceKind kind) {

		PatchSet patchSet = findPatchSet(object);
		if (object instanceof EObject && patchSet != null) {
			Match match = patchSet.getDiagramComparison().getMatch((EObject) object);
			if (match == null) {
				match = patchSet.getModelComparison().getMatch((EObject) object);
			}
			if (match != null) {
				return Iterables.size(Iterables.filter(match.getAllDifferences(), new Predicate<Diff>() {

					@Override
					public boolean apply(Diff input) {
						return input.getKind() == kind;
					}
				}));
			}

		}

		return -1;
	}

	/**
	 * finds the parent {@link PatchSet} of the given element, if it exists.
	 * 
	 * @param element
	 * @return the parent {@link PatchSet} or null if it cannot be found
	 */
	protected PatchSet findPatchSet(Object element) {

		ITreeContentProvider contentProvider = (ITreeContentProvider) viewer.getContentProvider();
		Object currentElement = element;

		while (currentElement != null && !(currentElement instanceof PatchSet)) {
			currentElement = contentProvider.getParent(currentElement);
		}

		if (currentElement instanceof PatchSet) {
			return (PatchSet) currentElement;
		}

		return null;
	}

}

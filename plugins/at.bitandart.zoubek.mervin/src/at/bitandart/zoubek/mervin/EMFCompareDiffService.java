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
package at.bitandart.zoubek.mervin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import at.bitandart.zoubek.mervin.model.modelreview.DiagramResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * An {@link IDiffService} implementation based on EMF Compare.
 * 
 * @author Florian Zoubek
 *
 */
public class EMFCompareDiffService implements IDiffService {

	@Override
	public void updateSelectedComparison(ModelReview review, IProgressMonitor progressMonitor) {

		progressMonitor.beginTask("Updating Comparison...", 2);
		progressMonitor.subTask("Comparing models...");

		// models

		PatchSet oldPatchSet = review.getLeftPatchSet();
		PatchSet newPatchSet = review.getRightPatchSet();

		EList<ModelResource> oldInvolvedModels = new BasicEList<ModelResource>();
		EList<ModelResource> newInvolvedModels = new BasicEList<ModelResource>();

		EList<PatchSet> patchSets = review.getPatchSets();
		if (!patchSets.isEmpty()) {
			oldInvolvedModels = patchSets.get(0).getOldInvolvedModels();
			newInvolvedModels = patchSets.get(0).getOldInvolvedModels();
		}

		if (oldPatchSet != null) {
			oldInvolvedModels = oldPatchSet.getNewInvolvedModels();
			newInvolvedModels = oldPatchSet.getOldInvolvedModels();
		}

		if (newPatchSet != null) {
			newInvolvedModels = newPatchSet.getNewInvolvedModels();
			if (oldPatchSet == null) {
				oldInvolvedModels = newPatchSet.getOldInvolvedModels();
			}
		}

		progressMonitor.worked(1);
		progressMonitor.subTask("Comparing diagrams...");

		Comparison selectedModelComparison = compareModelInstances(oldInvolvedModels, newInvolvedModels);
		review.setSelectedModelComparison(selectedModelComparison);

		// diagrams

		EList<DiagramResource> oldInvolvedDiagrams = new BasicEList<DiagramResource>();
		EList<DiagramResource> newInvolvedDiagrams = new BasicEList<DiagramResource>();

		if (!patchSets.isEmpty()) {
			oldInvolvedDiagrams = patchSets.get(0).getOldInvolvedDiagrams();
			newInvolvedDiagrams = patchSets.get(0).getOldInvolvedDiagrams();
		}

		if (oldPatchSet != null) {
			oldInvolvedDiagrams = oldPatchSet.getNewInvolvedDiagrams();
			newInvolvedDiagrams = oldPatchSet.getOldInvolvedDiagrams();
		}

		if (newPatchSet != null) {
			newInvolvedDiagrams = newPatchSet.getNewInvolvedDiagrams();
			if (oldPatchSet == null) {
				oldInvolvedDiagrams = newPatchSet.getOldInvolvedDiagrams();
			}
		}

		Comparison selectedDiagramComparison = compareModelInstances(oldInvolvedDiagrams, newInvolvedDiagrams);
		review.setSelectedDiagramComparison(selectedDiagramComparison);

		progressMonitor.worked(1);

	}

	/**
	 * compares to sets of {@link ModelInstance}s.
	 * 
	 * @param oldInvolvedModels
	 * @param newInvolvedModels
	 * @return the result of the comparison
	 */
	private Comparison compareModelInstances(EList<? extends ModelResource> oldInvolvedModels,
			EList<? extends ModelResource> newInvolvedModels) {
		ResourceSet oldResourceSet = new ResourceSetImpl();
		ResourceSet newResourceSet = new ResourceSetImpl();

		if (!oldInvolvedModels.isEmpty()) {
			// currently all old involved models share the same resource set
			oldResourceSet = oldInvolvedModels.get(0).getObjects().get(0).eResource().getResourceSet();
		}

		if (!newInvolvedModels.isEmpty()) {
			// currently all new involved models share the same resource set
			newResourceSet = newInvolvedModels.get(0).getObjects().get(0).eResource().getResourceSet();
		}

		EcoreUtil.resolveAll(oldResourceSet);
		EcoreUtil.resolveAll(newResourceSet);

		EMFCompare comparator = EMFCompare.builder().build();

		DefaultComparisonScope scope = new DefaultComparisonScope(newResourceSet, oldResourceSet, null);
		return comparator.compare(scope);
	}

}

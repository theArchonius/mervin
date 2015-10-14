/*******************************************************************************
 * Copyright (c) 2015 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.model.modelreview.impl.extended;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;

import at.bitandart.zoubek.mervin.model.modelreview.DiagramResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl;

/**
 * An {@link ModelReview} implementation that supports the derived attributes of
 * a {@link ModelReview} based on the other attributes.
 * 
 * @author Florian Zoubek
 *
 */
public class ExtendedModelReviewImpl extends ModelReviewImpl {

	@Override
	public Comparison getSelectedDiagramComparison() {

		if (selectedDiagramComparison == null) {

			// install notifier

			this.eAdapters().add(new EContentAdapter() {
				@Override
				public void notifyChanged(Notification notification) {

					// needed to adapt also containment references
					super.notifyChanged(notification);

					int featureID = notification.getFeatureID(PatchSet.class);
					if (featureID == ModelReviewPackage.MODEL_REVIEW__LEFT_PATCH_SET
							|| featureID == ModelReviewPackage.MODEL_REVIEW__RIGHT_PATCH_SET) {

						// left or right patch set has changed -> delete
						// cache
						selectedDiagramComparison = null;

					}
				}
			});

			EList<DiagramResource> involvedModelsLeft = new BasicEList<DiagramResource>();
			EList<DiagramResource> involvedModelsRight = new BasicEList<DiagramResource>();
			if (leftPatchSet != null) {
				involvedModelsLeft = leftPatchSet.getNewInvolvedDiagrams();
				involvedModelsRight = leftPatchSet.getOldInvolvedDiagrams();
			}

			if (rightPatchSet != null) {
				involvedModelsRight = rightPatchSet.getNewInvolvedDiagrams();
				if (leftPatchSet == null) {
					involvedModelsLeft = rightPatchSet.getOldInvolvedDiagrams();
				}
			}

			selectedDiagramComparison = compareModelInstances(involvedModelsLeft, involvedModelsRight);

			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						ModelReviewPackage.MODEL_REVIEW__SELECTED_DIAGRAM_COMPARISON, null, selectedDiagramComparison));
		}

		return selectedDiagramComparison;
	}

	@Override
	public Comparison getSelectedModelComparison() {

		if (selectedModelComparison == null) {

			// install notifier

			this.eAdapters().add(new EContentAdapter() {
				@Override
				public void notifyChanged(Notification notification) {

					// needed to adapt also containment references
					super.notifyChanged(notification);

					int featureID = notification.getFeatureID(PatchSet.class);
					if (featureID == ModelReviewPackage.MODEL_REVIEW__LEFT_PATCH_SET
							|| featureID == ModelReviewPackage.MODEL_REVIEW__RIGHT_PATCH_SET) {

						// left or right patch set has changed -> delete
						// cache
						selectedModelComparison = null;

					}
				}
			});

			EList<ModelResource> involvedModelsLeft = new BasicEList<ModelResource>();
			EList<ModelResource> involvedModelsRight = new BasicEList<ModelResource>();
			if (leftPatchSet != null) {
				involvedModelsLeft = leftPatchSet.getNewInvolvedModels();
				involvedModelsRight = leftPatchSet.getOldInvolvedModels();
			}

			if (rightPatchSet != null) {
				involvedModelsRight = rightPatchSet.getNewInvolvedModels();
				if (leftPatchSet == null) {
					involvedModelsLeft = rightPatchSet.getOldInvolvedModels();
				}
			}

			selectedModelComparison = compareModelInstances(involvedModelsLeft, involvedModelsRight);

			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						ModelReviewPackage.MODEL_REVIEW__SELECTED_MODEL_COMPARISON, null, selectedModelComparison));
		}

		return selectedModelComparison;
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

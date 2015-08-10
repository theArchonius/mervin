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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;

import at.bitandart.zoubek.mervin.model.modelreview.ModelInstance;
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

			selectedDiagramComparison = compareModelInstances(leftPatchSet.getNewInvolvedDiagrams(),
					rightPatchSet.getNewInvolvedDiagrams());

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

			selectedModelComparison = compareModelInstances(leftPatchSet.getNewInvolvedModels(),
					rightPatchSet.getNewInvolvedModels());

			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						ModelReviewPackage.MODEL_REVIEW__SELECTED_MODEL_COMPARISON, null, selectedModelComparison));
		}

		return selectedModelComparison;
	}

	/**
	 * compares to sets of {@link ModelInstance}s.
	 * 
	 * @param leftInvolvedModels
	 * @param rightInvolvedModels
	 * @return the result of the comparison
	 */
	private Comparison compareModelInstances(EList<? extends ModelInstance> leftInvolvedModels,
			EList<? extends ModelInstance> rightInvolvedModels) {
		ResourceSet oldResourceSet = new ResourceSetImpl();
		ResourceSet newResourceSet = new ResourceSetImpl();

		if (!leftInvolvedModels.isEmpty()) {
			oldResourceSet = leftInvolvedModels.get(0).getObjects().get(0).eResource().getResourceSet();
		}

		if (!rightInvolvedModels.isEmpty()) {
			newResourceSet = rightInvolvedModels.get(0).getObjects().get(0).eResource().getResourceSet();
		}

		EcoreUtil.resolveAll(oldResourceSet);
		EcoreUtil.resolveAll(newResourceSet);

		EMFCompare comparator = EMFCompare.builder().build();

		DefaultComparisonScope scope = new DefaultComparisonScope(oldResourceSet, newResourceSet, null);
		return comparator.compare(scope);
	}

}

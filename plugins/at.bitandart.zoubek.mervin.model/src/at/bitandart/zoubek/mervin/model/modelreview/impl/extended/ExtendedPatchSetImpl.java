/*******************************************************************************
 * Copyright (c) 2015, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.model.modelreview.impl.extended;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.gmf.runtime.notation.Diagram;

import at.bitandart.zoubek.mervin.model.modelreview.DiagramResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl;

/**
 * An {@link PatchSet} implementation that supports the derived attributes of a
 * {@link PatchSet} based on the other attributes.
 * 
 * @author Florian Zoubek
 *
 */
public class ExtendedPatchSetImpl extends PatchSetImpl {

	@Override
	public Map<EObject, Integer> getObjectChangeCount() {

		if (objectChangeCount == null) {

			// install notifier

			this.eAdapters().add(new EContentAdapter() {
				@Override
				public void notifyChanged(Notification notification) {

					// needed to adapt also containment references
					super.notifyChanged(notification);

					int featureID = notification.getFeatureID(PatchSet.class);
					if (featureID == ModelReviewPackage.PATCH_SET__MODEL_COMPARISON
							|| featureID == ModelReviewPackage.PATCH_SET__DIAGRAM_COMPARISON) {

						// model or diagram comparison has changed -> delete
						// cache
						objectChangeCount = null;
						maxObjectChangeCount = 0;

					}
				}
			});

			// calculate change count

			objectChangeCount = new HashMap<EObject, Integer>();
			Comparison modelComparison = getModelComparison();
			Comparison diagramComparison = getDiagramComparison();

			updateChangeCount(getNewInvolvedModels(), modelComparison);
			updateChangeCount(getOldInvolvedModels(), modelComparison);

			updateChangeCount(getNewInvolvedDiagrams(), diagramComparison);
			updateChangeCount(getOldInvolvedDiagrams(), diagramComparison);

		}

		return objectChangeCount;
	}

	/**
	 * updates the change count for all objects within the given collection of
	 * {@link ModelResource}s.
	 * 
	 * @param modelInstances
	 *            the {@link ModelResource}s that contain the objects to
	 *            calculate the change count for.
	 * @param comparison
	 *            the associated {@link Comparison} for the given
	 *            {@link ModelResource}s
	 */
	private void updateChangeCount(Collection<? extends ModelResource> modelInstances, Comparison comparison) {

		for (ModelResource modelInstance : modelInstances) {

			Stack<EObject> objectsToVisit = new Stack<>();
			objectsToVisit.addAll(modelInstance.getObjects());

			while (!objectsToVisit.isEmpty()) {

				EObject element = objectsToVisit.pop();
				Match match = comparison.getMatch(element);
				int changeCount = 0;

				// calculate count

				for (Iterator<Diff> allDifferences = match.getAllDifferences().iterator(); allDifferences
						.hasNext(); allDifferences.next()) {
					changeCount++;
				}

				if (changeCount > maxObjectChangeCount) {
					maxObjectChangeCount = changeCount;
				}
				objectChangeCount.put(element, changeCount);

				// add contained objects

				objectsToVisit.addAll(element.eContents());
			}
		}

	}

	@Override
	public Map<EObject, Integer> getObjectChangeRefCount() {

		if (objectChangeRefCount == null) {

			/*
			 * TODO Check if this map can be replaced completely with
			 * ECrossReferenceAdapter or at least can be turned in a "map" that
			 * uses the adapter directly to save memory
			 */

			objectChangeRefCount = new HashMap<EObject, Integer>();

			// install notifier

			this.eAdapters().add(new EContentAdapter() {
				@Override
				public void notifyChanged(Notification notification) {

					// needed to adapt also containment references
					super.notifyChanged(notification);

					int featureID = notification.getFeatureID(PatchSet.class);
					if (featureID == ModelReviewPackage.PATCH_SET__MODEL_COMPARISON
							|| featureID == ModelReviewPackage.PATCH_SET__DIAGRAM_COMPARISON) {

						// model or diagram comparison has changed -> delete
						// cache
						objectChangeRefCount = null;
						maxObjectChangeRefCount = 0;

					}
				}
			});

			// calculate change count

			objectChangeRefCount = new HashMap<EObject, Integer>();
			Comparison modelComparison = getModelComparison();
			Comparison diagramComparison = getDiagramComparison();

			updateChangeRefCount(getNewInvolvedModels(), modelComparison);
			updateChangeRefCount(getOldInvolvedModels(), modelComparison);

			updateChangeRefCount(getNewInvolvedDiagrams(), diagramComparison);
			updateChangeRefCount(getOldInvolvedDiagrams(), diagramComparison);

		}

		return objectChangeRefCount;
	}

	/**
	 * updates the reference count for all objects within the given collection
	 * of {@link ModelResource}s.
	 * 
	 * @param modelInstances
	 *            the {@link ModelResource}s that contain the objects to
	 *            calculate the reference count for.
	 * @param comparison
	 *            the associated {@link Comparison} for the given
	 *            {@link ModelResource}s
	 */
	private void updateChangeRefCount(Collection<? extends ModelResource> modelInstances, Comparison comparison) {

		for (ModelResource modelInstance : modelInstances) {

			Stack<EObject> objectsToVisit = new Stack<>();
			objectsToVisit.addAll(modelInstance.getObjects());
			if (!objectsToVisit.isEmpty()) {

				ResourceSet resourceSet = objectsToVisit.firstElement().eResource().getResourceSet();

				ECrossReferenceAdapter crossReferenceAdapter = ECrossReferenceAdapter
						.getCrossReferenceAdapter(resourceSet);
				if (crossReferenceAdapter == null) {

					// install adapter if none exists
					crossReferenceAdapter = new ECrossReferenceAdapter();
					crossReferenceAdapter.setTarget(resourceSet);
				}

				while (!objectsToVisit.isEmpty()) {

					EObject element = objectsToVisit.pop();

					int referenceCount = 0;

					EList<EReference> ownReferences = element.eClass().getEAllReferences();
					for (EReference reference : ownReferences) {

						Object value = element.eGet(reference);
						if (value instanceof EObject) {
							Match match = comparison.getMatch((EObject) value);
							if (match != null && !match.getDifferences().isEmpty()) {
								referenceCount++;
							}
						}
					}

					/*
					 * use the cross reference adapter to determine the inverse
					 * reference change count
					 */

					Collection<Setting> references = crossReferenceAdapter.getInverseReferences(element, true);

					for (Setting setting : references) {
						Match match = comparison.getMatch(setting.getEObject());
						if (match != null && !match.getDifferences().isEmpty()) {
							referenceCount++;
						}
					}

					if (referenceCount > maxObjectChangeRefCount) {
						maxObjectChangeRefCount = referenceCount;
					}
					objectChangeRefCount.put(element, referenceCount);

					// add contained objects

					objectsToVisit.addAll(element.eContents());
				}

			}
		}

	}

	@Override
	public EList<Diagram> getAllNewInvolvedDiagrams() {
		EList<Diagram> diagrams = new BasicEList<Diagram>();
		for (DiagramResource diagramResource : getNewInvolvedDiagrams()) {
			diagrams.addAll(diagramResource.getDiagrams());
		}
		return diagrams;
	}

	@Override
	public EList<Diagram> getAllOldInvolvedDiagrams() {
		EList<Diagram> diagrams = new BasicEList<Diagram>();
		for (DiagramResource diagramResource : getOldInvolvedDiagrams()) {
			diagrams.addAll(diagramResource.getDiagrams());
		}
		return diagrams;
	}

}

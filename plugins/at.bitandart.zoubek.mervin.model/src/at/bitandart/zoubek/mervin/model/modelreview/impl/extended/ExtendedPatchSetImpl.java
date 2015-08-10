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
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;

import at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance;
import at.bitandart.zoubek.mervin.model.modelreview.ModelInstance;
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
	 * {@link ModelInstance}s.
	 * 
	 * @param modelInstances
	 *            the {@link ModelInstance}s that contain the objects to
	 *            calculate the change count for.
	 * @param comparison
	 *            the associated {@link Comparison} for the given
	 *            {@link ModelInstance}s
	 */
	private void updateChangeCount(Collection<? extends ModelInstance> modelInstances, Comparison comparison) {

		for (ModelInstance modelInstance : modelInstances) {

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
	 * of {@link ModelInstance}s.
	 * 
	 * @param modelInstances
	 *            the {@link ModelInstance}s that contain the objects to
	 *            calculate the reference count for.
	 * @param comparison
	 *            the associated {@link Comparison} for the given
	 *            {@link ModelInstance}s
	 */
	private void updateChangeRefCount(Collection<? extends ModelInstance> modelInstances, Comparison comparison) {

		for (ModelInstance modelInstance : modelInstances) {

			Stack<EObject> objectsToVisit = new Stack<>();
			objectsToVisit.addAll(modelInstance.getObjects());

			while (!objectsToVisit.isEmpty()) {

				EObject element = objectsToVisit.pop();

				// calculate references count

				Collection<Setting> references = EcoreUtil.UsageCrossReferencer.find(element,
						element.eResource().getResourceSet());

				int referenceCount = references.size();
				if (referenceCount > maxObjectChangeCount) {
					maxObjectChangeRefCount = referenceCount;
				}
				objectChangeRefCount.put(element, referenceCount);

				// add contained objects

				objectsToVisit.addAll(element.eContents());
			}
		}

	}

	@Override
	public EList<Diagram> getAllNewInvolvedDiagrams() {
		EList<Diagram> diagrams = new BasicEList<Diagram>();
		for (DiagramInstance diagramInstance : getNewInvolvedDiagrams()) {
			diagrams.addAll(diagramInstance.getDiagrams());
		}
		return diagrams;
	}

	@Override
	public EList<Diagram> getAllOldInvolvedDiagrams() {
		EList<Diagram> diagrams = new BasicEList<Diagram>();
		for (DiagramInstance diagramInstance : getOldInvolvedDiagrams()) {
			diagrams.addAll(diagramInstance.getDiagrams());
		}
		return diagrams;
	}

}

/*******************************************************************************
 * Copyright (c) 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * The default mervin implementation of {@link IModelReviewHelper}.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinModelReviewHelper implements IModelReviewHelper {

	@Override
	public Set<Diff> getDifferences(EObject eObject, ModelReview modelReview) {

		Set<Diff> differences = new HashSet<Diff>();

		EList<PatchSet> patchSets = modelReview.getPatchSets();

		for (PatchSet patchSet : patchSets) {
			differences.addAll(patchSet.getModelComparison().getDifferences(eObject));
			differences.addAll(patchSet.getDiagramComparison().getDifferences(eObject));
		}

		return differences;
	}

}

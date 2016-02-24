/*******************************************************************************
 * Copyright (c) 2015, 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.model.modelreview.impl.extended;

import at.bitandart.zoubek.mervin.model.modelreview.DiagramResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewFactoryImpl;

/**
 * The default factory for all {@link ModelReview} elements. It is recommended
 * to extend and use this class instead of {@link ModelReviewFactoryImpl}
 * because {@link ModelReviewFactoryImpl} does not provide complete
 * implementations for all {@link ModelReview} elements.
 * 
 * @author Florian Zoubek
 *
 */
public class DefaultModelReviewFactory extends ModelReviewFactoryImpl {

	@Override
	public PatchSet createPatchSet() {
		return new ExtendedPatchSetImpl();
	}

	@Override
	public DiagramResource createDiagramResource() {
		return new ExtendedDiagramResourceImpl();
	}

}

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

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewFactoryImpl;

/**
 * @author Florian Zoubek
 *
 */
public class DefaultModelReviewFactory extends ModelReviewFactoryImpl {

	@Override
	public PatchSet createPatchSet() {
		return new ExtendedPatchSetImpl();
	}
	
}

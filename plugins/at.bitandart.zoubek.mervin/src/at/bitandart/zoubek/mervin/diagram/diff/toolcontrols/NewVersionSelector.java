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
package at.bitandart.zoubek.mervin.diagram.diff.toolcontrols;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * A {@link VersionSelector} for the new version of a {@link ModelReview}.
 * 
 * @author Florian Zoubek
 *
 */
public class NewVersionSelector extends VersionSelector {

	public NewVersionSelector() {
		super("New:");
	}

	@Override
	protected Object getSelectedVersion() {
		ModelReview activeReview = getActiveReview();
		return activeReview.getRightPatchSet();
	}

}

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
package at.bitandart.zoubek.mervin.review.explorer.content;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * A temporary container for the involved models of an {@link PatchSet}.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetInvolvedModelsTreeItem implements ITreeItem {

	private PatchSet patchSet;

	public PatchSetInvolvedModelsTreeItem(PatchSet patchSet) {
		super();
		this.patchSet = patchSet;
	}

	@Override
	public boolean hasChildren() {
		return !patchSet.getNewInvolvedModels().isEmpty();
	}

	@Override
	public Object[] getChildren() {
		return patchSet.getNewInvolvedModels().toArray();
	}

	@Override
	public Object getParent() {
		return patchSet;
	}

	@Override
	public Object getElement() {
		return "Involved models";
	}
}
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
package at.bitandart.zoubek.mervin.review.explorer.content;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * A temporary container for all patches of an {@link PatchSet}.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetTreeItem implements ITreeItemContainer {

	private PatchSet patchSet;

	public PatchSetTreeItem(PatchSet patchSet) {
		super();
		this.patchSet = patchSet;
	}

	@Override
	public boolean hasChildren() {
		return !patchSet.getPatches().isEmpty();
	}

	@Override
	public Object[] getChildren() {
		return patchSet.getPatches().toArray();
	}

	@Override
	public String getText() {
		return "Patches";
	}

	@Override
	public Object getParent() {
		return patchSet.getReview();
	}
}
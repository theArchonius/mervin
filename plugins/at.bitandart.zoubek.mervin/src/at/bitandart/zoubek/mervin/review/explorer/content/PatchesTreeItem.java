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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import at.bitandart.zoubek.mervin.model.modelreview.Patch;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.review.explorer.content.TreeItemCache.ITreeItemFactory;

/**
 * A temporary container for all patches of an {@link PatchSet}.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchesTreeItem implements ITreeItem, ITreeItemFactory<Patch> {

	private PatchSet patchSet;
	private Object parent;
	private TreeItemCache<Patch> itemCache;
	private List<Object> children;

	public PatchesTreeItem(PatchSet patchSet, Object parent) {
		super();
		this.patchSet = patchSet;
		children = new LinkedList<>();
		itemCache = new TreeItemCache<Patch>(this);
	}

	@Override
	public boolean hasChildren() {
		return !patchSet.getPatches().isEmpty();
	}

	@Override
	public Object[] getChildren() {

		children.clear();
		EList<Patch> patches = patchSet.getPatches();

		itemCache.invalidateOldElements(patches);
		itemCache.addTreeItems(patches, parent, children);

		return children.toArray();
	}

	@Override
	public Object getParent() {
		return parent;
	}

	@Override
	public Object getElement() {
		return "Patches";
	}

	@Override
	public ITreeItem createTreeItem(Patch patch, Object parent) {
		return new LeafTreeItem(patch, parent);
	}
}
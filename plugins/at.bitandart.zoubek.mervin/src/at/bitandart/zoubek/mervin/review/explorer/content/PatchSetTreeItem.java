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
package at.bitandart.zoubek.mervin.review.explorer.content;

import org.eclipse.emf.compare.Comparison;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * An {@link ITreeItem} that represents a {@link PatchSet}.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetTreeItem implements ITreeItem {

	private PatchSet patchSet;
	private Object parent;
	private ComparisonTreeItem modelComparisonTreeItem;
	private ComparisonTreeItem diagramComparisonTreeItem;
	private PatchesTreeItem patchesTreeItem;

	public PatchSetTreeItem(PatchSet patchSet, Object parent) {
		this.patchSet = patchSet;
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItem#hasChildren(
	 * )
	 */
	@Override
	public boolean hasChildren() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItem#getChildren(
	 * )
	 */
	@Override
	public Object[] getChildren() {

		Comparison modelComparison = patchSet.getModelComparison();
		if (modelComparisonTreeItem == null || modelComparisonTreeItem.getComparison() != modelComparison) {
			modelComparisonTreeItem = new ComparisonTreeItem(modelComparison, "Involved Models", this);
		}

		Comparison diagramComparison = patchSet.getDiagramComparison();
		if (diagramComparisonTreeItem == null || diagramComparisonTreeItem.getComparison() != diagramComparison) {
			diagramComparisonTreeItem = new ComparisonTreeItem(diagramComparison, "Involved Diagrams", this);
		}

		if (patchesTreeItem == null) {
			patchesTreeItem = new PatchesTreeItem(patchSet, this);
		}

		return new Object[] { modelComparisonTreeItem, diagramComparisonTreeItem, patchesTreeItem };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItem#getParent()
	 */
	@Override
	public Object getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItem#getElement()
	 */
	@Override
	public Object getElement() {
		return patchSet;
	}

}

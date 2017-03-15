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

import java.text.MessageFormat;

import org.eclipse.emf.compare.Comparison;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * An {@link ITreeItem} that represents the selected comparisons of a
 * {@link ModelReview}.
 * 
 * @author Florian Zoubek
 *
 */
public class SelectedComparisonTreeItem implements ITreeItem {

	private ModelReview modelReview;
	private ComparisonTreeItem modelComparisonTreeItem;
	private ComparisonTreeItem diagramComparisonTreeItem;

	public SelectedComparisonTreeItem(ModelReview modelReview) {
		this.modelReview = modelReview;
	}

	@Override
	public boolean hasChildren() {
		return modelReview != null && modelReview.getSelectedDiagramComparison() != null
				&& modelReview.getSelectedModelComparison() != null;
	}

	@Override
	public Object[] getChildren() {

		Comparison modelComparison = modelReview.getSelectedModelComparison();
		if (modelComparisonTreeItem == null || modelComparisonTreeItem.getComparison() != modelComparison) {
			modelComparisonTreeItem = new ComparisonTreeItem(modelComparison, "Involved Models", this);
		}

		Comparison diagramComparison = modelReview.getSelectedDiagramComparison();
		if (diagramComparisonTreeItem == null || diagramComparisonTreeItem.getComparison() != diagramComparison) {
			diagramComparisonTreeItem = new ComparisonTreeItem(diagramComparison, "Involved Diagrams", this);
		}

		return new Object[] { modelComparisonTreeItem, diagramComparisonTreeItem };
	}

	@Override
	public Object getParent() {
		return null;
	}

	@Override
	public Object getElement() {

		String oldVersion = "Base Version";
		String newVersion = "Base Version";

		PatchSet newPatchSet = modelReview.getRightPatchSet();
		if (newPatchSet != null) {
			newVersion = "PatchSet #" + newPatchSet.getId();
		}

		PatchSet oldPatchSet = modelReview.getLeftPatchSet();
		if (oldPatchSet != null) {
			oldVersion = "PatchSet #" + oldPatchSet.getId();
		}

		return MessageFormat.format("[Selected] {0} <> {1}", oldVersion, newVersion);
	}

}

/*******************************************************************************
 * Copyright (c) 2015, 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.property.diff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import at.bitandart.zoubek.mervin.IDiagramModelHelper;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.OverlayType;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.property.diff.PropertyDiffItemProvider.BaseEntry;
import at.bitandart.zoubek.mervin.property.diff.PropertyDiffItemProvider.ContainerEntry;
import at.bitandart.zoubek.mervin.property.diff.PropertyDiffItemProvider.SelectionEntry;
import at.bitandart.zoubek.mervin.review.ModelReviewEditorTrackingView;
import at.bitandart.zoubek.mervin.swt.diff.tree.TreeDiff;
import at.bitandart.zoubek.mervin.swt.diff.tree.TreeDiffSide;
import at.bitandart.zoubek.mervin.swt.diff.tree.TreeDiffType;
import at.bitandart.zoubek.mervin.swt.diff.tree.TreeDiffViewer;

/**
 * A {@link ModelReviewEditorTrackingView} that shows a side-by-side tree diff
 * of the currently selected model element.
 * 
 * @author Florian Zoubek
 *
 */
public class PropertyDiffView extends ModelReviewEditorTrackingView {

	/**
	 * id of the part descriptor for this view
	 */
	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.properties.diff";

	/**
	 * helper utility class that is used to retrieve information about the
	 * diagram model.
	 */
	@Inject
	private IDiagramModelHelper diagramModelHelper;

	/**
	 * the main panel of this view that contains all controls for this view.
	 */
	private Composite mainPanel;

	/**
	 * the side-by-side tree diff viewer used to show the differences of the
	 * current selection
	 */
	private TreeDiffViewer diffViewer;

	/**
	 * indicates if the view's control have been correctly initialized
	 */
	private boolean viewInitialized = false;

	/**
	 * the {@link IOverlayTypeStyleAdvisor} to use for the colors of the
	 * property diff viewer
	 */
	@Inject
	private IOverlayTypeStyleAdvisor styleAdvisor;

	@Inject
	public PropertyDiffView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());

		diffViewer = new TreeDiffViewer(mainPanel);
		TreeDiff treeDiff = diffViewer.getTreeDiff();
		treeDiff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		diffViewer.setTreeDiffItemProvider(new PropertyDiffItemProvider());
		treeDiff.setChangedSide(TreeDiffSide.LEFT);

		/* assign the colors of the style advisor */
		treeDiff.setDiffColor(TreeDiffType.ADD, styleAdvisor.getForegroundColorForOverlayType(OverlayType.ADDITION));
		treeDiff.setDiffColor(TreeDiffType.DELETE, styleAdvisor.getForegroundColorForOverlayType(OverlayType.DELETION));
		treeDiff.setDiffColor(TreeDiffType.MODIFY,
				styleAdvisor.getForegroundColorForOverlayType(OverlayType.MODIFICATION));

		viewInitialized = true;

	}

	@Inject
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional IStructuredSelection selection) {

		if (viewInitialized && selection != null) {

			Object propertyDiffInput = createInputFromSelection(selection);
			if (propertyDiffInput != null) {

				diffViewer.setInput(propertyDiffInput);
				diffViewer.refresh();

			} else {

				/* no input for the current selection found -> clear viewer */
				diffViewer.setInput(null);
				diffViewer.refresh();

			}

		}

	}

	/**
	 * creates the input object for the tree diff viewer based on the given
	 * selection.
	 * 
	 * @param selection
	 *            the selection used to create the input object for.
	 * @return the input object for the tree diff viewer.
	 */
	private Object createInputFromSelection(IStructuredSelection selection) {

		ModelReview currentModelReview = getCurrentModelReview();
		List<ContainerEntry> modelEntries = new ArrayList<>();

		if (currentModelReview != null) {

			Comparison modelComparison = getSelectedModelComparison();
			Comparison diagramComparison = getSelectedDiagramComparison();

			if (modelComparison != null || diagramComparison != null) {
				List<BaseEntry> selectionEntries = createSelectionEntries(selection, currentModelReview,
						modelComparison, diagramComparison);
				if (!selectionEntries.isEmpty()) {
					modelEntries.add(new ContainerEntry(null, "Selected Comparison (new)", "Selected Comparison (old)",
							selectionEntries));
				}
			}

			EList<PatchSet> patchSets = currentModelReview.getPatchSets();
			for (PatchSet patchSet : patchSets) {
				List<BaseEntry> selectionEntries = createSelectionEntries(selection, currentModelReview,
						patchSet.getModelComparison(), patchSet.getDiagramComparison());
				if (!selectionEntries.isEmpty()) {
					modelEntries.add(new ContainerEntry(null, "Patch Set " + patchSet.getId() + " (new)",
							"Base Version (old)", selectionEntries));
				}
			}
		}

		return modelEntries;
	}

	/**
	 * creates {@link SelectionEntry}s for the given selection and comparisons.
	 * 
	 * @param selection
	 *            the selection to obtain the elements to display from.
	 * @param currentModelReview
	 *            the {@link ModelReview} instance used to obtain the views for
	 *            the elements to display.
	 * @param modelComparison
	 *            the model {@link Comparison} to display the elements for.
	 * @param diagramComparison
	 *            the diagram {@link Comparison} to display the elements for.
	 * @return a {@link List} of all created selection entries, never null.
	 */
	private List<BaseEntry> createSelectionEntries(IStructuredSelection selection, ModelReview currentModelReview,
			Comparison modelComparison, Comparison diagramComparison) {
		List<BaseEntry> modelEntries = new ArrayList<>();
		Iterator<?> selectionIterator = selection.iterator();
		int selectionIndex = 1;
		while (selectionIterator.hasNext()) {

			Object object = selectionIterator.next();
			EObject selectedSemanticModelElement = diagramModelHelper.getSemanticModel(object);
			View selectedNotationModelElement = diagramModelHelper.getNotationModel(object);

			if (currentModelReview != null) {

				View originalNotationModelElement = (View) currentModelReview.getUnifiedModelMap()
						.getOriginal(selectedNotationModelElement);

				if (originalNotationModelElement != null) {
					selectedNotationModelElement = originalNotationModelElement;
				}
			}

			Match semanticModelMatch = null;
			if (modelComparison != null) {
				semanticModelMatch = modelComparison.getMatch(selectedSemanticModelElement);
			}

			Match notationModelMatch = null;
			if (diagramComparison != null) {
				notationModelMatch = diagramComparison.getMatch(selectedNotationModelElement);
			}

			/*
			 * create an entry only if the selection contains a model element
			 * contained in the comparison
			 */
			if (semanticModelMatch != null || notationModelMatch != null) {
				SelectionEntry modelEntry = new SelectionEntry(null, selectionIndex + ". ", semanticModelMatch,
						notationModelMatch);
				modelEntries.add(modelEntry);

				// TODO add other (referencing, context, etc...) notation
				// models

				selectionIndex++;
			}

		}
		return modelEntries;
	}

	/**
	 * @return the diagram comparison to use for this view.
	 */
	private Comparison getSelectedDiagramComparison() {

		ModelReview currentModelReview = getCurrentModelReview();
		if (currentModelReview != null) {
			return currentModelReview.getSelectedDiagramComparison();
		}

		return null;
	}

	/**
	 * @return the model comparison to use for this view.
	 */
	private Comparison getSelectedModelComparison() {

		ModelReview currentModelReview = getCurrentModelReview();
		if (currentModelReview != null) {
			return currentModelReview.getSelectedModelComparison();
		}

		return null;
	}

	@Override
	protected void updateValues() {

		if (viewInitialized) {

			ModelReview modelReview = getCurrentModelReview();

			if (modelReview == null) {

				/* no active model review -> clear diff viewer if necessary */
				if (diffViewer != null && diffViewer.getInput() != null) {

					diffViewer.setInput(null);
					diffViewer.refresh();
				}

			}
		}

	}

}
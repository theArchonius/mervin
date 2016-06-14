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
package at.bitandart.zoubek.mervin.property.diff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
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
import at.bitandart.zoubek.mervin.draw2d.figures.ChangeType;
import at.bitandart.zoubek.mervin.draw2d.figures.IChangeTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
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
	 * the {@link IChangeTypeStyleAdvisor} to use for the colors of the property
	 * diff viewer
	 */
	@Inject
	private IChangeTypeStyleAdvisor styleAdvisor;

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
		treeDiff.setDiffColor(TreeDiffType.ADD, styleAdvisor.getForegroundColorForChangeType(ChangeType.ADDITION));
		treeDiff.setDiffColor(TreeDiffType.DELETE, styleAdvisor.getForegroundColorForChangeType(ChangeType.DELETION));
		treeDiff.setDiffColor(TreeDiffType.MODIFY,
				styleAdvisor.getForegroundColorForChangeType(ChangeType.MODIFICATION));

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

		Comparison comparison = getComparison(selection);

		List<SelectionEntry> modelEntries = new ArrayList<>(selection.size());

		Iterator<?> selectionIterator = selection.iterator();
		int selectionIndex = 1;
		while (selectionIterator.hasNext()) {

			Object object = selectionIterator.next();
			EObject selectedSemanticModelElement = diagramModelHelper.getSemanticModel(object);
			View selectedNotationModelElement = diagramModelHelper.getNotationModel(object);

			Match semanticModelMatch = comparison.getMatch(selectedSemanticModelElement);
			Match notationModelMatch = comparison.getMatch(selectedNotationModelElement);

			/*
			 * create an entry only if the selection contains a model element
			 * contained in the comparison
			 */
			if (semanticModelMatch != null || notationModelMatch != null) {
				SelectionEntry modelEntry = new SelectionEntry("#" + selectionIndex, semanticModelMatch,
						notationModelMatch);
				modelEntries.add(modelEntry);

				// TODO add other (referencing, context, etc...) notation models

				selectionIndex++;
			}

		}

		return modelEntries;
	}

	/**
	 * returns the comparison associated with the given selection.
	 * 
	 * @param selection
	 *            the selection to get the comparison for.
	 * @return the comparison.
	 */
	private Comparison getComparison(IStructuredSelection selection) {

		ModelReview currentModelReview = getCurrentModelReview();
		if (currentModelReview != null) {
			return currentModelReview.getSelectedDiagramComparison();
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
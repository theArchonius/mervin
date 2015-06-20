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
package at.bitandart.zoubek.mervin.diagram.diff;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * A View that visualizes the difference between a set of diagram elements
 * within two patch sets of a {@link ModelReview} instance. The
 * {@link ModelReview} instance must be provided in the transient data map (
 * {@link MPart#getTransientData()}) of the part with the key
 * {@link #DATA_TRANSIENT_MODEL_REVIEW}. The {@link ModelReview} instance can be
 * obtained or modified by other views using the transient data map or using
 * {@link #getModelReview()} and {@link #setModelReview(ModelReview)}.
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramDiffView {

	private Composite mainPanel;

	@Inject
	private ESelectionService selectionService;
	@Inject
	private MPart part;

	/**
	 * the default part descriptor id associated with this view
	 */
	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.diagram.diffviewer";

	/**
	 * key of the associated model review in the transient data map of the
	 * corresponding {@link MPart}.
	 */
	public static final String DATA_TRANSIENT_MODEL_REVIEW = "transient-review";

	@Inject
	public DiagramDiffView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());

		// TODO replace with the diagram diff component
		Label nYILabel = new Label(mainPanel, SWT.NONE);
		nYILabel.setText("Not yet implemented");
		nYILabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	@PreDestroy
	public void preDestroy() {

	}

	@Focus
	public void onFocus() {
		mainPanel.setFocus();
		selectionService.setSelection(getModelReview());
	}

	/**
	 * sets the currently used model review instance
	 * 
	 * @param modelReview
	 *            the model review instance to set
	 */
	public void setModelReview(ModelReview modelReview) {
		part.getTransientData().put(DATA_TRANSIENT_MODEL_REVIEW, modelReview);
		selectionService.setSelection(getModelReview());
	}

	/**
	 * @return the currently used model review instance
	 */
	public ModelReview getModelReview() {

		// the review model is stored in the transient data map
		if (part.getTransientData().containsKey(DATA_TRANSIENT_MODEL_REVIEW)) {
			Object object = part.getTransientData().get(
					DATA_TRANSIENT_MODEL_REVIEW);

			if (object instanceof ModelReview) {
				return (ModelReview) object;
			}
		}

		return null;
	}

}
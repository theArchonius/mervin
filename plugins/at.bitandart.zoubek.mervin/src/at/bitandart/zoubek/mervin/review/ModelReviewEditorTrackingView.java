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
package at.bitandart.zoubek.mervin.review;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import at.bitandart.zoubek.mervin.diagram.diff.DiagramDiffView;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * Abstract base class for views that track editors that provide a
 * {@link ModelReview} instance via their transient data map and the key
 * {@link DiagramDiffView#DATA_TRANSIENT_MODEL_REVIEW}.
 * 
 * @author Florian Zoubek
 *
 */
public abstract class ModelReviewEditorTrackingView {

	private MPart activeModelReviewPart;

	/**
	 * updates all controls with the values of the current model review
	 */
	protected abstract void updateValues();

	public ModelReviewEditorTrackingView() {
		super();
	}

	@Inject
	public void setActiveModelReviewPart(@Named(IServiceConstants.ACTIVE_PART) @Optional MPart part,
			EModelService modelService, EPartService partService, MWindow window) {

		if (part != null && part.getTags().contains("View")) {

			// new active part is a view - the active model review part might be
			// left open so do not reset it unless it has been closed

			if (activeModelReviewPart != null && !activeModelReviewPart.isToBeRendered()) {

				// part has been closed
				activeModelReviewPart = null;
				updateValues();

			}

		} else if (part != null && part.getTransientData().containsKey(DiagramDiffView.DATA_TRANSIENT_MODEL_REVIEW)) {
			if (!part.equals(activeModelReviewPart)) {
				// part contains a model review
				activeModelReviewPart = part;
				updateValues();
			}

		} else {

			// part contains no model review
			activeModelReviewPart = null;
			updateValues();

		}

	}

	/**
	 * @return the current model review that should be shown in this view
	 */
	protected ModelReview getCurrentModelReview() {

		// obtain the model review from the last active part that contains a
		// model review
		if (activeModelReviewPart != null
				&& activeModelReviewPart.getTransientData().containsKey(DiagramDiffView.DATA_TRANSIENT_MODEL_REVIEW)) {

			Object object = activeModelReviewPart.getTransientData().get(DiagramDiffView.DATA_TRANSIENT_MODEL_REVIEW);

			if (object instanceof ModelReview) {
				return (ModelReview) object;
			}
		}

		return null;
	}

	public MPart getActiveModelReviewPart() {
		return activeModelReviewPart;
	}

}
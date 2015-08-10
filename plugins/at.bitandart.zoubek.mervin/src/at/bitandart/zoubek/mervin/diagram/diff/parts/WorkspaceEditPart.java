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
package at.bitandart.zoubek.mervin.diagram.diff.parts;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.notation.Diagram;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * An {@link EditPart} that provides a workspace with multiple child elements
 * that support stacking and minimizing.
 * 
 * @author Florian Zoubek
 *
 */
public class WorkspaceEditPart extends AbstractGraphicalEditPart {

	private final Adapter reviewUpdateAdapter = new EContentAdapter() {
		@Override
		public void notifyChanged(Notification notification) {

			// needed to adapt also containment references
			super.notifyChanged(notification);

			int featureID = notification.getFeatureID(PatchSet.class);
			if (featureID == ModelReviewPackage.MODEL_REVIEW__LEFT_PATCH_SET
					|| featureID == ModelReviewPackage.MODEL_REVIEW__RIGHT_PATCH_SET) {

				// left or right patch set has changed -> refresh
				refresh();

			}
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		FreeformLayer panel = new FreeformLayer();
		panel.setLayoutManager(new FreeformLayout());
		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy() {

			@Override
			protected Command getCreateCommand(CreateRequest request) {
				return new Command() {
				};
			}
		});
	}

	@Override
	protected List<Diagram> getModelChildren() {
		List<Diagram> diagrams = Collections.emptyList();
		ModelReview modelReview = getModelReview();
		if (modelReview != null) {
			PatchSet rightPatchSet = modelReview.getRightPatchSet();
			if (rightPatchSet != null) {
				diagrams = rightPatchSet.getAllNewInvolvedDiagrams();
			}
		}
		return diagrams;
	}

	@Override
	public void setModel(Object model) {
		if (model instanceof ModelReview) {
			// remove old adapter
			((ModelReview) model).eAdapters().remove(reviewUpdateAdapter);
		}

		super.setModel(model);

		if (model instanceof ModelReview) {
			// add update adapter
			((ModelReview) model).eAdapters().add(reviewUpdateAdapter);
		}
	}

	public ModelReview getModelReview() {
		Object model = getModel();
		if (model instanceof ModelReview) {
			return (ModelReview) model;
		}
		return null;

	}

}

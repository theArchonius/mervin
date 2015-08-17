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

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;

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
public class WorkspaceEditPart extends GraphicalEditPart {

	public WorkspaceEditPart(EObject model) {
		super(model);

	}

	private Adapter reviewUpdateAdapter;

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

	@Override
	public void setModel(Object model) {
		Object oldModel = getModel();
		if (oldModel != null) {
			if (model instanceof ModelReview) {
				// remove old adapter
				((ModelReview) model).eAdapters().remove(getReviewUpdateAdapter());
			} else if (model instanceof View) {
				((View) oldModel).getElement().eAdapters().remove(getReviewUpdateAdapter());
			}
		}

		super.setModel(model);

		if (model instanceof ModelReview) {
			// add update adapter
			((ModelReview) model).eAdapters().add(getReviewUpdateAdapter());
		} else if (model instanceof View) {
			resolveSemanticElement().eAdapters().add(getReviewUpdateAdapter());
		}
	}

	private Adapter getReviewUpdateAdapter() {

		if (reviewUpdateAdapter == null) {
			reviewUpdateAdapter = new EContentAdapter() {
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
		}
		return reviewUpdateAdapter;
	}

	public ModelReview getModelReview() {
		EObject model = resolveSemanticElement();
		if (model instanceof ModelReview) {
			return (ModelReview) model;
		}
		return null;

	}

}

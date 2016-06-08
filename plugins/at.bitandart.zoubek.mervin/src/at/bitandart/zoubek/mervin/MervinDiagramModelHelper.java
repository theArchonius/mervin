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
package at.bitandart.zoubek.mervin;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;

import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;

/**
 * A {@link IDiagramModelHelper} that supports GMF, GEF and Mervin specific
 * objects (e.g. linked views of {@link DifferenceOverlay}s).
 * 
 * @author Florian Zoubek
 *
 */
public class MervinDiagramModelHelper implements IDiagramModelHelper {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.ISemanticModelHelper#getSemanticModel(java.
	 * lang.Object)
	 */
	@Override
	public EObject getSemanticModel(Object object) {

		if (object instanceof GraphicalEditPart) {

			// GMF edit part -> use semantic model
			EObject semanticElement = ((GraphicalEditPart) object).resolveSemanticElement();
			return getSemanticModel(semanticElement);

		} else if (object instanceof ConnectionEditPart) {

			// GMF edit part -> use semantic model
			EObject semanticElement = ((ConnectionEditPart) object).resolveSemanticElement();
			return getSemanticModel(semanticElement);

		} else if (object instanceof EditPart) {

			// GEF edit part -> use model if it is an EObject
			Object model = ((EditPart) object).getModel();
			if (model instanceof EObject) {
				return getSemanticModel((EObject) model);
			}

		} else if (object instanceof View) {

			// GMF View
			return getSemanticModel(((View) object).getElement());

		} else if (object instanceof DifferenceOverlay) {

			// difference overlay
			View linkedView = ((DifferenceOverlay) object).getLinkedView();
			EObject semanticElement = linkedView.getElement();
			return getSemanticModel(semanticElement);

		} else if (object instanceof EObject) {

			// assume that EObjects are semantic model elements, so return it
			return (EObject) object;
		}

		return null;
	}

	@Override
	public View getNotationModel(Object object) {

		if (object instanceof EditPart) {

			// GEF edit part -> use model
			return getNotationModel(((EditPart) object).getModel());

		} else if (object instanceof View) {

			View view = (View) object;
			EObject element = view.getElement();
			if (element instanceof DifferenceOverlay) {
				return ((DifferenceOverlay) element).getLinkedView();
			}
			/* the passed object is already a non overlay view, so return it */
			return (View) object;

		}

		return null;
	}

}

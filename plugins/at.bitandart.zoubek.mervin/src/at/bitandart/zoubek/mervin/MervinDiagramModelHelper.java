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
package at.bitandart.zoubek.mervin;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.NotationPackage;
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

	@Override
	public Set<View> getReferencingViews(EObject object) {

		EObject eObject = (EObject) object;
		Resource resource = ((EObject) object).eResource();
		if (resource != null) {
			ResourceSet resourceSet = resource.getResourceSet();
			if (resourceSet != null) {

				Set<View> views = new HashSet<View>();

				ECrossReferenceAdapter crossReferenceAdapter = ECrossReferenceAdapter
						.getCrossReferenceAdapter(resourceSet);
				if (crossReferenceAdapter == null) {

					// install adapter if none exists
					crossReferenceAdapter = new ECrossReferenceAdapter();
					crossReferenceAdapter.setTarget(resourceSet);
				}
				Collection<Setting> inverseReferences = crossReferenceAdapter.getInverseReferences(eObject);
				for (Setting setting : inverseReferences) {
					if (setting.getEStructuralFeature() == NotationPackage.Literals.VIEW__ELEMENT) {
						views.add((View) setting.getEObject());
					}
				}
				return views;
			}
		}
		return Collections.emptySet();
	}

}

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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

/**
 * An {@link EditPart} that provides a view on a diagram.
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramEditPart extends ShapeCompartmentEditPart {

	public DiagramEditPart(View model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		DiagramContainerFigure figure = new DiagramContainerFigure(getCompartmentName(), getMapMode());
		figure.getContentPane().addLayoutListener(LayoutAnimator.getDefault());
		figure.setBorder(null);
		return figure;
	}

	@Override
	protected void refreshVisuals() {

		IFigure figure = getFigure();
		// TODO Replace when workspace layout is implemented
		figure.getParent().setConstraint(figure, new Rectangle(10, 10, 800, 300));
		super.refreshVisuals();
	}

	@Override
	public String getCompartmentName() {
		EObject element = getNotationView().getElement();
		if (element instanceof Diagram) {
			return ((Diagram) element).getName();
		}
		return super.getCompartmentName();
	}

	@Override
	protected void setRatio(Double ratio) {
		// ratio is not supported by parent figures with XYLayout layout
		// managers
	}
}

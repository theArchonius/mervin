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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;

/**
 * An {@link EditPart} that provides a view on a diagram.
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramEditPart extends GraphicalEditPart {

	public DiagramEditPart(EObject model) {
		super(model);
	}

	private IFigure contentPane;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		IFigure rectFigure = new RectangleFigure();
		ToolbarLayout fillLayout = new ToolbarLayout();
		fillLayout.setStretchMinorAxis(true);
		rectFigure.setLayoutManager(fillLayout);
		contentPane = new ScrollPane();
		contentPane.setLayoutManager(new XYLayout());
		rectFigure.add(contentPane);
		return rectFigure;
	}

	@Override
	protected void refreshVisuals() {

		IFigure figure = getFigure();
		// TODO Replace when workspace layout is implemented
		figure.getParent().setConstraint(figure, new Rectangle(10, 10, 1000, 2000));
		super.refreshVisuals();
	}

	@Override
	public IFigure getContentPane() {
		return contentPane;
	}
}

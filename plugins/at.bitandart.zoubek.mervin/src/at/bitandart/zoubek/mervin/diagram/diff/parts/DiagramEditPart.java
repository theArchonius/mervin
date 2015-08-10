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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;

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
		figure.getParent().setConstraint(figure, new Rectangle(10, 10, 3000, 3000));
		super.refreshVisuals();
	}

	@Override
	public IFigure getContentPane() {
		return contentPane;
	}

	/**
	 * 
	 * @return the diagram model assigned to this edit part or null if the model
	 *         instance is not an instance of {@link Diagram}
	 */
	public Diagram getDiagram() {
		Object model = getModel();
		if (model instanceof Diagram) {
			return (Diagram) model;
		}
		return null;
	}

	@Override
	protected List<?> getModelChildren() {
		Diagram diagram = getDiagram();
		if (diagram != null) {
			/*
			 * TODO return only context children for now, return the child views
			 */
			return diagram.getVisibleChildren();
		}
		return Collections.EMPTY_LIST;
	}
}

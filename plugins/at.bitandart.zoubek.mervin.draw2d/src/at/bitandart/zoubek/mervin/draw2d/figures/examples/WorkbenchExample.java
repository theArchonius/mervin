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
package at.bitandart.zoubek.mervin.draw2d.figures.examples;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;

import at.bitandart.zoubek.mervin.draw2d.ViewportFillingLayout;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultChangeTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.DiagramContainerFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.DiffWorkbench;

/**
 * Demonstrates the usage of {@link DiffWorkbench}.
 * 
 * @author Florian Zoubek
 *
 */
public class WorkbenchExample extends GMFExample {

	public static void main(String[] args) {
		new WorkbenchExample().run();
	}

	@Override
	protected void addChildFigures(IFigure parentFigure) {

		Layer primaryLayer = getPrimaryLayer();
		// the workbench should fill up the main viewport
		primaryLayer.setLayoutManager(new ViewportFillingLayout());

		// create the workbench
		DiffWorkbench diffWorkbench = new DiffWorkbench(getMapMode());
		primaryLayer.add(diffWorkbench);

		// add first workbench child
		diffWorkbench.getContentArea().add(createFirstWorkbenchChild());

		// add second workbench child
		diffWorkbench.getContentArea().add(createSecondWorkbenchChild());

	}

	private IFigure createFirstWorkbenchChild() {
		IFigure figure = createFirstWorkbenchChild();

		RectangleFigure rectangleFigure = new RectangleFigure();
		figure.add(rectangleFigure, new Rectangle(-getCanvasWidth(), -getCanvasHeight(), 100, 50));
		figure.add(rectangleFigure, new Rectangle(getCanvasWidth() * 2, -getCanvasHeight(), 100, 50));
		figure.add(rectangleFigure, new Rectangle(-getCanvasWidth(), getCanvasHeight() * 2, 100, 50));
		figure.add(rectangleFigure, new Rectangle(getCanvasWidth() * 2, getCanvasHeight() * 2, 100, 50));

		return figure;
	}

	private IFigure createSecondWorkbenchChild() {
		IFigure figure = createFirstWorkbenchChild();

		RectangleFigure rectangleFigure = new RectangleFigure();
		figure.add(rectangleFigure, new Rectangle((int) (getCanvasWidth() / 2.0), -getCanvasHeight(), 100, 50));
		figure.add(rectangleFigure, new Rectangle(-getCanvasWidth(), (int) (getCanvasHeight() / 2.0), 100, 50));
		figure.add(rectangleFigure, new Rectangle((int) (getCanvasWidth() / 2.0), getCanvasHeight() * 2, 100, 50));
		figure.add(rectangleFigure, new Rectangle(getCanvasWidth() * 2, (int) (getCanvasHeight() / 2.0), 100, 50));

		return figure;
	}

	private IFigure createWorkbenchChild() {
		DiagramContainerFigure figure = new DiagramContainerFigure("", new DefaultChangeTypeStyleAdvisor(),
				getMapMode());
		figure.getContentPane().setLayoutManager(new FreeFormLayoutEx());
		figure.getContentPane().addLayoutListener(LayoutAnimator.getDefault());
		return figure;
	}

}

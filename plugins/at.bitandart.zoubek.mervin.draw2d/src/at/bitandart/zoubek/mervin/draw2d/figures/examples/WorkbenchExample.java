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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;
import org.eclipse.swt.graphics.Image;

import at.bitandart.zoubek.mervin.draw2d.MervinResourceRegistry;
import at.bitandart.zoubek.mervin.draw2d.RegistryResourceManager;
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

		RegistryResourceManager registryResourceManager = getRegistryResourceManager();

		Image workbenchMaximizeImage = registryResourceManager
				.getImage(MervinResourceRegistry.IMAGE_WORKBENCH_MAXIMIZE);
		Image workbenchMinimizeImage = registryResourceManager
				.getImage(MervinResourceRegistry.IMAGE_WORKBENCH_MINIMIZE);
		Image workbenchWindowModeImage = registryResourceManager
				.getImage(MervinResourceRegistry.IMAGE_WORKBENCH_WINDOW_MODE);
		Image workbenchTabModeImage = registryResourceManager.getImage(MervinResourceRegistry.IMAGE_WORKBENCH_TAB_MODE);

		Layer primaryLayer = getPrimaryLayer();
		// the workbench should fill up the main viewport
		primaryLayer.setLayoutManager(new ViewportFillingLayout());

		// create the workbench
		DiffWorkbench diffWorkbench = new DiffWorkbench(getMapMode(), workbenchWindowModeImage, workbenchTabModeImage,
				workbenchMaximizeImage, workbenchMinimizeImage);
		primaryLayer.add(diffWorkbench, new Rectangle(0, 0, getCanvasWidth(), getCanvasHeight()));

		// add first workbench child
		IFigure firstChild = createFirstWorkbenchChild();
		diffWorkbench.getContentArea().add(firstChild, new Rectangle(-10, 20, 200, 100));

		// add second workbench child
		IFigure secondChild = createSecondWorkbenchChild();
		diffWorkbench.getContentArea().add(secondChild, new Rectangle(400, -40, 300, 150));

	}

	private IFigure createFirstWorkbenchChild() {
		DiagramContainerFigure figure = createWorkbenchChild("First Window");
		// the first child also has a toolbar with some child figures
		figure.getToolbarArea().add(new Label("Toolbar item 1"));
		figure.getToolbarArea().add(new Label("Toolbar item 2"));
		IFigure contentPane = figure.getContentPane();

		RectangleFigure rectangleFigure = new RectangleFigure();
		contentPane.add(rectangleFigure, new Rectangle(-getCanvasWidth(), -getCanvasHeight(), 100, 50));

		rectangleFigure = new RectangleFigure();
		contentPane.add(rectangleFigure, new Rectangle(getCanvasWidth() * 2, -getCanvasHeight(), 100, 50));

		rectangleFigure = new RectangleFigure();
		contentPane.add(rectangleFigure, new Rectangle(-getCanvasWidth(), getCanvasHeight() * 2, 100, 50));

		rectangleFigure = new RectangleFigure();
		contentPane.add(rectangleFigure, new Rectangle(getCanvasWidth() * 2, getCanvasHeight() * 2, 100, 50));

		return figure;
	}

	private IFigure createSecondWorkbenchChild() {
		DiagramContainerFigure figure = createWorkbenchChild("Second Window");
		IFigure contentPane = figure.getContentPane();

		RectangleFigure rectangleFigure = new RectangleFigure();
		contentPane.add(rectangleFigure, new Rectangle((int) (getCanvasWidth() / 2.0), -getCanvasHeight(), 100, 50));

		rectangleFigure = new RectangleFigure();
		contentPane.add(rectangleFigure, new Rectangle(-getCanvasWidth(), (int) (getCanvasHeight() / 2.0), 100, 50));

		rectangleFigure = new RectangleFigure();
		contentPane.add(rectangleFigure, new Rectangle((int) (getCanvasWidth() / 2.0), getCanvasHeight() * 2, 100, 50));

		rectangleFigure = new RectangleFigure();
		contentPane.add(rectangleFigure, new Rectangle(getCanvasWidth() * 2, (int) (getCanvasHeight() / 2.0), 100, 50));

		return figure;
	}

	private DiagramContainerFigure createWorkbenchChild(String title) {
		DiagramContainerFigure figure = new DiagramContainerFigure(title, new DefaultChangeTypeStyleAdvisor(),
				getMapMode());
		figure.setBackgroundColor(ColorConstants.white);
		figure.setOpaque(true);
		figure.getContentPane().setLayoutManager(new FreeFormLayoutEx());
		figure.getContentPane().addLayoutListener(LayoutAnimator.getDefault());
		return figure;
	}

}

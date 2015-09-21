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
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import at.bitandart.zoubek.mervin.draw2d.figures.ComposedClickable;
import at.bitandart.zoubek.mervin.draw2d.figures.ComposedFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.ComposedNodeFigure;

/**
 * Demonstrates the usage of {@link ComposedFigure}, {@link ComposedNodeFigure},
 * and {@link ComposedClickable}.
 * 
 * @author Florian Zoubek
 *
 */
public class ComposedFiguresExample extends BaseExample {

	public static void main(String[] args) {
		new ComposedFiguresExample().run();
	}

	@Override
	protected void addChildFigures(IFigure parentFigure) {

		ContainerWithTitle containerWithTitle = new ContainerWithTitle();
		NodeContainerWithTitle nodeContainerWithTitle = new NodeContainerWithTitle();
		ClickableContainerWithTitle clickableContainerWithTitle = new ClickableContainerWithTitle();

		// composed are initialized on demand
		assert(containerWithTitle.isInitialized() == false);
		assert(nodeContainerWithTitle.isInitialized() == false);
		assert(clickableContainerWithTitle.isInitialized() == false);

		// initialization triggers (for all IComposedFigures)

		// explictly
		nodeContainerWithTitle.initialize();
		assert(nodeContainerWithTitle.isInitialized() == true);

		// getChildren()
		clickableContainerWithTitle.getChildren();
		assert(clickableContainerWithTitle.isInitialized() == true);

		// adding the figure to a figure hierarchy
		parentFigure.add(containerWithTitle, new Rectangle(10, 10, 100, 100));
		assert(containerWithTitle.isInitialized() == true);

		// now add the other figures

		parentFigure.add(nodeContainerWithTitle, new Rectangle(120, 10, 100, 100));
		parentFigure.add(clickableContainerWithTitle, new Rectangle(10, 120, 210, 100));

	}

	private class ContainerWithTitle extends ComposedFigure {

		@Override
		protected void initializeFigure() {
			setLayoutManager(new ToolbarLayout());
		}

		@Override
		protected void initializeChildren() {

			Label titleLabel = new Label("Container Title");
			add(titleLabel);

			IFigure contentFigure = new RectangleFigure();
			contentFigure.setLayoutManager(new ToolbarLayout());
			add(contentFigure);

			Label child1 = new Label("Child 1");
			contentFigure.add(child1);

			Label child2 = new Label("Child 2");
			contentFigure.add(child2);

		}

	}

	private class NodeContainerWithTitle extends ComposedFigure {

		@Override
		protected void initializeFigure() {
			setLayoutManager(new ToolbarLayout());
		}

		@Override
		protected void initializeChildren() {

			Label titleLabel = new Label("Container Title");
			add(titleLabel);

			IFigure contentFigure = new RectangleFigure();
			contentFigure.setLayoutManager(new ToolbarLayout());
			add(contentFigure);

			Label child1 = new Label("Child 1");
			contentFigure.add(child1);

			Label child2 = new Label("Child 2");
			contentFigure.add(child2);

		}

	}

	private class ClickableContainerWithTitle extends ComposedClickable {

		@Override
		protected void initializeFigure() {
			setLayoutManager(new ToolbarLayout());
		}

		@Override
		protected void initializeChildren() {

			Label titleLabel = new Label("Container Title");
			add(titleLabel);

			IFigure contentFigure = new RectangleFigure();
			contentFigure.setLayoutManager(new ToolbarLayout());
			add(contentFigure);

			Label child1 = new Label("Child 1");
			contentFigure.add(child1);

			Label child2 = new Label("Child 2");
			contentFigure.add(child2);

		}

	}

}

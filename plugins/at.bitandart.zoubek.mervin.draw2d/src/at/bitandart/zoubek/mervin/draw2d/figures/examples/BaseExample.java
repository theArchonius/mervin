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

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Base class for all mervin draw2d examples. Provides a simple {@link Shell}
 * with a {@link FigureCanvas} and methods to customize its content.
 * 
 * @author Florian Zoubek
 *
 */
public class BaseExample {

	/**
	 * the root figure
	 */
	private IFigure rootFigure;

	/**
	 * the {@link Display} used by this example.
	 */
	private Display d;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new BaseExample().run();
	}

	/**
	 * runs the example.
	 */
	public void run() {
		d = new Display();
		Shell shell = new Shell(d);
		RowLayout shellLayout = new RowLayout(SWT.VERTICAL);
		shellLayout.center = true;
		shell.setLayout(shellLayout);

		// Canvas for draw2d figures

		FigureCanvas canvas = new FigureCanvas(shell);
		RowData canvasLayoutData = new RowData();
		canvasLayoutData.width = getCanvasWidth();
		canvasLayoutData.height = getCanvasWidth();
		canvas.setLayoutData(canvasLayoutData);

		// Draw 2d figures

		rootFigure = createRootFigure();

		addChildFigures(rootFigure);

		canvas.setContents(rootFigure);

		// setup shell

		shell.setText(getTitle());
		shell.pack();
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();
	}

	/**
	 * creates the root figure for the {@link FigureCanvas}. Subclasses may
	 * override.
	 * 
	 * @return the root figure
	 */
	protected IFigure createRootFigure() {
		Panel container = new Panel();
		container.setBackgroundColor(d.getSystemColor(SWT.COLOR_WHITE));
		XYLayout containerLayout = new XYLayout();

		// Assign layout listener for animation because we need to record the
		// layout state changes
		container.addLayoutListener(LayoutAnimator.getDefault());
		container.setLayoutManager(containerLayout);
		return container;
	}

	/**
	 * adds the child figures to the given parent figure. Does nothing by
	 * default, subclasses may override.
	 * 
	 * @param parentFigure
	 */
	protected void addChildFigures(IFigure parentFigure) {
		// Intentionally left empty
	}

	/**
	 * @return the minimum canvas width
	 */
	protected int getCanvasWidth() {
		return 500;
	}

	/**
	 * @return the minimum canvas height
	 */
	protected int getCanvasHeight() {
		return 500;
	}

	/**
	 * @return the title of this example
	 */
	protected String getTitle() {
		return "Mervin draw2d example";
	}

	public IFigure getRootFigure() {
		return rootFigure;
	}

}

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
package at.bitandart.zoubek.mervin.diagram.diff.figures.workbench;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ScrollPaneLayout;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.tooling.runtime.linklf.LinkLFShapeCompartmentEditPart;

/**
 * An {@link IDiffWorkbenchContainer} implementation that shows the contents of
 * a single diagram.
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramContainerFigure extends LinkLFShapeCompartmentEditPart.ShapeCompartmentFigureEx
		implements IDiffWorkbenchContainer {

	IDiffWorkbenchTrayFigure trayFigure;

	IDiffWorkbenchWindowTitleFigure windowTitleFigure;

	public DiagramContainerFigure(String compartmentTitle, IMapMode mm) {
		super(compartmentTitle, mm);
		remove(getTextPane());
		ScrollPane scrollPane = getScrollPane();
		scrollPane.setLayoutManager(new ScrollPaneLayout());
		setBorder(new LineBorder(ColorConstants.lightGray, 2));
	}

	@Override
	public String getWindowTitle() {
		return getCompartmentTitle();
	}

	@Override
	public IFigure getStatusArea() {
		// TODO implement status area
		return null;
	}

	@Override
	public IFigure getToolbarArea() {
		// TODO implement toolbar area
		return null;
	}

	@Override
	public IFigure getContentArea() {
		return getContentPane();
	}

	@Override
	public IDiffWorkbenchTrayFigure getTrayFigure() {
		if (trayFigure == null) {
			trayFigure = createTrayFigure();
		}
		return trayFigure;
	}

	/**
	 * Creates the associated {@link IDiffWorkbenchTrayFigure} for this figure.
	 */
	protected IDiffWorkbenchTrayFigure createTrayFigure() {
		return new TitleTrayFigure(this);
	}

	@Override
	public IDiffWorkbenchWindowTitleFigure getWindowTitleFigure() {
		if (windowTitleFigure == null) {
			windowTitleFigure = createWindowTitleFigure();
		}
		return windowTitleFigure;
	}

	/**
	 * Creates the associated {@link IDiffWorkbenchWindowTitleFigure} for this
	 * figure.
	 */
	protected IDiffWorkbenchWindowTitleFigure createWindowTitleFigure() {
		return new LabelWindowTitle(this);
	}

}

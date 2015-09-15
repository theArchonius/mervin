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
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;

import at.bitandart.zoubek.mervin.diagram.diff.figures.ComposedClickable;

/**
 * An {@link IDiffWorkbenchTrayFigure} implementation that shows the containers
 * title in a label.
 * 
 * @author Florian Zoubek
 *
 */
public class TitleTrayFigure extends ComposedClickable implements IDiffWorkbenchTrayFigure {

	private IDiffWorkbenchContainer container;

	private boolean active = false;

	private Label titleLabel;

	private IFigure contentPane;

	/**
	 * @param container
	 *            the associated {@link IDiffWorkbenchContainer}
	 */
	public TitleTrayFigure(IDiffWorkbenchContainer container) {
		super();
		this.container = container;
	}

	@Override
	protected void initializeFigure() {

		Insets borderInsets = new Insets();
		int horizontalSpacing = getDefaultHorizontalSpacing();
		borderInsets.left = borderInsets.right = horizontalSpacing;
		borderInsets.top = borderInsets.bottom = getDefaultVerticalSpacing();
		setBorder(new MarginBorder(borderInsets));

		ConstrainedToolbarLayout constrainedToolbarLayout = new ConstrainedToolbarLayout(true);
		constrainedToolbarLayout.setSpacing(horizontalSpacing);
		setLayoutManager(constrainedToolbarLayout);
		setOpaque(true);

	}

	@Override
	protected void initializeChildren() {
		titleLabel = createTitleLabel();
		add(titleLabel);

		contentPane = createContentPane();
		add(contentPane);
	}

	/**
	 * creates the content pane of this figure. This method should only be
	 * called once which is done by default in {@link #initializeChildren()}.
	 * 
	 * @return the content pane figure
	 */
	protected IFigure createContentPane() {
		Figure contentPaneFigure = new Figure();
		ConstrainedToolbarLayout constrainedToolbarLayout = new ConstrainedToolbarLayout(true);
		constrainedToolbarLayout.setSpacing(getDefaultHorizontalSpacing());
		contentPaneFigure.setLayoutManager(constrainedToolbarLayout);
		return contentPaneFigure;
	}

	/**
	 * Returns the default horizontal spacing used to separate elements in this
	 * figure. Subclasses may override.
	 */
	protected int getDefaultHorizontalSpacing() {
		return getTextUtil().getAscent(getFont());
	}

	/**
	 * Returns the default vertical spacing used to separate elements in this
	 * figure. Subclasses may override.
	 */
	protected int getDefaultVerticalSpacing() {
		return (int) (getDefaultHorizontalSpacing() * 0.618);
	}

	@Override
	protected void notifyInitializationComplete() {
		super.notifyInitializationComplete();
		updateStateColors();
	}

	/**
	 * creates the title label of this figure. This method should only be called
	 * once which is done by default in {@link #initializeChildren()}.
	 * 
	 * @return the title label figure
	 */
	protected Label createTitleLabel() {
		return new Label(container.getWindowTitle());
	}

	/**
	 * updates the color based on the current state.
	 */
	protected void updateStateColors() {
		if (isActive()) {
			setBackgroundColor(ColorConstants.white);
			setForegroundColor(ColorConstants.black);
		} else {
			setBackgroundColor(ColorConstants.gray);
			setForegroundColor(ColorConstants.lightGray);
		}
	}

	/**
	 * @return utilities for text metrics.
	 */
	protected TextUtilities getTextUtil() {
		return TextUtilities.INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.diagram.diff.figures.IDiffWorkbenchTrayFigure#
	 * getContainer()
	 */
	@Override
	public IDiffWorkbenchContainer getContainer() {
		return container;
	}

	@Override
	public void setActive(boolean value) {
		this.active = value;
		updateStateColors();
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public IFigure getContentPane() {
		if (!isInitialized()) {
			initialize();
		}
		return contentPane;
	}

}

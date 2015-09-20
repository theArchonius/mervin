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
package at.bitandart.zoubek.mervin.draw2d.figures.workbench;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;

import at.bitandart.zoubek.mervin.draw2d.figures.ComposedClickable;

/**
 * An {@link IDiffWorkbenchWindowTitleFigure} implementation that show the
 * container title in a label.
 * 
 * @author Florian Zoubek
 *
 */
public class LabelWindowTitle extends ComposedClickable implements IDiffWorkbenchWindowTitleFigure {

	private IDiffWorkbenchContainer container;

	private Label titleLabel;

	private IFigure contentPane;

	/**
	 * @param container
	 *            the associated {@link IDiffWorkbenchContainer}
	 */
	public LabelWindowTitle(IDiffWorkbenchContainer container) {
		this.container = container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.diagram.diff.figures.ComposedClickable#
	 * initializeFigure()
	 */
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
		setBackgroundColor(ColorConstants.lightGray);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.diagram.diff.figures.ComposedClickable#
	 * initializeChildren()
	 */
	@Override
	protected void initializeChildren() {
		titleLabel = createTitleLabel();
		add(titleLabel);

		contentPane = createContentPane();
		add(contentPane);
	}

	/**
	 * creates the title label for this figure. Subclasses may override.
	 */
	protected Label createTitleLabel() {
		return new Label(container.getWindowTitle());
	}

	/**
	 * creates the content pane figure. Subclasses may override.
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
	public IFigure getContentPane() {
		if (!isInitialized()) {
			initialize();
		}
		return contentPane;
	}

	/**
	 * @return utilities for text metrics.
	 */
	protected TextUtilities getTextUtil() {
		return TextUtilities.INSTANCE;
	}

	@Override
	public IDiffWorkbenchContainer getContainer() {
		return container;
	}

}

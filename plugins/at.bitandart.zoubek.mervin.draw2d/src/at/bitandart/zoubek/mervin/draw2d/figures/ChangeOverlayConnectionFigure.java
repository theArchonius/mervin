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
package at.bitandart.zoubek.mervin.draw2d.figures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import at.bitandart.zoubek.mervin.draw2d.figures.ChangeOverlayNodeFigure.ChangeType;

/**
 * An {@link IOverlayFigure} that is able to outline one or more connection
 * figures. An {@link IChangeTypeStyleAdvisor} can be used to control the colors
 * for this figure.
 * 
 * @author Florian Zoubek
 *
 */
public class ChangeOverlayConnectionFigure extends ComposedNodeFigure implements IOverlayFigure {

	private IChangeTypeStyleAdvisor styleAdvisor;

	// child figures
	private List<ConnectionChangeOutline> outlines = new ArrayList<ConnectionChangeOutline>();

	// change properties
	private ChangeType changeType = ChangeType.ADDITION;
	private boolean showCommentHint = false;

	/**
	 * @param styleAdivsor
	 *            the style advisor used to obtain the styles for the different
	 *            change types. Null values are not permitted.
	 * @param changeType
	 *            the change type that this figure represents
	 * @throws IllegalArgumentException
	 *             if null is passed as a style advisor
	 */
	public ChangeOverlayConnectionFigure(IChangeTypeStyleAdvisor styleAdvisor, ChangeType changeType) {
		if (styleAdvisor == null) {
			throw new IllegalArgumentException("A valid style advisor must be specified");
		}
		this.styleAdvisor = styleAdvisor;
		this.changeType = changeType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.draw2d.figures.IOverlayFigure#
	 * updateBoundsfromLinkedFigures(java.util.Collection)
	 */
	@Override
	public void updateFigure(Collection<IFigure> linkedFigures) {

		// make sure the figure is properly initialized
		if (!isInitialized()) {
			initialize();
		}

		// update outlines

		ListIterator<ConnectionChangeOutline> outlineIterator = outlines.listIterator();
		for (IFigure figure : linkedFigures) {

			if (figure instanceof Connection) {

				Connection connection = (Connection) figure;
				ConnectionChangeOutline outline;

				// reuse existing outlines if possible
				if (outlineIterator.hasNext()) {
					outline = outlineIterator.next();
				} else {
					outline = new ConnectionChangeOutline(styleAdvisor.getCommentColorForChangeType(changeType));
					outline.setShowCommentHint(showCommentHint);
					outline.setAlpha(128);
					outline.setLineWidthFloat(2f);
					outlineIterator.add(outline);
					add(outline);
				}

				outline.setPointsToCover(connection.getPoints());
			}

		}

		// remove unnecessary outlines
		while (outlineIterator.hasNext()) {
			outlineIterator.next();
			outlineIterator.remove();
		}

		applyChangeStyles();
	}

	@Override
	public Rectangle getBounds() {
		if (!isInitialized()) {
			initialize();
		}

		Rectangle bounds = null;

		for (Object child : getChildren()) {
			if (child instanceof IFigure) {
				if (bounds == null) {
					bounds = ((IFigure) child).getBounds().getCopy();
				} else {
					bounds.union(((IFigure) child).getBounds());
				}
			}
		}
		if (bounds == null) {
			bounds = new Rectangle();
		}
		bounds.expand(getInsets());

		return bounds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.draw2d.figures.ComposedNodeFigure#
	 * initializeFigure()
	 */
	@Override
	protected void initializeFigure() {
		setLayoutManager(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.draw2d.figures.ComposedNodeFigure#
	 * initializeChildren()
	 */
	@Override
	protected void initializeChildren() {
		// intentionally left empty
	}

	/**
	 * applies the styles to the child figures based on the current state
	 */
	private void applyChangeStyles() {

		Color foregroundColor = styleAdvisor.getForegroundColorForChangeType(changeType);
		Color backgroundColor = styleAdvisor.getBackgroundColorForChangeType(changeType);

		for (ConnectionChangeOutline outline : outlines) {
			outline.setBackgroundColor(backgroundColor);
			outline.setForegroundColor(foregroundColor);

			outline.setFill(changeType != ChangeType.LAYOUT);
		}
	}

	/**
	 * updates the outline properties based on the current state
	 */
	protected void updateOutline() {
		if (isInitialized()) {
			for (ConnectionChangeOutline outline : outlines) {
				outline.setShowCommentHint(showCommentHint);
			}
		}
	}

	/**
	 * @param changeType
	 *            the change type which this overlay represents.
	 */
	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
		applyChangeStyles();
	}

	/**
	 * @param showCommentsHint
	 *            true if a comment hint should be shown, false otherwise
	 */
	public void setShowCommentHint(boolean showCommentsHint) {
		this.showCommentHint = showCommentsHint;
		updateOutline();
	}

	/**
	 * @return the change type which this overlay represents.
	 */
	public ChangeType getChangeType() {
		return changeType;
	}

}

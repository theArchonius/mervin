/*******************************************************************************
 * Copyright (c) 2015, 2017 Florian Zoubek.
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
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * An {@link IOverlayFigure} that is able to outline one or more connection
 * figures. An {@link IOverlayTypeStyleAdvisor} can be used to control the
 * colors for this figure.
 * 
 * @author Florian Zoubek
 *
 */
public class OverlayConnectionFigure extends ComposedNodeFigure implements IOverlayFigure {

	private IOverlayTypeStyleAdvisor styleAdvisor;

	// child figures
	private List<ConnectionOutline> outlines = new ArrayList<ConnectionOutline>();

	// change properties
	private OverlayType overlayType = OverlayType.ADDITION;
	private boolean showCommentHint = false;

	/**
	 * @param styleAdivsor
	 *            the style advisor used to obtain the styles for the different
	 *            overlay types. Null values are not permitted.
	 * @param overlayType
	 *            the overlay type that this figure represents
	 * @throws IllegalArgumentException
	 *             if null is passed as a style advisor
	 */
	public OverlayConnectionFigure(IOverlayTypeStyleAdvisor styleAdvisor, OverlayType overlayType) {
		if (styleAdvisor == null) {
			throw new IllegalArgumentException("A valid style advisor must be specified");
		}
		this.styleAdvisor = styleAdvisor;
		this.overlayType = overlayType;
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

		ListIterator<ConnectionOutline> outlineIterator = outlines.listIterator();
		for (IFigure figure : linkedFigures) {

			if (figure instanceof Connection) {

				Connection connection = (Connection) figure;
				ConnectionOutline outline;

				// reuse existing outlines if possible
				if (outlineIterator.hasNext()) {
					outline = outlineIterator.next();
				} else {
					outline = new ConnectionOutline(styleAdvisor.getCommentColorForOverlayType(overlayType));
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

	@Override
	public boolean containsPoint(int x, int y) {

		/*
		 * only the individual outlines can contain points - otherwise the
		 * bounds of this figure are used for this test and this figure may
		 * "cover" other figures that are not covered by the outlines
		 */
		for (ConnectionOutline outline : outlines) {
			if (outline.containsPoint(x, y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * applies the styles to the child figures based on the current state
	 */
	private void applyChangeStyles() {

		Color foregroundColor = styleAdvisor.getForegroundColorForOverlayType(overlayType);
		Color backgroundColor = styleAdvisor.getBackgroundColorForOverlayType(overlayType);

		for (ConnectionOutline outline : outlines) {
			outline.setBackgroundColor(backgroundColor);
			outline.setForegroundColor(foregroundColor);

			outline.setFill(!(overlayType == OverlayType.LAYOUT || overlayType == OverlayType.COMMENT));
			outline.setOutline(overlayType != OverlayType.COMMENT);
		}
	}

	/**
	 * updates the outline properties based on the current state
	 */
	protected void updateOutline() {
		if (isInitialized()) {
			for (ConnectionOutline outline : outlines) {
				outline.setShowCommentHint(showCommentHint);
			}
		}
	}

	/**
	 * @param overlayType
	 *            the overlay type which this overlay represents.
	 */
	public void setOverlayType(OverlayType overlayType) {
		this.overlayType = overlayType;
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
	 * @return the overlay type which this overlay represents.
	 */
	public OverlayType getOverlayType() {
		return overlayType;
	}

	/**
	 * @return an unmodifiable list of the current connection outline of this
	 *         figure.
	 */
	public Collection<ConnectionOutline> getCurrentOutlines() {
		return Collections.unmodifiableCollection(outlines);
	}

}

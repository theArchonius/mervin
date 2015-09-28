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

import java.util.Collection;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import at.bitandart.zoubek.mervin.draw2d.figures.DimensionPropertyChangeIndicator.DimensionChange;
import at.bitandart.zoubek.mervin.draw2d.figures.DimensionPropertyChangeIndicator.DimensionProperty;

/**
 * An {@link IOverlayFigure} with a rectangular outline and indicators for
 * layout changes. A {@link IChangeTypeStyleAdvisor} can be used to control the
 * colors for this figure.
 * 
 * @author Florian Zoubek
 *
 */
public class ChangeOverlayNodeFigure extends ComposedNodeFigure implements IOverlayFigure {

	/**
	 * The change types supported by {@link ChangeOverlayNodeFigure}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public enum ChangeType {
		ADDITION, DELETION, MODIFICATION, LAYOUT
	}

	/**
	 * The diemnsion property change types supported by
	 * {@link ChangeOverlayNodeFigure}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public enum DimensionPropertyChangeType {
		NONE, SMALLER, BIGGER
	}

	// child figures

	private RectangularChangeOutline outline;
	private DirectionIndicator directionIndicator;
	private DimensionPropertyChangeIndicator boundsWidthSizeChangeIndicator;
	private DimensionPropertyChangeIndicator boundsHeightSizeChangeIndicator;

	// layout properties

	private Insets outlineInsets = new Insets(3);
	private Dimension defaultDirectionIndicatorSize = new Dimension(35, 35);
	private Dimension defaultBoundsWidthSizeChangeIndicatorSize = new Dimension(50, 20);
	private Dimension defaultBoundsHeightChangeIndicatorSize = new Dimension(20, 50);

	private IChangeTypeStyleAdvisor styleAdivsor;

	// change properties

	private Vector moveDirection = null;
	private ChangeType changeType = ChangeType.ADDITION;
	private boolean showCommentHint = false;
	private DimensionPropertyChangeType boundsWidthChangeType = DimensionPropertyChangeType.NONE;
	private DimensionPropertyChangeType boundsHeightChangeType = DimensionPropertyChangeType.NONE;

	/**
	 * @param styleAdivsor
	 *            the style advisor used to obtain the styles for the different
	 *            change types. Null values are not permitted.
	 * @param changeType
	 *            the change type that this figure represents
	 * @throws IllegalArgumentException
	 *             if null is passed as a style advisor
	 */
	public ChangeOverlayNodeFigure(IChangeTypeStyleAdvisor styleAdivsor, ChangeType changeType)
			throws IllegalArgumentException {
		if (styleAdivsor == null) {
			throw new IllegalArgumentException("A valid style advisor must be specified");
		}
		this.styleAdivsor = styleAdivsor;
		this.changeType = changeType;
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

		outline = new RectangularChangeOutline(styleAdivsor.getCommentColorForChangeType(changeType));
		outline.setAlpha(128);
		outline.setLineWidthFloat(2f);
		outline.setBounds(new Rectangle(0, 0, 20, 20));
		add(outline);
		if (moveDirection != null) {
			directionIndicator = new DirectionIndicator(moveDirection);
		} else {
			directionIndicator = new DirectionIndicator(new Vector(0, 1));
			directionIndicator.setVisible(false);
		}
		directionIndicator.setOpaque(true);
		directionIndicator.setBorder(new LineBorder(styleAdivsor.getForegroundColorForChangeType(changeType), 5));
		directionIndicator.setBounds(Rectangle.SINGLETON.setBounds(20, 0, defaultDirectionIndicatorSize.width,
				defaultDirectionIndicatorSize.height));
		add(directionIndicator);

		boundsWidthSizeChangeIndicator = new DimensionPropertyChangeIndicator(DimensionProperty.WIDTH,
				DimensionChange.BIGGER);
		boundsWidthSizeChangeIndicator.setOpaque(true);
		boundsWidthSizeChangeIndicator
				.setBorder(new LineBorder(styleAdivsor.getForegroundColorForChangeType(changeType), 5));
		add(boundsWidthSizeChangeIndicator);

		boundsHeightSizeChangeIndicator = new DimensionPropertyChangeIndicator(DimensionProperty.HEIGHT,
				DimensionChange.BIGGER);
		boundsHeightSizeChangeIndicator.setOpaque(true);
		boundsHeightSizeChangeIndicator
				.setBorder(new LineBorder(styleAdivsor.getForegroundColorForChangeType(changeType), 5));
		add(boundsHeightSizeChangeIndicator);

	}

	@Override
	protected void notifyInitializationComplete() {
		applyChangeStyles();
		updateDimensionPropertyChangeIndicators();
		updateOutline();
	}

	/**
	 * applies the styles to the child figures based on the current state
	 */
	private void applyChangeStyles() {

		Color foregroundColor = styleAdivsor.getForegroundColorForChangeType(changeType);
		Color backgroundColor = styleAdivsor.getBackgroundColorForChangeType(changeType);

		outline.setForegroundColor(foregroundColor);
		outline.setBackgroundColor(backgroundColor);
		outline.setFill(changeType != ChangeType.LAYOUT);

		directionIndicator.setBackgroundColor(foregroundColor);
		directionIndicator.setForegroundColor(styleAdivsor.getIndicatorColorForChangeType(changeType));
		Border indicatorBorder = directionIndicator.getBorder();
		if (indicatorBorder instanceof LineBorder) {
			((LineBorder) indicatorBorder).setColor(foregroundColor);
		}

		boundsHeightSizeChangeIndicator.setBackgroundColor(foregroundColor);
		boundsHeightSizeChangeIndicator.setForegroundColor(styleAdivsor.getIndicatorColorForChangeType(changeType));
		indicatorBorder = boundsHeightSizeChangeIndicator.getBorder();
		if (indicatorBorder instanceof LineBorder) {
			((LineBorder) indicatorBorder).setColor(foregroundColor);
		}

		boundsWidthSizeChangeIndicator.setBackgroundColor(foregroundColor);
		boundsWidthSizeChangeIndicator.setForegroundColor(styleAdivsor.getIndicatorColorForChangeType(changeType));
		indicatorBorder = boundsWidthSizeChangeIndicator.getBorder();
		if (indicatorBorder instanceof LineBorder) {
			((LineBorder) indicatorBorder).setColor(foregroundColor);
		}

	}

	@Override
	public void updateFigure(Collection<IFigure> linkedFigures) {

		Rectangle areaToCover = null;

		for (IFigure figure : linkedFigures) {
			Rectangle linkedFigureBounds = getLocalBounds(figure);

			if (areaToCover == null) {
				areaToCover = Rectangle.SINGLETON.setBounds(linkedFigureBounds);
			} else {
				areaToCover.union(linkedFigureBounds);
			}
		}

		// add insets and padding caused by the the line width
		areaToCover.expand(outlineInsets);
		double lineWidthSpacing = outline.getLineWidth() * 2.0;
		areaToCover.expand(lineWidthSpacing, lineWidthSpacing);

		Rectangle outlineBounds = areaToCover.getCopy();

		Rectangle dirIndicatorBounds = new Rectangle(outlineBounds.getTopRight(), defaultDirectionIndicatorSize);

		Rectangle horizontalSideChangeIndicatorBounds = new Rectangle(
				outlineBounds.getBottom().translate((int) defaultBoundsWidthSizeChangeIndicatorSize.width / -2.0, 0),
				defaultBoundsWidthSizeChangeIndicatorSize);

		Rectangle verticalSideChangeIndicatorBounds = new Rectangle();
		verticalSideChangeIndicatorBounds.setSize(defaultBoundsHeightChangeIndicatorSize);
		if (outlineBounds.height < defaultBoundsHeightChangeIndicatorSize.height + defaultDirectionIndicatorSize.height
				&& moveDirection != null) {

			// not enough space to display the side change indicator on the same
			// same side as the direction indicator -> use the other side
			Point left = outlineBounds.getLeft();
			verticalSideChangeIndicatorBounds.setLocation(left.x - defaultBoundsHeightChangeIndicatorSize.width,
					(int) (left.y - defaultBoundsHeightChangeIndicatorSize.height / 2.0));

		} else if (outlineBounds.height < 2
				* (defaultBoundsHeightChangeIndicatorSize.height / 2.0 + defaultDirectionIndicatorSize.height)
				&& moveDirection != null) {

			// not enough space to center the side change indicator on the right
			// side -> add it under the direction indicator
			verticalSideChangeIndicatorBounds.setLocation(dirIndicatorBounds.getBottomLeft());

		} else {

			// center it on the right side
			Point right = outlineBounds.getRight();
			verticalSideChangeIndicatorBounds.setLocation(right.x,
					(int) (right.y - defaultBoundsHeightChangeIndicatorSize.height / 2.0));

		}

		// obtain the main figure bounds
		areaToCover.union(dirIndicatorBounds);
		areaToCover.union(horizontalSideChangeIndicatorBounds);
		areaToCover.union(verticalSideChangeIndicatorBounds);

		/*
		 * update the bounds beginning with the parent to avoid potential child
		 * translation caused by setBounds();
		 */
		setBounds(areaToCover);
		outline.setBounds(outlineBounds);
		directionIndicator.setBounds(dirIndicatorBounds);
		boundsHeightSizeChangeIndicator.setBounds(verticalSideChangeIndicatorBounds);
		boundsWidthSizeChangeIndicator.setBounds(horizontalSideChangeIndicatorBounds);

	}

	/**
	 * updates all {@link DimensionPropertyChangeIndicator}s of this figure
	 * based on the current state
	 */
	protected void updateDimensionPropertyChangeIndicators() {

		updateDimensionPropertyChangeIndicator(boundsHeightSizeChangeIndicator, boundsHeightChangeType);
		updateDimensionPropertyChangeIndicator(boundsWidthSizeChangeIndicator, boundsWidthChangeType);

	}

	/**
	 * updates the outline properties based on the current state
	 */
	protected void updateOutline() {

		if (isInitialized()) {
			outline.setShowCommentHint(showCommentHint);
		}

	}

	/**
	 * updates the given indicator based on the given change type.
	 * 
	 * @param indicator
	 *            the indicator to update
	 * @param changeType
	 *            the changeType used to update the indicator.
	 */
	protected void updateDimensionPropertyChangeIndicator(DimensionPropertyChangeIndicator indicator,
			DimensionPropertyChangeType changeType) {

		if (isInitialized()) {

			switch (changeType) {
			case NONE:
				indicator.setVisible(false);
				break;
			case BIGGER:
				indicator.setVisible(true);
				indicator.setDimensionChange(DimensionChange.BIGGER);
				break;
			case SMALLER:
				indicator.setVisible(true);
				indicator.setDimensionChange(DimensionChange.SMALLER);
				break;
			}
		}
	}

	/**
	 * @param figure
	 * @return the bounds of the given figure relative to this figure's bounds
	 */
	private Rectangle getLocalBounds(IFigure figure) {

		Rectangle figureBounds = figure.getBounds().getCopy();
		figure.translateToAbsolute(figureBounds);
		translateToRelative(figureBounds);
		return figureBounds;

	}

	/**
	 * sets the move indicator direction.
	 * 
	 * @param moveDirection
	 *            the direction vector of the move direction or null if no move
	 *            should be shown
	 */
	public void setMoveDirection(Vector moveDirection) {
		if (!isInitialized()) {
			initialize();
		}
		this.moveDirection = moveDirection;
		if (moveDirection != null) {
			directionIndicator.setVisible(true);
			directionIndicator.setDirection(moveDirection);
		} else {
			directionIndicator.setVisible(false);
		}
		applyChangeStyles();
	}

	/**
	 * @return the direction of the move indicator or null if no direction is
	 *         set.
	 */
	public Vector getMoveDirection() {
		return moveDirection;
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
	 * @param boundsWidthChangeType
	 *            the change type to display for the width of the bounds
	 */
	public void setBoundsWidthChangeType(DimensionPropertyChangeType boundsWidthChangeType) {
		this.boundsWidthChangeType = boundsWidthChangeType;
		updateDimensionPropertyChangeIndicators();
	}

	/**
	 * 
	 * @param boundsHeightChangeType
	 *            the change type to display for the height of the bounds
	 */
	public void setBoundsHeightChangeType(DimensionPropertyChangeType boundsHeightChangeType) {
		this.boundsHeightChangeType = boundsHeightChangeType;
		updateDimensionPropertyChangeIndicators();
	}

	/**
	 * @return the change type which this overlay represents.
	 */
	public ChangeType getChangeType() {
		return changeType;
	}

	/**
	 * Base interface for style advisors used to obtain a style for a given
	 * {@link ChangeType}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public static interface IChangeTypeStyleAdvisor {

		public Color getForegroundColorForChangeType(ChangeType changeType);

		public Color getBackgroundColorForChangeType(ChangeType changeType);

		public Color getIndicatorColorForChangeType(ChangeType changeType);

		public Color getCommentColorForChangeType(ChangeType changeType);

	}

	/**
	 * The default style advisor for changes.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public static class DefaultChangeTypeStyleAdvisor implements IChangeTypeStyleAdvisor {

		private Color additionForegroundColor;
		private Color deletionForegroundColor;
		private Color modificationForegroundColor;
		private Color layoutForegroundColor;
		private Color defaultForegroundColor;

		private Color additionBackgroundColor;
		private Color deletionBackgroundColor;
		private Color modificationBackgroundColor;
		private Color layoutBackgroundColor;
		private Color defaultBackgroundColor;

		private Color commentsColor;

		public DefaultChangeTypeStyleAdvisor() {

			additionForegroundColor = new Color(Display.getDefault(), 82, 145, 102);
			additionBackgroundColor = new Color(Display.getDefault(), 82, 145, 102);

			deletionForegroundColor = new Color(Display.getDefault(), 153, 153, 153);
			deletionBackgroundColor = new Color(Display.getDefault(), 153, 153, 153);

			modificationForegroundColor = new Color(Display.getDefault(), 31, 120, 180);
			modificationBackgroundColor = new Color(Display.getDefault(), 31, 120, 180);

			layoutForegroundColor = new Color(Display.getDefault(), 178, 223, 138);
			layoutBackgroundColor = new Color(Display.getDefault(), 178, 223, 138);

			commentsColor = new Color(Display.getDefault(), 247, 177, 0);

			defaultForegroundColor = ColorConstants.black;
			defaultBackgroundColor = ColorConstants.white;

		}

		public void dispose() {

			additionForegroundColor.dispose();
			additionBackgroundColor.dispose();

			deletionForegroundColor.dispose();
			deletionBackgroundColor.dispose();

			modificationForegroundColor.dispose();
			modificationBackgroundColor.dispose();
			commentsColor.dispose();
		}

		@Override
		public Color getForegroundColorForChangeType(ChangeType changeType) {

			switch (changeType) {
			case ADDITION:
				return additionForegroundColor;
			case DELETION:
				return deletionForegroundColor;
			case MODIFICATION:
				return modificationForegroundColor;
			case LAYOUT:
				return layoutForegroundColor;
			}
			return defaultForegroundColor;

		}

		@Override
		public Color getBackgroundColorForChangeType(ChangeType changeType) {

			switch (changeType) {
			case ADDITION:
				return additionBackgroundColor;
			case DELETION:
				return deletionBackgroundColor;
			case MODIFICATION:
				return modificationBackgroundColor;
			case LAYOUT:
				return layoutBackgroundColor;
			}
			return defaultBackgroundColor;

		}

		@Override
		public Color getCommentColorForChangeType(ChangeType changeType) {
			return commentsColor;
		}

		@Override
		public Color getIndicatorColorForChangeType(ChangeType changeType) {
			return ColorConstants.black;
		}

	}

	/**
	 * @param showCommentsHint
	 *            true if a comment hint should be shown, false otherwise
	 */
	public void setShowCommentHint(boolean showCommentsHint) {
		this.showCommentHint = showCommentsHint;
		updateOutline();
	}
}

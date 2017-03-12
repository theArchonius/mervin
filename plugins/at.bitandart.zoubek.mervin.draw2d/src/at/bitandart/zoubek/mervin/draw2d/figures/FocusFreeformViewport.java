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
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformFigure;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * A {@link FreeformViewport} that supports focusing on a set of descendant
 * figures. If the focus mode is enabled, the viewport blends out all
 * non-focused IFigures by drawing a semitransparent layer on top of them with a
 * given color. Please note that the children are drawn only once if the focus
 * mode is disabled. If the focus mode is enabled, they are drawn multiple
 * times: Once for the background, then once for each focused figure.
 * 
 * @author Florian Zoubek
 *
 */
public class FocusFreeformViewport extends FreeformViewport {

	/**
	 * the list of figures which are focused on.
	 */
	private List<IFigure> focusedFigures;

	/**
	 * indicates that the focus mode is enabled.
	 */
	private boolean focusEnabled;

	/**
	 * the color of the blend layer.
	 */
	private Color blendLayerColor;

	/**
	 * the alpha value used to draw the blend layer. Range: 0 (transparent) -
	 * 255 (opaque).
	 */
	private int blendLayerAlpha;

	/**
	 * the width of the outline, the outline will be considered as disabled if
	 * this value is less or equal 0.0.
	 */
	private float outlineWidth;

	/**
	 * determines if scrolling should be enabled or not. Note that the scrollbar
	 * visibility should be set to {@link FigureCanvas#NEVER} if this viewport
	 * is used by a {@link FigureCanvas} and scrolling is disabled.
	 */
	private boolean scrollingEnabled = true;

	/**
	 * creates a new {@link FocusFreeformViewport} with the given values and
	 * disabled focus mode.
	 * 
	 * @param blendLayerColor
	 *            the color of the blend layer. Do not use colors with an alpha
	 *            channel to specify the alpha value for the blend layer -
	 *            specify it with the {@source blendLayerAlpha} parameter
	 *            instead.
	 * @param blendLayerAlpha
	 *            the alpha value used to draw the blend layer. Range: 0
	 *            (transparent) - 255 (opaque).
	 * @param outlineWidth
	 *            the width of the outline around each focused figure. The
	 *            outline will be considered as disabled if this value is less
	 *            or equal 0.0.
	 * @param scrollingEnabled
	 *            determines if scrolling should be enabled or not. Note that
	 *            the scrollbar visibility should be set to
	 *            {@link FigureCanvas#NEVER} if this viewport is used by a
	 *            {@link FigureCanvas} and scrolling is disabled.
	 */
	public FocusFreeformViewport(Color blendLayerColor, int blendLayerAlpha, float outlineWidth,
			boolean scrollingEnabled) {

		focusedFigures = new ArrayList<IFigure>();
		focusEnabled = false;
		this.blendLayerColor = blendLayerColor;
		this.blendLayerAlpha = blendLayerAlpha;
		this.outlineWidth = outlineWidth;
		this.scrollingEnabled = scrollingEnabled;

	}

	@Override
	protected void paintClientArea(Graphics graphics) {

		super.paintClientArea(graphics);

		if (focusEnabled) {

			// draw blend layer
			graphics.setAlpha(blendLayerAlpha);
			graphics.setBackgroundColor(blendLayerColor);
			Rectangle bounds = getBounds().getCopy();
			graphics.fillRectangle(bounds);
			graphics.restoreState();

			Point viewLocation = getViewLocation();

			/*
			 * redraw each focused figure
			 */
			for (IFigure focusedFigure : focusedFigures) {

				// redraw client area but clip to the focused figure
				Rectangle focusedBounds = getVisibleBounds(focusedFigure);
				this.translateToRelative(focusedBounds);
				this.translateFromParent(focusedBounds);
				focusedBounds.translate(-viewLocation.x, -viewLocation.y);

				if (outlineWidth > 0.0) {
					graphics.setAlpha(Math.min(blendLayerAlpha + 10, 255));
					graphics.setForegroundColor(blendLayerColor);
					graphics.setLineWidthFloat(outlineWidth);
					graphics.drawRectangle(focusedBounds.getCopy().expand(outlineWidth / 2, outlineWidth / 2));
					graphics.restoreState();
				}

				graphics.setClip(focusedBounds);

				super.paintClientArea(graphics);
				graphics.restoreState();

			}

		}

	}

	@Override
	protected void readjustScrollBars() {

		if (scrollingEnabled) {
			super.readjustScrollBars();
		} else {
			/*
			 * crop the freeform bounds to the client area to prevent
			 * "automatic" scrolling (e.g. using {@link
			 * EditPartViewer#reveal(EditPart)}) out of the client area
			 */
			if (getContents() == null)
				return;
			if (!(getContents() instanceof FreeformFigure))
				return;
			FreeformFigure ff = (FreeformFigure) getContents();
			Rectangle clientArea = getClientArea();
			Rectangle bounds = ff.getFreeformExtent().getCopy();
			bounds.intersect(new Rectangle(0, 0, clientArea.width, clientArea.height));
			ff.setFreeformBounds(bounds);

			getVerticalRangeModel().setAll(bounds.y, clientArea.height, bounds.bottom());
			getHorizontalRangeModel().setAll(bounds.x, clientArea.width, bounds.right());
		}
	}

	/**
	 * determines the actual visible bounds of the given figure based on their
	 * ancestors. This method does not take figures into account that are not
	 * ancestors of the given figure. So the bounds of such a figure will
	 * <strong>not</strong> be taken into account even if it occludes parts the
	 * given figure.
	 * 
	 * @param figure
	 * @return the visible absolute bounds of the figure
	 */
	private Rectangle getVisibleBounds(IFigure figure) {

		IFigure child = figure;
		Rectangle visibleBounds = child.getBounds().getCopy();
		visibleBounds.expand(10, 10);
		if (!child.isVisible()) {
			visibleBounds.setBounds(0, 0, 0, 0);
		}
		IFigure parent = figure.getParent();
		while (!visibleBounds.isEmpty() && parent != null) {

			/*
			 * Now intersect with the bounds of the parent figure -
			 * visibleBounds is in the coordinate system of the child's parent,
			 * but the bounds are in the coordinate system of the parent's
			 * parent. To safely intersect them, a translation to that
			 * coordinate system is necessary.
			 */
			parent.translateToParent(visibleBounds);
			visibleBounds.intersect(parent.getBounds());

			// move on to the next parent
			child = parent;
			parent = parent.getParent();
		}

		/*
		 * visibleBounds is in the coordinate system of the child's parent, but
		 * the absolute coordinates are needed, so translate them to absolute.
		 */
		child.translateToAbsolute(visibleBounds);

		return visibleBounds;

	}

	/**
	 * adds the given figure to the list of focused figures. Does nothing if the
	 * list of focused figures already contains the given figure.
	 * 
	 * @param figure
	 *            the figure to add.
	 */
	public void addFocusFigure(IFigure figure) {
		if (!focusedFigures.contains(figure)) {
			focusedFigures.add(figure);
			repaint();
		}
	}

	/**
	 * removes the given figure form the list of focused figures. Does nothing
	 * if the list of focused figures does not contain the given figure.
	 * 
	 * @param figure
	 *            the figure to remove.
	 * @return true if the figure was present in the list of focused figures,
	 *         false otherwise
	 */
	public boolean removeFocusFigure(IFigure figure) {
		boolean result = focusedFigures.remove(figure);
		if (result) {
			repaint();
		}
		return result;
	}

	/**
	 * clears the list of focused figures.
	 */
	public void clearFocusedFigures() {
		focusedFigures.clear();
	}

	/**
	 * @return true if the focus mode is enabled, false otherwise.
	 */
	public boolean isFocusEnabled() {
		return focusEnabled;
	}

	/**
	 * @param focusEnabled
	 *            a boolean indicating if the focus should be enabled (true) or
	 *            not (false).
	 */
	public void setFocusEnabled(boolean focusEnabled) {
		this.focusEnabled = focusEnabled;
		repaint();
	}

	/**
	 * @return the color of the blend layer.
	 */
	public Color getBlendLayerColor() {
		return blendLayerColor;
	}

	/**
	 * @param blendLayerColor
	 *            the color of the blend layer to set. Do not use colors with an
	 *            alpha channel to specify the alpha value for the blend layer,
	 *            use {@link #setBlendLayerAlpha(int)} instead.
	 */
	public void setBlendLayerColor(Color blendLayerColor) {
		this.blendLayerColor = blendLayerColor;
	}

	/**
	 * @return the alpha value of the blend layer. Range: 0 (transparent) - 255
	 *         (opaque).
	 */
	public int getBlendLayerAlpha() {
		return blendLayerAlpha;
	}

	/**
	 * @param blendLayerAlpha
	 *            the alpha value of the blend layer to set. The value must be
	 *            in the range from 0 (transparent) to 255 (opaque).
	 */
	public void setBlendLayerAlpha(int blendLayerAlpha) {
		if (blendLayerAlpha < 0 || blendLayerAlpha > 255) {
			throw new IllegalArgumentException(
					"Invalid alpha vale for the blend layer: The alpha value must be in the range 0-255 (both inclusive), but was: "
							+ blendLayerAlpha);
		}
		this.blendLayerAlpha = blendLayerAlpha;
	}

	/**
	 * @return a unmodifiable copy of the focused figures
	 */
	public List<IFigure> getFocusedFigures() {
		return Collections.unmodifiableList(focusedFigures);
	}

}

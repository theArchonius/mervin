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
package at.bitandart.zoubek.mervin.draw2d.figures.overlay;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * An rectangular outline for overlays which also to encode comment information
 * with a dashed outline in the specified color.
 * 
 * @author Florian Zoubek
 *
 */
public class RectangularOutline extends Shape {

	/**
	 * the color of the comment hint.
	 */
	private Color commentHintColor;

	/**
	 * determines if the comment hint should be shown or not.
	 */
	private boolean showCommentHint = false;

	/**
	 * @param commentHintColor
	 *            the color that is used to show the comment hint.
	 */
	public RectangularOutline(Color commentHintColor) {
		this.commentHintColor = commentHintColor;
	}

	@Override
	protected void fillShape(Graphics graphics) {
		graphics.fillRectangle(getBounds());
	}

	@Override
	protected void outlineShape(Graphics graphics) {

		graphics.pushState();

		graphics.setAlpha(255);
		graphics.drawRectangle(getOutlineRectangle());

		graphics.popState();
	}

	@Override
	public void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		if (showCommentHint) {
			paintCommentHint(graphics);
		}
	}

	/**
	 * paints the comment hint outline.
	 * 
	 * @param graphics
	 *            the graphics context used to draw the comment hint outline.
	 */
	protected void paintCommentHint(Graphics graphics) {

		graphics.pushState();

		graphics.setLineWidthFloat(getLineWidthFloat());

		graphics.setAlpha(255);
		graphics.setLineStyle(SWT.LINE_CUSTOM);
		graphics.setLineDash(new float[] { 15.0f, 15.0f });
		graphics.setForegroundColor(commentHintColor);
		graphics.drawRectangle(getOutlineRectangle());

		graphics.popState();
	}

	/**
	 * @return the rectangle used to draw the outline.
	 */
	protected Rectangle getOutlineRectangle() {

		float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
		int inset1 = (int) Math.floor(lineInset);
		int inset2 = (int) Math.ceil(lineInset);

		Rectangle r = new Rectangle(getBounds());
		r.x += inset1;
		r.y += inset1;
		r.width -= inset1 + inset2;
		r.height -= inset1 + inset2;

		return r;
	}

	/**
	 * @param showCommentHint
	 *            true if this outline should show a comment hint.
	 */
	public void setShowCommentHint(boolean showCommentHint) {
		this.showCommentHint = showCommentHint;
		repaint();
	}

}

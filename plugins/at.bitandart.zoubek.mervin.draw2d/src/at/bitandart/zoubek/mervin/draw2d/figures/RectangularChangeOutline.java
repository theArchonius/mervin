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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * An rectangular outline for changes which also to encode comment information
 * with a dashed outline in the specified color.
 * 
 * @author Florian Zoubek
 *
 */
public class RectangularChangeOutline extends Shape {

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
	public RectangularChangeOutline(Color commentHintColor) {
		this.commentHintColor = commentHintColor;
	}

	@Override
	protected void fillShape(Graphics graphics) {
		graphics.fillRectangle(getBounds());
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
		int inset1 = (int) Math.floor(lineInset);
		int inset2 = (int) Math.ceil(lineInset);

		Rectangle r = Rectangle.SINGLETON.setBounds(getBounds());
		r.x += inset1;
		r.y += inset1;
		r.width -= inset1 + inset2;
		r.height -= inset1 + inset2;

		graphics.pushState();
		graphics.setAlpha(255);
		graphics.drawRectangle(r);

		if (showCommentHint) {
			graphics.setLineStyle(SWT.LINE_CUSTOM);
			graphics.setLineDash(new float[] { 15.0f, 15.0f });
			graphics.setForegroundColor(commentHintColor);
			graphics.drawRectangle(r);
		}

		graphics.popState();

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

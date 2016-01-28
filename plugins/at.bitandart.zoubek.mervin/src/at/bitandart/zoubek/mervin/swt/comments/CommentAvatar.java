/*******************************************************************************
 * Copyright (c) 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.swt.comments;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * A widget that draws an avatar for a user with an optional link indicator at
 * the left or right side (Pass {@link SWT#LEFT} to the style bits to place the
 * avatar on the left side and the link indicator points to the right side. Pass
 * {@link SWT#RIGHT} to the style bits to place the avatar on the right side and
 * the link indicator points to the left side.)
 * 
 * @author Florian Zoubek
 *
 */
public class CommentAvatar extends Canvas {

	private Color avatarBackgroundColor;
	private Color avatarForegroundColor;

	private Point avatarSize = new Point(30, 30);
	private int linkWidth = 5;

	/**
	 * 
	 * @param parent
	 * @param style
	 *            Either {@link SWT#LEFT}, {@link SWT#RIGHT} or {@link SWT#NONE}
	 *            . Pass {@link SWT#LEFT} to place the avatar on the left side
	 *            and the link indicator points to the right side. Pass
	 *            {@link SWT#RIGHT} to place the avatar on the right side and
	 *            the link indicator points to the left side. Pass
	 *            {@link SWT#NONE} to draw only the avatar.
	 * 
	 */
	public CommentAvatar(Composite parent, int style) {
		super(parent, style);

		initializeColors();

		addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				avatarBackgroundColor.dispose();
				avatarForegroundColor.dispose();
			}
		});

		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				gc.setBackground(avatarBackgroundColor);

				if ((getStyle() & SWT.RIGHT) != 0) {

					drawLink(gc, linkWidth, (int) Math.round(avatarSize.y / 2.0), 0,
							(int) Math.round(avatarSize.y / 2.0));
					drawAvatar(gc, linkWidth, 0, avatarSize.x, avatarSize.y);

				} else if ((getStyle() & SWT.LEFT) != 0) {

					drawLink(gc, avatarSize.x, (int) Math.round(avatarSize.y / 2.0), avatarSize.x + linkWidth,
							(int) Math.round(avatarSize.y / 2.0));
					drawAvatar(gc, 0, 0, avatarSize.x, avatarSize.y);

				} else {
					drawAvatar(gc, avatarSize.x / 4, avatarSize.y / 4, avatarSize.x, avatarSize.y);
				}

			}

			/**
			 * draws the avatar within the given bounds.
			 * 
			 * @param gc
			 *            the graphics context used to draw the avatar.
			 * @param x
			 *            the x coordinate of the top-left point.
			 * @param y
			 *            the y coordinate of the top-left point.
			 * @param width
			 *            the width of the bounds to draw into.
			 * @param height
			 *            the height of the bounds to draw into.
			 */
			private void drawAvatar(GC gc, double x, double y, double width, double height) {

				gc.fillOval((int) Math.round(x), (int) Math.round(y), (int) Math.round(width),
						(int) Math.round(height));

				double scaledWidth = width * 0.618;
				double scaledHeight = height * 0.618;

				double headSize = Math.min(scaledWidth * 0.618, scaledHeight * 0.618);
				double headX = x + width / 2.0 - headSize / 2.0;
				double headY = y + height - (height * 0.618) - headSize / 2.0;

				gc.setBackground(avatarForegroundColor);
				// head
				gc.fillOval((int) Math.round(headX), (int) Math.round(headY), (int) Math.round(headSize),
						(int) Math.round(headSize));
				// "body"
				gc.fillArc((int) Math.round(x + width / 2.0 - scaledWidth / 2.0),
						(int) Math.round(y + height - (height * 0.618) + headSize / 2.0), (int) Math.round(scaledWidth),
						(int) Math.round(height * 0.618 * 0.618), 0, 180);

			}

			/**
			 * draws the link indicator from (fromX, fromY) to (toX,toY).
			 * 
			 * @param gc
			 *            the graphics context used to draw the avatar.
			 * @param fromX
			 *            the x coordinate of the start point.
			 * @param fromY
			 *            the y coordinate of the start point.
			 * @param toX
			 *            the x coordinate of the end point.
			 * @param toY
			 *            the y coordinate of the start point.
			 */
			private void drawLink(GC gc, int fromX, int fromY, int toX, int toY) {

				gc.setBackground(avatarBackgroundColor);
				gc.fillPolygon(new int[] { fromX, fromY, //
						toX, toY - (int) (avatarSize.y / 4), //
						toX, toY + (int) (avatarSize.y / 4) //
				});

			}
		});
	}

	private void initializeColors() {
		avatarForegroundColor = new Color(getDisplay(), new RGB(229, 229, 229));
		avatarBackgroundColor = new Color(getDisplay(), new RGB(127, 127, 127));
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return new Point(avatarSize.x + linkWidth, avatarSize.y);
	}

}

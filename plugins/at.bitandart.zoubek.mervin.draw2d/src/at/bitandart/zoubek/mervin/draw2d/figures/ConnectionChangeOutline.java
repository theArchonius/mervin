/*******************************************************************************
 * Copyright (c) 2015, 2016 Florian Zoubek.
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
import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import at.bitandart.zoubek.mervin.draw2d.DoublePrecisionVector;

/**
 * An arbitrary shaped outline for changes of {@link Connection}s which also
 * encode comment information with a dashed outline in the specified color.
 * 
 * @author Florian Zoubek
 *
 */
public class ConnectionChangeOutline extends Shape {

	/**
	 * a list of points that should be covered by this outline.
	 */
	private PointList pointsToCover = new PointList();

	/**
	 * the padding between the connection and the borders of the outline.
	 */
	private float paddingWidth = 5.0f;

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
	public ConnectionChangeOutline(Color commentHintColor) {
		this.commentHintColor = commentHintColor;
	}

	@Override
	public Rectangle getBounds() {
		return pointsToCover.getBounds().getCopy().expand(paddingWidth, paddingWidth);
	}

	@Override
	protected void fillShape(Graphics graphics) {

		int[] outlineShape = calculateOutlineShape(pointsToCover, paddingWidth + graphics.getLineWidthFloat(), 1);

		graphics.fillPolygon(outlineShape);
	}

	@Override
	protected void outlineShape(Graphics graphics) {

		graphics.pushState();
		graphics.setAlpha(255);

		int[] outlineShape = calculateOutlineShape(pointsToCover, paddingWidth, 1);

		graphics.drawPolygon(outlineShape);

		if (showCommentHint) {

			// draw comment hint outline
			graphics.pushState();
			graphics.setLineStyle(SWT.LINE_CUSTOM);
			graphics.setLineDash(new float[] { 15.0f, 15.0f });
			graphics.setForegroundColor(commentHintColor);
			graphics.drawPolygon(outlineShape);
			graphics.popState();

		}

		graphics.popState();

	}

	/**
	 * creates coordinates array for a outline shape which completely covers the
	 * given list of points that make up a connection.
	 * 
	 * @param pointsToCover
	 *            the list of points to cover.
	 * @param offset
	 *            the offset of the border of the outline shape to the points.
	 * @param lineWidth
	 *            the width of the line of the connection.
	 * @return the coordinates array with alternating x and y coordinates that
	 *         defines the outline shape ready for use with
	 *         {@link Graphics#fillPolygon(int[])} or
	 *         {@link Graphics#drawPolygon(int[])}
	 */
	private int[] calculateOutlineShape(PointList pointsToCover, double offset, double lineWidth) {

		PrecisionPoint firstPoint = new PrecisionPoint();
		PrecisionPoint secondPoint = new PrecisionPoint();

		int numPoints = pointsToCover.size();

		int[] outlineShape = new int[numPoints * 4];

		double actualOffset = offset;
		if (pointsToCover.size() > 1) {

			DoublePrecisionVector prevDirection = null;
			DoublePrecisionVector prevLeftSegmentDirection = null;
			DoublePrecisionVector prevRightSegmentDirection = null;
			Point prevLeftPoint = null;
			Point prevRightPoint = null;

			for (int i = 0; i < pointsToCover.size(); i++) {

				// P1
				pointsToCover.getPoint(firstPoint, i);

				if (i + 1 < pointsToCover.size()) {
					// P2
					pointsToCover.getPoint(secondPoint, i + 1);
				}

				DoublePrecisionVector segmentDirection = null;

				if (i != pointsToCover.size() - 1) {
					segmentDirection = new DoublePrecisionVector(firstPoint, secondPoint).normalize();
				} else {
					segmentDirection = prevDirection;
				}

				// no need to normalize as segmentDirection is already
				// normalized
				DoublePrecisionVector leftSegmentDirection = segmentDirection.getLeftPerpendicularVectorScreen();
				DoublePrecisionVector rightSegmentDirection = segmentDirection.getRightPerpendicularVectorScreen();

				/*
				 * if there is no previous segment just copy the segment
				 * directions
				 */

				if (prevDirection == null) {
					prevDirection = segmentDirection;
				}

				if (prevLeftSegmentDirection == null) {
					prevLeftSegmentDirection = leftSegmentDirection;
				}

				if (prevRightSegmentDirection == null) {
					prevRightSegmentDirection = rightSegmentDirection;
				}

				/*
				 * outline shape point at the "left" (counterclockwise) side of
				 * the Vector P1 -> P2
				 */
				Point leftPoint = null;

				double perpLeft = rightSegmentDirection.getDotProduct(prevDirection);

				if (perpLeft != 0) {

					/*
					 * find the intersection point of the left outline lines of
					 * the current and the previous segment and use it as right
					 * point
					 */
					DoublePrecisionVector w = new DoublePrecisionVector(
							new DoublePrecisionVector(firstPoint)
									.add(leftSegmentDirection.getMultipliedDoublePrecision(actualOffset)),
							new DoublePrecisionVector(prevLeftPoint.preciseX(), prevLeftPoint.preciseY()));
					double s = leftSegmentDirection.getDotProduct(w) / perpLeft;
					leftPoint = prevDirection.getMultiplied(s).toPoint();
					leftPoint.translate(prevLeftPoint);

				} else {

					/*
					 * direction stayed the same -> fall back to use simple
					 * translation along the left outline direction
					 */
					DoublePrecisionVector leftOutlineDirection = leftSegmentDirection
							.getAddedDoublePrecision(prevLeftSegmentDirection).normalize();
					leftPoint = firstPoint.getTranslated(leftOutlineDirection.getMultiplied(actualOffset).toPoint());

				}

				/*
				 * outline shape point at the "right" (clockwise) side of the
				 * Vector P1 -> P2
				 */
				Point rightPoint = null;

				double perpRight = rightSegmentDirection.getDotProduct(prevDirection);

				if (perpRight != 0) {

					/*
					 * find the intersection point of the right outline lines of
					 * the current and the previous segment and use it as right
					 * point
					 */
					DoublePrecisionVector w = new DoublePrecisionVector(
							new DoublePrecisionVector(firstPoint)
									.add(rightSegmentDirection.getMultipliedDoublePrecision(actualOffset)),
							new DoublePrecisionVector(prevRightPoint.preciseX(), prevRightPoint.preciseY()));
					double s = leftSegmentDirection.getDotProduct(w) / perpRight;
					rightPoint = prevDirection.getMultiplied(s).toPoint();
					rightPoint.translate(prevRightPoint);

				} else {

					/*
					 * direction stayed the same -> fall back to use simple
					 * translation along the right outline direction
					 */

					DoublePrecisionVector rightOutlineDirection = rightSegmentDirection
							.getAddedDoublePrecision(prevRightSegmentDirection).normalize();
					rightPoint = firstPoint.getTranslated(rightOutlineDirection.getMultiplied(actualOffset).toPoint());

				}

				if (i == 0) {

					/*
					 * beginning of the connection -> also translate the outline
					 * in the opposite direction of the segment in order to
					 * apply the correct offset
					 */
					PrecisionPoint startOffset = segmentDirection.getNegatedDoublePrecision().multiply(actualOffset)
							.toPoint();
					leftPoint.translate(startOffset);
					rightPoint.translate(startOffset);

				} else if (i == pointsToCover.size() - 1) {

					/*
					 * end of the connection -> also translate the outline in
					 * the direction of the segment in order to apply the
					 * correct offset
					 */
					PrecisionPoint endOffset = segmentDirection.getMultipliedDoublePrecision(actualOffset).toPoint();
					leftPoint.translate(endOffset);
					rightPoint.translate(endOffset);

				}

				// assign left point x
				outlineShape[i * 2] = leftPoint.x;
				// assign left point y
				outlineShape[i * 2 + 1] = leftPoint.y;

				// assign right point x
				outlineShape[outlineShape.length - 2 - i * 2] = rightPoint.x;
				// assign right point y
				outlineShape[outlineShape.length - 1 - i * 2] = rightPoint.y;

				// store the directions for use in the next iteration
				prevDirection = segmentDirection;
				prevLeftSegmentDirection = leftSegmentDirection;
				prevRightSegmentDirection = rightSegmentDirection;
				prevLeftPoint = leftPoint;
				prevRightPoint = rightPoint;
			}
		} else if (pointsToCover.size() > 0) {
			// a single point connection, outline it with a simple rectangle
			pointsToCover.getPoint(firstPoint, 0);

			Point topLeft = firstPoint.getTranslated(-actualOffset, -actualOffset);
			Point topRight = firstPoint.getTranslated(actualOffset, -actualOffset);
			Point bottomLeft = firstPoint.getTranslated(-actualOffset, actualOffset);
			Point bottomRight = firstPoint.getTranslated(actualOffset, actualOffset);

			outlineShape = new int[] { topLeft.x, topLeft.y, topRight.x, topRight.y, bottomRight.x, bottomRight.y,
					bottomLeft.x, bottomLeft.y, };
		}
		return outlineShape;
	}

	/**
	 * @param showCommentHint
	 *            true if this outline should show a comment hint.
	 */
	public void setShowCommentHint(boolean showCommentHint) {
		this.showCommentHint = showCommentHint;
		repaint();
	}

	/**
	 * @param pointsToCover
	 *            the points that make up the connection to cover
	 */
	public void setPointsToCover(PointList pointsToCover) {
		this.pointsToCover = pointsToCover.getCopy();
		// remove consecutive identical points from this list
		removeConsecutiveIdenticalPoints(this.pointsToCover);
		repaint();
	}

	/**
	 * removes all consecutive identical points from this list
	 * 
	 * @param pointList
	 *            the point list to modify
	 */
	private void removeConsecutiveIdenticalPoints(PointList pointList) {

		List<Integer> pointsToRemove = new ArrayList<Integer>(pointList.size());
		for (int i = 1; i < pointList.size(); i++) {
			Point point = pointList.getPoint(i);
			Point prevPoint = pointList.getPoint(i - 1);
			if (point.equals(prevPoint)) {
				pointsToRemove.add(i);
			}
		}

		for (Integer index : pointsToRemove) {
			pointList.removePoint(index);
		}
	}

}

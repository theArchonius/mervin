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
package at.bitandart.zoubek.mervin.draw2d.figures.offscreen;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import at.bitandart.zoubek.mervin.draw2d.DoublePrecisionVector;
import at.bitandart.zoubek.mervin.draw2d.figures.ChangeType;
import at.bitandart.zoubek.mervin.draw2d.figures.IChangeTypeStyleAdvisor;

/**
 * A {@link IOffScreenIndicator} that indicates the change type of an off-screen
 * {@link IFigure}. It draws a single character representing the change type in
 * the center, surrounded by a circle. Additionally an arrow points towards the
 * invisible figure. The visibility of this figure depends on the visibility of
 * the combined bounds of the linked figures - if the they intersect the
 * containers bounds the indicator is invisible, otherwise it is visible.
 * 
 * @author Florian Zoubek
 *
 */
public class OffScreenChangeIndicator extends Figure implements IOffScreenIndicator {

	/**
	 * the change type to indicate.
	 */
	private ChangeType changeType;

	/**
	 * the set of linked figures by this indicator
	 */
	private Set<IFigure> linkedFigures = new HashSet<IFigure>();

	/**
	 * the container
	 */
	private IFigure containerFigure;

	/**
	 * the style advisor used to obtain the colors for this indicator
	 */
	private IChangeTypeStyleAdvisor styleAdvisor;

	/**
	 * the line width used to draw the circle of this indicator
	 */
	private double lineWidth = 2.0;

	/**
	 * the padding between the circle and the bounds of the figure - this
	 * padding is used to make the padding visible.
	 */
	private double arrowPadding = 5.0;

	/**
	 * creates a new {@link OffScreenChangeIndicator} that uses the given
	 * {@link IChangeTypeStyleAdvisor} for drawing.
	 * 
	 * @param styleAdvisor
	 */
	public OffScreenChangeIndicator(IChangeTypeStyleAdvisor styleAdvisor) {
		this.styleAdvisor = styleAdvisor;
	}

	/**
	 * the figure listener used to update the visibility of this indicator.
	 */
	private FigureListener containerUpdateListener = new FigureListener() {

		@Override
		public void figureMoved(IFigure source) {
			updateVisibility();
			revalidate();
		}

	};

	@Override
	public Rectangle getLinkedFiguresBounds() {

		Rectangle linkedBounds = null;

		for (IFigure linkedFigure : linkedFigures) {
			Rectangle bounds = linkedFigure.getBounds().getCopy();
			linkedFigure.translateToAbsolute(bounds);
			if (linkedBounds == null) {
				linkedBounds = bounds.getCopy();
			} else {
				linkedBounds.union(bounds);
			}
		}

		if (linkedBounds == null) {
			return new Rectangle();
		}

		return linkedBounds;

	}

	/**
	 * updates the visibility of this figure based on the intersection of the
	 * combined bounds of all linked figures and the container figure bounds.
	 */
	private void updateVisibility() {

		if (linkedFigures != null && containerFigure != null) {

			Rectangle containerBounds = containerFigure.getBounds().getCopy();
			containerFigure.translateToAbsolute(containerBounds);

			Rectangle linkedFigureBounds = getLinkedFiguresBounds();

			if (!isVisible()) {
				if (!containerBounds.intersects(linkedFigureBounds)) {
					setVisible(true);
				}
			} else if (containerBounds.intersects(linkedFigureBounds)) {
				setVisible(false);
			}

		}
	}

	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		Font font = createSymbolFont();
		Dimension symbolExtents = TextUtilities.INSTANCE.getStringExtents(getSymbolFor(getChangeType()), font);
		font.dispose();

		double diameter = Math.max(symbolExtents.preciseWidth(), symbolExtents.preciseHeight());
		diameter += 2 * arrowPadding;
		return new PrecisionDimension(diameter, diameter);
	}

	@Override
	public void addLinkedFigure(IFigure linkedFigure) {
		this.linkedFigures.add(linkedFigure);
		revalidate();
		repaint();
	}

	@Override
	public void setContainerFigure(IFigure containerFigure) throws IllegalArgumentException {
		if (this.containerFigure != null) {
			this.containerFigure.removeFigureListener(containerUpdateListener);
		}
		this.containerFigure = containerFigure;
		this.containerFigure.addFigureListener(containerUpdateListener);
		revalidate();
		repaint();
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		PrecisionRectangle bounds = new PrecisionRectangle(getBounds());
		ChangeType changeType = getChangeType();

		graphics.setForegroundColor(styleAdvisor.getForegroundColorForChangeType(changeType));
		Rectangle shrinked = bounds.getShrinked(lineWidth / 2.0 + arrowPadding, lineWidth / 2.0 + arrowPadding);

		if (linkedFigures != null) {

			// draw arrow

			Point center = new PrecisionPoint(bounds.getCenter());
			Point absoluteCenter = center.getCopy();
			translateToAbsolute(absoluteCenter);

			double arrowLength = (bounds.preciseWidth() + 1) / 2.0;
			double arrowWidth = arrowLength;

			Point linkedFigureCenter = new PrecisionPoint(getLinkedFiguresBounds().getCenter());

			DoublePrecisionVector direction = new DoublePrecisionVector(new PrecisionPoint(absoluteCenter),
					new PrecisionPoint(linkedFigureCenter)).normalize();

			DoublePrecisionVector arrowTipVector = direction.getMultipliedDoublePrecision(arrowLength);
			Point arrowTip = center.getTranslated(arrowTipVector.x, arrowTipVector.y);

			DoublePrecisionVector arrowRightVector = direction.getRightPerpendicularVectorScreen()
					.getMultipliedDoublePrecision(arrowWidth);
			Point arrowRight = center.getTranslated(arrowRightVector.x, arrowRightVector.y);

			DoublePrecisionVector arrowLeftVector = direction.getLeftPerpendicularVectorScreen()
					.getMultipliedDoublePrecision(arrowWidth);
			Point arrowLeft = center.getTranslated(arrowLeftVector.x, arrowLeftVector.y);

			int[] arrowshape = new int[] { arrowTip.x, arrowTip.y, arrowRight.x, arrowRight.y, arrowLeft.x,
					arrowLeft.y };

			graphics.setBackgroundColor(styleAdvisor.getForegroundColorForChangeType(changeType));
			graphics.fillPolygon(arrowshape);

		}

		// draw enclosing circle

		graphics.setBackgroundColor(getBackgroundColor());
		graphics.fillOval(shrinked);
		graphics.setLineWidthFloat((float) lineWidth);
		graphics.drawOval(shrinked);

		// determine the extends of the symbol

		Font prevFont = graphics.getFont();
		Font symbolFont = createSymbolFont();
		String symbol = getSymbolFor(changeType);
		Dimension symbolExtents = TextUtilities.INSTANCE.getStringExtents(symbol, symbolFont);
		Point textPosition = shrinked.getCenter().translate(-symbolExtents.preciseWidth() / 2.0,
				-symbolExtents.preciseHeight() / 2.0);

		// draw the change type symbol

		graphics.setFont(symbolFont);
		graphics.drawText(symbol, (int) Math.round(textPosition.preciseX()), (int) Math.round(textPosition.preciseY()));
		graphics.setFont(prevFont);
		symbolFont.dispose();

	}

	/**
	 * @return a new font based on the assigned font, which should be used to
	 *         draw the smybol at the center of this indicator
	 */
	private Font createSymbolFont() {
		Font font = getFont();
		return FontDescriptor.createFrom(font).setStyle(SWT.BOLD).increaseHeight(0).createFont(Display.getDefault());

	}

	/**
	 * @param changeType
	 * @return the corresponding symbol to the given change type
	 */
	protected String getSymbolFor(ChangeType changeType) {
		switch (changeType) {
		case ADDITION:
			return "A";
		case DELETION:
			return "D";
		case MODIFICATION:
			return "M";
		default:
		case LAYOUT:
			return "L";
		}
	}

	@Override
	public IFigure getContainerFigure() {
		return containerFigure;
	}

	/**
	 * sets the change type of this indicator.
	 * 
	 * @param changeType
	 */
	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	/**
	 * 
	 * @return the change type of this indicator.
	 */
	public ChangeType getChangeType() {
		return changeType;
	}

	@Override
	public Set<IFigure> getLinkedFigures() {
		return Collections.unmodifiableSet(linkedFigures);
	}

	@Override
	public void removeLinkedFigure(IFigure linkedFigure) {
		linkedFigures.remove(linkedFigure);
	}

}

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
package at.bitandart.zoubek.mervin.review.explorer;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import at.bitandart.zoubek.mervin.draw2d.figures.ChangeType;
import at.bitandart.zoubek.mervin.draw2d.figures.IChangeTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.util.vis.MathUtil;

/**
 * An abstract{@link OwnerDrawLabelProvider} that draws stacked bars for each
 * {@link DifferenceKind} based on the number of differences for each kind. It
 * uses the given {@link IChangeTypeStyleAdvisor} to obtain the colors for each
 * bar, where {@link DifferenceKind#MOVE} is considered as a
 * {@link DifferenceKind#CHANGE}. The diff count is determined by a given
 * {@link IDifferenceCounter}.
 * 
 * @author Florian Zoubek
 *
 */
public abstract class DiffTypeOverviewLabelProvider extends OwnerDrawLabelProvider {

	private ColumnViewer viewer;
	private ViewerColumn column;
	private IChangeTypeStyleAdvisor styleAdvisor;
	private IDifferenceCounter diffCounter;

	/**
	 * @param styleAdvisor
	 *            the style advisor used to obtain the colors for the bars.
	 * @param diffCounter
	 *            the {@link IDifferenceCounter} that is used to obtain the
	 *            count values for the stacked bars.
	 */
	public DiffTypeOverviewLabelProvider(IChangeTypeStyleAdvisor styleAdvisor, IDifferenceCounter diffCounter) {
		this.styleAdvisor = styleAdvisor;
		this.diffCounter = diffCounter;
	}

	@Override
	protected void initialize(ColumnViewer viewer, ViewerColumn column) {
		Assert.isTrue(this.viewer == null && this.column == null, "Label provider instance already in use"); //$NON-NLS-1$

		this.viewer = viewer;
		this.column = column;
		super.initialize(viewer, column);
	}

	@Override
	protected void measure(Event event, Object element) {
		event.width += getDefaultWidth();
	}

	/**
	 * @return the default with for all labels.
	 */
	abstract protected int getDefaultWidth();

	/**
	 * calculates the value that is considered as the maximum possible
	 * difference count value (the upper bound, or the value represented by the
	 * rightmost position of the visible range) for the given element. The
	 * returned value must be higher or equal as the sum of all difference
	 * counts of all {@link DifferenceKind}s.
	 * 
	 * @param element
	 *            the element to retrieve the maximum difference count values
	 *            for.
	 * @return the maximum difference count (the upper bound, or the value
	 *         represented by the rightmost position of the visible range) for
	 *         the given element.
	 */
	abstract protected int getMaximumCount(Object element);

	@Override
	protected void paint(Event event, Object element) {

		Rectangle bounds = null;
		if (event.item instanceof TreeItem) {
			bounds = ((TreeItem) event.item).getBounds(event.index);
		}
		if (event.item instanceof TableItem) {
			bounds = ((TableItem) event.item).getBounds(event.index);
		}

		if (bounds == null) {
			/* not able to retrieve the bounds of the item */
			return;
		}
		bounds.width -= 1;

		int maxCountValue = getMaximumCount(element);
		DifferenceKind[] kinds = DifferenceKind.values();

		GC gc = event.gc;

		/* cache the background color to restore it later */
		Color previousBackgroundColor = event.gc.getBackground();
		drawDiffTypeStackedBars(gc, element, maxCountValue, kinds, bounds);
		gc.setBackground(previousBackgroundColor);

	}

	/**
	 * draws the stacked bars for the given element within the given bounds.
	 * 
	 * @param gc
	 *            the {@link GC} used to draw.
	 * @param element
	 *            the element to draw the stacked bars for.
	 * @param maxValue
	 *            the vale that is assigned to the rightmost position on the
	 *            visible range.
	 * @param kinds
	 *            the difference kinds to draw bars for.
	 * @param bounds
	 *            the bounds to draw the bars into.
	 */
	protected void drawDiffTypeStackedBars(GC gc, Object element, int maxValue, DifferenceKind[] kinds,
			Rectangle bounds) {

		int xOffset = 0;
		boolean drawSeparator = false;
		for (DifferenceKind kind : kinds) {
			int diffKindCount = Math.max(diffCounter.getDiffCount(element, kind), 0);
			/*
			 * the int cast is safe as the mapped value will be in the range
			 * from 0 to bounds.width - the latter is an integer, so the result
			 * has to fit in an integer.
			 */
			int width = (int) Math.round(MathUtil.map(diffKindCount, 0, maxValue, 0, bounds.width - 1));

			if (drawSeparator && width != 0) {
				gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_GRAY));
				gc.drawLine(bounds.x + xOffset, Math.round(bounds.y + bounds.height * 0.1f), bounds.x + xOffset,
						bounds.y + bounds.height);
			}

			gc.setBackground(styleAdvisor.getBackgroundColorForChangeType(toChangeType(kind)));
			gc.fillRectangle(bounds.x + xOffset, Math.round(bounds.y + bounds.height * 0.4f), width,
					Math.round(bounds.height * 0.6f));
			xOffset += width;
			if (width != 0) {
				drawSeparator = true;
			}
		}
	}

	@Override
	protected void erase(Event event, Object element) {
		/* copied from StyledCellLabelProvider */
		// use native erase
		// info has been set by 'update': announce that we paint ourselves
		event.detail &= ~SWT.FOREGROUND;
	}

	/**
	 * @return the {@link IDifferenceCounter} used to obtain the count values
	 *         for the bars.
	 */
	public IDifferenceCounter getDiffCounter() {
		return diffCounter;
	}

	/**
	 * converts {@link DifferenceKind}s to {@link ChangeType}s.
	 * {@link DifferenceKind#MOVE} and {@link DifferenceKind#CHANGE} will both
	 * be mapped to {@link ChangeType#MODIFICATION}.
	 * 
	 * @param kind
	 *            the {@link DifferenceKind} to convert.
	 * @return the corresponding {@link ChangeType}.
	 */
	protected ChangeType toChangeType(DifferenceKind kind) {
		switch (kind) {
		case ADD:
			return ChangeType.ADDITION;
		case DELETE:
			return ChangeType.DELETION;
		case MOVE:
			return ChangeType.MODIFICATION;
		case CHANGE:
			return ChangeType.MODIFICATION;
		default:
			return ChangeType.MODIFICATION;
		}
	}

}

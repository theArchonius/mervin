/*******************************************************************************
 * Copyright (c) 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.review.explorer;

import java.util.EnumMap;

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

import at.bitandart.zoubek.mervin.IOverlayTypeHelper;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.OverlayType;
import at.bitandart.zoubek.mervin.util.vis.MathUtil;

/**
 * An abstract{@link OwnerDrawLabelProvider} that draws stacked bars for each
 * {@link DifferenceKind} based on the number of differences for each kind. It
 * uses the given {@link IOverlayTypeStyleAdvisor} to obtain the colors for each
 * bar, where {@link DifferenceKind#MOVE} is considered as a
 * {@link DifferenceKind#CHANGE}. The diff count is determined by a given
 * {@link IDifferenceCounter}. The tooltip shows more shows the actual number of
 * differences for a subset of all {@link OverlayType}s. This provider also
 * shows an optional overview of types with a non-zero count.
 * 
 * @author Florian Zoubek
 *
 */
public abstract class DiffTypeOverviewLabelProvider extends OwnerDrawLabelProvider {

	private IOverlayTypeHelper overlayTypeHelper;
	private ColumnViewer viewer;
	private ViewerColumn column;
	private IOverlayTypeStyleAdvisor styleAdvisor;
	private IDifferenceCounter diffCounter;
	private OverlayType[] tooltipChangeTypes = new OverlayType[] { OverlayType.ADDITION, OverlayType.DELETION,
			OverlayType.MODIFICATION };
	private boolean drawTypeOverview;
	private int maxTypeOverviewWidth = 5;
	private int maxTypeOverviewExtent = 5;
	private float relTypeOverviewWidth = 0.25f;
	private float relVerticalPadding = 0.2f;

	/**
	 * @param styleAdvisor
	 *            the style advisor used to obtain the colors for the bars.
	 * @param diffCounter
	 *            the {@link IDifferenceCounter} that is used to obtain the
	 *            count values for the stacked bars.
	 * @param drawTypeOverview
	 *            true if the type overview should be drawn, false otherwise.
	 * @param overlayTypeHelper
	 *            the {@link IOverlayTypeHelper} used to map
	 *            {@link DifferenceKind}s to {@link OverlayType}s.
	 */
	public DiffTypeOverviewLabelProvider(IOverlayTypeStyleAdvisor styleAdvisor, IDifferenceCounter diffCounter,
			boolean drawTypeOverview, IOverlayTypeHelper overlayTypeHelper) {
		this.styleAdvisor = styleAdvisor;
		this.diffCounter = diffCounter;
		this.drawTypeOverview = drawTypeOverview;
		this.overlayTypeHelper = overlayTypeHelper;
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
		// horizontal padding
		int padding = Math.max(Math.round(bounds.height * relVerticalPadding), 1);
		bounds.height -= padding * 2;
		bounds.y += padding;
		// fixed vertical padding of 1 pixel
		bounds.width -= 2;
		bounds.x += 1;

		int maxCountValue = getMaximumCount(element);

		GC gc = event.gc;

		EnumMap<OverlayType, Integer> typeCounts = calculateDiffCount(element, DifferenceKind.values());

		/* cache the background color to restore it later */
		Color previousBackgroundColor = event.gc.getBackground();
		Rectangle stackedBarBounds = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
		if (drawTypeOverview) {

			int overviewWidth = Math.min(maxTypeOverviewWidth, Math.round(bounds.width * relTypeOverviewWidth));
			Rectangle typeOverviewBounds = new Rectangle(bounds.x, bounds.y, overviewWidth, bounds.height);

			stackedBarBounds.x = typeOverviewBounds.x + typeOverviewBounds.width + 1;
			stackedBarBounds.width -= typeOverviewBounds.width;

			drawTypeOverview(gc, element, typeCounts, typeOverviewBounds, false);

		}
		drawDiffTypeStackedBars(gc, element, maxCountValue, typeCounts, stackedBarBounds);
		gc.setBackground(previousBackgroundColor);

	}

	@Override
	public String getToolTipText(Object element) {

		/* show detailed type statistics in the tooltip */
		EnumMap<OverlayType, Integer> typeCounts = calculateDiffCount(element, DifferenceKind.values());

		StringBuilder tooltip = new StringBuilder();

		for (OverlayType changeType : tooltipChangeTypes) {

			int currentCount = 0;
			if (typeCounts.containsKey(changeType)) {
				currentCount = typeCounts.get(changeType).intValue();
			}
			if (tooltip.length() != 0) {
				tooltip.append("\n");
			}
			tooltip.append(changeType.toString());
			tooltip.append(": ");
			tooltip.append(currentCount);

		}
		return tooltip.toString();
	}

	/**
	 * calculates the counts for each given {@link DifferenceKind} using the
	 * current {@link IDifferenceCounter} and returns a map containing the
	 * counts mapped to their respective {@link OverlayType}.
	 * 
	 * @param element
	 *            the to get the diff count for.
	 * @return a map containing the counts mapped to their respective
	 *         {@link OverlayType}
	 */
	private EnumMap<OverlayType, Integer> calculateDiffCount(Object element, DifferenceKind[] kinds) {
		EnumMap<OverlayType, Integer> typeCounts = new EnumMap<OverlayType, Integer>(OverlayType.class);

		for (DifferenceKind kind : kinds) {

			OverlayType changeType = toChangeType(kind);
			int currentCount = 0;
			if (typeCounts.containsKey(changeType)) {
				currentCount = typeCounts.get(changeType).intValue();
			}
			currentCount += Math.max(diffCounter.getDiffCount(element, kind), 0);
			typeCounts.put(changeType, currentCount);

		}
		return typeCounts;
	}

	/**
	 * draws the stacked bars for the given element within the given bounds.
	 * 
	 * @param gc
	 *            the {@link GC} used to draw.
	 * @param element
	 *            the element to draw the stacked bars for.
	 * @param maxValue
	 *            the value that is assigned to the rightmost position on the
	 *            visible range.
	 * @param typeCounts
	 *            a map containing the counts for each {@link OverlayType}.
	 * @param bounds
	 *            the bounds to draw the bars into.
	 */
	protected void drawDiffTypeStackedBars(GC gc, Object element, int maxValue, EnumMap<OverlayType, Integer> counts,
			Rectangle bounds) {

		int xOffset = 0;
		for (OverlayType type : OverlayType.values()) {

			int diffKindCount = 0;
			Integer count = counts.get(type);
			if (count != null) {
				diffKindCount = count.intValue();
			}

			/*
			 * the int cast is safe as the mapped value will be in the range
			 * from 0 to bounds.width - the latter is an integer, so the result
			 * has to fit in an integer.
			 */
			int width = (int) Math.round(MathUtil.map(diffKindCount, 0, maxValue, 0, bounds.width - 1));

			gc.setBackground(styleAdvisor.getBackgroundColorForOverlayType(type));
			gc.fillRectangle(bounds.x + xOffset, bounds.y, width, bounds.height);
			xOffset += width;
		}
	}

	/**
	 * draws the type overview for the given element within the given bounds.
	 * 
	 * @param gc
	 *            the {@link GC} used to draw.
	 * @param element
	 *            the element to draw the type overview for.
	 * @param counts
	 *            a map containing the counts for each {@link OverlayType}.
	 * @param bounds
	 *            he bounds to draw the overview into.
	 * @param horizontal
	 *            true if the type should be arranged horizontally, false if
	 *            they should be arranged vertically.
	 */
	private void drawTypeOverview(GC gc, Object element, EnumMap<OverlayType, Integer> counts, Rectangle bounds,
			boolean horizontal) {

		int offset = 0;
		int boundsExtent = horizontal ? bounds.width : bounds.height;
		int extent = Math.max((int) Math.round(boundsExtent / (double) OverlayType.values().length),
				maxTypeOverviewExtent);

		int numTypes = 0;
		for (OverlayType type : OverlayType.values()) {

			Integer count = counts.get(type);
			if (count != null && count.intValue() > 0) {
				numTypes++;
			}
		}

		offset = Math.round((boundsExtent - (extent * numTypes)) * 0.5f);

		for (OverlayType type : OverlayType.values()) {

			int diffKindCount = 0;
			Integer count = counts.get(type);
			if (count != null) {
				diffKindCount = count.intValue();
			}
			if (diffKindCount > 0) {
				gc.setBackground(styleAdvisor.getBackgroundColorForOverlayType(type));
				if (horizontal) {
					gc.fillRectangle(bounds.x + offset, bounds.y, extent, bounds.height);
				} else {
					gc.fillRectangle(bounds.x, bounds.y + offset, bounds.width, extent);
				}
				offset += extent;
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
	 * converts {@link DifferenceKind}s to {@link OverlayType}s.
	 * {@link DifferenceKind#MOVE} and {@link DifferenceKind#CHANGE} will both
	 * be mapped to {@link OverlayType#MODIFICATION}.
	 * 
	 * @param kind
	 *            the {@link DifferenceKind} to convert.
	 * @return the corresponding {@link OverlayType}.
	 */
	protected OverlayType toChangeType(DifferenceKind kind) {
		return overlayTypeHelper.toOverlayType(kind);
	}

	/**
	 * sets the relative padding between the top and the content as well as
	 * between the content and the bottom. The actual padding is at least 1
	 * pixel.
	 * 
	 * @param relVerticalPadding
	 *            the relative vertical padding to set.
	 */
	public void setRelVerticalPadding(float relVerticalPadding) {
		this.relVerticalPadding = relVerticalPadding;
	}

	/**
	 * sets the maximum width that should be used for the type overview.
	 * 
	 * @param maxTypeOverviewWidth
	 *            the maximum width to set.
	 */
	public void setMaxTypeOverviewWidth(int maxTypeOverviewWidth) {
		this.maxTypeOverviewWidth = maxTypeOverviewWidth;
	}

	/**
	 * sets the relative width that should be used for the type overview. If the
	 * actual value exceeds the maximum width, the maximum width will be used
	 * instead.
	 * 
	 * @param relTypeOverviewWidth
	 *            the relative width to set.
	 */
	public void setRelTypeOverviewWidth(float relTypeOverviewWidth) {
		this.relTypeOverviewWidth = relTypeOverviewWidth;
	}

	/**
	 * sets the maximum extent (height) of a type in the type overview.
	 * 
	 * @param maxTypeOverviewExtent
	 *            the extent to set.
	 */
	public void setMaxTypeOverviewExtent(int maxTypeOverviewExtent) {
		this.maxTypeOverviewExtent = maxTypeOverviewExtent;
	}

	/**
	 * enables/disables the drawing of the type overview.
	 * 
	 * @param drawTypeOverview
	 *            true if the type overview should be drawn, false otherwise.
	 */
	public void setDrawTypeOverview(boolean drawTypeOverview) {
		this.drawTypeOverview = drawTypeOverview;
	}

}

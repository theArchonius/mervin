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
package at.bitandart.zoubek.mervin.draw2d.figures.offscreen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.geometry.Rectangle;

import at.bitandart.zoubek.mervin.draw2d.figures.IChangeTypeStyleAdvisor;

/**
 * A {@link FigureListener} that automatically replaces close
 * {@link OffScreenChangeIndicator}s by a single
 * {@link MergedOffScreenChangeIndicator}. Replaced indicators are not removed
 * form the figure tree, but their visibility is managed by this merger.
 * Therefore, changing the visibility of an indicator that is managed by this
 * merger is discouraged and may lead to unexpected behavior. To use this merger
 * add it to the indicator container as a layout listener and register the
 * indicators to merge to this merger by calling
 * {@link #registerIndicator(OffScreenChangeIndicator)}. Afterwards the merger
 * adds and removes {@link MergedOffScreenChangeIndicator}s after each layout
 * call of the indicator container or if {@link #updateMergedIndicators()} is
 * called.
 * 
 * @author Florian Zoubek
 *
 */
public class OffScreenChangeIndicatorMerger implements LayoutListener {

	/**
	 * the style advisor used to obtain the colors for merged indicators.
	 */
	private IChangeTypeStyleAdvisor styleAdvisor;

	/**
	 * the set of managed {@link OffScreenChangeIndicator}s.
	 */
	private Set<OffScreenChangeIndicator> managedIndicators = new LinkedHashSet<OffScreenChangeIndicator>();

	/**
	 * the current set of merged indicators.
	 */
	private Set<MergedOffScreenChangeIndicator> mergedIndicators = new LinkedHashSet<MergedOffScreenChangeIndicator>();

	/**
	 * the current mapping of change indicators to their containing merge
	 * indicators
	 */
	private Map<OffScreenChangeIndicator, MergedOffScreenChangeIndicator> mergedIndicatorMap = new HashMap<OffScreenChangeIndicator, MergedOffScreenChangeIndicator>();

	/**
	 * the container to add the merge indicators to.
	 */
	private IFigure indicatorContainer;

	/**
	 * 
	 * @param indicatorContainer
	 *            the container to add the merge indicators to.
	 * @param styleAdvisor
	 *            the style advisor used by the merge indicators.
	 */
	public OffScreenChangeIndicatorMerger(IFigure indicatorContainer, IChangeTypeStyleAdvisor styleAdvisor) {
		super();
		this.indicatorContainer = indicatorContainer;
		this.styleAdvisor = styleAdvisor;
	}

	public void updateMergedIndicators() {

		Set<OffScreenChangeIndicator> alreadyMerged = new HashSet<OffScreenChangeIndicator>();

		// TODO clean up code

		for (OffScreenChangeIndicator indicator : managedIndicators) {

			if (!alreadyMerged.contains(indicator)) {

				if (!indicator.areLinkedFiguresVisible()) {

					Rectangle indicatorBounds = indicator.getBounds().getCopy();
					indicator.translateToAbsolute(indicatorBounds);
					MergedOffScreenChangeIndicator mergedOffScreenChangeIndicator = mergedIndicatorMap.get(indicator);

					for (OffScreenChangeIndicator otherIndicator : managedIndicators) {

						if (!alreadyMerged.contains(otherIndicator) && otherIndicator != indicator
								&& !otherIndicator.areLinkedFiguresVisible()) {

							Rectangle otherBounds = otherIndicator.getBounds().getCopy();
							otherIndicator.translateToAbsolute(otherBounds);

							/*
							 * Merge only if the bounds intersect and their
							 * container figures are the same
							 */
							if (otherBounds.intersects(indicatorBounds)
									&& otherIndicator.getContainerFigure() == indicator.getContainerFigure()) {

								MergedOffScreenChangeIndicator otherMergedIndicator = mergedIndicatorMap
										.get(otherIndicator);

								if (mergedOffScreenChangeIndicator != null) {

									addIndicator(mergedOffScreenChangeIndicator, otherIndicator, alreadyMerged);
									if (!alreadyMerged.contains(indicator)) {
										/*
										 * make sure the indicator is marked as
										 * merged
										 */
										alreadyMerged.add(indicator);
									}

								} else if (otherMergedIndicator != null) {

									mergedOffScreenChangeIndicator = otherMergedIndicator;
									addIndicator(mergedOffScreenChangeIndicator, indicator, alreadyMerged);

								} else {

									mergedOffScreenChangeIndicator = createMergedOffScreenChangeIndicator();
									mergedOffScreenChangeIndicator.setContainerFigure(indicator.getContainerFigure());

									addIndicator(mergedOffScreenChangeIndicator, indicator, alreadyMerged);
									addIndicator(mergedOffScreenChangeIndicator, otherIndicator, alreadyMerged);

									indicatorContainer.add(mergedOffScreenChangeIndicator);
									mergedIndicators.add(mergedOffScreenChangeIndicator);

								}

							}

						}

					}

				}

				if (!alreadyMerged.contains(indicator)) {
					// indicator has not been merged
					removeIndicator(indicator);
					alreadyMerged.add(indicator);
				}

			}

		}

		// remove empty merged indicators

		Iterator<MergedOffScreenChangeIndicator> iterator = mergedIndicators.iterator();
		while (iterator.hasNext()) {

			MergedOffScreenChangeIndicator indicator = iterator.next();
			if (indicator.getMergedIndicators().isEmpty()) {
				indicatorContainer.remove(indicator);
				indicator.cleanUp();
				iterator.remove();
			}

		}

	}

	/**
	 * @return a new {@link MergedOffScreenChangeIndicator} that should be used
	 *         to merge {@link OffScreenChangeIndicator}s.
	 */
	protected MergedOffScreenChangeIndicator createMergedOffScreenChangeIndicator() {
		return new MergedOffScreenChangeIndicator(styleAdvisor);
	}

	/**
	 * adds the given indicator to the given merged indicator, updates its
	 * visibility and update the set of already merged indicators. Does not
	 * change the merged indicator if the given indicator is already part of the
	 * merged indicator.
	 * 
	 * @param newMergedIndicator
	 *            the merge indicator to add the given indicator to.
	 * @param indicator
	 *            the indicator to add.
	 * @param alreadyMerged
	 *            the set of already merged indicators to update.
	 */
	private void addIndicator(MergedOffScreenChangeIndicator newMergedIndicator, OffScreenChangeIndicator indicator,
			Set<OffScreenChangeIndicator> alreadyMerged) {

		MergedOffScreenChangeIndicator oldMergedIndicator = mergedIndicatorMap.get(indicator);

		if (oldMergedIndicator != null && newMergedIndicator != oldMergedIndicator) {
			removeIndicator(indicator);
		}
		if (newMergedIndicator != oldMergedIndicator) {
			newMergedIndicator.addIndicator(indicator);
			mergedIndicatorMap.put(indicator, newMergedIndicator);
		}

		indicator.setVisible(false);

		alreadyMerged.add(indicator);

	}

	/**
	 * removes the given indicator from its current merged indicator and updates
	 * its visibility. Does not change a merged indicator if the given indicator
	 * is not part of a merged indicator.
	 * 
	 * @param indicator
	 */
	private void removeIndicator(OffScreenChangeIndicator indicator) {

		MergedOffScreenChangeIndicator oldMergedIndicator = mergedIndicatorMap.remove(indicator);
		if (oldMergedIndicator != null) {
			oldMergedIndicator.removeIndicator(indicator);
		}

		indicator.setVisible(true);

	}

	/**
	 * registers the given indicator to this merger.
	 * 
	 * @param indicator
	 */
	public void registerIndicator(OffScreenChangeIndicator indicator) {
		managedIndicators.add(indicator);
	}

	/**
	 * removes the given indicator for the managed indicator set of this merger
	 * and removes it from the merged indicator if one exists.
	 * 
	 * @param indicator
	 */
	public void unregisterIndicator(OffScreenChangeIndicator indicator) {
		managedIndicators.remove(indicator);
		removeIndicator(indicator);
	}

	@Override
	public void invalidate(IFigure container) {
		// intentionally left empty
	}

	@Override
	public boolean layout(IFigure container) {
		return false;
	}

	@Override
	public void postLayout(IFigure container) {
		updateMergedIndicators();
	}

	@Override
	public void remove(IFigure child) {
		// intentionally left empty
	}

	@Override
	public void setConstraint(IFigure child, Object constraint) {
		// intentionally left empty
	}

}

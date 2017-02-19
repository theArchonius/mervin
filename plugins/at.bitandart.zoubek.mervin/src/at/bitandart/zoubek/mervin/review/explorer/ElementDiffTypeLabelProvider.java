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

import at.bitandart.zoubek.mervin.IOverlayTypeHelper;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.IOverlayTypeStyleAdvisor;

/**
 * A {@link DiffTypeOverviewLabelProvider} for the range from 0 to
 * {@link IDifferenceCounter#getDiffCount(Object)} - it shows the distribution
 * of difference kinds for a given element.
 * 
 * @author Florian Zoubek
 *
 */
public class ElementDiffTypeLabelProvider extends DiffTypeOverviewLabelProvider {

	/**
	 * @param styleAdvisor
	 * @param diffCounter
	 * @param drawTypeOverview
	 * @param overlayTypeHelper
	 * @see DiffTypeOverviewLabelProvider
	 */
	public ElementDiffTypeLabelProvider(IOverlayTypeStyleAdvisor styleAdvisor, IDifferenceCounter diffCounter,
			boolean drawTypeOverview, IOverlayTypeHelper overlayTypeHelper) {
		super(styleAdvisor, diffCounter, drawTypeOverview, overlayTypeHelper);
	}

	@Override
	protected int getMaximumCount(Object element) {
		return Math.max(getDiffCounter().getDiffCount(element), 0);
	}

	@Override
	protected int getDefaultWidth() {
		return 30;
	}

}

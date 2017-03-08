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
package at.bitandart.zoubek.mervin.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import at.bitandart.zoubek.mervin.review.HighlightMode;
import at.bitandart.zoubek.mervin.review.IReviewHighlightingPart;

/**
 * Switches between {@link HighlightMode#HOVER} and
 * {@link HighlightMode#SELECTION} for the given view if the given handled item
 * is selected or not.
 * 
 * @author Florian Zoubek
 *
 */
public class SwitchHighlightMode {

	@Execute
	public void execute(MPart part, MHandledItem item, EPartService partService, MApplication application) {

		Object object = part.getObject();

		if (object instanceof IReviewHighlightingPart) {
			HighlightMode highlightMode = HighlightMode.SELECTION;
			if (item.isSelected()) {
				highlightMode = HighlightMode.HOVER;
			}
			((IReviewHighlightingPart) object).setHighlightMode(highlightMode);
		}

	}
}

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

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import at.bitandart.zoubek.mervin.IMervinContextConstants;
import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * Clears all highlights of the current model review.
 * 
 * @author Florian Zoubek
 *
 */
public class ClearHighlights {

	@CanExecute
	public boolean canExecute(
			@Optional @Named(IMervinContextConstants.HIGHLIGHTED_ELEMENTS) List<Object> highlightedElements) {

		if (highlightedElements != null) {
			return !highlightedElements.isEmpty();
		}
		return false;
	}

	@Execute
	public void execute(IReviewHighlightService highlightService,
			@Named(IMervinContextConstants.ACTIVE_MODEL_REVIEW) @Optional ModelReview review) {

		if (review != null) {
			highlightService.clearHighlights(review);
		}
	}
}

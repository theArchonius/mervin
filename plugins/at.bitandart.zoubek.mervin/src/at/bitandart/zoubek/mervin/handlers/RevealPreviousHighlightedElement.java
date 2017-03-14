/*******************************************************************************
 * Copyright (c) 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import at.bitandart.zoubek.mervin.review.IReviewHighlightingPart;

/**
 * A command handler that reveals the previous highlighted element in parts that
 * implement {@link IReviewHighlightingPart}.
 * 
 * @author Florian Zoubek
 *
 */
public class RevealPreviousHighlightedElement {

	@CanExecute
	public boolean canExecute(@Optional MPart part) {

		if (part != null) {

			Object object = part.getObject();
			if (object instanceof IReviewHighlightingPart) {

				return ((IReviewHighlightingPart) object).hasPreviousHighlight();
			}
		}
		return false;
	}

	@Execute
	public void execute(@Optional MPart part, IEventBroker eventBroker) {

		if (part != null) {

			Object object = part.getObject();
			if (object instanceof IReviewHighlightingPart) {
				((IReviewHighlightingPart) object).revealPreviousHighlight();
			}
		}
	}
}

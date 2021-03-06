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
package at.bitandart.zoubek.mervin;

import javax.inject.Named;

/**
 * Context constants for injected values using the {@link Named} annotation.
 * 
 * @author Florian Zoubek
 *
 */
public interface IMervinContextConstants {

	/**
	 * constant for the currently active model review.
	 */
	public static final String ACTIVE_MODEL_REVIEW = "at.bitandart.zoubek.mervin.active.review";

	/**
	 * constant for the currently active diagram diff editor.
	 */
	public static final String ACTIVE_DIAGRAM_DIFF_EDITOR = "at.bitandart.zoubek.mervin.active.editor";

	/**
	 * constant for the current user who reviews the active model review.
	 */
	public static final String CURRENT_REVIEWER = "at.bitandart.zoubek.mervin.active.user";

	/**
	 * constant for the current EMF transactional editing domain for the active
	 * model review.
	 */
	public static final String ACTIVE_TRANSACTIONAL_EDITING_DOMAIN = "at.bitandart.zoubek.mervin.active.transactional.editing.domain";

	/**
	 * constant for the current GEF edit domain for the active model review.
	 */
	public static final String ACTIVE_EDIT_DOMAIN = "at.bitandart.zoubek.mervin.active.edit.domain";

	/**
	 * constant for a unmodifiable list of the highlighted elements for the
	 * currently active model review.
	 */
	public static final String HIGHLIGHTED_ELEMENTS = "at.bitandart.zoubek.mervin.highlights.list";

}

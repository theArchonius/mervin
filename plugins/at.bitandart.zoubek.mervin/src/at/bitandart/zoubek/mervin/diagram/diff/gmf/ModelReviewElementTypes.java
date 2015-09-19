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
package at.bitandart.zoubek.mervin.diagram.diff.gmf;

import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeEnumerator;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * @author Florian Zoubek
 *
 */
public class ModelReviewElementTypes extends AbstractElementTypeEnumerator {

	public static final IElementType MODEL_REVIEW = getElementType("at.bitandart.zoubek.mervin.model.ModelReview");
	public static final String MODEL_REVIEW_SEMANTIC_HINT = "Mervin_ModelReview_ModelReview";

	public static final IElementType DIAGRAM = getElementType("at.bitandart.zoubek.mervin.model.Diagram");
	public static final String DIAGRAM_SEMANTIC_HINT = "Mervin_ModelReview_Diagram";

	public static final IElementType OVERLAY_ADDITION = getElementType(
			"at.bitandart.zoubek.mervin.model.overlay.Additon");
	public static final String OVERLAY_ADDITION_SEMANTIC_HINT = "Mervin_ModelReview_Overlay_Addition";

	public static final IElementType OVERLAY_MODIFICATION = getElementType(
			"at.bitandart.zoubek.mervin.model.overlay.Modification");
	public static final String OVERLAY_MODIFICATION_SEMANTIC_HINT = "Mervin_ModelReview_Overlay_Modification";

	public static final IElementType OVERLAY_DELETION = getElementType(
			"at.bitandart.zoubek.mervin.model.overlay.Deletion");
	public static final String OVERLAY_DELETION_SEMANTIC_HINT = "Mervin_ModelReview_Overlay_Deletion";

}

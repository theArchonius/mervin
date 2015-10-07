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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.notation.View;

import at.bitandart.zoubek.mervin.diagram.diff.DiagramDiffView;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * An {@link AbstractViewProvider} for {@link ModelReview} elements for use in a
 * model review diff view.
 * 
 * @author Florian Zoubek
 *
 */
public class ModelReviewViewProvider extends AbstractViewProvider {

	private final Map<String, Class<?>> diagramMap = new HashMap<String, Class<?>>();
	private final Map<String, Class<?>> nodeMap = new HashMap<String, Class<?>>();

	{
		diagramMap.put(DiagramDiffView.PART_DESCRIPTOR_ID, ModelReviewDiffDiagramViewFactory.class);

		nodeMap.put(ModelReviewElementTypes.DIAGRAM_SEMANTIC_HINT, WorkspaceDiagramFactory.class);
		nodeMap.put(ModelReviewElementTypes.OVERLAY_DIFFERENCE_SEMANTIC_HINT, ChangeOverlayFactory.class);
	}

	@Override
	protected Class<?> getDiagramViewClass(IAdaptable semanticAdapter, String diagramKind) {
		return diagramMap.get(diagramKind);
	}

	@Override
	protected Class<?> getNodeViewClass(IAdaptable semanticAdapter, View containerView, String semanticHint) {
		if (semanticHint != null) {
			return nodeMap.get(semanticHint);
		}
		return null;
	}

}

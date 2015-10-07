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

import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.CreateRootEditPartOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.IEditPartOperation;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

import at.bitandart.zoubek.mervin.diagram.diff.DiagramDiffView;
import at.bitandart.zoubek.mervin.diagram.diff.parts.DiagramDiffRootEditPart;
import at.bitandart.zoubek.mervin.diagram.diff.parts.DiagramEditPart;
import at.bitandart.zoubek.mervin.diagram.diff.parts.DifferenceOverlayEditPart;
import at.bitandart.zoubek.mervin.diagram.diff.parts.WorkspaceEditPart;

/**
 * @author Florian Zoubek
 *
 */
public class ModelReviewEditPartProvider extends AbstractEditPartProvider {

	private final Map<String, Class<?>> diagramMap = new HashMap<String, Class<?>>();

	private final Map<String, Class<?>> nodeMap = new HashMap<String, Class<?>>();

	{
		diagramMap.put(DiagramDiffView.PART_DESCRIPTOR_ID, WorkspaceEditPart.class);

		nodeMap.put(ModelReviewElementTypes.DIAGRAM_SEMANTIC_HINT, DiagramEditPart.class);
		nodeMap.put(ModelReviewElementTypes.OVERLAY_DIFFERENCE_SEMANTIC_HINT, DifferenceOverlayEditPart.class);
	}

	@Override
	protected Class<?> getDiagramEditPartClass(View view) {
		return diagramMap.get(view.getType());
	}

	@Override
	protected Class<?> getNodeEditPartClass(View view) {
		return nodeMap.get(view.getType());
	}

	@Override
	public RootEditPart createRootEditPart(Diagram diagram) {
		if (diagram.getType().equals(DiagramDiffView.PART_DESCRIPTOR_ID)) {
			return new DiagramDiffRootEditPart(diagram.getMeasurementUnit());
		}
		return super.createRootEditPart(diagram);
	}

	@Override
	public boolean provides(IOperation operation) {
		if (operation instanceof CreateRootEditPartOperation) {
			CreateRootEditPartOperation actualOperation = ((CreateRootEditPartOperation) operation);
			View view = ((IEditPartOperation) actualOperation).getView();
			if (view instanceof Diagram && view.getType().equals(DiagramDiffView.PART_DESCRIPTOR_ID)) {
				return true;
			}
		}
		return super.provides(operation);
	}

}

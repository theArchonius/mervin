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
package at.bitandart.zoubek.mervin.diagram.diff;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

import at.bitandart.zoubek.mervin.diagram.diff.parts.DiagramEditPart;
import at.bitandart.zoubek.mervin.diagram.diff.parts.WorkspaceEditPart;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * This {@link EditPartFactory} creates {@link EditPart}s for diagram diffs and
 * uses the registered edit
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramDiffPartFactory implements EditPartFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 * java.lang.Object)
	 */
	@Override
	public EditPart createEditPart(EditPart context, Object model) {

		if (model instanceof ModelReview) {
			WorkspaceEditPart workspaceEditPart = new WorkspaceEditPart((ModelReview) model);
			return workspaceEditPart;
		} else if (model instanceof Diagram) {
			DiagramEditPart diagramEditPart = new DiagramEditPart((Diagram) model);
			return diagramEditPart;
		}

		/*
		 * no known model review element -> delegate to registered GMF edit part
		 * factories
		 */
		EditPartService editPartService = EditPartService.getInstance();
		if (model instanceof View) {
			return editPartService.createGraphicEditPart((View) model);
		}
		return editPartService.createEditPart(context, model);
	}

}

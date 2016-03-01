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
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gmf.runtime.notation.Diagram;

import at.bitandart.zoubek.mervin.diagram.diff.GMFDiagramDiffViewService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * Handles hide/show request for modification overlays.
 * 
 * @author Florian Zoubek
 *
 */
public class ToggleModificationOverlays {

	@Execute
	public void execute(MHandledToolItem item, ModelReview modelReview, Diagram diagram, EditDomain editDomain,
			TransactionalEditingDomain transactionalEditingDomain, GMFDiagramDiffViewService diagramDiffViewService) {

		diagramDiffViewService.updateOverlayTypeVisibility(editDomain, transactionalEditingDomain, diagram,
				GMFDiagramDiffViewService.TYPE_DESCRIPTOR_MODIFICATION, item.isSelected(), false);

	}

}
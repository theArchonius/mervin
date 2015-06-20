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
package at.bitandart.zoubek.mervin.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import at.bitandart.zoubek.mervin.diagram.diff.DiagramDiffView;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.review.wizards.LoadReviewWizard;

public class LoadReviewHandler {

	@Execute
	public void execute(Shell shell, LoadReviewWizard wizard,
			EPartService partService, EModelService modelService,
			MApplication application) {

		// let the user choose which review should be loaded 
		WizardDialog wizardDialog = new WizardDialog(shell, wizard);
		wizardDialog.setBlockOnOpen(true);
		
		if (wizardDialog.open() == WizardDialog.OK) {

			ModelReview modelReview = wizard.getLoadedModelReview();

			// assign the model data to the part
			MPart diagramDiffViewer = partService
					.createPart(DiagramDiffView.PART_DESCRIPTOR_ID);
			diagramDiffViewer.getTransientData().put(DiagramDiffView.DATA_TRANSIENT_MODEL_REVIEW, modelReview);

			// open the part in the default IDE editor stack
			MUIElement element = modelService.find(
					"org.eclipse.e4.primaryDataStack", application);
			
			if (element instanceof MPartStack) {
				((MPartStack) element).getChildren().add(diagramDiffViewer);
				partService.showPart(diagramDiffViewer, PartState.ACTIVATE);
			}
			
		}
	}

}
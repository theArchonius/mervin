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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import at.bitandart.zoubek.mervin.review.wizards.LoadReviewWizard;

public class LoadReviewHandler {

	@Execute
	public void execute(Shell shell, LoadReviewWizard wizard) {
		WizardDialog wizardDialog = new WizardDialog(shell, wizard);

		wizardDialog.setBlockOnOpen(true);
		if (wizardDialog.open() == WizardDialog.OK) {
			// TODO
		}
	}

}
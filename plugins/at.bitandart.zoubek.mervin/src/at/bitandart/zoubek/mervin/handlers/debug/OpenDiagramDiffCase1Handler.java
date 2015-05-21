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
package at.bitandart.zoubek.mervin.handlers.debug;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Shell;

import at.bitandart.zoubek.mervin.diagram.diff.DiagramDiffView;

/**
 * Opens a new diagram difference view with test case #1.
 */
public class OpenDiagramDiffCase1Handler {

	@Execute
	public static void execute(Shell shell, EPartService partService,
			EModelService modelService, MWindow window)
			throws ExecutionException {
		MPart diffViewer = partService
				.createPart(DiagramDiffView.PART_DESCRIPTOR_ID);
		List<MPartStack> stacks = modelService.findElements(window,
				"org.eclipse.e4.primaryDataStack", MPartStack.class, null);
		if (!stacks.isEmpty()) {
			stacks.get(0).getChildren().add(diffViewer);
			partService.showPart(diffViewer, PartState.VISIBLE);
		}
	}

}

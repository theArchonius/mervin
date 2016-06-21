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
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import at.bitandart.zoubek.mervin.patchset.history.PatchSetHistoryView;
import at.bitandart.zoubek.mervin.patchset.history.PatchSetHistoryView.VisibleDiffMode;

/**
 * Handles switch to a specific {@link VisibleDiffMode} in the patch set history
 * view.
 * 
 * @author Florian Zoubek
 *
 */
public class SwitchVisibleDiffsHandler {

	private VisibleDiffMode visibleDiffMode;

	/**
	 * @param visibleDiffMode
	 *            the {@link VisibleDiffMode} to switch to.
	 */
	public SwitchVisibleDiffsHandler(VisibleDiffMode visibleDiffMode) {
		this.visibleDiffMode = visibleDiffMode;
	}

	@Execute
	public void execute(MHandledItem item, EPartService partService, MApplication application) {

		if (item.isSelected()) {

			MPart patchSetHistoryPart = partService.findPart(PatchSetHistoryView.PART_DESCRIPTOR_ID);
			Object object = patchSetHistoryPart.getObject();

			if (object instanceof PatchSetHistoryView) {
				((PatchSetHistoryView) object).setVisibleDiffs(visibleDiffMode);
			}
		}
	}
}

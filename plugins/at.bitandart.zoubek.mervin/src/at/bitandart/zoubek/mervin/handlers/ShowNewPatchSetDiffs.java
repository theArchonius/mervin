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

import at.bitandart.zoubek.mervin.patchset.history.PatchSetHistoryView.VisibleDiffMode;

/**
 * Handles switch to show all diffs of the new patch set in the patch set
 * history view.
 * 
 * @author Florian Zoubek
 *
 */
public class ShowNewPatchSetDiffs extends SwitchVisibleDiffsHandler {

	public ShowNewPatchSetDiffs() {
		super(VisibleDiffMode.NEW_PATCHSET_DIFFS);
	}

}
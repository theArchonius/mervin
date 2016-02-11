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
package at.bitandart.zoubek.mervin.comments;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn;

/**
 * An {@link ICommentColumn} implementation for columns created for the mervin
 * model.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetColumn implements ICommentColumn {

	private PatchSet patchSet;

	/**
	 * creates a new {@link PatchSetColumn}.
	 * 
	 * @param patchSet
	 *            the {@link PatchSet} to associate to this column or null if
	 *            this column represents the base version.
	 */
	public PatchSetColumn(PatchSet patchSet) {
		super();
		this.patchSet = patchSet;
	}

	@Override
	public String getTitle() {
		if (patchSet != null) {
			return "PatchSet #" + patchSet.getId();
		} else {
			return "Base";
		}
	}

	/**
	 * @return the associated patch set or null if the column represents the
	 *         base version.
	 */
	public PatchSet getPatchSet() {
		return patchSet;
	}

}
/*******************************************************************************
 * Copyright (c) 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.patchset.history;

import java.util.ArrayList;

import org.eclipse.emf.compare.Diff;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;

/**
 * An {@link AbstractHistoryEntry} that represents a {@link DiffWithSimilarity}
 * within a regular {@link PatchSetHistoryEntryImpl}. This entry provides only a
 * value for the containing patch set and does not allow setting a value for a
 * given patch set explicitly.
 * 
 * @author Florian Zoubek
 *
 */
public class SubDiffEntry extends AbstractHistoryEntry<Diff, DiffWithSimilarity> {

	private PatchSet patchSet;
	private DiffWithSimilarity subDiff;

	/**
	 * @param patchSet
	 *            the patch set containing the given diff.
	 * @param subDiff
	 *            the {@link DiffWithSimilarity} assigned to this
	 *            {@link SubDiffEntry}.
	 */
	public SubDiffEntry(PatchSet patchSet, DiffWithSimilarity subDiff) {
		super(new ArrayList<IPatchSetHistoryEntry<?, ?>>());
		this.patchSet = patchSet;
		this.subDiff = subDiff;
	}

	@Override
	public DiffWithSimilarity getValue(PatchSet patchSet) {
		if (this.patchSet == patchSet) {
			return subDiff;
		}
		return null;
	}

	@Override
	public void setValue(PatchSet patchSet, DiffWithSimilarity value) {
		// Intentionally left empty
	}

	@Override
	public Diff getEntryObject() {
		return subDiff.getDiff();
	}

	public PatchSet getPatchSet() {
		return patchSet;
	}

}

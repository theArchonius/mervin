/*******************************************************************************
 * Copyright (c) 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.patchset.history;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * Abstract base class for {@link IPatchSetHistoryEntry}s that provides the
 * functionality to store a given list of the subentries.
 * 
 * @author Florian Zoubek
 *
 * @param <O>
 *            the type of the Object.
 * @param <V>the
 *            type of the values of the entry.
 */
public abstract class AbstractHistoryEntry<O, V> implements IPatchSetHistoryEntry<O, V> {

	protected IPatchSetHistoryEntry<?, ?> parent;

	protected List<IPatchSetHistoryEntry<?, ?>> subEntries;

	public AbstractHistoryEntry(IPatchSetHistoryEntry<?, ?> parent, List<IPatchSetHistoryEntry<?, ?>> subEntries) {
		super();
		this.subEntries = subEntries;
		this.parent = parent;
		updateSubentriesParents();
		updateParentSubentries();
	}

	private void updateSubentriesParents() {
		if (subEntries != null) {
			for (IPatchSetHistoryEntry<?, ?> subEntry : subEntries) {
				subEntry.setParent(this);
			}
		}
	}

	private void updateParentSubentries() {
		if (parent != null) {
			List<IPatchSetHistoryEntry<?, ?>> siblings = parent.getSubEntries();
			if (!siblings.contains(this)) {
				siblings.add(this);
			}
		}
	}

	@Override
	public Map<PatchSet, V> getValues(Collection<PatchSet> patchSets) {

		Map<PatchSet, V> valueMap = new HashMap<PatchSet, V>();

		for (PatchSet patchSet : patchSets) {

			V value = getValue(patchSet);

			if (value != null) {
				valueMap.put(patchSet, value);
			}
		}

		return valueMap;
	}

	@Override
	public List<IPatchSetHistoryEntry<?, ?>> getSubEntries() {
		return subEntries;
	}

	@Override
	public void setParent(IPatchSetHistoryEntry<?, ?> parent) {
		if (this.parent != parent && this.parent != null) {
			this.parent.getSubEntries().remove(this);
		}
		this.parent = parent;
		updateParentSubentries();
	}

	@Override
	public IPatchSetHistoryEntry<?, ?> getParent() {
		return parent;
	}

}
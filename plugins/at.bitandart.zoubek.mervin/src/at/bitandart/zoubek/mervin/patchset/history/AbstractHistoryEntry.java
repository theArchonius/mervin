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
package at.bitandart.zoubek.mervin.patchset.history;

import java.util.List;

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

	protected List<IPatchSetHistoryEntry<?, ?>> subEntries;

	public AbstractHistoryEntry(List<IPatchSetHistoryEntry<?, ?>> subEntries) {
		super();
		this.subEntries = subEntries;
	}

	@Override
	public List<IPatchSetHistoryEntry<?, ?>> getSubEntries() {
		return subEntries;
	}

}
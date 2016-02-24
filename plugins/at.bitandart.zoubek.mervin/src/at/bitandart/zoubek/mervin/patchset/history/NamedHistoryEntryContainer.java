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
 * A named container for {@link IPatchSetHistoryEntry}s for use within the UI.
 * 
 * @author Florian Zoubek
 *
 */
class NamedHistoryEntryContainer {

	List<? extends IPatchSetHistoryEntry<?, ?>> entries;

	private String name;

	public NamedHistoryEntryContainer(String name, List<? extends IPatchSetHistoryEntry<?, ?>> entries) {
		this.name = name;
		this.entries = entries;
	}

	public String getName() {
		return name;
	}

	public List<? extends IPatchSetHistoryEntry<?, ?>> getEntries() {
		return entries;
	}
}
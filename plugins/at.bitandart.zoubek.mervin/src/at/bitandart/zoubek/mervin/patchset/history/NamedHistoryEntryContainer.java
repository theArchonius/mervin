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

import java.util.List;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * A named container for {@link IPatchSetHistoryEntry}s for use within the UI.
 * 
 * @author Florian Zoubek
 *
 */
public class NamedHistoryEntryContainer extends AbstractHistoryEntry<String, String> {

	private String name;

	public NamedHistoryEntryContainer(String name, List<IPatchSetHistoryEntry<?, ?>> entries) {
		super(entries);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getValue(PatchSet patchSet) {
		return null;
	}

	@Override
	public void setValue(PatchSet patchSet, String value) {
		// intentionally left empty
	}

	@Override
	public String getEntryObject() {
		return name;
	}

}
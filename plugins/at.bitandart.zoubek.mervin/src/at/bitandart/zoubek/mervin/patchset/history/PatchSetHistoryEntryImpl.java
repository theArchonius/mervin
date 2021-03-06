/*******************************************************************************
 * Copyright (c) 2015, 2016, 2017 Florian Zoubek.
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
import java.util.HashMap;
import java.util.Map;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * Default implementation of {@link IPatchSetHistoryEntry} which holds the
 * object as a property and the values in a {@link Map}.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetHistoryEntryImpl<O, V> extends AbstractHistoryEntry<O, V> {

	private O entryObject;

	private Map<PatchSet, V> valueMap;

	public PatchSetHistoryEntryImpl(O entryObject) {
		this(null, entryObject);
	}

	public PatchSetHistoryEntryImpl(IPatchSetHistoryEntry<?, ?> parent, O entryObject) {
		super(parent, new ArrayList<IPatchSetHistoryEntry<?, ?>>());
		this.entryObject = entryObject;
		valueMap = new HashMap<PatchSet, V>();
	}

	@Override
	public V getValue(PatchSet patchSet) {
		return valueMap.get(patchSet);
	}

	@Override
	public O getEntryObject() {
		return entryObject;
	}

	/**
	 * sets the entry object to the specified object.
	 * 
	 * @param object
	 *            the object to set as the entry object.
	 */
	public void setEntryObject(O object) {
		this.entryObject = object;
	}

	@Override
	public void setValue(PatchSet patchSet, V value) {
		valueMap.put(patchSet, value);
	}
}

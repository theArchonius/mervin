/*******************************************************************************
 * Copyright (c) 2015, 2016 Florian Zoubek.
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
 * Represents an entry that holds values for one or many {@link PatchSet}s that
 * belong to an specified {@link Object}.
 * 
 * @param <O>
 *            the type of the object.
 * @param <V>
 *            the type of the values in the entry.
 * 
 * @author Florian Zoubek
 *
 */
public interface IPatchSetHistoryEntry<O, V> {

	/**
	 * @param patchSet
	 *            the patch set to retrieve the value for.
	 * @return the associated value to the given {@link PatchSet}, null if no
	 *         value exists.
	 */
	V getValue(PatchSet patchSet);

	/**
	 * @param patchSet
	 *            the patch set to set the value for.
	 * @param value
	 *            the associated value to the given {@link PatchSet}
	 */
	void setValue(PatchSet patchSet, V value);

	/**
	 * 
	 * @return the entry {@link Object} that is associated to the given values.
	 */
	O getEntryObject();

	/**
	 * @return a list containing all subentries. It is the responsibility of the
	 *         caller to set the parent of the entry if an entry is added to
	 *         this list.
	 * @see #setParent(IPatchSetHistoryEntry)
	 */
	List<IPatchSetHistoryEntry<?, ?>> getSubEntries();

	/**
	 * @return the parent entry or null if no parent entry exists.
	 */
	IPatchSetHistoryEntry<?, ?> getParent();

	/**
	 * sets the parent entry and updates the current and former parent's sub
	 * entries if necessary.
	 * 
	 * @param parent
	 *            the parent to set or null if the entry has no parent.
	 */
	void setParent(IPatchSetHistoryEntry<?, ?> parent);

}

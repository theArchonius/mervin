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
package at.bitandart.zoubek.mervin.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * A map that maps original {@link EObject}s of a unified model to one or more
 * copies and vice-versa.
 * 
 * @author Florian Zoubek
 *
 */
public class UnifiedModelMap {

	// underlying maps
	private Multimap<EObject, EObject> originalToCopyMap = HashMultimap.create();
	private Map<EObject, EObject> copyToOriginalMap = new HashMap<>();

	/**
	 * adds a mapping between the given original {@link EObject} and its given
	 * copy. Does nothing if the mapping already exists. The copy is added to an
	 * existing mapping if the original already exists. This method also removes
	 * any existing mapping of the given copy to another original.
	 * 
	 * @param original
	 *            the original {@link EObject}.
	 * @param copy
	 *            the copy of the given original {@link EObject}.
	 */
	public void put(EObject original, EObject copy) {

		EObject formerOriginal = copyToOriginalMap.get(copy);
		if (formerOriginal != original) {
			originalToCopyMap.remove(formerOriginal, copy);
		}
		originalToCopyMap.put(original, copy);
		copyToOriginalMap.put(copy, original);
	}

	/**
	 * @param original
	 *            the original {@link EObject} to check.
	 * @return true if a mapping for the given original {@link EObject} exists.
	 */
	public boolean containsOriginal(EObject original) {
		return originalToCopyMap.containsKey(original);
	}

	/**
	 * 
	 * @param copy
	 *            the copy {@link EObject} to check.
	 * @return true if a mapping for the given copy {@link EObject} exists.
	 */
	public boolean containsCopy(EObject copy) {
		return copyToOriginalMap.containsKey(copy);
	}

	/**
	 * puts all elements of the given map in this map using the rules defined in
	 * {@link #put(EObject, EObject)}.
	 * 
	 * @param map
	 *            the map to copy into this map.
	 */
	public void putAll(Map<EObject, EObject> map) {

		Set<Entry<EObject, EObject>> entries = map.entrySet();

		for (Entry<EObject, EObject> entry : entries) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * @param copy
	 *            the copy to get the original for.
	 * @return the corresponding original {@link EObject} for the given copy
	 */
	public EObject getOriginal(EObject copy) {
		return copyToOriginalMap.get(copy);
	}

	/**
	 * @param original
	 * @return an unmodifiable list of all copies mapped to the given original
	 *         {@link EObject}. Returns an empty collection if no copy is found,
	 *         never returns null.
	 */
	public Collection<EObject> getCopies(EObject original) {
		return Collections.unmodifiableCollection(originalToCopyMap.get(original));
	}

	/**
	 * clears this map completely.
	 */
	public void clear() {
		originalToCopyMap.clear();
		copyToOriginalMap.clear();
	}

}

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
package at.bitandart.zoubek.mervin.patchset.history.organizers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.Diff;

import at.bitandart.zoubek.mervin.patchset.history.IPatchSetHistoryEntry;
import at.bitandart.zoubek.mervin.patchset.history.NamedHistoryEntryContainer;

/**
 * A {@link DiffCategoryOrganizer} that organizes the diff entries by their
 * dependencies (defined by {@link Diff#getRequires()},
 * {@link Diff#getRequiredBy()}). Other entries will be removed.
 * 
 * @author Florian Zoubek
 *
 * @see Diff
 * @see Diff#getRequires()
 * @see Diff#getRequiredBy()
 */
public class DependencyDiffOrganizer extends DiffCategoryOrganizer {

	@Override
	public Collection<Object> groupPatchSetHistoryEntries(List<? extends IPatchSetHistoryEntry<?, ?>> modelEntries,
			List<? extends IPatchSetHistoryEntry<?, ?>> diagramEntries) {

		Collection<Object> rootEntries = super.groupPatchSetHistoryEntries(modelEntries, diagramEntries);
		for (Object entry : rootEntries) {

			if (entry instanceof NamedHistoryEntryContainer) {

				List<IPatchSetHistoryEntry<?, ?>> entries = ((NamedHistoryEntryContainer) entry).getSubEntries();
				organizeEntries(entries);
			}

		}

		return rootEntries;
	}

	/**
	 * organizes the given list of entries by their {@link Diff} dependencies.
	 * The given entries must not contain any subentries.
	 * 
	 * @param entries
	 *            the entries to organize
	 */
	private void organizeEntries(List<IPatchSetHistoryEntry<?, ?>> entries) {

		List<IPatchSetHistoryEntry<?, ?>> rootEntries = new ArrayList<IPatchSetHistoryEntry<?, ?>>();
		Map<Diff, IPatchSetHistoryEntry<?, ?>> entryMap = new HashMap<>();

		/*
		 * index the entries using their associated diff and extract the root
		 * diffs first
		 */
		for (IPatchSetHistoryEntry<?, ?> entry : entries) {
			Object entryObject = entry.getEntryObject();
			if (entryObject instanceof Diff) {

				Diff diff = (Diff) entryObject;
				if (isRootEntry(diff, entries)) {
					rootEntries.add(entry);
				}

				entryMap.put(diff, entry);
			}
		}

		/* use the index to add the children to the root entries */
		for (IPatchSetHistoryEntry<?, ?> entry : rootEntries) {
			addChildEntries(entry, entryMap);
		}

		/*
		 * the entries have been organized and stored in the other collection,
		 * now clear the old collection and write them back
		 */
		entries.clear();
		entries.addAll(rootEntries);

	}

	/**
	 * checks if the entry assigned with the given diff should be a root entry
	 * or not.
	 * 
	 * @param diff
	 *            the diff assigned to the entry.
	 * @param entries
	 *            the other entries.
	 * @return true if the entry is a root entry, false otherwise.
	 */
	private boolean isRootEntry(Diff diff, List<IPatchSetHistoryEntry<?, ?>> entries) {

		if (!diff.getRequires().isEmpty()) {
			for (Diff requiredDiff : diff.getRequires()) {
				for (IPatchSetHistoryEntry<?, ?> entry : entries) {
					if (requiredDiff == entry.getEntryObject()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * adds the child entries to the given entry by retrieving the depending
	 * Diff entries using the given entryMap. A dependent diff entry is not
	 * added if it cannot be found in the given map.
	 * 
	 * @param entry
	 *            the entry to add the child entries to.
	 * @param entryMap
	 *            the map used to find the entries for the depending diffs.
	 */
	private void addChildEntries(IPatchSetHistoryEntry<?, ?> entry, Map<Diff, IPatchSetHistoryEntry<?, ?>> entryMap) {

		Object entryObject = entry.getEntryObject();
		if (entryObject instanceof Diff) {

			Diff diff = (Diff) entryObject;
			for (Diff dependentDiff : diff.getRequiredBy()) {

				IPatchSetHistoryEntry<?, ?> dependentEntry = entryMap.get(dependentDiff);
				if (dependentEntry != null) {

					addChildEntries(dependentEntry, entryMap);

					entry.getSubEntries().add(dependentEntry);
				}
			}
		}
	}
}

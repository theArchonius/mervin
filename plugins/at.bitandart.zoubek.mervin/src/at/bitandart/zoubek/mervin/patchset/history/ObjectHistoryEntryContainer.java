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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;

/**
 * A {@link NamedHistoryEntryContainer} that is related to {@link EObject}s in
 * the patch set history. It allows access to the matches of assigned subentries
 * with {@link Diff} or {@link DiffWithSimilarity} values. This container
 * assumes that each subentry's {@link Diff} or {@link DiffWithSimilarity} value
 * belongs to the same match for the same patch set. Otherwise the behavior of
 * this class it undefined.
 * 
 * @author Florian Zoubek
 *
 */
public class ObjectHistoryEntryContainer extends NamedHistoryEntryContainer {

	public ObjectHistoryEntryContainer(IPatchSetHistoryEntry<?, ?> parent, String name,
			List<IPatchSetHistoryEntry<?, ?>> entries) {
		super(parent, name, entries);
	}

	/**
	 * @param patchSet
	 *            the patch set to retrieve the match for.
	 * @return match of a {@link PatchSet}s comparison assigned to the given
	 *         {@link PatchSet}.
	 */
	public Match getMatch(PatchSet patchSet) {

		for (IPatchSetHistoryEntry<?, ?> subEntry : getSubEntries()) {

			Object value = subEntry.getValue(patchSet);
			Match match = getMatch(value);

			if (match != null) {
				return match;
			}
		}

		return null;
	}

	/**
	 * @param object
	 *            the value to retrieve the match for.
	 * @return the match related to the given object value.
	 */
	private Match getMatch(Object object) {

		if (object instanceof Match) {
			return (Match) object;
		} else if (object instanceof DiffWithSimilarity) {
			return ((DiffWithSimilarity) object).getDiff().getMatch();
		} else if (object instanceof Diff) {
			return ((Diff) object).getMatch();
		}
		return null;
	}

	/**
	 * @param patchSets
	 *            the collection of {@link PatchSet} to retrieve the matches
	 *            for.
	 * @return a map that contains all matches for the given {@link PatchSet}s
	 *         of this entry. Contains no match if no match exists for a
	 *         particular {@link PatchSet}.
	 */
	public Map<PatchSet, Match> getMatches(Collection<PatchSet> patchSets) {

		Map<PatchSet, Match> matchMap = new HashMap<PatchSet, Match>();
		Set<PatchSet> remainingPatchSets = new HashSet<>(patchSets);

		for (IPatchSetHistoryEntry<?, ?> subEntry : getSubEntries()) {

			for (PatchSet patchSet : remainingPatchSets) {

				Object value = subEntry.getValue(patchSet);
				Match match = getMatch(value);

				if (match != null) {
					matchMap.put(patchSet, match);
				}

			}

			remainingPatchSets.removeAll(matchMap.keySet());

			if (remainingPatchSets.isEmpty()) {
				break;
			}
		}

		return matchMap;
	}

}

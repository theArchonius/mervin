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
package at.bitandart.zoubek.mervin.review.explorer.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;

/**
 * An {@link ITreeItem} that represents a {@link Match}
 * 
 * @author Florian Zoubek
 *
 */
public class MatchTreeItem implements ITreeItem {

	private Match match;
	private Map<Match, MatchTreeItem> itemCache;
	private Object parent;
	private MatchDifferencesTreeItem diffItem;

	public MatchTreeItem(Match match, Object parent) {
		this.match = match;
		this.parent = parent;
		itemCache = new HashMap<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItemContainer#
	 * hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return !match.getSubmatches().isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItemContainer#
	 * getChildren()
	 */
	@Override
	public Object[] getChildren() {

		EList<Match> submatches = match.getSubmatches();
		List<ITreeItem> children = new ArrayList<>(submatches.size() + 1);

		Set<Match> cachedMatches = itemCache.keySet();
		HashSet<Match> matchesToRemove = new HashSet<>(cachedMatches);
		matchesToRemove.removeAll(submatches);

		if (!matchesToRemove.isEmpty()) {
			cachedMatches.removeAll(matchesToRemove);
		}

		for (Match subMatch : match.getSubmatches()) {
			MatchTreeItem matchTreeItem = itemCache.get(subMatch);
			if (matchTreeItem == null) {
				matchTreeItem = new MatchTreeItem(subMatch, this);
				itemCache.put(subMatch, matchTreeItem);
			}
			children.add(matchTreeItem);
		}
		List<Diff> differences = match.getDifferences();
		if (!differences.isEmpty()) {
			if (diffItem == null) {
				diffItem = new MatchDifferencesTreeItem(getMatch(), this);
			}
			children.add(diffItem);
		}

		return children.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItemContainer#
	 * getParent()
	 */
	@Override
	public Object getParent() {
		return parent;
	}

	public Match getMatch() {
		return match;
	}

	@Override
	public Object getElement() {
		return getMatch();
	}

}

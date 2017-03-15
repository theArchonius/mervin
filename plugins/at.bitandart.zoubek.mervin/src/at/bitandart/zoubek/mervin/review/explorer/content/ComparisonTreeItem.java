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
package at.bitandart.zoubek.mervin.review.explorer.content;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;

/**
 * An {@link ITreeItem} that represents a {@link Comparison} with a title. Note
 * that {@link #getElement()} returns an {@link ComparisonWithTitle}.
 * 
 * @author Florian Zoubek
 *
 */
public class ComparisonTreeItem implements ITreeItem {

	private ComparisonWithTitle titledComparison;
	private Map<Match, MatchTreeItem> itemCache;
	private List<ITreeItem> children;
	private Object parent;

	public ComparisonTreeItem(Comparison comparison, String title, Object parent) {
		this.parent = parent;
		this.titledComparison = new ComparisonWithTitle(comparison, title);
		itemCache = new HashMap<>();
		children = new LinkedList<>();
	}

	@Override
	public boolean hasChildren() {
		return !titledComparison.getComparison().getMatches().isEmpty();
	}

	@Override
	public Object[] getChildren() {

		EList<Match> matches = titledComparison.getComparison().getMatches();
		children.clear();

		Set<Match> cachedMatches = itemCache.keySet();
		HashSet<Match> matchesToRemove = new HashSet<>(cachedMatches);
		matchesToRemove.removeAll(matches);

		if (!matchesToRemove.isEmpty()) {
			cachedMatches.removeAll(matchesToRemove);
		}

		for (Match subMatch : matches) {
			MatchTreeItem matchTreeItem = itemCache.get(subMatch);
			if (matchTreeItem == null) {
				matchTreeItem = new MatchTreeItem(subMatch, this);
				itemCache.put(subMatch, matchTreeItem);
			}
			children.add(matchTreeItem);
		}

		return children.toArray();
	}

	@Override
	public Object getParent() {
		return parent;
	}

	public Comparison getComparison() {
		return titledComparison.getComparison();
	}

	@Override
	public Object getElement() {
		return titledComparison;
	}

}
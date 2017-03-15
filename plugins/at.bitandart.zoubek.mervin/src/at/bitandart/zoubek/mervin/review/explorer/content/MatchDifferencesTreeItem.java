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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;

/**
 * An {@link ITreeItem} representing the differences of a {@link Match}.
 * 
 * @author Florian Zoubek
 *
 */
public class MatchDifferencesTreeItem implements ITreeItem {

	private Match match;
	private Object parent;
	private Map<Diff, ITreeItem> itemCache;
	private List<ITreeItem> children;

	public MatchDifferencesTreeItem(Match match, Object parent) {
		this.match = match;
		this.parent = parent;
		itemCache = new HashMap<>();
		children = new LinkedList<ITreeItem>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItem#hasChildren(
	 * )
	 */
	@Override
	public boolean hasChildren() {
		return !match.getDifferences().isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItem#getChildren(
	 * )
	 */
	@Override
	public Object[] getChildren() {

		children.clear();
		EList<Diff> differences = match.getDifferences();

		Set<Diff> cachedDiffs = itemCache.keySet();
		Set<Diff> diffsToRemove = new HashSet<>(cachedDiffs);
		diffsToRemove.removeAll(differences);

		if (!diffsToRemove.isEmpty()) {
			cachedDiffs.removeAll(diffsToRemove);
		}

		for (Diff diff : differences) {
			ITreeItem treeItem = itemCache.get(diff);
			if (treeItem == null) {
				treeItem = new LeafTreeItem(diff, this);
				itemCache.put(diff, treeItem);
			}
			children.add(treeItem);
		}

		return children.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItem#getParent()
	 */
	@Override
	public Object getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.review.explorer.content.ITreeItem#getElement()
	 */
	@Override
	public Object getElement() {
		return "[Differences]";
	}

}

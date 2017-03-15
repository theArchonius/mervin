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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class that manages caching of {@link ITreeItem}s created for a list
 * of elements. This cache does not support caching of multiple tree items for
 * the same element.
 * 
 * @author Florian Zoubek
 *
 */
public class TreeItemCache<T> {

	private Map<T, ITreeItem> cacheMap;
	private ITreeItemFactory<T> itemFactory;

	/**
	 * @param itemFactory
	 *            the {@link ITreeItemFactory} used to create tree items for not
	 *            already cached elements.
	 */
	public TreeItemCache(ITreeItemFactory<T> itemFactory) {
		cacheMap = new HashMap<>();
		this.itemFactory = itemFactory;
	}

	/**
	 * invalidates all cached elements that are not part of the given
	 * collection.
	 * 
	 * @param currentElements
	 *            the elements that should remain in the cache.
	 */
	public void invalidateOldElements(Collection<T> currentElements) {

		Set<T> cachedElements = cacheMap.keySet();
		HashSet<T> elementsToRemove = new HashSet<>(cachedElements);
		elementsToRemove.removeAll(currentElements);

		if (!elementsToRemove.isEmpty()) {
			cachedElements.removeAll(elementsToRemove);
		}
	}

	/**
	 * adds the managed {@link ITreeItem}s that correspond to the given list of
	 * current elements to the given list. Not cached items will be created and
	 * added to the cache.
	 * 
	 * @param currentElements
	 *            the elements corresponding to the tree item to add.
	 * @param parent
	 *            the parent for not cached tree items that will be passed to
	 *            the factory that creates the tree items for not cached tree
	 *            items.
	 * @param treeItemList
	 *            the list to add the managed tree items to.
	 */
	public void addTreeItems(List<T> currentElements, Object parent, List<Object> treeItemList) {

		for (T element : currentElements) {
			ITreeItem treeItem = cacheMap.get(element);
			if (treeItem == null) {
				treeItem = itemFactory.createTreeItem(element, parent);
				cacheMap.put(element, treeItem);
			}
			treeItemList.add(treeItem);
		}
	}

	/**
	 * associates the given item with the given element.
	 * 
	 * @param element
	 *            the element to cache the item.
	 * @param item
	 *            the item cached for the given element.
	 */
	public void put(T element, ITreeItem item) {
		cacheMap.put(element, item);
	}

	/**
	 * @param element
	 * @return the cached tree item for the element or null if no element has
	 *         been cached.
	 */
	public ITreeItem get(T element) {
		return cacheMap.get(element);
	}

	/**
	 * clears the cache.
	 */
	public void clear() {
		cacheMap.clear();
	}

	/**
	 * Base interface for factories that create {@link ITreeItem}s.
	 * 
	 * @author Florian Zoubek
	 *
	 * @param <T>
	 */
	public static interface ITreeItemFactory<T> {

		/**
		 * 
		 * @param element
		 *            the element to create the item for.
		 * @param parent
		 *            the parent of the item to create.
		 * @return the new tree item.
		 */
		ITreeItem createTreeItem(T element, Object parent);

	}

}

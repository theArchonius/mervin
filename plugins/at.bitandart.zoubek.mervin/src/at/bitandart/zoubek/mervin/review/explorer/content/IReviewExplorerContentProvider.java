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

import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * Base interface for the {@link TreeViewer} content providers used in the
 * review explorer view of mervin.
 * 
 * @author Florian Zoubek
 *
 */
public interface IReviewExplorerContentProvider extends ITreeContentProvider {

	/**
	 * @param object
	 *            the object to find the patch set for.
	 * @return the patch set that contains the given object or null if it is not
	 *         contained in any patch set.
	 */
	public PatchSet getContainingPatchSet(Object object);

	/**
	 * @param eObject
	 *            the {@link EObject} to find the match for.
	 * @return the differences of the match of the given {@link EObject}.
	 */
	public List<Diff> getMatchDiffsFor(EObject eObject);

	/**
	 * @param eObject
	 *            the {@link EObject} to find the difference for.
	 * @return the differences that reference to given EObject.
	 */
	public List<Diff> getDiffsFor(EObject eObject);

	/**
	 * collects all {@link ITreeItem}s that are associated with the given
	 * element and returns them.
	 * 
	 * @param element
	 *            the element to search {@link ITreeItem} for.
	 * @return the set of {@link ITreeItem}s that are associated with the given
	 *         element.
	 */
	public Set<ITreeItem> getTreeItemsFor(Object element);

}

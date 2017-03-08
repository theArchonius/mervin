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
package at.bitandart.zoubek.mervin.review;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.google.common.base.Predicate;

/**
 * A class that reveals the highlighted elements of a given {@link TreeViewer}
 * by selecting the next or previous highlighted element. A depth first
 * traversal is used that starts always at the last selected element or at the
 * first element if no element it selected. The highlight state is passed as a
 * {@link Predicate} which is evaluated while walking through the tree. This
 * class currently supports only {@link TreeViewer}s with an assigned
 * {@link ITreeContentProvider}. Note that this class requires that
 * {@link ITreeContentProvider#getParent(Object)} always returns an object for
 * valid child elements. The behavior is undefined if this is not true.
 * 
 * @author Florian Zoubek
 *
 */
public class HighlightRevealer {

	public TreeViewer treeViewer;
	public Predicate<Object> highlightedElementPredicate;
	public Predicate<Object> highlightContainerPredicate;

	/**
	 * @param treeViewer
	 *            the tree viewer to reveal the highlight for.
	 * @param highlightedElementPredicate
	 *            the predicate the will be evaluated for each element that is
	 *            traversed forwards or backwards.
	 * @param highlightContainerPredicate
	 *            a predicate that determines if a given element may contain
	 *            highlights or not. This containment check includes highlights
	 *            in the whole child tree of the element. If this predicate
	 *            returns false, the child tree of the given element is ignored
	 *            while walking through the tree. This may be used in
	 *            combination with a cache to speed up the computation of the
	 *            next or previous highlighted element.
	 */
	public HighlightRevealer(TreeViewer treeViewer, Predicate<Object> highlightedElementPredicate,
			Predicate<Object> highlightContainerPredicate) {
		super();
		if (treeViewer == null || highlightedElementPredicate == null) {
			throw new IllegalArgumentException(
					"Invalid arguments, the tree viewer and the highlight predicate must be set");
		}
		this.treeViewer = treeViewer;
		this.highlightedElementPredicate = highlightedElementPredicate;
		this.highlightContainerPredicate = highlightContainerPredicate;
	}

	/**
	 * reveals the next highlighted element, if it exists.
	 */
	public void revealNextHighlight() {

		Object highlightedElement = findHighlightedElement(true);
		if (highlightedElement != null) {
			treeViewer.reveal(highlightedElement);
			treeViewer.setSelection(new StructuredSelection(highlightedElement));
		}

	}

	/**
	 * reveals the previous highlighted element, if it exists.
	 */
	public void revealPreviousHighlight() {

		Object highlightedElement = findHighlightedElement(false);
		if (highlightedElement != null) {
			treeViewer.reveal(highlightedElement);
			treeViewer.setSelection(new StructuredSelection(highlightedElement));
		}
	}

	/**
	 * @return true is another highlighted element exists while walking forward,
	 *         false otherwise.
	 */
	public boolean hasNextHighlight() {
		return findHighlightedElement(true) != null;
	}

	/**
	 * @return true is another highlighted element exists while walking
	 *         backward, false otherwise.
	 */
	public boolean hasPreviousHighlight() {
		return findHighlightedElement(false) != null;
	}

	/**
	 * finds the next highlighted element while traversing (depth-first) the
	 * {@link TreeViewer}s content tree in the given direction.
	 * 
	 * @param forward
	 *            the direction to walk through the tree. true is forward, false
	 *            is backward.
	 * @return the first highlighted element in the given direction or null if
	 *         none could be found.
	 */
	protected Object findHighlightedElement(boolean forward) {

		Object currentElement = getCurrentElement();
		Object input = treeViewer.getInput();

		if (currentElement == null) {
			/* No current element to start from */
			return null;
		}

		ITreeContentProvider treeContentProvider = getTreeContentProvider();

		if (treeContentProvider == null) {
			/*
			 * No content provider to provide the context of the current element
			 */
			return null;
		}

		return findHighlightedElement(currentElement, input, treeContentProvider, forward, false);
	}

	/**
	 * finds the next highlighted element while traversing (depth-first) the
	 * given element's child tree in the given direction.
	 * 
	 * @param parent
	 *            the element whose child tree will be traversed.
	 * @param forward
	 *            the direction to walk through the child tree. true is forward,
	 *            false is backward.
	 * @return the first highlighted element in the given direction or null if
	 *         none could be found.
	 */
	protected Object findHighlightedChildElement(Object parent, boolean forward) {

		if (!highlightContainerPredicate.apply(parent)) {
			/* the parent does not contain highlights, so skip the traversal */
			return null;
		}

		Object[] children = getChildren(parent);
		int startIndex = 0;
		int direction = 1;
		if (!forward) {
			startIndex = children.length - 1;
			direction = -1;
		}
		for (int childIndex = startIndex; childIndex < children.length && childIndex >= 0; childIndex += direction) {

			Object child = children[childIndex];

			if (forward && highlightedElementPredicate.apply(child)) {
				/*
				 * visit the parent before its children if its an forward
				 * traversal
				 */
				return child;
			}

			Object highlightedChildElement = findHighlightedChildElement(child, forward);
			if (highlightedChildElement != null) {
				return highlightedChildElement;
			}

			if (!forward && highlightedElementPredicate.apply(child)) {
				/*
				 * visit the parent after its children if its an backward
				 * traversal
				 */
				return child;
			}
		}
		return null;
	}

	/**
	 * 
	 * finds the next highlighted element while traversing (depth-first) the
	 * {@link TreeViewer}s content tree in the given direction.
	 * 
	 * @param currentElement
	 *            the element to start the traversal from.
	 * @param input
	 *            the input object of the tree viewer.
	 * @param treeContentProvider
	 *            the content provider of the tree viewer.
	 * @param forward
	 *            the direction to walk through the tree. true is forward, false
	 *            is backward.
	 * @param skipCurrentChildren
	 *            true if the current element's children should be skipped in
	 *            the traversal, false otherwise.
	 * @return the first highlighted element in the given direction or null if
	 *         none could be found.
	 */
	protected Object findHighlightedElement(Object currentElement, Object input,
			ITreeContentProvider treeContentProvider, boolean forward, boolean skipCurrentChildren) {

		if (forward && !skipCurrentChildren) {
			/* search for the next highlighted elements in the children */
			Object highlightedChildElement = findHighlightedChildElement(currentElement, true);
			if (highlightedChildElement != null) {
				return highlightedChildElement;
			}
		}

		/* retrieve the siblings */
		Object parent = treeContentProvider.getParent(currentElement);
		Object[] siblings = null;

		/*
		 * do not walk through the siblings if the parent is known not to
		 * contain highlights
		 */
		if (parent == null || highlightContainerPredicate.apply(parent)) {

			if (parent == null) {
				siblings = getElements(input);
			} else {
				siblings = getChildren(parent);
			}

			/* search for the current element in the siblings */
			int currentElementIndex = 0;
			for (Object sibling : siblings) {
				if (sibling == currentElement) {
					break;
				}
				currentElementIndex++;
			}

			if (currentElementIndex >= siblings.length) {
				/*
				 * the current element was not found within the expected sibling
				 * scope
				 */
				return null;
			}

			int nextDirectionElementIndex = currentElementIndex;
			if (forward) {
				nextDirectionElementIndex++;
			} else {
				nextDirectionElementIndex--;
			}

			if (nextDirectionElementIndex < siblings.length && nextDirectionElementIndex >= 0) {

				/*
				 * move in the given direction and return the first highlighted
				 * element
				 */

				int direction = 1;
				if (!forward) {
					direction = -1;
				}
				for (; nextDirectionElementIndex < siblings.length
						&& nextDirectionElementIndex >= 0; nextDirectionElementIndex += direction) {

					Object sibling = siblings[nextDirectionElementIndex];

					if (highlightedElementPredicate.apply(sibling)) {
						return sibling;
					}

					Object highlightedChildElement = findHighlightedChildElement(sibling, forward);
					if (highlightedChildElement != null) {
						return highlightedChildElement;
					}
				}
			}
		}

		if (parent != null) {
			if (!forward && highlightedElementPredicate.apply(parent)) {
				/* also check the parent if we traverse backwards */
				return parent;
			}
			return findHighlightedElement(parent, input, treeContentProvider, forward, true);
		}

		return null;
	}

	/**
	 * @param element
	 *            the element to retrieve the children for.
	 * @return the children of the given element as used by the
	 *         {@link TreeViewer} - sorted by the viewer's comparator if it
	 *         exists.
	 */
	protected Object[] getChildren(Object element) {

		ITreeContentProvider treeContentProvider = getTreeContentProvider();
		if (treeContentProvider != null) {
			return getSorted(treeContentProvider.getChildren(element));
		}

		return new Object[0];
	}

	/**
	 * @param element
	 *            the input element to retrieve the root elements for.
	 * @return the root elements of the given input as used by the
	 *         {@link TreeViewer} - sorted by the viewer's comparator if it
	 *         exists.
	 */
	protected Object[] getElements(Object element) {

		ITreeContentProvider treeContentProvider = getTreeContentProvider();
		if (treeContentProvider != null) {
			return getSorted(treeContentProvider.getElements(element));
		}

		return new Object[0];
	}

	/**
	 * sorts the given objects by with the {@link TreeViewer}'s
	 * {@link Comparator}, if it exists.
	 * 
	 * @param objects
	 *            the objects to sort.
	 * @return the sorted objects or the unsorted objects if no comparator is
	 *         assigned.
	 */
	protected Object[] getSorted(Object[] objects) {
		final ViewerComparator comparator = treeViewer.getComparator();
		if (comparator == null) {
			return objects;
		}

		Arrays.sort(objects, new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				return comparator.compare(treeViewer, o1, o2);
			}
		});

		return objects;
	}

	/**
	 * 
	 * @return the assigned {@link ITreeContentProvider} of the current
	 *         {@link TreeViewer} or null if no {@link ITreeContentProvider} has
	 *         been assigned.
	 */
	protected ITreeContentProvider getTreeContentProvider() {
		IContentProvider contentProvider = treeViewer.getContentProvider();
		if (contentProvider instanceof ITreeContentProvider) {
			return (ITreeContentProvider) contentProvider;
		}
		return null;
	}

	/**
	 * @return the reference element to start the traversal from.
	 */
	protected Object getCurrentElement() {

		ISelection selection = treeViewer.getSelection();
		if (selection instanceof IStructuredSelection) {

			Object[] selectedElements = ((IStructuredSelection) selection).toArray();
			if (selectedElements.length > 0) {
				return selectedElements[selectedElements.length - 1];
			}
		}

		Object[] elements = getElements(treeViewer.getInput());
		if (elements.length > 0) {
			return elements[0];
		}
		return null;
	}
}

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
package at.bitandart.zoubek.mervin.util.vis;

import java.util.Comparator;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 * A {@link ThreeWayTreeViewerComparator} that compares using the given
 * comparator for the comparison of the real objects. Note that the default
 * implementation of {@link ViewerComparator} compares the labels provided by
 * {@link ILabelProvider} or {@link Object#toString()} instead of the objects.
 * This is problematic for {@link IBaseLabelProvider}s that do not implement
 * {@link ILabelProvider} and cannot be sorted by their
 * {@link Object#toString()} representation.
 * 
 * @author Florian Zoubek
 *
 */
public class ThreeWayObjectTreeViewerComparator extends ThreeWayTreeViewerComparator {

	private Comparator<Object> objectComparator;

	public ThreeWayObjectTreeViewerComparator(TreeViewer viewer, TreeViewerColumn treeColumn,
			Comparator<Object> objectComparator) {
		super(viewer, treeColumn);
		this.objectComparator = objectComparator;
	}

	@Override
	public int compare(Viewer viewer, Object object1, Object object2) {
		return objectComparator.compare(object1, object2) * getDirectionFactor();
	}

}

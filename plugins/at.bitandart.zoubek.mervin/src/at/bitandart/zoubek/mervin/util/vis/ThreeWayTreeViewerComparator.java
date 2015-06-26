/*******************************************************************************
 * Copyright (c) 2015 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.util.vis;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * A {@link ViewerComparator} and {@link SelectionListener} that can be applied
 * to {@link TreeColumn}s to provide the default three way (none, ascending,
 * descending) sorting behavior based on default {@link ViewerComparator}
 * implementation.
 * 
 * @author Florian Zoubek
 *
 */
public class ThreeWayTreeViewerComparator extends ViewerComparator implements
		SelectionListener {

	private TreeViewer viewer;
	private TreeViewerColumn treeColumn;

	private int sortDirection = SWT.NONE;

	public ThreeWayTreeViewerComparator(TreeViewer viewer,
			TreeViewerColumn treeColumn) {
		this.viewer = viewer;
		this.treeColumn = treeColumn;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		return super.compare(viewer, e1, e2) * getDirectionFactor();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Tree tree = viewer.getTree();
		switch (sortDirection) {
		case SWT.UP:
			sortDirection = SWT.DOWN;
			break;
		case SWT.DOWN:
			sortDirection = SWT.NONE;
			break;
		default:
		case SWT.NONE:
			sortDirection = SWT.UP;
			break;
		}
		tree.setSortDirection(sortDirection);
		viewer.setComparator(this);
		tree.setSortColumn(treeColumn.getColumn());
		viewer.refresh();

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/**
	 * @return the factor that can be used to turn the default comparison result
	 *         in the correct sorting direction.
	 */
	protected int getDirectionFactor() {
		switch (sortDirection) {
		case SWT.UP:
			return 1;
		case SWT.DOWN:
			return -1;
		default:
		case SWT.NONE:
			return 0;
		}
	}

	public TreeViewer getViewer() {
		return viewer;
	}

	public TreeViewerColumn getTreeColumn() {
		return treeColumn;
	}
}
/*******************************************************************************
 * Copyright (c) 2015, 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.util.vis;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;

/**
 * Same as {@link ThreeWayTreeViewerComparator} but uses exclusively the values
 * of the given label provider and also provides support for numeric sorting for
 * {@link NumericColumnLabelProvider}s.
 * 
 * @author Florian Zoubek
 *
 */
public class ThreeWayLabelTreeViewerComparator extends ThreeWayTreeViewerComparator {

	private ILabelProvider columnLabelProvider;

	public ThreeWayLabelTreeViewerComparator(TreeViewer viewer, TreeViewerColumn treeColumn,
			ILabelProvider columnLabelProvider) {
		super(viewer, treeColumn);
		this.columnLabelProvider = columnLabelProvider;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if (columnLabelProvider instanceof NumericColumnLabelProvider) {
			NumericColumnLabelProvider numericColumnLabelProvider = (NumericColumnLabelProvider) columnLabelProvider;
			return Float.compare(numericColumnLabelProvider.getValue(e1), numericColumnLabelProvider.getValue(e2))
					* getDirectionFactor();
		}
		return super.compare(viewer, columnLabelProvider.getText(e1), columnLabelProvider.getText(e2));
	}

}
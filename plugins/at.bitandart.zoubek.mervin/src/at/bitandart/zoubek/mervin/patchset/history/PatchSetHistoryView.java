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
package at.bitandart.zoubek.mervin.patchset.history;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import at.bitandart.zoubek.mervin.review.ModelReviewEditorTrackingView;

/**
 * Shows the similarity of differences to differences of other patch sets.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetHistoryView extends ModelReviewEditorTrackingView {

	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.patchset.history";

	/**
	 * indicates if all SWT controls and viewers have been correctly set up
	 */
	private boolean viewInitialized = false;

	// JFace Viewers

	/**
	 * tree viewer that shows the patch set history of the model review
	 */
	private TreeViewer historyTreeViewer;

	// SWT Controls

	/**
	 * the main panel of this view, that contains all controls for this view
	 */
	private Composite mainPanel;

	@Inject
	public PatchSetHistoryView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());

		// initialize tree viewer

		historyTreeViewer = new TreeViewer(mainPanel, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);
		historyTreeViewer.setComparator(new ViewerComparator());
		historyTreeViewer
				.setContentProvider(new PatchSetHistoryContentProvider());
		Tree reviewTree = historyTreeViewer.getTree();
		reviewTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		reviewTree.setLinesVisible(false);
		reviewTree.setHeaderVisible(true);
	}

	@Override
	protected void updateValues() {
		// we cannot update the controls if they are not initialized yet
		if (viewInitialized) {
			// TODO
		}

	}

	/**
	 * A {@link ITreeContentProvider} that provides the objects for the patch
	 * set history.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public class PatchSetHistoryContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getParent(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			// TODO Auto-generated method stub
			return false;
		}

	}

}
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.review.ModelReviewEditorTrackingView;
import at.bitandart.zoubek.mervin.util.vis.ThreeWayLabelTreeViewerComparator;

/**
 * Shows the similarity of differences to differences of other patch sets.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetHistoryView extends ModelReviewEditorTrackingView {

	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.patchset.history";

	private static final String CONTAINER_NAME_MODEL_DIFFERENCES = "Model Differences";

	private static final String CONTAINER_NAME_DIAGRAM_DIFFERENCES = "Diagram Differences";
	
	/**
	 * indicates if all SWT controls and viewers have been correctly set up
	 */
	private boolean viewInitialized = false;
	
	private EContentAdapter patchSetHistoryViewUpdater;

	@Inject
	private ISimilarityHistoryService similarityHistoryService;

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
		patchSetHistoryViewUpdater = new UpdatePatchSetHistoryViewAdapter();

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

		// set up all columns of the tree

		// main label column
		TreeViewerColumn labelColumn = new TreeViewerColumn(historyTreeViewer,
				SWT.NONE);
		labelColumn.getColumn().setResizable(true);
		labelColumn.getColumn().setMoveable(true);
		labelColumn.getColumn().setText("Diff");
		labelColumn.getColumn().setWidth(200);

		DiffNameColumnLabelProvider labelColumnLabelProvider = new DiffNameColumnLabelProvider();
		labelColumn.setLabelProvider(labelColumnLabelProvider);
		labelColumn.getColumn().addSelectionListener(
				new ThreeWayLabelTreeViewerComparator(historyTreeViewer,
						labelColumn, labelColumnLabelProvider));
		
		
		// TODO create columns per patch set see method below
		
		
		viewInitialized = true;
	}
	
	private TreeViewerColumn createColumnForPatchSet(TreeViewer historyTreeViewer, PatchSet patchSet){
		TreeViewerColumn column = new TreeViewerColumn(historyTreeViewer,
				SWT.NONE);
		column.getColumn().setResizable(true);
		column.getColumn().setMoveable(true);
		column.getColumn().setText(patchSet.getId());
		column.getColumn().setWidth(50);
		
		// TODO add label provider
		
		return column;
		
	}

	@Override
	protected void updateValues() {
		// we cannot update the controls if they are not initialized yet
		if (viewInitialized) {
			
			ModelReview currentModelReview = getCurrentModelReview();
			
			if(currentModelReview != null){
			
				if(!currentModelReview.eAdapters().contains(patchSetHistoryViewUpdater)){
					currentModelReview.eAdapters().add(patchSetHistoryViewUpdater);
				}
	
				updateHistoryTree(currentModelReview);
			}
			
			historyTreeViewer.refresh();
			mainPanel.redraw();
			mainPanel.update();
		}

	}
	
	private void updateHistoryTree(ModelReview currentModelReview){

		EList<PatchSet> patchSets = currentModelReview.getPatchSets();
		// treat the right patch set as the "active" patch set
		PatchSet rightPatchSet = currentModelReview.getRightPatchSet();
		
		List<Object> historyData = new ArrayList<Object>(2);

		if(rightPatchSet != null){
			
			List<IPatchSetHistoryEntry<Diff, Double>> modelHistoryEntries = similarityHistoryService
					.createModelEntries(rightPatchSet, patchSets);
			historyData.add(new NamedHistoryEntryContainer(
					CONTAINER_NAME_MODEL_DIFFERENCES, modelHistoryEntries));
	
			List<IPatchSetHistoryEntry<Diff, Double>> diagramHistoryEntries = similarityHistoryService
					.createDiagramEntries(rightPatchSet, patchSets);
			historyData.add(new NamedHistoryEntryContainer(
					CONTAINER_NAME_DIAGRAM_DIFFERENCES, diagramHistoryEntries));
			
		}else{
			historyData.add("Please select an active patch set (right side of the comparison is the active patch set)");
		}

		historyTreeViewer.setInput(historyData);
	}
	
	private class UpdatePatchSetHistoryViewAdapter extends EContentAdapter{
		
		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			updateValues();
		}
		
	}

	/**
	 * A {@link ITreeContentProvider} that provides the objects for the patch
	 * set history. This content provider only supports one viewer per instance,
	 * so do not share a single instance of it across multiple viewers.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public class PatchSetHistoryContentProvider implements ITreeContentProvider {

		private List<NamedHistoryEntryContainer> cachedContainers = new LinkedList<PatchSetHistoryView.NamedHistoryEntryContainer>();

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			cachedContainers.clear();

			// cache all containers for a parent identification in
			// #getParent(Object)

			Object[] elements = getElements(newInput);
			for (Object object : elements) {
				if (object instanceof NamedHistoryEntryContainer) {
					cachedContainers.add((NamedHistoryEntryContainer) object);
				}
			}
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Collection<?>) {
				return ((Collection<?>) inputElement).toArray();
			}
			return new Object[0];
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof NamedHistoryEntryContainer) {
				return ((NamedHistoryEntryContainer) parentElement)
						.getEntries().toArray();
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			for (NamedHistoryEntryContainer container : cachedContainers) {
				if (container.getEntries().contains(element)) {
					return container;
				}
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return element instanceof NamedHistoryEntryContainer;
		}

	}

	/**
	 * A named container for {@link IPatchSetHistoryEntry}s for use within the
	 * UI.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class NamedHistoryEntryContainer {

		private List<? extends IPatchSetHistoryEntry<?, ?>> entries;

		private String name;

		public NamedHistoryEntryContainer(String name,
				List<? extends IPatchSetHistoryEntry<?, ?>> entries) {
			this.name = name;
			this.entries = entries;
		}

		public String getName() {
			return name;
		}

		public List<? extends IPatchSetHistoryEntry<?, ?>> getEntries() {
			return entries;
		}
	}

	/**
	 * A {@link ColumnLabelProvider} implementation that provides labels for
	 * {@link NamedHistoryEntryContainer}s, as well as
	 * {@link IPatchSetHistoryEntry} entry objects which are instances of EMF
	 * compare model elements.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class DiffNameColumnLabelProvider extends ColumnLabelProvider {

		private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

		public DiffNameColumnLabelProvider() {
			adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(
					new ComposedAdapterFactory(EMFCompareRCPPlugin.getDefault()
							.createFilteredAdapterFactoryRegistry()));
		}

		@Override
		public String getText(Object element) {

			if (element instanceof NamedHistoryEntryContainer) {

				return ((NamedHistoryEntryContainer) element).getName();

			} else if (element instanceof IPatchSetHistoryEntry) {

				Object entryObject = ((IPatchSetHistoryEntry<?, ?>) element)
						.getEntryObject();
				// delegate to the default EMF compare label provider
				return adapterFactoryLabelProvider.getText(entryObject);

			}
			return super.getText(element);
		}

		@Override
		public Image getImage(Object element) {

			if (element instanceof IPatchSetHistoryEntry) {

				Object entryObject = ((IPatchSetHistoryEntry<?, ?>) element)
						.getEntryObject();
				// delegate to the default EMF compare label provider
				return adapterFactoryLabelProvider.getImage(entryObject);

			}
			return super.getImage(element);
		}

	}

}
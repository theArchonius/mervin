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

import java.text.MessageFormat;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.review.ModelReviewEditorTrackingView;
import at.bitandart.zoubek.mervin.util.vis.HSB;
import at.bitandart.zoubek.mervin.util.vis.NumericColoredColumnLabelProvider;
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

	private TreeViewerColumn labelColumn;

	@Inject
	public PatchSetHistoryView() {
		patchSetHistoryViewUpdater = new UpdatePatchSetHistoryViewAdapter();

	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());

		// initialize tree viewer

		historyTreeViewer = new TreeViewer(mainPanel, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		historyTreeViewer.setComparator(new ViewerComparator());
		historyTreeViewer.setContentProvider(new PatchSetHistoryContentProvider());
		Tree reviewTree = historyTreeViewer.getTree();
		reviewTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		reviewTree.setLinesVisible(false);
		reviewTree.setHeaderVisible(true);

		// set up all columns of the tree

		labelColumn = new TreeViewerColumn(historyTreeViewer, SWT.NONE);
		labelColumn.getColumn().setResizable(true);
		labelColumn.getColumn().setMoveable(true);
		labelColumn.getColumn().setText("Diff");
		labelColumn.getColumn().setWidth(400);

		DiffNameColumnLabelProvider labelColumnLabelProvider = new DiffNameColumnLabelProvider();
		labelColumn.setLabelProvider(labelColumnLabelProvider);
		labelColumn.getColumn().addSelectionListener(
				new ThreeWayLabelTreeViewerComparator(historyTreeViewer, labelColumn, labelColumnLabelProvider));

		viewInitialized = true;
	}

	/**
	 * creates a new column in the given tree viewer that contains the history
	 * value for the given patch set.
	 * 
	 * @param historyTreeViewer
	 *            the {@link TreeViewer} that should contain the color.
	 * @param patchSet
	 *            the {@link PatchSet} associated with this column.
	 * @return the created column
	 */
	private TreeViewerColumn createColumnForPatchSet(TreeViewer historyTreeViewer, PatchSet patchSet) {
		TreeViewerColumn column = new TreeViewerColumn(historyTreeViewer, SWT.NONE);
		column.getColumn().setResizable(true);
		column.getColumn().setMoveable(true);
		column.getColumn().setText(patchSet.getId());
		column.getColumn().setWidth(50);

		// TODO make color configurable
		column.setLabelProvider(new PatchSetSimilarityHistoryLabelProvider(patchSet, new HSB(205.0f, 0.f, 1.0f),
				new HSB(205.0f, 0.59f, 0.32f), Display.getCurrent().getSystemColor(SWT.COLOR_WHITE),
				Display.getCurrent().getSystemColor(SWT.COLOR_BLACK)));

		return column;

	}

	@Override
	protected void updateValues() {
		// we cannot update the controls if they are not initialized yet
		if (viewInitialized) {

			ModelReview currentModelReview = getCurrentModelReview();

			if (currentModelReview != null) {

				if (!currentModelReview.eAdapters().contains(patchSetHistoryViewUpdater)) {
					currentModelReview.eAdapters().add(patchSetHistoryViewUpdater);
				}

				updateHistoryTree(currentModelReview);
			}

			historyTreeViewer.refresh();
			mainPanel.redraw();
			mainPanel.update();
		}

	}

	/**
	 * updates the history tree data and the columns of the tree.
	 * 
	 * @param currentModelReview
	 *            the model review to retrieve the data from.
	 */
	private void updateHistoryTree(ModelReview currentModelReview) {

		EList<PatchSet> patchSets = currentModelReview.getPatchSets();
		// treat the right patch set as the "active" patch set
		PatchSet rightPatchSet = currentModelReview.getRightPatchSet();

		List<Object> historyData = new ArrayList<Object>(2);

		if (rightPatchSet != null) {

			List<IPatchSetHistoryEntry<Diff, Double>> modelHistoryEntries = similarityHistoryService
					.createModelEntries(rightPatchSet, patchSets);
			historyData.add(new NamedHistoryEntryContainer(CONTAINER_NAME_MODEL_DIFFERENCES, modelHistoryEntries));

			List<IPatchSetHistoryEntry<Diff, Double>> diagramHistoryEntries = similarityHistoryService
					.createDiagramEntries(rightPatchSet, patchSets);
			historyData.add(new NamedHistoryEntryContainer(CONTAINER_NAME_DIAGRAM_DIFFERENCES, diagramHistoryEntries));

		} else {
			historyData.add("Please select an active patch set (right side of the comparison is the active patch set)");
		}

		// update columns

		for (TreeColumn column : historyTreeViewer.getTree().getColumns()) {
			// remove all columns except the label column
			if (column != labelColumn.getColumn()) {
				column.dispose();
			}
		}

		// create new columns for each patch set
		for (PatchSet patchSet : patchSets) {
			createColumnForPatchSet(historyTreeViewer, patchSet);
		}

		historyTreeViewer.setInput(historyData);
	}

	private class UpdatePatchSetHistoryViewAdapter extends EContentAdapter {

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
				return ((NamedHistoryEntryContainer) parentElement).getEntries().toArray();
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

		public NamedHistoryEntryContainer(String name, List<? extends IPatchSetHistoryEntry<?, ?>> entries) {
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
			adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(new ComposedAdapterFactory(
					EMFCompareRCPPlugin.getDefault().createFilteredAdapterFactoryRegistry()));
		}

		@Override
		public String getText(Object element) {

			if (element instanceof NamedHistoryEntryContainer) {

				NamedHistoryEntryContainer container = (NamedHistoryEntryContainer) element;
				return MessageFormat.format("{0} ({1})", container.getName(), container.entries.size());

			} else if (element instanceof IPatchSetHistoryEntry) {

				Object entryObject = ((IPatchSetHistoryEntry<?, ?>) element).getEntryObject();
				// delegate to the default EMF compare label provider
				return adapterFactoryLabelProvider.getText(entryObject);

			}
			return super.getText(element);
		}

		@Override
		public Image getImage(Object element) {

			if (element instanceof IPatchSetHistoryEntry) {

				Object entryObject = ((IPatchSetHistoryEntry<?, ?>) element).getEntryObject();
				// delegate to the default EMF compare label provider
				return adapterFactoryLabelProvider.getImage(entryObject);

			}
			return super.getImage(element);
		}

	}

	/**
	 * A {@link NumericColoredColumnLabelProvider} that provides labels for
	 * {@link IPatchSetHistoryEntry}s with a {@link Number} as value. Other
	 * value types and objects will be ignored.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class PatchSetSimilarityHistoryLabelProvider extends NumericColoredColumnLabelProvider {

		private PatchSet patchSet;

		public PatchSetSimilarityHistoryLabelProvider(PatchSet patchSet, HSB minHSB, HSB maxHSB, Color fgColor1,
				Color fgColor2) {
			super(minHSB, maxHSB, fgColor1, fgColor2);
			this.patchSet = patchSet;
		}

		@Override
		public float getMaxValue(Object element) {
			return 1.0f;
		}

		@Override
		public float getMinValue(Object element) {
			return 0.0f;
		}

		@Override
		public float getValue(Object element) {
			if (element instanceof IPatchSetHistoryEntry) {
				Object value = ((IPatchSetHistoryEntry<?, ?>) element).getValue(patchSet);
				if (value instanceof Number) {
					return ((Number) value).floatValue();
				}
			}
			return 0;
		}

		@Override
		public boolean hasValue(Object element) {
			if (element instanceof IPatchSetHistoryEntry) {
				Object value = ((IPatchSetHistoryEntry<?, ?>) element).getValue(patchSet);
				return value instanceof Number;
			}
			return false;
		}

	}

}
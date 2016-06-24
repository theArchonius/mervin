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
package at.bitandart.zoubek.mervin.patchset.history;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.IReviewHighlightServiceListener;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.review.HighlightHoveredTreeItemMouseTracker;
import at.bitandart.zoubek.mervin.review.HighlightMode;
import at.bitandart.zoubek.mervin.review.HighlightSelectionListener;
import at.bitandart.zoubek.mervin.review.IReviewHighlightProvidingPart;
import at.bitandart.zoubek.mervin.review.ModelReviewEditorTrackingView;
import at.bitandart.zoubek.mervin.swt.ProgressPanel;
import at.bitandart.zoubek.mervin.util.vis.ThreeWayObjectTreeViewerComparator;

/**
 * Shows the similarity of differences to differences of other patch sets.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetHistoryView extends ModelReviewEditorTrackingView implements IReviewHighlightProvidingPart {

	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.patchset.history";

	public static final String PART_TOOLBAR_ID = "at.bitandart.zoubek.mervin.toolbar.patchset.history";

	public enum VisibleDiffMode {
		ALL_DIFFS, NEW_PATCHSET_DIFFS, OLD_PATCHSET_DIFFS
	}

	/**
	 * indicates if all SWT controls and viewers have been correctly set up
	 */
	private boolean viewInitialized = false;

	private EContentAdapter patchSetHistoryViewUpdater;

	/**
	 * the current {@link HighlightMode}, never null;
	 */
	private HighlightMode highlightMode = HighlightMode.SELECTION;

	@Inject
	private ISimilarityHistoryService similarityHistoryService;
	@Inject
	private IReviewHighlightService highlightService;

	private IPatchSetHistoryEntryOrganizer entryOrganizer;

	private VisibleDiffMode visibleDiffs = VisibleDiffMode.ALL_DIFFS;

	private boolean mergeEqualDiffs = true;

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
	private Shell shell;

	@Inject
	private Display display;

	// Colors

	private Color progressBackgroundColor;
	private Color progressForegroundColor;

	private HighlightStyler highlightStyler;

	private ProgressPanel progressPanel;

	private PatchSetHistoryTreeUpdater currentUpdateThread;

	@Inject
	public PatchSetHistoryView() {
		patchSetHistoryViewUpdater = new UpdatePatchSetHistoryViewAdapter();

	}

	@Inject
	public void setEntryOrganizer(IPatchSetHistoryEntryOrganizer entryOrganizer) {
		this.entryOrganizer = entryOrganizer;
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		initializeColors();
		highlightStyler = new HighlightStyler(display);

		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());

		// progress information panel

		progressPanel = new ProgressPanel(mainPanel, SWT.BORDER);
		progressPanel.setBackground(progressBackgroundColor);
		progressPanel.setForeground(progressForegroundColor);
		progressPanel.setVisible(false);
		progressPanel.setLayout(new GridLayout());
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.exclude = true;
		progressPanel.setLayoutData(gridData);

		// initialize tree viewer

		historyTreeViewer = new TreeViewer(mainPanel, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		historyTreeViewer.setComparator(new ViewerComparator());
		historyTreeViewer.setContentProvider(new PatchSetHistoryContentProvider());
		historyTreeViewer.addSelectionChangedListener(new HighlightSelectionListener(this));
		Tree histroryTree = historyTreeViewer.getTree();
		histroryTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		histroryTree.setLinesVisible(false);
		histroryTree.setHeaderVisible(true);
		histroryTree.addMouseTrackListener(new HighlightHoveredTreeItemMouseTracker(this));

		// set up all columns of the tree

		labelColumn = new TreeViewerColumn(historyTreeViewer, SWT.NONE);
		labelColumn.getColumn().setResizable(true);
		labelColumn.getColumn().setMoveable(true);
		labelColumn.getColumn().setText("Diff");
		labelColumn.getColumn().setWidth(400);

		DiffNameColumnLabelProvider labelColumnLabelProvider = new DiffNameColumnLabelProvider();
		labelColumn.setLabelProvider(labelColumnLabelProvider);
		labelColumn.getColumn().addSelectionListener(
				new ThreeWayObjectTreeViewerComparator(historyTreeViewer, labelColumn, labelColumnLabelProvider));

		viewInitialized = true;

		// refresh the viewer highlights if highlighting is requested
		highlightService.addHighlightServiceListener(new IReviewHighlightServiceListener() {

			@Override
			public void elementRemoved(ModelReview review, Object element) {

				historyTreeViewer.refresh();

			}

			@Override
			public void elementAdded(ModelReview review, Object element) {

				historyTreeViewer.refresh();

			}
		});
	}

	/**
	 * initializes the colors for the controls of this view.
	 */
	private void initializeColors() {
		progressBackgroundColor = new Color(display, new RGB(255, 230, 128));
		progressForegroundColor = new Color(display, new RGB(212, 170, 0));
	}

	@Override
	protected void updateValues() {
		// we cannot update the controls if they are not initialized yet
		if (viewInitialized) {

			final ModelReview currentModelReview = getCurrentModelReview();

			if (currentModelReview != null) {

				if (!currentModelReview.eAdapters().contains(patchSetHistoryViewUpdater)) {
					currentModelReview.eAdapters().add(patchSetHistoryViewUpdater);
				}

				if (currentUpdateThread != null && currentUpdateThread.isAlive()) {
					/*
					 * update thread is already running - disable the progress
					 * panel update, create a new monitor for the progess panel
					 * and cancel the previous thread using the old progress
					 * monitor
					 */
					currentUpdateThread.setUpdateProgressPanel(false);
					IProgressMonitor oldMonitor = progressPanel.getProgressMonitor();
					progressPanel.createNewProgressMonitor();
					oldMonitor.setCanceled(true);
				}

				PatchSet activePatchSet = null;
				if (visibleDiffs == VisibleDiffMode.NEW_PATCHSET_DIFFS) {
					activePatchSet = currentModelReview.getRightPatchSet();
				} else if (visibleDiffs == VisibleDiffMode.OLD_PATCHSET_DIFFS) {
					activePatchSet = currentModelReview.getLeftPatchSet();
				}

				currentUpdateThread = new PatchSetHistoryTreeUpdater(currentModelReview, activePatchSet,
						mergeEqualDiffs, similarityHistoryService, entryOrganizer, historyTreeViewer, labelColumn,
						progressPanel, mainPanel);

				currentUpdateThread.start();

			} else {
				// no current review, so reset the input of the history tree
				historyTreeViewer.setInput(Collections.EMPTY_LIST);
			}

			mainPanel.redraw();
			mainPanel.update();
		}

	}

	@PreDestroy
	private void dispose() {
		highlightStyler.dispose();
		progressBackgroundColor.dispose();
		progressForegroundColor.dispose();
	}

	@Override
	public ModelReview getHighlightedModelReview() {
		return getCurrentModelReview();
	}

	@Override
	public HighlightMode getHighlightMode() {
		return highlightMode;
	}

	@Override
	public void setHighlightMode(HighlightMode highlightMode) {

		if (highlightMode != null) {
			this.highlightMode = highlightMode;
			highlightService.clearHighlights(getCurrentModelReview());
		}
	}

	@Override
	public IReviewHighlightService getReviewHighlightService() {
		return highlightService;
	}

	private class UpdatePatchSetHistoryViewAdapter extends EContentAdapter {

		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			Object feature = notification.getFeature();
			if (visibleDiffs != VisibleDiffMode.ALL_DIFFS
					&& (feature == ModelReviewPackage.Literals.MODEL_REVIEW__LEFT_PATCH_SET
							|| feature == ModelReviewPackage.Literals.MODEL_REVIEW__RIGHT_PATCH_SET))
				display.syncExec(new Runnable() {

					@Override
					public void run() {
						updateValues();
					}
				});
		}

	}

	/**
	 * @param visibleDiffMode
	 */
	public void setVisibleDiffs(VisibleDiffMode visibleDiffMode) {
		this.visibleDiffs = visibleDiffMode;
		updateValues();
	}

	/**
	 * 
	 * @param mergeEqualDiffs
	 *            true if equal diffs should be merged into a single entry,
	 *            false otherwise
	 */
	public void setMergeEqualDiffs(boolean mergeEqualDiffs) {
		this.mergeEqualDiffs = mergeEqualDiffs;
		updateValues();
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

		private List<IPatchSetHistoryEntry<?, ?>> cachedContainers = new LinkedList<IPatchSetHistoryEntry<?, ?>>();

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
				if (object instanceof IPatchSetHistoryEntry<?, ?>) {
					cachedContainers.add((IPatchSetHistoryEntry<?, ?>) object);
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
			if (parentElement instanceof IPatchSetHistoryEntry<?, ?>) {
				return ((IPatchSetHistoryEntry<?, ?>) parentElement).getSubEntries().toArray();
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			for (IPatchSetHistoryEntry<?, ?> container : cachedContainers) {
				if (container.getSubEntries().contains(element)) {
					return container;
				}
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof IPatchSetHistoryEntry<?, ?>) {
				return !((IPatchSetHistoryEntry<?, ?>) element).getSubEntries().isEmpty();
			}
			return false;
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
	private class DiffNameColumnLabelProvider extends StyledCellLabelProvider implements Comparator<Object> {

		private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

		public DiffNameColumnLabelProvider() {
			adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(new ComposedAdapterFactory(
					EMFCompareRCPPlugin.getDefault().createFilteredAdapterFactoryRegistry()));
		}

		@Override
		public void update(ViewerCell cell) {

			Object element = cell.getElement();
			List<Object> highlightedElements = highlightService.getHighlightedElements(getCurrentModelReview());
			StyledString text = new StyledString();
			if (isHighlighted(element, highlightedElements)) {
				text.append(getText(element), highlightStyler);
			} else {
				text.append(getText(element));
			}
			if (element instanceof NamedHistoryEntryContainer) {

				NamedHistoryEntryContainer container = (NamedHistoryEntryContainer) element;
				text.append(" ");
				text.append(MessageFormat.format("({0})", countTotalNumberOfEntries(container)),
						StyledString.COUNTER_STYLER);

			}

			cell.setText(text.getString());
			cell.setStyleRanges(text.getStyleRanges());
			cell.setImage(getImage(element));

			super.update(cell);

		}

		/**
		 * counts the total number of child entries of the given entry.
		 * 
		 * @param entry
		 *            the entry to count the child entries for.
		 * @return the number of child entries.
		 */
		private int countTotalNumberOfEntries(IPatchSetHistoryEntry<?, ?> entry) {
			List<IPatchSetHistoryEntry<?, ?>> subEntries = entry.getSubEntries();
			int size = subEntries.size();
			for (IPatchSetHistoryEntry<?, ?> subEntry : subEntries) {
				size += countTotalNumberOfEntries(subEntry);
			}
			return size;
		}

		/**
		 * checks if the given element should be highlighted or not.
		 * 
		 * @param element
		 *            the element to check.
		 * @param highlightedElements
		 *            the set of elements to highlight.
		 * @return true if the given element should be highlighted, false
		 *         otherwise.
		 */
		private boolean isHighlighted(Object element, List<Object> highlightedElements) {

			if (highlightedElements.contains(element)) {
				return true;
			}

			if (element instanceof View) {
				if (isHighlighted(((View) element).getElement(), highlightedElements)) {
					return true;
				}
			}

			if (element instanceof IPatchSetHistoryEntry) {
				if (isHighlighted(((IPatchSetHistoryEntry<?, ?>) element).getEntryObject(), highlightedElements)) {
					return true;
				}
				// TODO include values
			}

			if (element instanceof Diff) {
				Match match = ((Diff) element).getMatch();
				if (isHighlighted(match.getLeft(), highlightedElements)
						|| isHighlighted(match.getRight(), highlightedElements)) {
					return true;
				}
			}

			if (element instanceof NamedHistoryEntryContainer) {
				for (Object entry : ((NamedHistoryEntryContainer) element).getSubEntries()) {
					if (isHighlighted(entry, highlightedElements)) {
						return true;
					}
				}
			}

			return false;
		}

		public String getText(Object element) {

			if (element instanceof NamedHistoryEntryContainer) {

				NamedHistoryEntryContainer container = (NamedHistoryEntryContainer) element;
				return container.getName();

			} else if (element instanceof IPatchSetHistoryEntry) {

				Object entryObject = ((IPatchSetHistoryEntry<?, ?>) element).getEntryObject();
				// delegate to the default EMF compare label provider
				return adapterFactoryLabelProvider.getText(entryObject);

			}
			return element.toString();
		}

		public Image getImage(Object element) {

			if (element instanceof IPatchSetHistoryEntry) {

				Object entryObject = ((IPatchSetHistoryEntry<?, ?>) element).getEntryObject();
				// delegate to the default EMF compare label provider
				return adapterFactoryLabelProvider.getImage(entryObject);

			}
			return null;
		}

		@Override
		public int compare(Object object1, Object object2) {
			String text1 = getText(object1);
			String text2 = getText(object2);
			return Policy.getComparator().compare(text1, text2);
		}

	}

}
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
package at.bitandart.zoubek.mervin.patchset.history;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeColumn;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;
import at.bitandart.zoubek.mervin.swt.ProgressPanel;
import at.bitandart.zoubek.mervin.util.vis.HSB;
import at.bitandart.zoubek.mervin.util.vis.ThreeWayLabelTreeViewerComparator;

/**
 * A thread that updates the Patch Set History data and assigns it to the given
 * tree viewer.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetHistoryTreeUpdater extends Thread {

	private ModelReview currentModelReview;
	private PatchSet activePatchSet;
	private boolean mergeEqualDiffs;
	private ISimilarityHistoryService similarityHistoryService;
	private IPatchSetHistoryEntryOrganizer organizer;
	private TreeViewer historyTreeViewer;
	private TreeViewerColumn labelColumn;
	private ProgressPanel progressPanel;
	private Composite mainPanel;
	private boolean updateProgressPanel;

	/**
	 * 
	 * @param currentModelReview
	 *            the model review instance to retrieve the data from.
	 * @param activePatchSet
	 *            the patch set whose diffs are shown, pass null if all diffs of
	 *            all patchsets should be shown.
	 * @param mergeEqualDiffs
	 *            true if equal diffs should be merged into a single entry,
	 *            false if an entry should be created for each diff in all patch
	 *            sets.
	 * @param similarityHistoryService
	 *            the similarity service instance used to calculate the history.
	 * @param organizer
	 *            the organizer used to group and order the history entries.
	 * @param historyTreeViewer
	 *            the tree viewer that contains the history data.
	 * @param labelColumn
	 *            the label column that will not be deleted during the update of
	 *            the treeviewer.
	 * @param progressPanel
	 *            the progress panel to show while the update is in progress.
	 * @param mainPanel
	 *            the main panel that needs to be layouted when the progress
	 *            panel is shown or hidden.
	 */
	public PatchSetHistoryTreeUpdater(ModelReview currentModelReview, PatchSet activePatchSet, boolean mergeEqualDiffs,
			ISimilarityHistoryService similarityHistoryService, IPatchSetHistoryEntryOrganizer organizer,
			TreeViewer historyTreeViewer, TreeViewerColumn labelColumn, ProgressPanel progressPanel,
			Composite mainPanel) {
		super();
		this.currentModelReview = currentModelReview;
		this.activePatchSet = activePatchSet;
		this.mergeEqualDiffs = mergeEqualDiffs;
		this.similarityHistoryService = similarityHistoryService;
		this.organizer = organizer;
		this.historyTreeViewer = historyTreeViewer;
		this.labelColumn = labelColumn;
		this.progressPanel = progressPanel;
		this.mainPanel = mainPanel;
		this.updateProgressPanel = true;
	}

	@Override
	public void run() {
		Display display = mainPanel.getDisplay();

		setProgressPanelVisibility(display, true);

		IProgressMonitor monitor = progressPanel.getProgressMonitor();
		updateHistoryTree(currentModelReview, monitor, display);
		done(monitor);

		setProgressPanelVisibility(display, false);
	}

	/**
	 * shows the progress panel if updating the progress panel is enabled.
	 */
	public void showProgressPanel() {

		Display display = mainPanel.getDisplay();
		setProgressPanelVisibility(display, true);
	}

	/**
	 * hides the progress panel if updating the progress panel is enabled.
	 */
	public void hideProgressPanel() {

		Display display = mainPanel.getDisplay();
		setProgressPanelVisibility(display, false);
	}

	/**
	 * updates the visibility of the progress panel if updating the progress
	 * panel is enabled.
	 * 
	 * @param display
	 *            the display the progress panel is contained in.
	 * @param visible
	 *            the visibility of the progress panel.
	 */
	private void setProgressPanelVisibility(Display display, final boolean visible) {

		if (updateProgressPanel) {
			display.syncExec(new Runnable() {

				@Override
				public void run() {
					progressPanel.setVisible(visible);
					((GridData) progressPanel.getLayoutData()).exclude = !visible;
					mainPanel.layout();
				}
			});
		}
	}

	/**
	 * updates the history tree data and the columns of the tree.
	 * 
	 * @param currentModelReview
	 *            the model review to retrieve the data from.
	 */
	private void updateHistoryTree(ModelReview currentModelReview, IProgressMonitor monitor, Display display) {

		monitor.beginTask("Updating Patch Set History...", 3);

		final EList<PatchSet> patchSets = currentModelReview.getPatchSets();

		final Collection<Object> historyData;

		if (activePatchSet != null) {

			List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> modelHistoryEntries = similarityHistoryService
					.createModelEntries(activePatchSet, patchSets, mergeEqualDiffs);

			if (monitor.isCanceled()) {
				return;
			}
			worked(monitor, 1);

			List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> diagramHistoryEntries = similarityHistoryService
					.createDiagramEntries(activePatchSet, patchSets, mergeEqualDiffs);
			historyData = organizer.groupPatchSetHistoryEntries(modelHistoryEntries, diagramHistoryEntries);

		} else {

			List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> modelHistoryEntries = similarityHistoryService
					.createModelEntries(patchSets, mergeEqualDiffs);

			if (monitor.isCanceled()) {
				return;
			}
			worked(monitor, 1);

			List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> diagramHistoryEntries = similarityHistoryService
					.createDiagramEntries(patchSets, mergeEqualDiffs);
			historyData = organizer.groupPatchSetHistoryEntries(modelHistoryEntries, diagramHistoryEntries);
		}

		if (monitor.isCanceled()) {
			return;
		}
		worked(monitor, 1);

		// update columns

		display.syncExec(new Runnable() {

			@Override
			public void run() {

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
				historyTreeViewer.refresh();
			}
		});

	}

	/**
	 * updates the monitor with the given amount of work if updating the
	 * progress panel is enabled.
	 * 
	 * @param monitor
	 *            the monitor to update.
	 * @param workDone
	 *            the work done.
	 */
	private void worked(IProgressMonitor monitor, int workDone) {
		if (updateProgressPanel) {
			monitor.worked(workDone);
		}
	}

	/**
	 * notifies the monitor that the work has been done if updating the progress
	 * panel is enabled.
	 * 
	 * @param monitor
	 *            the monitor to update.
	 */
	private void done(IProgressMonitor monitor) {
		if (updateProgressPanel) {
			monitor.done();
		}
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
		PatchSetSimilarityHistoryLabelProvider labelProvider = new PatchSetSimilarityHistoryLabelProvider(patchSet,
				new HSB(205.0f, 0.f, 1.0f), new HSB(205.0f, 0.59f, 0.32f),
				Display.getCurrent().getSystemColor(SWT.COLOR_WHITE),
				Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		column.setLabelProvider(labelProvider);

		column.getColumn()
				.addSelectionListener(new ThreeWayLabelTreeViewerComparator(historyTreeViewer, column, labelProvider));

		return column;

	}

	/**
	 * enables or disable the update of the assigned progress panel.
	 * 
	 * @param updateProgressPanel
	 *            true if the progress panel should be updated, false otherwise.
	 */
	public void setUpdateProgressPanel(boolean updateProgressPanel) {
		this.updateProgressPanel = updateProgressPanel;
	}
}
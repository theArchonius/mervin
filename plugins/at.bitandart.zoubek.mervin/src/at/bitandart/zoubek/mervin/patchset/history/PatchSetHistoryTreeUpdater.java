/*******************************************************************************
 * Copyright (c) 2016, 2017 Florian Zoubek.
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
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeColumn;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;
import at.bitandart.zoubek.mervin.patchset.history.organizers.IPatchSetHistoryEntryOrganizer;
import at.bitandart.zoubek.mervin.swt.ProgressPanel;
import at.bitandart.zoubek.mervin.swt.ProgressPanelOperationThread;
import at.bitandart.zoubek.mervin.util.vis.HSB;
import at.bitandart.zoubek.mervin.util.vis.ThreeWayLabelTreeViewerComparator;

/**
 * A {@link ProgressPanelOperationThread} that updates the Patch Set History
 * data and assigns it to the given tree viewer.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetHistoryTreeUpdater extends ProgressPanelOperationThread {

	private ModelReview currentModelReview;
	private PatchSet activePatchSet;
	private boolean mergeEqualDiffs;
	private ISimilarityHistoryService similarityHistoryService;
	private IPatchSetHistoryEntryOrganizer organizer;
	private TreeViewer historyTreeViewer;
	private TreeViewerColumn labelColumn;

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
			Composite mainPanel, Logger logger) {
		super(progressPanel, mainPanel, logger);
		this.currentModelReview = currentModelReview;
		this.activePatchSet = activePatchSet;
		this.mergeEqualDiffs = mergeEqualDiffs;
		this.similarityHistoryService = similarityHistoryService;
		this.organizer = organizer;
		this.historyTreeViewer = historyTreeViewer;
		this.labelColumn = labelColumn;
	}

	@Override
	protected void runOperation() {
		updateHistoryTree(currentModelReview, getProgressMonitor(), getDisplay());
	}

	/**
	 * updates the history tree data and the columns of the tree.
	 * 
	 * @param currentModelReview
	 *            the model review to retrieve the data from.
	 */
	private void updateHistoryTree(ModelReview currentModelReview, IProgressMonitor monitor, Display display) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Updating Patch Set History...", 3);

		final EList<PatchSet> patchSets = currentModelReview.getPatchSets();

		final Collection<Object> historyData;

		if (activePatchSet != null) {

			List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> modelHistoryEntries = similarityHistoryService
					.createModelEntries(activePatchSet, patchSets, mergeEqualDiffs, subMonitor.newChild(1));

			if (monitor.isCanceled()) {
				return;
			}

			List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> diagramHistoryEntries = similarityHistoryService
					.createDiagramEntries(activePatchSet, patchSets, mergeEqualDiffs, subMonitor.newChild(1));
			historyData = organizer.groupPatchSetHistoryEntries(modelHistoryEntries, diagramHistoryEntries);

		} else {

			List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> modelHistoryEntries = similarityHistoryService
					.createModelEntries(patchSets, mergeEqualDiffs, subMonitor.newChild(1));

			if (monitor.isCanceled()) {
				return;
			}

			List<IPatchSetHistoryEntry<Diff, DiffWithSimilarity>> diagramHistoryEntries = similarityHistoryService
					.createDiagramEntries(patchSets, mergeEqualDiffs, subMonitor.newChild(1));
			historyData = organizer.groupPatchSetHistoryEntries(modelHistoryEntries, diagramHistoryEntries);
		}

		if (monitor.isCanceled()) {
			return;
		}
		subMonitor.worked(1);

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
}

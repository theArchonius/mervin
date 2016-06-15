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

import java.util.ArrayList;
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
import at.bitandart.zoubek.mervin.swt.ProgressPanel;
import at.bitandart.zoubek.mervin.util.vis.HSB;

/**
 * A thread that updates the Patch Set History data and assigns it to the given
 * tree viewer.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetHistoryTreeUpdater extends Thread {

	private ModelReview currentModelReview;
	private ISimilarityHistoryService similarityHistoryService;
	private IPatchSetHistoryEntryOrganizer organizer;
	private TreeViewer historyTreeViewer;
	private TreeViewerColumn labelColumn;
	private ProgressPanel progressPanel;
	private Composite mainPanel;

	/**
	 * 
	 * @param currentModelReview
	 *            the model review instance to retrieve the data from.
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
	public PatchSetHistoryTreeUpdater(ModelReview currentModelReview,
			ISimilarityHistoryService similarityHistoryService, IPatchSetHistoryEntryOrganizer organizer,
			TreeViewer historyTreeViewer, TreeViewerColumn labelColumn, ProgressPanel progressPanel,
			Composite mainPanel) {
		super();
		this.currentModelReview = currentModelReview;
		this.similarityHistoryService = similarityHistoryService;
		this.organizer = organizer;
		this.historyTreeViewer = historyTreeViewer;
		this.labelColumn = labelColumn;
		this.progressPanel = progressPanel;
		this.mainPanel = mainPanel;
	}

	@Override
	public void run() {
		Display display = mainPanel.getDisplay();

		display.syncExec(new Runnable() {

			@Override
			public void run() {
				progressPanel.setVisible(true);
				((GridData) progressPanel.getLayoutData()).exclude = false;
				mainPanel.layout();
			}
		});

		IProgressMonitor monitor = progressPanel.getProgressMonitor();
		updateHistoryTree(currentModelReview, monitor, display);
		monitor.done();

		display.syncExec(new Runnable() {

			@Override
			public void run() {
				progressPanel.setVisible(false);
				((GridData) progressPanel.getLayoutData()).exclude = true;
				mainPanel.layout();
			}
		});
	}

	/**
	 * updates the history tree data and the columns of the tree.
	 * 
	 * @param currentModelReview
	 *            the model review to retrieve the data from.
	 */
	private void updateHistoryTree(ModelReview currentModelReview, IProgressMonitor monitor, Display display) {

		monitor.beginTask("Updating Patch Set History...", IProgressMonitor.UNKNOWN);

		final EList<PatchSet> patchSets = currentModelReview.getPatchSets();
		// treat the right patch set as the "active" patch set
		PatchSet rightPatchSet = currentModelReview.getRightPatchSet();

		final Collection<Object> historyData;

		if (rightPatchSet != null) {

			List<IPatchSetHistoryEntry<Diff, Double>> modelHistoryEntries = similarityHistoryService
					.createModelEntries(rightPatchSet, patchSets);

			List<IPatchSetHistoryEntry<Diff, Double>> diagramHistoryEntries = similarityHistoryService
					.createDiagramEntries(rightPatchSet, patchSets);
			historyData = organizer.groupPatchSetHistoryEntries(modelHistoryEntries, diagramHistoryEntries);

		} else {
			historyData = new ArrayList<Object>(1);
			historyData.add("Please select an active patch set (right side of the comparison is the active patch set)");
		}

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
		column.setLabelProvider(new PatchSetSimilarityHistoryLabelProvider(patchSet, new HSB(205.0f, 0.f, 1.0f),
				new HSB(205.0f, 0.59f, 0.32f), Display.getCurrent().getSystemColor(SWT.COLOR_WHITE),
				Display.getCurrent().getSystemColor(SWT.COLOR_BLACK)));

		return column;

	}
}

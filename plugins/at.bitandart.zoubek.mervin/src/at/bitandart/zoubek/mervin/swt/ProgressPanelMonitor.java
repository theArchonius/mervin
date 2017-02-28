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
package at.bitandart.zoubek.mervin.swt;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * An {@link IProgressMonitor} implementation tied to this progress panel
 * control.
 * 
 * @author Florian Zoubek
 *
 */
class ProgressPanelMonitor implements IProgressMonitor {

	/**
	 * 
	 */
	private ProgressPanel progressPanel;

	/**
	 * @param progressPanel
	 */
	ProgressPanelMonitor(ProgressPanel progressPanel) {
		this.progressPanel = progressPanel;
	}

	private String taskName;

	private int workState;

	private int totalWork;

	private boolean canceled = false;

	@Override
	public void worked(int work) {
		internalWorked(work);
	}

	@Override
	public void subTask(String name) {
		// not supported (yet)
	}

	@Override
	public void setTaskName(String name) {
		taskName = name;
	}

	@Override
	public void setCanceled(boolean value) {
		canceled = value;

	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void internalWorked(double work) {
		workState += work;
		if (workState > totalWork) {
			workState = totalWork;
		}

		if (hasValidPanel()) {
			this.progressPanel.getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {

					if (ProgressPanelMonitor.this.hasValidPanel()) {
						ProgressPanelMonitor.this.progressPanel.getProgressBar().setSelection(workState);
					}

				}
			});
		}

	}

	@Override
	public void done() {

		workState = 0;
		canceled = false;
		if (hasValidPanel()) {
			this.progressPanel.getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {

					if (ProgressPanelMonitor.this.hasValidPanel()) {

						ProgressPanelMonitor.this.progressPanel.getProgressLabel().setText("");

						ProgressPanelMonitor.this.progressPanel
								.getProgressBarLayout().topControl = ProgressPanelMonitor.this.progressPanel
										.getProgressBar();
						ProgressPanelMonitor.this.progressPanel.getProgressBar().setSelection(workState);
						ProgressPanelMonitor.this.progressPanel.getProgressBar().setMaximum(0);

						ProgressPanelMonitor.this.progressPanel.getProgressBarPanel().layout();

					}
				}
			});
		}

	}

	@Override
	public void beginTask(String name, int totalWork) {

		taskName = name;
		this.totalWork = totalWork;

		if (hasValidPanel()) {
			this.progressPanel.getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {

					if (ProgressPanelMonitor.this.hasValidPanel()) {

						ProgressPanelMonitor.this.progressPanel.getProgressLabel().setText(taskName);
						if (ProgressPanelMonitor.this.totalWork == IProgressMonitor.UNKNOWN) {
							ProgressPanelMonitor.this.progressPanel
									.getProgressBarLayout().topControl = ProgressPanelMonitor.this.progressPanel
											.getIndeterminedProgressBar();
						} else {
							ProgressPanelMonitor.this.progressPanel
									.getProgressBarLayout().topControl = ProgressPanelMonitor.this.progressPanel
											.getProgressBar();
							ProgressPanelMonitor.this.progressPanel.getProgressBar().setSelection(workState);
							ProgressPanelMonitor.this.progressPanel.getProgressBar()
									.setMaximum(ProgressPanelMonitor.this.totalWork);
						}

						ProgressPanelMonitor.this.progressPanel.getProgressBarPanel().layout();

					}
				}
			});
		}

	}

	/**
	 * @return true if a valid panel has been assigned, false otherwise.
	 */
	public boolean hasValidPanel() {
		return this.progressPanel != null && !this.progressPanel.isDisposed();
	}

	public void setProgressPanel(ProgressPanel progressPanel) {
		this.progressPanel = progressPanel;
	}
}
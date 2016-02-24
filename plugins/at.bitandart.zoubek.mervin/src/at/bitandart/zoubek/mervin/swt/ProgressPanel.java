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
package at.bitandart.zoubek.mervin.swt;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

/**
 * A progress panel that shows a progress bar and label for tasks and provides
 * an {@link IProgressMonitor}.
 * 
 * @author Florian Zoubek
 *
 */
public class ProgressPanel extends Composite {

	private Label progressLabel;
	private StackLayout progressBarLayout;
	private ProgressBar progressBar;
	private ProgressBar indeterminedProgressBar;

	private IProgressMonitor monitor;

	private Composite progressBarPanel;

	public ProgressPanel(Composite parent, int style) {
		super(parent, style);

		setLayout(new GridLayout());

		progressLabel = new Label(this, SWT.NONE);
		progressLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		progressBarPanel = new Composite(this, SWT.NONE);
		progressBarPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		progressBarLayout = new StackLayout();
		progressBarPanel.setLayout(progressBarLayout);

		progressBar = new ProgressBar(progressBarPanel, SWT.NONE);
		progressBarLayout.topControl = progressBar;

		indeterminedProgressBar = new ProgressBar(progressBarPanel, SWT.INDETERMINATE);

	}

	/**
	 * @return the {@link IProgressMonitor} registered to this control.
	 */
	public IProgressMonitor getProgressMonitor() {
		if (monitor == null) {
			monitor = new ProgressPanelMonitor();
		}
		return monitor;
	}

	/**
	 * An {@link IProgressMonitor} implementation tied to this progress panel
	 * control.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ProgressPanelMonitor implements IProgressMonitor {

		private String taskName;

		private int workState;

		private int totalWork;

		private boolean canceled;

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

			if (!isDisposed()) {
				getDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						progressBar.setSelection(workState);

					}
				});
			}

		}

		@Override
		public void done() {

			workState = 0;
			if (!isDisposed()) {
				getDisplay().syncExec(new Runnable() {

					@Override
					public void run() {

						progressLabel.setText("");

						progressBarLayout.topControl = progressBar;
						progressBar.setSelection(workState);
						progressBar.setMaximum(0);

						progressBarPanel.layout();

					}
				});
			}

		}

		@Override
		public void beginTask(String name, int totalWork) {

			taskName = name;
			this.totalWork = totalWork;

			if (!isDisposed()) {
				getDisplay().syncExec(new Runnable() {

					@Override
					public void run() {

						progressLabel.setText(taskName);
						if (ProgressPanelMonitor.this.totalWork == IProgressMonitor.UNKNOWN) {
							progressBarLayout.topControl = indeterminedProgressBar;
						} else {
							progressBarLayout.topControl = progressBar;
							progressBar.setSelection(workState);
							progressBar.setMaximum(ProgressPanelMonitor.this.totalWork);
						}

						progressBarPanel.layout();

					}
				});
			}

		}
	}

}

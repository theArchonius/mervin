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
			createNewProgressMonitor();
		}
		return monitor;
	}

	/**
	 * creates a new {@link IProgressMonitor} and registers it to this control.
	 * Any existing registered {@link IProgressMonitor} will be unregistered and
	 * won't affect this control any more.
	 * 
	 * @return the new {@link IProgressMonitor}
	 */
	public IProgressMonitor createNewProgressMonitor() {
		if (monitor != null && monitor instanceof ProgressPanelMonitor) {
			((ProgressPanelMonitor) monitor).setProgressPanel(null);
		}
		return monitor = new ProgressPanelMonitor(this);
	}

	protected Label getProgressLabel() {
		return progressLabel;
	}

	protected StackLayout getProgressBarLayout() {
		return progressBarLayout;
	}

	protected ProgressBar getProgressBar() {
		return progressBar;
	}

	protected ProgressBar getIndeterminedProgressBar() {
		return indeterminedProgressBar;
	}

	protected Composite getProgressBarPanel() {
		return progressBarPanel;
	}

}

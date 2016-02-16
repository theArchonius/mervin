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
package at.bitandart.zoubek.mervin.swt.comments.examples;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import at.bitandart.zoubek.mervin.swt.ProgressPanel;

/**
 * Demonstrates the usage of {@link ProgressPanel}.
 * 
 * @author Florian Zoubek
 *
 */
public class ProgressPanelExample {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		final ProgressPanel progressPanel = new ProgressPanel(shell, SWT.NONE);
		GridData progressGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		progressGridData.exclude = true;
		progressPanel.setVisible(false);
		progressPanel.setLayoutData(progressGridData);

		final Button startTaskButton = new Button(shell, SWT.PUSH);
		startTaskButton.setText("Start long running task");
		startTaskButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		startTaskButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				final IProgressMonitor monitor = progressPanel.getProgressMonitor();
				startTaskButton.setEnabled(false);
				progressPanel.setVisible(true);
				((GridData) progressPanel.getLayoutData()).exclude = false;
				shell.layout();
				shell.pack();

				new Thread(new Runnable() {

					@Override
					public void run() {
						monitor.beginTask("unknown work...", IProgressMonitor.UNKNOWN);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}

						monitor.beginTask("known work...", 20);
						for (int i = 0; i < 20; i++) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
							}
							monitor.worked(1);
						}
						monitor.done();
						progressPanel.getDisplay().syncExec(new Runnable() {

							@Override
							public void run() {
								((GridData) progressPanel.getLayoutData()).exclude = true;
								progressPanel.setVisible(false);
								startTaskButton.setEnabled(true);
								shell.layout();
								shell.pack();
							}
						});

					}
				}).start();

			}

		});

		shell.open();
		shell.pack();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}
}

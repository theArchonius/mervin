/*******************************************************************************
 * Copyright (c) 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.swt;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * A {@link Thread} that shows and hides the progress panel while it is running.
 * 
 * @author Florian Zoubek
 *
 */
@SuppressWarnings("restriction")
public class ProgressPanelOperationThread extends Thread {

	private ProgressPanel progressPanel;
	private Composite mainPanel;
	private boolean updateProgressPanel;
	private IProgressMonitor progressMonitor;
	private Logger logger;
	private IRunnableWithProgress runnable;

	/**
	 * @param runnable
	 * @param progressPanel
	 *            the progress panel to show while the update is in progress.
	 * @param mainPanel
	 *            the main panel that needs to be layouted when the progress
	 *            panel is shown or hidden.
	 */
	public ProgressPanelOperationThread(IRunnableWithProgress runnable, ProgressPanel progressPanel,
			Composite mainPanel, Logger logger) {
		this.progressPanel = progressPanel;
		this.mainPanel = mainPanel;
		this.updateProgressPanel = true;
		this.progressMonitor = progressPanel.getProgressMonitor();
		this.logger = logger;
		this.runnable = runnable;
	}

	@Override
	public void run() {

		showProgressPanel();
		try {
			runnable.run(getProgressMonitor());
		} catch (OperationCanceledException e) {
			logger.warn(e, "Operation has been cancelled.");
		} catch (InvocationTargetException e) {
			logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);
		}
		done(progressMonitor);

		hideProgressPanel();
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
	 * updates the monitor with the given amount of work if updating the
	 * progress panel is enabled.
	 * 
	 * @param monitor
	 *            the monitor to update.
	 * @param workDone
	 *            the work done.
	 */
	protected void worked(IProgressMonitor monitor, int workDone) {
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
	protected void done(IProgressMonitor monitor) {
		if (updateProgressPanel) {
			monitor.done();
		}
	}

	/**
	 * enables or disable the update of the assigned progress panel.
	 * 
	 * @param updateProgressPanel
	 *            true if the progress panel should be updated, false otherwise.
	 */
	protected void setUpdateProgressPanel(boolean updateProgressPanel) {
		this.updateProgressPanel = updateProgressPanel;
	}

	/**
	 * @return the progress monitor to use for this operation.
	 */
	protected IProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	/**
	 * @return the {@link Display} that is associated with the progress panel.
	 */
	protected Display getDisplay() {
		return mainPanel.getDisplay();
	}

	/**
	 * hides the progress panel, cancels the operation and disconnects this
	 * thread form the progress panel entirely. The thread may continue running
	 * until the implementing class reacts on the cancellation.
	 */
	public void cancelOperation() {
		hideProgressPanel();
		IProgressMonitor progressMonitor = getProgressMonitor();
		progressMonitor.setCanceled(true);
		disconnectFromProgressPanel();
	}

	/**
	 * disconnects the thread from the assigned progress panel without stopping
	 * the thread.
	 */
	public void disconnectFromProgressPanel() {
		setUpdateProgressPanel(false);
		progressPanel.createNewProgressMonitor();
	}

	public IRunnableWithProgress getRunnable() {
		return runnable;
	}
}

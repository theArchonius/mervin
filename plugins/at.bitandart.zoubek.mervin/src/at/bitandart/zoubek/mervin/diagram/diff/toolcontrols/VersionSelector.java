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
package at.bitandart.zoubek.mervin.diagram.diff.toolcontrols;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import at.bitandart.zoubek.mervin.IDiffService;
import at.bitandart.zoubek.mervin.IMervinContextConstants;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * An abstract Toolcontrol contribution that provides a combo box with a label
 * that allows the user to switch one side of the currently compared versions.
 * 
 * @author Florian Zoubek
 *
 */
@SuppressWarnings("restriction")
public abstract class VersionSelector {

	@Inject
	private IDiffService diffService;

	@Inject
	private Shell shell;

	// Logger

	@Inject
	private Logger logger;

	/**
	 * item value that represents the base version
	 */
	protected final String COMPARE_BASE = "Base Version";

	/**
	 * the combo viewer that allows selection of the version
	 */
	private ComboViewer versionViewer;

	/**
	 * the model review that is used to retrieve the selected and available
	 * versions.
	 */
	private ModelReview activeReview;

	/**
	 * the text of the label in front of the combo box.
	 */
	private String labelText = null;

	/**
	 * determines if the selection events should be ignored or not. Used to
	 * avoid unnecessary comparison updates while updating the control.
	 */
	private boolean ignoreSelection = false;

	public VersionSelector(String labelText) {
		super();
		this.labelText = labelText;
	}

	@PostConstruct
	public void createControls(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		comp.setLayout(layout);

		Label label = new Label(comp, SWT.NONE);
		label.setText(labelText);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(label);

		CCombo oldVersionCombo = new CCombo(comp, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		GridDataFactory.fillDefaults().hint(130, SWT.DEFAULT).applyTo(oldVersionCombo);
		versionViewer = new ComboViewer(oldVersionCombo);
		versionViewer.setContentProvider(ArrayContentProvider.getInstance());
		versionViewer.setLabelProvider(new VersionLabelProvider());
		versionViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				ISelection selection = event.getSelection();

				if (!ignoreSelection && !selection.isEmpty() && selection instanceof IStructuredSelection) {

					IStructuredSelection structuredSelection = (IStructuredSelection) selection;
					setVersion(structuredSelection.getFirstElement());
					udpateSelectedComparison();

				}
			}
		});
		update();
	}

	@Inject
	public void setActiveReview(@Named(IMervinContextConstants.ACTIVE_MODEL_REVIEW) ModelReview review) {
		this.activeReview = review;
		update();
	}

	/**
	 * updates the state of the control.
	 */
	protected void update() {
		ComboViewer versionViewer = getVersionViewer();
		ModelReview activeReview = getActiveReview();
		if (versionViewer != null && activeReview != null) {
			List<Object> patchSets = new LinkedList<Object>(activeReview.getPatchSets());
			patchSets.add(0, COMPARE_BASE);
			versionViewer.setInput(patchSets);
			Object version = getSelectedVersion();

			ignoreSelection = true;
			if (version != null) {
				versionViewer.setSelection(new StructuredSelection(version));
			} else {
				versionViewer.setSelection(new StructuredSelection(COMPARE_BASE));
			}
			ignoreSelection = false;
		}
	}

	/**
	 * @return the currently selected version or null if the no or the base
	 *         version has been selected.
	 */
	protected abstract Object getSelectedVersion();

	/**
	 * sets the given version as selected value in the active model review.
	 * 
	 * @param version
	 *            the version or {@link #COMPARE_BASE} if the base version has
	 *            been selected. Null is not permitted.
	 */
	protected abstract void setVersion(Object version);

	/**
	 * @return the review to switch the comparison version for.
	 */
	public ModelReview getActiveReview() {
		return activeReview;
	}

	/**
	 * @return the combo viewer used to switch the compared versions.
	 */
	public ComboViewer getVersionViewer() {
		return versionViewer;
	}

	/**
	 * updates the selected model comparison of the current model review.
	 */
	private void udpateSelectedComparison() {

		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell);
		try {
			progressMonitorDialog.run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					ModelReview modelReview = getActiveReview();
					diffService.updateSelectedComparison(modelReview, monitor);
					monitor.done();
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			logger.error(e, "Could not update the selected comparison");
		}

	}

}
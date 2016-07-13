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
package at.bitandart.zoubek.mervin.review;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import at.bitandart.zoubek.mervin.ICommandConstants;
import at.bitandart.zoubek.mervin.IDiffService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * A view that shows the options for the loaded {@link ModelReview} of the
 * currently active editor.
 * 
 * <p>
 * This class adapts to the following classes:
 * <ul>
 * <li>{@link ModelReview} - the current model review instance</li>
 * </ul>
 * </p>
 * 
 * @author Florian Zoubek
 *
 */
@SuppressWarnings("restriction")
public class ReviewOptionsView extends ModelReviewEditorTrackingView implements IAdaptable {

	private final class PatchSetLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof PatchSet) {
				PatchSet patchSet = (PatchSet) element;
				return MessageFormat.format("{0}", patchSet.getId());
			}
			return super.getText(element);
		}
	}

	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.review.options";

	/**
	 * Element that represents the base in the patch set comparison selector
	 */
	private static final String COMPARE_BASE = "Base";

	@Inject
	private Shell shell;

	// JFace viewers

	private ComboViewer leftSidePatchSetViewer;
	private ComboViewer rightSidePatchSetViewer;

	// SWT controls

	private ScrolledForm mainForm;
	private Composite mainPanel;

	private Composite persistencePanel;
	private Button loadButton;
	private Button saveButton;

	private Composite comparePanel;

	private Composite informationPanel;
	private Label reviewIdValueLabel;
	private Label patchSetsValueLabel;
	private Label commentsValueLabel;

	// Data

	private boolean viewInitialized = false;

	// Services

	@Inject
	private ECommandService commandService;
	@Inject
	private EHandlerService handlerService;
	@Inject
	private IDiffService diffService;

	// Logger

	@Inject
	private Logger logger;

	@Inject
	public ReviewOptionsView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		mainForm = toolkit.createScrolledForm(parent);

		mainPanel = mainForm.getBody();
		mainPanel.setLayout(new GridLayout());

		createInfoSection(toolkit);
		createCompareSection(toolkit);
		createPersistenceSection(toolkit);
		viewInitialized = true;
		updateValues();

	}

	/**
	 * creates the general information section
	 * 
	 * @param toolkit
	 */
	private void createInfoSection(FormToolkit toolkit) {

		Section informationSection = toolkit.createSection(mainPanel, Section.TITLE_BAR);
		informationSection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		informationSection.setText("Review Information");

		informationPanel = toolkit.createComposite(informationSection);
		informationPanel.setLayout(new GridLayout(2, true));
		informationSection.setClient(informationPanel);

		Label reviewIdLabel = toolkit.createLabel(informationPanel, "Review Id:");
		reviewIdLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		reviewIdValueLabel = toolkit.createLabel(informationPanel, "");
		reviewIdValueLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		Label commentsLabel = toolkit.createLabel(informationPanel, "Comments:");
		commentsLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		commentsValueLabel = toolkit.createLabel(informationPanel, "");
		commentsValueLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		Label patchSetsLabel = toolkit.createLabel(informationPanel, "PatchSets:");
		patchSetsLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		patchSetsValueLabel = toolkit.createLabel(informationPanel, "");
		patchSetsValueLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

	}

	/**
	 * creates the compare section
	 * 
	 * @param toolkit
	 */
	private void createCompareSection(FormToolkit toolkit) {

		Section compareSection = toolkit.createSection(mainPanel, Section.TITLE_BAR);
		compareSection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		compareSection.setText("Compare Patch Sets");

		comparePanel = toolkit.createComposite(compareSection);
		comparePanel.setLayout(new GridLayout(2, true));
		compareSection.setClient(comparePanel);

		CCombo leftSidePatchSetCombo = new CCombo(comparePanel, SWT.DROP_DOWN | SWT.READ_ONLY);
		leftSidePatchSetCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		toolkit.adapt(leftSidePatchSetCombo);
		leftSidePatchSetViewer = new ComboViewer(leftSidePatchSetCombo);
		leftSidePatchSetViewer.setContentProvider(ArrayContentProvider.getInstance());
		leftSidePatchSetViewer.setLabelProvider(new PatchSetLabelProvider());
		leftSidePatchSetViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				ISelection selection = event.getSelection();

				if (!selection.isEmpty() && selection instanceof IStructuredSelection) {

					IStructuredSelection structuredSelection = (IStructuredSelection) selection;
					Object element = structuredSelection.getFirstElement();
					ModelReview currentModelReview = getCurrentModelReview();

					if (COMPARE_BASE.equals(element)) {
						// null represents the base version
						currentModelReview.setLeftPatchSet(null);
						udpateSelectedComparison();

					} else if (element instanceof PatchSet) {
						currentModelReview.setLeftPatchSet((PatchSet) element);
						udpateSelectedComparison();
					}
				}
			}
		});

		CCombo rightSidePatchSetCombo = new CCombo(comparePanel, SWT.DROP_DOWN | SWT.READ_ONLY);
		rightSidePatchSetCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		toolkit.adapt(rightSidePatchSetCombo);
		rightSidePatchSetViewer = new ComboViewer(rightSidePatchSetCombo);
		rightSidePatchSetViewer.setContentProvider(ArrayContentProvider.getInstance());
		rightSidePatchSetViewer.setLabelProvider(new PatchSetLabelProvider());
		rightSidePatchSetViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				ISelection selection = event.getSelection();

				if (!selection.isEmpty() && selection instanceof IStructuredSelection) {

					IStructuredSelection structuredSelection = (IStructuredSelection) selection;
					Object element = structuredSelection.getFirstElement();
					ModelReview currentModelReview = getCurrentModelReview();

					if (COMPARE_BASE.equals(element)) {
						// null represents the base version
						currentModelReview.setRightPatchSet(null);
						udpateSelectedComparison();

					} else if (element instanceof PatchSet) {
						currentModelReview.setRightPatchSet((PatchSet) element);
						udpateSelectedComparison();
					}
				}
			}
		});

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
					ModelReview modelReview = getCurrentModelReview();
					diffService.updateSelectedComparison(modelReview, monitor);
					monitor.done();
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			logger.error(e, "Could not udapte the selected comparison");
		}

	}

	/**
	 * creates the persistence section
	 * 
	 * @param toolkit
	 */
	private void createPersistenceSection(FormToolkit toolkit) {

		persistencePanel = toolkit.createComposite(mainPanel, SWT.NONE);
		persistencePanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		persistencePanel.setLayout(new GridLayout(2, false));
		loadButton = toolkit.createButton(persistencePanel, "Load...", SWT.PUSH);
		loadButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		loadButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Command command = commandService.getCommand(ICommandConstants.LOAD_REVIEW);
				if (command != null && command.isEnabled()) {
					ParameterizedCommand loadCommand = new ParameterizedCommand(command, null);
					if (handlerService.canExecute(loadCommand)) {
						handlerService.executeHandler(loadCommand);
					} else {
						logger.error(MessageFormat.format(
								"Could not load review, no handler could execute the command {0}", loadCommand));
					}
				} else {
					if (command == null) {
						logger.error(MessageFormat.format("Could not load review, no command with id \"{0}\" found",
								ICommandConstants.LOAD_REVIEW));
					} else {
						logger.error(
								MessageFormat.format("Could not load review, the command is disabled {0}", command));
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		saveButton = toolkit.createButton(persistencePanel, "Save...", SWT.PUSH);
		saveButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		saveButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Command command = commandService.getCommand(ICommandConstants.SAVE_REVIEW);
				if (command != null && command.isEnabled()) {
					ParameterizedCommand saveCommand = new ParameterizedCommand(command, null);
					if (handlerService.canExecute(saveCommand)) {
						handlerService.executeHandler(saveCommand);
					} else {
						logger.error(MessageFormat.format(
								"Could not save review, no handler could execute the command {0}", saveCommand));
					}
				} else {
					if (command == null) {
						logger.error(MessageFormat.format("Could not save review, no command with id \"{0}\" found",
								ICommandConstants.SAVE_REVIEW));
					} else {
						logger.error(
								MessageFormat.format("Could not save review, the command is disabled {0}", command));
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

	}

	@Override
	protected void updateValues() {
		if (!viewInitialized) {
			return;
		}
		ModelReview currentModelReview = getCurrentModelReview();
		if (currentModelReview == null) {

			// update information section
			reviewIdValueLabel.setText("<none>");
			commentsValueLabel.setText("<none>");
			patchSetsValueLabel.setText("<none>");

			// update compare section
			leftSidePatchSetViewer.getCCombo().setEnabled(false);
			rightSidePatchSetViewer.getCCombo().setEnabled(false);

			leftSidePatchSetViewer.setInput(new Object[0]);
			rightSidePatchSetViewer.setInput(new Object[0]);

			// update persistence section
			loadButton.setEnabled(true);
			saveButton.setEnabled(false);

		} else {

			// update information section
			reviewIdValueLabel.setText(currentModelReview.getId());
			commentsValueLabel.setText(currentModelReview.getComments().size() + "");
			patchSetsValueLabel.setText(currentModelReview.getPatchSets().size() + "");

			// update compare section
			leftSidePatchSetViewer.getCCombo().setEnabled(true);
			rightSidePatchSetViewer.getCCombo().setEnabled(true);

			List<Object> patchSets = new LinkedList<Object>(currentModelReview.getPatchSets());
			patchSets.add(0, COMPARE_BASE);
			leftSidePatchSetViewer.setInput(patchSets);
			rightSidePatchSetViewer.setInput(patchSets);

			if (currentModelReview.getLeftPatchSet() != null) {
				leftSidePatchSetViewer.setSelection(new StructuredSelection(currentModelReview.getLeftPatchSet()));
			} else {
				leftSidePatchSetViewer.setSelection(new StructuredSelection(COMPARE_BASE));
			}

			if (currentModelReview.getRightPatchSet() != null) {
				leftSidePatchSetViewer.setSelection(new StructuredSelection(currentModelReview.getRightPatchSet()));
			} else {
				rightSidePatchSetViewer.setSelection(new StructuredSelection(COMPARE_BASE));
			}

			// update persistence section
			loadButton.setEnabled(true);
			saveButton.setEnabled(true);

		}

		informationPanel.layout();
		comparePanel.layout();
		persistencePanel.layout();
		mainPanel.layout();

		mainForm.layout();
		mainForm.reflow(true);
		mainForm.redraw();

	}

	@Override
	public <T> T getAdapter(Class<T> adapterType) {

		if (adapterType == ModelReview.class) {
			return adapterType.cast(getCurrentModelReview());
		}
		return null;
	}
}
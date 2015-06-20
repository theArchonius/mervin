/*******************************************************************************
 * Copyright (c) 2015 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.review;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
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
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import at.bitandart.zoubek.mervin.ICommandConstants;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

@SuppressWarnings("restriction")
public class ReviewOptionsView extends ModelReviewEditorTrackingView {

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
		
		Section informationSection = toolkit.createSection(mainPanel,
				Section.TITLE_BAR);
		informationSection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false));
		informationSection.setText("Review Information");

		informationPanel = toolkit.createComposite(informationSection);
		informationPanel.setLayout(new GridLayout(2, true));
		informationSection.setClient(informationPanel);

		Label reviewIdLabel = toolkit.createLabel(informationPanel,
				"Review Id:");
		reviewIdLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false));
		reviewIdValueLabel = toolkit.createLabel(informationPanel, "");
		reviewIdValueLabel.setLayoutData(new GridData(SWT.BEGINNING,
				SWT.CENTER, false, false));

		Label commentsLabel = toolkit
				.createLabel(informationPanel, "Comments:");
		commentsLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false));
		commentsValueLabel = toolkit.createLabel(informationPanel, "");
		commentsValueLabel.setLayoutData(new GridData(SWT.BEGINNING,
				SWT.CENTER, false, false));

		Label patchSetsLabel = toolkit.createLabel(informationPanel,
				"PatchSets:");
		patchSetsLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false));
		patchSetsValueLabel = toolkit.createLabel(informationPanel, "");
		patchSetsValueLabel.setLayoutData(new GridData(SWT.BEGINNING,
				SWT.CENTER, false, false));
		
	}

	/**
	 * creates the compare section
	 * 
	 * @param toolkit
	 */
	private void createCompareSection(FormToolkit toolkit) {
		
		Section compareSection = toolkit.createSection(mainPanel,
				Section.TITLE_BAR);
		compareSection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		compareSection.setText("Compare Patch Sets");

		comparePanel = toolkit.createComposite(compareSection);
		comparePanel.setLayout(new GridLayout(2, true));
		compareSection.setClient(comparePanel);

		CCombo leftSidePatchSetCombo = new CCombo(comparePanel, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		leftSidePatchSetCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false));
		toolkit.adapt(leftSidePatchSetCombo);
		leftSidePatchSetViewer = new ComboViewer(leftSidePatchSetCombo);
		leftSidePatchSetViewer.setContentProvider(ArrayContentProvider
				.getInstance());
		leftSidePatchSetViewer.setLabelProvider(new PatchSetLabelProvider());

		CCombo rightSidePatchSetCombo = new CCombo(comparePanel, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		rightSidePatchSetCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false));
		toolkit.adapt(rightSidePatchSetCombo);
		rightSidePatchSetViewer = new ComboViewer(rightSidePatchSetCombo);
		rightSidePatchSetViewer.setContentProvider(ArrayContentProvider
				.getInstance());
		rightSidePatchSetViewer.setLabelProvider(new PatchSetLabelProvider());
		
	}

	/**
	 * creates the persistence section
	 * 
	 * @param toolkit
	 */
	private void createPersistenceSection(FormToolkit toolkit) {
		
		persistencePanel = toolkit.createComposite(mainPanel, SWT.NONE);
		persistencePanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		persistencePanel.setLayout(new GridLayout(2, false));
		loadButton = toolkit
				.createButton(persistencePanel, "Load...", SWT.PUSH);
		loadButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false));
		loadButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Command command = commandService
						.getCommand(ICommandConstants.LOAD_REVIEW);
				if (command != null && command.isEnabled()) {
					ParameterizedCommand loadCommand = new ParameterizedCommand(
							command, null);
					if (handlerService.canExecute(loadCommand)) {
						handlerService.executeHandler(loadCommand);
					} else {
						logger.error(MessageFormat
								.format("Could not load review, no handler could execute the command {0}",
										loadCommand));
					}
				} else {
					if (command == null) {
						logger.error(MessageFormat
								.format("Could not load review, no command with id \"{0}\" found",
										ICommandConstants.LOAD_REVIEW));
					} else {
						logger.error(MessageFormat
								.format("Could not load review, the command is disabled {0}",
										command));
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		saveButton = toolkit
				.createButton(persistencePanel, "Save...", SWT.PUSH);
		saveButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		saveButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Command command = commandService
						.getCommand(ICommandConstants.SAVE_REVIEW);
				if (command != null && command.isEnabled()) {
					ParameterizedCommand loadCommand = new ParameterizedCommand(
							command, null);
					if (handlerService.canExecute(loadCommand)) {
						handlerService.executeHandler(loadCommand);
					} else {
						logger.error(MessageFormat
								.format("Could not save review, no handler could execute the command {0}",
										loadCommand));
					}
				} else {
					if (command == null) {
						logger.error(MessageFormat
								.format("Could not save review, no command with id \"{0}\" found",
										ICommandConstants.SAVE_REVIEW));
					} else {
						logger.error(MessageFormat
								.format("Could not save review, the command is disabled {0}",
										command));
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
			commentsValueLabel.setText(currentModelReview.getComments().size()
					+ "");
			patchSetsValueLabel.setText(currentModelReview.getPatchSets()
					.size() + "");

			// update compare section
			IStructuredSelection prevLeftSelection = leftSidePatchSetViewer
					.getStructuredSelection();
			leftSidePatchSetViewer.getCCombo().setEnabled(true);
			IStructuredSelection prevRightSelection = rightSidePatchSetViewer
					.getStructuredSelection();
			rightSidePatchSetViewer.getCCombo().setEnabled(true);

			PatchSet[] patchSets = currentModelReview.getPatchSets().toArray(
					new PatchSet[0]);
			leftSidePatchSetViewer.setInput(patchSets);
			rightSidePatchSetViewer.setInput(patchSets);

			if (!prevLeftSelection.isEmpty()
					&& currentModelReview.getPatchSets().contains(
							prevLeftSelection.getFirstElement())) {
				leftSidePatchSetViewer.setSelection(prevLeftSelection);
			} else if (patchSets.length > 0) {
				leftSidePatchSetViewer.setSelection(new StructuredSelection(
						patchSets[0]));
			}

			if (!prevRightSelection.isEmpty()
					&& currentModelReview.getPatchSets().contains(
							prevRightSelection.getFirstElement())) {
				rightSidePatchSetViewer.setSelection(prevRightSelection);
			} else if (patchSets.length > 0) {
				rightSidePatchSetViewer.setSelection(new StructuredSelection(
						patchSets[0]));
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
}
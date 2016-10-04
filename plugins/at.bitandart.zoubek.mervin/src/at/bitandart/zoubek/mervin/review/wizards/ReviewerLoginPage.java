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
package at.bitandart.zoubek.mervin.review.wizards;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Florian Zoubek
 *
 */
@Creatable
public class ReviewerLoginPage extends WizardPage {

	/* Default UI Text */

	final static public String PAGE_NAME = "Reviewer Login Page";
	final static private String EMPTY_USERNAME = "Please provide reviewer login credentials that will be used for the review.";
	final static private String DEFAULT_MESSAGE = EMPTY_USERNAME;
	final static private String PASSWORD_STUB_NOTICE = "This prototype does not support authentification with a remote review server yet."
			+ " Mervin uses currently only git to retrieve the data of the review, so no password is needed here.";
	final static private String LABEL_USERNAME = "Username:";
	final static private String LABEL_PASSWORD = "Password:";
	final static private String PAGE_TITLE = "Reviewer login";

	/**
	 * the input control for the name of the reviewer.
	 */
	private Text nameInput;

	/**
	 * the input control for the password of the reviewer.
	 */
	private Text passwordInput;

	/**
	 * the decoration for use name errors.
	 */
	private ControlDecoration nameErrorDecoration;

	public ReviewerLoginPage() {
		super(PAGE_NAME);
	}

	@Override
	public void createControl(Composite parent) {

		final FieldDecorationRegistry fieldDecorationRegistry = FieldDecorationRegistry.getDefault();

		final Composite mainPanel = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 12;
		mainPanel.setLayout(gridLayout);

		final Label userNameLabel = new Label(mainPanel, SWT.NONE);
		userNameLabel.setText(LABEL_USERNAME);
		userNameLabel.setLayoutData(GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).create());

		nameInput = new Text(mainPanel, SWT.BORDER);
		nameInput.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		nameInput.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text text = (Text) e.getSource();
				if (!text.getText().trim().isEmpty()) {
					setPageComplete(true);
					setMessage("", WizardPage.NONE);
					nameErrorDecoration.hide();
				} else {
					setPageComplete(false);
					setMessage(EMPTY_USERNAME, WizardPage.ERROR);
					nameErrorDecoration.show();
				}

			}
		});
		nameErrorDecoration = new ControlDecoration(nameInput, SWT.TOP | SWT.LEFT);
		Image errorImage = fieldDecorationRegistry.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		nameErrorDecoration.setImage(errorImage);
		nameErrorDecoration.setDescriptionText(EMPTY_USERNAME);

		final Label passwordLabel = new Label(mainPanel, SWT.NONE);
		passwordLabel.setText(LABEL_PASSWORD);
		passwordLabel.setLayoutData(GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).create());

		passwordInput = new Text(mainPanel, SWT.BORDER | SWT.PASSWORD);
		passwordInput.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		passwordInput.setEnabled(false);

		final ControlDecoration passwordStubInfo = new ControlDecoration(passwordInput, SWT.TOP | SWT.LEFT);
		Image informationImage = fieldDecorationRegistry.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION)
				.getImage();
		passwordStubInfo.setImage(informationImage);
		passwordStubInfo.setDescriptionText(PASSWORD_STUB_NOTICE);
		passwordStubInfo.show();

		setControl(mainPanel);
		setMessage(DEFAULT_MESSAGE);
		setTitle(PAGE_TITLE);
		setPageComplete(false);

	}

	/**
	 * @return the name of the reviewer provided by the user.
	 */
	public String getReviewerName() {
		if (nameInput != null) {
			return nameInput.getText();
		}
		return "";
	}

	/**
	 * @return the password of the reviewer provided by the user
	 */
	public String getPassword() {
		if (passwordInput != null) {
			return passwordInput.getText();
		}
		return "";
	}

}

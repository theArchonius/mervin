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
package at.bitandart.zoubek.mervin.review.wizards;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A wizard page that allows to select a local clone of a gerrit repository.
 * 
 * @author Florian Zoubek
 *
 */
@Creatable
public class GerritRepositorySelectionPage extends WizardPage {

	private Text repositoryPathControl;
	private Button useOnlyLocalRefsControl;

	public GerritRepositorySelectionPage() {
		super("Select local copy of Gerrit Repository");
		setPageComplete(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.
	 * widgets .Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		Composite mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout(3, false));

		Label repositoryPathLabel = new Label(mainPanel, SWT.NONE);
		repositoryPathLabel.setText("Repository Path:");
		repositoryPathLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		repositoryPathControl = new Text(mainPanel, SWT.BORDER);
		repositoryPathControl.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text text = (Text) e.getSource();
				try {
					if (isGitRepository(new URI(text.getText()))) {
						setPageComplete(true);
						setMessage("", WizardPage.NONE);
					} else {
						setPageComplete(false);
						setMessage("Not a git repository", WizardPage.ERROR);
					}
				} catch (URISyntaxException e1) {
					setPageComplete(false);
					setMessage("Invalid URI", WizardPage.ERROR);
				}

			}
		});
		repositoryPathControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Button selectButton = new Button(mainPanel, SWT.PUSH);
		selectButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		selectButton.setText("Select directory...");

		selectButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
				String uri = directoryDialog.open();
				if (uri != null) {
					repositoryPathControl.setText("file://" + uri);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// intentionally empty
			}
		});

		useOnlyLocalRefsControl = new Button(mainPanel, SWT.CHECK);
		useOnlyLocalRefsControl.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false, 3, 1));
		useOnlyLocalRefsControl.setText("Use only local refs (Use this for local copies of gerrit repos)");

		setControl(mainPanel);
		setMessage("Select a local copy of the gerrit repository you want to review.");
		setTitle("Select Repository");
	}

	/**
	 * 
	 * @param uri
	 * @return true if the given uri points at a directory that contains a git
	 *         repository.
	 */
	private static boolean isGitRepository(URI uri) {
		File gitRoot = new File(uri);
		File gitDir = new File(gitRoot, ".git");
		if (gitDir.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * @return the path of the selected git repository.
	 */
	public String getSelectedRepositoryPath() {
		return repositoryPathControl.getText();
	}

	public boolean shouldUseOnlyLocalRefs() {
		return useOnlyLocalRefsControl.getSelection();
	}
}

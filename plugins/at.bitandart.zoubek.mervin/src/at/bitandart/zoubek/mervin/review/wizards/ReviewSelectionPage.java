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

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import at.bitandart.zoubek.mervin.IReviewDescriptor;
import at.bitandart.zoubek.mervin.IReviewRepositoryService;
import at.bitandart.zoubek.mervin.exceptions.InvalidReviewRepositoryException;
import at.bitandart.zoubek.mervin.exceptions.RepositoryIOException;

/**
 * A Wizard page that let the user choose one identifier out of a list
 * identifiers obtained by the {@link IReviewRepositoryService} of the current
 * context.
 * 
 * @author Florian Zoubek
 *
 */
@Creatable
public class ReviewSelectionPage extends WizardPage {

	/**
	 * An {@link IRunnableWithProgress} that updates the list of reviews in an
	 * {@link ReviewSelectionPage} from the repository at the given URI.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private final class ReviewListUpdater implements IRunnableWithProgress {
		private final String uri;
		private final boolean useOnlyLocalRefs;

		private ReviewListUpdater(String uri, boolean useOnlyLocalRefs) {
			this.uri = uri;
			this.useOnlyLocalRefs = useOnlyLocalRefs;
		}

		@SuppressWarnings("restriction")
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			monitor.beginTask("Loading remote reviews...", IProgressMonitor.UNKNOWN);
			try {
				reviews.clear();
				reviews.addAll(repoService.getReviews(new URI(uri), useOnlyLocalRefs));
				uiSync.syncExec(new Runnable() {

					@Override
					public void run() {
						reviewListViewer.refresh();
					}
				});
			} catch (URISyntaxException | RepositoryIOException | InvalidReviewRepositoryException e) {
				logger.error(e);
				uiSync.syncExec(new Runnable() {

					@Override
					public void run() {
						/*
						 * FIXME add a more specific error message based on the
						 * exception thrown by the repository service
						 */
						MessageDialog.openError(getShell(), "Review list retrieval error",
								"Could not load all reviews for this repository. "
										+ "Make sure the git repository remote \"origin\" points at the correct "
										+ "accessible remote gerrit repository. "
										+ "See the error log for more details.");
					}
				});
			}
		}
	}

	private ListViewer reviewListViewer;
	private List<IReviewDescriptor> reviews;

	@SuppressWarnings("restriction")
	@Inject
	private org.eclipse.e4.core.services.log.Logger logger;

	@Inject
	private IReviewRepositoryService repoService;

	@Inject
	private UISynchronize uiSync;

	public ReviewSelectionPage() {
		super("Select Review");
		reviews = new LinkedList<>();
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
		mainPanel.setLayout(new GridLayout(1, false));

		reviewListViewer = new ListViewer(mainPanel, SWT.BORDER | SWT.SINGLE);
		reviewListViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof IReviewDescriptor) {
					return ((IReviewDescriptor) element).getTitle();
				}
				return super.getText(element);
			}
		});
		reviewListViewer.setContentProvider(ArrayContentProvider.getInstance());
		reviewListViewer.setInput(reviews);
		reviewListViewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		reviewListViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (selection != null) {
					setPageComplete(!selection.isEmpty());
				}

			}
		});

		setControl(mainPanel);
		setMessage("Select a review to load.");
		setTitle("Select Review");
		setPageComplete(false);

	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			IWizardPage previousPage = getPreviousPage();
			if (previousPage instanceof GerritRepositorySelectionPage) {
				GerritRepositorySelectionPage page = ((GerritRepositorySelectionPage) previousPage);

				loadChanges(page.getSelectedRepositoryPath(), page.shouldUseOnlyLocalRefs());
			}
		}
	};

	@SuppressWarnings("restriction")
	private void loadChanges(final String uri, final boolean useOnlyLocalRefs) {
		try {
			ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(getShell());
			progressMonitorDialog.run(true, true, new ReviewListUpdater(uri, useOnlyLocalRefs));
		} catch (InvocationTargetException | InterruptedException e) {
			logger.error(e);
			uiSync.syncExec(new Runnable() {

				@Override
				public void run() {
					MessageDialog.openError(getShell(), "Review list retrieval error",
							"Could not load all review for this repository. "
									+ "Make sure the repository remote \"origin\" points at the correct "
									+ "accessible remote gerrit repository. " + "See the error log for more details.");
				}
			});
		}

	}

	/**
	 * 
	 * @return the selected review id or null if none is selected
	 */
	public String getReviewId() {
		IStructuredSelection structuredSelection = reviewListViewer.getStructuredSelection();
		if (structuredSelection != null && !structuredSelection.isEmpty()) {
			Object firstElement = structuredSelection.getFirstElement();
			if (firstElement instanceof IReviewDescriptor) {
				return ((IReviewDescriptor) firstElement).getId();
			}
		}
		return null;
	}

}

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
package at.bitandart.zoubek.mervin.review.wizards;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;

import at.bitandart.zoubek.mervin.IReviewRepositoryService;
import at.bitandart.zoubek.mervin.exceptions.InvalidReviewException;
import at.bitandart.zoubek.mervin.exceptions.InvalidReviewRepositoryException;
import at.bitandart.zoubek.mervin.exceptions.RepositoryIOException;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.User;

/**
 * A Wizard that loads a {@link ModelReview} from a review repository.
 * 
 * @author Florian Zoubek
 *
 */
@Creatable
public class LoadReviewWizard extends Wizard {

	@Inject
	private GerritRepositorySelectionPage selectRepositoryPage;

	@Inject
	private ReviewSelectionPage gerritChangeSelectionPage;

	@Inject
	private IReviewRepositoryService repoService;

	@Inject
	private User reviewer;

	@SuppressWarnings("restriction")
	@Inject
	private org.eclipse.e4.core.services.log.Logger logger;

	private ModelReview modelReview;

	@Override
	public void addPages() {
		super.addPages();
		addPage(selectRepositoryPage);
		addPage(gerritChangeSelectionPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@SuppressWarnings("restriction")
	@Override
	public boolean performFinish() {

		String repositoryPath = selectRepositoryPage.getSelectedRepositoryPath();
		final URI uri;
		final String id = gerritChangeSelectionPage.getReviewId();

		try {
			uri = new URI(repositoryPath);

		} catch (URISyntaxException e) {
			// this should actually never happen, but to be sure:
			MessageDialog.openError(getShell(), "Invalid repository path", "Invalid repository path");
			return false;

		}

		try {
			new ProgressMonitorDialog(getShell()).run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						/*
						 * TODO add reviewer selection page and let the user
						 * choose the reviewer
						 */
						modelReview = repoService.loadReview(uri, id, reviewer, monitor);
					} catch (RepositoryIOException | InvalidReviewRepositoryException | InvalidReviewException e) {
						new InvocationTargetException(e);
					}

				}
			});

		} catch (InvocationTargetException e) {

			Throwable cause = e.getCause();

			if (cause instanceof RepositoryIOException) {
				logger.error(cause,
						MessageFormat.format(
								"An IO error occurred while loading the review with id \"{0}\" from repository at \"{0}\".",
								id, uri));
				MessageDialog.openError(getShell(), "IO Error",
						"An IO error occurred while loading the review. See the error log for details");

			} else if (cause instanceof InvalidReviewRepositoryException) {
				MessageDialog.openError(getShell(), "Invalid repository",
						"The specified repository cannot be found or is not accessible. Make sure to select a valid accessible repository.");

			} else if (cause instanceof InvalidReviewException) {
				MessageDialog.openError(getShell(), "Invalid review.",
						"The specified review does not exist or is not accessible. Make sure to select a valid accessible review.");

			} else {

				logger.error(cause, MessageFormat
						.format("Unexpected error during loading the review with id {0} and uri {1}", id, uri));
				MessageDialog.openError(getShell(), "Unexpected Error occured.",
						"Unexpected Error occured, see error log for details.");

			}
			return false;

		} catch (InterruptedException e) {

			logger.error(e, MessageFormat.format("Unexpected error during loading the review with id {0} and uri {1}",
					id, uri));
			MessageDialog.openError(getShell(), "Unexpected Error occured.",
					"Unexpected Error occured, see error log for details.");
			return false;

		}

		return true;
	}

	/**
	 * @return the loaded model review or null if the model could not be loaded
	 *         or has not been loaded yet.
	 */
	public ModelReview getLoadedModelReview() {
		return modelReview;
	}

}

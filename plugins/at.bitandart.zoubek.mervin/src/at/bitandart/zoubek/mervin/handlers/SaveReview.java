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
package at.bitandart.zoubek.mervin.handlers;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import at.bitandart.zoubek.mervin.IMervinContextConstants;
import at.bitandart.zoubek.mervin.IReviewRepositoryService;
import at.bitandart.zoubek.mervin.exceptions.InvalidReviewException;
import at.bitandart.zoubek.mervin.exceptions.InvalidReviewRepositoryException;
import at.bitandart.zoubek.mervin.exceptions.RepositoryIOException;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.User;

/**
 * Handles saving of the currently active review.
 * 
 * @author Florian Zoubek
 *
 */
public class SaveReview {

	@SuppressWarnings("restriction")
	@Inject
	private org.eclipse.e4.core.services.log.Logger logger;

	@SuppressWarnings("restriction")
	@Execute
	public void execute(final Shell shell, final IReviewRepositoryService repoService,
			final @Named(IMervinContextConstants.ACTIVE_MODEL_REVIEW) ModelReview modelReview, final User reviewer) {

		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell);
		try {
			progressMonitorDialog.run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					try {
						repoService.saveReview(new URI(modelReview.getRepositoryURI()), modelReview, reviewer, monitor);

					} catch (RepositoryIOException e) {

						logger.error(e,
								MessageFormat.format(
										"IO Error while saving the review with id \"{0}\" to repository \"{1}\"",
										modelReview.getId(), modelReview.getRepositoryURI()));
						MessageDialog.openError(shell, "Repository IO Error",
								"An IO error occured while saving the review. Make sure the originating repository is available and writeable. See error log for details.");

					} catch (InvalidReviewRepositoryException e) {

						logger.error(e,
								MessageFormat.format(
										"Invalid review repository detected while saving the review with id \"{0}\" to repository \"{1}\"",
										modelReview.getId(), modelReview.getRepositoryURI()));
						MessageDialog.openError(shell, "Invalid Repository",
								"The repository URI points to an invalid repository. Make sure the originating repository is available and writeable. See error log for details.");

					} catch (InvalidReviewException e) {

						logger.error(e,
								MessageFormat.format(
										"Invalid review detected while saving the review with id \"{0}\" to repository \"{1}\"",
										modelReview.getId(), modelReview.getRepositoryURI()));
						MessageDialog.openError(shell, "Invalid Review",
								"The current review is invalid:\n" + e.getMessage());

					} catch (URISyntaxException e) {

						logger.error(e,
								MessageFormat.format(
										"Invalid URI detected while saving the review with id \"{0}\" to repository \"{1}\"",
										modelReview.getId(), modelReview.getRepositoryURI()));
						MessageDialog.openError(shell, "Invalid URI",
								MessageFormat.format("The URI \"{0}\" is invalid. See error log for details.",
										modelReview.getRepositoryURI()));
					}

				}
			});
		} catch (InvocationTargetException | InterruptedException e) {

			logger.error(e, MessageFormat.format("Error while saving the review with id \"{0}\" to repository \"{1}\"",
					modelReview.getId(), modelReview.getRepositoryURI()));
			MessageDialog.openError(shell, "Unexpected Error",
					"An unexpected error occured while saving the review. See error log for details.");
		}
	}

}
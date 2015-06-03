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
package at.bitandart.zoubek.mervin;

import java.net.URI;
import java.util.List;

import at.bitandart.zoubek.mervin.exceptions.InvalidReviewException;
import at.bitandart.zoubek.mervin.exceptions.InvalidReviewRepositoryException;
import at.bitandart.zoubek.mervin.exceptions.RepositoryIOException;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * Base interface for all services that provide access to a repository that
 * contains reviews.
 * 
 * @author Florian Zoubek
 *
 */
public interface IReviewRepositoryService {

	/**
	 * lists all review descriptors of all reviews in the repository at the
	 * given {@link URI}.
	 * 
	 * @param uri
	 *            the {@link URI} of the repository
	 * @return a list of review descriptors of all review in the given
	 *         repository
	 * @throws InvalidReviewRepositoryException
	 *             if the repository cannot be found or is not accessible
	 * @throws RepositoryIOException
	 *             if an IO error occurs during reading the list of review
	 *             identifiers.
	 */
	public List<IReviewDescriptor> getReviews(URI uri)
			throws InvalidReviewRepositoryException, RepositoryIOException;

	/**
	 * loads the review with the given identifier from the repository at the
	 * given {@link URI}.
	 * 
	 * @param uri
	 *            the {@link URI} of the repository
	 * @param id
	 *            the identifier of the review to load
	 * @return the review instance
	 * @throws InvalidReviewRepositoryException
	 *             if the repository cannot be found or is not accessible
	 * @throws InvalidReviewException
	 *             if the review cannot be found or is not accessible
	 * @throws RepositoryIOException
	 *             if an IO error occurs during reading the review from the
	 *             repository
	 */
	public ModelReview loadReview(URI uri, String id)
			throws InvalidReviewRepositoryException, InvalidReviewException,
			RepositoryIOException;

	/**
	 * saves (or updates) the given review in the repository at the given
	 * {@link URI}.
	 * 
	 * @param uri
	 *            the {@link URI} of the repository to save the review into
	 * @param modelReview
	 *            the model review instance to save
	 * @throws InvalidReviewException
	 *             if the specified review instance does not met any constraints
	 *             defined by the repository or contains elements that cannot be
	 *             stored in the given repository
	 * @throws RepositoryIOException
	 *             if an IO error occurs while writing to the repository
	 */
	public void saveReview(URI uri, ModelReview modelReview)
			throws InvalidReviewException, RepositoryIOException;

}

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
package at.bitandart.zoubek.mervin.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.google.common.collect.Iterators;

import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * Base class for long running computations of highlight requests. It reads the
 * set of candidates to highlight form the given selection, collects the
 * highlighted elements, passes them to the given highlight service. Subclasses
 * must override {@link #collectHighlightedElements(Set, IProgressMonitor)} to
 * determine the highlighted elements that will be passed to the highlight
 * service.
 * 
 * @author Florian Zoubek
 *
 */
public abstract class SelectionHighlightComputation implements IRunnableWithProgress {

	private IStructuredSelection selection;

	private IReviewHighlightService highlightService;

	private ModelReview review;

	/**
	 * @param selection
	 *            the selection to retrieve the candidates to highlight from.
	 * @param highlightService
	 *            the highlight service to pass the highlighted elements to
	 * @param review
	 *            the review to highlight.
	 */
	public SelectionHighlightComputation(IStructuredSelection selection, IReviewHighlightService highlightService,
			ModelReview review) {
		this.highlightService = highlightService;
		this.review = review;
		this.selection = selection;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Processing selection...", 300);

		Set<Object> candidates = new HashSet<Object>();
		Iterators.addAll(candidates, (Iterator<?>) selection.iterator());

		subMonitor.worked(100);

		Set<Object> highlightedElements = collectHighlightedElements(candidates, subMonitor.newChild(100));

		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}

		applyHighlightedViews(highlightedElements, highlightService, review, subMonitor.newChild(100));
	}

	/**
	 * collects the elements to highlight for the given set of candidates and
	 * returns it.
	 * 
	 * @param candidates
	 *            the set of candidates to collect the highlighted elements for.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @return the set of collected elements to highlight.
	 */
	protected abstract Set<Object> collectHighlightedElements(Set<Object> candidates, IProgressMonitor monitor);

	/**
	 * passes the given highlighted elements to the given
	 * {@link IReviewHighlightService}.
	 * 
	 * @param highlightedElements
	 *            the highlighted elements to pass to the highlight service.
	 * @param highlightService
	 *            the highlight service to pass the elements to.
	 * @param review
	 *            the review to highlight.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	protected void applyHighlightedViews(Set<Object> highlightedElements, IReviewHighlightService highlightService,
			ModelReview review, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Adding highlighted views...",
				highlightedElements.size() * 100 + 20);

		highlightService.clearHighlights(review);
		subMonitor.worked(20);

		for (Object object : highlightedElements) {
			highlightService.addHighlightFor(review, object);
			subMonitor.worked(100);
		}
	}

}

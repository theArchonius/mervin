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
package at.bitandart.zoubek.mervin;

import org.eclipse.core.runtime.IProgressMonitor;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * A service that provides diff operations for {@link ModelReview} model
 * instances.
 * 
 * @author Florian Zoubek
 *
 */
public interface IDiffService {

	/**
	 * updates the {@code SelectedComparison} reference of the given review
	 * based on the selected left and right patch sets.
	 * 
	 * @param review
	 *            the review to update.
	 * @param progressMonitor
	 */
	public void updateSelectedComparison(ModelReview review, IProgressMonitor progressMonitor);

}

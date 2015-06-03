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

/**
 * Describes an review in an review repository, and provides all data necessary
 * to retrieve the actual review from the repository.
 * 
 * @author Florian Zoubek
 *
 */
public interface IReviewDescriptor {

	/**
	 * @return the unique identifier of the review
	 */
	public String getId();

	/**
	 * @return a textual description of the review that can be used as a title
	 */
	public String getTitle();

	/**
	 * @return a textual description of the review which can be used to provide
	 *         more details about the review, but might skip less important
	 *         details
	 */
	public String getShortDescription();

	/**
	 * @return a textual description of the review which can be used to provide
	 *         all details about the review that are known at the moment
	 */
	public String getLongDescription();

}

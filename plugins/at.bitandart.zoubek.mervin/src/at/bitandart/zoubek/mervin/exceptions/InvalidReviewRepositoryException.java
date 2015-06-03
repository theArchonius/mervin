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
package at.bitandart.zoubek.mervin.exceptions;

/**
 * Indicates that a specified review repository is invalid in some way or does
 * not exist. The message of this exception should provide more detail why the
 * repository is considered invalid.
 * 
 * @author Florian Zoubek
 * 
 */
public class InvalidReviewRepositoryException extends Exception {

	private static final long serialVersionUID = -1970975362767100452L;

	/**
	 * @see Exception#Exception(String)
	 */
	public InvalidReviewRepositoryException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public InvalidReviewRepositoryException(String message, Throwable cause) {
		super(message, cause);
	}

}

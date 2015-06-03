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
 * Indicates that the specified review is invalid in some way or does not exist.
 * The message of this exception should provide more detail why the review is
 * considered invalid.
 *
 * @author Florian Zoubek
 * 
 */
public class InvalidReviewException extends Exception {

	private static final long serialVersionUID = -7940791510654024341L;

	/**
	 * @see Exception#Exception(String)
	 */
	public InvalidReviewException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public InvalidReviewException(String message, Throwable cause) {
		super(message, cause);
	}
}

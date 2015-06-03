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

import java.io.IOException;

/**
 * Indicates that an error occurred during reading or writing from or to a given
 * repository.
 *
 * @author Florian Zoubek
 * 
 */
public class RepositoryIOException extends IOException {

	private static final long serialVersionUID = -6622130150810658014L;

	/**
	 * @see IOException#IOException(String)
	 */
	public RepositoryIOException(String message) {
		super(message);
	}

	/**
	 * @see IOException#IOException(String, Throwable)
	 */
	public RepositoryIOException(String message, Throwable cause) {
		super(message, cause);
	}

}

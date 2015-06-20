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
 * @author Florian Zoubek
 *
 */
public interface ICommandConstants {

	/**
	 * command id of the command that loads a model review specified by the user
	 */
	public String LOAD_REVIEW = "at.bitandart.zoubek.mervin.command.review.load";

	/**
	 * command id of the command that saves the model review provided by the
	 * currently active part
	 */
	public String SAVE_REVIEW = "at.bitandart.zoubek.mervin.command.review.save";

}

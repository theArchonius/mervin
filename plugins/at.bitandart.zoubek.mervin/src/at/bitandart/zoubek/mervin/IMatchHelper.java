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
package at.bitandart.zoubek.mervin;

import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;

/**
 * 
 * Base interface for helper classes providing shared operations for
 * {@link Match}es using a specific strategy.
 * 
 * @author Florian Zoubek
 *
 */
public interface IMatchHelper {

	/**
	 * convenience method to get the "new" value of a match.
	 * 
	 * @param match
	 *            the match containing the value.
	 * @return the new value of the given match.
	 */
	public EObject getNewValue(Match match);

	/**
	 * convenience method to get the "old" value of a match.
	 * 
	 * @param match
	 *            the match containing the value.
	 * @return the old value of the given match.
	 */
	public EObject getOldValue(Match match);

	/**
	 * convenience method to check if the given value is the "old" value of a
	 * match.
	 * 
	 * @param object
	 *            the object to check.
	 * @param match
	 *            the match containing the value.
	 * @return true if the given value is the old value of the given match.
	 */
	public boolean isOld(EObject object, Match match);

	/**
	 * convenience method to check if the given value is the "new" value of a
	 * match.
	 * 
	 * @param object
	 *            the object to check.
	 * @param match
	 *            the match containing the value.
	 * @return true if the given value is the new value of the given match.
	 */
	public boolean isNew(EObject object, Match match);

	/**
	 * extracts the opposite object in the given match.
	 * 
	 * @param match
	 *            the match to extract the opposite object from.
	 * @param object
	 *            the object whose opposite should extracted.
	 * @return the opposite object or null if the given object has no opposite
	 *         or if the given object is not present in the match.
	 */
	public EObject getOpposite(EObject object, Match match);
}

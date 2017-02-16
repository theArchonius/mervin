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
 * An {@link IMatchHelper} that interprets {@link Match}es like EMF Compare
 * interprets two way comparisons: Left is the "new" object, right is the "old"
 * object.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinMatchHelper implements IMatchHelper {

	@Override
	public EObject getNewValue(Match match) {
		if (match != null) {
			return match.getLeft();
		}
		return null;
	}

	@Override
	public EObject getOldValue(Match match) {
		if (match != null) {
			return match.getRight();
		}
		return null;
	}

	@Override
	public boolean isOld(EObject object, Match match) {
		return match != null && getOldValue(match) == object;
	}

	@Override
	public boolean isNew(EObject object, Match match) {
		return match != null && getNewValue(match) == object;
	}

	@Override
	public EObject getOpposite(EObject object, Match match) {
		if (match != null) {

			EObject left = match.getLeft();
			EObject right = match.getRight();

			if (left == object) {
				return right;
			} else if (right == object) {
				return left;
			}
		}
		return null;
	}

}

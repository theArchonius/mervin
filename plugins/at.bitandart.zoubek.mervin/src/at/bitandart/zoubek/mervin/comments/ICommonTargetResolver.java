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
package at.bitandart.zoubek.mervin.comments;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * Base interface that for classes that resolve the common target objects shared
 * by different target objects. Such a resolver is used by
 * {@link MervinCommentProvider} instances to determine the groups and their
 * common targets shared between link targets of different patch sets.
 * 
 * @author Florian Zoubek
 *
 */
public interface ICommonTargetResolver {

	/**
	 * find the common targets for the given targets using the given patch set.
	 * Targets that do not have a common target may be returned as common
	 * target.
	 * 
	 * @param targets
	 *            the targets to resolve the common targets for.
	 * @param patchSet
	 *            the patch set related to the given targets.
	 * @return the set of common targets.
	 */
	public Set<EObject> findCommonTargets(Collection<EObject> targets, PatchSet patchSet);

}

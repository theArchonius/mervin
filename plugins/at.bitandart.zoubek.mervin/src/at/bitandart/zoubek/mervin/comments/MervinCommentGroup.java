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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.swt.comments.data.ICommentGroup;

/**
 * A {@link ICommentGroup} implementation for comment groups created for the
 * mervin model.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinCommentGroup implements ICommentGroup {

	private Set<EObject> targets = new HashSet<EObject>();

	@Override
	public String getGroupTitle() {
		return "";
	}

	public Set<EObject> getTargets() {
		return targets;
	}

}
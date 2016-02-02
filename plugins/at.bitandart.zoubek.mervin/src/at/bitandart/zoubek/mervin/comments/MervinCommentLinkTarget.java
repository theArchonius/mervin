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

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLinkTarget;

/**
 * An {@link ICommentLinkTarget} implementation for link targets of the mervin
 * model.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinCommentLinkTarget implements ICommentLinkTarget {

	private List<EObject> targets;

	public MervinCommentLinkTarget(List<EObject> targets) {
		this.targets = targets;
	}

	@Override
	public String getDefaultText() {
		return targets.toString();
	}

	public List<EObject> getTargets() {
		return targets;
	}

}
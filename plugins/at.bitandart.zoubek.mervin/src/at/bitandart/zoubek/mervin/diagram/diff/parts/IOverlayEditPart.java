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
package at.bitandart.zoubek.mervin.diagram.diff.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;

/**
 * Base interface for all {@link EditPart}s that should be placed on an overlay.
 * 
 * @author Florian Zoubek
 *
 */
public interface IOverlayEditPart extends EditPart {

	/**
	 * @return the linked editpart to this overlay edit part or null if no edit
	 *         part is linked.
	 */
	public GraphicalEditPart getLinkedEditPart();

}

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

import org.eclipse.emf.compare.DifferenceKind;

import at.bitandart.zoubek.mervin.draw2d.figures.overlay.OverlayType;

/**
 * The default mervin implementation of {@link IOverlayTypeHelper}.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinOverlayTypeHelper implements IOverlayTypeHelper {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.bitandart.zoubek.mervin.IOverlayTypeHelper#toOverlayType(org.eclipse.
	 * emf.compare.DifferenceKind)
	 */
	@Override
	public OverlayType toOverlayType(DifferenceKind kind) {
		switch (kind) {
		case ADD:
			return OverlayType.ADDITION;
		case DELETE:
			return OverlayType.DELETION;
		case MOVE:
			return OverlayType.MODIFICATION;
		case CHANGE:
			return OverlayType.MODIFICATION;
		default:
			return OverlayType.MODIFICATION;
		}
	}

}

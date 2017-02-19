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
 * Base interface for classes that provide operations on {@link OverlayType}s
 * using a certain strategy.
 * 
 * @author Florian Zoubek
 *
 */
public interface IOverlayTypeHelper {

	/**
	 * converts the given {@link DifferenceKind} to a corresponding
	 * {@link OverlayType}.
	 * 
	 * @param kind
	 *            the {@link DifferenceKind} to convert.
	 * @return the corresponding {@link OverlayType}, never null.
	 */
	public OverlayType toOverlayType(DifferenceKind kind);

}

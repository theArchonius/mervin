/*******************************************************************************
 * Copyright (c) 2015, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.draw2d.figures;

/**
 * The change types supported by {@link IOverlayFigure}s.
 * 
 * @author Florian Zoubek
 *
 */
public enum ChangeType {
	/*
	 * TODO Rename this enum and its referencing classes as overlays are not
	 * necessarily linked to changed figures any more
	 */
	ADDITION, DELETION, MODIFICATION, LAYOUT,
	/**
	 * Pseudo change type for overlays
	 */
	COMMENT
}
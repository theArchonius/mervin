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
package at.bitandart.zoubek.mervin.draw2d.figures.overlay;

/**
 * The overlay types supported by {@link IOverlayFigure}s.
 * 
 * @author Florian Zoubek
 *
 */
public enum OverlayType {
	ADDITION, DELETION, MODIFICATION, LAYOUT, COMMENT
}
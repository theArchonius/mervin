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

import org.eclipse.gef.Disposable;
import org.eclipse.swt.graphics.Color;

/**
 * Base interface for style advisors used to obtain a style for a given
 * {@link OverlayType}.
 * 
 * @author Florian Zoubek
 *
 */
public interface IOverlayTypeStyleAdvisor extends Disposable {

	public Color getForegroundColorForOverlayType(OverlayType overlayType);

	public Color getBackgroundColorForOverlayType(OverlayType overlayType);

	public Color getIndicatorColorForOverlayType(OverlayType overlayType);

	public Color getCommentColorForOverlayType(OverlayType overlayType);

}
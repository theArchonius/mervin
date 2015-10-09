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
package at.bitandart.zoubek.mervin.draw2d.figures;

import org.eclipse.swt.graphics.Color;

/**
 * Base interface for style advisors used to obtain a style for a given
 * {@link ChangeType}.
 * 
 * @author Florian Zoubek
 *
 */
public interface IChangeTypeStyleAdvisor {

	public Color getForegroundColorForChangeType(ChangeType changeType);

	public Color getBackgroundColorForChangeType(ChangeType changeType);

	public Color getIndicatorColorForChangeType(ChangeType changeType);

	public Color getCommentColorForChangeType(ChangeType changeType);

}
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

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;

/**
 * A {@link ScalableFreeformRootEditPart} that contains the default layers and
 * an additional {@link #DIFF_HIGHLIGHT_LAYER} on top.
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramDiffRootEditPart extends ScalableFreeformRootEditPart {

	public static final String DIFF_HIGHLIGHT_LAYER = "at.bitandart.zoubek.mervin.diagram.diff.highlight.layer";

	@Override
	protected void createLayers(LayeredPane layeredPane) {
		super.createLayers(layeredPane);
		layeredPane.add(new FreeformLayer(), DIFF_HIGHLIGHT_LAYER);
	}
}

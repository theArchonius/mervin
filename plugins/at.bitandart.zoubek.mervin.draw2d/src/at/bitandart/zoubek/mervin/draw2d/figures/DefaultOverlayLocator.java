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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;

/**
 * The default locator for {@link IOverlayFigure}s which calls
 * {@link IOverlayFigure#updateFigure(Collection)} for
 * {@link IOverlayFigure}s. Does nothing for figures that do not implement
 * {@link IOverlayFigure}.
 * 
 * @author Florian Zoubek
 *
 */
public class DefaultOverlayLocator implements Locator {

	private Collection<IFigure> linkedFigures;

	/**
	 * @param linkedFigures
	 *            the linked figures used to update the figures bounds.
	 */
	public DefaultOverlayLocator(Collection<IFigure> linkedFigures) {
		this.linkedFigures = linkedFigures;
	}

	/**
	 * @param linkedFigure
	 *            the linked figure used to update the figures bounds.
	 */
	public DefaultOverlayLocator(IFigure linkedFigure) {
		this.linkedFigures = new ArrayList<IFigure>(1);
		linkedFigures.add(linkedFigure);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void relocate(IFigure target) {
		if (target instanceof IOverlayFigure) {
			((IOverlayFigure) target).updateFigure(linkedFigures);
		}
	}

}

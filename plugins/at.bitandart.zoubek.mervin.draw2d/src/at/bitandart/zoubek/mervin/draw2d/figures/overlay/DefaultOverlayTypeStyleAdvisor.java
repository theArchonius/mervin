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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * The default style advisor for changes.
 * 
 * @author Florian Zoubek
 *
 */
public class DefaultOverlayTypeStyleAdvisor implements IOverlayTypeStyleAdvisor {

	private Color additionForegroundColor;
	private Color deletionForegroundColor;
	private Color modificationForegroundColor;
	private Color layoutForegroundColor;
	private Color commentsForegroundColor;
	private Color defaultForegroundColor;

	private Color additionBackgroundColor;
	private Color deletionBackgroundColor;
	private Color modificationBackgroundColor;
	private Color layoutBackgroundColor;
	private Color commentsBackgroundColor;
	private Color defaultBackgroundColor;

	private Color commentsColor;

	public DefaultOverlayTypeStyleAdvisor() {

		additionForegroundColor = new Color(Display.getDefault(), 82, 145, 102);
		additionBackgroundColor = new Color(Display.getDefault(), 82, 145, 102);

		deletionForegroundColor = new Color(Display.getDefault(), 153, 153, 153);
		deletionBackgroundColor = new Color(Display.getDefault(), 153, 153, 153);

		modificationForegroundColor = new Color(Display.getDefault(), 31, 120, 180);
		modificationBackgroundColor = new Color(Display.getDefault(), 31, 120, 180);

		layoutForegroundColor = new Color(Display.getDefault(), 178, 223, 138);
		layoutBackgroundColor = new Color(Display.getDefault(), 178, 223, 138);

		commentsColor = new Color(Display.getDefault(), 247, 177, 0);
		commentsForegroundColor = commentsColor;
		commentsBackgroundColor = commentsColor;

		defaultForegroundColor = ColorConstants.black;
		defaultBackgroundColor = ColorConstants.white;

	}

	public void dispose() {

		additionForegroundColor.dispose();
		additionBackgroundColor.dispose();

		deletionForegroundColor.dispose();
		deletionBackgroundColor.dispose();

		modificationForegroundColor.dispose();
		modificationBackgroundColor.dispose();

		commentsColor.dispose();
	}

	@Override
	public Color getForegroundColorForOverlayType(OverlayType overlayType) {

		switch (overlayType) {
		case ADDITION:
			return additionForegroundColor;
		case DELETION:
			return deletionForegroundColor;
		case MODIFICATION:
			return modificationForegroundColor;
		case LAYOUT:
			return layoutForegroundColor;
		case COMMENT:
			return commentsForegroundColor;
		}
		return defaultForegroundColor;

	}

	@Override
	public Color getBackgroundColorForOverlayType(OverlayType overlayType) {

		switch (overlayType) {
		case ADDITION:
			return additionBackgroundColor;
		case DELETION:
			return deletionBackgroundColor;
		case MODIFICATION:
			return modificationBackgroundColor;
		case LAYOUT:
			return layoutBackgroundColor;
		case COMMENT:
			return commentsBackgroundColor;
		}
		return defaultBackgroundColor;

	}

	@Override
	public Color getCommentColorForOverlayType(OverlayType overlayType) {
		return commentsColor;
	}

	@Override
	public Color getIndicatorColorForOverlayType(OverlayType overlayType) {
		return ColorConstants.black;
	}

}
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
package at.bitandart.zoubek.mervin.draw2d;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * An {@link IResourceRegistry} containing all resource descriptors that do not
 * depend on external resources.
 * 
 * @author Florian Zoubek
 *
 */
public abstract class MervinResourceRegistry implements IResourceRegistry {

	public static final String IMAGE_WORKBENCH_TAB_MODE = "image_workbech_tab_mode";
	public static final String IMAGE_WORKBENCH_WINDOW_MODE = "image_workbech_window_mode";
	public static final String IMAGE_WORKBENCH_MINIMIZE = "image_workbech_minimize";
	public static final String IMAGE_WORKBENCH_MAXIMIZE = "image_workbech_maximize";

	protected Map<String, ImageDescriptor> imageRegistry;

	protected Map<String, ColorDescriptor> colorRegistry;

	protected Map<String, FontDescriptor> fontRegistry;

	public MervinResourceRegistry() {
		imageRegistry = new HashMap<String, ImageDescriptor>();
		colorRegistry = new HashMap<String, ColorDescriptor>();
		fontRegistry = new HashMap<String, FontDescriptor>();
	}

	@Override
	public ColorDescriptor getColorDescriptor(String identifier) {
		return colorRegistry.get(identifier);
	}

	@Override
	public FontDescriptor getFontDescriptor(String identifier) {
		return fontRegistry.get(identifier);
	}

	@Override
	public ImageDescriptor getImageDescriptor(String identifier) {
		return imageRegistry.get(identifier);
	}

}

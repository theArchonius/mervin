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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Florian Zoubek
 *
 */
public class PluginMervinResourceRegistry extends MervinResourceRegistry {

	public final String ICON_DGM_PATH = "platform:/plugin/at.bitandart.zoubek.mervin.draw2d/icons/dgm/";

	public PluginMervinResourceRegistry() throws MalformedURLException {

		imageRegistry.put(IMAGE_WORKBENCH_TAB_MODE,
				ImageDescriptor.createFromURL(new URL(ICON_DGM_PATH + IMAGE_WORKBENCH_TAB_MODE + "_16.png")));
		imageRegistry.put(IMAGE_WORKBENCH_WINDOW_MODE,
				ImageDescriptor.createFromURL(new URL(ICON_DGM_PATH + IMAGE_WORKBENCH_WINDOW_MODE + "_16.png")));
		imageRegistry.put(IMAGE_WORKBENCH_MINIMIZE,
				ImageDescriptor.createFromURL(new URL(ICON_DGM_PATH + IMAGE_WORKBENCH_MINIMIZE + "_16.png")));
		imageRegistry.put(IMAGE_WORKBENCH_MAXIMIZE,
				ImageDescriptor.createFromURL(new URL(ICON_DGM_PATH + IMAGE_WORKBENCH_MAXIMIZE + "_16.png")));

	}

}

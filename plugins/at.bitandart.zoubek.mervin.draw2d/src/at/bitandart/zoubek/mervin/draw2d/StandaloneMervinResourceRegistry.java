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

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Florian Zoubek
 *
 */
public class StandaloneMervinResourceRegistry extends MervinResourceRegistry {

	public StandaloneMervinResourceRegistry() {

		imageRegistry.put(IMAGE_WORKBENCH_TAB_MODE,
				ImageDescriptor.createFromURL(getClass().getResource("/icons/16/workbench_tab_mode.png")));
		imageRegistry.put(IMAGE_WORKBENCH_WINDOW_MODE,
				ImageDescriptor.createFromURL(getClass().getResource("/icons/16/workbench_window_mode.png")));
		imageRegistry.put(IMAGE_WORKBENCH_MINIMIZE,
				ImageDescriptor.createFromURL(getClass().getResource("/icons/16/workbench_to_content.png")));
		imageRegistry.put(IMAGE_WORKBENCH_MAXIMIZE,
				ImageDescriptor.createFromURL(getClass().getResource("/icons/16/workbench_to_tray.png")));

	}

}

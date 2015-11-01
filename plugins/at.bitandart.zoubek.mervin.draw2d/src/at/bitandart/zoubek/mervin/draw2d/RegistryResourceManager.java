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

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * This manager combines a {@link IResourceRegistry} and a
 * {@link ResourceManager}, where the registry is used to obtain the resource
 * descriptors which are than managed by the given resource manager. Similar to
 * {@link ResourceManager} resources create by this class are shared and may not
 * be disposed by their dispose methods. Use the {@code destroy*()} methods
 * instead.
 * 
 * @author Florian Zoubek
 *
 */
public class RegistryResourceManager {

	private IResourceRegistry resourceRegistry;

	private ResourceManager resourceManager;

	public RegistryResourceManager(IResourceRegistry registry, ResourceManager resourceManager) {
		super();
		this.resourceRegistry = registry;
		this.resourceManager = resourceManager;
	}

	/**
	 * retrieves the color associated with the given identifier from the
	 * registry, using the resource manager.
	 * 
	 * @param identifier
	 * @return the created color or null if no color could be created for the
	 *         given identifier.
	 */
	public Color getColor(String identifier) {
		ColorDescriptor colorDescriptor = resourceRegistry.getColorDescriptor(identifier);
		if (colorDescriptor != null) {
			return resourceManager.createColor(colorDescriptor);
		}
		return null;
	}

	/**
	 * retrieves the font associated with the given identifier from the
	 * registry, using the resource manager.
	 * 
	 * @param identifier
	 * @return the created font or null if no font could be created for the
	 *         given identifier.
	 */
	public Font getFont(String identifier) {
		FontDescriptor fontDescriptor = resourceRegistry.getFontDescriptor(identifier);
		if (fontDescriptor != null) {
			return resourceManager.createFont(fontDescriptor);
		}
		return null;
	}

	/**
	 * retrieves the image associated with the given identifier from the
	 * registry, using the resource manager.
	 * 
	 * @param identifier
	 * @return the created image or null if no image could be created for the
	 *         given identifier.
	 */
	public Image getImage(String identifier) {
		ImageDescriptor imageDescriptor = resourceRegistry.getImageDescriptor(identifier);
		if (imageDescriptor != null) {
			return resourceManager.createImage(imageDescriptor);
		}
		return null;
	}

	/**
	 * destroys the color associated with the given identifier from the
	 * registry, using the current resource manager.
	 * 
	 * @param identifier
	 */
	public void destroyColor(String identifier) {
		ColorDescriptor colorDescriptor = resourceRegistry.getColorDescriptor(identifier);
		if (colorDescriptor != null) {
			resourceManager.destroyColor(colorDescriptor);
		}
	}

	/**
	 * destroys the font associated with the given identifier from the registry,
	 * using the current resource manager.
	 * 
	 * @param identifier
	 */
	public void destroyFont(String identifier) {
		FontDescriptor fontDescriptor = resourceRegistry.getFontDescriptor(identifier);
		if (fontDescriptor != null) {
			resourceManager.destroyFont(fontDescriptor);
		}
	}

	/**
	 * destroys the image associated with the given identifier from the
	 * registry, using the current resource manager.
	 * 
	 * @param identifier
	 */
	public void destroyImage(String identifier) {
		ImageDescriptor imageDescriptor = resourceRegistry.getImageDescriptor(identifier);
		if (imageDescriptor != null) {
			resourceManager.destroyImage(imageDescriptor);
		}
	}

}

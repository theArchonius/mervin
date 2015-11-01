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

/**
 * A registry that maps identifiers to resource descriptors.
 * 
 * @author Florian Zoubek
 *
 */
public interface IResourceRegistry {

	/**
	 * @param identifier
	 * @return the {@link ColorDescriptor} registered for the given identifier
	 *         or null if no {@link ColorDescriptor} has been registered for
	 *         this identifier.
	 */
	public ColorDescriptor getColorDescriptor(String identifier);

	/**
	 * @param identifier
	 * @return the {@link FontDescriptor} registered for the given identifier or
	 *         null if no {@link FontDescriptor} has been registered for this
	 *         identifier.
	 */
	public FontDescriptor getFontDescriptor(String identifier);

	/**
	 * @param identifier
	 * @return the {@link ImageDescriptor} registered for the given identifier
	 *         or null if no {@link ImageDescriptor} has been registered for
	 *         this identifier.
	 */
	public ImageDescriptor getImageDescriptor(String identifier);

}

/*******************************************************************************
 * Copyright (c) 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.swt.diff.tree;

import org.eclipse.swt.graphics.Image;

/**
 * Represents the item of a side of a {@link TreeDiffItem}.
 * 
 * @author Florian Zoubek
 *
 */
public class TreeDiffSideItem {

	/**
	 * the label text for this item.
	 */
	private String label;

	/**
	 * the image for this item.
	 */
	private Image image;

	/**
	 * creates a new {@link TreeDiffSideItem} with the given arguments.
	 * 
	 * @param label
	 *            the label text of this item.
	 * @param image
	 *            the image of this item.
	 */
	public TreeDiffSideItem(String label, Image image) {
		super();
		this.label = label;
		this.image = image;
	}

	/**
	 * @return the label text of this item.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label text to set for this item.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the image the image of this item.
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set for this item.
	 */
	public void setImage(Image image) {
		this.image = image;
	}
}

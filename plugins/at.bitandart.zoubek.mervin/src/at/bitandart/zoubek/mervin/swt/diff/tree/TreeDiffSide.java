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

/**
 * Represents a side of a {@link TreeDiff}.
 * 
 * @author Florian Zoubek
 *
 */
public enum TreeDiffSide {
	LEFT, RIGHT;

	/**
	 * @return the opposite side.
	 */
	public TreeDiffSide getOpposite() {
		if (this == LEFT) {
			return RIGHT;
		} else {
			return LEFT;
		}
	}
}
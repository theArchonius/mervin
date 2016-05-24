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
 * Represents a diff type of a {@link TreeDiffItem}.
 * 
 * @author Florian Zoubek
 *
 */
public enum TreeDiffType {
	ADD, DELETE, MODIFY, EQUAL;

	/**
	 * @param excludedType
	 *            the type to exclude.
	 * @return an array of all {@link TreeDiffType}s except the given excluded
	 *         type.
	 */
	public static TreeDiffType[] getValuesExcluding(TreeDiffType excludedType) {

		TreeDiffType[] types = new TreeDiffType[values().length - 1];
		int i = 0;
		for (TreeDiffType type : values()) {
			if (type != excludedType) {
				types[i] = type;
				i++;
			}
		}

		return types;
	}
}

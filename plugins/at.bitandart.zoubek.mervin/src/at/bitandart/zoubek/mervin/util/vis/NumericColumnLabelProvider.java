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
package at.bitandart.zoubek.mervin.util.vis;

import org.eclipse.jface.viewers.ColumnLabelProvider;

public abstract class NumericColumnLabelProvider extends ColumnLabelProvider {

	public NumericColumnLabelProvider() {
		super();
	}

	@Override
	public String getText(Object element) {
		if (hasValue(element)) {
			return "" + getValue(element);
		}
		return null;
	}

	public abstract float getValue(Object element);

	public abstract boolean hasValue(Object element);

}
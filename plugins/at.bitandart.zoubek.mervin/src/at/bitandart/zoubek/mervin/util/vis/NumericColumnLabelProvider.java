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
package at.bitandart.zoubek.mervin.util.vis;

import java.text.DecimalFormat;

import org.eclipse.jface.viewers.ColumnLabelProvider;

public abstract class NumericColumnLabelProvider extends ColumnLabelProvider {

	private DecimalFormat decimalFormat;

	public NumericColumnLabelProvider() {
		super();
		decimalFormat = new DecimalFormat("#.###");
	}

	/**
	 * @param decimalFormat
	 *            the format to use for converting the value to a {@link String}
	 *            .
	 */
	public NumericColumnLabelProvider(DecimalFormat decimalFormat) {
		super();
		this.decimalFormat = decimalFormat;
	}

	@Override
	public String getText(Object element) {
		if (hasValue(element)) {
			return decimalFormat.format(getValue(element));
		}
		return null;
	}

	public abstract float getValue(Object element);

	public abstract boolean hasValue(Object element);

}
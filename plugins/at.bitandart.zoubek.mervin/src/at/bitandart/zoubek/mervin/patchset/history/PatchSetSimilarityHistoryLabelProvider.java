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
package at.bitandart.zoubek.mervin.patchset.history;

import org.eclipse.swt.graphics.Color;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.util.vis.HSB;
import at.bitandart.zoubek.mervin.util.vis.NumericColoredColumnLabelProvider;

/**
 * A {@link NumericColoredColumnLabelProvider} that provides labels for
 * {@link IPatchSetHistoryEntry}s with a {@link Number} as value. Other value
 * types and objects will be ignored.
 * 
 * @author Florian Zoubek
 *
 */
class PatchSetSimilarityHistoryLabelProvider extends NumericColoredColumnLabelProvider {

	private PatchSet patchSet;

	public PatchSetSimilarityHistoryLabelProvider(PatchSet patchSet, HSB minHSB, HSB maxHSB, Color fgColor1,
			Color fgColor2) {
		super(minHSB, maxHSB, fgColor1, fgColor2);
		this.patchSet = patchSet;
	}

	@Override
	public float getMaxValue(Object element) {
		return 1.0f;
	}

	@Override
	public float getMinValue(Object element) {
		return 0.0f;
	}

	@Override
	public float getValue(Object element) {
		if (element instanceof IPatchSetHistoryEntry) {
			Object value = ((IPatchSetHistoryEntry<?, ?>) element).getValue(patchSet);
			if (value instanceof Number) {
				return ((Number) value).floatValue();
			}
		}
		return 0;
	}

	@Override
	public boolean hasValue(Object element) {
		if (element instanceof IPatchSetHistoryEntry) {
			Object value = ((IPatchSetHistoryEntry<?, ?>) element).getValue(patchSet);
			return value instanceof Number;
		}
		return false;
	}

}
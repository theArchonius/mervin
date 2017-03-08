/*******************************************************************************
 * Copyright (c) 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.patchset.history;

import java.util.List;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;

/**
 * A named container for {@link IPatchSetHistoryEntry}s for use within the UI.
 * 
 * @author Florian Zoubek
 *
 */
public class NamedHistoryEntryContainer extends AbstractHistoryEntry<String, Double> {

	private String name;

	public NamedHistoryEntryContainer(String name, List<IPatchSetHistoryEntry<?, ?>> entries) {
		this(null, name, entries);
	}

	public NamedHistoryEntryContainer(IPatchSetHistoryEntry<?, ?> parent, String name,
			List<IPatchSetHistoryEntry<?, ?>> entries) {
		super(parent, entries);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Double getValue(PatchSet patchSet) {

		boolean hasValue = false;
		double valueSum = 0;

		List<IPatchSetHistoryEntry<?, ?>> subEntries = getSubEntries();

		for (IPatchSetHistoryEntry<?, ?> entry : subEntries) {

			Object value = entry.getValue(patchSet);

			if (value instanceof Number) {

				valueSum += ((Number) value).floatValue();
				hasValue = true;

			} else if (value instanceof DiffWithSimilarity) {

				valueSum += (float) ((DiffWithSimilarity) value).getSimilarity();
				hasValue = true;
			}
		}

		if (hasValue) {
			return valueSum / (double) subEntries.size();
		}
		return null;
	}

	@Override
	public void setValue(PatchSet patchSet, Double value) {
		// intentionally left empty
	}

	@Override
	public String getEntryObject() {
		return name;
	}

}
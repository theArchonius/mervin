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
package at.bitandart.zoubek.mervin.patchset.history.organizers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import at.bitandart.zoubek.mervin.patchset.history.IPatchSetHistoryEntry;
import at.bitandart.zoubek.mervin.patchset.history.NamedHistoryEntryContainer;

/**
 * A {@link IPatchSetHistoryEntryOrganizer} that groups the entries by their
 * category - either model or diagram differences.
 * 
 * @author Florian Zoubek
 *
 */
public class DiffCategoryOrganizer implements IPatchSetHistoryEntryOrganizer {

	protected static final String CONTAINER_NAME_MODEL_DIFFERENCES = "Model Differences";

	protected static final String CONTAINER_NAME_DIAGRAM_DIFFERENCES = "Diagram Differences";

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.bitandart.zoubek.mervin.patchset.history.
	 * IPatchSetHistoryEntryOrganizer#groupPatchSetHistoryEntries(java.util.
	 * List, java.util.List)
	 */
	@Override
	public Collection<Object> groupPatchSetHistoryEntries(List<? extends IPatchSetHistoryEntry<?, ?>> modelEntries,
			List<? extends IPatchSetHistoryEntry<?, ?>> diagramEntries) {
		List<Object> rootEntries = new ArrayList<Object>(2);
		rootEntries.add(new NamedHistoryEntryContainer(CONTAINER_NAME_MODEL_DIFFERENCES,
				new ArrayList<IPatchSetHistoryEntry<?, ?>>(modelEntries)));
		rootEntries.add(new NamedHistoryEntryContainer(CONTAINER_NAME_DIAGRAM_DIFFERENCES,
				new ArrayList<IPatchSetHistoryEntry<?, ?>>(diagramEntries)));
		return rootEntries;
	}

}

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

import java.util.Collection;
import java.util.List;

import at.bitandart.zoubek.mervin.patchset.history.IPatchSetHistoryEntry;

/**
 * Base interface for classes implementing strategies that group or organize
 * {@link IPatchSetHistoryEntry}s.
 * 
 * @author Florian Zoubek
 *
 */
public interface IPatchSetHistoryEntryOrganizer {

	/**
	 * groups the given model and diagram entries based on the strategy this
	 * class is using.
	 * 
	 * @param modelEntries
	 *            the {@link IPatchSetHistoryEntry}s for the semantic model.
	 * @param diagramEntries
	 *            the {@link IPatchSetHistoryEntry}s for the diagram model.
	 * @return a collection containing the grouped {@link IPatchSetHistoryEntry}
	 *         s.
	 */
	public Collection<Object> groupPatchSetHistoryEntries(List<? extends IPatchSetHistoryEntry<?, ?>> modelEntries,
			List<? extends IPatchSetHistoryEntry<?, ?>> diagramEntries);
}

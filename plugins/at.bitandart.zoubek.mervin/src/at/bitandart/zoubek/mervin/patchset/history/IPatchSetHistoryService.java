/*******************************************************************************
 * Copyright (c) 2015, 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.patchset.history;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * Base interface for services that create and handle entries that provide a
 * history for a given object across multiple {@link PatchSet}s.
 * 
 * @param <O>
 *            the type of the object that this service create entries for.
 * @param <V>
 *            the type of the values in the entry that this service creates.
 * 
 * @author Florian Zoubek
 *
 */
public interface IPatchSetHistoryService<O, V> {

	/**
	 * creates a new entry for the given object and collection of
	 * {@link PatchSet}s.
	 * 
	 * @param object
	 *            the object to create the entry for.
	 * @param patchSets
	 *            a collection of {@link PatchSet}s the entry should be able to
	 *            handle. However, the values of the created entry are allowed
	 *            to be null for any of the supplied patch sets.
	 * @param context
	 *            an object describing the context of the given object to create
	 *            the entry for. May be null if no context is available.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @return the entry matching the given parameters.
	 */
	IPatchSetHistoryEntry<O, V> createEntryFor(O object, Collection<PatchSet> patchSets, Object context,
			IProgressMonitor monitor);

	/**
	 * forces an update of a given entry's values using the collection of
	 * {@link PatchSet}s.
	 * 
	 * @param entry
	 *            the entry to update.
	 * @param patchSets
	 *            a {@link Collection} of {@link PatchSet}s whose values should
	 *            be updated.
	 * @param context
	 *            an object describing the context of the given object to create
	 *            the entry for. May be null if no context is available.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	void updateEntryFor(IPatchSetHistoryEntry<O, V> entry, Collection<PatchSet> patchSets, Object context,
			IProgressMonitor monitor);

	/**
	 * forces an update of a given entry's value using the given
	 * {@link PatchSet}.
	 * 
	 * @param entry
	 *            the entry to update.
	 * @param patchSet
	 *            the {@link PatchSet} whose associated value in the entry
	 *            should be udpated.
	 * @param context
	 *            an object describing the context of the given object to create
	 *            the entry for. May be null if no context is available.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	void updateEntryFor(IPatchSetHistoryEntry<O, V> entry, PatchSet patchSet, Object context, IProgressMonitor monitor);

}

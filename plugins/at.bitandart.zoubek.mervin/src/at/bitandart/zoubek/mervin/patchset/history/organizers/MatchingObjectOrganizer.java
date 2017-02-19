/*******************************************************************************
 * Copyright (c) 2017 Florian Zoubek.
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import at.bitandart.zoubek.mervin.IMatchHelper;
import at.bitandart.zoubek.mervin.IMervinContextConstants;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.IPatchSetHistoryEntry;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;
import at.bitandart.zoubek.mervin.patchset.history.NamedHistoryEntryContainer;

/**
 * An {@link DiffCategoryOrganizer} that groups the entries by their common
 * containing match. This organizer assumes that each entry contains only
 * {@link Diff}s whose matches are considered to have the same old or, if no old
 * value exists, the same new value.
 * 
 * @author Florian Zoubek
 *
 */
public class MatchingObjectOrganizer extends DiffCategoryOrganizer {

	private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	@Inject
	private IMatchHelper matchHelper;

	@Inject
	@Named(IMervinContextConstants.ACTIVE_MODEL_REVIEW)
	@Optional
	private ModelReview modelReview;

	public MatchingObjectOrganizer() {
		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
	}

	@Override
	public Collection<Object> groupPatchSetHistoryEntries(List<? extends IPatchSetHistoryEntry<?, ?>> modelEntries,
			List<? extends IPatchSetHistoryEntry<?, ?>> diagramEntries) {

		Collection<Object> rootEntries = super.groupPatchSetHistoryEntries(modelEntries, diagramEntries);
		for (Object entry : rootEntries) {

			if (entry instanceof NamedHistoryEntryContainer) {

				NamedHistoryEntryContainer container = ((NamedHistoryEntryContainer) entry);
				List<IPatchSetHistoryEntry<?, ?>> entries = new ArrayList<>(container.getSubEntries());
				organizeEntries(entries, container);
			}
		}

		return rootEntries;
	}

	private void organizeEntries(List<IPatchSetHistoryEntry<?, ?>> entries, IPatchSetHistoryEntry<?, ?> parent) {

		Map<Object, IPatchSetHistoryEntry<?, ?>> objectDiffMap = new HashMap<>();
		parent.getSubEntries().clear();

		for (IPatchSetHistoryEntry<?, ?> entry : entries) {

			Object key = getCategoryKey(entry);

			if (key != null) {

				IPatchSetHistoryEntry<?, ?> parentEntry = objectDiffMap.get(key);

				if (parentEntry == null) {

					parentEntry = new NamedHistoryEntryContainer(adapterFactoryLabelProvider.getText(key),
							new LinkedList<IPatchSetHistoryEntry<?, ?>>());

					parent.getSubEntries().add(parentEntry);
					objectDiffMap.put(key, parentEntry);
				}

				parentEntry.getSubEntries().add(entry);
			}
		}
	}

	/**
	 * @param entry
	 *            the entry to obtain the category key for.
	 * @return the category key for the given entry or null if no key could be
	 *         found.
	 */
	private Object getCategoryKey(IPatchSetHistoryEntry<?, ?> entry) {

		/* the old value is the primary key - as long as it is not null */
		Object primaryKey = null;

		/* the new value is the secondary key - as long as it is not null */
		Object secondaryKey = null;
		Object entryObject = entry.getEntryObject();

		if (entryObject instanceof Diff) {

			Diff diff = (Diff) entryObject;

			primaryKey = getPrimaryKey(diff);
			secondaryKey = getSecondaryKey(diff);
		}

		if (primaryKey != null) {
			return primaryKey;
		}

		/*
		 * try to obtain the primary key and secondary key from the value diff
		 * as fallback for the case that the assumption (the old values match
		 * or, if no old values exist, the new values match) is wrong
		 */
		if (modelReview != null) {

			EList<PatchSet> patchSets = modelReview.getPatchSets();
			for (PatchSet patchSet : patchSets) {

				Object value = entry.getValue(patchSet);
				Diff valueDiff = null;

				if (value instanceof Diff) {
					valueDiff = (Diff) value;
				} else if (value instanceof DiffWithSimilarity) {
					valueDiff = ((DiffWithSimilarity) value).getDiff();
				}

				if (valueDiff != null) {
					primaryKey = getPrimaryKey(valueDiff);
					if (secondaryKey != null) {
						secondaryKey = getSecondaryKey(valueDiff);
					}
				}

				if (primaryKey != null) {
					return primaryKey;
				}
			}

		}
		return secondaryKey;

	}

	/**
	 * @param diff
	 *            the {@link Diff} used to derive the key.
	 * @return the primary key derived from the given diff, or null if no
	 *         primary key could be derived.
	 */
	private Object getPrimaryKey(Diff diff) {
		return matchHelper.getOldValue(diff.getMatch());
	}

	/**
	 * @param diff
	 *            the {@link Diff} used to derive the key.
	 * @return the secondary key derived from the given diff, or null if no
	 *         secondary key could be derived.
	 */
	private Object getSecondaryKey(Diff diff) {
		return matchHelper.getNewValue(diff.getMatch());
	}

}

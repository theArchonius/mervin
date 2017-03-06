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
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import at.bitandart.zoubek.mervin.IMatchHelper;
import at.bitandart.zoubek.mervin.IMervinContextConstants;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.patchset.history.IPatchSetHistoryEntry;
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
				new ComposedAdapterFactory(EMFCompareRCPPlugin.getDefault().createFilteredAdapterFactoryRegistry()));
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
		Map<EPackage, IPatchSetHistoryEntry<?, ?>> packageEntries = new HashMap<>();
		parent.getSubEntries().clear();

		for (IPatchSetHistoryEntry<?, ?> entry : entries) {

			Object key = getCategoryKey(entry);

			if (key != null) {

				IPatchSetHistoryEntry<?, ?> parentEntry = objectDiffMap.get(key);

				if (parentEntry == null) {

					parentEntry = new NamedHistoryEntryContainer(adapterFactoryLabelProvider.getText(key),
							new LinkedList<IPatchSetHistoryEntry<?, ?>>());

					if (key instanceof EObject) {

						/* create a subentry for each containing package */

						EPackage containingPackage = getContainingPackage((EObject) key);
						if (containingPackage != null) {

							IPatchSetHistoryEntry<?, ?> packageEntry = packageEntries.get(containingPackage);
							if (packageEntry == null) {

								packageEntry = new NamedHistoryEntryContainer(
										adapterFactoryLabelProvider.getText(containingPackage),
										new LinkedList<IPatchSetHistoryEntry<?, ?>>());
								parent.getSubEntries().add(packageEntry);
								packageEntries.put(containingPackage, packageEntry);
							}
							packageEntry.getSubEntries().add(parentEntry);
						}

					} else {

						parent.getSubEntries().add(parentEntry);
					}

					objectDiffMap.put(key, parentEntry);
				}

				parentEntry.getSubEntries().add(entry);
			}
		}
	}

	/**
	 * @param eObject
	 *            the {@link EObject} to find the root {@link EPackage} for.
	 * @return the containing root package of the given {@link EObject}.
	 */
	private EPackage getContainingPackage(EObject eObject) {
		EPackage ePackage = eObject.eClass().getEPackage();
		while (ePackage.getESuperPackage() != null) {
			ePackage = ePackage.getESuperPackage();
		}
		return ePackage;
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

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
package at.bitandart.zoubek.mervin.patchset.history;

import java.util.Map;

import org.eclipse.emf.compare.Comparison;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import at.bitandart.zoubek.mervin.IReviewCompareService.Version;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * A cache that contains comparisons (between the {@link Version#OLD} versions
 * and the {@link Version#NEW} versions ) of two patch sets. Only one old and
 * new comparison is stored for each two patch sets.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetComparisonCache {

	private Table<PatchSetSpec, Version, Comparison> cacheData;

	public PatchSetComparisonCache() {
		cacheData = HashBasedTable.create();
	}

	/**
	 * adds the given comparisons for the given combination of patch sets. Any
	 * existing values will be overwritten.
	 * 
	 * @param patchSet1
	 *            one patch set of the combination.
	 * @param patchSet2
	 *            the other patch set of the combination.
	 * @param oldComparison
	 *            the comparison of the old versions.
	 * @param newComparison
	 *            the comparison of the new versions.
	 */
	public void add(PatchSet patchSet1, PatchSet patchSet2, Comparison oldComparison, Comparison newComparison) {
		PatchSetSpec patchSetSpec = new PatchSetSpec(patchSet1, patchSet2);
		cacheData.put(patchSetSpec, Version.OLD, oldComparison);
		cacheData.put(patchSetSpec, Version.NEW, newComparison);
	}

	/**
	 * removes the comparisons of the given patch set combination.
	 * 
	 * @param patchSet1
	 *            one patch set of the combination.
	 * @param patchSet2
	 *            the other patch set of the combination.
	 */
	public void remove(PatchSet patchSet1, PatchSet patchSet2) {
		PatchSetSpec patchSetSpec = new PatchSetSpec(patchSet1, patchSet2);
		cacheData.remove(patchSetSpec, Version.OLD);
		cacheData.remove(patchSet2, Version.NEW);
	}

	/**
	 * @param patchSet1
	 *            one patch set of the combination.
	 * @param patchSet2
	 *            the other patch set of the combination.
	 * @return true if the cache contains comparisons for the given patch set
	 *         combination, false otherwise.
	 */
	public boolean containsValues(PatchSet patchSet1, PatchSet patchSet2) {
		return !getComparisons(patchSet1, patchSet2).isEmpty();
	}

	/**
	 * @param patchSet1
	 *            one patch set of the combination.
	 * @param patchSet2
	 *            the other patch set of the combination.
	 * @param version
	 *            the version to get the comparison for.
	 * @return the cached comparison for the given parameters or null if no such
	 *         comparison exists.
	 */
	public Comparison getComparison(PatchSet patchSet1, PatchSet patchSet2, Version version) {
		return cacheData.get(new PatchSetSpec(patchSet1, patchSet2), version);
	}

	/**
	 * @param patchSet1
	 *            one patch set of the combination.
	 * @param patchSet2
	 *            the other patch set of the combination.
	 * @return the comparisons for the given patch set combination.
	 */
	public Map<Version, Comparison> getComparisons(PatchSet patchSet1, PatchSet patchSet2) {
		return cacheData.row(new PatchSetSpec(patchSet1, patchSet2));
	}

	/**
	 * internal class that describes a patch set comparison combination, where
	 * the order of the patch sets is not relevant.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class PatchSetSpec {

		private PatchSet patchSet1;
		private PatchSet patchSet2;

		public PatchSetSpec(PatchSet patchSet1, PatchSet patchSet2) {
			super();
			this.patchSet1 = patchSet1;
			this.patchSet2 = patchSet2;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((patchSet1 == null || patchSet2 == null) ? 0
					: patchSet1.getId().hashCode() * patchSet2.getId().hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PatchSetSpec other = (PatchSetSpec) obj;

			return (other.patchSet1 == patchSet1 && other.patchSet2 == patchSet2)
					|| (other.patchSet2 == patchSet1 && other.patchSet1 == patchSet2);

		}

	}

}

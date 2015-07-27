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
package at.bitandart.zoubek.mervin.patchset.history;

import java.util.List;

import org.eclipse.emf.compare.Diff;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * Base interface for services that create and handle entries that provide a
 * similarity history for a Diff across multiple {@link PatchSet}s.
 * 
 * @author Florian Zoubek
 *
 */
public interface ISimilarityHistoryService extends
		IPatchSetHistoryService<Diff, Double> {

	/**
	 * creates all entries for all model differences in the given patch set.
	 * 
	 * @param patchSet the patchSet to retrieve the {@link Diff}s from
	 * @param patchSets the patchSets used to calculate the similarity
	 * @return a list of {@link IPatchSetHistoryEntry}s for all model
	 *         differences in the given patch set
	 */
	public List<IPatchSetHistoryEntry<Diff, Double>> createModelEntries(
			PatchSet patchSet, List<PatchSet> patchSets);

	/**
	 * creates all entries for all model differences in the given list of patch
	 * sets. No duplicated entries will be created for equal diffs in different
	 * patch sets.
	 * 
	 * @param patchSets
	 * @return a list of {@link IPatchSetHistoryEntry}s for all model
	 *         differences in the given patch set
	 */
	public List<IPatchSetHistoryEntry<Diff, Double>> createModelEntries(
			List<PatchSet> patchSets);

	/**
	 * creates all entries for all diagram differences in the given patch set.
	 * 
	 * @param patchSet the patchSet to retrieve the {@link Diff}s from
	 * @param patchSets the patchSets used to calculate the similarity
	 * @return a list of {@link IPatchSetHistoryEntry}s for all diagram
	 *         differences in the given patch set
	 */
	public List<IPatchSetHistoryEntry<Diff, Double>> createDiagramEntries(
			PatchSet patchSet, List<PatchSet> patchSets);

	/**
	 * creates all entries for all diagram differences in the given list of
	 * patch sets. No duplicated entries will be created for equal diffs in
	 * different patch sets.
	 * 
	 * @param patchSets
	 * @return a list of {@link IPatchSetHistoryEntry}s for all diagram
	 *         differences in the given patch set
	 */
	public List<IPatchSetHistoryEntry<Diff, Double>> createDiagramEntries(
			List<PatchSet> patchSets);

}

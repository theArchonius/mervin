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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.eobject.EditionDistance;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.util.vis.MathUtil;

/**
 * A {@link IPatchSetHistoryService} that calculates the history of similarities
 * of a {@link Diff} object to the best matching {@link Diff} in a
 * {@link PatchSet}.
 * 
 * @author Florian Zoubek
 *
 */
public class ChangeSimilarityHistoryService implements
		ISimilarityHistoryService{

	@Override
	public IPatchSetHistoryEntry<Diff, Double> createEntryFor(Diff object,
			Collection<PatchSet> patchSets) {

		PatchSetHistoryEntryImpl<Diff, Double> entry = new PatchSetHistoryEntryImpl<Diff, Double>(object);
		updateEntryFor(entry, patchSets);

		return entry;
	}

	@Override
	public void updateEntryFor(IPatchSetHistoryEntry<Diff, Double> entry,
			Collection<PatchSet> patchSets) {

		for (PatchSet patchSet : patchSets) {
			updateEntryFor(entry, patchSet);
		}

	}

	@Override
	public void updateEntryFor(IPatchSetHistoryEntry<Diff, Double> entry,
			PatchSet patchSet) {

		Diff entryObject = entry.getEntryObject();
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		Match match = findMatch(entryObject, patchSet, comparison);

		if (match != null && match.getRight() != null) {

			double distance = calculateDistance(comparison, entryObject,
					match.getRight());
			entry.setValue(patchSet, distance);

		}

		postProcessEntry(entry, patchSet, match, comparison);

	}

	/**
	 * called once after
	 * {@link #updateEntryFor(IPatchSetHistoryEntry, Collection)} has finished.
	 * 
	 * @param entry
	 * @param patchSet
	 * @param match the matching diff in the patchSet, or null if none has been found
	 * @param comparison
	 */
	protected void postProcessEntry(IPatchSetHistoryEntry<Diff, Double> entry,
			PatchSet patchSet, Match match, Comparison comparison) {
		// intentionally left empty
	}

	protected Match findMatch(Diff entryObject, PatchSet patchSet,
			Comparison comparison) {

		IEObjectMatcher objectMatcher = DefaultMatchEngine
				.createDefaultEObjectMatcher(UseIdentifiers.NEVER);
		List<EObject> leftEObjects = new ArrayList<EObject>(1);
		leftEObjects.add(entryObject);

		EList<Diff> modelDifferences = patchSet.getModelComparison()
				.getDifferences();
		EList<Diff> diagramDifferences = patchSet.getDiagramComparison()
				.getDifferences();
		List<EObject> rightEObjects = new ArrayList<EObject>(
				modelDifferences.size() + diagramDifferences.size());
		rightEObjects.addAll(modelDifferences);
		rightEObjects.addAll(diagramDifferences);

		objectMatcher.createMatches(comparison, leftEObjects.iterator(),
				rightEObjects.iterator(), new ArrayList<EObject>().iterator(),
				new BasicMonitor());

		return comparison.getMatch(entryObject);
	}

	/**
	 * calculates the distance between to {@link EObject}s in the range 0(not
	 * equal) and 1(equal).
	 * 
	 * @param comparison
	 * @param left
	 * @param right
	 * @return
	 */
	protected double calculateDistance(Comparison comparison, EObject left,
			EObject right) {

		EditionDistance editionDistance = new EditionDistance(
				WeightProviderDescriptorRegistryImpl.createStandaloneInstance());
		return MathUtil.map(editionDistance.distance(comparison, left, right), 0.0, Double.MAX_VALUE, 1.0, 0.0);
	}


	@Override
	public List<IPatchSetHistoryEntry<Diff, Double>> createModelEntries(
			PatchSet patchSet, List<PatchSet> patchSets) {
		
		return createEntriesForDiffs(patchSet.getModelComparison().getDifferences(), patchSets);
	}

	@Override
	public List<IPatchSetHistoryEntry<Diff, Double>> createDiagramEntries(
			PatchSet patchSet, List<PatchSet> patchSets) {

		return createEntriesForDiffs(patchSet.getDiagramComparison().getDifferences(), patchSets);
	}
	
	private List<IPatchSetHistoryEntry<Diff, Double>> createEntriesForDiffs(List<Diff> differences,List<PatchSet> patchSets){
		
		List<IPatchSetHistoryEntry<Diff, Double>> entries = new LinkedList<IPatchSetHistoryEntry<Diff,Double>>();
		
		for(Diff diff : differences){
			IPatchSetHistoryEntry<Diff, Double> historyEntry = createEntryFor(diff, patchSets);
			entries.add(historyEntry);
		}
		
		return entries;
	}
	
	@Override
	public List<IPatchSetHistoryEntry<Diff, Double>> createModelEntries(
			List<PatchSet> patchSets) {
		throw new UnsupportedOperationException("Not yet implemented");
		// TODO implement
	}

	@Override
	public List<IPatchSetHistoryEntry<Diff, Double>> createDiagramEntries(
			List<PatchSet> patchSets) {
		throw new UnsupportedOperationException("Not yet implemented");
		// TODO implement
	}

}

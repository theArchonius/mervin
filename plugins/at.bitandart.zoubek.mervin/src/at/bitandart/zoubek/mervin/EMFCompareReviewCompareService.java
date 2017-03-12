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
package at.bitandart.zoubek.mervin;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import at.bitandart.zoubek.mervin.model.modelreview.DiagramResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.predicates.MervinPredicates;

/**
 * Default EMF Compare based {@link IReviewCompareService} that operates only on
 * versioned git resources.
 * 
 * @author Florian Zoubek
 *
 */
public class EMFCompareReviewCompareService implements IReviewCompareService {

	@Override
	public Comparison comparePatchSetModelVersions(PatchSet patchSet, IProgressMonitor monitor)
			throws OperationCanceledException {

		return comparePatchSetVersions(patchSet, Version.NEW, patchSet, Version.OLD, MervinPredicates.modelResource(),
				monitor);
	}

	@Override
	public Comparison comparePatchSetDiagramVersions(PatchSet patchSet, IProgressMonitor monitor)
			throws OperationCanceledException {

		return comparePatchSetVersions(patchSet, Version.NEW, patchSet, Version.OLD, MervinPredicates.diagramResource(),
				monitor);
	}

	@Override
	public Comparison compareWithPatchSetVersion(EObject eObject, PatchSet patchSet, Version version,
			IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		checkCancellation(subMonitor);

		ResourceSet resourceSet = getResourceSet(patchSet, version);

		EcoreUtil.resolveAll(resourceSet);

		EMFCompare comparator = createEMFCompare();

		IComparisonScope scope = createComparisonScope(eObject.eResource().getResourceSet(), resourceSet);
		Comparison comparison = comparator.compare(scope, BasicMonitor.toMonitor(subMonitor.newChild(100)));
		return comparison;
	}

	@Override
	public Comparison comparePatchSetVersions(PatchSet leftPatchSet, Version leftVersion, PatchSet rightPatchSet,
			Version rightVersion, IProgressMonitor monitor) {

		return comparePatchSetVersions(leftPatchSet, leftVersion, rightPatchSet, rightVersion,
				Predicates.<Resource> alwaysTrue(), monitor);
	}

	/**
	 * compares all diagrams and models of the given left patch set and version
	 * with all diagrams and models of the given right patch set and version.
	 * 
	 * @param newPatchSet
	 *            the new (left) patch containing the models/diagrams to
	 *            compare.
	 * @param newVersion
	 *            the new (left) version of the models/diagrams to compare with.
	 * @param oldPatchSet
	 *            the old (right) patch containing the models/diagrams to
	 *            compare.
	 * @param oldVersion
	 *            the old (right) version of the models/diagrams to compare
	 *            with.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @return the resulting comparison.
	 * @throws OperationCanceledException
	 *             if the operations has been cancelled by the given monitor.
	 */
	private Comparison comparePatchSetVersions(PatchSet newPatchSet, Version newVersion, PatchSet oldPatchSet,
			Version oldVersion, Predicate<Resource> resourceFilter, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		checkCancellation(subMonitor);

		ResourceSet newResourceSet = getResourceSet(newPatchSet, newVersion);
		ResourceSet oldResourceSet = getResourceSet(oldPatchSet, oldVersion);

		EcoreUtil.resolveAll(newResourceSet);
		EcoreUtil.resolveAll(oldResourceSet);

		EMFCompare comparator = createEMFCompare();

		IComparisonScope scope = createComparisonScope(newResourceSet, oldResourceSet, resourceFilter);
		Comparison comparison = comparator.compare(scope, BasicMonitor.toMonitor(subMonitor.newChild(100)));
		return comparison;
	}

	@Override
	public Comparison matchWithPatchSetVersion(EObject eObject, PatchSet patchSet, Version version,
			IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		checkCancellation(subMonitor);

		ResourceSet oldResourceSet = getResourceSet(patchSet, version);

		EcoreUtil.resolveAll(oldResourceSet);

		IComparisonScope scope = createComparisonScope(eObject.eResource().getResourceSet(), oldResourceSet);

		return MatchEngineFactoryRegistryImpl.createStandaloneInstance().getHighestRankingMatchEngineFactory(scope)
				.getMatchEngine().match(scope, BasicMonitor.toMonitor(subMonitor.newChild(100)));
	}

	@Override
	public Comparison matchPatchSetVersions(PatchSet patchSet, Version version, PatchSet otherPatchSet,
			Version otherVersion, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		checkCancellation(subMonitor);

		ResourceSet leftResourceSet = getResourceSet(patchSet, version);
		ResourceSet rightResourceSet = getResourceSet(otherPatchSet, otherVersion);

		EcoreUtil.resolveAll(leftResourceSet);
		EcoreUtil.resolveAll(rightResourceSet);

		IComparisonScope scope = createComparisonScope(leftResourceSet, rightResourceSet);

		return MatchEngineFactoryRegistryImpl.createStandaloneInstance().getHighestRankingMatchEngineFactory(scope)
				.getMatchEngine().match(scope, BasicMonitor.toMonitor(subMonitor.newChild(100)));
	}

	@Override
	public void updateSelectedComparison(ModelReview review, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Updating comparison...", 220);
		checkCancellation(subMonitor);

		PatchSet oldPatchSet = review.getLeftPatchSet();
		PatchSet newPatchSet = review.getRightPatchSet();
		PatchSet fallbackPatchSet = null;

		EList<PatchSet> patchSets = review.getPatchSets();
		if (!patchSets.isEmpty()) {
			fallbackPatchSet = patchSets.get(0);
		}

		ResourceSet oldResourceSet = null;
		ResourceSet newResourceSet = null;

		if (oldPatchSet != null) {
			oldResourceSet = getResourceSet(oldPatchSet, Version.NEW);

		} else if (oldPatchSet == null && fallbackPatchSet != null) {
			oldResourceSet = getResourceSet(fallbackPatchSet, Version.OLD);
		}

		if (newPatchSet != null) {
			newResourceSet = getResourceSet(newPatchSet, Version.NEW);

		} else if (oldPatchSet == null && fallbackPatchSet != null) {
			newResourceSet = getResourceSet(fallbackPatchSet, Version.OLD);
		}

		if (newResourceSet == null || oldResourceSet == null) {
			return;
		}

		EMFCompare emfCompare = createEMFCompare();

		subMonitor.worked(10);
		checkCancellation(subMonitor);

		IComparisonScope modelScope = createComparisonScope(newResourceSet, oldResourceSet,
				MervinPredicates.modelResource());
		Comparison modelComparison = emfCompare.compare(modelScope, BasicMonitor.toMonitor(subMonitor.newChild(100)));

		checkCancellation(subMonitor);

		IComparisonScope diagramScope = createComparisonScope(newResourceSet, oldResourceSet,
				MervinPredicates.diagramResource());
		Comparison diagramComparison = emfCompare.compare(diagramScope,
				BasicMonitor.toMonitor(subMonitor.newChild(100)));

		review.setSelectedModelComparison(modelComparison);
		review.setSelectedDiagramComparison(diagramComparison);
	}

	/**
	 * @return the default {@link EMFCompare} instance to use for comparison.
	 */
	private EMFCompare createEMFCompare() {
		return EMFCompare.builder()
				.setPostProcessorRegistry(EMFCompareRCPPlugin.getDefault().getPostProcessorRegistry()).build();
	}

	/**
	 * creates the comparison scope for the given resource sets.
	 * 
	 * @param newResourceSet
	 *            the new resource set.
	 * @param oldResourceSet
	 *            the old resource set.
	 * @return the {@link IComparisonScope} for the given resource sets.
	 */
	private IComparisonScope createComparisonScope(ResourceSet newResourceSet, ResourceSet oldResourceSet) {
		return createComparisonScope(newResourceSet, oldResourceSet, Predicates.<Resource> alwaysTrue());
	}

	/**
	 * creates the comparison scope for the given resource sets.
	 * 
	 * @param newResourceSet
	 *            the new (left) resource set.
	 * @param oldResourceSet
	 *            the old (right) resource set.
	 * @param resourceFilter
	 *            the resource filter to apply.
	 * @return the {@link IComparisonScope} for the given resource sets.
	 */
	private IComparisonScope createComparisonScope(ResourceSet newResourceSet, ResourceSet oldResourceSet,
			Predicate<Resource> resourceFilter) {

		DefaultComparisonScope scope = new DefaultComparisonScope(newResourceSet, oldResourceSet, null);
		scope.setResourceSetContentFilter(Predicates.and(MervinPredicates.mervinGitResource(), resourceFilter));
		return scope;
	}

	/**
	 * checks the monitor for cancellation an throws an
	 * {@link OperationCanceledException} if the monitor has been cancelled.
	 * 
	 * @param monitor
	 *            them monitor to check or null.
	 * @throws OperationCanceledException
	 *             if the monitor has been cancelled.
	 */
	private void checkCancellation(IProgressMonitor monitor) throws OperationCanceledException {
		if (monitor != null && monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
	}

	/**
	 * 
	 * @param patchSet
	 *            the patch set that contains the models and diagrams.
	 * @param version
	 *            the version to obtain the patch set for.
	 * @return the resource set containing all models and diagram of the
	 *         requested patch set and version.
	 */
	private ResourceSet getResourceSet(PatchSet patchSet, Version version) {

		List<ModelResource> involvedModels = getInvolvedModels(patchSet, version);
		List<DiagramResource> involvedDiagrams = getInvolvedDiagrams(patchSet, version);
		ResourceSet resourceSet = new ResourceSetImpl();

		if (!involvedModels.isEmpty()) {
			resourceSet = involvedModels.get(0).getObjects().get(0).eResource().getResourceSet();
		} else if (!involvedDiagrams.isEmpty()) {
			resourceSet = involvedDiagrams.get(0).getObjects().get(0).eResource().getResourceSet();
		}
		return resourceSet;
	}

	/**
	 * convenience method to retrieve a version of the involved models of a
	 * patch set.
	 * 
	 * @param patchSet
	 *            the patch set containing the models.
	 * @param version
	 *            the version of the models.
	 * @return the involved models or an empty list if no involved models exist.
	 */
	private List<ModelResource> getInvolvedModels(PatchSet patchSet, Version version) {
		switch (version) {
		case OLD:
			return patchSet.getOldInvolvedModels();
		case NEW:
			return patchSet.getNewInvolvedModels();
		default:
			return Collections.emptyList();
		}
	}

	/**
	 * convenience method to retrieve a version of the involved diagrams of a
	 * patch set.
	 * 
	 * @param patchSet
	 *            the patch set containing the diagrams.
	 * @param version
	 *            the version of the diagrams.
	 * @return the involved diagrams or an empty list if no involved diagrams
	 *         exist.
	 */
	private List<DiagramResource> getInvolvedDiagrams(PatchSet patchSet, Version version) {
		switch (version) {
		case OLD:
			return patchSet.getOldInvolvedDiagrams();
		case NEW:
			return patchSet.getNewInvolvedDiagrams();
		default:
			return Collections.emptyList();
		}
	}

}

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

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.common.base.Predicate;

import at.bitandart.zoubek.mervin.gerrit.GitURIParser;
import at.bitandart.zoubek.mervin.model.modelreview.DiagramResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelResource;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * Default EMF Compare based {@link IReviewCompareService} that operates only on
 * versioned git resources.
 * 
 * @author Florian Zoubek
 *
 */
public class EMFCompareReviewCompareService implements IReviewCompareService {

	/**
	 * A {@link Resource} predicate that returns true if the resource is a
	 * mervin git resource.
	 */
	private static final Predicate<Resource> PREDICATE_GIT_RESOURCE = new Predicate<Resource>() {

		@Override
		public boolean apply(Resource resource) {
			org.eclipse.emf.common.util.URI uri = resource.getURI();
			return uri.scheme().equals(GitURIParser.GIT_COMMIT_SCHEME);
		}
	};

	@Override
	public Comparison compareWithPatchSetVersion(EObject eObject, PatchSet patchSet, Version version) {

		ResourceSet resourceSet = getResourceSet(patchSet, version);

		EcoreUtil.resolveAll(resourceSet);

		EMFCompare comparator = EMFCompare.builder().build();

		DefaultComparisonScope scope = new DefaultComparisonScope(eObject.eResource().getResourceSet(), resourceSet,
				null);
		scope.setResourceSetContentFilter(PREDICATE_GIT_RESOURCE);
		Comparison comparison = comparator.compare(scope);
		return comparison;
	}

	@Override
	public Comparison comparePatchSetVersions(PatchSet leftPatchSet, Version leftVersion, PatchSet rightPatchSet,
			Version rightVersion) {

		ResourceSet leftResourceSet = getResourceSet(leftPatchSet, leftVersion);
		ResourceSet rightResourceSet = getResourceSet(rightPatchSet, rightVersion);

		EcoreUtil.resolveAll(leftResourceSet);
		EcoreUtil.resolveAll(rightResourceSet);

		EMFCompare comparator = EMFCompare.builder().build();

		DefaultComparisonScope scope = new DefaultComparisonScope(leftResourceSet, rightResourceSet, null);
		scope.setResourceSetContentFilter(PREDICATE_GIT_RESOURCE);
		Comparison comparison = comparator.compare(scope);
		return comparison;
	}

	@Override
	public Comparison matchWithPatchSetVersion(EObject eObject, PatchSet patchSet, Version version) {

		ResourceSet oldResourceSet = getResourceSet(patchSet, version);

		EcoreUtil.resolveAll(oldResourceSet);

		DefaultComparisonScope scope = new DefaultComparisonScope(eObject.eResource().getResourceSet(), oldResourceSet,
				null);
		scope.setResourceSetContentFilter(PREDICATE_GIT_RESOURCE);

		return MatchEngineFactoryRegistryImpl.createStandaloneInstance().getHighestRankingMatchEngineFactory(scope)
				.getMatchEngine().match(scope, new BasicMonitor());
	}

	@Override
	public Comparison matchPatchSetVersions(PatchSet patchSet, Version version, PatchSet otherPatchSet,
			Version otherVersion) {

		ResourceSet leftResourceSet = getResourceSet(patchSet, version);
		ResourceSet rightResourceSet = getResourceSet(otherPatchSet, otherVersion);

		EcoreUtil.resolveAll(leftResourceSet);
		EcoreUtil.resolveAll(rightResourceSet);

		DefaultComparisonScope scope = new DefaultComparisonScope(leftResourceSet, rightResourceSet, null);
		scope.setResourceSetContentFilter(PREDICATE_GIT_RESOURCE);

		return MatchEngineFactoryRegistryImpl.createStandaloneInstance().getHighestRankingMatchEngineFactory(scope)
				.getMatchEngine().match(scope, new BasicMonitor());
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

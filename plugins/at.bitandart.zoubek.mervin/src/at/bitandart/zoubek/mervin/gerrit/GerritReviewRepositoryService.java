/*******************************************************************************
 * Copyright (c) 2015, 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.gerrit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheBuilder;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryState;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.google.common.base.Predicate;

import at.bitandart.zoubek.mervin.IReviewDescriptor;
import at.bitandart.zoubek.mervin.IReviewRepositoryService;
import at.bitandart.zoubek.mervin.exceptions.InvalidReviewException;
import at.bitandart.zoubek.mervin.exceptions.InvalidReviewRepositoryException;
import at.bitandart.zoubek.mervin.exceptions.RepositoryIOException;
import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.CommentLink;
import at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch;
import at.bitandart.zoubek.mervin.model.modelreview.DiagramResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelPatch;
import at.bitandart.zoubek.mervin.model.modelreview.ModelResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.Patch;
import at.bitandart.zoubek.mervin.model.modelreview.PatchChangeType;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.User;

/**
 * <p>
 * This service handles reviews stored at a remote gerrit git repository
 * referenced from a local clone of the repository. Any URI provided to this
 * service used to identify a repository must point to a local git repository
 * where the origin remote must point to the remote gerrit git repository. Also
 * the user used to access the repository must have read and write access to all
 * change refs at the server.
 * </p>
 * 
 * <p>
 * <b>Important:</b> The review id of a change is not the change id, it is the
 * internal primary key of the change used by Gerrit!.
 * </p>
 * 
 * @author Florian Zoubek
 *
 * @see GerritReviewDescriptor
 */
public class GerritReviewRepositoryService implements IReviewRepositoryService {

	/**
	 * the ref pattern used to identify change refs and parse the corresponding
	 * change id and patch set id
	 */
	private static final String CHANGE_REF_PATTERN = "refs/changes/(?<modChangePk>\\d\\d)/(?<changePk>\\d*)/(?<patchSetId>.*)";

	@SuppressWarnings("unused")
	private static final String CHANGE_REF_PATTERN_GROUP_MOD_CHANGE_PK = "modChangePk";
	private static final String CHANGE_REF_PATTERN_GROUP_CHANGE_PK = "changePk";
	private static final String CHANGE_REF_PATTERN_GROUP_PATCH_SET_ID = "patchSetId";
	private static final String COMMENTS_BASE_REF = "refs/mervin/comments";
	private static final String COMMENTS_FILE_EXTENSION = "modelreview";
	private static final String COMMENTS_FILE_URI = "comments." + COMMENTS_FILE_EXTENSION;

	/**
	 * the model review factory used by this service to create all model
	 * elements
	 */
	@Inject
	private ModelReviewFactory modelReviewFactory;

	@SuppressWarnings("restriction")
	@Inject
	private org.eclipse.e4.core.services.log.Logger logger;

	@Inject
	private IEclipseContext eclipseContext;

	@SuppressWarnings("restriction")
	@Override
	public List<IReviewDescriptor> getReviews(URI uri) throws InvalidReviewRepositoryException {

		List<IReviewDescriptor> changeIds = new LinkedList<>();

		try {
			// connect to the local git repository
			Git git = Git.open(new File(uri));

			try {
				// Assume that origin refers to the remote gerrit repository
				// list all remote refs from origin
				Collection<Ref> remoteRefs = git.lsRemote().setTimeout(60).call();

				Pattern changeRefPattern = Pattern.compile(CHANGE_REF_PATTERN);

				// search for change refs
				for (Ref ref : remoteRefs) {

					Matcher matcher = changeRefPattern.matcher(ref.getName());
					if (matcher.matches()) {
						String changePk = matcher.group(CHANGE_REF_PATTERN_GROUP_CHANGE_PK);
						String changeId = "<unknown>";
						GerritReviewDescriptor reviewDescriptor;
						try {
							reviewDescriptor = new GerritReviewDescriptor(Integer.parseInt(changePk), changeId);
						} catch (NumberFormatException nfe) {
							// FIXME ignore it or throw an exception?
							break;
						}

						if (!changeIds.contains(reviewDescriptor)) {
							changeIds.add(reviewDescriptor);

							/*
							 * the change id is present in all commit messages,
							 * so we extract it from the commit message of the
							 * current ref
							 */
							FetchResult fetchResult = git.fetch().setRefSpecs(new RefSpec(ref.getName())).call();

							Ref localRef = fetchResult.getAdvertisedRef(ref.getName());
							RevWalk revWalk = new RevWalk(git.getRepository());
							RevCommit commit = revWalk.parseCommit(localRef.getObjectId());
							String[] paragraphs = commit.getFullMessage().split("\n");
							String lastParagraph = paragraphs[paragraphs.length - 1];
							Pattern pattern = Pattern.compile(".*Change-Id: (I[^ \n]*).*");
							Matcher changeIdMatcher = pattern.matcher(lastParagraph);

							if (changeIdMatcher.matches()) {
								changeId = changeIdMatcher.group(1);
								reviewDescriptor.setChangeId(changeId);
								;
							} else {
								logger.warn(MessageFormat.format(
										"Could not find the change id for Gerrit change with primary key {0}",
										changePk));
							}
							revWalk.close();
						}
					}
				}

			} catch (GitAPIException e) {
				throw new RepositoryIOException("Error during loading all remote changes", e);
			}

		} catch (IOException e) {
			throw new InvalidReviewRepositoryException("Could not open local git repository", e);
		}
		return changeIds;
	}

	@Override
	public ModelReview loadReview(URI uri, String id, User currentReviewer, IProgressMonitor monitor)
			throws InvalidReviewRepositoryException, InvalidReviewException, RepositoryIOException {
		/*
		 * Fetch all refs to the patch sets for the particular change and create
		 * the model instance from it
		 */

		monitor.beginTask("Connecting to repository", IProgressMonitor.UNKNOWN);

		try {
			Git git = Git.open(new File(uri));
			int iId = Integer.parseInt(id);

			// First of all: fetch the patch sets
			// git fetch origin
			// +refs/changes/id%100/<cid>/*:refs/changes/id%100/<cid>/*
			// Refspec of a patchset:
			// +refs/changes/id%100/<cid>/<psId>:refs/changes/id%100/<cid>/<psId>

			monitor.beginTask("Fetching change ref", IProgressMonitor.UNKNOWN);
			git.fetch()
					.setRefSpecs(new RefSpec(MessageFormat.format(
							"+refs/changes/{0,number,00}/{1}/*:refs/changes/{0,number,00}/{1}/*", iId % 100, iId)))
					.call();

			// create model instance

			ModelReview modelReview = modelReviewFactory.createModelReview();
			modelReview.setId(id);
			modelReview.setRepositoryURI(uri.toString());
			modelReview.setCurrentReviewer(currentReviewer);
			EList<PatchSet> patchSets = modelReview.getPatchSets();

			Repository repository = git.getRepository();
			Map<String, Ref> allRefs = repository.getAllRefs();
			Pattern changeRefPattern = Pattern.compile(CHANGE_REF_PATTERN);

			monitor.beginTask("Loading Patch Sets", allRefs.size());

			List<ResourceSet> resourceSets = new LinkedList<>();

			for (Entry<String, Ref> refEntry : allRefs.entrySet()) {
				Matcher matcher = changeRefPattern.matcher(refEntry.getValue().getName());
				if (matcher.matches() && matcher.group(CHANGE_REF_PATTERN_GROUP_CHANGE_PK).equals(id)) {

					PatchSet patchSet = modelReviewFactory.createPatchSet();
					patchSets.add(patchSet);
					patchSet.setId(matcher.group(CHANGE_REF_PATTERN_GROUP_PATCH_SET_ID));

					monitor.subTask("Loading Patch Set #" + patchSet.getId());

					// load patched files
					loadPatches(patchSet, refEntry.getValue(), git);

					// load involved models
					resourceSets.addAll(loadInvolvedModelsAndDiagrams(patchSet, refEntry.getValue(), git));

					// compare the involved models
					patchSet.setModelComparison(compareModels(patchSet));

					// compare the involved diagrams
					patchSet.setDiagramComparison(compareDiagrams(patchSet));
				}
				monitor.worked(1);
			}

			monitor.beginTask("Sorting Patch Sets", IProgressMonitor.UNKNOWN);

			/*
			 * sort by their identifiers, numeric identifiers before string
			 * identifiers (gerrit currently has only numeric patch set
			 * identifiers, but to be on the save side also consider non-numeric
			 * identifiers )
			 */
			ECollections.sort(patchSets, new Comparator<PatchSet>() {

				@Override
				public int compare(PatchSet o1, PatchSet o2) {
					String psId1 = o1.getId();
					String psId2 = o2.getId();
					Integer iPsId1 = null;
					Integer iPsId2 = null;
					try {
						iPsId1 = Integer.valueOf(psId1);
					} catch (NumberFormatException nfe) {
					}
					try {
						iPsId2 = Integer.valueOf(psId2);
					} catch (NumberFormatException nfe) {
					}

					if (iPsId1 != null && iPsId2 != null) {
						// both numeric ids
						return iPsId1.compareTo(iPsId2);
					} else if (iPsId1 != null && iPsId2 == null) {
						// only one is numeric, the numeric id is always less
						// than the string id
						return -1;
					} else if (iPsId1 == null && iPsId2 != null) {
						// only one is numeric, the numeric id is always less
						// than the string id
						return 1;
					}

					// fallback to string sort
					return psId1.compareTo(psId2);
				}
			});

			monitor.beginTask("Loading Comments", IProgressMonitor.UNKNOWN);

			loadComments(repository, modelReview, resourceSets);

			monitor.done();

			return modelReview;

		} catch (IOException e) {
			throw new InvalidReviewRepositoryException("Could not open local git repository", e);
		} catch (NumberFormatException e) {
			throw new InvalidReviewException(MessageFormat.format("Invalid review id: {0}", id));
		} catch (GitAPIException e) {
			throw new RepositoryIOException("Error occured during reading from the git repository", e);
		}
	}

	/**
	 * compares the old and new versions of the involved models in the specified
	 * {@link PatchSet}.
	 * 
	 * @param patchSet
	 * @return
	 */
	public Comparison compareModels(PatchSet patchSet) {

		EList<ModelResource> oldInvolvedModels = patchSet.getOldInvolvedModels();
		EList<ModelResource> newInvolvedModels = patchSet.getNewInvolvedModels();
		ResourceSet oldResourceSet = new ResourceSetImpl();
		ResourceSet newResourceSet = new ResourceSetImpl();

		if (!oldInvolvedModels.isEmpty()) {
			oldResourceSet = oldInvolvedModels.get(0).getObjects().get(0).eResource().getResourceSet();
		}

		if (!newInvolvedModels.isEmpty()) {
			newResourceSet = newInvolvedModels.get(0).getObjects().get(0).eResource().getResourceSet();
		}

		EcoreUtil.resolveAll(oldResourceSet);
		EcoreUtil.resolveAll(newResourceSet);

		EMFCompare comparator = EMFCompare.builder().build();

		DefaultComparisonScope scope = new DefaultComparisonScope(newResourceSet, oldResourceSet, null);
		scope.setResourceSetContentFilter(new Predicate<Resource>() {

			@Override
			public boolean apply(Resource resource) {
				return !resource.getURI().fileExtension().equals("notation");
			}
		});
		Comparison comparison = comparator.compare(scope);
		return comparison;
	}

	/**
	 * compares the old and new versions of the involved diagrams in the
	 * specified {@link PatchSet}.
	 * 
	 * @param patchSet
	 * @return
	 */
	public Comparison compareDiagrams(PatchSet patchSet) {

		EList<DiagramResource> oldInvolvedDiagrams = patchSet.getOldInvolvedDiagrams();
		EList<DiagramResource> newInvolvedDiagrams = patchSet.getNewInvolvedDiagrams();
		ResourceSet oldResourceSet = new ResourceSetImpl();
		ResourceSet newResourceSet = new ResourceSetImpl();

		if (!oldInvolvedDiagrams.isEmpty()) {
			oldResourceSet = oldInvolvedDiagrams.get(0).getObjects().get(0).eResource().getResourceSet();
		}

		if (!newInvolvedDiagrams.isEmpty()) {
			newResourceSet = newInvolvedDiagrams.get(0).getObjects().get(0).eResource().getResourceSet();
		}

		EcoreUtil.resolveAll(oldResourceSet);
		EcoreUtil.resolveAll(newResourceSet);

		EMFCompare comparator = EMFCompare.builder().build();

		DefaultComparisonScope scope = new DefaultComparisonScope(newResourceSet, oldResourceSet, null);
		scope.setResourceSetContentFilter(new Predicate<Resource>() {

			@Override
			public boolean apply(Resource resource) {
				return resource.getURI().fileExtension().equals("notation");
			}
		});
		Comparison comparison = comparator.compare(scope);
		return comparison;
	}

	/**
	 * loads all involved models and diagrams for the given patchSet using the
	 * given {@link Git} instance from the given git {@link Ref}.
	 * 
	 * @param patchSet
	 *            the patch set instance to store the involved models into
	 * @param ref
	 *            the git ref to the commit which contains the patch set.
	 * @param git
	 *            the git instance to use
	 * @return a list containing the resource sets for the old and the new model
	 *         resources.
	 * @throws IOException
	 */
	private List<ResourceSet> loadInvolvedModelsAndDiagrams(PatchSet patchSet, Ref ref, Git git) throws IOException {

		String commitHash = ref.getObjectId().name();

		RevWalk revWalk = new RevWalk(git.getRepository());
		RevCommit newCommit = revWalk.parseCommit(ref.getObjectId());
		RevCommit oldCommit = newCommit.getParent(0);
		revWalk.parseHeaders(oldCommit);
		revWalk.close();

		String parentCommitHash = oldCommit.getId().name();

		URI repoURI = git.getRepository().getDirectory().toURI();
		String authority = repoURI.getAuthority();
		String path = repoURI.getPath();
		String repoPath = (authority != null ? authority + "/" : "") + (path != null ? path : "");
		if (repoPath.endsWith("/")) {
			repoPath = repoPath.substring(0, repoPath.length() - 1);
		}

		ResourceSet newResourceSet = createGitAwareResourceSet(commitHash, repoPath,
				Collections.<ResourceSet> emptyList());
		ResourceSet oldModelResourceSet = createGitAwareResourceSet(parentCommitHash, repoPath,
				Collections.<ResourceSet> emptyList());

		for (Patch patch : patchSet.getPatches()) {
			if (patch instanceof ModelPatch || patch instanceof DiagramPatch) {
				org.eclipse.emf.common.util.URI newUri = org.eclipse.emf.common.util.URI
						.createURI(GitURIParser.GIT_COMMIT_SCHEME + "://" + repoPath + "/" + commitHash + "/"
								+ patch.getNewPath());

				org.eclipse.emf.common.util.URI oldUri = org.eclipse.emf.common.util.URI
						.createURI(GitURIParser.GIT_COMMIT_SCHEME + "://" + repoPath + "/" + parentCommitHash + "/"
								+ patch.getOldPath());

				if (patch.getChangeType() != PatchChangeType.DELETE) {
					// if the patch has been deleted no new resource exists
					Resource newResource = newResourceSet.getResource(newUri, true);
					try {
						applyResourceContent(newResource, patch, false);
					} catch (IOException e) {
						throw new IOException(MessageFormat.format("Could not load resource \"{0}\" for patch set {1}",
								newUri.toString(), patchSet.getId()), e);
					}
				}

				if (patch.getChangeType() != PatchChangeType.ADD) {
					// if the patch has been added no old resource exists
					Resource oldResource = oldModelResourceSet.getResource(oldUri, true);
					try {
						applyResourceContent(oldResource, patch, true);
					} catch (IOException e) {
						throw new IOException(MessageFormat.format("Could not load resource \"{0}\" for patch set {1}",
								oldUri.toString(), patchSet.getId()), e);
					}
				}

			}
		}

		List<ResourceSet> resourceSets = new ArrayList<>(2);
		resourceSets.add(oldModelResourceSet);
		resourceSets.add(newResourceSet);
		return resourceSets;
	}

	/**
	 * applies the content of the given resource to the patch instance and also
	 * update the involved models and diagrams references in the parent patch
	 * set.
	 * 
	 * @param resource
	 *            the resource to read form
	 * @param patch
	 *            the patch to apply the data to
	 * @param old
	 *            an boolean indicating if the resource contains content of the
	 *            old version or the new version
	 * @throws IOException
	 */
	private void applyResourceContent(Resource resource, Patch patch, boolean old) throws IOException {
		PatchSet patchSet = patch.getPatchSet();
		EList<ModelResource> involvedModels = null;
		EList<DiagramResource> involvedDiagrams = null;
		if (old) {
			involvedModels = patchSet.getOldInvolvedModels();
			involvedDiagrams = patchSet.getOldInvolvedDiagrams();
		} else {
			involvedModels = patchSet.getNewInvolvedModels();
			involvedDiagrams = patchSet.getNewInvolvedDiagrams();
		}

		resource.load(null);

		if (patch instanceof ModelPatch) {

			// create a new model resource and add all objects from
			// the resource to the object list
			ModelResource modelResource = modelReviewFactory.createModelResource();
			EList<EObject> containedObjects = modelResource.getObjects();
			containedObjects.addAll(resource.getContents());

			// determine the root package if possible
			List<EPackage> rootPackages = findRootPackages(containedObjects);
			modelResource.getRootPackages().addAll(rootPackages);
			if (old) {
				((ModelPatch) patch).setOldModelResource(modelResource);
			} else {
				((ModelPatch) patch).setNewModelResource(modelResource);
			}

			// also update the involved models
			updateInvolvedModels(involvedModels, containedObjects);

		} else if (patch instanceof DiagramPatch) {

			// create a new diagram resource and add all objects from
			// the resource to the object list
			DiagramResource diagramResource = modelReviewFactory.createDiagramResource();
			EList<EObject> containedObjects = diagramResource.getObjects();
			containedObjects.addAll(resource.getContents());

			// determine the root package if possible
			List<EPackage> rootPackages = findRootPackages(containedObjects);
			diagramResource.getRootPackages().addAll(rootPackages);
			if (old) {
				((DiagramPatch) patch).setOldDiagramResource(diagramResource);
			} else {
				((DiagramPatch) patch).setNewDiagramResource(diagramResource);
			}

			// also update the involved diagrams
			updateInvolvedDiagrams(involvedDiagrams, containedObjects);
		}
	}

	/**
	 * updates the given list of involved models with the given list of objects.
	 * 
	 * @param involvedModels
	 *            the list of involved models that should be updated
	 * @param objects
	 *            the objects used to update the list of involved models
	 */
	private void updateInvolvedModels(List<ModelResource> involvedModels, List<EObject> objects) {
		for (EObject object : objects) {
			EPackage rootPackage = findRootPackage(object);

			ModelResource modelResource = null;

			// search for already existing model resources
			for (ModelResource existingModel : involvedModels) {
				if (existingModel.getRootPackages().contains(rootPackage)) {
					modelResource = existingModel;
					break;
				}
			}

			if (modelResource == null) {
				// there is no existing model resource for the given root
				// package, so we have to create it
				modelResource = modelReviewFactory.createModelResource();
				modelResource.getRootPackages().add(rootPackage);
				involvedModels.add(modelResource);
			}
			modelResource.getObjects().add(object);
		}
	}

	/**
	 * updates the given list of involved diagrams with the given list of
	 * objects.
	 * 
	 * @param involvedDiagrams
	 *            the list of involved diagrams that should be updated
	 * @param objects
	 *            the objects used to update the list of involved diagrams
	 */
	private void updateInvolvedDiagrams(List<DiagramResource> involvedDiagrams, List<EObject> objects) {
		for (EObject object : objects) {
			EPackage rootPackage = findRootPackage(object);

			DiagramResource diagramResource = null;

			// search for already existing diagram resource
			for (DiagramResource existingDiagram : involvedDiagrams) {
				if (existingDiagram.getRootPackages().contains(rootPackage)) {
					diagramResource = existingDiagram;
					break;
				}
			}

			if (diagramResource == null) {
				// there is no existing diagram resource for the given root
				// package, so we have to create it
				diagramResource = modelReviewFactory.createDiagramResource();
				diagramResource.getRootPackages().add(rootPackage);
				involvedDiagrams.add(diagramResource);
			}
			diagramResource.getObjects().add(object);
		}
	}

	/**
	 * lists all root packages of the given list of objects.
	 * 
	 * @param objects
	 * @return a list of all root packages of the given list of objects
	 */
	private static List<EPackage> findRootPackages(EList<EObject> objects) {
		List<EPackage> packages = new LinkedList<EPackage>();
		for (EObject object : objects) {
			EPackage rootPackage = findRootPackage(object);
			if (!packages.contains(rootPackage)) {
				packages.add(rootPackage);
			}
		}
		return packages;
	}

	/**
	 * 
	 * @param object
	 * @return the root {@link EPackage} of the given object
	 */
	private static EPackage findRootPackage(EObject object) {
		EPackage ePackage = object.eClass().getEPackage();
		EPackage rootPackage = null;
		while (ePackage != null) {
			rootPackage = ePackage;
			ePackage = ePackage.getESuperPackage();
		}
		return rootPackage;
	}

	/**
	 * creates an resource set that resolves file
	 * {@link org.eclipse.emf.common.util.URI}s to files in the given git commit
	 * tree.
	 * 
	 * @param commitHash
	 *            the commit hash
	 * @param repoPath
	 *            the absolute path to the git repo
	 * @param fallbackSets
	 *            a list of {@link ResourceSet}s that should be considered when
	 *            searching for a resource with a specific URI
	 * @return the resource set
	 */
	private ResourceSet createGitAwareResourceSet(final String commitHash, final String repoPath,
			final List<ResourceSet> fallbackSets) {

		ResourceSet resourceSet = new ResourceSetImpl() {
			@Override
			protected Resource delegatedGetResource(org.eclipse.emf.common.util.URI uri, boolean loadOnDemand) {

				/*
				 * try to resolve the resource from one of the fallback resource
				 * sets
				 */
				for (ResourceSet resourceSet : fallbackSets) {

					Resource resource = resourceSet.getResource(uri, false);
					if (resource != null) {
						return resource;
					}
				}

				return super.delegatedGetResource(uri, loadOnDemand);
			}
		};

		resourceSet.setURIConverter(new ExtensibleURIConverterImpl() {
			@Override
			public org.eclipse.emf.common.util.URI normalize(org.eclipse.emf.common.util.URI uri) {
				org.eclipse.emf.common.util.URI normalizedURI = super.normalize(uri);
				if (uri.scheme().equals("file")) {
					org.eclipse.emf.common.util.URI oldPrefix = org.eclipse.emf.common.util.URI.createURI("file://");
					org.eclipse.emf.common.util.URI newPrefix = org.eclipse.emf.common.util.URI
							.createURI(GitURIParser.GIT_COMMIT_SCHEME + "://" + repoPath + "/" + commitHash + "/");
					normalizedURI = normalizedURI.replacePrefix(oldPrefix, newPrefix);
				}
				return normalizedURI;
			}
		});
		EList<URIHandler> uriHandlers = resourceSet.getURIConverter().getURIHandlers();
		uriHandlers.add(ContextInjectionFactory.make(ReadOnlyGitCommitURIHandler.class, eclipseContext));
		/*
		 * make sure the Git commit URI handler is placed before the standard
		 * URI Handler which handles by default any URI (otherwise the default
		 * URI handler is visited first and is used to load the resource)
		 */
		uriHandlers.move(uriHandlers.size() - 1, uriHandlers.size() - 2);
		TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain(resourceSet);
		return resourceSet;
	}

	/**
	 * loads all patches of from the given list of {@link DiffEntry}s.
	 * 
	 * @param patchSet
	 *            the patchSet to add the patches to.
	 * @param ref
	 *            the ref to the commit of the patch set.
	 * @param repository
	 *            the git repository instance
	 * @throws RepositoryIOException
	 */
	private void loadPatches(PatchSet patchSet, Ref ref, Git git) throws RepositoryIOException {

		EList<Patch> patches = patchSet.getPatches();
		Repository repository = git.getRepository();

		try {

			RevWalk revWalk = new RevWalk(repository);
			RevCommit newCommit = revWalk.parseCommit(ref.getObjectId());
			RevCommit oldCommit = newCommit.getParent(0);
			revWalk.parseHeaders(oldCommit);
			ObjectReader objectReader = repository.newObjectReader();
			revWalk.close();

			CanonicalTreeParser newTreeIterator = new CanonicalTreeParser();
			newTreeIterator.reset(objectReader, newCommit.getTree().getId());
			CanonicalTreeParser oldTreeIterator = new CanonicalTreeParser();
			oldTreeIterator.reset(objectReader, oldCommit.getTree().getId());

			List<DiffEntry> diffs = git.diff().setOldTree(oldTreeIterator).setNewTree(newTreeIterator).call();

			for (DiffEntry diff : diffs) {

				String newPath = diff.getNewPath();
				String oldPath = diff.getOldPath();
				Patch patch = null;

				/*
				 * only papyrus diagrams are supported for now, so models are in
				 * .uml files, diagrams in .notation files
				 */

				if (diff.getChangeType() != ChangeType.DELETE) {
					if (newPath.endsWith(".uml")) {
						patch = modelReviewFactory.createModelPatch();

					} else if (newPath.endsWith(".notation")) {
						patch = modelReviewFactory.createDiagramPatch();
					} else {
						patch = modelReviewFactory.createPatch();
					}
				} else {
					if (oldPath.endsWith(".uml")) {
						patch = modelReviewFactory.createModelPatch();

					} else if (oldPath.endsWith(".notation")) {
						patch = modelReviewFactory.createDiagramPatch();
					} else {
						patch = modelReviewFactory.createPatch();
					}
				}

				switch (diff.getChangeType()) {
				case ADD:
					patch.setChangeType(PatchChangeType.ADD);
					break;
				case COPY:
					patch.setChangeType(PatchChangeType.COPY);
					break;
				case DELETE:
					patch.setChangeType(PatchChangeType.DELETE);
					break;
				case MODIFY:
					patch.setChangeType(PatchChangeType.MODIFY);
					break;
				case RENAME:
					patch.setChangeType(PatchChangeType.RENAME);
					break;
				}

				patch.setNewPath(newPath);
				patch.setOldPath(oldPath);

				if (diff.getChangeType() != ChangeType.DELETE) {
					ObjectLoader objectLoader = repository.open(diff.getNewId().toObjectId());
					patch.setNewContent(objectLoader.getBytes());
				}
				if (diff.getChangeType() != ChangeType.ADD) {
					ObjectLoader objectLoader = repository.open(diff.getOldId().toObjectId());
					patch.setOldContent(objectLoader.getBytes());
				}
				patches.add(patch);

			}

		} catch (IOException e) {
			throw new RepositoryIOException(MessageFormat
					.format("An IO error occured during loading the patches for patch set #{0}", patchSet.getId()), e);
		} catch (GitAPIException e) {
			throw new RepositoryIOException(MessageFormat.format(
					"An JGit API error occured during loading the patches for patch set #{0}", patchSet.getId()), e);
		}
	}

	/**
	 * loads the comments for the given {@link ModelReview} from the given
	 * repository. The current reviewer should be set for the given model to
	 * avoid duplicated user objects representing the same user. The resource
	 * set used to load the comments tries to load resources containing
	 * referenced EObjects from the given list of model resource sets before
	 * creating the resource directly, in order to avoid duplicated model
	 * elements. This is necessary as {@link CommentLink}s may refer to objects
	 * contained in other resource sets.
	 * 
	 * @param repository
	 *            the repository to load the comments from.
	 * @param modelReview
	 *            the {@link ModelReview} to load the comments for.
	 * @param modelResourceSets
	 *            a list of resource sets to resolve referenced model elements
	 *            if they cannot be found in the comments model.
	 * @throws RepositoryIOException
	 */
	private void loadComments(Repository repository, final ModelReview modelReview, List<ResourceSet> modelResourceSets)
			throws RepositoryIOException {

		String commentRefName = getCommentRefName(modelReview);
		String repoPath = repository.getWorkTree().getPath();
		Ref commentRef;

		try {

			commentRef = repository.exactRef(commentRefName);
			if (commentRef == null) {
				// No comment ref -> no comments exist yet
				return;
			}
			String commitHash = commentRef.getObjectId().name();

			/* prepare the resource set and the resource for the comments */
			org.eclipse.emf.common.util.URI commentsUri = org.eclipse.emf.common.util.URI.createURI(
					GitURIParser.GIT_COMMIT_SCHEME + "://" + repoPath + "/" + commitHash + "/" + COMMENTS_FILE_URI);
			ResourceSet resourceSet = createGitAwareResourceSet(commitHash, repoPath, modelResourceSets);
			Resource commentsResource = resourceSet.createResource(commentsUri);

			/* load the comments from the repository */
			commentsResource.load(null);
			final EList<EObject> content = commentsResource.getContents();

			/* add the loaded comments to the given model review */

			TransactionalEditingDomain editingDomain = findTransactionalEditingDomainFor(commentsResource);

			Command addCommentsCommand = new RecordingCommand(editingDomain) {

				@Override
				protected void doExecute() {

					User reviewer = modelReview.getCurrentReviewer();

					for (EObject object : content) {

						if (object instanceof Comment) {

							Comment comment = (Comment) object;

							/* create a copy to avoid transaction problems */
							EcoreUtil.resolveAll(comment);
							comment = EcoreUtil.copy(comment);

							comment.resolvePatchSet(modelReview);
							User author = comment.getAuthor();

							if (isSameUser(reviewer, author)) {
								// avoid duplicated users
								comment.setAuthor(reviewer);
							}

							modelReview.getComments().add(comment);
						}
					}
				}

			};

			editingDomain.getCommandStack().execute(addCommentsCommand);

		} catch (IOException e) {
			throw new RepositoryIOException(
					MessageFormat.format("An IO error occured during loading the comments for the review with id #{0}",
							modelReview.getId()),
					e);
		}
	}

	/**
	 * finds the {@link TransactionalEditingDomain} for the given
	 * {@link Resource} if there is one.
	 * 
	 * @param resource
	 *            the resource to find the {@link TransactionalEditingDomain}
	 *            for.
	 * @return the {@link TransactionalEditingDomain} for the given resource or
	 *         null if no {@link TransactionalEditingDomain} can be found.
	 */
	private static TransactionalEditingDomain findTransactionalEditingDomainFor(Resource resource) {

		if (resource != null) {

			IEditingDomainProvider editingDomainProvider = (IEditingDomainProvider) EcoreUtil
					.getExistingAdapter(resource, IEditingDomainProvider.class);
			if (editingDomainProvider != null) {

				EditingDomain editingDomain = editingDomainProvider.getEditingDomain();
				if (editingDomain instanceof TransactionalEditingDomain) {
					return (TransactionalEditingDomain) editingDomain;
				}

			} else {
				return findTransactionalEditingDomainFor(resource.getResourceSet());
			}
		}

		return null;
	}

	/**
	 * finds the {@link TransactionalEditingDomain} for the given
	 * {@link ResourceSet} if there is one.
	 * 
	 * @param resourceSet
	 *            the resource set to find the
	 *            {@link TransactionalEditingDomain} for.
	 * @return the {@link TransactionalEditingDomain} for the given resource set
	 *         or null if no {@link TransactionalEditingDomain} can be found.
	 */
	private static TransactionalEditingDomain findTransactionalEditingDomainFor(ResourceSet resourceSet) {

		IEditingDomainProvider editingDomainProvider;
		if (resourceSet instanceof IEditingDomainProvider) {

			EditingDomain editingDomain = ((IEditingDomainProvider) resourceSet).getEditingDomain();
			if (editingDomain instanceof TransactionalEditingDomain) {
				return (TransactionalEditingDomain) editingDomain;
			}

		} else if (resourceSet != null) {

			editingDomainProvider = (IEditingDomainProvider) EcoreUtil.getExistingAdapter(resourceSet,
					IEditingDomainProvider.class);

			if (editingDomainProvider != null) {
				EditingDomain editingDomain = editingDomainProvider.getEditingDomain();
				if (editingDomain instanceof TransactionalEditingDomain) {
					return (TransactionalEditingDomain) editingDomain;
				}
			}
		}

		return null;
	}

	/**
	 * @param user1
	 * @param user2
	 * @return true if the given objects represent the same users, false
	 *         otherwise.
	 */
	private boolean isSameUser(User user1, User user2) {
		return user1 != null && user2 != null && user1.getName().equals(user2.getName());
	}

	@Override
	public void saveReview(URI uri, ModelReview modelReview, User currentReviewer, IProgressMonitor monitor)
			throws InvalidReviewRepositoryException, InvalidReviewException, RepositoryIOException {

		monitor.beginTask("Connecting to repository", IProgressMonitor.UNKNOWN);

		String repoFileURI = COMMENTS_FILE_URI;

		try {
			Git git = Git.open(new File(uri));
			Repository repository = git.getRepository();
			ObjectInserter objectInserter = repository.newObjectInserter();

			String commentRefName = getCommentRefName(modelReview);
			Ref commentRef = repository.exactRef(commentRefName);

			DirCache index = DirCache.newInCore();
			DirCacheBuilder dirCacheBuilder = index.builder();

			monitor.beginTask("Preparing commit...", IProgressMonitor.UNKNOWN);

			if (commentRef != null) {

				/*
				 * The ref already exists so we have to copy the previous
				 * RevTree to keep all already attached files
				 */

				RevWalk revWalk = new RevWalk(repository);
				RevCommit prevCommit = revWalk.parseCommit(commentRef.getObjectId());
				RevTree tree = prevCommit.getTree();

				List<String> ignoredFiles = new ArrayList<>();
				/*
				 * add file path of the new file to the ignored file paths, as
				 * we don't want any already existing old file in our new tree
				 */
				ignoredFiles.add(repoFileURI);
				buildDirCacheFromTree(tree, repository, dirCacheBuilder, ignoredFiles);

				revWalk.close();
			}

			monitor.beginTask("Writing comments file...", IProgressMonitor.UNKNOWN);

			ResourceSet resourceSet = new ResourceSetImpl();
			Resource resource = resourceSet.createResource(org.eclipse.emf.common.util.URI.createURI(repoFileURI));

			addCommentsToResource(modelReview, resource);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			resource.save(outputStream, null);

			// insert file as object
			byte[] content = outputStream.toByteArray();
			long length = content.length;
			InputStream inputStream = new ByteArrayInputStream(content);
			ObjectId objectId = objectInserter.insert(Constants.OBJ_BLOB, length, inputStream);
			inputStream.close();

			// create tree entry
			DirCacheEntry entry = new DirCacheEntry(repoFileURI);
			entry.setFileMode(FileMode.REGULAR_FILE);
			entry.setLastModified(System.currentTimeMillis());
			entry.setLength(length);
			entry.setObjectId(objectId);
			dirCacheBuilder.add(entry);

			dirCacheBuilder.finish();

			// write new tree in database
			ObjectId indexTreeId = index.writeTree(objectInserter);

			monitor.beginTask("Commiting comments...", IProgressMonitor.UNKNOWN);

			// create commit
			CommitBuilder commitBuilder = new CommitBuilder();
			PersonIdent personIdent = new PersonIdent("Mervin", "mervin@mervin.modelreview");
			commitBuilder.setCommitter(personIdent);
			commitBuilder.setAuthor(personIdent);
			commitBuilder
					.setMessage(MessageFormat.format("Updated comments by user \"{0}\"", currentReviewer.getName()));

			if (commentRef != null) {
				commitBuilder.setParentId(commentRef.getObjectId());
			}
			commitBuilder.setTreeId(indexTreeId);

			// commit
			ObjectId commitId = objectInserter.insert(commitBuilder);
			objectInserter.flush();

			RefUpdate refUpdate = repository.updateRef(commentRefName);
			refUpdate.setNewObjectId(commitId);
			if (commentRef != null)
				refUpdate.setExpectedOldObjectId(commentRef.getObjectId());
			else
				refUpdate.setExpectedOldObjectId(ObjectId.zeroId());

			/*
			 * TODO the result handling below is copied from the CommitCommand
			 * class, I don't know if this is really necessary in our case
			 */
			Result result = refUpdate.forceUpdate();
			switch (result) {
			case NEW:
			case FORCED:
			case FAST_FORWARD: {
				if (repository.getRepositoryState() == RepositoryState.MERGING_RESOLVED) {
					/*
					 * Commit was successful. Now delete the files used for
					 * merge commits
					 */
					repository.writeMergeCommitMsg(null);
					repository.writeMergeHeads(null);
				} else if (repository.getRepositoryState() == RepositoryState.CHERRY_PICKING_RESOLVED) {
					repository.writeMergeCommitMsg(null);
					repository.writeCherryPickHead(null);
				} else if (repository.getRepositoryState() == RepositoryState.REVERTING_RESOLVED) {
					repository.writeMergeCommitMsg(null);
					repository.writeRevertHead(null);
				}
				break;
			}
			case REJECTED:
			case LOCK_FAILURE:
				throw new RepositoryIOException("Error occured during writing to the git repository",
						new ConcurrentRefUpdateException("Could not lock ref " + refUpdate.getRef().getName(),
								refUpdate.getRef(), result));
			default:
				throw new RepositoryIOException("Error occured during writing to the git repository",
						new JGitInternalException(MessageFormat.format(JGitText.get().updatingRefFailed,
								refUpdate.getRef().getName(), commitId.toString(), result)));
			}

		} catch (IOException e) {
			throw new InvalidReviewRepositoryException("Could not open local git repository", e);
		} finally {
			monitor.done();
		}

	}

	/**
	 * adds a copy of the comments and their respective authors of the given
	 * model review to the given resource.
	 * 
	 * @param modelReview
	 *            the review to copy the comments from.
	 * @param resource
	 *            the resource to store the copied objects to.
	 */
	private void addCommentsToResource(ModelReview modelReview, Resource resource) {

		EList<Comment> originalComments = modelReview.getComments();
		Set<EObject> objectsToCopy = new HashSet<>();
		objectsToCopy.addAll(originalComments);
		objectsToCopy.addAll(modelReview.getPatchSets());

		Copier copier = new Copier();
		copier.copyAll(objectsToCopy);
		copier.copyReferences();

		List<Comment> comments = new ArrayList<>(originalComments.size());
		List<User> users = new LinkedList<>();
		for (Comment originalComment : originalComments) {

			Comment comment = (Comment) copier.get(originalComment);
			comments.add(comment);
			User author = comment.getAuthor();
			boolean skipAuthor = false;

			/*
			 * replace the patchset with a reference to the patch set id, as the
			 * patch set is derived from the repository in this case.
			 */

			PatchSet patchset = comment.getPatchset();
			comment.setPatchSetRefId(patchset.getId());
			comment.setPatchset(null);

			for (User user : users) {
				if (isSameUser(user, author)) {
					skipAuthor = true;
					break;
				}
			}

			if (!skipAuthor) {
				users.add(author);
			}
		}

		resource.getContents().addAll(comments);
		resource.getContents().addAll(users);
	}

	/**
	 * builds a dir cache by copying entries from an existing tree, skipping
	 * files if specified.
	 *
	 * @param tree
	 *            the tree to copy from.
	 * @param repository
	 *            the repository to work upon.
	 * @param dirCacheBuilder
	 *            the dir builder instance used to add entries to the dir cache.
	 * @param ignoredFilePaths
	 *            a list of file paths to ignore during copying.
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws CorruptObjectException
	 * @throws IOException
	 */
	private void buildDirCacheFromTree(RevTree tree, Repository repository, DirCacheBuilder dirCacheBuilder,
			List<String> ignoredFilePaths)
			throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		// TODO improve exception handling

		TreeWalk treeWalk = new TreeWalk(repository);
		int treeId = treeWalk.addTree(tree);
		treeWalk.setRecursive(true);

		while (treeWalk.next()) {
			String path = treeWalk.getPathString();

			CanonicalTreeParser prevTreeParser = treeWalk.getTree(treeId, CanonicalTreeParser.class);
			if (prevTreeParser != null && !ignoredFilePaths.contains(path)) {
				// create a new DirCacheEntry with data from the previous commit

				final DirCacheEntry dcEntry = new DirCacheEntry(path);
				dcEntry.setObjectId(prevTreeParser.getEntryObjectId());
				dcEntry.setFileMode(prevTreeParser.getEntryFileMode());
				dirCacheBuilder.add(dcEntry);
			}
		}
		treeWalk.close();
	}

	/**
	 * @param review
	 *            the review to retrieve the ref name for.
	 * @return the comment ref name for the given {@link ModelReview}.
	 */
	private String getCommentRefName(ModelReview review) {
		return getCommentRefName(review.getId());
	}

	/**
	 * @param changeId
	 *            the id of the gerrit change (primary key of the gerrit
	 *            change).
	 * @return the comment ref name for the given changeId.
	 */
	private String getCommentRefName(String changeId) {
		return COMMENTS_BASE_REF + "/" + changeId;
	}

}

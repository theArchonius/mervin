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
package at.bitandart.zoubek.mervin.gerrit;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import at.bitandart.zoubek.mervin.IReviewDescriptor;
import at.bitandart.zoubek.mervin.IReviewRepositoryService;
import at.bitandart.zoubek.mervin.exceptions.InvalidReviewException;
import at.bitandart.zoubek.mervin.exceptions.InvalidReviewRepositoryException;
import at.bitandart.zoubek.mervin.exceptions.RepositoryIOException;
import at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch;
import at.bitandart.zoubek.mervin.model.modelreview.ModelPatch;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.Patch;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

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

	/**
	 * the model review factory used by this service to create all model
	 * elements
	 */
	@Inject
	private ModelReviewFactory modelReviewFactory;

	@SuppressWarnings("restriction")
	@Inject
	private org.eclipse.e4.core.services.log.Logger logger;

	@SuppressWarnings("restriction")
	@Override
	public List<IReviewDescriptor> getReviews(URI uri)
			throws InvalidReviewRepositoryException {

		List<IReviewDescriptor> changeIds = new LinkedList<>();

		try {
			// connect to the local git repository
			Git git = Git.open(new File(uri));

			try {
				// Assume that origin refers to the remote gerrit repository
				// list all remote refs from origin
				Collection<Ref> remoteRefs = git.lsRemote().setTimeout(60)
						.call();

				Pattern changeRefPattern = Pattern.compile(CHANGE_REF_PATTERN);

				// search for change refs
				for (Ref ref : remoteRefs) {

					Matcher matcher = changeRefPattern.matcher(ref.getName());
					if (matcher.matches()) {
						String changePk = matcher.group(CHANGE_REF_PATTERN_GROUP_CHANGE_PK);
						String changeId = "<unknown>";
						GerritReviewDescriptor reviewDescriptor;
						try {
							reviewDescriptor = new GerritReviewDescriptor(
									Integer.parseInt(changePk), changeId);
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
							FetchResult fetchResult = git.fetch()
									.setRefSpecs(new RefSpec(ref.getName()))
									.call();

							Ref localRef = fetchResult.getAdvertisedRef(ref
									.getName());
							RevWalk revWalk = new RevWalk(git.getRepository());
							RevCommit commit = revWalk.parseCommit(localRef
									.getObjectId());
							String[] paragraphs = commit.getFullMessage()
									.split("\n");
							String lastParagraph = paragraphs[paragraphs.length - 1];
							Pattern pattern = Pattern
									.compile(".*Change-Id: (I[^ \n]*).*");
							Matcher changeIdMatcher = pattern
									.matcher(lastParagraph);

							if (changeIdMatcher.matches()) {
								changeId = changeIdMatcher.group(1);
								reviewDescriptor.setChangeId(changeId);
								;
							} else {
								logger.warn(MessageFormat
										.format("Could not find the change id for Gerrit change with primary key {0}",
												changePk));
							}
							revWalk.close();
						}
					}
				}

			} catch (GitAPIException e) {
				throw new RepositoryIOException(
						"Error during loading all remote changes", e);
			}

		} catch (IOException e) {
			throw new InvalidReviewRepositoryException(
					"Could not open local git repository", e);
		}
		return changeIds;
	}

	@Override
	public ModelReview loadReview(URI uri, String id)
			throws InvalidReviewRepositoryException, InvalidReviewException,
			RepositoryIOException {
		/*
		 * Fetch all refs to the patch sets for the particular change and create
		 * the model instance from it
		 */

		try {
			Git git = Git.open(new File(uri));
			int iId = Integer.parseInt(id);

			// First of all: fetch the patch sets
			// git fetch origin
			// +refs/changes/id%100/<cid>/*:refs/changes/id%100/<cid>/*
			// Refspec of a patchset:
			// +refs/changes/id%100/<cid>/<psId>:refs/changes/id%100/<cid>/<psId>

			git.fetch()
					.setRefSpecs(
							new RefSpec(
									MessageFormat
											.format("+refs/changes/{0,number,00}/{1}/*:refs/changes/{0,number,00}/{1}/*",
													iId % 100, iId))).call();

			// create model instance

			ModelReview modelReview = modelReviewFactory.createModelReview();
			modelReview.setId(id);
			EList<PatchSet> patchSets = modelReview.getPatchSets();

			Repository repository = git.getRepository();
			Map<String, Ref> allRefs = repository.getAllRefs();
			Pattern changeRefPattern = Pattern.compile(CHANGE_REF_PATTERN);

			for (Entry<String, Ref> refEntry : allRefs.entrySet()) {
				Matcher matcher = changeRefPattern.matcher(refEntry.getValue()
						.getName());
				if (matcher.matches() && matcher.group(CHANGE_REF_PATTERN_GROUP_CHANGE_PK).equals(id)) {
					PatchSet patchSet = modelReviewFactory.createPatchSet();
					patchSets.add(patchSet);
					patchSet.setId(matcher.group(CHANGE_REF_PATTERN_GROUP_PATCH_SET_ID));

					// load patched files
					loadPatches(patchSet, refEntry.getValue(), git);

					// load involved models
					loadInvolvedModels(patchSet, refEntry.getValue(), git);

					// TODO load involved diagrams

				}
			}
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

			return modelReview;

		} catch (IOException e) {
			throw new InvalidReviewRepositoryException(
					"Could not open local git repository", e);
		} catch (NumberFormatException e) {
			throw new InvalidReviewException(MessageFormat.format(
					"Invalid review id: {0}", id));
		} catch (GitAPIException e) {
			throw new RepositoryIOException(
					"Error occured during reading from the git repository", e);
		}
	}

	/**
	 * loads all involved models for the given patchSet using the given
	 * {@link Git} from the given git {@link Ref}.
	 * 
	 * @param patchSet
	 *            the patch set instance to store the involved models into
	 * @param ref
	 *            the git ref to the commit which contains the patch set.
	 * @param git
	 *            the git instance to use
	 * @throws IOException
	 */
	private void loadInvolvedModels(PatchSet patchSet, Ref ref, Git git)
			throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		final String commitHash = ref.getObjectId().name();
		URI repoURI = git.getRepository().getDirectory().toURI();
		String authority = repoURI.getAuthority();
		String path = repoURI.getPath();
		final String repoPath = (authority != null ? authority : "") + "/"
				+ (path != null ? path : "");
		resourceSet.setURIConverter(new ExtensibleURIConverterImpl() {
			@Override
			public org.eclipse.emf.common.util.URI normalize(
					org.eclipse.emf.common.util.URI uri) {
				org.eclipse.emf.common.util.URI normalizedURI = super
						.normalize(uri);
				if (uri.scheme().equals("file")) {
					org.eclipse.emf.common.util.URI oldPrefix = org.eclipse.emf.common.util.URI
							.createURI("file://");
					org.eclipse.emf.common.util.URI newPrefix = org.eclipse.emf.common.util.URI
							.createURI(GitURIParser.GIT_COMMIT_SCHEME + "://"
									+ repoPath + "/" + commitHash + "/");
					normalizedURI = normalizedURI.replacePrefix(oldPrefix,
							newPrefix);
				}
				return normalizedURI;
			}
		});

		resourceSet.getURIConverter().getURIHandlers()
				.add(new ReadOnlyGitCommitURIHandler());

		for (Patch patch : patchSet.getPatches()) {
			if (patch instanceof ModelPatch || patch instanceof DiagramPatch) {
				org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI
						.createURI(GitURIParser.GIT_COMMIT_SCHEME + "://"
								+ repoPath + "/" + commitHash + "/"
								+ patch.getPath());
				Resource resource = resourceSet.createResource(uri);
				try {
					resource.load(null);
					EcoreUtil.resolveAll(resource);
					if (patch instanceof ModelPatch) {

						// TODO set content

					} else if (patch instanceof DiagramPatch) {

						// TODO set content

					}
				} catch (IOException e) {
					throw new IOException(
							MessageFormat.format(
									"Could not load resource \"{0}\" for patch set {1}",
									uri.toString(), patchSet.getId()), e);
				}
			}
		}

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
	private void loadPatches(PatchSet patchSet, Ref ref, Git git)
			throws RepositoryIOException {

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

			List<DiffEntry> diffs = git.diff().setOldTree(oldTreeIterator)
					.setNewTree(newTreeIterator).call();

			for (DiffEntry diff : diffs) {

				String path = diff.getNewPath();
				Patch patch = null;

				/*
				 * only papyrus diagrams are supported for now, so models are in
				 * .uml files, diagrams in .notation files
				 */
				if (path.endsWith(".uml")) {
					patch = modelReviewFactory.createModelPatch();

				} else if (path.endsWith(".notation")) {
					patch = modelReviewFactory.createDiagramPatch();
				} else {
					patch = modelReviewFactory.createPatch();
				}

				patch.setPath(path);
				if (diff.getChangeType() != ChangeType.DELETE) {
					ObjectLoader objectLoader = repository.open(diff.getNewId()
							.toObjectId());
					patch.setContent(objectLoader.getBytes());
				}
				patches.add(patch);

			}

		} catch (IOException e) {
			throw new RepositoryIOException(
					MessageFormat.format(
							"An IO error occured during loading the patches for patch set #{0}",
							patchSet.getId()), e);
		} catch (GitAPIException e) {
			throw new RepositoryIOException(
					MessageFormat.format(
							"An JGit API error occured during loading the patches for patch set #{0}",
							patchSet.getId()), e);
		}
	}

	@Override
	public void saveReview(URI uri, ModelReview modelReview)
			throws InvalidReviewException, RepositoryIOException {
		// TODO Auto-generated method stub

	}

}

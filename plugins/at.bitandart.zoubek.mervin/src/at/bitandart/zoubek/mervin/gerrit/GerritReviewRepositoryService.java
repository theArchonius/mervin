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
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;

import at.bitandart.zoubek.mervin.IReviewDescriptor;
import at.bitandart.zoubek.mervin.IReviewRepositoryService;
import at.bitandart.zoubek.mervin.exceptions.InvalidReviewException;
import at.bitandart.zoubek.mervin.exceptions.InvalidReviewRepositoryException;
import at.bitandart.zoubek.mervin.exceptions.RepositoryIOException;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
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
						String changePk = matcher.group("changePk");
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
				if (matcher.matches()) {
					PatchSet patchSet = modelReviewFactory.createPatchSet();
					patchSet.setId(matcher.group("patchSetId"));

					// TODO load patches and involved diagrams
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
						return iPsId2.compareTo(iPsId2);
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

	@Override
	public void saveReview(URI uri, ModelReview modelReview)
			throws InvalidReviewException, RepositoryIOException {
		// TODO Auto-generated method stub

	}

}

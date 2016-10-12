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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

/**
 * An URI parser that is able to parse and load the object referenced by a git
 * commit URI. This git commit URI must have the following form:
 * <code>git-commit://&lt;repositoryPath&gt;/&lt;fullCommitHash&gt;/&lt;filepath&gt;/&lt;filename&gt;</code>
 * Please note that this URI does not allow referencing directories in a commit.
 * 
 * @author Florian Zoubek
 *
 */
public class GitURIParser {

	private org.eclipse.emf.common.util.URI uri;

	private String authority = "";

	/**
	 * the file path to the git repo, with leading slash.
	 */
	private String repoPath = "";

	/**
	 * the repository path used if no repository path is given in the URI
	 */
	private String defaultRepoPath = "";

	/**
	 * the file path to the file in the git commit, <b>without</b> a leading
	 * slash.
	 */
	private String filePath = "";
	private String commitHash = "";

	private boolean uriParsed = false;

	private RevCommit commit;
	private Repository repository;
	private ObjectLoader objectLoader;
	private ObjectId objectId;

	@SuppressWarnings("restriction")
	@Inject
	private org.eclipse.e4.core.services.log.Logger logger;

	/**
	 * the scheme for a commit URI
	 */
	public static final String GIT_COMMIT_SCHEME = "git-file";

	public GitURIParser(org.eclipse.emf.common.util.URI uri, String defaultRepoPath) {
		super();
		this.uri = uri;
		this.defaultRepoPath = defaultRepoPath;
	}

	/**
	 * parses the URI
	 * 
	 * @throws IOException
	 *             if an error occurs during parsing the URI
	 */
	@SuppressWarnings("restriction")
	private void parse() throws IOException {
		StringBuilder repoPathBuilder = new StringBuilder();
		StringBuilder filePathBuilder = new StringBuilder();
		if (uri.hasAuthority() && !uri.authority().isEmpty()) {
			if (repoPathBuilder.length() == 0) {
				repoPathBuilder.append("/");
			}
			authority = uri.authority();
		}
		if (uri.hasDevice()) {
			if (repoPathBuilder.length() == 0) {
				repoPathBuilder.append("/");
			}
			repoPathBuilder.append(uri.device());
			repoPathBuilder.append("/");
		}
		List<String> segments = uri.segmentsList();
		Iterator<String> segmentIt = segments.iterator();
		String currentSegment = "";

		// extract repo path
		while (segmentIt.hasNext() && !ObjectId.isId(currentSegment)) {
			repoPathBuilder.append(currentSegment);
			if (!currentSegment.isEmpty()) {
				if (repoPathBuilder.length() == 0) {
					repoPathBuilder.append("/");
				}
				repoPathBuilder.append("/");
			}
			currentSegment = segmentIt.next();
		}

		// extract the commit Hash
		commitHash = currentSegment;
		currentSegment = segmentIt.next();
		if (!ObjectId.isId(commitHash)) {
			commitHash = "";
			throw new IOException("Invalid URI: no commit hash found");
		}

		// extract file path within commit
		while (segmentIt.hasNext()) {
			filePathBuilder.append(currentSegment);
			filePathBuilder.append("/");
			currentSegment = segmentIt.next();
		}
		filePathBuilder.append(currentSegment);

		logger.debug("commit hash: " + commitHash);

		repoPath = repoPathBuilder.toString();
		logger.debug("repo path: " + repoPath);

		filePath = filePathBuilder.toString();
		logger.debug("file path: " + filePath);

		uriParsed = true;
	}

	/**
	 * loads the referenced {@link Repository}
	 * 
	 * @throws IOException
	 *             if an error occurs during parsing the URI
	 */
	@SuppressWarnings("restriction")
	private void loadRepository() throws IOException {

		if (!uriParsed)
			parse();

		URI repoURI;
		String repoPath = this.repoPath;
		if (repoPath == null || repoPath.isEmpty()) {
			repoPath = defaultRepoPath;
		}
		try {
			repoURI = new URI("file", authority, repoPath, null, null);
			logger.debug("repo URI: " + repoURI.toString());
		} catch (URISyntaxException e) {
			throw new IOException("Could not determine repository URI");
		}
		File repoDir = new File(repoURI);
		if (!repoDir.isDirectory()) {
			throw new IOException("Invalid git repository: " + repoDir.getAbsolutePath());
		}
		Git git = Git.open(repoDir);
		repository = git.getRepository();
	}

	/**
	 * loads the referenced commit
	 * 
	 * @throws IOException
	 *             if an error occurs during parsing the URI
	 */
	private void loadCommit() throws IOException {
		if (repository == null)
			loadRepository();
		RevWalk revWalk = new RevWalk(repository);
		commit = revWalk.parseCommit(ObjectId.fromString(commitHash));
		revWalk.close();
	}

	/**
	 * loads the referenced {@link ObjectId} and the referenced
	 * {@link ObjectLoader}
	 * 
	 * @throws IOException
	 *             if an error occurs during parsing the URI
	 */
	private void loadObject() throws IOException {
		if (commit == null)
			loadCommit();
		TreeWalk treeWalk = TreeWalk.forPath(repository, filePath, commit.getTree());
		if (treeWalk == null) {
			throw new IOException("Could not find a file at \"" + filePath + "\" in commit " + commitHash);
		}

		// finally we got the object in question
		objectId = treeWalk.getObjectId(0);

		objectLoader = repository.open(objectId);
	}

	/**
	 * @return the URI that is parsed
	 */
	public org.eclipse.emf.common.util.URI getUri() {
		return uri;
	}

	/**
	 * @return the authority that is used to obtain the repository
	 * @throws IOException
	 *             if an error occurs during parsing the URI
	 */
	public String getAuthority() throws IOException {
		if (!uriParsed)
			parse();
		return authority;
	}

	/**
	 * @return the path of the repository
	 * @throws IOException
	 *             if an error occurs during parsing the URI
	 */
	public String getRepoPath() throws IOException {
		if (!uriParsed)
			parse();
		return repoPath;
	}

	/**
	 * @return the file path within the commit
	 * @throws IOException
	 *             if an error occurs during parsing the URI
	 */
	public String getFilePath() throws IOException {
		if (!uriParsed)
			parse();
		return filePath;
	}

	/**
	 * @return the referenced commit hash
	 * @throws IOException
	 *             if an error occurs during parsing the URI
	 */
	public String getCommitHash() throws IOException {
		if (!uriParsed)
			parse();
		return commitHash;
	}

	/**
	 * @return the referenced commit
	 * @throws IOException
	 *             if an error occurs during parsing the URI
	 */
	public RevCommit getCommit() throws IOException {
		if (commit == null)
			loadCommit();
		return commit;
	}

	/**
	 * @return the referenced repository
	 * @throws IOException
	 *             if an error occurs during parsing the URI
	 */
	public Repository getRepository() throws IOException {
		if (repository == null)
			loadRepository();
		return repository;
	}

	/**
	 * @return the {@link ObjectLoader} of the referenced object
	 * @throws IOException
	 *             if an error occurs during parsing the URI
	 */
	public ObjectLoader getObjectLoader() throws IOException {
		if (objectLoader == null)
			loadObject();
		return objectLoader;
	}

	/**
	 * @return the object id of the referenced object
	 * @throws IOException
	 *             if an error occurs during parsing the URI
	 */
	public ObjectId getObjectId() throws IOException {
		if (objectId == null)
			loadObject();
		return objectId;
	}

}
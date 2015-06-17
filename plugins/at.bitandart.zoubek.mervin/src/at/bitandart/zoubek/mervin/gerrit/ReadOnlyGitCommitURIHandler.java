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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;

/**
 * An {@link URIHandler} that is able to access and query information about a
 * file in a git commit but not to write to such an file.
 * 
 * @author Florian Zoubek
 *
 */
public class ReadOnlyGitCommitURIHandler extends URIHandlerImpl {

	@Inject
	private IEclipseContext eclipseContext;
	
	public ReadOnlyGitCommitURIHandler() {
	}

	@Override
	public boolean canHandle(org.eclipse.emf.common.util.URI uri) {
		return uri.scheme().equals(GitURIParser.GIT_COMMIT_SCHEME);
	}

	@Override
	public InputStream createInputStream(org.eclipse.emf.common.util.URI uri,
			Map<?, ?> options) throws IOException {
		GitURIParser gitURIParser = new GitURIParser(uri);
		ContextInjectionFactory.inject(gitURIParser, eclipseContext);

		return gitURIParser.getObjectLoader().openStream();
	}

	/**
	 * throws an {@link IOException} as this URIHandler is read only
	 */
	@Override
	public OutputStream createOutputStream(org.eclipse.emf.common.util.URI uri,
			Map<?, ?> options) throws IOException {
		throw new IOException(
				"Cannot write to an existing git object, git objects are read-only");
	}

	/**
	 * throws an {@link IOException} as this URIHandler is read only
	 */
	@Override
	public void delete(org.eclipse.emf.common.util.URI uri, Map<?, ?> options)
			throws IOException {
		throw new IOException(
				"Cannot delete an existing git object, git objects are read-only");
	}

	@Override
	public boolean exists(org.eclipse.emf.common.util.URI uri, Map<?, ?> options) {

		GitURIParser gitURIParser = new GitURIParser(uri);
		try {
			return gitURIParser.getObjectId() != null;
		} catch (IOException e) {
			// intentionally left empty
		}
		return false;
	}

	@Override
	public Map<String, ?> getAttributes(org.eclipse.emf.common.util.URI uri,
			Map<?, ?> options) {

		Map<String, Object> result = new HashMap<String, Object>();
		GitURIParser gitURIParser = new GitURIParser(uri);
		try {
			Set<String> requestedAttributes = getRequestedAttributes(options);
			if (requestedAttributes == null
					|| requestedAttributes
							.contains(URIConverter.ATTRIBUTE_TIME_STAMP)) {
				result.put(URIConverter.ATTRIBUTE_TIME_STAMP, gitURIParser
						.getCommit().getCommitTime());
			}
			if (requestedAttributes == null
					|| requestedAttributes
							.contains(URIConverter.ATTRIBUTE_LENGTH)) {
				result.put(URIConverter.ATTRIBUTE_LENGTH, gitURIParser
						.getObjectLoader().getSize());
			}
			if (requestedAttributes == null
					|| requestedAttributes
							.contains(URIConverter.ATTRIBUTE_READ_ONLY)) {
				result.put(URIConverter.ATTRIBUTE_READ_ONLY, true);
			}
			if (requestedAttributes == null
					|| requestedAttributes
							.contains(URIConverter.ATTRIBUTE_HIDDEN)) {
				// not supported by git
				result.put(URIConverter.ATTRIBUTE_HIDDEN, false);
			}
			if (requestedAttributes == null
					|| requestedAttributes
							.contains(URIConverter.ATTRIBUTE_DIRECTORY)) {
				// we don't support directories
				result.put(URIConverter.ATTRIBUTE_DIRECTORY, false);
			}
		} catch (IOException e) {
			// Nothing to do here
		}

		return result;
	}

	/**
	 * does nothing, as this URIHandler is read only.
	 */
	@Override
	public void setAttributes(org.eclipse.emf.common.util.URI uri,
			Map<String, ?> attributes, Map<?, ?> options) throws IOException {
		// Nothing to do here as the git object is read only
	}

}
/*******************************************************************************
 * Copyright (c) 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.model.modelreview.impl.extended;

import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl;

/**
 * An {@link Comment} implementation that provides the default implementation
 * for
 * {@link Comment#resolvePatchSet(at.bitandart.zoubek.mervin.model.modelreview.ModelReview)}
 * .
 * 
 * @author Florian Zoubek
 *
 */
public class ExtendedCommentImpl extends CommentImpl {

	@Override
	public void resolvePatchSet(ModelReview review) {

		String refId = getPatchSetRefId();
		if (refId != null) {
			for (PatchSet patchSet : review.getPatchSets()) {
				if (patchSet.getId().equals(refId)) {
					setPatchset(patchSet);
					break;
				}
			}
		}
	}

}

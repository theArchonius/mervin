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
package at.bitandart.zoubek.mervin;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;

import at.bitandart.zoubek.mervin.comments.ICommonTargetResolver;
import at.bitandart.zoubek.mervin.comments.MervinCommentProvider;
import at.bitandart.zoubek.mervin.comments.MervinCommonTargetResolver;
import at.bitandart.zoubek.mervin.gerrit.GerritReviewRepositoryService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.User;
import at.bitandart.zoubek.mervin.model.modelreview.impl.extended.DefaultModelReviewFactory;
import at.bitandart.zoubek.mervin.patchset.history.ChangeSimilarityHistoryService;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentProvider;

/**
 * This addon is responsible for registering all mervin related services to the
 * context.
 */
public class MervinServicesAddon {

	@PostConstruct
	public void addServicesToContext(IEclipseContext context) {

		// add the default factory for the review model to the context
		DefaultModelReviewFactory defaultModelReviewFactory = new DefaultModelReviewFactory();
		context.set(ModelReviewFactory.class, defaultModelReviewFactory);

		// add diff service based on EMF Compare
		IDiffService diffService = ContextInjectionFactory.make(EMFCompareDiffService.class, context);
		context.set(IDiffService.class, diffService);

		/*
		 * the prototype supports currently only Gerrit, so use the Gerrit
		 * service by default
		 */
		GerritReviewRepositoryService gerritReviewRepositoryService = ContextInjectionFactory
				.make(GerritReviewRepositoryService.class, context);
		context.set(IReviewRepositoryService.class, gerritReviewRepositoryService);

		ChangeSimilarityHistoryService changeSimilarityHistoryService = ContextInjectionFactory
				.make(ChangeSimilarityHistoryService.class, context);
		context.set(ISimilarityHistoryService.class, changeSimilarityHistoryService);

		// add default user to the context
		User defaultUser = defaultModelReviewFactory.createUser();
		defaultUser.setName("Mervin Default User");
		context.set(User.class, defaultUser);

		ICommonTargetResolver commonTargetResolver = ContextInjectionFactory.make(MervinCommonTargetResolver.class,
				context);
		context.set(ICommonTargetResolver.class, commonTargetResolver);

		MervinCommentProvider mervinCommentProvider = ContextInjectionFactory.make(MervinCommentProvider.class,
				context);
		context.set(ICommentProvider.class, mervinCommentProvider);

	}

}

/*******************************************************************************
 * Copyright (c) 2015, 2016, 2017 Florian Zoubek.
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
import at.bitandart.zoubek.mervin.comments.MervinCommentLinkListener;
import at.bitandart.zoubek.mervin.comments.MervinCommentProvider;
import at.bitandart.zoubek.mervin.comments.MervinCommonTargetResolver;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.DefaultOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.gerrit.GerritReviewRepositoryService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.User;
import at.bitandart.zoubek.mervin.model.modelreview.impl.extended.DefaultModelReviewFactory;
import at.bitandart.zoubek.mervin.patchset.history.ChangeSimilarityHistoryService;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService;
import at.bitandart.zoubek.mervin.patchset.history.organizers.DependencyDiffOrganizer;
import at.bitandart.zoubek.mervin.patchset.history.organizers.IPatchSetHistoryEntryOrganizer;
import at.bitandart.zoubek.mervin.swt.comments.CommentList.CommentLinkListener;
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

		// add default user to the context
		User defaultUser = defaultModelReviewFactory.createUser();
		defaultUser.setName("Mervin Default User");
		context.set(User.class, defaultUser);
		context.set(IMervinContextConstants.CURRENT_REVIEWER, defaultUser);

		// add default style advisor for overlay types
		DefaultOverlayTypeStyleAdvisor styleAdvisor = ContextInjectionFactory.make(DefaultOverlayTypeStyleAdvisor.class,
				context);
		context.set(IOverlayTypeStyleAdvisor.class, styleAdvisor);

		// add highlight service
		MervinReviewHighlightService highlightService = ContextInjectionFactory.make(MervinReviewHighlightService.class,
				context);
		context.set(IReviewHighlightService.class, highlightService);

		// add diff service based on EMF Compare
		IDiffService diffService = ContextInjectionFactory.make(EMFCompareDiffService.class, context);
		context.set(IDiffService.class, diffService);

		/* add the default mervin model review helper */
		IModelReviewHelper modelReviewHelper = ContextInjectionFactory.make(MervinModelReviewHelper.class, context);
		context.set(IModelReviewHelper.class, modelReviewHelper);

		/* add the default mervin diagram model helper */
		IDiagramModelHelper semanticModelHelper = ContextInjectionFactory.make(MervinDiagramModelHelper.class, context);
		context.set(IDiagramModelHelper.class, semanticModelHelper);

		/* add the default mervin match helper */
		IMatchHelper matchHelper = ContextInjectionFactory.make(MervinMatchHelper.class, context);
		context.set(IMatchHelper.class, matchHelper);

		/* add the default mervin overlay type helper */
		IOverlayTypeHelper overlayHelper = ContextInjectionFactory.make(MervinOverlayTypeHelper.class, context);
		context.set(IOverlayTypeHelper.class, overlayHelper);

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

		// add default patch set history entry organizer
		IPatchSetHistoryEntryOrganizer entryOrganizer = ContextInjectionFactory.make(DependencyDiffOrganizer.class,
				context);
		context.set(IPatchSetHistoryEntryOrganizer.class, entryOrganizer);

		ICommonTargetResolver commonTargetResolver = ContextInjectionFactory.make(MervinCommonTargetResolver.class,
				context);
		context.set(ICommonTargetResolver.class, commonTargetResolver);

		MervinCommentProvider mervinCommentProvider = ContextInjectionFactory.make(MervinCommentProvider.class,
				context);
		context.set(ICommentProvider.class, mervinCommentProvider);

		MervinCommentLinkListener mervinCommentLinkListener = ContextInjectionFactory
				.make(MervinCommentLinkListener.class, context);
		context.set(CommentLinkListener.class, mervinCommentLinkListener);

		context.declareModifiable(IMervinContextConstants.ACTIVE_MODEL_REVIEW);
		context.declareModifiable(IMervinContextConstants.CURRENT_REVIEWER);
		context.declareModifiable(IMervinContextConstants.ACTIVE_DIAGRAM_DIFF_EDITOR);
		context.declareModifiable(IMervinContextConstants.HIGHLIGHTED_ELEMENTS);
		context.declareModifiable(IMervinContextConstants.ACTIVE_TRANSACTIONAL_EDITING_DOMAIN);
		context.declareModifiable(IMervinContextConstants.ACTIVE_EDIT_DOMAIN);

	}

}

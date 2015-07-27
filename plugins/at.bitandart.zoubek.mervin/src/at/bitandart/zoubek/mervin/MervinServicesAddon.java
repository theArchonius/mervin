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
package at.bitandart.zoubek.mervin;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;

import at.bitandart.zoubek.mervin.gerrit.GerritReviewRepositoryService;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.impl.extended.DefaultModelReviewFactory;
import at.bitandart.zoubek.mervin.patchset.history.ChangeSimilarityHistoryService;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService;

/**
 * This addon is responsible for registering all mervin related services to the
 * context.
 */
public class MervinServicesAddon {
	
	@PostConstruct
	public void addServicesToContext(IEclipseContext context){

		// add the default factory for the review model to the context
		context.set(ModelReviewFactory.class, new DefaultModelReviewFactory());
		
		// the prototype supports currently only Gerrit, so use the Gerrit
		// service by default
		GerritReviewRepositoryService gerritReviewRepositoryService = ContextInjectionFactory
				.make(GerritReviewRepositoryService.class, context);
		context.set(IReviewRepositoryService.class,
				gerritReviewRepositoryService);

		ChangeSimilarityHistoryService changeSimilarityHistoryService = ContextInjectionFactory
				.make(ChangeSimilarityHistoryService.class, context);
		context.set(ISimilarityHistoryService.class,
				changeSimilarityHistoryService);
	}
	

}

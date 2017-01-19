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
package at.bitandart.zoubek.mervin.papyrus.css;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.infra.gmfdiag.css.engine.ICSSElementProviderFactory;
import org.eclipse.papyrus.infra.gmfdiag.css.notation.CSSDiagram;
import org.eclipse.papyrus.infra.gmfdiag.css.provider.IPapyrusElementProvider;
import org.eclipse.papyrus.uml.diagram.css.dom.GMFUMLElementProvider;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * An {@link ICSSElementProviderFactory} that provides CSS support for UML
 * elements within Mervin diagrams.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinElementProviderFactory implements ICSSElementProviderFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.papyrus.infra.gmfdiag.css.engine.ICSSElementProviderFactory#
	 * isProviderFor(org.eclipse.papyrus.infra.gmfdiag.css.notation.CSSDiagram)
	 */
	@Override
	public boolean isProviderFor(CSSDiagram diagram) {
		EObject element = diagram.getElement();
		return element instanceof ModelReview;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.papyrus.infra.gmfdiag.css.engine.ICSSElementProviderFactory#
	 * createProvider(org.eclipse.papyrus.infra.gmfdiag.css.notation.CSSDiagram)
	 */
	@Override
	public IPapyrusElementProvider createProvider(CSSDiagram diagram) {

		return new GMFUMLElementProvider();
	}

}

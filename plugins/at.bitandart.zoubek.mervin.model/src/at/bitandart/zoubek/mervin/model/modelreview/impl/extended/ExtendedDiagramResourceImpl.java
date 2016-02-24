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
package at.bitandart.zoubek.mervin.model.modelreview.impl.extended;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramResourceImpl;

/**
 * An {@link ModelReview} implementation that supports the derived attributes of
 * a {@link ModelReview} based on the other attributes.
 * 
 * @author Florian Zoubek
 *
 */
public class ExtendedDiagramResourceImpl extends DiagramResourceImpl {
	@Override
	public EList<Diagram> getDiagrams() {

		EList<Diagram> diagrams = new BasicEList<Diagram>();

		for (EObject object : getObjects()) {

			if (object instanceof Diagram && !diagrams.contains(object)) {
				diagrams.add((Diagram) object);
			}

			// search for nested diagrams
			TreeIterator<EObject> eAllContentsIt = object.eAllContents();
			while (eAllContentsIt.hasNext()) {

				EObject childObject = eAllContentsIt.next();
				if (childObject instanceof Diagram && !diagrams.contains(childObject)) {
					diagrams.add((Diagram) childObject);
				}
			}
		}
		return diagrams;
	}
}

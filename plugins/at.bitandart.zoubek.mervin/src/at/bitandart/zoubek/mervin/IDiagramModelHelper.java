/*******************************************************************************
 * Copyright (c) 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin;

import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Base interface for helper classes providing shared operations for GMF
 * Diagrams using a specific strategy.
 * 
 * @author Florian Zoubek
 *
 */
public interface IDiagramModelHelper {

	/**
	 * @param object
	 *            the object to find the semantic model for.
	 * @return the semantic model of or related to the given object or null if
	 *         no semantic model can be found.
	 */
	public EObject getSemanticModel(Object object);

	/**
	 * @param object
	 *            the object to find the notation model for.
	 * @return the notation model of or related to the given object or null if
	 *         no notation model can be found.
	 */
	public View getNotationModel(Object object);

	/**
	 * @param object
	 *            the object to find the referencing view for.
	 * @return a set of all views referencing the given {@link EObject}, never
	 *         null.
	 */
	public Set<View> getReferencingViews(EObject object);

}

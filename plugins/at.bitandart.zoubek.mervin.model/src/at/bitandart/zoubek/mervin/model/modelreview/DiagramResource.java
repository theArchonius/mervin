/**
 * ******************************************************************************
 *  Copyright (c) 2015, 2016, 2017 Florian Zoubek.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *     Florian Zoubek - initial API and implementation
 * ******************************************************************************
 */
package at.bitandart.zoubek.mervin.model.modelreview;

import org.eclipse.emf.common.util.EList;

import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Diagram Resource</b></em>'. <!-- end-user-doc -->
 *
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDiagramResource()
 * @model
 * @generated
 */
public interface DiagramResource extends ModelResource {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation"
	 * @generated
	 */
	EList<Diagram> getDiagrams();

} // DiagramResource

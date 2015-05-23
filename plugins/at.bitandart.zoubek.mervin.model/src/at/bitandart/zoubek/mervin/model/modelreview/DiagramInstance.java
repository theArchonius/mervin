/**
 * ******************************************************************************
 *  Copyright (c) 2015 Florian Zoubek.
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

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Diagram Instance</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance#getNotationModel
 * <em>Notation Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDiagramInstance()
 * @model
 * @generated
 */
public interface DiagramInstance extends EObject {
	/**
	 * Returns the value of the '<em><b>Notation Model</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notation Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Notation Model</em>' reference.
	 * @see #setNotationModel(Diagram)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDiagramInstance_NotationModel()
	 * @model
	 * @generated
	 */
	Diagram getNotationModel();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance#getNotationModel
	 * <em>Notation Model</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Notation Model</em>' reference.
	 * @see #getNotationModel()
	 * @generated
	 */
	void setNotationModel(Diagram value);

} // DiagramInstance

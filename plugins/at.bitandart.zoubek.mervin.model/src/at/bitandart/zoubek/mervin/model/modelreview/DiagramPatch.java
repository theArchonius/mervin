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

import org.eclipse.emf.compare.Comparison;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Diagram Patch</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getComparison
 * <em>Comparison</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getDiagramInstance
 * <em>Diagram Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDiagramPatch()
 * @model
 * @generated
 */
public interface DiagramPatch extends Patch {

	/**
	 * Returns the value of the '<em><b>Comparison</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comparison</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Comparison</em>' reference.
	 * @see #setComparison(Comparison)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDiagramPatch_Comparison()
	 * @model
	 * @generated
	 */
	Comparison getComparison();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getComparison
	 * <em>Comparison</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Comparison</em>' reference.
	 * @see #getComparison()
	 * @generated
	 */
	void setComparison(Comparison value);

	/**
	 * Returns the value of the '<em><b>Diagram Instance</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagram Instance</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Diagram Instance</em>' reference.
	 * @see #setDiagramInstance(DiagramInstance)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDiagramPatch_DiagramInstance()
	 * @model
	 * @generated
	 */
	DiagramInstance getDiagramInstance();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getDiagramInstance
	 * <em>Diagram Instance</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Diagram Instance</em>' reference.
	 * @see #getDiagramInstance()
	 * @generated
	 */
	void setDiagramInstance(DiagramInstance value);
} // DiagramPatch

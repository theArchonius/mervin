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

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Diagram Patch</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getNewDiagramInstance
 * <em>New Diagram Instance</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getOldDiagramInstance
 * <em>Old Diagram Instance</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDiagramPatch()
 * @model
 * @generated
 */
public interface DiagramPatch extends Patch {

	/**
	 * Returns the value of the '<em><b>New Diagram Instance</b></em>'
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Diagram Instance</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>New Diagram Instance</em>' reference.
	 * @see #setNewDiagramInstance(DiagramInstance)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDiagramPatch_NewDiagramInstance()
	 * @model
	 * @generated
	 */
	DiagramInstance getNewDiagramInstance();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getNewDiagramInstance
	 * <em>New Diagram Instance</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>New Diagram Instance</em>'
	 *            reference.
	 * @see #getNewDiagramInstance()
	 * @generated
	 */
	void setNewDiagramInstance(DiagramInstance value);

	/**
	 * Returns the value of the '<em><b>Old Diagram Instance</b></em>'
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Diagram Instance</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Old Diagram Instance</em>' reference.
	 * @see #setOldDiagramInstance(DiagramInstance)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDiagramPatch_OldDiagramInstance()
	 * @model
	 * @generated
	 */
	DiagramInstance getOldDiagramInstance();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getOldDiagramInstance
	 * <em>Old Diagram Instance</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Old Diagram Instance</em>'
	 *            reference.
	 * @see #getOldDiagramInstance()
	 * @generated
	 */
	void setOldDiagramInstance(DiagramInstance value);
} // DiagramPatch

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

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Diagram Patch</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getNewDiagramResource
 * <em>New Diagram Resource</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getOldDiagramResource
 * <em>Old Diagram Resource</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDiagramPatch()
 * @model
 * @generated
 */
public interface DiagramPatch extends Patch {
	/**
	 * Returns the value of the '<em><b>New Diagram Resource</b></em>'
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Diagram Resource</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>New Diagram Resource</em>' reference.
	 * @see #setNewDiagramResource(DiagramResource)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDiagramPatch_NewDiagramResource()
	 * @model
	 * @generated
	 */
	DiagramResource getNewDiagramResource();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getNewDiagramResource
	 * <em>New Diagram Resource</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>New Diagram Resource</em>'
	 *            reference.
	 * @see #getNewDiagramResource()
	 * @generated
	 */
	void setNewDiagramResource(DiagramResource value);

	/**
	 * Returns the value of the '<em><b>Old Diagram Resource</b></em>'
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Diagram Resource</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Old Diagram Resource</em>' reference.
	 * @see #setOldDiagramResource(DiagramResource)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDiagramPatch_OldDiagramResource()
	 * @model
	 * @generated
	 */
	DiagramResource getOldDiagramResource();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getOldDiagramResource
	 * <em>Old Diagram Resource</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Old Diagram Resource</em>'
	 *            reference.
	 * @see #getOldDiagramResource()
	 * @generated
	 */
	void setOldDiagramResource(DiagramResource value);

} // DiagramPatch

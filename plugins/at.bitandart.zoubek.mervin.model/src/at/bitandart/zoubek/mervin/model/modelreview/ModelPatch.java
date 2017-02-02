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
 * <em><b>Model Patch</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getNewModelResource
 * <em>New Model Resource</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getOldModelResource
 * <em>Old Model Resource</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelPatch()
 * @model
 * @generated
 */
public interface ModelPatch extends Patch {
	/**
	 * Returns the value of the '<em><b>New Model Resource</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Model Resource</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>New Model Resource</em>' reference.
	 * @see #setNewModelResource(ModelResource)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelPatch_NewModelResource()
	 * @model
	 * @generated
	 */
	ModelResource getNewModelResource();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getNewModelResource
	 * <em>New Model Resource</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>New Model Resource</em>' reference.
	 * @see #getNewModelResource()
	 * @generated
	 */
	void setNewModelResource(ModelResource value);

	/**
	 * Returns the value of the '<em><b>Old Model Resource</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Model Resource</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Old Model Resource</em>' reference.
	 * @see #setOldModelResource(ModelResource)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelPatch_OldModelResource()
	 * @model
	 * @generated
	 */
	ModelResource getOldModelResource();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getOldModelResource
	 * <em>Old Model Resource</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Old Model Resource</em>' reference.
	 * @see #getOldModelResource()
	 * @generated
	 */
	void setOldModelResource(ModelResource value);

} // ModelPatch

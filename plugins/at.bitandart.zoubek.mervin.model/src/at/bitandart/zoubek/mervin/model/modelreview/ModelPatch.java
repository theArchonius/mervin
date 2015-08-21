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
 * <em><b>Model Patch</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getNewModelInstance
 * <em>New Model Instance</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getOldModelInstance
 * <em>Old Model Instance</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelPatch()
 * @model
 * @generated
 */
public interface ModelPatch extends Patch {

	/**
	 * Returns the value of the '<em><b>New Model Instance</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Model Instance</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>New Model Instance</em>' reference.
	 * @see #setNewModelInstance(ModelInstance)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelPatch_NewModelInstance()
	 * @model
	 * @generated
	 */
	ModelInstance getNewModelInstance();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getNewModelInstance
	 * <em>New Model Instance</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>New Model Instance</em>' reference.
	 * @see #getNewModelInstance()
	 * @generated
	 */
	void setNewModelInstance(ModelInstance value);

	/**
	 * Returns the value of the '<em><b>Old Model Instance</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Model Instance</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Old Model Instance</em>' reference.
	 * @see #setOldModelInstance(ModelInstance)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelPatch_OldModelInstance()
	 * @model
	 * @generated
	 */
	ModelInstance getOldModelInstance();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getOldModelInstance
	 * <em>Old Model Instance</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Old Model Instance</em>' reference.
	 * @see #getOldModelInstance()
	 * @generated
	 */
	void setOldModelInstance(ModelInstance value);
} // ModelPatch

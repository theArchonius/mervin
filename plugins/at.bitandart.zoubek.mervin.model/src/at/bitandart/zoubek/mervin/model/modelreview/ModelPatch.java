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
 * <em><b>Model Patch</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getComparison
 * <em>Comparison</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getModelInstance
 * <em>Model Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelPatch()
 * @model
 * @generated
 */
public interface ModelPatch extends Patch {

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
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelPatch_Comparison()
	 * @model
	 * @generated
	 */
	Comparison getComparison();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getComparison
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
	 * Returns the value of the '<em><b>Model Instance</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Instance</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Model Instance</em>' reference.
	 * @see #setModelInstance(ModelInstance)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelPatch_ModelInstance()
	 * @model
	 * @generated
	 */
	ModelInstance getModelInstance();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getModelInstance
	 * <em>Model Instance</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Model Instance</em>' reference.
	 * @see #getModelInstance()
	 * @generated
	 */
	void setModelInstance(ModelInstance value);
} // ModelPatch

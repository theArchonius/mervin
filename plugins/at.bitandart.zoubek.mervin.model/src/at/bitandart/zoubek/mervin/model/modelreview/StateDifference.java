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
 * <em><b>State Difference</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.StateDifference#getType
 * <em>Type</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getStateDifference()
 * @model
 * @generated
 */
public interface StateDifference extends ModelDifference {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute. The literals
	 * are from the enumeration
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.StateDifferenceType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.StateDifferenceType
	 * @see #setType(StateDifferenceType)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getStateDifference_Type()
	 * @model
	 * @generated
	 */
	StateDifferenceType getType();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.StateDifference#getType
	 * <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Type</em>' attribute.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.StateDifferenceType
	 * @see #getType()
	 * @generated
	 */
	void setType(StateDifferenceType value);

} // StateDifference

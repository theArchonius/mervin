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

import org.eclipse.draw2d.geometry.Vector;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Location Difference</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.LocationDifference#getMoveDirection
 * <em>Move Direction</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.LocationDifference#getOriginalLocation
 * <em>Original Location</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getLocationDifference()
 * @model
 * @generated
 */
public interface LocationDifference extends LayoutDifference {
	/**
	 * Returns the value of the '<em><b>Move Direction</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Move Direction</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Move Direction</em>' attribute.
	 * @see #setMoveDirection(Vector)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getLocationDifference_MoveDirection()
	 * @model dataType="at.bitandart.zoubek.mervin.model.modelreview.Vector"
	 * @generated
	 */
	Vector getMoveDirection();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.LocationDifference#getMoveDirection
	 * <em>Move Direction</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Move Direction</em>' attribute.
	 * @see #getMoveDirection()
	 * @generated
	 */
	void setMoveDirection(Vector value);

	/**
	 * Returns the value of the '<em><b>Original Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Original Location</em>' attribute.
	 * @see #setOriginalLocation(Vector)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getLocationDifference_OriginalLocation()
	 * @model dataType="at.bitandart.zoubek.mervin.model.modelreview.Vector"
	 * @generated
	 */
	Vector getOriginalLocation();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.LocationDifference#getOriginalLocation
	 * <em>Original Location</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Original Location</em>' attribute.
	 * @see #getOriginalLocation()
	 * @generated
	 */
	void setOriginalLocation(Vector value);

} // LocationDifference

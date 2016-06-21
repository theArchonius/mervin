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

import org.eclipse.draw2d.geometry.Dimension;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Size Difference</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.SizeDifference#getWidthChange
 * <em>Width Change</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.SizeDifference#getHeightChange
 * <em>Height Change</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.SizeDifference#getOriginalDimension
 * <em>Original Dimension</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getSizeDifference()
 * @model
 * @generated
 */
public interface SizeDifference extends LayoutDifference {
	/**
	 * Returns the value of the '<em><b>Width Change</b></em>' attribute. The
	 * literals are from the enumeration
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DimensionChange}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Width Change</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Width Change</em>' attribute.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DimensionChange
	 * @see #setWidthChange(DimensionChange)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getSizeDifference_WidthChange()
	 * @model
	 * @generated
	 */
	DimensionChange getWidthChange();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.SizeDifference#getWidthChange
	 * <em>Width Change</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Width Change</em>' attribute.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DimensionChange
	 * @see #getWidthChange()
	 * @generated
	 */
	void setWidthChange(DimensionChange value);

	/**
	 * Returns the value of the '<em><b>Height Change</b></em>' attribute. The
	 * literals are from the enumeration
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DimensionChange}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Height Change</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Height Change</em>' attribute.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DimensionChange
	 * @see #setHeightChange(DimensionChange)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getSizeDifference_HeightChange()
	 * @model
	 * @generated
	 */
	DimensionChange getHeightChange();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.SizeDifference#getHeightChange
	 * <em>Height Change</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Height Change</em>' attribute.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DimensionChange
	 * @see #getHeightChange()
	 * @generated
	 */
	void setHeightChange(DimensionChange value);

	/**
	 * Returns the value of the '<em><b>Original Dimension</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Dimension</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Original Dimension</em>' attribute.
	 * @see #setOriginalDimension(Dimension)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getSizeDifference_OriginalDimension()
	 * @model dataType="at.bitandart.zoubek.mervin.model.modelreview.Dimension"
	 * @generated
	 */
	Dimension getOriginalDimension();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.SizeDifference#getOriginalDimension
	 * <em>Original Dimension</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Original Dimension</em>' attribute.
	 * @see #getOriginalDimension()
	 * @generated
	 */
	void setOriginalDimension(Dimension value);

} // SizeDifference

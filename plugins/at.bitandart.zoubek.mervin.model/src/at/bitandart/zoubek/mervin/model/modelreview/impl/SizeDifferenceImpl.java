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
package at.bitandart.zoubek.mervin.model.modelreview.impl;

import at.bitandart.zoubek.mervin.model.modelreview.DimensionChange;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.SizeDifference;

import java.util.Collection;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.Diff;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Size Difference</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.SizeDifferenceImpl#getRawDiffs
 * <em>Raw Diffs</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.SizeDifferenceImpl#getWidthChange
 * <em>Width Change</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.SizeDifferenceImpl#getHeightChange
 * <em>Height Change</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.SizeDifferenceImpl#getOriginalDimension
 * <em>Original Dimension</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SizeDifferenceImpl extends MinimalEObjectImpl.Container implements SizeDifference {
	/**
	 * The cached value of the '{@link #getRawDiffs() <em>Raw Diffs</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRawDiffs()
	 * @generated
	 * @ordered
	 */
	protected EList<Diff> rawDiffs;

	/**
	 * The default value of the '{@link #getWidthChange() <em>Width Change</em>}
	 * ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getWidthChange()
	 * @generated
	 * @ordered
	 */
	protected static final DimensionChange WIDTH_CHANGE_EDEFAULT = DimensionChange.SMALLER;

	/**
	 * The cached value of the '{@link #getWidthChange() <em>Width Change</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getWidthChange()
	 * @generated
	 * @ordered
	 */
	protected DimensionChange widthChange = WIDTH_CHANGE_EDEFAULT;

	/**
	 * The default value of the '{@link #getHeightChange()
	 * <em>Height Change</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getHeightChange()
	 * @generated
	 * @ordered
	 */
	protected static final DimensionChange HEIGHT_CHANGE_EDEFAULT = DimensionChange.SMALLER;

	/**
	 * The cached value of the '{@link #getHeightChange() <em>Height Change</em>
	 * }' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHeightChange()
	 * @generated
	 * @ordered
	 */
	protected DimensionChange heightChange = HEIGHT_CHANGE_EDEFAULT;

	/**
	 * The default value of the '{@link #getOriginalDimension()
	 * <em>Original Dimension</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getOriginalDimension()
	 * @generated
	 * @ordered
	 */
	protected static final Dimension ORIGINAL_DIMENSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOriginalDimension()
	 * <em>Original Dimension</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getOriginalDimension()
	 * @generated
	 * @ordered
	 */
	protected Dimension originalDimension = ORIGINAL_DIMENSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected SizeDifferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.SIZE_DIFFERENCE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Diff> getRawDiffs() {
		if (rawDiffs == null) {
			rawDiffs = new EObjectResolvingEList<Diff>(Diff.class, this, ModelReviewPackage.SIZE_DIFFERENCE__RAW_DIFFS);
		}
		return rawDiffs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DimensionChange getWidthChange() {
		return widthChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setWidthChange(DimensionChange newWidthChange) {
		DimensionChange oldWidthChange = widthChange;
		widthChange = newWidthChange == null ? WIDTH_CHANGE_EDEFAULT : newWidthChange;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.SIZE_DIFFERENCE__WIDTH_CHANGE,
					oldWidthChange, widthChange));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DimensionChange getHeightChange() {
		return heightChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setHeightChange(DimensionChange newHeightChange) {
		DimensionChange oldHeightChange = heightChange;
		heightChange = newHeightChange == null ? HEIGHT_CHANGE_EDEFAULT : newHeightChange;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.SIZE_DIFFERENCE__HEIGHT_CHANGE,
					oldHeightChange, heightChange));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Dimension getOriginalDimension() {
		return originalDimension;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOriginalDimension(Dimension newOriginalDimension) {
		Dimension oldOriginalDimension = originalDimension;
		originalDimension = newOriginalDimension;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.SIZE_DIFFERENCE__ORIGINAL_DIMENSION, oldOriginalDimension, originalDimension));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.SIZE_DIFFERENCE__RAW_DIFFS:
			return getRawDiffs();
		case ModelReviewPackage.SIZE_DIFFERENCE__WIDTH_CHANGE:
			return getWidthChange();
		case ModelReviewPackage.SIZE_DIFFERENCE__HEIGHT_CHANGE:
			return getHeightChange();
		case ModelReviewPackage.SIZE_DIFFERENCE__ORIGINAL_DIMENSION:
			return getOriginalDimension();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ModelReviewPackage.SIZE_DIFFERENCE__RAW_DIFFS:
			getRawDiffs().clear();
			getRawDiffs().addAll((Collection<? extends Diff>) newValue);
			return;
		case ModelReviewPackage.SIZE_DIFFERENCE__WIDTH_CHANGE:
			setWidthChange((DimensionChange) newValue);
			return;
		case ModelReviewPackage.SIZE_DIFFERENCE__HEIGHT_CHANGE:
			setHeightChange((DimensionChange) newValue);
			return;
		case ModelReviewPackage.SIZE_DIFFERENCE__ORIGINAL_DIMENSION:
			setOriginalDimension((Dimension) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case ModelReviewPackage.SIZE_DIFFERENCE__RAW_DIFFS:
			getRawDiffs().clear();
			return;
		case ModelReviewPackage.SIZE_DIFFERENCE__WIDTH_CHANGE:
			setWidthChange(WIDTH_CHANGE_EDEFAULT);
			return;
		case ModelReviewPackage.SIZE_DIFFERENCE__HEIGHT_CHANGE:
			setHeightChange(HEIGHT_CHANGE_EDEFAULT);
			return;
		case ModelReviewPackage.SIZE_DIFFERENCE__ORIGINAL_DIMENSION:
			setOriginalDimension(ORIGINAL_DIMENSION_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ModelReviewPackage.SIZE_DIFFERENCE__RAW_DIFFS:
			return rawDiffs != null && !rawDiffs.isEmpty();
		case ModelReviewPackage.SIZE_DIFFERENCE__WIDTH_CHANGE:
			return widthChange != WIDTH_CHANGE_EDEFAULT;
		case ModelReviewPackage.SIZE_DIFFERENCE__HEIGHT_CHANGE:
			return heightChange != HEIGHT_CHANGE_EDEFAULT;
		case ModelReviewPackage.SIZE_DIFFERENCE__ORIGINAL_DIMENSION:
			return ORIGINAL_DIMENSION_EDEFAULT == null ? originalDimension != null
					: !ORIGINAL_DIMENSION_EDEFAULT.equals(originalDimension);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (widthChange: ");
		result.append(widthChange);
		result.append(", heightChange: ");
		result.append(heightChange);
		result.append(", originalDimension: ");
		result.append(originalDimension);
		result.append(')');
		return result.toString();
	}

} // SizeDifferenceImpl

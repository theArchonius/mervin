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

import at.bitandart.zoubek.mervin.model.modelreview.LocationDifference;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;

import java.util.Collection;

import org.eclipse.draw2d.geometry.Vector;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.Diff;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Location Difference</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.LocationDifferenceImpl#getRawDiffs
 * <em>Raw Diffs</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.LocationDifferenceImpl#getMoveDirection
 * <em>Move Direction</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.LocationDifferenceImpl#getOriginalLocation
 * <em>Original Location</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LocationDifferenceImpl extends MinimalEObjectImpl.Container implements LocationDifference {
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
	 * The default value of the '{@link #getMoveDirection()
	 * <em>Move Direction</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getMoveDirection()
	 * @generated
	 * @ordered
	 */
	protected static final Vector MOVE_DIRECTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMoveDirection()
	 * <em>Move Direction</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getMoveDirection()
	 * @generated
	 * @ordered
	 */
	protected Vector moveDirection = MOVE_DIRECTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getOriginalLocation()
	 * <em>Original Location</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getOriginalLocation()
	 * @generated
	 * @ordered
	 */
	protected static final Vector ORIGINAL_LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOriginalLocation()
	 * <em>Original Location</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getOriginalLocation()
	 * @generated
	 * @ordered
	 */
	protected Vector originalLocation = ORIGINAL_LOCATION_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected LocationDifferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.LOCATION_DIFFERENCE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Diff> getRawDiffs() {
		if (rawDiffs == null) {
			rawDiffs = new EObjectResolvingEList<Diff>(Diff.class, this,
					ModelReviewPackage.LOCATION_DIFFERENCE__RAW_DIFFS);
		}
		return rawDiffs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Vector getMoveDirection() {
		return moveDirection;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setMoveDirection(Vector newMoveDirection) {
		Vector oldMoveDirection = moveDirection;
		moveDirection = newMoveDirection;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.LOCATION_DIFFERENCE__MOVE_DIRECTION, oldMoveDirection, moveDirection));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Vector getOriginalLocation() {
		return originalLocation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOriginalLocation(Vector newOriginalLocation) {
		Vector oldOriginalLocation = originalLocation;
		originalLocation = newOriginalLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.LOCATION_DIFFERENCE__ORIGINAL_LOCATION, oldOriginalLocation, originalLocation));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.LOCATION_DIFFERENCE__RAW_DIFFS:
			return getRawDiffs();
		case ModelReviewPackage.LOCATION_DIFFERENCE__MOVE_DIRECTION:
			return getMoveDirection();
		case ModelReviewPackage.LOCATION_DIFFERENCE__ORIGINAL_LOCATION:
			return getOriginalLocation();
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
		case ModelReviewPackage.LOCATION_DIFFERENCE__RAW_DIFFS:
			getRawDiffs().clear();
			getRawDiffs().addAll((Collection<? extends Diff>) newValue);
			return;
		case ModelReviewPackage.LOCATION_DIFFERENCE__MOVE_DIRECTION:
			setMoveDirection((Vector) newValue);
			return;
		case ModelReviewPackage.LOCATION_DIFFERENCE__ORIGINAL_LOCATION:
			setOriginalLocation((Vector) newValue);
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
		case ModelReviewPackage.LOCATION_DIFFERENCE__RAW_DIFFS:
			getRawDiffs().clear();
			return;
		case ModelReviewPackage.LOCATION_DIFFERENCE__MOVE_DIRECTION:
			setMoveDirection(MOVE_DIRECTION_EDEFAULT);
			return;
		case ModelReviewPackage.LOCATION_DIFFERENCE__ORIGINAL_LOCATION:
			setOriginalLocation(ORIGINAL_LOCATION_EDEFAULT);
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
		case ModelReviewPackage.LOCATION_DIFFERENCE__RAW_DIFFS:
			return rawDiffs != null && !rawDiffs.isEmpty();
		case ModelReviewPackage.LOCATION_DIFFERENCE__MOVE_DIRECTION:
			return MOVE_DIRECTION_EDEFAULT == null ? moveDirection != null
					: !MOVE_DIRECTION_EDEFAULT.equals(moveDirection);
		case ModelReviewPackage.LOCATION_DIFFERENCE__ORIGINAL_LOCATION:
			return ORIGINAL_LOCATION_EDEFAULT == null ? originalLocation != null
					: !ORIGINAL_LOCATION_EDEFAULT.equals(originalLocation);
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
		result.append(" (moveDirection: ");
		result.append(moveDirection);
		result.append(", originalLocation: ");
		result.append(originalLocation);
		result.append(')');
		return result.toString();
	}

} // LocationDifferenceImpl

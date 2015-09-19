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

import at.bitandart.zoubek.mervin.model.modelreview.ChangeOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.compare.Diff;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Change Overlay</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ChangeOverlayImpl#getDiff
 * <em>Diff</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeOverlayImpl extends MinimalEObjectImpl.Container implements ChangeOverlay {
	/**
	 * The cached value of the '{@link #getDiff() <em>Diff</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDiff()
	 * @generated
	 * @ordered
	 */
	protected Diff diff;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ChangeOverlayImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.CHANGE_OVERLAY;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Diff getDiff() {
		if (diff != null && diff.eIsProxy()) {
			InternalEObject oldDiff = (InternalEObject) diff;
			diff = (Diff) eResolveProxy(oldDiff);
			if (diff != oldDiff) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelReviewPackage.CHANGE_OVERLAY__DIFF,
							oldDiff, diff));
			}
		}
		return diff;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Diff basicGetDiff() {
		return diff;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDiff(Diff newDiff) {
		Diff oldDiff = diff;
		diff = newDiff;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.CHANGE_OVERLAY__DIFF, oldDiff,
					diff));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.CHANGE_OVERLAY__DIFF:
			if (resolve)
				return getDiff();
			return basicGetDiff();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ModelReviewPackage.CHANGE_OVERLAY__DIFF:
			setDiff((Diff) newValue);
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
		case ModelReviewPackage.CHANGE_OVERLAY__DIFF:
			setDiff((Diff) null);
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
		case ModelReviewPackage.CHANGE_OVERLAY__DIFF:
			return diff != null;
		}
		return super.eIsSet(featureID);
	}

} // ChangeOverlayImpl

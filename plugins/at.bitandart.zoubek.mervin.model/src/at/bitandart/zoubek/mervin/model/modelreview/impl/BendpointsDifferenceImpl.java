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

import at.bitandart.zoubek.mervin.model.modelreview.BendpointsDifference;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.Diff;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Bendpoints Difference</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.BendpointsDifferenceImpl#getRawDiffs
 * <em>Raw Diffs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BendpointsDifferenceImpl extends MinimalEObjectImpl.Container implements BendpointsDifference {
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected BendpointsDifferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.BENDPOINTS_DIFFERENCE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Diff> getRawDiffs() {
		if (rawDiffs == null) {
			rawDiffs = new EObjectResolvingEList<Diff>(Diff.class, this,
					ModelReviewPackage.BENDPOINTS_DIFFERENCE__RAW_DIFFS);
		}
		return rawDiffs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.BENDPOINTS_DIFFERENCE__RAW_DIFFS:
			return getRawDiffs();
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
		case ModelReviewPackage.BENDPOINTS_DIFFERENCE__RAW_DIFFS:
			getRawDiffs().clear();
			getRawDiffs().addAll((Collection<? extends Diff>) newValue);
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
		case ModelReviewPackage.BENDPOINTS_DIFFERENCE__RAW_DIFFS:
			getRawDiffs().clear();
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
		case ModelReviewPackage.BENDPOINTS_DIFFERENCE__RAW_DIFFS:
			return rawDiffs != null && !rawDiffs.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // BendpointsDifferenceImpl

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

import at.bitandart.zoubek.mervin.model.modelreview.ModelInstance;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Model Instance</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelInstanceImpl#getObjects
 * <em>Objects</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelInstanceImpl#getRootPackage
 * <em>Root Package</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelInstanceImpl extends MinimalEObjectImpl.Container implements
		ModelInstance {
	/**
	 * The cached value of the '{@link #getObjects() <em>Objects</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getObjects()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> objects;

	/**
	 * The cached value of the '{@link #getRootPackage() <em>Root Package</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRootPackage()
	 * @generated
	 * @ordered
	 */
	protected EPackage rootPackage;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ModelInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.MODEL_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<EObject> getObjects() {
		if (objects == null) {
			objects = new EObjectResolvingEList<EObject>(EObject.class, this,
					ModelReviewPackage.MODEL_INSTANCE__OBJECTS);
		}
		return objects;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EPackage getRootPackage() {
		if (rootPackage != null && rootPackage.eIsProxy()) {
			InternalEObject oldRootPackage = (InternalEObject) rootPackage;
			rootPackage = (EPackage) eResolveProxy(oldRootPackage);
			if (rootPackage != oldRootPackage) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.MODEL_INSTANCE__ROOT_PACKAGE,
							oldRootPackage, rootPackage));
			}
		}
		return rootPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EPackage basicGetRootPackage() {
		return rootPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRootPackage(EPackage newRootPackage) {
		EPackage oldRootPackage = rootPackage;
		rootPackage = newRootPackage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.MODEL_INSTANCE__ROOT_PACKAGE,
					oldRootPackage, rootPackage));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.MODEL_INSTANCE__OBJECTS:
			return getObjects();
		case ModelReviewPackage.MODEL_INSTANCE__ROOT_PACKAGE:
			if (resolve)
				return getRootPackage();
			return basicGetRootPackage();
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
		case ModelReviewPackage.MODEL_INSTANCE__OBJECTS:
			getObjects().clear();
			getObjects().addAll((Collection<? extends EObject>) newValue);
			return;
		case ModelReviewPackage.MODEL_INSTANCE__ROOT_PACKAGE:
			setRootPackage((EPackage) newValue);
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
		case ModelReviewPackage.MODEL_INSTANCE__OBJECTS:
			getObjects().clear();
			return;
		case ModelReviewPackage.MODEL_INSTANCE__ROOT_PACKAGE:
			setRootPackage((EPackage) null);
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
		case ModelReviewPackage.MODEL_INSTANCE__OBJECTS:
			return objects != null && !objects.isEmpty();
		case ModelReviewPackage.MODEL_INSTANCE__ROOT_PACKAGE:
			return rootPackage != null;
		}
		return super.eIsSet(featureID);
	}

} // ModelInstanceImpl

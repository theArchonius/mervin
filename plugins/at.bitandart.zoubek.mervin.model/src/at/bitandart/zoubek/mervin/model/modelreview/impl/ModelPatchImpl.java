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
import at.bitandart.zoubek.mervin.model.modelreview.ModelPatch;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Model Patch</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelPatchImpl#getNewModelInstance
 * <em>New Model Instance</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelPatchImpl#getOldModelInstance
 * <em>Old Model Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelPatchImpl extends PatchImpl implements ModelPatch {
	/**
	 * The cached value of the '{@link #getNewModelInstance()
	 * <em>New Model Instance</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getNewModelInstance()
	 * @generated
	 * @ordered
	 */
	protected ModelInstance newModelInstance;
	/**
	 * The cached value of the '{@link #getOldModelInstance()
	 * <em>Old Model Instance</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getOldModelInstance()
	 * @generated
	 * @ordered
	 */
	protected ModelInstance oldModelInstance;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ModelPatchImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.MODEL_PATCH;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelInstance getNewModelInstance() {
		if (newModelInstance != null && newModelInstance.eIsProxy()) {
			InternalEObject oldNewModelInstance = (InternalEObject) newModelInstance;
			newModelInstance = (ModelInstance) eResolveProxy(oldNewModelInstance);
			if (newModelInstance != oldNewModelInstance) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.MODEL_PATCH__NEW_MODEL_INSTANCE,
							oldNewModelInstance, newModelInstance));
			}
		}
		return newModelInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelInstance basicGetNewModelInstance() {
		return newModelInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNewModelInstance(ModelInstance newNewModelInstance) {
		ModelInstance oldNewModelInstance = newModelInstance;
		newModelInstance = newNewModelInstance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.MODEL_PATCH__NEW_MODEL_INSTANCE,
					oldNewModelInstance, newModelInstance));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelInstance getOldModelInstance() {
		if (oldModelInstance != null && oldModelInstance.eIsProxy()) {
			InternalEObject oldOldModelInstance = (InternalEObject) oldModelInstance;
			oldModelInstance = (ModelInstance) eResolveProxy(oldOldModelInstance);
			if (oldModelInstance != oldOldModelInstance) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.MODEL_PATCH__OLD_MODEL_INSTANCE,
							oldOldModelInstance, oldModelInstance));
			}
		}
		return oldModelInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelInstance basicGetOldModelInstance() {
		return oldModelInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOldModelInstance(ModelInstance newOldModelInstance) {
		ModelInstance oldOldModelInstance = oldModelInstance;
		oldModelInstance = newOldModelInstance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.MODEL_PATCH__OLD_MODEL_INSTANCE,
					oldOldModelInstance, oldModelInstance));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.MODEL_PATCH__NEW_MODEL_INSTANCE:
			if (resolve)
				return getNewModelInstance();
			return basicGetNewModelInstance();
		case ModelReviewPackage.MODEL_PATCH__OLD_MODEL_INSTANCE:
			if (resolve)
				return getOldModelInstance();
			return basicGetOldModelInstance();
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
		case ModelReviewPackage.MODEL_PATCH__NEW_MODEL_INSTANCE:
			setNewModelInstance((ModelInstance) newValue);
			return;
		case ModelReviewPackage.MODEL_PATCH__OLD_MODEL_INSTANCE:
			setOldModelInstance((ModelInstance) newValue);
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
		case ModelReviewPackage.MODEL_PATCH__NEW_MODEL_INSTANCE:
			setNewModelInstance((ModelInstance) null);
			return;
		case ModelReviewPackage.MODEL_PATCH__OLD_MODEL_INSTANCE:
			setOldModelInstance((ModelInstance) null);
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
		case ModelReviewPackage.MODEL_PATCH__NEW_MODEL_INSTANCE:
			return newModelInstance != null;
		case ModelReviewPackage.MODEL_PATCH__OLD_MODEL_INSTANCE:
			return oldModelInstance != null;
		}
		return super.eIsSet(featureID);
	}

} // ModelPatchImpl

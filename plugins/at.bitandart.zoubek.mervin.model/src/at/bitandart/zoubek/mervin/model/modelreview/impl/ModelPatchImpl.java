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
package at.bitandart.zoubek.mervin.model.modelreview.impl;

import at.bitandart.zoubek.mervin.model.modelreview.ModelPatch;
import at.bitandart.zoubek.mervin.model.modelreview.ModelResource;
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
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelPatchImpl#getNewModelResource
 * <em>New Model Resource</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelPatchImpl#getOldModelResource
 * <em>Old Model Resource</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModelPatchImpl extends PatchImpl implements ModelPatch {
	/**
	 * The cached value of the '{@link #getNewModelResource()
	 * <em>New Model Resource</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getNewModelResource()
	 * @generated
	 * @ordered
	 */
	protected ModelResource newModelResource;

	/**
	 * The cached value of the '{@link #getOldModelResource()
	 * <em>Old Model Resource</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getOldModelResource()
	 * @generated
	 * @ordered
	 */
	protected ModelResource oldModelResource;

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
	public ModelResource getNewModelResource() {
		if (newModelResource != null && newModelResource.eIsProxy()) {
			InternalEObject oldNewModelResource = (InternalEObject) newModelResource;
			newModelResource = (ModelResource) eResolveProxy(oldNewModelResource);
			if (newModelResource != oldNewModelResource) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.MODEL_PATCH__NEW_MODEL_RESOURCE, oldNewModelResource, newModelResource));
			}
		}
		return newModelResource;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelResource basicGetNewModelResource() {
		return newModelResource;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNewModelResource(ModelResource newNewModelResource) {
		ModelResource oldNewModelResource = newModelResource;
		newModelResource = newNewModelResource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.MODEL_PATCH__NEW_MODEL_RESOURCE,
					oldNewModelResource, newModelResource));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelResource getOldModelResource() {
		if (oldModelResource != null && oldModelResource.eIsProxy()) {
			InternalEObject oldOldModelResource = (InternalEObject) oldModelResource;
			oldModelResource = (ModelResource) eResolveProxy(oldOldModelResource);
			if (oldModelResource != oldOldModelResource) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.MODEL_PATCH__OLD_MODEL_RESOURCE, oldOldModelResource, oldModelResource));
			}
		}
		return oldModelResource;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelResource basicGetOldModelResource() {
		return oldModelResource;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOldModelResource(ModelResource newOldModelResource) {
		ModelResource oldOldModelResource = oldModelResource;
		oldModelResource = newOldModelResource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.MODEL_PATCH__OLD_MODEL_RESOURCE,
					oldOldModelResource, oldModelResource));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.MODEL_PATCH__NEW_MODEL_RESOURCE:
			if (resolve)
				return getNewModelResource();
			return basicGetNewModelResource();
		case ModelReviewPackage.MODEL_PATCH__OLD_MODEL_RESOURCE:
			if (resolve)
				return getOldModelResource();
			return basicGetOldModelResource();
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
		case ModelReviewPackage.MODEL_PATCH__NEW_MODEL_RESOURCE:
			setNewModelResource((ModelResource) newValue);
			return;
		case ModelReviewPackage.MODEL_PATCH__OLD_MODEL_RESOURCE:
			setOldModelResource((ModelResource) newValue);
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
		case ModelReviewPackage.MODEL_PATCH__NEW_MODEL_RESOURCE:
			setNewModelResource((ModelResource) null);
			return;
		case ModelReviewPackage.MODEL_PATCH__OLD_MODEL_RESOURCE:
			setOldModelResource((ModelResource) null);
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
		case ModelReviewPackage.MODEL_PATCH__NEW_MODEL_RESOURCE:
			return newModelResource != null;
		case ModelReviewPackage.MODEL_PATCH__OLD_MODEL_RESOURCE:
			return oldModelResource != null;
		}
		return super.eIsSet(featureID);
	}

} // ModelPatchImpl

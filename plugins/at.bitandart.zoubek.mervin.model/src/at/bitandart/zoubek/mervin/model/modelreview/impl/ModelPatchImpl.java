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
import org.eclipse.emf.compare.Comparison;
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
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelPatchImpl#getComparison
 * <em>Comparison</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelPatchImpl#getModelInstance
 * <em>Model Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelPatchImpl extends PatchImpl implements ModelPatch {
	/**
	 * The cached value of the '{@link #getComparison() <em>Comparison</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getComparison()
	 * @generated
	 * @ordered
	 */
	protected Comparison comparison;
	/**
	 * The cached value of the '{@link #getModelInstance()
	 * <em>Model Instance</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getModelInstance()
	 * @generated
	 * @ordered
	 */
	protected ModelInstance modelInstance;

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
	public Comparison getComparison() {
		if (comparison != null && comparison.eIsProxy()) {
			InternalEObject oldComparison = (InternalEObject) comparison;
			comparison = (Comparison) eResolveProxy(oldComparison);
			if (comparison != oldComparison) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.MODEL_PATCH__COMPARISON,
							oldComparison, comparison));
			}
		}
		return comparison;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Comparison basicGetComparison() {
		return comparison;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setComparison(Comparison newComparison) {
		Comparison oldComparison = comparison;
		comparison = newComparison;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.MODEL_PATCH__COMPARISON, oldComparison,
					comparison));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelInstance getModelInstance() {
		if (modelInstance != null && modelInstance.eIsProxy()) {
			InternalEObject oldModelInstance = (InternalEObject) modelInstance;
			modelInstance = (ModelInstance) eResolveProxy(oldModelInstance);
			if (modelInstance != oldModelInstance) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.MODEL_PATCH__MODEL_INSTANCE,
							oldModelInstance, modelInstance));
			}
		}
		return modelInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelInstance basicGetModelInstance() {
		return modelInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setModelInstance(ModelInstance newModelInstance) {
		ModelInstance oldModelInstance = modelInstance;
		modelInstance = newModelInstance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.MODEL_PATCH__MODEL_INSTANCE,
					oldModelInstance, modelInstance));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.MODEL_PATCH__COMPARISON:
			if (resolve)
				return getComparison();
			return basicGetComparison();
		case ModelReviewPackage.MODEL_PATCH__MODEL_INSTANCE:
			if (resolve)
				return getModelInstance();
			return basicGetModelInstance();
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
		case ModelReviewPackage.MODEL_PATCH__COMPARISON:
			setComparison((Comparison) newValue);
			return;
		case ModelReviewPackage.MODEL_PATCH__MODEL_INSTANCE:
			setModelInstance((ModelInstance) newValue);
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
		case ModelReviewPackage.MODEL_PATCH__COMPARISON:
			setComparison((Comparison) null);
			return;
		case ModelReviewPackage.MODEL_PATCH__MODEL_INSTANCE:
			setModelInstance((ModelInstance) null);
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
		case ModelReviewPackage.MODEL_PATCH__COMPARISON:
			return comparison != null;
		case ModelReviewPackage.MODEL_PATCH__MODEL_INSTANCE:
			return modelInstance != null;
		}
		return super.eIsSet(featureID);
	}

} // ModelPatchImpl

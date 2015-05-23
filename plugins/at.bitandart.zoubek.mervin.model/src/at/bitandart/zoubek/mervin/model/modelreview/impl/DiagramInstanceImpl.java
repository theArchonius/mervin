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

import at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Diagram Instance</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramInstanceImpl#getNotationModel
 * <em>Notation Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramInstanceImpl extends MinimalEObjectImpl.Container implements
		DiagramInstance {
	/**
	 * The cached value of the '{@link #getNotationModel()
	 * <em>Notation Model</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getNotationModel()
	 * @generated
	 * @ordered
	 */
	protected Diagram notationModel;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DiagramInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.DIAGRAM_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Diagram getNotationModel() {
		if (notationModel != null && notationModel.eIsProxy()) {
			InternalEObject oldNotationModel = (InternalEObject) notationModel;
			notationModel = (Diagram) eResolveProxy(oldNotationModel);
			if (notationModel != oldNotationModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(
							this,
							Notification.RESOLVE,
							ModelReviewPackage.DIAGRAM_INSTANCE__NOTATION_MODEL,
							oldNotationModel, notationModel));
			}
		}
		return notationModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Diagram basicGetNotationModel() {
		return notationModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNotationModel(Diagram newNotationModel) {
		Diagram oldNotationModel = notationModel;
		notationModel = newNotationModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.DIAGRAM_INSTANCE__NOTATION_MODEL,
					oldNotationModel, notationModel));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.DIAGRAM_INSTANCE__NOTATION_MODEL:
			if (resolve)
				return getNotationModel();
			return basicGetNotationModel();
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
		case ModelReviewPackage.DIAGRAM_INSTANCE__NOTATION_MODEL:
			setNotationModel((Diagram) newValue);
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
		case ModelReviewPackage.DIAGRAM_INSTANCE__NOTATION_MODEL:
			setNotationModel((Diagram) null);
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
		case ModelReviewPackage.DIAGRAM_INSTANCE__NOTATION_MODEL:
			return notationModel != null;
		}
		return super.eIsSet(featureID);
	}

} // DiagramInstanceImpl

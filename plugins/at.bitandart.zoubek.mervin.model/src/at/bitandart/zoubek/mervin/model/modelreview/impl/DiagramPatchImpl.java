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
import at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Diagram Patch</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramPatchImpl#getNewDiagramInstance
 * <em>New Diagram Instance</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramPatchImpl#getOldDiagramInstance
 * <em>Old Diagram Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramPatchImpl extends PatchImpl implements DiagramPatch {
	/**
	 * The cached value of the '{@link #getNewDiagramInstance()
	 * <em>New Diagram Instance</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getNewDiagramInstance()
	 * @generated
	 * @ordered
	 */
	protected DiagramInstance newDiagramInstance;
	/**
	 * The cached value of the '{@link #getOldDiagramInstance()
	 * <em>Old Diagram Instance</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getOldDiagramInstance()
	 * @generated
	 * @ordered
	 */
	protected DiagramInstance oldDiagramInstance;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DiagramPatchImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.DIAGRAM_PATCH;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramInstance getNewDiagramInstance() {
		if (newDiagramInstance != null && newDiagramInstance.eIsProxy()) {
			InternalEObject oldNewDiagramInstance = (InternalEObject) newDiagramInstance;
			newDiagramInstance = (DiagramInstance) eResolveProxy(oldNewDiagramInstance);
			if (newDiagramInstance != oldNewDiagramInstance) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(
							this,
							Notification.RESOLVE,
							ModelReviewPackage.DIAGRAM_PATCH__NEW_DIAGRAM_INSTANCE,
							oldNewDiagramInstance, newDiagramInstance));
			}
		}
		return newDiagramInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramInstance basicGetNewDiagramInstance() {
		return newDiagramInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNewDiagramInstance(DiagramInstance newNewDiagramInstance) {
		DiagramInstance oldNewDiagramInstance = newDiagramInstance;
		newDiagramInstance = newNewDiagramInstance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.DIAGRAM_PATCH__NEW_DIAGRAM_INSTANCE,
					oldNewDiagramInstance, newDiagramInstance));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramInstance getOldDiagramInstance() {
		if (oldDiagramInstance != null && oldDiagramInstance.eIsProxy()) {
			InternalEObject oldOldDiagramInstance = (InternalEObject) oldDiagramInstance;
			oldDiagramInstance = (DiagramInstance) eResolveProxy(oldOldDiagramInstance);
			if (oldDiagramInstance != oldOldDiagramInstance) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(
							this,
							Notification.RESOLVE,
							ModelReviewPackage.DIAGRAM_PATCH__OLD_DIAGRAM_INSTANCE,
							oldOldDiagramInstance, oldDiagramInstance));
			}
		}
		return oldDiagramInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramInstance basicGetOldDiagramInstance() {
		return oldDiagramInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOldDiagramInstance(DiagramInstance newOldDiagramInstance) {
		DiagramInstance oldOldDiagramInstance = oldDiagramInstance;
		oldDiagramInstance = newOldDiagramInstance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.DIAGRAM_PATCH__OLD_DIAGRAM_INSTANCE,
					oldOldDiagramInstance, oldDiagramInstance));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.DIAGRAM_PATCH__NEW_DIAGRAM_INSTANCE:
			if (resolve)
				return getNewDiagramInstance();
			return basicGetNewDiagramInstance();
		case ModelReviewPackage.DIAGRAM_PATCH__OLD_DIAGRAM_INSTANCE:
			if (resolve)
				return getOldDiagramInstance();
			return basicGetOldDiagramInstance();
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
		case ModelReviewPackage.DIAGRAM_PATCH__NEW_DIAGRAM_INSTANCE:
			setNewDiagramInstance((DiagramInstance) newValue);
			return;
		case ModelReviewPackage.DIAGRAM_PATCH__OLD_DIAGRAM_INSTANCE:
			setOldDiagramInstance((DiagramInstance) newValue);
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
		case ModelReviewPackage.DIAGRAM_PATCH__NEW_DIAGRAM_INSTANCE:
			setNewDiagramInstance((DiagramInstance) null);
			return;
		case ModelReviewPackage.DIAGRAM_PATCH__OLD_DIAGRAM_INSTANCE:
			setOldDiagramInstance((DiagramInstance) null);
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
		case ModelReviewPackage.DIAGRAM_PATCH__NEW_DIAGRAM_INSTANCE:
			return newDiagramInstance != null;
		case ModelReviewPackage.DIAGRAM_PATCH__OLD_DIAGRAM_INSTANCE:
			return oldDiagramInstance != null;
		}
		return super.eIsSet(featureID);
	}

} // DiagramPatchImpl

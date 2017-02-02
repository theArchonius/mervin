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

import at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch;
import at.bitandart.zoubek.mervin.model.modelreview.DiagramResource;
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
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramPatchImpl#getNewDiagramResource
 * <em>New Diagram Resource</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramPatchImpl#getOldDiagramResource
 * <em>Old Diagram Resource</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DiagramPatchImpl extends PatchImpl implements DiagramPatch {
	/**
	 * The cached value of the '{@link #getNewDiagramResource()
	 * <em>New Diagram Resource</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getNewDiagramResource()
	 * @generated
	 * @ordered
	 */
	protected DiagramResource newDiagramResource;

	/**
	 * The cached value of the '{@link #getOldDiagramResource()
	 * <em>Old Diagram Resource</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getOldDiagramResource()
	 * @generated
	 * @ordered
	 */
	protected DiagramResource oldDiagramResource;

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
	public DiagramResource getNewDiagramResource() {
		if (newDiagramResource != null && newDiagramResource.eIsProxy()) {
			InternalEObject oldNewDiagramResource = (InternalEObject) newDiagramResource;
			newDiagramResource = (DiagramResource) eResolveProxy(oldNewDiagramResource);
			if (newDiagramResource != oldNewDiagramResource) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.DIAGRAM_PATCH__NEW_DIAGRAM_RESOURCE, oldNewDiagramResource,
							newDiagramResource));
			}
		}
		return newDiagramResource;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramResource basicGetNewDiagramResource() {
		return newDiagramResource;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNewDiagramResource(DiagramResource newNewDiagramResource) {
		DiagramResource oldNewDiagramResource = newDiagramResource;
		newDiagramResource = newNewDiagramResource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.DIAGRAM_PATCH__NEW_DIAGRAM_RESOURCE, oldNewDiagramResource, newDiagramResource));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramResource getOldDiagramResource() {
		if (oldDiagramResource != null && oldDiagramResource.eIsProxy()) {
			InternalEObject oldOldDiagramResource = (InternalEObject) oldDiagramResource;
			oldDiagramResource = (DiagramResource) eResolveProxy(oldOldDiagramResource);
			if (oldDiagramResource != oldOldDiagramResource) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.DIAGRAM_PATCH__OLD_DIAGRAM_RESOURCE, oldOldDiagramResource,
							oldDiagramResource));
			}
		}
		return oldDiagramResource;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramResource basicGetOldDiagramResource() {
		return oldDiagramResource;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOldDiagramResource(DiagramResource newOldDiagramResource) {
		DiagramResource oldOldDiagramResource = oldDiagramResource;
		oldDiagramResource = newOldDiagramResource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.DIAGRAM_PATCH__OLD_DIAGRAM_RESOURCE, oldOldDiagramResource, oldDiagramResource));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.DIAGRAM_PATCH__NEW_DIAGRAM_RESOURCE:
			if (resolve)
				return getNewDiagramResource();
			return basicGetNewDiagramResource();
		case ModelReviewPackage.DIAGRAM_PATCH__OLD_DIAGRAM_RESOURCE:
			if (resolve)
				return getOldDiagramResource();
			return basicGetOldDiagramResource();
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
		case ModelReviewPackage.DIAGRAM_PATCH__NEW_DIAGRAM_RESOURCE:
			setNewDiagramResource((DiagramResource) newValue);
			return;
		case ModelReviewPackage.DIAGRAM_PATCH__OLD_DIAGRAM_RESOURCE:
			setOldDiagramResource((DiagramResource) newValue);
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
		case ModelReviewPackage.DIAGRAM_PATCH__NEW_DIAGRAM_RESOURCE:
			setNewDiagramResource((DiagramResource) null);
			return;
		case ModelReviewPackage.DIAGRAM_PATCH__OLD_DIAGRAM_RESOURCE:
			setOldDiagramResource((DiagramResource) null);
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
		case ModelReviewPackage.DIAGRAM_PATCH__NEW_DIAGRAM_RESOURCE:
			return newDiagramResource != null;
		case ModelReviewPackage.DIAGRAM_PATCH__OLD_DIAGRAM_RESOURCE:
			return oldDiagramResource != null;
		}
		return super.eIsSet(featureID);
	}

} // DiagramPatchImpl

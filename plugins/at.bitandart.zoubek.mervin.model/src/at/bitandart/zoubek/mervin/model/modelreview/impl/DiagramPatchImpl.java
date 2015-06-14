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
import org.eclipse.emf.compare.Comparison;
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
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramPatchImpl#getComparison
 * <em>Comparison</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramPatchImpl#getDiagramInstance
 * <em>Diagram Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramPatchImpl extends PatchImpl implements DiagramPatch {
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
	 * The cached value of the '{@link #getDiagramInstance()
	 * <em>Diagram Instance</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getDiagramInstance()
	 * @generated
	 * @ordered
	 */
	protected DiagramInstance diagramInstance;

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
	public Comparison getComparison() {
		if (comparison != null && comparison.eIsProxy()) {
			InternalEObject oldComparison = (InternalEObject) comparison;
			comparison = (Comparison) eResolveProxy(oldComparison);
			if (comparison != oldComparison) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.DIAGRAM_PATCH__COMPARISON,
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
					ModelReviewPackage.DIAGRAM_PATCH__COMPARISON,
					oldComparison, comparison));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramInstance getDiagramInstance() {
		if (diagramInstance != null && diagramInstance.eIsProxy()) {
			InternalEObject oldDiagramInstance = (InternalEObject) diagramInstance;
			diagramInstance = (DiagramInstance) eResolveProxy(oldDiagramInstance);
			if (diagramInstance != oldDiagramInstance) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.DIAGRAM_PATCH__DIAGRAM_INSTANCE,
							oldDiagramInstance, diagramInstance));
			}
		}
		return diagramInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramInstance basicGetDiagramInstance() {
		return diagramInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDiagramInstance(DiagramInstance newDiagramInstance) {
		DiagramInstance oldDiagramInstance = diagramInstance;
		diagramInstance = newDiagramInstance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.DIAGRAM_PATCH__DIAGRAM_INSTANCE,
					oldDiagramInstance, diagramInstance));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.DIAGRAM_PATCH__COMPARISON:
			if (resolve)
				return getComparison();
			return basicGetComparison();
		case ModelReviewPackage.DIAGRAM_PATCH__DIAGRAM_INSTANCE:
			if (resolve)
				return getDiagramInstance();
			return basicGetDiagramInstance();
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
		case ModelReviewPackage.DIAGRAM_PATCH__COMPARISON:
			setComparison((Comparison) newValue);
			return;
		case ModelReviewPackage.DIAGRAM_PATCH__DIAGRAM_INSTANCE:
			setDiagramInstance((DiagramInstance) newValue);
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
		case ModelReviewPackage.DIAGRAM_PATCH__COMPARISON:
			setComparison((Comparison) null);
			return;
		case ModelReviewPackage.DIAGRAM_PATCH__DIAGRAM_INSTANCE:
			setDiagramInstance((DiagramInstance) null);
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
		case ModelReviewPackage.DIAGRAM_PATCH__COMPARISON:
			return comparison != null;
		case ModelReviewPackage.DIAGRAM_PATCH__DIAGRAM_INSTANCE:
			return diagramInstance != null;
		}
		return super.eIsSet(featureID);
	}

} // DiagramPatchImpl

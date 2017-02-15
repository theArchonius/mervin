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

import at.bitandart.zoubek.mervin.model.modelreview.Difference;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.EdgeDifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.gmf.runtime.notation.View;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Edge Difference Overlay</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.EdgeDifferenceOverlayImpl#getLinkedView
 * <em>Linked View</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.EdgeDifferenceOverlayImpl#getDifferences
 * <em>Differences</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.EdgeDifferenceOverlayImpl#isCommented
 * <em>Commented</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.EdgeDifferenceOverlayImpl#getDependentOverlays
 * <em>Dependent Overlays</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.EdgeDifferenceOverlayImpl#getDependencies
 * <em>Dependencies</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EdgeDifferenceOverlayImpl extends MinimalEObjectImpl.Container implements EdgeDifferenceOverlay {
	/**
	 * The cached value of the '{@link #getLinkedView() <em>Linked View</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLinkedView()
	 * @generated
	 * @ordered
	 */
	protected View linkedView;

	/**
	 * The cached value of the '{@link #getDifferences() <em>Differences</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDifferences()
	 * @generated
	 * @ordered
	 */
	protected EList<Difference> differences;

	/**
	 * The default value of the '{@link #isCommented() <em>Commented</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isCommented()
	 * @generated
	 * @ordered
	 */
	protected static final boolean COMMENTED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCommented() <em>Commented</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isCommented()
	 * @generated
	 * @ordered
	 */
	protected boolean commented = COMMENTED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDependentOverlays()
	 * <em>Dependent Overlays</em>}' reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getDependentOverlays()
	 * @generated
	 * @ordered
	 */
	protected EList<DifferenceOverlay> dependentOverlays;

	/**
	 * The cached value of the '{@link #getDependencies() <em>Dependencies</em>}
	 * ' reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDependencies()
	 * @generated
	 * @ordered
	 */
	protected EList<DifferenceOverlay> dependencies;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EdgeDifferenceOverlayImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.EDGE_DIFFERENCE_OVERLAY;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public View getLinkedView() {
		if (linkedView != null && linkedView.eIsProxy()) {
			InternalEObject oldLinkedView = (InternalEObject) linkedView;
			linkedView = (View) eResolveProxy(oldLinkedView);
			if (linkedView != oldLinkedView) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__LINKED_VIEW, oldLinkedView, linkedView));
			}
		}
		return linkedView;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public View basicGetLinkedView() {
		return linkedView;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLinkedView(View newLinkedView) {
		View oldLinkedView = linkedView;
		linkedView = newLinkedView;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__LINKED_VIEW, oldLinkedView, linkedView));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Difference> getDifferences() {
		if (differences == null) {
			differences = new EObjectContainmentEList<Difference>(Difference.class, this,
					ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DIFFERENCES);
		}
		return differences;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isCommented() {
		return commented;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCommented(boolean newCommented) {
		boolean oldCommented = commented;
		commented = newCommented;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__COMMENTED,
					oldCommented, commented));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<DifferenceOverlay> getDependentOverlays() {
		if (dependentOverlays == null) {
			dependentOverlays = new EObjectWithInverseResolvingEList.ManyInverse<DifferenceOverlay>(
					DifferenceOverlay.class, this, ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENT_OVERLAYS,
					ModelReviewPackage.DIFFERENCE_OVERLAY__DEPENDENCIES);
		}
		return dependentOverlays;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<DifferenceOverlay> getDependencies() {
		if (dependencies == null) {
			dependencies = new EObjectWithInverseResolvingEList.ManyInverse<DifferenceOverlay>(DifferenceOverlay.class,
					this, ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENCIES,
					ModelReviewPackage.DIFFERENCE_OVERLAY__DEPENDENT_OVERLAYS);
		}
		return dependencies;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENT_OVERLAYS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getDependentOverlays()).basicAdd(otherEnd,
					msgs);
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENCIES:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getDependencies()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DIFFERENCES:
			return ((InternalEList<?>) getDifferences()).basicRemove(otherEnd, msgs);
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENT_OVERLAYS:
			return ((InternalEList<?>) getDependentOverlays()).basicRemove(otherEnd, msgs);
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENCIES:
			return ((InternalEList<?>) getDependencies()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__LINKED_VIEW:
			if (resolve)
				return getLinkedView();
			return basicGetLinkedView();
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DIFFERENCES:
			return getDifferences();
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__COMMENTED:
			return isCommented();
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENT_OVERLAYS:
			return getDependentOverlays();
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENCIES:
			return getDependencies();
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
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__LINKED_VIEW:
			setLinkedView((View) newValue);
			return;
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DIFFERENCES:
			getDifferences().clear();
			getDifferences().addAll((Collection<? extends Difference>) newValue);
			return;
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__COMMENTED:
			setCommented((Boolean) newValue);
			return;
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENT_OVERLAYS:
			getDependentOverlays().clear();
			getDependentOverlays().addAll((Collection<? extends DifferenceOverlay>) newValue);
			return;
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENCIES:
			getDependencies().clear();
			getDependencies().addAll((Collection<? extends DifferenceOverlay>) newValue);
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
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__LINKED_VIEW:
			setLinkedView((View) null);
			return;
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DIFFERENCES:
			getDifferences().clear();
			return;
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__COMMENTED:
			setCommented(COMMENTED_EDEFAULT);
			return;
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENT_OVERLAYS:
			getDependentOverlays().clear();
			return;
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENCIES:
			getDependencies().clear();
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
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__LINKED_VIEW:
			return linkedView != null;
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DIFFERENCES:
			return differences != null && !differences.isEmpty();
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__COMMENTED:
			return commented != COMMENTED_EDEFAULT;
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENT_OVERLAYS:
			return dependentOverlays != null && !dependentOverlays.isEmpty();
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY__DEPENDENCIES:
			return dependencies != null && !dependencies.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (commented: ");
		result.append(commented);
		result.append(')');
		return result.toString();
	}

} // EdgeDifferenceOverlayImpl

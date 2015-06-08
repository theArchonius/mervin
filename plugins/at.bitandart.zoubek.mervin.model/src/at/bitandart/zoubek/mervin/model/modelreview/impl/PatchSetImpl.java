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
import at.bitandart.zoubek.mervin.model.modelreview.ModelInstance;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.Patch;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Patch Set</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getId
 * <em>Id</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getReview
 * <em>Review</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getPatches
 * <em>Patches</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getInvolvedModels
 * <em>Involved Models</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getInvolvedDiagrams
 * <em>Involved Diagrams</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PatchSetImpl extends MinimalEObjectImpl.Container implements
		PatchSet {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPatches() <em>Patches</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPatches()
	 * @generated
	 * @ordered
	 */
	protected EList<Patch> patches;

	/**
	 * The cached value of the '{@link #getInvolvedModels()
	 * <em>Involved Models</em>}' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getInvolvedModels()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelInstance> involvedModels;

	/**
	 * The cached value of the '{@link #getInvolvedDiagrams()
	 * <em>Involved Diagrams</em>}' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getInvolvedDiagrams()
	 * @generated
	 * @ordered
	 */
	protected EList<DiagramInstance> involvedDiagrams;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PatchSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.PATCH_SET;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.PATCH_SET__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelReview getReview() {
		if (eContainerFeatureID() != ModelReviewPackage.PATCH_SET__REVIEW)
			return null;
		return (ModelReview) eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetReview(ModelReview newReview,
			NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject) newReview,
				ModelReviewPackage.PATCH_SET__REVIEW, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setReview(ModelReview newReview) {
		if (newReview != eInternalContainer()
				|| (eContainerFeatureID() != ModelReviewPackage.PATCH_SET__REVIEW && newReview != null)) {
			if (EcoreUtil.isAncestor(this, newReview))
				throw new IllegalArgumentException(
						"Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newReview != null)
				msgs = ((InternalEObject) newReview).eInverseAdd(this,
						ModelReviewPackage.MODEL_REVIEW__PATCH_SETS,
						ModelReview.class, msgs);
			msgs = basicSetReview(newReview, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.PATCH_SET__REVIEW, newReview, newReview));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Patch> getPatches() {
		if (patches == null) {
			patches = new EObjectResolvingEList<Patch>(Patch.class, this,
					ModelReviewPackage.PATCH_SET__PATCHES);
		}
		return patches;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<ModelInstance> getInvolvedModels() {
		if (involvedModels == null) {
			involvedModels = new EObjectResolvingEList<ModelInstance>(
					ModelInstance.class, this,
					ModelReviewPackage.PATCH_SET__INVOLVED_MODELS);
		}
		return involvedModels;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<DiagramInstance> getInvolvedDiagrams() {
		if (involvedDiagrams == null) {
			involvedDiagrams = new EObjectResolvingEList<DiagramInstance>(
					DiagramInstance.class, this,
					ModelReviewPackage.PATCH_SET__INVOLVED_DIAGRAMS);
		}
		return involvedDiagrams;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelReviewPackage.PATCH_SET__REVIEW:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetReview((ModelReview) otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelReviewPackage.PATCH_SET__REVIEW:
			return basicSetReview(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(
			NotificationChain msgs) {
		switch (eContainerFeatureID()) {
		case ModelReviewPackage.PATCH_SET__REVIEW:
			return eInternalContainer().eInverseRemove(this,
					ModelReviewPackage.MODEL_REVIEW__PATCH_SETS,
					ModelReview.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.PATCH_SET__ID:
			return getId();
		case ModelReviewPackage.PATCH_SET__REVIEW:
			return getReview();
		case ModelReviewPackage.PATCH_SET__PATCHES:
			return getPatches();
		case ModelReviewPackage.PATCH_SET__INVOLVED_MODELS:
			return getInvolvedModels();
		case ModelReviewPackage.PATCH_SET__INVOLVED_DIAGRAMS:
			return getInvolvedDiagrams();
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
		case ModelReviewPackage.PATCH_SET__ID:
			setId((String) newValue);
			return;
		case ModelReviewPackage.PATCH_SET__REVIEW:
			setReview((ModelReview) newValue);
			return;
		case ModelReviewPackage.PATCH_SET__PATCHES:
			getPatches().clear();
			getPatches().addAll((Collection<? extends Patch>) newValue);
			return;
		case ModelReviewPackage.PATCH_SET__INVOLVED_MODELS:
			getInvolvedModels().clear();
			getInvolvedModels().addAll(
					(Collection<? extends ModelInstance>) newValue);
			return;
		case ModelReviewPackage.PATCH_SET__INVOLVED_DIAGRAMS:
			getInvolvedDiagrams().clear();
			getInvolvedDiagrams().addAll(
					(Collection<? extends DiagramInstance>) newValue);
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
		case ModelReviewPackage.PATCH_SET__ID:
			setId(ID_EDEFAULT);
			return;
		case ModelReviewPackage.PATCH_SET__REVIEW:
			setReview((ModelReview) null);
			return;
		case ModelReviewPackage.PATCH_SET__PATCHES:
			getPatches().clear();
			return;
		case ModelReviewPackage.PATCH_SET__INVOLVED_MODELS:
			getInvolvedModels().clear();
			return;
		case ModelReviewPackage.PATCH_SET__INVOLVED_DIAGRAMS:
			getInvolvedDiagrams().clear();
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
		case ModelReviewPackage.PATCH_SET__ID:
			return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
		case ModelReviewPackage.PATCH_SET__REVIEW:
			return getReview() != null;
		case ModelReviewPackage.PATCH_SET__PATCHES:
			return patches != null && !patches.isEmpty();
		case ModelReviewPackage.PATCH_SET__INVOLVED_MODELS:
			return involvedModels != null && !involvedModels.isEmpty();
		case ModelReviewPackage.PATCH_SET__INVOLVED_DIAGRAMS:
			return involvedDiagrams != null && !involvedDiagrams.isEmpty();
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
		result.append(" (id: ");
		result.append(id);
		result.append(')');
		return result.toString();
	}

} // PatchSetImpl

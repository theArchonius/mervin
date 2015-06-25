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

import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

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
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getNewInvolvedModels
 * <em>New Involved Models</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getNewInvolvedDiagrams
 * <em>New Involved Diagrams</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getOldInvolvedModels
 * <em>Old Involved Models</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getOldInvolvedDiagrams
 * <em>Old Involved Diagrams</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getModelComparison
 * <em>Model Comparison</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getDiagramComparison
 * <em>Diagram Comparison</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getObjectChangeCount
 * <em>Object Change Count</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getObjectChangeRefCount
 * <em>Object Change Ref Count</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getMaxObjectChangeCount
 * <em>Max Object Change Count</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl#getMaxObjectChangeRefCount
 * <em>Max Object Change Ref Count</em>}</li>
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
	 * The cached value of the '{@link #getNewInvolvedModels()
	 * <em>New Involved Models</em>}' reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getNewInvolvedModels()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelInstance> newInvolvedModels;

	/**
	 * The cached value of the '{@link #getNewInvolvedDiagrams()
	 * <em>New Involved Diagrams</em>}' reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getNewInvolvedDiagrams()
	 * @generated
	 * @ordered
	 */
	protected EList<DiagramInstance> newInvolvedDiagrams;

	/**
	 * The cached value of the '{@link #getOldInvolvedModels()
	 * <em>Old Involved Models</em>}' reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getOldInvolvedModels()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelInstance> oldInvolvedModels;

	/**
	 * The cached value of the '{@link #getOldInvolvedDiagrams()
	 * <em>Old Involved Diagrams</em>}' reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getOldInvolvedDiagrams()
	 * @generated
	 * @ordered
	 */
	protected EList<DiagramInstance> oldInvolvedDiagrams;

	/**
	 * The cached value of the '{@link #getModelComparison()
	 * <em>Model Comparison</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getModelComparison()
	 * @generated
	 * @ordered
	 */
	protected Comparison modelComparison;

	/**
	 * The cached value of the '{@link #getDiagramComparison()
	 * <em>Diagram Comparison</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getDiagramComparison()
	 * @generated
	 * @ordered
	 */
	protected Comparison diagramComparison;

	/**
	 * The cached value of the '{@link #getObjectChangeCount()
	 * <em>Object Change Count</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getObjectChangeCount()
	 * @generated
	 * @ordered
	 */
	protected Map<EObject, Integer> objectChangeCount;

	/**
	 * This is true if the Object Change Count attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean objectChangeCountESet;

	/**
	 * The cached value of the '{@link #getObjectChangeRefCount()
	 * <em>Object Change Ref Count</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getObjectChangeRefCount()
	 * @generated
	 * @ordered
	 */
	protected Map<EObject, Integer> objectChangeRefCount;

	/**
	 * This is true if the Object Change Ref Count attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean objectChangeRefCountESet;

	/**
	 * The default value of the '{@link #getMaxObjectChangeCount()
	 * <em>Max Object Change Count</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getMaxObjectChangeCount()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_OBJECT_CHANGE_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxObjectChangeCount()
	 * <em>Max Object Change Count</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getMaxObjectChangeCount()
	 * @generated
	 * @ordered
	 */
	protected int maxObjectChangeCount = MAX_OBJECT_CHANGE_COUNT_EDEFAULT;

	/**
	 * This is true if the Max Object Change Count attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean maxObjectChangeCountESet;

	/**
	 * The default value of the '{@link #getMaxObjectChangeRefCount()
	 * <em>Max Object Change Ref Count</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getMaxObjectChangeRefCount()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_OBJECT_CHANGE_REF_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxObjectChangeRefCount()
	 * <em>Max Object Change Ref Count</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getMaxObjectChangeRefCount()
	 * @generated
	 * @ordered
	 */
	protected int maxObjectChangeRefCount = MAX_OBJECT_CHANGE_REF_COUNT_EDEFAULT;

	/**
	 * This is true if the Max Object Change Ref Count attribute has been set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean maxObjectChangeRefCountESet;

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
			patches = new EObjectWithInverseResolvingEList<Patch>(Patch.class,
					this, ModelReviewPackage.PATCH_SET__PATCHES,
					ModelReviewPackage.PATCH__PATCH_SET);
		}
		return patches;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<ModelInstance> getNewInvolvedModels() {
		if (newInvolvedModels == null) {
			newInvolvedModels = new EObjectResolvingEList<ModelInstance>(
					ModelInstance.class, this,
					ModelReviewPackage.PATCH_SET__NEW_INVOLVED_MODELS);
		}
		return newInvolvedModels;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<DiagramInstance> getNewInvolvedDiagrams() {
		if (newInvolvedDiagrams == null) {
			newInvolvedDiagrams = new EObjectResolvingEList<DiagramInstance>(
					DiagramInstance.class, this,
					ModelReviewPackage.PATCH_SET__NEW_INVOLVED_DIAGRAMS);
		}
		return newInvolvedDiagrams;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<ModelInstance> getOldInvolvedModels() {
		if (oldInvolvedModels == null) {
			oldInvolvedModels = new EObjectResolvingEList<ModelInstance>(
					ModelInstance.class, this,
					ModelReviewPackage.PATCH_SET__OLD_INVOLVED_MODELS);
		}
		return oldInvolvedModels;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<DiagramInstance> getOldInvolvedDiagrams() {
		if (oldInvolvedDiagrams == null) {
			oldInvolvedDiagrams = new EObjectResolvingEList<DiagramInstance>(
					DiagramInstance.class, this,
					ModelReviewPackage.PATCH_SET__OLD_INVOLVED_DIAGRAMS);
		}
		return oldInvolvedDiagrams;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Comparison getModelComparison() {
		if (modelComparison != null && modelComparison.eIsProxy()) {
			InternalEObject oldModelComparison = (InternalEObject) modelComparison;
			modelComparison = (Comparison) eResolveProxy(oldModelComparison);
			if (modelComparison != oldModelComparison) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.PATCH_SET__MODEL_COMPARISON,
							oldModelComparison, modelComparison));
			}
		}
		return modelComparison;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Comparison basicGetModelComparison() {
		return modelComparison;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setModelComparison(Comparison newModelComparison) {
		Comparison oldModelComparison = modelComparison;
		modelComparison = newModelComparison;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.PATCH_SET__MODEL_COMPARISON,
					oldModelComparison, modelComparison));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Comparison getDiagramComparison() {
		if (diagramComparison != null && diagramComparison.eIsProxy()) {
			InternalEObject oldDiagramComparison = (InternalEObject) diagramComparison;
			diagramComparison = (Comparison) eResolveProxy(oldDiagramComparison);
			if (diagramComparison != oldDiagramComparison) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.PATCH_SET__DIAGRAM_COMPARISON,
							oldDiagramComparison, diagramComparison));
			}
		}
		return diagramComparison;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Comparison basicGetDiagramComparison() {
		return diagramComparison;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDiagramComparison(Comparison newDiagramComparison) {
		Comparison oldDiagramComparison = diagramComparison;
		diagramComparison = newDiagramComparison;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.PATCH_SET__DIAGRAM_COMPARISON,
					oldDiagramComparison, diagramComparison));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Map<EObject, Integer> getObjectChangeCount() {
		return objectChangeCount;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetObjectChangeCount() {
		return objectChangeCountESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Map<EObject, Integer> getObjectChangeRefCount() {
		return objectChangeRefCount;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetObjectChangeRefCount() {
		return objectChangeRefCountESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getMaxObjectChangeCount() {
		return maxObjectChangeCount;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetMaxObjectChangeCount() {
		return maxObjectChangeCountESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getMaxObjectChangeRefCount() {
		return maxObjectChangeRefCount;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetMaxObjectChangeRefCount() {
		return maxObjectChangeRefCountESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelReviewPackage.PATCH_SET__REVIEW:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetReview((ModelReview) otherEnd, msgs);
		case ModelReviewPackage.PATCH_SET__PATCHES:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getPatches())
					.basicAdd(otherEnd, msgs);
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
		case ModelReviewPackage.PATCH_SET__PATCHES:
			return ((InternalEList<?>) getPatches())
					.basicRemove(otherEnd, msgs);
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
		case ModelReviewPackage.PATCH_SET__NEW_INVOLVED_MODELS:
			return getNewInvolvedModels();
		case ModelReviewPackage.PATCH_SET__NEW_INVOLVED_DIAGRAMS:
			return getNewInvolvedDiagrams();
		case ModelReviewPackage.PATCH_SET__OLD_INVOLVED_MODELS:
			return getOldInvolvedModels();
		case ModelReviewPackage.PATCH_SET__OLD_INVOLVED_DIAGRAMS:
			return getOldInvolvedDiagrams();
		case ModelReviewPackage.PATCH_SET__MODEL_COMPARISON:
			if (resolve)
				return getModelComparison();
			return basicGetModelComparison();
		case ModelReviewPackage.PATCH_SET__DIAGRAM_COMPARISON:
			if (resolve)
				return getDiagramComparison();
			return basicGetDiagramComparison();
		case ModelReviewPackage.PATCH_SET__OBJECT_CHANGE_COUNT:
			return getObjectChangeCount();
		case ModelReviewPackage.PATCH_SET__OBJECT_CHANGE_REF_COUNT:
			return getObjectChangeRefCount();
		case ModelReviewPackage.PATCH_SET__MAX_OBJECT_CHANGE_COUNT:
			return getMaxObjectChangeCount();
		case ModelReviewPackage.PATCH_SET__MAX_OBJECT_CHANGE_REF_COUNT:
			return getMaxObjectChangeRefCount();
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
		case ModelReviewPackage.PATCH_SET__NEW_INVOLVED_MODELS:
			getNewInvolvedModels().clear();
			getNewInvolvedModels().addAll(
					(Collection<? extends ModelInstance>) newValue);
			return;
		case ModelReviewPackage.PATCH_SET__NEW_INVOLVED_DIAGRAMS:
			getNewInvolvedDiagrams().clear();
			getNewInvolvedDiagrams().addAll(
					(Collection<? extends DiagramInstance>) newValue);
			return;
		case ModelReviewPackage.PATCH_SET__OLD_INVOLVED_MODELS:
			getOldInvolvedModels().clear();
			getOldInvolvedModels().addAll(
					(Collection<? extends ModelInstance>) newValue);
			return;
		case ModelReviewPackage.PATCH_SET__OLD_INVOLVED_DIAGRAMS:
			getOldInvolvedDiagrams().clear();
			getOldInvolvedDiagrams().addAll(
					(Collection<? extends DiagramInstance>) newValue);
			return;
		case ModelReviewPackage.PATCH_SET__MODEL_COMPARISON:
			setModelComparison((Comparison) newValue);
			return;
		case ModelReviewPackage.PATCH_SET__DIAGRAM_COMPARISON:
			setDiagramComparison((Comparison) newValue);
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
		case ModelReviewPackage.PATCH_SET__NEW_INVOLVED_MODELS:
			getNewInvolvedModels().clear();
			return;
		case ModelReviewPackage.PATCH_SET__NEW_INVOLVED_DIAGRAMS:
			getNewInvolvedDiagrams().clear();
			return;
		case ModelReviewPackage.PATCH_SET__OLD_INVOLVED_MODELS:
			getOldInvolvedModels().clear();
			return;
		case ModelReviewPackage.PATCH_SET__OLD_INVOLVED_DIAGRAMS:
			getOldInvolvedDiagrams().clear();
			return;
		case ModelReviewPackage.PATCH_SET__MODEL_COMPARISON:
			setModelComparison((Comparison) null);
			return;
		case ModelReviewPackage.PATCH_SET__DIAGRAM_COMPARISON:
			setDiagramComparison((Comparison) null);
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
		case ModelReviewPackage.PATCH_SET__NEW_INVOLVED_MODELS:
			return newInvolvedModels != null && !newInvolvedModels.isEmpty();
		case ModelReviewPackage.PATCH_SET__NEW_INVOLVED_DIAGRAMS:
			return newInvolvedDiagrams != null
					&& !newInvolvedDiagrams.isEmpty();
		case ModelReviewPackage.PATCH_SET__OLD_INVOLVED_MODELS:
			return oldInvolvedModels != null && !oldInvolvedModels.isEmpty();
		case ModelReviewPackage.PATCH_SET__OLD_INVOLVED_DIAGRAMS:
			return oldInvolvedDiagrams != null
					&& !oldInvolvedDiagrams.isEmpty();
		case ModelReviewPackage.PATCH_SET__MODEL_COMPARISON:
			return modelComparison != null;
		case ModelReviewPackage.PATCH_SET__DIAGRAM_COMPARISON:
			return diagramComparison != null;
		case ModelReviewPackage.PATCH_SET__OBJECT_CHANGE_COUNT:
			return isSetObjectChangeCount();
		case ModelReviewPackage.PATCH_SET__OBJECT_CHANGE_REF_COUNT:
			return isSetObjectChangeRefCount();
		case ModelReviewPackage.PATCH_SET__MAX_OBJECT_CHANGE_COUNT:
			return isSetMaxObjectChangeCount();
		case ModelReviewPackage.PATCH_SET__MAX_OBJECT_CHANGE_REF_COUNT:
			return isSetMaxObjectChangeRefCount();
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
		result.append(", objectChangeCount: ");
		if (objectChangeCountESet)
			result.append(objectChangeCount);
		else
			result.append("<unset>");
		result.append(", objectChangeRefCount: ");
		if (objectChangeRefCountESet)
			result.append(objectChangeRefCount);
		else
			result.append("<unset>");
		result.append(", maxObjectChangeCount: ");
		if (maxObjectChangeCountESet)
			result.append(maxObjectChangeCount);
		else
			result.append("<unset>");
		result.append(", maxObjectChangeRefCount: ");
		if (maxObjectChangeRefCountESet)
			result.append(maxObjectChangeRefCount);
		else
			result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // PatchSetImpl

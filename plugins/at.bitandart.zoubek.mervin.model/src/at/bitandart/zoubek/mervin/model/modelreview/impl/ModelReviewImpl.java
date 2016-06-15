/**
 * ******************************************************************************
 *  Copyright (c) 2015, 2016 Florian Zoubek.
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

import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import com.google.common.collect.HashBiMap;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Model Review</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl#getId
 * <em>Id</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl#getPatchSets
 * <em>Patch Sets</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl#getComments
 * <em>Comments</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl#getLeftPatchSet
 * <em>Left Patch Set</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl#getRightPatchSet
 * <em>Right Patch Set</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl#getSelectedModelComparison
 * <em>Selected Model Comparison</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl#getSelectedDiagramComparison
 * <em>Selected Diagram Comparison</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl#isShowAdditions
 * <em>Show Additions</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl#isShowModifications
 * <em>Show Modifications</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl#isShowDeletions
 * <em>Show Deletions</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl#isShowLayoutChanges
 * <em>Show Layout Changes</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl#getUnifiedModelMap
 * <em>Unified Model Map</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModelReviewImpl extends MinimalEObjectImpl.Container implements ModelReview {
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
	 * The cached value of the '{@link #getPatchSets() <em>Patch Sets</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPatchSets()
	 * @generated
	 * @ordered
	 */
	protected EList<PatchSet> patchSets;

	/**
	 * The cached value of the '{@link #getComments() <em>Comments</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getComments()
	 * @generated
	 * @ordered
	 */
	protected EList<Comment> comments;

	/**
	 * The cached value of the '{@link #getLeftPatchSet()
	 * <em>Left Patch Set</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getLeftPatchSet()
	 * @generated
	 * @ordered
	 */
	protected PatchSet leftPatchSet;

	/**
	 * The cached value of the '{@link #getRightPatchSet()
	 * <em>Right Patch Set</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getRightPatchSet()
	 * @generated
	 * @ordered
	 */
	protected PatchSet rightPatchSet;

	/**
	 * The cached value of the '{@link #getSelectedModelComparison()
	 * <em>Selected Model Comparison</em>}' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getSelectedModelComparison()
	 * @generated
	 * @ordered
	 */
	protected Comparison selectedModelComparison;

	/**
	 * The cached value of the '{@link #getSelectedDiagramComparison()
	 * <em>Selected Diagram Comparison</em>}' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getSelectedDiagramComparison()
	 * @generated
	 * @ordered
	 */
	protected Comparison selectedDiagramComparison;

	/**
	 * The default value of the '{@link #isShowAdditions()
	 * <em>Show Additions</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isShowAdditions()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_ADDITIONS_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isShowAdditions()
	 * <em>Show Additions</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isShowAdditions()
	 * @generated
	 * @ordered
	 */
	protected boolean showAdditions = SHOW_ADDITIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #isShowModifications()
	 * <em>Show Modifications</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isShowModifications()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_MODIFICATIONS_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isShowModifications()
	 * <em>Show Modifications</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isShowModifications()
	 * @generated
	 * @ordered
	 */
	protected boolean showModifications = SHOW_MODIFICATIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #isShowDeletions()
	 * <em>Show Deletions</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isShowDeletions()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_DELETIONS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isShowDeletions()
	 * <em>Show Deletions</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isShowDeletions()
	 * @generated
	 * @ordered
	 */
	protected boolean showDeletions = SHOW_DELETIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #isShowLayoutChanges()
	 * <em>Show Layout Changes</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isShowLayoutChanges()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_LAYOUT_CHANGES_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isShowLayoutChanges()
	 * <em>Show Layout Changes</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isShowLayoutChanges()
	 * @generated
	 * @ordered
	 */
	protected boolean showLayoutChanges = SHOW_LAYOUT_CHANGES_EDEFAULT;

	/**
	 * The default value of the '{@link #getUnifiedModelMap()
	 * <em>Unified Model Map</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getUnifiedModelMap()
	 * @generated
	 * @ordered
	 */
	protected static final HashBiMap<Object, Object> UNIFIED_MODEL_MAP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUnifiedModelMap()
	 * <em>Unified Model Map</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getUnifiedModelMap()
	 * @generated
	 * @ordered
	 */
	protected HashBiMap<Object, Object> unifiedModelMap = UNIFIED_MODEL_MAP_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ModelReviewImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.MODEL_REVIEW;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.MODEL_REVIEW__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<PatchSet> getPatchSets() {
		if (patchSets == null) {
			patchSets = new EObjectContainmentWithInverseEList<PatchSet>(PatchSet.class, this,
					ModelReviewPackage.MODEL_REVIEW__PATCH_SETS, ModelReviewPackage.PATCH_SET__REVIEW);
		}
		return patchSets;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Comment> getComments() {
		if (comments == null) {
			comments = new EObjectResolvingEList<Comment>(Comment.class, this,
					ModelReviewPackage.MODEL_REVIEW__COMMENTS);
		}
		return comments;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchSet getLeftPatchSet() {
		if (leftPatchSet != null && leftPatchSet.eIsProxy()) {
			InternalEObject oldLeftPatchSet = (InternalEObject) leftPatchSet;
			leftPatchSet = (PatchSet) eResolveProxy(oldLeftPatchSet);
			if (leftPatchSet != oldLeftPatchSet) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.MODEL_REVIEW__LEFT_PATCH_SET, oldLeftPatchSet, leftPatchSet));
			}
		}
		return leftPatchSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchSet basicGetLeftPatchSet() {
		return leftPatchSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLeftPatchSet(PatchSet newLeftPatchSet) {
		PatchSet oldLeftPatchSet = leftPatchSet;
		leftPatchSet = newLeftPatchSet;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.MODEL_REVIEW__LEFT_PATCH_SET,
					oldLeftPatchSet, leftPatchSet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchSet getRightPatchSet() {
		if (rightPatchSet != null && rightPatchSet.eIsProxy()) {
			InternalEObject oldRightPatchSet = (InternalEObject) rightPatchSet;
			rightPatchSet = (PatchSet) eResolveProxy(oldRightPatchSet);
			if (rightPatchSet != oldRightPatchSet) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.MODEL_REVIEW__RIGHT_PATCH_SET, oldRightPatchSet, rightPatchSet));
			}
		}
		return rightPatchSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchSet basicGetRightPatchSet() {
		return rightPatchSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRightPatchSet(PatchSet newRightPatchSet) {
		PatchSet oldRightPatchSet = rightPatchSet;
		rightPatchSet = newRightPatchSet;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.MODEL_REVIEW__RIGHT_PATCH_SET,
					oldRightPatchSet, rightPatchSet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Comparison getSelectedModelComparison() {
		if (selectedModelComparison != null && selectedModelComparison.eIsProxy()) {
			InternalEObject oldSelectedModelComparison = (InternalEObject) selectedModelComparison;
			selectedModelComparison = (Comparison) eResolveProxy(oldSelectedModelComparison);
			if (selectedModelComparison != oldSelectedModelComparison) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.MODEL_REVIEW__SELECTED_MODEL_COMPARISON, oldSelectedModelComparison,
							selectedModelComparison));
			}
		}
		return selectedModelComparison;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Comparison basicGetSelectedModelComparison() {
		return selectedModelComparison;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSelectedModelComparison(Comparison newSelectedModelComparison) {
		Comparison oldSelectedModelComparison = selectedModelComparison;
		selectedModelComparison = newSelectedModelComparison;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.MODEL_REVIEW__SELECTED_MODEL_COMPARISON, oldSelectedModelComparison,
					selectedModelComparison));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Comparison getSelectedDiagramComparison() {
		if (selectedDiagramComparison != null && selectedDiagramComparison.eIsProxy()) {
			InternalEObject oldSelectedDiagramComparison = (InternalEObject) selectedDiagramComparison;
			selectedDiagramComparison = (Comparison) eResolveProxy(oldSelectedDiagramComparison);
			if (selectedDiagramComparison != oldSelectedDiagramComparison) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							ModelReviewPackage.MODEL_REVIEW__SELECTED_DIAGRAM_COMPARISON, oldSelectedDiagramComparison,
							selectedDiagramComparison));
			}
		}
		return selectedDiagramComparison;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Comparison basicGetSelectedDiagramComparison() {
		return selectedDiagramComparison;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSelectedDiagramComparison(Comparison newSelectedDiagramComparison) {
		Comparison oldSelectedDiagramComparison = selectedDiagramComparison;
		selectedDiagramComparison = newSelectedDiagramComparison;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.MODEL_REVIEW__SELECTED_DIAGRAM_COMPARISON, oldSelectedDiagramComparison,
					selectedDiagramComparison));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isShowAdditions() {
		return showAdditions;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setShowAdditions(boolean newShowAdditions) {
		boolean oldShowAdditions = showAdditions;
		showAdditions = newShowAdditions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.MODEL_REVIEW__SHOW_ADDITIONS,
					oldShowAdditions, showAdditions));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isShowModifications() {
		return showModifications;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setShowModifications(boolean newShowModifications) {
		boolean oldShowModifications = showModifications;
		showModifications = newShowModifications;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.MODEL_REVIEW__SHOW_MODIFICATIONS,
					oldShowModifications, showModifications));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isShowDeletions() {
		return showDeletions;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setShowDeletions(boolean newShowDeletions) {
		boolean oldShowDeletions = showDeletions;
		showDeletions = newShowDeletions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.MODEL_REVIEW__SHOW_DELETIONS,
					oldShowDeletions, showDeletions));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isShowLayoutChanges() {
		return showLayoutChanges;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setShowLayoutChanges(boolean newShowLayoutChanges) {
		boolean oldShowLayoutChanges = showLayoutChanges;
		showLayoutChanges = newShowLayoutChanges;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.MODEL_REVIEW__SHOW_LAYOUT_CHANGES,
					oldShowLayoutChanges, showLayoutChanges));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public HashBiMap<Object, Object> getUnifiedModelMap() {
		return unifiedModelMap;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setUnifiedModelMap(HashBiMap<Object, Object> newUnifiedModelMap) {
		HashBiMap<Object, Object> oldUnifiedModelMap = unifiedModelMap;
		unifiedModelMap = newUnifiedModelMap;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.MODEL_REVIEW__UNIFIED_MODEL_MAP,
					oldUnifiedModelMap, unifiedModelMap));
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
		case ModelReviewPackage.MODEL_REVIEW__PATCH_SETS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getPatchSets()).basicAdd(otherEnd, msgs);
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
		case ModelReviewPackage.MODEL_REVIEW__PATCH_SETS:
			return ((InternalEList<?>) getPatchSets()).basicRemove(otherEnd, msgs);
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
		case ModelReviewPackage.MODEL_REVIEW__ID:
			return getId();
		case ModelReviewPackage.MODEL_REVIEW__PATCH_SETS:
			return getPatchSets();
		case ModelReviewPackage.MODEL_REVIEW__COMMENTS:
			return getComments();
		case ModelReviewPackage.MODEL_REVIEW__LEFT_PATCH_SET:
			if (resolve)
				return getLeftPatchSet();
			return basicGetLeftPatchSet();
		case ModelReviewPackage.MODEL_REVIEW__RIGHT_PATCH_SET:
			if (resolve)
				return getRightPatchSet();
			return basicGetRightPatchSet();
		case ModelReviewPackage.MODEL_REVIEW__SELECTED_MODEL_COMPARISON:
			if (resolve)
				return getSelectedModelComparison();
			return basicGetSelectedModelComparison();
		case ModelReviewPackage.MODEL_REVIEW__SELECTED_DIAGRAM_COMPARISON:
			if (resolve)
				return getSelectedDiagramComparison();
			return basicGetSelectedDiagramComparison();
		case ModelReviewPackage.MODEL_REVIEW__SHOW_ADDITIONS:
			return isShowAdditions();
		case ModelReviewPackage.MODEL_REVIEW__SHOW_MODIFICATIONS:
			return isShowModifications();
		case ModelReviewPackage.MODEL_REVIEW__SHOW_DELETIONS:
			return isShowDeletions();
		case ModelReviewPackage.MODEL_REVIEW__SHOW_LAYOUT_CHANGES:
			return isShowLayoutChanges();
		case ModelReviewPackage.MODEL_REVIEW__UNIFIED_MODEL_MAP:
			return getUnifiedModelMap();
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
		case ModelReviewPackage.MODEL_REVIEW__ID:
			setId((String) newValue);
			return;
		case ModelReviewPackage.MODEL_REVIEW__PATCH_SETS:
			getPatchSets().clear();
			getPatchSets().addAll((Collection<? extends PatchSet>) newValue);
			return;
		case ModelReviewPackage.MODEL_REVIEW__COMMENTS:
			getComments().clear();
			getComments().addAll((Collection<? extends Comment>) newValue);
			return;
		case ModelReviewPackage.MODEL_REVIEW__LEFT_PATCH_SET:
			setLeftPatchSet((PatchSet) newValue);
			return;
		case ModelReviewPackage.MODEL_REVIEW__RIGHT_PATCH_SET:
			setRightPatchSet((PatchSet) newValue);
			return;
		case ModelReviewPackage.MODEL_REVIEW__SELECTED_MODEL_COMPARISON:
			setSelectedModelComparison((Comparison) newValue);
			return;
		case ModelReviewPackage.MODEL_REVIEW__SELECTED_DIAGRAM_COMPARISON:
			setSelectedDiagramComparison((Comparison) newValue);
			return;
		case ModelReviewPackage.MODEL_REVIEW__SHOW_ADDITIONS:
			setShowAdditions((Boolean) newValue);
			return;
		case ModelReviewPackage.MODEL_REVIEW__SHOW_MODIFICATIONS:
			setShowModifications((Boolean) newValue);
			return;
		case ModelReviewPackage.MODEL_REVIEW__SHOW_DELETIONS:
			setShowDeletions((Boolean) newValue);
			return;
		case ModelReviewPackage.MODEL_REVIEW__SHOW_LAYOUT_CHANGES:
			setShowLayoutChanges((Boolean) newValue);
			return;
		case ModelReviewPackage.MODEL_REVIEW__UNIFIED_MODEL_MAP:
			setUnifiedModelMap((HashBiMap<Object, Object>) newValue);
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
		case ModelReviewPackage.MODEL_REVIEW__ID:
			setId(ID_EDEFAULT);
			return;
		case ModelReviewPackage.MODEL_REVIEW__PATCH_SETS:
			getPatchSets().clear();
			return;
		case ModelReviewPackage.MODEL_REVIEW__COMMENTS:
			getComments().clear();
			return;
		case ModelReviewPackage.MODEL_REVIEW__LEFT_PATCH_SET:
			setLeftPatchSet((PatchSet) null);
			return;
		case ModelReviewPackage.MODEL_REVIEW__RIGHT_PATCH_SET:
			setRightPatchSet((PatchSet) null);
			return;
		case ModelReviewPackage.MODEL_REVIEW__SELECTED_MODEL_COMPARISON:
			setSelectedModelComparison((Comparison) null);
			return;
		case ModelReviewPackage.MODEL_REVIEW__SELECTED_DIAGRAM_COMPARISON:
			setSelectedDiagramComparison((Comparison) null);
			return;
		case ModelReviewPackage.MODEL_REVIEW__SHOW_ADDITIONS:
			setShowAdditions(SHOW_ADDITIONS_EDEFAULT);
			return;
		case ModelReviewPackage.MODEL_REVIEW__SHOW_MODIFICATIONS:
			setShowModifications(SHOW_MODIFICATIONS_EDEFAULT);
			return;
		case ModelReviewPackage.MODEL_REVIEW__SHOW_DELETIONS:
			setShowDeletions(SHOW_DELETIONS_EDEFAULT);
			return;
		case ModelReviewPackage.MODEL_REVIEW__SHOW_LAYOUT_CHANGES:
			setShowLayoutChanges(SHOW_LAYOUT_CHANGES_EDEFAULT);
			return;
		case ModelReviewPackage.MODEL_REVIEW__UNIFIED_MODEL_MAP:
			setUnifiedModelMap(UNIFIED_MODEL_MAP_EDEFAULT);
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
		case ModelReviewPackage.MODEL_REVIEW__ID:
			return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
		case ModelReviewPackage.MODEL_REVIEW__PATCH_SETS:
			return patchSets != null && !patchSets.isEmpty();
		case ModelReviewPackage.MODEL_REVIEW__COMMENTS:
			return comments != null && !comments.isEmpty();
		case ModelReviewPackage.MODEL_REVIEW__LEFT_PATCH_SET:
			return leftPatchSet != null;
		case ModelReviewPackage.MODEL_REVIEW__RIGHT_PATCH_SET:
			return rightPatchSet != null;
		case ModelReviewPackage.MODEL_REVIEW__SELECTED_MODEL_COMPARISON:
			return selectedModelComparison != null;
		case ModelReviewPackage.MODEL_REVIEW__SELECTED_DIAGRAM_COMPARISON:
			return selectedDiagramComparison != null;
		case ModelReviewPackage.MODEL_REVIEW__SHOW_ADDITIONS:
			return showAdditions != SHOW_ADDITIONS_EDEFAULT;
		case ModelReviewPackage.MODEL_REVIEW__SHOW_MODIFICATIONS:
			return showModifications != SHOW_MODIFICATIONS_EDEFAULT;
		case ModelReviewPackage.MODEL_REVIEW__SHOW_DELETIONS:
			return showDeletions != SHOW_DELETIONS_EDEFAULT;
		case ModelReviewPackage.MODEL_REVIEW__SHOW_LAYOUT_CHANGES:
			return showLayoutChanges != SHOW_LAYOUT_CHANGES_EDEFAULT;
		case ModelReviewPackage.MODEL_REVIEW__UNIFIED_MODEL_MAP:
			return UNIFIED_MODEL_MAP_EDEFAULT == null ? unifiedModelMap != null
					: !UNIFIED_MODEL_MAP_EDEFAULT.equals(unifiedModelMap);
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
		result.append(", showAdditions: ");
		result.append(showAdditions);
		result.append(", showModifications: ");
		result.append(showModifications);
		result.append(", showDeletions: ");
		result.append(showDeletions);
		result.append(", showLayoutChanges: ");
		result.append(showLayoutChanges);
		result.append(", unifiedModelMap: ");
		result.append(unifiedModelMap);
		result.append(')');
		return result.toString();
	}

} // ModelReviewImpl

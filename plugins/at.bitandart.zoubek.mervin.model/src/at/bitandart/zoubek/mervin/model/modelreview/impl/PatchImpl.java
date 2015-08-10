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

import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.Patch;

import at.bitandart.zoubek.mervin.model.modelreview.PatchChangeType;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Patch</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchImpl#getNewPath
 * <em>New Path</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchImpl#getOldPath
 * <em>Old Path</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchImpl#getNewContent
 * <em>New Content</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchImpl#getOldContent
 * <em>Old Content</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchImpl#getChangeType
 * <em>Change Type</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchImpl#getPatchSet
 * <em>Patch Set</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PatchImpl extends MinimalEObjectImpl.Container implements Patch {
	/**
	 * The default value of the '{@link #getNewPath() <em>New Path</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getNewPath()
	 * @generated
	 * @ordered
	 */
	protected static final String NEW_PATH_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getNewPath() <em>New Path</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getNewPath()
	 * @generated
	 * @ordered
	 */
	protected String newPath = NEW_PATH_EDEFAULT;
	/**
	 * The default value of the '{@link #getOldPath() <em>Old Path</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOldPath()
	 * @generated
	 * @ordered
	 */
	protected static final String OLD_PATH_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getOldPath() <em>Old Path</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOldPath()
	 * @generated
	 * @ordered
	 */
	protected String oldPath = OLD_PATH_EDEFAULT;
	/**
	 * The default value of the '{@link #getNewContent() <em>New Content</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getNewContent()
	 * @generated
	 * @ordered
	 */
	protected static final byte[] NEW_CONTENT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getNewContent() <em>New Content</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getNewContent()
	 * @generated
	 * @ordered
	 */
	protected byte[] newContent = NEW_CONTENT_EDEFAULT;
	/**
	 * The default value of the '{@link #getOldContent() <em>Old Content</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOldContent()
	 * @generated
	 * @ordered
	 */
	protected static final byte[] OLD_CONTENT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getOldContent() <em>Old Content</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOldContent()
	 * @generated
	 * @ordered
	 */
	protected byte[] oldContent = OLD_CONTENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getChangeType() <em>Change Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getChangeType()
	 * @generated
	 * @ordered
	 */
	protected static final PatchChangeType CHANGE_TYPE_EDEFAULT = PatchChangeType.ADD;
	/**
	 * The cached value of the '{@link #getChangeType() <em>Change Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getChangeType()
	 * @generated
	 * @ordered
	 */
	protected PatchChangeType changeType = CHANGE_TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPatchSet() <em>Patch Set</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPatchSet()
	 * @generated
	 * @ordered
	 */
	protected PatchSet patchSet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PatchImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.PATCH;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getNewPath() {
		return newPath;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNewPath(String newNewPath) {
		String oldNewPath = newPath;
		newPath = newNewPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.PATCH__NEW_PATH, oldNewPath,
					newPath));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getOldPath() {
		return oldPath;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOldPath(String newOldPath) {
		String oldOldPath = oldPath;
		oldPath = newOldPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.PATCH__OLD_PATH, oldOldPath,
					oldPath));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public byte[] getNewContent() {
		return newContent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNewContent(byte[] newNewContent) {
		byte[] oldNewContent = newContent;
		newContent = newNewContent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.PATCH__NEW_CONTENT, oldNewContent,
					newContent));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public byte[] getOldContent() {
		return oldContent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOldContent(byte[] newOldContent) {
		byte[] oldOldContent = oldContent;
		oldContent = newOldContent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.PATCH__OLD_CONTENT, oldOldContent,
					oldContent));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchChangeType getChangeType() {
		return changeType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setChangeType(PatchChangeType newChangeType) {
		PatchChangeType oldChangeType = changeType;
		changeType = newChangeType == null ? CHANGE_TYPE_EDEFAULT : newChangeType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.PATCH__CHANGE_TYPE, oldChangeType,
					changeType));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchSet getPatchSet() {
		if (patchSet != null && patchSet.eIsProxy()) {
			InternalEObject oldPatchSet = (InternalEObject) patchSet;
			patchSet = (PatchSet) eResolveProxy(oldPatchSet);
			if (patchSet != oldPatchSet) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelReviewPackage.PATCH__PATCH_SET,
							oldPatchSet, patchSet));
			}
		}
		return patchSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchSet basicGetPatchSet() {
		return patchSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetPatchSet(PatchSet newPatchSet, NotificationChain msgs) {
		PatchSet oldPatchSet = patchSet;
		patchSet = newPatchSet;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.PATCH__PATCH_SET, oldPatchSet, newPatchSet);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setPatchSet(PatchSet newPatchSet) {
		if (newPatchSet != patchSet) {
			NotificationChain msgs = null;
			if (patchSet != null)
				msgs = ((InternalEObject) patchSet).eInverseRemove(this, ModelReviewPackage.PATCH_SET__PATCHES,
						PatchSet.class, msgs);
			if (newPatchSet != null)
				msgs = ((InternalEObject) newPatchSet).eInverseAdd(this, ModelReviewPackage.PATCH_SET__PATCHES,
						PatchSet.class, msgs);
			msgs = basicSetPatchSet(newPatchSet, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.PATCH__PATCH_SET, newPatchSet,
					newPatchSet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelReviewPackage.PATCH__PATCH_SET:
			if (patchSet != null)
				msgs = ((InternalEObject) patchSet).eInverseRemove(this, ModelReviewPackage.PATCH_SET__PATCHES,
						PatchSet.class, msgs);
			return basicSetPatchSet((PatchSet) otherEnd, msgs);
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
		case ModelReviewPackage.PATCH__PATCH_SET:
			return basicSetPatchSet(null, msgs);
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
		case ModelReviewPackage.PATCH__NEW_PATH:
			return getNewPath();
		case ModelReviewPackage.PATCH__OLD_PATH:
			return getOldPath();
		case ModelReviewPackage.PATCH__NEW_CONTENT:
			return getNewContent();
		case ModelReviewPackage.PATCH__OLD_CONTENT:
			return getOldContent();
		case ModelReviewPackage.PATCH__CHANGE_TYPE:
			return getChangeType();
		case ModelReviewPackage.PATCH__PATCH_SET:
			if (resolve)
				return getPatchSet();
			return basicGetPatchSet();
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
		case ModelReviewPackage.PATCH__NEW_PATH:
			setNewPath((String) newValue);
			return;
		case ModelReviewPackage.PATCH__OLD_PATH:
			setOldPath((String) newValue);
			return;
		case ModelReviewPackage.PATCH__NEW_CONTENT:
			setNewContent((byte[]) newValue);
			return;
		case ModelReviewPackage.PATCH__OLD_CONTENT:
			setOldContent((byte[]) newValue);
			return;
		case ModelReviewPackage.PATCH__CHANGE_TYPE:
			setChangeType((PatchChangeType) newValue);
			return;
		case ModelReviewPackage.PATCH__PATCH_SET:
			setPatchSet((PatchSet) newValue);
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
		case ModelReviewPackage.PATCH__NEW_PATH:
			setNewPath(NEW_PATH_EDEFAULT);
			return;
		case ModelReviewPackage.PATCH__OLD_PATH:
			setOldPath(OLD_PATH_EDEFAULT);
			return;
		case ModelReviewPackage.PATCH__NEW_CONTENT:
			setNewContent(NEW_CONTENT_EDEFAULT);
			return;
		case ModelReviewPackage.PATCH__OLD_CONTENT:
			setOldContent(OLD_CONTENT_EDEFAULT);
			return;
		case ModelReviewPackage.PATCH__CHANGE_TYPE:
			setChangeType(CHANGE_TYPE_EDEFAULT);
			return;
		case ModelReviewPackage.PATCH__PATCH_SET:
			setPatchSet((PatchSet) null);
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
		case ModelReviewPackage.PATCH__NEW_PATH:
			return NEW_PATH_EDEFAULT == null ? newPath != null : !NEW_PATH_EDEFAULT.equals(newPath);
		case ModelReviewPackage.PATCH__OLD_PATH:
			return OLD_PATH_EDEFAULT == null ? oldPath != null : !OLD_PATH_EDEFAULT.equals(oldPath);
		case ModelReviewPackage.PATCH__NEW_CONTENT:
			return NEW_CONTENT_EDEFAULT == null ? newContent != null : !NEW_CONTENT_EDEFAULT.equals(newContent);
		case ModelReviewPackage.PATCH__OLD_CONTENT:
			return OLD_CONTENT_EDEFAULT == null ? oldContent != null : !OLD_CONTENT_EDEFAULT.equals(oldContent);
		case ModelReviewPackage.PATCH__CHANGE_TYPE:
			return changeType != CHANGE_TYPE_EDEFAULT;
		case ModelReviewPackage.PATCH__PATCH_SET:
			return patchSet != null;
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
		result.append(" (newPath: ");
		result.append(newPath);
		result.append(", oldPath: ");
		result.append(oldPath);
		result.append(", newContent: ");
		result.append(newContent);
		result.append(", oldContent: ");
		result.append(oldContent);
		result.append(", changeType: ");
		result.append(changeType);
		result.append(')');
		return result.toString();
	}

} // PatchImpl

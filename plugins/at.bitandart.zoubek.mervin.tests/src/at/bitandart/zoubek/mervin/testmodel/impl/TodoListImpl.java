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
package at.bitandart.zoubek.mervin.testmodel.impl;

import at.bitandart.zoubek.mervin.testmodel.TestModelPackage;
import at.bitandart.zoubek.mervin.testmodel.TodoEntry;
import at.bitandart.zoubek.mervin.testmodel.TodoList;

import at.bitandart.zoubek.mervin.testmodel.User;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Todo List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link at.bitandart.zoubek.mervin.testmodel.impl.TodoListImpl#getEntries <em>Entries</em>}</li>
 *   <li>{@link at.bitandart.zoubek.mervin.testmodel.impl.TodoListImpl#getOwner <em>Owner</em>}</li>
 *   <li>{@link at.bitandart.zoubek.mervin.testmodel.impl.TodoListImpl#getTags <em>Tags</em>}</li>
 *   <li>{@link at.bitandart.zoubek.mervin.testmodel.impl.TodoListImpl#getAuthorizedUsers <em>Authorized Users</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TodoListImpl extends MinimalEObjectImpl.Container implements TodoList {
	/**
	 * The cached value of the '{@link #getEntries() <em>Entries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntries()
	 * @generated
	 * @ordered
	 */
	protected EList<TodoEntry> entries;

	/**
	 * The cached value of the '{@link #getOwner() <em>Owner</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwner()
	 * @generated
	 * @ordered
	 */
	protected User owner;

	/**
	 * The cached value of the '{@link #getTags() <em>Tags</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTags()
	 * @generated
	 * @ordered
	 */
	protected EList<String> tags;

	/**
	 * The cached value of the '{@link #getAuthorizedUsers() <em>Authorized Users</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthorizedUsers()
	 * @generated
	 * @ordered
	 */
	protected EList<User> authorizedUsers;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TodoListImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestModelPackage.Literals.TODO_LIST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TodoEntry> getEntries() {
		if (entries == null) {
			entries = new EObjectContainmentEList<TodoEntry>(TodoEntry.class, this, TestModelPackage.TODO_LIST__ENTRIES);
		}
		return entries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public User getOwner() {
		if (owner != null && owner.eIsProxy()) {
			InternalEObject oldOwner = (InternalEObject)owner;
			owner = (User)eResolveProxy(oldOwner);
			if (owner != oldOwner) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TestModelPackage.TODO_LIST__OWNER, oldOwner, owner));
			}
		}
		return owner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public User basicGetOwner() {
		return owner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwner(User newOwner) {
		User oldOwner = owner;
		owner = newOwner;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestModelPackage.TODO_LIST__OWNER, oldOwner, owner));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getTags() {
		if (tags == null) {
			tags = new EDataTypeUniqueEList<String>(String.class, this, TestModelPackage.TODO_LIST__TAGS);
		}
		return tags;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<User> getAuthorizedUsers() {
		if (authorizedUsers == null) {
			authorizedUsers = new EObjectResolvingEList<User>(User.class, this, TestModelPackage.TODO_LIST__AUTHORIZED_USERS);
		}
		return authorizedUsers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestModelPackage.TODO_LIST__ENTRIES:
				return ((InternalEList<?>)getEntries()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TestModelPackage.TODO_LIST__ENTRIES:
				return getEntries();
			case TestModelPackage.TODO_LIST__OWNER:
				if (resolve) return getOwner();
				return basicGetOwner();
			case TestModelPackage.TODO_LIST__TAGS:
				return getTags();
			case TestModelPackage.TODO_LIST__AUTHORIZED_USERS:
				return getAuthorizedUsers();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TestModelPackage.TODO_LIST__ENTRIES:
				getEntries().clear();
				getEntries().addAll((Collection<? extends TodoEntry>)newValue);
				return;
			case TestModelPackage.TODO_LIST__OWNER:
				setOwner((User)newValue);
				return;
			case TestModelPackage.TODO_LIST__TAGS:
				getTags().clear();
				getTags().addAll((Collection<? extends String>)newValue);
				return;
			case TestModelPackage.TODO_LIST__AUTHORIZED_USERS:
				getAuthorizedUsers().clear();
				getAuthorizedUsers().addAll((Collection<? extends User>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case TestModelPackage.TODO_LIST__ENTRIES:
				getEntries().clear();
				return;
			case TestModelPackage.TODO_LIST__OWNER:
				setOwner((User)null);
				return;
			case TestModelPackage.TODO_LIST__TAGS:
				getTags().clear();
				return;
			case TestModelPackage.TODO_LIST__AUTHORIZED_USERS:
				getAuthorizedUsers().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case TestModelPackage.TODO_LIST__ENTRIES:
				return entries != null && !entries.isEmpty();
			case TestModelPackage.TODO_LIST__OWNER:
				return owner != null;
			case TestModelPackage.TODO_LIST__TAGS:
				return tags != null && !tags.isEmpty();
			case TestModelPackage.TODO_LIST__AUTHORIZED_USERS:
				return authorizedUsers != null && !authorizedUsers.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (tags: ");
		result.append(tags);
		result.append(')');
		return result.toString();
	}

} //TodoListImpl

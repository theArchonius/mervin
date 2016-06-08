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
package at.bitandart.zoubek.mervin.testmodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Todo List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link at.bitandart.zoubek.mervin.testmodel.TodoList#getEntries <em>Entries</em>}</li>
 *   <li>{@link at.bitandart.zoubek.mervin.testmodel.TodoList#getOwner <em>Owner</em>}</li>
 *   <li>{@link at.bitandart.zoubek.mervin.testmodel.TodoList#getTags <em>Tags</em>}</li>
 *   <li>{@link at.bitandart.zoubek.mervin.testmodel.TodoList#getAuthorizedUsers <em>Authorized Users</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.testmodel.TestModelPackage#getTodoList()
 * @model
 * @generated
 */
public interface TodoList extends EObject {
	/**
	 * Returns the value of the '<em><b>Entries</b></em>' containment reference list.
	 * The list contents are of type {@link at.bitandart.zoubek.mervin.testmodel.TodoEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entries</em>' containment reference list.
	 * @see at.bitandart.zoubek.mervin.testmodel.TestModelPackage#getTodoList_Entries()
	 * @model containment="true"
	 * @generated
	 */
	EList<TodoEntry> getEntries();

	/**
	 * Returns the value of the '<em><b>Owner</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owner</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owner</em>' reference.
	 * @see #setOwner(User)
	 * @see at.bitandart.zoubek.mervin.testmodel.TestModelPackage#getTodoList_Owner()
	 * @model
	 * @generated
	 */
	User getOwner();

	/**
	 * Sets the value of the '{@link at.bitandart.zoubek.mervin.testmodel.TodoList#getOwner <em>Owner</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owner</em>' reference.
	 * @see #getOwner()
	 * @generated
	 */
	void setOwner(User value);

	/**
	 * Returns the value of the '<em><b>Tags</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tags</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tags</em>' attribute list.
	 * @see at.bitandart.zoubek.mervin.testmodel.TestModelPackage#getTodoList_Tags()
	 * @model
	 * @generated
	 */
	EList<String> getTags();

	/**
	 * Returns the value of the '<em><b>Authorized Users</b></em>' reference list.
	 * The list contents are of type {@link at.bitandart.zoubek.mervin.testmodel.User}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Authorized Users</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Authorized Users</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.testmodel.TestModelPackage#getTodoList_AuthorizedUsers()
	 * @model
	 * @generated
	 */
	EList<User> getAuthorizedUsers();

} // TodoList

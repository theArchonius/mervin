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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see at.bitandart.zoubek.mervin.testmodel.TestModelFactory
 * @model kind="package"
 * @generated
 */
public interface TestModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "testmodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://zoubek.bitandart.at/mervin/testmodel";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "at.bitandart.zoubek.mervin.testmodel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TestModelPackage eINSTANCE = at.bitandart.zoubek.mervin.testmodel.impl.TestModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link at.bitandart.zoubek.mervin.testmodel.impl.TodoListImpl <em>Todo List</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see at.bitandart.zoubek.mervin.testmodel.impl.TodoListImpl
	 * @see at.bitandart.zoubek.mervin.testmodel.impl.TestModelPackageImpl#getTodoList()
	 * @generated
	 */
	int TODO_LIST = 0;

	/**
	 * The feature id for the '<em><b>Entries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO_LIST__ENTRIES = 0;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO_LIST__OWNER = 1;

	/**
	 * The feature id for the '<em><b>Tags</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO_LIST__TAGS = 2;

	/**
	 * The feature id for the '<em><b>Authorized Users</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO_LIST__AUTHORIZED_USERS = 3;

	/**
	 * The number of structural features of the '<em>Todo List</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO_LIST_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Todo List</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO_LIST_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link at.bitandart.zoubek.mervin.testmodel.TodoEntry <em>Todo Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see at.bitandart.zoubek.mervin.testmodel.TodoEntry
	 * @see at.bitandart.zoubek.mervin.testmodel.impl.TestModelPackageImpl#getTodoEntry()
	 * @generated
	 */
	int TODO_ENTRY = 1;

	/**
	 * The feature id for the '<em><b>Completed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO_ENTRY__COMPLETED = 0;

	/**
	 * The feature id for the '<em><b>Subentries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO_ENTRY__SUBENTRIES = 1;

	/**
	 * The number of structural features of the '<em>Todo Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Todo Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link at.bitandart.zoubek.mervin.testmodel.impl.TextEntryImpl <em>Text Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see at.bitandart.zoubek.mervin.testmodel.impl.TextEntryImpl
	 * @see at.bitandart.zoubek.mervin.testmodel.impl.TestModelPackageImpl#getTextEntry()
	 * @generated
	 */
	int TEXT_ENTRY = 2;

	/**
	 * The feature id for the '<em><b>Completed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_ENTRY__COMPLETED = TODO_ENTRY__COMPLETED;

	/**
	 * The feature id for the '<em><b>Subentries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_ENTRY__SUBENTRIES = TODO_ENTRY__SUBENTRIES;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_ENTRY__TITLE = TODO_ENTRY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_ENTRY__DESCRIPTION = TODO_ENTRY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Text Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_ENTRY_FEATURE_COUNT = TODO_ENTRY_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Text Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_ENTRY_OPERATION_COUNT = TODO_ENTRY_OPERATION_COUNT + 0;


	/**
	 * The meta object id for the '{@link at.bitandart.zoubek.mervin.testmodel.impl.UserImpl <em>User</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see at.bitandart.zoubek.mervin.testmodel.impl.UserImpl
	 * @see at.bitandart.zoubek.mervin.testmodel.impl.TestModelPackageImpl#getUser()
	 * @generated
	 */
	int USER = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__NAME = 0;

	/**
	 * The feature id for the '<em><b>Avatar</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__AVATAR = 1;

	/**
	 * The number of structural features of the '<em>User</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>User</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link at.bitandart.zoubek.mervin.testmodel.impl.AvatarImpl <em>Avatar</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see at.bitandart.zoubek.mervin.testmodel.impl.AvatarImpl
	 * @see at.bitandart.zoubek.mervin.testmodel.impl.TestModelPackageImpl#getAvatar()
	 * @generated
	 */
	int AVATAR = 4;

	/**
	 * The feature id for the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVATAR__URI = 0;

	/**
	 * The number of structural features of the '<em>Avatar</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVATAR_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Avatar</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVATAR_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link at.bitandart.zoubek.mervin.testmodel.TodoList <em>Todo List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Todo List</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.TodoList
	 * @generated
	 */
	EClass getTodoList();

	/**
	 * Returns the meta object for the containment reference list '{@link at.bitandart.zoubek.mervin.testmodel.TodoList#getEntries <em>Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entries</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.TodoList#getEntries()
	 * @see #getTodoList()
	 * @generated
	 */
	EReference getTodoList_Entries();

	/**
	 * Returns the meta object for the reference '{@link at.bitandart.zoubek.mervin.testmodel.TodoList#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Owner</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.TodoList#getOwner()
	 * @see #getTodoList()
	 * @generated
	 */
	EReference getTodoList_Owner();

	/**
	 * Returns the meta object for the attribute list '{@link at.bitandart.zoubek.mervin.testmodel.TodoList#getTags <em>Tags</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Tags</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.TodoList#getTags()
	 * @see #getTodoList()
	 * @generated
	 */
	EAttribute getTodoList_Tags();

	/**
	 * Returns the meta object for the reference list '{@link at.bitandart.zoubek.mervin.testmodel.TodoList#getAuthorizedUsers <em>Authorized Users</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Authorized Users</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.TodoList#getAuthorizedUsers()
	 * @see #getTodoList()
	 * @generated
	 */
	EReference getTodoList_AuthorizedUsers();

	/**
	 * Returns the meta object for class '{@link at.bitandart.zoubek.mervin.testmodel.TodoEntry <em>Todo Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Todo Entry</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.TodoEntry
	 * @generated
	 */
	EClass getTodoEntry();

	/**
	 * Returns the meta object for the attribute '{@link at.bitandart.zoubek.mervin.testmodel.TodoEntry#isCompleted <em>Completed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Completed</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.TodoEntry#isCompleted()
	 * @see #getTodoEntry()
	 * @generated
	 */
	EAttribute getTodoEntry_Completed();

	/**
	 * Returns the meta object for the containment reference list '{@link at.bitandart.zoubek.mervin.testmodel.TodoEntry#getSubentries <em>Subentries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Subentries</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.TodoEntry#getSubentries()
	 * @see #getTodoEntry()
	 * @generated
	 */
	EReference getTodoEntry_Subentries();

	/**
	 * Returns the meta object for class '{@link at.bitandart.zoubek.mervin.testmodel.TextEntry <em>Text Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Text Entry</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.TextEntry
	 * @generated
	 */
	EClass getTextEntry();

	/**
	 * Returns the meta object for the attribute '{@link at.bitandart.zoubek.mervin.testmodel.TextEntry#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.TextEntry#getTitle()
	 * @see #getTextEntry()
	 * @generated
	 */
	EAttribute getTextEntry_Title();

	/**
	 * Returns the meta object for the attribute '{@link at.bitandart.zoubek.mervin.testmodel.TextEntry#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.TextEntry#getDescription()
	 * @see #getTextEntry()
	 * @generated
	 */
	EAttribute getTextEntry_Description();

	/**
	 * Returns the meta object for class '{@link at.bitandart.zoubek.mervin.testmodel.User <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>User</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.User
	 * @generated
	 */
	EClass getUser();

	/**
	 * Returns the meta object for the attribute '{@link at.bitandart.zoubek.mervin.testmodel.User#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.User#getName()
	 * @see #getUser()
	 * @generated
	 */
	EAttribute getUser_Name();

	/**
	 * Returns the meta object for the containment reference '{@link at.bitandart.zoubek.mervin.testmodel.User#getAvatar <em>Avatar</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Avatar</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.User#getAvatar()
	 * @see #getUser()
	 * @generated
	 */
	EReference getUser_Avatar();

	/**
	 * Returns the meta object for class '{@link at.bitandart.zoubek.mervin.testmodel.Avatar <em>Avatar</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Avatar</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.Avatar
	 * @generated
	 */
	EClass getAvatar();

	/**
	 * Returns the meta object for the attribute '{@link at.bitandart.zoubek.mervin.testmodel.Avatar#getUri <em>Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uri</em>'.
	 * @see at.bitandart.zoubek.mervin.testmodel.Avatar#getUri()
	 * @see #getAvatar()
	 * @generated
	 */
	EAttribute getAvatar_Uri();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TestModelFactory getTestModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link at.bitandart.zoubek.mervin.testmodel.impl.TodoListImpl <em>Todo List</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see at.bitandart.zoubek.mervin.testmodel.impl.TodoListImpl
		 * @see at.bitandart.zoubek.mervin.testmodel.impl.TestModelPackageImpl#getTodoList()
		 * @generated
		 */
		EClass TODO_LIST = eINSTANCE.getTodoList();

		/**
		 * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TODO_LIST__ENTRIES = eINSTANCE.getTodoList_Entries();

		/**
		 * The meta object literal for the '<em><b>Owner</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TODO_LIST__OWNER = eINSTANCE.getTodoList_Owner();

		/**
		 * The meta object literal for the '<em><b>Tags</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TODO_LIST__TAGS = eINSTANCE.getTodoList_Tags();

		/**
		 * The meta object literal for the '<em><b>Authorized Users</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TODO_LIST__AUTHORIZED_USERS = eINSTANCE.getTodoList_AuthorizedUsers();

		/**
		 * The meta object literal for the '{@link at.bitandart.zoubek.mervin.testmodel.TodoEntry <em>Todo Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see at.bitandart.zoubek.mervin.testmodel.TodoEntry
		 * @see at.bitandart.zoubek.mervin.testmodel.impl.TestModelPackageImpl#getTodoEntry()
		 * @generated
		 */
		EClass TODO_ENTRY = eINSTANCE.getTodoEntry();

		/**
		 * The meta object literal for the '<em><b>Completed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TODO_ENTRY__COMPLETED = eINSTANCE.getTodoEntry_Completed();

		/**
		 * The meta object literal for the '<em><b>Subentries</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TODO_ENTRY__SUBENTRIES = eINSTANCE.getTodoEntry_Subentries();

		/**
		 * The meta object literal for the '{@link at.bitandart.zoubek.mervin.testmodel.impl.TextEntryImpl <em>Text Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see at.bitandart.zoubek.mervin.testmodel.impl.TextEntryImpl
		 * @see at.bitandart.zoubek.mervin.testmodel.impl.TestModelPackageImpl#getTextEntry()
		 * @generated
		 */
		EClass TEXT_ENTRY = eINSTANCE.getTextEntry();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEXT_ENTRY__TITLE = eINSTANCE.getTextEntry_Title();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEXT_ENTRY__DESCRIPTION = eINSTANCE.getTextEntry_Description();

		/**
		 * The meta object literal for the '{@link at.bitandart.zoubek.mervin.testmodel.impl.UserImpl <em>User</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see at.bitandart.zoubek.mervin.testmodel.impl.UserImpl
		 * @see at.bitandart.zoubek.mervin.testmodel.impl.TestModelPackageImpl#getUser()
		 * @generated
		 */
		EClass USER = eINSTANCE.getUser();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER__NAME = eINSTANCE.getUser_Name();

		/**
		 * The meta object literal for the '<em><b>Avatar</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference USER__AVATAR = eINSTANCE.getUser_Avatar();

		/**
		 * The meta object literal for the '{@link at.bitandart.zoubek.mervin.testmodel.impl.AvatarImpl <em>Avatar</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see at.bitandart.zoubek.mervin.testmodel.impl.AvatarImpl
		 * @see at.bitandart.zoubek.mervin.testmodel.impl.TestModelPackageImpl#getAvatar()
		 * @generated
		 */
		EClass AVATAR = eINSTANCE.getAvatar();

		/**
		 * The meta object literal for the '<em><b>Uri</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AVATAR__URI = eINSTANCE.getAvatar_Uri();

	}

} //TestModelPackage

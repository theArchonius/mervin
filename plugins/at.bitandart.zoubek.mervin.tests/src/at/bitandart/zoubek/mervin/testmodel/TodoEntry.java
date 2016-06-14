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
 * A representation of the model object '<em><b>Todo Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link at.bitandart.zoubek.mervin.testmodel.TodoEntry#isCompleted <em>Completed</em>}</li>
 *   <li>{@link at.bitandart.zoubek.mervin.testmodel.TodoEntry#getSubentries <em>Subentries</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.testmodel.TestModelPackage#getTodoEntry()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface TodoEntry extends EObject {
	/**
	 * Returns the value of the '<em><b>Completed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Completed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Completed</em>' attribute.
	 * @see #setCompleted(boolean)
	 * @see at.bitandart.zoubek.mervin.testmodel.TestModelPackage#getTodoEntry_Completed()
	 * @model
	 * @generated
	 */
	boolean isCompleted();

	/**
	 * Sets the value of the '{@link at.bitandart.zoubek.mervin.testmodel.TodoEntry#isCompleted <em>Completed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Completed</em>' attribute.
	 * @see #isCompleted()
	 * @generated
	 */
	void setCompleted(boolean value);

	/**
	 * Returns the value of the '<em><b>Subentries</b></em>' containment reference list.
	 * The list contents are of type {@link at.bitandart.zoubek.mervin.testmodel.TodoEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Subentries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Subentries</em>' containment reference list.
	 * @see at.bitandart.zoubek.mervin.testmodel.TestModelPackage#getTodoEntry_Subentries()
	 * @model containment="true"
	 * @generated
	 */
	EList<TodoEntry> getSubentries();

} // TodoEntry

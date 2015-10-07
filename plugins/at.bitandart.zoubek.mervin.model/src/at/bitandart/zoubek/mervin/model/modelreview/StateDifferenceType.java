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
package at.bitandart.zoubek.mervin.model.modelreview;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>State Difference Type</b></em>', and utility methods for working with
 * them. <!-- end-user-doc -->
 * 
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getStateDifferenceType()
 * @model
 * @generated
 */
public enum StateDifferenceType implements Enumerator {
	/**
	 * The '<em><b>ADDED</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #ADDED_VALUE
	 * @generated
	 * @ordered
	 */
	ADDED(0, "ADDED", "ADDED"),

	/**
	 * The '<em><b>DELETED</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #DELETED_VALUE
	 * @generated
	 * @ordered
	 */
	DELETED(1, "DELETED", "DELETED"),

	/**
	 * The '<em><b>MODIFIED</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #MODIFIED_VALUE
	 * @generated
	 * @ordered
	 */
	MODIFIED(2, "MODIFIED",
			"MODIFIED"), /**
							 * The '<em><b>UNKNOWN</b></em>' literal object.
							 * <!-- begin-user-doc --> <!-- end-user-doc -->
							 * 
							 * @see #UNKNOWN_VALUE
							 * @generated
							 * @ordered
							 */
	UNKNOWN(3, "UNKNOWN", "UNKNOWN");

	/**
	 * The '<em><b>ADDED</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ADDED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ADDED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ADDED_VALUE = 0;

	/**
	 * The '<em><b>DELETED</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DELETED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #DELETED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DELETED_VALUE = 1;

	/**
	 * The '<em><b>MODIFIED</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MODIFIED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #MODIFIED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MODIFIED_VALUE = 2;

	/**
	 * The '<em><b>UNKNOWN</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>UNKNOWN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #UNKNOWN
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int UNKNOWN_VALUE = 3;

	/**
	 * An array of all the '<em><b>State Difference Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final StateDifferenceType[] VALUES_ARRAY = new StateDifferenceType[] { ADDED, DELETED, MODIFIED,
			UNKNOWN, };

	/**
	 * A public read-only list of all the '<em><b>State Difference Type</b></em>
	 * ' enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<StateDifferenceType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>State Difference Type</b></em>' literal with the
	 * specified literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param literal
	 *            the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static StateDifferenceType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			StateDifferenceType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>State Difference Type</b></em>' literal with the
	 * specified name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param name
	 *            the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static StateDifferenceType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			StateDifferenceType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>State Difference Type</b></em>' literal with the
	 * specified integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static StateDifferenceType get(int value) {
		switch (value) {
		case ADDED_VALUE:
			return ADDED;
		case DELETED_VALUE:
			return DELETED;
		case MODIFIED_VALUE:
			return MODIFIED;
		case UNKNOWN_VALUE:
			return UNKNOWN;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private StateDifferenceType(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getLiteral() {
		return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string
	 * representation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}

} // StateDifferenceType

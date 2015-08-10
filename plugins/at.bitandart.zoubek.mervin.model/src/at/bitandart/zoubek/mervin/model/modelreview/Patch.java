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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Patch</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getNewPath
 * <em>New Path</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getOldPath
 * <em>Old Path</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getNewContent
 * <em>New Content</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getOldContent
 * <em>Old Content</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getChangeType
 * <em>Change Type</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getPatchSet
 * <em>Patch Set</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatch()
 * @model
 * @generated
 */
public interface Patch extends EObject {

	/**
	 * Returns the value of the '<em><b>New Path</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Path</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>New Path</em>' attribute.
	 * @see #setNewPath(String)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatch_NewPath()
	 * @model
	 * @generated
	 */
	String getNewPath();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getNewPath
	 * <em>New Path</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>New Path</em>' attribute.
	 * @see #getNewPath()
	 * @generated
	 */
	void setNewPath(String value);

	/**
	 * Returns the value of the '<em><b>Old Path</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Path</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Old Path</em>' attribute.
	 * @see #setOldPath(String)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatch_OldPath()
	 * @model
	 * @generated
	 */
	String getOldPath();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getOldPath
	 * <em>Old Path</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Old Path</em>' attribute.
	 * @see #getOldPath()
	 * @generated
	 */
	void setOldPath(String value);

	/**
	 * Returns the value of the '<em><b>New Content</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Content</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>New Content</em>' attribute.
	 * @see #setNewContent(byte[])
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatch_NewContent()
	 * @model
	 * @generated
	 */
	byte[] getNewContent();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getNewContent
	 * <em>New Content</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>New Content</em>' attribute.
	 * @see #getNewContent()
	 * @generated
	 */
	void setNewContent(byte[] value);

	/**
	 * Returns the value of the '<em><b>Old Content</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Content</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Old Content</em>' attribute.
	 * @see #setOldContent(byte[])
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatch_OldContent()
	 * @model
	 * @generated
	 */
	byte[] getOldContent();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getOldContent
	 * <em>Old Content</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Old Content</em>' attribute.
	 * @see #getOldContent()
	 * @generated
	 */
	void setOldContent(byte[] value);

	/**
	 * Returns the value of the '<em><b>Change Type</b></em>' attribute. The
	 * default value is <code>"ADD"</code>. The literals are from the
	 * enumeration
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchChangeType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Type</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Change Type</em>' attribute.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchChangeType
	 * @see #setChangeType(PatchChangeType)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatch_ChangeType()
	 * @model default="ADD"
	 * @generated
	 */
	PatchChangeType getChangeType();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getChangeType
	 * <em>Change Type</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Change Type</em>' attribute.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchChangeType
	 * @see #getChangeType()
	 * @generated
	 */
	void setChangeType(PatchChangeType value);

	/**
	 * Returns the value of the '<em><b>Patch Set</b></em>' reference. It is
	 * bidirectional and its opposite is '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getPatches
	 * <em>Patches</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Patch Set</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Patch Set</em>' reference.
	 * @see #setPatchSet(PatchSet)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatch_PatchSet()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getPatches
	 * @model opposite="patches"
	 * @generated
	 */
	PatchSet getPatchSet();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getPatchSet
	 * <em>Patch Set</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Patch Set</em>' reference.
	 * @see #getPatchSet()
	 * @generated
	 */
	void setPatchSet(PatchSet value);
} // Patch

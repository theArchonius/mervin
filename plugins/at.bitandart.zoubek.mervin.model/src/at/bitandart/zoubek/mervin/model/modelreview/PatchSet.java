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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Patch Set</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getId <em>Id
 * </em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getReview
 * <em>Review</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getPatches
 * <em>Patches</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getInvolvedModels
 * <em>Involved Models</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getInvolvedDiagrams
 * <em>Involved Diagrams</em>}</li>
 * </ul>
 * </p>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet()
 * @model
 * @generated
 */
public interface PatchSet extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getId
	 * <em>Id</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Review</b></em>' container reference. It
	 * is bidirectional and its opposite is '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getPatchSets
	 * <em>Patch Sets</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Review</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Review</em>' container reference.
	 * @see #setReview(ModelReview)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_Review()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getPatchSets
	 * @model opposite="patchSets" transient="false"
	 * @generated
	 */
	ModelReview getReview();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getReview
	 * <em>Review</em>}' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Review</em>' container reference.
	 * @see #getReview()
	 * @generated
	 */
	void setReview(ModelReview value);

	/**
	 * Returns the value of the '<em><b>Patches</b></em>' reference list. The
	 * list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Patches</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Patches</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_Patches()
	 * @model
	 * @generated
	 */
	EList<Patch> getPatches();

	/**
	 * Returns the value of the '<em><b>Involved Models</b></em>' reference
	 * list. The list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelInstance}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Involved Models</em>' reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Involved Models</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_InvolvedModels()
	 * @model
	 * @generated
	 */
	EList<ModelInstance> getInvolvedModels();

	/**
	 * Returns the value of the '<em><b>Involved Diagrams</b></em>' reference
	 * list. The list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Involved Diagrams</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Involved Diagrams</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_InvolvedDiagrams()
	 * @model
	 * @generated
	 */
	EList<DiagramInstance> getInvolvedDiagrams();

} // PatchSet

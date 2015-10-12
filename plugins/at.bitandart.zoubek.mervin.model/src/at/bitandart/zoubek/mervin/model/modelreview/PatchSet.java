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

import java.util.Map;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Patch Set</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getId
 * <em>Id</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getReview
 * <em>Review</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getPatches
 * <em>Patches</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getNewInvolvedModels
 * <em>New Involved Models</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getNewInvolvedDiagrams
 * <em>New Involved Diagrams</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getOldInvolvedModels
 * <em>Old Involved Models</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getOldInvolvedDiagrams
 * <em>Old Involved Diagrams</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getModelComparison
 * <em>Model Comparison</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getDiagramComparison
 * <em>Diagram Comparison</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getObjectChangeCount
 * <em>Object Change Count</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getObjectChangeRefCount
 * <em>Object Change Ref Count</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getMaxObjectChangeCount
 * <em>Max Object Change Count</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getMaxObjectChangeRefCount
 * <em>Max Object Change Ref Count</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getAllNewInvolvedDiagrams
 * <em>All New Involved Diagrams</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getAllOldInvolvedDiagrams
 * <em>All Old Involved Diagrams</em>}</li>
 * </ul>
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
	 * Returns the value of the '<em><b>New Involved Models</b></em>' reference
	 * list. The list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelInstance}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Involved Models</em>' reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>New Involved Models</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_NewInvolvedModels()
	 * @model
	 * @generated
	 */
	EList<ModelResource> getNewInvolvedModels();

	/**
	 * Returns the value of the '<em><b>New Involved Diagrams</b></em>'
	 * reference list. The list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramResource}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Involved Diagrams</em>' reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>New Involved Diagrams</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_NewInvolvedDiagrams()
	 * @model
	 * @generated
	 */
	EList<DiagramResource> getNewInvolvedDiagrams();

	/**
	 * Returns the value of the '<em><b>Old Involved Models</b></em>' reference
	 * list. The list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelInstance}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Involved Models</em>' reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Old Involved Models</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_OldInvolvedModels()
	 * @model
	 * @generated
	 */
	EList<ModelResource> getOldInvolvedModels();

	/**
	 * Returns the value of the '<em><b>Old Involved Diagrams</b></em>'
	 * reference list. The list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramResource}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Involved Diagrams</em>' reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Old Involved Diagrams</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_OldInvolvedDiagrams()
	 * @model
	 * @generated
	 */
	EList<DiagramResource> getOldInvolvedDiagrams();

	/**
	 * Returns the value of the '<em><b>Model Comparison</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Comparison</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Model Comparison</em>' reference.
	 * @see #setModelComparison(Comparison)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_ModelComparison()
	 * @model
	 * @generated
	 */
	Comparison getModelComparison();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getModelComparison
	 * <em>Model Comparison</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Model Comparison</em>' reference.
	 * @see #getModelComparison()
	 * @generated
	 */
	void setModelComparison(Comparison value);

	/**
	 * Returns the value of the '<em><b>Diagram Comparison</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagram Comparison</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Diagram Comparison</em>' reference.
	 * @see #setDiagramComparison(Comparison)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_DiagramComparison()
	 * @model
	 * @generated
	 */
	Comparison getDiagramComparison();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getDiagramComparison
	 * <em>Diagram Comparison</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Diagram Comparison</em>' reference.
	 * @see #getDiagramComparison()
	 * @generated
	 */
	void setDiagramComparison(Comparison value);

	/**
	 * Returns the value of the '<em><b>Object Change Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object Change Count</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Object Change Count</em>' attribute.
	 * @see #isSetObjectChangeCount()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_ObjectChangeCount()
	 * @model unsettable="true" transient="true" changeable="false"
	 *        derived="true"
	 * @generated
	 */
	Map<EObject, Integer> getObjectChangeCount();

	/**
	 * Returns whether the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getObjectChangeCount
	 * <em>Object Change Count</em>}' attribute is set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Object Change Count</em>' attribute
	 *         is set.
	 * @see #getObjectChangeCount()
	 * @generated
	 */
	boolean isSetObjectChangeCount();

	/**
	 * Returns the value of the '<em><b>Object Change Ref Count</b></em>'
	 * attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object Change Ref Count</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Object Change Ref Count</em>' attribute.
	 * @see #isSetObjectChangeRefCount()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_ObjectChangeRefCount()
	 * @model unsettable="true" transient="true" changeable="false"
	 *        derived="true"
	 * @generated
	 */
	Map<EObject, Integer> getObjectChangeRefCount();

	/**
	 * Returns whether the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getObjectChangeRefCount
	 * <em>Object Change Ref Count</em>}' attribute is set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Object Change Ref Count</em>'
	 *         attribute is set.
	 * @see #getObjectChangeRefCount()
	 * @generated
	 */
	boolean isSetObjectChangeRefCount();

	/**
	 * Returns the value of the '<em><b>Max Object Change Count</b></em>'
	 * attribute. The default value is <code>"0"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Object Change Count</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Max Object Change Count</em>' attribute.
	 * @see #isSetMaxObjectChangeCount()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_MaxObjectChangeCount()
	 * @model default="0" unsettable="true" transient="true" changeable="false"
	 *        derived="true"
	 * @generated
	 */
	int getMaxObjectChangeCount();

	/**
	 * Returns whether the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getMaxObjectChangeCount
	 * <em>Max Object Change Count</em>}' attribute is set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Max Object Change Count</em>'
	 *         attribute is set.
	 * @see #getMaxObjectChangeCount()
	 * @generated
	 */
	boolean isSetMaxObjectChangeCount();

	/**
	 * Returns the value of the '<em><b>Max Object Change Ref Count</b></em>'
	 * attribute. The default value is <code>"0"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Object Change Ref Count</em>' attribute
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Max Object Change Ref Count</em>'
	 *         attribute.
	 * @see #isSetMaxObjectChangeRefCount()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_MaxObjectChangeRefCount()
	 * @model default="0" unsettable="true" transient="true" changeable="false"
	 *        derived="true"
	 * @generated
	 */
	int getMaxObjectChangeRefCount();

	/**
	 * Returns whether the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getMaxObjectChangeRefCount
	 * <em>Max Object Change Ref Count</em>}' attribute is set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Max Object Change Ref Count</em>'
	 *         attribute is set.
	 * @see #getMaxObjectChangeRefCount()
	 * @generated
	 */
	boolean isSetMaxObjectChangeRefCount();

	/**
	 * Returns the value of the '<em><b>All New Involved Diagrams</b></em>'
	 * reference list. The list contents are of type
	 * {@link org.eclipse.gmf.runtime.notation.Diagram}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>All New Involved Diagrams</em>' reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>All New Involved Diagrams</em>' reference
	 *         list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_AllNewInvolvedDiagrams()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	EList<Diagram> getAllNewInvolvedDiagrams();

	/**
	 * Returns the value of the '<em><b>All Old Involved Diagrams</b></em>'
	 * reference list. The list contents are of type
	 * {@link org.eclipse.gmf.runtime.notation.Diagram}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>All Old Involved Diagrams</em>' reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>All Old Involved Diagrams</em>' reference
	 *         list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getPatchSet_AllOldInvolvedDiagrams()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	EList<Diagram> getAllOldInvolvedDiagrams();

} // PatchSet

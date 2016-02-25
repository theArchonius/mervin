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
package at.bitandart.zoubek.mervin.model.modelreview;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Model Review</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getId
 * <em>Id</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getPatchSets
 * <em>Patch Sets</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getComments
 * <em>Comments</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getLeftPatchSet
 * <em>Left Patch Set</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getRightPatchSet
 * <em>Right Patch Set</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getSelectedModelComparison
 * <em>Selected Model Comparison</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getSelectedDiagramComparison
 * <em>Selected Diagram Comparison</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#isShowAdditions
 * <em>Show Additions</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#isShowModifications
 * <em>Show Modifications</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#isShowDeletions
 * <em>Show Deletions</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#isShowLayoutChanges
 * <em>Show Layout Changes</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelReview()
 * @model
 * @generated
 */
public interface ModelReview extends EObject {
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
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelReview_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getId
	 * <em>Id</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Patch Sets</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet}. It is
	 * bidirectional and its opposite is '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getReview
	 * <em>Review</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Patch Sets</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Patch Sets</em>' containment reference
	 *         list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelReview_PatchSets()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getReview
	 * @model opposite="review" containment="true"
	 * @generated
	 */
	EList<PatchSet> getPatchSets();

	/**
	 * Returns the value of the '<em><b>Comments</b></em>' reference list. The
	 * list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comments</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Comments</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelReview_Comments()
	 * @model
	 * @generated
	 */
	EList<Comment> getComments();

	/**
	 * Returns the value of the '<em><b>Left Patch Set</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Patch Set</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Left Patch Set</em>' reference.
	 * @see #setLeftPatchSet(PatchSet)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelReview_LeftPatchSet()
	 * @model
	 * @generated
	 */
	PatchSet getLeftPatchSet();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getLeftPatchSet
	 * <em>Left Patch Set</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Left Patch Set</em>' reference.
	 * @see #getLeftPatchSet()
	 * @generated
	 */
	void setLeftPatchSet(PatchSet value);

	/**
	 * Returns the value of the '<em><b>Right Patch Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Patch Set</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Right Patch Set</em>' reference.
	 * @see #setRightPatchSet(PatchSet)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelReview_RightPatchSet()
	 * @model
	 * @generated
	 */
	PatchSet getRightPatchSet();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getRightPatchSet
	 * <em>Right Patch Set</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Right Patch Set</em>' reference.
	 * @see #getRightPatchSet()
	 * @generated
	 */
	void setRightPatchSet(PatchSet value);

	/**
	 * Returns the value of the '<em><b>Selected Model Comparison</b></em>'
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selected Model Comparison</em>' reference
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Selected Model Comparison</em>' reference.
	 * @see #setSelectedModelComparison(Comparison)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelReview_SelectedModelComparison()
	 * @model transient="true"
	 * @generated
	 */
	Comparison getSelectedModelComparison();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getSelectedModelComparison
	 * <em>Selected Model Comparison</em>}' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Selected Model Comparison</em>'
	 *            reference.
	 * @see #getSelectedModelComparison()
	 * @generated
	 */
	void setSelectedModelComparison(Comparison value);

	/**
	 * Returns the value of the '<em><b>Selected Diagram Comparison</b></em>'
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selected Diagram Comparison</em>' reference
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Selected Diagram Comparison</em>'
	 *         reference.
	 * @see #setSelectedDiagramComparison(Comparison)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelReview_SelectedDiagramComparison()
	 * @model transient="true"
	 * @generated
	 */
	Comparison getSelectedDiagramComparison();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getSelectedDiagramComparison
	 * <em>Selected Diagram Comparison</em>}' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Selected Diagram Comparison</em>'
	 *            reference.
	 * @see #getSelectedDiagramComparison()
	 * @generated
	 */
	void setSelectedDiagramComparison(Comparison value);

	/**
	 * Returns the value of the '<em><b>Show Additions</b></em>' attribute. The
	 * default value is <code>"true"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Show Additions</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Show Additions</em>' attribute.
	 * @see #setShowAdditions(boolean)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelReview_ShowAdditions()
	 * @model default="true"
	 * @generated
	 */
	boolean isShowAdditions();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#isShowAdditions
	 * <em>Show Additions</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Show Additions</em>' attribute.
	 * @see #isShowAdditions()
	 * @generated
	 */
	void setShowAdditions(boolean value);

	/**
	 * Returns the value of the '<em><b>Show Modifications</b></em>' attribute.
	 * The default value is <code>"true"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Show Modifications</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Show Modifications</em>' attribute.
	 * @see #setShowModifications(boolean)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelReview_ShowModifications()
	 * @model default="true"
	 * @generated
	 */
	boolean isShowModifications();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#isShowModifications
	 * <em>Show Modifications</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Show Modifications</em>' attribute.
	 * @see #isShowModifications()
	 * @generated
	 */
	void setShowModifications(boolean value);

	/**
	 * Returns the value of the '<em><b>Show Deletions</b></em>' attribute. The
	 * default value is <code>"false"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Show Deletions</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Show Deletions</em>' attribute.
	 * @see #setShowDeletions(boolean)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelReview_ShowDeletions()
	 * @model default="false"
	 * @generated
	 */
	boolean isShowDeletions();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#isShowDeletions
	 * <em>Show Deletions</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Show Deletions</em>' attribute.
	 * @see #isShowDeletions()
	 * @generated
	 */
	void setShowDeletions(boolean value);

	/**
	 * Returns the value of the '<em><b>Show Layout Changes</b></em>' attribute.
	 * The default value is <code>"true"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Show Layout Changes</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Show Layout Changes</em>' attribute.
	 * @see #setShowLayoutChanges(boolean)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getModelReview_ShowLayoutChanges()
	 * @model default="true"
	 * @generated
	 */
	boolean isShowLayoutChanges();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#isShowLayoutChanges
	 * <em>Show Layout Changes</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Show Layout Changes</em>' attribute.
	 * @see #isShowLayoutChanges()
	 * @generated
	 */
	void setShowLayoutChanges(boolean value);

} // ModelReview

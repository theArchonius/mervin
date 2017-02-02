/**
 * ******************************************************************************
 *  Copyright (c) 2015, 2016, 2017 Florian Zoubek.
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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage
 * @generated
 */
public interface ModelReviewFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated NOT
	 */
	ModelReviewFactory eINSTANCE = at.bitandart.zoubek.mervin.model.modelreview.impl.extended.DefaultModelReviewFactory
			.init();

	/**
	 * Returns a new object of class '<em>Model Review</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Model Review</em>'.
	 * @generated
	 */
	ModelReview createModelReview();

	/**
	 * Returns a new object of class '<em>Patch Set</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Patch Set</em>'.
	 * @generated
	 */
	PatchSet createPatchSet();

	/**
	 * Returns a new object of class '<em>Patch</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Patch</em>'.
	 * @generated
	 */
	Patch createPatch();

	/**
	 * Returns a new object of class '<em>Diagram Patch</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Diagram Patch</em>'.
	 * @generated
	 */
	DiagramPatch createDiagramPatch();

	/**
	 * Returns a new object of class '<em>Model Patch</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Model Patch</em>'.
	 * @generated
	 */
	ModelPatch createModelPatch();

	/**
	 * Returns a new object of class '<em>Comment</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Comment</em>'.
	 * @generated
	 */
	Comment createComment();

	/**
	 * Returns a new object of class '<em>Model Resource</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Model Resource</em>'.
	 * @generated
	 */
	ModelResource createModelResource();

	/**
	 * Returns a new object of class '<em>Diagram Resource</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Diagram Resource</em>'.
	 * @generated
	 */
	DiagramResource createDiagramResource();

	/**
	 * Returns a new object of class '<em>Node Difference Overlay</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Node Difference Overlay</em>'.
	 * @generated
	 */
	NodeDifferenceOverlay createNodeDifferenceOverlay();

	/**
	 * Returns a new object of class '<em>Edge Difference Overlay</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Edge Difference Overlay</em>'.
	 * @generated
	 */
	EdgeDifferenceOverlay createEdgeDifferenceOverlay();

	/**
	 * Returns a new object of class '<em>Location Difference</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Location Difference</em>'.
	 * @generated
	 */
	LocationDifference createLocationDifference();

	/**
	 * Returns a new object of class '<em>Size Difference</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Size Difference</em>'.
	 * @generated
	 */
	SizeDifference createSizeDifference();

	/**
	 * Returns a new object of class '<em>State Difference</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>State Difference</em>'.
	 * @generated
	 */
	StateDifference createStateDifference();

	/**
	 * Returns a new object of class '<em>Bendpoints Difference</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Bendpoints Difference</em>'.
	 * @generated
	 */
	BendpointsDifference createBendpointsDifference();

	/**
	 * Returns a new object of class '<em>Comment Link</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Comment Link</em>'.
	 * @generated
	 */
	CommentLink createCommentLink();

	/**
	 * Returns a new object of class '<em>User</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>User</em>'.
	 * @generated
	 */
	User createUser();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	ModelReviewPackage getModelReviewPackage();

} // ModelReviewFactory

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
	 * @generated
	 */
	ModelReviewFactory eINSTANCE = at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewFactoryImpl
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
	 * Returns a new object of class '<em>Model Instance</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Model Instance</em>'.
	 * @generated
	 */
	ModelInstance createModelInstance();

	/**
	 * Returns a new object of class '<em>Diagram Instance</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Diagram Instance</em>'.
	 * @generated
	 */
	DiagramInstance createDiagramInstance();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	ModelReviewPackage getModelReviewPackage();

} // ModelReviewFactory

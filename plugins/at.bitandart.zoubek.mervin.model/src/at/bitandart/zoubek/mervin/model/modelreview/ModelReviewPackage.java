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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory
 * @model kind="package"
 * @generated
 */
public interface ModelReviewPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "modelreview";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://zoubek.bitandart.at/mervin/modelreview";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "at.bitandart.zoubek.mervin";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	ModelReviewPackage eINSTANCE = at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl
			.init();

	/**
	 * The meta object id for the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl
	 * <em>Model Review</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getModelReview()
	 * @generated
	 */
	int MODEL_REVIEW = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_REVIEW__ID = 0;

	/**
	 * The feature id for the '<em><b>Patch Sets</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_REVIEW__PATCH_SETS = 1;

	/**
	 * The feature id for the '<em><b>Comments</b></em>' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_REVIEW__COMMENTS = 2;

	/**
	 * The number of structural features of the '<em>Model Review</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_REVIEW_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Model Review</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_REVIEW_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl
	 * <em>Patch Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getPatchSet()
	 * @generated
	 */
	int PATCH_SET = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET__ID = 0;

	/**
	 * The feature id for the '<em><b>Review</b></em>' container reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET__REVIEW = 1;

	/**
	 * The feature id for the '<em><b>Patches</b></em>' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET__PATCHES = 2;

	/**
	 * The feature id for the '<em><b>New Involved Models</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET__NEW_INVOLVED_MODELS = 3;

	/**
	 * The feature id for the '<em><b>New Involved Diagrams</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET__NEW_INVOLVED_DIAGRAMS = 4;

	/**
	 * The feature id for the '<em><b>Old Involved Models</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET__OLD_INVOLVED_MODELS = 5;

	/**
	 * The feature id for the '<em><b>Old Involved Diagrams</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET__OLD_INVOLVED_DIAGRAMS = 6;

	/**
	 * The feature id for the '<em><b>Model Comparison</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET__MODEL_COMPARISON = 7;

	/**
	 * The feature id for the '<em><b>Diagram Comparison</b></em>' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET__DIAGRAM_COMPARISON = 8;

	/**
	 * The number of structural features of the '<em>Patch Set</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET_FEATURE_COUNT = 9;

	/**
	 * The number of operations of the '<em>Patch Set</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchImpl
	 * <em>Patch</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.PatchImpl
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getPatch()
	 * @generated
	 */
	int PATCH = 2;

	/**
	 * The feature id for the '<em><b>New Path</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH__NEW_PATH = 0;

	/**
	 * The feature id for the '<em><b>Old Path</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH__OLD_PATH = 1;

	/**
	 * The feature id for the '<em><b>New Content</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH__NEW_CONTENT = 2;

	/**
	 * The feature id for the '<em><b>Old Content</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH__OLD_CONTENT = 3;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH__CHANGE_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Patch Set</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH__PATCH_SET = 5;

	/**
	 * The number of structural features of the '<em>Patch</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Patch</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramPatchImpl
	 * <em>Diagram Patch</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramPatchImpl
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getDiagramPatch()
	 * @generated
	 */
	int DIAGRAM_PATCH = 3;

	/**
	 * The feature id for the '<em><b>New Path</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH__NEW_PATH = PATCH__NEW_PATH;

	/**
	 * The feature id for the '<em><b>Old Path</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH__OLD_PATH = PATCH__OLD_PATH;

	/**
	 * The feature id for the '<em><b>New Content</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH__NEW_CONTENT = PATCH__NEW_CONTENT;

	/**
	 * The feature id for the '<em><b>Old Content</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH__OLD_CONTENT = PATCH__OLD_CONTENT;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH__CHANGE_TYPE = PATCH__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Patch Set</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH__PATCH_SET = PATCH__PATCH_SET;

	/**
	 * The feature id for the '<em><b>New Diagram Instance</b></em>' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH__NEW_DIAGRAM_INSTANCE = PATCH_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Old Diagram Instance</b></em>' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH__OLD_DIAGRAM_INSTANCE = PATCH_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Diagram Patch</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH_FEATURE_COUNT = PATCH_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Diagram Patch</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH_OPERATION_COUNT = PATCH_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelPatchImpl
	 * <em>Model Patch</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelPatchImpl
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getModelPatch()
	 * @generated
	 */
	int MODEL_PATCH = 4;

	/**
	 * The feature id for the '<em><b>New Path</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH__NEW_PATH = PATCH__NEW_PATH;

	/**
	 * The feature id for the '<em><b>Old Path</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH__OLD_PATH = PATCH__OLD_PATH;

	/**
	 * The feature id for the '<em><b>New Content</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH__NEW_CONTENT = PATCH__NEW_CONTENT;

	/**
	 * The feature id for the '<em><b>Old Content</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH__OLD_CONTENT = PATCH__OLD_CONTENT;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH__CHANGE_TYPE = PATCH__CHANGE_TYPE;

	/**
	 * The feature id for the '<em><b>Patch Set</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH__PATCH_SET = PATCH__PATCH_SET;

	/**
	 * The feature id for the '<em><b>New Model Instance</b></em>' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH__NEW_MODEL_INSTANCE = PATCH_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Old Model Instance</b></em>' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH__OLD_MODEL_INSTANCE = PATCH_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Model Patch</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH_FEATURE_COUNT = PATCH_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Model Patch</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH_OPERATION_COUNT = PATCH_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl
	 * <em>Comment</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getComment()
	 * @generated
	 */
	int COMMENT = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMMENT__ID = 0;

	/**
	 * The number of structural features of the '<em>Comment</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMMENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Comment</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelInstanceImpl
	 * <em>Model Instance</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelInstanceImpl
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getModelInstance()
	 * @generated
	 */
	int MODEL_INSTANCE = 6;

	/**
	 * The feature id for the '<em><b>Objects</b></em>' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_INSTANCE__OBJECTS = 0;

	/**
	 * The feature id for the '<em><b>Root Packages</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_INSTANCE__ROOT_PACKAGES = 1;

	/**
	 * The number of structural features of the '<em>Model Instance</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_INSTANCE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Model Instance</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_INSTANCE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramInstanceImpl
	 * <em>Diagram Instance</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramInstanceImpl
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getDiagramInstance()
	 * @generated
	 */
	int DIAGRAM_INSTANCE = 7;

	/**
	 * The feature id for the '<em><b>Objects</b></em>' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_INSTANCE__OBJECTS = MODEL_INSTANCE__OBJECTS;

	/**
	 * The feature id for the '<em><b>Root Packages</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_INSTANCE__ROOT_PACKAGES = MODEL_INSTANCE__ROOT_PACKAGES;

	/**
	 * The number of structural features of the '<em>Diagram Instance</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_INSTANCE_FEATURE_COUNT = MODEL_INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Diagrams</em>' operation. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_INSTANCE___GET_DIAGRAMS = MODEL_INSTANCE_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Diagram Instance</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_INSTANCE_OPERATION_COUNT = MODEL_INSTANCE_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchChangeType
	 * <em>Patch Change Type</em>}' enum. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchChangeType
	 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getPatchChangeType()
	 * @generated
	 */
	int PATCH_CHANGE_TYPE = 8;

	/**
	 * Returns the meta object for class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview
	 * <em>Model Review</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Model Review</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReview
	 * @generated
	 */
	EClass getModelReview();

	/**
	 * Returns the meta object for the attribute '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getId
	 * <em>Id</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getId()
	 * @see #getModelReview()
	 * @generated
	 */
	EAttribute getModelReview_Id();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getPatchSets
	 * <em>Patch Sets</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Patch Sets</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getPatchSets()
	 * @see #getModelReview()
	 * @generated
	 */
	EReference getModelReview_PatchSets();

	/**
	 * Returns the meta object for the reference list '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getComments
	 * <em>Comments</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Comments</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReview#getComments()
	 * @see #getModelReview()
	 * @generated
	 */
	EReference getModelReview_Comments();

	/**
	 * Returns the meta object for class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet
	 * <em>Patch Set</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Patch Set</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet
	 * @generated
	 */
	EClass getPatchSet();

	/**
	 * Returns the meta object for the attribute '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getId
	 * <em>Id</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getId()
	 * @see #getPatchSet()
	 * @generated
	 */
	EAttribute getPatchSet_Id();

	/**
	 * Returns the meta object for the container reference '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getReview
	 * <em>Review</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '<em>Review</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getReview()
	 * @see #getPatchSet()
	 * @generated
	 */
	EReference getPatchSet_Review();

	/**
	 * Returns the meta object for the reference list '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getPatches
	 * <em>Patches</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Patches</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getPatches()
	 * @see #getPatchSet()
	 * @generated
	 */
	EReference getPatchSet_Patches();

	/**
	 * Returns the meta object for the reference list '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getNewInvolvedModels
	 * <em>New Involved Models</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the reference list '
	 *         <em>New Involved Models</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getNewInvolvedModels()
	 * @see #getPatchSet()
	 * @generated
	 */
	EReference getPatchSet_NewInvolvedModels();

	/**
	 * Returns the meta object for the reference list '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getNewInvolvedDiagrams
	 * <em>New Involved Diagrams</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the reference list '
	 *         <em>New Involved Diagrams</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getNewInvolvedDiagrams()
	 * @see #getPatchSet()
	 * @generated
	 */
	EReference getPatchSet_NewInvolvedDiagrams();

	/**
	 * Returns the meta object for the reference list '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getOldInvolvedModels
	 * <em>Old Involved Models</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the reference list '
	 *         <em>Old Involved Models</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getOldInvolvedModels()
	 * @see #getPatchSet()
	 * @generated
	 */
	EReference getPatchSet_OldInvolvedModels();

	/**
	 * Returns the meta object for the reference list '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getOldInvolvedDiagrams
	 * <em>Old Involved Diagrams</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the reference list '
	 *         <em>Old Involved Diagrams</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getOldInvolvedDiagrams()
	 * @see #getPatchSet()
	 * @generated
	 */
	EReference getPatchSet_OldInvolvedDiagrams();

	/**
	 * Returns the meta object for the reference '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getModelComparison
	 * <em>Model Comparison</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the reference '<em>Model Comparison</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getModelComparison()
	 * @see #getPatchSet()
	 * @generated
	 */
	EReference getPatchSet_ModelComparison();

	/**
	 * Returns the meta object for the reference '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getDiagramComparison
	 * <em>Diagram Comparison</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the reference '<em>Diagram Comparison</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getDiagramComparison()
	 * @see #getPatchSet()
	 * @generated
	 */
	EReference getPatchSet_DiagramComparison();

	/**
	 * Returns the meta object for class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch <em>Patch</em>}
	 * '. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Patch</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Patch
	 * @generated
	 */
	EClass getPatch();

	/**
	 * Returns the meta object for the attribute '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getNewPath
	 * <em>New Path</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>New Path</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Patch#getNewPath()
	 * @see #getPatch()
	 * @generated
	 */
	EAttribute getPatch_NewPath();

	/**
	 * Returns the meta object for the attribute '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getOldPath
	 * <em>Old Path</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Old Path</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Patch#getOldPath()
	 * @see #getPatch()
	 * @generated
	 */
	EAttribute getPatch_OldPath();

	/**
	 * Returns the meta object for the attribute '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getNewContent
	 * <em>New Content</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>New Content</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Patch#getNewContent()
	 * @see #getPatch()
	 * @generated
	 */
	EAttribute getPatch_NewContent();

	/**
	 * Returns the meta object for the attribute '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getOldContent
	 * <em>Old Content</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Old Content</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Patch#getOldContent()
	 * @see #getPatch()
	 * @generated
	 */
	EAttribute getPatch_OldContent();

	/**
	 * Returns the meta object for the attribute '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getChangeType
	 * <em>Change Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Change Type</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Patch#getChangeType()
	 * @see #getPatch()
	 * @generated
	 */
	EAttribute getPatch_ChangeType();

	/**
	 * Returns the meta object for the reference '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getPatchSet
	 * <em>Patch Set</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Patch Set</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Patch#getPatchSet()
	 * @see #getPatch()
	 * @generated
	 */
	EReference getPatch_PatchSet();

	/**
	 * Returns the meta object for class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch
	 * <em>Diagram Patch</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Diagram Patch</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch
	 * @generated
	 */
	EClass getDiagramPatch();

	/**
	 * Returns the meta object for the reference '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getNewDiagramInstance
	 * <em>New Diagram Instance</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>New Diagram Instance</em>
	 *         '.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getNewDiagramInstance()
	 * @see #getDiagramPatch()
	 * @generated
	 */
	EReference getDiagramPatch_NewDiagramInstance();

	/**
	 * Returns the meta object for the reference '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getOldDiagramInstance
	 * <em>Old Diagram Instance</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Old Diagram Instance</em>
	 *         '.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch#getOldDiagramInstance()
	 * @see #getDiagramPatch()
	 * @generated
	 */
	EReference getDiagramPatch_OldDiagramInstance();

	/**
	 * Returns the meta object for class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch
	 * <em>Model Patch</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Model Patch</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelPatch
	 * @generated
	 */
	EClass getModelPatch();

	/**
	 * Returns the meta object for the reference '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getNewModelInstance
	 * <em>New Model Instance</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the reference '<em>New Model Instance</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getNewModelInstance()
	 * @see #getModelPatch()
	 * @generated
	 */
	EReference getModelPatch_NewModelInstance();

	/**
	 * Returns the meta object for the reference '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getOldModelInstance
	 * <em>Old Model Instance</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the reference '<em>Old Model Instance</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelPatch#getOldModelInstance()
	 * @see #getModelPatch()
	 * @generated
	 */
	EReference getModelPatch_OldModelInstance();

	/**
	 * Returns the meta object for class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment
	 * <em>Comment</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Comment</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Comment
	 * @generated
	 */
	EClass getComment();

	/**
	 * Returns the meta object for the attribute '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getId
	 * <em>Id</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Comment#getId()
	 * @see #getComment()
	 * @generated
	 */
	EAttribute getComment_Id();

	/**
	 * Returns the meta object for class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelInstance
	 * <em>Model Instance</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Model Instance</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelInstance
	 * @generated
	 */
	EClass getModelInstance();

	/**
	 * Returns the meta object for the reference list '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelInstance#getObjects
	 * <em>Objects</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Objects</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelInstance#getObjects()
	 * @see #getModelInstance()
	 * @generated
	 */
	EReference getModelInstance_Objects();

	/**
	 * Returns the meta object for the reference list '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelInstance#getRootPackages
	 * <em>Root Packages</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Root Packages</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelInstance#getRootPackages()
	 * @see #getModelInstance()
	 * @generated
	 */
	EReference getModelInstance_RootPackages();

	/**
	 * Returns the meta object for class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance
	 * <em>Diagram Instance</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>Diagram Instance</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance
	 * @generated
	 */
	EClass getDiagramInstance();

	/**
	 * Returns the meta object for the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance#getDiagrams()
	 * <em>Get Diagrams</em>}' operation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the '<em>Get Diagrams</em>' operation.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance#getDiagrams()
	 * @generated
	 */
	EOperation getDiagramInstance__GetDiagrams();

	/**
	 * Returns the meta object for enum '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchChangeType
	 * <em>Patch Change Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for enum '<em>Patch Change Type</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchChangeType
	 * @generated
	 */
	EEnum getPatchChangeType();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelReviewFactory getModelReviewFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that
	 * represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '
		 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl
		 * <em>Model Review</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewImpl
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getModelReview()
		 * @generated
		 */
		EClass MODEL_REVIEW = eINSTANCE.getModelReview();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MODEL_REVIEW__ID = eINSTANCE.getModelReview_Id();

		/**
		 * The meta object literal for the '<em><b>Patch Sets</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_REVIEW__PATCH_SETS = eINSTANCE
				.getModelReview_PatchSets();

		/**
		 * The meta object literal for the '<em><b>Comments</b></em>' reference
		 * list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_REVIEW__COMMENTS = eINSTANCE.getModelReview_Comments();

		/**
		 * The meta object literal for the '
		 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl
		 * <em>Patch Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.PatchSetImpl
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getPatchSet()
		 * @generated
		 */
		EClass PATCH_SET = eINSTANCE.getPatchSet();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PATCH_SET__ID = eINSTANCE.getPatchSet_Id();

		/**
		 * The meta object literal for the '<em><b>Review</b></em>' container
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATCH_SET__REVIEW = eINSTANCE.getPatchSet_Review();

		/**
		 * The meta object literal for the '<em><b>Patches</b></em>' reference
		 * list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATCH_SET__PATCHES = eINSTANCE.getPatchSet_Patches();

		/**
		 * The meta object literal for the '<em><b>New Involved Models</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATCH_SET__NEW_INVOLVED_MODELS = eINSTANCE
				.getPatchSet_NewInvolvedModels();

		/**
		 * The meta object literal for the '
		 * <em><b>New Involved Diagrams</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATCH_SET__NEW_INVOLVED_DIAGRAMS = eINSTANCE
				.getPatchSet_NewInvolvedDiagrams();

		/**
		 * The meta object literal for the '<em><b>Old Involved Models</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATCH_SET__OLD_INVOLVED_MODELS = eINSTANCE
				.getPatchSet_OldInvolvedModels();

		/**
		 * The meta object literal for the '
		 * <em><b>Old Involved Diagrams</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATCH_SET__OLD_INVOLVED_DIAGRAMS = eINSTANCE
				.getPatchSet_OldInvolvedDiagrams();

		/**
		 * The meta object literal for the '<em><b>Model Comparison</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATCH_SET__MODEL_COMPARISON = eINSTANCE
				.getPatchSet_ModelComparison();

		/**
		 * The meta object literal for the '<em><b>Diagram Comparison</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATCH_SET__DIAGRAM_COMPARISON = eINSTANCE
				.getPatchSet_DiagramComparison();

		/**
		 * The meta object literal for the '
		 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.PatchImpl
		 * <em>Patch</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.PatchImpl
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getPatch()
		 * @generated
		 */
		EClass PATCH = eINSTANCE.getPatch();

		/**
		 * The meta object literal for the '<em><b>New Path</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PATCH__NEW_PATH = eINSTANCE.getPatch_NewPath();

		/**
		 * The meta object literal for the '<em><b>Old Path</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PATCH__OLD_PATH = eINSTANCE.getPatch_OldPath();

		/**
		 * The meta object literal for the '<em><b>New Content</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PATCH__NEW_CONTENT = eINSTANCE.getPatch_NewContent();

		/**
		 * The meta object literal for the '<em><b>Old Content</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PATCH__OLD_CONTENT = eINSTANCE.getPatch_OldContent();

		/**
		 * The meta object literal for the '<em><b>Change Type</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PATCH__CHANGE_TYPE = eINSTANCE.getPatch_ChangeType();

		/**
		 * The meta object literal for the '<em><b>Patch Set</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATCH__PATCH_SET = eINSTANCE.getPatch_PatchSet();

		/**
		 * The meta object literal for the '
		 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramPatchImpl
		 * <em>Diagram Patch</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramPatchImpl
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getDiagramPatch()
		 * @generated
		 */
		EClass DIAGRAM_PATCH = eINSTANCE.getDiagramPatch();

		/**
		 * The meta object literal for the '<em><b>New Diagram Instance</b></em>
		 * ' reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAGRAM_PATCH__NEW_DIAGRAM_INSTANCE = eINSTANCE
				.getDiagramPatch_NewDiagramInstance();

		/**
		 * The meta object literal for the '<em><b>Old Diagram Instance</b></em>
		 * ' reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAGRAM_PATCH__OLD_DIAGRAM_INSTANCE = eINSTANCE
				.getDiagramPatch_OldDiagramInstance();

		/**
		 * The meta object literal for the '
		 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelPatchImpl
		 * <em>Model Patch</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelPatchImpl
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getModelPatch()
		 * @generated
		 */
		EClass MODEL_PATCH = eINSTANCE.getModelPatch();

		/**
		 * The meta object literal for the '<em><b>New Model Instance</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_PATCH__NEW_MODEL_INSTANCE = eINSTANCE
				.getModelPatch_NewModelInstance();

		/**
		 * The meta object literal for the '<em><b>Old Model Instance</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_PATCH__OLD_MODEL_INSTANCE = eINSTANCE
				.getModelPatch_OldModelInstance();

		/**
		 * The meta object literal for the '
		 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl
		 * <em>Comment</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getComment()
		 * @generated
		 */
		EClass COMMENT = eINSTANCE.getComment();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute COMMENT__ID = eINSTANCE.getComment_Id();

		/**
		 * The meta object literal for the '
		 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.ModelInstanceImpl
		 * <em>Model Instance</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelInstanceImpl
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getModelInstance()
		 * @generated
		 */
		EClass MODEL_INSTANCE = eINSTANCE.getModelInstance();

		/**
		 * The meta object literal for the '<em><b>Objects</b></em>' reference
		 * list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_INSTANCE__OBJECTS = eINSTANCE
				.getModelInstance_Objects();

		/**
		 * The meta object literal for the '<em><b>Root Packages</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_INSTANCE__ROOT_PACKAGES = eINSTANCE
				.getModelInstance_RootPackages();

		/**
		 * The meta object literal for the '
		 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramInstanceImpl
		 * <em>Diagram Instance</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.DiagramInstanceImpl
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getDiagramInstance()
		 * @generated
		 */
		EClass DIAGRAM_INSTANCE = eINSTANCE.getDiagramInstance();

		/**
		 * The meta object literal for the '<em><b>Get Diagrams</b></em>'
		 * operation. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EOperation DIAGRAM_INSTANCE___GET_DIAGRAMS = eINSTANCE
				.getDiagramInstance__GetDiagrams();

		/**
		 * The meta object literal for the '
		 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchChangeType
		 * <em>Patch Change Type</em>}' enum. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchChangeType
		 * @see at.bitandart.zoubek.mervin.model.modelreview.impl.ModelReviewPackageImpl#getPatchChangeType()
		 * @generated
		 */
		EEnum PATCH_CHANGE_TYPE = eINSTANCE.getPatchChangeType();

	}

} // ModelReviewPackage

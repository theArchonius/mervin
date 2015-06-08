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
	 * The feature id for the '<em><b>Involved Models</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET__INVOLVED_MODELS = 3;

	/**
	 * The feature id for the '<em><b>Involved Diagrams</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET__INVOLVED_DIAGRAMS = 4;

	/**
	 * The number of structural features of the '<em>Patch Set</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_SET_FEATURE_COUNT = 5;

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
	 * The feature id for the '<em><b>Path</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH__PATH = 0;

	/**
	 * The feature id for the '<em><b>Content</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH__CONTENT = 1;

	/**
	 * The number of structural features of the '<em>Patch</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATCH_FEATURE_COUNT = 2;

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
	 * The feature id for the '<em><b>Path</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH__PATH = PATCH__PATH;

	/**
	 * The feature id for the '<em><b>Content</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH__CONTENT = PATCH__CONTENT;

	/**
	 * The number of structural features of the '<em>Diagram Patch</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PATCH_FEATURE_COUNT = PATCH_FEATURE_COUNT + 0;

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
	 * The feature id for the '<em><b>Path</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH__PATH = PATCH__PATH;

	/**
	 * The feature id for the '<em><b>Content</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH__CONTENT = PATCH__CONTENT;

	/**
	 * The number of structural features of the '<em>Model Patch</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_PATCH_FEATURE_COUNT = PATCH_FEATURE_COUNT + 0;

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
	 * The feature id for the '<em><b>Root Package</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_INSTANCE__ROOT_PACKAGE = 1;

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
	 * The feature id for the '<em><b>Notation Model</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_INSTANCE__NOTATION_MODEL = 0;

	/**
	 * The number of structural features of the '<em>Diagram Instance</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_INSTANCE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Diagram Instance</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_INSTANCE_OPERATION_COUNT = 0;

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
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getInvolvedModels
	 * <em>Involved Models</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Involved Models</em>
	 *         '.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getInvolvedModels()
	 * @see #getPatchSet()
	 * @generated
	 */
	EReference getPatchSet_InvolvedModels();

	/**
	 * Returns the meta object for the reference '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getInvolvedDiagrams
	 * <em>Involved Diagrams</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the reference '<em>Involved Diagrams</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getInvolvedDiagrams()
	 * @see #getPatchSet()
	 * @generated
	 */
	EReference getPatchSet_InvolvedDiagrams();

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
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getPath
	 * <em>Path</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Path</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Patch#getPath()
	 * @see #getPatch()
	 * @generated
	 */
	EAttribute getPatch_Path();

	/**
	 * Returns the meta object for the attribute '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch#getContent
	 * <em>Content</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Content</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Patch#getContent()
	 * @see #getPatch()
	 * @generated
	 */
	EAttribute getPatch_Content();

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
	 * Returns the meta object for the reference '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelInstance#getRootPackage
	 * <em>Root Package</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Root Package</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelInstance#getRootPackage()
	 * @see #getModelInstance()
	 * @generated
	 */
	EReference getModelInstance_RootPackage();

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
	 * Returns the meta object for the reference '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance#getNotationModel
	 * <em>Notation Model</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Notation Model</em>'.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance#getNotationModel()
	 * @see #getDiagramInstance()
	 * @generated
	 */
	EReference getDiagramInstance_NotationModel();

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
		 * The meta object literal for the '<em><b>Involved Models</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATCH_SET__INVOLVED_MODELS = eINSTANCE
				.getPatchSet_InvolvedModels();

		/**
		 * The meta object literal for the '<em><b>Involved Diagrams</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATCH_SET__INVOLVED_DIAGRAMS = eINSTANCE
				.getPatchSet_InvolvedDiagrams();

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
		 * The meta object literal for the '<em><b>Path</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PATCH__PATH = eINSTANCE.getPatch_Path();

		/**
		 * The meta object literal for the '<em><b>Content</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PATCH__CONTENT = eINSTANCE.getPatch_Content();

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
		 * The meta object literal for the '<em><b>Root Package</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_INSTANCE__ROOT_PACKAGE = eINSTANCE
				.getModelInstance_RootPackage();

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
		 * The meta object literal for the '<em><b>Notation Model</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAGRAM_INSTANCE__NOTATION_MODEL = eINSTANCE
				.getDiagramInstance_NotationModel();

	}

} // ModelReviewPackage

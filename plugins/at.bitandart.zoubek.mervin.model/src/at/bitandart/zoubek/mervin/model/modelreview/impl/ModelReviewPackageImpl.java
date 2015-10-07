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
package at.bitandart.zoubek.mervin.model.modelreview.impl;

import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance;
import at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch;
import at.bitandart.zoubek.mervin.model.modelreview.Difference;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.DimensionChange;
import at.bitandart.zoubek.mervin.model.modelreview.LayoutDifference;
import at.bitandart.zoubek.mervin.model.modelreview.LocationDifference;
import at.bitandart.zoubek.mervin.model.modelreview.ModelDifference;
import at.bitandart.zoubek.mervin.model.modelreview.ModelInstance;
import at.bitandart.zoubek.mervin.model.modelreview.ModelPatch;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.Patch;
import at.bitandart.zoubek.mervin.model.modelreview.PatchChangeType;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.SizeDifference;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifference;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifferenceType;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class ModelReviewPackageImpl extends EPackageImpl implements ModelReviewPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass modelReviewEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass patchSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass patchEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass diagramPatchEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass modelPatchEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass commentEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass modelInstanceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass diagramInstanceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass differenceOverlayEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass differenceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass layoutDifferenceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass modelDifferenceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass locationDifferenceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass sizeDifferenceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass stateDifferenceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum patchChangeTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum stateDifferenceTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum dimensionChangeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EDataType vectorEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory
	 * method {@link #init init()}, which also performs initialization of the
	 * package, or returns the registered package, if one already exists. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ModelReviewPackageImpl() {
		super(eNS_URI, ModelReviewFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model,
	 * and for any others upon which it depends.
	 * 
	 * <p>
	 * This method is used to initialize {@link ModelReviewPackage#eINSTANCE}
	 * when that field is accessed. Clients should not invoke it directly.
	 * Instead, they should simply access that field to obtain the package. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ModelReviewPackage init() {
		if (isInited)
			return (ModelReviewPackage) EPackage.Registry.INSTANCE.getEPackage(ModelReviewPackage.eNS_URI);

		// Obtain or create and register package
		ModelReviewPackageImpl theModelReviewPackage = (ModelReviewPackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof ModelReviewPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
						: new ModelReviewPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ComparePackage.eINSTANCE.eClass();
		NotationPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theModelReviewPackage.createPackageContents();

		// Initialize created meta-data
		theModelReviewPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theModelReviewPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ModelReviewPackage.eNS_URI, theModelReviewPackage);
		return theModelReviewPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getModelReview() {
		return modelReviewEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getModelReview_Id() {
		return (EAttribute) modelReviewEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getModelReview_PatchSets() {
		return (EReference) modelReviewEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getModelReview_Comments() {
		return (EReference) modelReviewEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getModelReview_LeftPatchSet() {
		return (EReference) modelReviewEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getModelReview_RightPatchSet() {
		return (EReference) modelReviewEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getModelReview_SelectedModelComparison() {
		return (EReference) modelReviewEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getModelReview_SelectedDiagramComparison() {
		return (EReference) modelReviewEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getPatchSet() {
		return patchSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPatchSet_Id() {
		return (EAttribute) patchSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPatchSet_Review() {
		return (EReference) patchSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPatchSet_Patches() {
		return (EReference) patchSetEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPatchSet_NewInvolvedModels() {
		return (EReference) patchSetEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPatchSet_NewInvolvedDiagrams() {
		return (EReference) patchSetEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPatchSet_OldInvolvedModels() {
		return (EReference) patchSetEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPatchSet_OldInvolvedDiagrams() {
		return (EReference) patchSetEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPatchSet_ModelComparison() {
		return (EReference) patchSetEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPatchSet_DiagramComparison() {
		return (EReference) patchSetEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPatchSet_ObjectChangeCount() {
		return (EAttribute) patchSetEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPatchSet_ObjectChangeRefCount() {
		return (EAttribute) patchSetEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPatchSet_MaxObjectChangeCount() {
		return (EAttribute) patchSetEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPatchSet_MaxObjectChangeRefCount() {
		return (EAttribute) patchSetEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPatchSet_AllNewInvolvedDiagrams() {
		return (EReference) patchSetEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPatchSet_AllOldInvolvedDiagrams() {
		return (EReference) patchSetEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getPatch() {
		return patchEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPatch_NewPath() {
		return (EAttribute) patchEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPatch_OldPath() {
		return (EAttribute) patchEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPatch_NewContent() {
		return (EAttribute) patchEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPatch_OldContent() {
		return (EAttribute) patchEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPatch_ChangeType() {
		return (EAttribute) patchEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPatch_PatchSet() {
		return (EReference) patchEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDiagramPatch() {
		return diagramPatchEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDiagramPatch_NewDiagramInstance() {
		return (EReference) diagramPatchEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDiagramPatch_OldDiagramInstance() {
		return (EReference) diagramPatchEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getModelPatch() {
		return modelPatchEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getModelPatch_NewModelInstance() {
		return (EReference) modelPatchEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getModelPatch_OldModelInstance() {
		return (EReference) modelPatchEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getComment() {
		return commentEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getComment_Id() {
		return (EAttribute) commentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getModelInstance() {
		return modelInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getModelInstance_Objects() {
		return (EReference) modelInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getModelInstance_RootPackages() {
		return (EReference) modelInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDiagramInstance() {
		return diagramInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EOperation getDiagramInstance__GetDiagrams() {
		return diagramInstanceEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDifferenceOverlay() {
		return differenceOverlayEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDifferenceOverlay_LinkedView() {
		return (EReference) differenceOverlayEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDifferenceOverlay_Differences() {
		return (EReference) differenceOverlayEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDifference() {
		return differenceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDifference_RawDiffs() {
		return (EReference) differenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getLayoutDifference() {
		return layoutDifferenceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getModelDifference() {
		return modelDifferenceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getLocationDifference() {
		return locationDifferenceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLocationDifference_MoveDirection() {
		return (EAttribute) locationDifferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSizeDifference() {
		return sizeDifferenceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSizeDifference_WidthChange() {
		return (EAttribute) sizeDifferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSizeDifference_HeightChange() {
		return (EAttribute) sizeDifferenceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getStateDifference() {
		return stateDifferenceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getStateDifference_Type() {
		return (EAttribute) stateDifferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getPatchChangeType() {
		return patchChangeTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getStateDifferenceType() {
		return stateDifferenceTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getDimensionChange() {
		return dimensionChangeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EDataType getVector() {
		return vectorEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelReviewFactory getModelReviewFactory() {
		return (ModelReviewFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to
	 * have no affect on any invocation but its first. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		modelReviewEClass = createEClass(MODEL_REVIEW);
		createEAttribute(modelReviewEClass, MODEL_REVIEW__ID);
		createEReference(modelReviewEClass, MODEL_REVIEW__PATCH_SETS);
		createEReference(modelReviewEClass, MODEL_REVIEW__COMMENTS);
		createEReference(modelReviewEClass, MODEL_REVIEW__LEFT_PATCH_SET);
		createEReference(modelReviewEClass, MODEL_REVIEW__RIGHT_PATCH_SET);
		createEReference(modelReviewEClass, MODEL_REVIEW__SELECTED_MODEL_COMPARISON);
		createEReference(modelReviewEClass, MODEL_REVIEW__SELECTED_DIAGRAM_COMPARISON);

		patchSetEClass = createEClass(PATCH_SET);
		createEAttribute(patchSetEClass, PATCH_SET__ID);
		createEReference(patchSetEClass, PATCH_SET__REVIEW);
		createEReference(patchSetEClass, PATCH_SET__PATCHES);
		createEReference(patchSetEClass, PATCH_SET__NEW_INVOLVED_MODELS);
		createEReference(patchSetEClass, PATCH_SET__NEW_INVOLVED_DIAGRAMS);
		createEReference(patchSetEClass, PATCH_SET__OLD_INVOLVED_MODELS);
		createEReference(patchSetEClass, PATCH_SET__OLD_INVOLVED_DIAGRAMS);
		createEReference(patchSetEClass, PATCH_SET__MODEL_COMPARISON);
		createEReference(patchSetEClass, PATCH_SET__DIAGRAM_COMPARISON);
		createEAttribute(patchSetEClass, PATCH_SET__OBJECT_CHANGE_COUNT);
		createEAttribute(patchSetEClass, PATCH_SET__OBJECT_CHANGE_REF_COUNT);
		createEAttribute(patchSetEClass, PATCH_SET__MAX_OBJECT_CHANGE_COUNT);
		createEAttribute(patchSetEClass, PATCH_SET__MAX_OBJECT_CHANGE_REF_COUNT);
		createEReference(patchSetEClass, PATCH_SET__ALL_NEW_INVOLVED_DIAGRAMS);
		createEReference(patchSetEClass, PATCH_SET__ALL_OLD_INVOLVED_DIAGRAMS);

		patchEClass = createEClass(PATCH);
		createEAttribute(patchEClass, PATCH__NEW_PATH);
		createEAttribute(patchEClass, PATCH__OLD_PATH);
		createEAttribute(patchEClass, PATCH__NEW_CONTENT);
		createEAttribute(patchEClass, PATCH__OLD_CONTENT);
		createEAttribute(patchEClass, PATCH__CHANGE_TYPE);
		createEReference(patchEClass, PATCH__PATCH_SET);

		diagramPatchEClass = createEClass(DIAGRAM_PATCH);
		createEReference(diagramPatchEClass, DIAGRAM_PATCH__NEW_DIAGRAM_INSTANCE);
		createEReference(diagramPatchEClass, DIAGRAM_PATCH__OLD_DIAGRAM_INSTANCE);

		modelPatchEClass = createEClass(MODEL_PATCH);
		createEReference(modelPatchEClass, MODEL_PATCH__NEW_MODEL_INSTANCE);
		createEReference(modelPatchEClass, MODEL_PATCH__OLD_MODEL_INSTANCE);

		commentEClass = createEClass(COMMENT);
		createEAttribute(commentEClass, COMMENT__ID);

		modelInstanceEClass = createEClass(MODEL_INSTANCE);
		createEReference(modelInstanceEClass, MODEL_INSTANCE__OBJECTS);
		createEReference(modelInstanceEClass, MODEL_INSTANCE__ROOT_PACKAGES);

		diagramInstanceEClass = createEClass(DIAGRAM_INSTANCE);
		createEOperation(diagramInstanceEClass, DIAGRAM_INSTANCE___GET_DIAGRAMS);

		differenceOverlayEClass = createEClass(DIFFERENCE_OVERLAY);
		createEReference(differenceOverlayEClass, DIFFERENCE_OVERLAY__LINKED_VIEW);
		createEReference(differenceOverlayEClass, DIFFERENCE_OVERLAY__DIFFERENCES);

		differenceEClass = createEClass(DIFFERENCE);
		createEReference(differenceEClass, DIFFERENCE__RAW_DIFFS);

		layoutDifferenceEClass = createEClass(LAYOUT_DIFFERENCE);

		modelDifferenceEClass = createEClass(MODEL_DIFFERENCE);

		locationDifferenceEClass = createEClass(LOCATION_DIFFERENCE);
		createEAttribute(locationDifferenceEClass, LOCATION_DIFFERENCE__MOVE_DIRECTION);

		sizeDifferenceEClass = createEClass(SIZE_DIFFERENCE);
		createEAttribute(sizeDifferenceEClass, SIZE_DIFFERENCE__WIDTH_CHANGE);
		createEAttribute(sizeDifferenceEClass, SIZE_DIFFERENCE__HEIGHT_CHANGE);

		stateDifferenceEClass = createEClass(STATE_DIFFERENCE);
		createEAttribute(stateDifferenceEClass, STATE_DIFFERENCE__TYPE);

		// Create enums
		patchChangeTypeEEnum = createEEnum(PATCH_CHANGE_TYPE);
		stateDifferenceTypeEEnum = createEEnum(STATE_DIFFERENCE_TYPE);
		dimensionChangeEEnum = createEEnum(DIMENSION_CHANGE);

		// Create data types
		vectorEDataType = createEDataType(VECTOR);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		ComparePackage theComparePackage = (ComparePackage) EPackage.Registry.INSTANCE
				.getEPackage(ComparePackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage) EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		NotationPackage theNotationPackage = (NotationPackage) EPackage.Registry.INSTANCE
				.getEPackage(NotationPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		diagramPatchEClass.getESuperTypes().add(this.getPatch());
		modelPatchEClass.getESuperTypes().add(this.getPatch());
		diagramInstanceEClass.getESuperTypes().add(this.getModelInstance());
		layoutDifferenceEClass.getESuperTypes().add(this.getDifference());
		modelDifferenceEClass.getESuperTypes().add(this.getDifference());
		locationDifferenceEClass.getESuperTypes().add(this.getLayoutDifference());
		sizeDifferenceEClass.getESuperTypes().add(this.getLayoutDifference());
		stateDifferenceEClass.getESuperTypes().add(this.getModelDifference());

		// Initialize classes, features, and operations; add parameters
		initEClass(modelReviewEClass, ModelReview.class, "ModelReview", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getModelReview_Id(), ecorePackage.getEString(), "id", null, 0, 1, ModelReview.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelReview_PatchSets(), this.getPatchSet(), this.getPatchSet_Review(), "patchSets", null, 0,
				-1, ModelReview.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelReview_Comments(), this.getComment(), null, "comments", null, 0, -1, ModelReview.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelReview_LeftPatchSet(), this.getPatchSet(), null, "leftPatchSet", null, 0, 1,
				ModelReview.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelReview_RightPatchSet(), this.getPatchSet(), null, "rightPatchSet", null, 0, 1,
				ModelReview.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelReview_SelectedModelComparison(), theComparePackage.getComparison(), null,
				"selectedModelComparison", null, 0, 1, ModelReview.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE,
				!IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getModelReview_SelectedDiagramComparison(), theComparePackage.getComparison(), null,
				"selectedDiagramComparison", null, 0, 1, ModelReview.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE,
				!IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(patchSetEClass, PatchSet.class, "PatchSet", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPatchSet_Id(), ecorePackage.getEString(), "id", null, 0, 1, PatchSet.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchSet_Review(), this.getModelReview(), this.getModelReview_PatchSets(), "review", null, 0,
				1, PatchSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchSet_Patches(), this.getPatch(), this.getPatch_PatchSet(), "patches", null, 0, -1,
				PatchSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchSet_NewInvolvedModels(), this.getModelInstance(), null, "newInvolvedModels", null, 0, -1,
				PatchSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchSet_NewInvolvedDiagrams(), this.getDiagramInstance(), null, "newInvolvedDiagrams", null,
				0, -1, PatchSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchSet_OldInvolvedModels(), this.getModelInstance(), null, "oldInvolvedModels", null, 0, -1,
				PatchSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchSet_OldInvolvedDiagrams(), this.getDiagramInstance(), null, "oldInvolvedDiagrams", null,
				0, -1, PatchSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchSet_ModelComparison(), theComparePackage.getComparison(), null, "modelComparison", null,
				0, 1, PatchSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchSet_DiagramComparison(), theComparePackage.getComparison(), null, "diagramComparison",
				null, 0, 1, PatchSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = createEGenericType(ecorePackage.getEMap());
		EGenericType g2 = createEGenericType(theEcorePackage.getEObject());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(ecorePackage.getEIntegerObject());
		g1.getETypeArguments().add(g2);
		initEAttribute(getPatchSet_ObjectChangeCount(), g1, "objectChangeCount", null, 0, 1, PatchSet.class,
				IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(ecorePackage.getEMap());
		g2 = createEGenericType(theEcorePackage.getEObject());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(ecorePackage.getEIntegerObject());
		g1.getETypeArguments().add(g2);
		initEAttribute(getPatchSet_ObjectChangeRefCount(), g1, "objectChangeRefCount", null, 0, 1, PatchSet.class,
				IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatchSet_MaxObjectChangeCount(), ecorePackage.getEInt(), "maxObjectChangeCount", "0", 0, 1,
				PatchSet.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatchSet_MaxObjectChangeRefCount(), ecorePackage.getEInt(), "maxObjectChangeRefCount", "0", 0,
				1, PatchSet.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				IS_DERIVED, IS_ORDERED);
		initEReference(getPatchSet_AllNewInvolvedDiagrams(), theNotationPackage.getDiagram(), null,
				"allNewInvolvedDiagrams", null, 0, -1, PatchSet.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
				!IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getPatchSet_AllOldInvolvedDiagrams(), theNotationPackage.getDiagram(), null,
				"allOldInvolvedDiagrams", null, 0, -1, PatchSet.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
				!IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(patchEClass, Patch.class, "Patch", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPatch_NewPath(), ecorePackage.getEString(), "newPath", null, 0, 1, Patch.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatch_OldPath(), ecorePackage.getEString(), "oldPath", null, 0, 1, Patch.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatch_NewContent(), ecorePackage.getEByteArray(), "newContent", null, 0, 1, Patch.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatch_OldContent(), ecorePackage.getEByteArray(), "oldContent", null, 0, 1, Patch.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatch_ChangeType(), this.getPatchChangeType(), "changeType", "ADD", 0, 1, Patch.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatch_PatchSet(), this.getPatchSet(), this.getPatchSet_Patches(), "patchSet", null, 0, 1,
				Patch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(diagramPatchEClass, DiagramPatch.class, "DiagramPatch", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDiagramPatch_NewDiagramInstance(), this.getDiagramInstance(), null, "newDiagramInstance",
				null, 0, 1, DiagramPatch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDiagramPatch_OldDiagramInstance(), this.getDiagramInstance(), null, "oldDiagramInstance",
				null, 0, 1, DiagramPatch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(modelPatchEClass, ModelPatch.class, "ModelPatch", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getModelPatch_NewModelInstance(), this.getModelInstance(), null, "newModelInstance", null, 0, 1,
				ModelPatch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelPatch_OldModelInstance(), this.getModelInstance(), null, "oldModelInstance", null, 0, 1,
				ModelPatch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(commentEClass, Comment.class, "Comment", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getComment_Id(), ecorePackage.getEString(), "id", null, 0, 1, Comment.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(modelInstanceEClass, ModelInstance.class, "ModelInstance", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getModelInstance_Objects(), ecorePackage.getEObject(), null, "objects", null, 0, -1,
				ModelInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelInstance_RootPackages(), theEcorePackage.getEPackage(), null, "rootPackages", null, 0,
				-1, ModelInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(diagramInstanceEClass, DiagramInstance.class, "DiagramInstance", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		initEOperation(getDiagramInstance__GetDiagrams(), theNotationPackage.getDiagram(), "getDiagrams", 0, -1,
				IS_UNIQUE, IS_ORDERED);

		initEClass(differenceOverlayEClass, DifferenceOverlay.class, "DifferenceOverlay", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDifferenceOverlay_LinkedView(), theNotationPackage.getView(), null, "linkedView", null, 0, 1,
				DifferenceOverlay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDifferenceOverlay_Differences(), this.getDifference(), null, "differences", null, 0, -1,
				DifferenceOverlay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(differenceEClass, Difference.class, "Difference", IS_ABSTRACT, IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDifference_RawDiffs(), theComparePackage.getDiff(), null, "rawDiffs", null, 0, -1,
				Difference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(layoutDifferenceEClass, LayoutDifference.class, "LayoutDifference", IS_ABSTRACT, IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(modelDifferenceEClass, ModelDifference.class, "ModelDifference", IS_ABSTRACT, IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(locationDifferenceEClass, LocationDifference.class, "LocationDifference", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLocationDifference_MoveDirection(), this.getVector(), "moveDirection", null, 0, 1,
				LocationDifference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(sizeDifferenceEClass, SizeDifference.class, "SizeDifference", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSizeDifference_WidthChange(), this.getDimensionChange(), "widthChange", null, 0, 1,
				SizeDifference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getSizeDifference_HeightChange(), this.getDimensionChange(), "heightChange", null, 0, 1,
				SizeDifference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(stateDifferenceEClass, StateDifference.class, "StateDifference", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStateDifference_Type(), this.getStateDifferenceType(), "type", null, 0, 1,
				StateDifference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(patchChangeTypeEEnum, PatchChangeType.class, "PatchChangeType");
		addEEnumLiteral(patchChangeTypeEEnum, PatchChangeType.ADD);
		addEEnumLiteral(patchChangeTypeEEnum, PatchChangeType.COPY);
		addEEnumLiteral(patchChangeTypeEEnum, PatchChangeType.DELETE);
		addEEnumLiteral(patchChangeTypeEEnum, PatchChangeType.MODIFY);
		addEEnumLiteral(patchChangeTypeEEnum, PatchChangeType.RENAME);

		initEEnum(stateDifferenceTypeEEnum, StateDifferenceType.class, "StateDifferenceType");
		addEEnumLiteral(stateDifferenceTypeEEnum, StateDifferenceType.ADDED);
		addEEnumLiteral(stateDifferenceTypeEEnum, StateDifferenceType.DELETED);
		addEEnumLiteral(stateDifferenceTypeEEnum, StateDifferenceType.MODIFIED);
		addEEnumLiteral(stateDifferenceTypeEEnum, StateDifferenceType.UNKNOWN);

		initEEnum(dimensionChangeEEnum, DimensionChange.class, "DimensionChange");
		addEEnumLiteral(dimensionChangeEEnum, DimensionChange.SMALLER);
		addEEnumLiteral(dimensionChangeEEnum, DimensionChange.BIGGER);
		addEEnumLiteral(dimensionChangeEEnum, DimensionChange.UNKNOWN);

		// Initialize data types
		initEDataType(vectorEDataType, Vector.class, "Vector", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} // ModelReviewPackageImpl

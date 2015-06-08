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

import at.bitandart.zoubek.mervin.model.modelreview.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class ModelReviewFactoryImpl extends EFactoryImpl implements
		ModelReviewFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static ModelReviewFactory init() {
		try {
			ModelReviewFactory theModelReviewFactory = (ModelReviewFactory) EPackage.Registry.INSTANCE
					.getEFactory(ModelReviewPackage.eNS_URI);
			if (theModelReviewFactory != null) {
				return theModelReviewFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ModelReviewFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelReviewFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case ModelReviewPackage.MODEL_REVIEW:
			return createModelReview();
		case ModelReviewPackage.PATCH_SET:
			return createPatchSet();
		case ModelReviewPackage.PATCH:
			return createPatch();
		case ModelReviewPackage.DIAGRAM_PATCH:
			return createDiagramPatch();
		case ModelReviewPackage.MODEL_PATCH:
			return createModelPatch();
		case ModelReviewPackage.MODEL_INSTANCE:
			return createModelInstance();
		case ModelReviewPackage.DIAGRAM_INSTANCE:
			return createDiagramInstance();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName()
					+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelReview createModelReview() {
		ModelReviewImpl modelReview = new ModelReviewImpl();
		return modelReview;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchSet createPatchSet() {
		PatchSetImpl patchSet = new PatchSetImpl();
		return patchSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Patch createPatch() {
		PatchImpl patch = new PatchImpl();
		return patch;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramPatch createDiagramPatch() {
		DiagramPatchImpl diagramPatch = new DiagramPatchImpl();
		return diagramPatch;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelPatch createModelPatch() {
		ModelPatchImpl modelPatch = new ModelPatchImpl();
		return modelPatch;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelInstance createModelInstance() {
		ModelInstanceImpl modelInstance = new ModelInstanceImpl();
		return modelInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramInstance createDiagramInstance() {
		DiagramInstanceImpl diagramInstance = new DiagramInstanceImpl();
		return diagramInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelReviewPackage getModelReviewPackage() {
		return (ModelReviewPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ModelReviewPackage getPackage() {
		return ModelReviewPackage.eINSTANCE;
	}

} // ModelReviewFactoryImpl

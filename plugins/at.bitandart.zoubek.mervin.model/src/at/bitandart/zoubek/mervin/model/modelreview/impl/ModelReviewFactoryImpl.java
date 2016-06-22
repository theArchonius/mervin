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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
public class ModelReviewFactoryImpl extends EFactoryImpl implements ModelReviewFactory {
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
		case ModelReviewPackage.COMMENT:
			return createComment();
		case ModelReviewPackage.MODEL_RESOURCE:
			return createModelResource();
		case ModelReviewPackage.DIAGRAM_RESOURCE:
			return createDiagramResource();
		case ModelReviewPackage.NODE_DIFFERENCE_OVERLAY:
			return createNodeDifferenceOverlay();
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY:
			return createEdgeDifferenceOverlay();
		case ModelReviewPackage.LOCATION_DIFFERENCE:
			return createLocationDifference();
		case ModelReviewPackage.SIZE_DIFFERENCE:
			return createSizeDifference();
		case ModelReviewPackage.STATE_DIFFERENCE:
			return createStateDifference();
		case ModelReviewPackage.BENDPOINTS_DIFFERENCE:
			return createBendpointsDifference();
		case ModelReviewPackage.COMMENT_LINK:
			return createCommentLink();
		case ModelReviewPackage.USER:
			return createUser();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
		case ModelReviewPackage.PATCH_CHANGE_TYPE:
			return createPatchChangeTypeFromString(eDataType, initialValue);
		case ModelReviewPackage.STATE_DIFFERENCE_TYPE:
			return createStateDifferenceTypeFromString(eDataType, initialValue);
		case ModelReviewPackage.DIMENSION_CHANGE:
			return createDimensionChangeFromString(eDataType, initialValue);
		case ModelReviewPackage.VECTOR:
			return createVectorFromString(eDataType, initialValue);
		case ModelReviewPackage.DIMENSION:
			return createDimensionFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case ModelReviewPackage.PATCH_CHANGE_TYPE:
			return convertPatchChangeTypeToString(eDataType, instanceValue);
		case ModelReviewPackage.STATE_DIFFERENCE_TYPE:
			return convertStateDifferenceTypeToString(eDataType, instanceValue);
		case ModelReviewPackage.DIMENSION_CHANGE:
			return convertDimensionChangeToString(eDataType, instanceValue);
		case ModelReviewPackage.VECTOR:
			return convertVectorToString(eDataType, instanceValue);
		case ModelReviewPackage.DIMENSION:
			return convertDimensionToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
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
	public Comment createComment() {
		CommentImpl comment = new CommentImpl();
		return comment;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelResource createModelResource() {
		ModelResourceImpl modelResource = new ModelResourceImpl();
		return modelResource;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramResource createDiagramResource() {
		DiagramResourceImpl diagramResource = new DiagramResourceImpl();
		return diagramResource;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NodeDifferenceOverlay createNodeDifferenceOverlay() {
		NodeDifferenceOverlayImpl nodeDifferenceOverlay = new NodeDifferenceOverlayImpl();
		return nodeDifferenceOverlay;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EdgeDifferenceOverlay createEdgeDifferenceOverlay() {
		EdgeDifferenceOverlayImpl edgeDifferenceOverlay = new EdgeDifferenceOverlayImpl();
		return edgeDifferenceOverlay;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LocationDifference createLocationDifference() {
		LocationDifferenceImpl locationDifference = new LocationDifferenceImpl();
		return locationDifference;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SizeDifference createSizeDifference() {
		SizeDifferenceImpl sizeDifference = new SizeDifferenceImpl();
		return sizeDifference;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public StateDifference createStateDifference() {
		StateDifferenceImpl stateDifference = new StateDifferenceImpl();
		return stateDifference;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public BendpointsDifference createBendpointsDifference() {
		BendpointsDifferenceImpl bendpointsDifference = new BendpointsDifferenceImpl();
		return bendpointsDifference;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public CommentLink createCommentLink() {
		CommentLinkImpl commentLink = new CommentLinkImpl();
		return commentLink;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public User createUser() {
		UserImpl user = new UserImpl();
		return user;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchChangeType createPatchChangeTypeFromString(EDataType eDataType, String initialValue) {
		PatchChangeType result = PatchChangeType.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertPatchChangeTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public StateDifferenceType createStateDifferenceTypeFromString(EDataType eDataType, String initialValue) {
		StateDifferenceType result = StateDifferenceType.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertStateDifferenceTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DimensionChange createDimensionChangeFromString(EDataType eDataType, String initialValue) {
		DimensionChange result = DimensionChange.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertDimensionChangeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Vector createVectorFromString(EDataType eDataType, String initialValue) {
		return (Vector) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertVectorToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Dimension createDimensionFromString(EDataType eDataType, String initialValue) {
		return (Dimension) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertDimensionToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
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

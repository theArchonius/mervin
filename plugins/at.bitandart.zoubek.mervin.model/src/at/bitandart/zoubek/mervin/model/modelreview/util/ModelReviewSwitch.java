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
package at.bitandart.zoubek.mervin.model.modelreview.util;

import at.bitandart.zoubek.mervin.model.modelreview.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance
 * hierarchy. It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object and proceeding up the
 * inheritance hierarchy until a non-null result is returned, which is the
 * result of the switch. <!-- end-user-doc -->
 * 
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage
 * @generated
 */
public class ModelReviewSwitch<T> extends Switch<T> {
	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static ModelReviewPackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelReviewSwitch() {
		if (modelPackage == null) {
			modelPackage = ModelReviewPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns
	 * a non null result; it yields that result. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code>
	 *         call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
		case ModelReviewPackage.MODEL_REVIEW: {
			ModelReview modelReview = (ModelReview) theEObject;
			T result = caseModelReview(modelReview);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.PATCH_SET: {
			PatchSet patchSet = (PatchSet) theEObject;
			T result = casePatchSet(patchSet);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.PATCH: {
			Patch patch = (Patch) theEObject;
			T result = casePatch(patch);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.DIAGRAM_PATCH: {
			DiagramPatch diagramPatch = (DiagramPatch) theEObject;
			T result = caseDiagramPatch(diagramPatch);
			if (result == null)
				result = casePatch(diagramPatch);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.MODEL_PATCH: {
			ModelPatch modelPatch = (ModelPatch) theEObject;
			T result = caseModelPatch(modelPatch);
			if (result == null)
				result = casePatch(modelPatch);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.COMMENT: {
			Comment comment = (Comment) theEObject;
			T result = caseComment(comment);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.MODEL_RESOURCE: {
			ModelResource modelResource = (ModelResource) theEObject;
			T result = caseModelResource(modelResource);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.DIAGRAM_RESOURCE: {
			DiagramResource diagramResource = (DiagramResource) theEObject;
			T result = caseDiagramResource(diagramResource);
			if (result == null)
				result = caseModelResource(diagramResource);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.DIFFERENCE_OVERLAY: {
			DifferenceOverlay differenceOverlay = (DifferenceOverlay) theEObject;
			T result = caseDifferenceOverlay(differenceOverlay);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.NODE_DIFFERENCE_OVERLAY: {
			NodeDifferenceOverlay nodeDifferenceOverlay = (NodeDifferenceOverlay) theEObject;
			T result = caseNodeDifferenceOverlay(nodeDifferenceOverlay);
			if (result == null)
				result = caseDifferenceOverlay(nodeDifferenceOverlay);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.EDGE_DIFFERENCE_OVERLAY: {
			EdgeDifferenceOverlay edgeDifferenceOverlay = (EdgeDifferenceOverlay) theEObject;
			T result = caseEdgeDifferenceOverlay(edgeDifferenceOverlay);
			if (result == null)
				result = caseDifferenceOverlay(edgeDifferenceOverlay);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.DIFFERENCE: {
			Difference difference = (Difference) theEObject;
			T result = caseDifference(difference);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.LAYOUT_DIFFERENCE: {
			LayoutDifference layoutDifference = (LayoutDifference) theEObject;
			T result = caseLayoutDifference(layoutDifference);
			if (result == null)
				result = caseDifference(layoutDifference);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.MODEL_DIFFERENCE: {
			ModelDifference modelDifference = (ModelDifference) theEObject;
			T result = caseModelDifference(modelDifference);
			if (result == null)
				result = caseDifference(modelDifference);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.LOCATION_DIFFERENCE: {
			LocationDifference locationDifference = (LocationDifference) theEObject;
			T result = caseLocationDifference(locationDifference);
			if (result == null)
				result = caseLayoutDifference(locationDifference);
			if (result == null)
				result = caseDifference(locationDifference);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.SIZE_DIFFERENCE: {
			SizeDifference sizeDifference = (SizeDifference) theEObject;
			T result = caseSizeDifference(sizeDifference);
			if (result == null)
				result = caseLayoutDifference(sizeDifference);
			if (result == null)
				result = caseDifference(sizeDifference);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.STATE_DIFFERENCE: {
			StateDifference stateDifference = (StateDifference) theEObject;
			T result = caseStateDifference(stateDifference);
			if (result == null)
				result = caseModelDifference(stateDifference);
			if (result == null)
				result = caseDifference(stateDifference);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.BENDPOINTS_DIFFERENCE: {
			BendpointsDifference bendpointsDifference = (BendpointsDifference) theEObject;
			T result = caseBendpointsDifference(bendpointsDifference);
			if (result == null)
				result = caseLayoutDifference(bendpointsDifference);
			if (result == null)
				result = caseDifference(bendpointsDifference);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelReviewPackage.COMMENT_LINK: {
			CommentLink commentLink = (CommentLink) theEObject;
			T result = caseCommentLink(commentLink);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Model Review</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Model Review</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelReview(ModelReview object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Patch Set</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Patch Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePatchSet(PatchSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Patch</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Patch</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePatch(Patch object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Diagram Patch</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Diagram Patch</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramPatch(DiagramPatch object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Model Patch</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Model Patch</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelPatch(ModelPatch object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Comment</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Comment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComment(Comment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Model Resource</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Model Resource</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelResource(ModelResource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Diagram Resource</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Diagram Resource</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramResource(DiagramResource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Difference Overlay</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Difference Overlay</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDifferenceOverlay(DifferenceOverlay object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Node Difference Overlay</em>'. <!-- begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Node Difference Overlay</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeDifferenceOverlay(NodeDifferenceOverlay object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Edge Difference Overlay</em>'. <!-- begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Edge Difference Overlay</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEdgeDifferenceOverlay(EdgeDifferenceOverlay object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Difference</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Difference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDifference(Difference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Layout Difference</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Layout Difference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLayoutDifference(LayoutDifference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Model Difference</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Model Difference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelDifference(ModelDifference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Location Difference</em>'. <!-- begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Location Difference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLocationDifference(LocationDifference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Size Difference</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Size Difference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSizeDifference(SizeDifference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>State Difference</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>State Difference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStateDifference(StateDifference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Bendpoints Difference</em>'. <!-- begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Bendpoints Difference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBendpointsDifference(BendpointsDifference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Comment Link</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Comment Link</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCommentLink(CommentLink object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>EObject</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch, but this is
	 * the last case anyway. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} // ModelReviewSwitch

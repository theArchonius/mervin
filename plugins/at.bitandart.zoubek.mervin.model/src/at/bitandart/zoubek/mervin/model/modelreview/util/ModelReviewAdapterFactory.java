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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides
 * an adapter <code>createXXX</code> method for each class of the model. <!--
 * end-user-doc -->
 * 
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage
 * @generated
 */
public class ModelReviewAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static ModelReviewPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelReviewAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ModelReviewPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc --> This implementation returns <code>true</code> if
	 * the object is either the model's package or is an instance object of the
	 * model. <!-- end-user-doc -->
	 * 
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ModelReviewSwitch<Adapter> modelSwitch = new ModelReviewSwitch<Adapter>() {
		@Override
		public Adapter caseModelReview(ModelReview object) {
			return createModelReviewAdapter();
		}

		@Override
		public Adapter casePatchSet(PatchSet object) {
			return createPatchSetAdapter();
		}

		@Override
		public Adapter casePatch(Patch object) {
			return createPatchAdapter();
		}

		@Override
		public Adapter caseDiagramPatch(DiagramPatch object) {
			return createDiagramPatchAdapter();
		}

		@Override
		public Adapter caseModelPatch(ModelPatch object) {
			return createModelPatchAdapter();
		}

		@Override
		public Adapter caseComment(Comment object) {
			return createCommentAdapter();
		}

		@Override
		public Adapter caseModelInstance(ModelInstance object) {
			return createModelInstanceAdapter();
		}

		@Override
		public Adapter caseDiagramInstance(DiagramInstance object) {
			return createDiagramInstanceAdapter();
		}

		@Override
		public Adapter caseChangeOverlay(ChangeOverlay object) {
			return createChangeOverlayAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview
	 * <em>Model Review</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReview
	 * @generated
	 */
	public Adapter createModelReviewAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet
	 * <em>Patch Set</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a
	 * case when inheritance will catch all the cases anyway. <!-- end-user-doc
	 * -->
	 * 
	 * @return the new adapter.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet
	 * @generated
	 */
	public Adapter createPatchSetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Patch <em>Patch</em>}
	 * '. <!-- begin-user-doc --> This default implementation returns null so
	 * that we can easily ignore cases; it's useful to ignore a case when
	 * inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Patch
	 * @generated
	 */
	public Adapter createPatchAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch
	 * <em>Diagram Patch</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch
	 * @generated
	 */
	public Adapter createDiagramPatchAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelPatch
	 * <em>Model Patch</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelPatch
	 * @generated
	 */
	public Adapter createModelPatchAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment
	 * <em>Comment</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a
	 * case when inheritance will catch all the cases anyway. <!-- end-user-doc
	 * -->
	 * 
	 * @return the new adapter.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Comment
	 * @generated
	 */
	public Adapter createCommentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelInstance
	 * <em>Model Instance</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelInstance
	 * @generated
	 */
	public Adapter createModelInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance
	 * <em>Diagram Instance</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance
	 * @generated
	 */
	public Adapter createDiagramInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ChangeOverlay
	 * <em>Change Overlay</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ChangeOverlay
	 * @generated
	 */
	public Adapter createChangeOverlayAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case. <!-- begin-user-doc --> This
	 * default implementation returns null. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // ModelReviewAdapterFactory

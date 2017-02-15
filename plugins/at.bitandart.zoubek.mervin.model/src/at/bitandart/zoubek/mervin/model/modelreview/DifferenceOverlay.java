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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.notation.View;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Difference Overlay</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay#getLinkedView
 * <em>Linked View</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay#getDifferences
 * <em>Differences</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay#isCommented
 * <em>Commented</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay#getDependentOverlays
 * <em>Dependent Overlays</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay#getDependencies
 * <em>Dependencies</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDifferenceOverlay()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface DifferenceOverlay extends EObject {
	/**
	 * Returns the value of the '<em><b>Linked View</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Linked View</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Linked View</em>' reference.
	 * @see #setLinkedView(View)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDifferenceOverlay_LinkedView()
	 * @model
	 * @generated
	 */
	View getLinkedView();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay#getLinkedView
	 * <em>Linked View</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Linked View</em>' reference.
	 * @see #getLinkedView()
	 * @generated
	 */
	void setLinkedView(View value);

	/**
	 * Returns the value of the '<em><b>Differences</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Difference}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Differences</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Differences</em>' containment reference
	 *         list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDifferenceOverlay_Differences()
	 * @model containment="true"
	 * @generated
	 */
	EList<Difference> getDifferences();

	/**
	 * Returns the value of the '<em><b>Commented</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Commented</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Commented</em>' attribute.
	 * @see #setCommented(boolean)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDifferenceOverlay_Commented()
	 * @model
	 * @generated
	 */
	boolean isCommented();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay#isCommented
	 * <em>Commented</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Commented</em>' attribute.
	 * @see #isCommented()
	 * @generated
	 */
	void setCommented(boolean value);

	/**
	 * Returns the value of the '<em><b>Dependent Overlays</b></em>' reference
	 * list. The list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay}.
	 * It is bidirectional and its opposite is '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay#getDependencies
	 * <em>Dependencies</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependent Overlays</em>' reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Dependent Overlays</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDifferenceOverlay_DependentOverlays()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay#getDependencies
	 * @model opposite="dependencies"
	 * @generated
	 */
	EList<DifferenceOverlay> getDependentOverlays();

	/**
	 * Returns the value of the '<em><b>Dependencies</b></em>' reference list.
	 * The list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay}.
	 * It is bidirectional and its opposite is '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay#getDependentOverlays
	 * <em>Dependent Overlays</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependencies</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Dependencies</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDifferenceOverlay_Dependencies()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay#getDependentOverlays
	 * @model opposite="dependentOverlays"
	 * @generated
	 */
	EList<DifferenceOverlay> getDependencies();

} // DifferenceOverlay

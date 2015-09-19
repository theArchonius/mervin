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

import org.eclipse.emf.compare.Diff;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Change Overlay</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.ChangeOverlay#getDiff
 * <em>Diff</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getChangeOverlay()
 * @model
 * @generated
 */
public interface ChangeOverlay extends EObject {
	/**
	 * Returns the value of the '<em><b>Diff</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diff</em>' reference isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Diff</em>' reference.
	 * @see #setDiff(Diff)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getChangeOverlay_Diff()
	 * @model
	 * @generated
	 */
	Diff getDiff();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.ChangeOverlay#getDiff
	 * <em>Diff</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Diff</em>' reference.
	 * @see #getDiff()
	 * @generated
	 */
	void setDiff(Diff value);

} // ChangeOverlay

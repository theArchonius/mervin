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

import org.eclipse.emf.compare.Diff;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Difference</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.Difference#getRawDiffs
 * <em>Raw Diffs</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDifference()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface Difference extends EObject {
	/**
	 * Returns the value of the '<em><b>Raw Diffs</b></em>' reference list. The
	 * list contents are of type {@link org.eclipse.emf.compare.Diff}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Raw Diffs</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Raw Diffs</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getDifference_RawDiffs()
	 * @model
	 * @generated
	 */
	EList<Diff> getRawDiffs();

} // Difference

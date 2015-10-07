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
package at.bitandart.zoubek.mervin.model.modelreview.tests;

import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc --> A test case for the model object '
 * <em><b>Difference Overlay</b></em>'. <!-- end-user-doc -->
 * 
 * @generated
 */
public class DifferenceOverlayTest extends TestCase {

	/**
	 * The fixture for this Difference Overlay test case. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DifferenceOverlay fixture = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(DifferenceOverlayTest.class);
	}

	/**
	 * Constructs a new Difference Overlay test case with the given name. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DifferenceOverlayTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Difference Overlay test case. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void setFixture(DifferenceOverlay fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Difference Overlay test case. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DifferenceOverlay getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(ModelReviewFactory.eINSTANCE.createDifferenceOverlay());
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} // DifferenceOverlayTest

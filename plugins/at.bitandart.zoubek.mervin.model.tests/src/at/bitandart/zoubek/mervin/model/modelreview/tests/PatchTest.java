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
package at.bitandart.zoubek.mervin.model.modelreview.tests;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.Patch;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc --> A test case for the model object '
 * <em><b>Patch</b></em>'. <!-- end-user-doc -->
 * 
 * @generated
 */
public class PatchTest extends TestCase {

	/**
	 * The fixture for this Patch test case. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected Patch fixture = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(PatchTest.class);
	}

	/**
	 * Constructs a new Patch test case with the given name. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Patch test case. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected void setFixture(Patch fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Patch test case. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Patch getFixture() {
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
		setFixture(ModelReviewFactory.eINSTANCE.createPatch());
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

} // PatchTest

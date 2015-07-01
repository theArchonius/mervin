/*******************************************************************************
 * Copyright (c) 2015 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import at.bitandart.zoubek.mervin.util.vis.MathUtil;

/**
 * @author Florian Zoubek
 *
 */
public class MathUtilTest {

	@Test
	public void testMap_destRange_MAX_VALUE_null() {
		assertEquals(0.0, MathUtil.map(0.0, 0.0, Double.MAX_VALUE, 0.0, 1.0), Math.pow(10, -15));
	}
	
	@Test
	public void testMap_destRange_MAX_VALUE_null_inverted() {
		assertEquals(1.0, MathUtil.map(0.0, 0.0, Double.MAX_VALUE, 1.0, 0.0), Math.pow(10, -15));
	}
	
	@Test
	public void testMap_destRange_MAX_VALUE_one() {
		assertEquals(1.0, MathUtil.map(Double.MAX_VALUE, 0.0, Double.MAX_VALUE, 0.0, 1.0), Math.pow(10, -15));
	}
	
	@Test
	public void testMap_destRange_MAX_VALUE_one_inverted() {
		assertEquals(0.0, MathUtil.map(Double.MAX_VALUE, 0.0, Double.MAX_VALUE, 1.0, 0.0), Math.pow(10, -15));
	}

}

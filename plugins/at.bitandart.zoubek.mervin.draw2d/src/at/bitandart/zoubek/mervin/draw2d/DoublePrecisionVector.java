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
package at.bitandart.zoubek.mervin.draw2d;

import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Vector;

/**
 * An extension of the {@link Vector} class providing additional methods with at
 * least double precision.
 * 
 * @author Florian Zoubek
 *
 */
public class DoublePrecisionVector extends Vector {

	/**
	 * @see Vector#Vector(PrecisionPoint)
	 */
	public DoublePrecisionVector(PrecisionPoint point) {
		super(point);
	}

	/**
	 * creates a new {@link DoublePrecisionVector} with the values of the given
	 * vector.
	 */
	public DoublePrecisionVector(Vector vector) {
		this(vector.x, vector.y);
	}

	/**
	 * @see Vector#Vector(double, double)
	 */
	public DoublePrecisionVector(double x, double y) {
		super(x, y);
	}

	/**
	 * @see Vector#Vector(PrecisionPoint, PrecisionPoint)
	 */
	public DoublePrecisionVector(PrecisionPoint from, PrecisionPoint to) {
		super(from, to);
	}

	/**
	 * @see Vector#Vector(Vector, Vector)
	 */
	public DoublePrecisionVector(Vector from, Vector to) {
		super(from, to);
	}

	/**
	 * @return a new {@link PrecisionPoint} with the x and y values of this
	 *         vector.
	 */
	public PrecisionPoint toPrecisionPoint() {
		return new PrecisionPoint(x, y);
	}

	/**
	 * modifies this vector to be the perpendicular vector on the left hand side
	 * with respect to the common screen coordinate system where the positive
	 * x-axis points right and the positive y-axis points down.
	 * 
	 * @return this instance for convenience.
	 */
	public DoublePrecisionVector toLeftPerpendicularVectorScreen() {
		double x = this.x;
		double y = this.y;

		this.x = y;
		this.y = -x;

		return this;
	}

	/**
	 * creates a new vector that is perpendicular to this vector on the left
	 * hand side with respect to the common screen coordinate system where the
	 * positive x-axis points right and the positive y-axis points down.
	 * 
	 * @return the perpendicular vector.
	 */
	public DoublePrecisionVector getLeftPerpendicularVectorScreen() {
		return new DoublePrecisionVector(this).toLeftPerpendicularVectorScreen();
	}

	/**
	 * modifies this vector to be the perpendicular vector on the right hand
	 * side with respect to the common screen coordinate system where the
	 * positive x-axis points right and the positive y-axis points down.
	 * 
	 * @return this instance for convenience.
	 */
	public DoublePrecisionVector toRightPerpendicularVectorScreen() {
		double x = this.x;
		double y = this.y;

		this.x = -y;
		this.y = x;

		return this;
	}

	/**
	 * creates a new vector that is perpendicular to this vector on the right
	 * hand side with respect to the common screen coordinate system where the
	 * positive x-axis points right and the positive y-axis points down.
	 * 
	 * @return the perpendicular vector.
	 */
	public DoublePrecisionVector getRightPerpendicularVectorScreen() {
		return new DoublePrecisionVector(this).toRightPerpendicularVectorScreen();
	}

	/**
	 * normalizes this vector to length ~= 1.
	 * 
	 * @return this for convenience.
	 */
	public DoublePrecisionVector normalize() {
		return divide(this.getLength());
	}

	/**
	 * @return a normalized (length ~=1) copy of this vector.
	 */
	public DoublePrecisionVector getNormalizedDoublePrecision() {
		return new DoublePrecisionVector(this).divide(this.getLength());
	}

	/**
	 * adds the given vector to this vector.
	 * 
	 * @param summand
	 *            the vector to add.
	 * @return this for convenience.
	 */
	public DoublePrecisionVector add(Vector summand) {

		this.x = this.x + summand.x;
		this.y = this.y + summand.y;

		return this;
	}

	@Override
	public Vector getAdded(Vector other) {
		return getAddedDoublePrecision(other);
	}

	/**
	 * @param summand
	 *            the vector to add
	 * @return a copy of this vector with the given other vector added to it.
	 */
	public DoublePrecisionVector getAddedDoublePrecision(Vector summand) {
		return new DoublePrecisionVector(this).add(summand);
	}

	/**
	 * subtracts the given vector form this vector.
	 * 
	 * @param subtrahend
	 *            the vector to subtract
	 * @return this for convenience
	 */
	public DoublePrecisionVector subtract(Vector subtrahend) {

		this.x = this.x - subtrahend.x;
		this.y = this.y - subtrahend.y;

		return this;
	}

	@Override
	public Vector getSubtracted(Vector other) {
		return getSubtractedDoublePrecisicon(other);
	}

	/**
	 * @param subtrahend
	 *            the vector to subtract
	 * @return a copy of this vector with the given other vector subtracted from
	 *         it.
	 */
	public DoublePrecisionVector getSubtractedDoublePrecisicon(Vector subtrahend) {
		return new DoublePrecisionVector(this).subtract(subtrahend);
	}

	/**
	 * divides this vector with the given divisor.
	 * 
	 * @param divisor
	 * @return this for convenience
	 */
	public DoublePrecisionVector divide(double divisor) {

		this.x = this.x / divisor;
		this.y = this.y / divisor;

		return this;
	}

	@Override
	public Vector getDivided(double divisor) {
		return getDividedDoublePrecision(divisor);
	}

	/**
	 * @param divisor
	 * @return a copy of this vector, divided by the given divisor.
	 */
	public DoublePrecisionVector getDividedDoublePrecision(double divisor) {
		return new DoublePrecisionVector(this).divide(divisor);
	}

	/**
	 * multiplies this factor by the given factor.
	 * 
	 * @param factor
	 * @return this for convenience.
	 */
	public DoublePrecisionVector multiply(double factor) {

		this.x = this.x * factor;
		this.y = this.y * factor;

		return this;
	}

	@Override
	public Vector getMultiplied(double factor) {
		return getMultipliedDoublePrecision(factor);
	}

	/**
	 * @param factor
	 * @return a copy of this vector, multiplied by the given factor.
	 */
	public DoublePrecisionVector getMultipliedDoublePrecision(double factor) {
		return new DoublePrecisionVector(this).multiply(factor);
	}

	/**
	 * negates this vector.
	 * 
	 * @return this for convenience.
	 */
	public DoublePrecisionVector negate() {

		this.x = -this.x;
		this.y = -this.y;

		return this;
	}

	/**
	 * @return a negated copy of this vector.
	 */
	public DoublePrecisionVector getNegatedDoublePrecision() {
		return new DoublePrecisionVector(this).negate();
	}

}

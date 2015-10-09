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
package at.bitandart.zoubek.mervin.draw2d.figures.examples;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Vector;

import at.bitandart.zoubek.mervin.draw2d.figures.DirectionIndicator;

/**
 * Demonstrates the usage of {@link DirectionIndicator}.
 * 
 * @author Florian Zoubek
 *
 */
public class DirectionIndicatorExample extends BaseExample {

	public static void main(String[] args) {
		new DirectionIndicatorExample().run();
	}

	@Override
	protected void addChildFigures(IFigure parentFigure) {

		// creates 20 indicators with different angles between 0 and 360 degrees
		int steps = 20;
		int column = -1;
		int row = 0;
		for (int i = 0; i < steps; i++) {
			double angleDeg = 360.0 / (double) steps * i;

			if (i % 5 == 0) {
				column++;
				row = 0;
			}

			// convert degrees to direction vector
			double angleRad = Math.toRadians(angleDeg);
			Vector direction = new Vector(Math.cos(angleRad), Math.sin(angleRad));

			// create the indicator
			DirectionIndicator indicator = new DirectionIndicator(direction);

			// add info labels
			Label anglelabel = new Label(String.format("%.2f", angleDeg));
			int angleHeight = anglelabel.getTextBounds().height;
			parentFigure.add(anglelabel,
					new Rectangle(10 + 110 * row, column * 150 + 10, 100, anglelabel.getTextBounds().height));

			Label dirlabel = new Label(String.format("(%.2f , %.2f)", direction.x, direction.y));
			int dirHeight = dirlabel.getTextBounds().height;
			parentFigure.add(dirlabel, new Rectangle(10 + 110 * row, column * 150 + 20 + angleHeight, 100, dirHeight));

			// add the indicator
			parentFigure.add(indicator,
					new Rectangle(10 + 110 * row, column * 150 + 30 + dirHeight + angleHeight, 100, 50));

			row++;
		}
	}

}

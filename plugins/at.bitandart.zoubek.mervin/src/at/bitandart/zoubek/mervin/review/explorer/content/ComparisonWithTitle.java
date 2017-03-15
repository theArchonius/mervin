/*******************************************************************************
 * Copyright (c) 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.review.explorer.content;

import org.eclipse.emf.compare.Comparison;

/**
 * Represents an {@link Comparison} associated with a given title.
 * 
 * @author Florian Zoubek
 *
 */
public class ComparisonWithTitle {

	private Comparison comparison;
	private String title;

	public ComparisonWithTitle(Comparison comparison, String title) {
		this.comparison = comparison;
		this.title = title;
	}

	/**
	 * @return the {@link Comparison}.
	 */
	public Comparison getComparison() {
		return comparison;
	}

	/**
	 * @return the associated title.
	 */
	public String getTitle() {
		return title;
	};

}

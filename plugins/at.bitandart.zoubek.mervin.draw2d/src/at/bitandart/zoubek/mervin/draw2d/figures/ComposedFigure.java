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
package at.bitandart.zoubek.mervin.draw2d.figures;

import java.util.List;

import org.eclipse.draw2d.Figure;

/**
 * A {@link Figure} composed by one or more child {@link Figure}s. This figure
 * provides an post-construct initialization mechanism that is invoked when this
 * figure is added in a figure hierarchy, {@link #getChildren()} is called or
 * explicitly when {@link #initialize()} is invoked. This makes sure that the
 * figure's state is initialized properly through their constructors before
 * invoking the initialization mechanism, although the initialization mechanism
 * may be overridden or calls overrideable methods. The initialization state can
 * be retrieved by {@link #isInitialized()}. Subclasses must override
 * {@link #initializeFigure()} and {@link #initializeChildren()} to do the
 * initialization logic. {@link #notifyInitializationComplete()} may be
 * overridden to implement some post initialization logic.
 * 
 * @author Florian Zoubek
 *
 */
public abstract class ComposedFigure extends Figure implements IComposedFigure {

	private boolean initialized;

	public ComposedFigure() {
		initialized = false;
	}

	@Override
	public void addNotify() {
		if (!initialized) {
			initialize();
		}
		super.addNotify();
	}

	@Override
	public List<?> getChildren() {
		if (!initialized) {
			initialize();
		}
		return super.getChildren();
	}

	/**
	 * initializes the figure and its child figures
	 */
	public void initialize() {
		initializeFigure();
		initializeChildren();
		initialized = true;
		notifyInitializationComplete();
	}

	/**
	 * initializes the figure and its child figures
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Initializes the figure itself. Intended to be overridden by subclasses.
	 */
	abstract protected void initializeFigure();

	/**
	 * Initializes all child figures. Intended to be overridden by subclasses.
	 */
	abstract protected void initializeChildren();

	/**
	 * Called once the figure and its children have been initialized (hence,
	 * {@link #isInitialized()} will always return false). Intended to be
	 * overridden by subclasses, does nothing by default.
	 */
	protected void notifyInitializationComplete() {
		// intentionally left empty
	}

}

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
package at.bitandart.zoubek.mervin.diagram.diff.parts;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;

/**
 * A {@link ScalableFreeformRootEditPart} that contains the default layers and
 * an additional {@link #DIFF_HIGHLIGHT_LAYER} on top.
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramDiffRootEditPart extends DiagramRootEditPart {

	public DiagramDiffRootEditPart(MeasurementUnit measurementUnit) {
		super(measurementUnit);
	}

	public static final String DIFF_HIGHLIGHT_LAYER = "at.bitandart.zoubek.mervin.diagram.diff.highlight.layer";

	@Override
	protected void createLayers(LayeredPane layeredPane) {
		super.createLayers(layeredPane);
		layeredPane.add(new FreeformLayer(), DIFF_HIGHLIGHT_LAYER);
		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
		layout.setStretchMinorAxis(false);
		layout.setStretchMajorAxis(false);
		layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_TOPLEFT);
		getContentPane().setLayoutManager(new ViewportFillingLayout());
	}

	/**
	 * A {@link LayoutManager} that fills the nearest parent viewport with the
	 * first child.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ViewportFillingLayout extends AbstractLayout {

		@Override
		public void layout(IFigure container) {
			Viewport viewport = findParentViewport(container);
			Dimension preferredSize = new Dimension(0, 0);

			if (viewport != null) {
				preferredSize.setSize(viewport.getBounds().getSize());
				for (Object child : container.getChildren()) {
					if (child instanceof IFigure) {
						IFigure childFigure = (IFigure) child;
						childFigure.setBounds(Rectangle.SINGLETON.setLocation(0, 0).setSize(preferredSize));
						break;
					}
				}
			}
		}

		@Override
		protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
			Viewport viewport = findParentViewport(container);
			Dimension preferredSize = new Dimension(0, 0);

			if (viewport != null) {
				preferredSize.setSize(viewport.getBounds().getSize());
			}

			return preferredSize;
		}

		/**
		 * finds the nearest parent viewport of the figure.
		 * 
		 * @param figure
		 *            the figure to start the search.
		 * @return the nearest parent viewport instance or null if no viewport
		 *         could be found.
		 */
		private Viewport findParentViewport(IFigure figure) {
			IFigure parent = figure.getParent();
			while (parent != null) {
				if (parent instanceof Viewport) {
					return (Viewport) parent;
				}
				parent = parent.getParent();
			}
			return null;
		}

	}
}

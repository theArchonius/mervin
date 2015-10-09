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

import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Vector;

import at.bitandart.zoubek.mervin.draw2d.figures.ChangeOverlayNodeFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.ChangeOverlayNodeFigure.DimensionPropertyChangeType;
import at.bitandart.zoubek.mervin.draw2d.figures.ChangeType;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultChangeTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultOverlayLocator;
import at.bitandart.zoubek.mervin.draw2d.figures.OverlayLinkedFigureListener;

/**
 * Demonstrates the usage of {@link ChangeOverlayNodeFigure}.
 * 
 * @author Florian Zoubek
 *
 */
public class ChangeOverlayNodeFiguresExample extends BaseExample {

	private static final String PRIMARY_LAYER = "primary layer";
	private static final String OVERLAY_LAYER = "overlay layer";
	private DefaultChangeTypeStyleAdvisor styleAdvisor;

	public static void main(String[] args) {
		new ChangeOverlayNodeFiguresExample().run();
	}

	@Override
	protected IFigure createRootFigure() {

		LayeredPane layeredPane = new LayeredPane();

		/*
		 * the primary layer where all linked figures reside
		 */

		Layer primaryLayer = new Layer();
		primaryLayer.setOpaque(true);
		primaryLayer.setBackgroundColor(org.eclipse.draw2d.ColorConstants.white);
		GridLayout gridLayout = new GridLayout(10, true);
		gridLayout.horizontalSpacing = 75;
		gridLayout.verticalSpacing = 75;
		primaryLayer.setLayoutManager(gridLayout);
		primaryLayer.setBorder(new MarginBorder(100));
		layeredPane.add(primaryLayer, PRIMARY_LAYER);

		/*
		 * Overlay Figures should always be placed on a overlay layer with a
		 * delegating layout (or any layout that calls
		 * IOverlayFigure#updateBoundsfromLinkedFigures()) that is placed above
		 * the layer with the linked figures.
		 */

		Layer overlayLayer = new Layer();
		overlayLayer.setLayoutManager(new DelegatingLayout());
		layeredPane.add(overlayLayer, OVERLAY_LAYER);

		return layeredPane;
	}

	@Override
	protected void addChildFigures(IFigure parentFigure) {

		if (parentFigure instanceof LayeredPane) {

			LayeredPane layeredPane = (LayeredPane) parentFigure;
			Layer primaryLayer = layeredPane.getLayer(PRIMARY_LAYER);
			Layer overlayLayer = layeredPane.getLayer(OVERLAY_LAYER);

			styleAdvisor = new DefaultChangeTypeStyleAdvisor();

			OverlayLinkedFigureListener overlayLinkedFigureListener = new OverlayLinkedFigureListener(overlayLayer);

			for (ChangeType changeType : ChangeType.values()) {

				/* add the linked figures to the primary layer */

				Label label = new Label("figure");
				label.addFigureListener(overlayLinkedFigureListener);
				label.setBorder(new LineBorder());
				primaryLayer.add(label, getGridDataConstraint());

				Label labelWithComments = new Label("figure");
				labelWithComments.addFigureListener(overlayLinkedFigureListener);
				labelWithComments.setBorder(new LineBorder());
				primaryLayer.add(labelWithComments, getGridDataConstraint());

				Label widthResizedLabel = new Label("figure");
				widthResizedLabel.addFigureListener(overlayLinkedFigureListener);
				widthResizedLabel.setBorder(new LineBorder());
				primaryLayer.add(widthResizedLabel, getGridDataConstraint());

				Label heightResizedLabel = new Label("figure");
				heightResizedLabel.addFigureListener(overlayLinkedFigureListener);
				heightResizedLabel.setBorder(new LineBorder());
				primaryLayer.add(heightResizedLabel, getGridDataConstraint());

				Label resizedLabel = new Label("figure");
				resizedLabel.addFigureListener(overlayLinkedFigureListener);
				resizedLabel.setBorder(new LineBorder());
				primaryLayer.add(resizedLabel, getGridDataConstraint());

				Label resizedLabelWithComments = new Label("figure");
				resizedLabelWithComments.addFigureListener(overlayLinkedFigureListener);
				resizedLabelWithComments.setBorder(new LineBorder());
				primaryLayer.add(resizedLabelWithComments, getGridDataConstraint());

				Label movedLabel = new Label("figure");
				movedLabel.addFigureListener(overlayLinkedFigureListener);
				movedLabel.setBorder(new LineBorder());
				primaryLayer.add(movedLabel, getGridDataConstraint());

				Label movedLabelWithComments = new Label("figure");
				movedLabelWithComments.addFigureListener(overlayLinkedFigureListener);
				movedLabelWithComments.setBorder(new LineBorder());
				primaryLayer.add(movedLabelWithComments, getGridDataConstraint());

				Label movedResizedLabel = new Label("figure");
				movedResizedLabel.addFigureListener(overlayLinkedFigureListener);
				movedResizedLabel.setBorder(new LineBorder());
				primaryLayer.add(movedResizedLabel, getGridDataConstraint());

				Label movedResizedLabelWithComments = new Label("figure");
				movedResizedLabelWithComments.addFigureListener(overlayLinkedFigureListener);
				movedResizedLabelWithComments.setBorder(new LineBorder());
				primaryLayer.add(movedResizedLabelWithComments, getGridDataConstraint());

				/*
				 * Add the overlay figures to the overlay layer. In this case
				 * overlay figures are linked to other figures on the primary
				 * layer using the DefaultOverlayLocator. This locator simply
				 * delegates the bounds update command to
				 * IOverlayFigure#updateBoundsfromLinkedFigures(). Notice that
				 * the following figures demonstrate the usage but do not
				 * represent all possible variations.
				 */

				ChangeOverlayNodeFigure simpleOverlay = new ChangeOverlayNodeFigure(styleAdvisor, changeType);
				overlayLayer.add(simpleOverlay, new DefaultOverlayLocator(label));

				ChangeOverlayNodeFigure overlayWithComments = new ChangeOverlayNodeFigure(styleAdvisor, changeType);
				overlayWithComments.setShowCommentHint(true);
				overlayLayer.add(overlayWithComments, new DefaultOverlayLocator(labelWithComments));

				ChangeOverlayNodeFigure overlayWithWidthResize = new ChangeOverlayNodeFigure(styleAdvisor, changeType);
				overlayWithWidthResize.setBoundsWidthChangeType(DimensionPropertyChangeType.SMALLER);
				overlayLayer.add(overlayWithWidthResize, new DefaultOverlayLocator(widthResizedLabel));

				ChangeOverlayNodeFigure overlayWithHeightResize = new ChangeOverlayNodeFigure(styleAdvisor, changeType);
				overlayWithHeightResize.setBoundsHeightChangeType(DimensionPropertyChangeType.SMALLER);
				overlayLayer.add(overlayWithHeightResize, new DefaultOverlayLocator(heightResizedLabel));

				ChangeOverlayNodeFigure overlayWithResize = new ChangeOverlayNodeFigure(styleAdvisor, changeType);
				overlayWithResize.setBoundsHeightChangeType(DimensionPropertyChangeType.SMALLER);
				overlayWithResize.setBoundsWidthChangeType(DimensionPropertyChangeType.BIGGER);
				overlayLayer.add(overlayWithResize, new DefaultOverlayLocator(resizedLabel));

				ChangeOverlayNodeFigure overlayWithCommentsResize = new ChangeOverlayNodeFigure(styleAdvisor,
						changeType);
				overlayWithCommentsResize.setBoundsHeightChangeType(DimensionPropertyChangeType.SMALLER);
				overlayWithCommentsResize.setBoundsWidthChangeType(DimensionPropertyChangeType.SMALLER);
				overlayWithCommentsResize.setShowCommentHint(true);
				overlayLayer.add(overlayWithCommentsResize, new DefaultOverlayLocator(resizedLabelWithComments));

				ChangeOverlayNodeFigure overlayWithMove = new ChangeOverlayNodeFigure(styleAdvisor, changeType);
				overlayWithMove.setMoveDirection(new Vector(0, 1));
				overlayLayer.add(overlayWithMove, new DefaultOverlayLocator(movedLabel));

				ChangeOverlayNodeFigure overlayWithMoveResize = new ChangeOverlayNodeFigure(styleAdvisor, changeType);
				overlayWithMoveResize.setBoundsHeightChangeType(DimensionPropertyChangeType.BIGGER);
				overlayWithMoveResize.setBoundsWidthChangeType(DimensionPropertyChangeType.SMALLER);
				overlayWithMoveResize.setMoveDirection(new Vector(1, 1));
				overlayLayer.add(overlayWithMoveResize, new DefaultOverlayLocator(movedResizedLabel));

				ChangeOverlayNodeFigure overlayWithCommentsMove = new ChangeOverlayNodeFigure(styleAdvisor, changeType);
				overlayWithCommentsMove.setShowCommentHint(true);
				overlayWithCommentsMove.setMoveDirection(new Vector(-1, 0.5));
				overlayLayer.add(overlayWithCommentsMove, new DefaultOverlayLocator(movedLabelWithComments));

				ChangeOverlayNodeFigure overlayWithCommentsMoveResize = new ChangeOverlayNodeFigure(styleAdvisor,
						changeType);
				overlayWithCommentsMoveResize.setShowCommentHint(true);
				overlayWithCommentsMoveResize.setBoundsHeightChangeType(DimensionPropertyChangeType.SMALLER);
				overlayWithCommentsMoveResize.setBoundsWidthChangeType(DimensionPropertyChangeType.BIGGER);
				overlayWithCommentsMoveResize.setMoveDirection(new Vector(-1, 0.5));
				overlayLayer.add(overlayWithCommentsMoveResize,
						new DefaultOverlayLocator(movedResizedLabelWithComments));

			}

		}
	}

	/**
	 * @return the default grid data constraint for figures on the primary
	 *         layer.
	 */
	private GridData getGridDataConstraint() {
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData.heightHint = 50;
		gridData.widthHint = 75;
		return gridData;
	}

	@Override
	protected void cleanUp() {
		if (styleAdvisor != null) {
			styleAdvisor.dispose();
		}
	}

}

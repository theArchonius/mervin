/*******************************************************************************
 * Copyright (c) 2015, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.draw2d.figures.examples;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemsAwareFreeFormLayer;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimatableScrollPane;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

import at.bitandart.zoubek.mervin.draw2d.figures.FocusFreeformViewport;

/**
 * Demonstrates the usage of {@link FocusFreeformViewportExample}. The focused
 * figures can be modified by clicking on the figures, the focus mode can be
 * disable by pressing the 'f' key.
 * 
 * @author Florian Zoubek
 *
 */
@SuppressWarnings("restriction")
public class FocusFreeformViewportExample extends GMFExample {

	private FocusFreeformViewport focusFreeformViewport;

	public static void main(String[] args) {
		new FocusFreeformViewportExample().run();
	}

	@Override
	protected FreeformViewport createFreeformViewport() {

		focusFreeformViewport = new FocusFreeformViewport(ColorConstants.black, 128, 0.0f, true);
		focusFreeformViewport.setFocusEnabled(true);

		// add a key listener that allows enabling and disabling the focus mode
		getCanvas().addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// intentionally left empty
			}

			@Override
			public void keyReleased(KeyEvent e) {

				// use 'f' or 'F' to toggle focus mode on/off
				if (e.character == 'f' || e.character == 'F') {
					focusFreeformViewport.setFocusEnabled(!focusFreeformViewport.isFocusEnabled());
				}
			}

		});

		return focusFreeformViewport;
	}

	@Override
	protected void addChildFigures(IFigure parentFigure) {

		Layer primaryLayer = getPrimaryLayer();
		primaryLayer.setBackgroundColor(ColorConstants.white);
		primaryLayer.setOpaque(true);

		/*
		 * a mouse listener that allows the user to modify the list of focused
		 * figures by clicking
		 */
		FocusMouseListener focusMouseListener = new FocusMouseListener();

		// add some content

		// rectangles
		RectangleFigure rect1 = new RectangleFigure();
		rect1.addMouseListener(focusMouseListener);
		primaryLayer.add(rect1, new Rectangle(20, 42, 300, 176));

		RectangleFigure rect2 = new RectangleFigure();
		rect2.addMouseListener(focusMouseListener);
		primaryLayer.add(rect2, new Rectangle(74, 142, 230, 123));

		RectangleFigure rect3 = new RectangleFigure();
		rect3.addMouseListener(focusMouseListener);
		primaryLayer.add(rect3, new Rectangle(432, 334, 300, 297));

		RectangleFigure rect4 = new RectangleFigure();
		rect4.addMouseListener(focusMouseListener);
		primaryLayer.add(rect4, new Rectangle(237, 462, 120, 132));

		RectangleFigure rect5 = new RectangleFigure();
		rect5.addMouseListener(focusMouseListener);
		primaryLayer.add(rect5, new Rectangle(509, 0, 130, 375));

		/*
		 * the focused figures may also be contained in scrollpanes
		 */

		// add scrollpane

		ScrollPane scrollPane = new AnimatableScrollPane();
		scrollPane.setViewport(new FreeformViewport());
		FreeformLayer layer = new BorderItemsAwareFreeFormLayer();
		layer.setOpaque(true);
		layer.setBackgroundColor(ColorConstants.white);
		layer.setLayoutManager(new FreeFormLayoutEx());
		scrollPane.setContents(layer);
		scrollPane.setBorder(new LineBorder());

		primaryLayer.add(scrollPane, new Rectangle(5, 250, 300, 375));

		// add scrollpane content (same layout as above)

		RectangleFigure rect6 = new RectangleFigure();
		rect6.addMouseListener(focusMouseListener);
		layer.add(rect6, new Rectangle(20, 42, 300, 176));

		RectangleFigure rect7 = new RectangleFigure();
		rect7.addMouseListener(focusMouseListener);
		layer.add(rect7, new Rectangle(74, 142, 230, 123));

		RectangleFigure rect8 = new RectangleFigure();
		rect8.addMouseListener(focusMouseListener);
		layer.add(rect8, new Rectangle(432, 334, 300, 297));

		RectangleFigure rect9 = new RectangleFigure();
		rect9.addMouseListener(focusMouseListener);
		layer.add(rect9, new Rectangle(237, 462, 120, 132));

		RectangleFigure rect10 = new RectangleFigure();
		rect10.addMouseListener(focusMouseListener);
		layer.add(rect10, new Rectangle(509, 0, 130, 375));

		// set some of the figures to be focused

		focusFreeformViewport.addFocusFigure(rect4);
		focusFreeformViewport.addFocusFigure(rect10);

	}

	/**
	 * A {@link MouseListener} that adds/removes figures to/from the focused
	 * figures when a mouse button is pressed.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private final class FocusMouseListener implements MouseListener {
		@Override
		public void mousePressed(MouseEvent me) {
			Object source = me.getSource();
			if (source instanceof IFigure) {
				IFigure figure = (IFigure) source;
				if (focusFreeformViewport.getFocusedFigures().contains(figure)) {
					focusFreeformViewport.removeFocusFigure(figure);
				} else {
					focusFreeformViewport.addFocusFigure(figure);
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent me) {
			// Intentionally left empty
		}

		@Override
		public void mouseDoubleClicked(MouseEvent me) {
			// Intentionally left empty
		}
	}

}

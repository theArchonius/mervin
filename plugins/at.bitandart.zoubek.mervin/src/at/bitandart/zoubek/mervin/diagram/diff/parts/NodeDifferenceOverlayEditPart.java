/*******************************************************************************
 * Copyright (c) 2015, 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.diagram.diff.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;

import at.bitandart.zoubek.mervin.draw2d.figures.ChangeOverlayNodeFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.ChangeOverlayNodeFigure.DimensionPropertyChangeType;
import at.bitandart.zoubek.mervin.draw2d.figures.ChangeType;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.IOffScreenIndicator;
import at.bitandart.zoubek.mervin.draw2d.figures.offscreen.OffScreenChangeIndicator;
import at.bitandart.zoubek.mervin.model.modelreview.Difference;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.DimensionChange;
import at.bitandart.zoubek.mervin.model.modelreview.LocationDifference;
import at.bitandart.zoubek.mervin.model.modelreview.NodeDifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.SizeDifference;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifference;

/**
 * An {@link ShapeNodeEditPart} implementation for {@link NodeDifferenceOverlay}
 * s.
 * 
 * @author Florian Zoubek
 *
 */
public class NodeDifferenceOverlayEditPart extends AbstractDifferenceOverlayEditPart {

	/* old bounds outline figures */

	/**
	 * the outline figure for the old bounds of the linked view of this overlay,
	 * null if the bounds did not change.
	 */
	private IFigure oldBoundsOutline;

	/**
	 * the connector figure that connects the old bounds outline with the linked
	 * view of this overlay, null if the bounds did not change.
	 */
	private IFigure oldBoundsConnector;

	public NodeDifferenceOverlayEditPart(View view) {
		super(view);
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();

		DifferenceOverlay differenceOverlay = getDifferenceOverlay();
		ChangeOverlayNodeFigure changeOverlayNodeFigure = getChangeOverlayNodeFigure();
		OffScreenChangeIndicator offScreenChangeIndicator = getOffScreenChangeIndicator();

		if (differenceOverlay != null && changeOverlayNodeFigure != null) {

			changeOverlayNodeFigure.setShowCommentHint(differenceOverlay.isCommented());

			EList<Difference> differences = differenceOverlay.getDifferences();
			boolean noStateDifference = true;

			Vector originalLocation = null;
			Dimension originalDimension = null;

			for (Difference difference : differences) {

				if (difference instanceof StateDifference) {

					switch (((StateDifference) difference).getType()) {
					case ADDED:
						changeOverlayNodeFigure.setChangeType(ChangeType.ADDITION);
						if (offScreenChangeIndicator != null) {
							offScreenChangeIndicator.setChangeType(ChangeType.ADDITION);
						}
						noStateDifference = false;
						break;
					case DELETED:
						changeOverlayNodeFigure.setChangeType(ChangeType.DELETION);
						if (offScreenChangeIndicator != null) {
							offScreenChangeIndicator.setChangeType(ChangeType.DELETION);
						}
						noStateDifference = false;
						break;
					case MODIFIED:
						changeOverlayNodeFigure.setChangeType(ChangeType.MODIFICATION);
						if (offScreenChangeIndicator != null) {
							offScreenChangeIndicator.setChangeType(ChangeType.MODIFICATION);
						}
						noStateDifference = false;
						break;
					default:
						// do nothing
					}

				} else if (difference instanceof SizeDifference) {

					SizeDifference sizeDifference = (SizeDifference) difference;

					changeOverlayNodeFigure
							.setBoundsHeightChangeType(toDimensionPropertyChangeType(sizeDifference.getHeightChange()));
					changeOverlayNodeFigure
							.setBoundsWidthChangeType(toDimensionPropertyChangeType(sizeDifference.getWidthChange()));
					originalDimension = sizeDifference.getOriginalDimension();

				} else if (difference instanceof LocationDifference) {

					LocationDifference locationDifference = (LocationDifference) difference;
					changeOverlayNodeFigure.setMoveDirection(locationDifference.getMoveDirection());
					originalLocation = locationDifference.getOriginalLocation();
				}

			}
			if (noStateDifference) {
				changeOverlayNodeFigure.setChangeType(ChangeType.LAYOUT);
				if (offScreenChangeIndicator != null) {
					offScreenChangeIndicator.setChangeType(ChangeType.LAYOUT);
				}

				IFigure linkedFigure = getLinkedEditPart().getFigure();

				if (oldBoundsOutline == null) {

					/*
					 * create old bounds outline figures if they do not exist
					 * yet
					 */

					RectangleFigure outlineRectangle = new RectangleFigure();
					outlineRectangle.setFill(false);
					outlineRectangle.setVisible(false);
					outlineRectangle.setLineWidth(2);
					outlineRectangle.setLineStyle(SWT.LINE_CUSTOM);
					outlineRectangle.setLineDash(new float[] { 5.0f, 2.0f });
					oldBoundsOutline = outlineRectangle;

					Polyline connectorLine = new Polyline();
					connectorLine.setVisible(false);
					connectorLine.setLineWidth(2);
					connectorLine.setLineStyle(SWT.LINE_CUSTOM);
					connectorLine.setLineDash(new float[] { 5.0f, 2.0f });
					oldBoundsConnector = connectorLine;

					changeOverlayNodeFigure.getParent().add(oldBoundsOutline, null);
					changeOverlayNodeFigure.getParent().add(oldBoundsConnector, null);
				}
				/* update the constraints of the outline figure */
				changeOverlayNodeFigure.getParent().setConstraint(oldBoundsOutline,
						new OldBoundsLocator(originalLocation, originalDimension, linkedFigure));
				changeOverlayNodeFigure.getParent().setConstraint(oldBoundsConnector,
						new OldBoundsConnectorLocator(originalLocation, linkedFigure));

			} else {

				/*
				 * remove old bounds outline figures if they are not needed
				 * anymore
				 */
				if (oldBoundsOutline != null) {
					oldBoundsOutline.getParent().remove(oldBoundsOutline);
				}
				if (oldBoundsConnector != null) {
					oldBoundsConnector.getParent().remove(oldBoundsConnector);
				}
			}
		}

	}

	/**
	 * A {@link Locator} that is responsible to relocate the old bounds outline
	 * figure based on the old bounds and the current linked figure.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private static class OldBoundsLocator implements Locator {

		private Vector oldLocation;
		private Dimension oldDimension;
		private IFigure changedFigure;

		/**
		 * @param oldLocation
		 *            the old location or null if the location did not change.
		 * @param oldDimension
		 *            the old Dimension or null if the dimensions did not
		 *            change.
		 * @param changedFigure
		 *            the current figure of whose bounds have changed.
		 */
		public OldBoundsLocator(Vector oldLocation, Dimension oldDimension, IFigure changedFigure) {
			this.oldLocation = oldLocation;
			this.oldDimension = oldDimension;
			this.changedFigure = changedFigure;
		}

		@Override
		public void relocate(IFigure figure) {

			Rectangle linkedFigureBounds = changedFigure.getBounds();

			/*
			 * use the current bounds of the changed figure as fallback values
			 * and set the changed value later
			 */
			PrecisionRectangle bounds = new PrecisionRectangle(linkedFigureBounds);

			if (oldLocation != null) {
				bounds.setPreciseLocation(oldLocation.x, oldLocation.y);
			}

			if (oldDimension != null) {

				if (oldDimension.width > -1) {
					bounds.setPreciseWidth(oldDimension.preciseWidth());
				}

				if (oldDimension.height > -1) {
					bounds.setPreciseHeight(oldDimension.preciseHeight());
				}
			}

			/*
			 * the bounds are relative to the parent of the changed figure.
			 * However, the outline figure is not in the same parent, so
			 * transform the bounds relative to the outline figure's parent.
			 */
			changedFigure.getParent().translateToAbsolute(bounds);
			figure.getParent().translateToRelative(bounds);
			figure.setBounds(bounds);

		}

	}

	/**
	 * A {@link Locator} that is responsible to relocate the old bounds outline
	 * connector figure based on the old location and the current linked figure.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private static class OldBoundsConnectorLocator implements Locator {

		private Vector oldLocation;
		private IFigure changedFigure;

		/**
		 * @param oldLocation
		 *            the old location or null if the location did not change.
		 * @param changedFigure
		 *            the current figure of whose bounds have changed.
		 */
		public OldBoundsConnectorLocator(Vector oldLocation, IFigure changedFigure) {
			this.oldLocation = oldLocation;
			this.changedFigure = changedFigure;
		}

		@Override
		public void relocate(IFigure figure) {

			if (figure instanceof Polyline) {
				Rectangle linkedFigureBounds = changedFigure.getBounds();

				/*
				 * use the current location of the changed figure as fallback
				 * values and set the changed value later
				 */
				PrecisionPoint startPoint = new PrecisionPoint(linkedFigureBounds.getLocation());
				PrecisionPoint endPoint = new PrecisionPoint(linkedFigureBounds.getLocation());

				if (oldLocation != null) {
					endPoint.setPreciseLocation(oldLocation.x, oldLocation.y);
				}
				/*
				 * the points are relative to the parent of the changed figure.
				 * However, the connector figure is not in the same parent, so
				 * transform the bounds relative to the outline figure's parent.
				 */
				changedFigure.getParent().translateToAbsolute(endPoint);
				changedFigure.getParent().translateToAbsolute(startPoint);
				figure.getParent().translateToRelative(endPoint);
				figure.getParent().translateToRelative(startPoint);
				((Polyline) figure).setEndpoints(startPoint, endPoint);
			}
		}

	}

	@Override
	public void activate() {
		super.activate();
		getFigure().addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// Intentionally left empty
			}

			@Override
			public void mouseHover(MouseEvent arg0) {
				// Intentionally left empty
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				/*
				 * show the outline figures only if the mouse is over the
				 * overlay figure
				 */
				if (oldBoundsOutline != null) {
					oldBoundsOutline.setVisible(false);
				}
				if (oldBoundsConnector != null) {
					oldBoundsConnector.setVisible(false);
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

				/*
				 * show the outline figures only if the mouse is over the
				 * overlay figure
				 */
				if (oldBoundsOutline != null) {
					oldBoundsOutline.setVisible(true);
				}
				if (oldBoundsConnector != null) {
					oldBoundsConnector.setVisible(true);
				}
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// Intentionally left empty
			}
		});
	}

	@Override
	public void deactivate() {
		if (oldBoundsOutline != null) {
			oldBoundsOutline.getParent().remove(oldBoundsOutline);
		}
		super.deactivate();
	}

	/**
	 * converts a {@link DimensionChange} to a
	 * {@link DimensionPropertyChangeType}
	 * 
	 * @param dimensionChange
	 *            the {@link DimensionChange} to convert
	 * @return the corresponding {@link DimensionPropertyChangeType}
	 */
	private static DimensionPropertyChangeType toDimensionPropertyChangeType(DimensionChange dimensionChange) {
		switch (dimensionChange) {
		case SMALLER:
			return DimensionPropertyChangeType.SMALLER;
		case BIGGER:
			return DimensionPropertyChangeType.BIGGER;
		default:
		case EQUAL:
		case UNKNOWN:
			return DimensionPropertyChangeType.NONE;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#
	 * createNodeFigure()
	 */
	@Override
	protected NodeFigure createNodeFigure() {
		return new ChangeOverlayNodeFigure(getStyleAdvisor(), ChangeType.ADDITION);
	}

	@Override
	protected IOffScreenIndicator createOffScreenIndicator() {
		return new OffScreenChangeIndicator(getStyleAdvisor());
	}

	protected ChangeOverlayNodeFigure getChangeOverlayNodeFigure() {
		IFigure figure = getFigure();
		if (figure instanceof ChangeOverlayNodeFigure) {
			return (ChangeOverlayNodeFigure) figure;
		}
		return null;
	}

	protected OffScreenChangeIndicator getOffScreenChangeIndicator() {
		IOffScreenIndicator offScreenIndicator = getOffScreenIndicator();
		if (offScreenIndicator instanceof OffScreenChangeIndicator) {
			return (OffScreenChangeIndicator) offScreenIndicator;
		}
		return null;
	}

}

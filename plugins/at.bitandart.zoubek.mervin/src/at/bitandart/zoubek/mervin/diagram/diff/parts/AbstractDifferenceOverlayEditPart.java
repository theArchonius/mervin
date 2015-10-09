package at.bitandart.zoubek.mervin.diagram.diff.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.notation.View;

import at.bitandart.zoubek.mervin.draw2d.figures.DefaultChangeTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultOverlayLocator;
import at.bitandart.zoubek.mervin.draw2d.figures.OverlayLinkedFigureListener;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;

public abstract class AbstractDifferenceOverlayEditPart extends ShapeNodeEditPart implements IOverlayEditPart {

	protected DefaultChangeTypeStyleAdvisor styleAdvisor;
	protected IFigure prevLinkedFigure = null;

	public AbstractDifferenceOverlayEditPart(View view) {
		super(view);
		styleAdvisor = new DefaultChangeTypeStyleAdvisor();
	}

	/**
	 * convenience method to get the associated change overlay.
	 * 
	 * @return the associated {@link DifferenceOverlay} instance or null if the
	 *         semantic element is not an instance of {@link DifferenceOverlay}.
	 */
	public DifferenceOverlay getDifferenceOverlay() {

		EObject semanticElement = resolveSemanticElement();
		if (semanticElement instanceof DifferenceOverlay) {
			return (DifferenceOverlay) semanticElement;
		}
		return null;

	}

	protected Layer getParentOverlayLayer() {
		// TODO retrieve it from the parent edit part instead
		return (Layer) getFigure().getParent();
	}

	@Override
	public GraphicalEditPart getLinkedEditPart() {

		DifferenceOverlay changeOverlay = getDifferenceOverlay();
		if (changeOverlay != null) {

			Object editPart = getViewer().getEditPartRegistry().get(changeOverlay.getLinkedView());
			if (editPart instanceof GraphicalEditPart) {
				return (GraphicalEditPart) editPart;
			}

		}

		return null;
	}

	@Override
	public void deactivate() {
		super.deactivate();
		styleAdvisor.dispose();
	}

	@Override
	protected void refreshBounds() {
		// apply the constraint
		EditPart parent = getParent();
		GraphicalEditPart linkedEditPart = getLinkedEditPart();

		IFigure linkedFigure = null;

		if (linkedEditPart != null) {
			linkedFigure = linkedEditPart.getFigure();
		}
		if (parent instanceof GraphicalEditPart && linkedFigure != null) {

			if (linkedFigure != prevLinkedFigure) {
				linkedFigure.addFigureListener(new OverlayLinkedFigureListener(getParentOverlayLayer()));
				prevLinkedFigure = linkedFigure;
			}

			((GraphicalEditPart) parent).setLayoutConstraint(this, getFigure(),
					new DefaultOverlayLocator(linkedFigure));
		}
	}

}
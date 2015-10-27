package at.bitandart.zoubek.mervin.draw2d.figures.offscreen;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Abstract {@link Figure} that provides common implementations for most of the
 * methods defined by {@link IOffScreenIndicator}.
 * 
 * @author Florian Zoubek
 *
 */
public class AbstractOffScreenIndicator extends Figure implements IOffScreenIndicator {

	/**
	 * the set of linked figures by this indicator.
	 */
	protected Set<IFigure> linkedFigures = new HashSet<IFigure>();

	/**
	 * the container figure.
	 */
	private IFigure containerFigure;

	/**
	 * the figure listener used to update the visibility of this indicator.
	 */
	private FigureListener containerUpdateListener = new FigureListener() {

		@Override
		public void figureMoved(IFigure source) {
			revalidate();
		}

	};

	@Override
	public Rectangle getLinkedFiguresBounds() {

		Rectangle linkedBounds = null;

		for (IFigure linkedFigure : linkedFigures) {
			Rectangle bounds = linkedFigure.getBounds().getCopy();
			linkedFigure.translateToAbsolute(bounds);
			if (linkedBounds == null) {
				linkedBounds = bounds.getCopy();
			} else {
				linkedBounds.union(bounds);
			}
		}

		if (linkedBounds == null) {
			return new Rectangle();
		}

		return linkedBounds;

	}

	@Override
	public boolean areLinkedFiguresVisible() {
		Rectangle containerBounds = containerFigure.getBounds().getCopy();
		containerFigure.translateToAbsolute(containerBounds);

		Rectangle linkedFigureBounds = getLinkedFiguresBounds();

		return containerBounds.intersects(linkedFigureBounds);
	}

	@Override
	public void paint(Graphics graphics) {
		if (!areLinkedFiguresVisible()) {
			super.paint(graphics);
		}
	}

	@Override
	public void addLinkedFigure(IFigure linkedFigure) {
		this.linkedFigures.add(linkedFigure);
		revalidate();
		repaint();
	}

	@Override
	public void setContainerFigure(IFigure containerFigure) throws IllegalArgumentException {
		if (this.containerFigure != null) {
			this.containerFigure.removeFigureListener(containerUpdateListener);
		}
		this.containerFigure = containerFigure;
		if (this.containerFigure != null) {
			this.containerFigure.addFigureListener(containerUpdateListener);
		}
		revalidate();
		repaint();
	}

	@Override
	public IFigure getContainerFigure() {
		return containerFigure;
	}

	@Override
	public Set<IFigure> getLinkedFigures() {
		return Collections.unmodifiableSet(linkedFigures);
	}

	@Override
	public void removeLinkedFigure(IFigure linkedFigure) {
		linkedFigures.remove(linkedFigure);
	}

	@Override
	public void cleanUp() {

		if (containerFigure != null) {
			containerFigure.removeFigureListener(containerUpdateListener);
			containerFigure = null;
		}
		for (IFigure linkedFigure : linkedFigures) {
			linkedFigure.removeFigureListener(containerUpdateListener);
		}
		linkedFigures.clear();

	}

}
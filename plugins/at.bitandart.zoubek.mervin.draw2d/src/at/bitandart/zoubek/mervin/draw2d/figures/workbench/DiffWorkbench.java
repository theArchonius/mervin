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
package at.bitandart.zoubek.mervin.draw2d.figures.workbench;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ScrollPaneLayout;
import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.tooling.runtime.linklf.LinkLFShapeCompartmentEditPart;
import org.eclipse.swt.graphics.Image;

import at.bitandart.zoubek.mervin.draw2d.figures.ComposedClickable;

/**
 * Default implementation of {@link IDiffWorkbench}.
 * 
 * @author Florian Zoubek
 *
 */
public class DiffWorkbench extends LinkLFShapeCompartmentEditPart.ShapeCompartmentFigureEx implements IDiffWorkbench {

	/**
	 * the tray area figure
	 */
	private IFigure trayArea;

	/**
	 * the content area figure
	 */
	private IFigure contentArea;

	/**
	 * the layout for the content of the compartment
	 */
	private WorkbenchContentLayout contentLayout;

	/**
	 * indicates if all child figures have been created and initialized properly
	 */
	private boolean childrenInitialized = false;

	/**
	 * the current display mode of this workbench
	 */
	private DisplayMode displayMode;

	/**
	 * the action listener for events related to container elements
	 */
	private ContainerElementActionListener containerElementActionListener;

	/**
	 * the image icon for the display mode switch button to
	 * {@link DisplayMode#WINDOW}
	 */
	private Image btnImageWindowMode;

	/**
	 * the image icon for the display mode switch button to
	 * {@link DisplayMode#TAB}
	 */
	private Image btnImageTabMode;

	/**
	 * the image icon for buttons which send containers to the tray
	 */
	private Image btnImageToTray;

	/**
	 * the image icon for buttons which send containers to the content area
	 */
	private Image btnImageToContent;

	/**
	 * creates a {@link DiffWorkbench} instance with {@link DisplayMode#TAB}.
	 * 
	 * @param mapmode
	 */
	public DiffWorkbench(IMapMode mapmode, Image btnImageWindowMode, Image btnImageTabMode, Image btnImageToContent,
			Image btnImageToTray) {
		this(DisplayMode.TAB, mapmode, btnImageWindowMode, btnImageTabMode, btnImageToContent, btnImageToTray);
	}

	/**
	 * creates a {@link DiffWorkbench} instance with {@link DisplayMode}.
	 * 
	 * @param displayMode
	 * @param mapmode
	 * @param btnImageWindowMode
	 *            the image icon for the display mode switch button to
	 *            {@link DisplayMode#WINDOW}
	 * @param btnImageTabMode
	 *            the image icon for the display mode switch button to
	 *            {@link DisplayMode#TAB}
	 * @param btnImageToContent
	 *            the image icon for buttons which send containers to the
	 *            content area
	 * @param btnImageToTray
	 *            the image icon for buttons which send containers to the tray
	 */
	public DiffWorkbench(DisplayMode displayMode, IMapMode mapmode, Image btnImageWindowMode, Image btnImageTabMode,
			Image btnImageToContent, Image btnImageToTray) {
		super("", mapmode);
		remove(getTextPane());
		remove(getScrollPane());
		this.displayMode = displayMode;
		containerElementActionListener = new ContainerElementActionListener();

		this.btnImageWindowMode = btnImageWindowMode;
		this.btnImageTabMode = btnImageTabMode;
		this.btnImageToContent = btnImageToContent;
		this.btnImageToTray = btnImageToTray;

	}

	/**
	 * creates and initializes all child figures.
	 */
	private void initializeChildFigures() {
		setLayoutManager(new BorderLayout());
		trayArea = createTrayArea();
		add(trayArea, BorderLayout.TOP);

		contentArea = getContentPane();
		contentLayout = new WorkbenchContentLayout();
		contentLayout.setDisplayMode(getDisplayMode());
		contentArea.setLayoutManager(contentLayout);

		ScrollPane scrollPane = getScrollPane();
		scrollPane.setLayoutManager(new ScrollPaneLayout());
		add(scrollPane, BorderLayout.CENTER);
		childrenInitialized = true;
	}

	@Override
	public List<?> getChildren() {
		if (!childrenInitialized) {
			initializeChildFigures();
		}
		return super.getChildren();
	}

	@Override
	public IFigure getContentArea() {
		if (!childrenInitialized) {
			initializeChildFigures();
		}
		return contentArea;
	}

	@Override
	public IFigure getTrayArea() {
		if (!childrenInitialized) {
			initializeChildFigures();
		}
		return trayArea;
	}

	/**
	 * creates the tray area, this method should only be called once and is
	 * called by default in {@link #initializeChildFigures()}.
	 * 
	 * @return the tray area figure
	 */
	protected IFigure createTrayArea() {
		Figure trayAreaFigure = new Figure() {
			@Override
			public Dimension getMinimumSize(int wHint, int hHint) {
				if (getChildren().isEmpty()) {
					return new Dimension(1, (int) (TextUtilities.INSTANCE.getAscent(getFont()) * 2.236));
				}
				return super.getMinimumSize(wHint, hHint);
			}
		};
		trayAreaFigure.setBackgroundColor(ColorConstants.darkGray);
		trayAreaFigure.setForegroundColor(ColorConstants.white);
		trayAreaFigure.setOpaque(true);
		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout(true);
		layout.setStretchMajorAxis(false);
		trayAreaFigure.setLayoutManager(layout);
		trayAreaFigure.setBorder(new MarginBorder(2, 2, 0, 2));
		return trayAreaFigure;
	}

	@Override
	public void addNotify() {
		super.addNotify();
		updateTray(null, getDisplayMode());
	}

	@Override
	public void validate() {
		updateTray(getDisplayMode(), getDisplayMode());
		super.validate();
	}

	/**
	 * updates the tray based on the content's child figures and the old and new
	 * display mode.
	 * 
	 * @param oldMode
	 *            the old display mode, may be null or equal to the new mode.
	 * @param newMode
	 *            the new display mode, may not be null.
	 */
	private void updateTray(DisplayMode oldMode, DisplayMode newMode) {

		if (!childrenInitialized) {
			initializeChildFigures();
		}

		notifyPreTrayUpdate();

		cleanUpWorkbenchFigures(oldMode, newMode);

		if (newMode == DisplayMode.TAB) {

			/*
			 * determine expected tray figures
			 */
			Set<IDiffWorkbenchTrayFigure> expectedTrayFigures = new HashSet<IDiffWorkbenchTrayFigure>();
			List<Object> childrenToRemove = new ArrayList<Object>(trayArea.getChildren().size());

			for (Object child : getContentArea().getChildren()) {
				if (child instanceof IDiffWorkbenchContainer) {
					expectedTrayFigures.add(((IDiffWorkbenchContainer) child).getTrayFigure());

				}
			}

			boolean hasActiveFigure = false;
			IDiffWorkbenchTrayFigure firstTrayFigure = null;
			/*
			 * leave existing expected tray figures and non tray figures in the
			 * tray, collect unexpected tray figures for removal, find the first
			 * tray figure, check if there is an active figure, and update the
			 * content panes if necessary
			 */
			for (Object child : trayArea.getChildren()) {
				if (child instanceof IDiffWorkbenchTrayFigure) {
					if (!expectedTrayFigures.remove(child)) {
						// unexpected tray figure -> remove it
						childrenToRemove.add(child);
					} else {
						IDiffWorkbenchTrayFigure trayFigure = (IDiffWorkbenchTrayFigure) child;
						if (firstTrayFigure == null) {
							firstTrayFigure = trayFigure;
						}
						hasActiveFigure = hasActiveFigure || trayFigure.getContainer().isActive();
						if (oldMode != DisplayMode.TAB) {
							// update the children of the tray figure
							clearTrayContentPane(trayFigure);
							addButtons(trayFigure, newMode);
						}
					}
				}
			}

			/*
			 * remove unexpected tray figures
			 */
			for (Object child : childrenToRemove) {
				if (child instanceof IFigure) {
					IFigure childFigure = (IFigure) child;
					trayArea.remove(childFigure);
					if (childFigure instanceof IDiffWorkbenchTrayFigure) {
						unregisterListeners((IDiffWorkbenchTrayFigure) childFigure);
					}
				}
			}

			// add missing expected tray figures
			for (IDiffWorkbenchTrayFigure trayFigure : expectedTrayFigures) {
				clearTrayContentPane(trayFigure);
				registerListeners(trayFigure);
				trayArea.add(trayFigure);
				addButtons(trayFigure, newMode);
				if (firstTrayFigure == null) {
					firstTrayFigure = trayFigure;
				}
				hasActiveFigure = hasActiveFigure || trayFigure.getContainer().isActive();
			}

			// if there is not active tray figure mark the first one as active
			if (!hasActiveFigure && firstTrayFigure != null) {
				firstTrayFigure.getContainer().setActive();
			}

		} else if (newMode == DisplayMode.WINDOW) {

			/*
			 * determine expected window title figures
			 */
			Set<IDiffWorkbenchWindowTitleFigure> validWindowTitleFigures = new HashSet<IDiffWorkbenchWindowTitleFigure>();

			for (Object child : getContentArea().getChildren()) {
				if (child instanceof IDiffWorkbenchContainer) {
					validWindowTitleFigures.add(((IDiffWorkbenchContainer) child).getWindowTitleFigure());
				}
			}

			Set<IDiffWorkbenchWindowTitleFigure> expectedWindowTitleFigures = new HashSet<IDiffWorkbenchWindowTitleFigure>(
					validWindowTitleFigures);

			removeUnexpectedWindowTitleFigures(expectedWindowTitleFigures, contentArea, oldMode);
			removeUnexpectedWindowTitleFigures(expectedWindowTitleFigures, trayArea, oldMode);

			// add missing expected title figures
			for (IDiffWorkbenchWindowTitleFigure titleFigure : expectedWindowTitleFigures) {
				registerListeners(titleFigure);
				clearFigure(titleFigure.getContentPane());
				contentArea.add(titleFigure);
				addButtons(titleFigure);
			}

			// update constraints for window title figures in the content area
			for (Object child : contentArea.getChildren()) {

				if (child instanceof IDiffWorkbenchWindowTitleFigure) {

					IDiffWorkbenchWindowTitleFigure windowTitleFigure = (IDiffWorkbenchWindowTitleFigure) child;
					Point containerRefPoint = windowTitleFigure.getContainer().getBounds().getBottomLeft();
					Object constraint = contentArea.getLayoutManager().getConstraint(windowTitleFigure);

					if (constraint == null || (constraint instanceof Rectangle
							&& !((Rectangle) constraint).getLocation().equals(containerRefPoint))) {
						contentArea.setConstraint(windowTitleFigure,
								new Rectangle(containerRefPoint, new Dimension(-1, -1)));
					}

				}
			}
		}

		notifyPostTrayUpdate();
	}

	/**
	 * removes unexpected {@link IDiffWorkbenchWindowTitleFigure}s in the given
	 * container and updates the set of expected title figures such that it
	 * contains only figures that are expected but not present in the given
	 * container.
	 * 
	 * @param expectedWindowTitleFigures
	 *            a set of expected window title figures
	 * @param container
	 *            the container to operate upon
	 * @param oldMode
	 *            the previous {@link DisplayMode}
	 */
	private void removeUnexpectedWindowTitleFigures(Set<IDiffWorkbenchWindowTitleFigure> expectedWindowTitleFigures,
			IFigure container, DisplayMode oldMode) {

		List<Object> childrenToRemove = new ArrayList<Object>(container.getChildren().size());
		/*
		 * leave existing expected title figures and non title figures in the
		 * content pane, collect unexpected title figures for removal, and
		 * update the content panes if necessary
		 */
		for (Object child : container.getChildren()) {
			if (child instanceof IDiffWorkbenchWindowTitleFigure) {
				if (!expectedWindowTitleFigures.remove(child)) {
					// unexpected title figure -> remove it
					childrenToRemove.add(child);
				} else {
					IDiffWorkbenchWindowTitleFigure windowTitleFigure = (IDiffWorkbenchWindowTitleFigure) child;

					if (oldMode != DisplayMode.WINDOW) {
						// update the children of the title figure
						clearWindowTitleContentPane(windowTitleFigure);
						addButtons(windowTitleFigure);
					}
				}
			}
		}

		/*
		 * remove unexpected title figures
		 */
		for (Object child : childrenToRemove) {
			if (child instanceof IFigure) {
				IFigure childFigure = (IFigure) child;
				container.remove(childFigure);
				if (childFigure instanceof IDiffWorkbenchWindowTitleFigure) {
					IDiffWorkbenchWindowTitleFigure windowTitleFigure = (IDiffWorkbenchWindowTitleFigure) childFigure;
					unregisterListeners(windowTitleFigure);
					clearWindowTitleContentPane(windowTitleFigure);
				}
			}
		}
	}

	/**
	 * removes all obsolete workbench figures for the given {@link DisplayMode}
	 * change (figures created for the old mode and that are not needed in the
	 * new mode) if there are any. This method does nothing if the mode did not
	 * change.
	 * 
	 * @param oldMode
	 *            the old {@link DisplayMode}
	 * @param newMode
	 *            the new {@link DisplayMode}
	 */
	private void cleanUpWorkbenchFigures(DisplayMode oldMode, DisplayMode newMode) {

		if (oldMode != newMode) {
			if (oldMode == DisplayMode.WINDOW) {
				// reset bounds set by the WorkbenchContentLayout
				resetContainerBounds();
				removeAllWindowTitleFigures();
				clearFigure(trayArea);
				trayArea.setBorder(new MarginBorder(2, 2, 0, 2));
			} else if (oldMode == DisplayMode.TAB) {
				clearFigure(trayArea);
				trayArea.setBorder(new MarginBorder(2, 2, 2, 2));
			}
		}

	}

	/**
	 * reset all {@link IDiffWorkbenchContainer} bounds in the content area to
	 * Rectangle(0,0,0,0).
	 */
	private void resetContainerBounds() {
		Rectangle emptyBounds = Rectangle.SINGLETON;
		emptyBounds.setBounds(0, 0, 0, 0);
		for (Object child : contentArea.getChildren()) {
			if (child instanceof IDiffWorkbenchContainer) {
				((IDiffWorkbenchContainer) child).setBounds(emptyBounds);
			}
		}
	}

	/**
	 * removes all {@link IDiffWorkbenchWindowTitleFigure}s from the content
	 * area
	 */
	private void removeAllWindowTitleFigures() {

		List<?> children = new ArrayList<>((List<?>) contentArea.getChildren());
		for (Object child : children) {
			if (child instanceof IDiffWorkbenchWindowTitleFigure) {
				IDiffWorkbenchWindowTitleFigure titleFigure = (IDiffWorkbenchWindowTitleFigure) child;
				contentArea.remove(titleFigure);
				clearWindowTitleContentPane(titleFigure);
				titleFigure.getContainer().setVisible(true);
				titleFigure.setVisible(true);
			}
		}

	}

	/**
	 * clears the content pane of the given
	 * {@link IDiffWorkbenchWindowTitleFigure}.
	 * 
	 * @param windowTitleFigure
	 *            the figure to clear.
	 */
	private void clearWindowTitleContentPane(IDiffWorkbenchWindowTitleFigure windowTitleFigure) {
		clearFigure(windowTitleFigure.getContentPane());
	}

	/**
	 * clears the content pane of the given {@link IDiffWorkbenchTrayFigure}.
	 * 
	 * @param trayFigure
	 *            the figure to clear.
	 */
	private void clearTrayContentPane(IDiffWorkbenchTrayFigure trayFigure) {
		clearFigure(trayFigure.getContentPane());
	}

	/**
	 * clears the children of the given figure.
	 * 
	 * @param figure
	 *            the figure whose children to clear.
	 */
	private void clearFigure(IFigure figure) {

		List<?> childrenCpy = new ArrayList<>((List<?>) figure.getChildren());
		for (Object child : childrenCpy) {
			if (child instanceof IFigure) {
				figure.remove((IFigure) child);
			}
		}

	}

	/**
	 * adds the default buttons to the given {@link IDiffWorkbenchTrayFigure}
	 * based on the given {@link DisplayMode}.
	 * 
	 * @param trayFigure
	 * @param displayMode
	 */
	private void addButtons(final IDiffWorkbenchTrayFigure trayFigure, DisplayMode displayMode) {

		if (displayMode == DisplayMode.TAB) {

			ImageButton windowModeBtn = new ImageButton(btnImageWindowMode);
			windowModeBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					setDisplayMode(DisplayMode.WINDOW);
				}
			});
			trayFigure.getContentPane().add(windowModeBtn);

		} else if (displayMode == DisplayMode.WINDOW) {

			// tab mode button
			ImageButton windowModeBtn = new ImageButton(btnImageTabMode);
			windowModeBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					setDisplayMode(DisplayMode.TAB);
				}
			});
			trayFigure.getContentPane().add(windowModeBtn);

			// to content button
			ImageButton toContentBtn = new ImageButton(btnImageToContent);
			toContentBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					sendToContentArea(trayFigure);
				}
			});
			trayFigure.getContentPane().add(toContentBtn);

		}

	}

	/**
	 * adds the default buttons to the given
	 * {@link IDiffWorkbenchWindowTitleFigure}.
	 * 
	 * @param titleFigure
	 */
	private void addButtons(final IDiffWorkbenchWindowTitleFigure titleFigure) {

		// tab mode button
		ImageButton windowModeBtn = new ImageButton(btnImageTabMode);
		windowModeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {

				Object source = event.getSource();

				if (source instanceof IFigure) {

					IWorkbenchContainerElement containerElement = findContainingContainerElement((IFigure) source);
					if (containerElement != null) {
						containerElement.getContainer().setActive();
					}
				}

				setDisplayMode(DisplayMode.TAB);
			}

			/**
			 * finds the {@link IWorkbenchContainerElement} of the parent
			 * hierarchy (inlcuding the given figure) of the given figure.
			 * 
			 * @param figure
			 *            the figure to find the containing
			 *            {@link IWorkbenchContainerElement} for.
			 * @return the containing {@link IWorkbenchContainerElement} or null
			 *         if the figure is not an
			 *         {@link IWorkbenchContainerElement} and is not contained
			 *         in an {@link IWorkbenchContainerElement}.
			 */
			private IWorkbenchContainerElement findContainingContainerElement(IFigure figure) {

				IFigure parent = figure;
				while (parent != null && !(parent instanceof IWorkbenchContainerElement)) {
					parent = parent.getParent();
				}

				if (parent instanceof IWorkbenchContainerElement) {
					return (IWorkbenchContainerElement) parent;
				}

				return null;
			}

		});
		titleFigure.getContentPane().add(windowModeBtn);

		// to tray Button
		ImageButton toTrayBtn = new ImageButton(btnImageToTray);
		toTrayBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				sendToTrayArea(titleFigure.getContainer());
			}
		});
		titleFigure.getContentPane().add(toTrayBtn);
	}

	/**
	 * registers the default listeners for the given
	 * {@link IDiffWorkbenchTrayFigure}.
	 * 
	 * @param trayFigure
	 */
	protected void registerListeners(IDiffWorkbenchTrayFigure trayFigure) {
		trayFigure.addActionListener(containerElementActionListener);
	}

	/**
	 * registers the default listeners for the given
	 * {@link IDiffWorkbenchWindowTitleFigure}.
	 * 
	 * @param titleFigure
	 */
	protected void registerListeners(IDiffWorkbenchWindowTitleFigure titleFigure) {
		titleFigure.addActionListener(containerElementActionListener);
	}

	/**
	 * removes the default listeners for the given
	 * {@link IDiffWorkbenchTrayFigure}.
	 * 
	 * @param trayFigure
	 */
	protected void unregisterListeners(IDiffWorkbenchTrayFigure trayFigure) {
		trayFigure.removeActionListener(containerElementActionListener);
	}

	/**
	 * removes the default listeners for the given
	 * {@link IDiffWorkbenchWindowTitleFigure}.
	 * 
	 * @param titleFigure
	 */
	protected void unregisterListeners(IDiffWorkbenchWindowTitleFigure titleFigure) {
		titleFigure.removeActionListener(containerElementActionListener);
	}

	@Override
	public DisplayMode getDisplayMode() {
		return displayMode;
	}

	@Override
	public void erase() {
		// btnImageWindowMode.dispose();
		super.erase();
	}

	@Override
	public void setDisplayMode(DisplayMode displayMode) {

		DisplayMode oldMode = getDisplayMode();

		notifyPreDisplayModeChange(oldMode, displayMode);

		this.displayMode = displayMode;
		contentLayout.setDisplayMode(displayMode);
		updateTray(oldMode, displayMode);
		getContentArea().revalidate();
		revalidate();
		repaint();

		notifyPostDisplayModeChange(oldMode, displayMode);

	}

	@Override
	public void add(IFigure figure, Object constraint, int index) {

		if (figure instanceof IDiffWorkbenchContainer) {
			addContainer((IDiffWorkbenchContainer) figure, constraint);
		} else {
			super.add(figure, constraint, index);
		}

	}

	@Override
	public void addContainer(IDiffWorkbenchContainer container, Object constraint) {
		getContentArea().add(container, constraint);
		registerContainer(container);
	}

	@Override
	public void registerContainer(IDiffWorkbenchContainer container) {
		container.setWorkbench(this);
	}

	@Override
	public void remove(IFigure figure) {
		if (figure instanceof IDiffWorkbenchContainer) {
			removeContainer((IDiffWorkbenchContainer) figure);
		} else {
			super.remove(figure);
		}
	}

	@Override
	public void removeContainer(IDiffWorkbenchContainer container) {
		getContentArea().remove(container);
		unregisterContainer(container);
	}

	@Override
	public void unregisterContainer(IDiffWorkbenchContainer container) {
		container.setWorkbench(null);
	}

	@Override
	public void sendToTrayArea(IDiffWorkbenchContainer container) {

		if (getDisplayMode() != DisplayMode.TAB) {

			// IFigure contentArea = getContentArea();
			// if (contentArea.getChildren().contains(container)) {
			// contentArea.remove(container);
			// }

			notifyPreSentToTrayArea(this, container);
			container.setVisible(false);
			container.getWindowTitleFigure().setVisible(false);

			IDiffWorkbenchTrayFigure trayFigure = container.getTrayFigure();
			if (!trayArea.getChildren().contains(trayFigure)) {
				clearTrayContentPane(trayFigure);
				trayArea.add(trayFigure);
				addButtons(trayFigure, getDisplayMode());
			}
			notifyPostSentToTrayArea(this, container);
		}

	}

	@Override
	public void sendToContentArea(IDiffWorkbenchContainer container) {

		if (getDisplayMode() != DisplayMode.TAB) {

			notifyPreSendToContentArea(this, container);
			container.setVisible(true);
			container.getWindowTitleFigure().setVisible(true);

			IDiffWorkbenchTrayFigure trayFigure = container.getTrayFigure();
			if (trayArea.getChildren().contains(trayFigure)) {
				trayArea.remove(trayFigure);
			}
			notifyPostSendToContentArea(this, container);
		}

	}

	@Override
	public void sendToContentArea(IDiffWorkbenchTrayFigure trayFigure) {
		sendToContentArea(trayFigure.getContainer());
	}

	@Override
	public void setActiveContainer(IDiffWorkbenchContainer container) {

		IFigure parent = container.getParent();
		if (parent != null) {

			IDiffWorkbenchContainer oldTopContainer = null;
			for (Object child : parent.getChildren()) {
				if (child instanceof IDiffWorkbenchContainer) {
					oldTopContainer = (IDiffWorkbenchContainer) child;
					break;
				}
			}
			notifyPreTopContainerChanged(this, oldTopContainer, container);

			LayoutManager parentLayoutManager = parent.getLayoutManager();
			Object constraint = null;
			if (parentLayoutManager != null) {
				constraint = parentLayoutManager.getConstraint(container);
			}
			parent.remove(container);
			parent.add(container, constraint, 0);

			notifyPostTopContainerChanged(this, oldTopContainer, container);
		}

	}

	@Override
	public IDiffWorkbenchContainer getActiveContainer() {

		List<?> contentChildren = getContentArea().getChildren();

		for (Object child : contentChildren) {
			if (child instanceof IDiffWorkbenchContainer) {
				return (IDiffWorkbenchContainer) child;
			}
		}

		return null;
	}

	@Override
	public boolean containsContainer(IDiffWorkbenchContainer container) {
		return getContentArea().getChildren().contains(container);
	}

	@Override
	public void addWorkbenchListener(IWorkbenchListener workbenchListener) {
		addListener(IWorkbenchListener.class, workbenchListener);
	}

	@Override
	public void removeWorkbenchListener(IWorkbenchListener workbenchListener) {
		removeListener(IWorkbenchListener.class, workbenchListener);
	}

	/**
	 * notify all listeners that pre-display mode change calls should be made
	 * 
	 * @param oldMode
	 * @param newMode
	 */
	protected void notifyPreDisplayModeChange(DisplayMode oldMode, DisplayMode newMode) {
		Iterator<?> listeners = getListeners(IWorkbenchListener.class);
		while (listeners.hasNext()) {
			((IWorkbenchListener) listeners.next()).preDisplayModeChange(this, oldMode, newMode);
		}
	}

	/**
	 * notify all listeners that post-display mode change calls should be made
	 * 
	 * @param oldMode
	 * @param newMode
	 */
	protected void notifyPostDisplayModeChange(DisplayMode oldMode, DisplayMode newMode) {
		Iterator<?> listeners = getListeners(IWorkbenchListener.class);
		while (listeners.hasNext()) {
			((IWorkbenchListener) listeners.next()).postDisplayModeChange(this, oldMode, newMode);
		}
	}

	/**
	 * notify all listeners that pre-tray update calls should be made
	 */
	protected void notifyPreTrayUpdate() {
		Iterator<?> listeners = getListeners(IWorkbenchListener.class);
		while (listeners.hasNext()) {
			((IWorkbenchListener) listeners.next()).preTrayUpdate(this);
		}
	}

	/**
	 * notify all listeners that post-tray update calls should be made
	 */
	protected void notifyPostTrayUpdate() {
		Iterator<?> listeners = getListeners(IWorkbenchListener.class);
		while (listeners.hasNext()) {
			((IWorkbenchListener) listeners.next()).postTrayUpdate(this);
		}
	}

	/**
	 * notify all listeners that pre-top container changed calls should be made
	 */
	protected void notifyPreTopContainerChanged(DiffWorkbench diffWorkbench, IDiffWorkbenchContainer oldTopContainer,
			IDiffWorkbenchContainer container) {
		Iterator<?> listeners = getListeners(IWorkbenchListener.class);
		while (listeners.hasNext()) {
			((IWorkbenchListener) listeners.next()).preTopContainerChanged(this, oldTopContainer, container);
		}
	}

	/**
	 * notify all listeners that post-top container changed calls should be made
	 */
	protected void notifyPostTopContainerChanged(DiffWorkbench diffWorkbench, IDiffWorkbenchContainer oldTopContainer,
			IDiffWorkbenchContainer container) {
		Iterator<?> listeners = getListeners(IWorkbenchListener.class);
		while (listeners.hasNext()) {
			((IWorkbenchListener) listeners.next()).postTopContainerChanged(this, oldTopContainer, container);
		}
	}

	/**
	 * notify all listeners that pre-send to content area calls should be made
	 */
	protected void notifyPreSendToContentArea(DiffWorkbench diffWorkbench, IDiffWorkbenchContainer container) {
		Iterator<?> listeners = getListeners(IWorkbenchListener.class);
		while (listeners.hasNext()) {
			((IWorkbenchListener) listeners.next()).preSendToContentArea(this, container);
		}
	}

	/**
	 * notify all listeners that post-send to content area calls should be made
	 */
	protected void notifyPostSendToContentArea(DiffWorkbench diffWorkbench, IDiffWorkbenchContainer container) {
		Iterator<?> listeners = getListeners(IWorkbenchListener.class);
		while (listeners.hasNext()) {
			((IWorkbenchListener) listeners.next()).postSendToContentArea(this, container);
		}
	}

	/**
	 * notify all listeners that pre-send to tray area calls should be made
	 */
	protected void notifyPreSentToTrayArea(DiffWorkbench diffWorkbench, IDiffWorkbenchContainer container) {
		Iterator<?> listeners = getListeners(IWorkbenchListener.class);
		while (listeners.hasNext()) {
			((IWorkbenchListener) listeners.next()).preSendToTrayArea(this, container);
		}
	}

	/**
	 * notify all listeners that post-send to tray area calls should be made
	 */
	protected void notifyPostSentToTrayArea(DiffWorkbench diffWorkbench, IDiffWorkbenchContainer container) {
		Iterator<?> listeners = getListeners(IWorkbenchListener.class);
		while (listeners.hasNext()) {
			((IWorkbenchListener) listeners.next()).postSendToTrayArea(this, container);
		}
	}

	/**
	 * A listener that handles the mouse events to determine the active
	 * workbench container.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public static class ContainerElementActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {

			Object source = event.getSource();
			if (source instanceof IWorkbenchContainerElement) {

				// mark the related container as active
				IWorkbenchContainerElement containerElement = (IWorkbenchContainerElement) source;
				containerElement.getContainer().setActive();
			}

		}

	}

	/**
	 * Represents a clickable image button figure.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public static class ImageButton extends ComposedClickable {

		private Image image;

		public ImageButton(Image image) {
			this.image = image;
		}

		@Override
		protected void initializeFigure() {
			setLayoutManager(new ConstrainedToolbarLayout(true));
			setBorder(new MarginBorder(3));
		}

		@Override
		protected void initializeChildren() {

			Label label = new Label(image);
			add(label);
		}

	}

}

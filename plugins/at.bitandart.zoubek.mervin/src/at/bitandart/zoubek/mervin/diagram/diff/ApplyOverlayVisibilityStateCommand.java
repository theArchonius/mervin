/*******************************************************************************
 * Copyright (c) 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.diagram.diff;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;

/**
 * A Command that updates the visibility of the view related to an overlay of a
 * particular type. If specified, the linked (overlayed) view's visibility is
 * also updated.
 * 
 * @author Florian Zoubek
 *
 */
final class ApplyOverlayVisibilityStateCommand extends AbstractTransactionalCommand {

	private Diagram diagram;
	private IOverlayVisibilityState overlayVisibilityState;

	/**
	 * 
	 * @param domain
	 *            the EMF editing domain to act upon.
	 * @param diagram
	 *            the diagram containing the overlays.
	 * @param overlayVisibilityState
	 *            an {@link IOverlayVisibilityState} describing the visibility
	 *            of the overlay types to set.
	 */
	public ApplyOverlayVisibilityStateCommand(TransactionalEditingDomain domain, Diagram diagram,
			IOverlayVisibilityState overlayVisibilityState) {
		super(domain, "", null);
		this.diagram = diagram;
		this.overlayVisibilityState = overlayVisibilityState;
	}

	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		for (IOverlayTypeDescriptor overlayDescriptor : overlayVisibilityState.getAllOverlayTypeDescriptors()) {
			overlayDescriptor.storeTypeVisibility(diagram, overlayVisibilityState.getVisibility(overlayDescriptor));
		}
		BiMap<View, DifferenceOverlay> viewToOverlayMap = HashBiMap.create();
		for (Object child : diagram.getChildren()) {
			if (child instanceof View) {
				buildViewToOverlayMap((View) child, viewToOverlayMap);
			}
		}
		BiMap<DifferenceOverlay, View> overlayToViewMap = viewToOverlayMap.inverse();
		Set<DifferenceOverlay> differenceOverlays = overlayToViewMap.keySet();
		Map<DifferenceOverlay, OverlayVisibility> visibilityCache = new HashMap<>();
		for (DifferenceOverlay overlay : differenceOverlays) {
			updateVisibility(overlay, overlayToViewMap, visibilityCache);
		}
		return CommandResult.newOKCommandResult();
	}

	/**
	 * builds the view to overlay index for the given view and stores it into
	 * the given {@link BiMap}.
	 * 
	 * @param view
	 *            the view to create the index for.
	 * @param viewToOverlayMap
	 *            the {@link BiMap} to store the index into.
	 */
	private void buildViewToOverlayMap(View view, BiMap<View, DifferenceOverlay> viewToOverlayMap) {

		EObject element = view.getElement();
		if (element instanceof DifferenceOverlay) {
			viewToOverlayMap.put(view, (DifferenceOverlay) element);
		}

		for (Object child : view.getChildren()) {
			if (child instanceof View) {
				buildViewToOverlayMap((View) child, viewToOverlayMap);
			}
		}

	}

	/**
	 * updates the visibility if the given overlay with respect to the overlays
	 * it depends on.
	 * 
	 * @param overlay
	 *            the overlay to update.
	 * @param overlayToViewMap
	 *            the index used to map an overlay to a view.
	 * @param visibilityCache
	 *            a cache to containing the already computed overlay
	 *            visibilities.
	 */
	private void updateVisibility(DifferenceOverlay overlay, BiMap<DifferenceOverlay, View> overlayToViewMap,
			Map<DifferenceOverlay, OverlayVisibility> visibilityCache) {

		OverlayVisibility overlayVisibility = calculateOverlayVisibility(overlay, visibilityCache,
				new HashSet<DifferenceOverlay>());

		View view = overlayToViewMap.get(overlay);
		if (view != null) {
			view.setVisible(overlayVisibility.isVisible());
			if (overlayVisibility.isUpdateOverlaidElement()) {
				View linkedView = overlay.getLinkedView();
				if (linkedView != null) {
					linkedView.setVisible(overlayVisibility.isVisible());
				}
			}
		}

	}

	/**
	 * calculates the visibility if the given overlay with respect to the
	 * overlays it depends on. The dependencies of the recurring overlay will be
	 * ignored if a dependency circle is detected.
	 * 
	 * @param overlay
	 *            the overlay to calculate the visibility for.
	 * @param visibilityCache
	 *            a cache to containing the already computed overlay
	 *            visibilities.
	 * @param dependentOverlays
	 *            the set of all dependent overlays found so far of the given
	 *            overlay.
	 * @return the calculated visibility state with respect to dependencies.
	 */
	public OverlayVisibility calculateOverlayVisibility(DifferenceOverlay overlay,
			Map<DifferenceOverlay, OverlayVisibility> visibilityCache, Set<DifferenceOverlay> dependentOverlays) {

		OverlayVisibility overlayVisibility = visibilityCache.get(overlay);
		if (overlayVisibility == null) {

			if (!dependentOverlays.contains(overlay)) {

				dependentOverlays.add(overlay);
				EList<DifferenceOverlay> dependencies = overlay.getDependencies();

				for (DifferenceOverlay dependency : dependencies) {

					OverlayVisibility dependentVisibility = calculateOverlayVisibility(dependency, visibilityCache,
							dependentOverlays);

					if (dependentVisibility.isUpdateOverlaidElement() && !dependentVisibility.isVisible()) {
						overlayVisibility = new OverlayVisibility(false, true);
						break;
					}
				}

				dependentOverlays.remove(overlay);
			}

			if (overlayVisibility == null) {
				overlayVisibility = new OverlayVisibility(isVisible(overlay), shouldUpdateOverlaidElement(overlay));
			}

			visibilityCache.put(overlay, overlayVisibility);
		}

		return overlayVisibility;

	}

	/**
	 * @param overlay
	 *            the overlay to retrieve the flag for.
	 * @return true if the overlaid element should be updated for the given
	 *         overlay without respect to the overlay's dependencies, false
	 *         otherwise
	 */
	public boolean shouldUpdateOverlaidElement(DifferenceOverlay overlay) {

		for (IOverlayTypeDescriptor overlayDescriptor : overlayVisibilityState.getAllOverlayTypeDescriptors()) {
			if (overlayDescriptor.isType(overlay)) {
				if (overlayVisibilityState.shouldUpdateOverlaidElement(overlayDescriptor)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param overlay
	 *            the overlay to retrieve the flag for.
	 * @return true if the overlay should be visible without respect to the
	 *         overlay's dependencies, false otherwise.
	 */
	public boolean isVisible(DifferenceOverlay overlay) {

		for (IOverlayTypeDescriptor overlayDescriptor : overlayVisibilityState.getAllOverlayTypeDescriptors()) {
			if (overlayDescriptor.isType(overlay)) {
				if (overlayVisibilityState.getVisibility(overlayDescriptor)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Helper class that holds the visibility information for an overlay.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class OverlayVisibility {

		private boolean visible = false;
		private boolean updateOverlaidElement = false;

		public OverlayVisibility(boolean visible, boolean updateOverlaidElement) {
			super();
			this.visible = visible;
			this.updateOverlaidElement = updateOverlaidElement;
		}

		public boolean isVisible() {
			return visible;
		}

		public boolean isUpdateOverlaidElement() {
			return updateOverlaidElement;
		}
	}

}
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
package at.bitandart.zoubek.mervin.review.explorer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeViewer;

import at.bitandart.zoubek.mervin.review.explorer.content.IReviewExplorerContentProvider;

/**
 * A {@link IRunnableWithProgress} that derives the set of objects to highlight
 * based on a set of elements in the review explorer view.
 * 
 * @author Florian Zoubek
 *
 */
public class ReviewExplorerHighlightUpdater implements IRunnableWithProgress {

	private Set<Object> baseElements;
	private Set<Object> objectsToHighlight;
	private TreeViewer treeViewer;
	private IReviewExplorerContentProvider contentProvider;
	private IEventBroker eventBroker;

	/**
	 * 
	 * @param baseElements
	 *            the set of elements to derive the highlighted objects from
	 * @param objectsToHighlight
	 *            the set of objects to store the highlighted object into.
	 * @param treeViewer
	 *            the {@link TreeViewer} that must be refreshed after the
	 *            highlighted elements have been computed.
	 * @param contentProvider
	 *            the {@link IReviewExplorerContentProvider} used to derive
	 *            objects.
	 * @param eventBroker
	 *            the {@link IEventBroker} to use for UI update requests
	 */
	public ReviewExplorerHighlightUpdater(Set<Object> baseElements, Set<Object> objectsToHighlight,
			TreeViewer treeViewer, IReviewExplorerContentProvider contentProvider, IEventBroker eventBroker) {

		this.baseElements = baseElements;
		this.objectsToHighlight = objectsToHighlight;
		this.treeViewer = treeViewer;
		this.contentProvider = contentProvider;
		this.eventBroker = eventBroker;
	}

	@Override
	public void run(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException {
		progressMonitor.beginTask("Recalculating highlights...", IProgressMonitor.UNKNOWN);
		// TODO apply filter
		objectsToHighlight.addAll(baseElements);

		addContainers(new HashSet<>(objectsToHighlight), objectsToHighlight, progressMonitor);

		addParentElements(new HashSet<>(objectsToHighlight), objectsToHighlight, progressMonitor);

		treeViewer.getControl().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				treeViewer.refresh();

				eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
			}
		});
	}

	/**
	 * Adds the parent elements for the given set of child objects to the set of
	 * highlighted objects using the current {@link #contentProvider}.
	 * 
	 * @param children
	 *            the set of objects to calculate the parents for.
	 * @param objectsToHighlight
	 *            the set of objects to store the parent objects into.
	 * @param progressMonitor
	 *            the {@link IProgressMonitor} to check the cancellation state.
	 */
	protected void addParentElements(Set<Object> children, Set<Object> objectsToHighlight,
			IProgressMonitor progressMonitor) {

		if (progressMonitor.isCanceled()) {
			return;
		}

		for (Object child : children) {

			if (progressMonitor.isCanceled()) {
				return;
			}

			Object parent = contentProvider.getParent(child);

			while (parent != null) {

				if (progressMonitor.isCanceled()) {
					return;
				}

				objectsToHighlight.add(parent);
				parent = contentProvider.getParent(parent);
			}

		}
	}

	/**
	 * Adds the containers for EObjects in the given set of child objects to the
	 * set of highlighted objects.
	 * 
	 * @param children
	 *            the set of objects to calculate the containers for.
	 * @param objectsToHighlight
	 *            the set of objects to store the containers into.
	 * @param progressMonitor
	 *            the {@link IProgressMonitor} to check the cancellation state.
	 */
	protected void addContainers(Set<Object> children, Set<Object> objectsToHighlight,
			IProgressMonitor progressMonitor) {

		for (Object child : children) {

			if (child instanceof EObject) {

				if (progressMonitor.isCanceled()) {
					return;
				}

				EObject container = ((EObject) child).eContainer();

				while (container != null) {

					if (progressMonitor.isCanceled()) {
						return;
					}

					objectsToHighlight.add(container);
					container = container.eContainer();
				}
			}
		}
	}

}

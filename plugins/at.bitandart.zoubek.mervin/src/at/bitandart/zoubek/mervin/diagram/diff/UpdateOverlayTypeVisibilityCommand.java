/*******************************************************************************
 * Copyright (c) 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.diagram.diff;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;

/**
 * A Command that updates the visibility of the view related to an overlay of a
 * particular type. If specified, the linked (overlayed) view's visibility is
 * also updated.
 * 
 * @author Florian Zoubek
 *
 */
final class UpdateOverlayTypeVisibilityCommand extends AbstractTransactionalCommand {

	private Diagram diagram;
	private IOverlayTypeDescriptor overlayDescriptor;
	private boolean visibility;
	private boolean updateOverlayedElement;

	public UpdateOverlayTypeVisibilityCommand(TransactionalEditingDomain domain, Diagram diagram,
			IOverlayTypeDescriptor overlayDescriptor, boolean visibility, boolean updateOverlayedElement) {
		super(domain, "", null);
		this.diagram = diagram;
		this.overlayDescriptor = overlayDescriptor;
		this.visibility = visibility;
		this.updateOverlayedElement = updateOverlayedElement;
	}

	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		overlayDescriptor.storeTypeVisibility(diagram, visibility);
		for (Object child : diagram.getChildren()) {
			if (child instanceof View) {
				updateVisibility((View) child);
			}
		}
		return CommandResult.newOKCommandResult();
	}

	private void updateVisibility(View view) {

		EObject element = view.getElement();
		if (element instanceof DifferenceOverlay) {
			DifferenceOverlay overlay = ((DifferenceOverlay) element);
			if (overlayDescriptor.isType(overlay)) {
				view.setVisible(visibility);
				if (updateOverlayedElement) {
					View linkedView = overlay.getLinkedView();
					if (linkedView != null) {
						linkedView.setVisible(visibility);
					}
				}
			}
		}

		for (Object child : view.getChildren()) {
			if (child instanceof View) {
				updateVisibility((View) child);
			}
		}

	}

}
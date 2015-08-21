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

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SemanticEditPolicy;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * An {@link EditPart} that provides a workspace with multiple child elements
 * that support stacking and minimizing.
 * 
 * @author Florian Zoubek
 *
 */
public class WorkspaceEditPart extends GraphicalEditPart {

	public WorkspaceEditPart(EObject model) {
		super(model);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		FreeformLayer panel = new FreeformLayer();
		panel.setLayoutManager(new FreeformLayout());
		return panel;
	}

	public ModelReview getModelReview() {
		EObject model = resolveSemanticElement();
		if (model instanceof ModelReview) {
			return (ModelReview) model;
		}
		return null;

	}

	@Override
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		removeEditPolicy(EditPolicyRoles.SEMANTIC_ROLE);
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new SemanticEditPolicy() {

			/**
			 * Overridden to prevent deletion of semantic model elements,
			 * inspired by
			 * {@code org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.NonSemanticEditPolicy}
			 * .
			 */
			protected Command getSemanticCommand(IEditCommandRequest editRequest) {

				if (editRequest instanceof DestroyElementRequest) {
					if (getHost() instanceof GraphicalEditPart) {
						return new ICommandProxy(new DeleteCommand(editRequest.getEditingDomain(),
								((GraphicalEditPart) getHost()).getPrimaryView()));
					}
				}
				return super.getSemanticCommand(editRequest);
			}
		});
		installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
	}

}

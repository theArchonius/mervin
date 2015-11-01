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

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SemanticEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.graphics.Image;

import at.bitandart.zoubek.mervin.draw2d.MervinResourceRegistry;
import at.bitandart.zoubek.mervin.draw2d.RegistryResourceManager;
import at.bitandart.zoubek.mervin.draw2d.StandaloneMervinResourceRegistry;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.DiffWorkbench;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.IDiffWorkbench;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * An {@link EditPart} that provides a workspace with multiple child elements
 * that support stacking and minimizing.
 * 
 * @author Florian Zoubek
 *
 */
public class WorkspaceEditPart extends ShapeNodeEditPart implements IPrimaryEditPart {

	private RegistryResourceManager registryResourceManager;

	public WorkspaceEditPart(View model) {
		super(model);

		registryResourceManager = new RegistryResourceManager(new StandaloneMervinResourceRegistry(),
				new LocalResourceManager(JFaceResources.getResources()));
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
	}

	public ModelReview getModelReview() {
		EObject model = resolveSemanticElement();
		if (model instanceof ModelReview) {
			return (ModelReview) model;
		}
		return null;

	}

	@Override
	public IFigure getContentPane() {
		if (figure instanceof IDiffWorkbench) {
			return ((IDiffWorkbench) figure).getContentArea();
		}
		return figure;
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
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy());
	}

	@Override
	protected NodeFigure createNodeFigure() {
		Image workbenchMaximizeImage = registryResourceManager
				.getImage(MervinResourceRegistry.IMAGE_WORKBENCH_MAXIMIZE);
		Image workbenchMinimizeImage = registryResourceManager
				.getImage(MervinResourceRegistry.IMAGE_WORKBENCH_MINIMIZE);
		Image workbenchWindowModeImage = registryResourceManager
				.getImage(MervinResourceRegistry.IMAGE_WORKBENCH_WINDOW_MODE);
		Image workbenchTabModeImage = registryResourceManager.getImage(MervinResourceRegistry.IMAGE_WORKBENCH_TAB_MODE);
		DiffWorkbench figure = new DiffWorkbench(getMapMode(), workbenchWindowModeImage, workbenchTabModeImage,
				workbenchMaximizeImage, workbenchMinimizeImage);
		return figure;
	}
}

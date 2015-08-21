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
package at.bitandart.zoubek.mervin.diagram.diff;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gmf.runtime.common.ui.action.ActionManager;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.emf.commands.core.command.EditingDomainUndoContext;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * A View that visualizes the difference between a set of diagram elements
 * within two patch sets of a {@link ModelReview} instance. The
 * {@link ModelReview} instance must be provided in the transient data map (
 * {@link MPart#getTransientData()}) of the part with the key
 * {@link #DATA_TRANSIENT_MODEL_REVIEW}. The {@link ModelReview} instance can be
 * obtained or modified by other views using the transient data map or using
 * {@link #getModelReview()} and {@link #setModelReview(ModelReview)}.
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramDiffView {

	private Composite mainPanel;

	private Control viewerControl;

	@Inject
	private ESelectionService selectionService;
	@Inject
	private MPart part;
	@Inject
	private GMFDiagramDiffViewService diagramDiffViewService;

	/**
	 * the default part descriptor id associated with this view
	 */
	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.diagram.diffviewer";

	/**
	 * key of the associated model review in the transient data map of the
	 * corresponding {@link MPart}.
	 */
	public static final String DATA_TRANSIENT_MODEL_REVIEW = "transient-review";

	private TransactionalEditingDomain editingDomain;

	@Inject
	public DiagramDiffView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());
		// mainPanel.setLayout(new FillLayout());

		ResourceSet resourceSet = new ResourceSetImpl();
		final Resource resource = new ResourceImpl(URI.createURI("mervin-model-review-resource.resource"));
		resourceSet.getResources().add(resource);
		// Diagram diagram = ViewService.getInstance().createDiagram(new
		// EObjectAdapter(getModelReview()),
		// PART_DESCRIPTOR_ID, new PreferencesHint(PART_DESCRIPTOR_ID));
		editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain(resourceSet);

		GraphicalViewer viewer = new DiagramDiffViewer();
		viewerControl = viewer.createControl(mainPanel);
		viewerControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
		viewer.setEditPartFactory(EditPartService.getInstance());
		viewer.setEditDomain(createEditDomain());
		viewerControl.setBackground(parent.getBackground());

		final Diagram diagram = diagramDiffViewService.createAndConnectViewModel(getModelReview(),
				viewer.getEditDomain(), editingDomain, new PreferencesHint(PART_DESCRIPTOR_ID));
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
			protected void doExecute() {
				resource.getContents().add(diagram);
			}
		});
		viewer.setRootEditPart(EditPartService.getInstance().createRootEditPart(diagram));
		viewer.setContents(diagram);

	}

	@PreDestroy
	public void preDestroy() {

	}

	@Focus
	public void onFocus() {
		viewerControl.setFocus();
		selectionService.setSelection(getModelReview());
	}

	/**
	 * sets the currently used model review instance
	 * 
	 * @param modelReview
	 *            the model review instance to set
	 */
	public void setModelReview(ModelReview modelReview) {
		part.getTransientData().put(DATA_TRANSIENT_MODEL_REVIEW, modelReview);
		selectionService.setSelection(getModelReview());
	}

	/**
	 * @return the currently used model review instance
	 */
	public ModelReview getModelReview() {

		// the review model is stored in the transient data map
		if (part.getTransientData().containsKey(DATA_TRANSIENT_MODEL_REVIEW)) {
			Object object = part.getTransientData().get(DATA_TRANSIENT_MODEL_REVIEW);

			if (object instanceof ModelReview) {
				return (ModelReview) object;
			}
		}

		return null;
	}

	private EditDomain createEditDomain() {
		E4DiagramEditDomain e4DiagramEditDomain = new E4DiagramEditDomain();
		e4DiagramEditDomain.setActionManager(new ActionManager(OperationHistoryFactory.getOperationHistory()));

		DiagramCommandStack diagramStack = new DiagramCommandStack(e4DiagramEditDomain);
		diagramStack.setOperationHistory(e4DiagramEditDomain.getActionManager().getOperationHistory());
		diagramStack.setUndoContext(new EditingDomainUndoContext(editingDomain, null));
		e4DiagramEditDomain.setCommandStack(diagramStack);
		return e4DiagramEditDomain;

	}

	class E4DiagramEditDomain extends EditDomain implements IDiagramEditDomain {

		/** the action manager */
		private ActionManager actionManager;

		@Override
		public DiagramCommandStack getDiagramCommandStack() {
			return (DiagramCommandStack) getCommandStack();
		}

		@Override
		public ActionManager getActionManager() {
			return actionManager;
		}

		public void setActionManager(ActionManager actionManager) {
			this.actionManager = actionManager;
		}

	}

}
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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gmf.runtime.common.ui.action.ActionManager;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.emf.commands.core.command.EditingDomainUndoContext;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.papyrus.infra.gmfdiag.css.helper.CSSHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * <p>
 * A View that visualizes the difference between a set of diagram elements
 * within two patch sets of a {@link ModelReview} instance. The
 * {@link ModelReview} instance must be provided in the transient data map (
 * {@link MPart#getTransientData()}) of the part with the key
 * {@link #DATA_TRANSIENT_MODEL_REVIEW}. The {@link ModelReview} instance can be
 * obtained or modified by other views using the transient data map or using
 * {@link #getModelReview()} and {@link #setModelReview(ModelReview)}.
 * </p>
 * <p>
 * This class adapts to the following classes:
 * <ul>
 * <li>{@link ModelReview} - the loaded model review instance</li>
 * <li>{@link Diagram} - the diagram instance of the diff view</li>
 * <li>{@link RootEditPart} - the {@link RootEditPart} of the graphical viewer
 * </li>
 * <li>{@link IFigure} - the root {@link IFigure} of the graphical viewer</li>
 * </ul>
 * </p>
 * 
 * 
 * @author Florian Zoubek
 *
 */
public class DiagramDiffView implements IAdaptable {

	private Composite mainPanel;

	private GraphicalViewer viewer;

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
		if (getModelReview() != null) {

			ResourceSet resourceSet = new ResourceSetImpl();
			CSSHelper.installCSSSupport(resourceSet);
			final Resource resource = resourceSet
					.createResource(URI.createURI("mervin-model-review-resource.resource.notation"));
			resourceSet.getResources().add(resource);
			// Diagram diagram = ViewService.getInstance().createDiagram(new
			// EObjectAdapter(getModelReview()),
			// PART_DESCRIPTOR_ID, new PreferencesHint(PART_DESCRIPTOR_ID));
			editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain(resourceSet);

			viewer = new DiagramDiffViewer();
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

			viewer.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					selectionService.setSelection(selection);
				}

			});

		} else {
			Label infoLabel = new Label(mainPanel, SWT.CENTER);
			infoLabel.setText("Could not restore review - close this view and try reloading the review");
		}
	}

	@PreDestroy
	public void preDestroy() {

	}

	@Focus
	public void onFocus() {
		if (viewerControl != null) {
			viewerControl.setFocus();
		} else {
			mainPanel.setFocus();
		}
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

	@Override
	public <T> T getAdapter(Class<T> adapterType) {

		if (adapterType == ModelReview.class) {
			return adapterType.cast(getModelReview());

		} else if (adapterType == Diagram.class) {
			return adapterType.cast(viewer.getContents().getModel());

		} else if (adapterType == RootEditPart.class) {
			return adapterType.cast(viewer.getRootEditPart());

		} else if (adapterType == IFigure.class) {

			RootEditPart rootEditPart = viewer.getRootEditPart();
			if (rootEditPart instanceof GraphicalEditPart) {
				return adapterType.cast(((GraphicalEditPart) rootEditPart).getFigure());
			}

		}
		return null;
	}

}
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
package at.bitandart.zoubek.mervin.diagram.diff;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
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
import org.eclipse.gmf.runtime.notation.View;
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

import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.IReviewHighlightServiceListener;
import at.bitandart.zoubek.mervin.diagram.diff.parts.IFocusHighlightEditPart;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

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
	private IReviewHighlightService highlightService;
	@Inject
	private MPart part;
	@Inject
	private MWindow window;
	@Inject
	private GMFDiagramDiffViewService diagramDiffViewService;

	private IReviewHighlightServiceListener highlightListener = new DiffHighlightListener();

	/**
	 * the default part descriptor id associated with this view
	 */
	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.diagram.diffviewer";

	/**
	 * key of the associated model review in the transient data map of the
	 * corresponding {@link MPart}.
	 */
	public static final String DATA_TRANSIENT_MODEL_REVIEW = "transient-review";

	/**
	 * key of the associated diagram viewer review in the transient data map of
	 * the corresponding {@link MPart}.
	 */
	public static final String DATA_TRANSIENT_DIAGRAM_VIEWER = "transient-diagram-viewer";

	private TransactionalEditingDomain editingDomain;

	@Inject
	public DiagramDiffView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent, IEclipseContext context) {

		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());

		ModelReview modelReview = getModelReview();
		context.set(ModelReview.class, modelReview);

		if (modelReview != null) {

			ResourceSet resourceSet = new ResourceSetImpl();
			CSSHelper.installCSSSupport(resourceSet);
			final Resource resource = resourceSet
					.createResource(URI.createURI("mervin-model-review-resource.resource.notation"));
			resourceSet.getResources().add(resource);
			// Diagram diagram = ViewService.getInstance().createDiagram(new
			// EObjectAdapter(getModelReview()),
			// PART_DESCRIPTOR_ID, new PreferencesHint(PART_DESCRIPTOR_ID));
			editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain(resourceSet);
			context.set(EditingDomain.class, editingDomain);
			context.set(TransactionalEditingDomain.class, editingDomain);

			EditDomain editDomain = createEditDomain();
			context.set(EditDomain.class, editDomain);

			viewer = new DiagramDiffViewer();
			viewerControl = viewer.createControl(mainPanel);
			viewerControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
			viewer.setEditPartFactory(EditPartService.getInstance());
			viewer.setEditDomain(editDomain);
			viewerControl.setBackground(parent.getBackground());

			final Diagram diagram = diagramDiffViewService.createAndConnectViewModel(getModelReview(),
					viewer.getEditDomain(), editingDomain, new PreferencesHint(PART_DESCRIPTOR_ID));
			editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
				protected void doExecute() {
					resource.getContents().add(diagram);
				}
			});

			context.set(Diagram.class, diagram);

			viewer.setRootEditPart(EditPartService.getInstance().createRootEditPart(diagram));
			viewer.setContents(diagram);

			viewer.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					selectionService.setSelection(selection);
				}

			});
			part.getTransientData().put(DATA_TRANSIENT_DIAGRAM_VIEWER, viewer);
			highlightService.addHighlightServiceListener(highlightListener);

		} else {
			Label infoLabel = new Label(mainPanel, SWT.CENTER);
			infoLabel.setText("Could not restore review - close this view and try reloading the review");
		}
	}

	@PreDestroy
	public void preDestroy() {

		part.getTransientData().remove(DATA_TRANSIENT_DIAGRAM_VIEWER);
		highlightService.removeHighlightServiceListener(highlightListener);

	}

	@Focus
	public void onFocus() {
		if (viewerControl != null) {
			viewerControl.setFocus();
		} else {
			mainPanel.setFocus();
		}
		selectionService.setSelection(getModelReview());
		window.getContext().set(DiagramDiffView.class, this);
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

	/**
	 * 
	 * @return the edit part that manages the focus-based temporary
	 *         highlighting.
	 */
	private IFocusHighlightEditPart getFocusHighlightEditPart() {
		if (viewer != null) {
			RootEditPart rootEditPart = viewer.getRootEditPart();
			if (rootEditPart instanceof IFocusHighlightEditPart) {
				return (IFocusHighlightEditPart) rootEditPart;
			}
		}
		return null;
	}

	/**
	 * A {@link IReviewHighlightServiceListener} that adds the corresponding
	 * figures to the temporary highlighted figures of the
	 * {@link IFocusHighlightEditPart} of the diagram.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class DiffHighlightListener implements IReviewHighlightServiceListener {

		@Override
		public void elementAdded(ModelReview review, Object element) {

			IFocusHighlightEditPart focusableEditPart = getFocusHighlightEditPart();
			if (focusableEditPart != null) {

				IFigure focusFigure = findFigureFor(review, element, focusableEditPart);

				if (focusFigure != null) {
					focusableEditPart.addFocusHighlightFigure(focusFigure);
				}
			}

		}

		@Override
		public void elementRemoved(ModelReview review, Object element) {

			IFocusHighlightEditPart focusableEditPart = getFocusHighlightEditPart();
			if (focusableEditPart != null) {

				IFigure focusFigure = findFigureFor(review, element, focusableEditPart);

				if (focusFigure != null) {
					focusableEditPart.removeFocusHighlightFigure(focusFigure);
				}
			}

		}

		/**
		 * finds the corresponding figure for the given review and element,
		 * contained in the given root {@link EditPart}.
		 * 
		 * @param review
		 *            the review associated with the given element.
		 * @param element
		 *            the element to find the figure for.
		 * @param rootEditPart
		 *            the root {@link EditPart} to search in.
		 * @return the figure representing the given element or null if none
		 *         could be found.
		 */
		private IFigure findFigureFor(ModelReview review, Object element, EditPart rootEditPart) {

			if (element instanceof EObject) {
				EObject eObject = (EObject) element;

				IFigure figure = findFigureInEditPartTree(eObject, rootEditPart);
				if (figure != null) {
					return figure;
				}
				figure = findFigureInPatchSetMatch(eObject, review.getRightPatchSet(), rootEditPart);
				if (figure != null) {
					return figure;
				}
				figure = findFigureInPatchSetMatch(eObject, review.getLeftPatchSet(), rootEditPart);
				if (figure != null) {
					return figure;
				}
			}

			return null;

		}

		/**
		 * finds the corresponding figure for the given {@link EObject},
		 * starting at the given root {@link EditPart} (inclusive).
		 * 
		 * @param eObject
		 *            the object to search for.
		 * @param rootEditPart
		 *            the root edit part to start the search at (inclusive).
		 * @return the corresponding figure or null if none could be found.
		 */
		private IFigure findFigureInEditPartTree(EObject eObject, EditPart rootEditPart) {

			// search for the element itself
			EditPart editPart = findEditPart(eObject, rootEditPart);

			if (editPart != null && editPart instanceof GraphicalEditPart) {
				return ((GraphicalEditPart) editPart).getFigure();
			}
			return null;
		}

		/**
		 * finds the corresponding figure for the given and element based on the
		 * matching information of comparison in the given patch set.
		 * 
		 * @param eObject
		 *            the object to find the match for.
		 * @param patchSet
		 *            the patch set to search for the match.
		 * @param rootEditPart
		 *            the root edit part containing the potential match object
		 *            of the match.
		 * @return the corresponding figure or null if none could be found.
		 */
		private IFigure findFigureInPatchSetMatch(EObject eObject, PatchSet patchSet, EditPart rootEditPart) {

			if (patchSet != null) {
				Match match = patchSet.getModelComparison().getMatch(eObject);
				if (match != null) {
					EObject matchedObject = match.getLeft();
					if (matchedObject == eObject) {
						matchedObject = match.getRight();
					}
					EditPart editPart = findEditPart(eObject, rootEditPart);
					if (editPart != null && editPart instanceof GraphicalEditPart) {
						return ((GraphicalEditPart) editPart).getFigure();
					}
				}
			}
			return null;

		}

		/**
		 * finds the corresponding edit part for the given model object.
		 * 
		 * @param object
		 *            the object to search for.
		 * @param rootEditPart
		 *            the root edit part to start the search.
		 * @return the corresponding edit part or null if none could be found.
		 */
		private EditPart findEditPart(Object object, EditPart rootEditPart) {

			Object model = rootEditPart.getModel();
			if (model == object || (model instanceof View && ((View) model).getElement() == object)) {
				return rootEditPart;
			}
			List<?> children = rootEditPart.getChildren();
			for (Object child : children) {
				EditPart editPart = findEditPart(object, (EditPart) child);
				if (editPart != null) {
					return editPart;
				}
			}
			return null;

		}

	}

}
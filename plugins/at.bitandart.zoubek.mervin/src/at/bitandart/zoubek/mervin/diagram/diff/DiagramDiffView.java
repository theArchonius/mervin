/*******************************************************************************
 * Copyright (c) 2015, 2016, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.diagram.diff;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.ConnectionEditPart;
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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.papyrus.infra.elementtypesconfigurations.registries.ElementTypeSetConfigurationRegistry;
import org.eclipse.papyrus.infra.gmfdiag.css.helper.CSSHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import at.bitandart.zoubek.mervin.IDiagramModelHelper;
import at.bitandart.zoubek.mervin.IMervinContextConstants;
import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.IReviewHighlightServiceListener;
import at.bitandart.zoubek.mervin.diagram.diff.GMFDiagramDiffViewService.DiagramDiffServiceListener;
import at.bitandart.zoubek.mervin.diagram.diff.parts.DiagramEditPart;
import at.bitandart.zoubek.mervin.diagram.diff.parts.IFocusHighlightEditPart;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.DiagramContainerFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.IDiffWorkbenchTrayFigure;
import at.bitandart.zoubek.mervin.draw2d.figures.workbench.IDiffWorkbenchWindowTitleFigure;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.IPatchSetHistoryEntry;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;
import at.bitandart.zoubek.mervin.review.HighlightMode;
import at.bitandart.zoubek.mervin.review.HighlightSelectionListener;
import at.bitandart.zoubek.mervin.review.IReviewHighlightingPart;

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
public class DiagramDiffView implements IAdaptable, IReviewHighlightingPart {

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
	private IEclipseContext context;
	@Inject
	private EMenuService menuService;
	@Inject
	private IDiagramModelHelper diagramModelHelper;

	private GMFDiagramDiffViewService diagramDiffViewService;

	private DiffHighlightListener highlightListener = new DiffHighlightListener();

	private boolean ignoreHighlights = false;

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

	/**
	 * the menu id of the context menu for this view
	 */
	public static final String VIEW_CONTEXTMENU_ID = "at.bitandart.zoubek.mervin.contextmenu.view.diagram.diff";

	private TransactionalEditingDomain editingDomain;

	@Inject
	public DiagramDiffView() {

	}

	@Inject
	public void setDiagramDiffViewService(GMFDiagramDiffViewService diagramDiffViewService) {

		/* remove old node update listener if necessary */
		if (this.diagramDiffViewService != null) {
			this.diagramDiffViewService.removeListener(highlightListener);
		}

		this.diagramDiffViewService = diagramDiffViewService;
		/*
		 * listen for node updates to update the highlighted elements
		 * accordingly
		 */
		diagramDiffViewService.addListener(highlightListener);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());

		ModelReview modelReview = getModelReview();
		context.set(ModelReview.class, modelReview);

		if (modelReview != null) {

			/*
			 * This call is necessary to make sure that papyrus is properly
			 * initialized
			 */
			ElementTypeSetConfigurationRegistry.getInstance();

			String title = MessageFormat.format("Mervin Review #{0}", modelReview.getId(),
					modelReview.getPatchSets().size());
			part.setLabel(title);

			ResourceSet resourceSet = new ResourceSetImpl();

			/* activate papyrus CSS support */
			CSSHelper.installCSSSupport(resourceSet);

			final Resource resource = resourceSet
					.createResource(URI.createURI("mervin-model-review-resource.resource.notation"));
			resourceSet.getResources().add(resource);
			editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain(resourceSet);
			context.set(EditingDomain.class, editingDomain);
			context.set(TransactionalEditingDomain.class, editingDomain);

			EditDomain editDomain = createEditDomain();
			context.set(EditDomain.class, editDomain);

			viewer = new DiagramDiffViewer();
			viewerControl = viewer.createControl(mainPanel);
			viewerControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			if (viewerControl instanceof FigureCanvas) {
				/*
				 * for some reason the scrollbars also appear even if no
				 * scrolling is possible/necessary, so deactivate them for the
				 * figure canvas
				 */
				((FigureCanvas) viewerControl).setScrollBarVisibility(FigureCanvas.NEVER);
			}

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

			menuService.registerContextMenu(viewer.getControl(), VIEW_CONTEXTMENU_ID);

			viewer.addSelectionChangedListener(new HighlightSelectionListener(this) {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {

					ignoreHighlights = true;
					super.selectionChanged(event);
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					selectionService.setSelection(selection);
					ignoreHighlights = false;

				}

				@Override
				protected void addElementsToHighlight(Object object, Set<Object> elements) {

					EObject semanticModel = diagramModelHelper.getSemanticModel(object);
					if (semanticModel != null) {
						elements.add(semanticModel);
					}

					EObject notationModel = diagramModelHelper.getNotationModel(object);
					if (notationModel != null) {

						EObject originalNotationModel = getModelReview().getUnifiedModelMap()
								.getOriginal(notationModel);

						if (originalNotationModel != null) {
							elements.add(originalNotationModel);

						} else {
							elements.add(notationModel);
						}
					}
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
		context.remove(ModelReview.class);
		context.remove(Diagram.class);
		context.modify(IMervinContextConstants.ACTIVE_MODEL_REVIEW, null);
		context.modify(IMervinContextConstants.ACTIVE_DIAGRAM_DIFF_EDITOR, null);
		context.modify(IMervinContextConstants.ACTIVE_TRANSACTIONAL_EDITING_DOMAIN, null);
		context.modify(IMervinContextConstants.ACTIVE_EDIT_DOMAIN, null);
		context.modify(IMervinContextConstants.CURRENT_REVIEWER, null);

	}

	@Focus
	public void onFocus() {

		if (viewerControl != null) {
			viewerControl.setFocus();
		} else {
			mainPanel.setFocus();
		}

		/* update selection */
		selectionService.setSelection(getModelReview());

		/* update context */
		context.modify(IMervinContextConstants.ACTIVE_DIAGRAM_DIFF_EDITOR, this);
		context.modify(IMervinContextConstants.ACTIVE_MODEL_REVIEW, getModelReview());
		context.modify(IMervinContextConstants.CURRENT_REVIEWER, getModelReview().getCurrentReviewer());
		context.modify(IMervinContextConstants.ACTIVE_TRANSACTIONAL_EDITING_DOMAIN, editingDomain);
		if (viewer != null) {
			context.modify(IMervinContextConstants.ACTIVE_EDIT_DOMAIN, viewer.getEditDomain());
		}
	}

	/**
	 * sets the currently used model review instance
	 * 
	 * @param modelReview
	 *            the model review instance to set
	 */
	public void setModelReview(ModelReview modelReview) {

		part.getTransientData().put(DATA_TRANSIENT_MODEL_REVIEW, modelReview);
		context.modify(IMervinContextConstants.ACTIVE_MODEL_REVIEW, getModelReview());
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
	private class DiffHighlightListener implements IReviewHighlightServiceListener, DiagramDiffServiceListener {

		@Override
		public void elementAdded(ModelReview review, Object element) {

			if (!ignoreHighlights) {
				IFocusHighlightEditPart focusableEditPart = getFocusHighlightEditPart();
				if (focusableEditPart != null) {

					Set<IFigure> figures = new HashSet<>();

					collectFiguresFor(review, element, focusableEditPart, figures);

					for (IFigure figure : figures) {
						focusableEditPart.addFocusHighlightFigure(figure);
					}
				}
			}

		}

		@Override
		public void elementRemoved(ModelReview review, Object element) {

			IFocusHighlightEditPart focusableEditPart = getFocusHighlightEditPart();
			if (focusableEditPart != null) {

				Set<IFigure> figures = new HashSet<>();
				collectFiguresFor(review, element, focusableEditPart, figures);

				for (IFigure figure : figures) {
					focusableEditPart.removeFocusHighlightFigure(figure);
				}
			}

		}

		/**
		 * collects the corresponding figures for the given review and element,
		 * contained in the given root {@link EditPart}.
		 * 
		 * @param review
		 *            the review associated with the given element.
		 * @param element
		 *            the element to find the figure for.
		 * @param rootEditPart
		 *            the root edit part to start the search at (inclusive).
		 * @param figureSet
		 *            the set to store the collected figures into.
		 */
		private void collectFiguresFor(ModelReview review, Object element, EditPart rootEditPart,
				Set<IFigure> figureSet) {

			int previousFigureSetSize = figureSet.size();

			if (element instanceof IPatchSetHistoryEntry<?, ?>) {

				IPatchSetHistoryEntry<?, ?> historyEntry = (IPatchSetHistoryEntry<?, ?>) element;

				collectFiguresFor(review, historyEntry.getEntryObject(), rootEditPart, figureSet);
				if (previousFigureSetSize != figureSet.size()) {
					return;
				}

				Object value = historyEntry.getValue(review.getLeftPatchSet());
				if (value != null) {
					collectFiguresFor(review, value, rootEditPart, figureSet);
					if (previousFigureSetSize != figureSet.size()) {
						return;
					}
				}

				value = historyEntry.getValue(review.getRightPatchSet());
				if (value != null) {
					collectFiguresFor(review, value, rootEditPart, figureSet);
					if (previousFigureSetSize != figureSet.size()) {
						return;
					}
				}
			}

			if (element instanceof DiffWithSimilarity) {
				Diff diff = ((DiffWithSimilarity) element).getDiff();
				if (diff != null) {
					collectFiguresFor(review, diff, rootEditPart, figureSet);
					if (previousFigureSetSize != figureSet.size()) {
						return;
					}
				}
			}

			if (element instanceof Diff) {
				Object value = MatchUtil.getValue((Diff) element);
				if (value != null) {
					collectFiguresFor(review, value, rootEditPart, figureSet);
					if (previousFigureSetSize != figureSet.size()) {
						return;
					}
				}
			}

			if (element instanceof EObject) {
				EObject eObject = (EObject) element;

				collectFiguresOfSelectedComparison(review, eObject, rootEditPart, figureSet);
				if (previousFigureSetSize != figureSet.size()) {
					return;
				}
				collectFiguresNotContainedInSelectedComparison(review, eObject, rootEditPart, figureSet);
			}

		}

		/**
		 * collects the figures for a given eObject that has been extracted out
		 * of a match.
		 * 
		 * @param review
		 *            the model review associated with the given object.
		 * @param eObject
		 *            the object to search the figure for.
		 * @param rootEditPart
		 * @param figureSet
		 *            the set to store the collected figures into.
		 */
		private void collectFiguresOfMatchedObject(ModelReview review, EObject eObject, EditPart rootEditPart,
				Set<IFigure> figureSet) {

			/*
			 * A view has been very likely copied in the unified diagram
			 * model,therefore check if there is also a figure for this model
			 */
			if (eObject instanceof View) {
				Collection<EObject> unifiedCopies = review.getUnifiedModelMap().getCopies(eObject);
				for (EObject unifiedCopy : unifiedCopies) {
					if (unifiedCopy != null) {
						collectFiguresInEditPartTree(unifiedCopy, rootEditPart, figureSet);
					}
				}
			}

			collectFiguresInEditPartTree(eObject, rootEditPart, figureSet);
		}

		/**
		 * collects the corresponding figures for the given review and element
		 * which are known not to be contained in the selected comparison of the
		 * review.
		 * 
		 * @param review
		 *            the model review associated with the given object.
		 * @param eObject
		 *            the object to search for.
		 * @param rootEditPart
		 *            the root edit part to start the search at (inclusive).
		 * @param figureSet
		 *            the set to store the collected figures into.
		 */
		private void collectFiguresNotContainedInSelectedComparison(ModelReview review, EObject eObject,
				EditPart rootEditPart, Set<IFigure> figureSet) {

			EObject baseEObject = findMatchingEObject(review, eObject);

			if (baseEObject == null) {
				/*
				 * No base version found, so give up any further search. There
				 * might be a match in one of the other patch sets, but this
				 * requires a potentially expensive compare of the given patch
				 * sets. If this changes in the future, add the lookup here
				 * before canceling the search.
				 */
				return;
			}

			collectFiguresOfPatchSetMatch(review, baseEObject, review.getRightPatchSet(), rootEditPart, figureSet);

			collectFiguresOfPatchSetMatch(review, baseEObject, review.getLeftPatchSet(), rootEditPart, figureSet);
		}

		/**
		 * finds the corresponding matched eObject in one of the patch sets of
		 * the given review, if it exists.
		 * 
		 * @param review
		 *            the review containing the patch sets to search for the
		 *            matching eObject
		 * @param eObject
		 *            the {@link EObject} to search the matching object for.
		 * @return the matched {@link EObject} or null if none could be found.
		 */
		private EObject findMatchingEObject(ModelReview review, EObject eObject) {
			EList<PatchSet> patchSets = review.getPatchSets();
			for (PatchSet patchSet : patchSets) {
				EObject baseEObject = findMatchingEObject(patchSet.getModelComparison(), eObject);
				if (baseEObject != null) {
					return baseEObject;
				}
				baseEObject = findMatchingEObject(patchSet.getDiagramComparison(), eObject);
				if (baseEObject != null) {
					return baseEObject;
				}
			}
			return null;
		}

		/**
		 * finds the corresponding matched eObject in the given comparison, if
		 * it exists.
		 * 
		 * @param comparison
		 *            the comparison used to find the matching object.
		 * @param eObject
		 *            the {@link EObject} to search the matching object for.
		 * @return the matched {@link EObject} or null if none could be found.
		 */
		private EObject findMatchingEObject(Comparison comparison, EObject eObject) {
			Match match = comparison.getMatch(eObject);
			if (match != null) {
				EObject left = match.getLeft();
				if (left != eObject) {
					return left;
				} else {
					return match.getRight();
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
		 * @param figureSet
		 *            the set to store the collected figures into.
		 */
		private void collectFiguresInEditPartTree(EObject eObject, EditPart rootEditPart, Set<IFigure> figureSet) {

			// search for the element itself
			EditPart editPart = findEditPart(eObject, rootEditPart);

			if (editPart != null && editPart instanceof GraphicalEditPart) {

				figureSet.add(((GraphicalEditPart) editPart).getFigure());
				collectContainingDiagramContainerTrayFigures(editPart, figureSet, new HashSet<EditPart>());

			}
		}

		/**
		 * collects the tray figures of the {@link DiagramEditPart}s which
		 * contain the given edit part or the source/target of the given
		 * {@link ConnectionEditPart}.
		 * 
		 * @param editPart
		 *            the edit part to find the tary figure for.
		 * @param figureSet
		 *            the set to store the collected figures into.
		 * @param excludedEditParts
		 *            the set of edit parts to exclude from the search.
		 */
		private void collectContainingDiagramContainerTrayFigures(EditPart editPart, Set<IFigure> figureSet,
				Set<EditPart> excludedEditParts) {

			if (excludedEditParts.contains(editPart)) {
				return;
			}
			excludedEditParts.add(editPart);

			if (editPart instanceof DiagramEditPart) {

				DiagramContainerFigure diagramContainerFigure = ((DiagramEditPart) editPart)
						.getDiagramContainerFigure();
				if (diagramContainerFigure != null) {
					IDiffWorkbenchTrayFigure trayFigure = diagramContainerFigure.getTrayFigure();
					if (trayFigure != null) {
						figureSet.add(trayFigure);
					}
					IDiffWorkbenchWindowTitleFigure windowTitleFigure = diagramContainerFigure.getWindowTitleFigure();
					if (windowTitleFigure != null) {
						figureSet.add(windowTitleFigure);
					}
				}

			} else {

				EditPart parent = editPart.getParent();
				if (parent != null) {
					collectContainingDiagramContainerTrayFigures(parent, figureSet, excludedEditParts);
				}

				if (editPart instanceof ConnectionEditPart) {

					ConnectionEditPart connectionEditPart = (ConnectionEditPart) editPart;
					EditPart source = connectionEditPart.getSource();
					EditPart target = connectionEditPart.getTarget();
					if (source != null) {
						collectContainingDiagramContainerTrayFigures(source, figureSet, excludedEditParts);
					}
					if (target != null) {
						collectContainingDiagramContainerTrayFigures(target, figureSet, excludedEditParts);
					}
				}
			}

		}

		/**
		 * collects the corresponding figures for the given review and element
		 * based on the matching information of the comparisons in the given
		 * patch set.
		 * 
		 * @param review
		 *            the model review associated with the given object.
		 * @param eObject
		 *            the object to find the match for.
		 * @param patchSet
		 *            the patch set to search for the match.
		 * @param rootEditPart
		 *            the root edit part to start the search at (inclusive).
		 * @param figureSet
		 *            the set to store the collected figures into.
		 */
		private void collectFiguresOfPatchSetMatch(ModelReview review, EObject eObject, PatchSet patchSet,
				EditPart rootEditPart, Set<IFigure> figureSet) {

			if (patchSet != null) {

				collectFiguresOfComparison(review, eObject, patchSet.getModelComparison(), rootEditPart, figureSet);

				collectFiguresOfComparison(review, eObject, patchSet.getDiagramComparison(), rootEditPart, figureSet);
			}

		}

		/**
		 * collects the corresponding figures for the given review and element
		 * based on the matching information of the selected comparison of the
		 * given model review.
		 * 
		 * @param review
		 *            the model review associated with the given object.
		 * @param eObject
		 *            the object to find the match for.
		 * @param rootEditPart
		 *            the root edit part to start the search at (inclusive).
		 * @param figureSet
		 *            the set to store the collected figures into.
		 */
		private void collectFiguresOfSelectedComparison(ModelReview review, EObject eObject, EditPart rootEditPart,
				Set<IFigure> figureSet) {

			collectFiguresOfComparison(review, eObject, review.getSelectedModelComparison(), rootEditPart, figureSet);

			collectFiguresOfComparison(review, eObject, review.getSelectedDiagramComparison(), rootEditPart, figureSet);

		}

		/**
		 * collects the corresponding figures for the given review and element
		 * based on the matching information in the given comparison.
		 * 
		 * @param review
		 *            the model review associated with the given object.
		 * @param eObject
		 *            the object to find the match for.
		 * @param comparison
		 *            the comparison to get the match for the given object.
		 * @param rootEditPart
		 *            the root edit part to start the search at (inclusive).
		 * @param figureSet
		 *            the set to store the collected figures into.
		 */
		private void collectFiguresOfComparison(ModelReview review, EObject eObject, Comparison comparison,
				EditPart rootEditPart, Set<IFigure> figureSet) {

			Match match = comparison.getMatch(eObject);
			if (match != null) {

				collectFiguresOfMatch(review, match, rootEditPart, figureSet);
			}
		}

		/**
		 * finds the corresponding figures for the given review and element
		 * based on the matching information in the given match.
		 * 
		 * @param review
		 *            the model review associated with the given object.
		 * @param match
		 *            the match used to get the matching eObject from.
		 * @param rootEditPart
		 *            the root edit part to start the search at (inclusive).
		 * @param figureSet
		 *            the set to store the collected figures into.
		 */
		private void collectFiguresOfMatch(ModelReview review, Match match, EditPart rootEditPart,
				Set<IFigure> figureSet) {

			EObject left = match.getLeft();
			EObject right = match.getRight();

			if (left != null) {
				collectFiguresOfMatchedObject(review, left, rootEditPart, figureSet);
			}

			if (right != null) {
				collectFiguresOfMatchedObject(review, right, rootEditPart, figureSet);
			}
		}

		/**
		 * finds the corresponding edit part for the given model object.
		 * 
		 * @param object
		 *            the object to search for.
		 * @param rootEditPart
		 *            the root edit part to start the search at (inclusive).
		 * @return the corresponding edit part or null if none could be found.
		 */
		private EditPart findEditPart(Object object, EditPart rootEditPart) {

			Object model = rootEditPart.getModel();
			if (model == object || (model instanceof View && ((View) model).getElement() == object)) {
				return rootEditPart;
			}

			/* search in children */
			EditPart editPart = findEditPart(object, rootEditPart.getChildren());
			if (editPart != null) {
				return editPart;
			}

			if (rootEditPart instanceof GraphicalEditPart) {
				GraphicalEditPart graphicalEditpart = (GraphicalEditPart) rootEditPart;

				/* search in source connections */
				editPart = findEditPart(object, graphicalEditpart.getSourceConnections());
				if (editPart != null) {
					return editPart;
				}

				/* search in target connections */
				editPart = findEditPart(object, graphicalEditpart.getTargetConnections());
				if (editPart != null) {
					return editPart;
				}
			}
			return null;
		}

		/**
		 * finds the corresponding edit part for the given model object in the
		 * given list of possible edit parts.
		 * 
		 * @param object
		 *            the object to search for.
		 * @param editParts
		 *            a list that contain edit parts, non-edit parts will be
		 *            ignored.
		 * @return the corresponding edit part or null if none could be found.
		 */
		private EditPart findEditPart(Object object, List<?> editParts) {

			for (Object child : editParts) {

				EditPart editPart = findEditPart(object, (EditPart) child);
				if (editPart != null) {
					return editPart;
				}
			}

			return null;
		}

		@Override
		public void diagramNodesUpdated(Diagram workspaceDiagram, ModelReview review) {

			if (review == getModelReview()) {

				IFocusHighlightEditPart focusHighlightEditPart = getFocusHighlightEditPart();
				if (focusHighlightEditPart != null) {

					/*
					 * clear the existing highlighted figures in the edit parts
					 */
					focusHighlightEditPart.disableFocusHighlightMode();

					List<Object> highlightedElements = highlightService.getHighlightedElements(review);
					for (Object element : highlightedElements) {
						elementAdded(review, element);
					}
				}
			}
		}
	}

	@Override
	public void setHighlightMode(HighlightMode highlightMode) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public HighlightMode getHighlightMode() {
		return HighlightMode.SELECTION;
	}

	@Override
	public ModelReview getHighlightedModelReview() {
		return getModelReview();
	}

	@Override
	public IReviewHighlightService getReviewHighlightService() {
		return highlightService;
	}

	@Override
	public void revealNextHighlight() {
		// Not (yet) supported in this view
	}

	@Override
	public void revealPreviousHighlight() {
		// Not (yet) supported in this view
	}

	@Override
	public boolean hasNextHighlight() {
		return false;
	}

	@Override
	public boolean hasPreviousHighlight() {
		return false;
	}

}
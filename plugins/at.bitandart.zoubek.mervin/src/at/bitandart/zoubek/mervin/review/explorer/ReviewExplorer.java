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
package at.bitandart.zoubek.mervin.review.explorer;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ElementMatcher;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import at.bitandart.zoubek.mervin.IDiagramModelHelper;
import at.bitandart.zoubek.mervin.IMatchHelper;
import at.bitandart.zoubek.mervin.IModelReviewHelper;
import at.bitandart.zoubek.mervin.IOverlayTypeHelper;
import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.IReviewHighlightServiceListener;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.DefaultOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.OverlayType;
import at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch;
import at.bitandart.zoubek.mervin.model.modelreview.ModelPatch;
import at.bitandart.zoubek.mervin.model.modelreview.ModelResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.Patch;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.review.HighlightHoveredTreeItemMouseTracker;
import at.bitandart.zoubek.mervin.review.HighlightMode;
import at.bitandart.zoubek.mervin.review.HighlightSelectionListener;
import at.bitandart.zoubek.mervin.review.IReviewHighlightProvidingPart;
import at.bitandart.zoubek.mervin.review.ModelReviewEditorTrackingView;
import at.bitandart.zoubek.mervin.review.explorer.content.DifferencesTreeItem;
import at.bitandart.zoubek.mervin.review.explorer.content.IReviewExplorerContentProvider;
import at.bitandart.zoubek.mervin.review.explorer.content.ITreeItemContainer;
import at.bitandart.zoubek.mervin.review.explorer.content.ModelReviewContentProvider;
import at.bitandart.zoubek.mervin.swt.ProgressPanel;
import at.bitandart.zoubek.mervin.swt.text.styles.ComposedStyler;
import at.bitandart.zoubek.mervin.swt.text.styles.DiffStyler;
import at.bitandart.zoubek.mervin.swt.text.styles.FontStyler;
import at.bitandart.zoubek.mervin.swt.text.styles.HighlightStyler;
import at.bitandart.zoubek.mervin.swt.text.styles.OverlayTypeStyler;
import at.bitandart.zoubek.mervin.util.vis.HSB;
import at.bitandart.zoubek.mervin.util.vis.NumericColoredColumnLabelProvider;
import at.bitandart.zoubek.mervin.util.vis.ThreeWayLabelTreeViewerComparator;
import at.bitandart.zoubek.mervin.util.vis.ThreeWayObjectTreeViewerComparator;

/**
 * Review exploration view - shows an overview of the currently loaded model
 * review provided by the last active editor.
 * 
 * <p>
 * This class adapts to the following classes:
 * <ul>
 * <li>{@link ModelReview} - the current model review instance</li>
 * </ul>
 * </p>
 * 
 * @author Florian Zoubek
 * 
 * @see ModelReviewEditorTrackingView
 *
 */
public class ReviewExplorer extends ModelReviewEditorTrackingView implements IReviewHighlightProvidingPart, IAdaptable {

	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.review";

	public static final String VIEW_MENU_ID = "at.bitandart.zoubek.mervin.menu.view.review.explorer";

	public static final String VIEW_MENU_ITEM_HIGHLIGHT_SWITCH_MODE = "at.bitandart.zoubek.mervin.menu.view.review.explorer.highlight.switchmode";

	@Inject
	private ESelectionService selectionService;

	@Inject
	private IReviewHighlightService highlightService;

	@Inject
	private Display display;

	@Inject
	private IMatchHelper matchHelper;

	@Inject
	private IOverlayTypeHelper overlayTypeHelper;

	@Inject
	private IDiagramModelHelper diagramModelHelper;

	@Inject
	private IModelReviewHelper modelReviewHelper;

	// text styles
	private HighlightStyler highlightStyler;

	private FontStyler diffStyler;

	private EnumMap<OverlayType, OverlayTypeStyler> overlayTypeStylers = new EnumMap<>(OverlayType.class);

	private IOverlayTypeStyleAdvisor styleAdvisor;

	/**
	 * the complete list of filtered and derived elements to highlight in this
	 * view.
	 */
	private Set<Object> objectsToHighlight = new HashSet<>();

	/**
	 * the current highlight mode, never null
	 */
	private HighlightMode highlightMode = HighlightMode.SELECTION;

	// JFace Viewers

	/**
	 * tree viewer that shows the overview of the model review
	 */
	private TreeViewer reviewTreeViewer;

	// SWT Controls

	/**
	 * the main panel of this view, that contains all controls for this view
	 */
	private Composite mainPanel;

	private ProgressPanel progressPanel;

	// Data

	/**
	 * indicates if all SWT controls and viewers have been correctly set up
	 */
	private boolean viewInitialized = false;

	private ReviewExplorerHighlightUpdater reviewHighlightUpdater;

	private ModelReviewContentProvider reviewExplorerContentProvider;

	@PostConstruct
	public void postConstruct(Composite parent, EModelService modelService, MPart part) {

		syncMenuAndToolbarItemState(modelService, part);

		styleAdvisor = new DefaultOverlayTypeStyleAdvisor();

		highlightStyler = new HighlightStyler(display);
		diffStyler = new DiffStyler(display);

		for (OverlayType overlayType : OverlayType.values()) {
			overlayTypeStylers.put(overlayType, new OverlayTypeStyler(overlayType, styleAdvisor));
		}

		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());

		// initialize tree viewer

		reviewTreeViewer = new TreeViewer(mainPanel, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		reviewExplorerContentProvider = new ModelReviewContentProvider(matchHelper);
		reviewTreeViewer.setContentProvider(reviewExplorerContentProvider);
		reviewTreeViewer.addSelectionChangedListener(new HighlightSelectionListener(this) {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				super.selectionChanged(event);
				ISelection selection = event.getSelection();
				selectionService.setSelection(selection);
			}
		});
		ColumnViewerToolTipSupport.enableFor(reviewTreeViewer);

		Tree reviewTree = reviewTreeViewer.getTree();
		reviewTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		reviewTree.setLinesVisible(false);
		reviewTree.setHeaderVisible(true);
		reviewTree.addMouseTrackListener(new HighlightHoveredTreeItemMouseTracker(this));

		// set up all columns of the tree

		// main label column
		TreeViewerColumn labelColumn = new TreeViewerColumn(reviewTreeViewer, SWT.NONE);
		labelColumn.getColumn().setResizable(true);
		labelColumn.getColumn().setMoveable(true);
		labelColumn.getColumn().setText("Element");
		labelColumn.getColumn().setWidth(200);
		ModelReviewExplorerMainColumnLabelProvider labelColumnLabelProvider = new ModelReviewExplorerMainColumnLabelProvider(
				reviewExplorerContentProvider, overlayTypeHelper);
		labelColumn.setLabelProvider(labelColumnLabelProvider);
		ThreeWayObjectTreeViewerComparator defaultComparator = new ThreeWayObjectTreeViewerComparator(reviewTreeViewer,
				labelColumn, labelColumnLabelProvider);
		labelColumn.getColumn().addSelectionListener(defaultComparator);
		reviewTreeViewer.setComparator(defaultComparator);

		// total diff type overview column
		IDifferenceCounter diffCounter = new ModelReviewContentViewerDiffCounter(reviewTreeViewer);
		TreeViewerColumn totalDiffTypeColumn = new TreeViewerColumn(reviewTreeViewer, SWT.NONE);
		totalDiffTypeColumn.getColumn().setResizable(true);
		totalDiffTypeColumn.getColumn().setMoveable(false);
		totalDiffTypeColumn.getColumn().setText("");
		totalDiffTypeColumn.getColumn().setAlignment(SWT.CENTER);
		totalDiffTypeColumn.getColumn().setToolTipText("Total diff type overview");
		DiffTypeOverviewLabelProvider totalDiffTypeLabelProvider = new TotalDiffTypeLabelProvider(styleAdvisor,
				diffCounter, true, overlayTypeHelper);
		totalDiffTypeColumn.setLabelProvider(totalDiffTypeLabelProvider);
		totalDiffTypeColumn.getColumn().addSelectionListener(new ThreeWayObjectTreeViewerComparator(reviewTreeViewer,
				totalDiffTypeColumn, new DiffCounterComparator(diffCounter)));

		// element diff type overview column
		TreeViewerColumn elementDiffTypeColumn = new TreeViewerColumn(reviewTreeViewer, SWT.NONE);
		elementDiffTypeColumn.getColumn().setResizable(true);
		elementDiffTypeColumn.getColumn().setMoveable(false);
		elementDiffTypeColumn.getColumn().setText("");
		elementDiffTypeColumn.getColumn().setAlignment(SWT.CENTER);
		elementDiffTypeColumn.getColumn().setToolTipText("Element diff type overview");
		DiffTypeOverviewLabelProvider elementDiffTypeLabelProvider = new ElementDiffTypeLabelProvider(styleAdvisor,
				diffCounter, false, overlayTypeHelper);
		elementDiffTypeColumn.setLabelProvider(elementDiffTypeLabelProvider);
		elementDiffTypeColumn.getColumn().addSelectionListener(new ThreeWayObjectTreeViewerComparator(reviewTreeViewer,
				elementDiffTypeColumn, new DiffCounterComparator(diffCounter)));

		// change count column
		TreeViewerColumn changeCountColumn = new TreeViewerColumn(reviewTreeViewer, SWT.NONE);
		changeCountColumn.getColumn().setResizable(true);
		changeCountColumn.getColumn().setMoveable(false);
		changeCountColumn.getColumn().setText("#C");
		changeCountColumn.getColumn().setAlignment(SWT.CENTER);
		changeCountColumn.getColumn().setToolTipText("Number of contained differences");
		ChangeCountColumnLabelProvider changeCountColumnLabelProvider = new ChangeCountColumnLabelProvider(
				reviewTreeViewer, Display.getCurrent().getSystemColor(SWT.COLOR_WHITE),
				Display.getCurrent().getSystemColor(SWT.COLOR_BLACK), diffCounter);
		changeCountColumn.setLabelProvider(changeCountColumnLabelProvider);
		changeCountColumn.getColumn().addSelectionListener(new ThreeWayLabelTreeViewerComparator(reviewTreeViewer,
				changeCountColumn, changeCountColumnLabelProvider));

		// reference count column
		TreeViewerColumn refCountColumn = new TreeViewerColumn(reviewTreeViewer, SWT.NONE);
		refCountColumn.getColumn().setResizable(true);
		refCountColumn.getColumn().setMoveable(false);
		refCountColumn.getColumn().setText("#RC");
		refCountColumn.getColumn().setAlignment(SWT.CENTER);
		refCountColumn.getColumn().setToolTipText("Number of referenced and referencing changed elements");
		ReferencedChangeCountColumnLabelProvider refChangeCountColumnlabelProvider = new ReferencedChangeCountColumnLabelProvider(
				reviewTreeViewer, Display.getCurrent().getSystemColor(SWT.COLOR_WHITE),
				Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		refCountColumn.setLabelProvider(refChangeCountColumnlabelProvider);
		refCountColumn.getColumn().addSelectionListener(new ThreeWayLabelTreeViewerComparator(reviewTreeViewer,
				refCountColumn, refChangeCountColumnlabelProvider));

		// the resource column
		TreeViewerColumn resourceColumn = new TreeViewerColumn(reviewTreeViewer, SWT.NONE);
		resourceColumn.getColumn().setResizable(true);
		resourceColumn.getColumn().setMoveable(true);
		resourceColumn.getColumn().setText("Resource");
		resourceColumn.getColumn().setWidth(200);
		ModelReviewExplorerResourceColumnLabelProvider resourceColumnLabelProvider = new ModelReviewExplorerResourceColumnLabelProvider();
		resourceColumn.setLabelProvider(resourceColumnLabelProvider);
		resourceColumn.getColumn().addSelectionListener(
				new ThreeWayLabelTreeViewerComparator(reviewTreeViewer, resourceColumn, resourceColumnLabelProvider));

		progressPanel = new ProgressPanel(mainPanel, SWT.NONE);
		progressPanel.setVisible(false);
		progressPanel.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).exclude(true).create());

		// all controls updated, now update them with the given values
		viewInitialized = true;

		// refresh the viewer highlights if highlighting is requested
		highlightService.addHighlightServiceListener(new IReviewHighlightServiceListener() {

			@Override
			public void elementRemoved(ModelReview review, Object element) {

				updateObjectsToHighlight();
				reviewTreeViewer.refresh();

			}

			@Override
			public void elementAdded(ModelReview review, Object element) {

				updateObjectsToHighlight();
				reviewTreeViewer.refresh();

			}
		});

		updateValues();
	}

	@PreDestroy
	private void preDestroy() {
		if (highlightStyler != null) {
			highlightStyler.dispose();
		}
		if (diffStyler != null) {
			diffStyler.dispose();
		}
		if (styleAdvisor != null) {
			styleAdvisor.dispose();
		}
	}

	/**
	 * updates the list of filtered and derived highlighted elements from the
	 * current highlight service for the current model review.
	 */
	private void updateObjectsToHighlight() {

		if (reviewHighlightUpdater != null && reviewHighlightUpdater.isAlive()) {
			reviewHighlightUpdater.cancelOperation();
			reviewHighlightUpdater = null;
		}

		objectsToHighlight = new HashSet<Object>();
		ModelReview currentModelReview = getCurrentModelReview();

		if (currentModelReview != null) {

			List<Object> highlightedElements = highlightService.getHighlightedElements(getCurrentModelReview());

			reviewHighlightUpdater = new ReviewExplorerHighlightUpdater(progressPanel, mainPanel, highlightedElements,
					objectsToHighlight, currentModelReview, reviewTreeViewer, reviewExplorerContentProvider,
					diagramModelHelper, modelReviewHelper);

			reviewHighlightUpdater.start();

		}

	}

	/**
	 * synchronizes the menu and toolbar item state of radio and check items
	 * with this view.
	 * 
	 * @param modelService
	 *            the service used to find the menu items
	 * @param part
	 *            the part containing the menu items
	 */
	private void syncMenuAndToolbarItemState(EModelService modelService, MPart part) {

		/* for now, just enforce the default state */
		ElementMatcher matcher = new ElementMatcher(VIEW_MENU_ITEM_HIGHLIGHT_SWITCH_MODE, MHandledMenuItem.class,
				(List<String>) null);
		/*
		 * IN_PART is not part of ANYWHERE, so use this variant of findElements
		 * and pass IN_PART as search flag
		 */
		List<MHandledMenuItem> items = modelService.findElements(part, MHandledMenuItem.class, EModelService.IN_PART,
				matcher);
		for (MHandledMenuItem item : items) {
			item.setSelected(false);
		}
	}

	@Override
	protected void updateValues() {

		// we cannot update the controls if they are not initialized yet
		if (viewInitialized) {

			updateObjectsToHighlight();
			ModelReview currentModelReview = getCurrentModelReview();

			// update the tree viewer
			reviewTreeViewer.setInput(currentModelReview);
			reviewTreeViewer.refresh();

			for (TreeColumn treeColumn : reviewTreeViewer.getTree().getColumns()) {
				treeColumn.pack();
			}

			reviewTreeViewer.getTree().layout();
			mainPanel.layout();
		}
	}

	@Override
	public void setHighlightMode(HighlightMode highlightMode) {

		if (highlightMode != null) {
			this.highlightMode = highlightMode;
			highlightService.clearHighlights(getCurrentModelReview());
		}

	}

	@Override
	public HighlightMode getHighlightMode() {
		return highlightMode;
	}

	@Override
	public IReviewHighlightService getReviewHighlightService() {
		return highlightService;
	}

	@Override
	public ModelReview getHighlightedModelReview() {
		return getCurrentModelReview();
	}

	@Override
	public <T> T getAdapter(Class<T> adapterType) {

		if (adapterType == ModelReview.class) {
			return adapterType.cast(getCurrentModelReview());
		}
		return null;
	}

	/**
	 * {@link ColumnLabelProvider} for the main column in the model review tree.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ModelReviewExplorerMainColumnLabelProvider extends StyledCellLabelProvider
			implements Comparator<Object> {

		/**
		 * label provider for model and diagram elements
		 */
		private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

		/**
		 * the overlay type helper used to convert {@link DifferenceKind}s to
		 * {@link OverlayType}s.
		 */
		private IOverlayTypeHelper overlayTypeHelper;

		/**
		 * the content provider used to determine the children during
		 * highlighting.
		 */
		private IReviewExplorerContentProvider contentProvider;

		public ModelReviewExplorerMainColumnLabelProvider(IReviewExplorerContentProvider contentProvider,
				IOverlayTypeHelper overlayTypeHelper) {
			/*
			 * Obtain the registered label providers for model and diagram
			 * elements
			 */
			adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(new ComposedAdapterFactory(
					EMFCompareRCPPlugin.getDefault().createFilteredAdapterFactoryRegistry()));
			this.contentProvider = contentProvider;
			this.overlayTypeHelper = overlayTypeHelper;
		}

		@Override
		public void update(ViewerCell cell) {

			Object element = cell.getElement();
			StyledString text = new StyledString();

			List<Styler> stylers = new ArrayList<Styler>();

			/* collect the stylers that should be applied to the label */
			if (element instanceof Diff) {

				stylers.add(diffStyler);
				OverlayTypeStyler typeStyler = overlayTypeStylers
						.get(overlayTypeHelper.toOverlayType(((Diff) element).getKind()));

				if (typeStyler != null) {
					stylers.add(typeStyler);
				}

			} else if (element instanceof DifferencesTreeItem) {
				stylers.add(diffStyler);

			} else if (element instanceof EObject) {

				/*
				 * color EObjects based on their referencing containment
				 * difference
				 */

				List<Diff> diffs = contentProvider.getDiffsFor((EObject) element);
				for (Diff diff : diffs) {

					if (diff instanceof ReferenceChange && ((ReferenceChange) diff).getReference().isContainment()) {

						OverlayTypeStyler typeStyler = overlayTypeStylers
								.get(overlayTypeHelper.toOverlayType(diff.getKind()));
						if (typeStyler != null) {
							stylers.add(typeStyler);
						}

						break;
					}
				}
			}

			if (isHighlighted(element, objectsToHighlight)) {
				stylers.add(highlightStyler);
			}

			/* all stylers collected, now apply them */

			if (stylers.isEmpty()) {
				/* no styles */
				text.append(getText(element));

			} else {

				Styler styler = null;
				if (stylers.size() > 1) {
					/* multiple styles, so compose them to one style */
					styler = new ComposedStyler(stylers.toArray(new Styler[stylers.size()]));
				} else {
					styler = stylers.get(0);
				}

				text.append(getText(element), styler);
			}

			cell.setText(text.getString());
			cell.setStyleRanges(text.getStyleRanges());
			cell.setImage(getImage(element));

			super.update(cell);

		}

		@Override
		public String getToolTipText(Object element) {
			return getText(element);
		}

		/**
		 * checks if the given element should be highlighted or not.
		 * 
		 * @param element
		 *            the element to check.
		 * @param highlightedElements
		 *            the set of elements to highlight.
		 * @return true if the given element should be highlighted, false
		 *         otherwise.
		 */
		private boolean isHighlighted(Object element, Set<Object> highlightedElements) {

			if (highlightedElements.contains(element)) {
				return true;
			}

			if (element instanceof ModelPatch) {
				return isHighlighted(((ModelPatch) element).getNewModelResource(), highlightedElements)
						|| isHighlighted(((ModelPatch) element).getOldModelResource(), highlightedElements);
			}

			if (element instanceof DiagramPatch) {
				return isHighlighted(((DiagramPatch) element).getNewDiagramResource(), highlightedElements)
						|| isHighlighted(((DiagramPatch) element).getOldDiagramResource(), highlightedElements);
			}

			return false;
		}

		/**
		 * @param element
		 *            the element to retrieve the label text for.
		 * @return the label text for the given element.
		 */
		public String getText(Object element) {

			if (element instanceof PatchSet) {
				return MessageFormat.format("PatchSet #{0}", ((PatchSet) element).getId());
			}

			if (element instanceof ITreeItemContainer) {
				return ((ITreeItemContainer) element).getText();
			}

			if (element instanceof Patch) {
				return ((Patch) element).getNewPath();
			}

			if (element instanceof ModelResource) {
				return ((ModelResource) element).getRootPackages().get(0).getName();
			}

			if (element instanceof EObject) {
				return adapterFactoryLabelProvider.getText(element);
			}

			return element.toString();
		}

		/**
		 * @param element
		 *            the element to retrieve the label image for.
		 * @return the label icon for the given element
		 */
		public Image getImage(Object element) {

			if (element instanceof EObject) {
				try {
					return adapterFactoryLabelProvider.getImage(element);
				} catch (Exception e) {
					/*
					 * just in case that the label provider throws some
					 * unexpected exceptions, e.g. sometimes for diffs...
					 */
					// TODO log warning
					return null;
				}
			}
			return null;

		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			adapterFactoryLabelProvider.addListener(listener);
			super.addListener(listener);
		}

		@Override
		public void dispose() {
			adapterFactoryLabelProvider.dispose();
			super.dispose();
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return adapterFactoryLabelProvider.isLabelProperty(element, property);
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			adapterFactoryLabelProvider.removeListener(listener);
			super.removeListener(listener);
		}

		@Override
		public int compare(Object object1, Object object2) {
			String text1 = getText(object1);
			String text2 = getText(object2);
			return Policy.getComparator().compare(text1, text2);
		}
	}

	/**
	 * {@link ColumnLabelProvider} for the resource column in the model review
	 * tree.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ModelReviewExplorerResourceColumnLabelProvider extends ColumnLabelProvider {

		@Override
		public String getText(Object element) {

			if (element instanceof EObject) {

				Resource resource = ((EObject) element).eResource();
				if (resource != null) {
					return resource.getURI().toString();
				}

			}
			return null;
		}

	}

	/**
	 * Base class for all {@link NumericColoredColumnLabelProvider}s that count
	 * elements of the model review and need access to the model review.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private abstract class ModelReviewCounterColumnLabelProvider extends NumericColoredColumnLabelProvider {

		ContentViewer viewer;

		public ModelReviewCounterColumnLabelProvider(ContentViewer viewer, HSB minHSB, HSB maxHSB, Color fgColor1,
				Color fgColor2) {
			super(minHSB, maxHSB, fgColor1, fgColor2);
			this.viewer = viewer;
		}

		/**
		 * finds the parent {@link PatchSet} of the given element, if it exists.
		 * 
		 * @param element
		 * @return the parent {@link PatchSet} or null if it cannot be found
		 */
		protected PatchSet findPatchSet(Object element) {

			ITreeContentProvider contentProvider = (ITreeContentProvider) viewer.getContentProvider();
			Object currentElement = element;

			while (currentElement != null && !(currentElement instanceof PatchSet)) {
				currentElement = contentProvider.getParent(currentElement);
			}

			if (currentElement instanceof PatchSet) {
				return (PatchSet) currentElement;
			}

			return null;
		}
	}

	/**
	 * {@link NumericColoredColumnLabelProvider} for the change count column of
	 * the model review tree.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ChangeCountColumnLabelProvider extends ModelReviewCounterColumnLabelProvider {

		private IDifferenceCounter diffCounter;

		public ChangeCountColumnLabelProvider(ContentViewer viewer, Color fgBright, Color fgDark,
				IDifferenceCounter diffCounter) {
			super(viewer, new HSB(205.0f, 0.f, 1.0f), new HSB(205.0f, 0.59f, 0.32f), fgBright, fgDark);
			this.diffCounter = diffCounter;
		}

		@Override
		public float getMaxValue(Object element) {
			return Math.max(diffCounter.getTotalDiffCount(element), 0);
		}

		@Override
		public float getMinValue(Object element) {
			return 0;
		}

		@Override
		public float getValue(Object element) {
			return Math.max(diffCounter.getDiffCount(element), 0);
		}

		@Override
		public boolean hasValue(Object element) {
			return diffCounter.getDiffCount(element) >= 0;
		}

	}

	/**
	 * {@link NumericColoredColumnLabelProvider} for the reference count column
	 * of the model review tree.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ReferencedChangeCountColumnLabelProvider extends ModelReviewCounterColumnLabelProvider {

		public ReferencedChangeCountColumnLabelProvider(ContentViewer viewer, Color fgBright, Color fgDark) {
			super(viewer, new HSB(205.0f, 0.f, 1.0f), new HSB(205.0f, 0.59f, 0.32f), fgBright, fgDark);
		}

		@Override
		public float getMaxValue(Object element) {

			PatchSet patchSet = findPatchSet(element);
			if (patchSet != null) {
				return patchSet.getMaxObjectChangeRefCount();
			}

			return 0;
		}

		@Override
		public float getMinValue(Object element) {
			return 0;
		}

		@Override
		public float getValue(Object element) {

			PatchSet patchSet = findPatchSet(element);
			if (patchSet != null) {

				Map<EObject, Integer> objectChangeRefCount = patchSet.getObjectChangeRefCount();
				if (objectChangeRefCount.containsKey(element)) {
					return objectChangeRefCount.get(element);
				}

			}

			return 0;
		}

		@Override
		public boolean hasValue(Object element) {

			PatchSet patchSet = findPatchSet(element);
			if (patchSet != null) {

				Map<EObject, Integer> objectChangeRefCount = patchSet.getObjectChangeRefCount();
				return objectChangeRefCount.containsKey(element);
			}

			return false;
		}

	}

}

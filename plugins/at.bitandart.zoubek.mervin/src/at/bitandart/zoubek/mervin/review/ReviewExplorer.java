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
package at.bitandart.zoubek.mervin.review;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ElementMatcher;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.IReviewHighlightServiceListener;
import at.bitandart.zoubek.mervin.model.modelreview.DiagramPatch;
import at.bitandart.zoubek.mervin.model.modelreview.DiagramResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelPatch;
import at.bitandart.zoubek.mervin.model.modelreview.ModelResource;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.Patch;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.HighlightStyler;
import at.bitandart.zoubek.mervin.patchset.history.IPatchSetHistoryEntry;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;
import at.bitandart.zoubek.mervin.util.vis.HSB;
import at.bitandart.zoubek.mervin.util.vis.NumericColoredColumnLabelProvider;
import at.bitandart.zoubek.mervin.util.vis.ThreeWayLabelTreeViewerComparator;
import at.bitandart.zoubek.mervin.util.vis.ThreeWayObjectTreeViewerComparator;

/**
 * Review exploration view - shows an overview of the currently loaded model
 * review provided by the last active editor.
 * 
 * @author Florian Zoubek
 * 
 * @see ModelReviewEditorTrackingView
 *
 */
public class ReviewExplorer extends ModelReviewEditorTrackingView implements IReviewHighlightProvidingPart {

	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.review";

	public static final String VIEW_MENU_ID = "at.bitandart.zoubek.mervin.menu.view.review.explorer";

	public static final String VIEW_MENU_ITEM_HIGHLIGHT_SWITCH_MODE = "at.bitandart.zoubek.mervin.menu.view.review.explorer.highlight.switchmode";

	@Inject
	private ESelectionService selectionService;

	@Inject
	private IReviewHighlightService highlightService;

	@Inject
	private Display display;

	private HighlightStyler highlightStyler;

	/**
	 * the complete list of filtered and derived elements to highlight in this
	 * view.
	 */
	private List<Object> objectsToHighlight = new LinkedList<>();

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

	// Data

	/**
	 * indicates if all SWT controls and viewers have been correctly set up
	 */
	private boolean viewInitialized = false;

	@PostConstruct
	public void postConstruct(Composite parent, EModelService modelService, MPart part) {

		syncMenuAndToolbarItemState(modelService, part);

		highlightStyler = new HighlightStyler(display);

		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());

		// initialize tree viewer

		reviewTreeViewer = new TreeViewer(mainPanel, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		reviewTreeViewer.setComparator(new ViewerComparator());
		reviewTreeViewer.setContentProvider(new ModelReviewContentProvider());
		reviewTreeViewer.addSelectionChangedListener(new HighlightSelectionListener(this) {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				super.selectionChanged(event);
				ISelection selection = event.getSelection();
				selectionService.setSelection(selection);
			}
		});

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
		ModelReviewExplorerMainColumnLabelProvider labelColumnLabelProvider = new ModelReviewExplorerMainColumnLabelProvider();
		labelColumn.setLabelProvider(labelColumnLabelProvider);
		labelColumn.getColumn().addSelectionListener(
				new ThreeWayObjectTreeViewerComparator(reviewTreeViewer, labelColumn, labelColumnLabelProvider));

		// change count column
		TreeViewerColumn changeCountColumn = new TreeViewerColumn(reviewTreeViewer, SWT.NONE);
		changeCountColumn.getColumn().setResizable(true);
		changeCountColumn.getColumn().setMoveable(false);
		changeCountColumn.getColumn().setText("#C");
		changeCountColumn.getColumn().setAlignment(SWT.CENTER);
		changeCountColumn.getColumn().setToolTipText("Number of changed elements");
		ChangeCountColumnLabelProvider changeCountColumnLabelProvider = new ChangeCountColumnLabelProvider(
				reviewTreeViewer, Display.getCurrent().getSystemColor(SWT.COLOR_WHITE),
				Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		changeCountColumn.setLabelProvider(changeCountColumnLabelProvider);
		changeCountColumn.getColumn().addSelectionListener(new ThreeWayLabelTreeViewerComparator(reviewTreeViewer,
				changeCountColumn, changeCountColumnLabelProvider));

		// reference count column
		TreeViewerColumn refCountColumn = new TreeViewerColumn(reviewTreeViewer, SWT.NONE);
		refCountColumn.getColumn().setResizable(true);
		refCountColumn.getColumn().setMoveable(false);
		refCountColumn.getColumn().setText("#RC");
		refCountColumn.getColumn().setAlignment(SWT.CENTER);
		refCountColumn.getColumn().setToolTipText("Number of references to the tgiven elements");
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

		// all controls updated, now update them with the given values
		viewInitialized = true;

		// refresh the viewer highlights if highlighting is requested
		highlightService.addHighlightServiceListener(new IReviewHighlightServiceListener() {

			@Override
			public void elementRemoved(ModelReview review, Object element) {

				updatesObjectToHighlight();
				reviewTreeViewer.refresh();

			}

			@Override
			public void elementAdded(ModelReview review, Object element) {

				updatesObjectToHighlight();
				reviewTreeViewer.refresh();

			}
		});

		updateValues();
	}

	/**
	 * updates the list of filtered and derived highlighted elements from the
	 * current highlight service for the current model review.
	 */
	private void updatesObjectToHighlight() {

		objectsToHighlight.clear();
		ModelReview currentModelReview = getCurrentModelReview();

		if (currentModelReview != null) {

			List<Object> highlightedElements = highlightService.getHighlightedElements(getCurrentModelReview());
			// TODO apply filter
			objectsToHighlight.addAll(highlightedElements);

			addDerivedElementsToHighlight(currentModelReview, highlightedElements, objectsToHighlight);
		}

	}

	/**
	 * adds the derived objects to highlight for the given {@link ModelReview}
	 * {@link Diff} to the given list of highlighted objects.
	 * 
	 * @param modelReview
	 *            the model review to highlight elements for.
	 * @param highlightedElements
	 *            the highlighted elements as reported by the highlight service.
	 * @param objectsToHighlight
	 *            the list of elements to add the derived highlighted elements
	 *            to.
	 */
	protected void addDerivedElementsToHighlight(ModelReview modelReview, List<Object> highlightedElements,
			List<Object> objectsToHighlight) {

		for (Object highlightedElement : highlightedElements) {

			if (highlightedElement instanceof IPatchSetHistoryEntry<?, ?>) {

				IPatchSetHistoryEntry<?, ?> historyEntry = (IPatchSetHistoryEntry<?, ?>) highlightedElement;
				Object entryObject = historyEntry.getEntryObject();

				/* check the entry object first */
				if (entryObject instanceof Diff) {

					// TODO apply filter
					Diff diff = (Diff) entryObject;
					addDerivedElementsToHighlight(diff, objectsToHighlight);
				}

				EList<PatchSet> patchSets = modelReview.getPatchSets();
				for (PatchSet patchSet : patchSets) {

					Object value = historyEntry.getValue(patchSet);
					if (value instanceof DiffWithSimilarity) {

						// TODO apply filter
						Diff diff = ((DiffWithSimilarity) value).getDiff();
						// TODO apply filter
						addDerivedElementsToHighlight(diff, objectsToHighlight);
					}
				}
			}
		}
	}

	/**
	 * adds the derived objects to highlight for the given highlighted
	 * {@link Diff} to the given list of highlighted objects.
	 * 
	 * @param diff
	 *            the highlighted diff to add derived highlighted objects to the
	 *            given list of highlighted objects
	 * @param objectsToHighlight
	 */
	protected void addDerivedElementsToHighlight(Diff diff, List<Object> objectsToHighlight) {

		Match match = diff.getMatch();
		EObject left = match.getLeft();
		EObject right = match.getRight();
		Object value = MatchUtil.getValue(diff);
		// TODO apply filter
		if (value != null) {
			objectsToHighlight.add(value);
		}
		if (left != null) {
			objectsToHighlight.add(left);
		}
		if (right != null) {
			objectsToHighlight.add(right);
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

			updatesObjectToHighlight();
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

		public ModelReviewExplorerMainColumnLabelProvider() {
			/*
			 * Obtain the registered label providers for model and diagram
			 * elements
			 */
			adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(
					new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		}

		@Override
		public void update(ViewerCell cell) {

			Object element = cell.getElement();
			StyledString text = new StyledString();
			if (isHighlighted(element, objectsToHighlight)) {
				text.append(getText(element), highlightStyler);
			} else {
				text.append(getText(element));
			}

			cell.setText(text.getString());
			cell.setStyleRanges(text.getStyleRanges());
			cell.setImage(getImage(element));

			super.update(cell);

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
		private boolean isHighlighted(Object element, List<Object> highlightedElements) {

			if (highlightedElements.contains(element)) {
				return true;
			}

			if (element instanceof View) {
				if (isHighlighted(((View) element).getElement(), highlightedElements)) {
					return true;
				}
			}

			if (element instanceof TreeItemContainer) {
				return isHighlighted(((TreeItemContainer) element).getChildren(), highlightedElements);
			}

			if (element instanceof PatchSet) {
				return isHighlighted(((PatchSet) element).getPatches(), highlightedElements);
			}

			if (element instanceof ModelPatch) {
				return isHighlighted(((ModelPatch) element).getNewModelResource(), highlightedElements)
						|| isHighlighted(((ModelPatch) element).getOldModelResource(), highlightedElements);
			}

			if (element instanceof DiagramPatch) {
				return isHighlighted(((DiagramPatch) element).getNewDiagramResource(), highlightedElements)
						|| isHighlighted(((DiagramPatch) element).getOldDiagramResource(), highlightedElements);
			}

			if (element instanceof ModelResource) {
				return isHighlighted(((ModelResource) element).getObjects(), highlightedElements);
			}

			if (element instanceof EObject) {
				return isHighlighted(((EObject) element).eContents(), highlightedElements);
			}

			return false;
		}

		/**
		 * checks if one of the elements provided by the given iterable is
		 * highlighted or not.
		 * 
		 * @param iterable
		 *            the iterable that provides the elements.
		 * @param highlightedElements
		 *            the set of elements to highlight.
		 * @return true if one of the elements provided by the given iterable
		 *         should be highlighted, false otherwise.
		 */
		public boolean isHighlighted(Iterable<?> iterable, List<Object> highlightedElements) {
			for (Object element : iterable) {
				if (isHighlighted(element, highlightedElements)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * checks if one of the elements provided by the given array is
		 * highlighted or not.
		 * 
		 * @param array
		 *            the array that provides the elements.
		 * @param highlightedElements
		 *            the set of elements to highlight.
		 * @return true if one of the elements provided by the given array
		 *         should be highlighted, false otherwise.
		 */
		public boolean isHighlighted(Object[] array, List<Object> highlightedElements) {
			for (Object element : array) {
				if (isHighlighted(element, highlightedElements)) {
					return true;
				}
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

			if (element instanceof TreeItemContainer) {
				return ((TreeItemContainer) element).getText();
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
				return adapterFactoryLabelProvider.getImage(element);
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

		public ChangeCountColumnLabelProvider(ContentViewer viewer, Color fgBright, Color fgDark) {
			super(viewer, new HSB(205.0f, 0.f, 1.0f), new HSB(205.0f, 0.59f, 0.32f), fgBright, fgDark);
		}

		@Override
		public float getMaxValue(Object element) {

			PatchSet patchSet = findPatchSet(element);
			if (patchSet != null) {
				return patchSet.getMaxObjectChangeCount();
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

				Map<EObject, Integer> objectChangeCount = patchSet.getObjectChangeCount();
				if (objectChangeCount.containsKey(element)) {
					return objectChangeCount.get(element);
				}

			}

			return 0;
		}

		@Override
		public boolean hasValue(Object element) {

			PatchSet patchSet = findPatchSet(element);
			if (patchSet != null) {

				Map<EObject, Integer> objectChangeCount = patchSet.getObjectChangeCount();
				return objectChangeCount.containsKey(element);
			}

			return false;
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

	/**
	 * An {@link ITreeContentProvider} for the model review tree. Shows
	 * currently the contents of all {@link PatchSet}s in a {@link ModelReview}
	 * as well as the involved models and diagrams.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ModelReviewContentProvider implements ITreeContentProvider {

		private AdapterFactoryContentProvider adapterFactoryContentProvider;

		private ModelReview modelReview;
		private Map<PatchSet, Collection<Object>> cachedPatchSetChildren = new HashMap<>();

		public ModelReviewContentProvider() {
			adapterFactoryContentProvider = new AdapterFactoryContentProvider(
					new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			adapterFactoryContentProvider.inputChanged(viewer, oldInput, newInput);
			// we need the root model review to find the parent of some children
			if (newInput instanceof ModelReview) {
				modelReview = (ModelReview) newInput;
			}
			// clear the cache
			cachedPatchSetChildren.clear();
		}

		@Override
		public void dispose() {
			adapterFactoryContentProvider.dispose();
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof ModelReview) {
				return true;
			}
			if (element instanceof PatchSet) {
				return true;
			}
			if (element instanceof TreeItemContainer) {
				return ((TreeItemContainer) element).hasChildren();
			}
			if (element instanceof ModelResource) {
				return !((ModelResource) element).getObjects().isEmpty();
			}
			if (element instanceof EObject) {
				return adapterFactoryContentProvider.hasChildren(element);
			}
			return false;
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof ModelReview) {
				return null;
			}
			if (element instanceof PatchSet) {
				return ((PatchSet) element).getReview();
			}
			if (element instanceof DiagramResource) {
				DiagramResource diagramResource = (DiagramResource) element;
				for (PatchSet patchSet : modelReview.getPatchSets()) {
					if (patchSet.getNewInvolvedDiagrams().contains(diagramResource)
							|| patchSet.getNewInvolvedDiagrams().contains(diagramResource)) {
						return patchSet;
					}
				}
				return null;
			}
			if (element instanceof ModelResource) {
				ModelResource modelResource = (ModelResource) element;
				for (PatchSet patchSet : modelReview.getPatchSets()) {
					if (patchSet.getNewInvolvedModels().contains(modelResource)
							|| patchSet.getOldInvolvedModels().contains(modelResource)) {
						return patchSet;
					}
				}
				return null;
			}
			if (element instanceof EObject) {
				EObject eObject = (EObject) element;
				Object parent = adapterFactoryContentProvider.getParent(element);

				/*
				 * FIXME It might be better to use an own implementation of
				 * EcoreUtil.UsageCrossReferencer to detect references to the
				 * model resource
				 */
				if (parent == null || parent instanceof Resource) {
					// search for an containing model or diagram resource
					for (PatchSet patchSet : modelReview.getPatchSets()) {
						ModelResource modelResource = findContainingModelResource(patchSet.getNewInvolvedDiagrams(),
								eObject);
						if (modelResource != null)
							return modelResource;
						modelResource = findContainingModelResource(patchSet.getOldInvolvedDiagrams(), eObject);
						if (modelResource != null)
							return modelResource;
						modelResource = findContainingModelResource(patchSet.getNewInvolvedModels(), eObject);
						if (modelResource != null)
							return modelResource;
						modelResource = findContainingModelResource(patchSet.getOldInvolvedModels(), eObject);
						if (modelResource != null)
							return modelResource;
					}
				}
				return parent;
			}
			return null;
		}

		/**
		 * finds the {@link ModelResource} that contains the given object.
		 * 
		 * @param modelResources
		 *            a {@link Collection} of {@link ModelResource}s to check
		 * @param object
		 * @return the {@link ModelResource} or null if no {@link ModelResource}
		 *         contains the object
		 */
		private ModelResource findContainingModelResource(Collection<? extends ModelResource> modelResources,
				EObject object) {
			for (ModelResource modelResource : modelResources) {
				if (modelResource.getObjects().contains(object)) {
					return modelResource;
				}
			}
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ModelReview) {
				return ((ModelReview) inputElement).getPatchSets().toArray();
			}
			return new Object[0];
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof PatchSet) {

				PatchSet patchSet = (PatchSet) parentElement;
				/*
				 * These categories do not exist in the model, so we have to
				 * create temporary containers. We cache them to make sure that
				 * these categories stay the same even if the tree is refreshed.
				 */
				if (!cachedPatchSetChildren.containsKey(patchSet)) {
					List<Object> children = new LinkedList<>();
					children.add(new InvolvedModelsTreeItem(patchSet));
					children.add(new InvolvedDiagramsTreeItem(patchSet));
					children.add(new PatchSetTreeItem(patchSet));
					cachedPatchSetChildren.put(patchSet, children);
				}
				return cachedPatchSetChildren.get(patchSet).toArray();
			}
			if (parentElement instanceof TreeItemContainer) {
				return ((TreeItemContainer) parentElement).getChildren();
			}
			if (parentElement instanceof ModelResource) {
				return ((ModelResource) parentElement).getObjects().toArray();
			}
			if (parentElement instanceof EObject) {
				return adapterFactoryContentProvider.getChildren(parentElement);
			}
			return new Object[0];
		}
	}

	/**
	 * Base interface for a temporary container of elements in an
	 * {@link TreeViewer}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private interface TreeItemContainer {

		/**
		 * @return true if the container has children, false otherwise
		 */
		public boolean hasChildren();

		/**
		 * @return an array of all children of this container
		 */
		public Object[] getChildren();

		/**
		 * @return the label text for this container
		 */
		public String getText();
	}

	/**
	 * A temporary container for all patches of an {@link PatchSet}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class PatchSetTreeItem implements TreeItemContainer {

		private PatchSet patchSet;

		public PatchSetTreeItem(PatchSet patchSet) {
			super();
			this.patchSet = patchSet;
		}

		@Override
		public boolean hasChildren() {
			return !patchSet.getPatches().isEmpty();
		}

		@Override
		public Object[] getChildren() {
			return patchSet.getPatches().toArray();
		}

		@Override
		public String getText() {
			return "Patches";
		}
	}

	/**
	 * A temporary container for the involved models of an {@link PatchSet}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class InvolvedModelsTreeItem implements TreeItemContainer {

		private PatchSet patchSet;

		public InvolvedModelsTreeItem(PatchSet patchSet) {
			super();
			this.patchSet = patchSet;
		}

		@Override
		public boolean hasChildren() {
			return !patchSet.getNewInvolvedModels().isEmpty();
		}

		@Override
		public Object[] getChildren() {
			return patchSet.getNewInvolvedModels().toArray();
		}

		@Override
		public String getText() {
			return "Involved models";
		}
	}

	/**
	 * A temporary container for the involved diagrams of an {@link PatchSet}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class InvolvedDiagramsTreeItem implements TreeItemContainer {

		private PatchSet patchSet;

		public InvolvedDiagramsTreeItem(PatchSet patchSet) {
			super();
			this.patchSet = patchSet;
		}

		@Override
		public boolean hasChildren() {
			return !patchSet.getNewInvolvedDiagrams().isEmpty();
		}

		@Override
		public Object[] getChildren() {
			return patchSet.getNewInvolvedDiagrams().toArray();
		}

		@Override
		public String getText() {
			return "Involved diagrams";
		}
	}

}

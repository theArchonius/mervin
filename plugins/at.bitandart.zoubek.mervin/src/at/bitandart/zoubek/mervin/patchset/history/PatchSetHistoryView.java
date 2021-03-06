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
package at.bitandart.zoubek.mervin.patchset.history;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ElementMatcher;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;

import com.google.common.base.Predicate;

import at.bitandart.zoubek.mervin.IMatchHelper;
import at.bitandart.zoubek.mervin.IOverlayTypeHelper;
import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.IReviewHighlightServiceListener;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.DefaultOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.IOverlayTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.overlay.OverlayType;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.patchset.history.ISimilarityHistoryService.DiffWithSimilarity;
import at.bitandart.zoubek.mervin.patchset.history.organizers.IPatchSetHistoryEntryOrganizer;
import at.bitandart.zoubek.mervin.review.HighlightHoveredTreeItemMouseTracker;
import at.bitandart.zoubek.mervin.review.HighlightMode;
import at.bitandart.zoubek.mervin.review.HighlightRevealer;
import at.bitandart.zoubek.mervin.review.HighlightSelectionListener;
import at.bitandart.zoubek.mervin.review.IReviewHighlightingPart;
import at.bitandart.zoubek.mervin.review.ModelReviewEditorTrackingView;
import at.bitandart.zoubek.mervin.swt.ProgressPanel;
import at.bitandart.zoubek.mervin.swt.ProgressPanelOperationThread;
import at.bitandart.zoubek.mervin.swt.text.styles.ComposedStyler;
import at.bitandart.zoubek.mervin.swt.text.styles.HighlightStyler;
import at.bitandart.zoubek.mervin.swt.text.styles.OverlayTypeStyler;
import at.bitandart.zoubek.mervin.util.vis.ThreeWayObjectTreeViewerComparator;

/**
 * Shows the similarity of differences to differences of other patch sets.
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
 */
public class PatchSetHistoryView extends ModelReviewEditorTrackingView implements IReviewHighlightingPart, IAdaptable {

	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.patchset.history";

	public static final String PART_TOOLBAR_ID = "at.bitandart.zoubek.mervin.toolbar.patchset.history";

	public static final String VIEW_MENU_ID = "at.bitandart.zoubek.mervin.menu.view.patchset.history";

	public static final String VIEW_MENU_ITEM_HIGHLIGHT_SWITCH_MODE = "at.bitandart.zoubek.mervin.menu.view.patchset.history.highlight.switchmode";

	public static final String VIEW_MENU_ITEM_MERGE_EQUAL_DIFFS = "at.bitandart.zoubek.mervin.menu.view.patchset.history.mergeequaldiffs";

	public static final String VIEW_MENU_ITEM_RADIO_VISIBLE_DIFFS_ALL_PATCHSETS = "at.bitandart.zoubek.mervin.menu.view.patchset.history.visiblediffs.allpatchsets";

	public static final String VIEW_MENU_ITEM_RADIO_VISIBLE_DIFFS_NEW_PATCHSET = "at.bitandart.zoubek.mervin.menu.view.patchset.history.visiblediffs.newpatchset";

	public static final String VIEW_MENU_ITEM_RADIO_VISIBLE_DIFFS_OLD_PATCHSET = "at.bitandart.zoubek.mervin.menu.view.patchset.history.visiblediffs.oldpatchset";

	/**
	 * the menu id of the context menu for this view
	 */
	public static final String VIEW_CONTEXTMENU_ID = "at.bitandart.zoubek.mervin.contextmenu.view.patchset.history";

	public enum VisibleDiffMode {
		ALL_DIFFS, NEW_PATCHSET_DIFFS, OLD_PATCHSET_DIFFS
	}

	/**
	 * indicates if all SWT controls and viewers have been correctly set up
	 */
	private boolean viewInitialized = false;

	private EContentAdapter patchSetHistoryViewUpdater;

	/**
	 * the current {@link HighlightMode}, never null;
	 */
	private HighlightMode highlightMode = HighlightMode.SELECTION;

	@Inject
	private ISimilarityHistoryService similarityHistoryService;
	@Inject
	private IReviewHighlightService highlightService;

	@Inject
	private IOverlayTypeHelper overlayTypeHelper;

	@Inject
	private IMatchHelper matchHelper;

	@Inject
	private IEventBroker eventBroker;

	@Inject
	private EMenuService menuService;

	@Inject
	private ESelectionService selectionService;

	@Inject
	private Logger logger;

	private IPatchSetHistoryEntryOrganizer entryOrganizer;

	private VisibleDiffMode visibleDiffs = VisibleDiffMode.ALL_DIFFS;

	private boolean mergeEqualDiffs = true;

	private Set<Object> highlightedElements = new HashSet<>();

	// JFace Viewers

	/**
	 * tree viewer that shows the patch set history of the model review
	 */
	private TreeViewer historyTreeViewer;

	// SWT Controls

	/**
	 * the main panel of this view, that contains all controls for this view
	 */
	private Composite mainPanel;

	private TreeViewerColumn labelColumn;

	@Inject
	private Display display;

	// Colors

	private Color progressBackgroundColor;
	private Color progressForegroundColor;

	private ProgressPanel progressPanel;

	private ProgressPanelOperationThread currentProcessingThread;

	private DiffNameColumnLabelProvider labelColumnLabelProvider;

	private boolean ignoreSelectionHighlight = false;

	private HighlightRevealer highlightRevealer;

	@Inject
	public PatchSetHistoryView() {
		patchSetHistoryViewUpdater = new UpdatePatchSetHistoryViewAdapter();

	}

	@Inject
	public void setEntryOrganizer(IPatchSetHistoryEntryOrganizer entryOrganizer) {
		this.entryOrganizer = entryOrganizer;
	}

	@PostConstruct
	public void postConstruct(Composite parent, EModelService modelService, MPart part) {

		syncMenuAndToolbarItemState(modelService, part);

		initializeColors();

		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());

		// progress information panel

		progressPanel = new ProgressPanel(mainPanel, SWT.BORDER);
		progressPanel.setBackground(progressBackgroundColor);
		progressPanel.setForeground(progressForegroundColor);
		progressPanel.setVisible(false);
		progressPanel.setLayout(new GridLayout());
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.exclude = true;
		progressPanel.setLayoutData(gridData);

		// initialize tree viewer

		final PatchSetHistoryContentProvider patchSetHistoryContentProvider = new PatchSetHistoryContentProvider();
		historyTreeViewer = new TreeViewer(mainPanel, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.VIRTUAL);
		historyTreeViewer.setUseHashlookup(true);
		historyTreeViewer.setContentProvider(patchSetHistoryContentProvider);
		historyTreeViewer.addSelectionChangedListener(new HighlightSelectionListener(this) {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (!ignoreSelectionHighlight) {
					super.selectionChanged(event);
				}
				ISelection selection = event.getSelection();
				selectionService.setSelection(selection);
				eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
			}

			@Override
			protected void addElementsToHighlight(Object object, Set<Object> elements) {

				if (object instanceof Match) {
					addElementsToHighlight((Match) object, elements);

				} else if (object instanceof DiffWithSimilarity) {
					addElementsToHighlight((DiffWithSimilarity) object, elements);

				} else if (object instanceof IPatchSetHistoryEntry<?, ?>) {
					addElementsToHighlight((IPatchSetHistoryEntry<?, ?>) object, elements);

				} else {
					super.addElementsToHighlight(object, elements);
				}
			}

			/**
			 * adds the elements to highlight for the given
			 * {@link ObjectHistoryEntryContainer}.
			 * 
			 * @param entry
			 *            the {@link ObjectHistoryEntryContainer} to obtain the
			 *            elements to highlight from.
			 * @param elements
			 *            the set of elements to highlight.
			 */
			protected void addElementsToHighlight(ObjectHistoryEntryContainer entry, Set<Object> elements) {

				ModelReview modelReview = getCurrentModelReview();

				Map<PatchSet, Match> matches = entry.getMatches(modelReview.getPatchSets());

				for (Match match : matches.values()) {
					addElementsToHighlight(match, elements);
				}
			}

			/**
			 * adds the elements to highlight for the given {@link Match}.
			 * 
			 * @param entry
			 *            the match to obtain the elements to highlight from.
			 * @param elements
			 *            the set of elements to highlight.
			 */
			protected void addElementsToHighlight(Match match, Set<Object> elements) {

				EObject newValue = matchHelper.getNewValue(match);
				if (newValue != null) {
					elements.add(newValue);
				}
				EObject oldValue = matchHelper.getOldValue(match);
				if (oldValue != null) {
					elements.add(oldValue);
				}
			}

			/**
			 * adds the elements to highlight for the given
			 * {@link NamedHistoryEntryContainer}.
			 * 
			 * @param entry
			 *            the entry to obtain the elements to highlight from.
			 * @param elements
			 *            the set of elements to highlight.
			 */
			protected void addElementsToHighlight(NamedHistoryEntryContainer entry, Set<Object> elements) {

				for (IPatchSetHistoryEntry<?, ?> subEntry : entry.getSubEntries()) {
					addElementsToHighlight(subEntry, elements);
				}
			}

			/**
			 * adds the elements to highlight for the given
			 * {@link IPatchSetHistoryEntry}.
			 * 
			 * @param entry
			 *            the entry to obtain the elements to highlight from.
			 * @param elements
			 *            the set of elements to highlight.
			 */
			protected void addElementsToHighlight(IPatchSetHistoryEntry<?, ?> entry, Set<Object> elements) {

				if (entry instanceof ObjectHistoryEntryContainer) {
					addElementsToHighlight((ObjectHistoryEntryContainer) entry, elements);

				} else if (entry instanceof NamedHistoryEntryContainer) {
					addElementsToHighlight((NamedHistoryEntryContainer) entry, elements);

				} else {
					addElementsToHighlight(entry.getEntryObject(), elements);

					EList<PatchSet> patchSets = getCurrentModelReview().getPatchSets();
					for (PatchSet patchSet : patchSets) {
						addElementsToHighlight(entry.getValue(patchSet), elements);
					}
				}
			}

			/**
			 * 
			 * adds the elements to highlight for the given
			 * {@link DiffWithSimilarity}.
			 * 
			 * @param diffWithSimilarity
			 *            the {@link DiffWithSimilarity} to obtain the elements
			 *            to highlight from.
			 * @param elements
			 *            the set of elements to highlight.
			 */
			protected void addElementsToHighlight(DiffWithSimilarity diffWithSimilarity, Set<Object> elements) {
				addElementsToHighlight(diffWithSimilarity.getDiff(), elements);
			}
		});

		menuService.registerContextMenu(historyTreeViewer.getControl(), VIEW_CONTEXTMENU_ID);

		Tree histroryTree = historyTreeViewer.getTree();
		histroryTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		histroryTree.setLinesVisible(false);
		histroryTree.setHeaderVisible(true);
		histroryTree.addMouseTrackListener(new HighlightHoveredTreeItemMouseTracker(this));

		// set up all columns of the tree

		labelColumn = new TreeViewerColumn(historyTreeViewer, SWT.NONE);
		labelColumn.getColumn().setResizable(true);
		labelColumn.getColumn().setMoveable(true);
		labelColumn.getColumn().setText("Diff");
		labelColumn.getColumn().setWidth(400);

		labelColumnLabelProvider = new DiffNameColumnLabelProvider(patchSetHistoryContentProvider, overlayTypeHelper);
		labelColumn.setLabelProvider(labelColumnLabelProvider);
		ThreeWayObjectTreeViewerComparator defaultComparator = new ThreeWayObjectTreeViewerComparator(historyTreeViewer,
				labelColumn, labelColumnLabelProvider);
		labelColumn.getColumn().addSelectionListener(defaultComparator);
		historyTreeViewer.setComparator(defaultComparator);

		highlightRevealer = new HighlightRevealer(historyTreeViewer, new Predicate<Object>() {

			@Override
			public boolean apply(Object element) {

				if (labelColumnLabelProvider.isHighlighted(element, highlightedElements)) {
					Object[] children = patchSetHistoryContentProvider.getChildren(element);
					for (Object child : children) {
						if (labelColumnLabelProvider.isHighlighted(child, highlightedElements)) {
							return false;
						}
					}
					return true;
				}
				return false;
			}
		}, new Predicate<Object>() {

			@Override
			public boolean apply(Object element) {
				return labelColumnLabelProvider.isHighlighted(element, highlightedElements);
			}
		});

		viewInitialized = true;

		// refresh the viewer highlights if highlighting is requested
		highlightService.addHighlightServiceListener(new IReviewHighlightServiceListener() {

			@Override
			public void elementsRemoved(ModelReview review, Set<Object> element) {

				updateHighlightedElements();
			}

			/**
			 * updates the highlighted elements cache.
			 */
			private void updateHighlightedElements() {
				if (currentProcessingThread != null
						&& currentProcessingThread.getRunnable() instanceof PatchSetHistoryHighlightUpdater
						&& currentProcessingThread.isAlive()) {
					currentProcessingThread.cancelOperation();
					currentProcessingThread = null;
				}

				highlightedElements = new HashSet<>();

				currentProcessingThread = new ProgressPanelOperationThread(
						new PatchSetHistoryHighlightUpdater(highlightedElements, highlightService, historyTreeViewer,
								getHighlightedModelReview(), eventBroker),
						progressPanel, mainPanel, logger);
				currentProcessingThread.start();

				historyTreeViewer.getControl().getDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						historyTreeViewer.refresh();
					}
				});
			}

			@Override
			public void elementsAdded(ModelReview review, Set<Object> element) {

				updateHighlightedElements();
			}
		});
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
		/* highlight mode -> disabled by default */
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

		/* visible diffs -> all patch sets is default */
		matcher = new ElementMatcher(VIEW_MENU_ITEM_RADIO_VISIBLE_DIFFS_ALL_PATCHSETS, MHandledMenuItem.class,
				(List<String>) null);
		items = modelService.findElements(part, MHandledMenuItem.class, EModelService.IN_PART, matcher);
		for (MHandledMenuItem item : items) {
			item.setSelected(true);
		}

		matcher = new ElementMatcher(VIEW_MENU_ITEM_RADIO_VISIBLE_DIFFS_OLD_PATCHSET, MHandledMenuItem.class,
				(List<String>) null);
		items = modelService.findElements(part, MHandledMenuItem.class, EModelService.IN_PART, matcher);
		for (MHandledMenuItem item : items) {
			item.setSelected(false);
		}

		matcher = new ElementMatcher(VIEW_MENU_ITEM_RADIO_VISIBLE_DIFFS_NEW_PATCHSET, MHandledMenuItem.class,
				(List<String>) null);
		items = modelService.findElements(part, MHandledMenuItem.class, EModelService.IN_PART, matcher);
		for (MHandledMenuItem item : items) {
			item.setSelected(false);
		}

		/* merge equal diffs -> enabled by default */
		matcher = new ElementMatcher(VIEW_MENU_ITEM_MERGE_EQUAL_DIFFS, MHandledMenuItem.class, (List<String>) null);
		items = modelService.findElements(part, MHandledMenuItem.class, EModelService.IN_PART, matcher);
		for (MHandledMenuItem item : items) {
			item.setSelected(true);
		}
	}

	/**
	 * initializes the colors for the controls of this view.
	 */
	private void initializeColors() {
		progressBackgroundColor = new Color(display, new RGB(255, 230, 128));
		progressForegroundColor = new Color(display, new RGB(212, 170, 0));
	}

	@Override
	protected void updateValues() {
		// we cannot update the controls if they are not initialized yet
		if (viewInitialized) {

			final ModelReview currentModelReview = getCurrentModelReview();

			if (currentModelReview != null) {

				if (!currentModelReview.eAdapters().contains(patchSetHistoryViewUpdater)) {
					currentModelReview.eAdapters().add(patchSetHistoryViewUpdater);
				}

				if (currentProcessingThread != null && currentProcessingThread.isAlive()) {
					/*
					 * update thread is already running - cancel the operation
					 */
					currentProcessingThread.cancelOperation();
				}

				PatchSet activePatchSet = null;
				if (visibleDiffs == VisibleDiffMode.NEW_PATCHSET_DIFFS) {
					activePatchSet = currentModelReview.getRightPatchSet();
				} else if (visibleDiffs == VisibleDiffMode.OLD_PATCHSET_DIFFS) {
					activePatchSet = currentModelReview.getLeftPatchSet();
				}

				highlightedElements = new HashSet<>();

				currentProcessingThread = new ProgressPanelOperationThread(
						new PatchSetHistoryTreeUpdater(currentModelReview, activePatchSet, mergeEqualDiffs,
								similarityHistoryService, entryOrganizer, historyTreeViewer, labelColumn,
								new PatchSetHistoryHighlightUpdater(highlightedElements, highlightService,
										historyTreeViewer, getHighlightedModelReview(), eventBroker)),
						progressPanel, mainPanel, logger);

				currentProcessingThread.start();

			} else {
				// no current review, so reset the input of the history tree
				historyTreeViewer.setInput(Collections.EMPTY_LIST);
			}

			mainPanel.redraw();
			mainPanel.update();
		}

	}

	@PreDestroy
	private void dispose() {
		labelColumnLabelProvider.dispose();
		progressBackgroundColor.dispose();
		progressForegroundColor.dispose();
	}

	@Override
	public ModelReview getHighlightedModelReview() {
		return getCurrentModelReview();
	}

	@Override
	public HighlightMode getHighlightMode() {
		return highlightMode;
	}

	@Override
	public void setHighlightMode(HighlightMode highlightMode) {

		if (highlightMode != null) {
			this.highlightMode = highlightMode;
			highlightService.clearHighlights(getCurrentModelReview());
		}
	}

	@Override
	public IReviewHighlightService getReviewHighlightService() {
		return highlightService;
	}

	private class UpdatePatchSetHistoryViewAdapter extends EContentAdapter {

		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			Object feature = notification.getFeature();
			if (visibleDiffs != VisibleDiffMode.ALL_DIFFS
					&& (feature == ModelReviewPackage.Literals.MODEL_REVIEW__LEFT_PATCH_SET
							|| feature == ModelReviewPackage.Literals.MODEL_REVIEW__RIGHT_PATCH_SET))
				display.syncExec(new Runnable() {

					@Override
					public void run() {
						updateValues();
					}
				});
		}

	}

	/**
	 * @param visibleDiffMode
	 */
	public void setVisibleDiffs(VisibleDiffMode visibleDiffMode) {
		this.visibleDiffs = visibleDiffMode;
		updateValues();
	}

	/**
	 * 
	 * @param mergeEqualDiffs
	 *            true if equal diffs should be merged into a single entry,
	 *            false otherwise
	 */
	public void setMergeEqualDiffs(boolean mergeEqualDiffs) {
		this.mergeEqualDiffs = mergeEqualDiffs;
		updateValues();
	}

	@Override
	public <T> T getAdapter(Class<T> adapterType) {

		if (adapterType == ModelReview.class) {
			return adapterType.cast(getCurrentModelReview());
		}
		return null;
	}

	/**
	 * A {@link ITreeContentProvider} that provides the objects for the patch
	 * set history. This content provider only supports one viewer per instance,
	 * so do not share a single instance of it across multiple viewers.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public class PatchSetHistoryContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Collection<?>) {
				return ((Collection<?>) inputElement).toArray();
			}
			return new Object[0];
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof IPatchSetHistoryEntry<?, ?>) {
				return ((IPatchSetHistoryEntry<?, ?>) parentElement).getSubEntries().toArray();
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof IPatchSetHistoryEntry<?, ?>) {
				return ((IPatchSetHistoryEntry<?, ?>) element).getParent();
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof IPatchSetHistoryEntry<?, ?>) {
				return !((IPatchSetHistoryEntry<?, ?>) element).getSubEntries().isEmpty();
			}
			return false;
		}

	}

	/**
	 * A {@link ColumnLabelProvider} implementation that provides labels for
	 * {@link NamedHistoryEntryContainer}s, as well as
	 * {@link IPatchSetHistoryEntry} entry objects which are instances of EMF
	 * compare model elements. This class allocates system memory and should be
	 * disposed once it is not used any more.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class DiffNameColumnLabelProvider extends StyledCellLabelProvider implements Comparator<Object> {

		private AdapterFactoryLabelProvider adapterFactoryLabelProvider;
		private ITreeContentProvider contentProvider;
		private IOverlayTypeStyleAdvisor styleAdvisor;
		private EnumMap<OverlayType, Styler> overlayTypeStylers = new EnumMap<>(OverlayType.class);
		private IOverlayTypeHelper overlayTypeHelper;
		private HighlightStyler highlightStyler;

		public DiffNameColumnLabelProvider(ITreeContentProvider contentProvider, IOverlayTypeHelper overlayTypeHelper) {

			adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(new ComposedAdapterFactory(
					EMFCompareRCPPlugin.getDefault().createFilteredAdapterFactoryRegistry()));
			this.contentProvider = contentProvider;

			this.overlayTypeHelper = overlayTypeHelper;

			styleAdvisor = new DefaultOverlayTypeStyleAdvisor();

			highlightStyler = new HighlightStyler(display);

			for (OverlayType overlayType : OverlayType.values()) {
				overlayTypeStylers.put(overlayType, new OverlayTypeStyler(overlayType, styleAdvisor));
			}
		}

		@Override
		public void update(ViewerCell cell) {

			Object element = cell.getElement();
			StyledString text = new StyledString();

			List<Styler> stylers = new ArrayList<Styler>();

			if (isHighlighted(element, highlightedElements)) {
				stylers.add(highlightStyler);
			}

			if (element instanceof IPatchSetHistoryEntry<?, ?>) {
				Object entryObject = ((IPatchSetHistoryEntry<?, ?>) element).getEntryObject();
				if (entryObject instanceof Diff) {
					Styler overlayStyler = overlayTypeStylers
							.get(overlayTypeHelper.toOverlayType(((Diff) entryObject).getKind()));
					if (overlayStyler != null) {
						stylers.add(overlayStyler);
					}
				}
			}

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

			if (element instanceof NamedHistoryEntryContainer) {

				NamedHistoryEntryContainer container = (NamedHistoryEntryContainer) element;
				text.append(" ");
				text.append(MessageFormat.format("({0})", countTotalNumberOfEntries(container)),
						StyledString.COUNTER_STYLER);

			}

			cell.setText(text.getString());
			cell.setStyleRanges(text.getStyleRanges());
			cell.setImage(getImage(element));

			super.update(cell);

		}

		/**
		 * counts the total number of child entries of the given entry.
		 * 
		 * @param entry
		 *            the entry to count the child entries for.
		 * @return the number of child entries.
		 */
		private int countTotalNumberOfEntries(IPatchSetHistoryEntry<?, ?> entry) {
			List<IPatchSetHistoryEntry<?, ?>> subEntries = entry.getSubEntries();
			if (subEntries.isEmpty()) {
				return 1;
			}
			int size = 0;
			boolean containsSubDiffEntry = false;
			for (IPatchSetHistoryEntry<?, ?> subEntry : subEntries) {
				if (subEntry instanceof SubDiffEntry) {
					containsSubDiffEntry = true;
				} else {
					size += countTotalNumberOfEntries(subEntry);
				}
			}
			if (containsSubDiffEntry) {
				size++;
			}
			return size;
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
		public boolean isHighlighted(Object element, Set<Object> highlightedElements) {

			if (element == null) {
				return false;
			}

			if (highlightedElements.contains(element)) {
				return true;
			}

			return false;
		}

		public String getText(Object element) {

			if (element instanceof NamedHistoryEntryContainer) {

				NamedHistoryEntryContainer container = (NamedHistoryEntryContainer) element;
				return container.getName();

			} else if (element instanceof IPatchSetHistoryEntry) {

				Object entryObject = ((IPatchSetHistoryEntry<?, ?>) element).getEntryObject();
				// delegate to the default EMF compare label provider
				String providerText = adapterFactoryLabelProvider.getText(entryObject);

				if (element instanceof SubDiffEntry) {
					return MessageFormat.format("PS#{0}: {1}", ((SubDiffEntry) element).getPatchSet().getId(),
							providerText);
				}

				return providerText;

			}
			return element.toString();
		}

		public Image getImage(Object element) {

			if (element instanceof IPatchSetHistoryEntry) {

				Object entryObject = ((IPatchSetHistoryEntry<?, ?>) element).getEntryObject();
				// delegate to the default EMF compare label provider
				try {
					return adapterFactoryLabelProvider.getImage(entryObject);
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
		public int compare(Object object1, Object object2) {
			String text1 = getText(object1);
			String text2 = getText(object2);
			return Policy.getComparator().compare(text1, text2);
		}

		public void dispose() {
			highlightStyler.dispose();
		}

	}

	@Override
	public void revealNextHighlight() {
		ignoreSelectionHighlight = true;
		highlightRevealer.revealNextHighlight();
		ignoreSelectionHighlight = false;
	}

	@Override
	public void revealPreviousHighlight() {
		ignoreSelectionHighlight = true;
		highlightRevealer.revealPreviousHighlight();
		ignoreSelectionHighlight = false;
	}

	@Override
	public boolean hasNextHighlight() {
		return highlightRevealer.hasNextHighlight();
	}

	@Override
	public boolean hasPreviousHighlight() {
		return highlightRevealer.hasPreviousHighlight();
	}

}
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
package at.bitandart.zoubek.mervin.review;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import at.bitandart.zoubek.mervin.model.modelreview.DiagramInstance;
import at.bitandart.zoubek.mervin.model.modelreview.ModelInstance;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.Patch;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.util.vis.HSB;
import at.bitandart.zoubek.mervin.util.vis.NumericColoredColumnLabelProvider;

public class ReviewExplorer extends ModelReviewEditorTrackingView {

	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.review";

	// JFace Viewers

	private TreeViewer reviewTreeViewer;

	// SWT Controls

	private Composite mainPanel;
	private TreeViewerColumn labelColumn;
	private TreeViewerColumn changeCountColumn;

	// Data

	private boolean viewInitialized = false;

	@Inject
	public ReviewExplorer() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());

		reviewTreeViewer = new TreeViewer(mainPanel, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);
		reviewTreeViewer.setComparator(new ViewerComparator());
		reviewTreeViewer.setContentProvider(new ModelReviewContentProvider());
		Tree reviewTree = reviewTreeViewer.getTree();
		reviewTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		reviewTree.setLinesVisible(false);
		reviewTree.setHeaderVisible(true);

		labelColumn = new TreeViewerColumn(reviewTreeViewer, SWT.NONE);
		labelColumn.getColumn().setResizable(true);
		labelColumn.getColumn().setMoveable(true);
		labelColumn.getColumn().setText("Element");
		labelColumn.getColumn().setWidth(200);
		labelColumn
				.setLabelProvider(new ModelReviewExplorerMainColumnLabelProvider());
		labelColumn.getColumn().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				switch (reviewTreeViewer.getTree().getSortDirection()) {
				case SWT.UP:
					reviewTreeViewer.getTree().setSortDirection(SWT.DOWN);
					break;
				case SWT.DOWN:
					reviewTreeViewer.getTree().setSortDirection(SWT.NONE);
					break;
				default:
				case SWT.NONE:
					reviewTreeViewer.getTree().setSortDirection(SWT.UP);
					break;
				}
				reviewTreeViewer.getTree().setSortColumn(
						labelColumn.getColumn());
				reviewTreeViewer.refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		changeCountColumn = new TreeViewerColumn(reviewTreeViewer, SWT.NONE);
		changeCountColumn.getColumn().setResizable(true);
		changeCountColumn.getColumn().setMoveable(false);
		changeCountColumn.getColumn().setText("#C");
		changeCountColumn.getColumn().setAlignment(SWT.CENTER);
		changeCountColumn.getColumn().setToolTipText(
				"Number of changed elements");
		changeCountColumn.setLabelProvider(new ChangeCountColumnLabelProvider(
				reviewTreeViewer, Display.getCurrent().getSystemColor(
						SWT.COLOR_WHITE), Display.getCurrent().getSystemColor(
						SWT.COLOR_BLACK)));
		changeCountColumn.getColumn().addSelectionListener(
				new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						switch (reviewTreeViewer.getTree().getSortDirection()) {
						case SWT.UP:
							reviewTreeViewer.getTree().setSortDirection(
									SWT.DOWN);
							break;
						case SWT.DOWN:
							reviewTreeViewer.getTree().setSortDirection(
									SWT.NONE);
							break;
						default:
						case SWT.NONE:
							reviewTreeViewer.getTree().setSortDirection(SWT.UP);
							break;
						}
						reviewTreeViewer.getTree().setSortColumn(
								changeCountColumn.getColumn());
						reviewTreeViewer.refresh();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});

		TreeViewerColumn refChangeCountColumn = new TreeViewerColumn(
				reviewTreeViewer, SWT.NONE);
		refChangeCountColumn.getColumn().setResizable(true);
		refChangeCountColumn.getColumn().setMoveable(false);
		refChangeCountColumn.getColumn().setText("#RC");
		refChangeCountColumn.getColumn().setAlignment(SWT.CENTER);
		refChangeCountColumn.getColumn().setToolTipText(
				"Number of references to changed elements");
		refChangeCountColumn
				.setLabelProvider(new ReferencedChangeCountColumnLabelProvider(
						reviewTreeViewer, Display.getCurrent().getSystemColor(
								SWT.COLOR_WHITE), Display.getCurrent()
								.getSystemColor(SWT.COLOR_BLACK)));

		TreeViewerColumn resourceColumn = new TreeViewerColumn(
				reviewTreeViewer, SWT.NONE);
		resourceColumn.getColumn().setResizable(true);
		resourceColumn.getColumn().setMoveable(true);
		resourceColumn.getColumn().setText("Resource");
		resourceColumn.getColumn().setWidth(200);
		resourceColumn
				.setLabelProvider(new ModelReviewExplorerResourceColumnLabelProvider());

		viewInitialized = true;
		updateValues();
	}

	@Override
	protected void updateValues() {
		if (viewInitialized) {

			ModelReview currentModelReview = getCurrentModelReview();

			reviewTreeViewer.setInput(currentModelReview);
			reviewTreeViewer.refresh();
			for (TreeColumn treeColumn : reviewTreeViewer.getTree()
					.getColumns()) {
				treeColumn.pack();
			}
			reviewTreeViewer.getTree().layout();
			mainPanel.layout();
		}
	}

	private class ModelReviewExplorerMainColumnLabelProvider extends
			ColumnLabelProvider {

		private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

		public ModelReviewExplorerMainColumnLabelProvider() {
			adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(
					new ComposedAdapterFactory(
							ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		}

		@Override
		public String getText(Object element) {
			if (element instanceof PatchSet) {
				return MessageFormat.format("PatchSet #{0}",
						((PatchSet) element).getId());
			}
			if (element instanceof TreeItemContainer) {
				return ((TreeItemContainer) element).getText();
			}
			if (element instanceof Patch) {
				return ((Patch) element).getNewPath();
			}
			if (element instanceof ModelInstance) {
				return ((ModelInstance) element).getRootPackages().get(0)
						.getName();
			}
			if (element instanceof EObject) {
				return adapterFactoryLabelProvider.getText(element);
			}
			return element.toString();
		}

		@Override
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
			return adapterFactoryLabelProvider.isLabelProperty(element,
					property);
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			adapterFactoryLabelProvider.removeListener(listener);
			super.removeListener(listener);
		}
	}

	private class ModelReviewExplorerResourceColumnLabelProvider extends
			ColumnLabelProvider {

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

	private class ChangeCountColumnLabelProvider extends
			NumericColoredColumnLabelProvider {

		ContentViewer viewer;

		public ChangeCountColumnLabelProvider(ContentViewer viewer,
				Color fgBright, Color fgDark) {
			super(new HSB(205.0f, 0.f, 1.0f), new HSB(205.0f, 0.59f, 0.32f),
					fgBright, fgDark);
			this.viewer = viewer;
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

				Map<EObject, Integer> objectChangeCount = patchSet
						.getObjectChangeCount();
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

				Map<EObject, Integer> objectChangeCount = patchSet
						.getObjectChangeCount();
				return objectChangeCount.containsKey(element);
			}

			return false;
		}

		private PatchSet findPatchSet(Object element) {

			ITreeContentProvider contentProvider = (ITreeContentProvider) viewer
					.getContentProvider();
			Object currentElement = element;

			while (currentElement != null
					&& !(currentElement instanceof PatchSet)) {
				currentElement = contentProvider.getParent(currentElement);
			}

			if (currentElement instanceof PatchSet) {
				return (PatchSet) currentElement;
			}

			return null;
		}

	}

	private class ReferencedChangeCountColumnLabelProvider extends
			NumericColoredColumnLabelProvider {

		ContentViewer viewer;

		public ReferencedChangeCountColumnLabelProvider(ContentViewer viewer,
				Color fgBright, Color fgDark) {
			super(new HSB(205.0f, 0.f, 1.0f), new HSB(205.0f, 0.59f, 0.32f),
					fgBright, fgDark);
			this.viewer = viewer;
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

				Map<EObject, Integer> objectChangeRefCount = patchSet
						.getObjectChangeRefCount();
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

				Map<EObject, Integer> objectChangeRefCount = patchSet
						.getObjectChangeRefCount();
				return objectChangeRefCount.containsKey(element);
			}

			return false;
		}

		private PatchSet findPatchSet(Object element) {

			ITreeContentProvider contentProvider = (ITreeContentProvider) viewer
					.getContentProvider();
			Object currentElement = element;

			while (currentElement != null
					&& !(currentElement instanceof PatchSet)) {
				currentElement = contentProvider.getParent(currentElement);
			}

			if (currentElement instanceof PatchSet) {
				return (PatchSet) currentElement;
			}

			return null;
		}

	}

	private class ModelReviewContentProvider implements ITreeContentProvider {

		private AdapterFactoryContentProvider adapterFactoryContentProvider;

		private ModelReview modelReview;

		public ModelReviewContentProvider() {
			adapterFactoryContentProvider = new AdapterFactoryContentProvider(
					new ComposedAdapterFactory(
							ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			adapterFactoryContentProvider.inputChanged(viewer, oldInput,
					newInput);
			if (newInput instanceof ModelReview) {
				modelReview = (ModelReview) newInput;
			}
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
			if (element instanceof ModelInstance) {
				return !((ModelInstance) element).getObjects().isEmpty();
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
			if (element instanceof DiagramInstance) {
				DiagramInstance diagramInstance = (DiagramInstance) element;
				for (PatchSet patchSet : modelReview.getPatchSets()) {
					if (patchSet.getNewInvolvedDiagrams().contains(
							diagramInstance)
							|| patchSet.getNewInvolvedDiagrams().contains(
									diagramInstance)) {
						return patchSet;
					}
				}
				return null;
			}
			if (element instanceof ModelInstance) {
				ModelInstance modelInstance = (ModelInstance) element;
				for (PatchSet patchSet : modelReview.getPatchSets()) {
					if (patchSet.getNewInvolvedModels().contains(modelInstance)
							|| patchSet.getOldInvolvedModels().contains(
									modelInstance)) {
						return patchSet;
					}
				}
				return null;
			}
			if (element instanceof EObject) {
				EObject eObject = (EObject) element;
				Object parent = adapterFactoryContentProvider
						.getParent(element);

				/*
				 * FIXME It might be better to use an own implementation of
				 * EcoreUtil.UsageCrossReferencer to detect references to the
				 * model instance
				 */
				if (parent == null || parent instanceof Resource) {
					// search for an containing model or diagram instance
					for (PatchSet patchSet : modelReview.getPatchSets()) {
						ModelInstance modelInstance = findContainingModelInstance(
								patchSet.getNewInvolvedDiagrams(), eObject);
						if (modelInstance != null)
							return modelInstance;
						modelInstance = findContainingModelInstance(
								patchSet.getOldInvolvedDiagrams(), eObject);
						if (modelInstance != null)
							return modelInstance;
						modelInstance = findContainingModelInstance(
								patchSet.getNewInvolvedModels(), eObject);
						if (modelInstance != null)
							return modelInstance;
						modelInstance = findContainingModelInstance(
								patchSet.getOldInvolvedModels(), eObject);
						if (modelInstance != null)
							return modelInstance;
					}
				}
				return parent;
			}
			return null;
		}

		private ModelInstance findContainingModelInstance(
				Collection<? extends ModelInstance> modelInstances,
				EObject object) {
			for (ModelInstance modelInstance : modelInstances) {
				if (modelInstance.getObjects().contains(object)) {
					return modelInstance;
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
				return new Object[] { new InvolvedModelsTreeItem(patchSet),
						new InvolvedDiagramsTreeItem(patchSet),
						new PatchSetTreeItem(patchSet) };
			}
			if (parentElement instanceof TreeItemContainer) {
				return ((TreeItemContainer) parentElement).getChildren();
			}
			if (parentElement instanceof ModelInstance) {
				return ((ModelInstance) parentElement).getObjects().toArray();
			}
			if (parentElement instanceof PatchSet) {
				return new Object[0];
			}
			if (parentElement instanceof EObject) {
				return adapterFactoryContentProvider.getChildren(parentElement);
			}
			return new Object[0];
		}
	}

	private interface TreeItemContainer {
		public boolean hasChildren();

		public Object[] getChildren();

		public String getText();
	}

	private class PatchSetTreeItem implements TreeItemContainer {

		private PatchSet patchSet;

		public PatchSetTreeItem(PatchSet patchSet) {
			super();
			this.patchSet = patchSet;
		}

		@Override
		public boolean hasChildren() {
			return !patchSet.getNewInvolvedModels().isEmpty();
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

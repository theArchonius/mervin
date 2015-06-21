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
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
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
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import at.bitandart.zoubek.mervin.model.modelreview.ModelInstance;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.Patch;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

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

		labelColumn = new TreeViewerColumn(reviewTreeViewer,
				SWT.NONE);
		labelColumn.getColumn().setResizable(true);
		labelColumn.getColumn().setMoveable(true);
		labelColumn.getColumn().setText("Element");
		labelColumn.getColumn().setWidth(200);
		labelColumn
				.setLabelProvider(new ModelReviewExplorerMainColumnLabelProvider());
		labelColumn.getColumn().addSelectionListener(
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
				Display.getCurrent().getSystemColor(SWT.COLOR_WHITE), Display
						.getCurrent().getSystemColor(SWT.COLOR_BLACK)));
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
						Display.getCurrent().getSystemColor(SWT.COLOR_WHITE),
						Display.getCurrent().getSystemColor(SWT.COLOR_BLACK)));

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

	private abstract class NumericColoredColumnLabelProvider extends
			ColumnLabelProvider {

		private Map<Object, Color> colors = new HashMap<>();

		private Color fgColor1;
		private Color fgColor2;
		private float fgColor1Brightness;
		private float fgColor2Brightness;

		public NumericColoredColumnLabelProvider(Color fgColor1, Color fgColor2) {
			this.fgColor1 = fgColor1;
			this.fgColor2 = fgColor2;
			fgColor1Brightness = getBrightness(this.fgColor1.getRGB());
			fgColor2Brightness = getBrightness(this.fgColor2.getRGB());
		}

		@Override
		public Color getBackground(Object element) {
			if (hasValue(element)) {

				RGB rgb = computeRGB(getValue(element), getMinValue(),
						getMaxValue());
				Color color = new Color(Display.getCurrent(), rgb);

				if (colors.containsKey(element)) {
					colors.get(element).dispose();
				}
				colors.put(element, color);

				return color;
			}
			return null;
		}

		private RGB computeRGB(float value, float minValue, float maxValue) {
			double relVal = 1.0 - ((1.0 / (maxValue - minValue)) * value);
			RGB rgb = new RGB(205.0f, 0.59f, (float) (0.32 + relVal
					* (1.0 - 0.32)));
			return rgb;
		}

		private float getBrightness(RGB rgb) {
			return ((rgb.red * 299) + (rgb.green * 587) + (rgb.blue * 114)) / 1000.0f;
		}

		private float getColorDifference(RGB rgb, RGB rgb2) {
			return Math.abs(rgb.red - rgb2.red)
					+ Math.abs(rgb.green - rgb2.green)
					+ Math.abs(rgb.blue - rgb2.blue);
		}

		@Override
		public Color getForeground(Object element) {
			if (hasValue(element)) {

				RGB rgb = computeRGB(getValue(element), getMinValue(),
						getMaxValue());
				float brightness = getBrightness(rgb);
				// Difference metric based on
				// http://www.w3.org/TR/AERT#color-contrast
				float fgColor1Diff = Math.abs(brightness - fgColor1Brightness)
						+ getColorDifference(fgColor1.getRGB(), rgb);
				float fgColor2Diff = Math.abs(brightness - fgColor2Brightness)
						+ getColorDifference(fgColor2.getRGB(), rgb);

				if (fgColor2Diff > fgColor1Diff) {
					return fgColor2;
				} else {
					return fgColor1;
				}
			}
			return null;
		}

		@Override
		public void dispose() {
			for (Color color : colors.values()) {
				color.dispose();
			}
			super.dispose();
		}

		@Override
		public String getText(Object element) {
			if (hasValue(element)) {
				return "" + getValue(element);
			}
			return null;
		}

		public abstract float getMaxValue();

		public abstract float getMinValue();

		public abstract float getValue(Object element);

		public abstract boolean hasValue(Object element);

	}

	private class ChangeCountColumnLabelProvider extends
			NumericColoredColumnLabelProvider {

		Map<Object, Double> cachedValues = new HashMap<Object, Double>();

		public ChangeCountColumnLabelProvider(Color fgBright, Color fgDark) {
			super(fgBright, fgDark);
		}

		@Override
		public float getMaxValue() {
			// Dummy value for testing
			// TODO replace with actual value
			return 100;
		}

		@Override
		public float getMinValue() {
			return 0;
		}

		@Override
		public float getValue(Object element) {
			// Dummy value for testing
			// TODO replace with actual value
			if (!cachedValues.containsKey(element)) {
				cachedValues.put(element,
						new Double(Math.round(Math.random() * 100)));
			}
			return cachedValues.get(element).floatValue();
		}

		@Override
		public boolean hasValue(Object element) {
			// Dummy value for testing
			// TODO only model objects and patch sets have values
			return true;
		}

	}

	private class ReferencedChangeCountColumnLabelProvider extends
			NumericColoredColumnLabelProvider {

		Map<Object, Double> cachedValues = new HashMap<Object, Double>();

		public ReferencedChangeCountColumnLabelProvider(Color fgBright,
				Color fgDark) {
			super(fgBright, fgDark);
		}

		@Override
		public float getMaxValue() {
			// Dummy value for testing
			// TODO replace with actual value
			return 100;
		}

		@Override
		public float getMinValue() {
			return 0;
		}

		@Override
		public float getValue(Object element) {
			// Dummy value for testing
			if (!cachedValues.containsKey(element)) {
				cachedValues.put(element,
						new Double(Math.round(Math.random() * 100)));
			}
			return cachedValues.get(element).floatValue();
		}

		@Override
		public boolean hasValue(Object element) {
			// Dummy value for testing
			// TODO only model objects and patch sets have values
			return true;
		}

	}

	private class ModelReviewContentProvider implements ITreeContentProvider {

		private AdapterFactoryContentProvider adapterFactoryContentProvider;

		public ModelReviewContentProvider() {
			adapterFactoryContentProvider = new AdapterFactoryContentProvider(
					new ComposedAdapterFactory(
							ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			adapterFactoryContentProvider.inputChanged(viewer, oldInput,
					newInput);
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
			if (element instanceof ModelInstance) {
				return null;
			}
			if (element instanceof EObject) {
				return adapterFactoryContentProvider.getParent(element);
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

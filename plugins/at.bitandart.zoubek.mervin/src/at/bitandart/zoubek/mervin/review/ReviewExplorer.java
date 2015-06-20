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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

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

	// Data

	private boolean viewInitialized = false;

	@Inject
	public ReviewExplorer() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new GridLayout());

		reviewTreeViewer = new TreeViewer(mainPanel, SWT.SINGLE);
		reviewTreeViewer.setLabelProvider(new ModelReviewTreeLabelProvider());
		reviewTreeViewer.setContentProvider(new ModelReviewContentProvider());
		Tree reviewTree = reviewTreeViewer.getTree();
		reviewTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		viewInitialized = true;
	}

	@Override
	protected void updateValues() {
		if (viewInitialized) {

			ModelReview currentModelReview = getCurrentModelReview();

			reviewTreeViewer.setInput(currentModelReview);
		}
	}

	private class ModelReviewTreeLabelProvider extends LabelProvider {

		private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

		public ModelReviewTreeLabelProvider() {
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
			return super.getText(element);
		}
		
		@Override
		public Image getImage(Object element) {
			if(element instanceof EObject){
				return adapterFactoryLabelProvider.getImage(element);
			}
			return super.getImage(element);
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
			adapterFactoryContentProvider.inputChanged(viewer, oldInput, newInput);
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
			if(element instanceof ModelReview){
				return null;
			}
			if(element instanceof PatchSet){
				return ((PatchSet) element).getReview();
			}
			if(element instanceof ModelInstance){
				return null;
			}
			if(element instanceof EObject){
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
			if(parentElement instanceof PatchSet){
				return new Object[0];
			}
			if(parentElement instanceof EObject){
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

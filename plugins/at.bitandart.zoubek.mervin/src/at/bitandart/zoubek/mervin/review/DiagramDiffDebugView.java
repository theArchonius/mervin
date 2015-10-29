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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class DiagramDiffDebugView extends ModelReviewEditorTrackingView {

	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.debug.diff.diagram";

	// Data

	/**
	 * indicates if all SWT controls and viewers have been correctly set up
	 */
	private boolean viewInitialized = false;

	// SWT controls
	private ScrolledForm mainForm;
	private Composite mainPanel;

	private TreeViewer modelTreeViewer;
	private TreeViewer editPartTreeViewer;
	private TreeViewer figureTreeViewer;

	@Inject
	public DiagramDiffDebugView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		mainForm = toolkit.createScrolledForm(parent);

		mainPanel = mainForm.getBody();
		mainPanel.setLayout(new GridLayout());

		CTabFolder tabFolder = new CTabFolder(mainPanel, SWT.BOTTOM);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// toolkit.adapt(tabFolder, true, true);

		createViewModelTab(toolkit, tabFolder);
		createEditPartTab(toolkit, tabFolder);
		createFigureTab(toolkit, tabFolder);

		viewInitialized = true;
		updateValues();
	}

	private void createViewModelTab(FormToolkit toolkit, CTabFolder tabFolder) {
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NULL);
		tabItem.setText("View Model");
		Composite body = toolkit.createComposite(tabFolder);
		body.setLayout(new GridLayout());
		tabItem.setControl(body);
		tabFolder.setSelection(tabItem);

		modelTreeViewer = new TreeViewer(body, SWT.VIRTUAL);
		modelTreeViewer.setUseHashlookup(true);
		Tree modelTree = modelTreeViewer.getTree();
		modelTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		modelTreeViewer.setContentProvider(new EmfAndReflectionContentProvider(modelTreeViewer));
		modelTreeViewer.setLabelProvider(new EmfAndReflectionCellLabelProvider());

		Menu contextMenu = new Menu(modelTree);
		MenuItem refreshItem = new MenuItem(contextMenu, SWT.NULL);
		refreshItem.setText("refresh");
		refreshItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				modelTreeViewer.refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		modelTree.setMenu(contextMenu);
	}

	private void createEditPartTab(FormToolkit toolkit, CTabFolder tabFolder) {
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NULL);
		tabItem.setText("Edit Parts");
		Composite body = toolkit.createComposite(tabFolder);
		body.setLayout(new GridLayout());
		tabItem.setControl(body);

		editPartTreeViewer = new TreeViewer(body, SWT.VIRTUAL);
		editPartTreeViewer.setUseHashlookup(true);
		Tree editPartTree = editPartTreeViewer.getTree();
		editPartTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		editPartTreeViewer.setContentProvider(new EmfAndReflectionContentProvider(editPartTreeViewer));
		editPartTreeViewer.setLabelProvider(new EmfAndReflectionCellLabelProvider());

		Menu contextMenu = new Menu(editPartTree);
		MenuItem refreshItem = new MenuItem(contextMenu, SWT.NULL);
		refreshItem.setText("refresh");
		refreshItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				editPartTreeViewer.refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		editPartTree.setMenu(contextMenu);

	}

	private void createFigureTab(FormToolkit toolkit, CTabFolder tabFolder) {
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NULL);
		tabItem.setText("Figures");
		Composite body = toolkit.createComposite(tabFolder);
		body.setLayout(new GridLayout());
		tabItem.setControl(body);

		figureTreeViewer = new TreeViewer(body, SWT.VIRTUAL);
		figureTreeViewer.setUseHashlookup(true);
		Tree figureTree = figureTreeViewer.getTree();
		figureTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		figureTreeViewer.setContentProvider(new EmfAndReflectionContentProvider(figureTreeViewer));
		figureTreeViewer.setLabelProvider(new EmfAndReflectionCellLabelProvider());

		Menu contextMenu = new Menu(figureTree);
		MenuItem refreshItem = new MenuItem(contextMenu, SWT.NULL);
		refreshItem.setText("refresh");
		refreshItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				figureTreeViewer.refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		figureTree.setMenu(contextMenu);
	}

	@Override
	protected void updateValues() {
		// we cannot update the controls if they are not initialized yet
		if (viewInitialized) {

			MPart activeModelReviewPart = getActiveModelReviewPart();
			if (activeModelReviewPart != null) {

				Object viewObject = activeModelReviewPart.getObject();
				if (viewObject instanceof IAdaptable) {

					Diagram diagram = ((IAdaptable) viewObject).getAdapter(Diagram.class);
					modelTreeViewer.setInput(new Object[] { diagram });

					RootEditPart rootEditPart = ((IAdaptable) viewObject).getAdapter(RootEditPart.class);
					editPartTreeViewer.setInput(new Object[] { rootEditPart });

					IFigure rootFigure = ((IAdaptable) viewObject).getAdapter(IFigure.class);
					figureTreeViewer.setInput(new Object[] { rootFigure });

				} else {

					modelTreeViewer.setInput(null);
					editPartTreeViewer.setInput(null);
					figureTreeViewer.setInput(null);

				}

			} else {

				modelTreeViewer.setInput(null);
				editPartTreeViewer.setInput(null);
				figureTreeViewer.setInput(null);

			}

		}
	}

	/**
	 * An {@link ILazyTreeContentProvider} that that lists EMF structural
	 * features of {@link EObject}s and non static fields of for all non-
	 * {@link EObject} instances using reflection.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class EmfAndReflectionContentProvider implements ILazyTreeContentProvider {

		TreeViewer treeViewer;

		public EmfAndReflectionContentProvider(TreeViewer treeViewer) {
			this.treeViewer = treeViewer;
		}

		@Override
		public void updateElement(Object parent, int index) {
			Object element = parent;

			if (element instanceof ObjectWithParent) {
				element = ((ObjectWithParent) element).getObject();
			}

			if (element instanceof List<?>) {

				List<?> list = (List<?>) element;
				ObjectWithListParent wrappedChild = new ObjectWithListParent(parent, list.get(index), index);
				treeViewer.replace(parent, index, wrappedChild);
				treeViewer.setHasChildren(wrappedChild, true);

			} else if (element instanceof Object[]) {

				Object[] array = (Object[]) element;
				ObjectWithListParent wrappedChild = new ObjectWithListParent(parent, array[index], index);
				treeViewer.replace(parent, index, wrappedChild);
				treeViewer.setHasChildren(wrappedChild, true);

			} else if (element instanceof EObject) {

				EObject eObject = (EObject) element;
				EClass eClass = eObject.eClass();
				EStructuralFeature feature = eClass.getEAllStructuralFeatures().get(index);

				Object child = eObject.eGet(feature);
				ObjectWithEObjectParent wrappedChild = new ObjectWithEObjectParent(parent, child, feature);
				treeViewer.replace(parent, index, wrappedChild);
				treeViewer.setHasChildren(wrappedChild, true);

			} else if (element != null) {

				Class<? extends Object> clazz = element.getClass();
				// int fieldIndex = getFieldIndex(index, clazz);
				List<Field> fields = getAllDeclaredNonStaticFields(clazz);
				Field field = fields.get(index);
				Object child = null;
				try {
					field.setAccessible(true);
					child = field.get(element);
				} catch (SecurityException | IllegalAccessException e) {
					child = "<Inaccessible>";
				}
				ObjectWithJavaParent wrappedChild = new ObjectWithJavaParent(parent, child, field);
				treeViewer.replace(parent, index, wrappedChild);
				treeViewer.setHasChildren(wrappedChild, true);

			}
		}

		/**
		 * list all non-static fields of the given class and all of its
		 * superclasses.
		 * 
		 * @param clazz
		 * @return a list of all non static fields
		 */
		private List<Field> getAllDeclaredNonStaticFields(Class<?> clazz) {
			List<Field> fields = new LinkedList<Field>();
			for (Field field : clazz.getDeclaredFields()) {
				if (!Modifier.isStatic(field.getModifiers())) {
					fields.add(field);
				}
			}
			Class<?> superclass = clazz.getSuperclass();
			if (superclass != null) {
				fields.addAll(getAllDeclaredNonStaticFields(superclass));
			}
			return fields;
		}

		/**
		 * 
		 * @param elementIndex
		 * @param clazz
		 * @return the corresponding field index in the list of all declared
		 *         fields for the given non-static field index
		 */
		private int getFieldIndex(int elementIndex, Class<? extends Object> clazz) {
			int fieldIndex = 0;
			int indicesLeft = elementIndex;
			Field[] fields = clazz.getDeclaredFields();
			for (; fieldIndex < fields.length && indicesLeft >= 0; fieldIndex++) {
				if (!Modifier.isStatic(fields[fieldIndex].getModifiers())) {
					indicesLeft--;
					if (indicesLeft <= 0) {
						break;
					}
				}
			}
			return fieldIndex;
		}

		@Override
		public void updateChildCount(Object parent, int currentChildCount) {

			Object element = parent;

			if (element instanceof ObjectWithParent) {
				element = ((ObjectWithParent) element).getObject();
			}

			if (element instanceof EObject) {

				EObject eObject = (EObject) element;
				EClass eClass = eObject.eClass();
				treeViewer.setChildCount(parent, eClass.getEAllStructuralFeatures().size());

			} else if (element instanceof Object[]) {

				Object[] array = (Object[]) element;
				treeViewer.setChildCount(parent, array.length);

			} else if (element instanceof Collection<?>) {

				treeViewer.setChildCount(parent, ((Collection<?>) element).size());

			} else {

				Class<? extends Object> clazz = element.getClass();
				treeViewer.setChildCount(parent, getAllDeclaredNonStaticFields(clazz).size());

			}

		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof ObjectWithParent) {
				return ((ObjectWithParent) element).getParent();
			}
			return null;
		}

		@Override
		public void dispose() {
			// intentionally left empty
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// intentionally left empty
		}

	}

	/**
	 * A {@link CellLabelProvider} implementation for
	 * {@link EmfAndReflectionContentProvider} elements.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class EmfAndReflectionCellLabelProvider extends CellLabelProvider {

		@Override
		public void update(ViewerCell cell) {

			Object element = cell.getElement();
			Object realElement = element;
			String fieldName = null;
			String realElementClass = "";
			String shortenedToString = "null";
			if (element instanceof ObjectWithJavaParent) {

				ObjectWithJavaParent wrappedElement = (ObjectWithJavaParent) element;
				Field field = wrappedElement.getField();
				realElement = wrappedElement.getObject();
				fieldName = field.getName();

			} else if (element instanceof ObjectWithListParent) {

				ObjectWithListParent wrappedElement = (ObjectWithListParent) element;
				int index = wrappedElement.getIndex();
				realElement = wrappedElement.getObject();
				fieldName = "[" + index + "]";

			} else if (element instanceof ObjectWithEObjectParent) {

				ObjectWithEObjectParent wrappedElement = (ObjectWithEObjectParent) element;
				EStructuralFeature feature = wrappedElement.getEStructuralFeature();
				realElement = wrappedElement.getObject();
				fieldName = feature.getName();
			}

			if (realElement != null) {
				realElementClass = realElement.getClass().getCanonicalName();
				shortenedToString = createStringRepresentation((ObjectWithParent) element);
				if (shortenedToString == null) {
					shortenedToString = realElement.toString();
					if (shortenedToString.length() > 100) {
						shortenedToString = shortenedToString.substring(0, 99) + "...";
					}
				}
			}

			if (fieldName != null) {
				cell.setText(fieldName + " : \"" + shortenedToString + "\" (" + realElementClass + ")");

			} else {
				cell.setText("\"" + shortenedToString + "\" (" + realElementClass + ")");
			}

		}

		private String createStringRepresentation(ObjectWithParent element) {

			if (element.getParent() instanceof ObjectWithParent
					&& ((ObjectWithParent) element.getParent()).getObject() instanceof IFigure) {
				IFigure figure = (IFigure) ((ObjectWithParent) element.getParent()).getObject();
				if (element.getObject() instanceof Rectangle) {
					Rectangle rectangle = (Rectangle) element.getObject();
					Point location = rectangle.getLocation();
					figure.translateToAbsolute(location);

					return rectangle.x + " / " + rectangle.y + " (" + location.x + " / " + location.y + ") w: "
							+ rectangle.width + " h: " + rectangle.height;
				}
			}

			return null;
		}
	}

	/**
	 * Represents an Object and its referencing parent Object.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ObjectWithParent {

		private Object parent;
		private Object object;

		public ObjectWithParent(Object parent, Object object) {
			super();
			this.parent = parent;
			this.object = object;
		}

		public Object getObject() {
			return object;
		}

		public Object getParent() {
			return parent;
		}
	}

	/**
	 * Represents an {@link Object} inside a List-structured parent like Arrays
	 * and {@link List}s.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ObjectWithListParent extends ObjectWithParent {

		private int index;

		public ObjectWithListParent(Object parent, Object object, int index) {
			super(parent, object);
			this.index = index;
		}

		public int getIndex() {
			return index;
		}
	}

	/**
	 * Represents an {@link Object} and its referencing parent java
	 * {@link Object} with information about the referencing field.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ObjectWithJavaParent extends ObjectWithParent {

		private Field field;

		public ObjectWithJavaParent(Object parent, Object object, Field field) {
			super(parent, object);
			this.field = field;
		}

		public Field getField() {
			return field;
		}
	}

	/**
	 * Represents an {@link Object} referenced by an {@link EObject}s structural
	 * feature.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class ObjectWithEObjectParent extends ObjectWithParent {

		private EStructuralFeature eStructuralFeature;

		public ObjectWithEObjectParent(Object parent, Object object, EStructuralFeature eStructuralFeature) {
			super(parent, object);
			this.eStructuralFeature = eStructuralFeature;
		}

		/**
		 * @return the eStructuralFeature
		 */
		public EStructuralFeature getEStructuralFeature() {
			return eStructuralFeature;
		}

	}
}
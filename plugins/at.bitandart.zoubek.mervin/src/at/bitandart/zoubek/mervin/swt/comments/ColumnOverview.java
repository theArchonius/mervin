/*******************************************************************************
 * Copyright (c) 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.swt.comments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn;

/**
 * A Control that shows an overview of a list of {@link ICommentColumn}s. This
 * class manages its child controls on its own, so no child should be added
 * manually to this control. The same holds true for its layout.
 * 
 * @author Florian Zoubek
 *
 */
public class ColumnOverview extends Composite {

	// controls
	private CLabel headerLabel;

	// data
	private List<ICommentColumn> overviewColumns = new ArrayList<>();

	// Utilities
	private FormToolkit toolkit;

	private Font titleFont;
	private Color titleForeground;
	private Color titleBackground;

	public ColumnOverview(FormToolkit toolkit, Composite parent, int style) {
		super(parent, style);
		this.toolkit = toolkit;
		toolkit.adapt(this);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);

		titleFont = FontDescriptor.createFrom(getFont()).setStyle(SWT.BOLD).increaseHeight(1)
				.createFont(Display.getDefault());

		// the title colors are system colors, so do not dispose them later
		titleForeground = getDisplay().getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
		titleBackground = getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
		headerLabel = new CLabel(this, SWT.CENTER);
		toolkit.adapt(headerLabel);

		headerLabel.setText("Overview");
		headerLabel.setBackground(titleBackground);
		headerLabel.setForeground(titleForeground);
		headerLabel.setFont(titleFont);
		headerLabel.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).span(1, 0).create());

		addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				titleFont.dispose();
			}
		});
	}

	/**
	 * sets the columns shown in this overview. The given list will be copied,
	 * so any changes to the list will not be applied to this control until this
	 * method is called again with the updated list.
	 * 
	 * @param overviewColumns
	 *            the columns to show in this control
	 */
	public void setOverviewColumns(List<ICommentColumn> overviewColumns) {
		this.overviewColumns.clear();
		this.overviewColumns.addAll(overviewColumns);
		updateControls();
	}

	/**
	 * @return a unmodifiable list of the {@link ICommentColumn}s shown by this
	 *         control
	 */
	public List<ICommentColumn> getOverviewColumns() {
		return Collections.unmodifiableList(overviewColumns);
	}

	/**
	 * updates the controls by re-using existing and hiding unnecessary child
	 * controls.
	 */
	private void updateControls() {

		int numColumns = overviewColumns.size();
		getGridLayout().numColumns = numColumns;
		headerLabel.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).span(numColumns, 0).create());

		int childControlOffset = 1;
		/*
		 * the list of children will be manipulated - so use a copy to determine
		 * controls to recycle
		 */
		Control[] children = getChildren().clone();
		childControlOffset = addColumnTitles(children, childControlOffset);
		childControlOffset = addColumnCommentCounts(children, childControlOffset);
		hideRemainingControls(children, childControlOffset);
	}

	/**
	 * hides the controls of the given control list beginning with the control
	 * at the given offset (inclusive).
	 * 
	 * @param controls
	 *            the controls to hide
	 * @param controlOffset
	 *            the offset of the first control to hide. Preceding controls
	 *            will be unaffected.
	 */
	private void hideRemainingControls(Control[] controls, int controlOffset) {
		for (int i = controlOffset; i < controls.length; i++) {
			controls[i].setLayoutData(GridDataFactory.fillDefaults().exclude(true).create());
		}
	}

	/**
	 * adds or updates the column title children by reusing existing children if
	 * possible.
	 * 
	 * @param children
	 *            a list of child controls to re-use.
	 * @param childControlOffset
	 *            the offset of the first control to re-use.
	 * @return the successor of last re-used control.
	 */
	private int addColumnTitles(Control[] children, int childControlOffset) {

		for (ICommentColumn column : overviewColumns) {

			CLabel columnHeaderLabel = null;
			/* try to reuse the child labels if possible */

			childControlOffset = nextChildCLabelIndexAndHideOthers(children, childControlOffset);

			if (childControlOffset < children.length) {
				columnHeaderLabel = (CLabel) children[childControlOffset];
				childControlOffset++;
			}

			if (columnHeaderLabel == null) {
				columnHeaderLabel = new CLabel(this, SWT.CENTER);
				toolkit.adapt(columnHeaderLabel);
			}

			columnHeaderLabel.setText(column.getTitle());
			columnHeaderLabel.setBackground(titleBackground);
			columnHeaderLabel.setForeground(titleForeground);
			columnHeaderLabel.setFont(titleFont);
			columnHeaderLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		}

		return childControlOffset;
	}

	/**
	 * adds or updates the column count children by reusing existing children if
	 * possible.
	 * 
	 * @param children
	 *            the list of child controls to re-use.
	 * @param childControlOffset
	 *            the offset of the first control to re-use.
	 * @return the successor of last re-used control.
	 */
	private int addColumnCommentCounts(Control[] children, int childControlOffset) {

		for (ICommentColumn column : overviewColumns) {

			CLabel countLabel = null;
			/* try to reuse the child labels if possible */

			childControlOffset = nextChildCLabelIndexAndHideOthers(children, childControlOffset);

			if (childControlOffset < children.length) {
				countLabel = (CLabel) children[childControlOffset];
				childControlOffset++;
			}

			if (countLabel == null) {
				countLabel = new CLabel(this, SWT.CENTER);
			}
			countLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			countLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
			countLabel.setFont(getFont());
			toolkit.adapt(countLabel);
			countLabel.setText(column.getCommentCount() + " Comment(s)");
			countLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		}

		return childControlOffset;
	}

	/**
	 * walks along the given child controls and stops at the first
	 * {@link CLabel}, starting at the given offset. Any passed child non-
	 * {@link CLabel} controls will be hidden.
	 * 
	 * @param children
	 *            the list of child controls to re-use.
	 * @param childControlOffset
	 *            the offset of the first control to start.
	 * @return the index of the next {@link CLabel} in the given list.
	 */
	private int nextChildCLabelIndexAndHideOthers(Control[] children, int childControlOffset) {

		while (childControlOffset < children.length && !(children[childControlOffset] instanceof CLabel)) {
			children[childControlOffset].setLayoutData(GridDataFactory.fillDefaults().exclude(true).create());
			childControlOffset++;
		}
		return childControlOffset;
	}

	/**
	 * convenience method to retrieve the layout.
	 * 
	 * @return the controls {@link GridLayout}.
	 */
	private GridLayout getGridLayout() {
		return (GridLayout) getLayout();
	}

	@Override
	public void setLayout(Layout layout) {
		/*
		 * disallow changing the layout once it has been set
		 */
		if (getLayout() == null) {
			super.setLayout(layout);
		}
	}
}

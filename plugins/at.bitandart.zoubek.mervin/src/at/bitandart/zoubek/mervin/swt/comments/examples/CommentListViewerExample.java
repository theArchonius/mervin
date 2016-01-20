/*******************************************************************************
 * Copyright (c) 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.swt.comments.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.bitandart.zoubek.mervin.swt.comments.CommentListViewer;

/**
 * Example that demonstrates the usage of the {@link CommentListViewer} class.
 * 
 * @author Florian Zoubek
 *
 */
public class CommentListViewerExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		FormToolkit toolkit = new FormToolkit(display);

		/*
		 * create a scrolled composite that allows wrapping (based on SWT
		 * Snippet 166)
		 * 
		 * It is *NOT* recommended to use the same technique with a
		 * ScrolledForm, as a call of ScrolledForm#reflow() may change the
		 * minimum size (depending on the style bits and layout manager of the
		 * body) and break the computation of the correct wrapped layout.
		 */
		final ScrolledComposite scrollComposite = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.H_SCROLL);
		toolkit.adapt(scrollComposite);
		final Composite body = toolkit.createComposite(scrollComposite);
		body.setLayout(new GridLayout());

		scrollComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scrollComposite.setContent(body);
		scrollComposite.setExpandVertical(true);
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle r = scrollComposite.getClientArea();
				scrollComposite.setMinSize(body.computeSize(r.width, SWT.DEFAULT));
			}
		});

		// ##################################
		// # create the comment list view
		// ##################################

		// TODO

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

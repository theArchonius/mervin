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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.bitandart.zoubek.mervin.swt.comments.CommentEditor;
import at.bitandart.zoubek.mervin.swt.comments.CommentEditor.CommentEditorListener;
import at.bitandart.zoubek.mervin.swt.comments.data.CommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.CommentLinkTarget;

/**
 * Example that demonstrates the usage of the {@link CommentEditor} control.
 * 
 * @author Florian Zoubek
 *
 */
public class CommentEditorExample {

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
		final Composite body = new Composite(scrollComposite, SWT.NONE);
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
		// # create the comment editor
		// ##################################

		final CommentEditor editor = new CommentEditor(body, SWT.NONE, toolkit);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		String text = "This is an example ";
		String linkText = "link";
		editor.setText(text + linkText);
		editor.addCommentLink(new ExampleCommentLink(text.length(), linkText.length(),
				new ExampleLinkTarget(new String[] { "predefined item" })));

		editor.addCommentEditorListener(new CommentEditorListener() {

			@Override
			public void doSave(CommentEditor commentEditor) {

				// retrieve links
				java.util.List<CommentLink> commentLinks = commentEditor.getCommentLinks();
				StringBuilder sb = new StringBuilder();
				for (CommentLink commentLink : commentLinks) {
					sb.append(commentLink.toString());
					sb.append("\n");
				}

				// retrieve raw comment text
				String commentText = commentEditor.getText();

				MessageDialog.openInformation(shell, "Save called",
						"Comment has been saved with the following text:\n\n" + commentText + "\n\n and "
								+ commentLinks.size() + " link(s):\n\n" + commentLinks.toString());
			}
		});

		// ##################################

		// create a list with items to select
		final List itemList = new List(body, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		GridData listGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		listGridData.heightHint = 100;
		itemList.setLayoutData(listGridData);
		for (int i = 0; i < 30; i++) {
			itemList.add("Item " + i);
		}
		itemList.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				/*
				 * items have been selected that can be a target of the link, so
				 * create a link target for then and update the editor
				 */
				String[] selection = itemList.getSelection();
				ExampleLinkTarget linkTarget = new ExampleLinkTarget(selection);
				editor.setCurrentLinkTarget(linkTarget);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// intentionally left empty
			}
		});

		Button deselectButton = new Button(body, SWT.PUSH);
		deselectButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		deselectButton.setText("deselect all");
		deselectButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				itemList.deselectAll();
				// nothing is selected -> also update the editor
				editor.setCurrentLinkTarget(null);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Intentionally left empty
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

/**
 * Example implementation of a link target. The current selected link target
 * must be set using the
 * {@link CommentEditor#setCurrentLinkTarget(CommentLinkTarget)}.
 * 
 * @author Florian Zoubek
 *
 */
class ExampleLinkTarget implements CommentLinkTarget {

	private String[] items;

	public ExampleLinkTarget(String[] items) {
		this.items = items;
	}

	@Override
	public String getDefaultText() {
		StringBuilder sb = new StringBuilder();
		for (String item : items) {
			sb.append(item);
			sb.append(" ,");
		}
		int length = sb.length();
		sb.delete(length - 2, length);
		return sb.toString();
	}

	@Override
	public String toString() {
		return getDefaultText();
	}

}

/**
 * Example implementation of a comment target used to demonstrate
 * {@link CommentEditor#addCommentLink(CommentLink)}.
 * 
 * @author Florian Zoubek
 *
 */
class ExampleCommentLink implements CommentLink {

	private int startIndex;
	private int length;
	private CommentLinkTarget commentLinkTarget;

	public ExampleCommentLink(int startIndex, int length, CommentLinkTarget commentLinkTarget) {
		this.startIndex = startIndex;
		this.length = length;
		this.commentLinkTarget = commentLinkTarget;
	}

	@Override
	public int getStartIndex() {
		return startIndex;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public CommentLinkTarget getCommentLinkTarget() {
		return commentLinkTarget;
	}

}

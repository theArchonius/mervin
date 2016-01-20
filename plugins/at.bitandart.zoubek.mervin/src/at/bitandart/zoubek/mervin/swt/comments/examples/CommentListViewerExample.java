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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.bitandart.zoubek.mervin.swt.comments.CommentList;
import at.bitandart.zoubek.mervin.swt.comments.CommentList.CommentLinkListener;
import at.bitandart.zoubek.mervin.swt.comments.CommentListViewer;
import at.bitandart.zoubek.mervin.swt.comments.data.Comment;
import at.bitandart.zoubek.mervin.swt.comments.data.CommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.data.CommentGroup;
import at.bitandart.zoubek.mervin.swt.comments.data.CommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.IComment;
import at.bitandart.zoubek.mervin.swt.comments.data.IComment.Alignment;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentGroup;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentProvider;

/**
 * Example that demonstrates the usage of the {@link CommentListViewer} class.
 * 
 * @author Florian Zoubek
 *
 */
public class CommentListViewerExample {

	private static Text eventOutput;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));
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
		body.setLayout(new GridLayout(1, false));

		scrollComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
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
		CommentListViewer commentListViewer = new CommentListViewer(body, toolkit);
		commentListViewer.setCommentProvider(new ExampleCommentProvider());

		CommentList commentListControl = commentListViewer.getCommentListControl();
		commentListControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3));

		commentListControl.addCommentLinkListener(new CommentLinkListener() {

			@Override
			public void commentLinkClicked(ICommentLink commentLink) {

				String text = eventOutput.getText();
				if (!text.isEmpty()) {
					text += "\n";
				}
				text += "Comment link click detected on comment link:\n" + commentLink.toString() + "\n";
				eventOutput.setText(text);

			}

			@Override
			public void commentLinkEnter(ICommentLink commentLink) {

				String text = eventOutput.getText();
				if (!text.isEmpty()) {
					text += "\n";
				}
				text += "Comment link enter detected on comment link:\n" + commentLink.toString() + "\n";
				eventOutput.setText(text);
			}

			@Override
			public void commentLinkExit(ICommentLink commentLink) {

				String text = eventOutput.getText();
				if (!text.isEmpty()) {
					text += "\n";
				}
				text += "Comment link exit detected on comment link:\n" + commentLink.toString() + "\n";
				eventOutput.setText(text);

			}
		});

		// TODO add example code that manages added comments

		commentListViewer.refresh();

		// ##################################

		// create a list with items to select
		final org.eclipse.swt.widgets.List itemList = new org.eclipse.swt.widgets.List(shell,
				SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		GridData listGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		listGridData.heightHint = 100;
		listGridData.minimumWidth = 300;
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
				// TODO set link target to comment list once it is
				// implemented
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// intentionally left empty
			}
		});

		Button deselectButton = new Button(shell, SWT.PUSH);
		deselectButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		deselectButton.setText("deselect all");
		deselectButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				itemList.deselectAll();
				// nothing is selected -> also update the editor
				// TODO set null link target to comment list once it is
				// implemented
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Intentionally left empty
			}
		});

		// event listener output

		eventOutput = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		eventOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		body.layout();
		scrollComposite.layout();
		shell.layout();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static class ExampleCommentProvider implements ICommentProvider {

		@Override
		public List<ICommentColumn> getCommentColumns(Object input) {

			List<ICommentColumn> commentColumn = new LinkedList<ICommentColumn>();
			commentColumn.add(new CommentColumn("Column 1"));
			commentColumn.add(new CommentColumn("Column 2"));
			return commentColumn;
		}

		@Override
		public List<ICommentGroup> getCommentGroups(Object input) {

			List<ICommentGroup> commentGroups = new LinkedList<ICommentGroup>();
			commentGroups.add(new CommentGroup("Group 1"));
			commentGroups.add(new CommentGroup("Group 2"));
			commentGroups.add(new CommentGroup("Group 3"));

			return commentGroups;
		}

		@Override
		public List<IComment> getComments(ICommentGroup group, ICommentColumn commentColumn) {

			List<IComment> comments = new LinkedList<IComment>();

			String prefix = group.getGroupTitle() + " - " + commentColumn.getTitle() + " -\n";

			List<ICommentLink> links = new LinkedList<ICommentLink>();
			links.add(new CommentLink(prefix.length(), 5, null));// Lorem
			links.add(new CommentLink(prefix.length() + 22, 4, null));// amet

			comments.add(new Comment("Someone", new GregorianCalendar(2015, 3, 2, 3, 46),
					prefix + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
					Alignment.LEFT, links));
			comments.add(new Comment("Someone other", new GregorianCalendar(2001, 12, 7, 3, 13),
					prefix + "At vero eos et accusam et justo duo dolores et ea rebum.", Alignment.RIGHT,
					new ArrayList<ICommentLink>(0)));
			comments.add(new Comment("Someone", new GregorianCalendar(2015, 6, 2, 5, 46),
					prefix + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
					Alignment.LEFT, new ArrayList<ICommentLink>(0)));
			comments.add(new Comment("Someone other", new GregorianCalendar(2015, 11, 2, 8, 16), prefix + "...",
					Alignment.RIGHT, new ArrayList<ICommentLink>(0)));
			comments.add(new Comment("Someone other", new GregorianCalendar(2015, 11, 2, 8, 16),
					prefix + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
					Alignment.RIGHT, new ArrayList<ICommentLink>(0)));

			return comments;
			// return new LinkedList<IComment>();
		}

	}
}

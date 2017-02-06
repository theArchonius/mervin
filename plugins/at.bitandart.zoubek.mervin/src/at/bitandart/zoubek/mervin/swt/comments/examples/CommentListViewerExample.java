/*******************************************************************************
 * Copyright (c) 2016, 2017 Florian Zoubek.
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.bitandart.zoubek.mervin.swt.comments.CommentList;
import at.bitandart.zoubek.mervin.swt.comments.CommentList.CommentLinkListener;
import at.bitandart.zoubek.mervin.swt.comments.CommentList.CommentModifyListener;
import at.bitandart.zoubek.mervin.swt.comments.CommentListViewer;
import at.bitandart.zoubek.mervin.swt.comments.data.Comment;
import at.bitandart.zoubek.mervin.swt.comments.data.CommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.data.CommentGroup;
import at.bitandart.zoubek.mervin.swt.comments.data.CommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.CommentLinkTarget;
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

		final ExampleInputData inputData = new ExampleInputData();

		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));
		FormToolkit toolkit = new FormToolkit(display);

		// ##################################
		// # create the comment list viewer
		// ##################################
		final CommentListViewer commentListViewer = new CommentListViewer(shell, toolkit, SWT.V_SCROLL | SWT.H_SCROLL);
		commentListViewer.setCommentProvider(new ExampleCommentProvider());

		final CommentList commentListControl = commentListViewer.getCommentListControl();
		commentListControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));

		// link events
		commentListControl.addCommentLinkListener(new CommentLinkListener() {

			@Override
			public void commentLinkClicked(ICommentLink commentLink) {

				String text = "";
				if (!eventOutput.getText().isEmpty()) {
					text += "\n";
				}
				text += "Comment link click detected on comment link:\n" + commentLink.toString() + "\n";
				eventOutput.append(text);

			}

			@Override
			public void commentLinkEnter(ICommentLink commentLink) {

				String text = "";
				if (!eventOutput.getText().isEmpty()) {
					text += "\n";
				}
				text += "Comment link enter detected on comment link:\n" + commentLink.toString() + "\n";
				eventOutput.append(text);
			}

			@Override
			public void commentLinkExit(ICommentLink commentLink) {

				String text = "";
				if (!eventOutput.getText().isEmpty()) {
					text += "\n";
				}
				text += "Comment link exit detected on comment link:\n" + commentLink.toString() + "\n";
				eventOutput.append(text);

			}
		});

		// modify events

		commentListControl.addCommentModifyListener(new CommentModifyListener() {

			@Override
			public void commentAdded(String text, List<ICommentLink> commentLinks, ICommentColumn commentColumn,
					IComment answerdComment) {
				String group = inputData.groups.get(0);
				String column = commentColumn.getTitle();
				if (answerdComment != null) {
					Cell cell = findCell(((ExampleComment) answerdComment).originalEntry);
					group = cell.group;
				}

				String markupText = createMarkupText(text, commentLinks);

				ExampleCommentEntry newEntry = new ExampleCommentEntry(inputData.user, GregorianCalendar.getInstance(),
						markupText);

				addEntry(newEntry, group, column);

				commentListViewer.refresh();
			}

			private String createMarkupText(String text, List<ICommentLink> commentLinks) {

				StringBuilder markupText = new StringBuilder(text);
				int offset = 0;
				for (ICommentLink link : commentLinks) {
					int insertionIndex = link.getStartIndex() + link.getLength() + offset;
					String target = "[" + link.getCommentLinkTarget().getDefaultText().replace(' ', '_') + "]";
					markupText.insert(insertionIndex, target);
					offset += target.length();
				}

				return markupText.toString();
			}

			private void addEntry(ExampleCommentEntry newEntry, String group, String column) {
				Cell cell = new Cell(column, group);
				List<ExampleCommentEntry> list = inputData.comments.get(cell);
				if (list == null) {
					list = new LinkedList<CommentListViewerExample.ExampleCommentEntry>();
					inputData.comments.put(cell, list);
				}
				list.add(newEntry);

			}

			private Cell findCell(ExampleCommentEntry entry) {
				for (Cell cell : inputData.comments.keySet()) {
					List<ExampleCommentEntry> list = inputData.comments.get(cell);
					if (list != null && list.contains(entry)) {
						return cell;
					}
				}
				return null;
			}
		});

		commentListViewer.setInput(inputData);

		commentListViewer.refresh();

		// ##################################

		// create a list with items to select
		final org.eclipse.swt.widgets.List itemList = new org.eclipse.swt.widgets.List(shell,
				SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		GridData listGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		listGridData.minimumHeight = 100;
		listGridData.heightHint = 100;
		listGridData.minimumWidth = 300;
		itemList.setLayoutData(listGridData);
		for (int i = 0; i < 30; i++) {
			itemList.add("Item-" + i);
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
				commentListControl.setCurrentLinkTarget(linkTarget);
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
				commentListControl.setCurrentLinkTarget(null);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Intentionally left empty
			}
		});

		// event listener output

		eventOutput = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		eventOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		shell.layout();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * Very simple example input data object for demonstration purposes that
	 * should be replaced with the real model used by the application. Do not
	 * use it in production.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private final static class ExampleInputData {

		/*
		 * Note: To keep the code short, no getter or setter methods have been
		 * created and all accessible fields are public.
		 */

		public List<String> groups = new ArrayList<String>();

		public Map<Cell, List<ExampleCommentEntry>> comments = new HashMap<Cell, List<ExampleCommentEntry>>();

		public String column1;
		public String column2;

		public String user;

		private int numPredefinedGroups = 5;
		private int numPredefinedComments = 35;

		public ExampleInputData() {

			column1 = "Column 1";
			column2 = "Column 2";

			groups.add("New Comments");

			for (int i = 0; i < numPredefinedGroups; i++) {
				if (i == 0) {
					groups.add("Group " + i + " with a long title to demostrate overflow behavior: "
							+ BlindtextUtil.getLoremIpsumSentences(0, 2));
				} else {
					groups.add("Group " + i);
				}
			}

			Random randomGenerator = new Random(1);

			user = BlindtextUtil.getName(nextAbsInt(randomGenerator, 31));

			Calendar time = GregorianCalendar.getInstance();
			time.add(Calendar.YEAR, -(1 + nextAbsInt(randomGenerator, 6)));

			for (int i = 0; i < numPredefinedComments; i++) {

				// create random time
				time.add(Calendar.DAY_OF_MONTH, nextAbsInt(randomGenerator, 2));
				time.add(Calendar.HOUR_OF_DAY, nextAbsInt(randomGenerator, 7));
				time.add(Calendar.MINUTE, nextAbsInt(randomGenerator, 16));
				time.add(Calendar.SECOND, nextAbsInt(randomGenerator, 46));

				// create markup text
				StringBuilder markupText = new StringBuilder(BlindtextUtil
						.getLoremIpsumSentences(nextAbsInt(randomGenerator), 1 + nextAbsInt(randomGenerator, 11)));

				int numLinks = nextAbsInt(randomGenerator, 6);

				for (int n = 0; n < numLinks; n++) {
					int index = -1;
					while (index < 0 || (index > 0 && markupText.charAt(index - 1) == ']')) {
						index = markupText.indexOf(" ", nextAbsInt(randomGenerator, markupText.length()));
					}
					if (index >= 0) {
						markupText.insert(index, "[Item-" + nextAbsInt(randomGenerator) + "]");
					}
				}

				ExampleCommentEntry entry = new ExampleCommentEntry(
						BlindtextUtil.getName(nextAbsInt(randomGenerator, 30)), (Calendar) time.clone(),
						markupText.toString());

				String column = null;
				String group = null;

				if (randomGenerator.nextBoolean()) {
					column = column1;
				} else {
					column = column2;
				}

				group = groups.get(1 + nextAbsInt(randomGenerator, groups.size() - 1));

				Cell cell = new Cell(column, group);

				List<ExampleCommentEntry> list = comments.get(cell);
				if (list == null) {
					list = new LinkedList<CommentListViewerExample.ExampleCommentEntry>();
					comments.put(cell, list);
				}
				list.add(entry);
			}

		}

		private static int nextAbsInt(Random randomGenerator) {
			return Math.abs(randomGenerator.nextInt());
		}

		private static int nextAbsInt(Random randomGenerator, int bound) {
			return Math.abs(randomGenerator.nextInt(bound));
		}

	}

	private final static class Cell {

		public String column;
		public String group;

		public Cell(String column, String group) {
			super();
			this.column = column;
			this.group = group;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((column == null) ? 0 : column.hashCode());
			result = prime * result + ((group == null) ? 0 : group.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			if (column == null) {
				if (other.column != null)
					return false;
			} else if (!column.equals(other.column))
				return false;
			if (group == null) {
				if (other.group != null)
					return false;
			} else if (!group.equals(other.group))
				return false;
			return true;
		}
	}

	private final static class ExampleCommentEntry {

		public String author;
		public Calendar creationDateTime;
		public String markupText;

		public ExampleCommentEntry(String author, Calendar creationDateTime, String markupText) {
			super();
			this.author = author;
			this.creationDateTime = creationDateTime;
			this.markupText = markupText;
		}

	}

	private static class ExampleCommentProvider implements ICommentProvider {

		@Override
		public List<ICommentColumn> getVisibleCommentColumns(Object input) {

			ExampleInputData data = (ExampleInputData) input;
			List<ICommentColumn> commentColumns = new LinkedList<ICommentColumn>();
			commentColumns.add(new CommentColumn(data.column1, countComments(data.column1, data)));
			commentColumns.add(new CommentColumn(data.column2, countComments(data.column1, data)));

			return commentColumns;
		}

		/**
		 * @param column
		 *            the column to count the comments for.
		 * @param data
		 *            the data containing the comment information.
		 * @return the comment count for the specified column
		 */
		private int countComments(String column, ExampleInputData data) {

			int count = 0;
			Set<Entry<Cell, List<ExampleCommentEntry>>> cellEntries = data.comments.entrySet();

			for (Entry<Cell, List<ExampleCommentEntry>> entry : cellEntries) {
				if (entry.getKey().column.equals(column)) {
					count += entry.getValue().size();
				}
			}
			return count;
		}

		@Override
		public List<ICommentColumn> getAllCommentColumns(Object input) {

			ExampleInputData data = (ExampleInputData) input;
			List<ICommentColumn> commentColumns = new LinkedList<ICommentColumn>();

			/*
			 * add some artificial columns before and after the real columns for
			 * demonstration
			 */

			commentColumns.add(new CommentColumn("Column 0", 34));

			/* real columns */

			commentColumns.add(new CommentColumn(data.column1, countComments(data.column1, data)));
			commentColumns.add(new CommentColumn(data.column2, countComments(data.column1, data)));

			/* again some artificial columns */

			commentColumns.add(new CommentColumn("Column 3", 42));
			commentColumns.add(new CommentColumn("Column 4", 7));

			return commentColumns;
		}

		@Override
		public List<ICommentGroup> getCommentGroups(Object input) {

			ExampleInputData data = (ExampleInputData) input;
			List<ICommentGroup> commentGroups = new LinkedList<ICommentGroup>();

			for (String group : data.groups) {
				commentGroups.add(new CommentGroup(group));
			}

			return commentGroups;
		}

		@Override
		public List<IComment> getComments(Object input, ICommentGroup group, ICommentColumn commentColumn) {

			List<IComment> comments = new LinkedList<IComment>();

			ExampleInputData data = (ExampleInputData) input;

			Cell cell = new Cell(commentColumn.getTitle(), group.getGroupTitle());

			List<ExampleCommentEntry> list = data.comments.get(cell);

			if (list != null) {
				for (ExampleCommentEntry entry : list) {
					comments.add(
							new ExampleComment(entry.author, entry.creationDateTime, filterMarkup(entry.markupText),
									(data.user.equals(entry.author) ? Alignment.LEFT : Alignment.RIGHT),
									parseCommentLinks(entry.markupText), entry));
				}
			}

			return comments;
		}

		private String filterMarkup(String markupText) {
			// return markupText;
			return markupText.replaceAll("\\[[^\\]]*\\]", "");
		}

		private List<ICommentLink> parseCommentLinks(String markupText) {

			ArrayList<ICommentLink> links = new ArrayList<ICommentLink>();

			int offset = 0;
			Pattern markupPattern = Pattern.compile("([^ \\[]+)\\[([^\\]]*)\\]");
			Matcher matcher = markupPattern.matcher(markupText);

			while (matcher.find()) {
				int startIndex = matcher.start(1) - offset;
				int endIndex = matcher.end(1) - offset;
				/*
				 * correct the offset to calculate the index in the filtered
				 * text
				 */
				offset += (matcher.end(2) + 1) - (matcher.start(2) - 1);
				ICommentLink commentLink = new CommentLink(startIndex, endIndex - startIndex,
						new CommentLinkTarget(matcher.group(2)));
				links.add(commentLink);
			}

			return links;
		}

	}

	private static class ExampleComment extends Comment {

		public ExampleCommentEntry originalEntry;

		public ExampleComment(String author, Calendar creationTime, String body, Alignment alignment,
				List<ICommentLink> commentLinks, ExampleCommentEntry entry) {
			super(author, creationTime, body, alignment, commentLinks);
			this.originalEntry = entry;
		}

	}

	private static class BlindtextUtil {

		private static String loremIpsum = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr,  sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,  sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,  sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
				+ "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.\n"
				+ "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n"
				+ "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.\n"
				+ "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis.\n"
				+ "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,  sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,  At accusam aliquyam diam diam dolore dolores duo eirmod eos erat, et nonumy sed tempor et et invidunt justo labore Stet clita ea et gubergren, kasd magna no rebum. sanctus sea sed takimata ut vero voluptua. est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,  sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat.\n"
				+ "Consetetur sadipscing elitr,  sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,  sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,  sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n";

		private static String getLoremIpsum() {
			return loremIpsum;
		}

		private static String getLoremIpsumSentences(int offset, int numSentences) {
			StringBuilder result = new StringBuilder();
			String[] sentences = loremIpsum.split("\\.");
			int n = offset % sentences.length;
			for (int i = 0; i < numSentences; i++) {
				if (n >= sentences.length) {
					n = 0;
				}
				result.append(sentences[n]);
				result.append(".");
				n++;
			}
			return result.toString();

		}

		private static String getLoremIpsumParagraphs(int offset, int numSentences) {
			StringBuilder result = new StringBuilder();
			String[] paragraphs = loremIpsum.split("\n");
			int n = offset % paragraphs.length;
			for (int i = 0; i < numSentences; i++) {
				if (n >= paragraphs.length) {
					n = 0;
				}
				result.append(paragraphs[n]);
				result.append("\n");
				n++;
			}
			return result.toString();

		}

		private static String[] names = new String[] { "Houston", "James", "Alex", "Francesco", "Wilbert", "Elisha",
				"Graham", "Keira", "Junita", "Nadine" };

		private static String getName(int index) {
			return names[index % names.length];
		}
	}
}

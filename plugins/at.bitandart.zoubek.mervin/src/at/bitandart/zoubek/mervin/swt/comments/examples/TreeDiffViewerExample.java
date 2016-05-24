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
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import at.bitandart.zoubek.mervin.draw2d.figures.ChangeType;
import at.bitandart.zoubek.mervin.draw2d.figures.DefaultChangeTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.draw2d.figures.IChangeTypeStyleAdvisor;
import at.bitandart.zoubek.mervin.swt.diff.tree.ITreeDiffItemProvider;
import at.bitandart.zoubek.mervin.swt.diff.tree.TreeDiff;
import at.bitandart.zoubek.mervin.swt.diff.tree.TreeDiffSide;
import at.bitandart.zoubek.mervin.swt.diff.tree.TreeDiffType;
import at.bitandart.zoubek.mervin.swt.diff.tree.TreeDiffViewer;

/**
 * Demonstrates the usage of {@link TreeDiffViewer}.
 * 
 * @author Florian Zoubek
 *
 */
public class TreeDiffViewerExample {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		/*
		 * create the example input data
		 */
		Object input = createInput();

		/* create the viewer */
		TreeDiffViewer treeDiffViewer = new TreeDiffViewer(shell);

		/*
		 * apply the item provider which extracts the diff data for the viewer
		 */
		treeDiffViewer.setTreeDiffItemProvider(new ITreeDiffItemProvider() {

			@Override
			public Object[] getRootItems(Object input) {
				if (input instanceof Collection<?>) {
					return ((Collection<?>) input).toArray();
				}
				return null;
			}

			@Override
			public String getDiffItemText(Object item, TreeDiffSide side) {
				if (item instanceof Match) {
					switch (side) {
					case LEFT:
						return ((Match) item).getLeftValue();
					case RIGHT:
						return ((Match) item).getRightValue();
					default:
						break;
					}
				}
				return "";
			}

			@Override
			public Image getDiffItemImage(Object item, TreeDiffSide side) {
				return null;
			}

			@Override
			public Object[] getChildren(Object item) {

				if (item instanceof Match) {
					return ((Match) item).getSubmatches().toArray();
				}

				return new Object[0];
			}

			@Override
			public TreeDiffType getTreeDiffType(Object item) {
				if (item instanceof Match) {
					switch (((Match) item).getDiffType()) {
					case ADD:
						return TreeDiffType.ADD;
					case DELETE:
						return TreeDiffType.DELETE;
					case MODIFY:
						return TreeDiffType.MODIFY;
					case EQUAL:
						return TreeDiffType.EQUAL;
					}
				}
				return TreeDiffType.EQUAL;
			}
		});

		/*
		 * create the default change type style advisor that is used later to
		 * get the colors for the viewer
		 */
		IChangeTypeStyleAdvisor styleAdvisor = new DefaultChangeTypeStyleAdvisor();

		/* apply the colors for the different diff relations */
		TreeDiff treeDiff = treeDiffViewer.getTreeDiff();
		treeDiff.setDiffColor(TreeDiffType.ADD, styleAdvisor.getForegroundColorForChangeType(ChangeType.ADDITION));
		treeDiff.setDiffColor(TreeDiffType.DELETE, styleAdvisor.getForegroundColorForChangeType(ChangeType.DELETION));
		treeDiff.setDiffColor(TreeDiffType.MODIFY,
				styleAdvisor.getForegroundColorForChangeType(ChangeType.MODIFICATION));

		/* apply the input data */
		treeDiffViewer.setInput(input);
		/* refresh the control from the input data */
		treeDiffViewer.refresh();

		shell.open();
		shell.setSize(800, 600);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		styleAdvisor.dispose();

	}

	/* input data creation */

	private static Object createInput() {
		List<Match> matches = new ArrayList<>(5);

		matches.add(createSimpleDeletion());
		matches.add(createSimpleMatch());
		matches.add(createSimpleAddition());
		matches.add(createSimpleModification());

		matches.add(createMatchWithSubmatches());
		matches.add(createDeletedMatchWithSubmatches());
		matches.add(createAddedMatchWithSubmatches());
		matches.add(createComplexMatchWithSubmatches());

		matches.add(createMatchWithAddedSubmatch());
		matches.add(createMatchWithAddedSubmatches());
		matches.add(createMatchWithDeletedSubmatch());
		matches.add(createMatchWithDeletedSubmatches());

		return matches;
	}

	private static Match createSimpleMatch() {
		return new Match("Same value", "Same value", Match.DiffType.EQUAL);
	}

	private static Match createSimpleAddition() {
		return new Match(null, "Added value", Match.DiffType.ADD);
	}

	private static Match createSimpleDeletion() {
		return new Match("Deleted value", null, Match.DiffType.DELETE);
	}

	private static Match createSimpleModification() {
		return new Match("Value", "Modified value", Match.DiffType.MODIFY);
	}

	private static Match createMatchWithSubmatches() {
		Match match = createSimpleMatch();
		List<Match> submatches = match.getSubmatches();
		submatches.add(createSimpleMatch());
		submatches.add(createSimpleMatch());
		return match;
	}

	private static Match createAddedMatchWithSubmatches() {
		Match match = createSimpleAddition();
		List<Match> submatches = match.getSubmatches();
		submatches.add(createSimpleAddition());
		submatches.add(createSimpleAddition());
		return match;
	}

	private static Match createDeletedMatchWithSubmatches() {
		Match match = createSimpleDeletion();
		List<Match> submatches = match.getSubmatches();
		submatches.add(createSimpleDeletion());
		submatches.add(createSimpleDeletion());
		return match;
	}

	private static Match createComplexMatchWithSubmatches() {
		Match match = createSimpleMatch();
		List<Match> submatches = match.getSubmatches();
		submatches.add(createAddedMatchWithSubmatches());
		submatches.add(createMatchWithSubmatches());
		submatches.add(createAddedMatchWithSubmatches());
		return match;
	}

	private static Match createMatchWithDeletedSubmatch() {
		Match match = createSimpleMatch();
		List<Match> submatches = match.getSubmatches();
		submatches.add(createSimpleDeletion());
		return match;
	}

	private static Match createMatchWithDeletedSubmatches() {
		Match match = createSimpleMatch();
		List<Match> submatches = match.getSubmatches();
		submatches.add(createSimpleDeletion());
		submatches.add(createDeletedMatchWithSubmatches());
		submatches.add(createSimpleDeletion());
		return match;
	}

	private static Match createMatchWithAddedSubmatch() {
		Match match = createSimpleMatch();
		List<Match> submatches = match.getSubmatches();
		submatches.add(createSimpleAddition());
		return match;
	}

	private static Match createMatchWithAddedSubmatches() {
		Match match = createSimpleMatch();
		List<Match> submatches = match.getSubmatches();
		submatches.add(createSimpleAddition());
		submatches.add(createAddedMatchWithSubmatches());
		submatches.add(createSimpleAddition());
		return match;
	}

	/**
	 * Example data model of the input data.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private static class Match {

		enum DiffType {
			ADD, DELETE, MODIFY, EQUAL
		}

		private String leftValue;
		private String rightValue;

		private DiffType diffType;

		private List<Match> submatches = new ArrayList<>();

		public Match(String leftValue, String rightValue, DiffType diffType) {
			super();
			this.leftValue = leftValue;
			this.rightValue = rightValue;
			this.diffType = diffType;
		}

		/**
		 * @return the left value
		 */
		public String getLeftValue() {
			return leftValue;
		}

		/**
		 * @return the right value
		 */
		public String getRightValue() {
			return rightValue;
		}

		/**
		 * @return the submatches
		 */
		public List<Match> getSubmatches() {
			return submatches;
		}

		/**
		 * @return the diff type
		 */
		public DiffType getDiffType() {
			return diffType;
		}

	}
}

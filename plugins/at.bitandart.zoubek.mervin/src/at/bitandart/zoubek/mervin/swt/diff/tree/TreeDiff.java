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
package at.bitandart.zoubek.mervin.swt.diff.tree;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import at.bitandart.zoubek.mervin.swt.comments.examples.TreeDiffViewerExample;
import at.bitandart.zoubek.mervin.util.vis.ColorUtil;

/**
 * A widget that shows differences between to trees side-by-side, that
 * visualizes the following relations:
 * <ul>
 * <li>Addition</li>
 * <li>Deletion</li>
 * <li>Modification</li>
 * <li>Equality</li>
 * </ul>
 * Each of these relations can be visualized in a different color by specifying
 * a color using {@link #setDiffColor(TreeDiffType, Color)}. A relation will be
 * drawn with a default color if no colors are specified, which is the same for
 * all relations. Please note that the color of the label of an Equality
 * relation cannot be specified. Also note that the layout of this widget cannot
 * be changed, so any layout passed to {@link #setLayout(Layout)} will be
 * ignored.
 * 
 * @author Florian Zoubek
 * 
 * @see TreeDiffViewer
 * @see TreeDiffItem
 * @see TreeDiffViewerExample
 *
 */
public class TreeDiff extends Composite {

	private static final int DEFAULT_CENTER_WIDTH = 20;

	// color styles
	private Map<TreeDiffType, Color> diffColorStyles = new EnumMap<>(TreeDiffType.class);
	private Map<TreeDiffType, Color> diffBackgroundColorStyles = new EnumMap<>(TreeDiffType.class);
	private Color defaultDiffColor;
	private Color defaultDiffBackgroundColor;
	// foreground colors used for text on a colored background
	private Color fgColor1;
	private Color fgColor2;

	// Providers
	private TreeDiffContentProvider leftTreeDiffContentProvider;
	private TreeDiffContentProvider rightTreeDiffContentProvider;

	// Tree listeners
	private TreeViewerSynchronizer treeViewerSynchronizer;
	private SashRedrawer sashRedrawer;

	// SWT Controls
	private Sash centerSash;
	private TreeViewer leftTreeViewer;
	private TreeViewer rightTreeViewer;

	// Diff data
	private List<TreeDiffItem> rootDiffItems = new LinkedList<>();

	// Sash data
	private double splitRatio = 0.5;

	/**
	 * determines which side is considered as changed (new)
	 */
	private TreeDiffSide changedSide = TreeDiffSide.RIGHT;

	/**
	 * creates a new {@link TreeDiff} widget.
	 * 
	 * @param parent
	 *            the parent composite to create the widget for.
	 */
	public TreeDiff(Composite parent) {
		super(parent, SWT.NONE);

		// initialize colors
		defaultDiffColor = getDisplay().getSystemColor(SWT.COLOR_GRAY);
		defaultDiffBackgroundColor = new Color(getDisplay(),
				ColorUtil.blend(defaultDiffColor.getRGB(), 0.5, new RGB(255, 255, 255)));
		fgColor1 = getDisplay().getSystemColor(SWT.COLOR_WHITE);
		fgColor2 = getDisplay().getSystemColor(SWT.COLOR_BLACK);

		// initialize layout
		setLayout(new TreeDiffLayout());

		// create the sash control
		centerSash = new Sash(this, SWT.VERTICAL);
		new SashSplitUpdater(centerSash);
		centerSash.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {

				paintCenter(e.gc);

			}
		});

		// initialize the content providers for the trees
		leftTreeDiffContentProvider = new TreeDiffContentProvider(TreeDiffType.getValuesExcluding(TreeDiffType.ADD));
		rightTreeDiffContentProvider = new TreeDiffContentProvider(
				TreeDiffType.getValuesExcluding(TreeDiffType.DELETE));

		/* initialize the tree synchronization and sash redraw listeners */
		treeViewerSynchronizer = new TreeViewerSynchronizer();
		sashRedrawer = new SashRedrawer();
		TreeViewerRedrawer treeViewerRedrawer = new TreeViewerRedrawer();

		/* create the left tree viewer */
		leftTreeViewer = new TreeViewer(this);
		leftTreeViewer.setContentProvider(leftTreeDiffContentProvider);
		leftTreeViewer.setLabelProvider(new TreeDiffItemSideLabelProvider(TreeDiffSide.LEFT));
		leftTreeViewer.setInput(rootDiffItems);
		leftTreeViewer.addTreeListener(treeViewerSynchronizer);
		leftTreeViewer.addTreeListener(sashRedrawer);
		leftTreeViewer.getTree().addMouseMoveListener(sashRedrawer);
		leftTreeViewer.getTree().addMouseMoveListener(treeViewerRedrawer);
		leftTreeViewer.addTreeListener(treeViewerRedrawer);
		leftTreeViewer.addSelectionChangedListener(treeViewerSynchronizer);
		ScrollBar leftVerticalScrollBar = leftTreeViewer.getTree().getVerticalBar();
		leftVerticalScrollBar.addSelectionListener(sashRedrawer);

		/*
		 * create paint listener that draws the visualization on the left tree
		 */
		leftTreeViewer.getTree().addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {

				paintTreeOverlays(TreeDiffSide.LEFT, e.gc);

			}
		});

		/* create the right tree viewer */
		rightTreeViewer = new TreeViewer(this);
		rightTreeViewer.setContentProvider(rightTreeDiffContentProvider);
		rightTreeViewer.setLabelProvider(new TreeDiffItemSideLabelProvider(TreeDiffSide.RIGHT));
		rightTreeViewer.setInput(rootDiffItems);
		rightTreeViewer.addTreeListener(treeViewerSynchronizer);
		rightTreeViewer.addTreeListener(sashRedrawer);
		rightTreeViewer.getTree().addMouseMoveListener(sashRedrawer);
		rightTreeViewer.getTree().addMouseMoveListener(treeViewerRedrawer);
		rightTreeViewer.addTreeListener(treeViewerRedrawer);
		rightTreeViewer.addSelectionChangedListener(treeViewerSynchronizer);
		ScrollBar rightVerticalScrollBar = rightTreeViewer.getTree().getVerticalBar();
		rightVerticalScrollBar.addSelectionListener(sashRedrawer);

		/*
		 * create paint listener that draws the visualization on the right tree
		 */
		rightTreeViewer.getTree().addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {

				paintTreeOverlays(TreeDiffSide.RIGHT, e.gc);

			}
		});

		/* make the synchronizer aware of the trees to synchronize */
		List<TreeViewer> syncedTreeViewers = treeViewerSynchronizer.getSyncedTreeViewers();
		syncedTreeViewers.add(leftTreeViewer);
		syncedTreeViewers.add(rightTreeViewer);
	}

	/**
	 * draws the visualization on the center of this widget (on top of the sash)
	 * 
	 * @param gc
	 *            the {@link GC} used to draw onto the sash widget.
	 */
	protected void paintCenter(GC gc) {

		drawDiffCenter(rootDiffItems, gc);
	}

	/**
	 * draws the visualization onto the tree on the given {@link TreeDiffSide}.
	 * 
	 * @param side
	 *            the side to draw onto.
	 * @param gc
	 *            the {@link GC} used to draw onto the tree widget.
	 */
	protected void paintTreeOverlays(TreeDiffSide side, GC gc) {

		drawTreeOverlay(rootDiffItems, side, gc);
	}

	/**
	 * draws the visualization for the given items on the center of this widget
	 * (on top of the sash)
	 * 
	 * @param items
	 *            the items to draw.
	 * @param gc
	 *            the {@link GC} used to draw onto the sash widget.
	 */
	protected void drawDiffCenter(List<TreeDiffItem> items, GC gc) {

		for (TreeDiffItem item : items) {
			drawDiffCenter(item, gc);
			if (leftTreeViewer.getExpandedState(item) || rightTreeViewer.getExpandedState(item)) {
				drawDiffCenter(item.getChildren(), gc);
			}
		}
	}

	/**
	 * draws the visualization for the given items onto the tree on the given
	 * {@link TreeDiffSide}.
	 * 
	 * @param items
	 *            the items to draw.
	 * @param side
	 *            the side to draw onto.
	 * @param gc
	 *            the {@link GC} used to draw onto the tree widget.
	 */
	protected void drawTreeOverlay(List<TreeDiffItem> items, TreeDiffSide side, GC gc) {

		for (TreeDiffItem item : items) {
			drawTreeOverlay(item, side, gc);
			if (leftTreeViewer.getExpandedState(item) || rightTreeViewer.getExpandedState(item)) {
				drawTreeOverlay(item.getChildren(), side, gc);
			}
		}
	}

	/**
	 * draws the visualization for the given item on the center of this widget
	 * (on top of the sash)
	 * 
	 * @param item
	 *            the item to draw.
	 * @param gc
	 *            the {@link GC} used to draw onto the sash widget.
	 */
	protected void drawDiffCenter(TreeDiffItem item, GC gc) {

		switch (item.getTreeDiffType()) {
		case ADD:
			drawAddedItemCenter(item, gc);
			break;
		case DELETE:
			drawDeletedItemCenter(item, gc);
			break;
		case MODIFY:
			drawModifiedItemCenter(item, gc);
			break;
		case EQUAL:
			drawEqualItemCenter(item, gc);
			break;
		}
	}

	/**
	 * draws the visualization for the given items onto the tree on the given
	 * {@link TreeDiffSide}.
	 * 
	 * @param item
	 *            the item to draw.
	 * @param side
	 *            the side to draw onto.
	 * @param gc
	 *            the {@link GC} used to draw onto the tree widget.
	 */
	protected void drawTreeOverlay(TreeDiffItem item, TreeDiffSide side, GC gc) {

		switch (item.getTreeDiffType()) {
		case ADD:
			drawAddedItemOverlay(item, side, gc);
			break;
		case DELETE:
			drawDeletedItemOverlay(item, side, gc);
			break;
		case MODIFY:
			drawModifiedItemOverlay(item, side, gc);
			break;
		case EQUAL:
			drawEqualTreeOverlay(item, side, gc);
			break;
		}
	}

	/**
	 * draws the visualization in the center for the given added
	 * {@link TreeDiffItem}.
	 * 
	 * @param item
	 *            the item to draw.
	 * @param gc
	 *            the {@link GC} used to draw onto the sash widget.
	 */
	protected void drawAddedItemCenter(TreeDiffItem item, GC gc) {

		drawCenterLine(item, gc);
	}

	/**
	 * draws the visualization in the center for the given deleted
	 * {@link TreeDiffItem}.
	 * 
	 * @param item
	 *            the item to draw.
	 * @param gc
	 *            the {@link GC} used to draw onto the sash widget.
	 */
	protected void drawDeletedItemCenter(TreeDiffItem item, GC gc) {

		drawCenterLine(item, gc);
	}

	/**
	 * draws the visualization in the center for the given modified
	 * {@link TreeDiffItem}.
	 * 
	 * @param item
	 *            the item to draw.
	 * @param gc
	 *            the {@link GC} used to draw onto the sash widget.
	 */
	protected void drawModifiedItemCenter(TreeDiffItem item, GC gc) {

		drawCenterLine(item, gc);
	}

	/**
	 * draws the visualization in the center for the given unmodified
	 * {@link TreeDiffItem}.
	 * 
	 * @param item
	 *            the item to draw.
	 * @param gc
	 *            the {@link GC} used to draw onto the sash widget.
	 */
	protected void drawEqualItemCenter(TreeDiffItem item, GC gc) {

		TreeItem leftTreeItem = getContextTreeItem(item, TreeDiffSide.LEFT);
		Rectangle leftBounds = toAbsoluteBounds(leftTreeItem.getBounds(), leftTreeViewer.getTree());

		TreeItem rightTreeItem = getContextTreeItem(item, TreeDiffSide.RIGHT);
		Rectangle rightBounds = toAbsoluteBounds(rightTreeItem.getBounds(), rightTreeViewer.getTree());

		Point cursorLocation = getDisplay().getCursorLocation();

		if (leftBounds.contains(cursorLocation) || rightBounds.contains(cursorLocation)) {

			drawCenterLine(item.getTreeDiffType(), leftTreeItem, rightTreeItem, gc);
		}
	}

	/**
	 * draws a line that connects the left and the right visualization of the
	 * given item at the center.
	 * 
	 * @param item
	 *            the item to draw the connection line.
	 * @param gc
	 *            the {@link GC} used to draw onto the sash widget.
	 */
	protected void drawCenterLine(TreeDiffItem item, GC gc) {

		TreeItem leftTreeItem = getContextTreeItem(item, TreeDiffSide.LEFT);
		TreeItem rightTreeItem = getContextTreeItem(item, TreeDiffSide.RIGHT);

		drawCenterLine(item.getTreeDiffType(), leftTreeItem, rightTreeItem, gc);

	}

	/**
	 * draws a line that connects the left and the right visualization of the
	 * given items with the given {@link TreeDiffType} at the center.
	 * 
	 * @param type
	 *            the type of the of the {@link TreeDiffItem} to draw the line
	 *            for.
	 * @param leftTreeItem
	 *            the left {@link TreeItem} to draw the line from.
	 * @param rightTreeItem
	 *            the right {@link TreeItem} to draw the line to.
	 * @param gc
	 *            the {@link GC} used to draw onto the sash widget.
	 */
	protected void drawCenterLine(TreeDiffType type, TreeItem leftTreeItem, TreeItem rightTreeItem, GC gc) {

		Point fromPoint = centerSash.toControl(getAttachmentPoint(leftTreeItem, type, TreeDiffSide.LEFT));
		Point toPoint = centerSash.toControl(getAttachmentPoint(rightTreeItem, type, TreeDiffSide.RIGHT));

		gc.setForeground(getDiffColor(type));
		drawCenterLine(fromPoint, toPoint, gc);

	}

	/**
	 * draws a line that connects the left and the right visualization starting
	 * at the point {@code from} and ending at the point {@code to}.
	 * 
	 * @param from
	 *            the point to start the line.
	 * @param to
	 *            the point to end the line.
	 * @param gc
	 *            the {@link GC} used to draw onto the sash widget.
	 */
	protected void drawCenterLine(Point from, Point to, GC gc) {
		gc.drawLine(from.x, from.y, to.x, to.y);
	}

	/**
	 * draws the overlay on the given side for the given added item.
	 * 
	 * @param item
	 *            the item to draw.
	 * @param side
	 *            the side to draw the overlay to.
	 * @param gc
	 *            the {@link GC} used to draw onto the tree widget.
	 */
	protected void drawAddedItemOverlay(TreeDiffItem item, TreeDiffSide side, GC gc) {

		gc.setForeground(getDiffColor(TreeDiffType.ADD));
		if (side == changedSide.getOpposite()) {
			drawInsertionLine(item, side, TreeDiffType.ADD, gc);
		} else {
			drawOutline(item, side, TreeDiffType.ADD, gc);
		}

	}

	/**
	 * draws the overlay on the given side for the given deleted item.
	 * 
	 * @param item
	 *            the item to draw.
	 * @param side
	 *            the side to draw the overlay to.
	 * @param gc
	 *            the {@link GC} used to draw onto the tree widget.
	 */
	protected void drawDeletedItemOverlay(TreeDiffItem item, TreeDiffSide side, GC gc) {

		gc.setForeground(getDiffColor(TreeDiffType.DELETE));
		if (side == changedSide) {
			drawInsertionLine(item, side, TreeDiffType.DELETE, gc);
		} else {
			drawOutline(item, side, TreeDiffType.DELETE, gc);
		}
	}

	/**
	 * draws the overlay on the given side for the given modified item.
	 * 
	 * @param item
	 *            the item to draw.
	 * @param side
	 *            the side to draw the overlay to.
	 * @param gc
	 *            the {@link GC} used to draw onto the tree widget.
	 */
	protected void drawModifiedItemOverlay(TreeDiffItem item, TreeDiffSide side, GC gc) {

		gc.setForeground(getDiffColor(TreeDiffType.MODIFY));
		drawOutline(item, side, TreeDiffType.MODIFY, gc);

	}

	/**
	 * draws the overlay on the given side for the given unmodified item.
	 * 
	 * @param item
	 *            the item to draw.
	 * @param side
	 *            the side to draw the overlay to.
	 * @param gc
	 *            the {@link GC} used to draw onto the tree widget.
	 */
	protected void drawEqualTreeOverlay(TreeDiffItem item, TreeDiffSide side, GC gc) {

		TreeItem leftContextTreeItem = getContextTreeItem(item, TreeDiffSide.LEFT);
		Rectangle leftBounds = toAbsoluteBounds(leftContextTreeItem.getBounds(), leftTreeViewer.getTree());
		TreeItem rightContextTreeItem = getContextTreeItem(item, TreeDiffSide.RIGHT);
		Rectangle rightBounds = toAbsoluteBounds(rightContextTreeItem.getBounds(), rightTreeViewer.getTree());

		Point cursorLocation = getDisplay().getCursorLocation();

		if (leftBounds.contains(cursorLocation) || rightBounds.contains(cursorLocation)) {

			gc.setForeground(getDiffColor(TreeDiffType.EQUAL));
			drawOutline(item, side, TreeDiffType.EQUAL, gc);
		}

	}

	/**
	 * Convenience method to get the {@link Display}-relative bounds for the
	 * given control-relative bounds.
	 * 
	 * @param bounds
	 *            the bounds relative to {@code parent}.
	 * @param parent
	 *            the control that the bounds are relative to.
	 * @return the {@link Display}-relative bounds.
	 */
	protected Rectangle toAbsoluteBounds(Rectangle bounds, Control parent) {
		Point absoluteLocation = parent.toDisplay(bounds.x, bounds.y);
		return new Rectangle(absoluteLocation.x, absoluteLocation.y, bounds.width, bounds.height);
	}

	/**
	 * returns the context {@link TreeItem} for the given {@link TreeDiffSide}
	 * and {@link TreeDiffItem}. The context {@link TreeItem} is the item that
	 * should be used to draw the diff visualization for the given
	 * {@link TreeDiffItem}.
	 * 
	 * @param item
	 *            the {@link TreeDiffItem} to retrieve the context
	 *            {@link TreeItem} for.
	 * @param side
	 *            the {@link TreeDiffSide} retrieve the context {@link TreeItem}
	 *            for.
	 * @return the context {@link TreeItem} or null if no context treeItem could
	 *         be found.
	 */
	protected TreeItem getContextTreeItem(TreeDiffItem item, TreeDiffSide side) {

		TreeViewer treeViewer = leftTreeViewer;
		if (side == TreeDiffSide.RIGHT) {
			treeViewer = rightTreeViewer;
		}

		switch (item.getTreeDiffType()) {
		case ADD:
			if (side == changedSide.getOpposite()) {
				return findContextTreeItemUsingInsertionPoint(item, treeViewer);
			} else {
				return findTreeItem(treeViewer, item);
			}
		case DELETE:
			if (side == changedSide) {
				return findContextTreeItemUsingInsertionPoint(item, treeViewer);
			} else {
				return findTreeItem(treeViewer, item);
			}
		case EQUAL:
		case MODIFY:
			return findTreeItem(treeViewer, item);
		default:
		}

		return null;

	}

	/**
	 * determines the {@link InsertionPoint} for the given {@link TreeDiffItem}
	 * in the given {@link TreeViewer} and returns the context {@link TreeItem}
	 * for it.
	 * 
	 * @param item
	 *            the item to determine the {@link InsertionPoint} for.
	 * @param treeViewer
	 *            the {@link TreeViewer} to determine the {@link InsertionPoint}
	 *            for.
	 * @return the context {@link TreeItem} for the determined
	 *         {@link InsertionPoint}.
	 */
	protected TreeItem findContextTreeItemUsingInsertionPoint(TreeDiffItem item, TreeViewer treeViewer) {

		InsertionPointTreeContext insertionContext = getInsertionPointTreeContext(item, treeViewer);
		if (insertionContext.lastNotExpandedChild != null) {
			return insertionContext.lastNotExpandedChild;
		}

		return insertionContext.ancestor;
	}

	/**
	 * determines the {@link InsertionPoint} for the given {@link TreeDiffItem}
	 * in the given {@link TreeViewer} and returns the
	 * {@link InsertionPointTreeContext} for it.
	 * 
	 * @param item
	 *            the item to determine the {@link InsertionPoint} for.
	 * @param treeViewer
	 *            the {@link TreeViewer} to determine the {@link InsertionPoint}
	 *            for.
	 * @return the {@link InsertionPointTreeContext} for the determined
	 *         {@link InsertionPoint}. It never returns null but the contexts
	 *         fields may be null.
	 */
	protected InsertionPointTreeContext getInsertionPointTreeContext(TreeDiffItem item, TreeViewer treeViewer) {

		InsertionPoint insertionPoint = findInsertionPointForType(null, item,
				TreeDiffType.getValuesExcluding(item.getTreeDiffType()));

		InsertionPointTreeContext context = new InsertionPointTreeContext(insertionPoint);

		context.ancestor = findTreeItem(treeViewer, insertionPoint.ancestor);
		/*
		 * the context item is the tree item before the descendant - that is the
		 * last not expanded child of the ancestor if the ancestor and the
		 * descendant are siblings
		 */
		if (insertionPoint.descendant != null && insertionPoint.descendant.isSiblingOf(insertionPoint.ancestor)) {
			context.lastNotExpandedChild = getLastNotExpandedChild(context.ancestor);
			if (context.lastNotExpandedChild == context.ancestor) {
				context.lastNotExpandedChild = null;
			}
		}

		return context;
	}

	/**
	 * Helper class that stores the {@link TreeItem} associated with a given
	 * {@link InsertionPoint}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	protected static class InsertionPointTreeContext {

		/**
		 * the {@link TreeItem} associated with the ancestor of an
		 * {@link InsertionPoint}, or null if no such {@link TreeItem} exists.
		 */
		public TreeItem ancestor;

		/**
		 * the last not expanded child of the ancestor {@link TreeItem}, or null
		 * if no such {@link TreeItem} exists.
		 */
		public TreeItem lastNotExpandedChild;

		/**
		 * the {@link InsertionPoint} associated with this context.
		 */
		public InsertionPoint insertionPoint;

		/**
		 * @param insertionPoint
		 *            the {@link InsertionPoint} associated with this context.
		 */
		public InsertionPointTreeContext(InsertionPoint insertionPoint) {
			this(insertionPoint, null);
		}

		/**
		 * @param insertionPoint
		 *            the {@link InsertionPoint} associated with this context.
		 * @param ancestor
		 *            the {@link TreeItem} associated with the ancestor of an
		 *            {@link InsertionPoint}.
		 */
		public InsertionPointTreeContext(InsertionPoint insertionPoint, TreeItem ancestor) {
			this(insertionPoint, ancestor, null);
		}

		/**
		 * @param insertionPoint
		 *            the {@link InsertionPoint} associated with this context.
		 * @param ancestor
		 *            the {@link TreeItem} associated with the ancestor of an
		 *            {@link InsertionPoint}.
		 * @param lastNotExpandedChild
		 *            the last not expanded child {@link TreeItem} of the
		 *            ancestor {@link TreeItem}
		 */
		public InsertionPointTreeContext(InsertionPoint insertionPoint, TreeItem ancestor,
				TreeItem lastNotExpandedChild) {
			this.insertionPoint = insertionPoint;
			this.ancestor = ancestor;
			this.lastNotExpandedChild = lastNotExpandedChild;
		}

	}

	/**
	 * draws an insertion line based on the {@link InsertionPoint} for the given
	 * {@link TreeDiffItem} on the given {@link TreeDiffSide}, styled for the
	 * given {@link TreeDiffType}.
	 * 
	 * @param item
	 *            the item to draw the insertion line for.
	 * @param side
	 *            the side to draw the insertion line for.
	 * @param diffType
	 *            the {@link TreeDiffType} that should be used to derive the
	 *            style of the insertion line.
	 * @param gc
	 *            the {@link GC} used to draw onto the tree widget.
	 */
	protected void drawInsertionLine(TreeDiffItem item, TreeDiffSide side, TreeDiffType diffType, GC gc) {

		TreeViewer treeViewer = null;

		if (side == TreeDiffSide.LEFT) {
			treeViewer = leftTreeViewer;
		} else {
			treeViewer = rightTreeViewer;
		}

		Tree tree = treeViewer.getTree();

		/* determine the tree items around the insertion point */
		InsertionPointTreeContext insertionPointTreeContext = getInsertionPointTreeContext(item, treeViewer);

		TreeItem ancestorTreeItem = insertionPointTreeContext.ancestor;
		TreeItem lastNotExpandedChild = ancestorTreeItem;
		if (insertionPointTreeContext.lastNotExpandedChild != null) {
			lastNotExpandedChild = insertionPointTreeContext.lastNotExpandedChild;
		}

		InsertionPoint insertionPoint = insertionPointTreeContext.insertionPoint;

		/* determine the point to attach to the center line */
		Point attachmentPoint = tree.toControl(getAttachmentPoint(lastNotExpandedChild, diffType, side));

		/*
		 * determine the bounds of the first item that should be used for
		 * insertions before the first item
		 */
		Rectangle firstItemBounds = null;
		if (tree.getItemCount() > 0) {
			firstItemBounds = tree.getItem(0).getBounds();
		}

		/*
		 * initialize the bounds that should be used for rendering with fallback
		 * values
		 */

		/*
		 * the ancestor bounds are needed to determine the correct indentation
		 * and width
		 */
		Rectangle ancestorBounds = new Rectangle((firstItemBounds != null) ? firstItemBounds.x : 0,
				(firstItemBounds != null) ? firstItemBounds.y : 0,
				(firstItemBounds != null) ? firstItemBounds.width : 100, 0);
		/*
		 * the bounds of the last expanded child are needed to determine the
		 * correct y-value and width
		 */
		Rectangle lastNotExpandedChildBounds = new Rectangle(ancestorBounds.x, ancestorBounds.y, ancestorBounds.width,
				ancestorBounds.height);

		/* apply the real tree item bounds if possible */
		if (ancestorTreeItem != null) {
			ancestorBounds = ancestorTreeItem.getBounds();
		}
		if (lastNotExpandedChild != null) {
			lastNotExpandedChildBounds = lastNotExpandedChild.getBounds();
		}

		int startX = ancestorBounds.x;
		int endX = Math.max(ancestorBounds.x + ancestorBounds.width,
				lastNotExpandedChildBounds.x + lastNotExpandedChildBounds.width);

		if (insertionPoint.descendant != null && insertionPoint.descendant.getParent() == insertionPoint.ancestor
				&& ancestorTreeItem != null) {
			/*
			 * the descendant is the child of the ancestor, indentation is
			 * needed to indicate that this is an insertion of a child of the
			 * ancestor and not an insertion of a sibling. Use a static
			 * indentation value as fallback for now as this is platform
			 * specific.
			 */
			startX = ancestorTreeItem.getBounds().x + 10;
			if (ancestorTreeItem.getItemCount() > 0) {
				/*
				 * the ancestor has child items, use them to derive the correct
				 * indentation and width.
				 */
				Rectangle firstChildBounds = ancestorTreeItem.getItem(0).getBounds();
				startX = firstChildBounds.x;
				endX = Math.max(ancestorBounds.x + ancestorBounds.width, firstChildBounds.x + firstChildBounds.width);
			}
		}

		/*
		 * derive the start and end point for the insertion line from the bounds
		 */
		Point insertionLineStart = new Point(startX, lastNotExpandedChildBounds.y + lastNotExpandedChildBounds.height);
		Point insertionLineEnd = new Point(endX, insertionLineStart.y);

		/* draw the insertion line */
		gc.setLineWidth(4);
		gc.drawLine(insertionLineStart.x, insertionLineStart.y, insertionLineEnd.x, insertionLineEnd.y);
		gc.setLineWidth(1);

		/* draw the connection line to the center */
		if (side == TreeDiffSide.LEFT) {
			gc.drawLine(insertionLineEnd.x, insertionLineEnd.y, attachmentPoint.x, attachmentPoint.y);
		} else {
			gc.drawLine(insertionLineStart.x, insertionLineStart.y, attachmentPoint.x, attachmentPoint.y);
		}

	}

	/**
	 * draws an outline around the context tree item on the given
	 * {@link TreeDiffSide} for the given {@link TreeDiffItem}, styled for the
	 * given {@link TreeDiffType}.
	 * 
	 * @param item
	 *            the {@link TreeDiffItem} to draw the outline for.
	 * @param side
	 *            the {@link TreeDiffSide} to draw the outline for.
	 * @param diffType
	 *            the {@link TreeDiffType} to draw the outline for.
	 * @param gc
	 *            the {@link GC} used to draw onto the tree widget.
	 */
	protected void drawOutline(TreeDiffItem item, TreeDiffSide side, TreeDiffType diffType, GC gc) {

		int horizontalMargin = -2;

		TreeViewer treeViewer = null;

		if (side == TreeDiffSide.LEFT) {
			treeViewer = leftTreeViewer;
		} else {
			treeViewer = rightTreeViewer;
		}

		Tree tree = treeViewer.getTree();

		/* find the item to outline and get its bounds */
		TreeItem treeItem = getContextTreeItem(item, side);
		Rectangle bounds = new Rectangle(0, 0, 0, 0);
		if (treeItem != null) {
			bounds = treeItem.getBounds();
		}

		/* determine the point to attach to at the bounds of the item */
		Point outlineAttachmentPoint = null;
		if (side == TreeDiffSide.LEFT) {
			outlineAttachmentPoint = new Point(bounds.x + bounds.width + horizontalMargin,
					bounds.y + (int) (bounds.height / 2.0));
		} else {
			outlineAttachmentPoint = new Point(bounds.x + horizontalMargin, bounds.y + (int) (bounds.height / 2.0));
		}

		/* determine the point to attach to at the center */
		Point sashAttachmentPoint = tree.toControl(getAttachmentPoint(treeItem, diffType, side));

		/* draw the connection line from the bounds to the center */
		gc.drawLine(sashAttachmentPoint.x, sashAttachmentPoint.y, outlineAttachmentPoint.x, outlineAttachmentPoint.y);

	}

	/**
	 * finds the last (determined by the order drawn on the screen) not expanded
	 * child {@link TreeItem} in the children subtree for the given
	 * {@link TreeItem}.
	 * 
	 * @param item
	 *            the item to search the last not expanded item for.
	 * @return the last not expanded item or the given item if it has no
	 *         children or is not expanded.
	 */
	protected TreeItem getLastNotExpandedChild(TreeItem item) {
		if (item != null && item.getExpanded() && item.getItemCount() > 0) {
			return getLastNotExpandedChild(item.getItem(item.getItemCount() - 1));
		}
		return item;
	}

	/**
	 * determines the center attachment point for the given {@link TreeItem}
	 * with respect of the given {@link TreeDiffType} on the given
	 * {@link TreeDiffSide}.
	 * 
	 * @param item
	 *            the item to get the attachment point for.
	 * @param type
	 *            the type to get the attachment point for.
	 * @param side
	 *            the side to get the attachment point for.
	 * @return the attachment point for the given item.
	 */
	protected Point getAttachmentPoint(TreeItem item, TreeDiffType type, TreeDiffSide side) {

		Tree tree = leftTreeViewer.getTree();
		if (side == TreeDiffSide.RIGHT) {
			tree = rightTreeViewer.getTree();
		}

		Rectangle bounds = new Rectangle(0, 0, 0, 0);

		if (tree.getItemCount() > 0) {
			/*
			 * use the bounds of the first item as fallback for not existing
			 * items
			 */
			Rectangle firstItemBounds = tree.getItem(0).getBounds();
			bounds.x = firstItemBounds.x;
			bounds.y = firstItemBounds.y;
		}

		if (item != null) {
			bounds = item.getBounds();
		}

		return getAttachmentPoint(bounds, type, side);
	}

	/**
	 * 
	 * determines the center attachment point for the given Bounds with respect
	 * of the given {@link TreeDiffType} on the given {@link TreeDiffSide}.
	 * 
	 * @param bounds
	 *            the bounds to get the attachment point for.
	 * @param type
	 *            the type to get the attachment point for.
	 * @param side
	 *            the side to get the attachment point for.
	 * @return the {@link Display}-relative attachment point for the given
	 *         bounds.
	 */
	protected Point getAttachmentPoint(Rectangle bounds, TreeDiffType type, TreeDiffSide side) {

		TreeViewer treeViewer = leftTreeViewer;

		if (side == TreeDiffSide.RIGHT) {
			treeViewer = rightTreeViewer;
		}

		Point fromPoint = new Point(0, 0);
		if ((type == TreeDiffType.ADD && side == changedSide.getOpposite())
				|| (type == TreeDiffType.DELETE && side == changedSide)) {
			/*
			 * "insertion line" (opposite side of an addition/right side of a
			 * deletion): attach to the projected lower corners of the bounds,
			 * but ignore the x coordinate for now.
			 */
			fromPoint.y = bounds.y + bounds.height;
		} else {
			/*
			 * default behavior: attach to the projected center of the
			 * left/right sides of the bounds, but ignore the x coordinate for
			 * now.
			 */
			fromPoint.y = bounds.y + (int) (bounds.height / 2.0);
		}

		/*
		 * the point is relative to the tree, so transform it to be relative to
		 * the display
		 */
		fromPoint = treeViewer.getTree().toDisplay(fromPoint);

		/* project onto the center sash control side */
		if (side == TreeDiffSide.LEFT) {
			fromPoint.x = 0;
		} else {
			fromPoint.x = centerSash.getBounds().width;
		}

		/*
		 * the x-value is relative to the sash, so transform it to be relative
		 * to the display
		 */
		fromPoint.x = centerSash.toDisplay(fromPoint.x, 0).x;

		return fromPoint;

	}

	/**
	 * finds the {@link TreeItem} in the given {@link TreeViewer} for the given
	 * {@link TreeDiffItem}.
	 * 
	 * @param treeViewer
	 *            the {@link TreeViewer} to search.
	 * @param item
	 *            the {@link TreeDiffItem} to search for.
	 * @return the {@link TreeItem} associated to the given {@link TreeDiffItem}
	 */
	protected TreeItem findTreeItem(TreeViewer treeViewer, TreeDiffItem item) {
		return findTreeItem(treeViewer.getTree().getItems(), item);
	}

	/**
	 * finds the {@link TreeItem} in the given array of {@link TreeItem}s and
	 * their children for the given {@link TreeDiffItem}.
	 * 
	 * @param treeItems
	 *            the {@link TreeItem}s to search.
	 * @param diffItem
	 *            the {@link TreeDiffItem} to search for.
	 * @return the {@link TreeItem} associated to the given {@link TreeDiffItem}
	 */
	protected TreeItem findTreeItem(TreeItem[] treeItems, TreeDiffItem diffItem) {

		if (diffItem != null) {
			for (TreeItem treeItem : treeItems) {
				if (treeItem.getData() == diffItem) {
					return treeItem;
				}
				TreeItem item = findTreeItem(treeItem.getItems(), diffItem);
				if (item != null) {
					return item;
				}
			}
		}

		return null;
	}

	/**
	 * finds the first insertion point for the given {@link TreeDiffItem} which
	 * is described by the first ancestor that is of one of the given
	 * {@link TreeDiffType}s and its descendant by walking its parents and
	 * siblings upwards (in the order they are drawn on the screen).
	 * 
	 * @param prevItem
	 *            the previous or descendant item, may be null in the first
	 *            call.
	 * @param item
	 *            the {@link TreeDiffItem} to start the search at.
	 * @param types
	 *            the {@link TreeDiffType} a {@link TreeDiffItem} on the way to
	 *            the root must have to be considered as ancestor.
	 * @return the found {@link InsertionPoint} but never null. However, the
	 *         contained ancestor and or its descendant may be null.
	 */
	protected InsertionPoint findInsertionPointForType(TreeDiffItem prevItem, TreeDiffItem item, TreeDiffType[] types) {

		if (isOfType(item, types)) {
			return new InsertionPoint(item, prevItem);
		} else {
			TreeDiffItem parent = item.getParent();

			List<TreeDiffItem> siblings = null;
			if (parent == null && rootDiffItems.contains(item)) {
				/* the root items are the siblings if no parent is specified */
				siblings = rootDiffItems;
			} else if (parent != null) {
				siblings = parent.getChildren();
			}

			if (siblings != null) {

				int index = siblings.indexOf(item);
				ListIterator<TreeDiffItem> ancestorIterator = siblings.listIterator(index);
				/*
				 * move through the sibling collection upwards to find the
				 * ancestor
				 */
				while (ancestorIterator.hasPrevious()) {
					TreeDiffItem ancestor = ancestorIterator.previous();
					if (isOfType(ancestor, types)) {
						ancestorIterator.next(); // ancestor again
						return new InsertionPoint(ancestor, ancestorIterator.next());
					}
				}

				if (parent != null) {
					/*
					 * no insertion point found in the current level, so move
					 * one level up
					 */
					return findInsertionPointForType(siblings.get(0), parent, types);
				} else {
					return new InsertionPoint(null, siblings.get(0));
				}

			}
		}
		return new InsertionPoint();
	}

	/**
	 * Helper class that describes a point between an ancestor and descendant in
	 * the diff hierachy (as it would be drawn in a standard SWT {@link Tree}
	 * widget). Note that the ancestor and/or the descendant may be null. An
	 * insertion point with no ancestor but an descendant describes an insertion
	 * at the top of the tree, whereas an insertion point with an ancestor but
	 * no descendant describes an insertion at the end of the tree. An insertion
	 * point describes an insertion into an empty tree if both the ancestor and
	 * the descendant are null.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	protected class InsertionPoint {

		/**
		 * the ancestor before the insertion point.
		 */
		public TreeDiffItem ancestor;

		/**
		 * the descendant after the insertion point.
		 */
		public TreeDiffItem descendant;

		public InsertionPoint() {
		}

		/**
		 * 
		 * @param ancestor
		 *            the ancestor before the insertion point.
		 * @param descendant
		 *            the descendant after the insertion point.
		 */
		public InsertionPoint(TreeDiffItem ancestor, TreeDiffItem descendant) {
			this.ancestor = ancestor;
			this.descendant = descendant;
		}
	}

	/**
	 * determines if the given {@link TreeDiffItem} is of any of the given
	 * types.
	 * 
	 * @param item
	 *            the {@link TreeDiffItem} to test.
	 * @param types
	 *            the types to used to check the {@link TreeDiffItem}
	 * @return true if the {@link TreeDiffItem} is of any of the given types,
	 *         false otherwise.
	 */
	protected boolean isOfType(TreeDiffItem item, TreeDiffType[] types) {
		for (TreeDiffType type : types) {
			if (item.getTreeDiffType() == type) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the width of the center control.
	 */
	protected int getCenterWidth() {
		return DEFAULT_CENTER_WIDTH;
	}

	/**
	 * @return the split ratio that divides the control into the left and right
	 *         side.
	 */
	public double getSplitRatio() {
		return splitRatio;
	}

	/**
	 * sets the color to use for {@link TreeDiffItem}s of the given type. The so
	 * specified colors are not managed by this widget and must be disposed by
	 * the caller once the colors and this widget is no longer used.
	 * 
	 * @param type
	 *            the type to associate the given color with.
	 * @param color
	 *            the color to associate the given type with.
	 */
	public void setDiffColor(TreeDiffType type, Color color) {
		if (type != null && color != null) {
			diffColorStyles.put(type, color);
			Color backgroundColor = new Color(getDisplay(),
					ColorUtil.blend(color.getRGB(), 0.5, new RGB(255, 255, 255)));
			diffBackgroundColorStyles.put(type, backgroundColor);
		}
	}

	@Override
	public void dispose() {
		defaultDiffBackgroundColor.dispose();
		for (Color color : diffBackgroundColorStyles.values()) {
			color.dispose();
		}
		super.dispose();
	}

	/**
	 * @param type
	 *            the type the retrieve the color for.
	 * @return the associated color for the given type.
	 */
	protected Color getDiffColor(TreeDiffType type) {

		Color color = diffColorStyles.get(type);
		if (color == null) {
			color = defaultDiffColor;
		}

		return color;
	}

	/**
	 * @param type
	 *            the type the retrieve the background color for.
	 * @return the associated background color for the given type.
	 */
	protected Color getDiffBackgroundColor(TreeDiffType type) {

		Color color = diffBackgroundColorStyles.get(type);
		if (color == null) {
			color = defaultDiffBackgroundColor;
		}

		return color;
	}

	/**
	 * this control does not allow changing the layout, so specifying any other
	 * layout than the default internal layout will have no effect.
	 */
	@Override
	public void setLayout(Layout layout) {
		/*
		 * disallow changing the layout once it has been set (which is done in
		 * the constructor)
		 */
		if (getLayout() == null) {
			super.setLayout(layout);
		}
	}

	/**
	 * The layout used to layout out the left, right and sash widgets based on
	 * the {@link TreeDiff#splitRatio}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class TreeDiffLayout extends Layout {

		@Override
		protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
			return new Point(100, 100);
		}

		@Override
		protected void layout(Composite composite, boolean flushCache) {

			Rectangle r = composite.getClientArea();

			int centerWidth = getCenterWidth();
			/*
			 * calculate the widths based on the split ration and the center
			 * width
			 */
			int leftWidth = (int) ((r.width - centerWidth) * getSplitRatio());
			int rightWidth = r.width - leftWidth - centerWidth;

			int height = r.height;

			int y = 0;

			/* apply the new bounds */
			if (centerSash != null && !centerSash.isDisposed())
				centerSash.setBounds(leftWidth, y, centerWidth, height);

			leftTreeViewer.getControl().setBounds(0, y, leftWidth, height);
			rightTreeViewer.getControl().setBounds(leftWidth + centerWidth, y, rightWidth, height);

		}

	}

	/**
	 * The mouse listeners used to allow resizing the control by dragging the
	 * sash.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class SashSplitUpdater extends MouseAdapter implements MouseMoveListener {

		private boolean mouseDrag = false;
		private Point dragOrigin = new Point(0, 0);
		private int leftOriginalWidth;
		private int rightOriginalWidth;

		public SashSplitUpdater(Sash sash) {
			sash.addMouseListener(this);
			sash.addMouseMoveListener(this);
		}

		@Override
		public void mouseDown(MouseEvent e) {

			/*
			 * start dragging and remember the original widths and initial
			 * horizontal mouse position
			 */
			leftOriginalWidth = leftTreeViewer.getControl().getSize().x;
			rightOriginalWidth = rightTreeViewer.getControl().getSize().x;

			dragOrigin.x = e.widget.getDisplay().getCursorLocation().x;

			mouseDrag = true;
		}

		@Override
		public void mouseUp(MouseEvent e) {

			/*
			 * finished dragging, recalculate the split ratio for the final time
			 */
			mouseDrag = false;
			updateSplitRatio(e.x - dragOrigin.x);

		}

		@Override
		public void mouseMove(MouseEvent e) {
			if (mouseDrag) {
				/* preview the new split ratio by applying it */
				updateSplitRatio(e.widget.getDisplay().getCursorLocation().x - dragOrigin.x);
			}
		}

		/**
		 * Recalculates and applies the new split ratio based on the horizontal
		 * move distance (relative to the original horizontal position).
		 * 
		 * @param dX
		 *            the horizontal move distance in pixels relative to the
		 *            original horizontal position.
		 */
		private void updateSplitRatio(int dX) {

			int centerWidth = centerSash.getSize().x;

			if (leftOriginalWidth + dX > centerWidth && rightOriginalWidth - dX > centerWidth) {
				int newLeftWidth = leftOriginalWidth + dX;
				int newRightWidth = rightOriginalWidth - dX;
				splitRatio = (double) newLeftWidth / (double) (newLeftWidth + newRightWidth);
			}

			layout(true);
			getDisplay().update();
		}

	}

	/**
	 * A {@link ITreeViewerListener} and {@link ISelectionChangedListener} that
	 * synchronizes the expansion state and the selection state if a list of
	 * {@link TreeViewer}s with each other. To synchronize a set of
	 * {@link TreeViewer}s add the to the collection returned by
	 * {@link TreeViewerSynchronizer#getSyncedTreeViewers()}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class TreeViewerSynchronizer implements ITreeViewerListener, ISelectionChangedListener {

		private List<TreeViewer> syncedTreeViewers = new ArrayList<>();
		private boolean synchronizingState = false;

		@Override
		public void treeCollapsed(TreeExpansionEvent event) {

			syncExpansionState(event.getTreeViewer(), event.getElement(), false);
		}

		@Override
		public void treeExpanded(TreeExpansionEvent event) {

			syncExpansionState(event.getTreeViewer(), event.getElement(), true);
		}

		/**
		 * @return the list of synchronized {@link TreeViewer}s by this
		 *         {@link TreeViewerSynchronizer}.
		 */
		public List<TreeViewer> getSyncedTreeViewers() {

			return syncedTreeViewers;
		}

		/**
		 * synchronizes the expansion state of the given {@link TreeViewer} with
		 * the other {@link TreeViewer}s.
		 * 
		 * @param currentTreeViewer
		 *            the tree viewer whose expansion state has changed.
		 * @param element
		 *            the element whose expansion state has changed.
		 * @param expanded
		 *            the expansion state to synchronize with the other
		 *            {@link TreeViewer}s.
		 */
		private void syncExpansionState(AbstractTreeViewer currentTreeViewer, Object element, boolean expanded) {

			for (TreeViewer treeViewerToSync : syncedTreeViewers) {
				if (treeViewerToSync != currentTreeViewer) {
					treeViewerToSync.setExpandedState(element, expanded);
				}
			}
		}

		/**
		 * synchronizes the selection state of the given {@link TreeViewer} with
		 * the other {@link TreeViewer}s.
		 * 
		 * @param currentTreeViewer
		 *            the tree viewer whose selection state has changed.
		 * @param selection
		 *            the new selection of the given {@link TreeViewer}.
		 */
		private void syncSelectionState(AbstractTreeViewer currentTreeViewer, ISelection selection) {

			if (!synchronizingState) {
				/* prevent cyclic listener invocation */
				synchronizingState = true;

				for (TreeViewer treeViewerToSync : syncedTreeViewers) {
					if (treeViewerToSync != currentTreeViewer) {
						treeViewerToSync.setSelection(selection);
					}
				}

				synchronizingState = false;
			}
		}

		@Override
		public void selectionChanged(SelectionChangedEvent event) {

			Object source = event.getSource();
			if (source instanceof AbstractTreeViewer) {
				syncSelectionState((AbstractTreeViewer) source, event.getSelection());
			}
		}

	}

	/**
	 * A {@link MouseMoveListener} and {@link ITreeViewerListener} that redraws
	 * the left and right {@link TreeViewer}s to ensure the proper redrawing of
	 * the diff visualization.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class TreeViewerRedrawer implements MouseMoveListener, ITreeViewerListener {

		@Override
		public void mouseMove(MouseEvent e) {
			leftTreeViewer.getTree().redraw();
			rightTreeViewer.getTree().redraw();
		}

		@Override
		public void treeCollapsed(TreeExpansionEvent event) {
			redrawOtherTreeViewer(event.getTreeViewer());
		}

		@Override
		public void treeExpanded(TreeExpansionEvent event) {
			redrawOtherTreeViewer(event.getTreeViewer());
		}

		/**
		 * redraws the other {@link TreeViewer}.
		 * 
		 * @param originatingTreeViewer
		 *            the {@link TreeViewer} not to redraw.
		 */
		private void redrawOtherTreeViewer(AbstractTreeViewer originatingTreeViewer) {
			if (originatingTreeViewer == leftTreeViewer) {
				rightTreeViewer.getTree().redraw();
			} else if (originatingTreeViewer == rightTreeViewer) {
				leftTreeViewer.getTree().redraw();
			}
		}

	}

	/**
	 * An {@link ITreeViewerListener}, {@link SelectionListener} and
	 * {@link MouseMoveListener} that redraws the center sash to ensure the
	 * proper redrawing of the diff visualization.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class SashRedrawer implements ITreeViewerListener, SelectionListener, MouseMoveListener {

		@Override
		public void treeCollapsed(TreeExpansionEvent event) {
			centerSash.redraw();
		}

		@Override
		public void treeExpanded(TreeExpansionEvent event) {
			centerSash.redraw();
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			centerSash.redraw();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// intentionally left empty

		}

		@Override
		public void mouseMove(MouseEvent e) {
			centerSash.redraw();
		}

	}

	/**
	 * The {@link ITreeContentProvider} for the left and right TreeViewers that
	 * allows filtering of {@link TreeDiffItem}s based on their type.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class TreeDiffContentProvider implements ITreeContentProvider {

		private TreeDiffType[] typesToDisplay;

		/**
		 * @param typesToDisplay
		 *            the set of types to display.
		 */
		public TreeDiffContentProvider(TreeDiffType[] typesToDisplay) {
			this.typesToDisplay = typesToDisplay;
		}

		@Override
		public void dispose() {
			// Intentionally left empty
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// Intentionally left empty
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Collection<?>) {
				return filterTypes(((Collection<?>) inputElement)).toArray();
			}
			return new Object[0];
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof TreeDiffItem) {
				return filterTypes(((TreeDiffItem) parentElement).getChildren()).toArray();
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof TreeDiffItem) {
				return ((TreeDiffItem) element).getParent();
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof TreeDiffItem) {
				return !filterTypes(((TreeDiffItem) element).getChildren()).isEmpty();
			}
			return false;
		}

		/**
		 * filters all {@link TreeDiffItem}s that should not be displayed from
		 * the given collection.
		 * 
		 * @param collection
		 *            the collection to filter.
		 * @return a new filtered collection containing the not filtered items
		 *         of the given collection.
		 */
		private Collection<?> filterTypes(Collection<?> collection) {
			List<TreeDiffItem> result = new ArrayList<TreeDiffItem>(collection.size());
			for (Object element : collection) {
				if (element instanceof TreeDiffItem && isOfType((TreeDiffItem) element, typesToDisplay)) {
					result.add((TreeDiffItem) element);
				}
			}
			return result;
		}

		/**
		 * @param typesToDisplay
		 *            the types that should be displayed.
		 */
		public void setTypesToDisplay(TreeDiffType[] typesToDisplay) {
			this.typesToDisplay = typesToDisplay;
		}
	}

	/**
	 * The label provider for the left and right tree viewers that colors the
	 * tree items based on their {@link TreeDiffType}s.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class TreeDiffItemSideLabelProvider extends StyledCellLabelProvider {

		private TreeDiffSide side;

		/**
		 * @param side
		 *            the corresponding {@link TreeDiffSide} of the
		 *            {@link TreeViewer} that this label provider should provide
		 *            labels for.
		 */
		public TreeDiffItemSideLabelProvider(TreeDiffSide side) {
			this.side = side;
		}

		@Override
		public void update(ViewerCell cell) {

			Object element = cell.getElement();
			if (element instanceof TreeDiffItem) {

				final TreeDiffItem item = (TreeDiffItem) element;
				TreeDiffSideItem diffItemSide = getDiffItemSide(item);

				if (diffItemSide != null) {

					StyledString labelText = new StyledString();
					labelText.append(diffItemSide.getLabel(), new DiffTypeStyler(item.getTreeDiffType()));

					/* add the diff type counts for non-equal items */
					DiffTypeCount diffTypeCount = calculateChildDiffCount(item);
					if (!diffTypeCount.hasOnlyEqualChildDiffs()) {
						labelText.append(" ");
						for (TreeDiffType diffType : TreeDiffType.getValuesExcluding(TreeDiffType.EQUAL)) {
							int count = diffTypeCount.getCount(diffType);
							if (count > 0) {
								labelText.append(MessageFormat.format(" ({0}) ", count),
										new DiffTypeCountStyler(diffType));
							}
						}
					}

					cell.setText(labelText.getString());
					cell.setStyleRanges(labelText.getStyleRanges());

					Image image = diffItemSide.getImage();
					if (image != null) {
						cell.setImage(image);
					}

				}

			}

			super.update(cell);

		}

		/**
		 * calculates the count of each type in the child hierarchy of the given
		 * {@link TreeDiffItem}.
		 * 
		 * @param item
		 *            the item to calculate the counts for.
		 * @return a {@link DiffTypeCount} that stores the calculated counts for
		 *         each {@link TreeDiffType} in the child hierarchy of the given
		 *         {@link TreeDiffItem}.
		 */
		private DiffTypeCount calculateChildDiffCount(TreeDiffItem item) {

			DiffTypeCount diffTypeCount = new DiffTypeCount();
			List<TreeDiffItem> children = item.getChildren();
			for (TreeDiffItem child : children) {
				updateChildDiffCount(child, diffTypeCount);
			}
			return diffTypeCount;
		}

		/**
		 * updates the given {@link DiffTypeCount} with the {@link TreeDiffType}
		 * counts of the given item and its child hierarchy.
		 * 
		 * @param item
		 *            the item to update the counts for.
		 * @param childItemDiffTypeCount
		 *            the {@link DiffTypeCount} to store the counts in.
		 */
		private void updateChildDiffCount(TreeDiffItem item, DiffTypeCount childItemDiffTypeCount) {

			childItemDiffTypeCount.increment(item.getTreeDiffType());
			List<TreeDiffItem> children = item.getChildren();
			for (TreeDiffItem child : children) {
				updateChildDiffCount(child, childItemDiffTypeCount);
			}
		}

		/**
		 * returns the {@link TreeDiffSideItem} based on the configured
		 * {@link TreeDiffSide}.
		 * 
		 * @param item
		 *            the item to retrieve the side for.
		 * @return the {@link TreeDiffSideItem} of the given item based on the
		 *         current {@link TreeDiffSide}.
		 */
		private TreeDiffSideItem getDiffItemSide(TreeDiffItem item) {

			if (side == TreeDiffSide.LEFT) {
				return item.getLeftSideItem();
			} else {
				return item.getRightSideItem();
			}

		}

		/**
		 * A helper class that stores the counts of {@link TreeDiffType}. The
		 * count for each {@link TreeDiffType} is 0 by default.
		 * 
		 * @author Florian Zoubek
		 *
		 */
		private class DiffTypeCount {

			private EnumMap<TreeDiffType, Integer> countData = new EnumMap<>(TreeDiffType.class);

			public DiffTypeCount() {

				for (TreeDiffType type : TreeDiffType.values()) {
					countData.put(type, 0);
				}
			}

			/**
			 * increments the count of the given TreeDiffType by 1.
			 * 
			 * @param treeDiffType
			 *            the {@link TreeDiffType} to increment the count for.
			 */
			public void increment(TreeDiffType treeDiffType) {
				int count = countData.get(treeDiffType);
				count++;
				countData.put(treeDiffType, count);
			}

			/**
			 * @param treeDiffType
			 *            the {@link TreeDiffType} to retrieve the count for.
			 * @return the current count for the given {@link TreeDiffType}.
			 */
			public int getCount(TreeDiffType treeDiffType) {
				return countData.get(treeDiffType);
			}

			/**
			 * @return true if the counts of all non equal {@link TreeDiffType}s
			 *         is 0, false otherwise.
			 */
			public boolean hasOnlyEqualChildDiffs() {

				for (TreeDiffType type : TreeDiffType.values()) {
					if (type != TreeDiffType.EQUAL && countData.get(type) > 0) {
						return false;
					}
				}
				return true;
			}

		}

		/**
		 * A {@link Styler} that applies the default text style for elements
		 * associated with a given {@link TreeDiffType}.
		 * 
		 * @author Florian Zoubek
		 *
		 */
		private class DiffTypeStyler extends Styler {

			private TreeDiffType type;

			public DiffTypeStyler(TreeDiffType type) {
				this.type = type;
			}

			@Override
			public void applyStyles(TextStyle textStyle) {
				if (type != TreeDiffType.EQUAL) {
					Color diffColor = getDiffColor(type);
					textStyle.foreground = diffColor;
				}
			}

		}

		/**
		 * A {@link Styler} that applies the counter text style for counts
		 * associated with a given {@link TreeDiffType}.
		 * 
		 * @author Florian Zoubek
		 *
		 */
		private class DiffTypeCountStyler extends Styler {

			private TreeDiffType type;

			public DiffTypeCountStyler(TreeDiffType type) {
				this.type = type;
			}

			@Override
			public void applyStyles(TextStyle textStyle) {

				if (type != TreeDiffType.EQUAL) {

					Color diffColor = getDiffBackgroundColor(type);
					textStyle.background = diffColor;

					double contrast1 = ColorUtil.calculateContrastRatio(diffColor.getRGB(), fgColor1.getRGB());
					double contrast2 = ColorUtil.calculateContrastRatio(diffColor.getRGB(), fgColor2.getRGB());

					if (contrast1 > contrast2) {
						textStyle.foreground = fgColor1;
					} else {
						textStyle.foreground = fgColor2;
					}
				}
			}
		}
	}

	/**
	 * sets the root {@link TreeDiffItem}s to display and refreshes the widget.
	 * 
	 * @param items
	 *            the root {@link TreeDiffItem} to set.
	 */
	public void setItemData(List<TreeDiffItem> items) {

		removeAllItemData();
		for (TreeDiffItem item : items) {
			appendItemData(item);
		}
		refreshData();

	}

	/**
	 * appends the given item to the already existing root {@link TreeDiffItems}
	 * and refreshes the widget.
	 * 
	 * @param item
	 *            the item to append.
	 */
	public void appendItem(TreeDiffItem item) {

		appendItemData(item);
		refreshData();
	}

	/**
	 * appends the given item to the already existing root {@link TreeDiffItems}
	 * 
	 * @param item
	 *            the item to append.
	 */
	protected void appendItemData(TreeDiffItem item) {

		rootDiffItems.add(item);
	}

	/**
	 * removes the given item from the already existing root
	 * {@link TreeDiffItems} and refreshes the widget.
	 * 
	 * @param item
	 *            the item to remove.
	 */
	public void removeItem(TreeDiffItem item) {

		removeItemData(item);
		refreshData();
	}

	/**
	 * removes the given item from the already existing root
	 * {@link TreeDiffItems}.
	 * 
	 * @param item
	 *            the item to remove.
	 */
	protected void removeItemData(TreeDiffItem item) {

		rootDiffItems.remove(item);
	}

	/**
	 * removes the all items from the already existing root
	 * {@link TreeDiffItems} and refreshes the widget.
	 */
	public void removeAllItems() {

		removeAllItemData();
		refreshData();
	}

	/**
	 * removes the all items from the already existing root
	 * {@link TreeDiffItems}.
	 */
	protected void removeAllItemData() {

		rootDiffItems.clear();
	}

	/**
	 * refreshes the widget data.
	 */
	protected void refreshData() {

		leftTreeViewer.refresh();
		rightTreeViewer.refresh();
		centerSash.redraw();
		redraw();
	}

	/**
	 * sets the side which will be considered as changed. For example, elements
	 * that are present on the left side but not on the right side will be
	 * considered as deleted if the changed side is set to
	 * {@link TreeDiffSide#RIGHT} and as deleted if the changed side is set to
	 * {@link TreeDiffSide#LEFT}. The changed side is set to
	 * {@link TreeDiffSide#RIGHT} by default.
	 * 
	 * @param changedSide
	 *            the changed side.
	 */
	public void setChangedSide(TreeDiffSide changedSide) {
		this.changedSide = changedSide;
		if (changedSide == TreeDiffSide.RIGHT) {

			leftTreeDiffContentProvider.setTypesToDisplay(TreeDiffType.getValuesExcluding(TreeDiffType.ADD));
			rightTreeDiffContentProvider.setTypesToDisplay(TreeDiffType.getValuesExcluding(TreeDiffType.DELETE));

		} else {

			leftTreeDiffContentProvider.setTypesToDisplay(TreeDiffType.getValuesExcluding(TreeDiffType.DELETE));
			rightTreeDiffContentProvider.setTypesToDisplay(TreeDiffType.getValuesExcluding(TreeDiffType.ADD));
		}
		refreshData();
	}

	/**
	 * @return the side that is considered as changed.
	 * @see #setChangedSide(TreeDiffSide)
	 */
	public TreeDiffSide getChangedSide() {
		return changedSide;
	}

}

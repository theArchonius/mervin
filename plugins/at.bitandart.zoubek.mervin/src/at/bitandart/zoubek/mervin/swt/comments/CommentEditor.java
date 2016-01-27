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
package at.bitandart.zoubek.mervin.swt.comments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import at.bitandart.zoubek.mervin.swt.comments.data.CommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLinkTarget;

/**
 * A control that allows editing a comment. A comment contains text where parts
 * of this text link to some target. This control uses internally various
 * {@link GridLayout} instances in favor of {@link TableWrapLayout} for
 * performance reasons. As a side effect, it does not support wrapping of its
 * content if one of its parents is a subclass of {@link ScrolledForm} or
 * {@link ExpandableComposite}.
 * 
 * To allow creation of links for a specified range, a link target must be
 * specified using {@link #setCurrentLinkTarget(ICommentLinkTarget)}. If the
 * length of the selection range is smaller than 1, the default text of the link
 * target will inserted as link text.
 * 
 * @author Florian Zoubek
 *
 */
public class CommentEditor extends Composite {

	/**
	 * the current link target that should be applied if a new link is created.
	 */
	private ICommentLinkTarget currentLinkTarget;

	// Listeners

	private List<CommentEditorListener> commentEditorListeners = new ArrayList<CommentEditor.CommentEditorListener>();

	private CommentLineStyleListener commentLineStyleListener;

	// SWT Controls

	private StyledText commentInput;
	private Button addLinkButton;
	private Button removeLinkButton;
	private Button saveButton;

	public CommentEditor(Composite parent, int style, FormToolkit toolkit) {

		super(parent, style);
		toolkit.adapt(this);
		this.setLayout(new GridLayout());

		commentInput = new StyledText(this, SWT.WRAP | SWT.BORDER);
		commentInput.setText("");
		toolkit.adapt(commentInput);
		GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		commentInput.setLayoutData(gridData);

		commentInput.addExtendedModifyListener(new ExtendedModifyListener() {

			@Override
			public void modifyText(ExtendedModifyEvent event) {

				/*
				 * update the comment links based on the input
				 */
				int offset = event.length - event.replacedText.length();
				for (ICommentLink link : commentLineStyleListener.getCommentLinks()) {
					int linkStart = link.getStartIndex();
					int linkLength = link.getLength();
					if (linkStart > event.start) {
						link.setStartIndex(linkStart + offset);
					} else if (RangeUtil.doRangesOverlap(event.start, event.length, linkStart, linkLength)) {
						link.setLength(linkLength + offset);
					}
				}
				commentInput.redraw();

				/*
				 * make sure the comment input grows and shrinks based on its
				 * current content. The layout method of the parent of the
				 * editor control must called as it may provide more space for
				 * the whole editor control.
				 */
				getShell().layout(true, true);

			}
		});

		commentLineStyleListener = new CommentLineStyleListener();
		commentInput.addLineStyleListener(commentLineStyleListener);

		/*
		 * add a caret listener to enable the remove link button based on the
		 * current caret position. Note that this listener only uses the caret
		 * offset, selections are handled by the selection listener as
		 * getSelectionRange() seems to be updated only after the caret listener
		 * is called.
		 */
		commentInput.addCaretListener(new CaretListener() {

			@Override
			public void caretMoved(CaretEvent event) {

				boolean enableRemoveButton = false;
				for (ICommentLink link : commentLineStyleListener.getCommentLinks()) {
					if (RangeUtil.doRangesOverlap(event.caretOffset, 0, link.getStartIndex(), link.getLength())) {
						enableRemoveButton = true;
						break;
					}
				}
				removeLinkButton.setEnabled(enableRemoveButton);
			}
		});

		/*
		 * add a caret listener to enable the remove link button based on the
		 * current selection range.
		 */
		commentInput.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!removeLinkButton.isEnabled()) {
					Point selectionRange = commentInput.getSelectionRange();
					int offset = selectionRange.x;
					int length = selectionRange.y;

					for (ICommentLink link : commentLineStyleListener.getCommentLinks()) {
						if (RangeUtil.doRangesOverlap(offset, length, link.getStartIndex(), link.getLength())) {
							removeLinkButton.setEnabled(true);
							break;
						}
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Intentionally left empty
			}
		});

		Composite buttonPanel = toolkit.createComposite(this, SWT.BORDER);
		buttonPanel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		buttonPanel.setLayout(new GridLayout(3, false));

		addLinkButton = toolkit.createButton(buttonPanel, "add Link", SWT.PUSH);
		addLinkButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		addLinkButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (currentLinkTarget != null) {

					Point selectionRange = commentInput.getSelectionRange();
					int offset = selectionRange.x;
					int length = selectionRange.y;
					if (selectionRange.y < 1) {

						String defaultLinkText = currentLinkTarget.getDefaultText();
						length = defaultLinkText.length();
						commentInput.insert(defaultLinkText);

					}
					InternalCommentLink commentLink = new InternalCommentLink(offset, length, currentLinkTarget);
					addCommentLink(commentLink);

				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// left intentionally empty
			}
		});

		removeLinkButton = toolkit.createButton(buttonPanel, "remove Link", SWT.PUSH);
		removeLinkButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		removeLinkButton.setEnabled(false);
		removeLinkButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Point selectionRange = commentInput.getSelectionRange();
				int offset = selectionRange.x;
				int length = selectionRange.y;

				ListIterator<ICommentLink> linkIterator = commentLineStyleListener.getCommentLinks().listIterator();

				while (linkIterator.hasNext()) {
					ICommentLink link = linkIterator.next();
					if (RangeUtil.doRangesOverlap(offset, length, link.getStartIndex(), link.getLength())) {
						linkIterator.remove();
					}
				}
				commentInput.redraw();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// left intentionally empty
			}
		});

		saveButton = toolkit.createButton(buttonPanel, "Save", SWT.NONE);
		saveButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		saveButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				notifySave();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Intentionally left empty
			}
		});

		refreshAddLinkButton();

	}

	/**
	 * notifies all registered {@link CommentEditorListener}s of a save event.
	 */
	private void notifySave() {
		for (CommentEditorListener commentEditorListener : commentEditorListeners) {
			commentEditorListener.doSave(this);
		}
	}

	/**
	 * refreshes the controls children
	 */
	private void refresh() {

		refreshAddLinkButton();

	}

	/**
	 * refreshes the "add link" button.
	 */
	private void refreshAddLinkButton() {
		if (!isDisposed()) {
			addLinkButton.setEnabled(currentLinkTarget != null);
		}
	}

	/**
	 * removes all current data from this control and refreshes the state of its
	 * children.
	 */
	public void clearData() {

		currentLinkTarget = null;
		commentInput.setText("");
		commentInput.setStyleRanges(new StyleRange[0]);

		refresh();

	}

	/**
	 * sets the current link target.
	 * 
	 * @param currentLinkTarget
	 */
	public void setCurrentLinkTarget(ICommentLinkTarget currentLinkTarget) {

		this.currentLinkTarget = currentLinkTarget;
		refreshAddLinkButton();

	}

	/**
	 * sets the raw text of the currently edited comment and removes all current
	 * {@link ICommentLink}s from this control.
	 * 
	 * @param text
	 *            the raw text to set.
	 */
	public void setText(String text) {
		commentInput.setText(text);
		commentInput.setStyleRanges(new StyleRange[0]);
	}

	/**
	 * @return the raw text of the comment.
	 */
	public String getText() {

		return commentInput.getText();
	}

	/**
	 * adds the given comment link to the currently edited comment.
	 * 
	 * @param commentLink
	 *            the comment link to add.
	 */
	public void addCommentLink(ICommentLink commentLink) {

		List<ICommentLink> commentLinks = commentLineStyleListener.getCommentLinks();

		commentLinks.add(commentLink);
		/*
		 * sort the comment links by their start index
		 */
		Collections.sort(commentLinks, new Comparator<ICommentLink>() {

			@Override
			public int compare(ICommentLink link1, ICommentLink link2) {
				return link1.getStartIndex() - link2.getStartIndex();
			}
		});
		commentInput.redraw();

	}

	/**
	 * @return an unmodifiable list of the current comment links stored in this
	 *         control.
	 */
	public List<ICommentLink> getCommentLinks() {

		return Collections.unmodifiableList(commentLineStyleListener.getCommentLinks());
	}

	/**
	 * registers the given comment editor listener to this control.
	 * 
	 * @param commentEditorListener
	 *            the listener to register.
	 */
	public void addCommentEditorListener(CommentEditorListener commentEditorListener) {
		commentEditorListeners.add(commentEditorListener);
	}

	/**
	 * removes the given comment editor listener from this control. Does nothing
	 * if the listener has not been registered to this control.
	 * 
	 * @param commentEditorListener
	 *            the listener to register.
	 */
	public void removeCommentEditorListener(CommentEditorListener commentEditorListener) {
		commentEditorListeners.remove(commentEditorListener);
	}

	/**
	 * Base interface for a listener that handles events of
	 * {@link CommentEditor} instances.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public static interface CommentEditorListener {

		/**
		 * called if the save button of the comment editor has been pressed.
		 * 
		 * @param commentEditor
		 *            the comment editor which save button has been pressed.
		 */
		public void doSave(CommentEditor commentEditor);

	}

	/**
	 * A simple {@link ICommentLink} implementation used to create new
	 * CommentLinks in this editor.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class InternalCommentLink extends CommentLink {

		public InternalCommentLink(int startIndex, int length, ICommentLinkTarget commentLinkTarget) {
			super(startIndex, length, commentLinkTarget);
		}

	}

}

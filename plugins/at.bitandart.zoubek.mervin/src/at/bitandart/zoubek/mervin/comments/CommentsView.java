/*******************************************************************************
 * Copyright (c) 2015, 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.comments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.bitandart.zoubek.mervin.IReviewHighlightService;
import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.CommentLink;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.User;
import at.bitandart.zoubek.mervin.review.ModelReviewEditorTrackingView;
import at.bitandart.zoubek.mervin.swt.comments.CommentList;
import at.bitandart.zoubek.mervin.swt.comments.CommentList.CommentLinkListener;
import at.bitandart.zoubek.mervin.swt.comments.CommentList.CommentModifyListener;
import at.bitandart.zoubek.mervin.swt.comments.CommentListViewer;
import at.bitandart.zoubek.mervin.swt.comments.data.IComment;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentColumn;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLink;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentLinkTarget;
import at.bitandart.zoubek.mervin.swt.comments.data.ICommentProvider;

/**
 * A view that shows the comments of the loaded model review of the currently
 * active editor.
 * 
 * <p>
 * This class adapts to the following classes:
 * <ul>
 * <li>{@link ModelReview} - the current model review instance</li>
 * </ul>
 * </p>
 * 
 * @author Florian Zoubek
 *
 */
public class CommentsView extends ModelReviewEditorTrackingView implements IAdaptable {

	public static final String PART_DESCRIPTOR_ID = "at.bitandart.zoubek.mervin.partdescriptor.comments";

	/**
	 * the comment provider used to load the comments from the mervin model
	 */
	@Inject
	private ICommentProvider commentProvider;

	@Inject
	private ModelReviewFactory modelFactory;

	@Inject
	private IReviewHighlightService reviewHighlightService;

	@Inject
	private User currentUser;

	@Inject
	private Display display;

	// Viewers
	private CommentListViewer commentListViewer;

	// Data
	private boolean viewInitialized = false;

	/**
	 * content adapter used to notify this view of updates to the current review
	 * model
	 */
	private UpdateCommentViewAdapter commentViewUpdater = new UpdateCommentViewAdapter();

	@Inject
	public CommentsView() {
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		parent.setLayout(new FillLayout());

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());

		commentListViewer = new CommentListViewer(parent, toolkit, SWT.V_SCROLL);
		commentListViewer.setCommentProvider(commentProvider);
		CommentList control = commentListViewer.getCommentListControl();
		control.addCommentLinkListener(new CommentLinkListener() {

			@Override
			public void commentLinkClicked(ICommentLink commentLink) {
				// Intentionally left empty
			}

			@Override
			public void commentLinkEnter(ICommentLink commentLink) {

				ICommentLinkTarget commentLinkTarget = commentLink.getCommentLinkTarget();
				ModelReview modelReview = getCurrentModelReview();

				if (modelReview != null && commentLinkTarget instanceof MervinCommentLinkTarget) {
					List<EObject> targets = ((MervinCommentLinkTarget) commentLinkTarget).getTargets();

					for (EObject target : targets) {
						reviewHighlightService.addHighlightFor(modelReview, target);
					}
				}

			}

			@Override
			public void commentLinkExit(ICommentLink commentLink) {

				ICommentLinkTarget commentLinkTarget = commentLink.getCommentLinkTarget();
				ModelReview modelReview = getCurrentModelReview();

				if (modelReview != null && commentLinkTarget instanceof MervinCommentLinkTarget) {
					List<EObject> targets = ((MervinCommentLinkTarget) commentLinkTarget).getTargets();

					for (EObject target : targets) {
						reviewHighlightService.removeHighlightFor(modelReview, target);
					}
				}

			}

		});
		control.addCommentModifyListener(new CommentModifyListener() {

			@Override
			public void commentAdded(String text, List<ICommentLink> commentLinks, ICommentColumn commentColumn,
					IComment answerdComment) {

				ModelReview currentModelReview = getCurrentModelReview();
				if (currentModelReview != null) {

					Comment comment = modelFactory.createComment();
					comment.setText(text);
					comment.setAuthor(currentUser);
					comment.setCreationTime(System.currentTimeMillis());

					if (answerdComment != null && answerdComment instanceof MervinComment) {
						comment.setRepliedTo(((MervinComment) answerdComment).getRealComment());
					}

					if (commentColumn instanceof PatchSetColumn) {
						PatchSet patchSet = ((PatchSetColumn) commentColumn).getPatchSet();
						if (patchSet != null) {
							comment.setPatchset(patchSet);
						}
					}

					for (ICommentLink commentLink : commentLinks) {

						ICommentLinkTarget commentLinkTarget = commentLink.getCommentLinkTarget();
						if (commentLinkTarget instanceof MervinCommentLinkTarget) {
							CommentLink realCommentLink = modelFactory.createCommentLink();
							realCommentLink.setStart(commentLink.getStartIndex());
							realCommentLink.setLength(commentLink.getLength());
							realCommentLink.setComment(comment);
							realCommentLink.getTargets()
									.addAll(((MervinCommentLinkTarget) commentLinkTarget).getTargets());
						}

					}

					currentModelReview.getComments().add(comment);

					commentListViewer.refresh();
				}

			}
		});

		viewInitialized = true;
		updateValues();

	}

	@Inject
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional IStructuredSelection selection) {

		if (viewInitialized && selection != null) {

			List<EObject> targets = extractLinkTargets(selection);
			CommentList commentListControl = commentListViewer.getCommentListControl();
			if (!targets.isEmpty()) {
				commentListControl.setCurrentLinkTarget(new MervinCommentLinkTarget(targets));
			}

		}

	}

	/**
	 * extracts all EObject link targets from the given selection.
	 * 
	 * @param selection
	 *            the selection containing the link targets.
	 * @return a list of EObject link targets, does not return null.
	 */
	private List<EObject> extractLinkTargets(IStructuredSelection selection) {

		List<EObject> targets = new ArrayList<EObject>(selection.size());

		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {

			Object element = iterator.next();
			if (element instanceof EObject) {

				targets.add((EObject) element);

			} else if (element instanceof GraphicalEditPart) {

				// GMF edit part -> use semantic model
				EObject semanticElement = ((GraphicalEditPart) element).resolveSemanticElement();
				targets.add(extractFromTemporaryContainer(semanticElement));

			} else if (element instanceof ConnectionEditPart) {

				// GMF edit part -> use semantic model
				EObject semanticElement = ((ConnectionEditPart) element).resolveSemanticElement();
				targets.add(extractFromTemporaryContainer(semanticElement));

			} else if (element instanceof EditPart) {

				// GEF edit part -> use model if it is an EObject
				Object model = ((EditPart) element).getModel();
				if (model instanceof EObject) {
					targets.add((EObject) model);
				}

			}

		}

		return targets;
	}

	/**
	 * extracts the underlying {@link EObject} from any given temporary
	 * container like {@link DifferenceOverlay}. If the given object is not a
	 * temporary container the object is returned again.
	 * 
	 * @param object
	 *            the object that may be an temporary container.
	 * @return the underlying {@link EObject}, never returns null.
	 */
	private EObject extractFromTemporaryContainer(EObject object) {

		if (object instanceof DifferenceOverlay) {

			View linkedView = ((DifferenceOverlay) object).getLinkedView();
			EObject semanticElement = linkedView.getElement();

			if (semanticElement != null) {
				return semanticElement;
			} else {
				return linkedView;
			}

		}

		/*
		 * object is not contained in a temporary container, so return it again.
		 */
		return object;
	}

	@Focus
	public void onFocus() {
		if (commentListViewer != null) {
			commentListViewer.getControl().setFocus();
		}
	}

	@Override
	protected void updateValues() {

		if (!viewInitialized) {
			return;
		}
		ModelReview currentModelReview = getCurrentModelReview();
		if (currentModelReview != null) {
			if (!currentModelReview.eAdapters().contains(commentViewUpdater)) {
				currentModelReview.eAdapters().add(commentViewUpdater);
			}
		}
		commentListViewer.setInput(currentModelReview);
		commentListViewer.refresh();
	}

	@Override
	public <T> T getAdapter(Class<T> adapterType) {

		if (adapterType == ModelReview.class) {
			return adapterType.cast(getCurrentModelReview());
		}
		return null;
	}

	/**
	 * {@link EContentAdapter} that notifies this view about changes in the
	 * model.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class UpdateCommentViewAdapter extends EContentAdapter {

		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			display.syncExec(new Runnable() {

				@Override
				public void run() {
					updateValues();
				}
			});
		}

	}

}
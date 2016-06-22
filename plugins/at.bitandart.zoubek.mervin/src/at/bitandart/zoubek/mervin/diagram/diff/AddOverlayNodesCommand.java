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
package at.bitandart.zoubek.mervin.diagram.diff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Location;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.Size;
import org.eclipse.gmf.runtime.notation.View;

import com.google.common.collect.BiMap;

import at.bitandart.zoubek.mervin.diagram.diff.gmf.ModelReviewElementTypes;
import at.bitandart.zoubek.mervin.draw2d.DoublePrecisionVector;
import at.bitandart.zoubek.mervin.model.modelreview.BendpointsDifference;
import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.CommentLink;
import at.bitandart.zoubek.mervin.model.modelreview.Difference;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.DimensionChange;
import at.bitandart.zoubek.mervin.model.modelreview.LocationDifference;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.SizeDifference;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifference;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifferenceType;

/**
 * An {@link AbstractTransactionalCommand} that adds overlays based on the
 * differences in a given two way comparison for a given number of {@link View}
 * s.
 * 
 * @author Florian Zoubek
 *
 */
class AddOverlayNodesCommand extends AbstractTransactionalCommand {

	private Comparison diagramComparison;
	private View container;
	private BiMap<EObject, EObject> inverseCopyMap;
	private PreferencesHint preferencesHint;
	private ModelReviewFactory reviewFactory;
	private Collection<Object> overlayedViews;
	private ModelReview modelReview;
	private Set<EObject> cachedLinkTargets;

	public AddOverlayNodesCommand(TransactionalEditingDomain domain, Comparison diagramComparison,
			BiMap<EObject, EObject> copyMap, View container, Collection<Object> overlayedViews,
			PreferencesHint preferencesHint, ModelReviewFactory reviewFactory, ModelReview modelReview) {
		super(domain, "", null);
		this.diagramComparison = diagramComparison;
		this.container = container;
		this.inverseCopyMap = copyMap.inverse();
		this.preferencesHint = preferencesHint;
		this.reviewFactory = reviewFactory;
		this.overlayedViews = overlayedViews;
		this.modelReview = modelReview;
	}

	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		cachedLinkTargets = collectLinkTargets();

		for (Object child : overlayedViews) {

			if (child instanceof View) {
				View childView = (View) child;
				createOverlayForView(childView, true);
			}
		}

		return CommandResult.newOKCommandResult();
	}

	/**
	 * creates an overlay for the given view and its child views if specified.
	 * If the view has been added or deleted, all child views will be ignored
	 * regardless of the value of the {@code includeChildren} parameter.
	 * 
	 * @param view
	 *            the view to create the overlay for.
	 * @param includeChildren
	 *            true if overlays should be created also for child views, if
	 *            the view has not been added or deleted.
	 */
	private void createOverlayForView(View view, boolean includeChildren) {

		View originalView = (View) inverseCopyMap.get(view);
		if (originalView == null) {
			/*
			 * FIXME quick fix to avoid NPE - needs further investigation this
			 * should not happen as all copied views should have an original
			 * version
			 */
			return;
		}

		/*
		 * find referencing differences and search for the ReferenceChange that
		 * describes a modification of a node's children or a diagrams edges
		 */
		EList<Diff> referencingDifferences = diagramComparison.getDifferences(originalView);
		ReferenceChange viewReferenceChange = null;
		for (Diff difference : referencingDifferences) {
			if (difference instanceof ReferenceChange) {
				EReference reference = ((ReferenceChange) difference).getReference();
				if (reference == NotationPackage.Literals.VIEW__PERSISTED_CHILDREN
						|| reference == NotationPackage.Literals.VIEW__TRANSIENT_CHILDREN
						|| reference == NotationPackage.Literals.DIAGRAM__PERSISTED_EDGES
						|| reference == NotationPackage.Literals.DIAGRAM__TRANSIENT_EDGES) {
					viewReferenceChange = (ReferenceChange) difference;
					break;
				}
			}
		}

		/*
		 * Get all matches that might contain differences that should be part of
		 * an overlay
		 */
		Match viewElementMatch = diagramComparison.getMatch(view.getElement());
		Match layoutConstraintMatch = null;
		Match bendpointsMatch = null;

		if (view instanceof Node) {
			Object layoutConstraint = originalView.eGet(NotationPackage.Literals.NODE__LAYOUT_CONSTRAINT);
			if (layoutConstraint instanceof EObject) {
				layoutConstraintMatch = diagramComparison.getMatch((EObject) layoutConstraint);
			}
		}

		if (view instanceof Edge) {
			Object bendpoints = originalView.eGet(NotationPackage.Literals.EDGE__BENDPOINTS);
			if (bendpoints instanceof EObject) {
				bendpointsMatch = diagramComparison.getMatch((EObject) bendpoints);
			}
		}

		// extract the differences
		EList<Diff> layoutConstraintDifferences = null;
		EList<Diff> bendpointDifferences = null;
		EList<Diff> viewElementDifferences = null;

		if (layoutConstraintMatch != null) {
			layoutConstraintDifferences = layoutConstraintMatch.getDifferences();
		}
		if (bendpointsMatch != null) {
			bendpointDifferences = bendpointsMatch.getDifferences();
		}
		boolean hasComments = false;
		boolean hasChildComments = false;
		if (viewElementMatch != null) {
			viewElementDifferences = viewElementMatch.getDifferences();
			hasComments = containsTargetsOfCommentLinks(viewElementMatch);
			hasChildComments = containSubmatchesTargetsOfCommentLinks(viewElementMatch);
		}

		boolean hasViewReferenceDifferences = viewReferenceChange != null;
		boolean hasLayoutConstraintDifferences = (layoutConstraintDifferences != null
				&& !layoutConstraintDifferences.isEmpty());
		boolean hasBendpointsDifferences = (bendpointDifferences != null && !bendpointDifferences.isEmpty());
		boolean hasViewElementDifferences = (viewElementDifferences != null && !viewElementDifferences.isEmpty());
		boolean ignoreChildren = false;

		if (hasViewReferenceDifferences || hasLayoutConstraintDifferences || hasBendpointsDifferences
				|| hasViewElementDifferences || hasComments || hasChildComments) {

			// create an overlay depending on the view type
			DifferenceOverlay differenceOverlay = null;
			String type = null;
			if (view instanceof Edge) {
				differenceOverlay = reviewFactory.createEdgeDifferenceOverlay();
				type = ModelReviewElementTypes.OVERLAY_DIFFERENCE_EDGE_SEMANTIC_HINT;
			} else {
				differenceOverlay = reviewFactory.createNodeDifferenceOverlay();
				type = ModelReviewElementTypes.OVERLAY_DIFFERENCE_NODE_SEMANTIC_HINT;
			}
			differenceOverlay.setLinkedView(view);

			// Determine actual differences

			if (hasLayoutConstraintDifferences) {
				handleLayoutConstraintDifferences(layoutConstraintMatch, differenceOverlay);
			}

			if (hasBendpointsDifferences) {
				handleBendpointDifferences(bendpointsMatch, differenceOverlay);
			}

			/*
			 * determine state difference type based on the reference change of
			 * the view in its parent
			 */

			if (hasViewReferenceDifferences) {

				StateDifference stateDifference = reviewFactory.createStateDifference();
				stateDifference.getRawDiffs().add(viewReferenceChange);
				StateDifferenceType stateDifferenceType = toStateDifferenceType(viewReferenceChange.getKind());
				stateDifference.setType(stateDifferenceType);
				differenceOverlay.getDifferences().add(stateDifference);
				/*
				 * all child views must have the same difference type or are
				 * unchanged if the type is an addition or deletion - this done
				 * to avoid distracting multiple overlays for a addition or
				 * deletion of a view containing nested views
				 */
				ignoreChildren = stateDifferenceType == StateDifferenceType.ADDED
						|| stateDifferenceType == StateDifferenceType.DELETED;

			}

			differenceOverlay.setCommented(hasComments);

			if (!hasComments && ignoreChildren && hasChildComments) {
				differenceOverlay.setCommented(hasChildComments);
			}

			if (!differenceOverlay.getDifferences().isEmpty()) {

				/*
				 * found review relevant differences, so add the overlay into
				 * the view model
				 */
				ViewService.createNode(container, differenceOverlay, type, preferencesHint);

			}

		}

		if (includeChildren && !ignoreChildren) {
			for (Object child : view.getChildren()) {
				if (child instanceof View) {
					View childView = (View) child;
					createOverlayForView(childView, true);
				}
			}
		}
	}

	/**
	 * determines if the left or the right version of the given match have been
	 * set as link target in one of the comments of the review.
	 * 
	 * @param match
	 *            the match containing the EObjects to check.
	 * @return true if the left or the right EObject of the given match have
	 *         been set as link target, false otherwise.
	 */
	private boolean containsTargetsOfCommentLinks(Match match) {

		return cachedLinkTargets.contains(match.getLeft()) || cachedLinkTargets.contains(match.getRight());
	}

	/**
	 * determines if the submatches contain an {@link EObject} that is a target
	 * of a comment link.
	 * 
	 * @param match
	 *            the match containing the submatches to check
	 * @return true if any of the submatches contain an {@link EObject} that is
	 *         a target of a comment link.
	 */
	private boolean containSubmatchesTargetsOfCommentLinks(Match match) {

		for (Match submatch : match.getAllSubmatches()) {
			if (containsTargetsOfCommentLinks(submatch)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * collects all link targets of all comments in the current review and
	 * returns them as set.
	 * 
	 * @return the set of all link targets of all comments of the current
	 *         review.
	 */
	private Set<EObject> collectLinkTargets() {
		Set<EObject> targets = new HashSet<EObject>();
		// cache targets in set for faster lookup
		for (Comment comment : modelReview.getComments()) {
			for (CommentLink link : comment.getCommentLinks()) {
				targets.addAll(link.getTargets());
			}
		}
		return targets;
	}

	/**
	 * handles bendpoint difference of an {@link Edge} by adding
	 * {@link Difference}s to the given difference overlay based on the match of
	 * the edge's bendpoints. This method currently supports only
	 * {@link RelativeBendpoints}
	 * 
	 * @param bendpointMatch
	 *            the match of the bendpoints object
	 * @param differenceOverlay
	 *            the difference overlay to add the differences to
	 */
	private void handleBendpointDifferences(Match bendpointMatch, DifferenceOverlay differenceOverlay) {

		for (Diff difference : bendpointMatch.getDifferences()) {

			if (difference instanceof AttributeChange) {

				AttributeChange attributeChange = (AttributeChange) difference;

				if (attributeChange.getAttribute() == NotationPackage.Literals.RELATIVE_BENDPOINTS__POINTS) {

					BendpointsDifference bendpointsDifference = reviewFactory.createBendpointsDifference();
					bendpointsDifference.getRawDiffs().add(attributeChange);
					differenceOverlay.getDifferences().add(bendpointsDifference);

				}

			}

		}

	}

	/**
	 * Handles layout constraint differences of a {@link Node} by adding
	 * {@link Difference} s to the given difference overlay based on the match
	 * of the node's layout constraint.
	 * 
	 * @param layoutConstraintMatch
	 *            the match of the layout constraint
	 * @param differenceOverlay
	 *            the difference overlay to add the differences to
	 */
	private void handleLayoutConstraintDifferences(Match layoutConstraintMatch, DifferenceOverlay differenceOverlay) {
		EList<Diff> layoutConstraintDifferences = layoutConstraintMatch.getDifferences();
		PrecisionDimension oldSize = null;
		PrecisionDimension newSize = null;
		DoublePrecisionVector oldLocation = null;
		DoublePrecisionVector newLocation = null;
		List<Diff> sizeRawDiffs = new ArrayList<Diff>(2);
		List<Diff> locationRawDiffs = new ArrayList<Diff>(2);

		for (Diff layoutDifference : layoutConstraintDifferences) {

			// TODO update DifferenceOverlay properties based on the
			// differences

			if (layoutDifference instanceof AttributeChange) {

				AttributeChange attributeChange = (AttributeChange) layoutDifference;

				if (attributeChange.getAttribute() == NotationPackage.Literals.LOCATION__X
						|| attributeChange.getAttribute() == NotationPackage.Literals.LOCATION__Y) {

					locationRawDiffs.add(attributeChange);

					if (oldLocation == null) {

						/*
						 * two way comparison is assumed, so left is new, right
						 * is old
						 */
						oldLocation = locationConstraintToVector(layoutConstraintMatch.getRight(), 0.0);
					}

					if (newLocation == null) {

						/*
						 * two way comparison is assumed, so left is new, right
						 * is old
						 */
						newLocation = locationConstraintToVector(layoutConstraintMatch.getLeft(), -1.0);
					}

				} else if (attributeChange.getAttribute() == NotationPackage.Literals.SIZE__HEIGHT
						|| attributeChange.getAttribute() == NotationPackage.Literals.SIZE__WIDTH) {

					sizeRawDiffs.add(attributeChange);

					if (oldSize == null) {

						/*
						 * two way comparison is assumed, so left is new, right
						 * is old
						 */
						oldSize = sizeConstraintToDimension(layoutConstraintMatch.getRight(), -1.0);
					}

					if (newSize == null) {

						/*
						 * two way comparison is assumed, so left is new, right
						 * is old
						 */
						newSize = sizeConstraintToDimension(layoutConstraintMatch.getLeft(), -1.0);
					}
				}
			}
		}

		if (oldLocation != null && newLocation != null) {

			// determine move direction

			DoublePrecisionVector moveDirection = newLocation.getSubtractedDoublePrecisicon(oldLocation);

			if (moveDirection.getLength() != 0) {

				moveDirection.normalize();
				LocationDifference locationDifference = reviewFactory.createLocationDifference();
				locationDifference.getRawDiffs().addAll(locationRawDiffs);
				locationDifference.setMoveDirection(moveDirection);
				locationDifference.setOriginalLocation(oldLocation);

				differenceOverlay.getDifferences().add(locationDifference);

			}

		}

		if (oldSize != null && newSize != null) {

			// determine size differences

			SizeDifference sizeDifference = reviewFactory.createSizeDifference();
			sizeDifference.getRawDiffs().addAll(sizeRawDiffs);
			sizeDifference.setOriginalDimension(oldSize);

			if (oldSize.preciseWidth() < newSize.preciseWidth()) {

				sizeDifference.setWidthChange(DimensionChange.BIGGER);

			} else if (oldSize.preciseWidth() > newSize.preciseWidth()) {

				sizeDifference.setWidthChange(DimensionChange.SMALLER);

			} else {

				sizeDifference.setWidthChange(DimensionChange.UNKNOWN);

			}

			if (oldSize.preciseHeight() < newSize.preciseHeight()) {

				sizeDifference.setHeightChange(DimensionChange.BIGGER);

			} else if (oldSize.preciseHeight() > newSize.preciseHeight()) {

				sizeDifference.setHeightChange(DimensionChange.SMALLER);

			} else {

				sizeDifference.setHeightChange(DimensionChange.UNKNOWN);

			}

			differenceOverlay.getDifferences().add(sizeDifference);

		}
	}

	/**
	 * convenience method to convert the given object to a double value. If the
	 * object cannot be converted, the default value is returned.
	 * 
	 * @param value
	 *            the object to convert.
	 * @param defaultValue
	 *            the default value to return if the conversion is not possible.
	 * @return the converted double value or the default value if the conversion
	 *         was not possible.
	 */
	private static double toDouble(Object value, double defaultValue) {

		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		return defaultValue;
	}

	/**
	 * convenience method to extract the dimension from an {@link EObject}
	 * representing a {@link Size} object.
	 * 
	 * @param object
	 *            the {@link EObject} representing a {@link Size} object.
	 * @param defaultValue
	 *            the default value to use for the width and height if the
	 *            size's values could not be converted to a number.
	 * @return the extracted dimension.
	 */
	private static PrecisionDimension sizeConstraintToDimension(EObject object, double defaultValue) {
		return new PrecisionDimension(toDouble(object.eGet(NotationPackage.Literals.SIZE__WIDTH), defaultValue),
				toDouble(object.eGet(NotationPackage.Literals.SIZE__HEIGHT), defaultValue));
	}

	/**
	 * convenience method to extract a vector from an {@link EObject}
	 * representing a {@link Location} object.
	 * 
	 * @param object
	 *            the {@link EObject} representing a {@link Location} object.
	 * @param defaultValue
	 *            the default value to use for the x and y if the location's
	 *            values could not be converted to a number.
	 * @return the extracted vector.
	 */
	private static DoublePrecisionVector locationConstraintToVector(EObject object, double defaultValue) {
		return new DoublePrecisionVector(toDouble(object.eGet(NotationPackage.Literals.LOCATION__X), defaultValue),
				toDouble(object.eGet(NotationPackage.Literals.LOCATION__Y), defaultValue));
	}

	/**
	 * converts the given {@link DifferenceKind} to a
	 * {@link StateDifferenceType}
	 * 
	 * @param diffKind
	 *            the {@link DifferenceKind} to convert
	 * @return the corresponding {@link StateDifferenceType}
	 */
	private StateDifferenceType toStateDifferenceType(DifferenceKind diffKind) {

		switch (diffKind) {
		case ADD:
			return StateDifferenceType.ADDED;
		case MOVE:
		case CHANGE:
			return StateDifferenceType.MODIFIED;
		case DELETE:
			return StateDifferenceType.DELETED;
		default:
			return StateDifferenceType.UNKNOWN;
		}
	}

}
/*******************************************************************************
 * Copyright (c) 2015, 2016, 2017 Florian Zoubek.
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
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
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
import at.bitandart.zoubek.mervin.util.UnifiedModelMap;

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
	private UnifiedModelMap unifiedModelMap;
	private PreferencesHint preferencesHint;
	private ModelReviewFactory reviewFactory;
	private Collection<Object> overlayedViews;
	private ModelReview modelReview;
	private Set<EObject> cachedLinkTargets;

	public AddOverlayNodesCommand(TransactionalEditingDomain domain, Comparison diagramComparison,
			UnifiedModelMap unifiedModelMap, View container, Collection<Object> overlayedViews,
			PreferencesHint preferencesHint, ModelReviewFactory reviewFactory, ModelReview modelReview) {
		super(domain, "", null);
		this.diagramComparison = diagramComparison;
		this.container = container;
		this.unifiedModelMap = unifiedModelMap;
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
				createOverlayForView(childView, true, new LinkedList<DifferenceOverlay>());
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
	private void createOverlayForView(View view, boolean includeChildren, List<DifferenceOverlay> parentOverlays) {

		View originalView = (View) unifiedModelMap.getOriginal(view);
		if (originalView == null) {
			/*
			 * FIXME quick fix to avoid NPE - needs further investigation this
			 * should not happen as all copied views should have an original
			 * version
			 */
			return;
		}

		DifferenceOverlay differenceOverlay = null;

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
		Match viewMatch = diagramComparison.getMatch(originalView);
		Match elementMatch = diagramComparison.getMatch(view.getElement());
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

		if (elementMatch != null) {

			viewElementDifferences = elementMatch.getDifferences();
			hasComments = containsTargetsOfCommentLinks(elementMatch);
			EObject parent = view.eContainer();

			if (parent instanceof View) {
				/*
				 * the same element may be referenced by multiple views - to
				 * improve the readability of the overlays, only report comments
				 * if the parent overlay does not already show them.
				 */
				Match parentViewMatch = diagramComparison.getMatch(((View) parent).getElement());
				hasComments = hasComments && parentViewMatch != elementMatch;
			}
		}

		boolean hasViewReferenceDifferences = viewReferenceChange != null;
		boolean hasLayoutConstraintDifferences = (layoutConstraintDifferences != null
				&& !layoutConstraintDifferences.isEmpty());
		boolean hasBendpointsDifferences = (bendpointDifferences != null && !bendpointDifferences.isEmpty());
		boolean hasViewElementDifferences = (viewElementDifferences != null && !viewElementDifferences.isEmpty());
		boolean ignoreChildren = false;

		if (hasViewReferenceDifferences || hasLayoutConstraintDifferences || hasBendpointsDifferences
				|| hasViewElementDifferences || hasComments) {

			// create an overlay depending on the view type
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

				StateDifferenceType stateDifferenceType = toStateDifferenceType(viewReferenceChange.getKind());

				/*
				 * do not create a state difference if it is visually inherited
				 * by an overlay of a parent view (the 'parent' overlay)
				 */
				if (!hasParentSameStateDifferenceType(parentOverlays, stateDifferenceType)) {

					StateDifference stateDifference = reviewFactory.createStateDifference();
					stateDifference.getRawDiffs().add(viewReferenceChange);
					stateDifference.setType(stateDifferenceType);
					differenceOverlay.getDifferences().add(stateDifference);
				}

			}

			differenceOverlay.setCommented(hasComments);

			if (!differenceOverlay.getDifferences().isEmpty() || differenceOverlay.isCommented()) {

				/*
				 * found review relevant differences, so add the overlay into
				 * the view model
				 */
				ViewService.createNode(container, differenceOverlay, type, preferencesHint);
				parentOverlays.add(differenceOverlay);

			} else {
				differenceOverlay = null;
			}

		}

		if (includeChildren && !ignoreChildren) {
			for (Object child : view.getChildren()) {
				if (child instanceof View) {
					View childView = (View) child;
					createOverlayForView(childView, true, parentOverlays);
				}
			}
		}

		parentOverlays.remove(differenceOverlay);
	}

	/**
	 * determines if the parent overlay has the same state difference type as
	 * the given state difference type.
	 * 
	 * @param parentOverlays
	 *            a list of the parent overlays, ordered from the top to the
	 *            bottom (meaning the direct parent overlay is at the last
	 *            index).
	 * @param stateDifferenceType
	 *            the difference type to check.
	 * @return true if the the parent overlay has the same state difference
	 *         type, false otherwise.
	 */
	private boolean hasParentSameStateDifferenceType(List<DifferenceOverlay> parentOverlays,
			StateDifferenceType stateDifferenceType) {

		ListIterator<DifferenceOverlay> overlayIterator = parentOverlays.listIterator(parentOverlays.size());
		/*
		 * An overlay without state difference has the same state difference as
		 * its parent, so walk the parent overlay list from the bottom to the
		 * top. The first state difference found this way is then checked with
		 * the given state difference.
		 */
		while (overlayIterator.hasPrevious()) {
			DifferenceOverlay overlay = overlayIterator.previous();
			EList<Difference> differences = overlay.getDifferences();
			for (Difference difference : differences) {
				if (difference instanceof StateDifference) {
					return ((StateDifference) difference).getType() == stateDifferenceType;
				}
			}
		}
		return false;
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
	 * determines if any child of the given matched view objects has comments.
	 * 
	 * @param match
	 *            the match of the parent view object.
	 * @return true if any child object of the matched view objects has
	 *         comments, false otherwise. Returns always false for matches of
	 *         non-{@link View} objects.
	 */
	private boolean hasChildComments(Match match) {

		if (match != null) {
			Comparison comparison = match.getComparison();

			EObject left = match.getLeft();
			if (left instanceof View && hasChildComments((View) left, comparison)) {
				return true;
			}

			EObject right = match.getRight();
			if (right instanceof View && hasChildComments((View) right, comparison)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * determines if any child of the given view object has comments.
	 * 
	 * @param view
	 *            the parent view to determine the child comment state for.
	 * @param comparison
	 *            the comparison used to look up matches of the children.
	 * @return true if any child object of the given view object has comments,
	 *         false otherwise.
	 */
	private boolean hasChildComments(View view, Comparison comparison) {

		if (view != null) {

			TreeIterator<EObject> contents = view.eAllContents();
			while (contents.hasNext()) {

				EObject child = contents.next();

				/* check comments for views */
				Match childMatch = comparison.getMatch(child);
				if (childMatch != null && containsTargetsOfCommentLinks(childMatch)) {
					return true;
				}

				/* check comments for view elements */
				if (child instanceof View) {
					View childView = (View) child;
					Match elementMatch = comparison.getMatch(childView.getElement());
					if (elementMatch != null && containsTargetsOfCommentLinks(elementMatch)) {
						return true;
					}
				}
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
						oldLocation = locationConstraintToVector(layoutConstraintMatch.getRight(), null);
					}

					if (newLocation == null) {

						/*
						 * two way comparison is assumed, so left is new, right
						 * is old
						 */
						newLocation = locationConstraintToVector(layoutConstraintMatch.getLeft(), null);
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

		if (oldLocation != null || newLocation != null) {

			// determine move direction

			DoublePrecisionVector moveDirection = null;
			if (oldLocation != null && newLocation != null) {
				moveDirection = newLocation.getSubtractedDoublePrecisicon(oldLocation);
				if (moveDirection.getLength() != 0) {
					moveDirection.normalize();
				}
			}

			LocationDifference locationDifference = reviewFactory.createLocationDifference();
			locationDifference.getRawDiffs().addAll(locationRawDiffs);
			locationDifference.setMoveDirection(moveDirection);
			locationDifference.setOriginalLocation(oldLocation);

			differenceOverlay.getDifferences().add(locationDifference);

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
	private static Double toDouble(Object value, Double defaultValue) {

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
	 * @return the extracted dimension or null if the double conversion yields
	 *         null for the width or height attribute.
	 */
	private static PrecisionDimension sizeConstraintToDimension(EObject object, Double defaultValue) {
		Double width = toDouble(object.eGet(NotationPackage.Literals.SIZE__WIDTH), defaultValue);
		Double height = toDouble(object.eGet(NotationPackage.Literals.SIZE__HEIGHT), defaultValue);
		if (width != null && height != null) {
			return new PrecisionDimension(width, height);
		}
		return null;
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
	 * @return the extracted vector or null if the double conversion yields null
	 *         for the x or y attribute.
	 */
	private static DoublePrecisionVector locationConstraintToVector(EObject object, Double defaultValue) {
		Double x = toDouble(object.eGet(NotationPackage.Literals.LOCATION__X), defaultValue);
		Double y = toDouble(object.eGet(NotationPackage.Literals.LOCATION__Y), defaultValue);
		if (x != null && y != null) {
			return new DoublePrecisionVector(x, y);
		}
		return null;
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
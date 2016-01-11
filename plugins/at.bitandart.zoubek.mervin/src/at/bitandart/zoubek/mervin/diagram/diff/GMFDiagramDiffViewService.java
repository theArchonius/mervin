/*******************************************************************************
 * Copyright (c) 2015 Florian Zoubek.
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
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.commands.CreateDiagramCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.View;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import at.bitandart.zoubek.mervin.diagram.diff.gmf.ModelReviewElementTypes;
import at.bitandart.zoubek.mervin.draw2d.DoublePrecisionVector;
import at.bitandart.zoubek.mervin.model.modelreview.BendpointsDifference;
import at.bitandart.zoubek.mervin.model.modelreview.Difference;
import at.bitandart.zoubek.mervin.model.modelreview.DifferenceOverlay;
import at.bitandart.zoubek.mervin.model.modelreview.DimensionChange;
import at.bitandart.zoubek.mervin.model.modelreview.LocationDifference;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.SizeDifference;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifference;
import at.bitandart.zoubek.mervin.model.modelreview.StateDifferenceType;

// TODO remove Createable annotation and move creation in addon
/**
 * 
 * @author Florian Zoubek
 *
 */
@Creatable
public class GMFDiagramDiffViewService {

	@Inject
	private ModelReviewFactory reviewFactory;

	/**
	 * creates a view model for the given {@link ModelReview} instance and adds
	 * adapters to update the view model based on changes to the
	 * {@link ModelReview} instance.
	 * 
	 * @param modelReview
	 *            the {@link ModelReview} instance which is used to create the
	 *            view model and gets the adapters attached
	 * @param editDomain
	 *            the GEF {@link EditDomain} used to execute commands which
	 *            affect the view model
	 * @param transactionalEditingDomain
	 *            the {@link TransactionalEditingDomain} used by commands to
	 *            update the view model.
	 * @param preferencesHint
	 * @return the view model diagram instance or null if the creation of the
	 *         view model failed
	 */
	public Diagram createAndConnectViewModel(final ModelReview modelReview, final EditDomain editDomain,
			final TransactionalEditingDomain transactionalEditingDomain, final PreferencesHint preferencesHint) {

		final Diagram diagram = createDiagram(modelReview, editDomain, transactionalEditingDomain, preferencesHint);

		if (diagram != null) {
			updateDiagramNodes(diagram, modelReview, editDomain, transactionalEditingDomain, preferencesHint);
		}

		modelReview.eAdapters().add(new ModelReviewChangeAdapter(editDomain, preferencesHint, diagram, modelReview,
				transactionalEditingDomain));

		return diagram;
	}

	/**
	 * removes all attached adapters and listeners from the model review model
	 * that belong to the given diagram diff view model.
	 * 
	 * @param diagram
	 *            the diagram diff view model
	 */
	public void disconnectViewModel(Diagram diagram) {
		EObject object = diagram.getElement();
		if (object instanceof ModelReview) {
			EList<Adapter> eAdapters = object.eAdapters();
			Adapter adapterToRemove = null;
			for (Adapter adapter : eAdapters) {
				if (adapter instanceof ModelReviewChangeAdapter
						&& ((ModelReviewChangeAdapter) adapter).getDiagram() == diagram) {
					adapterToRemove = adapter;
					break;
				}
			}
			eAdapters.remove(adapterToRemove);
		}
	}

	/**
	 * creates a diagram view for the given {@link ModelReview} instance.
	 * 
	 * @param modelReview
	 *            the {@link ModelReview} instance to create the diagram for
	 * @param editDomain
	 *            the {@link EditDomain} used to execute the creation command
	 * @param transactionalEditingDomain
	 *            the {@link TransactionalEditingDomain} used to create the
	 *            {@link Diagram} instance
	 * @param preferencesHint
	 * @return the diagram or null if no diagram could not be created
	 */
	private Diagram createDiagram(ModelReview modelReview, EditDomain editDomain,
			TransactionalEditingDomain transactionalEditingDomain, PreferencesHint preferencesHint) {
		CreateDiagramCommand createDiagramCommand = new CreateDiagramCommand(transactionalEditingDomain,
				"ModelReviewDiff", modelReview, DiagramDiffView.PART_DESCRIPTOR_ID, preferencesHint);
		CommandResult result = executeCommand(createDiagramCommand, editDomain);
		if (result.getStatus().isOK() && result.getReturnValue() instanceof Diagram) {
			Diagram diagram = (Diagram) result.getReturnValue();
			return diagram;
		}
		return null;
	}

	/**
	 * deletes all children of the given workspace diagram instance and creates
	 * new child diagram views with their content.
	 * 
	 * @param workspaceDiagram
	 *            the workspace diagram instance
	 * @param modelReview
	 *            the {@link ModelReview} instance used to obtain the original
	 *            diagram instances
	 * @param editDomain
	 *            the GEF {@link EditDomain} used to execute the commands
	 * @param transactionalEditingDomain
	 *            the {@link TransactionalEditingDomain} used tby commands to
	 *            update the model
	 * @param preferencesHint
	 */
	private void updateDiagramNodes(Diagram workspaceDiagram, ModelReview modelReview, EditDomain editDomain,
			TransactionalEditingDomain transactionalEditingDomain, PreferencesHint preferencesHint) {

		// remove old diagram nodes
		EList<?> children = workspaceDiagram.getChildren();
		if (!children.isEmpty()) {
			CompositeCommand compositeCommand = new CompositeCommand("");
			for (Object child : children) {
				if (child instanceof View) {
					compositeCommand.add(new DeleteCommand(transactionalEditingDomain, (View) child));
				}
			}
			executeCommand(compositeCommand, editDomain);
		}

		// add new diagram nodes
		PatchSet rightPatchSet = modelReview.getRightPatchSet();
		if (rightPatchSet != null) {
			EList<Diagram> allNewInvolvedDiagrams = rightPatchSet.getAllNewInvolvedDiagrams();
			CompositeCommand compositeCommand = new CompositeCommand("");
			for (Diagram diagram : allNewInvolvedDiagrams) {
				compositeCommand
						.add(new CreateCommand(transactionalEditingDomain,
								new ViewDescriptor(new EObjectAdapter(diagram), Node.class,
										ModelReviewElementTypes.DIAGRAM_SEMANTIC_HINT, preferencesHint),
								workspaceDiagram));
			}
			if (!compositeCommand.isEmpty()) {
				executeCommand(compositeCommand.reduce(), editDomain);
			}
		}

		// add children for each diagram

		children = workspaceDiagram.getChildren();
		for (Object workspaceChild : children) {

			if (workspaceChild instanceof View && ((View) workspaceChild).getElement() instanceof Diagram) {
				final View childView = (View) workspaceChild;
				final Diagram diagram = (Diagram) childView.getElement();

				CompositeCommand compositeCommand = new CompositeCommand("");
				/*
				 * TODO return only context children. For now, return all
				 * visible child views
				 */
				List<Object> childrenAndEdges = new LinkedList<Object>();
				childrenAndEdges.addAll((List<?>) diagram.getEdges());
				childrenAndEdges.addAll((List<?>) diagram.getVisibleChildren());

				/*
				 * Copy the diagram contents and keep the mapping information to
				 * associate the copies to the differences later
				 */
				Copier copier = new Copier();
				Collection<Object> childrenAndEdgesCopy = copier.copyAll(childrenAndEdges);
				copier.copyReferences();
				HashBiMap<EObject, EObject> copyMap = HashBiMap.create(copier);

				List<Object> childrenCopy = new LinkedList<Object>(childrenAndEdgesCopy);
				compositeCommand.add(new AddDiagramEdgesAndNodesCommand(transactionalEditingDomain, workspaceDiagram,
						(View) childView, filterEdges(childrenCopy), filterNonEdges(childrenCopy), diagram.getType()));

				compositeCommand.add(new AddOverlayNodesCommand(transactionalEditingDomain,
						modelReview.getSelectedDiagramComparison(), copyMap, childView, childrenCopy, preferencesHint,
						reviewFactory));

				executeCommand(compositeCommand.reduce(), editDomain);
			}
		}
	}

	private List<Edge> filterEdges(List<Object> elements) {
		List<Edge> edges = new LinkedList<Edge>();
		for (Object child : elements) {
			if (child instanceof Edge) {
				edges.add((Edge) child);
			}
		}
		return edges;
	}

	private List<View> filterNonEdges(List<Object> elements) {

		List<View> nodes = new LinkedList<View>();
		for (Object child : elements) {
			if (child instanceof View && !(child instanceof Edge)) {
				nodes.add((View) child);
			}
		}
		return nodes;
	}

	private class AddDiagramEdgesAndNodesCommand extends AbstractTransactionalCommand {

		private View parentView;
		private Diagram diagram;
		private String originalDiagramType;
		private List<Edge> edges;
		private List<View> nodes;

		public AddDiagramEdgesAndNodesCommand(TransactionalEditingDomain domain, Diagram diagram, View parentView,
				List<Edge> edges, List<View> nodes, String originalDiagramType) {
			super(domain, "", null);
			this.parentView = parentView;
			this.diagram = diagram;
			this.edges = edges;
			this.nodes = nodes;
			this.originalDiagramType = originalDiagramType;
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {

			for (Edge edge : edges) {
				diagram.insertEdge(edge);
				addMissingModelHint(edge);
			}

			for (View node : nodes) {
				parentView.insertChild(node);
				addMissingModelHint(node);
			}

			return CommandResult.newOKCommandResult();
		}

		/**
		 * sets a model hint for generated GMF editpart provider if it does not
		 * exist.
		 * 
		 * @param view
		 *            the view to add the model hint
		 */
		private void addMissingModelHint(View view) {
			if (view.getEAnnotation("Shortcut") == null) {
				EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
				eAnnotation.setSource("Shortcut");
				eAnnotation.getDetails().put("modelID", originalDiagramType);
				view.getEAnnotations().add(eAnnotation);
			}
		}
	}

	/**
	 * An {@link AbstractTransactionalCommand} that adds overlays based on the
	 * differences in a given two way comparison for a given number of
	 * {@link View}s.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private class AddOverlayNodesCommand extends AbstractTransactionalCommand {

		private Comparison diagramComparison;
		private View container;
		private BiMap<EObject, EObject> inverseCopyMap;
		private PreferencesHint preferencesHint;
		private ModelReviewFactory reviewFactory;
		private Collection<Object> overlayedViews;

		public AddOverlayNodesCommand(TransactionalEditingDomain domain, Comparison diagramComparison,
				BiMap<EObject, EObject> copyMap, View container, Collection<Object> overlayedViews,
				PreferencesHint preferencesHint, ModelReviewFactory reviewFactory) {
			super(domain, "", null);
			this.diagramComparison = diagramComparison;
			this.container = container;
			this.inverseCopyMap = copyMap.inverse();
			this.preferencesHint = preferencesHint;
			this.reviewFactory = reviewFactory;
			this.overlayedViews = overlayedViews;
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {

			for (Object child : overlayedViews) {

				if (child instanceof View) {
					View childView = (View) child;
					createOverlayForView(childView, true);
				}
			}

			return CommandResult.newOKCommandResult();
		}

		/**
		 * creates an overlay for the given view and its child views if
		 * specified. If the view has been added or deleted, all child views
		 * will be ignored regardless of the value of the
		 * {@code includeChildren} parameter.
		 * 
		 * @param view
		 *            the view to create the overlay for.
		 * @param includeChildren
		 *            true if overlays should be created also for child views,
		 *            if the view has not been added or deleted.
		 */
		private void createOverlayForView(View view, boolean includeChildren) {

			View originalView = (View) inverseCopyMap.get(view);
			if (originalView == null) {
				/*
				 * FIXME quick fix to avoid NPE - needs further investigation
				 * this should not happen as all copied views should have an
				 * original version
				 */
				return;
			}

			/*
			 * find referencing differences and search for the ReferenceChange
			 * that describes a modification of a node's children or a diagrams
			 * edges
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
			 * Get all matches that might contain differences that should be
			 * part of an overlay
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
			if (viewElementMatch != null) {
				viewElementDifferences = viewElementMatch.getDifferences();
			}

			boolean hasViewReferenceDifferences = viewReferenceChange != null;
			boolean hasLayoutConstraintDifferences = (layoutConstraintDifferences != null
					&& !layoutConstraintDifferences.isEmpty());
			boolean hasBendpointsDifferences = (bendpointDifferences != null && !bendpointDifferences.isEmpty());
			boolean hasViewElementDifferences = (viewElementDifferences != null && !viewElementDifferences.isEmpty());
			boolean ignoreChildren = false;

			if (hasViewReferenceDifferences || hasLayoutConstraintDifferences || hasBendpointsDifferences
					|| hasViewElementDifferences) {

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
				 * determine state difference type based on the reference change
				 * of the view in its parent
				 */

				if (hasViewReferenceDifferences) {

					StateDifference stateDifference = reviewFactory.createStateDifference();
					stateDifference.getRawDiffs().add(viewReferenceChange);
					StateDifferenceType stateDifferenceType = toStateDifferenceType(viewReferenceChange.getKind());
					stateDifference.setType(stateDifferenceType);
					differenceOverlay.getDifferences().add(stateDifference);
					/*
					 * all child views must have the same difference type or are
					 * unchanged if the type is an addition or deletion - this
					 * done to avoid distracting multiple overlays for a
					 * addition or deletion of a view containing nested views
					 */
					ignoreChildren = stateDifferenceType == StateDifferenceType.ADDED
							|| stateDifferenceType == StateDifferenceType.DELETED;

				}

				if (!differenceOverlay.getDifferences().isEmpty()) {

					/*
					 * found review relevant differences, so add the overlay
					 * into the view model
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
		 * handles bendpoint difference of an {@link Edge} by adding
		 * {@link Difference}s to the given difference overlay based on the
		 * match of the edge's bendpoints. This method currently supports only
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
		 * {@link Difference} s to the given difference overlay based on the
		 * match of the node's layout constraint.
		 * 
		 * @param layoutConstraintMatch
		 *            the match of the layout constraint
		 * @param differenceOverlay
		 *            the difference overlay to add the differences to
		 */
		private void handleLayoutConstraintDifferences(Match layoutConstraintMatch,
				DifferenceOverlay differenceOverlay) {
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
							oldLocation = new DoublePrecisionVector(0.0, 0.0);
						}
						if (newLocation == null) {
							newLocation = new DoublePrecisionVector(0.0, 0.0);
						}

						Object value = attributeChange.getValue();
						if (value instanceof Number) {

							double doubleValue = ((Number) value).doubleValue();
							if (attributeChange.getAttribute() == NotationPackage.Literals.LOCATION__X) {
								newLocation.x = doubleValue;
							} else {
								newLocation.y = doubleValue;
							}

						}

						// two way comparison is assumed
						EObject oldLayoutVersion = layoutConstraintMatch.getRight();
						if (oldLayoutVersion != null) {

							try {

								Object oldValue = oldLayoutVersion.eGet(attributeChange.getAttribute());
								if (oldValue instanceof Number) {

									double doubleValue = ((Number) oldValue).doubleValue();
									if (attributeChange.getAttribute() == NotationPackage.Literals.LOCATION__X) {
										oldLocation.x = doubleValue;
									} else {
										oldLocation.y = doubleValue;
									}

								}

							} catch (IllegalArgumentException e) {
								/*
								 * Intentionally left empty, use the default
								 * value 0 for the old location value
								 */
							}

						}

					} else if (attributeChange.getAttribute() == NotationPackage.Literals.SIZE__HEIGHT
							|| attributeChange.getAttribute() == NotationPackage.Literals.SIZE__WIDTH) {

						sizeRawDiffs.add(attributeChange);

						if (oldSize == null) {
							oldSize = new PrecisionDimension(0.0, 0.0);
						}
						if (newSize == null) {
							newSize = new PrecisionDimension(0.0, 0.0);
						}

						Object value = attributeChange.getValue();
						if (value instanceof Number) {

							double doubleValue = ((Number) value).doubleValue();
							if (attributeChange.getAttribute() == NotationPackage.Literals.SIZE__HEIGHT) {
								newSize.setPreciseHeight(doubleValue);
							} else {
								newSize.setPreciseWidth(doubleValue);
							}

						}

						// two way comparison is assumed
						EObject oldLayoutVersion = layoutConstraintMatch.getRight();
						if (oldLayoutVersion != null) {

							try {

								Object oldValue = oldLayoutVersion.eGet(attributeChange.getAttribute());
								if (oldValue instanceof Number) {

									double doubleValue = ((Number) oldValue).doubleValue();
									if (attributeChange.getAttribute() == NotationPackage.Literals.SIZE__HEIGHT) {
										oldSize.setPreciseHeight(doubleValue);
									} else {
										oldSize.setPreciseWidth(doubleValue);
									}

								}

							} catch (IllegalArgumentException e) {
								/*
								 * Intentionally left empty, use the default
								 * value 0 for the old location value
								 */
							}

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

					differenceOverlay.getDifferences().add(locationDifference);

				}

			}

			if (oldSize != null && newSize != null) {

				// determine size differences

				SizeDifference sizeDifference = reviewFactory.createSizeDifference();
				sizeDifference.getRawDiffs().addAll(sizeRawDiffs);

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

	/**
	 * executes the given {@link ICommand} with the given {@link EditDomain} and
	 * returns the result of the command.
	 * 
	 * @param command
	 *            the command to execute
	 * @param editDomain
	 *            the GEF {@link EditDomain} used to execute the command
	 * @return the result of the command
	 */
	private CommandResult executeCommand(ICommand command, EditDomain editDomain) {
		editDomain.getCommandStack().execute(new ICommandProxy(command));
		return command.getCommandResult();
	}

	/**
	 * An {@link EContentAdapter} used to listen to changes in a
	 * {@link ModelReview} instance that trigger changes in the view model.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private final class ModelReviewChangeAdapter extends EContentAdapter {
		private final EditDomain editDomain;
		private final PreferencesHint preferencesHint;
		private final Diagram diagram;
		private final ModelReview modelReview;
		private final TransactionalEditingDomain transactionalEditingDomain;

		private ModelReviewChangeAdapter(EditDomain editDomain, PreferencesHint preferencesHint, Diagram diagram,
				ModelReview modelReview, TransactionalEditingDomain transactionalEditingDomain) {
			this.editDomain = editDomain;
			this.preferencesHint = preferencesHint;
			this.diagram = diagram;
			this.modelReview = modelReview;
			this.transactionalEditingDomain = transactionalEditingDomain;
		}

		@Override
		public void notifyChanged(Notification notification) {

			// needed to adapt also containment references
			super.notifyChanged(notification);

			int featureID = notification.getFeatureID(PatchSet.class);
			if (featureID == ModelReviewPackage.MODEL_REVIEW__LEFT_PATCH_SET
					|| featureID == ModelReviewPackage.MODEL_REVIEW__RIGHT_PATCH_SET) {

				updateDiagramNodes(diagram, modelReview, editDomain, transactionalEditingDomain, preferencesHint);

			}
		}

		public Diagram getDiagram() {
			return diagram;
		}
	}
}

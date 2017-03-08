/*******************************************************************************
 * Copyright (c) 2015, 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.property.diff;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import at.bitandart.zoubek.mervin.swt.diff.tree.ITreeDiffItemProvider;
import at.bitandart.zoubek.mervin.swt.diff.tree.TreeDiffSide;
import at.bitandart.zoubek.mervin.swt.diff.tree.TreeDiffType;

/**
 * An {@link ITreeDiffItemProvider} that provides data for
 * {@link SelectionEntry}s, {@link MatchEntry}s and {@link ObjectEntry}s. The
 * root input object must be an {@link SelectionEntry}, a {@link Collection} of
 * {@link SelectionEntry}s or an array of {@link SelectionEntry}s.
 * 
 * @author Florian Zoubek
 *
 */
public class PropertyDiffItemProvider implements ITreeDiffItemProvider {

	private static final String SEMANTIC_MODEL_ELEMENT_PREFIX = "Model: ";
	private static final String NOTATION_MODEL_ELEMENT_PREFIX = "Diagram Model: ";
	private static final String OTHER_NOTATION_MODEL_ELEMENTS_PREFIX = "Diagram Model: ";

	private AdapterFactoryLabelProvider labelProvider;

	public PropertyDiffItemProvider() {
		labelProvider = new AdapterFactoryLabelProvider(
				new ComposedAdapterFactory(EMFCompareRCPPlugin.getDefault().createFilteredAdapterFactoryRegistry()));
	}

	@Override
	public Object[] getRootItems(Object input) {

		if (input instanceof BaseEntry) {

			/* single entry -> return only it's children */
			return getEntryChildren((BaseEntry) input);

		} else if (input instanceof BaseEntry[]) {

			BaseEntry[] entries = (BaseEntry[]) input;
			return entries;

		} else if (input instanceof Collection<?>) {

			Collection<?> collection = (Collection<?>) input;
			if (containsOnlyBaseEntries(collection)) {
				return collection.toArray();
			}
		}

		return new Object[0];
	}

	/**
	 * @param entry
	 *            the entry to get the children for.
	 * @return the children for the given entry.
	 */
	private Object[] getEntryChildren(BaseEntry entry) {

		if (entry instanceof ContainerEntry) {
			return ((ContainerEntry) entry).getChildEntries().toArray();
		} else if (entry instanceof SelectionEntry) {
			return getEntryChildren((SelectionEntry) entry);
		} else if (entry instanceof ListEntry) {
			return getEntryChildren((ListEntry) entry);
		} else if (entry instanceof MatchEntry) {
			return getEntryChildren((MatchEntry) entry);
		}

		return new Object[0];
	}

	/**
	 * @param entry
	 *            the entry to get the children for.
	 * @return the children for the given entry.
	 */
	private Object[] getEntryChildren(SelectionEntry entry) {

		Match modelElementMatch = entry.getModelElementMatch();
		Match notationElementMatch = entry.getNotationElementMatch();
		List<Match> otherNotationElementMatches = entry.getOtherNotationElementMatches();

		List<BaseEntry> children = new ArrayList<BaseEntry>();

		if (modelElementMatch != null) {

			children.add(new MatchEntry(entry, SEMANTIC_MODEL_ELEMENT_PREFIX, modelElementMatch, null,
					new ReferencingDiffCache(entry.getModelElementMatch())));
		}

		if (notationElementMatch != null) {

			children.add(new MatchEntry(entry, NOTATION_MODEL_ELEMENT_PREFIX, notationElementMatch, null,
					new ReferencingDiffCache(entry.getNotationElementMatch())));
		}

		if (!otherNotationElementMatches.isEmpty()) {

			ListEntry listEntry = new ListEntry(entry, OTHER_NOTATION_MODEL_ELEMENTS_PREFIX, null);
			children.add(listEntry);
			List<BaseEntry> elementList = listEntry.getElementList();

			int i = 0;
			for (Match match : otherNotationElementMatches) {

				elementList
						.add(new MatchEntry(listEntry, "[" + i + "] ", match, null, new ReferencingDiffCache(match)));
				i++;
			}
		}

		return children.toArray();
	}

	/**
	 * @param listEntry
	 *            the entry to get the children for.
	 * @return the children for the given entry.
	 */
	private Object[] getEntryChildren(ListEntry listEntry) {

		return listEntry.getElementList().toArray();

	}

	/**
	 * @param entry
	 *            the entry to get the children for.
	 * @return the children for the given entry.
	 */
	private Object[] getEntryChildren(MatchEntry entry) {

		Match match = entry.getMatch();
		EClass commonClass = null;
		EObject left = match.getLeft();
		EObject right = match.getRight();
		EObject origin = match.getOrigin();

		EStructuralFeature referencingFeature = entry.getReferencingFeature();

		if (referencingFeature instanceof EAttribute
				|| (referencingFeature instanceof EReference && !((EReference) referencingFeature).isContainment())) {
			/*
			 * do not create children for non-containment features as they may
			 * result in a graph instead of tree
			 */
			return new Object[0];
		}

		Comparison comparison = match.getComparison();

		/* find the common class used to get all features */
		if (left != null) {
			commonClass = left.eClass();
		} else if (right != null) {
			commonClass = right.eClass();
		} else if (origin != null) {
			commonClass = origin.eClass();
		}

		if (commonClass != null) {

			/* create a cache for the diffs for each feature */
			Map<EStructuralFeature, DiffCache> featureDiffMap = new HashMap<>();
			for (Diff diff : match.getDifferences()) {
				EStructuralFeature structuralFeature = MatchUtil.getStructuralFeature(diff);
				if (structuralFeature != null) {
					DiffCache featureDiffCache = featureDiffMap.get(structuralFeature);
					if (featureDiffCache == null) {
						featureDiffCache = new DiffCache();
						featureDiffMap.put(structuralFeature, featureDiffCache);
					}
					featureDiffCache.addDiff(diff);
				}
			}

			List<EStructuralFeature> allStructuralFeatures = commonClass.getEAllStructuralFeatures();
			ListIterator<EStructuralFeature> featureIterator = allStructuralFeatures.listIterator();

			List<BaseEntry> children = new LinkedList<>();
			while (featureIterator.hasNext()) {

				EStructuralFeature structuralFeature = featureIterator.next();
				Object leftValue = (left != null) ? left.eGet(structuralFeature) : null;
				Object rightValue = (right != null) ? right.eGet(structuralFeature) : null;

				DiffCache featureDiffCache = featureDiffMap.get(structuralFeature);
				if (featureDiffCache == null) {
					featureDiffCache = new DiffCache();
				}

				if (structuralFeature instanceof EReference || structuralFeature instanceof EAttribute) {

					// create the children for each feature

					if (!structuralFeature.isMany()) {

						addMonoValuedChildren(entry, comparison, children, leftValue, rightValue, structuralFeature,
								featureDiffCache);

					} else {

						addMultiValuedChildren(entry, comparison, children, leftValue, rightValue, structuralFeature,
								featureDiffCache);

					}

				}
			}
			return children.toArray();
		}

		return new Object[0];
	}

	/**
	 * adds the children for the given multi-valued feature and the given left
	 * and right values to the given list of children.
	 * 
	 * @param parent
	 *            the parent {@link BaseEntry} for all created child entries, or
	 *            null if the there is no parent entry.
	 * @param comparison
	 *            the comparison used to retrieve the matches for the values.
	 * @param children
	 *            the list of children to add the new children to.
	 * @param leftValue
	 *            the left value of the feature.
	 * @param rightValue
	 *            the right value of the feature.
	 * @param feature
	 *            the feature to create the children for.
	 * @param featureDiffs
	 *            a {@link DiffCache} containing all diffs for the given
	 *            feature.
	 */
	private void addMultiValuedChildren(BaseEntry parent, Comparison comparison, List<BaseEntry> children,
			Object leftValue, Object rightValue, EStructuralFeature feature, DiffCache featureDiffs) {

		ListEntry listEntry = new ListEntry(parent, getText(feature), null);

		children.add(listEntry);

		List<BaseEntry> elementList = listEntry.getElementList();

		EList<?> leftList = null;
		EList<?> rightList = null;

		/* only ELists are supported for now */
		if (leftValue instanceof EList) {
			leftList = (EList<?>) leftValue;
		}
		if (rightValue instanceof EList) {
			rightList = (EList<?>) rightValue;
		}

		if ((rightList == null || rightList.isEmpty()) && leftList != null) {

			/* only the left list contains values, so simply add all of them */
			addAllRemainingFeatureEntries(listEntry, leftList.listIterator(), TreeDiffSide.LEFT, comparison, feature,
					featureDiffs);

		} else if ((leftList == null || leftList.isEmpty()) && rightList != null) {

			/* only the right list contains values, so simply add all of them */
			addAllRemainingFeatureEntries(listEntry, rightList.listIterator(), TreeDiffSide.RIGHT, comparison, feature,
					featureDiffs);

		} else {

			/*
			 * both lists have entries, so create create an unified list with a
			 * reasonable order. This is done by walking through both lists in
			 * parallel and adding one entry for added/deleted value and merging
			 * equal or modified value into one entry.
			 */
			ListIterator<?> leftIterator = leftList.listIterator();
			ListIterator<?> rightIterator = rightList.listIterator();

			leftValue = null;
			rightValue = null;

			while (leftIterator.hasNext() && rightIterator.hasNext()) {

				if (leftValue == null) {

					if (leftIterator.hasNext()) {
						leftValue = leftIterator.next();

					} else {
						break;
					}
				}
				if (rightValue == null) {

					if (rightIterator.hasNext()) {
						rightValue = rightIterator.next();

					} else {
						break;
					}
				}

				Match leftMatch = null;
				Match rightMatch = null;

				EObject leftEObj = null;
				EObject rightEObj = null;

				if (leftValue instanceof EObject) {
					leftEObj = (EObject) leftValue;
					leftMatch = comparison.getMatch(leftEObj);
				}

				if (rightValue instanceof EObject) {
					rightEObj = (EObject) rightValue;
					rightMatch = comparison.getMatch(rightEObj);
				}
				if (leftMatch != null && rightMatch != null) {

					/* Both EObjects are present in the comparison */

					if (leftMatch == rightMatch) {
						/*
						 * CHANGE or simple match - create a single entry for it
						 */
						elementList.add(
								createMatchEntry(listEntry, "", leftMatch, TreeDiffSide.LEFT, feature, featureDiffs));

						leftValue = null;
						rightValue = null;

					} else {

						/*
						 * moves are not supported by the tree diff viewer
						 * (yet), so add one entry for the left and and one for
						 * the right value.
						 */
						if (isMoveMatch(featureDiffs, leftMatch)) {

							elementList.add(createMatchEntry(listEntry, "", leftMatch, TreeDiffSide.LEFT, feature,
									featureDiffs));

							leftValue = null;

						} else if (isMoveMatch(featureDiffs, rightMatch)) {

							elementList.add(createMatchEntry(listEntry, "", rightMatch, TreeDiffSide.RIGHT, feature,
									featureDiffs));

							rightValue = null;

						} else {

							/*
							 * No move, but an addition or deletion - deletions
							 * will be created before additions
							 */
							EObject leftMatchedValue = leftMatch.getRight();

							if (leftMatchedValue == null) {

								/*
								 * the left entry has been deleted and should be
								 * shown before the right Object
								 */
								elementList.add(createMatchEntry(listEntry, "", leftMatch, TreeDiffSide.LEFT, feature,
										featureDiffs));

								leftValue = null;

							} else {
								EObject container = leftMatchedValue.eContainer();
								if (rightEObj.eContainer() == container) {

									/*
									 * if the index of the matched entry is
									 * smaller, add it before the right object
									 */
									if (rightList.indexOf(rightEObj) > rightList.indexOf(leftMatchedValue)) {
										elementList.add(createMatchEntry(listEntry, "", leftMatch, TreeDiffSide.LEFT,
												feature, featureDiffs));
										leftValue = null;
									} else {
										elementList.add(createMatchEntry(listEntry, "", rightMatch, TreeDiffSide.RIGHT,
												feature, featureDiffs));
										rightValue = null;
									}

								} else {
									/*
									 * the left entry has been moved into
									 * another container, so add it before the
									 * right object
									 */
									elementList.add(createMatchEntry(listEntry, "", leftMatch, TreeDiffSide.LEFT,
											feature, featureDiffs));
									leftValue = null;
								}

							}
						}
					}

				} else {

					/*
					 * the values are not part of the comparison, so do some
					 * basic differencing logic based on Object#equals(Object)
					 */

					if (leftValue != null) {

						if (leftValue.equals(rightValue)) {

							/* values are equal */
							elementList.add(new ObjectEntry(listEntry, "", comparison, leftValue, rightValue));
							leftValue = null;
							rightValue = null;

						} else {

							/*
							 * assume left has been deleted, so add single entry
							 */
							elementList.add(new ObjectEntry(listEntry, "", comparison, leftValue, null));
							leftValue = null;
						}

					} else if (leftValue == rightValue) {

						/* values are both null */
						elementList.add(new ObjectEntry(listEntry, "", comparison, leftValue, rightValue));
						leftValue = null;
						rightValue = null;

					} else {

						/*
						 * assume left has been deleted, so add single entry
						 */
						elementList.add(new ObjectEntry(listEntry, "", comparison, leftValue, null));
						leftValue = null;
					}
				}

			} // while end

			/*
			 * reset the iterators in case the objects have not been handled yet
			 */
			if (leftValue != null) {
				leftIterator.previous();
			}

			if (rightValue != null) {
				rightIterator.previous();
			}

			if (leftIterator.hasNext()) {

				/* add entries for the remaining values */
				addAllRemainingFeatureEntries(listEntry, leftIterator, TreeDiffSide.LEFT, comparison, feature,
						featureDiffs);

			} else if (rightIterator.hasNext()) {

				/* add entries for the remaining values */
				addAllRemainingFeatureEntries(listEntry, rightIterator, TreeDiffSide.RIGHT, comparison, feature,
						featureDiffs);
			}
		}
	}

	/**
	 * convenience method to get a description text for the given feature.
	 * 
	 * @param feature
	 *            the feature to create the text for.
	 * @return a String describing the given feature.
	 */
	protected String getText(EStructuralFeature feature) {

		StringBuilder sb = new StringBuilder();
		if (feature.isDerived()) {
			sb.append("[Derived] ");
		}

		if (feature instanceof EReference && ((EReference) feature).isContainment()) {
			sb.append("[Containment] ");
		}

		sb.append(feature.getName());
		sb.append(" : ");
		sb.append(feature.getEType().getName());

		if (feature.isMany()) {
			sb.append(" []");
		}

		return sb.toString();
	}

	/**
	 * checks if the values of the given match is affected by a move or not.
	 * This is the case if a diff in the given DiffCache with kind move exists
	 * for the left or right value of the match.
	 * 
	 * @param featureDiffs
	 *            the {@link DiffCache} containing the diffs to consider.
	 * @param match
	 *            the match to check.
	 * @return true if the given match is affected by a move, false otherwise.
	 */
	private boolean isMoveMatch(DiffCache featureDiffs, Match match) {

		EObject left = match.getLeft();
		EObject right = match.getRight();

		List<Diff> diffs = featureDiffs.getDiffs();
		for (Diff diff : diffs) {

			Object diffValue = MatchUtil.getValue(diff);
			if (diffValue != null && (diffValue == left || diffValue == right)
					&& diff.getKind() == DifferenceKind.MOVE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * creates a {@link MatchEntry} for the given match.
	 * 
	 * @param parent
	 *            the parent {@link BaseEntry} of the created entry, or null if
	 *            the there is no parent entry.
	 * @param labelTextPrefix
	 *            the label text prefix for the {@link MatchEntry}.
	 * @param match
	 *            the match associated with the entry.
	 * @param matchSide
	 *            the primary side of the match - this parameter affects only
	 *            {@link MovedMatchEntry}s and will be used to determine on
	 *            which side the entry will be shown in the tree.
	 * @param referencingFeature
	 *            the feature of the {@link EClass}s of the value represented by
	 *            the parent entry that references one value of this match
	 * @param featureDiffs
	 *            a {@link DiffCache} containing all diffs related to the
	 *            referencing feature.
	 * @return a {@link MatchEntry} for the given parameters.
	 */
	private MatchEntry createMatchEntry(BaseEntry parent, String labelTextPrefix, Match match, TreeDiffSide matchSide,
			EStructuralFeature referencingFeature, DiffCache featureDiffs) {

		DiffCache diffCache = new ReferencingDiffCache(match);

		if (diffCache.containsKind(DifferenceKind.MOVE)) {
			return new MovedMatchEntry(parent, labelTextPrefix, match, referencingFeature, diffCache, matchSide);
		}
		return new MatchEntry(parent, labelTextPrefix, match, referencingFeature, diffCache);
	}

	/**
	 * 
	 * adds the children for the given mono-valued feature and the given left
	 * and right values to the given list of children.
	 * 
	 * @param parent
	 *            the parent {@link BaseEntry} for all created child entries, or
	 *            null if the there is no parent entry.
	 * @param comparison
	 *            the comparison used to retrieve the matches for the values.
	 * @param children
	 *            the list of children to add the new children to.
	 * @param leftValue
	 *            the left value of the feature.
	 * @param rightValue
	 *            the right value of the feature
	 * @param feature
	 *            the feature to create the children for.
	 * @param featureDiffs
	 *            a {@link DiffCache} containing all diffs related to the given
	 *            feature.
	 */
	private void addMonoValuedChildren(BaseEntry parent, Comparison comparison, List<BaseEntry> children,
			Object leftValue, Object rightValue, EStructuralFeature feature, DiffCache featureDiffs) {

		if ((leftValue != null || rightValue != null) && (leftValue == null || leftValue instanceof EObject)
				&& (rightValue == null || rightValue instanceof EObject)) {

			Match leftMatch = comparison.getMatch((EObject) leftValue);
			Match rightMatch = comparison.getMatch((EObject) rightValue);

			if (leftMatch == null && rightMatch == null) {

				/* No match, treat as non-EObject */
				children.add(new ObjectEntry(parent, "", comparison, leftValue, rightValue));

			} else {
				if (leftMatch == rightMatch) {

					/*
					 * same match, so add only one entry
					 */
					children.add(createMatchEntry(parent, getText(feature) + " : ", leftMatch, TreeDiffSide.LEFT,
							feature, featureDiffs));

				} else {

					/*
					 * two different matches, add one list entry for each match
					 */
					ListEntry listEntry = new ListEntry(parent, getText(feature), null);
					List<BaseEntry> elementList = listEntry.getElementList();
					if (leftMatch != null) {
						elementList.add(createMatchEntry(listEntry, getText(feature) + " : ", leftMatch,
								TreeDiffSide.LEFT, feature, featureDiffs));
					}
					if (rightMatch != null) {
						elementList.add(createMatchEntry(listEntry, getText(feature) + " : ", rightMatch,
								TreeDiffSide.RIGHT, feature, featureDiffs));
					}
					if (elementList.size() == 1) {
						elementList.get(0).setParent(parent);
						children.add(elementList.get(0));

					} else {

						children.add(listEntry);
					}

				}
			}

		} else {

			children.add(new ObjectEntry(parent, getText(feature) + " : ", comparison, leftValue, rightValue));

		}
	}

	/**
	 * checks if the given collection contains only {@link SelectionEntry}s.
	 * 
	 * @param collection
	 *            the collection to check.
	 * @return true if the collection contains only {@link SelectionEntry}s,
	 *         false otherwise
	 */
	private boolean containsOnlyBaseEntries(Collection<?> collection) {
		return Iterables.all(collection, Predicates.instanceOf(BaseEntry.class));
	}

	/**
	 * Convenience class that holds a set of {@link Diff}s and tracks the
	 * {@link DifferenceKind} contained in the set.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	protected static class DiffCache {

		protected List<Diff> diffs = new ArrayList<Diff>();
		protected Set<DifferenceKind> differenceKinds = new HashSet<>();

		public DiffCache() {
		}

		/**
		 * adds the given {@link Diff} to the cache.
		 * 
		 * @param diff
		 *            the {@link Diff} to add.
		 */
		public void addDiff(Diff diff) {
			if (!diffs.contains(diff)) {
				diffs.add(diff);
			}
			differenceKinds.add(diff.getKind());
		}

		/**
		 * checks if a {@link Diff} of a specific {@link DifferenceKind} is
		 * contained in this cache.
		 * 
		 * @param kind
		 *            the {@link DifferenceKind} to search for.
		 * @return true if any {@link Diff} in this cache is of the given
		 *         {@link DifferenceKind}, false otherwise.
		 */
		public boolean containsKind(DifferenceKind kind) {
			return differenceKinds.contains(kind);
		}

		/**
		 * @return an unmodifiable unordered list containing all diffs of this
		 *         cache.
		 */
		public List<Diff> getDiffs() {
			return Collections.unmodifiableList(diffs);
		}

	}

	/**
	 * A {@link DiffCache} that stores the diffs that reference the values of
	 * the given {@link Match}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	protected static class ReferencingDiffCache extends DiffCache {

		/**
		 * creates a new {@link ReferencingDiffCache} for the given
		 * {@link Match}.
		 * 
		 * @param match
		 *            the match to create the cache for.
		 */
		public ReferencingDiffCache(Match match) {
			if (match != null) {
				addDiffs(match, match.getDifferences());
				addReferencingDiffs(match);
			}
		}

		/**
		 * adds the referencing diff of the given match to this cache.
		 * 
		 * @param match
		 *            the match to add the diffs for.
		 */
		private void addReferencingDiffs(Match match) {

			EObject left = match.getLeft();
			if (left != null) {
				addReferencingDiffs(left, match.getComparison());
			}

			EObject right = match.getRight();
			if (right != null) {
				addReferencingDiffs(right, match.getComparison());
			}
		}

		/**
		 * adds the diffs of the given comparison that reference the given
		 * {@link EObject} to this cache.
		 * 
		 * @param child
		 *            the object that should be referenced by a {@link Diff}.
		 * @param comparison
		 *            the comparison containing the diffs.
		 */
		private void addReferencingDiffs(EObject child, Comparison comparison) {

			EObject eContainer = child.eContainer();
			if (eContainer != null) {

				Match containerMatch = comparison.getMatch(eContainer);
				EList<Diff> differences = containerMatch.getDifferences();
				for (Diff diff : differences) {

					Object diffValue = MatchUtil.getValue(diff);
					if (diffValue != null && diffValue == child) {
						addDiff(diff);
					}
				}
			}
		}

		/**
		 * adds all {@link Diff}s and {@link ResourceAttachmentChange}s of the
		 * given collection to this cache if they are related to the given
		 * match.
		 * 
		 * @param match
		 *            the match that should be related to the diffs.
		 * @param diffs
		 *            the collection of diffs to search through.
		 */
		private void addDiffs(Match match, Collection<Diff> diffs) {

			EObject left = match.getLeft();
			EObject right = match.getRight();

			for (Diff diff : diffs) {
				Object diffValue = MatchUtil.getValue(diff);
				if ((diffValue != null && (diffValue == left || diffValue == right))
						|| diff instanceof ResourceAttachmentChange
								&& (diff.getKind() == DifferenceKind.ADD || diff.getKind() == DifferenceKind.DELETE)) {
					addDiff(diff);
				}
			}
		}

	}

	/**
	 * adds entries to the given list entry for each element of the given
	 * iterator of a list on the given side.
	 * 
	 * @param listEntry
	 *            the {@link ListEntry} to add the entries to.
	 * @param listIterator
	 *            the iterator of the list of elements to create entries for.
	 * @param side
	 *            the side of the list of entries to add.
	 * @param comparison
	 *            the comparison used to find the matches to the objects in the
	 *            list.
	 * @param feature
	 *            the feature that referenced the list.
	 * @param featureDiffs
	 *            a {@link DiffCache} containing all diffs for the given
	 *            feature.
	 */
	private void addAllRemainingFeatureEntries(ListEntry listEntry, ListIterator<?> listIterator, TreeDiffSide side,
			Comparison comparison, EStructuralFeature feature, DiffCache featureDiffs) {

		List<BaseEntry> elementList = listEntry.getElementList();
		while (listIterator.hasNext()) {

			Object nextElement = listIterator.next();

			if (nextElement instanceof EObject) {

				Match match = comparison.getMatch((EObject) nextElement);
				MatchEntry matchEntry = createMatchEntry(listEntry, "", match, side, feature, featureDiffs);
				elementList.add(matchEntry);

			} else {

				if (side == TreeDiffSide.LEFT) {
					elementList.add(new ObjectEntry(listEntry, "", comparison, nextElement, null));
				}
			}
		}

	}

	@Override
	public Object[] getChildren(Object item) {

		if (item instanceof BaseEntry) {
			return getEntryChildren((BaseEntry) item);
		}

		return new Object[0];
	}

	/**
	 * retrieves the text of the given entry for the given {@link TreeDiffSide}.
	 * 
	 * @param entry
	 *            the entry to create the text for.
	 * @param side
	 *            the side to create the text for.
	 * @return the text for the given parameters.
	 */
	private String getEntryText(BaseEntry entry, TreeDiffSide side) {

		String text = entry.getLabelTextPrefix();
		if (entry instanceof ContainerEntry) {

			switch (side) {
			case LEFT:
				text += ((ContainerEntry) entry).getLeftLabel();
				break;
			case RIGHT:
				text += ((ContainerEntry) entry).getRightLabel();
				break;
			}

		} else if (entry instanceof SelectionEntry) {

			text += "Selection";

		} else if (entry instanceof ObjectEntry) {

			Object value;
			if (side == TreeDiffSide.LEFT) {
				value = ((ObjectEntry) entry).getLeft();
			} else {
				value = ((ObjectEntry) entry).getRight();
			}
			text += value != null ? (value instanceof String) ? "\"" + value.toString() + "\"" : value.toString()
					: "<null>";

		} else if (entry instanceof MatchEntry) {

			EObject value;
			Match match = ((MatchEntry) entry).getMatch();
			if (side == TreeDiffSide.LEFT) {
				value = match.getLeft();
			} else {
				value = match.getRight();
			}
			if (value != null) {
				String factoryText = labelProvider.getText(value);
				if (factoryText != null) {
					text += factoryText;
				} else {
					text += value.eClass().getName();
				}
			} else {
				text += "<null>";
			}

		} else if (entry instanceof ListEntry) {

			text += MessageFormat.format(" - {0} Element(s)", ((ListEntry) entry).getElementList().size());
		}

		return text;

	}

	@Override
	public String getDiffItemText(Object item, TreeDiffSide side, TreeDiffSide changedSide) {

		if (item instanceof BaseEntry) {
			return getEntryText((BaseEntry) item, side);
		}

		return "";
	}

	@Override
	public Image getDiffItemImage(Object item, TreeDiffSide side, TreeDiffSide changedSide) {

		// TODO

		return null;
	}

	/**
	 * Determines the {@link TreeDiffType} for the given {@link BaseEntry}. This
	 * method considers the left side as containing the changed (new) version.
	 * 
	 * @param entry
	 *            the entry to determine the {@link TreeDiffType} for.
	 * @return the {@link TreeDiffType} for the given entry.
	 */
	private TreeDiffType getEntryDiffType(BaseEntry entry) {

		if (entry instanceof ContainerEntry) {

			return TreeDiffType.EQUAL;

		} else if (entry instanceof MovedMatchEntry) {

			if (((MovedMatchEntry) entry).getSide() == TreeDiffSide.LEFT) {
				return TreeDiffType.ADD;
			} else {
				return TreeDiffType.DELETE;
			}

		} else if (entry instanceof MatchEntry) {

			MatchEntry matchEntry = (MatchEntry) entry;
			DiffCache diffs = matchEntry.getDiffs();
			if (diffs.containsKind(DifferenceKind.ADD)) {

				return TreeDiffType.ADD;

			} else if (diffs.containsKind(DifferenceKind.DELETE)) {

				return TreeDiffType.DELETE;

			} else if (diffs.containsKind(DifferenceKind.CHANGE)) {

				return TreeDiffType.MODIFY;

			}

		} else if (entry instanceof ObjectEntry) {

			ObjectEntry objectEntry = (ObjectEntry) entry;
			Object left = objectEntry.getLeft();
			Object right = objectEntry.getRight();

			if (left != null) {

				if (left.equals(right)) {

					return TreeDiffType.EQUAL;

				} else if (right != null) {

					return TreeDiffType.MODIFY;

				} else {

					return TreeDiffType.ADD;
				}

			} else {

				if (left == right) {

					return TreeDiffType.EQUAL;

				} else {

					return TreeDiffType.DELETE;
				}
			}
		} else if (entry instanceof BaseEntry) {
			BaseEntry baseEntry = (BaseEntry) entry;
			BaseEntry parentEntry = baseEntry.getParent();
			if (parentEntry != null) {
				return getEntryDiffType(parentEntry);
			}
		}

		return TreeDiffType.EQUAL;
	}

	@Override
	public TreeDiffType getTreeDiffType(Object item, TreeDiffSide changedSide) {

		if (item instanceof BaseEntry) {
			TreeDiffType type = getEntryDiffType((BaseEntry) item);

			if (changedSide == TreeDiffSide.RIGHT) {
				/*
				 * #getEntryDiffType assumes that the left side is the changed
				 * side, so switch ADD & DELETE if this is not the requested
				 * side by the viewer
				 */
				if (type == TreeDiffType.ADD) {

					type = TreeDiffType.DELETE;

				} else if (type == TreeDiffType.DELETE) {

					type = TreeDiffType.ADD;
				}
			}
			return type;
		}

		return TreeDiffType.EQUAL;
	}

	/**
	 * Base class for all entries of this provider.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	protected static class BaseEntry {

		private String labelTextPrefix;
		private BaseEntry parent;

		/**
		 * @param parent
		 * @param labelTextPrefix
		 *            the prefix for the label of this entry.
		 */
		public BaseEntry(BaseEntry parent, String labelTextPrefix) {
			super();
			this.parent = parent;
			this.labelTextPrefix = labelTextPrefix;
		}

		/**
		 * @return the label prefix
		 */
		public String getLabelTextPrefix() {
			return labelTextPrefix;
		}

		/**
		 * @return the parent
		 */
		public BaseEntry getParent() {
			return parent;
		}

		/**
		 * @param parent
		 *            the parent to set
		 */
		public void setParent(BaseEntry parent) {
			this.parent = parent;
		}

	}

	/**
	 * Base class for all entries that are related to a Comparison.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	protected static class EntryWithComparison extends BaseEntry {

		private Comparison comparison;

		/**
		 * 
		 * @param parent
		 *            the parent entry or null if no parent exists.
		 * @param labelTextPrefix
		 *            the prefix for the label of this entry.
		 * @param comparison
		 *            the comparison used to create this entry.
		 */
		public EntryWithComparison(BaseEntry parent, String labelTextPrefix, Comparison comparison) {
			super(parent, labelTextPrefix);
			this.comparison = comparison;
		}

		/**
		 * @return the comparison used to create this entry.
		 */
		public Comparison getComparison() {
			return comparison;
		}
	}

	/**
	 * An {@link BaseEntry} that holds a number of child {@link BaseEntry}s.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	protected static class ContainerEntry extends BaseEntry {

		private List<BaseEntry> childEntries;
		private String leftLabel;
		private String rightLabel;

		/**
		 * 
		 * @param parent
		 *            the parent entry or null if no parent exists.
		 * @param leftLabel
		 *            the label shown on the left side.
		 * @param rightLabel
		 *            the label shown on the right side.
		 * @param childEntries
		 *            the list of all child entries. Parent references of the
		 *            given entries will be updated to reference the new
		 *            container entry.
		 */
		public ContainerEntry(BaseEntry parent, String leftLabel, String rightLabel, List<BaseEntry> childEntries) {
			super(parent, "");
			this.childEntries = childEntries;
			this.leftLabel = leftLabel;
			this.rightLabel = rightLabel;
			for (BaseEntry entry : childEntries) {
				entry.setParent(this);
			}

		}

		/**
		 * @return the label of the left side.
		 */
		public String getLeftLabel() {
			return leftLabel;
		}

		/**
		 * @return the label of the right side.
		 */
		public String getRightLabel() {
			return rightLabel;
		}

		/**
		 * @return the list of all child entries. The caller is responsible for
		 *         maintaining the parent references if the returned list is
		 *         changed.
		 */
		public List<BaseEntry> getChildEntries() {
			return childEntries;
		}

	}

	/**
	 * A {@link BaseEntry} that represents a selection of a diagram element,
	 * containing its model element, notation element and other contextual
	 * notation elements.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	protected static class SelectionEntry extends BaseEntry {

		private Match modelElementMatch;

		private Match notationElementMatch;

		private List<Match> otherNotationElementMatches = new ArrayList<>();

		public SelectionEntry(BaseEntry parent, String labelTextPrefix, Match modelElementMatch,
				Match notationElementMatch) {
			super(parent, labelTextPrefix);
			this.modelElementMatch = modelElementMatch;
			this.notationElementMatch = notationElementMatch;
		}

		/**
		 * @return the modelElement
		 */
		public Match getModelElementMatch() {
			return modelElementMatch;
		}

		/**
		 * @return the notationElement
		 */
		public Match getNotationElementMatch() {
			return notationElementMatch;
		}

		/**
		 * @return the otherNotationElements
		 */
		public List<Match> getOtherNotationElementMatches() {
			return otherNotationElementMatches;
		}
	}

	/**
	 * An {@link EntryWithComparison} that represents a matched EObject that was
	 * discovered through a referencing {@link EStructuralFeature}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	protected static class MatchEntry extends EntryWithComparison {

		private EStructuralFeature referencingFeature;

		private Match match;

		private DiffCache diffs;

		public MatchEntry(BaseEntry parent, String labelTextPrefix, Match match, EStructuralFeature referencingFeature,
				DiffCache diffs) {

			super(parent, labelTextPrefix, match.getComparison());
			this.match = match;
			this.referencingFeature = referencingFeature;
			this.diffs = diffs;

		}

		/**
		 * @return the element
		 */
		public Match getMatch() {
			return match;
		}

		/**
		 * @return the feature that references the EObjects in the match
		 */
		public EStructuralFeature getReferencingFeature() {
			return referencingFeature;
		}

		public DiffCache getDiffs() {
			return diffs;
		}

	}

	/**
	 * A {@link MatchEntry} that represents a match whose values have been
	 * moved.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	protected static class MovedMatchEntry extends MatchEntry {

		private TreeDiffSide side;

		public MovedMatchEntry(BaseEntry parent, String labelTextPrefix, Match match,
				EStructuralFeature referencingFeature, DiffCache diffs, TreeDiffSide side) {

			super(parent, labelTextPrefix, match, referencingFeature, diffs);
			this.side = side;

		}

		public TreeDiffSide getSide() {
			return side;
		}

	}

	/**
	 * An {@link EntryWithComparison} that represents java objects on the left
	 * and right side that are related.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	protected static class ObjectEntry extends EntryWithComparison {

		private Object left;
		private Object right;

		public ObjectEntry(BaseEntry parent, String labelTextPrefix, Comparison comparison, Object left, Object right) {
			super(parent, labelTextPrefix, comparison);
			this.left = left;
			this.right = right;
		}

		/**
		 * @return the left object
		 */
		public Object getLeft() {
			return left;
		}

		/**
		 * @return the right object
		 */
		public Object getRight() {
			return right;
		}

	}

	/**
	 * A {@link BaseEntry} that contains a list of child {@link BaseEntry}s.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	protected static class ListEntry extends BaseEntry {

		private Image image;
		private List<BaseEntry> elementList = new ArrayList<>();

		public ListEntry(BaseEntry parent, String labelTextPrefix, Image image) {
			super(parent, labelTextPrefix);
			this.image = image;
		}

		/**
		 * @return the image
		 */
		public Image getImage() {
			return image;
		}

		/**
		 * @return the elementList
		 */
		public List<BaseEntry> getElementList() {
			return elementList;
		}

	}
}
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
package at.bitandart.zoubek.mervin.property.diff;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;

import at.bitandart.zoubek.mervin.property.diff.PropertyDiffItemProvider.BaseEntry;
import at.bitandart.zoubek.mervin.property.diff.PropertyDiffItemProvider.ListEntry;
import at.bitandart.zoubek.mervin.property.diff.PropertyDiffItemProvider.MatchEntry;
import at.bitandart.zoubek.mervin.property.diff.PropertyDiffItemProvider.ObjectEntry;
import at.bitandart.zoubek.mervin.property.diff.PropertyDiffItemProvider.ReferencingDiffCache;
import at.bitandart.zoubek.mervin.swt.diff.tree.TreeDiffSide;
import at.bitandart.zoubek.mervin.swt.diff.tree.TreeDiffType;
import at.bitandart.zoubek.mervin.testmodel.Avatar;
import at.bitandart.zoubek.mervin.testmodel.TestModelFactory;
import at.bitandart.zoubek.mervin.testmodel.TestModelPackage;
import at.bitandart.zoubek.mervin.testmodel.TextEntry;
import at.bitandart.zoubek.mervin.testmodel.TodoEntry;
import at.bitandart.zoubek.mervin.testmodel.TodoList;
import at.bitandart.zoubek.mervin.testmodel.User;

/**
 * A JUnit test that tests {@link PropertyDiffItemProvider}.
 * 
 * @author Florian Zoubek
 * 
 */
public class PropertyDiffItemProviderTest {

	private TestModelFactory modelFactory;

	@Before
	public void setUp() {
		modelFactory = TestModelFactory.eINSTANCE;
	}

	/* # getChildren() - Tests # */

	/* ## MatchEntry ## */

	/* ### Mono-Valued-Attribute ### */

	@Test
	public void testGetChildren_MatchEntry_equal_monoValued_Attribute() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";

		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);

		TextEntry rightTextEntry = modelFactory.createTextEntry();
		rightTextEntry.setTitle(title);

		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		Object[] entries = itemProvider.getChildren(matchEntry);

		assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
	}

	@Test
	public void testGetChildren_MatchEntry_addedRight_monoValued_Attribute() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";

		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);
		leftTextEntry.setDescription(null);

		TextEntry rightTextEntry = modelFactory.createTextEntry();
		rightTextEntry.setTitle(title);
		rightTextEntry.setDescription("Description");

		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		Object[] entries = itemProvider.getChildren(matchEntry);

		assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
	}

	@Test
	public void testGetChildren_MatchEntry_deletedRight_monoValued_Attribute() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";

		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);
		leftTextEntry.setDescription("Description");

		TextEntry rightTextEntry = modelFactory.createTextEntry();
		rightTextEntry.setTitle(title);
		rightTextEntry.setDescription(null);

		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		Object[] entries = itemProvider.getChildren(matchEntry);

		assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
	}

	@Test
	public void testGetChildren_MatchEntry_modified_monoValued_Attribute() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";

		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);
		leftTextEntry.setDescription("Description");

		TextEntry rightTextEntry = modelFactory.createTextEntry();
		rightTextEntry.setTitle(title);
		rightTextEntry.setDescription("Description2");

		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		Object[] entries = itemProvider.getChildren(matchEntry);

		assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
	}

	// TODO add tests for Multi-Valued-Attributes

	/* ### Mono-Valued-Reference ### */

	@Test
	public void testGetChildren_MatchEntry_equal_monoValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left
		User leftUser = modelFactory.createUser();
		leftUser.setName("User");

		Avatar leftAvatar = modelFactory.createAvatar();
		leftUser.setAvatar(leftAvatar);

		// right

		User rightUser = EcoreUtil.copy(leftUser);

		// compare
		Comparison comparison = compare(leftUser, rightUser);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);
		UserChildren children = assertUserChildren(leftUser, rightUser, entries, 1);
		assertMatchEntry(children.singleAvatarEntry, leftUser.getAvatar(), rightUser.getAvatar(), matchEntry);
	}

	@Test
	public void testGetChildren_MatchEntry_addedRight_monoValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left

		User leftUser = modelFactory.createUser();
		leftUser.setName("User");

		// right

		User rightUser = EcoreUtil.copy(leftUser);
		Avatar rightAvatar = modelFactory.createAvatar();
		leftUser.setAvatar(rightAvatar);

		// compare
		Comparison comparison = compare(leftUser, rightUser);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);
		UserChildren children = assertUserChildren(leftUser, rightUser, entries, 1);
		assertMatchEntry(children.singleAvatarEntry, leftUser.getAvatar(), rightUser.getAvatar(), matchEntry);
	}

	@Test
	public void testGetChildren_MatchEntry_deletedRight_monoValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left
		User leftUser = modelFactory.createUser();
		leftUser.setName("User");

		Avatar leftAvatar = modelFactory.createAvatar();
		leftUser.setAvatar(leftAvatar);

		// right

		User rightUser = EcoreUtil.copy(leftUser);
		rightUser.setAvatar(null);

		// compare
		Comparison comparison = compare(leftUser, rightUser);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);
		UserChildren children = assertUserChildren(leftUser, rightUser, entries, 1);
		assertMatchEntry(children.singleAvatarEntry, leftUser.getAvatar(), rightUser.getAvatar(), matchEntry);
	}

	@Test
	public void testGetChildren_MatchEntry_modified_monoValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left
		User leftUser = modelFactory.createUser();
		leftUser.setName("User");

		Avatar leftAvatar = modelFactory.createAvatar();
		leftAvatar.setUri("xyz://here");
		leftUser.setAvatar(leftAvatar);

		// right

		User rightUser = EcoreUtil.copy(leftUser);
		rightUser.getAvatar().setUri("xyz://somewhere");

		// compare
		Comparison comparison = compare(leftUser, rightUser);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);
		UserChildren children = assertUserChildren(leftUser, rightUser, entries, 1);
		assertMatchEntry(children.singleAvatarEntry, leftUser.getAvatar(), rightUser.getAvatar(), matchEntry);
	}

	@Test
	public void testGetChildren_MatchEntry_move_monoValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left
		User leftUser = modelFactory.createUser();
		leftUser.setName("User");

		Avatar leftAvatar = modelFactory.createAvatar();
		leftAvatar.setUri("xyz://here");
		leftUser.setAvatar(leftAvatar);

		User leftUser2 = modelFactory.createUser();

		ResourceSet leftResourceSet = new ResourceSetImpl();
		Resource leftResource = leftResourceSet.createResource(URI.createURI("test.model"));
		leftResource.getContents().add(leftUser);
		leftResource.getContents().add(leftUser2);

		// right

		User rightUser = EcoreUtil.copy(leftUser);
		User rightUser2 = EcoreUtil.copy(leftUser2);
		rightUser2.setAvatar(rightUser.getAvatar());
		rightUser.setAvatar(null);

		ResourceSet rightResourceSet = new ResourceSetImpl();
		Resource rightResource = rightResourceSet.createResource(URI.createURI("test.model"));
		rightResource.getContents().add(rightUser);
		rightResource.getContents().add(rightUser2);

		// compare
		Comparison comparison = compare(leftResource, rightResource);
		Match match = comparison.getMatches().get(0);
		Match match2 = comparison.getMatches().get(1);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));
		MatchEntry matchEntry2 = new PropertyDiffItemProvider.MatchEntry(null, "", match2, null,
				new ReferencingDiffCache(match2));

		// retrieve and check entries for the first match

		Object[] entries = itemProvider.getChildren(matchEntry);
		UserChildren children = assertUserChildren(leftUser, rightUser, entries, 1);
		assertMatchEntry(children.singleAvatarEntry, leftUser.getAvatar(), rightUser2.getAvatar(), matchEntry);

		// retrieve and check entries for the second match

		entries = itemProvider.getChildren(matchEntry2);
		children = assertUserChildren(leftUser2, rightUser2, entries, 1);
		assertMatchEntry(children.singleAvatarEntry, leftUser.getAvatar(), rightUser2.getAvatar(), matchEntry2);

	}

	/* #### Multi-Valued-Reference - tests #### */

	@Test
	public void testGetChildren_MatchEntry_equal_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";

		// left
		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);

		EList<TodoEntry> leftSubentries = leftTextEntry.getSubentries();
		TextEntry subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.1");
		leftSubentries.add(subEntry);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.2");
		leftSubentries.add(subEntry);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.3");
		leftSubentries.add(subEntry);

		// right
		TextEntry rightTextEntry = EcoreUtil.copy(leftTextEntry);

		// compare
		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);

		// check entries
		TextEntryChildren children = assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
		List<BaseEntry> elementList = children.subEntriesEntry.getElementList();

		assertThat(elementList.size(), is(3));

		for (int i = 0; i < 3; i++) {
			assertThat(elementList.get(i), instanceOf(MatchEntry.class));
			assertMatchEntry((MatchEntry) elementList.get(i), leftTextEntry.getSubentries().get(i),
					rightTextEntry.getSubentries().get(i), children.subEntriesEntry);
		}
	}

	/* #### Multi-Valued-Reference - Deletion #### */

	@Test
	public void testGetChildren_MatchEntry_deletedOneRight_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";
		int numEntries = 9;
		boolean deletedIndices[] = new boolean[numEntries];

		for (int i = 0; i < numEntries; i++) {
			deletedIndices[i] = i == 3;
		}

		performSimpleTextEntryDeleteTest(itemProvider, title, numEntries, deletedIndices);
	}

	@Test
	public void testGetChildren_MatchEntry_deletedAllRight_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";
		int numEntries = 9;
		boolean deletedIndices[] = new boolean[numEntries];

		for (int i = 0; i < numEntries; i++) {
			deletedIndices[i] = true;
		}

		performSimpleTextEntryDeleteTest(itemProvider, title, numEntries, deletedIndices);
	}

	@Test
	public void testGetChildren_MatchEntry_deletedRangeRight_inBetween_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";
		int numEntries = 9;
		boolean deletedIndices[] = new boolean[numEntries];

		for (int i = 0; i < numEntries; i++) {
			deletedIndices[i] = i > 2 && i < 6;
		}

		performSimpleTextEntryDeleteTest(itemProvider, title, numEntries, deletedIndices);
	}

	@Test
	public void testGetChildren_MatchEntry_deletedRangeRight_atBeginning_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";
		int numEntries = 9;
		boolean deletedIndices[] = new boolean[numEntries];

		for (int i = 0; i < numEntries; i++) {
			deletedIndices[i] = i < 3;
		}

		performSimpleTextEntryDeleteTest(itemProvider, title, numEntries, deletedIndices);
	}

	@Test
	public void testGetChildren_MatchEntry_deletedRangeRight_atEnd_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";
		int numEntries = 9;
		boolean deletedIndices[] = new boolean[numEntries];

		for (int i = 0; i < numEntries; i++) {
			deletedIndices[i] = i > 5;
		}

		performSimpleTextEntryDeleteTest(itemProvider, title, numEntries, deletedIndices);
	}

	@Test
	public void testGetChildren_MatchEntry_deletedMulipleRight_inBetween_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";
		int numEntries = 9;
		boolean deletedIndices[] = new boolean[numEntries];

		for (int i = 0; i < numEntries; i++) {
			deletedIndices[i] = i == 1 || i == 5 || i == 7;
		}

		performSimpleTextEntryDeleteTest(itemProvider, title, numEntries, deletedIndices);
	}

	private void performSimpleTextEntryDeleteTest(PropertyDiffItemProvider itemProvider, String title, int numEntries,
			boolean[] deletedIndices) {
		// left
		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);

		EList<TodoEntry> leftSubentries = leftTextEntry.getSubentries();
		TextEntry subEntry = null;

		for (int i = 1; i <= numEntries; i++) {
			subEntry = modelFactory.createTextEntry();
			subEntry.setTitle(title + "1." + i);
			leftSubentries.add(subEntry);
		}

		// right
		TextEntry rightTextEntry = EcoreUtil.copy(leftTextEntry);
		for (int i = numEntries - 1; i >= 0; i--) {
			if (deletedIndices[i]) {
				rightTextEntry.getSubentries().remove(i);
			}
		}

		// compare
		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);

		// check entries
		TextEntryChildren children = assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
		List<BaseEntry> elementList = children.subEntriesEntry.getElementList();

		assertThat(elementList.size(), is(leftSubentries.size()));

		for (int leftIndex = 0, rightIndex = 0; leftIndex < leftSubentries.size(); leftIndex++) {

			if (deletedIndices[leftIndex]) {

				// added left, removed right
				assertThat(elementList.get(leftIndex), instanceOf(MatchEntry.class));
				assertMatchEntry((MatchEntry) elementList.get(leftIndex), leftTextEntry.getSubentries().get(leftIndex),
						null, children.subEntriesEntry);

			} else {

				// equal
				assertThat(elementList.get(leftIndex), instanceOf(MatchEntry.class));
				assertMatchEntry((MatchEntry) elementList.get(leftIndex), leftTextEntry.getSubentries().get(leftIndex),
						rightTextEntry.getSubentries().get(rightIndex), children.subEntriesEntry);
				rightIndex++;
			}
		}
	}

	/* #### Multi-Valued-Reference - Addition #### */

	@Test
	public void testGetChildren_MatchEntry_addedOneRight_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";
		int numEntries = 9;

		boolean addedIndices[] = new boolean[numEntries];

		for (int i = 0; i < numEntries; i++) {
			addedIndices[i] = i == 3;
		}

		performSimpleTextEntryAddTest(itemProvider, title, addedIndices);
	}

	@Test
	public void testGetChildren_MatchEntry_addedMultipleRight_emptyLeft_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";
		int numEntries = 9;

		boolean addedIndices[] = new boolean[numEntries];

		for (int i = 0; i < numEntries; i++) {
			addedIndices[i] = true;
		}

		performSimpleTextEntryAddTest(itemProvider, title, addedIndices);
	}

	@Test
	public void testGetChildren_MatchEntry_addedRangeRight_inBetween_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";
		int numEntries = 9;

		boolean addedIndices[] = new boolean[numEntries];

		for (int i = 0; i < numEntries; i++) {
			addedIndices[i] = i > 2 && i < 6;
		}

		performSimpleTextEntryAddTest(itemProvider, title, addedIndices);
	}

	private void performSimpleTextEntryAddTest(PropertyDiffItemProvider itemProvider, String title,
			boolean[] addedIndices) {
		TextEntry subEntry = null;
		// left
		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);

		for (int i = 1; i <= addedIndices.length; i++) {
			if (!addedIndices[i - 1]) {
				subEntry = modelFactory.createTextEntry();
				subEntry.setTitle(title + "1." + i);
				leftTextEntry.getSubentries().add(subEntry);
			}
		}

		// right
		TextEntry rightTextEntry = EcoreUtil.copy(leftTextEntry);

		for (int i = 1; i <= addedIndices.length; i++) {
			if (addedIndices[i - 1]) {
				subEntry = modelFactory.createTextEntry();
				subEntry.setTitle(title + "1." + i);
				rightTextEntry.getSubentries().add(i - 1, subEntry);
			}
		}

		// compare
		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);

		// check entries
		TextEntryChildren children = assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
		List<BaseEntry> elementList = children.subEntriesEntry.getElementList();

		assertThat(elementList.size(), is(rightTextEntry.getSubentries().size()));

		for (int rightIndex = 0, leftIndex = 0; rightIndex < addedIndices.length; rightIndex++) {

			if (addedIndices[rightIndex]) {

				// removed left, added right
				assertThat(elementList.get(rightIndex), instanceOf(MatchEntry.class));
				assertMatchEntry((MatchEntry) elementList.get(rightIndex), null,
						rightTextEntry.getSubentries().get(rightIndex), children.subEntriesEntry);

			} else {

				assertThat(elementList.get(rightIndex), instanceOf(MatchEntry.class));
				assertMatchEntry((MatchEntry) elementList.get(rightIndex), leftTextEntry.getSubentries().get(leftIndex),
						rightTextEntry.getSubentries().get(rightIndex), children.subEntriesEntry);
				leftIndex++;
			}
		}
	}

	@Test
	public void testGetChildren_MatchEntry_addedRangeRight_atBeginning_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";
		int numEntries = 9;

		boolean addedIndices[] = new boolean[numEntries];

		for (int i = 0; i < numEntries; i++) {
			addedIndices[i] = i < 3;
		}

		performSimpleTextEntryAddTest(itemProvider, title, addedIndices);
	}

	@Test
	public void testGetChildren_MatchEntry_addedRangeRight_atEnd_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";
		int numEntries = 9;

		boolean addedIndices[] = new boolean[numEntries];

		for (int i = 0; i < numEntries; i++) {
			addedIndices[i] = i > 5;
		}

		performSimpleTextEntryAddTest(itemProvider, title, addedIndices);
	}

	@Test
	public void testGetChildren_MatchEntry_addedMultipleRight_inBetween_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";
		int numEntries = 9;

		boolean addedIndices[] = new boolean[numEntries];

		for (int i = 0; i < numEntries; i++) {
			addedIndices[i] = i == 1 || i == 5 || i == 7;
		}

		performSimpleTextEntryAddTest(itemProvider, title, addedIndices);
	}

	/* #### Multi-Valued-Reference - add & deletion */

	@Test
	public void testGetChildren_MatchEntry_addDelete_sameIndex_middle_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";

		// left
		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);

		EList<TodoEntry> leftSubentries = leftTextEntry.getSubentries();
		TextEntry subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.1");
		leftSubentries.add(subEntry);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle("Deleted");
		leftSubentries.add(subEntry);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.3");
		leftSubentries.add(subEntry);

		// right
		TextEntry rightTextEntry = EcoreUtil.copy(leftTextEntry);

		rightTextEntry.getSubentries().remove(1);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle("New");
		rightTextEntry.getSubentries().add(1, subEntry);

		// compare
		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);

		// check entries
		TextEntryChildren children = assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
		List<BaseEntry> elementList = children.subEntriesEntry.getElementList();

		assertThat(elementList.size(), is(4));

		// first entry -> equal
		assertThat(elementList.get(0), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(0), leftTextEntry.getSubentries().get(0),
				rightTextEntry.getSubentries().get(0), children.subEntriesEntry);

		// second entry -> delete
		assertThat(elementList.get(1), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(1), leftTextEntry.getSubentries().get(1), null,
				children.subEntriesEntry);

		// third entry -> add
		assertThat(elementList.get(2), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(2), null, rightTextEntry.getSubentries().get(1),
				children.subEntriesEntry);

		// fourth entry -> equal
		assertThat(elementList.get(3), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(3), leftTextEntry.getSubentries().get(2),
				rightTextEntry.getSubentries().get(2), children.subEntriesEntry);
	}

	@Test
	public void testGetChildren_MatchEntry_addDelete_sameIndex_start_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";

		// left
		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);

		EList<TodoEntry> leftSubentries = leftTextEntry.getSubentries();
		TextEntry subEntry = modelFactory.createTextEntry();
		subEntry.setTitle("Deleted");
		leftSubentries.add(subEntry);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.2");
		leftSubentries.add(subEntry);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.3");
		leftSubentries.add(subEntry);

		// right
		TextEntry rightTextEntry = EcoreUtil.copy(leftTextEntry);

		rightTextEntry.getSubentries().remove(0);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle("New");
		rightTextEntry.getSubentries().add(0, subEntry);

		// compare
		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);

		// check entries
		TextEntryChildren children = assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
		List<BaseEntry> elementList = children.subEntriesEntry.getElementList();

		assertThat(elementList.size(), is(4));

		// first entry -> delete
		assertThat(elementList.get(0), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(0), leftTextEntry.getSubentries().get(0), null,
				children.subEntriesEntry);

		// second entry -> add
		assertThat(elementList.get(1), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(1), null, rightTextEntry.getSubentries().get(0),
				children.subEntriesEntry);

		// third entry -> equal
		assertThat(elementList.get(2), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(2), leftTextEntry.getSubentries().get(1),
				rightTextEntry.getSubentries().get(1), children.subEntriesEntry);

		// fourth entry -> equal
		assertThat(elementList.get(3), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(3), leftTextEntry.getSubentries().get(2),
				rightTextEntry.getSubentries().get(2), children.subEntriesEntry);
	}

	@Test
	public void testGetChildren_MatchEntry_addDelete_sameIndex_end_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";

		// left
		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);

		EList<TodoEntry> leftSubentries = leftTextEntry.getSubentries();
		TextEntry subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.1");
		leftSubentries.add(subEntry);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.2");
		leftSubentries.add(subEntry);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle("Deleted");
		leftSubentries.add(subEntry);

		// right
		TextEntry rightTextEntry = EcoreUtil.copy(leftTextEntry);

		rightTextEntry.getSubentries().remove(2);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle("New");
		rightTextEntry.getSubentries().add(2, subEntry);

		// compare
		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);

		// check entries
		TextEntryChildren children = assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
		List<BaseEntry> elementList = children.subEntriesEntry.getElementList();

		assertThat(elementList.size(), is(4));

		// first entry -> equal
		assertThat(elementList.get(0), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(0), leftTextEntry.getSubentries().get(0),
				rightTextEntry.getSubentries().get(0), children.subEntriesEntry);

		// second entry -> equal
		assertThat(elementList.get(1), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(1), leftTextEntry.getSubentries().get(1),
				rightTextEntry.getSubentries().get(1), children.subEntriesEntry);

		// third entry -> delete
		assertThat(elementList.get(2), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(2), leftTextEntry.getSubentries().get(2), null,
				children.subEntriesEntry);

		// fourth entry -> add
		assertThat(elementList.get(3), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(3), null, rightTextEntry.getSubentries().get(2),
				children.subEntriesEntry);

	}

	/* #### Multi-Valued-Reference - move #### */

	@Test
	public void testGetChildren_MatchEntry_move_sameContainer_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";

		// left
		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);

		EList<TodoEntry> leftSubentries = leftTextEntry.getSubentries();
		TextEntry subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.1");
		leftSubentries.add(subEntry);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.2");
		leftSubentries.add(subEntry);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.3");
		leftSubentries.add(subEntry);

		// right
		TextEntry rightTextEntry = EcoreUtil.copy(leftTextEntry);

		rightTextEntry.getSubentries().move(2, 0);

		// compare
		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);

		// check entries
		TextEntryChildren children = assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
		List<BaseEntry> elementList = children.subEntriesEntry.getElementList();

		assertThat(elementList.size(), is(4));

		// first entry -> moved, represents old entry
		assertThat(elementList.get(0), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(0), leftTextEntry.getSubentries().get(0),
				rightTextEntry.getSubentries().get(2), children.subEntriesEntry);

		// second entry -> equal
		assertThat(elementList.get(1), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(1), leftTextEntry.getSubentries().get(1),
				rightTextEntry.getSubentries().get(0), children.subEntriesEntry);

		// third entry -> equal
		assertThat(elementList.get(2), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(2), leftTextEntry.getSubentries().get(2),
				rightTextEntry.getSubentries().get(1), children.subEntriesEntry);

		// fourth entry -> moved, represents new entry
		assertThat(elementList.get(3), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(3), leftTextEntry.getSubentries().get(0),
				rightTextEntry.getSubentries().get(2), children.subEntriesEntry);
	}

	@Test
	public void testGetChildren_MatchEntry_move_otherContainer_multiValued_containmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title 1";
		String title2 = "Title 2";

		// left
		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);

		EList<TodoEntry> leftSubentries = leftTextEntry.getSubentries();
		TextEntry subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.1");
		leftSubentries.add(subEntry);

		TextEntry leftTextEntry2 = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title2);

		ResourceSet leftResourceSet = new ResourceSetImpl();
		Resource leftResource = leftResourceSet.createResource(URI.createURI("test.model"));
		leftResource.getContents().add(leftTextEntry);
		leftResource.getContents().add(leftTextEntry2);

		// right
		TextEntry rightTextEntry = EcoreUtil.copy(leftTextEntry);
		TextEntry rightTextEntry2 = EcoreUtil.copy(leftTextEntry2);
		rightTextEntry2.getSubentries().add(rightTextEntry.getSubentries().remove(0));

		ResourceSet rightResourceSet = new ResourceSetImpl();
		Resource rightResource = rightResourceSet.createResource(URI.createURI("test.model"));
		rightResource.getContents().add(rightTextEntry);
		rightResource.getContents().add(rightTextEntry2);

		// compare
		Comparison comparison = compare(leftResource, rightResource);
		Match match = comparison.getMatches().get(0);
		Match match2 = comparison.getMatches().get(1);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));
		MatchEntry matchEntry2 = new PropertyDiffItemProvider.MatchEntry(null, "", match2, null,
				new ReferencingDiffCache(match2));

		// retrieve entries for the first match

		Object[] entries = itemProvider.getChildren(matchEntry);

		// check entries for first match
		TextEntryChildren children = assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
		List<BaseEntry> elementList = children.subEntriesEntry.getElementList();

		assertThat(elementList.size(), is(1));

		// first entry -> moved, represents old entry
		assertThat(elementList.get(0), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(0), leftTextEntry.getSubentries().get(0),
				rightTextEntry2.getSubentries().get(0), children.subEntriesEntry);

		// retrieve entries for the second match

		entries = itemProvider.getChildren(matchEntry2);

		// check entries for second match
		children = assertTextEntryChildren(leftTextEntry2, rightTextEntry2, matchEntry2, entries);
		elementList = children.subEntriesEntry.getElementList();

		assertThat(elementList.size(), is(1));

		// first entry -> moved, represents new entry
		assertThat(elementList.get(0), instanceOf(MatchEntry.class));
		assertMatchEntry((MatchEntry) elementList.get(0), leftTextEntry.getSubentries().get(0),
				rightTextEntry2.getSubentries().get(0), children.subEntriesEntry);
	}

	/* # getDiffItemType() - Tests # */

	/* ## MatchEntry ## */

	@Test
	public void testGetDiffItemType_MatchEntry_equal_simple() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left
		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle("Title");

		// right
		TextEntry rightTextEntry = EcoreUtil.copy(leftTextEntry);

		// compare
		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// check type
		assertThat(itemProvider.getTreeDiffType(matchEntry, TreeDiffSide.LEFT), is(TreeDiffType.EQUAL));
		assertThat(itemProvider.getTreeDiffType(matchEntry, TreeDiffSide.RIGHT), is(TreeDiffType.EQUAL));
	}

	@Test
	public void testGetDiffItemType_MatchEntry_addRight_simple() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left

		ResourceSet leftResourceSet = new ResourceSetImpl();
		Resource leftResource = leftResourceSet.createResource(URI.createURI("test.model"));

		// right
		TextEntry rightTextEntry = modelFactory.createTextEntry();
		rightTextEntry.setTitle("Title");
		ResourceSet rightResourceSet = new ResourceSetImpl();
		Resource rightResource = rightResourceSet.createResource(URI.createURI("test.model"));
		rightResource.getContents().add(rightTextEntry);

		// compare
		Comparison comparison = compare(leftResource, rightResource);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// check type
		assertThat(itemProvider.getTreeDiffType(matchEntry, TreeDiffSide.LEFT), is(TreeDiffType.DELETE));
		assertThat(itemProvider.getTreeDiffType(matchEntry, TreeDiffSide.RIGHT), is(TreeDiffType.ADD));
	}

	@Test
	public void testGetDiffItemType_MatchEntry_deleteRight_simple() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left
		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle("Title");

		ResourceSet leftResourceSet = new ResourceSetImpl();
		Resource leftResource = leftResourceSet.createResource(URI.createURI("test.model"));
		leftResource.getContents().add(leftTextEntry);

		// right
		ResourceSet rightResourceSet = new ResourceSetImpl();
		Resource rightResource = rightResourceSet.createResource(URI.createURI("test.model"));

		// compare
		Comparison comparison = compare(leftResource, rightResource);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// check type
		assertThat(itemProvider.getTreeDiffType(matchEntry, TreeDiffSide.LEFT), is(TreeDiffType.ADD));
		assertThat(itemProvider.getTreeDiffType(matchEntry, TreeDiffSide.RIGHT), is(TreeDiffType.DELETE));
	}

	@Test
	public void testGetDiffItemType_MatchEntry_move_sameContainer() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title";

		// left
		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);

		EList<TodoEntry> leftSubentries = leftTextEntry.getSubentries();
		TextEntry subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.1");
		leftSubentries.add(subEntry);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.2");
		leftSubentries.add(subEntry);

		subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.3");
		leftSubentries.add(subEntry);

		// right
		TextEntry rightTextEntry = EcoreUtil.copy(leftTextEntry);

		rightTextEntry.getSubentries().move(2, 0);

		// compare
		Comparison comparison = compare(leftTextEntry, rightTextEntry);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);

		// check entries
		TextEntryChildren children = assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
		List<BaseEntry> elementList = children.subEntriesEntry.getElementList();

		assertThat(elementList.size(), is(4));

		// first entry -> moved, represents old entry
		assertThat(elementList.get(0), instanceOf(MatchEntry.class));
		assertThat(itemProvider.getTreeDiffType(elementList.get(0), TreeDiffSide.LEFT), is(TreeDiffType.ADD));
		assertThat(itemProvider.getTreeDiffType(elementList.get(0), TreeDiffSide.RIGHT), is(TreeDiffType.DELETE));

		// fourth entry -> moved, represents new entry
		assertThat(elementList.get(3), instanceOf(MatchEntry.class));
		assertThat(itemProvider.getTreeDiffType(elementList.get(3), TreeDiffSide.LEFT), is(TreeDiffType.DELETE));
		assertThat(itemProvider.getTreeDiffType(elementList.get(3), TreeDiffSide.RIGHT), is(TreeDiffType.ADD));
	}

	@Test
	public void testGetDiffItemType_MatchEntry_move_differentContainers() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		String title = "Title 1";
		String title2 = "Title 2";

		// left
		TextEntry leftTextEntry = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title);

		EList<TodoEntry> leftSubentries = leftTextEntry.getSubentries();
		TextEntry subEntry = modelFactory.createTextEntry();
		subEntry.setTitle(title + "1.1");
		leftSubentries.add(subEntry);

		TextEntry leftTextEntry2 = modelFactory.createTextEntry();
		leftTextEntry.setTitle(title2);

		ResourceSet leftResourceSet = new ResourceSetImpl();
		Resource leftResource = leftResourceSet.createResource(URI.createURI("test.model"));
		leftResource.getContents().add(leftTextEntry);
		leftResource.getContents().add(leftTextEntry2);

		// right
		TextEntry rightTextEntry = EcoreUtil.copy(leftTextEntry);
		TextEntry rightTextEntry2 = EcoreUtil.copy(leftTextEntry2);
		rightTextEntry2.getSubentries().add(rightTextEntry.getSubentries().remove(0));

		ResourceSet rightResourceSet = new ResourceSetImpl();
		Resource rightResource = rightResourceSet.createResource(URI.createURI("test.model"));
		rightResource.getContents().add(rightTextEntry);
		rightResource.getContents().add(rightTextEntry2);

		// compare
		Comparison comparison = compare(leftResource, rightResource);
		Match match = comparison.getMatches().get(0);
		Match match2 = comparison.getMatches().get(1);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match, null,
				new ReferencingDiffCache(match));
		MatchEntry matchEntry2 = new PropertyDiffItemProvider.MatchEntry(null, "", match2, null,
				new ReferencingDiffCache(match2));

		// retrieve entries for the first match

		Object[] entries = itemProvider.getChildren(matchEntry);

		// check entries for first match
		TextEntryChildren children = assertTextEntryChildren(leftTextEntry, rightTextEntry, matchEntry, entries);
		List<BaseEntry> elementList = children.subEntriesEntry.getElementList();

		assertThat(elementList.size(), is(1));

		// first entry -> moved, represents old entry
		assertThat(elementList.get(0), instanceOf(MatchEntry.class));
		assertThat(itemProvider.getTreeDiffType(elementList.get(0), TreeDiffSide.LEFT), is(TreeDiffType.ADD));
		assertThat(itemProvider.getTreeDiffType(elementList.get(0), TreeDiffSide.RIGHT), is(TreeDiffType.DELETE));

		// retrieve entries for the second match

		entries = itemProvider.getChildren(matchEntry2);

		// check entries for second match
		children = assertTextEntryChildren(leftTextEntry2, rightTextEntry2, matchEntry2, entries);
		elementList = children.subEntriesEntry.getElementList();

		assertThat(elementList.size(), is(1));

		// first entry -> moved, represents new entry
		assertThat(elementList.get(0), instanceOf(MatchEntry.class));
		assertThat(itemProvider.getTreeDiffType(elementList.get(0), TreeDiffSide.LEFT), is(TreeDiffType.DELETE));
		assertThat(itemProvider.getTreeDiffType(elementList.get(0), TreeDiffSide.RIGHT), is(TreeDiffType.ADD));
	}

	/* ## ObjectEntry ## */

	@Test
	public void testGetDiffItemType_ObjectEntry_equal_simple() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left
		String left = "Some Text";

		// right
		String right = left;

		ObjectEntry objectEntry = new PropertyDiffItemProvider.ObjectEntry(null, "", null, left, right);

		// check type
		assertThat(itemProvider.getTreeDiffType(objectEntry, TreeDiffSide.LEFT), is(TreeDiffType.EQUAL));
		assertThat(itemProvider.getTreeDiffType(objectEntry, TreeDiffSide.RIGHT), is(TreeDiffType.EQUAL));
	}

	@Test
	public void testGetDiffItemType_ObjectEntry_addRight_simple() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left
		String left = null;

		// right
		String right = "Some Text";

		ObjectEntry objectEntry = new PropertyDiffItemProvider.ObjectEntry(null, "", null, left, right);

		// check type
		assertThat(itemProvider.getTreeDiffType(objectEntry, TreeDiffSide.LEFT), is(TreeDiffType.DELETE));
		assertThat(itemProvider.getTreeDiffType(objectEntry, TreeDiffSide.RIGHT), is(TreeDiffType.ADD));
	}

	@Test
	public void testGetDiffItemType_ObjectEntry_deleteRight_simple() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left
		String left = "Some Text";

		// right
		String right = null;

		ObjectEntry objectEntry = new PropertyDiffItemProvider.ObjectEntry(null, "", null, left, right);

		// check type
		assertThat(itemProvider.getTreeDiffType(objectEntry, TreeDiffSide.LEFT), is(TreeDiffType.ADD));
		assertThat(itemProvider.getTreeDiffType(objectEntry, TreeDiffSide.RIGHT), is(TreeDiffType.DELETE));
	}

	@Test
	public void testGetDiffItemType_ObjectEntry_modify_simple() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left
		String left = "Some Text";

		// right
		String right = "Other Text";

		ObjectEntry objectEntry = new PropertyDiffItemProvider.ObjectEntry(null, "", null, left, right);

		// check type
		assertThat(itemProvider.getTreeDiffType(objectEntry, TreeDiffSide.LEFT), is(TreeDiffType.MODIFY));
		assertThat(itemProvider.getTreeDiffType(objectEntry, TreeDiffSide.RIGHT), is(TreeDiffType.MODIFY));
	}

	/* ### Non-Containment features ### */

	@Test
	public void testGetChildren_MatchEntry_monoValued_nonContainmentReference() {

		PropertyDiffItemProvider itemProvider = new PropertyDiffItemProvider();

		// left

		User leftUser = modelFactory.createUser();
		leftUser.setName("User");

		// right

		User rightUser = EcoreUtil.copy(leftUser);

		// compare
		Comparison comparison = compare(leftUser, rightUser);
		Match match = comparison.getMatches().get(0);

		MatchEntry matchEntry = new PropertyDiffItemProvider.MatchEntry(null, "", match,
				TestModelPackage.Literals.TODO_LIST__OWNER, new ReferencingDiffCache(match));

		// retrieve entries

		Object[] entries = itemProvider.getChildren(matchEntry);
		assertThat(entries.length, is(0));
	}

	// TODO add tests for other PropertyDiffItemProvider methods

	/* # Helper classes # */

	/**
	 * compares the two {@link Notifier}s where the right side is the reference
	 * side (differences are detected on the left side).
	 * 
	 * @param leftNotifier
	 * @param rightNotifier
	 * @return the comparison result.
	 */
	private Comparison compare(Notifier leftNotifier, Notifier rightNotifier) {

		EMFCompare comparator = EMFCompare.builder().build();
		DefaultComparisonScope scope = new DefaultComparisonScope(leftNotifier, rightNotifier, null);

		return comparator.compare(scope);
	}

	/**
	 * Helper class to access parsed child entries of a {@link MatchEntry}
	 * referencing a {@link TextEntry}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private static class TextEntryChildren {
		public ObjectEntry completedEntry;
		public ListEntry subEntriesEntry;
		public ObjectEntry titleEntry;
		public ObjectEntry descriptionEntry;
	}

	/**
	 * Helper class to access parsed child entries of a {@link MatchEntry}
	 * referencing a {@link TodoList}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private static class TodoListChildren {
		public ListEntry entriesEntry;
		public MatchEntry singleOwnerEntry;
		public ListEntry ownerEntry;
		public ListEntry tagsEntry;
		public ListEntry authorizedUsersEntry;
	}

	/**
	 * Helper class to access parsed child entries of a {@link MatchEntry}
	 * referencing a {@link User}.
	 * 
	 * @author Florian Zoubek
	 *
	 */
	private static class UserChildren {
		public ObjectEntry nameEntry;
		public MatchEntry singleAvatarEntry;
		public ListEntry avatarEntry;
	}

	/**
	 * validates the given entries for a {@link MatchEntry} referencing a
	 * {@link TodoList}.
	 * 
	 * @param leftTodoList
	 *            the left {@link TodoList} to match or null.
	 * @param rightTodoList
	 *            the right {@link TodoList} to match or null.
	 * @param entries
	 *            the entries to validate.
	 * @return An helper object to access the children if the validation passes.
	 */
	private TodoListChildren assertTodoListChildren(TodoList leftTodoList, TodoList rightTodoList, Object[] entries,
			int expectedOwnerSize) {

		assertThat(entries.length, is(4));
		assertThat(entries[0], instanceOf(ListEntry.class));
		assertThat(entries[2], instanceOf(ListEntry.class));
		assertThat(entries[3], instanceOf(ListEntry.class));

		TodoListChildren children = new TodoListChildren();

		children.entriesEntry = (ListEntry) entries[0];
		children.tagsEntry = (ListEntry) entries[2];
		children.authorizedUsersEntry = (ListEntry) entries[3];
		if (expectedOwnerSize < 2) {

			assertThat(entries[1], instanceOf(MatchEntry.class));
			children.singleOwnerEntry = (MatchEntry) entries[1];
			assertThat(children.singleOwnerEntry.getLabelTextPrefix(),
					containsString(TestModelPackage.Literals.TODO_LIST__OWNER.getName()));

		} else if (expectedOwnerSize > 0) {

			assertThat(entries[1], instanceOf(ListEntry.class));
			children.ownerEntry = (ListEntry) entries[1];

			assertThat(children.ownerEntry.getElementList().size(), is(expectedOwnerSize));
			assertThat(children.ownerEntry.getLabelTextPrefix(),
					containsString(TestModelPackage.Literals.TODO_LIST__OWNER.getName()));

		}

		assertThat(children.entriesEntry.getLabelTextPrefix(),
				containsString(TestModelPackage.Literals.TODO_LIST__ENTRIES.getName()));
		assertThat(children.tagsEntry.getLabelTextPrefix(),
				containsString(TestModelPackage.Literals.TODO_LIST__TAGS.getName()));
		assertThat(children.authorizedUsersEntry.getLabelTextPrefix(),
				containsString(TestModelPackage.Literals.TODO_LIST__AUTHORIZED_USERS.getName()));

		return children;
	}

	/**
	 * validates the given entries for a {@link MatchEntry} referencing a
	 * {@link User}.
	 * 
	 * @param leftUser
	 *            the left {@link TodoList} to match or null.
	 * @param rightUser
	 *            the right {@link TodoList} to match or null.
	 * @param entries
	 *            the entries to validate.
	 * @return An helper object to access the children if the validation passes.
	 */
	private UserChildren assertUserChildren(User leftUser, User rightUser, Object[] entries, int expectedAvatarSize) {

		assertThat(entries.length, is(2));
		assertThat(entries[0], instanceOf(ObjectEntry.class));

		UserChildren children = new UserChildren();

		children.nameEntry = (ObjectEntry) entries[0];
		if (expectedAvatarSize < 2) {

			assertThat(entries[1], instanceOf(MatchEntry.class));
			children.singleAvatarEntry = (MatchEntry) entries[1];
			assertThat(children.singleAvatarEntry.getLabelTextPrefix(),
					containsString(TestModelPackage.Literals.USER__AVATAR.getName()));

		} else if (expectedAvatarSize > 0) {

			assertThat(entries[1], instanceOf(ListEntry.class));
			children.avatarEntry = (ListEntry) entries[1];

			assertThat(children.avatarEntry.getElementList().size(), is(expectedAvatarSize));
			assertThat(children.avatarEntry.getLabelTextPrefix(),
					containsString(TestModelPackage.Literals.USER__AVATAR.getName()));

		}

		assertThat(children.nameEntry.getLabelTextPrefix(),
				containsString(TestModelPackage.Literals.USER__NAME.getName()));

		return children;
	}

	/**
	 * validates the given entries for a {@link MatchEntry} referencing a
	 * {@link TextEntry}.
	 * 
	 * @param leftEntry
	 *            the left {@link TextEntry} to match or null.
	 * @param rightEntry
	 *            the right {@link TextEntry} to match or null
	 * @param entries
	 *            the entries to validate.
	 * @return An helper object to access the children if the validation passes.
	 */
	private TextEntryChildren assertTextEntryChildren(TextEntry leftEntry, TextEntry rightEntry, BaseEntry parent,
			Object[] entries) {

		assertThat(entries.length, is(4));
		assertThat(entries[0], instanceOf(ObjectEntry.class));
		assertThat(entries[1], instanceOf(ListEntry.class));
		assertThat(entries[2], instanceOf(ObjectEntry.class));
		assertThat(entries[3], instanceOf(ObjectEntry.class));

		TextEntryChildren children = new TextEntryChildren();

		children.completedEntry = (ObjectEntry) entries[0];
		children.subEntriesEntry = (ListEntry) entries[1];
		children.titleEntry = (ObjectEntry) entries[2];
		children.descriptionEntry = (ObjectEntry) entries[3];

		assertThat(children.completedEntry.getParent(), is(parent));
		assertThat(children.subEntriesEntry.getParent(), is(parent));
		assertThat(children.titleEntry.getParent(), is(parent));
		assertThat(children.descriptionEntry.getParent(), is(parent));

		assertThat(children.subEntriesEntry.getLabelTextPrefix(),
				containsString(TestModelPackage.Literals.TODO_ENTRY__SUBENTRIES.getName()));

		if (leftEntry != null) {
			assertThat(children.completedEntry.getLeft(), is((Object) leftEntry.isCompleted()));
			assertThat(children.titleEntry.getLeft(), is((Object) leftEntry.getTitle()));
			assertThat(children.descriptionEntry.getLeft(), is((Object) leftEntry.getDescription()));
		}

		if (rightEntry != null) {
			assertThat(children.completedEntry.getRight(), is((Object) rightEntry.isCompleted()));
			assertThat(children.titleEntry.getRight(), is((Object) rightEntry.getTitle()));
			assertThat(children.descriptionEntry.getRight(), is((Object) rightEntry.getDescription()));
		}

		return children;
	}

	/**
	 * validates the given {@link MatchEntry} based on the given expected left
	 * and right {@link Object}s.
	 * 
	 * @param matchEntry
	 * @param leftObj
	 * @param rightObj
	 * @param parent
	 */
	private void assertMatchEntry(MatchEntry matchEntry, Object leftObj, Object rightObj, BaseEntry parent) {

		Match match = matchEntry.getMatch();

		assertThat(match.getLeft(), is((Object) leftObj));
		assertThat(match.getRight(), is((Object) rightObj));
		assertThat(matchEntry.getParent(), is(parent));
	}

}

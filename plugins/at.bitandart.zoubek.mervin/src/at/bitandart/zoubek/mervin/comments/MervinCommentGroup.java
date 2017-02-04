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
package at.bitandart.zoubek.mervin.comments;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.swt.comments.data.ICommentGroup;

/**
 * A {@link ICommentGroup} implementation for comment groups created for the
 * mervin model.
 * 
 * @author Florian Zoubek
 *
 */
public class MervinCommentGroup implements ICommentGroup {

	/**
	 * the attribute name comparison value used in {@link #getName(EObject)}
	 */
	protected static final String ATTRIBUTE_NAME_NAME = "name";

	/**
	 * the attribute name comparison value used in {@link #getTitle(EObject)}
	 */
	protected static final String ATTRIBUTE_NAME_TITLE = "title";

	/**
	 * the attribute name comparison value used in {@link #getId(EObject)}
	 */
	protected static final String ATTRIBUTE_NAME_ID = "id";

	private Set<EObject> targets = new HashSet<EObject>();

	@Override
	public String getGroupTitle() {
		StringBuilder sb = new StringBuilder();

		for (EObject target : targets) {
			if (sb.length() != 0) {
				sb.append(", ");
			}
			sb.append(getLabel(target));
		}

		return sb.toString();
	}

	/**
	 * determines the label for the given {@link EObject}. The label consists of
	 * the class name combined with either the name, title, or id attribute of
	 * the given {@link EObject}. If not such attribute exists only the class
	 * name is returned.
	 * 
	 * @param eObject
	 *            the {@link EObject} to obtain the label for.
	 * @return the label for the given eObject.
	 */
	protected String getLabel(EObject eObject) {

		String label = null;

		if (eObject instanceof Match) {
			Match match = (Match) eObject;
			EObject left = match.getLeft();
			EObject right = match.getRight();
			StringBuilder sb = new StringBuilder();

			if (left != null) {
				sb.append(getLabel(left));
			}
			if (right != null) {
				String rightLabel = getLabel(right);
				if (!rightLabel.equals(sb.toString())) {
					sb.append("/");
					sb.append(rightLabel);
				}
			}

			return sb.toString();
		}

		if (label == null) {
			label = getName(eObject);
		}

		if (label == null) {
			label = getTitle(eObject);
		}

		if (label == null) {
			label = getId(eObject);
		}
		if (label != null) {
			return getClassName(eObject) + " " + label;
		}
		return getClassName(eObject);
	}

	/**
	 * @param eObject
	 *            the {@link EObject} to obtain the attributes for.
	 * @return the list of attributes of the given {@link EObject} used by
	 *         operations of this class.
	 */
	protected List<EAttribute> getAttributes(EObject eObject) {
		return eObject.eClass().getEAllAttributes();
	}

	/**
	 * @param eObject
	 *            the {@link EObject} to retrieve the attribute value for.
	 * @param attributeName
	 *            the name of the attribute to retrieve the value for.
	 * @return the label for the value of the attribute with the given name or
	 *         null if no such attribute exists.
	 */
	protected String getAttributeValueLabel(EObject eObject, String attributeName) {

		List<EAttribute> attributes = getAttributes(eObject);
		for (EAttribute attribute : attributes) {
			if (attribute.getName().equalsIgnoreCase(attributeName)) {
				Object value = eObject.eGet(attribute);
				if (value != null) {
					return "\"" + value.toString() + "\"";
				}
			}
		}

		return null;
	}

	/**
	 * @param eObject
	 *            the {@link EObject} to obtain the name for.
	 * @return the name of the given {@link EObject} or null if it has no name.
	 */
	private String getName(EObject eObject) {
		return getAttributeValueLabel(eObject, ATTRIBUTE_NAME_NAME);
	}

	/**
	 * @param eObject
	 *            the {@link EObject} to obtain the title for.
	 * @return the title of the given {@link EObject} or null if it has no
	 *         title.
	 */
	private String getTitle(EObject eObject) {
		return getAttributeValueLabel(eObject, ATTRIBUTE_NAME_TITLE);
	}

	/**
	 * @param eObject
	 *            the {@link EObject} to obtain the id for.
	 * @return the id of the given {@link EObject} or null if it has no id.
	 */
	private String getId(EObject eObject) {
		return getAttributeValueLabel(eObject, ATTRIBUTE_NAME_ID);
	}

	/**
	 * @param eObject
	 *            the {@link EObject} to obtain the class name for.
	 * @return the name of the class of the given {@link EObject}.
	 */
	private String getClassName(EObject eObject) {
		return eObject.eClass().getName();
	}

	public Set<EObject> getTargets() {
		return targets;
	}

}
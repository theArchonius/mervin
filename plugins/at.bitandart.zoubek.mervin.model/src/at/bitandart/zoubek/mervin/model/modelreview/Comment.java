/**
 * ******************************************************************************
 *  Copyright (c) 2015, 2016 Florian Zoubek.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *     Florian Zoubek - initial API and implementation
 * ******************************************************************************
 */
package at.bitandart.zoubek.mervin.model.modelreview;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Comment</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getId
 * <em>Id</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getText
 * <em>Text</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getCommentLinks
 * <em>Comment Links</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getComment()
 * @model
 * @generated
 */
public interface Comment extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getComment_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getId
	 * <em>Id</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Text</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Text</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Text</em>' attribute.
	 * @see #setText(String)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getComment_Text()
	 * @model
	 * @generated
	 */
	String getText();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getText
	 * <em>Text</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
	void setText(String value);

	/**
	 * Returns the value of the '<em><b>Comment Links</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.CommentLink}. It is
	 * bidirectional and its opposite is '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.CommentLink#getComment
	 * <em>Comment</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comment Links</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Comment Links</em>' containment reference
	 *         list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getComment_CommentLinks()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.CommentLink#getComment
	 * @model opposite="comment" containment="true"
	 * @generated
	 */
	EList<CommentLink> getCommentLinks();

} // Comment

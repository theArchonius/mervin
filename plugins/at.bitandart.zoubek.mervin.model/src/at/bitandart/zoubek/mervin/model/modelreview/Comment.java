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
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getAuthor
 * <em>Author</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getCreationTime
 * <em>Creation Time</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getText
 * <em>Text</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getCommentLinks
 * <em>Comment Links</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getReplies
 * <em>Replies</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getRepliedTo
 * <em>Replied To</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getPatchset
 * <em>Patchset</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getPatchSetRefId
 * <em>Patch Set Ref Id</em>}</li>
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
	 * Returns the value of the '<em><b>Author</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Author</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Author</em>' reference.
	 * @see #setAuthor(User)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getComment_Author()
	 * @model required="true"
	 * @generated
	 */
	User getAuthor();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getAuthor
	 * <em>Author</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Author</em>' reference.
	 * @see #getAuthor()
	 * @generated
	 */
	void setAuthor(User value);

	/**
	 * Returns the value of the '<em><b>Creation Time</b></em>' attribute. The
	 * default value is <code>"0"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Creation Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Creation Time</em>' attribute.
	 * @see #setCreationTime(long)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getComment_CreationTime()
	 * @model default="0"
	 * @generated
	 */
	long getCreationTime();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getCreationTime
	 * <em>Creation Time</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Creation Time</em>' attribute.
	 * @see #getCreationTime()
	 * @generated
	 */
	void setCreationTime(long value);

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

	/**
	 * Returns the value of the '<em><b>Replies</b></em>' reference list. The
	 * list contents are of type
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment}. It is
	 * bidirectional and its opposite is '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getRepliedTo
	 * <em>Replied To</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Replies</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Replies</em>' reference list.
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getComment_Replies()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Comment#getRepliedTo
	 * @model opposite="repliedTo"
	 * @generated
	 */
	EList<Comment> getReplies();

	/**
	 * Returns the value of the '<em><b>Replied To</b></em>' reference. It is
	 * bidirectional and its opposite is '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getReplies
	 * <em>Replies</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Replied To</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Replied To</em>' reference.
	 * @see #setRepliedTo(Comment)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getComment_RepliedTo()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Comment#getReplies
	 * @model opposite="replies"
	 * @generated
	 */
	Comment getRepliedTo();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getRepliedTo
	 * <em>Replied To</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Replied To</em>' reference.
	 * @see #getRepliedTo()
	 * @generated
	 */
	void setRepliedTo(Comment value);

	/**
	 * Returns the value of the '<em><b>Patchset</b></em>' reference. It is
	 * bidirectional and its opposite is '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getComments
	 * <em>Comments</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Patchset</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Patchset</em>' reference.
	 * @see #setPatchset(PatchSet)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getComment_Patchset()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.PatchSet#getComments
	 * @model opposite="comments"
	 * @generated
	 */
	PatchSet getPatchset();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getPatchset
	 * <em>Patchset</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Patchset</em>' reference.
	 * @see #getPatchset()
	 * @generated
	 */
	void setPatchset(PatchSet value);

	/**
	 * Returns the value of the '<em><b>Patch Set Ref Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Patch Set Ref Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Patch Set Ref Id</em>' attribute.
	 * @see #setPatchSetRefId(String)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getComment_PatchSetRefId()
	 * @model
	 * @generated
	 */
	String getPatchSetRefId();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getPatchSetRefId
	 * <em>Patch Set Ref Id</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Patch Set Ref Id</em>' attribute.
	 * @see #getPatchSetRefId()
	 * @generated
	 */
	void setPatchSetRefId(String value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model
	 * @generated
	 */
	void resolvePatchSet(ModelReview review);

} // Comment

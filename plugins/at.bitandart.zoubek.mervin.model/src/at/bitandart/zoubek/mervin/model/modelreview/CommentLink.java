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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Comment Link</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.CommentLink#getComment
 * <em>Comment</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.CommentLink#getStart
 * <em>Start</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.CommentLink#getLength
 * <em>Length</em>}</li>
 * <li>{@link at.bitandart.zoubek.mervin.model.modelreview.CommentLink#getTarget
 * <em>Target</em>}</li>
 * </ul>
 *
 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getCommentLink()
 * @model
 * @generated
 */
public interface CommentLink extends EObject {
	/**
	 * Returns the value of the '<em><b>Comment</b></em>' container reference.
	 * It is bidirectional and its opposite is '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.Comment#getCommentLinks
	 * <em>Comment Links</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comment</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Comment</em>' container reference.
	 * @see #setComment(Comment)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getCommentLink_Comment()
	 * @see at.bitandart.zoubek.mervin.model.modelreview.Comment#getCommentLinks
	 * @model opposite="commentLinks" transient="false"
	 * @generated
	 */
	Comment getComment();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.CommentLink#getComment
	 * <em>Comment</em>}' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Comment</em>' container reference.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(Comment value);

	/**
	 * Returns the value of the '<em><b>Start</b></em>' attribute. The default
	 * value is <code>"0"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Start</em>' attribute.
	 * @see #setStart(int)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getCommentLink_Start()
	 * @model default="0"
	 * @generated
	 */
	int getStart();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.CommentLink#getStart
	 * <em>Start</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Start</em>' attribute.
	 * @see #getStart()
	 * @generated
	 */
	void setStart(int value);

	/**
	 * Returns the value of the '<em><b>Length</b></em>' attribute. The default
	 * value is <code>"0"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Length</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Length</em>' attribute.
	 * @see #setLength(int)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getCommentLink_Length()
	 * @model default="0"
	 * @generated
	 */
	int getLength();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.CommentLink#getLength
	 * <em>Length</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Length</em>' attribute.
	 * @see #getLength()
	 * @generated
	 */
	void setLength(int value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Target</em>' attribute.
	 * @see #setTarget(Object)
	 * @see at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage#getCommentLink_Target()
	 * @model
	 * @generated
	 */
	Object getTarget();

	/**
	 * Sets the value of the '
	 * {@link at.bitandart.zoubek.mervin.model.modelreview.CommentLink#getTarget
	 * <em>Target</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Target</em>' attribute.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(Object value);

} // CommentLink

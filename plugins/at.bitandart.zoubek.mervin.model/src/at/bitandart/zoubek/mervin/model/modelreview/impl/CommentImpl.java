/**
 * ******************************************************************************
 *  Copyright (c) 2015, 2016, 2017 Florian Zoubek.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *     Florian Zoubek - initial API and implementation
 * ******************************************************************************
 */
package at.bitandart.zoubek.mervin.model.modelreview.impl;

import at.bitandart.zoubek.mervin.model.modelreview.Comment;
import at.bitandart.zoubek.mervin.model.modelreview.CommentLink;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;
import at.bitandart.zoubek.mervin.model.modelreview.User;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Comment</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl#getId
 * <em>Id</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl#getAuthor
 * <em>Author</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl#getCreationTime
 * <em>Creation Time</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl#getText
 * <em>Text</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl#getCommentLinks
 * <em>Comment Links</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl#getReplies
 * <em>Replies</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl#getRepliedTo
 * <em>Replied To</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl#getPatchset
 * <em>Patchset</em>}</li>
 * <li>
 * {@link at.bitandart.zoubek.mervin.model.modelreview.impl.CommentImpl#getPatchSetRefId
 * <em>Patch Set Ref Id</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CommentImpl extends MinimalEObjectImpl.Container implements Comment {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAuthor() <em>Author</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAuthor()
	 * @generated
	 * @ordered
	 */
	protected User author;

	/**
	 * The default value of the '{@link #getCreationTime()
	 * <em>Creation Time</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getCreationTime()
	 * @generated
	 * @ordered
	 */
	protected static final long CREATION_TIME_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getCreationTime() <em>Creation Time</em>
	 * }' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCreationTime()
	 * @generated
	 * @ordered
	 */
	protected long creationTime = CREATION_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected static final String TEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected String text = TEXT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCommentLinks() <em>Comment Links</em>
	 * }' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getCommentLinks()
	 * @generated
	 * @ordered
	 */
	protected EList<CommentLink> commentLinks;

	/**
	 * The cached value of the '{@link #getReplies() <em>Replies</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getReplies()
	 * @generated
	 * @ordered
	 */
	protected EList<Comment> replies;

	/**
	 * The cached value of the '{@link #getRepliedTo() <em>Replied To</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRepliedTo()
	 * @generated
	 * @ordered
	 */
	protected Comment repliedTo;

	/**
	 * The cached value of the '{@link #getPatchset() <em>Patchset</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPatchset()
	 * @generated
	 * @ordered
	 */
	protected PatchSet patchset;

	/**
	 * The default value of the '{@link #getPatchSetRefId()
	 * <em>Patch Set Ref Id</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getPatchSetRefId()
	 * @generated
	 * @ordered
	 */
	protected static final String PATCH_SET_REF_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPatchSetRefId()
	 * <em>Patch Set Ref Id</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getPatchSetRefId()
	 * @generated
	 * @ordered
	 */
	protected String patchSetRefId = PATCH_SET_REF_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected CommentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelReviewPackage.Literals.COMMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.COMMENT__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public User getAuthor() {
		if (author != null && author.eIsProxy()) {
			InternalEObject oldAuthor = (InternalEObject) author;
			author = (User) eResolveProxy(oldAuthor);
			if (author != oldAuthor) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelReviewPackage.COMMENT__AUTHOR,
							oldAuthor, author));
			}
		}
		return author;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public User basicGetAuthor() {
		return author;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAuthor(User newAuthor) {
		User oldAuthor = author;
		author = newAuthor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.COMMENT__AUTHOR, oldAuthor,
					author));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public long getCreationTime() {
		return creationTime;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCreationTime(long newCreationTime) {
		long oldCreationTime = creationTime;
		creationTime = newCreationTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.COMMENT__CREATION_TIME,
					oldCreationTime, creationTime));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getText() {
		return text;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setText(String newText) {
		String oldText = text;
		text = newText;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.COMMENT__TEXT, oldText, text));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<CommentLink> getCommentLinks() {
		if (commentLinks == null) {
			commentLinks = new EObjectContainmentWithInverseEList<CommentLink>(CommentLink.class, this,
					ModelReviewPackage.COMMENT__COMMENT_LINKS, ModelReviewPackage.COMMENT_LINK__COMMENT);
		}
		return commentLinks;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Comment> getReplies() {
		if (replies == null) {
			replies = new EObjectWithInverseResolvingEList<Comment>(Comment.class, this,
					ModelReviewPackage.COMMENT__REPLIES, ModelReviewPackage.COMMENT__REPLIED_TO);
		}
		return replies;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Comment getRepliedTo() {
		if (repliedTo != null && repliedTo.eIsProxy()) {
			InternalEObject oldRepliedTo = (InternalEObject) repliedTo;
			repliedTo = (Comment) eResolveProxy(oldRepliedTo);
			if (repliedTo != oldRepliedTo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelReviewPackage.COMMENT__REPLIED_TO,
							oldRepliedTo, repliedTo));
			}
		}
		return repliedTo;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Comment basicGetRepliedTo() {
		return repliedTo;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetRepliedTo(Comment newRepliedTo, NotificationChain msgs) {
		Comment oldRepliedTo = repliedTo;
		repliedTo = newRepliedTo;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.COMMENT__REPLIED_TO, oldRepliedTo, newRepliedTo);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRepliedTo(Comment newRepliedTo) {
		if (newRepliedTo != repliedTo) {
			NotificationChain msgs = null;
			if (repliedTo != null)
				msgs = ((InternalEObject) repliedTo).eInverseRemove(this, ModelReviewPackage.COMMENT__REPLIES,
						Comment.class, msgs);
			if (newRepliedTo != null)
				msgs = ((InternalEObject) newRepliedTo).eInverseAdd(this, ModelReviewPackage.COMMENT__REPLIES,
						Comment.class, msgs);
			msgs = basicSetRepliedTo(newRepliedTo, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.COMMENT__REPLIED_TO, newRepliedTo,
					newRepliedTo));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchSet getPatchset() {
		if (patchset != null && patchset.eIsProxy()) {
			InternalEObject oldPatchset = (InternalEObject) patchset;
			patchset = (PatchSet) eResolveProxy(oldPatchset);
			if (patchset != oldPatchset) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelReviewPackage.COMMENT__PATCHSET,
							oldPatchset, patchset));
			}
		}
		return patchset;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchSet basicGetPatchset() {
		return patchset;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetPatchset(PatchSet newPatchset, NotificationChain msgs) {
		PatchSet oldPatchset = patchset;
		patchset = newPatchset;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelReviewPackage.COMMENT__PATCHSET, oldPatchset, newPatchset);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setPatchset(PatchSet newPatchset) {
		if (newPatchset != patchset) {
			NotificationChain msgs = null;
			if (patchset != null)
				msgs = ((InternalEObject) patchset).eInverseRemove(this, ModelReviewPackage.PATCH_SET__COMMENTS,
						PatchSet.class, msgs);
			if (newPatchset != null)
				msgs = ((InternalEObject) newPatchset).eInverseAdd(this, ModelReviewPackage.PATCH_SET__COMMENTS,
						PatchSet.class, msgs);
			msgs = basicSetPatchset(newPatchset, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.COMMENT__PATCHSET, newPatchset,
					newPatchset));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getPatchSetRefId() {
		return patchSetRefId;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setPatchSetRefId(String newPatchSetRefId) {
		String oldPatchSetRefId = patchSetRefId;
		patchSetRefId = newPatchSetRefId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelReviewPackage.COMMENT__PATCH_SET_REF_ID,
					oldPatchSetRefId, patchSetRefId));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void resolvePatchSet(ModelReview review) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelReviewPackage.COMMENT__COMMENT_LINKS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getCommentLinks()).basicAdd(otherEnd, msgs);
		case ModelReviewPackage.COMMENT__REPLIES:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getReplies()).basicAdd(otherEnd, msgs);
		case ModelReviewPackage.COMMENT__REPLIED_TO:
			if (repliedTo != null)
				msgs = ((InternalEObject) repliedTo).eInverseRemove(this, ModelReviewPackage.COMMENT__REPLIES,
						Comment.class, msgs);
			return basicSetRepliedTo((Comment) otherEnd, msgs);
		case ModelReviewPackage.COMMENT__PATCHSET:
			if (patchset != null)
				msgs = ((InternalEObject) patchset).eInverseRemove(this, ModelReviewPackage.PATCH_SET__COMMENTS,
						PatchSet.class, msgs);
			return basicSetPatchset((PatchSet) otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelReviewPackage.COMMENT__COMMENT_LINKS:
			return ((InternalEList<?>) getCommentLinks()).basicRemove(otherEnd, msgs);
		case ModelReviewPackage.COMMENT__REPLIES:
			return ((InternalEList<?>) getReplies()).basicRemove(otherEnd, msgs);
		case ModelReviewPackage.COMMENT__REPLIED_TO:
			return basicSetRepliedTo(null, msgs);
		case ModelReviewPackage.COMMENT__PATCHSET:
			return basicSetPatchset(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelReviewPackage.COMMENT__ID:
			return getId();
		case ModelReviewPackage.COMMENT__AUTHOR:
			if (resolve)
				return getAuthor();
			return basicGetAuthor();
		case ModelReviewPackage.COMMENT__CREATION_TIME:
			return getCreationTime();
		case ModelReviewPackage.COMMENT__TEXT:
			return getText();
		case ModelReviewPackage.COMMENT__COMMENT_LINKS:
			return getCommentLinks();
		case ModelReviewPackage.COMMENT__REPLIES:
			return getReplies();
		case ModelReviewPackage.COMMENT__REPLIED_TO:
			if (resolve)
				return getRepliedTo();
			return basicGetRepliedTo();
		case ModelReviewPackage.COMMENT__PATCHSET:
			if (resolve)
				return getPatchset();
			return basicGetPatchset();
		case ModelReviewPackage.COMMENT__PATCH_SET_REF_ID:
			return getPatchSetRefId();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ModelReviewPackage.COMMENT__ID:
			setId((String) newValue);
			return;
		case ModelReviewPackage.COMMENT__AUTHOR:
			setAuthor((User) newValue);
			return;
		case ModelReviewPackage.COMMENT__CREATION_TIME:
			setCreationTime((Long) newValue);
			return;
		case ModelReviewPackage.COMMENT__TEXT:
			setText((String) newValue);
			return;
		case ModelReviewPackage.COMMENT__COMMENT_LINKS:
			getCommentLinks().clear();
			getCommentLinks().addAll((Collection<? extends CommentLink>) newValue);
			return;
		case ModelReviewPackage.COMMENT__REPLIES:
			getReplies().clear();
			getReplies().addAll((Collection<? extends Comment>) newValue);
			return;
		case ModelReviewPackage.COMMENT__REPLIED_TO:
			setRepliedTo((Comment) newValue);
			return;
		case ModelReviewPackage.COMMENT__PATCHSET:
			setPatchset((PatchSet) newValue);
			return;
		case ModelReviewPackage.COMMENT__PATCH_SET_REF_ID:
			setPatchSetRefId((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case ModelReviewPackage.COMMENT__ID:
			setId(ID_EDEFAULT);
			return;
		case ModelReviewPackage.COMMENT__AUTHOR:
			setAuthor((User) null);
			return;
		case ModelReviewPackage.COMMENT__CREATION_TIME:
			setCreationTime(CREATION_TIME_EDEFAULT);
			return;
		case ModelReviewPackage.COMMENT__TEXT:
			setText(TEXT_EDEFAULT);
			return;
		case ModelReviewPackage.COMMENT__COMMENT_LINKS:
			getCommentLinks().clear();
			return;
		case ModelReviewPackage.COMMENT__REPLIES:
			getReplies().clear();
			return;
		case ModelReviewPackage.COMMENT__REPLIED_TO:
			setRepliedTo((Comment) null);
			return;
		case ModelReviewPackage.COMMENT__PATCHSET:
			setPatchset((PatchSet) null);
			return;
		case ModelReviewPackage.COMMENT__PATCH_SET_REF_ID:
			setPatchSetRefId(PATCH_SET_REF_ID_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ModelReviewPackage.COMMENT__ID:
			return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
		case ModelReviewPackage.COMMENT__AUTHOR:
			return author != null;
		case ModelReviewPackage.COMMENT__CREATION_TIME:
			return creationTime != CREATION_TIME_EDEFAULT;
		case ModelReviewPackage.COMMENT__TEXT:
			return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
		case ModelReviewPackage.COMMENT__COMMENT_LINKS:
			return commentLinks != null && !commentLinks.isEmpty();
		case ModelReviewPackage.COMMENT__REPLIES:
			return replies != null && !replies.isEmpty();
		case ModelReviewPackage.COMMENT__REPLIED_TO:
			return repliedTo != null;
		case ModelReviewPackage.COMMENT__PATCHSET:
			return patchset != null;
		case ModelReviewPackage.COMMENT__PATCH_SET_REF_ID:
			return PATCH_SET_REF_ID_EDEFAULT == null ? patchSetRefId != null
					: !PATCH_SET_REF_ID_EDEFAULT.equals(patchSetRefId);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
		case ModelReviewPackage.COMMENT___RESOLVE_PATCH_SET__MODELREVIEW:
			resolvePatchSet((ModelReview) arguments.get(0));
			return null;
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (id: ");
		result.append(id);
		result.append(", creationTime: ");
		result.append(creationTime);
		result.append(", text: ");
		result.append(text);
		result.append(", patchSetRefId: ");
		result.append(patchSetRefId);
		result.append(')');
		return result.toString();
	}

} // CommentImpl

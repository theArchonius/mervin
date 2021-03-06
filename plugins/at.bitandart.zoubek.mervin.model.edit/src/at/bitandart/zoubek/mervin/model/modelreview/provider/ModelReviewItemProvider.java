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
package at.bitandart.zoubek.mervin.model.modelreview.provider;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewFactory;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a
 * {@link at.bitandart.zoubek.mervin.model.modelreview.ModelReview} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class ModelReviewItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider,
		IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelReviewItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addIdPropertyDescriptor(object);
			addRepositoryURIPropertyDescriptor(object);
			addCommentsPropertyDescriptor(object);
			addLeftPatchSetPropertyDescriptor(object);
			addRightPatchSetPropertyDescriptor(object);
			addSelectedModelComparisonPropertyDescriptor(object);
			addSelectedDiagramComparisonPropertyDescriptor(object);
			addShowAdditionsPropertyDescriptor(object);
			addShowModificationsPropertyDescriptor(object);
			addShowDeletionsPropertyDescriptor(object);
			addShowLayoutChangesPropertyDescriptor(object);
			addUnifiedModelMapPropertyDescriptor(object);
			addCurrentReviewerPropertyDescriptor(object);
			addShowCommentsPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Id feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addIdPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(), getString("_UI_ModelReview_id_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_id_feature",
								"_UI_ModelReview_type"),
						ModelReviewPackage.Literals.MODEL_REVIEW__ID, true, false, false,
						ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Repository URI feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addRepositoryURIPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(), getString("_UI_ModelReview_repositoryURI_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_repositoryURI_feature",
								"_UI_ModelReview_type"),
						ModelReviewPackage.Literals.MODEL_REVIEW__REPOSITORY_URI, true, false, false,
						ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Comments feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addCommentsPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(), getString("_UI_ModelReview_comments_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_comments_feature",
								"_UI_ModelReview_type"),
						ModelReviewPackage.Literals.MODEL_REVIEW__COMMENTS, true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Left Patch Set feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addLeftPatchSetPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(), getString("_UI_ModelReview_leftPatchSet_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_leftPatchSet_feature",
								"_UI_ModelReview_type"),
						ModelReviewPackage.Literals.MODEL_REVIEW__LEFT_PATCH_SET, true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Right Patch Set feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addRightPatchSetPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(), getString("_UI_ModelReview_rightPatchSet_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_rightPatchSet_feature",
								"_UI_ModelReview_type"),
						ModelReviewPackage.Literals.MODEL_REVIEW__RIGHT_PATCH_SET, true, false, true, null, null,
						null));
	}

	/**
	 * This adds a property descriptor for the Selected Model Comparison
	 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addSelectedModelComparisonPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ModelReview_selectedModelComparison_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_selectedModelComparison_feature",
						"_UI_ModelReview_type"),
				ModelReviewPackage.Literals.MODEL_REVIEW__SELECTED_MODEL_COMPARISON, true, false, true, null, null,
				null));
	}

	/**
	 * This adds a property descriptor for the Selected Diagram Comparison
	 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addSelectedDiagramComparisonPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ModelReview_selectedDiagramComparison_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_selectedDiagramComparison_feature",
						"_UI_ModelReview_type"),
				ModelReviewPackage.Literals.MODEL_REVIEW__SELECTED_DIAGRAM_COMPARISON, true, false, true, null, null,
				null));
	}

	/**
	 * This adds a property descriptor for the Show Additions feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addShowAdditionsPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(), getString("_UI_ModelReview_showAdditions_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_showAdditions_feature",
								"_UI_ModelReview_type"),
						ModelReviewPackage.Literals.MODEL_REVIEW__SHOW_ADDITIONS, true, false, false,
						ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Show Modifications feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addShowModificationsPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(), getString("_UI_ModelReview_showModifications_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_showModifications_feature",
								"_UI_ModelReview_type"),
						ModelReviewPackage.Literals.MODEL_REVIEW__SHOW_MODIFICATIONS, true, false, false,
						ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Show Deletions feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addShowDeletionsPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(), getString("_UI_ModelReview_showDeletions_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_showDeletions_feature",
								"_UI_ModelReview_type"),
						ModelReviewPackage.Literals.MODEL_REVIEW__SHOW_DELETIONS, true, false, false,
						ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Show Layout Changes feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addShowLayoutChangesPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(), getString("_UI_ModelReview_showLayoutChanges_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_showLayoutChanges_feature",
								"_UI_ModelReview_type"),
						ModelReviewPackage.Literals.MODEL_REVIEW__SHOW_LAYOUT_CHANGES, true, false, false,
						ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Unified Model Map feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addUnifiedModelMapPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(), getString("_UI_ModelReview_unifiedModelMap_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_unifiedModelMap_feature",
								"_UI_ModelReview_type"),
						ModelReviewPackage.Literals.MODEL_REVIEW__UNIFIED_MODEL_MAP, true, false, false,
						ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Current Reviewer feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addCurrentReviewerPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(), getString("_UI_ModelReview_currentReviewer_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_currentReviewer_feature",
								"_UI_ModelReview_type"),
						ModelReviewPackage.Literals.MODEL_REVIEW__CURRENT_REVIEWER, true, false, true, null, null,
						null));
	}

	/**
	 * This adds a property descriptor for the Show Comments feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addShowCommentsPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
						getResourceLocator(), getString("_UI_ModelReview_showComments_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ModelReview_showComments_feature",
								"_UI_ModelReview_type"),
						ModelReviewPackage.Literals.MODEL_REVIEW__SHOW_COMMENTS, true, false, false,
						ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to
	 * deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand},
	 * {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in
	 * {@link #createCommand}. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(ModelReviewPackage.Literals.MODEL_REVIEW__PATCH_SETS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper
		// feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns ModelReview.gif. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ModelReview"));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((ModelReview) object).getId();
		return label == null || label.length() == 0 ? getString("_UI_ModelReview_type")
				: getString("_UI_ModelReview_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to
	 * update any cached children and by creating a viewer notification, which
	 * it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(ModelReview.class)) {
		case ModelReviewPackage.MODEL_REVIEW__ID:
		case ModelReviewPackage.MODEL_REVIEW__REPOSITORY_URI:
		case ModelReviewPackage.MODEL_REVIEW__SHOW_ADDITIONS:
		case ModelReviewPackage.MODEL_REVIEW__SHOW_MODIFICATIONS:
		case ModelReviewPackage.MODEL_REVIEW__SHOW_DELETIONS:
		case ModelReviewPackage.MODEL_REVIEW__SHOW_LAYOUT_CHANGES:
		case ModelReviewPackage.MODEL_REVIEW__UNIFIED_MODEL_MAP:
		case ModelReviewPackage.MODEL_REVIEW__SHOW_COMMENTS:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
			return;
		case ModelReviewPackage.MODEL_REVIEW__PATCH_SETS:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
			return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing the children that can be created under this object. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add(createChildParameter(ModelReviewPackage.Literals.MODEL_REVIEW__PATCH_SETS,
				ModelReviewFactory.eINSTANCE.createPatchSet()));
	}

	/**
	 * Return the resource locator for this item provider's resources. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return MervinModelReviewEditPlugin.INSTANCE;
	}

}

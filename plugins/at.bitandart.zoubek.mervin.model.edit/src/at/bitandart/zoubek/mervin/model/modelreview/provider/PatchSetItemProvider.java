/**
 * ******************************************************************************
 *  Copyright (c) 2015 Florian Zoubek.
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

import at.bitandart.zoubek.mervin.model.modelreview.ModelReviewPackage;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

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
 * {@link at.bitandart.zoubek.mervin.model.modelreview.PatchSet} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class PatchSetItemProvider extends ItemProviderAdapter implements
		IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchSetItemProvider(AdapterFactory adapterFactory) {
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
			addPatchesPropertyDescriptor(object);
			addNewInvolvedModelsPropertyDescriptor(object);
			addNewInvolvedDiagramsPropertyDescriptor(object);
			addOldInvolvedModelsPropertyDescriptor(object);
			addOldInvolvedDiagramsPropertyDescriptor(object);
			addModelComparisonPropertyDescriptor(object);
			addDiagramComparisonPropertyDescriptor(object);
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
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_PatchSet_id_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_PatchSet_id_feature", "_UI_PatchSet_type"),
				ModelReviewPackage.Literals.PATCH_SET__ID, true, false, false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Patches feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addPatchesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_PatchSet_patches_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_PatchSet_patches_feature", "_UI_PatchSet_type"),
				ModelReviewPackage.Literals.PATCH_SET__PATCHES, true, false,
				true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the New Involved Models feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addNewInvolvedModelsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_PatchSet_newInvolvedModels_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_PatchSet_newInvolvedModels_feature",
						"_UI_PatchSet_type"),
				ModelReviewPackage.Literals.PATCH_SET__NEW_INVOLVED_MODELS,
				true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the New Involved Diagrams feature.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addNewInvolvedDiagramsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_PatchSet_newInvolvedDiagrams_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_PatchSet_newInvolvedDiagrams_feature",
						"_UI_PatchSet_type"),
				ModelReviewPackage.Literals.PATCH_SET__NEW_INVOLVED_DIAGRAMS,
				true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Old Involved Models feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addOldInvolvedModelsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_PatchSet_oldInvolvedModels_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_PatchSet_oldInvolvedModels_feature",
						"_UI_PatchSet_type"),
				ModelReviewPackage.Literals.PATCH_SET__OLD_INVOLVED_MODELS,
				true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Old Involved Diagrams feature.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addOldInvolvedDiagramsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_PatchSet_oldInvolvedDiagrams_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_PatchSet_oldInvolvedDiagrams_feature",
						"_UI_PatchSet_type"),
				ModelReviewPackage.Literals.PATCH_SET__OLD_INVOLVED_DIAGRAMS,
				true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Model Comparison feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addModelComparisonPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_PatchSet_modelComparison_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_PatchSet_modelComparison_feature",
						"_UI_PatchSet_type"),
				ModelReviewPackage.Literals.PATCH_SET__MODEL_COMPARISON, true,
				false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Diagram Comparison feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDiagramComparisonPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_PatchSet_diagramComparison_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_PatchSet_diagramComparison_feature",
						"_UI_PatchSet_type"),
				ModelReviewPackage.Literals.PATCH_SET__DIAGRAM_COMPARISON,
				true, false, true, null, null, null));
	}

	/**
	 * This returns PatchSet.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object,
				getResourceLocator().getImage("full/obj16/PatchSet"));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((PatchSet) object).getId();
		return label == null || label.length() == 0 ? getString("_UI_PatchSet_type")
				: getString("_UI_PatchSet_type") + " " + label;
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

		switch (notification.getFeatureID(PatchSet.class)) {
		case ModelReviewPackage.PATCH_SET__ID:
			fireNotifyChanged(new ViewerNotification(notification,
					notification.getNotifier(), false, true));
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
	protected void collectNewChildDescriptors(
			Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
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

package at.bitandart.zoubek.mervin.diagram.diff.toolcontrols;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import at.bitandart.zoubek.mervin.IMervinContextConstants;
import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;

/**
 * An abstract Toolcontrol contribution that provides a combo box with a label
 * that allows the user to switch one side of the currently compared versions.
 * 
 * @author Florian Zoubek
 *
 */
public abstract class VersionSelector {

	/**
	 * item value that represents the base version
	 */
	protected final String COMPARE_BASE = "Base";

	/**
	 * the combo viewer that allows selection of the version
	 */
	private ComboViewer versionViewer;

	/**
	 * the model review that is used to retrieve the selected and available
	 * versions.
	 */
	private ModelReview activeReview;

	/**
	 * the text of the label in front of the combo box.
	 */
	private String labelText = null;

	public VersionSelector(String labelText) {
		super();
		this.labelText = labelText;
	}

	@PostConstruct
	public void createControls(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		comp.setLayout(layout);

		Label label = new Label(comp, SWT.NONE);
		label.setText(labelText);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(label);

		CCombo oldVersionCombo = new CCombo(comp, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		GridDataFactory.fillDefaults().hint(130, SWT.DEFAULT).applyTo(oldVersionCombo);
		versionViewer = new ComboViewer(oldVersionCombo);
		versionViewer.setContentProvider(ArrayContentProvider.getInstance());
		versionViewer.setLabelProvider(new VersionLabelProvider());
		update();
	}

	@Inject
	public void setActiveReview(@Named(IMervinContextConstants.ACTIVE_MODEL_REVIEW) ModelReview review) {
		this.activeReview = review;
		update();
	}

	/**
	 * updates the state of the control.
	 */
	protected void update() {
		ComboViewer versionViewer = getVersionViewer();
		ModelReview activeReview = getActiveReview();
		if (versionViewer != null && activeReview != null) {
			List<Object> patchSets = new LinkedList<Object>(activeReview.getPatchSets());
			patchSets.add(0, COMPARE_BASE);
			versionViewer.setInput(patchSets);
			Object version = getSelectedVersion();

			if (version != null) {
				versionViewer.setSelection(new StructuredSelection(version));
			} else {
				versionViewer.setSelection(new StructuredSelection(COMPARE_BASE));
			}
		}
	}

	/**
	 * @return the currently selected version or null if the no or the base
	 *         version has been selected.
	 */
	protected abstract Object getSelectedVersion();

	/**
	 * @return the review to switch the comparison version for.
	 */
	public ModelReview getActiveReview() {
		return activeReview;
	}

	/**
	 * @return the combo viewer used to switch the compared versions.
	 */
	public ComboViewer getVersionViewer() {
		return versionViewer;
	}

}
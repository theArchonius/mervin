/*******************************************************************************
 * Copyright (c) 2017 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.ecore.EObject;

import at.bitandart.zoubek.mervin.model.modelreview.ModelReview;
import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * Base interface for services that provide comparison operations on artifacts
 * of reviews.
 * 
 * @author Florian Zoubek
 *
 */
public interface IReviewCompareService {

	/**
	 * Represents the version of a patch set
	 * 
	 * @author Florian Zoubek
	 *
	 */
	public enum Version {
		/**
		 * the old version of a patch set.
		 */
		OLD,
		/**
		 * the new version of a patch set.
		 */
		NEW
	}

	/**
	 * compares the old and the new model versions of the given {@link PatchSet}
	 * .
	 * 
	 * @param patchSet
	 *            the patch set whose versions to compare.
	 * @return the resulting comparison.
	 * @throws OperationCanceledException
	 *             if the operations has been cancelled by the given monitor.
	 */
	public Comparison comparePatchSetModelVersions(PatchSet patchSet, IProgressMonitor monitor)
			throws OperationCanceledException;

	/**
	 * compares the old and the new diagram versions of the given
	 * {@link PatchSet} .
	 * 
	 * @param patchSet
	 *            the patch set whose versions to compare.
	 * @return the resulting comparison.
	 * @throws OperationCanceledException
	 *             if the operations has been cancelled by the given monitor.
	 */
	public Comparison comparePatchSetDiagramVersions(PatchSet patchSet, IProgressMonitor monitor)
			throws OperationCanceledException;

	/**
	 * compares the given object with all diagrams and models of the given patch
	 * set and version. The actual comparison may operate on a bigger set of
	 * objects which at least contains the given object.
	 * 
	 * @param eObject
	 *            the {@link EObject} to compare.
	 * @param patchSet
	 *            the patch set containing the models/diagrams to compare with.
	 * @param version
	 *            the version of the models/diagrams to compare with.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @return the resulting comparison.
	 * @throws OperationCanceledException
	 *             if the operations has been cancelled by the given monitor.
	 */
	public Comparison compareWithPatchSetVersion(EObject eObject, PatchSet patchSet, Version version,
			IProgressMonitor monitor) throws OperationCanceledException;

	/**
	 * compares all diagrams and models of the given left patch set and version
	 * with all diagrams and models of the given right patch set and version.
	 * 
	 * @param leftPatchSet
	 *            the left patch containing the models/diagrams to compare.
	 * @param leftVersion
	 *            the left version of the models/diagrams to compare with.
	 * @param rightPatchSet
	 *            the right patch containing the models/diagrams to compare.
	 * @param rightVersion
	 *            the right version of the models/diagrams to compare with.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @return the resulting comparison.
	 * @throws OperationCanceledException
	 *             if the operations has been cancelled by the given monitor.
	 */
	public Comparison comparePatchSetVersions(PatchSet leftPatchSet, Version leftVersion, PatchSet rightPatchSet,
			Version rightVersion, IProgressMonitor monitor) throws OperationCanceledException;

	/**
	 * matches the given object with all diagrams and models of the given patch
	 * set and version. The actual matching may operate on a bigger set of
	 * objects which at least contains the given object.
	 * 
	 * @param eObject
	 *            the {@link EObject} to match.
	 * @param patchSet
	 *            the patch set containing the models/diagrams to match with.
	 * @param version
	 *            the version of the models/diagrams to match with.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @return the comparison containing only the matching information.
	 * @throws OperationCanceledException
	 *             if the operations has been cancelled by the given monitor.
	 */
	public Comparison matchWithPatchSetVersion(EObject eObject, PatchSet patchSet, Version version,
			IProgressMonitor monitor) throws OperationCanceledException;

	/**
	 * matches all diagrams and models of the given left patch set and version
	 * with all diagrams and models of the given right patch set and version.
	 * 
	 * @param leftPatchSet
	 *            the left patch containing the models/diagrams to match.
	 * @param leftVersion
	 *            the left version of the models/diagrams to match with.
	 * @param rightPatchSet
	 *            the right patch containing the models/diagrams to match.
	 * @param rightVersion
	 *            the right version of the models/diagrams to match with.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @return the comparison containing only the matching information.
	 * @throws OperationCanceledException
	 *             if the operations has been cancelled by the given monitor.
	 */
	public Comparison matchPatchSetVersions(PatchSet leftPatchSet, Version leftVersion, PatchSet rightPatchSet,
			Version rightVersion, IProgressMonitor monitor) throws OperationCanceledException;

	/**
	 * updates the {@code SelectedComparison} reference of the given review
	 * based on the selected left and right patch sets.
	 * 
	 * @param review
	 *            the review to update.
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 * @throws OperationCanceledException
	 *             if the operations has been cancelled by the given monitor.
	 */
	public void updateSelectedComparison(ModelReview review, IProgressMonitor monitor)
			throws OperationCanceledException;

}

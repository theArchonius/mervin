/*******************************************************************************
 * Copyright (c) 2016 Florian Zoubek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Zoubek - initial API and implementation
 *******************************************************************************/
package at.bitandart.zoubek.mervin.diagram.diff.toolcontrols;

import java.text.MessageFormat;

import org.eclipse.jface.viewers.LabelProvider;

import at.bitandart.zoubek.mervin.model.modelreview.PatchSet;

/**
 * A simple {@link LabelProvider} for versions in a {@link VersionSelector}.
 * 
 * @author Florian Zoubek
 *
 */
public class VersionLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof PatchSet) {
			PatchSet patchSet = (PatchSet) element;
			return MessageFormat.format("PatchSet #{0}", patchSet.getId());
		}
		return super.getText(element);
	}
}

/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     SpringSource    - Adapted for use with AJDT
 *******************************************************************************/
package org.eclipse.ajdt.internal.ui.wizards.exports;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;

public abstract class AbstractExportTab {

	protected AbstractExportWizardPage fPage;

	public AbstractExportTab(AbstractExportWizardPage page) {
		fPage = page;
	}

	protected abstract Control createControl(Composite parent);

	protected abstract void initialize(IDialogSettings settings);

	protected abstract void saveSettings(IDialogSettings settings);

	protected void initializeCombo(IDialogSettings settings, String key, Combo combo) {
		for (int i = 0; i < 6; i++) {
			String curr = settings.get(key + String.valueOf(i));
			if (curr != null && combo.indexOf(curr) == -1) {
				combo.add(curr);
			}
		}
		if (combo.getItemCount() > 0)
			combo.setText(combo.getItem(0));
	}

	protected void saveCombo(IDialogSettings settings, String key, Combo combo) {
		if (combo.getText().trim().length() > 0) {
			settings.put(key + String.valueOf(0), combo.getText().trim());
			String[] items = combo.getItems();
			int nEntries = Math.min(items.length, 5);
			for (int i = 0; i < nEntries; i++) {
				settings.put(key + String.valueOf(i + 1), items[i].trim());
			}
		}
	}

	protected void chooseFile(Combo combo, String filter) {
		FileDialog dialog = new FileDialog(fPage.getShell(), SWT.SAVE);
		String path = combo.getText();
		if (path.trim().length() == 0)
			path = PDEPlugin.getWorkspace().getRoot().getLocation().toString();
		dialog.setFileName(path);
		dialog.setFilterExtensions(new String[] {filter});
		String res = dialog.open();
		if (res != null) {
			if (combo.indexOf(res) == -1)
				combo.add(res, 0);
			combo.setText(res);
		}
	}

}

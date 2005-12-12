/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Matt Chapman - initial version
 *******************************************************************************/
package org.eclipse.ajdt.internal.bc;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ajdt.core.CoreUtils.FilenameFilter;
import org.eclipse.ajdt.internal.ui.text.UIMessages;
import org.eclipse.ajdt.ui.buildconfig.IBuildConfiguration;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class BuildConfiguration implements IBuildConfiguration {

	private IProject project;
	
	public BuildConfiguration(IProject project) {
		this.project = project;
	}
	
	public String getName() {
		return UIMessages.CompilerMonitor_default_config;
	}

	public IFile getFile() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getIncludedJavaFiles(FilenameFilter filter) {
		// TODO Process JDT's include/exclude mechanism
		// include all source files for now
		List sourceFiles = new ArrayList();
		try {
			IJavaProject jp = JavaCore.create(project);
			IClasspathEntry[] cpes = jp.getRawClasspath();
			for (int i = 0; i < cpes.length; i++) {
				if (cpes[i].getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					IPath path = cpes[i].getPath();
					IResource res = project.findMember(path
							.removeFirstSegments(1));
					if ((res != null) && (res instanceof IContainer)) {
						List l = allFiles((IContainer) res, filter);
						sourceFiles.addAll(l);
					}
				}
			}
		} catch (JavaModelException e) {
		}
		return sourceFiles;
	}

	//return a list of all file resources in the given folder, including all
	// sub-folders
	private List allFiles(IContainer folder, final FilenameFilter filter) {
		final List contents = new ArrayList();
		try {
			folder.accept(new IResourceVisitor() {
				public boolean visit(IResource res) {
					if (res.getType() == IResource.FILE
							&& filter.accept(res
									.getName())) {
						contents.add(res.getLocation().toFile());
					}
					return true;
				}
			});
		} catch (CoreException e) {
		}
		return contents;
	}
	
	public boolean isIncluded(IResource correspondingResource) {
		// TODO Auto-generated method stub
		return true;
	}

	public List getIncludedJavaFileNames(FilenameFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public void update(boolean b) {
		// TODO Auto-generated method stub
		
	}

}

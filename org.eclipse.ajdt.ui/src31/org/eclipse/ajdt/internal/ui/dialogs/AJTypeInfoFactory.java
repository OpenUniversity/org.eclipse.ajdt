/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ajdt.internal.ui.dialogs;

import java.util.Arrays;
import java.util.Comparator;

import org.aspectj.ajdt.core.AspectJCore;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.corext.util.IFileTypeInfo;
import org.eclipse.jdt.internal.corext.util.JarFileEntryTypeInfo;
import org.eclipse.jdt.internal.corext.util.Strings;
import org.eclipse.jdt.internal.corext.util.TypeInfo;
import org.eclipse.jdt.internal.corext.util.UnresolvableTypeInfo;

// Copied from TypeInfoFactory to create AJ Types where appropriate
// Changes marked with // AspectJ Change
public class AJTypeInfoFactory {
	
	private String[] fProjects;
	private TypeInfo fLast;
	private char[] fBuffer;

	private static final String CLASS= "class"; //$NON-NLS-1$
	private static final String JAVA= "java"; //$NON-NLS-1$
	// AspectJ Change
	private static final String AJ= "aj"; //$NON-NLS-1$
	
	public AJTypeInfoFactory() {
		super();
		fProjects= getProjectList();
		fLast= null;
		fBuffer= new char[512];
	}

	public TypeInfo create(char[] packageName, char[] typeName, char[][] enclosingName, int modifiers, String path) {
		String pn= getPackageName(packageName);
		String tn= new String(typeName);
		TypeInfo result= null;
		int index= path.indexOf(IJavaSearchScope.JAR_FILE_ENTRY_SEPARATOR);
		if (index != -1) {
			result= createJarFileEntryTypeInfo(pn, tn, enclosingName, modifiers, path, getJarFileEntryTypeInfo(fLast), index);
		} else {
			String project= getProject(path);
			if (project != null) {
				// AspectJ Change Begin
				if(path.endsWith(".aj")) { // $NON-NLS-1$
					result= createAJCUTypeInfo(pn, tn, enclosingName, modifiers, path, getIFileTypeInfo(fLast), project);
				} else {
					result= createIFileTypeInfo(pn, tn, enclosingName, modifiers, path, getIFileTypeInfo(fLast), project);
				}
				// AspectJ Change End
			}
		}
		if (result == null) {
			result= new UnresolvableTypeInfo(pn, tn, enclosingName, modifiers, path);
		} else {
			fLast= result;
		}
		return result;
	}
	
	private static JarFileEntryTypeInfo getJarFileEntryTypeInfo(TypeInfo info) {
		if (info == null || info.getElementType() != TypeInfo.JAR_FILE_ENTRY_TYPE_INFO)
			return null;
		return (JarFileEntryTypeInfo)info;
	}
	
	private static IFileTypeInfo getIFileTypeInfo(TypeInfo info) {
		if (info == null || info.getElementType() != TypeInfo.IFILE_TYPE_INFO)
			return null;
		return (IFileTypeInfo)info;
	}
	
	private TypeInfo createJarFileEntryTypeInfo(String packageName, String typeName, char[][] enclosingName, int modifiers, String path, JarFileEntryTypeInfo last, int index) {
		String jar= path.substring(0, index);
		String rest= path.substring(index + 1);
		index= rest.lastIndexOf(AJCUTypeInfo.SEPARATOR);
		if (index != -1) {
			rest= rest.substring(index + 1);
		}
		String file= null;
		String extension= null;
		index= rest.lastIndexOf(AJCUTypeInfo.EXTENSION_SEPARATOR);
		if (index != -1) {
			file= rest.substring(0, index);
			extension= rest.substring(index + 1);
		} else {
			return null;
		}
		if (last != null) {
			if (last.getJar().equals(jar)) {
				jar= last.getJar();
			} else {
				// System.out.println("Creating new jar: " + jar);
				jar= createString(jar);
			}
		} else {
			jar= createString(jar);
		}
		if (typeName.equals(file)) {
			file= typeName;
		} else {
			file= createString(file);
		}
		if (CLASS.equals(extension))
			extension= CLASS;
		else if (JAVA.equals(extension))
			extension= JAVA;
		else
			extension= createString(extension);
		return new JarFileEntryTypeInfo(packageName, typeName, enclosingName, modifiers, jar, file, extension);
	}
	
	private TypeInfo createIFileTypeInfo(String packageName, String typeName, char[][] enclosingName, int modifiers, String path, IFileTypeInfo last, String project) {
		String rest= path.substring(project.length() + 1); // the first slashes.
		int index= rest.lastIndexOf(AJCUTypeInfo.SEPARATOR);
		if (index == -1)
			return null;
		String middle= rest.substring(0, index);
		rest= rest.substring(index + 1);
		index= rest.lastIndexOf(AJCUTypeInfo.EXTENSION_SEPARATOR);
		String file= null;
		String extension= null;
		if (index != -1) {
			file= rest.substring(0, index);
			extension= rest.substring(index + 1);
		} else {
			return null;
		}
		String src= null;
		int ml= middle.length();
		int pl= packageName.length();
		// if we have a source or package then we have to substract the leading '/'
		if (ml > 0 && ml - 1 > pl) {
			 // If we have a package then we have to substract the '/' between src and package
			src= middle.substring(1, ml - pl - (pl > 0 ? 1 : 0));
		}
		if (last != null) {
			if (src != null && src.equals(last.getFolder()))
				src= last.getFolder();
		}
		if (typeName.equals(file)) {
			file= typeName;
		} else {
			file= createString(file);
		}
		if (CLASS.equals(extension))
			extension= CLASS;
		else if (JAVA.equals(extension))
			extension= JAVA;
		else
			extension= createString(extension);
		
		return new IFileTypeInfo(packageName, typeName, enclosingName, modifiers, project, src, file, extension);
	}

	// AspectJ Change Begin
	private TypeInfo createAJCUTypeInfo(String packageName, String typeName, char[][] enclosingName, int modifiers, String path, IFileTypeInfo last, String project) {
		String rest= path.substring(project.length() + 1); // the first slashes.
		int index= rest.lastIndexOf(AJCUTypeInfo.SEPARATOR);
		if (index == -1)
			return null;
		String middle= rest.substring(0, index);
		rest= rest.substring(index + 1);
		index= rest.lastIndexOf(AJCUTypeInfo.EXTENSION_SEPARATOR);
		String file= null;
		String extension= null;
		if (index != -1) {
			file= rest.substring(0, index);
			extension= rest.substring(index + 1);
		} else {
			return null;
		}
		String src= null;
		int ml= middle.length();
		int pl= packageName.length();
		// if we have a source or package then we have to substract the leading '/'
		if (ml > 0 && ml - 1 > pl) {
			 // If we have a package then we have to substract the '/' between src and package
			src= middle.substring(1, ml - pl - (pl > 0 ? 1 : 0));
		}
		if (last != null) {
			if (src != null && src.equals(last.getFolder()))
				src= last.getFolder();
		}
		if (typeName.equals(file)) {
			file= typeName;
		} else {
			file= createString(file);
		}
		if (AJ.equals(extension))
			extension= AJ;
				
		return new AJCUTypeInfo(packageName, typeName, enclosingName, modifiers, project, src, file, extension);
	}
	// AspectJ Change End
	
	private String getPackageName(char[] packageName) {
		if (fLast == null)
			return new String(packageName);
		String lastPackageName= fLast.getPackageName();
		if (Strings.equals(lastPackageName, packageName))
			return lastPackageName;
		return new String(packageName);
	}
	
	private String getProject(String path) {
		for (int i= 0; i < fProjects.length; i++) {
			String project= fProjects[i];
			if (path.startsWith(project, 1))
				return project;
		}
		return null;
	}
	
	private String createString(String s) {
		if (s == null)
			return null;
		int length= s.length();
		if (length > fBuffer.length)
			fBuffer= new char[length];
		s.getChars(0, length, fBuffer, 0);
		return new String(fBuffer, 0, length);
	}
	
	private static String[] getProjectList() {
		IJavaModel model= JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		String[] result;
		try {
			IJavaProject[] projects= model.getJavaProjects();
			result= new String[projects.length];
			for (int i= 0; i < projects.length; i++) {
				result[i]= projects[i].getElementName();
			}
		} catch (JavaModelException e) {
			result= new String[0];
		}
		// We have to sort the list of project names to make sure that we cut of the longest
		// project from the path, if two projects with the same prefix exist. For example
		// org.eclipse.jdt.ui and org.eclipse.jdt.ui.tests.
		Arrays.sort(result, new Comparator() {
			public int compare(Object o1, Object o2) {
				int l1= ((String)o1).length();
				int l2= ((String)o2).length();
				if (l1 < l2)
					return 1;
				if (l2 < l1)
					return -1; 
				return  0;
			}
			public boolean equals(Object obj) {
				return super.equals(obj);
			}
		});
		return result;
	}		
}

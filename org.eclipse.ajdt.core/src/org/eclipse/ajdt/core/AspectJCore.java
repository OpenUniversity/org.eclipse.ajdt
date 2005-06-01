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
package org.eclipse.ajdt.core;

import org.eclipse.ajdt.core.javaelements.AJCodeElement;
import org.eclipse.ajdt.core.javaelements.AJCompilationUnit;
import org.eclipse.ajdt.core.javaelements.AJInjarElement;
import org.eclipse.ajdt.core.javaelements.AspectElement;
import org.eclipse.ajdt.internal.core.CoreUtils;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.internal.core.DefaultWorkingCopyOwner;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.core.util.MementoTokenizer;

public class AspectJCore {

	/**
	 * Returns the Java model element corresponding to the given handle
	 * identifier generated by <code>IJavaElement.getHandleIdentifier()</code>,
	 * or <code>null</code> if unable to create the associated element.
	 * 
	 * @param handleIdentifier
	 *            the given handle identifier
	 * @return the Java element corresponding to the handle identifier
	 */
	public static IJavaElement create(String handleIdentifier) {
		return create(handleIdentifier, DefaultWorkingCopyOwner.PRIMARY);
	}

	private static int indexOfIgnoringEscapes(String str, char ch) {
		boolean prevEscape = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == JavaElement.JEM_ESCAPE) {
				prevEscape = true;
			} else {
				if ((c == ch) && (!prevEscape)) {
					return i;
				}
				prevEscape = false;
			}
		}
		return -1;
	}

	private static IJavaElement getCodeElement(String codeElementHandle, JavaElement parent) {
		int li = indexOfIgnoringEscapes(
				codeElementHandle,
				JavaElement.JEM_COUNT);
		if (li != -1) {
			String cname = codeElementHandle
					.substring(0, li);
			String rest = codeElementHandle
					.substring(li + 1);
			li = indexOfIgnoringEscapes(rest,
					JavaElement.JEM_COUNT);
			if (li != -1) {
				String lineStr = rest.substring(0,li);
				try {
					int line = new Integer(lineStr)
							.intValue();
					return new AJCodeElement(
							parent, line, cname);
				} catch (NumberFormatException e) {
				}
			}
		} else {
			// no line number
			return new AJCodeElement(parent, 0, codeElementHandle);
		}
		return null;
	}
	
	public static IJavaElement create(String handleIdentifier,
			WorkingCopyOwner owner) {
		if (handleIdentifier == null) {
			return null;
		}

		boolean isCodeElement = false;
		String codeElementHandle = "";

		int codeElementDelimPos = indexOfIgnoringEscapes(handleIdentifier,
				AspectElement.JEM_CODEELEMENT);
		if (codeElementDelimPos != -1) {
			isCodeElement = true;
			codeElementHandle = handleIdentifier
					.substring(codeElementDelimPos + 1);
			handleIdentifier = handleIdentifier.substring(0,
					codeElementDelimPos);
		}

		MementoTokenizer memento = new AJMementoTokenizer(handleIdentifier);
		while (memento.hasMoreTokens()) {
			String token = memento.nextToken();
			// System.out.println("token: " + token);
			if ((token.charAt(0) == AspectElement.JEM_ASPECT_CU)
					|| (token.charAt(0) == JavaElement.JEM_COMPILATIONUNIT)) {
				int index1 = handleIdentifier
						.indexOf(JavaElement.JEM_COMPILATIONUNIT);
				int index2 = handleIdentifier
					.indexOf(AspectElement.JEM_ASPECT_CU);
				int index = Math.max(index1,index2);
				if (index != -1) {
					IJavaElement je = JavaCore.create(handleIdentifier
							.substring(0, index));
					if (je instanceof PackageFragment) {
						PackageFragment pf = (PackageFragment) je;
						String cuName = handleIdentifier.substring(index + 1);
						int ind1 = cuName.indexOf(JavaElement.JEM_TYPE);
						if (ind1 != -1) {
							cuName = cuName.substring(0, ind1);
						}						
						int ind2 = cuName.indexOf(AspectElement.JEM_ASPECT_TYPE);
						if (ind2 != -1) {
							cuName = cuName.substring(0, ind2);
						}
						int ind3 = cuName.indexOf(AspectElement.JEM_ITD);
						if (ind3 != -1) {
							cuName = cuName.substring(0, ind3);
						}
						int ind4 = cuName.indexOf(AspectElement.JEM_DECLARE);
						if (ind4 != -1) {
							cuName = cuName.substring(0, ind4);
						}
						//System.out.println("cuName=" + cuName);
						if (CoreUtils.ASPECTJ_SOURCE_ONLY_FILTER.accept(cuName)) {
							JavaElement cu = new AJCompilationUnit(pf, cuName,
									owner);
							token = memento.nextToken();
							if (!memento.hasMoreTokens()) {
								return cu;
							}
							IJavaElement restEl = cu.getHandleFromMemento(
									memento.nextToken(), memento, owner);
							if (restEl != null) {
								if (isCodeElement) {
									// there was an AJCodeElement at the end of
									// the handle
									IJavaElement codeEl = getCodeElement(codeElementHandle,(JavaElement) restEl);
									if (codeEl != null) {
										return codeEl;
									}
								}
								return restEl;
							}
						} else {
							IJavaElement restEl = pf.getHandleFromMemento(
									token, memento, owner);
							if (restEl != null) {
								if (isCodeElement) {
									// there was an AJCodeElement at the end of
									// the handle
									IJavaElement codeEl = getCodeElement(codeElementHandle,(JavaElement) restEl);
									if (codeEl != null) {
										return codeEl;
									}
								}
								return restEl;
							}
						}
					}
				}
			}
		}
		if (isCodeElement) {
			// an injar aspect with no parent
			return new AJInjarElement(codeElementHandle);
		}
		return JavaCore.create(handleIdentifier);
	}

}

class AJMementoTokenizer extends MementoTokenizer {
	private static final String COUNT = Character
			.toString(JavaElement.JEM_COUNT);

	private static final String JAVAPROJECT = Character
			.toString(JavaElement.JEM_JAVAPROJECT);

	private static final String PACKAGEFRAGMENTROOT = Character
			.toString(JavaElement.JEM_PACKAGEFRAGMENTROOT);

	private static final String PACKAGEFRAGMENT = Character
			.toString(JavaElement.JEM_PACKAGEFRAGMENT);

	private static final String FIELD = Character
			.toString(JavaElement.JEM_FIELD);

	private static final String METHOD = Character
			.toString(JavaElement.JEM_METHOD);

	private static final String INITIALIZER = Character
			.toString(JavaElement.JEM_INITIALIZER);

	private static final String COMPILATIONUNIT = Character
			.toString(JavaElement.JEM_COMPILATIONUNIT);

	private static final String CLASSFILE = Character
			.toString(JavaElement.JEM_CLASSFILE);

	private static final String TYPE = Character.toString(JavaElement.JEM_TYPE);

	private static final String PACKAGEDECLARATION = Character
			.toString(JavaElement.JEM_PACKAGEDECLARATION);

	private static final String IMPORTDECLARATION = Character
			.toString(JavaElement.JEM_IMPORTDECLARATION);

	private static final String LOCALVARIABLE = Character
			.toString(JavaElement.JEM_LOCALVARIABLE);

	// begin AspectJ change
	private static final String ASPECT_CU = Character
			.toString(AspectElement.JEM_ASPECT_CU);

	private static final String TYPE_PARAMETER = Character
			.toString(AspectElement.JEM_TYPE_PARAMETER);

	private static final String ADVICE = Character
			.toString(AspectElement.JEM_ADVICE);

	private static final String ASPECT_TYPE = Character
			.toString(AspectElement.JEM_ASPECT_TYPE);

	private static final String CODEELEMENT = Character
			.toString(AspectElement.JEM_CODEELEMENT);

	private static final String ITD = Character
			.toString(AspectElement.JEM_ITD);

	private static final String DECLARE = Character
		.toString(AspectElement.JEM_DECLARE);

	// end AspectJ change

	private final char[] memento;

	private final int length;

	private int index = 0;

	public AJMementoTokenizer(String memento) {
		super(memento);
		this.memento = memento.toCharArray();
		this.length = this.memento.length;
	}

	public boolean hasMoreTokens() {
		return this.index < this.length;
	}

	public String nextToken() {
		int start = this.index;
		StringBuffer buffer = null;
		switch (this.memento[this.index++]) {
		case JavaElement.JEM_ESCAPE:
			buffer = new StringBuffer();
			buffer.append(this.memento[this.index]);
			start = ++this.index;
			break;
		case JavaElement.JEM_COUNT:
			return COUNT;
		case JavaElement.JEM_JAVAPROJECT:
			return JAVAPROJECT;
		case JavaElement.JEM_PACKAGEFRAGMENTROOT:
			return PACKAGEFRAGMENTROOT;
		case JavaElement.JEM_PACKAGEFRAGMENT:
			return PACKAGEFRAGMENT;
		case JavaElement.JEM_FIELD:
			return FIELD;
		case JavaElement.JEM_METHOD:
			return METHOD;
		case JavaElement.JEM_INITIALIZER:
			return INITIALIZER;
		case JavaElement.JEM_COMPILATIONUNIT:
			return COMPILATIONUNIT;
		case JavaElement.JEM_CLASSFILE:
			return CLASSFILE;
		case JavaElement.JEM_TYPE:
			return TYPE;
		case JavaElement.JEM_PACKAGEDECLARATION:
			return PACKAGEDECLARATION;
		case JavaElement.JEM_IMPORTDECLARATION:
			return IMPORTDECLARATION;
		case JavaElement.JEM_LOCALVARIABLE:
			return LOCALVARIABLE;
		// begin AspectJ change
		case AspectElement.JEM_ASPECT_CU:
			return ASPECT_CU;
		case AspectElement.JEM_TYPE_PARAMETER:
			return TYPE_PARAMETER;
		case AspectElement.JEM_ADVICE:
			return ADVICE;
		case AspectElement.JEM_ASPECT_TYPE:
			return ASPECT_TYPE;
		case AspectElement.JEM_CODEELEMENT:
			return CODEELEMENT;
		case AspectElement.JEM_ITD:
			return ITD;
		case AspectElement.JEM_DECLARE:
			return DECLARE;
		// end AspectJ change
		}
		loop: while (this.index < this.length) {
			switch (this.memento[this.index]) {
			case JavaElement.JEM_ESCAPE:
				if (buffer == null)
					buffer = new StringBuffer();
				buffer.append(this.memento, start, this.index - start);
				start = ++this.index;
				break;
			case JavaElement.JEM_COUNT:
			case JavaElement.JEM_JAVAPROJECT:
			case JavaElement.JEM_PACKAGEFRAGMENTROOT:
			case JavaElement.JEM_PACKAGEFRAGMENT:
			case JavaElement.JEM_FIELD:
			case JavaElement.JEM_METHOD:
			case JavaElement.JEM_INITIALIZER:
			case JavaElement.JEM_COMPILATIONUNIT:
			case JavaElement.JEM_CLASSFILE:
			case JavaElement.JEM_TYPE:
			case JavaElement.JEM_PACKAGEDECLARATION:
			case JavaElement.JEM_IMPORTDECLARATION:
			case JavaElement.JEM_LOCALVARIABLE:
			// begin AspectJ change
			case AspectElement.JEM_ASPECT_CU:
			case AspectElement.JEM_TYPE_PARAMETER:
			case AspectElement.JEM_ADVICE:
			case AspectElement.JEM_ASPECT_TYPE:
			case AspectElement.JEM_CODEELEMENT:
			case AspectElement.JEM_ITD:
			case AspectElement.JEM_DECLARE:
			// end AspectJ change
				break loop;
			}
			this.index++;
		}
		if (buffer != null) {
			buffer.append(this.memento, start, this.index - start);
			return buffer.toString();
		} else {
			return new String(this.memento, start, this.index - start);
		}
	}
}
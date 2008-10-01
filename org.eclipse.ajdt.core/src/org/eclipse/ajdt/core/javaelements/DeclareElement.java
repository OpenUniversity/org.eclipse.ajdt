/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Luzius Meisser - initial implementation
 *******************************************************************************/
package org.eclipse.ajdt.core.javaelements;

import org.aspectj.asm.IProgramElement;
import org.aspectj.asm.IProgramElement.Kind;
import org.eclipse.ajdt.core.model.AJProjectModelFactory;
import org.eclipse.jdt.internal.core.JavaElement;

/**
 * @author Luzius Meisser
 */
public class DeclareElement extends AspectJMemberElement{
	
	public DeclareElement(JavaElement parent, String name, String[] parameterTypes) {
		super(parent, name, parameterTypes);
	}
	
	/**
	 * @see JavaElement#getHandleMemento()
	 */
	protected char getHandleMementoDelimiter() {
		return AspectElement.JEM_DECLARE;
	}
	
	
	protected Object createElementInfo() {
	    try {
            IProgramElement ipe = AJProjectModelFactory.getInstance().getModelForJavaElement(this)
                    .javaElementToProgramElement(this);

    	    DeclareElementInfo elementInfo = new DeclareElementInfo();
    	    
    	    elementInfo.setSourceRangeStart(ipe.getSourceLocation().getOffset());
    	    elementInfo.setName(name.toCharArray());
    	    elementInfo.setAJKind(getKindForString(name));
        
            return elementInfo;
        } catch (Exception e) {
            // can fail for any of a number of reasons.
            // return null so that we can try again later.
            return null;
        }
	}
	
   protected Kind getKindForString(String kindString) {
        for (int i = 0; i < IProgramElement.Kind.ALL.length; i++) {
            if (kindString.startsWith(IProgramElement.Kind.ALL[i].toString())) return IProgramElement.Kind.ALL[i];  
        }
        return IProgramElement.Kind.ERROR;
    }

//   public String getHandleIdentifier() {
//       return super.getHandleIdentifier() + AspectElement.JEM_EXTRA_INFO + 
//           ((DeclareElementInfo) createElementInfo()).getSourceRange().getOffset();
//   }
}
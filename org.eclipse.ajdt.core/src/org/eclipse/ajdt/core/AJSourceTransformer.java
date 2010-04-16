/*******************************************************************************
 * Copyright (c) 2009 SpringSource and others
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Andrew Eisenberg - initial version
 *******************************************************************************/
package org.eclipse.ajdt.core;

import org.eclipse.ajdt.core.codeconversion.AspectsConvertingParser;
import org.eclipse.ajdt.core.codeconversion.ConversionOptions;
import org.eclipse.contribution.jdt.sourceprovider.ISourceTransformer;

public class AJSourceTransformer implements ISourceTransformer {

    public char[] convert(char[] toConvert) {
        AspectsConvertingParser parser = new AspectsConvertingParser((char[]) toConvert.clone());
        parser.convert(ConversionOptions.CONSTANT_SIZE_KEEP_POINTCUTS);
        return parser.content;
    }

}

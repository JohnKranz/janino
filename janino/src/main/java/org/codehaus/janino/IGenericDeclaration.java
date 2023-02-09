package org.codehaus.janino;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.nullanalysis.Nullable;

public interface IGenericDeclaration {

    /**
     * @return Zero-length array if this {@link IClass} declares no type variables
     */
    ITypeVariable[]
    getITypeVariables() throws CompileException;

    @Nullable ITypeVariable
    findITypeVariable(String name) throws CompileException;

}

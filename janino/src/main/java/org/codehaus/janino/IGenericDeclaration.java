package org.codehaus.janino;

import org.codehaus.commons.compiler.CompileException;

import java.util.List;

public interface IGenericDeclaration {

    /**
     * @return Zero-length array if this {@link IClass} declares no type variables
     */
    List<ITypeVariable>
    getITypeVariables() throws CompileException;

    ITypeVariable
    findITypeVariable(String name) throws CompileException;

}

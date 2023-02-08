package org.codehaus.janino;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.InternalCompilerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IGenericDeclaration {

    /**
     * @return Zero-length array if this {@link IClass} declares no type variables
     */
    List<ITypeVariable>
    getITypeVariables() throws CompileException;

    ITypeVariable
    resolveGeneric(String name) throws CompileException;

    IClass getDeclaringIClass() throws CompileException;

}

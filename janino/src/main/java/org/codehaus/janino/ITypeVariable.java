
/*
 * Janino - An embedded Java[TM] compiler
 *
 * Copyright (c) 2021 Arno Unkrig. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *       following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *       following disclaimer in the documentation and/or other materials provided with the distribution.
 *    3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote
 *       products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.codehaus.janino;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.InternalCompilerException;

import java.util.*;

/**
 * Type bounds can either be a class or interface type, or a type variable. Example: {@code MySet<K extends
 * Comparable & T>}
 */
public abstract
class ITypeVariable extends IClass {

    protected String name;
    protected IGenericDeclaration iGenericDeclaration;
    protected List<IClass> bounds;

    protected List<IClass> interfaces = new ArrayList<>();

    public ITypeVariable(String name, IGenericDeclaration iGenericDeclaration){
        this.name = name;
        this.iGenericDeclaration = iGenericDeclaration;
    }

    public IGenericDeclaration getIGenericDeclaration() {
        return iGenericDeclaration;
    }

    public String getName(){
        return name;
    }

    protected void applyBounds(List<IClass> bounds) throws CompileException{
        this.bounds = bounds;
        boolean skip = true;
        for(IClass clz : bounds){
            if(skip){
                skip = false;
                continue;
            }
            if(clz.isInterface()){
                interfaces.add(clz);
            }else throw new CompileException("Bound type should be interface here.",null);
        }
    }

    protected abstract void reclassifyBounds() throws CompileException;

    @Override
    public List<ITypeVariable> getITypeVariables() throws CompileException {
        return Collections.emptyList();
    }

    @Override
    public IClass getSuperclass() throws CompileException {
        return bounds.get(0);
    }

    @Override
    public List<IClass> getInterfaces() throws CompileException {
        return interfaces;
    }

    @Override
    public Access getAccess() {
        return Access.PRIVATE;
    }

    @Override
    public boolean isAbstract() {
        return false;
    }

    @Override
    public String getDescriptor() {
        return bounds.get(0).getDescriptor();
    }

    @Override public boolean isFinal() {return false;}

    @Override public boolean isInterface() {return false;}







    @Override public IClass getOuterIClass() throws CompileException {return null;}
    @Override public IClass getDeclaringIClass() throws CompileException {return null;}
    @Override public List<IClass> getDeclaredIClasses() throws CompileException {return Collections.emptyList();}
    @Override public List<IField> getDeclaredIFields() {return Collections.emptyList();}
    @Override public List<IMethod> getDeclaredIMethods() {return NO_IMETHODS;}
    @Override public List<IConstructor> getDeclaredIConstructors() {return Collections.emptyList();}
    @Override public IAnnotation[] getIAnnotations() throws CompileException {return NO_ANNOTATIONS;}
    @Override public boolean isEnum() {return false;}
    @Override public boolean isArray() {return false;}
    @Override public boolean isPrimitive() {return false;}
    @Override public boolean isPrimitiveNumeric() {return false;}
    @Override public IClass getComponentType() {return null;}
}

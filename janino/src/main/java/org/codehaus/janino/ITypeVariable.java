
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
public
class ITypeVariable extends IClass {

    protected String name;
    protected IClass superClass;
    protected List<IClass> interfaces = new ArrayList<>();
    protected IGenericDeclaration iGenericDeclaration;
    protected IClass declaringIClass;

    public ITypeVariable(String name, IGenericDeclaration iGenericDeclaration){
        this.name = name;
        this.iGenericDeclaration = iGenericDeclaration;
    }

    @Override
    public IClass getDeclaringIClass() throws CompileException {
        if(iGenericDeclaration instanceof IClass)
            return (IClass) iGenericDeclaration;
        return iGenericDeclaration.getDeclaringIClass();
    }

    public IGenericDeclaration getIGenericDeclaration() {
        return iGenericDeclaration;
    }

    public String getName(){
        return name;
    }

    protected void setBounds(IClassLoader iClassLoader,List<IClass> bounds) throws CompileException{
        for(IClass clz : bounds){
            if(superClass == null){
                if(clz.isInterface())
                    superClass = iClassLoader.TYPE_java_lang_Object;
                else superClass = clz;
            }else if(clz.isInterface()){
                interfaces.add(clz);
            }else throw new CompileException("Bound type should be interface here.",null);
        }
    }

    public static abstract class ITypeVariableMap<T>{
        protected Map<String,ITypeVariable> iTypeVariableMap = new HashMap<>();
        protected Map<String,List<T>> iBoundDataMap = new HashMap<>();
        protected IGenericDeclaration igd;
        public ITypeVariableMap(IGenericDeclaration igd){
            this.igd = igd;
        }

        public void addITypeVariable(String name,List<T> boundsData){
            if(iTypeVariableMap.containsKey(name)) throw new InternalCompilerException("Duplicated type parameter "+name);
            iTypeVariableMap.put(name,new ITypeVariable(name,igd));
            iBoundDataMap.put(name,boundsData);
        }

        public ITypeVariable resolveGeneric(String name) throws CompileException {
            ITypeVariable itv = iTypeVariableMap.get(name);
            if(itv== null) itv = igd.getDeclaringIClass().resolveGeneric(name);
            return itv;
        }

        public abstract IClass reclassifyBoundType(T typeData) throws CompileException ;

        public List<ITypeVariable> apply(IClassLoader iClassLoader) throws CompileException {
            for(String name : iBoundDataMap.keySet()){
                List<IClass> bounds = new ArrayList<>();
                for(T data : iBoundDataMap.get(name)){
                    bounds.add(reclassifyBoundType(data));
                }
                iTypeVariableMap.get(name).setBounds(iClassLoader,bounds);
            }
            return new ArrayList<>(iTypeVariableMap.values());
        }
    }





    @Override
    public List<ITypeVariable> getITypeVariables() throws CompileException {
        return Collections.emptyList();
    }

    @Override
    public IClass getSuperclass() throws CompileException {
        return superClass;
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
        return null;
    }

    @Override public boolean isFinal() {return false;}

    @Override public boolean isInterface() {return false;}









    @Override
    public IClass getOuterIClass() throws CompileException {
        return null;
    }

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

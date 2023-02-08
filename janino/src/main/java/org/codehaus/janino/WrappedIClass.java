package org.codehaus.janino;

import org.codehaus.commons.compiler.CompileException;

import java.util.List;

public class WrappedIClass extends IClass{

    @Override public boolean isArray() {return false;}
    @Override public boolean isPrimitive() {return false;}
    @Override public boolean isPrimitiveNumeric() {return false;}

    @Override public IClass getComponentType() {return null;}
    @Override
    public IAnnotation[] getIAnnotations() throws CompileException {
        return wrappedClass.getIAnnotations();
    }

    @Override
    public List<ITypeVariable> getITypeVariables() throws CompileException {
        return wrappedClass.getITypeVariables();
    }

    @Override
    public IClass getSuperclass() throws CompileException {
        return wrappedClass.getSuperclass();
    }

    @Override
    public List<IMethod> getDeclaredIMethods(){
        return wrappedClass.getDeclaredIMethods();
    }

    @Override
    public List<IConstructor> getDeclaredIConstructors() {
        return wrappedClass.getDeclaredIConstructors();
    }

    @Override
    public List<IClass> getInterfaces() throws CompileException {
        return wrappedClass.getInterfaces();
    }

    @Override
    public List<IField> getDeclaredIFields() {
        return wrappedClass.getDeclaredIFields();
    }

    @Override
    public List<IClass> getDeclaredIClasses() throws CompileException {
        return wrappedClass.getDeclaredIClasses();
    }

    @Override
    public IClass getOuterIClass() throws CompileException {
        return wrappedClass.getOuterIClass();
    }

    @Override
    public IClass getDeclaringIClass() throws CompileException {
        return wrappedClass.getDeclaringIClass();
    }

    @Override public boolean isInterface() {return wrappedClass.isInterface();}
    @Override public boolean isEnum() {return wrappedClass.isEnum();}
    @Override public boolean isAbstract() {return wrappedClass.isAbstract();}
    @Override public String getDescriptor() {return wrappedClass.getDescriptor();}
    @Override public Access getAccess() {return wrappedClass.getAccess();}
    @Override public boolean isFinal() {return wrappedClass.isFinal();}



    protected IClass wrappedClass;

}

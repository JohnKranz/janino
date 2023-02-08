
/*
 * Janino - An embedded Java[TM] compiler
 *
 * Copyright (c) 2001-2010 Arno Unkrig. All rights reserved.
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

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.InternalCompilerException;
import org.codehaus.commons.nullanalysis.Nullable;

/**
 * A simplified equivalent to "java.lang.reflect".
 * <p>
 *   'JLS7' means a reference to the <a href="http://docs.oracle.com/javase/specs/">Java Language Specification, Java
 *   SE 7 Edition</a>.
 * </p>
 */
public abstract
class IClass {

    private static final Logger LOGGER = Logger.getLogger(IClass.class.getName());

    /**
     * Special return value for {@link IField#getConstantValue()} indicating that the field does <em>not</em> have a
     * constant value.
     */
    public static final Object NOT_CONSTANT = new Object() {
        @Override public String toString() { return "NOT_CONSTANT"; }
    };

    /**
     * The {@link IClass} of the {@code null} literal.
     */
    public static final IClass NULL = new IClass() {
        @Override public ITypeVariable[]  getITypeVariables()        { return new ITypeVariable[0]; }
        @Override @Nullable public IClass getComponentType()         { return null;                 }
        @Override public IAnnotation[] getIAnnotations() throws CompileException {return IClass.NO_ANNOTATIONS;}
        @Override public List<IClass>         getDeclaredIClasses()      { return Collections.emptyList();        }
        @Override public List<IConstructor>   getDeclaredIConstructors() { return Collections.emptyList();  }
        @Override public List<IField>         getDeclaredIFields()       { return Collections.emptyList();        }
        @Override public List<IMethod>        getDeclaredIMethods()      { return Collections.emptyList();       }
        @Override @Nullable public IClass getDeclaringIClass()       { return null;                 }
        @Override public String           getDescriptor()            { return "";                   }
        @Override public List<IClass>         getInterfaces()            { return Collections.emptyList();        }
        @Override @Nullable public IClass getOuterIClass()           { return null;                 }
        @Override @Nullable public IClass getSuperclass()            { return null;                 }
        @Override public boolean             isAbstract()                { return false;                }
        @Override public boolean             isArray()                   { return false;                }
        @Override public boolean             isFinal()                   { return true;                 }
        @Override public boolean             isEnum()                    { return false;                }
        @Override public boolean             isInterface()               { return false;                }
        @Override public boolean             isPrimitive()               { return true;                 }
        @Override public Access              getAccess()                 { return Access.PUBLIC;        }
        @Override public boolean             isPrimitiveNumeric()        { return false;                }
        @Override public String              toString()                  { return "(null type)";        }
    };

    /**
     * The {@link IClass} object for the type VOID.
     */
    public static final IClass VOID    = new PrimitiveIClass(Descriptor.VOID);

    /**
     * The {@link IClass} object for the primitive type BYTE.
     */
    public static final IClass BYTE    = new PrimitiveIClass(Descriptor.BYTE);

    /**
     * The {@link IClass} object for the primitive type CHAR.
     */
    public static final IClass CHAR    = new PrimitiveIClass(Descriptor.CHAR);

    /**
     * The {@link IClass} object for the primitive type DOUBLE.
     */
    public static final IClass DOUBLE  = new PrimitiveIClass(Descriptor.DOUBLE);

    /**
     * The {@link IClass} object for the primitive type FLOAT.
     */
    public static final IClass FLOAT   = new PrimitiveIClass(Descriptor.FLOAT);

    /**
     * The {@link IClass} object for the primitive type INT.
     */
    public static final IClass INT     = new PrimitiveIClass(Descriptor.INT);

    /**
     * The {@link IClass} object for the primitive type LONG.
     */
    public static final IClass LONG    = new PrimitiveIClass(Descriptor.LONG);

    /**
     * The {@link IClass} object for the primitive type SHORT.
     */
    public static final IClass SHORT   = new PrimitiveIClass(Descriptor.SHORT);

    /**
     * The {@link IClass} object for the primitive type BOOLEAN.
     */
    public static final IClass BOOLEAN = new PrimitiveIClass(Descriptor.BOOLEAN);

    private static
    class PrimitiveIClass extends IClass {
        private final String fieldDescriptor;

        PrimitiveIClass(String fieldDescriptor) { this.fieldDescriptor = fieldDescriptor; }

        @Override public ITypeVariable[]  getITypeVariables()        { return new ITypeVariable[0]; }
        @Override @Nullable public IClass getComponentType()         { return null;                 }

        @Override public IAnnotation[] getIAnnotations() throws CompileException {return IClass.NO_ANNOTATIONS;}

        @Override public List<IClass>         getDeclaredIClasses()      { return Collections.emptyList();        }
        @Override public List<IConstructor>   getDeclaredIConstructors() { return Collections.emptyList();  }
        @Override public List<IField>         getDeclaredIFields()       { return Collections.emptyList();        }
        @Override public List<IMethod>        getDeclaredIMethods()      { return Collections.emptyList();       }
        @Override @Nullable public IClass getDeclaringIClass()       { return null;                 }
        @Override public String           getDescriptor()            { return this.fieldDescriptor; }
        @Override public List<IClass>         getInterfaces()            { return Collections.emptyList();        }
        @Override @Nullable public IClass getOuterIClass()           { return null;                 }
        @Override @Nullable public IClass getSuperclass()            { return null;                 }
        @Override public boolean             isAbstract()                { return false;                }
        @Override public boolean             isArray()                   { return false;                }
        @Override public boolean             isFinal()                   { return true;                 }
        @Override public boolean             isEnum()                    { return false;                }
        @Override public boolean             isInterface()               { return false;                }
        @Override public boolean             isPrimitive()               { return true;                 }
        @Override public Access              getAccess()                 { return Access.PUBLIC;        }

        @Override public boolean
        isPrimitiveNumeric() { return Descriptor.isPrimitiveNumeric(this.fieldDescriptor); }
    }

    /**
     * @return Zero-length array if this {@link IClass} declares no type variables
     */
    public abstract ITypeVariable[]
    getITypeVariables() throws CompileException;

    public ITypeVariable resolveGeneric(String name) throws CompileException {
        for(ITypeVariable itv : getITypeVariables()){
            if(itv.getName().equals(name))
                return itv;
        }
        if(getDeclaringIClass() == null)
            throw new CompileException("Generic type " + name + "not found.",null);
        return getDeclaringIClass().resolveGeneric(name);
    }


    public IClass getParameterizedType(ITypeVariable itv){return itv;}

    /**
     * Returns the superclass of the class.
     * <p>
     *   Returns {@code null} for class {@link Object}, interfaces, arrays, primitive types and {@code void}.
     * </p>
     */
    @Nullable public abstract IClass
    getSuperclass() throws CompileException;


    /**
     * Returns the methods of the class or interface (but not inherited methods). For covariant methods, only the
     * method with the most derived return type is included.
     * <p>
     *   Returns an empty array for an array, primitive type or {@code void}.
     * </p>
     */
    public abstract List<IMethod>
    getDeclaredIMethods();

    public List<IMethod> getDeclaredIMethods(String name){
        List<IMethod> iMethods = new ArrayList<>();
        for(IMethod method : getDeclaredIMethods()){
            if(method.getName().equals(name))
                iMethods.add(method);
        }
        return iMethods;
    }

    /**
     * Returns all methods declared in the class or interface, its superclasses and its superinterfaces.
     *
     * @return an array of {@link IMethod}s that must not be modified
     */
    public final List<IMethod>
    getIMethods() throws CompileException {

        if (this.iMethodCache != null) return this.iMethodCache;

        List<IMethod> iMethods = new ArrayList<>();
        this.getIMethods(iMethods);
        return this.iMethodCache = iMethods;
    }
    @Nullable private List<IMethod> iMethodCache;

    private void
    getIMethods(List<IMethod> result) throws CompileException {
        List<IMethod> ms = this.getDeclaredIMethods();

        SCAN_DECLARED_METHODS:
        for (IMethod candidate : ms) {
            MethodDescriptor candidateDescriptor = candidate.getDescriptor();
            String           candidateName       = candidate.getName();

            // Check if a method with the same name and descriptor has been added before.
            for (IMethod oldMethod : result) {
                if (
                    candidateName.equals(oldMethod.getName())
                    && candidateDescriptor.equals(oldMethod.getDescriptor())
                ) continue SCAN_DECLARED_METHODS;
            }
            result.add(candidate);
        }
        IClass sc = this.getSuperclass();
        if (sc != null) sc.getIMethods(result);

        for (IClass ii : this.getInterfaces()) ii.getIMethods(result);
    }

    protected static final List<IMethod> NO_IMETHODS = Collections.emptyList();

    /**
     * @return Whether this {@link IClass} (or its superclass or the interfaces it implements) has an {@link IMethod}
     *         with the given name and parameter types
     */
    public final boolean
    hasIMethod(String methodName, List<IClass> parameterTypes) throws CompileException {
        return this.findIMethod(methodName, parameterTypes) != null;
    }

    /**
     * @return The {@link IMethod} declared in this {@link IClass} (or its superclass or the interfaces it implements)
     *         with the given name and parameter types, or {@code null} if an applicable method could not be found
     */
    @Nullable public final IMethod
    findIMethod(String methodName, List<IClass> parameterTypes) throws CompileException {
        {
            IMethod result = null;
            for (IMethod im : this.getDeclaredIMethods(methodName)) {
                if (
                        im.getParameterTypes().equals(parameterTypes)
                    && (result == null || result.getReturnType().isAssignableFrom(im.getReturnType()))
                ) result = im;
            }
            if (result != null) return result;
        }

        {
            IClass superclass = this.getSuperclass();
            if (superclass != null) {
                IMethod result = superclass.findIMethod(methodName, parameterTypes);
                if (result != null) return result;
            }
        }

        {
            List<IClass> interfaces = this.getInterfaces();
            for (IClass interfacE : interfaces) {
                IMethod result = interfacE.findIMethod(methodName, parameterTypes);
                if (result != null) return result;
            }
        }

        return null;
    }

    /**
     * Returns all the constructors declared by the class represented by the type. If the class has a default
     * constructor, it is included.
     * <p>
     *   Returns an array with zero elements for an interface, array, primitive type or {@code void}.
     * </p>
     */
    public abstract List<IConstructor>
    getDeclaredIConstructors();

    /**
     * @return The {@link IConstructor} declared in this {@link IClass} with the given parameter types, or {@code null}
     *         if an applicable constructor could not be found
     */
    @Nullable public final IConstructor
    findIConstructor(List<IClass> parameterTypes) throws CompileException {
        List<IConstructor> ics = this.getDeclaredIConstructors();
        for (IConstructor ic : ics) {
            if (ic.getParameterTypes().equals(parameterTypes)) return ic;
        }

        return null;
    }

    /**
     * Returns the interfaces implemented by the class, respectively the superinterfaces of the interface, respectively
     * <code>{</code> {@link Cloneable}{@code ,} {@link Serializable} <code>}</code> for arrays.
     * <p>
     *   Returns an empty array for primitive types and {@code void}.
     * </p>
     */
    public abstract List<IClass>
    getInterfaces() throws CompileException;

    /**
     * Returns the {@link IField}s declared in this {@link IClass} (but not inherited fields).
     *
     * @return An empty array for an array, primitive type or {@code void}
     */
    public abstract List<IField>
    getDeclaredIFields();

    public IField
    getDeclaredIField(String name) {
        for(IField field : this.getDeclaredIFields()){
            if(field.getName().equals(name)) return field;
        }
        return null;
    }

    /**
     * @return The synthetic fields of an anonymous or local class, in the order in which they are passed to all
     *         constructors
     */
    public List<IField>
    getSyntheticIFields() { return Collections.emptyList(); }

    /**
     * Returns the classes and interfaces declared as members of the class (but not inherited classes and interfaces).
     * <p>
     *   Returns an empty array for an array, primitive type or {@code void}.
     * </p>
     */
    public abstract List<IClass>
    getDeclaredIClasses() throws CompileException ;

    /**
     * The following types have an "outer class":
     * <ul>
     *   <li>Anonymous classes declared in a non-static method of a class
     *   <li>Local classes declared in a non-static method of a class
     *   <li>Non-static member classes
     * </ul>
     *
     * @return The outer class of this type, or {@code null}
     */
    @Nullable public abstract IClass
    getOuterIClass() throws CompileException;

    /**
     * @return The accessibility of this type
     */
    public abstract Access getAccess();

    /**
     * Whether subclassing is allowed (JVMS 4.1 access_flags)
     *
     * @return {@code true} if subclassing is prohibited
     */
    public abstract boolean isFinal();

    /**
     * @return If this class is a member class, the declaring class, otherwise {@code null}
     */
    @Nullable public abstract IClass
    getDeclaringIClass() throws CompileException;

    /**
     * Whether the class may be instantiated (JVMS 4.1 access_flags).
     *
     * @return {@code true} if instantiation is prohibited
     */
    public abstract boolean isAbstract();

    public abstract String
    getDescriptor();

    /**
     * Convenience method that determines the field descriptors of an array of {@link IClass}es.
     *
     * @see #getDescriptor()
     */
    public static String[]
    getDescriptors(List<IClass> iClasses) {
        String[] descriptors = new String[iClasses.size()];
        for (int i = 0; i < iClasses.size(); ++i) descriptors[i] = iClasses.get(i).getDescriptor();
        return descriptors;
    }

    /**
     * @return Whether this type represents an enum
     */
    public abstract boolean isEnum();

    /**
     * @return Whether this type represents an interface
     */
    public abstract boolean isInterface();

    /**
     * @return Whether  this type represents an array
     */
    public abstract boolean isArray();

    /**
     * @return Whether this type represents a primitive type or {@code void}
     */
    public abstract boolean isPrimitive();

    /**
     * @return Whether this type represents {@code byte}, {@code short}, {@code int}, {@code long}, {@code char},
     *         {@code float} or {@code double}
     */
    public abstract boolean isPrimitiveNumeric();

    /**
     * @return The component type of the array, or {@code null} for classes, interfaces, primitive types and {@code
     *         void}
     */
    @Nullable public abstract IClass
    getComponentType();

    @Override public String
    toString() {
        String className = Descriptor.toClassName(this.getDescriptor());
        if (className.startsWith("java.lang.") && className.indexOf('.', 10) == -1) className = className.substring(10);
        return className;
    }

    /**
     * Determines if {@code this} is assignable from <var>that</var>. This is true if {@code this} is identical with
     * <var>that</var> (JLS7 5.1.1), or if <var>that</var> is widening-primitive-convertible to {@code this} (JLS7
     * 5.1.2), or if <var>that</var> is widening-reference-convertible to {@code this} (JLS7 5.1.5).
     */
    public boolean
    isAssignableFrom(IClass that) throws CompileException {

        // Identity conversion, JLS7 5.1.1
        if (this == that) return true;

        // Widening primitive conversion, JLS7 5.1.2
        {
            String ds = that.getDescriptor() + this.getDescriptor();
            if (ds.length() == 2 && IClass.PRIMITIVE_WIDENING_CONVERSIONS.contains(ds)) return true;
        }

        // Widening reference conversion, JLS7 5.1.5
        {

            // JLS7 5.1.4.1: Target type is superclass of source class type.
            if (that.isSubclassOf(this)) return true;

            // JLS7 5.1.4.2: Source class type implements target interface type.
            // JLS7 5.1.4.4: Source interface type implements target interface type.
            if (that.implementsInterface(this)) return true;

            // JLS7 5.1.4.3 Convert "null" literal to any reference type.
            if (that == IClass.NULL && !this.isPrimitive()) return true;

            // JLS7 5.1.4.5: From any interface to type "Object".
            if (that.isInterface() && this.getDescriptor().equals(Descriptor.JAVA_LANG_OBJECT)) return true;

            if (that.isArray()) {

                // JLS7 5.1.4.6: From any array type to type "Object".
                if (this.getDescriptor().equals(Descriptor.JAVA_LANG_OBJECT)) return true;

                // JLS7 5.1.4.7: From any array type to type "Cloneable".
                if (this.getDescriptor().equals(Descriptor.JAVA_LANG_CLONEABLE)) return true;

                // JLS7 5.1.4.8: From any array type to type "java.io.Serializable".
                if (this.getDescriptor().equals(Descriptor.JAVA_IO_SERIALIZABLE)) return true;

                // JLS7 5.1.4.9: From SC[] to TC[] while SC if widening reference convertible to TC.
                if (this.isArray()) {
                    IClass thisCt = this.getComponentType();
                    IClass thatCt = that.getComponentType();

                    assert thisCt != null;
                    assert thatCt != null;

                    if (!thisCt.isPrimitive() && thisCt.isAssignableFrom(thatCt)) return true;
                }
            }
        }
        return false;
    }

    private static final Set<String> PRIMITIVE_WIDENING_CONVERSIONS = new HashSet<>();
    static {
        String[] pwcs = {
            Descriptor.BYTE  + Descriptor.SHORT,

            Descriptor.BYTE  + Descriptor.INT,
            Descriptor.SHORT + Descriptor.INT,
            Descriptor.CHAR  + Descriptor.INT,

            Descriptor.BYTE  + Descriptor.LONG,
            Descriptor.SHORT + Descriptor.LONG,
            Descriptor.CHAR  + Descriptor.LONG,
            Descriptor.INT   + Descriptor.LONG,

            Descriptor.BYTE  + Descriptor.FLOAT,
            Descriptor.SHORT + Descriptor.FLOAT,
            Descriptor.CHAR  + Descriptor.FLOAT,
            Descriptor.INT   + Descriptor.FLOAT,

            Descriptor.LONG  + Descriptor.FLOAT,

            Descriptor.BYTE  + Descriptor.DOUBLE,
            Descriptor.SHORT + Descriptor.DOUBLE,
            Descriptor.CHAR  + Descriptor.DOUBLE,
            Descriptor.INT   + Descriptor.DOUBLE,

            Descriptor.LONG  + Descriptor.DOUBLE,

            Descriptor.FLOAT + Descriptor.DOUBLE,
        };
        for (String pwc : pwcs) IClass.PRIMITIVE_WIDENING_CONVERSIONS.add(pwc);
    }

    /**
     * Returns {@code true} if this class is an immediate or non-immediate subclass of {@code that} class.
     */
    public boolean
    isSubclassOf(IClass that) throws CompileException {
        for (IClass sc = this.getSuperclass(); sc != null; sc = sc.getSuperclass()) {
            if (sc == that) return true;
        }
        return false;
    }

    /**
     * If {@code this} represents a class: Return {@code true} if this class directly or indirectly implements {@code
     * that} interface.
     * <p>
     *   If {@code this} represents an interface: Return {@code true} if this interface directly or indirectly extends
     *   {@code that} interface.
     * </p>
     */
    public boolean
    implementsInterface(IClass that) throws CompileException {
        for (IClass c = this; c != null; c = c.getSuperclass()) {
            List<IClass> tis = c.getInterfaces();
            for (IClass ti : tis) {
                if (ti == that || ti.implementsInterface(that)) return true;
            }
        }
        return false;
    }

    /**
     * If <var>name</var> is {@code null}, finds all {@link IClass}es visible in the scope of the current
     * class.
     * <p>
     *   If <var>name</var> is not {@code null}, finds the member {@link IClass}es that has the given name. If
     *   the name is ambiguous (i.e. if more than one superclass, interface of enclosing type declares a type with that
     *   name), then the size of the returned array is greater than one.
     * </p>
     * <p>
     *   Examines superclasses, interfaces and enclosing type declarations.
     * </p>
     *
     * @return an array of {@link IClass}es in unspecified order, possibly of length zero
     */
    List<IClass>
    findMemberType(@Nullable String name) throws CompileException {
        List<IClass> res = (List<IClass>) this.memberTypeCache.get(name);
        if (res == null) {

            // Notice: A type may be added multiply to the result set because we are in its scope
            // multiply. E.g. the type is a member of a superclass AND a member of an enclosing type.
            Set<IClass> s = new HashSet<>();
            this.findMemberType(name, s);
            res = s.isEmpty() ? IClass.ZERO_ICLASSES : new ArrayList<>(s);

            this.memberTypeCache.put(name, res);
        }

        return res;
    }
    private final Map<String /*name*/, List<IClass>> memberTypeCache = new HashMap<>();
    private static final List<IClass>                ZERO_ICLASSES   = Collections.emptyList();
    private void
    findMemberType(@Nullable String name, Collection<IClass> result) throws CompileException {

        // Search for a type with the given name in the current class.
        List<IClass> memberTypes = this.getDeclaredIClasses();
        if (name == null) {
            result.addAll(memberTypes);
        } else {
            String memberDescriptor = Descriptor.fromClassName(
                Descriptor.toClassName(this.getDescriptor())
                + '$'
                + name
            );
            for (final IClass mt : memberTypes) {
                if (mt.getDescriptor().equals(memberDescriptor)) {
                    result.add(mt);
                    return;
                }
            }
        }

        // Examine superclass.
        {
            IClass superclass = this.getSuperclass();
            if (superclass != null) superclass.findMemberType(name, result);
        }

        // Examine interfaces.
        for (IClass i : this.getInterfaces()) i.findMemberType(name, result);

        // Examine enclosing type declarations.
        {
            IClass declaringIClass = this.getDeclaringIClass();
            IClass outerIClass     = this.getOuterIClass();
            if (declaringIClass != null) {
                declaringIClass.findMemberType(name, result);
            }
            if (outerIClass != null && outerIClass != declaringIClass) {
                outerIClass.findMemberType(name, result);
            }
        }
    }

    /**
     * @return The annotations of this type (possibly the empty array)
     */
    public abstract IAnnotation[]
    getIAnnotations() throws CompileException;

    /**
     * Array of zero {@link IAnnotation}s.
     */
    public static final IAnnotation[] NO_ANNOTATIONS = new IAnnotation[0];

    /**
     * Base for the members of an {@link IClass}. {@link IMember} are expected to be immutable, i.e. all getter methods
     * return constant values.
     */
    public
    interface IMember {

        /**
         * @return One of {@link Access#PRIVATE}, {@link Access#PROTECTED}, {@link Access#DEFAULT} and {@link
         *         Access#PUBLIC}.
         */
        Access getAccess();

        /**
         * @return Modifiers and/or annotations of this member
         */
        IAnnotation[] getAnnotations();

        /**
         * @return The {@link IClass} that declares this {@link IClass.IMember}
         */
        IClass getDeclaringIClass();
    }

    /**
     * Base class for {@link IConstructor} and {@link IMethod}.
     */
    public abstract
    class IInvocable implements IMember {

        private boolean argsNeedAdjust;

        /**
         * TODO
         */
        public void
        setArgsNeedAdjust(boolean newVal) { this.argsNeedAdjust = newVal; }

        /**
         * TODO
         */
        public boolean
        argsNeedAdjust() { return this.argsNeedAdjust; }

        /**
         * @return Whether this invocable is 'variable arity', i.e. its last parameter has an ellipsis ('...') after
         *         the type
         */
        public abstract boolean isVarargs();

        // Implement IMember.

        @Override public IClass getDeclaringIClass() { return IClass.this; }

        /**
         * Returns the types of the parameters of this constructor or method. This method is fast.
         */
        public final List<IClass>
        getParameterTypes() throws CompileException {
            if (this.parameterTypesCache != null) return this.parameterTypesCache;
            return (this.parameterTypesCache = this.getParameterTypes2());
        }
        @Nullable private List<IClass> parameterTypesCache;

        /**
         * Opposed to the {@link Constructor}, there is no magic "{@code this$0}" parameter.
         * <p>
         *   Opposed to the {@link Constructor}, {@code enum}s have no magic parameters "{@code String name}" and
         *   "{@code int ordinal}".
         * </p>
         * <p>
         *   However, the "synthetic parameters" ("{@code val$}<var>locvar</var>") <em>are</em> included.
         * </p>
         */
        public abstract List<IClass>
        getParameterTypes2() throws CompileException;

        /**
         * Returns the method descriptor of this constructor or method. This method is fast.
         */
        public final MethodDescriptor
        getDescriptor() throws CompileException {
            if (this.descriptorCache != null) return this.descriptorCache;
            return (this.descriptorCache = this.getDescriptor2());
        }
        @Nullable private MethodDescriptor descriptorCache;

        /**
         * Uncached implementation of {@link #getDescriptor()}.
         */
        public abstract MethodDescriptor
        getDescriptor2() throws CompileException;

        /**
         * Returns the types thrown by this constructor or method. This method is fast.
         */
        public final List<IClass>
        getThrownExceptions() throws CompileException {
            if (this.thrownExceptionsCache != null) return this.thrownExceptionsCache;
            return (this.thrownExceptionsCache = this.getThrownExceptions2());
        }
        @Nullable private List<IClass> thrownExceptionsCache;

        /**
         * @return The types thrown by this constructor or method
         */
        public abstract List<IClass>
        getThrownExceptions2() throws CompileException;

        /**
         * @return Whether this {@link IInvocable} is more specific then <var>that</var> (in the sense of JLS7
         *         15.12.2.5)
         */
        public boolean
        isMoreSpecificThan(IInvocable that) throws CompileException {
            IClass.LOGGER.entering(null, "isMoreSpecificThan", that);

            // a variable-length argument is always less specific than a fixed arity.
            final boolean thatIsVarargs;

            if ((thatIsVarargs = that.isVarargs()) != this.isVarargs()) {

                // Only one of the two is varargs.
                return thatIsVarargs;
            } else
            if (thatIsVarargs) {

                // Both are varargs.
                final List<IClass> thisParameterTypes = this.getParameterTypes();
                final List<IClass> thatParameterTypes = that.getParameterTypes();

                List<IClass> t, u;
                int      n, k;

                if (thisParameterTypes.size() >= thatParameterTypes.size()) {
                    t = thisParameterTypes;
                    u = thatParameterTypes;
                    n = t.size();
                    k = u.size();
                    List<IClass> s = u;
                    // this = T | T_n
                    // that = U | U_k
                    // n >= k
                    //              ignore generics, for now

                    // T0, T1, ..., Tn-1, Tn[]
                    // U0, U1, .., Uk[]
                    final int kMinus1 = k - 1;
                    for (int j = 0; j < kMinus1; ++j) {
                        // expect T[j] <: S[j]
                        if (!s.get(j).isAssignableFrom(t.get(j))) {
                            return false;
                        }
                    }

                    final IClass sk1 = s.get(kMinus1).getComponentType();
                    assert sk1 != null;

                    final int nMinus1 = n - 1;
                    for (int j = kMinus1; j < nMinus1; ++j) {
                        // expect T[j] <: S[k -1]
                        if (!sk1.isAssignableFrom(t.get(j))) {
                            return false;
                        }
                    }
                    if (!sk1.isAssignableFrom(t.get(nMinus1))) {
                        return false;
                    }
                } else {
                    u = thisParameterTypes;
                    t = thatParameterTypes;
                    n = t.size();
                    k = u.size();
                    List<IClass> s = t;
                    // n >= k
                    final int kMinus1 = k - 1;
                    for (int j = 0; j < kMinus1; ++j) {
                        // expect U[j] <: S[j]
                        if (!s.get(j).isAssignableFrom(u.get(j))) {
                            return false;
                        }
                    }

                    final IClass uk1 = u.get(kMinus1).getComponentType();
                    assert uk1 != null;

                    final int nMinus1 = n - 1;
                    for (int j = kMinus1; j < nMinus1; ++j) {
                        // expect U[k -1] <: S[j]
                        if (!s.get(j).isAssignableFrom(uk1)) {
                            return false;
                        }
                    }
                    IClass snm1ct = s.get(nMinus1).getComponentType();
                    assert snm1ct != null;
                    if (!snm1ct.isAssignableFrom(uk1)) {
                        return false;
                    }
                }

                return true;
            }

            // both are fixed arity

            // The following case is tricky: JLS7 says that the invocation is AMBIGUOUS, but only JAVAC 1.2 issues an
            // error; JAVAC 1.4.1, 1.5.0 and 1.6.0 obviously ignore the declaring type and invoke "A.meth(String)".
            // JLS7 is not clear about this. For compatibility with JAVA 1.4.1, 1.5.0 and 1.6.0, JANINO also ignores
            // the declaring type.
            //
            // See also JANINO-79 and JlsTests / 15.12.2.2
            // if (false) {
            //     if (!that.getDeclaringIClass().isAssignableFrom(this.getDeclaringIClass())) {
            //         if (IClass.DEBUG) System.out.println("falsE");
            //         return false;
            //     }
            // }

            List<IClass> thisParameterTypes = this.getParameterTypes();
            List<IClass> thatParameterTypes = that.getParameterTypes();
            for (int i = 0; i < thisParameterTypes.size(); ++i) {
                if (!thatParameterTypes.get(i).isAssignableFrom(thisParameterTypes.get(i))) {
                    IClass.LOGGER.exiting(null, "isMoreSpecificThan", false);
                    return false;
                }
            }

            boolean result = !thisParameterTypes.equals(thatParameterTypes);
            IClass.LOGGER.exiting(null, "isMoreSpecificThan", result);
            return result;
        }

        /**
         * @return Whether this {@link IInvocable} is less specific then <var>that</var> (in the sense of JLS7
         *         15.12.2.5)
         */
        public boolean
        isLessSpecificThan(IInvocable that) throws CompileException { return that.isMoreSpecificThan(this); }

        @Override public abstract String
        toString();
    }

    /**
     * Representation of a constructor of an {@link IClass}.
     */
    public abstract
    class IConstructor extends IInvocable {

        @Override public MethodDescriptor
        getDescriptor2() throws CompileException {

            List<IClass> parameterTypes = this.getParameterTypes();

            // Iff this is an inner class, prepend the magic "this$0" constructor parameter.
            {
                IClass outerIClass = IClass.this.getOuterIClass();
                if (outerIClass != null) {
                    List<IClass> tmp = new ArrayList<>();
                    tmp.add(outerIClass);
                    System.arraycopy(parameterTypes, 0, tmp, 1, parameterTypes.size());
                    parameterTypes = tmp;
                }
            }

            String[] parameterFds = IClass.getDescriptors(parameterTypes);

            // Iff this is an enum, prepend the magic "String name" and "int ordinal" constructor parameters.
            if (this.getDeclaringIClass().isEnum()) {
                String[] tmp = new String[parameterFds.length + 2];
                tmp[0] = Descriptor.JAVA_LANG_STRING;
                tmp[1] = Descriptor.INT;
                System.arraycopy(parameterFds, 0, tmp, 2, parameterFds.length);
                parameterFds = tmp;
            }

            return new MethodDescriptor(Descriptor.VOID, parameterFds);
        }

        @Override public String
        toString() {
            StringBuilder sb = new StringBuilder(this.getDeclaringIClass().toString());
            sb.append('(');
            try {
                List<IClass> parameterTypes = this.getParameterTypes();
                for (int i = 0; i < parameterTypes.size(); ++i) {
                    if (i > 0) sb.append(", ");
                    sb.append(parameterTypes.get(i).toString());
                }
            } catch (CompileException ex) {
                sb.append("<invalid type>");
            }
            sb.append(')');
            return sb.toString();
        }
    }

    /**
     * Representation of a method in an {@link IClass}.
     */
    public abstract
    class IMethod extends IInvocable {

        /**
         * @return Whether this method is STATIC
         */
        public abstract boolean isStatic();

        /**
         * @return Whether this method is ABSTRACT
         */
        public abstract boolean isAbstract();

        /**
         * @return The return type of this method
         */
        public abstract IClass getReturnType() throws CompileException;

        /**
         * @return The name of this method
         */
        public abstract String getName();

        @Override public MethodDescriptor
        getDescriptor2() throws CompileException {
            return new MethodDescriptor(
                this.getReturnType().getDescriptor(),
                IClass.getDescriptors(this.getParameterTypes())
            );
        }

        @Override public String
        toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.getAccess().toString()).append(' ');
            if (this.isStatic()) sb.append("static ");
            if (this.isAbstract()) sb.append("abstract ");
            try {
                sb.append(this.getReturnType().toString());
            } catch (CompileException ex) {
                sb.append("<invalid type>");
            }
            sb.append(' ');
            sb.append(this.getDeclaringIClass().toString());
            sb.append('.');
            sb.append(this.getName());
            sb.append('(');
            try {
                List<IClass> parameterTypes = this.getParameterTypes();
                for (int i = 0; i < parameterTypes.size(); ++i) {
                    if (i > 0) sb.append(", ");
                    sb.append(parameterTypes.get(i).toString());
                }
            } catch (CompileException ex) {
                sb.append("<invalid type>");
            }
            sb.append(')');
            try {
                List<IClass> tes = this.getThrownExceptions();
                if (tes.size() > 0) {
                    sb.append(" throws ").append(tes.get(0));
                    for (int i = 1; i < tes.size(); ++i) sb.append(", ").append(tes.get(i));
                }
            } catch (CompileException ex) {
                sb.append("<invalid thrown exception type>");
            }
            return sb.toString();
        }
    }

    /**
     * Representation of a field of this {@link IClass}.
     */
    public abstract
    class IField implements IMember {

        // Implement IMember.
        @Override public abstract Access getAccess();
        @Override public IClass          getDeclaringIClass() { return IClass.this; }

        /**
         * @return Whether this field is STATIC
         */
        public abstract boolean isStatic();

        /**
         * @return The type of this field
         */
        public abstract IClass getType() throws CompileException;

        /**
         * @return The name this field
         */
        public abstract String getName();

        /**
         * @return The descriptor of this field
         */
        public String
        getDescriptor() throws CompileException { return this.getType().getDescriptor(); }

        /**
         * Returns the value of the field if it is a compile-time constant value, i.e. the field is FINAL and its
         * initializer is a constant expression (JLS7 15.28, bullet 12).
         */
        @Nullable public abstract Object getConstantValue() throws CompileException;

        @Override public String
        toString() { return this.getDeclaringIClass().toString() + "." + this.getName(); }
    }

    /**
     * Representation of a Java "annotation".
     */
    public
    interface IAnnotation {

        /**
         * @return The type of the annotation
         */
        IClass getAnnotationType() throws CompileException;

        /**
         * Returns the value of the <var>name</var>d element:
         * <dl>
         *   <dt>{@link Boolean}</dt>
         *   <dt>{@link Byte}</dt>
         *   <dt>{@link Character}</dt>
         *   <dt>{@link Double}</dt>
         *   <dt>{@link Float}</dt>
         *   <dt>{@link Integer}</dt>
         *   <dt>{@link Long}</dt>
         *   <dt>{@link Short}</dt>
         *   <dd>
         *     A primitive value
         *   </dd>
         *   <dt>{@link String}</dt>
         *   <dd>
         *     A string value
         *   </dd>
         *   <dt>{@link IField}</dt>
         *   <dd>
         *     An enum constant
         *   </dd>
         *   <dt>{@link IClass}</dt>
         *   <dd>
         *     A class literal
         *   </dd>
         *   <dt>{@link IAnnotation}</dt>
         *   <dd>
         *     An annotation
         *   </dd>
         *   <dt>{@link Object}{@code []}</dt>
         *   <dd>
         *     An array value
         *   </dd>
         * </dl>
         * <p>
         *   Notice that {@code null} is <em>not</em> a valid return value.
         * </p>
         */
        Object getElementValue(String name) throws CompileException;
    }


//    /**
//     * @param typeArguments Zero-length array if this {@link IClass} is not parameterized
//     */
//    public IClass
//    parameterize(final IClass[] typeArguments) throws CompileException {
//
//        ITypeVariable[] iClassVariables = this.getITypeVariables();
//
//        if (typeArguments.length == 0 && iClassVariables.length == 0) return this;
//
//        if (iClassVariables.length == 0) {
//            throw new CompileException("Class \"" + this.toString() + "\" cannot be parameterized", null);
//        }
//
//        if (typeArguments.length == 0) {
//            // throw new CompileException("Class \"" + this.toString() + "\" must be parameterized", null);
//            return this;
//        }
//
//        List<IClass> key = Arrays.asList(typeArguments);
//
//        Map<List<IClass> /*typeArguments*/, IParameterizedType> m = this.parameterizations;
//        if (m == null) this.parameterizations = (m = new HashMap<List<IClass>, IParameterizedType>());
//
//        {
//            IClass result = m.get(key);
//            if (result != null) return result;
//        }
//
//        if (iClassVariables.length != typeArguments.length) {
//            throw new CompileException((
//                "Number of type arguments ("
//                + typeArguments.length
//                + ") does not match number of type variables ("
//                + iClassVariables.length
//                + ") of class \""
//                + this.toString()
//                + "\""
//            ), null);
//        }
//
//        for (int i = 0; i < iClassVariables.length; i++) {
//            ITypeVariable tv = iClassVariables[i];
//            IClass         ta = typeArguments[i];
//
//            for (IClass b : tv.getBounds()) {
//                assert b instanceof IClass;
//
//                if (ta instanceof IClass) {
//                    IClass taic = (IClass) ta;
//                    if (!(ta == b || taic.isInterface() || taic.isSubclassOf((IClass) b))) {
//                        throw new CompileException(
//                            "Type argument #" + (1 + i) + " (" + ta + ") is not a subclass of bound " + b,
//                            null
//                        );
//                    }
//                } else
//                if (ta instanceof IWildcardType) {
//                    IWildcardType tawt = (IWildcardType) ta;
//                    IClass ub = (IClass) tawt.getUpperBound();
//                    if (!(ub == b || ub.isSubclassOf((IClass) b))) {
//                        throw new CompileException(
//                            "Type argument #" + (1 + i) + " (" + ta + ") is not a subclass of " + b,
//                            null
//                        );
//                    }
//                    IClass lb = (IClass) tawt.getLowerBound();
//                    if (lb != null && !"Ljava/lang/Object;".equals(((IClass) b).getDescriptor())) {
//                        throw new CompileException(
//                            "Type argument #" + (1 + i) + " (" + ta + ") is not a subclass of " + b,
//                            null
//                        );
//                    }
//                } else
//                if (ta instanceof ITypeVariable) {
//                    throw new AssertionError(ta.getClass() + ": " + ta);
//                } else
//                if (ta instanceof IParameterizedType) {
//                    throw new AssertionError(ta.getClass() + ": " + ta);
//                } else
//                {
//                    throw new AssertionError(ta.getClass() + ": " + ta);
//                }
//            }
//        }
//
//        IParameterizedType result = new IParameterizedType() {
//
//            @Override public IClass[] getActualTypeArguments() { return typeArguments; }
//            @Override public IClass getRawType() { return IClass.this; }
//
//            @Override public String
//            toString() {
//                return IClass.this + "<" + Arrays.toString(typeArguments) + ">";
//            }
//        };
//
//        m.put(key, result);
//
//        return result;
//    }
//    @Nullable Map<List<IClass> /*typeArguments*/, IParameterizedType> parameterizations;
}

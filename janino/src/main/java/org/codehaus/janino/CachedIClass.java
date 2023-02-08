package org.codehaus.janino;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.nullanalysis.Nullable;

import java.io.Serializable;
import java.util.*;

public abstract class CachedIClass extends IClass{

    /**
     * @return Zero-length array if this {@link IClass} declares no type variables
     */
    @Override
    public final List<ITypeVariable>
    getITypeVariables() throws CompileException {
        if (this.iClassVariablesCache != null) return this.iClassVariablesCache;
        return (this.iClassVariablesCache = this.getITypeVariables2());
    }
    @Nullable private List<ITypeVariable> iClassVariablesCache;

    /**
     * The uncached version of {@link #getDeclaredIConstructors()} which must be implemented by derived classes.
     */
    protected abstract List<ITypeVariable> getITypeVariables2() throws CompileException;

    /**
     * Returns all the constructors declared by the class represented by the type. If the class has a default
     * constructor, it is included.
     * <p>
     *   Returns an array with zero elements for an interface, array, primitive type or {@code void}.
     * </p>
     */
    public final List<IConstructor>
    getDeclaredIConstructors() {
        if (this.declaredIConstructorsCache != null) return this.declaredIConstructorsCache;

        return (this.declaredIConstructorsCache = this.getDeclaredIConstructors2());
    }
    @Nullable private List<IConstructor> declaredIConstructorsCache;

    /**
     * The uncached version of {@link #getDeclaredIConstructors()} which must be implemented by derived classes.
     */
    protected abstract List<IConstructor> getDeclaredIConstructors2();

    /**
     * Returns the methods of the class or interface (but not inherited methods). For covariant methods, only the
     * method with the most derived return type is included.
     * <p>
     *   Returns an empty array for an array, primitive type or {@code void}.
     * </p>
     */
    public final List<IMethod>
    getDeclaredIMethods() {
        if (this.declaredIMethodsCache != null) return this.declaredIMethodsCache;
        return (this.declaredIMethodsCache = this.getDeclaredIMethods2());
    }
    @Nullable private List<IMethod> declaredIMethodsCache;

    /**
     * The uncached version of {@link #getDeclaredIMethods()} which must be implemented by derived classes.
     */
    protected abstract List<IMethod> getDeclaredIMethods2();

    /**
     * Returns all methods with the given name declared in the class or interface (but not inherited methods).
     * <p>
     *   Returns an empty array if no methods with that name are declared.
     * </p>
     *
     * @return an array of {@link IMethod}s that must not be modified
     */
    public final List<IMethod>
    getDeclaredIMethods(String methodName) {
        Map<String, Object> dimc = this.declaredIMethodCache;
        if (dimc == null) {
            List<IMethod> dims = this.getDeclaredIMethods();

            // Fill the map with "IMethod"s and "List<IMethod>"s.
            dimc = new HashMap<>();
            for (IMethod dim : dims) {
                String  mn  = dim.getName();
                Object  o   = dimc.get(mn);
                if (o == null) {
                    dimc.put(mn, dim);
                } else
                if (o instanceof IMethod) {
                    List<IMethod> l = new ArrayList<>();
                    l.add((IMethod) o);
                    l.add(dim);
                    dimc.put(mn, l);
                } else {
                    @SuppressWarnings("unchecked") List<IMethod> tmp = (List<IMethod>) o;
                    tmp.add(dim);
                }
            }

            // Convert "IMethod"s and "List"s to "IMethod[]"s.
            for (Map.Entry<String, Object/*IMethod-or-List<IMethod>*/> me : dimc.entrySet()) {
                Object v = me.getValue();
                if (v instanceof IMethod) {
                    me.setValue(Collections.singletonList((IMethod) v));
                } else {
                    @SuppressWarnings("unchecked") List<IMethod> l = (List<IMethod>) v;
                    me.setValue(l);
                }
            }
            this.declaredIMethodCache = dimc;
        }

        List<IMethod> methods = (List<IMethod>) dimc.get(methodName);
        return methods == null ? IClass.NO_IMETHODS : methods;
    }
    @Nullable private Map<String /*methodName*/, Object /*IMethod-or-List<IMethod>*/> declaredIMethodCache;


    /**
     * Returns the {@link IField}s declared in this {@link IClass} (but not inherited fields).
     *
     * @return An empty array for an array, primitive type or {@code void}
     */
    public final List<IField>
    getDeclaredIFields() {
        Collection<IField> allFields = this.getDeclaredIFieldsCache().values();
        return new ArrayList<>(allFields);
    }

    /**
     * @return {@code String fieldName => IField}
     */
    private Map<String /*fieldName*/, IField>
    getDeclaredIFieldsCache() {
        if (this.declaredIFieldsCache != null) return this.declaredIFieldsCache;

        List<IField> fields = this.getDeclaredIFields2();

        Map<String /*fieldName*/, IField> m = new LinkedHashMap<>();
        for (IField f : fields) m.put(f.getName(), f);
        return (this.declaredIFieldsCache = m);
    }

    /**
     * Returns the named {@link IField} declared in this {@link IClass} (does not work for inherited fields).
     *
     * @return {@code null} iff this {@link IClass} does not declare an {@link IField} with that name
     */
    @Override @Nullable public final IField
    getDeclaredIField(String name) { return (IField) this.getDeclaredIFieldsCache().get(name); }

    /**
     * Clears the cache of declared fields which this class maintains in order to minimize the invocations of {@link
     * #getDeclaredIFields2()}.
     */
    protected void
    clearIFieldCaches() { this.declaredIFieldsCache = null; }

    @Nullable private Map<String /*fieldName*/, IField> declaredIFieldsCache;

    /**
     * Uncached version of {@link #getDeclaredIFields()}.
     */
    protected abstract List<IField> getDeclaredIFields2();

    /**
     * Returns the superclass of the class.
     * <p>
     *   Returns {@code null} for class {@link Object}, interfaces, arrays, primitive types and {@code void}.
     * </p>
     */
    @Nullable public final IClass
    getSuperclass() throws CompileException {
        if (this.superclassIsCached) return this.superclassCache;

        IClass sc = this.getSuperclass2();
        if (sc != null && sc.isSubclassOf(this)) {
            throw new CompileException(
                    "Class circularity detected for \"" + Descriptor.toClassName(this.getDescriptor()) + "\"",
                    null
            );
        }
        this.superclassIsCached = true;
        return (this.superclassCache = sc);
    }
    private boolean          superclassIsCached;
    @Nullable private IClass superclassCache;

    /**
     * @see #getSuperclass()
     */
    @Nullable protected abstract IClass
    getSuperclass2() throws CompileException;


    /**
     * Returns the interfaces implemented by the class, respectively the superinterfaces of the interface, respectively
     * <code>{</code> {@link Cloneable}{@code ,} {@link Serializable} <code>}</code> for arrays.
     * <p>
     *   Returns an empty array for primitive types and {@code void}.
     * </p>
     */
    public final List<IClass>
    getInterfaces() throws CompileException {
        if (this.interfacesCache != null) return this.interfacesCache;

        List<IClass> is = this.getInterfaces2();
        for (IClass ii : is) {
            if (ii.implementsInterface(this)) {
                throw new CompileException(
                        "Interface circularity detected for \"" + Descriptor.toClassName(this.getDescriptor()) + "\"",
                        null
                );
            }
        }
        return (this.interfacesCache = is);
    }
    @Nullable private List<IClass> interfacesCache;

    /**
     * @see #getInterfaces()
     */
    protected abstract List<IClass> getInterfaces2() throws CompileException;



    /**
     * Returns the classes and interfaces declared as members of the class (but not inherited classes and interfaces).
     * <p>
     *   Returns an empty array for an array, primitive type or {@code void}.
     * </p>
     */
    public final List<IClass>
    getDeclaredIClasses() throws CompileException {
        if (this.declaredIClassesCache != null) return this.declaredIClassesCache;
        return (this.declaredIClassesCache = this.getDeclaredIClasses2());
    }
    @Nullable private List<IClass> declaredIClassesCache;

    /**
     * @return The member types of this type
     */
    protected abstract List<IClass> getDeclaredIClasses2() throws CompileException;




    /**
     * @return If this class is a member class, the declaring class, otherwise {@code null}
     */
    @Nullable public final IClass
    getDeclaringIClass() throws CompileException {
        if (!this.declaringIClassIsCached) {
            this.declaringIClassCache    = this.getDeclaringIClass2();
            this.declaringIClassIsCached = true;
        }
        return this.declaringIClassCache;
    }
    private boolean          declaringIClassIsCached;
    @Nullable private IClass declaringIClassCache;

    /**
     * @return If this class is a member class, the declaring class, otherwise {@code null}
     */
    @Nullable protected abstract IClass
    getDeclaringIClass2() throws CompileException;


    /**
     * Returns the field descriptor for the type as defined by JVMS 4.3.2. This method is fast.
     */
    public final String
    getDescriptor() {
        if (this.descriptorCache != null) return this.descriptorCache;
        return (this.descriptorCache = this.getDescriptor2());
    }
    @Nullable private String descriptorCache;

    /**
     * @return The field descriptor for the type as defined by JVMS 4.3.2.
     */
    protected abstract String getDescriptor2();

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
    @Nullable public final IClass
    getOuterIClass() throws CompileException {
        if (this.outerIClassIsCached) return this.outerIClassCache;

        this.outerIClassIsCached = true;
        return (this.outerIClassCache = this.getOuterIClass2());
    }
    private boolean          outerIClassIsCached;
    @Nullable private IClass outerIClassCache;

    /**
     * @see #getOuterIClass()
     */
    @Nullable protected abstract IClass
    getOuterIClass2() throws CompileException;

    /**
     * @return The component type of the array, or {@code null} for classes, interfaces, primitive types and {@code
     *         void}
     */
    @Nullable public final IClass
    getComponentType() {
        if (this.componentTypeIsCached) return this.componentTypeCache;

        this.componentTypeCache    = this.getComponentType2();
        this.componentTypeIsCached = true;
        return this.componentTypeCache;
    }
    private boolean          componentTypeIsCached;
    @Nullable private IClass componentTypeCache;

    /**
     * @see #getComponentType()
     */
    @Nullable protected abstract IClass
    getComponentType2();

    /**
     * @return The annotations of this type (possibly the empty array)
     */
    public final IAnnotation[]
    getIAnnotations() throws CompileException {
        if (this.iAnnotationsCache != null) return this.iAnnotationsCache;
        return (this.iAnnotationsCache = this.getIAnnotations2());
    }
    @Nullable private IAnnotation[] iAnnotationsCache;

    /**
     * @throws CompileException
     */
    protected IAnnotation[]
    getIAnnotations2() throws CompileException { return IClass.NO_ANNOTATIONS; }

    /**
     * This class caches the declared methods in order to minimize the invocations of {@link #getDeclaredIMethods2()}.
     */
    public void
    invalidateMethodCaches() {
        this.declaredIMethodsCache = null;
        this.declaredIMethodCache  = null;
    }

}

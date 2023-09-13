package de.tum.in.ase.fop;

import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"unused", "FieldMayBeFinal"})

public class MJClass extends MJType {

    private MJClass superClass;

    private boolean isAbstract;

    public MJClass getSuperClass() {
        return superClass;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public MJClass(
            String name,
            List<MJInterface> implementedInterfaces,
            List<MJMethod> methods,
            List<MJGenericType> genericTypes,
            MJClass superClass,
            boolean isAbstract
    ) {
        super(name, implementedInterfaces, methods, genericTypes);
        this.superClass = superClass;
        this.isAbstract = isAbstract;
    }

    @Override
    protected List<MJMethod> getAllMethods() {
        return getAllMethods(new ArrayList<>());
    }

    @Override
    protected List<MJMethod> getAllMethods(List<MJMethodSignature> usedSignature) {
        List<MJMethod> res = super.getAllMethods(usedSignature);
        res.forEach(m -> usedSignature.add(new MJMethodSignature(m)));
        if(superClass != null) {
            res.addAll(superClass.getAllMethods(usedSignature));
        }
        return res;
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public boolean isValid() {

        // Superclasses must not be the same class.
        if(superClass != null) {
            MJClass inheritedClass = superClass;
            while(inheritedClass != null) {
                if(inheritedClass.equals(this)){
                    return false;
                }
                inheritedClass = inheritedClass.getSuperClass();
            }
        }

        // Superclass should be valid (and recursively checks all inherited classes and interfaces).
        if(superClass != null && !superClass.isValid()) {
            return false;
        }

        // Interfaces should be valid.
        if(implementedInterfaces.stream().anyMatch(i -> !i.isValid())) {
            return false;
        }

        List<MJMethod> allMethods = getAllMethods();

        // Static methods must be implemented.
        if(allMethods.stream().filter(MJMethod::isStatic).anyMatch(m -> !m.isImplemented())) {
            return false;
        }

        // Static methods cannot have genericTypes.
        if(allMethods.stream().filter(MJMethod::isStatic).anyMatch(
                m -> m.getParameters().stream().anyMatch(MJGenericType.class::isInstance)
        )) {
            return false;
        }

        // When a class is not abstract, all methods must be implemented.
        if(!isAbstract && allMethods.stream().anyMatch(m -> !m.isImplemented())) {
            return false;
        }

        return true;
    }

    @Override
    public MJMethod getMethod(String name, MJType[] parameters) {
        MJMethod res = getMethod(name, parameters, methods, true, true);
        if(res == null && superClass != null) res = superClass.getMethod(name, parameters);
        return res;
    }

    public MJMethod getDynamicMethod(String name, MJType[] parameters) {
        MJMethod res = getMethod(name, parameters, methods, false, true);
        if(res == null && superClass != null) res = superClass.getDynamicMethod(name, parameters);
        return res;
    }

    @Override
    public boolean inheritsType(MJType type) {

        if(type instanceof MJGenericType) {
            return inheritsType(((MJGenericType) type).getExtendedType());
        }

        if(type instanceof MJClass || type instanceof MJInterface) {
            return this.equals(type) || (superClass != null && superClass.inheritsType(type))
                    || super.inheritsType(type);
        }

        return false;
    }

    @Override
    public boolean isInheritedBy(MJType type) {
        return type.inheritsType(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MJClass mjClass)) return false;
        if (!super.equals(o)) return false;

        if (isAbstract() != mjClass.isAbstract()) return false;
        return getSuperClass() != null ?
                getSuperClass().equals(mjClass.getSuperClass()) : mjClass.getSuperClass() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getSuperClass() != null ? getSuperClass().hashCode() : 0);
        result = 31 * result + (isAbstract() ? 1 : 0);
        return result;
    }
}

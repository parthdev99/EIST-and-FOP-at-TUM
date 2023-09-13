package de.tum.in.ase.fop;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

@SuppressWarnings({"unused", "FieldMayBeFinal"})

public abstract class MJType {

    protected String name;

    protected List<MJInterface> implementedInterfaces;

    protected List<MJMethod> methods;

    protected List<MJGenericType> genericTypes;

    public MJType(
            String name,
            List<MJInterface> implementedInterfaces,
            List<MJMethod> methods,
            List<MJGenericType> genericTypes
    ) {
        this.name = name;
        this.implementedInterfaces = implementedInterfaces;
        this.methods = methods;
        this.genericTypes = genericTypes;
    }

    public String getName() {
        return name;
    }

    public List<MJInterface> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    public List<MJGenericType> getGenericTypes() {
        return genericTypes;
    }

    public List<MJMethod> getMethods() {
        return methods;
    }

    protected List<MJMethod> getAllMethods() {
        return getAllMethods(new ArrayList<>());
    }

    protected List<MJMethod> getAllMethods(List<MJMethodSignature> usedSignature)  {
        List<MJMethod> res = new ArrayList<>();
        methods.forEach(method -> {
            MJMethodSignature signature = new MJMethodSignature(method);
            if(!usedSignature.contains(signature)) {
                res.add(method);
                usedSignature.add(signature);
            }
        });
        implementedInterfaces.forEach(implementedInterface ->
                res.addAll(implementedInterface.getAllMethods(usedSignature)));
        return res;
    }

    public MJMethod getMethod(String name, MJType[] parameters) {
        return getMethod(name, parameters, methods, true, true);
    }

    public Set<MJMethodSignature> getAllMethodSignatures() {
        Set<MJMethodSignature> res = new HashSet<>();
        methods.forEach(m -> res.add(new MJMethodSignature(m)));
        implementedInterfaces.forEach(i -> res.addAll(i.getAllMethodSignatures()));
        return res;
    }

    public boolean isValid() {
        return false;
    }

    @SuppressWarnings("SameParameterValue")
    protected MJMethod getMethod(
            String name,
            MJType[] parameters,
            List<MJMethod> methods,
            boolean shouldBeStatic,
            boolean shouldBeImplemented
    ) {

        if(methods == null) {
            return null;
        }

        for(MJMethod method: methods) {

            if(method.isStatic() != shouldBeStatic) {
                continue;
            }
            if(method.isImplemented() != shouldBeImplemented) {
                continue;
            }
            if(!method.getName().equals(name)) {
                continue;
            }
            MJType[] parametersArray = new MJType[method.getParameters().size()];
            method.getParameters().toArray(parametersArray);
            if(!checkParametersCondition(parameters, parametersArray)) {
                continue;
            }

            return method;
        }

        return null;
    }

    private boolean checkParametersCondition(MJType[] parameters, MJType[] requiredParameters) {

        if(parameters == null) {
            return requiredParameters == null || requiredParameters.length == 0;
        }

        if(parameters.length != requiredParameters.length) {
            return false;
        }

        for(int i = 0; i < parameters.length; i++) {
            if(!parameters[i].inheritsType(requiredParameters[i])) {
                return false;
            }
        }

        return true;
    }

    public boolean inheritsType(MJType type) {

        if(type instanceof MJGenericType) {
            return inheritsType(((MJGenericType) type).getExtendedType());
        }

        if(type instanceof MJInterface) {
            return this.equals(type) || implementedInterfaces.contains((MJInterface) type) ||
                    implementedInterfaces.stream().anyMatch(i -> i.inheritsType(type));
        }

        return false;
    }

    public boolean isInheritedBy(MJType type) {
        return type.inheritsType(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MJType mjType)) return false;

        if (getName() != null ? !getName().equals(mjType.getName()) : mjType.getName() != null) return false;
        if (getImplementedInterfaces() != null ?
                !getImplementedInterfaces().equals(mjType.getImplementedInterfaces()) :
                mjType.getImplementedInterfaces() != null)
            return false;
        if (getGenericTypes() != null ?
                !getGenericTypes().equals(mjType.getGenericTypes()) : mjType.getGenericTypes() != null)
            return false;
        return getMethods() != null ? getMethods().equals(mjType.getMethods()) : mjType.getMethods() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getImplementedInterfaces() != null ? getImplementedInterfaces().hashCode() : 0);
        result = 31 * result + (getGenericTypes() != null ? getGenericTypes().hashCode() : 0);
        result = 31 * result + (getMethods() != null ? getMethods().hashCode() : 0);
        return result;
    }
}
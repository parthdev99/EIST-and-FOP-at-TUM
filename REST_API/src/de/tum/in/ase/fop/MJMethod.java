package de.tum.in.ase.fop;

import java.util.List;

@SuppressWarnings({"unused", "FieldMayBeFinal"})

public class MJMethod {

    private String name;

    private boolean isStatic;

    private boolean isImplemented;

    private List<MJType> parameters;

    public String getName() {
        return name;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isImplemented() {
        return isImplemented;
    }

    public List<MJType> getParameters() {
        return parameters;
    }

    public MJMethod(String name, boolean isStatic, boolean isImplemented, List<MJType> parameters) {
        this.name = name;
        this.isStatic = isStatic;
        this.isImplemented = isImplemented;
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MJMethod mjMethod)) return false;
        if (!super.equals(o)) return false;

        if (isStatic() != mjMethod.isStatic()) return false;
        if (isImplemented() != mjMethod.isImplemented()) return false;
        if (getName() != null ? !getName().equals(mjMethod.getName()) : mjMethod.getName() != null) return false;
        return getParameters() != null ? getParameters().equals(mjMethod.getParameters()) : mjMethod.getParameters() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (isStatic() ? 1 : 0);
        result = 31 * result + (isImplemented() ? 1 : 0);
        result = 31 * result + (getParameters() != null ? getParameters().hashCode() : 0);
        return result;
    }
}

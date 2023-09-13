package de.tum.in.ase.fop;

import java.util.List;

@SuppressWarnings({"unused", "FieldMayBeFinal"})

public class MJMethodSignature {

    private String name;

    private List<MJType> parameters;

    public MJMethodSignature(String name, List<MJType> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public MJMethodSignature(MJMethod method) {
        this.name = method.getName();
        this.parameters = method.getParameters();
    }

    public String getName() {
        return name;
    }

    public List<MJType> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MJMethodSignature that)) return false;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getParameters() != null ? !getParameters().equals(that.getParameters()) : that.getParameters() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getParameters() != null ? getParameters().hashCode() : 0);
        return result;
    }
}

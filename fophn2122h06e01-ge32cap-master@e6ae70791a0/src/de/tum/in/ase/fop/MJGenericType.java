package de.tum.in.ase.fop;

@SuppressWarnings({"unused", "FieldMayBeFinal"})

public class MJGenericType extends MJType {

    private MJType extendedType;

    public MJType getExtendedType() {
        return extendedType;
    }

    public MJGenericType(String name, MJType extendedType) {
        super(name, null, null, null);
        this.extendedType = extendedType;
    }

    @Override
    public boolean isValid() {
        return extendedType.isValid();
    }

    @Override
    public MJMethod getMethod(String name, MJType[] parameters) {
        return getMethod(name, parameters, methods, true, true);
    }

    @Override
    public boolean inheritsType(MJType type) {
        return extendedType.inheritsType(type);
    }

    @Override
    public boolean isInheritedBy(MJType type) {
        return type.inheritsType(extendedType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MJGenericType that)) return false;
        if (!super.equals(o)) return false;

        return getExtendedType() != null ?
                getExtendedType().equals(that.getExtendedType()) : that.getExtendedType() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getExtendedType() != null ? getExtendedType().hashCode() : 0);
        return result;
    }
}

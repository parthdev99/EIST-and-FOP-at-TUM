package de.tum.in.ase.fop;

@SuppressWarnings({"unused", "FieldMayBeFinal"})

public class ConcreteType {

    private String typeName;

    private MJType concreteType;

    public ConcreteType(String typeName, MJType concreteType) {
        this.typeName = typeName;
        this.concreteType = concreteType;
    }

    public String getTypeName() {
        return typeName;
    }

    public MJType getConcreteType() {
        return concreteType;
    }
}

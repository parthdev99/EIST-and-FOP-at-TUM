package de.tum.in.ase.fop;

import java.util.List;

@SuppressWarnings({"unused", "FieldMayBeFinal"})

public class MJInterface extends MJType {

    public MJInterface(
            String name,
            List<MJInterface> implementedInterfaces,
            List<MJMethod> methods,
            List<MJGenericType> genericTypes
    ) {
        super(name, implementedInterfaces, methods, genericTypes);
    }

    @Override
    public MJMethod getMethod(String name, MJType[] parameters) {
        return null;
    }

    @Override
    public boolean isValid() {
        // There is no method or any method is not implemented
        return (methods == null || methods.stream().noneMatch(MJMethod::isImplemented))
                && implementedInterfaces.stream().allMatch(MJInterface::isValid);
    }

    @Override
    public boolean inheritsType(MJType type) {
        return super.inheritsType(type);
    }

    @Override
    public boolean isInheritedBy(MJType type) {
        return type.inheritsType(this);
    }
}

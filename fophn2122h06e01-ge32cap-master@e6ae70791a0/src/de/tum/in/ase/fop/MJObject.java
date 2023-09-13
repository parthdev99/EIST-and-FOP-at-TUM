package de.tum.in.ase.fop;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings({"unused", "FieldMayBeFinal"})

public class MJObject {

    private MJClass mjClass;

    private List<ConcreteType> concreteGenericTypes;

    public List<ConcreteType> getConcreteGenericTypes() {
        return concreteGenericTypes;
    }

    public MJClass getMjClass() {
        return mjClass;
    }

    public MJObject(MJClass mjClass, List<ConcreteType> concreteGenericTypes) {
        this.mjClass = mjClass;
        this.concreteGenericTypes = concreteGenericTypes;
    }

    public MJMethod getMethod(String name, MJType[] parameters) {
        return mjClass.getDynamicMethod(name, parameters);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean isValid() {
        // A corresponding class must be valid.
        if(!mjClass.isValid()) {
            return false;
        }

        // A corresponding class must not be abstract.
        if(mjClass.isAbstract()) {
            return false;
        }

        // All generic types in a corresponding class must be determined in concreteGenericTypes.
        if(!checkTypes(mjClass.getGenericTypes(), concreteGenericTypes)) {
            return false;
        }

        return true;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean checkTypes(List<MJGenericType> genericTypes, List<ConcreteType> concreteTypes) {

        List<ConcreteType> copiedConcreteTypes = new ArrayList<>(concreteTypes);

        ListIterator<ConcreteType> concreteTypeItr;

        for(MJGenericType genericType: genericTypes) {

            concreteTypeItr = copiedConcreteTypes.listIterator();

            boolean flag = false;

            while(concreteTypeItr.hasNext()) {

                ConcreteType concreteType = concreteTypeItr.next();

                if(!genericType.getName().equals(concreteType.getTypeName())) {
                    continue;
                }

                if(!(genericType.getExtendedType() == null ||
                        concreteType.getConcreteType().inheritsType((genericType.getExtendedType())))) {
                    continue;
                }

                flag = true;
                concreteTypeItr.remove();
                break;
            }

            if(!flag) {
                return false;
            }
        }

        return copiedConcreteTypes.size() == 0;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MJObject mjObject)) return false;

        if (getMjClass() != null ? !getMjClass().equals(mjObject.getMjClass()) : mjObject.getMjClass() != null)
            return false;
        return getConcreteGenericTypes() != null ?
                getConcreteGenericTypes().equals(mjObject.getConcreteGenericTypes()) :
                mjObject.getConcreteGenericTypes() == null;
    }

    @Override
    public int hashCode() {
        int result = getMjClass() != null ? getMjClass().hashCode() : 0;
        result = 31 * result + (getConcreteGenericTypes() != null ? getConcreteGenericTypes().hashCode() : 0);
        return result;
    }
}

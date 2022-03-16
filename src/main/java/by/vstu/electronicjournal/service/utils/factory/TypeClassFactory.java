package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.TypeClassDTO;
import by.vstu.electronicjournal.entity.TypeClass;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;

public final class TypeClassFactory implements AbstractFactoryForRelatedResources<TypeClass, TypeClassDTO> {

    @Override
    public TypeClass create(TypeClassDTO dto) {
        TypeClass typeClass = new TypeClass();
        typeClass.setName(dto.getName());
        typeClass.setIdFromSource(dto.getId());
        //typeClass.setStatus(Status.valueOf(dto.getStatus()));
        return typeClass;
    }
}

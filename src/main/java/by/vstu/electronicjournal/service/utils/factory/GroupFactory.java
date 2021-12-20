package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.GroupDTO;
import by.vstu.electronicjournal.entity.Group;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;

public final class GroupFactory implements AbstractFactoryForRelatedResources<Group, GroupDTO> {

    @Override
    public Group create(GroupDTO dto) {
        Group group = new Group();
        group.setName(dto.getName());
        group.setIdFromSource(dto.getId());
        return group;
    }
}

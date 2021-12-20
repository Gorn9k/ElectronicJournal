package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.GroupDTO;
import by.vstu.electronicjournal.entity.Group;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.GroupRepository;
import by.vstu.electronicjournal.service.GroupService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.factory.GroupFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class GroupServiceImpl
        extends CommonCRUDServiceImpl<Group, GroupDTO, GroupRepository>
        implements GroupService {

    @Value("${entrance.common-info}")
    private String path;

    @Autowired
    private Mapper<Group, GroupDTO> mapper;

    @Autowired
    private GroupRepository groupRepository;

    private ActuatorFromGeneralResources<GroupDTO> relatedResources;

    public GroupServiceImpl() {
        super(Group.class, GroupDTO.class);
    }

    @PostConstruct
    void settingUp() {
        relatedResources = new ActuatorFromGeneralResourcesImpl(GroupDTO.class, groupRepository, new GroupFactory(), mapper);
    }

    @Override
    public List<GroupDTO> search(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(groupRepository.findAll(getSpecifications(query)), GroupDTO.class);
    }

    @Override
    public List<GroupDTO> validator(String query) {
        String queryToCommonInfo = String.format("%s/groups/search?q=%s", path, query);
        return relatedResources.findAndAddThings(queryToCommonInfo);
    }
}

package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.SubGroupDTO;
import by.vstu.electronicjournal.entity.SubGroup;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.SubGroupRepository;
import by.vstu.electronicjournal.service.SubGroupService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubGroupServiceImpl extends CommonCRUDServiceImpl<SubGroup, SubGroupDTO, SubGroupRepository>
        implements SubGroupService {

    @Autowired
    private Mapper<SubGroup, SubGroupDTO> mapper;

    @Autowired
    private SubGroupRepository subGroupRepository;

    public SubGroupServiceImpl() {
        super(SubGroup.class, SubGroupDTO.class);
    }

    @Override
    public List<SubGroupDTO> search(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(subGroupRepository.findAll(getSpecifications(query)), SubGroupDTO.class);
    }
}

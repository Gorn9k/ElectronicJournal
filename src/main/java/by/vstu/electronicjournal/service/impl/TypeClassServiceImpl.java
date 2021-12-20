package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.TypeClassDTO;
import by.vstu.electronicjournal.entity.TypeClass;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.TypeClassRepository;
import by.vstu.electronicjournal.service.TypeClassService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.factory.TypeClassFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TypeClassServiceImpl
        extends CommonCRUDServiceImpl<TypeClass, TypeClassDTO, TypeClassRepository>
        implements TypeClassService {

    @Value("${entrance.common-info}")
    private String path;

    @Autowired
    private Mapper<TypeClass, TypeClassDTO> mapper;

    @Autowired
    private TypeClassRepository typeClassRepository;

    private ActuatorFromGeneralResources<TypeClassDTO> relatedResources;

    public TypeClassServiceImpl() {
        super(TypeClass.class, TypeClassDTO.class);
    }

    @PostConstruct
    void settingUp() {
        relatedResources = new ActuatorFromGeneralResourcesImpl(TypeClassDTO.class, typeClassRepository, new TypeClassFactory(), mapper);
    }

    @Override
    public List<TypeClassDTO> search(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(typeClassRepository.findAll(getSpecifications(query)), TypeClassDTO.class);
    }

    @Override
    public List<TypeClassDTO> validator(String query) {
        String queryToCommonInfo = String.format("%s/types-of-classes/search?q=%s", path, query);
        return relatedResources.findAndAddThings(queryToCommonInfo);
    }
}

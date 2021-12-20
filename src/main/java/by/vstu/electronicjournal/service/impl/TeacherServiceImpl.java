package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.TeacherDTO;
import by.vstu.electronicjournal.entity.Teacher;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.TeacherRepository;
import by.vstu.electronicjournal.service.TeacherService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.factory.TeacherFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TeacherServiceImpl
        extends CommonCRUDServiceImpl<Teacher, TeacherDTO, TeacherRepository>
        implements TeacherService {

    @Value("${entrance.common-info}")
    private String path;

    @Autowired
    private Mapper mapper;

    @Autowired
    private TeacherRepository teacherRepository;

    private ActuatorFromGeneralResources<TeacherDTO> relatedResources;

    public TeacherServiceImpl() {
        super(Teacher.class, TeacherDTO.class);
    }

    @PostConstruct
    void settingUp() {
        relatedResources = new ActuatorFromGeneralResourcesImpl(TeacherDTO.class, teacherRepository, new TeacherFactory(), mapper);
    }

    @Override
    public List<TeacherDTO> search(String query) {

        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(teacherRepository.findAll(getSpecifications(query)), TeacherDTO.class);
    }

    @Override
    public List<TeacherDTO> validator(String query) {
        String queryToCommonInfo = String.format("%s/employees/search?q=%s", path, query);
        return relatedResources.findAndAddThings(queryToCommonInfo);
    }
}

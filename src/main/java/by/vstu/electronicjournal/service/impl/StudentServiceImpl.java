package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.StudentDTO;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.Student;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.JournalSiteRepository;
import by.vstu.electronicjournal.repository.StudentRepository;
import by.vstu.electronicjournal.service.JournalSiteService;
import by.vstu.electronicjournal.service.StudentService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.factory.StudentFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl
        extends CommonCRUDServiceImpl<Student, StudentDTO, StudentRepository>
        implements StudentService {

    @Value("${entrance.common-info}")
    private String path;

    @Autowired
    private Mapper<Student, StudentDTO> mapper;

    @Autowired
    private StudentRepository studentRepository;

    private ActuatorFromGeneralResources<StudentDTO> relatedResources;

    @Autowired
    private JournalSiteService journalSiteService;

    public StudentServiceImpl() {
        super(Student.class, StudentDTO.class);
    }

    @PostConstruct
    void settingUp() {
        relatedResources = new ActuatorFromGeneralResourcesImpl(StudentDTO.class, studentRepository, new StudentFactory(), mapper);
    }

    @Override
    public List<StudentDTO> search(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(studentRepository.findAll(getSpecifications(query)), StudentDTO.class);
    }

    @Override
    public List<StudentDTO> getStudentsByGroup(String query) {
        List<JournalSiteDTO> journalSiteDTOs = journalSiteService.search(query);
        List<StudentDTO> students = new ArrayList<>();
        if (journalSiteDTOs.size()!=0) {
            journalSiteDTOs.get(0).getJournalHeaders().get(0).getJournalContents().stream().forEach(journalContentDTO ->
                    students.add(journalContentDTO.getStudent()));
        }
        return students;
    }


    @Override
    public List<StudentDTO> validator(String query) {
        String queryToCommonInfo = String.format("%s/students/search?q=%s", path, query);
        return relatedResources.findAndAddThings(queryToCommonInfo);
    }
}

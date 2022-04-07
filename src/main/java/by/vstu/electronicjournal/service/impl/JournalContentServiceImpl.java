package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.JournalContentDTO;
import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.StudentDTO;
import by.vstu.electronicjournal.dto.StudentPerformanceDTO;
import by.vstu.electronicjournal.entity.JournalContent;
import by.vstu.electronicjournal.entity.JournalHeader;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.Student;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.JournalContentRepository;
import by.vstu.electronicjournal.repository.JournalSiteRepository;
import by.vstu.electronicjournal.repository.StudentRepository;
import by.vstu.electronicjournal.service.JournalContentService;
import by.vstu.electronicjournal.service.StudentService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JournalContentServiceImpl
        extends CommonCRUDServiceImpl<JournalContent, JournalContentDTO, JournalContentRepository>
        implements JournalContentService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JournalContentRepository journalContentRepository;

    @Autowired
    private JournalSiteRepository journalSiteRepository;

    @Autowired
    private StudentService studentService;

    public JournalContentServiceImpl() {
        super(JournalContent.class, JournalContentDTO.class);
    }

    @Override
    public List<JournalContentDTO> search(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(journalContentRepository.findAll(getSpecifications(query)), JournalContentDTO.class);
    }

    @Override
    public void generate(List<JournalSite> params) {

        for (JournalSite journalSite : params) {
            for (JournalHeader journalHeader : journalSite.getJournalHeaders()) {
                generate(journalHeader);
            }
        }
    }

    @Override
    public void generate(JournalHeader header) {
        if (header.getJournalContents() == null || header.getJournalContents().size() == 0) {
            String query = String.format("group.name==%s;subGroupIdentificator=", header.getJournalSite().getGroup().getName());

            if (header.getSubGroup() == 0) {
                query += "in=(1,2)";
            } else {
                query += "=" + header.getSubGroup();
            }

            List<StudentDTO> studentDTOS = (List<StudentDTO>) studentService.validator(query);

            for (StudentDTO studentDTO : studentDTOS) {

                JournalContent journalContent = new JournalContent();
                Student student = (Student) mapper.toEntity(studentDTO, Student.class);
                // student.setGroup(header.getJournalSite().getGroup());
                journalContent.setStatus(Status.ACTIVE);
                journalContent.setJournalHeader(header);
                journalContent.setStudent(student);

                journalContentRepository.save(journalContent);
            }
        }
    }
}

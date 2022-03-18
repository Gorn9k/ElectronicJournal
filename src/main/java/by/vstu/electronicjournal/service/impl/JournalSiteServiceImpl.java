package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.dto.GroupDTO;
import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.TeacherDTO;
import by.vstu.electronicjournal.dto.requestBodyParams.PatternDTO;
import by.vstu.electronicjournal.entity.*;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.JournalSiteRepository;
import by.vstu.electronicjournal.service.DisciplineService;
import by.vstu.electronicjournal.service.GroupService;
import by.vstu.electronicjournal.service.JournalSiteService;
import by.vstu.electronicjournal.service.TeacherService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JournalSiteServiceImpl
        extends CommonCRUDServiceImpl<JournalSite, JournalSiteDTO, JournalSiteRepository>
        implements JournalSiteService {

    @Value("${entrance.timetable}")
    private String path;

    @Autowired
    private Mapper mapper;

    @Autowired
    private JournalSiteRepository journalSiteRepository;

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private GroupService groupService;

    public JournalSiteServiceImpl() {
        super(JournalSite.class, JournalSiteDTO.class);
    }

    @Override
    public List<JournalSiteDTO> search(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(journalSiteRepository.findAll(getSpecifications(query)), JournalSiteDTO.class);
    }

    @Override
    public List<JournalSite> generate() {

        RestTemplate restTemplate = new RestTemplate();

        String queryToCommonInfo = String.format("%s/patterns/", path);
        List<PatternDTO> patternDTOS =
                restTemplate.exchange(queryToCommonInfo, HttpMethod.GET, null, new ParameterizedTypeReference<List<PatternDTO>>() {
                }).getBody();

        List<JournalSite> result = new ArrayList<>();

        for (PatternDTO patternDTO : patternDTOS) {

            DisciplineDTO disciplineDTO = disciplineService.validator("name==\'" + patternDTO.getDisciplineName() + "\'").get(0);
            TeacherDTO teacherDTO = teacherService.validator(parsingFIOTeacher(patternDTO.getTeacherFio())).get(0);
            GroupDTO groupDTO = groupService.validator("name==\'" + patternDTO.getGroupName() + "\'").get(0);

            JournalSite journalSite = new JournalSite();
            journalSite.setDiscipline((Discipline) mapper.toEntity(disciplineDTO, Discipline.class));
            journalSite.setTeacher((Teacher) mapper.toEntity(teacherDTO, Teacher.class));
            journalSite.setGroup((Group) mapper.toEntity(groupDTO, Group.class));

            result.add(journalSite);
        }

        return journalSiteRepository.saveAll(result);
    }

    @Override
    public JournalSiteDTO getFilteredByTeacherAndGroupAndDisciplineTypeClassAndSubGroup(Long teacherId, String groupName, Long disciplineId, Long typeClassId, Integer subGroupNumber){
        JournalSite journalSite = journalSiteRepository.findByTeacherIdAndGroupNameAndDisciplineId(teacherId, groupName, disciplineId).get(0);
        journalSite.setJournalHeaders(journalSite.getJournalHeaders().stream().filter(journalHeader -> journalHeader.getTypeClass().getId().equals(typeClassId) &&
                journalHeader.getSubGroup().equals(subGroupNumber) && journalHeader.getDateOfLesson()!=null).collect(Collectors.toList()));
        return (JournalSiteDTO) mapper.toDTO(journalSite, JournalSiteDTO.class);
    }

    private String parsingFIOTeacher(String fio) {
        fio = fio.replace("\'", "").replace(".", " ");
        String[] temp = fio.split(" ");
        return String.format("surname==%s;name==%s*;patronymic==%s*", temp[0], temp[1], temp[2]);
    }
}

package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.*;
import by.vstu.electronicjournal.dto.requestBodyParams.ParamsForCreateJournalHeader;
import by.vstu.electronicjournal.dto.requestBodyParams.PatternDTO;
import by.vstu.electronicjournal.entity.JournalContent;
import by.vstu.electronicjournal.entity.JournalHeader;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.JournalContentRepository;
import by.vstu.electronicjournal.repository.JournalHeaderRepository;
import by.vstu.electronicjournal.repository.JournalSiteRepository;
import by.vstu.electronicjournal.service.JournalContentService;
import by.vstu.electronicjournal.service.JournalHeaderService;
import by.vstu.electronicjournal.service.JournalSiteService;
import by.vstu.electronicjournal.service.TypeClassService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JournalHeaderServiceImpl
        extends CommonCRUDServiceImpl<JournalHeader, JournalHeaderDTO, JournalHeaderRepository>
        implements JournalHeaderService {

    @Value("${entrance.timetable}")
    private String path;

    @Autowired
    private Mapper mapper;

    @Autowired
    private JournalSiteRepository journalSiteRepository;

    @Autowired
    private JournalHeaderRepository journalHeaderRepository;

    @Autowired
    private JournalContentRepository journalContentRepository;

    @Autowired
    private JournalContentService journalContentService;

    @Autowired
    private JournalSiteService journalSiteService;

    @Autowired
    private TypeClassService typeClassService;

    public JournalHeaderServiceImpl() {
        super(JournalHeader.class, JournalHeaderDTO.class);
    }

    @Override
    public List<JournalHeaderDTO> search(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(journalHeaderRepository.findAll(getSpecifications(query)),
                JournalHeaderDTO.class);
    }

    @Override
    public JournalHeaderDTO create(ParamsForCreateJournalHeader params) {

        JournalSite journalSite = journalSiteRepository.getById(params.getJournalSiteId());
        JournalHeader journalHeader = (JournalHeader) mapper
                .toEntity(params.getJournalHeaderDTO(), JournalHeader.class);
        journalHeader.setJournalSite(journalSite);
        journalHeader = journalHeaderRepository.save(journalHeader);

        journalContentService.generate(journalHeader);

        return (JournalHeaderDTO) mapper.toDTO(journalHeader, JournalHeaderDTO.class);
    }

    @Override
    public List<JournalSite> generate(List<JournalSite> params) {

        RestTemplate restTemplate = new RestTemplate();

        List<String> lessonDays = new ArrayList<>();
        List<Integer> lessonNumbers = new ArrayList<>();
        List<Integer> subGroups = new ArrayList<>();
        List<String> groupNames = new ArrayList<>();
        List<String> typeClasses = new ArrayList<>();
        List<String> teacherNames = new ArrayList<>();
        List<String> locations = new ArrayList<>();
        List<String> disciplineNames = new ArrayList<>();

        for (JournalSite journalSite : params) {

            String queryToCommonInfo = String.format(
                    "%s/patterns/search?q=groupName==%s;disciplineName==\'%s\';teacherFio==%s*",
                    path,
                    journalSite.getGroup().getName(),
                    journalSite.getDiscipline().getName(),
                    journalSite.getTeacher().getSurname()
            );
            List<PatternDTO> patternDTOS =
                    restTemplate.exchange(queryToCommonInfo, HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<PatternDTO>>() {
                            }).getBody();

            List<JournalHeaderDTO> headerDTOS = new ArrayList<>();

            boolean flag;

            for (PatternDTO patternDTO : patternDTOS) {
                flag = false;
                if (!headerDTOS.isEmpty()) {
                    continue;
                }
                if (lessonDays.isEmpty() && lessonNumbers.isEmpty() && subGroups.isEmpty() && groupNames.isEmpty() &&
                        typeClasses.isEmpty() && teacherNames.isEmpty() && locations.isEmpty() && disciplineNames.isEmpty()) {
                    lessonDays.add(patternDTO.getLessonDay());
                    lessonNumbers.add(patternDTO.getLessonNumber());
                    subGroups.add(patternDTO.getSubGroup());
                    groupNames.add(patternDTO.getGroupName());
                    typeClasses.add(patternDTO.getTypeClassName());
                    teacherNames.add(patternDTO.getTeacherFio());
                    locations.add(patternDTO.getLocation());
                    disciplineNames.add(patternDTO.getDisciplineName());
                    JournalHeaderDTO journalHeaderDTO = new JournalHeaderDTO();
                    journalHeaderDTO.setSubGroup(patternDTO.getSubGroup());
                    journalHeaderDTO.setHoursCount(patternDTO.getLessonNumber());
                    journalHeaderDTO.setStatus(Status.ACTIVE);
                    TypeClassDTO typeClassDTO = typeClassService
                            .validator("name==\'" + patternDTO.getTypeClassName() + "\'").get(0);
                    journalHeaderDTO.setTypeClass(typeClassDTO);

                    headerDTOS.add(journalHeaderDTO);
                } else {
                    for (int i = 0; i < lessonDays.size(); i++) {
                        if (lessonDays.get(i).equals(patternDTO.getLessonDay()) &&
                                lessonNumbers.get(i).equals(patternDTO.getLessonNumber()) &&
                                subGroups.get(i).equals(patternDTO.getSubGroup()) &&
                                groupNames.get(i).equals(patternDTO.getGroupName()) &&
                                typeClasses.get(i).equals(patternDTO.getTypeClassName()) &&
                                teacherNames.get(i).equals(patternDTO.getTeacherFio()) &&
                                locations.get(i).equals(patternDTO.getLocation()) && disciplineNames.get(i).equals(patternDTO.getDisciplineName())) {
                                flag = true;
                        }
                    }
                    if (!flag) {
                        lessonDays.add(patternDTO.getLessonDay());
                        lessonNumbers.add(patternDTO.getLessonNumber());
                        subGroups.add(patternDTO.getSubGroup());
                        groupNames.add(patternDTO.getGroupName());
                        typeClasses.add(patternDTO.getTypeClassName());
                        teacherNames.add(patternDTO.getTeacherFio());
                        locations.add(patternDTO.getLocation());
                        disciplineNames.add(patternDTO.getDisciplineName());
                        JournalHeaderDTO journalHeaderDTO = new JournalHeaderDTO();
                        journalHeaderDTO.setSubGroup(patternDTO.getSubGroup());
                        journalHeaderDTO.setHoursCount(patternDTO.getLessonNumber());
                        journalHeaderDTO.setStatus(Status.ACTIVE);
                        TypeClassDTO typeClassDTO = typeClassService
                                .validator("name==\'" + patternDTO.getTypeClassName() + "\'").get(0);
                        journalHeaderDTO.setTypeClass(typeClassDTO);

                        headerDTOS.add(journalHeaderDTO);
                    }
                }
            }

            List<JournalHeader> journalHeaders = mapper.toEntities(headerDTOS, JournalHeader.class);
            journalHeaders.stream()
                    .forEach(journalHeader -> journalHeader.setJournalSite(journalSite));
            journalSite.setJournalHeaders(journalHeaderRepository.saveAll(journalHeaders));
        }
        return params;
    }

    @Override
    public List<JournalContentDTO> editList(Long id, List<JournalContentDTO> dtos) {

        JournalHeader header = journalHeaderRepository.getById(id);
        List<JournalContent> contents = mapper.toEntities(dtos, JournalContent.class);

        for (JournalContent content : contents) {
            content.setJournalHeader(header);
        }
        return mapper
                .toDTOs(journalContentRepository.saveAllAndFlush(contents), JournalContentDTO.class);
    }

    @Override
    public AcademicPerformanceDTO getTotalNumberMissedClassesByStudentForPeriod(String query) {
        Long studentId = Long.parseLong(query.split("==")[1].split(";")[0]);
        LocalDate after = null, before = null;
        int year = 0, month = 0, dayOfMonth;
        try {
            year = Integer.parseInt(query.split("==")[2].split("and")[0].split("-")[0]);
            month = Integer.parseInt(query.split("==")[2].split("and")[0].split("-")[1]);
            dayOfMonth = Integer.parseInt(query.split("==")[2].split("and")[0].split("-")[2]);
            after = LocalDate.of(year,month,dayOfMonth);
            year = Integer.parseInt(query.split("==")[2].split("and")[1].split("-")[0]);
            month = Integer.parseInt(query.split("==")[2].split("and")[1].split("-")[1]);
            dayOfMonth = Integer.parseInt(query.split("==")[2].split("and")[1].split("-")[2]);
            before = LocalDate.of(year,month,dayOfMonth);
        } catch (Exception e) {
            System.out.println("Incorrect format date!");
        }
        query = String.format("dateOfLesson>=%s and dateOfLesson<=%s", after, before);
        List<JournalHeaderDTO> journalHeaderDTOs = search(query);
        List<JournalContentDTO> journalContentDTOs = new ArrayList<>();
        AcademicPerformanceDTO academicPerformanceDTO = new AcademicPerformanceDTO();
        StudentPerformanceDTO studentPerformanceDTO = new StudentPerformanceDTO();
        journalHeaderDTOs.forEach(journalHeaderDTO -> journalContentDTOs.addAll(journalHeaderDTO.getJournalContents()));
        List<JournalContentDTO> journalContentDTOList = journalContentDTOs.stream().filter(journalContentDTO -> journalContentDTO.getStudent().getId().equals(studentId)).collect(Collectors.toList());
        if (journalContentDTOList.size()!=0) {
            studentPerformanceDTO.setStudentDTO(journalContentDTOList.get(0).getStudent());
            academicPerformanceDTO.setTotalNumberPasses(journalContentDTOList.stream().filter(journalContentDTO ->
                    journalContentDTO.getPresence()!=null && journalContentDTO.getPresence().equals(false)).count());
        }
        academicPerformanceDTO.setStudentPerformanceDTO(studentPerformanceDTO);
        return academicPerformanceDTO;
    }
}

package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.*;
import by.vstu.electronicjournal.dto.requestBodyParams.PatternDTO;
import by.vstu.electronicjournal.entity.*;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.JournalSiteRepository;
import by.vstu.electronicjournal.service.DisciplineService;
import by.vstu.electronicjournal.service.GroupService;
import by.vstu.electronicjournal.service.JournalSiteService;
import by.vstu.electronicjournal.service.TeacherService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import liquibase.pro.packaged.S;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    public List<JournalSiteDTO> searchByTeacherAndDiscipline(String query){
        Map<String, JournalSiteDTO> map = new HashMap<>();
        search(query).forEach(journalSiteDTO -> map.put(journalSiteDTO.getGroup().getName(), journalSiteDTO));
        List<JournalSiteDTO> journalSiteDTOS = new ArrayList<>();
        journalSiteDTOS.addAll(map.values());
        return journalSiteDTOS;
    }

    @Override
    public List<AcademicPerformanceDTO> getGeneralAcademicPerformance(String query) {
        List<JournalSiteDTO> journalSites = search(query);
        List<Map<StudentDTO, List<JournalContentDTO>>> mapArrayList = new ArrayList<>();
        List<JournalHeaderDTO> journalHeaderDTOs = new ArrayList<>();
        journalSites.forEach(journalSiteDTO -> journalHeaderDTOs.addAll(journalSiteDTO.getJournalHeaders()));
        List<JournalContentDTO> journalContentDTOs = new ArrayList<>();
        journalHeaderDTOs.forEach(journalHeaderDTO -> journalContentDTOs.addAll(journalHeaderDTO.getJournalContents()));
        Set<StudentDTO> hashSet = journalContentDTOs.stream().map(JournalContentDTO::getStudent).collect(Collectors.toSet());
        hashSet.forEach(studentDTO -> {
            Map<StudentDTO, List<JournalContentDTO>> map1 = new HashMap<>();
            map1.put(studentDTO, journalContentDTOs.stream().filter(journalContentDTO ->
                    journalContentDTO.getStudent().equals(studentDTO)).collect(Collectors.toList()));
            mapArrayList.add(map1);
        });

        List<AcademicPerformanceDTO> academicPerformanceDTOList = new ArrayList<>();

        mapArrayList.forEach(studentDTOListMap -> studentDTOListMap.forEach((key, value) -> {
            AcademicPerformanceDTO academicPerformanceDTO = new AcademicPerformanceDTO();
            StudentPerformanceDTO studentPerformanceDTO = new StudentPerformanceDTO();
            studentPerformanceDTO.setStudentDTO(key);
            long count = value.stream().filter(journalContentDTO ->
                    journalContentDTO.getGrade()!=null).count();
            if (count != 0) {
                studentPerformanceDTO.setOverallGPA(value.stream().mapToInt(JournalContentDTO::getGrade).average().getAsDouble());
            }
            count = value.stream().filter(journalContentDTO ->
                    journalContentDTO.getPresence()!=null && journalContentDTO.getPresence().equals(false)).count();
            if (count != 0) {
                academicPerformanceDTO.setTotalNumberPasses(count);
            }
            count = value.stream().filter(journalContentDTO ->
                    journalContentDTO.getLateness() != null && journalContentDTO.getLateness() != 0).count();
            if (count != 0) {
                academicPerformanceDTO.setTotalNumberLates(count);
            }
            academicPerformanceDTO.setStudentPerformanceDTO(studentPerformanceDTO);
            academicPerformanceDTOList.add(academicPerformanceDTO);
        }));

        return academicPerformanceDTOList;
    }

    @Override
    public StudentPerformanceDTO getGeneralStudentProgressInDiscipline(String query) {
        List<JournalSiteDTO> journalSites = search(query.split(";")[0]);
        StudentPerformanceDTO studentPerformanceDTO = new StudentPerformanceDTO();
        if (!journalSites.isEmpty()) {
            Long studentId = Long.parseLong(query.split(";")[1].split("==")[1]);
            List<JournalHeaderDTO> journalHeaderDTOs = new ArrayList<>();
            journalSites.forEach(journalSiteDTO -> journalHeaderDTOs.addAll(journalSiteDTO.getJournalHeaders()));
            List<JournalContentDTO> journalContentDTOs = new ArrayList<>();
            journalHeaderDTOs.forEach(journalHeaderDTO -> journalContentDTOs.addAll(journalHeaderDTO.getJournalContents()));
            List<JournalContentDTO> journalContentDTOList = journalContentDTOs.stream().filter(journalContentDTO ->
                    journalContentDTO.getStudent().getId().equals(studentId)).collect(Collectors.toList());
            if (journalContentDTOList.size()!=0) {
                studentPerformanceDTO.setStudentDTO(journalContentDTOList.get(0).getStudent());
                long count = journalContentDTOList.stream().filter(journalContentDTO ->
                        journalContentDTO.getGrade() != null).count();
                if (count != 0) {
                    studentPerformanceDTO.setOverallGPA(journalContentDTOList.stream().mapToInt(JournalContentDTO::getGrade).average().getAsDouble());
                }
            }
        }
        return studentPerformanceDTO;
    }

    @Override
    public AcademicPerformanceDTO getNumberMissedClassesByStudent(String query) {
        List<JournalSiteDTO> journalSites = search(query.split(";")[0]);
        AcademicPerformanceDTO academicPerformanceDTO = new AcademicPerformanceDTO();
        StudentPerformanceDTO studentPerformanceDTO = new StudentPerformanceDTO();
        if (!journalSites.isEmpty()) {
            Long studentId = Long.parseLong(query.split(";")[1].split("==")[1]);
            List<JournalHeaderDTO> journalHeaderDTOs = new ArrayList<>();
            journalSites.forEach(journalSiteDTO -> journalHeaderDTOs.addAll(journalSiteDTO.getJournalHeaders()));
            List<JournalContentDTO> journalContentDTOs = new ArrayList<>();
            journalHeaderDTOs.forEach(journalHeaderDTO -> journalContentDTOs.addAll(journalHeaderDTO.getJournalContents()));
            List<JournalContentDTO> journalContentDTOList = journalContentDTOs.stream().filter(journalContentDTO ->
                    journalContentDTO.getStudent().getId().equals(studentId)).collect(Collectors.toList());
            if (journalContentDTOList.size()!=0) {
                studentPerformanceDTO.setStudentDTO(journalContentDTOList.get(0).getStudent());
                academicPerformanceDTO.setStudentPerformanceDTO(studentPerformanceDTO);
                long count = journalContentDTOList.stream().filter(journalContentDTO ->
                        journalContentDTO.getPresence()!=null && journalContentDTO.getPresence().equals(false)).count();
                if (count != 0) {
                    academicPerformanceDTO.setTotalNumberPasses(count);
                }
            }
        }
        return academicPerformanceDTO;
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
            journalSite.setStatus(Status.ACTIVE);
            result.add(journalSite);
        }

        return journalSiteRepository.saveAll(result);
    }

    @Override
    public JournalSiteDTO getFilteredByTeacherAndGroupAndDisciplineTypeClassAndSubGroup(Long teacherIdFromSource, String groupName, Long disciplineId, Long typeClassId, Integer subGroupNumber){
        JournalSite journalSite = journalSiteRepository.findByTeacherIdFromSourceAndGroupNameAndDisciplineId(teacherIdFromSource, groupName, disciplineId).get(0);
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

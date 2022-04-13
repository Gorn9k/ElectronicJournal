package by.vstu.electronicjournal.service.utils.impl;

import static java.time.LocalDate.now;

import by.vstu.electronicjournal.dto.JournalHeaderDTO;
import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.requestBodyParams.ContentDTO;
import by.vstu.electronicjournal.dto.requestBodyParams.ParamsForCreateJournalHeader;
import by.vstu.electronicjournal.service.JournalContentService;
import by.vstu.electronicjournal.service.JournalHeaderService;
import by.vstu.electronicjournal.service.JournalSiteService;
import by.vstu.electronicjournal.service.TypeClassService;
import by.vstu.electronicjournal.service.utils.UtilService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@EnableScheduling
public class UtilServiceImpl implements UtilService {

    @Value("${entrance.timetable}")
    private String path;

    @Autowired
    private JournalSiteService journalSiteService;

    @Autowired
    private JournalHeaderService journalHeaderService;

    @Autowired
    private JournalContentService journalContentService;

    @Autowired
    private TypeClassService typeClassService;


    @Override
    public void generate() {
        journalContentService.generate(journalHeaderService.generate(journalSiteService.generate()));
    }

    @Scheduled(cron = "0 0 7 * * *")
    @Override
    public void generateJournalHeadersEveryDay() {

        int year = 0, month = 0, dayOfMonth;
        LocalDate after, before;
        String query = "2022-04-11and2022-04-12";
        year = Integer.parseInt(query.split("-")[0]);
        month = Integer.parseInt(query.split("-")[1]);
        dayOfMonth = Integer.parseInt(query.split("-")[2].split("and")[0]);
        after = LocalDate.of(year, month, dayOfMonth);
        year = Integer.parseInt(query.split("-")[2].split("and")[1]);
        month = Integer.parseInt(query.split("-")[3]);
        dayOfMonth = Integer.parseInt(query.split("-")[4]);
        before = LocalDate.of(year, month, dayOfMonth).plusDays(1);

        List<ContentDTO> usedContentDTOS = new ArrayList<>();

        System.out.println(LocalTime.now());
        for (LocalDate date = after; date.isBefore(before); date = date.plusDays(1)) {
            for (ContentDTO dto : getContentFromTimetable(date)) {
            if (dto.getDisciplineName().equals("Физическая культура")) {
                List<JournalSiteDTO> siteDTOS = journalSiteService.search(
                        String.format(
                                "discipline.name==\'%s\';teacher.surname==%s;teacher.name==%s*;teacher.patronymic==%s*;group.name==\'%s\'",
                                dto.getDisciplineName(),
                                dto.getTeacherFio().split(" ")[0],
                                dto.getTeacherFio().split(" ")[1],
                                dto.getTeacherFio().split(" ")[2],
                                dto.getGroupName()
                        )
                );
                for (JournalSiteDTO journalSiteDTO : siteDTOS) {

                    boolean flag = false;

                    for (JournalHeaderDTO journalHeaderDTO : journalSiteDTO.getJournalHeaders()) {
                        try {
                            if (!journalHeaderDTO.getTypeClass().getName().equals(dto.getTypeClassName()) ||
                                    !journalHeaderDTO.getSubGroup().equals(dto.getSubGroup()) || !journalHeaderDTO.getHoursCount().equals(dto.getLessonNumber())
                                    || journalHeaderDTO.getDateOfLesson().isEqual(date)) {
                                flag = true;
                            }
                        } catch (NullPointerException e) {
                            continue;
                        }

                    }

                    if (flag) {
                        continue;
                    }

                    if (usedContentDTOS.isEmpty() || !usedContentDTOS.contains(dto)) {
                        usedContentDTOS.add(dto);
                        ParamsForCreateJournalHeader params = new ParamsForCreateJournalHeader();
                        JournalHeaderDTO journalHeaderDTO = new JournalHeaderDTO();
                        journalHeaderDTO.setHoursCount(dto.getLessonNumber());
                        journalHeaderDTO.setSubGroup(dto.getSubGroup());
                        journalHeaderDTO.setDateOfLesson(dto.getLessonDate());
                        journalHeaderDTO.setTypeClass(
                                typeClassService.validator("name==\'" + dto.getTypeClassName() + "\'").get(0));

                        params.setJournalSiteId(journalSiteDTO.getId());
                        params.setJournalHeaderDTO(journalHeaderDTO);

                        journalHeaderService.create(params);
                    }
                }
            }
        }
        }
    }

    private LinkedList<ContentDTO> getContentFromTimetable(LocalDate date) {
        RestTemplate restTemplate = new RestTemplate();

        String query = String.format("%s/content/search?q=lessonDate==%s",
                path,
                date
        );
        LinkedList<ContentDTO> contentDTOS =
                restTemplate.exchange(query, HttpMethod.GET, null,
                        new ParameterizedTypeReference<LinkedList<ContentDTO>>() {
                        }).getBody();

        for (ContentDTO dto : contentDTOS) {
            if (dto.getChanges() != null) {
                if (!dto.getChanges().getPostponed().isEqual(now())) {
                    contentDTOS.remove(dto);
                    continue;
                }
                if (dto.getChanges().getCanceled() != null && dto.getChanges().getPostponed()
                        .isEqual(null)) {
                    contentDTOS.remove(dto);
                    continue;
                }
            }
        }

        query = String.format("%s/content/search?q=changes.postponed==%s",
                path,
                date
        );

        LinkedList<ContentDTO> postponedContentDTO =
                restTemplate.exchange(query, HttpMethod.GET, null,
                        new ParameterizedTypeReference<LinkedList<ContentDTO>>() {
                        }).getBody();

        contentDTOS.addAll(postponedContentDTO);

        return contentDTOS;
    }
}


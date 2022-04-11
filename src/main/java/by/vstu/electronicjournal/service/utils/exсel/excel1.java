package by.vstu.electronicjournal.service.utils.exсel;

import by.vstu.electronicjournal.dto.JournalContentDTO;
import by.vstu.electronicjournal.dto.JournalHeaderDTO;
import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.entity.JournalContent;
import by.vstu.electronicjournal.entity.JournalHeader;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.repository.JournalContentRepository;
import by.vstu.electronicjournal.repository.JournalHeaderRepository;
import by.vstu.electronicjournal.repository.JournalSiteRepository;
import by.vstu.electronicjournal.service.JournalContentService;
import by.vstu.electronicjournal.service.JournalHeaderService;
import by.vstu.electronicjournal.service.JournalSiteService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class excel1 {

    private String groupName;
    private String cathedra;
    private List<JournalSiteDTO> journalSites;
    private List<JournalHeaderDTO> journalHeaders;
    private List<JournalContentDTO> journalContents;
    private Map<LocalDate, List<JournalSiteDTO>> map;
    private List<LocalDate> dates;

    @Autowired
    private JournalSiteService journalSiteService;

    @Autowired
    private JournalHeaderService journalHeaderService;

    @Autowired
    private JournalContentService journalContentService;

    public excel1 getInfo(String query) {
        journalSites = journalSiteService.search("group.name==А-33");
        int year = 0, month = 0, dayOfMonth;
        LocalDate after, before;
        year = Integer.parseInt(query.split("==")[2].split("and")[0].split("-")[0]);
        month = Integer.parseInt(query.split("==")[2].split("and")[0].split("-")[1]);
        dayOfMonth = Integer.parseInt(query.split("==")[2].split("and")[0].split("-")[2]);
        after = LocalDate.of(year, month, dayOfMonth).minusDays(1);
        year = Integer.parseInt(query.split("==")[2].split("and")[1].split("-")[0]);
        month = Integer.parseInt(query.split("==")[2].split("and")[1].split("-")[1]);
        dayOfMonth = Integer.parseInt(query.split("==")[2].split("and")[1].split("-")[2]);
        before = LocalDate.of(year, month, dayOfMonth).plusDays(1);

        journalSites.stream().forEach(journalSiteDTO -> journalSiteDTO.setJournalHeaders(journalSiteDTO.getJournalHeaders().stream().
                filter(journalHeaderDTO -> journalHeaderDTO.getDateOfLesson() != null && journalHeaderDTO.getDateOfLesson().isAfter(after) &&
                        journalHeaderDTO.getDateOfLesson().isBefore(before)).collect(Collectors.toList())));
        excel1 excel1 = new excel1();
        List<JournalSiteDTO> journalSiteDTOList = new ArrayList<>(journalSites);
        List<JournalSiteDTO> journalSiteDTOList1;
        map = new HashMap<>();
        dates = new ArrayList<>();
        for (LocalDate date = after.plusDays(1); date.isBefore(before); date = date.plusDays(1)) {
            journalSiteDTOList1 = new ArrayList<>();
            LocalDate finalDate = date;
            List<JournalSiteDTO> finalJournalSiteDTOList = journalSiteDTOList1;
            journalSiteDTOList.stream().forEach(journalSiteDTO -> {
                JournalSiteDTO journalSiteDTO1 = new JournalSiteDTO();
                journalSiteDTO1.setDiscipline(journalSiteDTO.getDiscipline());
                journalSiteDTO1.setTeacher(journalSiteDTO.getTeacher());
                journalHeaders = new ArrayList<>(journalSiteDTO.getJournalHeaders());
                journalHeaders = journalHeaders.stream().filter(journalHeaderDTO -> journalHeaderDTO.getDateOfLesson() != null &&
                    journalHeaderDTO.getDateOfLesson().equals(finalDate)).collect(Collectors.toList());
                journalSiteDTO1.setJournalHeaders(journalHeaders);
                if (journalSiteDTO1.getJournalHeaders().size() != 0) {
                        finalJournalSiteDTOList.add(journalSiteDTO1);
                }
            });
            dates.add(date);
            map.put(date, journalSiteDTOList1);
        }
        map.entrySet().forEach(localDateListEntry -> {
            System.out.println(localDateListEntry.getKey() + "->");
            localDateListEntry.getValue().forEach(journalSiteDTO -> System.out.println(journalSiteDTO.getTeacher().getSurname() + " " + journalSiteDTO.getJournalHeaders().size()));
        });


        excel1.setJournalSites(journalSites);
        excel1.setMap(map);
        excel1.setDates(dates);
        return excel1;
    }
}

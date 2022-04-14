package by.vstu.electronicjournal.service.utils.exсel;

import by.vstu.electronicjournal.ElectronicJournalApplication;
import by.vstu.electronicjournal.dto.*;
import by.vstu.electronicjournal.service.DisciplineService;
import by.vstu.electronicjournal.service.JournalSiteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.pro.packaged.S;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class excel2 {

    private static Map<LocalDate, List<JournalSiteDTO>> map;
    private static LinkedList<LocalDate> dates;
    private static List<JournalHeaderDTO> journalHeaders;

    public static void setSecondInfo(ConfigurableApplicationContext cat, String cathedraName) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String query = String.format("%s/common-info/disciplines/search?q=department.shortName==%s", "http://192.168.11.252:8082", cathedraName);
        List<DisciplineDTO> disciplineDTOS = new ArrayList<>();
        restTemplate.exchange(query, HttpMethod.GET, null, new ParameterizedTypeReference<List<DisciplineDTO>>() {
        }).getBody().stream().forEach(entry -> {
            disciplineDTOS.add(objectMapper.convertValue(entry, DisciplineDTO.class));
        });
        JournalSiteService journalSiteService = cat.getBean(JournalSiteService.class);
        List<JournalSiteDTO> journalSiteDTOs = new ArrayList<>();
        disciplineDTOS.forEach(disciplineDTO ->
            journalSiteDTOs.addAll(journalSiteService.getByDisciplineName(disciplineDTO.getName())));

        int year = 0, month = 0, dayOfMonth;
        LocalDate after, before;
        query = "2022-03-21and2022-04-13";
        year = Integer.parseInt(query.split("and")[0].split("-")[0]);
        month = Integer.parseInt(query.split("and")[0].split("-")[1]);
        dayOfMonth = Integer.parseInt(query.split("and")[0].split("-")[2]);
        after = LocalDate.of(year, month, dayOfMonth).minusDays(1);
        year = Integer.parseInt(query.split("and")[1].split("-")[0]);
        month = Integer.parseInt(query.split("and")[1].split("-")[1]);
        dayOfMonth = Integer.parseInt(query.split("and")[1].split("-")[2]);
        before = LocalDate.of(year, month, dayOfMonth).plusDays(1);

        journalSiteDTOs.stream().forEach(journalSiteDTO -> journalSiteDTO.setJournalHeaders(journalSiteDTO.getJournalHeaders().stream().
                filter(journalHeaderDTO -> journalHeaderDTO.getDateOfLesson() != null && journalHeaderDTO.getDateOfLesson().isAfter(after) &&
                        journalHeaderDTO.getDateOfLesson().isBefore(before)).collect(Collectors.toList())));

        List<JournalSiteDTO> journalSiteDTOList = new ArrayList<>(journalSiteDTOs);
        List<JournalSiteDTO> journalSiteDTOList1;
        map = new HashMap<>();
        dates = new LinkedList<>();

        for (LocalDate date = after.plusDays(1); date.isBefore(before); date = date.plusDays(1)) {
            journalSiteDTOList1 = new ArrayList<>();
            LocalDate finalDate = date;
            List<JournalSiteDTO> finalJournalSiteDTOList = journalSiteDTOList1;
            journalSiteDTOList.forEach(journalSiteDTO -> {
                JournalSiteDTO journalSiteDTO1 = new JournalSiteDTO();
                journalSiteDTO1.setDiscipline(journalSiteDTO.getDiscipline());
                journalSiteDTO1.setTeacher(journalSiteDTO.getTeacher());
                journalSiteDTO1.setGroup(journalSiteDTO.getGroup());
                journalHeaders = new ArrayList<>(journalSiteDTO.getJournalHeaders());
                journalHeaders = journalHeaders.stream().filter(journalHeaderDTO -> journalHeaderDTO.getDateOfLesson() != null &&
                        journalHeaderDTO.getDateOfLesson().equals(finalDate)).collect(Collectors.toList());
                journalSiteDTO1.setJournalHeaders(journalHeaders);
                if (journalSiteDTO1.getJournalHeaders().size() != 0) {
                    journalHeaders.stream().forEach(journalHeaderDTO -> journalHeaderDTO.setJournalContents(journalHeaderDTO.getJournalContents().stream().
                            filter(journalContentDTO -> journalContentDTO.getPresence()==null || journalContentDTO.getPresence().equals(false)).collect(Collectors.toList())));
                    journalHeaders = journalHeaders.stream().filter(journalHeaderDTO -> journalHeaderDTO.getJournalContents().size() > 0).collect(Collectors.toList());
                    if (journalSiteDTO1.getJournalHeaders().size() != 0) {
                        finalJournalSiteDTOList.add(journalSiteDTO1);
                    }
                }
            });
            if (journalSiteDTOList1.size() > 0) {
                dates.add(date);
                map.put(date, journalSiteDTOList1);
            }
        }
    }

    public static Workbook getSecondExcel(ConfigurableApplicationContext cat, String cathedraName) throws IOException {
        setSecondInfo(cat, "ИСАП");

        FileInputStream fileInputStream = new FileInputStream("C:/Users/User/Desktop/secondOt4et.xlsx");
        Workbook wb = new XSSFWorkbook(fileInputStream);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                if (i==0 && j==1) {
                    wb.getSheetAt(0).getRow(i).createCell(j).setCellValue("ИСАП");
                } else if (i==1 && j==1) {
                    wb.getSheetAt(0).getRow(i).createCell(j).setCellValue("ФИТР");
                } else if (i==4 && j==0) {
                    configure(wb, i);
                }
            }
        }

        fileInputStream.close();
        return wb;
    }

    private static void configure(Workbook workbook, int i) {
        if (!dates.isEmpty()) {
            int indexOfPass = i;
            LocalDate date = dates.remove();
            for (JournalSiteDTO journalSiteDTO : map.get(date)) {
                int countOfSudents = journalSiteDTO.getJournalHeaders().get(0).getJournalContents().size();
                Row row = workbook.getSheetAt(0).createRow(i);
                workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(
                        i, //first row (0-based)
                        i + countOfSudents - 1, //last row  (0-based)
                        0, //first column (0-based)
                        0  //last column  (0-based)
                ));
                workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(
                        i, //first row (0-based)
                        i + countOfSudents - 1, //last row  (0-based)
                        1, //first column (0-based)
                        1  //last column  (0-based)
                ));
                workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(
                        i, //first row (0-based)
                        i + countOfSudents - 1, //last row  (0-based)
                        2, //first column (0-based)
                        2  //last column  (0-based)
                ));
                workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(
                        i, //first row (0-based)
                        i + countOfSudents - 1, //last row  (0-based)
                        3, //first column (0-based)
                        3  //last column  (0-based)
                ));
                workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(
                        i, //first row (0-based)
                        i + countOfSudents - 1, //last row  (0-based)
                        6, //first column (0-based)
                        6  //last column  (0-based)
                ));
                workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(
                        i, //first row (0-based)
                        i + countOfSudents - 1, //last row  (0-based)
                        8, //first column (0-based)
                        8  //last column  (0-based)
                ));
                if (indexOfPass==i) {
                    CellStyle cellStyle = workbook.createCellStyle();
                    CreationHelper createHelper = workbook.getCreationHelper();
                    cellStyle.setDataFormat(
                            createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
                    Cell cell = row.createCell(6);
                    cell.setCellValue(date);
                    cell.setCellStyle(cellStyle);
                }
                DisciplineDTO disciplineDTO = journalSiteDTO.getDiscipline();
                TeacherDTO teacher = journalSiteDTO.getTeacher();
                List<String> strings = Arrays.stream(disciplineDTO.getName().split(" ")).map(s -> {
                    s = s.toUpperCase();
                    s = Character.toString(s.charAt(0));
                    return s;
                }).collect(Collectors.toList());
                StringBuilder disciplineName = new StringBuilder();
                strings.stream().forEach(disciplineName::append);
                row.createCell(0).setCellValue(new String(disciplineName));
                row.createCell(1).setCellValue(journalSiteDTO.getJournalHeaders().get(0).getTypeClass().getName());
                row.createCell(8).setCellValue(teacher.getSurname() + " " + teacher.getName().charAt(0) + "." +
                        (teacher.getPatronymic() == null ? "" : teacher.getPatronymic().charAt(0) + "."));
                List<JournalContentDTO> journalContentDTOList = journalSiteDTO.getJournalHeaders().get(0).getJournalContents();
                row.createCell(3).setCellValue(journalContentDTOList.size());
                for (int j = 0; j < journalContentDTOList.size(); j++, i++) {
                    if (j!=0) {
                        row = workbook.getSheetAt(0).createRow(i);
                    }
                    JournalContentDTO journalContent = journalContentDTOList.get(j);
                    String studentName = journalContent.getStudent().getSurname() + " " + journalContent.getStudent().getName().toUpperCase().charAt(0) +
                            "." + (journalContent.getStudent().getPatronymic() == null ? "" : journalContent.getStudent().getPatronymic().toUpperCase().charAt(0) + ".");
                    row.createCell(4).setCellValue(studentName + ", " + journalSiteDTO.getGroup().getName());
                    row.createCell(5).setCellValue(2);
                }
            }
            configure(workbook, i);
        }
    }
}

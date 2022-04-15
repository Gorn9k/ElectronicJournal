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
import org.apache.poi.ss.util.PropertyTemplate;
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
        //RestTemplate restTemplate = new RestTemplate();
        //ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String query = String.format("%s/common-info/disciplines/search?q=department.shortName==%s", "http://192.168.11.252:8082", cathedraName);
        //List<DisciplineDTO> disciplineDTOS = new ArrayList<>();
       //restTemplate.exchange(query, HttpMethod.GET, null, new ParameterizedTypeReference<List<DisciplineDTO>>() {
        //}).getBody().stream().forEach(entry -> {
         //   disciplineDTOS.add(objectMapper.convertValue(entry, DisciplineDTO.class));
        //});
        JournalSiteService journalSiteService = cat.getBean(JournalSiteService.class);
        List<JournalSiteDTO> journalSiteDTOs = new ArrayList<>();
        journalSiteDTOs.addAll(journalSiteService.search("group.name=in=(Тм-34, Тээ-5, Тт-5, Тм-33, Тээ-4, Тт-4, Тм-32, Тээ-3, Тт-3, А-35, Ит-11, Км-5, А-34, Ит-10, Км-4, А-33, Ит-8, Ит-9, Км-3, Ит-6, Ит-7)"));
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
                        journalHeaderDTO.getDateOfLesson().equals(finalDate) && !journalHeaderDTO.getTypeClass().getName().equals("Лекция")).collect(Collectors.toList());
                if (journalHeaders.size() != 0) {
                    journalHeaders.stream().forEach(journalHeaderDTO -> journalHeaderDTO.setJournalContents(journalHeaderDTO.getJournalContents().stream().
                            filter(journalContentDTO -> journalContentDTO.getPresence()==null || journalContentDTO.getPresence().equals(false)).collect(Collectors.toList())));
                    journalHeaders = journalHeaders.stream().filter(journalHeaderDTO -> journalHeaderDTO.getJournalContents().size() > 0).collect(Collectors.toList());
                    if (journalHeaders.size() != 0) {
                        journalSiteDTO1.setJournalHeaders(journalHeaders);
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

        FileInputStream fileInputStream = new FileInputStream("C:/GornakA/excel/secondOt4et.xlsx");
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

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        CellStyle cellStyle1 = wb.createCellStyle();
        CreationHelper createHelper = wb.getCreationHelper();
        cellStyle1.setDataFormat(
                createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle1.setWrapText(true);

        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.LEFT);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setWrapText(true);

        for (Row row : wb.getSheetAt(0)){
            for (Cell cell : row){
                if (cell.getColumnIndex() == 6) {
                    cell.setCellStyle(cellStyle1);
                } else if (cell.getColumnIndex() == 4) {
                    cell.setCellStyle(cellStyle2);
                } else {
                    cell.setCellStyle(cellStyle);
                }
            }
        }
        PropertyTemplate propertyTemplate=new PropertyTemplate();
        propertyTemplate.drawBorders(new CellRangeAddress(0,wb.getSheetAt(0).getLastRowNum(),0,8),
                BorderStyle.THIN,BorderExtent.ALL);
        propertyTemplate.applyBorders(wb.getSheetAt(0));

        fileInputStream.close();
        return wb;
    }

    private static void configure(Workbook workbook, int i) {
        if (!dates.isEmpty()) {
            int indexOfPass = i;
            LocalDate date = dates.remove();
            int countOfSudentsForDate = 0;
            for (JournalSiteDTO journalSiteDTO : map.get(date)) {
                int countOfSudents = journalSiteDTO.getJournalHeaders().get(0).getJournalContents().size();
                countOfSudentsForDate = countOfSudentsForDate + countOfSudents;
                Row row = workbook.getSheetAt(0).createRow(i);
                if (i + countOfSudents - 1 > i) {
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
                            8, //first column (0-based)
                            8  //last column  (0-based)
                    ));
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
            Cell cell = workbook.getSheetAt(0).getRow(indexOfPass).createCell(6);
            cell.setCellValue(date);
            workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(
                    indexOfPass, //first row (0-based)
                    indexOfPass + countOfSudentsForDate - 1, //last row  (0-based)
                    6, //first column (0-based)
                    6  //last column  (0-based)
            ));
            configure(workbook, i);
        }
    }
}
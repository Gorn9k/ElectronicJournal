package by.vstu.electronicjournal.service.utils.exсel;

import by.vstu.electronicjournal.dto.*;
import by.vstu.electronicjournal.service.JournalSiteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class ExcelServiceImpl implements ExcelService {

    private Map<LocalDate, List<JournalSiteDTO>> map;
    private LinkedList<LocalDate> dates;
    private List<JournalHeaderDTO> journalHeaders;
    private List<JournalSiteDTO> journalSiteDTOs;
    @Value("${EXCEL_FILE_PATH}")
    private String filePath;
    @Autowired
    private JournalSiteService journalSiteService;

    @Override
    public Workbook getPerformanceReport(String facultyName) {
        setParams(facultyName);
        Workbook wb = null;
        String path = filePath + "ot4et_po_platnim_otrabotkam_za_mes9c.xlsx";
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            wb = new XSSFWorkbook(fileInputStream);

            wb.getSheetAt(0).getRow(0).createCell(1).setCellValue("ИСАП");
            wb.getSheetAt(0).getRow(1).createCell(1).setCellValue(facultyName);
            generateReport(wb, 4);

            CellStyle cellStyleForEveryoneCells = wb.createCellStyle();
            cellStyleForEveryoneCells.setAlignment(HorizontalAlignment.CENTER);
            cellStyleForEveryoneCells.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleForEveryoneCells.setWrapText(true);

            CellStyle cellStyleForDate = wb.createCellStyle();
            CreationHelper createHelper = wb.getCreationHelper();
            cellStyleForDate.setDataFormat(
                    createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
            cellStyleForDate.setAlignment(HorizontalAlignment.CENTER);
            cellStyleForDate.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleForDate.setWrapText(true);

            CellStyle cellStyleForStudents = wb.createCellStyle();
            cellStyleForStudents.setAlignment(HorizontalAlignment.LEFT);
            cellStyleForStudents.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleForStudents.setWrapText(true);

            for (Row row : wb.getSheetAt(0)) {
                for (Cell cell : row) {
                    if (cell.getColumnIndex() == 6) {
                        cell.setCellStyle(cellStyleForDate);
                    } else if (cell.getColumnIndex() == 4) {
                        cell.setCellStyle(cellStyleForStudents);
                    } else {
                        cell.setCellStyle(cellStyleForEveryoneCells);
                    }
                }
            }

            PropertyTemplate propertyTemplate = new PropertyTemplate();
            propertyTemplate.drawBorders(new CellRangeAddress(0, wb.getSheetAt(0).getLastRowNum(), 0, 8),
                    BorderStyle.THIN, BorderExtent.ALL);
            propertyTemplate.applyBorders(wb.getSheetAt(0));
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File " + path + " not found!");
        } catch (IOException ioException) {
            System.out.println("Incorrect import excel file!");
        }

        return wb;
    }

    private void setParams(String facultyName) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String query = String.format("%s/common-info/groups/search?q=specialty.faculty.shortName==%s", "http://192.168.11.252:8082", facultyName);
        List<GroupDTO> groupDTOS = new ArrayList<>();
        restTemplate.exchange(query, HttpMethod.GET, null, new ParameterizedTypeReference<List<GroupDTO>>() {
        }).getBody().stream().forEach(entry -> {
            groupDTOS.add(objectMapper.convertValue(entry, GroupDTO.class));
        });
        journalSiteDTOs = new ArrayList<>();
        groupDTOS.forEach(groupDTO -> journalSiteDTOs.addAll(journalSiteService.search(String.format("group.name==%s", groupDTO.getName()))));

        query = "2022-03-21and2022-04-18";
        int year = 0, month = 0, dayOfMonth;
        LocalDate after, before;
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
                            filter(journalContentDTO -> journalContentDTO.getPresence() != null && journalContentDTO.getPresence().equals(false)).collect(Collectors.toList())));
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

    private void generateReport(Workbook workbook, int i) {
        Sheet sheet = workbook.getSheetAt(0);
        if (!dates.isEmpty()) {
            int indexOfPass = i;
            LocalDate date = dates.remove();
            int countOfStudentsForDate = 0;
            for (JournalSiteDTO journalSiteDTO : map.get(date)) {
                int countOfSudents = journalSiteDTO.getJournalHeaders().get(0).getJournalContents().size();
                countOfStudentsForDate = countOfStudentsForDate + countOfSudents;
                Row row = workbook.getSheetAt(0).createRow(i);
                if (i + countOfSudents - 1 > i) {
                    addMergedRegion(sheet, i, i + countOfSudents - 1, 0, 0);
                    addMergedRegion(sheet, i, i + countOfSudents - 1, 1, 1);
                    addMergedRegion(sheet, i, i + countOfSudents - 1, 2, 2);
                    addMergedRegion(sheet, i, i + countOfSudents - 1, 3, 3);
                    addMergedRegion(sheet, i, i + countOfSudents - 1, 8, 8);
                }
                DisciplineDTO disciplineDTO = journalSiteDTO.getDiscipline();
                TeacherDTO teacher = journalSiteDTO.getTeacher();
                List<String> strings = Arrays.stream(disciplineDTO.getName().split(" ")).map(s -> {
                    if (disciplineDTO.getName().split(" ").length != 1) {
                        s = s.toUpperCase();
                        s = Character.toString(s.charAt(0));
                    }
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
                    if (j != 0) {
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
            addMergedRegion(sheet, indexOfPass, indexOfPass + countOfStudentsForDate - 1, 6, 6);
            generateReport(workbook, i);
        }
    }

    private void addMergedRegion(Sheet sheet, int firstRow, int lastRow, int firstColumn, int lastColumn) {
        sheet.addMergedRegion(new CellRangeAddress(
                firstRow, //first row (0-based)
                lastRow, //last row  (0-based)
                firstColumn, //first column (0-based)
                lastColumn  //last column  (0-based)
        ));
    }

    @Override
    public Workbook getPassReport(String groupName) {
        Workbook wb = null;
        String path = filePath + "ot4et_po_propyskam_za_period.xlsx";
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            wb = new XSSFWorkbook(fileInputStream);
            setParamsForPassReport(groupName);
            toFormPassReport(wb);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File " + path + " not found!");
        } catch (IOException ioException) {
            System.out.println("Incorrect import excel file!");
        }

        return wb;
    }

    private void setParamsForPassReport(String groupName) {
        journalSiteDTOs = journalSiteService.search(String.format("group.name==%s", groupName));

        String query = "2022-03-21and2022-04-18";
        int year = 0, month = 0, dayOfMonth;
        LocalDate after, before;
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
            journalSiteDTOList.stream().forEach(journalSiteDTO -> {
                JournalSiteDTO journalSiteDTO1 = new JournalSiteDTO();
                journalSiteDTO1.setDiscipline(journalSiteDTO.getDiscipline());
                journalSiteDTO1.setTeacher(journalSiteDTO.getTeacher());
                journalSiteDTO1.setGroup(journalSiteDTO.getGroup());
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
    }

    private void toFormPassReport(Workbook workbook) {
        int indexForDate = 0;
        int indexForDateTemp = 0;
        int indexForDateDiscp = 0;
        int indexForDateDate = 0;
        int indexForDateStudent = 0;
        int rte = 0;
        int inx = 0;
        int forde = 0;
        int ij = 0;
        int nomer = 0;
        int newpage = 0;
        JournalHeaderDTO journalHeaderDTO = new JournalHeaderDTO();
        List<JournalHeaderDTO> journalHeaderDTOList = new ArrayList<>();
        map.values().stream().forEach(journalSiteDTOS -> journalSiteDTOS.stream().forEach(journalSiteDTO ->
                journalHeaderDTOList.addAll(journalSiteDTO.getJournalHeaders())));

        int maxSize = journalHeaderDTOList.stream().mapToInt(journalHeaderDTO1 ->
                journalHeaderDTO1.getJournalContents().size()).max().getAsInt();
        journalHeaderDTO = journalHeaderDTOList.stream().filter(journalHeaderDTO1 -> journalHeaderDTO1.getJournalContents().size()==maxSize).collect(Collectors.toList()).get(0);

        Set<String> setForStudents = new TreeSet<>(journalHeaderDTO.getJournalContents().stream().map(journalContentDTO ->
                journalContentDTO.getStudent().getSurname() + " " + journalContentDTO.getStudent().getName().toUpperCase().charAt(0) + "." +
                        (journalContentDTO.getStudent().getPatronymic() == null ? "" : journalContentDTO.getStudent().getPatronymic().toUpperCase().charAt(0) + ".")).collect(Collectors.toList()));

        for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
            int i = 0, j = 0;
            int indexForTeacher = 0;
            inx = 0;
            indexForDateTemp = indexForDate;
            indexForDateDiscp = indexForDate;
            indexForDateDate = indexForDate;
            indexForDateStudent = indexForDate;
            forde = 0;
            rte = 0;
            for (Row row : workbook.getSheetAt(index)) {
                String nameForContent = null;
                for (Cell cell : row) {
                    if (index == 0) {
                        if ((i == 0 && j == 1) || (i >= 11 && i <= 46 && j >= 0 && j < 2)) {
                            if (i == 0 && j == 1) {
                                cell.setCellValue(map.get(dates.getFirst()).get(0).getGroup().getName());
                            }
                            if (j == 0 && (i >= 11 && i <= 46)) {
                                if (setForStudents.size() > nomer) {
                                    cell.setCellValue(nomer + 1);
                                }
                                if (setForStudents.size() >= nomer) {
                                    nomer++;
                                }

                            }
                            if (j == 1 && (i >= 11 && i <= 46)) {
                                try {
                                    String name = new ArrayList<>(setForStudents).get(ij++);
                                    cell.setCellValue(name);
                                } catch (Exception e) {
                                    cell.setCellValue("");
                                }
                            }
                        }
                    } else if (i == 0 && j == 1) {
                        cell.setCellValue(map.get(dates.getFirst()).get(0).getGroup().getName());
                    } else if (i == 2 && j == 2) {

                    } else if (i == 3 && j >= 2 && j < 92) {
                        if (dates.size() > indexForDate && map.get(dates.get(indexForDate)).size() > indexForTeacher && j % 2 == 0) {
                            TeacherDTO teacher = map.get(dates.get(indexForDate)).get(indexForTeacher++).getTeacher();
                            cell.setCellValue(teacher.getSurname() + " " + teacher.getName().charAt(0) + "." +
                                    (teacher.getPatronymic() == null ? "" : teacher.getPatronymic().charAt(0) + "."));
                        } else {
                            cell.setCellValue("");
                            cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        }
                        if (j % 18 == 0) {
                            indexForDate++;
                            indexForTeacher = 0;
                        }
                    } else if (i == 4 && j >= 2 && j < 92) {
                        cell.setCellValue("");
                        cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    } else if (i == 5 && j >= 2 && j < 92) {
                        if (dates.size() > indexForDateTemp && map.get(dates.get(indexForDateTemp)).size() > indexForTeacher && j % 2 == 0) {
                            TypeClassDTO typeClassDTO = map.get(dates.get(indexForDateTemp)).get(indexForTeacher++).getJournalHeaders().get(0).getTypeClass();
                            switch (typeClassDTO.getName()) {
                                case "Лабораторная работа":
                                    cell.setCellValue("ЛБ");
                                    break;
                                case "Лекция":
                                    cell.setCellValue("ЛК");
                                    break;
                                case "Практическая работа":
                                    cell.setCellValue("ПР");
                                    break;
                                default:
                                    cell.setCellValue("No info");
                            }

                        } else {
                            cell.setCellValue("");
                            cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        }
                        if (j % 18 == 0) {
                            indexForDateTemp++;
                            indexForTeacher = 0;
                        }
                    } else if (i == 6 && j >= 2 && j < 92) {
                        if (dates.size() > indexForDateDiscp && map.get(dates.get(indexForDateDiscp)).size() > indexForTeacher && j % 2 == 0) {
                            DisciplineDTO disciplineDTO = map.get(dates.get(indexForDateDiscp)).get(indexForTeacher++).getDiscipline();
                            List<String> strings = Arrays.stream(disciplineDTO.getName().split(" ")).map(s -> {
                                s = s.toUpperCase();
                                s = Character.toString(s.charAt(0));
                                return s;
                            }).collect(Collectors.toList());
                            StringBuilder disciplineName = new StringBuilder();
                            strings.stream().forEach(disciplineName::append);
                            cell.setCellValue(new String(disciplineName));
                        } else {
                            cell.setCellValue("");
                            cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        }
                        if (j % 18 == 0) {
                            indexForDateDiscp++;
                            indexForTeacher = 0;
                        }
                    } else if (i == 8 && j >= 2 && j < 92) {
                        cell.setCellValue("");
                        cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    } else if (i == 9 && j >= 2 && j < 92) {
                        if (dates.size() > indexForDateDate && j % 2 == 0) {
                            cell.setCellValue(dates.get(indexForDateDate));
                        } else {
                            cell.setCellValue("");
                            cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        }
                        if (j % 18 == 0) {
                            indexForDateDate++;
                        }
                    } else if (i >= 11 && i <= 36 && j >= 0 && j < 92) {

                        if (j == 0 && setForStudents.size() > rte) {
                            cell.setCellValue(rte + 1);
                        }
                        if (j == 1) {
                            try {
                                nameForContent = new ArrayList<>(setForStudents).get(rte);
                                cell.setCellValue(nameForContent);
                            } catch (Exception e) {
                                cell.setCellValue("");
                            }

                        }
                        if (j >= 2) {
                            try {
                                if (dates.size() + 4 >= indexForDate + indexForDateStudent && map.get(dates.get(newpage)).size() > inx && j % 2 == 0 &&
                                        forde++ < map.get(dates.get(newpage)).size()) {
                                    JournalContentDTO journalContent;
                                    try {
                                        Set<JournalContentDTO> set = new TreeSet<>(map.get(dates.get(newpage)).
                                                get(inx).getJournalHeaders().get(0).getJournalContents());
                                        journalContent = new ArrayList<>(set).get(rte);
                                        //System.out.println(dates.get(newpage));
                                        //System.out.println(map.get(dates.get(newpage)).
                                        //        get(inx).getDiscipline().getName());
                                        //System.out.println(journalContent.getStudent().getSurname());
                                        //System.out.println(journalContent.getPresence());
                                        String nameOfCurrentStudent = journalContent.getStudent().getSurname() + " " + journalContent.getStudent().getName().toUpperCase().charAt(0) +
                                                "." + (journalContent.getStudent().getPatronymic() == null ? "" : journalContent.getStudent().getPatronymic().toUpperCase().charAt(0) + ".");
                                        if (!nameOfCurrentStudent.equals(nameForContent)) {
                                            int indexRow = 0;
                                            List<String> strings = new ArrayList<>(setForStudents);
                                            for (int k = 0; k < strings.size(); k++) {
                                                if (strings.get(k).equals(nameOfCurrentStudent)) {
                                                    indexRow = k;
                                                }
                                            }
                                            if (journalContent.getPresence() != null && journalContent.getPresence().equals(false)) {
                                                workbook.getSheetAt(index).getRow(indexRow + 11).getCell(cell.getColumnIndex()).setCellValue(2);
                                            } else {
                                                workbook.getSheetAt(index).getRow(indexRow + 11).getCell(cell.getColumnIndex()).setCellValue("");
                                            }
                                            cell.setCellValue("");
                                        }
                                        else if (journalContent.getPresence() != null && journalContent.getPresence().equals(false)) {
                                            cell.setCellValue(2);
                                        } else {
                                            cell.setCellValue("");
                                        }
                                        inx++;
                                    } catch (Exception e) {
                                        inx++;
                                    }
                                } else {
                                    cell.setCellValue("");
                                }
                            } catch (Exception e) {
                                cell.setCellValue("");
                            }
                        }
                        if (j != 0 && j % 18 == 0) {
                            indexForDateStudent++;
                            forde = 0;
                            inx = 0;
                            newpage++;
                        }
                    }
                    j++;
                }
                if (i >= 11) {
                    rte++;
                }
                i++;
                indexForDateStudent = 0;
                if (index==1) {
                    newpage = 0;
                } else {
                    newpage = indexForDate - 5;
                }
                j = 0;
                inx = 0;
            }
            if (index != 0) {
                indexForDate += 2;
            }
        }
    }

}
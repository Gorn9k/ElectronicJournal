package by.vstu.electronicjournal;

import by.vstu.electronicjournal.dto.*;
import by.vstu.electronicjournal.entity.JournalContent;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.Teacher;
import by.vstu.electronicjournal.service.utils.exсel.excel1;
import by.vstu.electronicjournal.service.utils.exсel.excel2;
import liquibase.pro.packaged.A;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


import by.vstu.electronicjournal.dto.*;
import by.vstu.electronicjournal.entity.JournalContent;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.Teacher;
import by.vstu.electronicjournal.service.utils.exсel.excel1;
import liquibase.pro.packaged.A;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class ElectronicJournalApplication {

    public static ConfigurableApplicationContext cat;

    public static void main(String[] args) {
        cat = SpringApplication.run(ElectronicJournalApplication.class, args);
    }

    public static Workbook getExcel(ConfigurableApplicationContext cat, String groupName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:/GornakA/excel/example.xlsx");
        Workbook wb = new XSSFWorkbook(fileInputStream);
        List<CellReference> referenceList = new ArrayList<>();
        excel1 excel1 = cat.getBean(excel1.class);
        Map<LocalDate, List<JournalSiteDTO>> map = excel1.getInfo(String.format("group.name==%s;dateOfLesson==%sand%s", groupName, "2022-03-21", "2022-04-15")).getMap();
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

        for (int index = 0; index < wb.getNumberOfSheets(); index++) {
            int i = 0, j = 0;
            int indexForTeacher = 0;
            inx = 0;
            indexForDateTemp = indexForDate;
            indexForDateDiscp = indexForDate;
            indexForDateDate = indexForDate;
            indexForDateStudent = indexForDate;
            forde = 0;
            rte = 0;
            for (Row row : wb.getSheetAt(index)) {
                String nameForContent = null;
                for (Cell cell : row) {
                    if (index == 0) {
                        if ((i == 0 && j == 1) || (i >= 11 && i <= 50 && j >= 0 && j < 2)) {
                            if (i == 0 && j == 1) {
                                cell.setCellValue(groupName);
                            }
                            if (j == 0 && (i >= 11 && i <= 50)) {
                                if (setForStudents.size() > nomer) {
                                    cell.setCellValue(nomer + 1);
                                }
                                if (setForStudents.size() >= nomer) {
                                    nomer++;
                                }

                            }
                            if (j == 1 && (i >= 11 && i <= 50)) {
                                try {
                                    String name = new ArrayList<>(setForStudents).get(ij++);
                                    cell.setCellValue(name);
                                } catch (Exception e) {
                                    cell.setCellValue("");
                                }
                            }
                        }
                    } else if (i == 0 && j == 1) {
                        cell.setCellValue(groupName);
                    } else if (i == 2 && j == 2) {

                    } else if (i == 3 && j >= 2 && j < 72) {
                        if (excel1.getDates().size() > indexForDate && map.get(excel1.getDates().get(indexForDate)).size() > indexForTeacher && j % 2 == 0) {
                            TeacherDTO teacher = map.get(excel1.getDates().get(indexForDate)).get(indexForTeacher++).getTeacher();
                            cell.setCellValue(teacher.getSurname() + " " + teacher.getName().charAt(0) + "." +
                                    (teacher.getPatronymic() == null ? "" : teacher.getPatronymic().charAt(0) + "."));
                        } else {
                            cell.setCellValue("");
                            cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        }
                        if (j % 14 == 0) {
                            indexForDate++;
                            indexForTeacher = 0;
                        }
                    } else if (i == 4 && j >= 2 && j < 72) {
                        cell.setCellValue("");
                        cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    } else if (i == 5 && j >= 2 && j < 72) {
                        if (excel1.getDates().size() > indexForDateTemp && map.get(excel1.getDates().get(indexForDateTemp)).size() > indexForTeacher && j % 2 == 0) {
                            TypeClassDTO typeClassDTO = map.get(excel1.getDates().get(indexForDateTemp)).get(indexForTeacher++).getJournalHeaders().get(0).getTypeClass();
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
                        if (j % 14 == 0) {
                            indexForDateTemp++;
                            indexForTeacher = 0;
                        }
                    } else if (i == 6 && j >= 2 && j < 72) {
                        if (excel1.getDates().size() > indexForDateDiscp && map.get(excel1.getDates().get(indexForDateDiscp)).size() > indexForTeacher && j % 2 == 0) {
                            DisciplineDTO disciplineDTO = map.get(excel1.getDates().get(indexForDateDiscp)).get(indexForTeacher++).getDiscipline();
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
                        if (j % 14 == 0) {
                            indexForDateDiscp++;
                            indexForTeacher = 0;
                        }
                    } else if (i == 8 && j >= 2 && j < 72) {
                        cell.setCellValue("");
                        cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    } else if (i == 9 && j >= 2 && j < 72) {
                        if (excel1.getDates().size() > indexForDateDate && j % 2 == 0) {
                            cell.setCellValue(excel1.getDates().get(indexForDateDate));
                        } else {
                            cell.setCellValue("");
                            cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        }
                        if (j % 14 == 0) {
                            indexForDateDate++;
                        }
                    } else if (i >= 11 && i <= 50 && j >= 0 && j < 72) {

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
                                if (excel1.getDates().size() + 4 >= indexForDate + indexForDateStudent && map.get(excel1.getDates().get(newpage)).size() > inx && j % 2 == 0 &&
                                        forde++ < map.get(excel1.getDates().get(newpage)).size()) {
                                    JournalContentDTO journalContent;
                                    try {
                                        Set<JournalContentDTO> set = new TreeSet<>(map.get(excel1.getDates().get(newpage)).
                                                get(inx).getJournalHeaders().get(0).getJournalContents());
                                        journalContent = new ArrayList<>(set).get(rte);
                                        //System.out.println(excel1.getDates().get(newpage));
                                        //System.out.println(map.get(excel1.getDates().get(newpage)).
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
                                            if (journalContent.getPresence() == null || journalContent.getPresence().equals(false)) {
                                                wb.getSheetAt(index).getRow(indexRow + 11).getCell(cell.getColumnIndex()).setCellValue(2);
                                            } else {
                                                wb.getSheetAt(index).getRow(indexRow + 11).getCell(cell.getColumnIndex()).setCellValue("");
                                            }
                                            cell.setCellValue("");
                                        }
                                        else if (journalContent.getPresence() == null || journalContent.getPresence().equals(false)) {
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
                        if (j != 0 && j % 14 == 0) {
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

        fileInputStream.close();
        return wb;
    }

    public static String getCellText(Cell cell) {
        String result = "";

        switch (cell.getCellType()) {
            case STRING:
                result = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    result = cell.getDateCellValue().toString();
                } else {
                    result = Double.toString(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                result = Boolean.toString(cell.getBooleanCellValue());
                break;
            case FORMULA:
                result = cell.getCellFormula();
                break;
            case BLANK:
                break;
            default:
                System.out.println("None type!");
        }
        return result;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
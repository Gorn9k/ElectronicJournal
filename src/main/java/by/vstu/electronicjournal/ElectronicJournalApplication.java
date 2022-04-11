package by.vstu.electronicjournal;

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

    public static void getExcel(ConfigurableApplicationContext cat, String groupName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:/GornakA/excel/example.xlsx");
        Workbook wb = new XSSFWorkbook(fileInputStream);
        FileOutputStream fileOutputStream = new FileOutputStream("new.xlsx");
        List<CellReference> referenceList = new ArrayList<>();
        excel1 excel1 = cat.getBean(excel1.class);
        //List<JournalSiteDTO> journalSites = excel1.getInfo(String.format("group.name==%s;dateOfLesson==%sand%s", "А-33", "2022-03-21", "2022-03-24")).getJournalSites();
        Map<LocalDate, List<JournalSiteDTO>> map = excel1.getInfo(String.format("group.name==%s;dateOfLesson==%sand%s", groupName, "2022-03-21", "2022-04-06")).getMap();

        int indexForDate = 0;
        int indexForDateTemp = 0;
        int indexForDateDiscp = 0;
        int indexForDateDate = 0;
        int indexForDateStudent = 0;
        int rte = 0;
        int inx = 0;
        int forde = 0;
        int forpage = 0;
        int fde = 0;
        int ij = 0;
        int nomer = 0;
        for (int index = 0; index < wb.getNumberOfSheets(); index++) {
            int i = 0, j = 0;
            int indexForTeacher = 0;
            inx = 0;
            indexForDateTemp = indexForDate;
            indexForDateDiscp = indexForDate;
            indexForDateDate = indexForDate;
            indexForDateStudent = indexForDate;
            forde = 0;
            fde = index;
            rte = 0;
            for (Row row : wb.getSheetAt(index)) {
                for (Cell cell : row) {
                    if (index == 0) {
                        if ((i == 0 && j == 1) || (i >= 11 && i <= 36 && j >= 0 && j < 2)) {
                            if (j== 0 && (i >= 11 && i <= 40)) {
                                cell.setCellValue(nomer);
                                if(map.get(excel1.getDates().get(0)).
                                        get(0).getJournalHeaders().get(0).getJournalContents().size() >= nomer) {
                                     nomer++;
                                }

                            }
                            if (j == 1 && (i >= 11 && i <= 40)) {
                                try {
                                    StudentDTO studentDTO = map.get(excel1.getDates().get(0)).
                                            get(0).getJournalHeaders().get(0).getJournalContents().get(ij++).getStudent();
                                    cell.setCellValue(studentDTO.getSurname() + " " + studentDTO.getName().toUpperCase().charAt(0) + ". " +
                                            studentDTO.getPatronymic().toUpperCase().charAt(0) + ".");
                                } catch (Exception e) {
                                    cell.setCellValue("");
                                }
                            }
                        }
                    }
                    /*else if ((i == 2 && j == 2) || (i == 3 && j >= 2 && j < 72) || (i == 4 && j >= 2 && j < 72) ||
                            (i == 5 && j >= 2 && j < 72) || (i == 6 && j >= 2 && j < 72) || (i == 8 && j >= 2 && j < 72) ||
                            (i == 9 && j >= 2 && j < 72) || (i >= 11 && i <= 26 && j >= 0 && j < 72)) {
                        CellReference cellReference = new CellReference(row.getRowNum(), cell.getColumnIndex());
                        referenceList.add(cellReference);
                        cell.setCellValue("");
                        cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    }

                     */


                    else if (i == 0 && j == 1) {
                        cell.setCellValue(groupName);
                    } else if (i == 2 && j == 2) {

                    } else if (i == 3 && j >= 2 && j < 72) {
                        if (excel1.getDates().size() > indexForDate && map.get(excel1.getDates().get(indexForDate)).size() > indexForTeacher && j % 2 == 0) {
                            TeacherDTO teacher = map.get(excel1.getDates().get(indexForDate)).get(indexForTeacher++).getTeacher();
                            cell.setCellValue(teacher.getSurname() + " " + teacher.getName().charAt(0) + "." + teacher.getPatronymic().charAt(0) + ".");
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
                    } else if (i >= 11 && i <= 36 && j >= 0 && j < 72) {
                        if (j == 0) {
                            cell.setCellValue(rte + 1);
                        }
                        if (j == 1) {
                            if (map.get(excel1.getDates().get(indexForDateStudent)).
                                    get(inx).getJournalHeaders().get(0).getJournalContents().size() > 1) {
                                try {
                                    StudentDTO studentDTO = map.get(excel1.getDates().get(indexForDateStudent)).
                                            get(inx).getJournalHeaders().get(0).getJournalContents().get(rte).getStudent();
                                    cell.setCellValue(studentDTO.getSurname() + " " + studentDTO.getName().toUpperCase().charAt(0) + ". " +
                                            studentDTO.getPatronymic().toUpperCase().charAt(0) + ".");
                                } catch (Exception e) {
                                    cell.setCellValue("");
                                }
                            }
                        }
                        if (j >= 2) {
                            if (excel1.getDates().size() > indexForDateStudent && map.get(excel1.getDates().get(indexForDateStudent)).size() > inx && j % 2 == 0 &&
                                    forde++ < map.get(excel1.getDates().get(indexForDateStudent)).size()) {
                                if (map.get(excel1.getDates().get(indexForDateStudent)).
                                        get(inx).getJournalHeaders().get(0).getJournalContents().size() > 1) {
                                    try {
                                        JournalContentDTO journalContent = map.get(excel1.getDates().get(indexForDateStudent)).
                                                get(inx++).getJournalHeaders().get(0).getJournalContents().get(rte);
                                        if (journalContent.getPresence() == null || journalContent.getPresence().equals(false)) {
                                            cell.setCellValue(2);
                                        } else {
                                            cell.setCellValue("");
                                        }
                                    } catch (Exception e) {
                                        cell.setCellValue("");
                                    }
                                }
                            }
                        }
                        if (j != 0 && j % 14 == 0) {
                            indexForDateStudent++;
                            forde = 0;
                            inx = 0;
                            forpage++;
                        }
                    }
                    j++;

                }
                if (i >= 11) {
                    rte++;
                }
                i++;
                j = 0;
                indexForDateStudent = 0;
                inx = 0;
            }
            if (index != 0) {
                indexForDate += 2;
            }
        }
        wb.write(fileOutputStream);
        wb.close();
        fileInputStream.close();
        fileOutputStream.close();
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

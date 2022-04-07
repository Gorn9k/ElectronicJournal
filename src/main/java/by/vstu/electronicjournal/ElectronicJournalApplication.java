package by.vstu.electronicjournal;

import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.TeacherDTO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ElectronicJournalApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ElectronicJournalApplication.class, args);
        /*ConfigurableApplicationContext cat = SpringApplication.run(ElectronicJournalApplication.class, args);
        FileInputStream fileInputStream = new FileInputStream("C:/Users/User/Desktop/11111/example.xlsx");
        Workbook wb = new XSSFWorkbook(fileInputStream);
        FileOutputStream fileOutputStream = new FileOutputStream("C:/Users/User/Desktop/11111/new.xlsx");
        List<CellReference> referenceList = new ArrayList<>();
        excel1 excel1 = cat.getBean(excel1.class);
        //List<JournalSiteDTO> journalSites = excel1.getInfo(String.format("group.name==%s;dateOfLesson==%sand%s", "А-33", "2022-03-21", "2022-03-24")).getJournalSites();
        Map<LocalDate, List<JournalSiteDTO>> map = excel1.getInfo(String.format("group.name==%s;dateOfLesson==%sand%s", "А-33", "2022-03-21", "2022-04-06")).getMap();
        for (int index = 0; index < wb.getNumberOfSheets(); index++) {
            int i = 0, j = 0;
            int indexForDate = 0;
            int indexForTeacher = 0;
            for (Row row : wb.getSheetAt(index)) {
                for (Cell cell : row) {
                    if (index==0) {
                        if ((i == 0 && j == 1) || (i >= 11 && i <= 25 && j >= 0 && j < 2)) {
                            cell.setCellValue("");
                            cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
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


                    else if (i == 0 && j == 1) {
                        cell.setCellValue("А-33");
                    }
                    else if (i == 2 && j == 2) {

                    }
                    else if (i == 3 && j >= 2 && j < 72) {
                        if (excel1.getDates().size() > indexForDate && map.get(excel1.getDates().get(indexForDate)).size() > indexForTeacher && j%2==0) {
                            TeacherDTO teacher = map.get(excel1.getDates().get(indexForDate)).get(indexForTeacher++).getTeacher();
                            cell.setCellValue(teacher.getSurname() + " " + teacher.getName().charAt(0) + "." + teacher.getPatronymic().charAt(0) + ".");
                        }
                        else {
                            cell.setCellValue("");
                            cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        }
                        if (j%14==0) {
                            indexForDate++;
                            indexForTeacher = 0;
                        }
                    }
                    else if (i == 4 && j >= 2 && j < 72) {
                        cell.setCellValue("");
                        cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    }
                    else if (i == 5 && j >= 2 && j < 72) {
                        cell.setCellValue("");
                        cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    }
                    else if (i == 6 && j >= 2 && j < 72) {
                        cell.setCellValue("");
                        cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    }
                    else if (i == 8 && j >= 2 && j < 72) {
                        cell.setCellValue("");
                        cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    }
                    else if (i == 9 && j >= 2 && j < 72) {
                        cell.setCellValue("");
                        cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    }
                    else if (i >= 11 && i <= 26 && j >= 0 && j < 72) {
                        cell.setCellValue("");
                        cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    }
                    j++;
                }
                i++;
                j = 0;
            }
        }
        wb.write(fileOutputStream);
        wb.close();
        fileInputStream.close();
        fileOutputStream.close();
        */
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

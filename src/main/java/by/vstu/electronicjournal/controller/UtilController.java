package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.ElectronicJournalApplication;
import by.vstu.electronicjournal.service.utils.UtilService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
@RequestMapping("utils")
public class UtilController {

    @Autowired
    private UtilService utilService;

    @GetMapping("generate")
    public void generate() {
        utilService.generate();
    }

    @GetMapping("update")
    public void update() {
        utilService.generateJournalHeadersEveryDay();
    }

    @GetMapping("myExcel")
    public void getExcel(HttpServletResponse response, @RequestParam String groupName) throws IOException {

        //тут дальше создаем файл
        ElectronicJournalApplication.getExcel(ElectronicJournalApplication.cat, groupName);
        FileInputStream fileInputStream = new FileInputStream("C:/Users/User/Desktop/projects/actual/ej/new.xlsx");
        response.setHeader("Content-Disposition", "inline;filename=\"" + URLEncoder.encode("new.xlsx", "UTF-8") + "\"");
        response.setContentType("application/xlsx");

        Workbook workbook = new XSSFWorkbook(fileInputStream);
        OutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);

        outputStream.flush();
        outputStream.close();

    }
}


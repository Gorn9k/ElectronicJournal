package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.service.utils.UtilService;
import by.vstu.electronicjournal.service.utils.exсel.ExcelService;
import org.apache.poi.ss.usermodel.Workbook;
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
    @Autowired
    private ExcelService excelService;

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
        response.setHeader("Content-Disposition", "inline;filename=\"" + URLEncoder.encode("Отчет_по_пропускам_за_месяц.xlsx", "UTF-8") + "\"");
        response.setContentType("application/xlsx");

        Workbook workbook = excelService.getPassReport(groupName);
        OutputStream outputStream = response.getOutputStream();

        workbook.getCreationHelper().createFormulaEvaluator().clearAllCachedResultValues();
        workbook.setForceFormulaRecalculation(true);

        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
        outputStream.close();

    }

    @GetMapping("mySecondExcel")
    public void getSecondExcel(HttpServletResponse response, @RequestParam String facultyName) throws IOException {

        //тут дальше создаем файл
        response.setHeader("Content-Disposition", "inline;filename=\"" + URLEncoder.encode("Отчет_по_платным_отработкам_за_месяц.xlsx", "UTF-8") + "\"");
        response.setContentType("application/xlsx");

        Workbook workbook = excelService.getPerformanceReport(facultyName);
        OutputStream outputStream = response.getOutputStream();

        //workbook.getCreationHelper().createFormulaEvaluator().clearAllCachedResultValues();
        //workbook.setForceFormulaRecalculation(true);

        workbook.write(outputStream);
        workbook.close();

        outputStream.flush();
        outputStream.close();

    }
}


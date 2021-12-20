package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.service.utils.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}


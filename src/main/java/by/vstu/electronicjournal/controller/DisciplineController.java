package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.service.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("disciplines")
public class DisciplineController {

    @Autowired
    private DisciplineService disciplineService;

    @GetMapping("search")
    public List<DisciplineDTO> search(@RequestParam("q") String query) {
        return disciplineService.search(query);
    }
}

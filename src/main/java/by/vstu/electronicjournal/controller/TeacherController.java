package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.TeacherDTO;
import by.vstu.electronicjournal.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("search")
    public List<TeacherDTO> search(@RequestParam("q") String query) {
        return teacherService.search(query);
    }

}

package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.StudentDTO;
import by.vstu.electronicjournal.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("search")
    public List<StudentDTO> search(@RequestParam("q") String query) {
        return studentService.search(query);
    }

    @GetMapping("searchByGroup")
    public List<StudentDTO> getStudentsByGroup(@RequestParam("q") String query) {
        return studentService.getStudentsByGroup(query);
    }
}

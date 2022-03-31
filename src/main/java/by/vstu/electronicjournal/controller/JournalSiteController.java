package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.AcademicPerformanceDTO;
import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.StudentPerformanceDTO;
import by.vstu.electronicjournal.service.JournalSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("journal-sites")
public class JournalSiteController {

    @Autowired
    private JournalSiteService journalSiteService;

    @GetMapping("search")
    public List<JournalSiteDTO> search(@RequestParam("q") String query) {
        return journalSiteService.search(query);
    }

    @GetMapping("searchWithoutDublicate")
    public JournalSiteDTO searchWithoutDublicate(@RequestParam("q") String query) {
        return journalSiteService.search(query).get(0);
    }

    @GetMapping("searchByTeacherAndDiscipline")
    public List<JournalSiteDTO> searchByTeacherAndDiscipline(@RequestParam("q") String query) {
        return journalSiteService.searchByTeacherAndDiscipline(query);
    }

    @GetMapping("getAcademicPerformanceByGroupAndDicsipline")
    public List<AcademicPerformanceDTO> getGeneralAcademicPerformance(@RequestParam("q") String query) {
        return journalSiteService.getGeneralAcademicPerformance(query);
    }

    @GetMapping("getAcademicPerformanceByDisciplineAndStudent")
    public AcademicPerformanceDTO getGeneralStudentProgressInDiscipline(@RequestParam("q") String query) {
        return journalSiteService.getGeneralStudentProgressInDiscipline(query);
    }

    @PostMapping
    public JournalSiteDTO create(@RequestBody JournalSiteDTO dto) {
        return journalSiteService.create(dto);
    }

    @GetMapping("{id}")
    public JournalSiteDTO getById(@PathVariable("id") Long id) {
        return journalSiteService.findOne(id);
    }

    @GetMapping("filter")
    public JournalSiteDTO getFilteredByTeacherAndGroupAndDisciplineAndTypeClassAndSubGroup(@RequestParam("teacher_idFromSource") Long teacherIdFromSource,
               @RequestParam("group_name") String groupName, @RequestParam("discipline_id") Long disciplineId, @RequestParam("type_class_id") Long typeClassId,
               @RequestParam("sub_group_number") Integer subGroupNumber) {
        return journalSiteService.getFilteredByTeacherAndGroupAndDisciplineTypeClassAndSubGroup(teacherIdFromSource, groupName, disciplineId, typeClassId, subGroupNumber);
    }

    @PatchMapping("{id}")
    public JournalSiteDTO editById(@PathVariable("id") Long id, @RequestBody JournalSiteDTO dto) {
        return journalSiteService.update(id, dto);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        journalSiteService.deleteById(id);
    }
}

package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.JournalContentDTO;
import by.vstu.electronicjournal.service.JournalContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("journal-contents")
public class JournalContentController {

    @Autowired
    private JournalContentService journalContentService;

    @GetMapping("search")
    public List<JournalContentDTO> search(@RequestParam("q") String query) {
        return journalContentService.search(query);
    }

    @PostMapping
    public JournalContentDTO create(@RequestBody JournalContentDTO contentDTO) {
        return journalContentService.create(contentDTO);
    }

    @GetMapping("{id}")
    public JournalContentDTO getById(@PathVariable("id") Long id) {
        return journalContentService.findOne(id);
    }

    @PatchMapping("{id}")
    public JournalContentDTO editById(@PathVariable("id") Long id, @RequestBody JournalContentDTO contentDTO) {
        return journalContentService.update(id, contentDTO);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        journalContentService.deleteById(id);
    }
}

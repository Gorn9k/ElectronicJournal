package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.AcademicPerformanceDTO;
import by.vstu.electronicjournal.dto.JournalContentDTO;
import by.vstu.electronicjournal.dto.JournalHeaderDTO;
import by.vstu.electronicjournal.dto.requestBodyParams.ParamsForCreateJournalHeader;
import by.vstu.electronicjournal.service.JournalHeaderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("journal-headers")
public class JournalHeaderController {

	@Autowired
	private JournalHeaderService JournalHeaderService;

	@GetMapping("search")
	public List<JournalHeaderDTO> search(@RequestParam("q") String query) {
		return JournalHeaderService.search(query);
	}

	@GetMapping("academicPerformance")
	public AcademicPerformanceDTO getTotalNumberMissedClassesByStudentForPeriod(@RequestParam("q") String query) {
		return JournalHeaderService.getTotalNumberMissedClassesByStudentForPeriod(query);
	}

	@PostMapping
	public JournalHeaderDTO create(@RequestBody ParamsForCreateJournalHeader dto) {
		return JournalHeaderService.create(dto);
	}

	@GetMapping("{id}")
	public JournalHeaderDTO getById(@PathVariable("id") Long id) {
		return JournalHeaderService.findOne(id);
	}

	@PatchMapping("{id}")
	public JournalHeaderDTO editById(@PathVariable("id") Long id,
		@RequestBody JournalHeaderDTO dto) {
		return JournalHeaderService.update(id, dto);
	}

	@PatchMapping("{id}/content")
	public List<JournalContentDTO> editList(@PathVariable("id") Long id,
		@RequestBody List<JournalContentDTO> dtos) {
		return JournalHeaderService.editList(id, dtos);
	}

	@DeleteMapping("{id}")
	public void deleteById(@PathVariable("id") Long id) {
		JournalHeaderService.deleteById(id);
	}
}

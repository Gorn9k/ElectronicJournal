package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.AcademicPerformanceDTO;
import by.vstu.electronicjournal.dto.StudentDTO;
import by.vstu.electronicjournal.entity.Student;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.ValidationFromReliableResources;

import java.util.List;

public interface StudentService
        extends CRUDService<StudentDTO>, RSQLSearch<Student>, ValidationFromReliableResources<StudentDTO> {

    /**
     * Search changes by params
     *
     * @param query params in RSQL format. Used {@link RSQLSearch}
     * @return list of changes
     */
    List<StudentDTO> search(String query);

    List<StudentDTO> getStudentsByGroup(String query);
}

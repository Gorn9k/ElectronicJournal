package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.TeacherDTO;
import by.vstu.electronicjournal.entity.Teacher;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.ValidationFromReliableResources;

import java.util.List;

public interface TeacherService
        extends CRUDService<TeacherDTO>, RSQLSearch<Teacher>, ValidationFromReliableResources<TeacherDTO> {

    /**
     * Search changes by params
     *
     * @param query params in RSQL format. Used {@link RSQLSearch}
     * @return list of changes
     */
    List<TeacherDTO> search(String query);
}

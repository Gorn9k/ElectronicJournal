package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.entity.Discipline;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.ValidationFromReliableResources;

import java.util.List;

public interface DisciplineService
        extends CRUDService<DisciplineDTO>, RSQLSearch<Discipline>, ValidationFromReliableResources<DisciplineDTO> {

    /**
     * Search changes by params
     *
     * @param query params in RSQL format. Used {@link RSQLSearch}
     * @return list of changes
     */
    List<DisciplineDTO> search(String query);

    List<DisciplineDTO> getDisciplinesByGroup(String query);
}

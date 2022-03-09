package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.GroupDTO;
import by.vstu.electronicjournal.entity.Group;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.ValidationFromReliableResources;

import java.util.List;

public interface GroupService
        extends CRUDService<GroupDTO>, RSQLSearch<Group>, ValidationFromReliableResources<GroupDTO> {
    /**
     * Search changes by params
     *
     * @param query params in RSQL format. Used {@link RSQLSearch}
     * @return list of changes
     */
    List<GroupDTO> search(String query);


}

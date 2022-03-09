package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.SubGroupDTO;
import by.vstu.electronicjournal.entity.SubGroup;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.ValidationFromReliableResources;

import java.util.List;

public interface SubGroupService extends CRUDService<SubGroupDTO>, RSQLSearch<SubGroup> {
    List<SubGroupDTO> search(String query);
}

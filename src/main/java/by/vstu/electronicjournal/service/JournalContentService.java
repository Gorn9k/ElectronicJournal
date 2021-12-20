package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.JournalContentDTO;
import by.vstu.electronicjournal.entity.JournalContent;
import by.vstu.electronicjournal.entity.JournalHeader;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;

import java.util.List;

public interface JournalContentService extends CRUDService<JournalContentDTO>, RSQLSearch<JournalContent> {

    /**
     * Search changes by params
     *
     * @param query params in RSQL format. Used {@link RSQLSearch}
     * @return list of changes
     */
    List<JournalContentDTO> search(String query);

    /**
     * Generate journal for employees.
     * This method must be run after generate journalSite.
     */
    void generate(List<JournalSite> params);

    /**
     * Generate content for one header
     * This method update records of headers for
     */
    void generate(JournalHeader header);
}

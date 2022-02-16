package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import java.util.List;

public interface JournalSiteService extends CRUDService<JournalSiteDTO>, RSQLSearch<JournalSite> {

	/**
	 * Search changes by params
	 *
	 * @param query params in RSQL format. Used {@link RSQLSearch}
	 * @return list of changes
	 */
	List<JournalSiteDTO> search(String query);

	/**
	 * Generate journal for employees. This method must be run after create content in timetable
	 * app. Right now, this method start if it was called. In future, it must be run automatically
	 *
	 * @deprecated
	 */
	List<JournalSite> generate(List<JournalSite> sites);
}

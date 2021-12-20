package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.JournalSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JournalSiteRepository extends JpaRepository<JournalSite, Long>, JpaSpecificationExecutor<JournalSite> {
}

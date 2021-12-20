package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.JournalContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JournalContentRepository extends JpaRepository<JournalContent, Long>, JpaSpecificationExecutor<JournalContent> {
}

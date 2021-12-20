package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.JournalHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JournalHeaderRepository extends JpaRepository<JournalHeader, Long>, JpaSpecificationExecutor<JournalHeader> {
}

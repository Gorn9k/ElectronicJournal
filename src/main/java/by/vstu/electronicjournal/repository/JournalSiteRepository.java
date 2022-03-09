package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface JournalSiteRepository extends JpaRepository<JournalSite, Long>, JpaSpecificationExecutor<JournalSite> {
    JournalSite findByTeacherIdAndGroupIdAndDisciplineId(Long teacherId, Long groupId, Long disciplineId);
}

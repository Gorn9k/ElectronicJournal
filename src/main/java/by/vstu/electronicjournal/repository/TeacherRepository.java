package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.Teacher;
import by.vstu.electronicjournal.repository.utils.RelationWithParentIdentifiers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TeacherRepository
        extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher>, RelationWithParentIdentifiers<Teacher> {

    Teacher findByIdFromSource(Long souceId);
}

package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.Group;
import by.vstu.electronicjournal.repository.utils.RelationWithParentIdentifiers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GroupRepository
        extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group>, RelationWithParentIdentifiers<Group> {
}

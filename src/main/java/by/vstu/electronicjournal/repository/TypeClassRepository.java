package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.TypeClass;
import by.vstu.electronicjournal.repository.utils.RelationWithParentIdentifiers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TypeClassRepository
        extends JpaRepository<TypeClass, Long>, JpaSpecificationExecutor<TypeClass>, RelationWithParentIdentifiers<TypeClass> {
}

package by.vstu.electronicjournal.repository.utils;

import by.vstu.electronicjournal.entity.common.AbstractEntity;

public interface RelationWithParentIdentifiers<E extends AbstractEntity> {

    E findByIdFromSource(Long id);
}

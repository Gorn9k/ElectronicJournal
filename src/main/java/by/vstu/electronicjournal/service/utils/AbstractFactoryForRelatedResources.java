package by.vstu.electronicjournal.service.utils;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.entity.common.AbstractEntity;

public interface AbstractFactoryForRelatedResources<E extends AbstractEntity, D extends AbstractDTO> {

    E create(D dto);
}

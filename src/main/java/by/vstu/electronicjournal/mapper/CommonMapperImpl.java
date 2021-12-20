package by.vstu.electronicjournal.mapper;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.entity.common.AbstractEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommonMapperImpl<E extends AbstractEntity, D extends AbstractDTO>
        implements Mapper<E, D> {

    @Override
    public D toDTO(E entity, Class<D> type) {
        return mapper.map(entity, type);
    }

    @Override
    public E toEntity(D dto, Class<E> type) {
        return mapper.map(dto, type);
    }

    @Override
    public List<D> toDTOs(List<E> entities, Class<D> type) {
        return entities.stream().map(e -> mapper.map(e, type)).collect(Collectors.toList());
    }

    @Override
    public List<E> toEntities(List<D> dtos, Class<E> type) {
        return dtos.stream().map(d -> toEntity(d, type)).collect(Collectors.toList());
    }
}

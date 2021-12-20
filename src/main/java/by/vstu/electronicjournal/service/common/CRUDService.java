package by.vstu.electronicjournal.service.common;

import by.vstu.electronicjournal.dto.common.AbstractDTO;

import java.util.List;

/**
 * CRUD interface
 *
 * @param <D> type of class to be returned. Must be extends from {@link AbstractDTO}
 */
public interface CRUDService<D extends AbstractDTO> {

    /**
     * Search one row bu id
     *
     * @param id for search
     * @return one row from table by id
     */
    public D findOne(long id);

    /**
     * Get all records of this entity
     *
     * @return list rows of selected entity
     */
    public List<D> findAll();

    /**
     * Create new row id db
     *
     * @param dto with data will be insert
     * @return dto from db
     */
    public D create(D dto);

    /**
     * For update selected row
     *
     * @param id  for identify selected row
     * @param dto data for insert
     * @return modify row
     * @throws IllegalArgumentException if have no access to field. Insert info about field
     * @throws NoSuchFieldException     if field not found.
     */
    public D update(Long id, D dto);

    /**
     * Delete row by dto
     *
     * @param dto data for identify row for delete
     */
    public void delete(D dto);

    /**
     * Delete row by id
     *
     * @param id for identify row for delete
     */
    public void deleteById(long id);
}

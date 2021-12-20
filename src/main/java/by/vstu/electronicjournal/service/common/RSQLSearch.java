package by.vstu.electronicjournal.service.common;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.service.common.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.jpa.domain.Specification;

/**
 * Interface for using RSQL searching
 *
 * @param <E> Class entity for searching. Must be extends from {@link AbstractEntity}
 */
public interface RSQLSearch<E extends AbstractEntity> {

    /**
     * Create {@link Specification} for search.
     *
     * @param query data for create specifications
     * @return Specification for selected entity
     * @throws IllegalArgumentException if in query was errors
     */
    default Specification<E> getSpecifications(String query) {
        try {
            Node rootNode = new RSQLParser().parse(query);

            return (Specification<E>) rootNode.accept(new CustomRsqlVisitor<E>());

        } catch (Exception e) {
            throw new IllegalArgumentException("There are errors in the search query string");
        }
    }
}

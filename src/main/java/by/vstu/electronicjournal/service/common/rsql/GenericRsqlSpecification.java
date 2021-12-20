package by.vstu.electronicjournal.service.common.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class GenericRsqlSpecification<T> implements Specification<T> {

    private String property;
    private ComparisonOperator operator;
    private List<String> arguments;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Object> args = castArguments(root);
        Object argument = args.get(0);
        switch (RsqlSearchOperation.getSimpleOperator(operator)) {

            case EQUAL: {
                if (argument instanceof String) {
                    return builder.like(getPath(root), argument.toString().replace('*', '%'));
                } else if (argument == null) {
                    return builder.isNull(getPath(root));
                } else {
                    return builder.equal(getPath(root), argument);
                }
            }
            case NOT_EQUAL: {
                if (argument instanceof String) {
                    return builder.notLike(getPath(root), argument.toString().replace('*', '%'));
                } else if (argument == null) {
                    return builder.isNotNull(getPath(root));
                } else {
                    return builder.notEqual(getPath(root), argument);
                }
            }
            case GREATER_THAN: {
                if (getProperty(root).equals(LocalDate.class)){
                    return builder.greaterThan(getPath(root).as(LocalDate.class),(LocalDate) argument);
                }
                return builder.greaterThan(getPath(root), argument.toString());
            }
            case GREATER_THAN_OR_EQUAL: {
                if (getProperty(root).equals(LocalDate.class)){
                    return builder.greaterThanOrEqualTo(getPath(root).as(LocalDate.class),(LocalDate) argument);
                }
                return builder.greaterThanOrEqualTo(getPath(root), argument.toString());
            }
            case LESS_THAN: {
                if (getProperty(root).equals(LocalDate.class)){
                    return builder.lessThan(getPath(root).as(LocalDate.class),(LocalDate) argument);
                }
                return builder.lessThan(getPath(root), argument.toString());
            }
            case LESS_THAN_OR_EQUAL: {
                if (getProperty(root).equals(LocalDate.class)){
                    return builder.lessThanOrEqualTo(getPath(root).as(LocalDate.class),(LocalDate) argument);
                }
                return builder.lessThanOrEqualTo(getPath(root), argument.toString());
            }
            case IN:
                return getPath(root).in(args);
            case NOT_IN:
                return builder.not(getPath(root).in(args));
        }

        return null;
    }

    private Path<String> getPath(final Root<T> root) {
        if (property.contains(".")) {
            Path<String> path = (Path<String>) root;
            String[] tempProperty = property.split("\\.");
            for (String field : tempProperty) {
                path = path.get(field);
            }
            return path;
        }
        return root.get(property);
    }

    private Class<? extends Object> getProperty(final Root<T> root) {
        return getPath(root).getJavaType();
    }

    private List<Object> castArguments(final Root<T> root) {

        Class<? extends Object> type = getProperty(root);

        List<Object> args = arguments.stream().map(arg -> {

            if (type.equals(Integer.class)) return Integer.parseInt(arg);
            if (type.equals(Long.class)) return Long.parseLong(arg);
            if (type.equals(Boolean.class)) return Boolean.getBoolean(arg);
            if (type.equals(LocalDate.class)) return LocalDate.parse(arg);
            return arg;
        }).collect(Collectors.toList());

        return args;
    }

    public GenericRsqlSpecification(String property, ComparisonOperator operator, List<String> arguments) {
        this.property = property;
        this.operator = operator;
        this.arguments = arguments;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    public void setOperator(ComparisonOperator operator) {
        this.operator = operator;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }
}

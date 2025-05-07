package mk.ukim.finki.wp.fcseservices.service.specifications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.ProfessorTitle;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.examschedule.ExamSession;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProject;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectProgramme;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public class FieldFilterSpecification {






    public static <T> Specification<T> filterEquals(Class<T> clazz, String field, StudyCycle value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            } else {
                return criteriaBuilder.equal(fieldToPath(field, root), value);
            }
        };
    }

    public static <T> Specification<T> filterEquals(Class<T> clazz, String field, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(fieldToPath(field, root), value);
    }

    public static <T, V extends Comparable> Specification<T> greaterThen(Class<T> clazz, String field, V value) {
        if (value == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(fieldToPath(field, root), value);
    }

    public static <T, V> Specification<T> filterEqualsV(Class<T> clazz, String field, V value) {
        if (value == null) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(fieldToPath(field, root), value);
    }

    public static <T, V extends Comparable> Specification<T> greaterThan(Class<T> clazz, String field, V value) {
        if (value == null) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(fieldToPath(field, root), value);
    }


    public static <T> Specification<T> filterEquals(Class<T> clazz, String field, Long value) {
        if (value == null) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(fieldToPath(field, root), value);
    }


    public static <T> Specification<T> filterContainsText(Class<T> clazz, String field, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(fieldToPath(field, root)),
                        "%" + value.toLowerCase() + "%"
                );
    }

    private static <T> Path fieldToPath(String field, Root<T> root) {
        String[] parts = field.split("\\.");
        Path res = root;
        for (String p : parts) {
            res = res.get(p);
        }
        return res;
    }

    public static <T> Specification<T> filterContainsTextCaseInsensitive(Class<T> clazz, String field, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(fieldToPath(field, root)),
                        "%" + value.toLowerCase() + "%"
                );
    }

    public static <T> Specification<T> filterFieldNotInList(Class<T> clazz, String field, List<?> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<Object> fieldPath = fieldToPath(field, root);
            return criteriaBuilder.not(fieldPath.in(values));
        };
    }

    public static <T, E extends Enum> Specification<T> filterEnumEquals(Class<T> clazz, String field, E value) {
        if (value == null) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(fieldToPath(field, root), value);
    }

    public static <T> Specification<T> valueInList(String fieldName, String targetValue) {
        if (fieldName == null || targetValue == null || targetValue.isEmpty()){
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<List<Object>> listPath = fieldToPath(fieldName,root);

            // Create a predicate to check if the list contains the target value
            return criteriaBuilder.isMember(targetValue, listPath);
        };
    }

    public static <T, E extends Enum> Specification<T> enumValueInList(String fieldName, E targetValue) {
        if (fieldName == null || targetValue == null){
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<List<Object>> listPath = fieldToPath(fieldName,root);

            // Create a predicate to check if the list contains the target value
            return criteriaBuilder.isMember(targetValue, listPath);
        };
    }

    public static <T> Specification<T> filterExamSession(Class<T> entityClass, String attributeName, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        return (root, query, builder) -> builder.equal(root.get(attributeName), ExamSession.valueOf(value));
    }

}

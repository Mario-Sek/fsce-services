package mk.ukim.finki.wp.fcseservices.service.specifications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.base.ProfessorTitle;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProject;
import mk.ukim.finki.wp.fcseservices.model.projects.ScientificProjectProgramme;
import org.springframework.data.jpa.domain.Specification;


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

    // Метод за филтрирање според grantHolderName со JOIN
    public static Specification<ScientificProject> hasGrantHolderName(String grantHolderName) {
        return (root, query, cb) -> {
            if (grantHolderName == null || grantHolderName.isEmpty()) {
                return cb.conjunction(); // Ако е null, не филтрираме
            }
            Join<ScientificProject, ScientificProjectProgramme> programmeJoin = root.join("programme", JoinType.LEFT);
            return cb.like(cb.lower(programmeJoin.get("grantHolderName")), "%" + grantHolderName.toLowerCase() + "%");
        };
    }

    // Метод за филтрирање според international со JOIN
    public static Specification<ScientificProject> hasInternational(Boolean international) {
        return (root, query, cb) -> {
            if (international == null) {
                return cb.conjunction(); // Ако е null, не филтрираме
            }
            Join<ScientificProject, ScientificProjectProgramme> programmeJoin = root.join("programme", JoinType.LEFT);
            return cb.equal(programmeJoin.get("international"), international);
        };
    }

    public static Specification<ScientificProject> hasProgrammeName(String programmeName) {
        return (root, query, cb) -> {
            if (programmeName == null || programmeName.isEmpty()) {
                return cb.conjunction(); // Ако е null, не филтрираме
            }
            Join<ScientificProject, ScientificProjectProgramme> programmeJoin = root.join("programme", JoinType.LEFT);
            return cb.like(cb.lower(programmeJoin.get("programmeName")), "%" + programmeName.toLowerCase() + "%");
        };
    }

}

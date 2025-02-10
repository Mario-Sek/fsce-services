package mk.ukim.finki.wp.fcseservices.model.base;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Deprecated
public class StudentCourses {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Course course;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentCourses studentCourses = (StudentCourses) o;
        return getId() != null && Objects.equals(getId(), studentCourses.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

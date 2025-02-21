package mk.ukim.finki.wp.fcseservices.model.teachingallocation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Immutable;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "teacher_allocation_stats_view")
@Immutable
@JsonPropertyOrder({"id", "semesterCode", "professorId",
        "numberOfLectureSubjects", "numberOfExerciseSubjects", "numberOfLabSubjects",
        "numberOfLectureGroups", "numberOfExerciseGroups", "numberOfLabGroups",
        "totalLectureStudents", "totalExerciseStudents", "totalLabStudents",
        "totalLectureClasses", "totalExerciseClasses", "totalLabClasses", "totalClasses",
        "numberOfLectureEnGroups", "numberOfExerciseEnGroups", "numberOfLabEnGroups"})
public class TeacherAllocationStats {

    @Id
    private String id;

    @JsonIgnore
    @ManyToOne
    private Professor professor;

    @Column(name = "professor_id", insertable = false, updatable = false)
    private String professorId;

    @JsonIgnore
    @ManyToOne
    private Semester semester;

    @Column(name = "semester_code", insertable = false, updatable = false)
    private String semesterCode;

    private Float numberOfLectureSubjects;
    private Float numberOfExerciseSubjects;
    private Float numberOfLabSubjects;

    private Float numberOfLectureGroups;
    private Float numberOfExerciseGroups;
    private Float numberOfLabGroups;

    private Integer totalLectureStudents;
    private Integer totalExerciseStudents;
    private Integer totalLabStudents;

    private Float totalLectureClasses;
    private Float totalExerciseClasses;
    private Float totalLabClasses;

    private Float totalClasses;

    private Float numberOfLectureEnGroups;
    private Float numberOfExerciseEnGroups;
    private Float numberOfLabEnGroups;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TeacherAllocationStats that = (TeacherAllocationStats) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}

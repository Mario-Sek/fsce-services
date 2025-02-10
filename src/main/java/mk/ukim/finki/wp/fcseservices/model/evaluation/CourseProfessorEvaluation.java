package mk.ukim.finki.wp.fcseservices.model.evaluation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class CourseProfessorEvaluation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Professor professor;

    private short grade;

    @Column(length = 5000)
    private String comment;
}

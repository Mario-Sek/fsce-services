package mk.ukim.finki.wp.fcseservices.model.enrollments;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.base.Subject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class StudentSubjectEnrollment {

    @Id
    private String id;

    @ManyToOne
    private Semester semester;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Subject subject;

    private Boolean valid;

    @Column(length = 4000)
    private String invalidNote;

    private Short numEnrollments;

    private String groupName;

    private Long groupId;

    @ManyToOne
    private JoinedSubject joinedSubject;

    // should be obtained from the course
    @Deprecated
    @ManyToOne
    private Professor professor;

    // should be obtained from the course
    @Deprecated
    private String professors;

    // should be obtained from the course
    @Deprecated
    private String assistants;

    @ManyToOne
    private Course course;

    public StudentSubjectEnrollment(Semester semester, Student student, Subject subject) {
        this.id = String.format("%s-%s-%s", semester.getCode(), student.getIndex(), subject.getId());
        this.semester = semester;
        this.student = student;
        this.subject = subject;
    }

}

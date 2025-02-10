package mk.ukim.finki.wp.fcseservices.model.enrollments;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.base.Student;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class StudentSemesterEnrollment {

    @Id
    private String id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Semester semester;

    private Double paymentAmount;

    private Boolean paymentConfirmed;

    private Boolean valid;

    @Column(length = 4000)
    private String invalidNote;

    public StudentSemesterEnrollment(Semester semester, Student student) {
        this.id = String.format("%s-%s", semester.getCode(), student.getIndex());
        this.semester = semester;
        this.student = student;
    }
}

package mk.ukim.finki.wp.fcseservices.model.equivalence;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.base.StudyProgram;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StudentEquivalenceRequest {

    @Id
    public String id;

    @ManyToOne
    public Student student;

    @ManyToOne
    public StudyProgram oldStudyProgram;

    @ManyToOne
    public StudyProgram newStudyProgram;

    @Enumerated(EnumType.STRING)
    public EquivalenceStatus status = EquivalenceStatus.REQUESTED;

    public StudentEquivalenceRequest(Student student, StudyProgram oldStudyProgram, StudyProgram newStudyProgram) {
        this.student = student;
        this.oldStudyProgram = oldStudyProgram;
        this.newStudyProgram = newStudyProgram;

        this.id = String.format("%s_%s_%s", student.getIndex(), oldStudyProgram.getCode(), newStudyProgram.getCode());
    }
}

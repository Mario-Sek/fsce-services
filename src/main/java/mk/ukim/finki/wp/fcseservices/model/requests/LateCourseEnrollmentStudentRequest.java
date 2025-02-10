package mk.ukim.finki.wp.fcseservices.model.requests;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class LateCourseEnrollmentStudentRequest extends StudentRequest {

    @ManyToOne
    private JoinedSubject joinedSubject;

    @ManyToOne
    private Professor professor;

    private Boolean professorApproved;

    @Override
    public boolean canBeApproved() {
        return professorApproved;
    }

    public boolean canBeApprovedByProfessor() {
        return professorApproved == null || !professorApproved;
    }
}

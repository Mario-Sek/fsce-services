package mk.ukim.finki.wp.fcseservices.model.requests;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class CourseEnrollmentWithoutRequirementsStudentRequest extends StudentRequest {

    @ManyToOne
    private JoinedSubject joinedSubject;

}

package mk.ukim.finki.wp.fcseservices.model.accreditations;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProfessorAcademicTitles {

    @Id
    private String id;

    @ManyToOne
    private Professor professor;

    @ManyToOne
    private AcademicTitle academicTitle;
}

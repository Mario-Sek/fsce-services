package mk.ukim.finki.wp.fcseservices.model.disciplinary;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DisciplinaryRecord {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private DisciplinaryType type;

    private LocalDate reportingDate;

    @Column(length = 10_000)
    private String description;

    @ManyToOne
    private Professor reporter;

    @ManyToOne
    private JoinedSubject joinedSubject;


    private Float severity;

    @Enumerated(EnumType.STRING)
    private DisciplinaryStatus status;

    private Boolean notifiedStudent;

    private LocalDateTime studentLastAccess;

    private Boolean admittedByStudent;

    @Column(length = 10_000)
    private String studentNote;

    private LocalDateTime studentLastNote;

    @ManyToOne
    private DisciplinarySanction suggestedDisciplinarySanction;

    @ManyToOne
    private DisciplinaryMeeting meeting;

    @ManyToOne
    private DisciplinaryDecision decision;

}

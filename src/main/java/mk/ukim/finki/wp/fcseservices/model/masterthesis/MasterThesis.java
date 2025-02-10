package mk.ukim.finki.wp.fcseservices.model.masterthesis;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MasterThesis {

    @Id
    @GeneratedValue
    private Long id;

    private String applicationArchiveNumber;

    private String deadlineExtensionArchiveNumber;

    private String decisionForCommissionElectionArchiveNumber;

    private String masterThesisApprovalArchiveNumber;

    private LocalDate masterThesisDueDate; // one year after the application, and one additional year if there is deadline extension

    @Enumerated(EnumType.STRING)
    private MasterThesisStatus status;

    @ManyToOne
    private Student student;

    private String title;

    private String area;

    @Column(length = 5000)
    private String description;

    // isProfessor == true and not elected
    @ManyToOne
    private Professor mentor;

    // isProfessor == true
    @ManyToOne
    private Professor firstMember;

    // any professor
    @ManyToOne
    private Professor secondMember;

    @Lob
    private byte[] thesisText;

    @Embedded
    private MasterThesisPresentation presentation;

    // 5 - 10
    private Integer grade;

    private LocalDateTime lastUpdate;

    @ManyToOne
    private Professor coordinator;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MasterThesis that = (MasterThesis) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

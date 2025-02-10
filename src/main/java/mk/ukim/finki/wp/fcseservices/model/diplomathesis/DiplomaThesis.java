package mk.ukim.finki.wp.fcseservices.model.diplomathesis;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DiplomaThesis {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private DiplomaThesisStatus status;

    @ManyToOne
    private Student student;

    private String title;

    private String area;

    @Lob
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
    private DiplomaThesisPresentation presentation;

    // 5 - 10
    private Integer grade;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DiplomaThesis that = (DiplomaThesis) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

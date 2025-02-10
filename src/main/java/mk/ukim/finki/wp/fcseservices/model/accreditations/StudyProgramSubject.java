package mk.ukim.finki.wp.fcseservices.model.accreditations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.StudyProgram;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StudyProgramSubject {

    @Id
    private String id;

    @ManyToOne
    private SubjectDetails subject;

    @ManyToOne
    private StudyProgram studyProgram;

    private Boolean mandatory;

    private Short semester;

    @Column(name = "\"order\"")
    private Float order;

    private String subjectGroup;

    @Column(length = 5000)
    private String dependenciesOverride;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudyProgramSubject professor = (StudyProgramSubject) o;
        return getId() != null && Objects.equals(getId(), professor.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}

package mk.ukim.finki.wp.fcseservices.model.teachingallocation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.Subject;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SubjectNameMapping {
    @Id
    private Long id;

    @ManyToOne
    private Subject subject;

    private String oldName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SubjectNameMapping subjectNameMapping = (SubjectNameMapping) o;
        return getId() != null && Objects.equals(getId(), subjectNameMapping.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}

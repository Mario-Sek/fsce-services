package mk.ukim.finki.wp.fcseservices.model.diplomathesis;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.User;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DiplomaThesisStatusChange {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private DiplomaThesis diplomaThesis;

    private LocalDate statusChangeDate;

    @Enumerated(EnumType.STRING)
    private DiplomaThesisStatus nextStatus;

    @ManyToOne
    private User statusChangedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DiplomaThesisStatusChange that = (DiplomaThesisStatusChange) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

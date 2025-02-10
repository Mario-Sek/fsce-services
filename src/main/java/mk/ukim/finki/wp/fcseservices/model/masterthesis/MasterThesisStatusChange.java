package mk.ukim.finki.wp.fcseservices.model.masterthesis;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.User;
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
public class MasterThesisStatusChange {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private MasterThesis thesis;

    private LocalDateTime statusChangeDate;

    @Enumerated(EnumType.STRING)
    private MasterThesisStatus nextStatus;

    @ManyToOne
    private User statusChangedBy;

    private Boolean approved;

    @Column(length = 5_000)
    private String note;

    private LocalDate nextStatusProcessingDueDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MasterThesisStatusChange that = (MasterThesisStatusChange) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

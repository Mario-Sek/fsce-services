package mk.ukim.finki.wp.fcseservices.model.results;


import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Results {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "results_sequence")
    @SequenceGenerator(name = "results_sequence", sequenceName = "results_sequence", allocationSize = 1)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne
    private YearExamSession session;

    @ManyToOne
    private JoinedSubject joinedSubject;

    private byte[] pdfBytes;

    private String note;

    private LocalDateTime uploadedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Results results = (Results) o;
        return getId() != null && Objects.equals(getId(), results.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

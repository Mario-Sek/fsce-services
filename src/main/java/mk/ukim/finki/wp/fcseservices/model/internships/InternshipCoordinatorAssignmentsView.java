package mk.ukim.finki.wp.fcseservices.model.internships;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "internship_coordinator_assignments_view")
@Immutable
@Getter
@Setter
@NoArgsConstructor
public class InternshipCoordinatorAssignmentsView {
    @Id
    private String professorId;

    private int assignmentsCount;
}

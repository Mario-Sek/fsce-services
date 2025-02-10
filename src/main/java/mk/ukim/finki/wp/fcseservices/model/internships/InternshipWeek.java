package mk.ukim.finki.wp.fcseservices.model.internships;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class InternshipWeek {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name="internship_id")
    private Internship internship;

    private String description;

    private String companyComment;

    private String coordinatorComment;

    private Integer workingHours;

    public InternshipWeek(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public InternshipWeek(LocalDate startDate, LocalDate endDate, Internship internship, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.internship = internship;
        this.description = description;
    }
}

package mk.ukim.finki.wp.fcseservices.model.internships;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.memorandum.Company;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Internship {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_index")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor coordinator;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Enumerated(EnumType.STRING)
    private InternshipStatus status;

    @OneToMany(mappedBy = "internship",cascade = CascadeType.ALL)
    private List<InternshipWeek> journal;

    private String description;
    
    private LocalDate startDate;

    private LocalDate endDate;

    private Integer weeklyHours;

    private boolean foreignCompany;

    private String companyToken;

    private ZonedDateTime companyTokenExpiration;

    public Internship(Student student) {
        this.student = student;
    }
}

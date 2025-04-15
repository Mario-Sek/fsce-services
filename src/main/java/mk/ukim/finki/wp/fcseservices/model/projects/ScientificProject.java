package mk.ukim.finki.wp.fcseservices.model.projects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ScientificProject {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ScientificProjectStatus status;

    private String name;

    private String keywords;

    @Column(length = 10_000)
    private String goalsDescription;

    @Column(length = 5_000)
    private String relatedPublicationsOrProjects;


    @Column(length = 10_000)
    private String report;

    @Column(length = 10_000)
    private String expectedResults;

    @ManyToOne
    private Professor coordinator;

    @ManyToOne
    private ScientificProjectCall projectCall;

    @ManyToOne
    private ScientificProjectProgramme programme;


    public ScientificProject(Long id, ScientificProjectStatus status, String name, String keywords, String goalsDescription, String relatedPublicationsOrProjects, String report, String expectedResults, Professor coordinator, ScientificProjectCall projectCall, ScientificProjectProgramme programme) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.keywords = keywords;
        this.goalsDescription = goalsDescription;
        this.relatedPublicationsOrProjects = relatedPublicationsOrProjects;
        this.report = report;
        this.expectedResults = expectedResults;
        this.coordinator = coordinator;
        this.projectCall = projectCall;
        this.programme = programme;
    }
}

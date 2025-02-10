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


}

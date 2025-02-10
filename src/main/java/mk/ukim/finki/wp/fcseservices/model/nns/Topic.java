package mk.ukim.finki.wp.fcseservices.model.nns;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Student;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Topic {

    @Id
    @GeneratedValue
    private Long id;

    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private TopicCategory categoryName;

    private String subCategoryName; // type

    @Column(length = 6000)
    private String description;

    private Boolean isAccepted;

    private Integer acceptNumber;
    private Integer againstNumber;
    private Integer sustainedNumber;

    private String discussion;

    @ManyToOne
    private TeachingAndScientificMeeting meeting;

    @ManyToOne
    private Student mentionedStudent;

    @ManyToOne
    private Professor professor;

    @ManyToMany
    @ToString.Exclude
    private List<Professor> mentionedProfessors;



}

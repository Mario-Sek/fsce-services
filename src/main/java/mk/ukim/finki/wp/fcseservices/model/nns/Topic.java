package mk.ukim.finki.wp.fcseservices.model.nns;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private TopicCategory category;

    @Enumerated(EnumType.STRING)
    private TopicStatus status;

    private String subCategoryName;

    @Column(length = 6000)
    private String description;

    private Boolean isAccepted;

    private Integer acceptNumber;
    private Integer againstNumber;
    private Integer sustainedNumber;

    private String discussion;
    @Column(name = "file_url")
    private String fileURL;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TopicComment> comments;

    @ManyToOne
    @JsonBackReference
    private Professor requestedByProfessor;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="meeting_id")
    private TeachingAndScientificMeeting meeting;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="mentioned_student_id")
    private Student mentionedStudent;

    @ManyToOne
    @JsonBackReference
    private Professor mentionedProfessor;

    @ManyToMany
    @ToString.Exclude
    @JsonBackReference
    private List<CommissionMember> commissionMembers;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private TopicFile file;

    private Boolean isVotable;

}

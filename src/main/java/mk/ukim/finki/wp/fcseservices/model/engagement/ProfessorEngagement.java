package mk.ukim.finki.wp.fcseservices.model.engagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.*;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonPropertyOrder({"id", "professorName", "subjectName", "semesterCode", "classType", "language",
        "sharedWithOtherTeacher", "numberOfClasses", "numberOfStudents", "consultative", "note"})
public class ProfessorEngagement extends TrackedHistoryEntity {

    @Id
    private String id;

    @JsonIgnore
    @ManyToOne
    private Professor professor;

    @JsonIgnore
    @ManyToOne
    private Semester semester;

    @JsonIgnore
    @ManyToOne
    private JoinedSubject subject;

    @Enumerated(EnumType.STRING)
    private ClassType classType;

    private Float numberOfClasses;

    private Boolean sharedWithOtherTeacher;

    @Enumerated(EnumType.STRING)
    private Language language;

    private Short numberOfStudents;

    private Boolean consultative;

    @Column(length = 2000)
    private String note;

    @Column(length = 4000)
    private String validationMessage;

    private Float calculatedNumberOfClasses;
    private Short calculatedNumberOfStudents;


    public ProfessorEngagement(Professor professor,
                               Semester semester,
                               JoinedSubject subject,
                               ClassType classType,
                               Language language,
                               Float numberOfClasses,
                               Boolean otherTeacher,
                               Short numberOfStudents,
                               Boolean consultative,
                               String note) {
        this.id = generateId(professor.getId(), semester.getCode(),
                subject.getAbbreviation(), classType, language);
        this.professor = professor;
        this.semester = semester;
        this.subject = subject;
        this.classType = classType;
        this.numberOfClasses = numberOfClasses;
        this.sharedWithOtherTeacher = otherTeacher;
        this.language = language;
        this.numberOfStudents = numberOfStudents;
        this.consultative = consultative;
        this.note = note;
    }

    public ProfessorEngagement(Professor professor,
                               Semester semester,
                               JoinedSubject subject,
                               ClassType classType,
                               Language language,
                               Float numberOfClasses,
                               Boolean otherTeacher,
                               Short numberOfStudents,
                               Boolean consultative,
                               String note,
                               Float calculatedNumberOfClasses,
                               Short calculatedNumberOfStudents) {
        this.id = generateId(professor.getId(), semester.getCode(),
                subject.getAbbreviation(), classType, language);
        this.professor = professor;
        this.semester = semester;
        this.subject = subject;
        this.classType = classType;
        this.numberOfClasses = numberOfClasses;
        this.sharedWithOtherTeacher = otherTeacher;
        this.language = language;
        this.numberOfStudents = numberOfStudents;
        this.consultative = consultative;
        this.calculatedNumberOfClasses = calculatedNumberOfClasses;
        this.calculatedNumberOfStudents = calculatedNumberOfStudents;
        this.note = note;
    }

    public static String generateId(String prof, String semester, String subject, ClassType type, Language lang) {
        return String.format("%s-%s-%s-%s-%s", prof, semester, subject, type.name(), lang.name());
    }

    @Transient
    @JsonProperty("professorName")
    public String getProfessorName() {
        return professor.getName();
    }

    @Transient
    @JsonProperty("subjectName")
    public String getSubjectName() {
        return subject.displayName();
    }


    @Transient
    @JsonProperty("semesterCode")
    public String getSemesterCode() {
        return semester.getCode();
    }


}

package mk.ukim.finki.wp.fcseservices.model.teachingallocation;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.accreditations.StudyCycle;
import mk.ukim.finki.wp.fcseservices.model.base.SemesterType;
import mk.ukim.finki.wp.fcseservices.model.base.Subject;
import mk.ukim.finki.wp.fcseservices.model.base.TrackedHistoryEntity;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JoinedSubject extends TrackedHistoryEntity {

    @Id
    private String abbreviation;

    @Column(length = 1000)
    private String name;

    private String codes;

    @Enumerated(EnumType.STRING)
    private SemesterType semesterType;

    @ManyToOne
    private Subject mainSubject;

    private String mandatoryForStudyPrograms;

    private Integer weeklyLecturesClasses;

    private Integer weeklyAuditoriumClasses;

    private Integer weeklyLabClasses;

    @Enumerated(EnumType.STRING)
    private StudyCycle cycle;

    @Column(length = 4_000)
    private String validationMessage;

    public JoinedSubject(String id, String name, String codes, SemesterType semesterType,
                         Subject mainSubject, Integer weeklyLecturesClasses, Integer weeklyAuditoriumClasses,
                         Integer weeklyLabClasses, StudyCycle cycle, String validationMessage) {
        this.abbreviation = id;
        this.name = name;
        this.codes = codes;
        this.semesterType = semesterType;
        this.mainSubject = mainSubject;
        this.weeklyLecturesClasses = weeklyLecturesClasses;
        this.weeklyAuditoriumClasses = weeklyAuditoriumClasses;
        this.weeklyLabClasses = weeklyLabClasses;
        this.cycle = cycle;
        this.validationMessage = validationMessage;
    }

    @Transient
    public String displayName() {
        return String.format("%s [%s] (%d+%d+%d)", name, abbreviation, weeklyLecturesClasses, weeklyAuditoriumClasses, weeklyLabClasses);
    }

    @Transient
    public List<String> codesList() {
        return codes != null ? List.of(codes.split(";")).stream().map(String::trim).toList() : List.of();
    }
}

package mk.ukim.finki.wp.fcseservices.model.teachingallocation;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.TrackedHistoryEntity;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CoursePreference extends TrackedHistoryEntity {

    @Id
    @Column(name = "subject_id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "subject_id",insertable = false,updatable = false)
    private JoinedSubject subject;

    @Enumerated(EnumType.STRING)
    private LectureSharing lectureSharing;

    @Enumerated(EnumType.STRING)
    private LectureSharing auditoriumExercisesSharing;

    private boolean preferOnlineLectures;

    private boolean preferOnlineExercises;

    private boolean labExercisesAsConsultations;


    public CoursePreference(JoinedSubject subject) {
        this.subject = subject;
        this.id = subject.getAbbreviation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CoursePreference that = (CoursePreference) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

package mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Room;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Semester semester;

    @ManyToOne
    private JoinedSubject joinedSubject;

    // iKnow professor
    @ManyToOne
    private Professor professor;

    @Deprecated
    @ManyToOne
    private Professor assistant;

    // professors that jointly teach the course
    private String professors;

    // assistants that jointly teach the course
    private String assistants;

    // first time enrolled student groups included in the course
    @ManyToMany
    private List<StudentGroup> studentGroups;

    // adequate rooms for the course
    @ManyToMany
    private List<Room> rooms;

    private Integer numberOfFirstEnrollments;

    private Integer numberOfReEnrollments;

    private Float groupPortion = 1.0F;

    private String groups;

    private Boolean english;

    public Integer getTotalStudents() {
        return Optional.ofNullable(numberOfFirstEnrollments).orElse(0) +
                Optional.ofNullable(numberOfReEnrollments).orElse(0);
    }
}

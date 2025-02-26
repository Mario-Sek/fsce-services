package mk.ukim.finki.wp.fcseservices.model.attendance;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.Room;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ScheduledClassSession {

    @Id
    private Long id;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Room room;

    @ManyToOne
    private Semester semester;

    @Enumerated(EnumType.STRING)
    private ClassType type;

    LocalTime startTime;

    LocalTime endTime;

    DayOfWeek dayOfWeek;

}

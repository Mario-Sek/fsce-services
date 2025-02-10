package mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "course_size")
@Immutable
public class CourseSize {

    @Id
    private String id;

    private String semesterCode;

    private Long groupId;

    private String groupName;

    private String joinedSubjectAbbreviation;

    private Integer defaultSize;

    private Long firstTimeEnrollments;

    private Long reEnrollments;

    private Boolean english;
}

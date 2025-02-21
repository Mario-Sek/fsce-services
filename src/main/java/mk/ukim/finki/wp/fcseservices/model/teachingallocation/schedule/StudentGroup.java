package mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mk.ukim.finki.wp.fcseservices.model.base.Semester;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@JsonPropertyOrder({"semesterCode", "programs", "name", "studyYear", "lastNameRegex"})
public class StudentGroup {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "semester_code", insertable = false, updatable = false)
    private String semesterCode;

    private String programs;

    private String name;

    private Short studyYear;

    private String lastNameRegex;

    @JsonIgnore
    @ManyToOne
    private Semester semester;

    private Boolean english = false;
}

package mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class StudentGroup {

    @Id
    @GeneratedValue
    private Long id;

    private String programs;

    private String name;

    private Short studyYear;

    private String lastNameRegex;

    private Integer defaultSize;

    @ManyToOne
    private Semester semester;

    private Boolean english = false;

}

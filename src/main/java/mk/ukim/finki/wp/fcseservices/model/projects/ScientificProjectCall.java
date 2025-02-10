package mk.ukim.finki.wp.fcseservices.model.projects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ScientificProjectCall {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime applicationDeadline;

    @ManyToOne
    ScientificProjectProgramme programme;


    @Enumerated(EnumType.STRING)
    private ScientificCallStatus status;

}

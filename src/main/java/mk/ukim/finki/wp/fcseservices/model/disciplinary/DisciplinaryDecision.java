package mk.ukim.finki.wp.fcseservices.model.disciplinary;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DisciplinaryDecision {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private DisciplinaryRecord record;

    @ManyToOne
    private DisciplinarySanction sanction;

    @Column(length = 3000)
    private String description;
}

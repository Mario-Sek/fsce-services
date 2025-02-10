package mk.ukim.finki.wp.fcseservices.model.disciplinary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DisciplinarySanction {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(length = 3000)
    private String description;
}

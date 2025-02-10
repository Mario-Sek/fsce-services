package mk.ukim.finki.wp.fcseservices.model.disciplinary;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DisciplinaryType {

    @Id
    private String id;

    private String name;

}

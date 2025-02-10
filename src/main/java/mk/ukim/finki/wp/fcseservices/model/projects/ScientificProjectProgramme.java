package mk.ukim.finki.wp.fcseservices.model.projects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ScientificProjectProgramme {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private GrantHolder grantHolder;

    private Boolean international;
}

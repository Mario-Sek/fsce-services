package mk.ukim.finki.wp.fcseservices.model.equivalence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EquivalentSubjectMapping {

    @Id
    private String targetSubjectCode;

    // the list of codes of the subjects that are equivalent to the target subject, separated by ;
    // when two or more subjects are equivalent to the target subject, they are combined with +;
    // example: "S4;S1+S2;S1+S3;S2+S3;S5" means that the target subject will be considered equivalent to any of the following passed subjects:
    // S4, S1 and S2, S1 and S3, S2 and S3, S5
    private String equivalentSubjectCodes;
}
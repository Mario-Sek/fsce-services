package mk.ukim.finki.wp.fcseservices.model.accreditations;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectObligationDuration {

    private String totalHours;
    private String lectureHours;
    private String exerciseHours;
    private String selfLearningHours;
    private String projectHours;
    private String homeworkHours;
}

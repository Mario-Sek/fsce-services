package mk.ukim.finki.wp.fcseservices.model.disciplinary;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DisciplinaryMeeting {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate disciplinaryMeetingDate;


}

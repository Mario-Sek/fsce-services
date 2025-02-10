package mk.ukim.finki.wp.fcseservices.model.attendance;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Holiday {

    @Id
    private LocalDate date;

    private String name;
}

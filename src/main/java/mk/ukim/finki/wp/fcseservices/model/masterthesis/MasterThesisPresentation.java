package mk.ukim.finki.wp.fcseservices.model.masterthesis;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.wp.fcseservices.model.base.Room;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterThesisPresentation {

    @ManyToOne
    @JoinColumn(name = "location")
    private Room location;

    private LocalDateTime presentationStartTime;
}

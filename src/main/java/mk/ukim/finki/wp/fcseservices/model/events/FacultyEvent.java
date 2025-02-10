package mk.ukim.finki.wp.fcseservices.model.events;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.wp.fcseservices.model.base.EntityReference;
import mk.ukim.finki.wp.fcseservices.model.base.Room;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "events_view")
@Immutable
public class FacultyEvent {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private FacultyEventType type;

    private String name;

    @Column(length = 10_000)
    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne
    private Room physicalLocation;

    private String onlineUrl;

    @Embedded
    private EntityReference reference;


}

package mk.ukim.finki.wp.fcseservices.model.irregularities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Irregularity {
    @Id
    @Column(length = 8)
    private String id;

    private String title;

    private String description;

    private LocalDateTime date;

    private boolean locked;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JsonBackReference
    private Professor professor;

    @ManyToOne
    @JsonBackReference
    private Room room;

    @ManyToOne
    @JsonBackReference
    private Subject subject;

    @ManyToOne
    @JsonBackReference
    private User administrativeStaff;

    private String generalIrregularity;

    @Enumerated(EnumType.STRING)
    private IrregularityType type;

    @Enumerated(EnumType.STRING)
    private IrregularityStatus status;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "irregularity", cascade = CascadeType.ALL)
    private List<IrregularityComment> staffComment;

    private String markedBy;
    private LocalDateTime markedOn;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        this.locked = false;
        this.date = LocalDateTime.now();
    }

}

package mk.ukim.finki.wp.fcseservices.model.irregularities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@NoArgsConstructor
@Data
public class IrregularityComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private Date dateAnswered;

    @ManyToOne
    private User staffUser;

    @ManyToOne
    @JoinColumn(name = "irregularity_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Irregularity irregularity;

    @Override
    public String toString() {
        return comment;
    }
}

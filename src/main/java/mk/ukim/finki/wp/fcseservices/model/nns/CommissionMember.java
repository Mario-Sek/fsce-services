package mk.ukim.finki.wp.fcseservices.model.nns;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommissionMember
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Professor professor;

    @Enumerated(EnumType.STRING)
    private CommissionMemberType type;

    @ManyToMany(mappedBy = "commissionMembers")
    @JsonManagedReference
    private List<Topic> topics;

    public CommissionMember(Professor professor, CommissionMemberType type)
    {
        this.professor = professor;
        this.type = type;
    }
}

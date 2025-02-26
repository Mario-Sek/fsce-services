package mk.ukim.finki.wp.fcseservices.model.nns;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.wp.fcseservices.model.base.Room;
import mk.ukim.finki.wp.fcseservices.model.base.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TeachingAndScientificMeeting {

    @Id
    private String meetingNumber;

    private LocalDateTime date;

    private Integer presentMembers;

    private Integer eligibleMembers;

    @ManyToOne
    private Room room;

    @ManyToOne
    private User chairMan;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Topic> topicsList;
}

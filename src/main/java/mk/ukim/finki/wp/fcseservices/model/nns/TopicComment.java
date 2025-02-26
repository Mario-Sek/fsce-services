package mk.ukim.finki.wp.fcseservices.model.nns;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TopicComment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000)
    private String commentText;

    @ManyToOne
    @JsonBackReference
    private Topic topic;

    private LocalDateTime datePosted;

    @ManyToOne
    @JsonBackReference
    private Professor professor;

    public TopicComment(String commentText, Topic topic, LocalDateTime datePosted, Professor professor)
    {
        this.commentText = commentText;
        this.topic = topic;
        this.datePosted = datePosted;
        this.professor = professor;
    }
}

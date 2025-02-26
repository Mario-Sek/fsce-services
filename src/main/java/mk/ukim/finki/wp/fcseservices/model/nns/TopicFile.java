package mk.ukim.finki.wp.fcseservices.model.nns;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TopicFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private byte[] content;

    public TopicFile(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }
}

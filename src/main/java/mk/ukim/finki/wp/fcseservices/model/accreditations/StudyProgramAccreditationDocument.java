package mk.ukim.finki.wp.fcseservices.model.accreditations;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StudyProgramAccreditationDocument {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AccreditationDocumentTypes type;

    @ManyToOne
    private StudyProgramDetails studyProgram;

    @Lob
    private byte[] document;

    private LocalDateTime creationDate = LocalDateTime.now();
}
